/*
 * 文件名称:          IElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:06:29
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

/**
 * Model的元素，主要是章节、段落、Leaf
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-11
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IElement
{
    /**
     * 
     */
    public short getType();
    /**
     * 开始Offset
     */
    public void setStartOffset(long start);
    public long getStartOffset();
    /**
     * 结束Offset
     */
    public void setEndOffset(long end);
    public long getEndOffset();
    
    /**
     * 
     */
    public void setAttribute(IAttributeSet attrSet);
    
    /**
     * 得到属性集
     */
    public IAttributeSet getAttribute();    
    
    /**
     * 得到文本
     */
    public String getText(IDocument doc);
    
    /**
     * 
     */
    public void dispose();
    
}
