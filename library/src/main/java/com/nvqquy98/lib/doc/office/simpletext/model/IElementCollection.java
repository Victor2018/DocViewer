/*
 * 文件名称:          IElementCollection.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:27:45
 */

package com.nvqquy98.lib.doc.office.simpletext.model;

/**
 * Element集合接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-29
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IElementCollection
{
    /**
     * 得到指定Office的Element
     */
    public IElement getElement(long offset);
    
    /**
     * 得到指定index的Offset
     */
    public IElement getElementForIndex(int index);
    
    /**
     * 
     */
    public int size();

    /**
     * 
     */
    public void dispose();

}
