/*
 * 文件名称:          SlideshowListener.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:19:37
 */
package com.cherry.lib.doc.office.macro;

import com.cherry.lib.doc.office.common.ISlideShow;

public interface SlideShowListener
{
    //begin slideshow
    public static final byte SlideShow_Begin = ISlideShow.SlideShow_Begin;                           //0
    //exit slideshow
    public static final byte SlideShow_Exit = ISlideShow.SlideShow_Exit;          //1
    //previous step of animation
    public static final byte SlideShow_PreviousStep = ISlideShow.SlideShow_PreviousStep;   //2
    //next step of animation
    public static final byte SlideShow_NextStep = ISlideShow.SlideShow_NextStep;//3
    //previous slide
    public static final byte SlideShow_PreviousSlide = ISlideShow.SlideShow_PreviousSlide;//4
    //next slide
    public static final byte SlideShow_NextSlide = ISlideShow.SlideShow_NextSlide;//5
    
//    /**
//     * 
//     * @param actionType
//     */
//    public void slideshow(byte actionType);
    
    /**
     * exit slideshow
     */
    public void exit();
}
