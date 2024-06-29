/*
 * 文件名称:          FadeAnimation.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:00:42
 */
package com.nvqquy98.lib.doc.office.pg.animate;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-11-28
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class FadeAnimation extends Animation
{
    public FadeAnimation(ShapeAnimation shapeAnim)
    {
        super(shapeAnim);
        
        initAnimationKeyPoint();
    }
    
    public FadeAnimation(ShapeAnimation shapeAnim, int duration)
    {
        super(shapeAnim, duration);
        
        initAnimationKeyPoint();
    }
    
    public FadeAnimation(ShapeAnimation shapeAnim, int duration, int fps)
    {
        super(shapeAnim, duration, fps);
        
        initAnimationKeyPoint();
    }
    
    /**
     * 
     */
    private void initAnimationKeyPoint()
    {
        if(shapeAnim != null)
        {
            begin = new AnimationInformation(null, (shapeAnim.getAnimationType() == ShapeAnimation.SA_ENTR) ? 0 : 255, 0);
            end = new AnimationInformation(null, (shapeAnim.getAnimationType() == ShapeAnimation.SA_ENTR) ? 255 : 0, 0);
            current = new AnimationInformation(null, (shapeAnim.getAnimationType() == ShapeAnimation.SA_ENTR) ? 0 : 255, 0);
        }
        else
        {
            begin = new AnimationInformation(null, 0, 0);
            end = new AnimationInformation(null, 255, 0);
            current = new AnimationInformation(null, 0, 0);
        }        
    }
    
    /**
     * 定时器
     */
    public void animation(int frameIndex)
    {
        if(shapeAnim != null && current != null)
        {
            switch(shapeAnim.getAnimationType())
            {
                case ShapeAnimation.SA_ENTR:
                    fadeIn(frameIndex * delay);
                    break;
                    
                case ShapeAnimation.SA_EMPH:
                    fadeIn(frameIndex * delay);
                    break;
                        
                case ShapeAnimation.SA_EXIT:
                    fadeOut(frameIndex * delay);
                    break;
                    
                default:
                    break;
            }
        }        
    }
    
    /**
     * 
     */
    public void start()
    {
        super.start();        
        current.setProgress(0f);
    }
    
    /**
     * stop animation 
     */
    public void stop()
    {
        super.stop();
        
        if(current != null)
        {
            current.setAngle(0);
            current.setProgress(1f);
            
            if(shapeAnim != null)
            {
                switch(shapeAnim.getAnimationType())
                {
                    case ShapeAnimation.SA_ENTR:
                        current.setAlpha(255);
                        break;
                        
                    case ShapeAnimation.SA_EXIT:
                        current.setAlpha(0);
                        break;
                        
                    default:
                        break;
                } 
            }         
        }
    }
    
    /**
     * 
     * @param time
     */
    private void fadeIn(int time)
    {
        if(time < duration)
        {            
            float progress = time / duration;
            current.setProgress(progress);
            current.setAlpha((int)(255 * progress));
            //current.setAngle((int)(AnimationInformation.ROTATION * progress));
        }
        else
        {
            status = Animation.AnimStatus_End;
            current.setProgress(1f);
            current.setAlpha(255);
            //current.setAngle(AnimationInformation.ROTATION);
        }
    }    
    
    /**
     * 
     * @param time
     */
    private void fadeOut(int time)
    {
        if(time < duration)
        {
            float progress = time / duration;
            current.setProgress(progress);
            current.setAlpha((int)(255 * ( 1 - progress)));
            //current.setAngle((int)(AnimationInformation.ROTATION * progress));
        }
        else
        {
            status = Animation.AnimStatus_End;
            current.setProgress(1f);
            current.setAlpha(0);
            //current.setAngle(AnimationInformation.ROTATION);
        }
    }

}
