/*
 * 文件名称:          TableCellStyle.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:32:21
 */
package com.nvqquy98.lib.doc.office.pg.model.tableStyle;

import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;

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
public class TableCellStyle
{    
    public TableCellBorders getTableCellBorders()
    {
        return cellBorders;
    }
    
    public void setTableCellBorders(TableCellBorders cellBorders)
    {
        this.cellBorders = cellBorders;
    }
    
    public Element getTableCellBgFill()
    {
        return bgFill;
    }
    
    public void setTableCellBgFill(Element bgFill)
    {
        this.bgFill = bgFill;
    }
    
    public void setFontAttributeSet(IAttributeSet fontAttr)
    {
    	this.fontAttr = fontAttr;
    }
    
    public IAttributeSet getFontAttributeSet()
    {
    	return fontAttr;
    }
    
    public void dispose()
    {
        cellBorders = null;
        bgFill = null;
        fontAttr = null;
    }
    
    private TableCellBorders cellBorders;
    private Element bgFill;
    private IAttributeSet fontAttr;
}
