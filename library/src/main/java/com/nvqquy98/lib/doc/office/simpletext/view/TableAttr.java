/*
 * 文件名称:          TableAttr.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:38:31
 */
package com.nvqquy98.lib.doc.office.simpletext.view;

/**
 * table attribute
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-21
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class TableAttr
{

    // 上边距
    public int topMargin;
    // 左边距
    public int leftMargin;
    // 右边距
    public int rightMargin;
    // 下边距
    public int bottomMargin;
    // cell宽度
    public int cellWidth;
    // cell vertical align
    public byte cellVerticalAlign;
    // cell background
    public int cellBackground;
    
    /**
     * 
     */
    public void reset()
    {
        topMargin = 0;
        leftMargin = 0;
        rightMargin = 0;
        bottomMargin = 0;
        cellVerticalAlign = 0;
        cellBackground = -1;
    }
    
    
}
