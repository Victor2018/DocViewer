/*
 * 文件名称:          MergedCell.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:09:52
 */
package com.nvqquy98.lib.doc.office.ss.other;

/**
 * TODO: 这个类主要管理合并单元格完全不可见部分的宽度和高度，配合ScrolledCell类决定了当前合并单元格的可见部分
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-12
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class MergeCell
{  
    public void reset()
    {
        setWidth(0);
        setHeight(0);
        setFrozenRow(false);
        setFrozenColumn(false);
        setNovisibleWidth(0);
        setNoVisibleHeight(0);
    }
    
    /**
     * @return Returns the width.
     */
    public float getWidth()
    {
        return width;
    }

    /**
     * @param width The width to set.
     */
    public void setWidth(float width)
    {
        this.width = width;
    }

    /**
     * @return Returns the height.
     */
    public float getHeight()
    {
        return height;
    }

    /**
     * @param height The height to set.
     */
    public void setHeight(float height)
    {
        this.height = height;
    }

    /**
     * 
     */
    public void dispose()
    {
        
    }
    
    /**
     * @return Returns the frozenRow.
     */
    public boolean isFrozenRow()
    {
        return frozenRow;
    }

    /**
     * @param frozenRow The frozenRow to set.
     */
    public void setFrozenRow(boolean frozenRow)
    {
        this.frozenRow = frozenRow;
    }

    /**
     * @return Returns the frozenColumn.
     */
    public boolean isFrozenColumn()
    {
        return frozenColumn;
    }

    /**
     * @param frozenColumn The frozenColumn to set.
     */
    public void setFrozenColumn(boolean frozenColumn)
    {
        this.frozenColumn = frozenColumn;
    }

    /**
     * @return Returns the novisibleWidth.
     */
    public float getNovisibleWidth()
    {
        return novisibleWidth;
    }

    /**
     * @param novisibleWidth The novisibleWidth to set.
     */
    public void setNovisibleWidth(float novisibleWidth)
    {
        this.novisibleWidth = novisibleWidth;
    }

    /**
     * @return Returns the noVisibleHeight.
     */
    public float getNoVisibleHeight()
    {
        return noVisibleHeight;
    }

    /**
     * @param noVisibleHeight The noVisibleHeight to set.
     */
    public void setNoVisibleHeight(float noVisibleHeight)
    {
        this.noVisibleHeight = noVisibleHeight;
    }

    private float width;
    private float height;
    
    private boolean frozenRow;
    private boolean frozenColumn;
    //frozen cell:   no visible width in the right and no visible height in the bottom
    //free cell:     no vislble width in the left and no visible height in the top
    // 合并单元格不可见单元格宽度
    private float novisibleWidth;
    // 合并单元格不可见单元格高度
    private float noVisibleHeight;
    
}
