/*
 * 文件名称:          TableStyle.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:06:41
 */
package com.nvqquy98.lib.doc.office.ss.model.table;

import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-17
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SSTable
{

    public CellRangeAddress getTableReference()
    {
        return ref;
    }
    public void setTableReference(CellRangeAddress ref)
    {
        this.ref = ref;
    }
    
    public boolean isHeaderRowShown()
    {
        return headerRowShown;
    }
    public void setHeaderRowShown(boolean headerRowShown)
    {
        this.headerRowShown = headerRowShown;
    }
    public boolean isTotalRowShown()
    {
        return totalRowShown;
    }
    public void setTotalRowShown(boolean totalRowShown)
    {
        this.totalRowShown = totalRowShown;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public boolean isShowFirstColumn()
    {
        return showFirstColumn;
    }
    public void setShowFirstColumn(boolean showFirstColumn)
    {
        this.showFirstColumn = showFirstColumn;
    }
    public boolean isShowLastColumn()
    {
        return showLastColumn;
    }
    public void setShowLastColumn(boolean showLastColumn)
    {
        this.showLastColumn = showLastColumn;
    }
    public boolean isShowRowStripes()
    {
        return showRowStripes;
    }
    public void setShowRowStripes(boolean showRowStripes)
    {
        this.showRowStripes = showRowStripes;
    }
    public boolean isShowColumnStripes()
    {
        return showColumnStripes;
    }
    public void setShowColumnStripes(boolean showColumnStripes)
    {
        this.showColumnStripes = showColumnStripes;
    }
    
    public int getTableBorderDxfId()
    {
		return tableBorderDxfId;
	}
	public void setTableBorderDxfId(int tableBorderDxfId)
	{
		this.tableBorderDxfId = tableBorderDxfId;
	}
	public int getHeaderRowDxfId() 
	{
		return headerRowDxfId;
	}
	public void setHeaderRowDxfId(int headerRowDxfId) 
	{
		this.headerRowDxfId = headerRowDxfId;
	}
	public int getHeaderRowBorderDxfId() 
	{
		return headerRowBorderDxfId;
	}
	public void setHeaderRowBorderDxfId(int headerRowBorderDxfId) 
	{
		this.headerRowBorderDxfId = headerRowBorderDxfId;
	}
	public int getTotalsRowDxfId() 
	{
		return totalsRowDxfId;
	}
	public void setTotalsRowDxfId(int totalsRowDxfId) 
	{
		this.totalsRowDxfId = totalsRowDxfId;
	}
	public int getTotalsRowBorderDxfId()
	{
		return totalsRowBorderDxfId;
	}
	public void setTotalsRowBorderDxfId(int totalsRowBorderDxfId)
	{
		this.totalsRowBorderDxfId = totalsRowBorderDxfId;
	}
	
    private CellRangeAddress ref;
    private boolean headerRowShown = true;
    private boolean totalRowShown = false;
    
    //table style information
    private String name;
    private boolean showFirstColumn = false;
    private boolean showLastColumn = false;
    private boolean showRowStripes = false;
    private boolean showColumnStripes = false;

	private int tableBorderDxfId = -1;
    private int headerRowDxfId = -1;
    private int headerRowBorderDxfId = -1;
    
    private int totalsRowDxfId = -1;
    private int totalsRowBorderDxfId = -1;
}
