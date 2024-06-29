/*
 * 文件名称:          MinRowAndColumnInformation.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:22:31
 */
package com.nvqquy98.lib.doc.office.ss.other;

import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.PaneInformation;


/**
 * min row and column information of current sheet after scrolling
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-27
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SheetScroller
{
    public void reset()
    {
        setMinRowIndex(0);
        setMinColumnIndex(0);        

        setRowHeight(0);
        setColumnWidth(0);
        
        setVisibleRowHeight(0);
        setVisibleColumnWidth(0);
        
        setRowAllVisible(true);
        setColumnAllVisible(true);
    }
    
    public void update(Sheet sheet, int scrollX, int scrollY)
    {
        reset();
        
        setVisibleRowHeight(scrollY);
        setVisibleColumnWidth(scrollX);
        
        PaneInformation paneInfo = sheet.getPaneInformation();
        if(paneInfo != null)
        {
            setMinRowIndex(paneInfo.getHorizontalSplitTopRow());
            setMinColumnIndex(paneInfo.getVerticalSplitLeftColumn());
        }
        
        int maxSheetRows = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXROW_03 : Workbook.MAXROW_07; 
        int maxSheetColumns = sheet.getWorkbook().isBefore07Version() ? Workbook.MAXCOLUMN_03 : Workbook.MAXCOLUMN_07;
        Row row;
        if(scrollY > 0)
        {
        	int firstRow = sheet.getFirstRowNum();
        	int lastRow = sheet.getLastRowNum();
        	int defaultRowHeight = sheet.getDefaultRowHeight();
        	
        	//update row information
            while (visibleRowHeight >= 1 && minRowIndex <= maxSheetRows) 
            {
            	if(minRowIndex >= firstRow && minRowIndex <= lastRow)
            	{
            		row = sheet.getRow(minRowIndex);
            	}
            	else
            	{
            		row = null;
            	}
                            
                if(row == null || (row != null && !row.isZeroHeight()))
                {
                	rowHeight = (row == null ? defaultRowHeight : row.getRowPixelHeight());
                	visibleRowHeight = visibleRowHeight - rowHeight;
                }
                minRowIndex++;
            }
            
            if(minRowIndex != maxSheetRows)
            {
            	minRowIndex--; 
                setVisibleRowHeight(Math.abs(getVisibleRowHeight()));
                if(getVisibleRowHeight() < 1)
                {
                	minRowIndex++;
                    setVisibleRowHeight(0);
                }
                else
                {
                    setRowAllVisible(false);
                }
            }
            else
            {
            	minRowIndex--;
            	row = sheet.getRow(minRowIndex);
            	while(row != null && row.isZeroHeight())
            	{
            		minRowIndex--;
                	row = sheet.getRow(getMinRowIndex());
            	}
            	
            	setVisibleRowHeight(0);
            }
        }
        
        if(scrollX > 0)
        {
            //update column information
            while (visibleColumnWidth >= 1 && minColumnIndex <= maxSheetColumns) 
            {
                if(!sheet.isColumnHidden(minColumnIndex))
                {
                	columnWidth = sheet.getColumnPixelWidth(minColumnIndex);
                    visibleColumnWidth -= columnWidth;
                }
                minColumnIndex++;
             }
            
            if(minColumnIndex != maxSheetColumns)
            {
            	minColumnIndex--;            
                setVisibleColumnWidth(Math.abs(getVisibleColumnWidth()));
                if(getVisibleColumnWidth() < 1)
                {
                	minColumnIndex++;
                    setVisibleColumnWidth(0);
                }
                else
                {
                    setColumnAllVisible(false);
                }
            }
            else
            {
            	minColumnIndex--;  
            	while(sheet.isColumnHidden(minColumnIndex))
            	{
            		minColumnIndex--;
            	}
                setVisibleColumnWidth(0);
            }
        }
    }
    
    /**
     * @return Returns the minRowIndex.
     */
    public int getMinRowIndex()
    {
        return minRowIndex;
    }

    /**
     * @param minRowIndex The minRowIndex to set.
     */
    public void setMinRowIndex(int minRowIndex)
    {
        this.minRowIndex = minRowIndex;
    }

    /**
     * 
     */
    public void dispose()
    {
        
    }
    
    /**
     * @return Returns the minColumnIndex.
     */
    public int getMinColumnIndex()
    {
        return minColumnIndex;
    }

    /**
     * @param minColumnIndex The minColumnIndex to set.
     */
    public void setMinColumnIndex(int minColumnIndex)
    {
        this.minColumnIndex = minColumnIndex;
    }

    /**
     * @return Returns the columnWidth.
     */
    public float getColumnWidth()
    {
        return columnWidth;
    }

    /**
     * @param columnWidth The columnWidth to set.
     */
    public void setColumnWidth(float columnWidth)
    {
        this.columnWidth = columnWidth;
    }

    /**
     * @return Returns the rowHeight.
     */
    public float getRowHeight()
    {
        return rowHeight;
    }

    /**
     * @param rowHeight The rowHeight to set.
     */
    public void setRowHeight(float rowHeight)
    {
        this.rowHeight = rowHeight;
    }

    /**
     * @return Returns the isRowAllVisible.
     */
    public boolean isRowAllVisible()
    {
        return isRowAllVisible;
    }

    /**
     * @param isRowAllVisible The isRowAllVisible to set.
     */
    public void setRowAllVisible(boolean isRowAllVisible)
    {
        this.isRowAllVisible = isRowAllVisible;
    }

    /**
     * @return Returns the isColumnAllVisible.
     */
    public boolean isColumnAllVisible()
    {
        return isColumnAllVisible;
    }

    /**
     * @param isColumnAllVisible The isColumnAllVisible to set.
     */
    public void setColumnAllVisible(boolean isColumnAllVisible)
    {
        this.isColumnAllVisible = isColumnAllVisible;
    }

    /**
     * @return Returns the visibleRowHeight.
     */
    public double getVisibleRowHeight()
    {
        return visibleRowHeight;
    }

    /**
     * @param visibleRowHeight The visibleRowHeight to set.
     */
    public void setVisibleRowHeight(double visibleRowHeight)
    {
        this.visibleRowHeight = visibleRowHeight;
    }

    /**
     * @return Returns the visibleColumnWidth.
     */
    public double getVisibleColumnWidth()
    {
        return visibleColumnWidth;
    }

    /**
     * @param visibleColumnWidth The visibleColumnWidth to set.
     */
    public void setVisibleColumnWidth(double visibleColumnWidth)
    {
        this.visibleColumnWidth = visibleColumnWidth;
    }

    //current min row and column index
    private int minRowIndex;
    private int minColumnIndex;
    
    //current min row height and column width(zoom = 1)
    private float columnWidth;
    private float rowHeight;
    
    private boolean isRowAllVisible = true;
    private boolean isColumnAllVisible = true;
    
    //current min visible row height and visible column width(zoom = 1)
    private double visibleRowHeight;
    private double visibleColumnWidth;
}
