/*
 * 文件名称:          PatchInfo.java
 *  
 * 编译器:            android2.2
 * 时间:              下午9:45:39
 */
package com.cherry.lib.doc.office.pdf;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class RepaintAreaInfo
{
    
    /**
     * 
     * @param reapintBitmap     repaint bitmap instance
     * @param viewWidth         the width of repaint component 
     * @param viewHeight        the height of repaint component 
     * @param repaintArea       repaint area
     */
    public RepaintAreaInfo(Bitmap reapintBitmap, int viewWidth, int viewHeight, Rect repaintArea)
    {
        this.bm = reapintBitmap;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.repaintArea = repaintArea;
    }
    //
    public Bitmap bm;
    //
    public int viewWidth;
    //
    public int viewHeight;
    //
    public Rect repaintArea;
}
