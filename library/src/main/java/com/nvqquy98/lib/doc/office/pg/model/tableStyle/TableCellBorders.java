/*
 * 文件名称:          TableCellBorders.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:25:43
 */
package com.nvqquy98.lib.doc.office.pg.model.tableStyle;

import com.nvqquy98.lib.doc.office.fc.dom4j.Element;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-3-22
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class TableCellBorders
{
    
    public Element getLeftBorder()
    {
        return left;
    }
    public void setLeftBorder(Element left)
    {
        this.left = left;
    }
    public Element getTopBorder()
    {
        return top;
    }
    public void setTopBorder(Element top)
    {
        this.top = top;
    }
    public Element getRightBorder()
    {
        return right;
    }
    public void setRightBorder(Element right)
    {
        this.right = right;
    }
    public Element getBottomBorder()
    {
        return bottom;
    }
    public void setBottomBorder(Element bottom)
    {
        this.bottom = bottom;
    }
    
    private Element left;
    private Element top;
    private Element right;
    private Element bottom;
}
