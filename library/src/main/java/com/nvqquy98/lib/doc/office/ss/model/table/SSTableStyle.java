/*
 * 文件名称:          SSTableStyle.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:42:34
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
public class SSTableStyle
{

    public SSTableCellStyle getBand1H()
    {
        return band1H;
    }
    public void setBand1H(SSTableCellStyle band1h)
    {
        band1H = band1h;
    }
    public SSTableCellStyle getBand2H()
    {
        return band2H;
    }
    public void setBand2H(SSTableCellStyle band2h)
    {
        band2H = band2h;
    }
    public SSTableCellStyle getBand1V()
    {
        return band1V;
    }
    public void setBand1V(SSTableCellStyle band1v)
    {
        band1V = band1v;
    }
    public SSTableCellStyle getBand2V()
    {
        return band2V;
    }
    public void setBand2V(SSTableCellStyle band2v)
    {
        band2V = band2v;
    }
    public SSTableCellStyle getFirstCol()
    {
        return firstCol;
    }
    public void setFirstCol(SSTableCellStyle firstCol)
    {
        this.firstCol = firstCol;
    }
    public SSTableCellStyle getLastCol()
    {
        return lastCol;
    }
    public void setLastCol(SSTableCellStyle lastCol)
    {
        this.lastCol = lastCol;
    }
    public SSTableCellStyle getFirstRow()
    {
        return firstRow;
    }
    public void setFirstRow(SSTableCellStyle firstRow)
    {
        this.firstRow = firstRow;
    }
    public SSTableCellStyle getLastRow()
    {
        return lastRow;
    }
    public void setLastRow(SSTableCellStyle lastRow)
    {
        this.lastRow = lastRow;
    }
    
    
    private SSTableCellStyle band1H;
    private SSTableCellStyle band2H;
    private SSTableCellStyle band1V;
    private SSTableCellStyle band2V;
    private SSTableCellStyle firstCol;
    private SSTableCellStyle lastCol;
    private SSTableCellStyle firstRow;
    private SSTableCellStyle lastRow;
}
