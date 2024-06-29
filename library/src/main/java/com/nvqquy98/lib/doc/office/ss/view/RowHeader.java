/*
 * 文件名称:          RowHeader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:59:15
 */
package com.nvqquy98.lib.doc.office.ss.view;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.other.SheetScroller;
import com.nvqquy98.lib.doc.office.ss.util.HeaderUtil;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;

/**
 * 行标题
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
public class RowHeader
{
    private static final int EXTEDES_WIDTH = 10;
    
    
    /**
     * 
     * @param sheetView
     */
    public RowHeader(SheetView sheetView)
    {
        this.sheetview = sheetView;
    }
    /**
     * 
     * @param canvas
     * @param rightBound
     * @param zoom
     */
    public int getRowBottomBound(Canvas canvas, float zoom)
    {   
        canvas.save();
        Rect clip = canvas.getClipBounds();
        
        Paint paint = PaintKit.instance().getPaint();
        paint.setTextSize(SSConstant.HEADER_TEXT_FONTSZIE * zoom);
 
        y = (SSConstant.DEFAULT_COLUMN_HEADER_HEIGHT * zoom);
        
        //draw no frozen header
        layoutRowLine(canvas, 0, zoom, paint);
        
        canvas.restore();
        
        return Math.min((int)y, clip.bottom);
    }
    
    private void layoutRowLine(Canvas canvas, int rowStart, float zoom, Paint paint)
    {
        Rect clip = canvas.getClipBounds();
        
        float h = 0;
        Sheet sheet = sheetview.getCurrentSheet();
        Row row;
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        int rowIndex = minRowAndColumnInformation.getMinRowIndex() > rowStart ? minRowAndColumnInformation.getMinRowIndex() : rowStart;
        if(!minRowAndColumnInformation.isRowAllVisible())
        {
            rowIndex += 1;
            y += (minRowAndColumnInformation.getVisibleRowHeight() * zoom);
        }
        
        int maxSheetRows = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXROW_03 : Workbook.MAXROW_07;        
        while (y <= clip.bottom && rowIndex < maxSheetRows) 
        {
            row = sheet.getRow(rowIndex);            
            if(row != null && row.isZeroHeight())
            {
                rowIndex++;
                continue;
            }
            
            h = row == null ? sheetview.getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight();
            h = (h * zoom);            
            y += h;
            rowIndex++;
        }
    }
    
    /**
     * 
     * @param canvas
     */
    public void draw(Canvas canvas, int rightBound, float zoom)
    {   
        canvas.save();
        Paint paint = PaintKit.instance().getPaint();  
        
        //save paint property      
        int oldColor = paint.getColor();
        float oldTextSize = paint.getTextSize();
        
        paint.setTextSize(SSConstant.HEADER_TEXT_FONTSZIE * zoom);
 
        y = (SSConstant.DEFAULT_COLUMN_HEADER_HEIGHT * zoom);      
        
        rect = canvas.getClipBounds();
        rect.set(0, 0, rowHeaderWidth, rect.bottom);
        // 左上角的一块区域
        paint.setColor(SSConstant.HEADER_FILL_COLOR);
        canvas.drawRect(rect, paint);
        
        //draw no frozen header
        drawRowLine(canvas, rightBound, 0, zoom, paint); 
        
        //restore paint
        paint.setColor(oldColor);
        paint.setTextSize(oldTextSize);    
        
        canvas.restore();
    }    
    
    /**
     * 
     * @param canvas
     * @param x
     * @param y
     * @param w
     * @param zoom
     * @param paint
     */
    private void drawFirstVisibleHeader(Canvas canvas, float rightBound, float zoom, Paint paint)
    {
        FontMetrics fm = paint.getFontMetrics();
        Rect clip = canvas.getClipBounds();
        float visibleRowHeight = 0;
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        float rowHeight = (minRowAndColumnInformation.getRowHeight() * zoom);
        visibleRowHeight = (float)(minRowAndColumnInformation.getVisibleRowHeight() * zoom);
        
        
        // 绘制header
        if(HeaderUtil.instance().isActiveRow(sheetview.getCurrentSheet(), minRowAndColumnInformation.getMinRowIndex()))
        {
            paint.setColor(SSConstant.ACTIVE_COLOR);
        }
        else
        {
            paint.setColor(SSConstant.HEADER_FILL_COLOR);
        }
        
        rect.set(0, (int)y, rowHeaderWidth, (int)(y + visibleRowHeight));
        canvas.drawRect(rect, paint);        
        
        // 绘线
        paint.setColor(SSConstant.GRIDLINE_COLOR);
        canvas.drawRect(0, y, rightBound, y + 1, paint);
        //head line
        paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
        canvas.drawRect(0, y, rowHeaderWidth, y + 1, paint);
        // 绘制文本
        canvas.save();
        canvas.clipRect(rect);
        paint.setColor(SSConstant.HEADER_TEXT_COLOR); 
        
        String rowText = String.valueOf(minRowAndColumnInformation.getMinRowIndex() + 1);
        int textWidth = (int)paint.measureText(rowText);
        int textX = (rowHeaderWidth - textWidth) / 2;
        int textY = (int)(rowHeight - Math.ceil(fm.descent - fm.ascent));
        canvas.drawText(rowText, textX, y + textY - fm.ascent - (rowHeight -visibleRowHeight), paint);
        
        canvas.restore();
    }
    
    private void drawRowLine(Canvas canvas, int rightBound, int rowStart, float zoom, Paint paint)
    {
        FontMetrics fm = paint.getFontMetrics();        
        Rect clip = canvas.getClipBounds();
        
        
        float h = 0;
        Sheet sheet = sheetview.getCurrentSheet();
        Row row;
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        int rowIndex = minRowAndColumnInformation.getMinRowIndex() > rowStart ? minRowAndColumnInformation.getMinRowIndex() : rowStart;
        if(!minRowAndColumnInformation.isRowAllVisible())
        {
            //draw rest part of first row            
            drawFirstVisibleHeader(canvas, rightBound, zoom, paint);
            rowIndex += 1;
            y += (minRowAndColumnInformation.getVisibleRowHeight() * zoom);
        } 
        
        int maxSheetRows = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXROW_03 : Workbook.MAXROW_07;
        while (y <= clip.bottom && rowIndex < maxSheetRows) 
        {
            row = sheet.getRow(rowIndex);
            
            if(row != null && row.isZeroHeight())
            {
                // redraw header grid line          
                paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
                canvas.drawRect(0, y - 1, rowHeaderWidth, y + 1, paint);                
                rowIndex++;
                continue;
            }
            
            h = row == null ? sheetview.getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight();
            h = (h * zoom);
            
          
            // 绘制header
            if(HeaderUtil.instance().isActiveRow(sheetview.getCurrentSheet(), rowIndex))
            {
                paint.setColor(SSConstant.ACTIVE_COLOR);
            }
            else
            {
                paint.setColor(SSConstant.HEADER_FILL_COLOR);
            }
            
            rect.set(0, (int)y, rowHeaderWidth, (int)(y + h));
            canvas.drawRect(rect, paint);
            
            // 绘线
            paint.setColor(SSConstant.GRIDLINE_COLOR);
            canvas.drawRect(0, y, rightBound, y + 1, paint);
            //head line
            paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
            canvas.drawRect(0, y, rowHeaderWidth, y + 1, paint);
            // 绘制文本
            canvas.save();
            canvas.clipRect(rect);
            paint.setColor(SSConstant.HEADER_TEXT_COLOR); 
            
            String rowText = String.valueOf(rowIndex + 1);
            int textWidth = (int)paint.measureText(rowText);
            int textX = (rowHeaderWidth - textWidth) / 2;
            int textY = (int)(h - Math.ceil(fm.descent - fm.ascent));
            canvas.drawText(rowText, textX, y + textY - fm.ascent, paint);
            
            canvas.restore();
            
            y += h;
            rowIndex++;
        }
        
        // 绘线最后一根线
        paint.setColor(SSConstant.GRIDLINE_COLOR);
        canvas.drawRect(0, y, rightBound, y + 1, paint);
        //head line
        paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
        canvas.drawRect(0, y, rowHeaderWidth, y + 1, paint);
        
        // 有空白需要填充
        if (y < clip.bottom)
        {
            paint.setColor(SSConstant.HEADER_FILL_COLOR);
            rect.set(0, (int)(y + 1), clip.right, clip.bottom);
            canvas.drawRect(rect, paint);
        }  
        //draw  line between row header and sheet body
        paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
        canvas.drawRect(rowHeaderWidth, 0, rowHeaderWidth + 1, y, paint);
    }    
    
    /**
     * 计算行标题宽度 
     */
    public void calculateRowHeaderWidth(float zoom)
    {
        Paint paint = PaintKit.instance().getPaint();
        paint.setTextSize(SSConstant.HEADER_TEXT_FONTSZIE);
        rowHeaderWidth = Math.round(paint.measureText(String.valueOf(sheetview.getCurrentMinRow()))) + EXTEDES_WIDTH;
        rowHeaderWidth = Math.round(Math.max(rowHeaderWidth, SSConstant.DEFAULT_ROW_HEADER_WIDTH)  * zoom);
    }
    
    
    /**
     * @return Returns the rowHeaderWidth.
     */
    public int getRowHeaderWidth()
    {
        return rowHeaderWidth;
    }

    /**
     * @param rowHeaderWidth The rowHeaderWidth to set.
     */
    public void setRowHeaderWidth(int rowHeaderWidth)
    {
        this.rowHeaderWidth = rowHeaderWidth;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        sheetview = null;
        rect =  null;
    }
    
    //
    private SheetView sheetview;    
    // 行标题宽度
    private int rowHeaderWidth = SSConstant.DEFAULT_ROW_HEADER_WIDTH;
    //
    private float y;
    
    private Rect rect;
}
