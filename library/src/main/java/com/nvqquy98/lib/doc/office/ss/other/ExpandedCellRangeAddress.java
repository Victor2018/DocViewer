/*
 * 文件名称:          ExtendedCellRangeAddress.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:34:27
 */
package com.nvqquy98.lib.doc.office.ss.other;

import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-6-4
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ExpandedCellRangeAddress 
{
    public ExpandedCellRangeAddress(Cell expandedCell, int firstRow, int firstCol, int lastRow, int lastCol)
    {
        this.expandedCell = expandedCell;
        rangeAddr = new CellRangeAddress(firstRow, firstCol, lastRow, lastCol);
    }

    /**
     * 
     * @return
     */
    public CellRangeAddress getRangedAddress()
    {
        return rangeAddr;
    }
    
    /**
     * 
     * @return
     */
    public Cell getExpandedCell()
    {
        return expandedCell;
    }
    
    
    public void dispose()
    {
        rangeAddr = null;
        
        expandedCell = null;
        
    }
    private CellRangeAddress rangeAddr;
    
    private Cell expandedCell;
}
