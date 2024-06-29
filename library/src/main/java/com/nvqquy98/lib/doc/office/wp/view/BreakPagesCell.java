/*
 * 文件名称:          BreakPagesCell.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:36:30
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.wp.model.CellElement;

/**
 * 布局过程记录跨页的单元格
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-6-11
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class BreakPagesCell
{

    public BreakPagesCell(CellElement cell, long breakOffset)
    {
        this.cell = cell;
        this.breakOffset = breakOffset;
    }
    
    
    /**
     * 
     */
    public CellElement getCell()
    {
        return cell;
    }
    
    /**
     * 
     */
    public long getBreakOffset()
    {
        return breakOffset;
    }
    
    // across patges cell;
    private CellElement cell;
    // across pages offset
    private long breakOffset;
    
}
