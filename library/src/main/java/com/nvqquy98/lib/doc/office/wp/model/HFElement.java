/*
 * 文件名称:          HFElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:39:17
 */
package com.nvqquy98.lib.doc.office.wp.model;

import com.nvqquy98.lib.doc.office.simpletext.model.AbstractElement;

/**
 * 页眉、页脚元素
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-30
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class HFElement extends AbstractElement
{

    /**
     * 
     * @param elemType  element type
     * @param hftype    first, odd, even   
     */
    public HFElement(short elemType, byte hftype)
    {
        super();
        this.hfType = hftype;
        this.elemType = elemType;
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see com.nvqquy98.lib.doc.office.simpletext.model.AbstractElement#getType()
     *
     */
    public short getType()
    {
        return elemType;
    }
    
    /**
     * @return Returns the type.
     */
    public byte getHFType()
    {
        return hfType;
    }
    
    // first, odd, even
    private byte hfType;
    //
    private short elemType;
}
