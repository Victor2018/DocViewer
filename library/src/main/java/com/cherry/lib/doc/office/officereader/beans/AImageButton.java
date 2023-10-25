/*
 * 文件名称:          AImageButton.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:52:27
 */
package com.cherry.lib.doc.office.officereader.beans;


import com.cherry.lib.doc.office.common.PaintKit;
import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.office.system.IControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 自定义ImageButton
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-7
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AImageButton extends View implements OnGestureListener, OnClickListener
{
    /**
     * 
     * @param context           Activity实例
     * @param toolstip          tool tip文本
     * @param iconResID         显示的图标ID
     * @param iconResIdDisable  button不可用时显示图片，如查不需要处理请传入-1
     * @param actionID          button的ActionID
     */
    public AImageButton(Context context, IControl control, String toolstip, 
        int iconResID, int iconResIdDisable, int actionID)
    {
        super(context);
        this.control = control;
        this.toolstip = toolstip;
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inDensity = context.getResources().getDisplayMetrics().densityDpi;  
        opts.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), iconResID, opts);
        if (iconResIdDisable != -1)
        {
            this.bitmapDisable = BitmapFactory.decodeResource(getContext().getResources(), iconResIdDisable, opts);
        }
        this.actionID = actionID;
        
        gesture = new GestureDetector(context, this);
        setFocusable(true);
        setClickable(true);
        setLongClickable(true);
        this.setOnClickListener(this);
    }

    /**
     * 
     */
    protected void onFocusChanged(boolean gainFocus,
        int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        int id = gainFocus ? focusBgResID : normalBgResID;
        if (id != -1) {
            setBackgroundResource(id);
        }
    }
    
    /**
     * 单击事件
     */
    public void onClick(View v)
    {
        if(!longPressed && v instanceof AImageButton)
        {
            control.actionEvent(((AImageButton)v).getActionID(), null);            
        }        
        longPressed = false;
    }
    
    
    /**
     * 
     *
     */
    public boolean onTouchEvent(MotionEvent event)
    {
        gesture.onTouchEvent(event);
        int action = event.getAction();
        if (!isEnabled())
        {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                control.actionEvent(EventConstant.SYS_CLOSE_TOOLTIP, null);
            }
            return true;
        }
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (pushBgResID != -1)
                {
                    setBackgroundResource(pushBgResID);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (normalBgResID == -1)
                {
                    setBackgroundDrawable(null);
                }
                else
                {
                    setBackgroundResource(normalBgResID);
                }
                control.actionEvent(EventConstant.SYS_CLOSE_TOOLTIP, null);
                break;
        }
        return super.onTouchEvent(event);
    }
    
    /**
     * 
     *
     */
    public void onDraw(Canvas canvas)
    {
        if (bitmap == null)
        {
            return ;
        }
        if (isEnabled())
        {
            canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, (getHeight() - bitmap.getHeight()) / 2, PaintKit.instance().getPaint());
        }
        else if (bitmapDisable != null)
        {
            canvas.drawBitmap(bitmapDisable, (getWidth() - bitmapDisable.getWidth()) / 2, (getHeight() - bitmapDisable.getHeight()) / 2, PaintKit.instance().getPaint());
        }
    }
    /**
     * 
     */
    public boolean onDown(MotionEvent e)
    {
        return false;
    }
    
    /**
     * 
     */
    public void onShowPress(MotionEvent e)
    {
        
    }
    
    /**
     * 
     */
    public boolean onSingleTapUp(MotionEvent e)
    {
        return false;
    }
    
    /**
     * 
     */
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        return false;
    }
    
    /**
     * 
     */
    public void onLongPress(MotionEvent e)
    {
        longPressed = true;
        
        if (toolstip != null && toolstip.length() > 0)
        {
            control.actionEvent(EventConstant.SYS_SHOW_TOOLTIP, toolstip);
        }
    }
    
    /**
     * 
     */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        return false;
    }
    
    
    /**
     * @return Returns the actionID.
     */
    public int getActionID()
    {
        return actionID;
    }


    /**
     * @return Returns the tooltip.
     */
    public String getToolstip()
    {
        return toolstip;
    }
    
    /**
     * 设置获得焦点时背景ID
     */
    public void setFocusBgResID(int focusBgResID)
    {
        this.focusBgResID = focusBgResID;
    }

    /**
     * 设置按下时背景ID
     * 
     * @param pushBgResID The pushBgResID to set.
     */
    public void setPushBgResID(int pushBgResID)
    {
        this.pushBgResID = pushBgResID;
    }

    /**
     * 设置正常模式下的背景
     * 
     * @param normalBgResID The normalBgResID to set.
     */
    public void setNormalBgResID(int normalBgResID)
    {
        setBackgroundResource(normalBgResID);
        this.normalBgResID = normalBgResID;
    }
    
    /**
     * 
     */
    public int getIconWidth()
    {
        return bitmap == null ? 0 : bitmap.getWidth();
    }
    
    /**
     * 
     */
    public int getIconHeight()
    {
        return bitmap == null ? 0 : bitmap.getHeight();
    }
    
    /**
     * 
     */
    public void dispose()
    {
        toolstip = null;
        control = null;
        if (bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }
        if (bitmapDisable != null)
        {
            bitmapDisable.recycle();
            bitmapDisable = null;
        }
        gesture = null;
    }

    // 动作ID
    protected int actionID;
    // 普通状态下背景图片ID
    protected int normalBgResID = -1;
    // 按下的背景图片ID
    protected int pushBgResID = - 1;
    // 获得焦点的背景图片ID
    protected int focusBgResID = -1;
    // 提示信息
    protected String toolstip;
    // 显示图片
    protected Bitmap bitmap;
    // 不可用的图片
    protected Bitmap bitmapDisable;
    //
    protected GestureDetector gesture;
    //
    protected IControl control;
    // 绘制器
    //
    protected boolean longPressed;
}
