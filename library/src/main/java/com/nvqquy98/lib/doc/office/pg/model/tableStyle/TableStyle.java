/*
 * 文件名称:          TableStyle.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:36:41
 */
package com.nvqquy98.lib.doc.office.pg.model.tableStyle;

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
public class TableStyle
{

    public TableCellStyle getWholeTable()
    {
        return wholeTable;
    }
    public void setWholeTable(TableCellStyle wholeTable)
    {
        this.wholeTable = wholeTable;
    }
    public TableCellStyle getBand1H()
    {
        return band1H;
    }
    public void setBand1H(TableCellStyle band1h)
    {
        band1H = band1h;
    }
    public TableCellStyle getBand2H()
    {
        return band2H;
    }
    public void setBand2H(TableCellStyle band2h)
    {
        band2H = band2h;
    }
    public TableCellStyle getBand1V()
    {
        return band1V;
    }
    public void setBand1V(TableCellStyle band1v)
    {
        band1V = band1v;
    }
    public TableCellStyle getBand2V()
    {
        return band2V;
    }
    public void setBand2V(TableCellStyle band2v)
    {
        band2V = band2v;
    }
    public TableCellStyle getFirstCol()
    {
        return firstCol;
    }
    public void setFirstCol(TableCellStyle firstCol)
    {
        this.firstCol = firstCol;
    }
    public TableCellStyle getLastCol()
    {
        return lastCol;
    }
    public void setLastCol(TableCellStyle lastCol)
    {
        this.lastCol = lastCol;
    }
    public TableCellStyle getFirstRow()
    {
        return firstRow;
    }
    public void setFirstRow(TableCellStyle firstRow)
    {
        this.firstRow = firstRow;
    }
    public TableCellStyle getLastRow()
    {
        return lastRow;
    }
    public void setLastRow(TableCellStyle lastRow)
    {
        this.lastRow = lastRow;
    }
    
    public void dispose()
    {
        if(wholeTable != null)
        {
            wholeTable.dispose();
            wholeTable = null;
        }
        
        if(band1H != null)
        {
            band1H.dispose();
            band1H = null;
        }
        
        if(band2H != null)
        {
            band2H.dispose();
            band2H = null;
        }
        
        if(band1V != null)
        {
            band1V.dispose();
            band1V = null;
        }
        
        if(band2V != null)
        {
            band2V.dispose();
            band2V = null;
        }
        
        if(firstCol != null)
        {
            firstCol.dispose();
            firstCol = null;
        }
        
        if(lastCol != null)
        {
            lastCol.dispose();
            lastCol = null;
        }
        
        if(firstRow != null)
        {
            firstRow.dispose();
            firstRow = null;
        }
        
        if(lastRow != null)
        {
            lastRow.dispose();
            lastRow = null;
        }
    }
    private TableCellStyle wholeTable;
    private TableCellStyle band1H;
    private TableCellStyle band2H;
    private TableCellStyle band1V;
    private TableCellStyle band2V;
    private TableCellStyle firstCol;
    private TableCellStyle lastCol;
    private TableCellStyle firstRow;
    private TableCellStyle lastRow;
}
