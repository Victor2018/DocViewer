/*
 * 文件名称:          CellRangeAddress.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:49:30
 */
package com.nvqquy98.lib.doc.office.ss.model;

/**
 * 合并单元
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-21
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class CellRangeAddress
{
    /**
     * 
     * @param firstRow
     * @param firstCol
     * @param lastRow
     * @param lastCol
     */
    public CellRangeAddress(int firstRow, int firstCol, int lastRow, int lastCol)
    {
        this.firstRow = firstRow;
        this.firstCol = firstCol;
        this.lastRow = lastRow;
        this.lastCol = lastCol;
    }
    
    /**
     * @return Returns the firstRow.
     */
    public int getFirstRow()
    {
        return firstRow;
    }
    /**
     * @param firstRow The firstRow to set.
     */
    public void setFirstRow(int firstRow)
    {
        this.firstRow = firstRow;
    }

    /**
     * @return Returns the firstCol.
     */
    public int getFirstColumn()
    {
        return firstCol;
    }

    /**
     * @param firstCol The firstCol to set.
     */
    public void setFirstColumn(int firstCol)
    {
        this.firstCol = firstCol;
    }

    /**
     * @return Returns the lastRow.
     */
    public int getLastRow()
    {
        return lastRow;
    }

    /**
     * @param lastRow The lastRow to set.
     */
    public void setLastRow(int lastRow)
    {
        this.lastRow = lastRow;
    }

    /**
     * @return Returns the lastCol.
     */
    public int getLastColumn()
    {
        return lastCol;
    }

    /**
     * @param lastCol The lastCol to set.
     */
    public void setLastColumn(int lastCol)
    {
        this.lastCol = lastCol;
    }
    
    /**
     * 
     * @param rowInd
     * @param colInd
     * @return
     */
    public boolean isInRange(int rowInd, int colInd)
    {
        return firstRow <= rowInd  &&  rowInd <= lastRow &&
                firstCol <= colInd && colInd <= lastCol;
    }

    public void dispose()
    {
        
    }
    
    private int firstRow;
    private int firstCol;
    private int lastRow;
    private int lastCol;

}
