/*
 * 文件名称:          TableCellStyle.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:30:16
 */
package com.nvqquy98.lib.doc.office.ss.model.table;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-18
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SSTableCellStyle
{
    public SSTableCellStyle(int fillColor)
    {
        this.fillColor = fillColor;
    }
    
    public int getFontColor()
    {
        return fontColor;
    }


    public void setFontColor(int fontColor)
    {
        this.fontColor = fontColor;
    }
    
    public Integer getBorderColor()
    {
        return borderColor;
    }
    public void setBorderColor(int borderColor)
    {
        this.borderColor = borderColor;
    }
    public Integer getFillColor()
    {
        return fillColor;
    }
    public void setFillColor(int fillColor)
    {
        this.fillColor = fillColor;
    }
    
    private int fontColor = 0xFF000000;
    //left, right, top, bottom
    private Integer borderColor;
    //background
    private Integer fillColor;
}
