/*
 * 文件名称:          ModelUtil.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:23:24
 */
package com.nvqquy98.lib.doc.office.ss.util;

import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.drawing.CellAnchor;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.other.SheetScroller;
import com.nvqquy98.lib.doc.office.ss.util.format.NumericFormatter;
import com.nvqquy98.lib.doc.office.ss.view.SheetView;

import android.graphics.Rect;
import android.graphics.RectF;


/**
 * excel用到工具类
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-10
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ModelUtil
{
    //
    private static ModelUtil mu = new ModelUtil();
    
    private RectF area = new RectF();
    //
    public static ModelUtil instance()
    {
        return mu;
    }
   
    public static Rectangle processRect(Rectangle rect, float angle)
    {
    	angle = angle % 360;
    	if ((angle > 45 && angle <= 135) || (angle > 225 && angle < 315))
        {    		
   		 	double centerX = rect.getCenterX();
   		 	double centerY = rect.getCenterY();
   		 
            rect.x = (int)Math.round(centerX - rect.height / 2);
            rect.y = (int)Math.round(centerY - rect.width / 2);
            
            int temp = rect.width;
            rect.width = rect.height;
            rect.height = temp;
        } 
    	return rect;
    }
    
    /**
     * 得到合交单元的CellRangeAddress的Index
     * 
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public int getCellRangeAddressIndex(Sheet sheet, int row, int column)
    {
        int len = sheet.getMergeRangeCount();
        for (int i = 0; i < len; i++)
        {
            CellRangeAddress cra = sheet.getMergeRange(i);
            if (containsCell(cra, row, column))
            {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 得到合交单元的CellRangeAddress
     * 
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public CellRangeAddress getCellRangeAddress(Sheet sheet, int row, int column)
    {
        int len = sheet.getMergeRangeCount();
        for (int i = 0; i < len; i++)
        {
            CellRangeAddress cra = sheet.getMergeRange(i);
            if (containsCell(cra, row, column))
            {
                return cra;
            }
        }
        return null;
    }
    
    
    /**
     * 
     * @param cr
     * @param rowIx
     * @param colIx
     * @return
     */
    
    public boolean containsCell(CellRangeAddress cr, int row, int col)
    {
        return row >= cr.getFirstRow() && row <= cr.getLastRow()
            && col >= cr.getFirstColumn() && col <= cr.getLastColumn();
    }
    
    
    /**
     * 
     * @param sheet
     * @param cellRangeAddress
     * @return
     */
    public RectF getCellRangeAddressAnchor(SheetView sheetview, CellRangeAddress cellRangeAddress)
    {
        
        //left and right of rect
        area.left = getValueX(sheetview, cellRangeAddress.getFirstColumn(), 0);
        area.top = getValueY(sheetview, cellRangeAddress.getFirstRow(), 0);
        
        area.right = 
            getValueX(sheetview, cellRangeAddress.getLastColumn() + 1, 0);
        area.bottom = 
            getValueY(sheetview, cellRangeAddress.getLastRow() + 1, 0);
        
        return area;
    }
    
    /**
     * 
     * @param sheet
     * @param cellRangeAddress
     * @return
     */
    private Rect getCellRangeAddressAnchor(Sheet sheet, CellRangeAddress cellRangeAddress)
    {
        Rect area = new Rect();
        //left and right of rect
        area.left = Math.round(getValueX(sheet, cellRangeAddress.getFirstColumn(), 0));
        area.top = Math.round(getValueY(sheet, cellRangeAddress.getFirstRow(), 0));
        
        area.right = 
            Math.round(getValueX(sheet, cellRangeAddress.getLastColumn() + 1, 0));
        area.bottom = 
            Math.round(getValueY(sheet, cellRangeAddress.getLastRow() + 1, 0));
        
        return area;
    }
    
    /**
     * area based on the 0 column of the 0 row
     * @param sheet
     * @param clientAnchor
     * @return
     */
    public Rectangle getCellAnchor(Sheet sheet, CellAnchor cellAnchor)
    {
        Rectangle area = new Rectangle();
        if (cellAnchor == null)
        {
            return null;
        }
        //left and right of rect
        area.x = Math.round(getValueX(sheet, cellAnchor.getStart().getColumn(), cellAnchor.getStart().getDX()));
        area.y = Math.round(getValueY(sheet, cellAnchor.getStart().getRow(), cellAnchor.getStart().getDY()));
        
        if(cellAnchor.getType() == CellAnchor.TWOCELLANCHOR)
        {
            area.width = Math.round(getValueX(sheet, cellAnchor.getEnd().getColumn(), cellAnchor.getEnd().getDX()) - area.x); 
            area.height = Math.round(getValueY(sheet, cellAnchor.getEnd().getRow(), cellAnchor.getEnd().getDY()) - area.y);
        }
        else if(cellAnchor.getType() == CellAnchor.ONECELLANCHOR)
        {
            area.width = cellAnchor.getWidth();
            area.height = cellAnchor.getHeight();            
        }
        
        return area;
    }
    
    /**
     * 
     * @param sheet
     * @param cell
     * @return
     */
    public RectF getCellAnchor(SheetView sheetview, Cell cell)
    {
        if(cell == null)
        {
            return null;
        }
        
       
        if(cell.getRangeAddressIndex() >= 0)
        {
            return getCellRangeAddressAnchor(sheetview, sheetview.getCurrentSheet().getMergeRange(cell.getRangeAddressIndex()));
        }
        else
        {
            
            //left and right of rect
            area.left = getValueX(sheetview, cell.getColNumber(), 0);
            area.top = getValueY(sheetview, cell.getRowNumber(), 0);
            
            area.right = getValueX(sheetview, cell.getColNumber() + 1, 0);
            area.bottom = getValueY(sheetview, cell.getRowNumber() + 1, 0);
            
            
            return area;
        }
        
    }
    
    /**
     * a relatively rect area based on view left-top
     * @param sheetview
     * @param row
     * @param column
     * @return
     */
    public RectF getCellAnchor(SheetView sheetview,  int row, int column)
    {
        Sheet sheet = sheetview.getCurrentSheet();
        
        if(sheet.getRow(row) != null && sheet.getRow(row).getCell(column) != null)
        {
            Cell cell = sheet.getRow(row).getCell(column);
            if(cell.getRangeAddressIndex() >= 0)
            {
                return getCellRangeAddressAnchor(sheetview, sheet.getMergeRange(cell.getRangeAddressIndex()));
            }
        }
        
        //left and right of rect
        area.left = getValueX(sheetview, column, 0);
        area.top = getValueY(sheetview, row, 0);
        
        area.right = getValueX(sheetview, column + 1, 0);
        area.bottom = getValueY(sheetview, row + 1, 0);
        
        
        return area;
    }
    
    /**
     * a relatively rect area based on view left-top
     * @param sheetview
     * @param row
     * @param column
     * @return
     */
    public RectF getCellAnchor(SheetView sheetview,  int row, int column1, int column2)
    {
        Sheet sheet = sheetview.getCurrentSheet();
        
        if(sheet.getRow(row) != null && sheet.getRow(row).getCell(column1) != null && sheet.getRow(row).getCell(column2) != null)
        {
            Cell cell = sheet.getRow(row).getCell(column2);
            if(cell.getRangeAddressIndex() >= 0)
            {
            	column2 = sheet.getMergeRange(cell.getRangeAddressIndex()).getLastColumn();
            }
        }
        
        //left and right of rect
        area.left = getValueX(sheetview, column1, 0);
        area.top = getValueY(sheetview, row, 0);
        
        area.right = getValueX(sheetview, column2 + 1, 0);
        area.bottom = getValueY(sheetview, row + 1, 0);
        
        return area;
    }
    
    /**
     * an absolute rect area based on body left-top 
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public Rect getCellAnchor(Sheet sheet,  int row, int column)
    {
        if(sheet.getRow(row) != null && sheet.getRow(row).getCell(column) != null)
        {
            Cell cell = sheet.getRow(row).getCell(column);
            if(cell.getRangeAddressIndex() >= 0)
            {
                return getCellRangeAddressAnchor(sheet, sheet.getMergeRange(cell.getRangeAddressIndex()));
            }
        }
        
        Rect area = new Rect();
        //left and right of rect
        area.left = Math.round(getValueX(sheet, column, 0));
        area.top = Math.round(getValueY(sheet, row, 0));
        
        area.right = Math.round(getValueX(sheet, column + 1, 0));
        area.bottom = Math.round(getValueY(sheet, row + 1, 0));
        
        return area;
    }
    
    /**
     * 
     * @param sheet
     * @param row
     * @param column
     * @param ignoreMergerCell
     * @return
     */
    public Rect getCellAnchor(Sheet sheet,  int row, int column, boolean ignoreMergerCell)
    {
        if(!ignoreMergerCell && sheet.getRow(row) != null && sheet.getRow(row).getCell(column) != null)
        {
            Cell cell = sheet.getRow(row).getCell(column);
            if(cell.getRangeAddressIndex() >= 0)
            {
                return getCellRangeAddressAnchor(sheet, sheet.getMergeRange(cell.getRangeAddressIndex()));
            }
            
            return null;
        }
        else
        {
            Rect area = new Rect();
            //left and right of rect
            area.left = Math.round(getValueX(sheet, column, 0));
            area.top = Math.round(getValueY(sheet, row, 0));
            
            area.right = Math.round(getValueX(sheet, column + 1, 0));
            area.bottom = Math.round(getValueY(sheet, row + 1, 0));
            
            return area;
        }
    }
    
    /**
     * 
     * @param column
     * @param dx
     * @return
     */
    private float getValueX(Sheet sheet, int columnIndex, int dx)
    {
        float x = 0;
        
        for(int i = 0; i< columnIndex; i++)
        {
            if(sheet.isColumnHidden(i))
            {
                continue;
            }
            x += sheet.getColumnPixelWidth(i);
        }
        
        return dx + x;
    }
    
    private float getValueY(Sheet sheet, int rowIndex, int dy)
    {
      float y = 0;    
      float h = 0;
      for(int i = 0; i < rowIndex; i++)
      {
          Row row = sheet.getRow(i);
          if(row != null && row.isZeroHeight())
          {
              continue;
          }
          h = row == null ? sheet.getDefaultRowHeight() : row.getRowPixelHeight();
          y += h;
      }
      
      return y + dy;
    }
    
    public float getValueX(SheetView sheetview, int columnIndex, float dx)
    {
        float x = sheetview.getRowHeaderWidth(); 
        float w = 0;
        Sheet sheet = sheetview.getCurrentSheet();
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        int colStart = minRowAndColumnInformation.getMinColumnIndex() > 0 ? minRowAndColumnInformation.getMinColumnIndex() : 0;
        if(colStart < columnIndex && !minRowAndColumnInformation.isColumnAllVisible())
        {
            colStart += 1;
            x += (minRowAndColumnInformation.getVisibleColumnWidth() * sheetview.getZoom());
        }
        
        int maxColumns = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXCOLUMN_03 : Workbook.MAXCOLUMN_07;
        while(colStart < columnIndex && colStart <= maxColumns)
        {
            if(sheet.isColumnHidden(colStart))
            {
                colStart++;
                continue;
            }
            
            w = (sheet.getColumnPixelWidth(colStart) * sheetview.getZoom());
            
            x += w;
            colStart++;
        }
        
        return dx + x;
    }
    
    /**
     * 
     * @param rowIndex
     * @param dy
     * @return
     */
    public float getValueY(SheetView sheetview, int rowIndex, float dy)
    {
        float y = (SSConstant.DEFAULT_COLUMN_HEADER_HEIGHT * sheetview.getZoom());
        float h = 0;
        Sheet sheet = sheetview.getCurrentSheet();
        Row row;
        SheetScroller minRowAndColumnInformation = sheetview.getMinRowAndColumnInformation();
        int rowStart = minRowAndColumnInformation.getMinRowIndex() > 0 ? minRowAndColumnInformation.getMinRowIndex() : 0;
        if(rowStart < rowIndex && !minRowAndColumnInformation.isRowAllVisible())
        {
            rowStart += 1;
            y += (minRowAndColumnInformation.getVisibleRowHeight() * sheetview.getZoom());
        } 
        
        int maxRows = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXROW_03 : Workbook.MAXROW_07;
        while (rowStart < rowIndex && rowStart <= maxRows)
        {
            row = sheet.getRow(rowStart);
            
            if(row != null && row.isZeroHeight())
            {
                rowStart++;
                continue;
            }
            
            h = row == null ? sheetview.getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight();
            h = (h * sheetview.getZoom());
            
            y += h;
            rowStart++;
        }
        
        return y + dy;
    }  
    
    /**
     * 
     * @param cell
     * @return
     */
    public String getFormatContents(Workbook book, Cell cell)
    {
        //
        if(!cell.hasValidValue())
        {
            return null;
        }
        
        CellStyle style = cell.getCellStyle();
        String value = "";
        
        short numericType;
        
        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanValue()).toUpperCase();
                break;
                
            case Cell.CELL_TYPE_NUMERIC:
                String key = style.getFormatCode();
                if(key == null)
                {
                    key = "General"; 
                    numericType = Cell.CELL_TYPE_NUMERIC_GENERAL;
                }
                else
                {
                    if(cell.getCellNumericType() > 0)
                    {
                        numericType = cell.getCellNumericType();
                    }
                    else
                    {
                        numericType = NumericFormatter.instance().getNumericCellType(key);
                        cell.setCellNumericType(numericType);
                    }                    
                } 
                
                try
                {
                    if(numericType == Cell.CELL_TYPE_NUMERIC_SIMPLEDATE)
                    {
                        value = NumericFormatter.instance().getFormatContents(key, cell.getDateCellValue(book.isUsing1904DateWindowing()));
                       
                        //store string content, so no need to convert any more
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(book.addSharedString(value));
                    }
                    else
                    {
                        
                        value = NumericFormatter.instance().getFormatContents(key, cell.getNumberValue(), numericType);
                    }      
                }
                catch (Exception ex) 
                {
                    value =  String.valueOf( cell.getNumberValue());            
                }       
                break;
                
            case Cell.CELL_TYPE_STRING:
                if(cell.getStringCellValueIndex() >= 0)
                {
                    value = book.getSharedString(cell.getStringCellValueIndex());
                }
                break;
            case Cell.CELL_TYPE_FORMULA:
                break;

            case Cell.CELL_TYPE_ERROR:
                value = ErrorEval.getText(cell.getErrorValue());
                break;
            default:
                break;
        }  
        return value;
    }
}
