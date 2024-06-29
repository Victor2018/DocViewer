/*
 * 文件名称:          AnimationManager.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:58:01
 */
package com.nvqquy98.lib.doc.office.pg.animate;

import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.ITimerListener;
import com.nvqquy98.lib.doc.office.system.beans.ATimer;

public class AnimationManager implements ITimerListener
{
    public AnimationManager(IControl control)
    {
        this.control = control;
    }    
    
    public void setAnimation(IAnimation animation)
    {
        //stop last animation
        if(this.animation != null && timer != null && timer.isRunning())
        {            
            timer.stop();
            this.animation.stop();
        }
        
        this.animation = animation;
    }
    
    /**
     * 
     * @param delay
     */
    public void beginAnimation(int delay)
    {
        if(timer ==  null)
        {
            timer = new ATimer(delay, this);
        }
        
        if(animation != null)
        {            
            actionIndex = 0;
            animation.start();
            
            timer.start();
            if(control.getOfficeToPicture() != null)
            {
                control.getOfficeToPicture().setModeType(IOfficeToPicture.VIEW_CHANGING);
            }
        }        
    }
    
    public void restartAnimationTimer()
    {
    	if(timer != null)
    	{
    		timer.restart();
    	}
    }
    
    public void killAnimationTimer()
    {
    	if (timer != null)
        {
            timer.stop();
        }
    }
    
    /**
     * 
     */
    public void stopAnimation()
    {
        if(animation != null)
        {           
            if (timer != null)
            {
                timer.stop();
            }
            if (animation != null)
            {
                animation.stop();
            }
            
            if(control.getOfficeToPicture() != null)
            {
                control.getOfficeToPicture().setModeType(IOfficeToPicture.VIEW_CHANGE_END);
            }            
            control.actionEvent(EventConstant.PG_REPAINT_ID, null);
        } 
    }
    
    /**
     * 定时器
     */
    public void actionPerformed()
    {
        if(animation != null && animation.getAnimationStatus() != Animation.AnimStatus_End)
        {
            animation.animation(++actionIndex); 
            
            control.actionEvent(EventConstant.PG_REPAINT_ID, null);

            if (timer != null)
            {
                timer.restart();
            }
        }
        else
        {
            if (timer != null)
            {
                timer.stop();
            }
            if(control.getOfficeToPicture() != null)
            {
                control.getOfficeToPicture().setModeType(IOfficeToPicture.VIEW_CHANGE_END);
            }
            control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
        }
             
    }
    
    /**
     * animation stoped or not
     * @return
     */
    public boolean hasStoped()
    {
        if(animation != null)
        {            
            return animation.getAnimationStatus() == Animation.AnimStatus_End;
        }
        
        return true;
    }
    
    public void dispose()
    {
        control =  null;
        animation = null;
        if(timer != null)
        {
            timer.dispose();
            timer = null;
        }
    }
    
    
    private IAnimation animation;
    
    private ATimer timer;
    private int actionIndex;
    private IControl control;
}
