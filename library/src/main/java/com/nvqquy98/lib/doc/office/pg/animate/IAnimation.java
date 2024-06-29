
package com.nvqquy98.lib.doc.office.pg.animate;

import android.graphics.Rect;

public interface IAnimation
{
    public class AnimationInformation
    {
        //rotation during animating 
        public static final int ROTATION = 720;
        
        public AnimationInformation(Rect postion, int alpha, int angle)
        {
            if(postion != null)
            {
                this.postion = new Rect(postion);
            }
            
            this.alpha = alpha;
            this.angle = angle;
        }
        
        /**
         * 
         * @param postion
         * @param alpha
         * @param angle
         */
        public void setAnimationInformation(Rect postion, int alpha, int angle)
        {
            if(postion != null)
            {
                this.postion = new Rect(postion);
            }
            this.alpha = alpha;
            this.angle = angle;
            
            progress = 0;
        }
        
        /**
         * current progress
         * @param progress
         */
        public void setProgress(float progress)
        {
            this.progress = progress;
        }
        
        /**
         * current progress
         * @return
         */
        public float getProgress()
        {
            return progress;
        }
        
        /**
         * 
         * @param postion
         */
        public void setPostion(Rect postion)
        {
            if(postion != null)
            {
                this.postion = new Rect(postion);
            }
        }
        
        /**
         * get current shape postion
         * @return
         */
        public Rect getPostion()
        {
            return postion;
        }
        
        public void setAlpha(int alpha)
        {
            this.alpha = alpha;
        }
        
        /**
         * get  current alpha
         * @return
         */
        public int getAlpha()
        {
            return alpha;
        }
        
        public void setAngle(int angle)
        {
            this.angle = angle;
        }
        
        /**
         * get current roration
         * @return
         */
        public int getAngle()
        {
            return angle;
        }
        
        /**
         * 
         */
        public void dispose()
        {
            postion = null;
        }
        
        private int alpha;
        private int angle;
        private Rect postion;
        
        private float progress;
    }
   
    /**
     * 
     * @return
     */
    public byte getAnimationStatus();
    
    /**
     * start animation
     */
    public void start();
    
    /**
     * stop animation 
     */
    public void stop();
    
    /**
     * update animation parameters
     * @param frameIndex
     */
    public void animation(int frameIndex);
    
    /**
     * get current animation information of frame when animating
     * @return
     */
    public AnimationInformation getCurrentAnimationInfor();
    
    /**
     * 
     * @return
     */
    public ShapeAnimation getShapeAnimation();
    
    /**
     * get animation duration(ms)
     * @return
     */
    public int getDuration();
    
    /**
     * set animation duration(ms)
     */
    public void setDuration(int duration);
    
    /**
     * 
     */
    public int getFPS();
    
    public void dispose();
}
