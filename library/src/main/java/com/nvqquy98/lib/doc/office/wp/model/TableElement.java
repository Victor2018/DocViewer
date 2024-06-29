/*
 * 文件名称:          TableElement.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:37:40
 */
package com.nvqquy98.lib.doc.office.wp.model;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.ElementCollectionImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.model.LeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;

/**
 * table element
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-4-17
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class TableElement extends ParagraphElement
{

    /**
     * 
     */
    public TableElement()
    {
        super();
        rowElement = new ElementCollectionImpl(10);
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPModelConstant.TABLE_ELEMENT;
    }
    
    /**
     * 
     */
    public void appendRow(RowElement rowElem)
    {
        rowElement.addElement(rowElem);
    }
    
    /**
     * 
     */
    public IElement getRowElement(long offset)
    {
        return rowElement.getElement(offset);
    }
    
    /**
     * 得到指定index的Offset
     */
    public IElement getElementForIndex(int index)
    {
        return rowElement.getElementForIndex(index);
    }
    
    /**
     * 
     *
     */
    public String getText(IDocument doc)
    {
        return "";
    }
    
    /**
     * 
     */
    public void appendLeaf(LeafElement leafElem)
    {
    }
    
    /**
     * 
     */
    public IElement getLeaf(long offset)
    {
        return null;
    }
    
    private ElementCollectionImpl rowElement;
    
}
