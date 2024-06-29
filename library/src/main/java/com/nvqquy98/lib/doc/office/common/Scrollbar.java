/*
 * 文件名称:          Scrollbar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:30:54
 */
package com.nvqquy98.lib.doc.office.common;

import com.nvqquy98.lib.doc.office.java.awt.Dimension;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-13
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class Scrollbar
{
    private final int SCROLLBAR_SIZE = 5;
    private final int SCROLLBAR_OFFBORDER = 2;
 
    private final int SCROLLBAR_COLOR_ALPHA = 125;
    // scroll bar color
    private final int SCROLLBAR_COLOR = 0x8f444444;/*0x8fcecfd6;*/ 
    
    public void setPageSize(int width, int height)
    {
       this.pageSize.setSize(width, height);
    }
    
    /**
     * call setPageSize to set page size before call draw function
     * @param canvas
     * @param zoom
     */
    public void draw(Canvas canvas, int currentX, int currentY, Paint paint)
    {
        Rect clip = canvas.getClipBounds();
        
        int oldColor = paint.getColor();
        int alpha = paint.getAlpha();
        
        paint.setColor(SCROLLBAR_COLOR);        
        paint.setAlpha(SCROLLBAR_COLOR_ALPHA);
        
        if(pageSize.width > clip.right)
        {          
            drawHorizontalScrollBar(canvas, currentX, paint);
        }
        
        if(pageSize.height > clip.bottom)
        {
            drawVerticalScrollBar(canvas, currentY, paint);
        }
        
        paint.setColor(oldColor);
        paint.setAlpha(alpha);
    }
    
    /**
     * 
     * @param canvas
     * @param zoom
     */
    private void drawHorizontalScrollBar(Canvas canvas, int currentX, Paint paint)
    {
        Rect clip = canvas.getClipBounds();
        
        //horizontal scroll bar length
        float length = clip.right * clip.right / pageSize.width;
        float pixelSteps = pageSize.width / (float)clip.right;
        
        //find the scroll bar rect
        float left = (pageSize.width / 2 - currentX) / pixelSteps - length / 2;
        rect.set(left, clip.bottom - SCROLLBAR_SIZE - SCROLLBAR_OFFBORDER, left + length, clip.bottom - SCROLLBAR_OFFBORDER);
        canvas.drawRoundRect(rect, SCROLLBAR_SIZE / 2, SCROLLBAR_SIZE / 2, paint);
    }
    
    /**
     * 
     * @param canvas
     * @param zoom
     */
    private void drawVerticalScrollBar(Canvas canvas, int currentY, Paint paint)
    {
        Rect clip = canvas.getClipBounds();
        
        //vertical scroll bar length
        float length = clip.bottom * clip.bottom / pageSize.height;
        float pixelSteps = pageSize.height / (float)clip.bottom;
        //find the scroll bar rect
        float top = (pageSize.height / 2 - currentY) / pixelSteps - length / 2;       

        rect.set(clip.right - SCROLLBAR_SIZE - SCROLLBAR_OFFBORDER, top, clip.right - SCROLLBAR_OFFBORDER, top + length);        
        canvas.drawRoundRect(rect, SCROLLBAR_SIZE / 2, SCROLLBAR_SIZE / 2, paint);             
    }
    //
    private Dimension pageSize = new Dimension();
    //
    private RectF rect = new RectF();
}
