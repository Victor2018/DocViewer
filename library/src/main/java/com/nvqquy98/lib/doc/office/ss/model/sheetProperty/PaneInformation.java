/*
 * 文件名称:          PanInformation.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:49:30
 */

package com.nvqquy98.lib.doc.office.ss.model.sheetProperty;

/**
 * 窗口冻结
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
public class PaneInformation
{
    /** Constant for active pane being the lower right*/
    public static final byte PANE_LOWER_RIGHT = (byte)0;
    /** Constant for active pane being the upper right*/
    public static final byte PANE_UPPER_RIGHT = (byte)1;
    /** Constant for active pane being the lower left*/
    public static final byte PANE_LOWER_LEFT = (byte)2;
    /** Constant for active pane being the upper left*/
    public static final byte PANE_UPPER_LEFT = (byte)3;

    private short topRow;
    private short leftColumn;
    private boolean frozen = true;

    public PaneInformation()
    {
        
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param top
     * @param left
     * @param active
     * @param frozen
     */
    public PaneInformation(short top, short left, boolean frozen)
    {
        this.topRow = top;
        this.leftColumn = left;
        this.frozen = frozen;
    }

    public void setHorizontalSplitTopRow(short topRow)
    {
        this.topRow = topRow;
    }
    
    /**
     * For a horizontal split returns the top row in the BOTTOM pane.
     * @return 0 if there is no horizontal split, or the top row of the bottom pane.
     */
    public short getHorizontalSplitTopRow()
    {
        return topRow;
    }

    public void setVerticalSplitLeftColumn(short leftColumn)
    {
        this.leftColumn = leftColumn;
    }
    
    /**
     * For a vertical split returns the left column in the RIGHT pane.
     * @return 0 if there is no vertical split, or the left column in the RIGHT pane.
     */
    public short getVerticalSplitLeftColumn()
    {
        return leftColumn;
    }
    
    public void setFreePane(boolean frozen)
    {
        this.frozen = frozen;
    }
    
    /** Returns true if this is a Freeze pane, false if it is a split pane.
     */
    public boolean isFreezePane()
    {
        return frozen;
    }
}
