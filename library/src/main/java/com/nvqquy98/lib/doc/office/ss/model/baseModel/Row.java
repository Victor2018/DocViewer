/*
 * 文件名称:          Row.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:17:14
 */

package com.nvqquy98.lib.doc.office.ss.model.baseModel;

import java.util.Collection;
import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.other.ExpandedCellRangeAddress;


/**
 * Row of this sheet
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-16
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class Row
{

    /**
     * 构造器
     */
    public Row(int capacity)
    {
        this.lastCol = capacity;
        
        //cells table
        cells = new Hashtable<Integer,Cell>(capacity);
        
        //row propperty
        rowProp = new RowProperty();
    }
    
    public void setSheet(Sheet sheet)
    {
        this.sheet = sheet;
    }
    
    /**
     * Get the cell representing a given column (logical cell)
     * 0-based. If you ask for a cell that is not defined, then
     * you get a null.
     * This is the basic call, with no policies applied
     *
     * @param cellIndex  0 based column number
     * @param style  whether create a cell by row&column style when called cell is null 
     * @return Cell representing that column or null if undefined.
     */
    private Cell retrieveCell(int cellIndex, boolean style)
    {
        try
        {
            if (cellIndex < 0 )
            {
                return null;
            }
            
            Cell cell = cells.get(cellIndex);        
            if(cell == null && style)
            {
                //first check row default style
                cell = createCellByStyle(styleIndex, cellIndex);
                if( cell == null)
                {
                    //then check column default style
                    cell = createCellByStyle(sheet.getColumnStyle(cellIndex), cellIndex);
                }
            }
            
            return cell; 
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private Cell createCellByStyle(int styleIndex, int column)
    {
        Cell cell = null;
        CellStyle cellStyle = sheet.getWorkbook().getCellStyle(styleIndex);
        if(cellStyle != null 
            && ((cellStyle.getFillPatternType() == BackgroundAndFill.FILL_SOLID && (cellStyle.getFgColor() & 0xFFFFFF) != 0xFFFFFF)
                || cellStyle.getBorderLeft() > 0
                || cellStyle.getBorderTop() > 0
                || cellStyle.getBorderRight() > 0
                || cellStyle.getBorderBottom() > 0))
        {
            cell = new Cell(Cell.CELL_TYPE_NUMERIC);
            cell.setColNumber(column);
            cell.setRowNumber(rowNumber);
            cell.setCellStyle(styleIndex);
            cell.setSheet(sheet);
            
            cells.put(column, cell);
        }
        
        return cell;
    }
    
    /**
     * 
     * @param cellnum
     * @return
     */
    public Cell getCell(int cellnum)
    {
        return retrieveCell(cellnum, true);
    }

    /**
     * Get the cell representing a given column (logical cell)
     *  0-based.  If you ask for a cell that is not defined, then
     *  your supplied policy says what to do
     *
     * @param cellnum  0 based column number
     */
    public Cell getCell(int cellnum, boolean style)
    {
        return retrieveCell(cellnum, style);
    }
    
    /**
     * 
     * @return
     */
    public Collection<Cell> cellCollection()
    {
        return cells.values();
    }
    
    /**
     * add Cell
     */
    public void addCell(Cell cell)
    {
        int column = cell.getColNumber();   
        cells.put(column, cell);

        // fix up firstCol and lastCol indexes
        firstCol = Math.min(firstCol, column);
        lastCol = Math.max(lastCol, column + 1);
    }

    /**
     * @return Returns the firstCol.
     */
    public int getFirstCol()
    {
        return firstCol;
    }

    /**
     * @param firstCol The firstCol to set.
     */
    public void setFirstCol(int firstCol)
    {
        this.firstCol = firstCol;
    }

    /**
     * @return Returns the lastCol.
     */
    public int getLastCol()
    {
        return lastCol;
    }

    /**
     * @param lastCol The lastCol to set.
     */
    public void setLastCol(int lastCol)
    {
        this.lastCol = lastCol;
    }

    /**
     * @return Returns the styleIndex.
     */
    public int getRowStyle()
    {
        return styleIndex;
    }

    /**
     * @param styleIndex The styleIndex to set.
     */
    public void setRowStyle(int styleIndex)
    {
        this.styleIndex = styleIndex;
    }
    
    /**
     * @return Returns the rowPixelHeight.
     */
    public float getRowPixelHeight()
    {
        return rowPixelHeight;
    }

    /**
     * @param rowPixelHeight The rowPixelHeight to set.
     */
    public void setRowPixelHeight(float rowPixelHeight)
    {
        this.rowPixelHeight = rowPixelHeight;
    }

    /**
     * @return Returns the rowNumber.
     */
    public int getRowNumber()
    {
        return rowNumber;
    }

    /**
     * @param rowNumber The rowNumber to set.
     */
    public void setRowNumber(int rowNumber)
    {
        this.rowNumber = rowNumber;
    }

    /**
     * @return Returns the isEmpty.
     */
    public boolean isEmpty()
    {
        return cells.size() == 0;
    }

    /**
     * @return Returns the zeroHeight.
     */
    public boolean isZeroHeight()
    {
        return rowProp.isZeroHeight();
    }

    /**
     * @param zeroHeight The zeroHeight to set.
     */
    public void setZeroHeight(boolean zeroHeight)
    {
        rowProp.setRowProperty(RowProperty.ROWPROPID_ZEROHEIGHT, zeroHeight);
    }
    
    /**
     * @return Returns the physicalNumberOfCells.
     */
    public int getPhysicalNumberOfCells()
    {
        return cells.size();
    }

    /**
     * clear all cells 
     */
    public void removeAllCells()
    {
        Collection<Cell> cellCollection = cells.values();
        for(Cell cell : cellCollection)
        {
            cell.dispose();
        }
        cells.clear();
    }
    
    /**
     * remove all cells of hidden row, except one which is the first row&col of merged cell
     */
    public void removeCellsForHiddenRow()
    {
        if(!rowProp.isZeroHeight())
        {
            return;
        }
        
        Collection<Cell> cellCollection = cells.values();
        for(Cell cell : cellCollection)
        {
            if(cell.getRangeAddressIndex() >= 0)
            {
                continue;
            }
            cell.dispose();
        }        
    }
    
    /**
     * 
     */
    public void completed()
    {
        rowProp.setRowProperty(RowProperty.ROWPROPID_COMPLETED, true);
    }
    
    /**
     * 
     * @return
     */
    public boolean isCompleted()
    {
        return rowProp.isCompleted();
    }
    
    /**
     * has checked cells whose widths extend cell itself width, so we need extend its width to layout cell contents
     * @param init
     */
    public void setInitExpandedRangeAddress(boolean init)
    {
        rowProp.setRowProperty(RowProperty.ROWPROPID_INITEXPANDEDRANGEADDR, init);
    }
    
    /**
     * 
     * @return
     */
    public boolean isInitExpandedRangeAddress()
    {
        return rowProp.isInitExpandedRangeAddr();
    }
    
    /**
     * 
     * @param index
     * @param addr
     */
    public void addExpandedRangeAddress(int index, ExpandedCellRangeAddress addr)
    {
        rowProp.setRowProperty(RowProperty.ROWPROPID_EXPANDEDRANGEADDRLIST, addr);
    }
    
    /**
     * 
     * @return
     */
    public int getExpandedCellCount()
    {
        return rowProp.getExpandedCellCount();
    }
    
    /**
     * 
     * @param index
     * @return
     */
    public ExpandedCellRangeAddress getExpandedRangeAddress(int index)
    {
        return rowProp.getExpandedCellRangeAddr(index);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        removeAllCells();

        if(rowProp != null)
        {
            rowProp.dispose();
            rowProp = null;
        }

        sheet = null;
        cells = null;
    }


    protected Sheet sheet;
    // 
    protected int firstCol;
    //
    protected int lastCol;
    //
    protected int rowNumber;
    
    //
    protected int styleIndex;
    // 像素值的行高，默认18个像素
    private float rowPixelHeight = SSConstant.DEFAULT_ROW_HEIGHT;
    
    private RowProperty rowProp;
    // 行中cell
    protected Hashtable<Integer,Cell> cells;
}
