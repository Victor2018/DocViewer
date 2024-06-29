/*
 * 文件名称:          ColumnHeader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:00:26
 */
package com.nvqquy98.lib.doc.office.ss.view;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.other.SheetScroller;
import com.nvqquy98.lib.doc.office.ss.util.HeaderUtil;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;

/**
 * 列标题
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-8
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ColumnHeader
{
    /**
     * 
     * @param sheetView
     */
    public ColumnHeader(SheetView sheetView)
    {
        this.sheetview = sheetView;
        
        rect = new Rect();
    }
    
    public int getColumnRightBound(Canvas canvas, float zoom)
    {
    	canvas.save();
    	Rect clip = canvas.getClipBounds();
        Paint paint = PaintKit.instance().getPaint();        
        paint.setTextSize(SSConstant.HEADER_TEXT_FONTSZIE * zoom);
        
        x = sheetview.getRowHeaderWidth();
         
        layoutColumnLine(canvas, 0, zoom, paint);
        
        canvas.restore();
        
        return Math.min((int)x, clip.right);
    }
    
    private void layoutColumnLine(Canvas canvas, int columnStart, float zoom, Paint paint)
    {
        //    
        float w = 0;    
        Rect clip = canvas.getClipBounds();
        
        Sheet sheet = sheetview.getCurrentSheet();
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        int colIndex = minRowAndColumnInformation.getMinColumnIndex() > columnStart ? minRowAndColumnInformation.getMinColumnIndex() : columnStart;
        if(!minRowAndColumnInformation.isColumnAllVisible())
        {
            colIndex += 1;
            x += (minRowAndColumnInformation.getVisibleColumnWidth() * zoom);
        }
        
        int maxSheetColumns = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXCOLUMN_03 : Workbook.MAXCOLUMN_07;
        while(x <= clip.right && colIndex < maxSheetColumns) 
        {
            if(sheet.isColumnHidden(colIndex))
            {
                colIndex++;
                continue;
            }
            
            w = (sheet.getColumnPixelWidth(colIndex) * zoom);
            x += w;
            colIndex++;
        }
    }
    
    /**
     * 
     * @param canvas
     */
    public void draw(Canvas canvas, int bottomBound, float zoom)
    {
        canvas.save();
        Paint paint = PaintKit.instance().getPaint();
        
        //save paint property      
        int oldColor = paint.getColor();
        float oldTextSize = paint.getTextSize();
        
        paint.setTextSize(SSConstant.HEADER_TEXT_FONTSZIE * zoom);
        
        x = sheetview.getRowHeaderWidth(); 
        
        Rect clip = canvas.getClipBounds();
         
        drawColumnLine(canvas, bottomBound, 0, zoom, paint);
        
        //draw line between column header and sheet body
        paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
        canvas.drawRect(0, columnHeaderHeight, x, columnHeaderHeight + 1, paint);
        
        //restore paint
        paint.setColor(oldColor);
        paint.setTextSize(oldTextSize);
        canvas.restore();
    }
    
    private void drawFirstVisibleColumn(Canvas canvas, float zoom, Paint paint)
    {
        FontMetrics fm = paint.getFontMetrics();  
        float visibleColumnWidth = 0;
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        //draw rest part of first column
        float columnWidht = (minRowAndColumnInformation.getColumnWidth() * zoom);
        visibleColumnWidth = (float)(minRowAndColumnInformation.getVisibleColumnWidth() * zoom);
        // 绘制header
        if(HeaderUtil.instance().isActiveColumn(sheetview.getCurrentSheet(), minRowAndColumnInformation.getMinColumnIndex()))
        {
            paint.setColor(SSConstant.ACTIVE_COLOR);
        }
        else
        {
            paint.setColor(SSConstant.HEADER_FILL_COLOR);
        }
        
        rect.set((int)x, 0, (int)(x + visibleColumnWidth), columnHeaderHeight);
        canvas.drawRect(rect, paint);        
       
        //header line
        paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
        canvas.drawRect(x, 0, x + 1, columnHeaderHeight, paint);
        
        // 绘制文本
        canvas.save();
        canvas.clipRect(rect);
        paint.setColor(SSConstant.HEADER_TEXT_COLOR);
        
        String rowText = HeaderUtil.instance().getColumnHeaderTextByIndex(minRowAndColumnInformation.getMinColumnIndex());
        float textWidth = paint.measureText(rowText);
        float textX = (columnWidht - textWidth) / 2;
        float textY = (int)(columnHeaderHeight - Math.ceil(fm.descent - fm.ascent)) / 2;
        canvas.drawText(rowText, x + textX - (columnWidht - visibleColumnWidth), textY - fm.ascent, paint);
        
        canvas.restore();
    }
    
    /**
     * 
     * @param canvas
     * @param columnStart
     * @param x
     * @param h
     * @param zoom
     * @param paint
     */
    private void drawColumnLine(Canvas canvas, int bottomBound, int columnStart, float zoom, Paint paint)
    {
        FontMetrics fm = paint.getFontMetrics();        
        //    
        float w = 0;    
        Rect clip = canvas.getClipBounds();
        
        Sheet sheet = sheetview.getCurrentSheet();
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        int colIndex = minRowAndColumnInformation.getMinColumnIndex() > columnStart ? minRowAndColumnInformation.getMinColumnIndex() : columnStart;
        if(!minRowAndColumnInformation.isColumnAllVisible())
        {
            drawFirstVisibleColumn(canvas, zoom, paint);
            colIndex += 1;
            x += (minRowAndColumnInformation.getVisibleColumnWidth() * zoom);
        }
        
        int maxSheetColumns = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXCOLUMN_03 : Workbook.MAXCOLUMN_07;
        while(x <= clip.right && colIndex < maxSheetColumns)
        {
            if(sheet.isColumnHidden(colIndex))
            {
                // redraw header grid line          
                paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
                canvas.drawRect(x - 1, 0, x + 1, columnHeaderHeight, paint);

                colIndex++;
                continue;
            }
            
            w = (sheet.getColumnPixelWidth(colIndex) * zoom);
            // 绘制header
            if(HeaderUtil.instance().isActiveColumn(sheetview.getCurrentSheet(), colIndex))
            {
                paint.setColor(SSConstant.ACTIVE_COLOR);
            }
            else
            {
                paint.setColor(SSConstant.HEADER_FILL_COLOR);
            }
            
            rect.set((int)x, 0, (int)(x + w), columnHeaderHeight);
            canvas.drawRect(rect, paint);
           
            if(colIndex != minRowAndColumnInformation.getMinColumnIndex())
            {
                // 绘线
                paint.setColor(SSConstant.GRIDLINE_COLOR);
                canvas.drawRect(x, 0, x + 1, bottomBound, paint);   
            }   
            //header line
            paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
            canvas.drawRect(x, 0, x + 1, columnHeaderHeight, paint);
            
            // 绘制文本
            canvas.save();
            canvas.clipRect(rect);
            paint.setColor(SSConstant.HEADER_TEXT_COLOR);
            
            String colText = HeaderUtil.instance().getColumnHeaderTextByIndex(colIndex);
            int textWidth = (int)paint.measureText(colText);
            float textX = (w - textWidth) / 2;
            float textY = (int)(columnHeaderHeight - Math.ceil(fm.descent - fm.ascent)) / 2;
            canvas.drawText(colText, x + textX, textY - fm.ascent, paint);
            
            canvas.restore();
            x += w;
            colIndex++;
        }
        
        // 绘线最后一根线
        // 绘线
        paint.setColor(SSConstant.GRIDLINE_COLOR);
        canvas.drawRect(x, 0, x + 1, bottomBound, paint);
        //header line
        paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
        canvas.drawRect(x, 0, x + 1, columnHeaderHeight, paint);
        
        // 有空白需要填充
        if (x < clip.right)
        {
            paint.setColor(SSConstant.HEADER_FILL_COLOR);
            rect.set((int)x + 1, 0, clip.right, clip.bottom);
            canvas.drawRect(rect, paint);
        }
    }    
    
    /**
     * @return Returns the columnHeaderHeight.
     */
    public int getColumnHeaderHeight()
    {
        return columnHeaderHeight;
    }

    /**
     * @param columnHeaderHeight The columnHeaderHeight to set.
     */
    public void setColumnHeaderHeight(int columnHeaderHeight)
    {
        this.columnHeaderHeight = columnHeaderHeight;
    }

    /**
     * 
     */
    public void calculateColumnHeaderHeight(float zoom)
    {
        columnHeaderHeight = Math.round(SSConstant.DEFAULT_COLUMN_HEADER_HEIGHT  * zoom);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        sheetview = null;
        rect = null;
    }
    
    //
    private SheetView sheetview;
    // 列标题高度
    private int columnHeaderHeight = SSConstant.DEFAULT_COLUMN_HEADER_HEIGHT;    

    private float x;
    
    private Rect rect;
}
