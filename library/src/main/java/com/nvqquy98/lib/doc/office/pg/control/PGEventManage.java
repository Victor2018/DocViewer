/*
 * 文件名称:          PGEventManage.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:00:11
 */
package com.nvqquy98.lib.doc.office.pg.control;

import com.nvqquy98.lib.doc.office.common.ISlideShow;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.beans.AEventManage;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * PG触摸、手型事件管理
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-15
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PGEventManage extends AEventManage
{   
    /**
     * 
     * @param spreadsheet
     */
    public PGEventManage(Presentation presentation, IControl control)
    {
        super(presentation.getContext(), control);
        this.presentation = presentation;
        presentation.setOnTouchListener(this);
        presentation.setLongClickable(true);
    }     
    
    /**
     * 触摸事件
     *
     */
    public boolean onTouch(View v, MotionEvent event)
    {
        super.onTouch(v, event);                
        return false;
    }
    
    /**
     * 
     *
     */
    public boolean onDoubleTap(MotionEvent e)
    {
        super.onDoubleTap(e);
        return true;
    }

    
    
    /**
     * 
     *
     */
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        super.onScroll(e1, e2, distanceX, distanceY); 
        return true;
    }
    
    /**
     * Fling the scroll view
     *
     * @param velocityX  X方向速率
     * @param velocityY  Y方向速率 
     */
    public void fling(int velocityX, int velocityY)
    {
        if(presentation.isSlideShow())
        {          
            if (Math.abs(velocityY) < 400 &&   Math.abs(velocityX) < 400)
            {
                presentation.slideShow(ISlideShow.SlideShow_NextStep);
                return;
            }
            
            super.fling(velocityX, velocityY);
            int currentIndex = presentation.getCurrentIndex();
            
            if (Math.abs(velocityY) > Math.abs(velocityX))
            {
                //vertical
                if (velocityY < 0 && currentIndex >= 0)
                {
                  //previous step
                    presentation.slideShow(ISlideShow.SlideShow_NextStep);                    
                }
                else if (velocityY > 0 && currentIndex <= presentation.getRealSlideCount() - 1)
                {
                    //next step
                    presentation.slideShow(ISlideShow.SlideShow_PreviousStep);
                }
            }            
            else
            {
                // horizontal
                if (velocityX < 0 && currentIndex >= 0)
                {
                    // previous Slide 
                    presentation.slideShow(ISlideShow.SlideShow_PreviousSlide);
                }                
                else if (velocityX > 0 && currentIndex < presentation.getRealSlideCount() - 1)
                {
                    // next Slide
                    presentation.slideShow(ISlideShow.SlideShow_NextSlide);
                }
            }
        }
        
    }
    
    /**
     * 
     *
     */
    public boolean onSingleTapUp(MotionEvent e)
    {
        super.onSingleTapUp(e);
        if (e.getAction() == MotionEvent.ACTION_UP)
        {
            Rect drawRect = presentation.getSlideDrawingRect();
            if(presentation.isSlideShow() && drawRect.contains((int)e.getX(), (int)e.getY()))
            {               
                //not click hyperlink, then go to next step
                this.presentation.slideShow(ISlideShow.SlideShow_NextStep);
            }
        }
        return true;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        presentation = null;
    }
    // Spreadsheet
    private Presentation presentation;
}
