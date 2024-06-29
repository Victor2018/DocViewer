/*
 * 文件名称:          CellInformation.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:51:12
 */
package com.nvqquy98.lib.doc.office.ss.other;

/**
 * TODO: 这个类主要是管理单个Cell（或者合并单元格中的其中一个单元格）的信息，滚动后可见部分的宽和高
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-28
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class DrawingCell
{
    public void reset()
    {
        setRowIndex(0);
        setColumnIndex(0);
        setLeft(0);
        setTop(0);
        setWidth(0);
        setHeight(0);
        setVisibleWidth(0);
        setVisibleHeight(0);
    }
    
    
    
    /**
     * @return Returns the rowIndex.
     */
    public int getRowIndex()
    {
        return rowIndex;
    }

    /**
     * @param rowIndex The rowIndex to set.
     */
    public void setRowIndex(int rowIndex)
    {
        this.rowIndex = rowIndex;
    }

    /**
     * 
     */
    public void increaseRow()
    {
        rowIndex = rowIndex + 1;
    }
    
    /**
     * @param columnIndex The columnIndex to set.
     */
    public void increaseColumn()
    {
        columnIndex = columnIndex + 1;
    }
    
    /**
     * 
     */
    public int getColumnIndex()
    {
        return columnIndex;
    }

    /**
     * @param columnIndex The columnIndex to set.
     */
    public void setColumnIndex(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }

    /**
     * @return Returns the left.
     */
    public float getLeft()
    {
        return left;
    }

    /**
     * @param left The left to set.
     */
    public void setLeft(float left)
    {
        this.left = left;
    }

    public void increaseLeftWithVisibleWidth()
    {
        left = left + visibleWidth;
    }
    
    /**
     * @return Returns the top.
     */
    public float getTop()
    {
        return top;
    }

    /**
     * @param top The top to set.
     */
    public void setTop(float top)
    {
        this.top = top;
    }

    public void increaseTopWithVisibleHeight()
    {
        top = top + visibleHeight;
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
     * @return Returns the visibleWidth.
     */
    public float getVisibleWidth()
    {
        return visibleWidth;
    }

    /**
     * @param visibleWidth The visibleWidth to set.
     */
    public void setVisibleWidth(float visibleWidth)
    {
        this.visibleWidth = visibleWidth;
    }

    /**
     * @return Returns the visibleHeight.
     */
    public float getVisibleHeight()
    {
        return visibleHeight;
    }

    /**
     * @param visibleHeight The visibleHeight to set.
     */
    public void setVisibleHeight(float visibleHeight)
    {
        this.visibleHeight = visibleHeight;
    }
    
    public void dispose()
    {
        
    }
    //
    private int rowIndex;
    private int columnIndex;
    //left top postion
    private float left;
    private float top;
    
    private float width;
    private float height;
    //
    private float visibleWidth;
    private float visibleHeight;
}
