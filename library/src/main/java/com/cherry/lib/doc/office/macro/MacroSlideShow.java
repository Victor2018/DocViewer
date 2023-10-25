/*
 * 文件名称:          MacroSlideShow.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:51:54
 */
package com.cherry.lib.doc.office.macro;

import com.cherry.lib.doc.office.common.ISlideShow;

public class MacroSlideShow implements ISlideShow
{
    /**
     * 
     * 
     */
    protected MacroSlideShow(SlideShowListener listener)
    {
        this.listener = listener;
    }
    
//    /**
//     * 
//     * @param actionType
//     */
//    public void slideshow(byte actionType)
//    {
//        if(listener != null)
//        {
//            listener.slideshow(actionType);
//        }
//    }
    
    /**
     * exit slideshow
     */
    public void exit()
    {
        if(listener !=  null)
        {
            listener.exit();
        }
    }
    
    public void dispose()
    {
        listener = null;
    }
    
    //
    private SlideShowListener listener;
}
