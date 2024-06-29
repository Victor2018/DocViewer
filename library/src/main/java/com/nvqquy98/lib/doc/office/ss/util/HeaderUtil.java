/*
 * 文件名称:          ColumnUtil.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:02:37
 */
package com.nvqquy98.lib.doc.office.ss.util;

import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-27
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class HeaderUtil
{

    //
    private static HeaderUtil util = new HeaderUtil();
    //
    public static HeaderUtil instance()
    {
        return util;
    }
    
    
    /**
     * 得到列标题显示文本
     */
    public  String getColumnHeaderTextByIndex(int index)
    {
        String result = "";
        for (; index >= 0; index = index / 26 - 1) {
            result = (char)((char)(index%26)+'A') + result;
        }
        return result;
    }
    
    public  int getColumnHeaderIndexByText(String text)
    {
        int index = 0;
        for(int i =  0; i < text.length(); i++)
        {
            index = index * 26 + (text.charAt(i) - 'A') + 1;
        }
        return index - 1;
    }
    
    public boolean isActiveRow(Sheet sheet, int row)
    {
        if(sheet.getActiveCellType() == Sheet.ACTIVECELL_COLUMN)
        {
            return true;
        }
        else if(sheet.getActiveCellType() == Sheet.ACTIVECELL_ROW)
        {
            if(sheet.getActiveCellRow() == row)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            boolean active = false;        
            Cell cell = sheet.getActiveCell();
            if(cell != null && cell.getRangeAddressIndex() >= 0)
            {
                CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                if(cr.getFirstRow() <= row && cr.getLastRow() >= row)
                {
                    active = true;
                }
            }
            else if(sheet.getActiveCellRow() == row)
            {
                active = true;
            }

            return active;
        }
        
        
    }
    
    public boolean isActiveColumn(Sheet sheet, int col)
    {
        if(sheet.getActiveCellType() == Sheet.ACTIVECELL_ROW)
        {
            return true;
        }
        else if(sheet.getActiveCellType() == Sheet.ACTIVECELL_COLUMN)
        {
            if(sheet.getActiveCellColumn() == col)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            boolean active = false;
            Cell cell = sheet.getActiveCell();
            if(cell != null && cell.getRangeAddressIndex() >= 0)
            {
                CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                if(cr.getFirstColumn() <= col && cr.getLastColumn() >= col)
                {
                    active = true;
                }
            }
            else if(sheet.getActiveCellColumn() == col)
            {
                active = true;
            }
            
            return active;
        }
        
    }
}
