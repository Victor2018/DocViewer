/*
 * 文件名称:          SlideView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午8:58:00
 */
package com.nvqquy98.lib.doc.office.pg.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.shape.GroupShape;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.animate.Animation;
import com.nvqquy98.lib.doc.office.pg.animate.AnimationManager;
import com.nvqquy98.lib.doc.office.pg.animate.EmphanceAnimation;
import com.nvqquy98.lib.doc.office.pg.animate.FadeAnimation;
import com.nvqquy98.lib.doc.office.pg.animate.IAnimation;
import com.nvqquy98.lib.doc.office.pg.animate.ShapeAnimation;
import com.nvqquy98.lib.doc.office.pg.control.Presentation;
import com.nvqquy98.lib.doc.office.pg.model.PGSlide;
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.CalloutView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

/**
 * Slide视图
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-10
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class SlideShowView
{   
    /**
     * 
     * @param presentation
     */
    public SlideShowView(Presentation presentation, PGSlide slide)
    {
        this.presentation = presentation;
        this.slide = slide;
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(24); 
        
        bgRect = new Rect();
    }
    
    /**
     * remove old animation  information
     */
    private void removeAnimation()
    {        
        if(shapeVisible == null)
        {
            shapeVisible = new HashMap<Integer, Map<Integer, IAnimation>>();
        }
        else
        {
            shapeVisible.clear();
            slideshowStep = 0;
        }
        
        if(animationMgr != null)
        {
            animationMgr.stopAnimation();
        }
        
        if(presentation.getEditor() != null)
        {
            presentation.getEditor().clearAnimation();
        }
        
        if(slide != null)
        {
            int count = slide.getShapeCount();
            for (int i = 0; i < count; i++)
            {
                removeShapeAnimation(slide.getShape(i));
            } 
        }
    }
    
    private void removeShapeAnimation(IShape shape)
    {
        if(shape instanceof GroupShape)
        {
            IShape[] shapes = ((GroupShape)shape).getShapes();
            for(IShape item : shapes)
            {
                removeShapeAnimation(item);
            }
        }
        else
        {
            IAnimation anim = shape.getAnimation();
            if(anim != null)
            {
                shape.setAnimation(null);
                anim.dispose();
                anim = null;
            }
        }
        
        
    }
    
    /**
     * slideshow begin
     */
    public void initSlideShow(PGSlide slide, boolean showAnimation)
    {
        //remove old animation  information
        removeAnimation();
        
        this.slide = slide;
        if(slide == null)
        {
            return;
        }
        
        List<ShapeAnimation> shapeAnimLst = slide.getSlideShowAnimation();
        if(shapeAnimLst != null)
        {
            int count = shapeAnimLst.size();
            for (int i = 0; i < count; i++)
            {
                ShapeAnimation shapeAnim = shapeAnimLst.get(i);
                Map<Integer, IAnimation> animMap = shapeVisible.get(shapeAnim.getShapeID());
                if(animMap == null)
                {
                    animMap = new HashMap<Integer, IAnimation>();                    
                    shapeVisible.put(shapeAnim.getShapeID(), animMap);                                       
                }
                
                for(int para = shapeAnim.getParagraphBegin(); para <= shapeAnim.getParagraphEnd(); para++)
                {
                    IAnimation animation = animMap.get(para);
                    if(animation == null)
                    {
                        animation = new FadeAnimation(shapeAnim, animDuration);
                        for(para = shapeAnim.getParagraphBegin(); para <= shapeAnim.getParagraphEnd(); para++)
                        {
                            animMap.put(para, animation);
                        }                        
                        setShapeAnimation(shapeAnim.getShapeID(), animation);
                        break;
                    }
                }
            }
        }
        
        //page animation
        if(animationMgr == null)
        {
            animationMgr = presentation.getControl().getSysKit().getAnimationManager();
        }
        
        if(slide.hasTransition())
        {
            if(pageAnimation ==  null)
            {
                pageAnimation = new FadeAnimation(new ShapeAnimation(ShapeAnimation.Slide, ShapeAnimation.SA_ENTR), animDuration);
            }
            else
            {
                pageAnimation.setDuration(animDuration);
            }
            
            animationMgr.setAnimation(pageAnimation);
            if(showAnimation)
            {
                animationMgr.beginAnimation(1000 / pageAnimation.getFPS()); 
            }
            else
            {
                animationMgr.stopAnimation();
            }
        }               
    }
    
    /**
     * 
     * @param shapeID
     * @param animation
     * @param beginAnimation start now or not
     */
    private void setShapeAnimation(int shapeID, IAnimation animation)
    {        
        int count = slide.getShapeCount();
        for (int i = 0; i < count; i++)
        {
            IShape shape = slide.getShape(i);
            if((shape.getShapeID() == shapeID || shape.getGroupShapeID() == shapeID) && shape.getAnimation() == null )
            {                
                setShapeAnimation(shape, animation);            
            }
        }
    }
    
    private void setShapeAnimation(IShape shape, IAnimation animation)
    {
        if(shape instanceof GroupShape)
        {
            IShape[] shapes = ((GroupShape)shape).getShapes();
            for(IShape item : shapes)
            {
                setShapeAnimation(item, animation);
            }
        }
        else
        {                    
            shape.setAnimation(animation);                
        }
    }
    
    public void endSlideShow()
    {        
        //remove old animation  information
        removeAnimation();
    }
    
    /**
     * exit
     * @return
     */
    public boolean isExitSlideShow()
    {        
        return slide == null;
    }
    
    /**
     * will goto next slide
     * @return
     */
    public boolean gotopreviousSlide()
    {
        List<ShapeAnimation> shapeAnimLst = slide.getSlideShowAnimation();
        if(shapeAnimLst != null)
        {
            return slideshowStep <= 0;
        }
       
        return true;
    }
    
    /**
     * will goto next slide
     * @return
     */
    public boolean gotoNextSlide()
    {
        List<ShapeAnimation> shapeAnimLst = slide.getSlideShowAnimation();
        if(shapeAnimLst != null)
        {
            return slideshowStep >= shapeAnimLst.size();
        }
       
        return true;
    }
    
    /**
     * called when current view is full screen, show shapes one by one after click event
     */
    public void previousActionSlideShow()
    {
        int oldSlideshowStep = slideshowStep - 1;        
        initSlideShow(slide, false);
        while(slideshowStep < oldSlideshowStep)
        {
            slideshowStep = slideshowStep + 1;            
            updateShapeAnimation(slideshowStep, false);;
        }        
    }
        
    /**
     * called when current view is full screen, show shapes one by one after click event
     */
    public void nextActionSlideShow()
    {
        slideshowStep = slideshowStep + 1;       
        
        updateShapeAnimation(slideshowStep, true);
    }

    /**
     * go to last action of current slide
     */
    public void gotoLastAction()
    {
        while(!gotoNextSlide())
        {
            slideshowStep = slideshowStep + 1;       
            
            updateShapeAnimation(slideshowStep, false);
        }
    }
    /**
     * 
     * @param curSlideShowStep
     * @param isNextAction
     */
    private void updateShapeAnimation(int curSlideShowStep, boolean showAnimation)
    {
        List<ShapeAnimation> shapeAnimLst = slide.getSlideShowAnimation();
        if(shapeAnimLst != null)
        {            
            ShapeAnimation shapeAnim = shapeAnimLst.get(curSlideShowStep - 1);   
            
            updateShapeArea(shapeAnim.getShapeID(), presentation.getZoom());
            IAnimation animation;
            if(shapeAnim.getAnimationType() != ShapeAnimation.SA_EMPH)
            {                
                animation = new FadeAnimation(shapeAnim, animDuration);
            }
            else
            {
                animation = new EmphanceAnimation(shapeAnim, animDuration);
            }
            
            Map<Integer, IAnimation> animMap = shapeVisible.get(shapeAnim.getShapeID());
            animMap.put(shapeAnim.getParagraphBegin(), animation);
            
            updateShapeAnimation(shapeAnim.getShapeID(), animation, showAnimation);
        }
    }
    
    /**
     * 
     * @param shapeID
     * @param animation
     * @param isNextAction
     */
    private void updateShapeAnimation(int shapeID, IAnimation animation, boolean showAnimation)
    {        
        animationMgr.setAnimation(animation);
        
        int count = slide.getShapeCount();
        for (int i = 0; i < count; i++)
        {
            IShape shape = slide.getShape(i);
            if((shape.getShapeID() == shapeID || shape.getGroupShapeID() == shapeID))
            { 
                this.setShapeAnimation(shape, animation);               
            }
        }        
        
        if(showAnimation)
        {
            animationMgr.beginAnimation(1000 / animation.getFPS());
        }
        else
        {
            animationMgr.stopAnimation();
        }
    }
    
    private void updateShapeArea(int shapeID, float zoom)
    {
        int count = slide.getShapeCount();
        for (int i = 0; i < count; i++)
        {
            IShape shape = slide.getShape(i);
            if(shape.getShapeID() == shapeID)
            {
                Rectangle shapeRect = shape.getBounds();
                if(shapeRect != null)
                {
                    int left = Math.round(shapeRect.x * zoom);
                    int top = Math.round(shapeRect.y * zoom);
                    int width = Math.round(shapeRect.width * zoom);
                    int height = Math.round(shapeRect.height * zoom);
                    
                    if(animShapeArea == null)
                    {
                        animShapeArea = new Rect(left, top, left + width, top + height);
                    }
                    else
                    {
                        animShapeArea.set(left, top, left + width, top + height);
                    }

                    
                    return;
                }
            }
        }
        
        animShapeArea = null;
    }
    
    /**
     * 更变要显示的slide
     */
    public void changeSlide(PGSlide slide)
    {
        this.slide = slide;
    }
    
    /**
     * drawing area size
     * @return
     */
    public Rect getDrawingRect()
    {
        return bgRect;
    }
    
    /**
     * 
     * @param canvas 画板 
     * @param zoom 缩放
     * @param callouts 便签 
     */
    public void drawSlide(Canvas canvas, float zoom, CalloutView callouts)
    {
        if(pageAnimation != null && pageAnimation.getAnimationStatus() != Animation.AnimStatus_End)
        {
            //page animation
            zoom *= pageAnimation.getCurrentAnimationInfor().getProgress();
            if(zoom <= 0.001f)
            {
            	return;
            }
        }
        
        Dimension d = presentation.getPageSize();
        int w = (int)(d.width * zoom);
        int h = (int)(d.height * zoom);
        int x = (presentation.getmWidth() - w) / 2 ;
        int y = (presentation.getmHeight() - h) / 2;
        
        canvas.save();
        canvas.translate(x, y);
        canvas.clipRect(0, 0, w, h);            
        bgRect.set(0, 0, w, h);
        
        SlideDrawKit.instance().drawSlide(canvas, presentation.getPGModel(), presentation.getEditor(), slide, zoom, shapeVisible);

        canvas.restore();    
        
    	if (callouts != null)
    	{
	        if(pageAnimation != null && pageAnimation.getAnimationStatus() != Animation.AnimStatus_End)
	        {
	        	callouts.setVisibility(View.INVISIBLE);
	        }
	        else
	        {
	    		callouts.setZoom(zoom);
	    		callouts.layout(x, y, x + w, y + h);
	    		callouts.setVisibility(View.VISIBLE);
	        }
    	}
    }

    
    /**
     * 
     * @param canvas 画板 
     * @param zoom 缩放
     */
    public void drawSlideForToPicture(Canvas canvas, float zoom,int originBitmapW, int originBitmapH)
    { 
        Rect rect = canvas.getClipBounds();
        if (rect.width() != originBitmapW || rect.height() != originBitmapH)
        {
            zoom *= Math.min((rect.width() / (float)originBitmapW), (rect.height() / (float)originBitmapH));
        }
        SlideDrawKit.instance().drawSlide(canvas, presentation.getPGModel(), presentation.getEditor(), slide, zoom, shapeVisible);
    }
    
    /**
     * 
     * @return
     */
    public boolean animationStoped()
    {
        if(animationMgr != null)
        {
            return animationMgr.hasStoped();
        }
        
        return true;
    }
    
    /**
     * set animation duration(ms)
     * @param duration larger than 100ms
     */
    public void setAnimationDuration(int duration)
    {
        this.animDuration = duration;
    }
    
    /**
     * slideshow to image
     * @param slide
     * @param step animation index(base 1)
     * @return
     */
    public Bitmap getSlideshowToImage(PGSlide slide, int step)
    {
        this.slide = slide;
        initSlideShow(slide, false);
        while(slideshowStep < step - 1)
        {
            slideshowStep = slideshowStep + 1;            
            updateShapeAnimation(slideshowStep, false);;
        }
        
        Bitmap image = SlideDrawKit.instance().slideToImage(presentation.getPGModel(), presentation.getEditor(), slide, shapeVisible);
        
        //remove old animation  information
        removeAnimation();
        
        return image;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        paint = null;
        presentation = null;
        slide = null;
        
        if(animationMgr != null)
        {
            animationMgr.dispose();
            animationMgr = null;
        }
        
        if(shapeVisible !=  null)
        {
            shapeVisible.clear();
            shapeVisible = null;
        }
    }
    
    private Paint paint;
    private Rect bgRect;
    // 
    private Presentation presentation;
    //
    private PGSlide slide;
    
    //slideshow
    private int slideshowStep =  0;
    //
    private AnimationManager animationMgr;
    //shapeid, subshape id/paragraph id, visible
    private Map<Integer, Map<Integer, IAnimation>> shapeVisible; 
    //invalidate area
    private Rect animShapeArea;
    private IAnimation pageAnimation;
    private int animDuration = Animation.Duration;
}
