/*
 * 文件名称:          CellElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:52:18
 */
package com.nvqquy98.lib.doc.office.wp.model;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.AbstractElement;

/**
 * table cell element
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
public class CellElement extends AbstractElement
{
    /**
     * 
     */
    public CellElement()
    {
        super();
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPModelConstant.TABLE_CELL_ELEMENT;
    }
}
