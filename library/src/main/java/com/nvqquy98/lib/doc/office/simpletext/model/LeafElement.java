/*
 * 文件名称:          LeafElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午9:00:44
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;

/**
 * Leaf Element
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-28
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class LeafElement extends AbstractElement
{
    /**
     * 
     */
    public LeafElement(String text)
    {
        super();
        this.text = text;
    }
    
    /**
     * 
     */
    public short getType()
    {
        return  WPModelConstant.LEAF_ELEMENT;
    }


    /**
     * 
     */
    public String getText(IDocument doc)
    {
        return text;
    }
    
    /**
     * 
     */
    public void setText(String text)
    {
        this.text = text;
        this.setEndOffset(getStartOffset() + text.length());
    }

    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        text = null;
    }
    
    /**
     * 
     */
    
    //
    private String text;
}
