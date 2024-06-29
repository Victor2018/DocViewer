/*
 * 文件名称:          CellLeafElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:43:29
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-4-27
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class CellLeafElement extends LeafElement
{

    public CellLeafElement(Cell cell, int offStart, int offEnd)
    {
        super(null);
        
        book = cell.getSheet().getWorkbook();
        
        this.sharedStringIndex = cell.getStringCellValueIndex();
        this.offStart = offStart;
        this.offEnd = offEnd;
    }
    
    /**
     * 
     */
    public String getText(IDocument doc)
    {
        if(appendNewline)
        {            
            return book.getSharedString(sharedStringIndex).substring(offStart, offEnd) + "\n";
        }
        else
        {
            return book.getSharedString(sharedStringIndex).substring(offStart, offEnd);
        }
        
    }
    
//    /**
//     * 
//     *
//     */
//    public long getEndOffset()
//    {        
//        if(appendNewline)
//        {
//            return end + 1;
//        }
//        else
//        {
//            return end;
//        }
//        
//    }
    
    /**
     * 
     */
    public void appendNewlineFlag()
    {
        appendNewline = true;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        book = null;
    }
    
    private Workbook book;
    
    private int sharedStringIndex;
    
    private int offStart;
    
    private int offEnd;
    
    private boolean appendNewline;
}
