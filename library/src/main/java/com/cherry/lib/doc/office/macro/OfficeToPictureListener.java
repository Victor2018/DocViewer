/*
 * 文件名称:          IOfficeToPictureListener.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:19:51
 */
package com.cherry.lib.doc.office.macro;

import android.graphics.Bitmap;

/**
 * office to picture listener
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-10
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface OfficeToPictureListener
{

    /**
     * Get converter to of picture Bitmap instance, if the return is empty, is not generated picture
     * 
     * @param componentWidth  engine component width
     * @param componentHeight engine component height
     * 
     * @return Bitmap instance
     */
    public Bitmap getBitmap(int componentWidth, int componentHeight);

    /**
     * picture generated, the callback method
     * 
     * @param bitmap  generated picture bitmap  
     */
    public void callBack(Bitmap bitmap);
}
