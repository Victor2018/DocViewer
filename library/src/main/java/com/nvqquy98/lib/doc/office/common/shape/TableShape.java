/*
 * 文件名称:           Table.java
 *  
 * 编译器:             android2.2
 * 时间:               下午5:11:07
 */
package com.nvqquy98.lib.doc.office.common.shape;

/**
 * Represents a table
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-4-11
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class TableShape extends AbstractShape
{
    /**
     * 
     * @param numrows
     * @param numcols
     */
    public TableShape(int numrows, int numcols)
    {
        if (numrows < 1)
            throw new IllegalArgumentException("The number of rows must be greater than 1");
        if (numcols < 1)
            throw new IllegalArgumentException("The number of columns must be greater than 1");

        this.rowCnt = numrows;
        this.colCnt = numcols;
        
        cells = new TableCell[numrows * numcols];
    }
    
    /**
     * get type of this shape
     */
    public short getType()
    {
        return SHAPE_TABLE;
    }
    
    /**
     * get a table cell
     * @param row
     * @param column
     * @return
     */
    public TableCell getCell(int index)
    {
        if (index >= cells.length)
        {
            return null;
        }
        return cells[index];
    }
    
    /**
     * set a table cell
     * @param row
     * @param column
     * @param tableCell
     */
    public void addCell(int index, TableCell tableCell)
    {
        cells[index] = tableCell;
    }

    /**
     * 
     * @return
     */
    public int getCellCount()
    {
        return cells.length;
    }
    
    /**
     * 
     * @param shape07
     */
    public void setTable07(boolean shape07)
    {
        this.shape07 = shape07;
    }
    
    /**
     * 
     * @return
     */
    public boolean isTable07()
    {
        return shape07;
    }
    
    public boolean isFirstRow()
    {
        return firstRow;
    }

    public void setFirstRow(boolean firstRow)
    {
        this.firstRow = firstRow;
    }

    public boolean isLastRow()
    {
        return lastRow;
    }

    public void setLastRow(boolean lastRow)
    {
        this.lastRow = lastRow;
    }

    public boolean isFirstCol()
    {
        return firstCol;
    }

    public void setFirstCol(boolean firstCol)
    {
        this.firstCol = firstCol;
    }

    public boolean isLastCol()
    {
        return lastCol;
    }

    public void setLastCol(boolean lastCol)
    {
        this.lastCol = lastCol;
    }

    public boolean isBandRow()
    {
        return bandRow;
    }

    public void setBandRow(boolean bandRow)
    {
        this.bandRow = bandRow;
    }

    public boolean isBandCol()
    {
        return bandCol;
    }

    public void setBandCol(boolean bandCol)
    {
        this.bandCol = bandCol;
    }
    
    public int getRowCount()
    {
        return rowCnt;
    }

    public void setRowCount(int rowCnt)
    {
        this.rowCnt = rowCnt;
    }

    public int getColumnCount()
    {
        return colCnt;
    }

    public void setColumnCount(int colCnt)
    {
        this.colCnt = colCnt;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (cells != null)
        {
            for (int i = 0; i < cells.length; i++)
            {
                TableCell cell = cells[i];
                if (cell != null)
                {
                    cell.dispose();
                }
            }
            cells = null;
        }
    }

    //
    private TableCell[] cells;


    private int rowCnt;
    private int colCnt;
    // autoShape is 07 or 03
    private boolean shape07 = true;
    //table property
    private boolean firstRow = false;    
    private boolean lastRow = false;
    private boolean firstCol = false;
    private boolean lastCol = false;
    private boolean bandRow = false;
    private boolean bandCol = false;
}
