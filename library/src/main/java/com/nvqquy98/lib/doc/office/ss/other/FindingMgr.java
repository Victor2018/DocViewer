/*
 * 文件名称:          SearchCell.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:08:32
 */
package com.nvqquy98.lib.doc.office.ss.other;

import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.util.ModelUtil;


/**
 * TODO: find data which you're searching for
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-3-12
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class FindingMgr
{
    public FindingMgr()
    {
    }    
    
    /**
     * find cells which contain interesting contents
     * @param value
     * @return
     */
    public Cell findCell(Sheet sheet, String value)
    {     
        if (value == null || sheet == null)
        {
            return null;
        }
        this.sheet = sheet;
        this.value = value;
        
        String cellContent;
        if(value != null && value.length() > 0)
        {
            Row row;
            //search from current active cell
            for(int i = sheet.getActiveCellRow(); i <= sheet.getLastRowNum(); i++)
            {
                row = sheet.getRow(i);
                if(row == null)
                {
                    continue;
                }
                
                int j = (i == sheet.getActiveCellRow()) ? sheet.getActiveCellColumn() : row.getFirstCol();
                for(; j <= row.getLastCol(); j++)
                {
                    findedCell = row.getCell(j);
                    if(findedCell == null)
                    {
                        continue;
                    }
                    
                    cellContent = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), findedCell);
                    if(cellContent != null && cellContent.contains(value))
                    {
                        return findedCell;
                    }
                }
            }
            
            //reached to the end of document, then search from the begin of the document
            for(int i = sheet.getFirstRowNum(); i <= sheet.getActiveCellRow(); i++)
            {
                row = sheet.getRow(i);
                if(row == null)
                {
                    continue;
                }
                
                int j = row.getFirstCol();
                for(; j <= row.getLastCol(); j++)
                {
                    findedCell = row.getCell(j);
                    if(findedCell == null)
                    {
                        continue;
                    }
                    
                    cellContent = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), findedCell);
                    if(cellContent != null && cellContent.contains(value))
                    {
                        return findedCell;
                    }
                }
            }
        }        
        
        return null;
    }
    
    public Cell findBackward()
    {
        if(findedCell == null || value == null || sheet == null)
        {
            return null;
        }
        
        String cellContent;
        Row row;
        Cell cell;
        for(int i = findedCell.getRowNumber(); i >= sheet.getFirstRowNum(); i--)
        {
            row = sheet.getRow(i);
            if(row == null)
            {
                continue;
            }
            
            int j = (i == findedCell.getRowNumber()) ? findedCell.getColNumber() - 1 : row.getLastCol();
           
            
            for(; j >= 0; j--)
            {
                cell = row.getCell(j);
                if(cell == null)
                {
                    continue;
                }
                
                cellContent = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), cell);
                if(cellContent != null && cellContent.contains(value))
                {
                    findedCell = cell;
                    return findedCell;
                }
            }
        }           
        
        return null;
    }
    
    public Cell findForward()
    {
        if(findedCell == null || value == null || sheet == null)
        {
            return null;
        }
        
        String cellContent;
        Row row;
        Cell cell;
        for(int i = findedCell.getRowNumber(); i <= sheet.getLastRowNum(); i++)
        {
            row = sheet.getRow(i);
            if(row == null)
            {
                continue;
            }
            
            int j = (i == findedCell.getRowNumber()) ? findedCell.getColNumber() + 1 : row.getFirstCol();
           
            
            for(; j <= row.getLastCol(); j++)
            {
                cell = row.getCell(j);
                if(cell == null)
                {
                    continue;
                }
                
                cellContent = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), cell);
                if(cellContent != null && cellContent.contains(value))
                {
                    findedCell = cell;
                    return findedCell;
                }
            }
        }           
        
        return null;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        sheet = null;
        value = null;
        findedCell = null;
    }
    
    private Sheet sheet;
    //finding value
    private String value;
    
    //finded cell
    private Cell findedCell;
    
}
