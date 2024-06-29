/*
 * 文件名称:          RowElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:50:08
 */
package com.nvqquy98.lib.doc.office.wp.model;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.AbstractElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ElementCollectionImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;

/**
 * table row element
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
public class RowElement extends AbstractElement
{
    /**
     * 
     */
    public RowElement()
    {
        super();
        cellElement = new ElementCollectionImpl(10);
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPModelConstant.TABLE_ROW_ELEMENT;
    }
    
    /**
     * 
     */
    public void appendCell(CellElement cellElem)
    {
        cellElement.addElement(cellElem);
    }
    
    /**
     * 
     */
    public IElement getCellElement(long offset)
    {
        return cellElement.getElement(offset);
    }
    
    /**
     * 得到指定index的Offset
     */
    public IElement getElementForIndex(int index)
    {
        return cellElement.getElementForIndex(index);
    }
    
    /**
     * 插入Element至指定的index
     * @param element
     * @param index
     */
    public void insertElementForIndex(IElement element, int index)
    {
        cellElement.insertElementForIndex(element, index);
    }
    
    /**
     * 
     */
    public int getCellNumber()
    {
        return cellElement.size();
    }
    
    //
    private ElementCollectionImpl cellElement;
    
}
