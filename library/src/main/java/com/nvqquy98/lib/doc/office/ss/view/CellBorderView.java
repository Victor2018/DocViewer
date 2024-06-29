/*
 * Filename:        CellBorderView.java
 * LegalCopyright	Copyright (C) wxiwei Inc. 2011-2014
 * Compiler:        JDK1.5.0_01
 * Time:            上午8:40:52
 */
package com.nvqquy98.lib.doc.office.ss.view;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTableCellStyle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * excel 应用控制
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-11-3
 * <p>
 * 负责人:          jqin
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class CellBorderView
{    
    public CellBorderView( SheetView sheetView)
    {
        this.sheetView = sheetView;        
    }     
    
    /**
     * 
     * @param canvas
     * @param cell
     * @param cellInfor.rowIndex
     * @param cellInfor.columnIndex
     * @param cellInfor.left
     * @param cellInfor.top
     * @param w
     * @param cellInfor.height
     * @return
     */
    public void draw(Canvas canvas, Cell cell, RectF rect, SSTableCellStyle tableCellStyle)
    {  
        Paint paint = PaintKit.instance().getPaint();
        //save paint property      
        int oldColor = paint.getColor();         
        
        Workbook book =  sheetView.getSpreadsheet().getWorkbook();        
       
        canvas.save();
        int colorIndex;
        int color;
        // draw left border
        if(rect.left > sheetView.getRowHeaderWidth())
        {
            if((colorIndex = LeftBorder(cell)) > -1)
            {
                paint.setColor( book.getColor(colorIndex));
                canvas.drawRect(rect.left, rect.top, rect.left + 1, rect.bottom, paint);
            }  
            else if(tableCellStyle != null && tableCellStyle.getBorderColor() != null)
            {
                paint.setColor(tableCellStyle.getBorderColor());
                canvas.drawRect(rect.left, rect.top, rect.left + 1, rect.bottom, paint);
            }
        }        

        // draw top border
        if(rect.top > sheetView.getColumnHeaderHeight())
        {
            if((colorIndex = TopBorder(cell)) > -1)
            {
                paint.setColor(book.getColor(colorIndex));
                canvas.drawRect(rect.left, rect.top, rect.right, rect.top + 1, paint);
            }
            else if(tableCellStyle != null && tableCellStyle.getBorderColor() != null)
            {
                paint.setColor(tableCellStyle.getBorderColor());
                canvas.drawRect(rect.left, rect.top, rect.right, rect.top + 1, paint);
            }
        }        
        
        // draw right border
        if(rect.right > sheetView.getRowHeaderWidth())
        {
            if((colorIndex = RightBorder(cell)) > -1)
            {
                paint.setColor(book.getColor(colorIndex));
                canvas.drawRect(rect.right, rect.top, rect.right + 1, rect.bottom, paint);
            }
            else if(tableCellStyle != null && tableCellStyle.getBorderColor() != null)
            {
                paint.setColor(tableCellStyle.getBorderColor());
                canvas.drawRect(rect.right, rect.top, rect.right + 1, rect.bottom, paint);
            }
        }        
        
        // draw bottom border
        if(rect.bottom > sheetView.getColumnHeaderHeight())
        {
            if((colorIndex = BottomBorder(cell)) > -1)
            {
                paint.setColor(book.getColor(colorIndex));
                canvas.drawRect(rect.left, rect.bottom, rect.right, rect.bottom + 1, paint);
            }
            else if(tableCellStyle != null && tableCellStyle.getBorderColor() != null)
            {
                paint.setColor(tableCellStyle.getBorderColor());
                canvas.drawRect(rect.left, rect.bottom, rect.right, rect.bottom + 1, paint);
            }
        }        

        paint.setColor(oldColor);
        canvas.restore();      
    } 
   
    /**
     * 
     * @param cell
     * @return left border color index
     */
    private int LeftBorder(Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        Sheet sheet = sheetView.getCurrentSheet();  
        if (cell.getRangeAddressIndex() >= 0)
        {
            CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
            Cell tempCell = sheet.getRow(cr.getFirstRow()).getCell(cr.getFirstColumn());
            if(tempCell != null)
            {
                style = tempCell.getCellStyle();
                cell = tempCell;
            }
        }
        
        boolean hasLeft = false;
        int color = -1;
        if(style != null && style.getBorderLeft() > CellStyle.BORDER_NONE)
        {
            hasLeft = true;
            color = style.getBorderLeftColorIdx();
        }
        else
        {
            Cell tempCell = sheet.getRowByColumnsStyle(cell.getRowNumber()).getCell(cell.getColNumber() - 1);
            if(tempCell != null)
            {
                style = tempCell.getCellStyle();
                if (tempCell.getRangeAddressIndex() >= 0)
                {
                    CellRangeAddress cr = sheet.getMergeRange(tempCell.getRangeAddressIndex());
                    tempCell = sheet.getRow(cr.getLastRow()).getCell(cr.getLastColumn());
                    if(tempCell != null)
                    {
                        style = tempCell.getCellStyle();
                    }
                } 
                if(style != null && style.getBorderRight() > CellStyle.BORDER_NONE)
                {
                    hasLeft = true;
                    color = style.getBorderRightColorIdx();
                }
            }
        }
        
        
        if(hasLeft && cell.getExpandedRangeAddressIndex() >= 0)
        {
            CellRangeAddress cr = sheet.getRow(cell.getRowNumber()).getExpandedRangeAddress(cell.getExpandedRangeAddressIndex()).getRangedAddress();
            if(cell.getColNumber() != cr.getFirstColumn())
            {
                hasLeft = false;
            }
        }
        
        if(hasLeft)
        {
            return color;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * 
     * @param cell
     * @return right border color index
     */
    private int RightBorder(Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        Sheet sheet = sheetView.getCurrentSheet();
        if (cell.getRangeAddressIndex() >= 0)
        {
            CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
            Cell tempCell = sheet.getRow(cr.getLastRow()).getCell(cr.getLastColumn());
            if(tempCell != null)
            {
                style = tempCell.getCellStyle();
                cell = tempCell;
            }
        } 
        boolean hasRight = false;
        int color = -1;
        if(style != null && style.getBorderRight() > CellStyle.BORDER_NONE)
        {
            hasRight = true;
            color = style.getBorderRightColorIdx();
        }
        else
        {
            Cell tempCell = sheet.getRowByColumnsStyle(cell.getRowNumber()).getCell(cell.getColNumber() + 1);
            if(tempCell != null)
            {  

                style = tempCell.getCellStyle();
                if (tempCell.getRangeAddressIndex() >= 0)
                {
                    CellRangeAddress cr = sheet.getMergeRange(tempCell.getRangeAddressIndex());
                    tempCell = sheet.getRow(cr.getFirstRow()).getCell(cr.getFirstColumn());
                    if(tempCell != null)
                    {
                        style = tempCell.getCellStyle();
                    }
                } 
                if(style != null && style.getBorderLeft() > CellStyle.BORDER_NONE)
                {
                    hasRight = true;
                    color = style.getBorderLeftColorIdx();
                }
            }
        }        
        
        if(hasRight && cell.getExpandedRangeAddressIndex() >= 0)
        {
            CellRangeAddress cr = sheet.getRow(cell.getRowNumber()).getExpandedRangeAddress(cell.getExpandedRangeAddressIndex()).getRangedAddress();
            if(cell.getColNumber() != cr.getLastColumn())
            {
                hasRight = false;
            }
        }
        
        if(hasRight)
        {
            return color;
        }
        else
        {           
            return -1;
        }
    }
    
    /**
     * 
     * @param cell
     * @return top border color index
     */
    private int TopBorder(Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        Sheet sheet = sheetView.getCurrentSheet();
        if (cell.getRangeAddressIndex() >= 0)
        {
            CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
            Cell tempCell = sheet.getRow(cr.getFirstRow()).getCell(cr.getFirstColumn());
            if(tempCell != null)
            {
                style = tempCell.getCellStyle();
                cell = tempCell;
            }
        } 
        
        if(style != null && style.getBorderTop() > CellStyle.BORDER_NONE)
        {
            return style.getBorderTopColorIdx();
        }
        
        Row topRow = sheet.getRowByColumnsStyle(cell.getRowNumber() - 1);
        if(topRow != null)
        {
            cell= topRow.getCell(cell.getColNumber());
            if(cell != null)
            {  

                style = cell.getCellStyle();
                if (cell.getRangeAddressIndex() >= 0)
                {
                    CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                    cell = sheet.getRow(cr.getLastRow()).getCell(cr.getLastColumn());
                    if(cell != null)
                    {
                        style = cell.getCellStyle();
                    }
                } 
                if(style != null && style.getBorderBottom() > CellStyle.BORDER_NONE)
                {
                    return style.getBorderBottomColorIdx();
                }
            }
        }        
        
        return -1;
    }
    
    /**
     * 
     * @param cell
     * @return bottom border color index
     */
    private int BottomBorder(Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        Sheet sheet = sheetView.getCurrentSheet();
        if (cell.getRangeAddressIndex() >= 0)
        {
            CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
            Cell tempCell = sheet.getRow(cr.getLastRow()).getCell(cr.getLastColumn());
            if(tempCell != null)
            {
                style = tempCell.getCellStyle();
                cell = tempCell;
            }
        } 
        
        if(style != null && style.getBorderBottom() > CellStyle.BORDER_NONE)
        {
            return style.getBorderBottomColorIdx();
        }
        
        Row topRow = sheet.getRowByColumnsStyle(cell.getRowNumber() + 1);
        if(topRow != null)
        {
            cell= topRow.getCell(cell.getColNumber());
            if(cell != null)
            {  

                style = cell.getCellStyle();
                if (cell.getRangeAddressIndex() >= 0)
                {
                    CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                    cell = sheet.getRow(cr.getFirstRow()).getCell(cr.getFirstColumn());
                    if(cell != null)
                    {
                        style = cell.getCellStyle();
                    }
                } 
                if(style != null && style.getBorderTop() > CellStyle.BORDER_NONE)
                {
                    return style.getBorderTopColorIdx();
                }
            }
        }        
        
        return -1;
    }
    
    /**
     * 
     * @param canvas
     * @param rect
     * @param activeCellType
     */
    public void drawActiveCellBorder(Canvas canvas, RectF rect, short activeCellType)
    {
        Rect clipBounds = canvas.getClipBounds();
        clipBounds.left = sheetView.getRowHeaderWidth();
        clipBounds.top = sheetView.getColumnHeaderHeight();
        
        canvas.save();
        canvas.clipRect(clipBounds);
        
        Paint paint = PaintKit.instance().getPaint();
        //save paint property      
        int oldColor = paint.getColor(); 
        
        paint.setColor(Color.BLACK);
        if(activeCellType == Sheet.ACTIVECELL_SINGLE && rect.left != rect.right && rect.top != rect.bottom)
        {
            //left frame
            //if(rect.left > sheetView.getRowHeaderWidth())
            {
                canvas.drawRect(rect.left - 2, rect.top - 2, rect.left + 1, rect.bottom + 2, paint);
            }
            //top
            //if(rect.top > sheetView.getColumnHeaderHeight())
            {
                canvas.drawRect(rect.left - 2, rect.top - 2, rect.right + 2, rect.top + 1, paint);
            }
            //right frame
            //if(rect.right > sheetView.getRowHeaderWidth())
            {
                canvas.drawRect(rect.right - 1, rect.top - 2, rect.right + 2, rect.bottom + 2, paint);
            }
            //bottom
            //if(rect.bottom > sheetView.getColumnHeaderHeight())
            {
                canvas.drawRect(rect.left - 2, rect.bottom - 1, rect.right + 2, rect.bottom + 2, paint);
            }
        }
        else if(activeCellType == Sheet.ACTIVECELL_ROW && rect.top != rect.bottom)
        {
            //top
            canvas.drawRect(clipBounds.left - 2, rect.top - 2, clipBounds.right + 10, rect.top + 1, paint);
            
            //bottom
            canvas.drawRect(clipBounds.left - 2, rect.bottom - 1, clipBounds.right + 10, rect.bottom + 2, paint);
        }
        else if(activeCellType == Sheet.ACTIVECELL_COLUMN && rect.left != rect.right)
        {
            //left frame
            canvas.drawRect(rect.left - 2, clipBounds.top - 2, rect.left + 1, clipBounds.bottom + 2, paint);
            
            //right frame
            canvas.drawRect(rect.right - 1, clipBounds.top - 2, rect.right + 2, clipBounds.bottom + 2, paint);
        }

        paint.setColor(oldColor);
        canvas.restore();
    }
    
    public void dispose()
    {
        sheetView = null;
    }
    
    // 当前显示Sheet
    private SheetView sheetView;
}
