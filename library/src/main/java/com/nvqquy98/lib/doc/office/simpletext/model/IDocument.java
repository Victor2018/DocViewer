/*
 * 文件名称:          IDocument.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:49:19
 */
package com.nvqquy98.lib.doc.office.simpletext.model;


/**
 * 文本model接口
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
public interface IDocument
{
    
    /**
     * 得到指定的offset的区域
     * @see com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant
     */
    public long getArea(long offset);
    
    /**
     * 得到区域开始位置
     * @see com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant
     */
    public long getAreaStart(long offset);
    
    /**
     * 得到区域结束位置
     * @see com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant
     */
    public long getAreaEnd(long offset);
    
    /**
     * 得到区域文本长度
     */
    public long getLength(long offset);
    
    /**
     * 得到章节元素
     */
    public IElement getSection(long offset);    
    
    /**
     * 得到段落元素 
     */
    public IElement getParagraph(long offset);
    /**
     * 
     */
    public IElement getParagraphForIndex(int index, long area);
    
    /**
     * 添加段落
     * @param sectionElement    章节元素
     * @param paraElement       段落无素       
     */
    public void appendParagraph(IElement element, long offset);
    
    /**
     * 
     */
    public void appendSection(IElement elem);
    
    /**
     * 
     */
    public void appendElement(IElement elem, long offset);
    
    /**
     * 得到leaf元素
     */
    public IElement getLeaf(long offset);
    
    /**
     * 得到页眉、页脚元素
     * @param area 区域
     * @param type Element类型，首页、奇数页、偶数页
     */    
    public IElement getHFElement(long area, byte type);
    
    /**
     * 得到脚注、尾注元素
     * @param area 区域
     */
    public IElement getFEElement(long offset);
    
    /**
     * 插入文本
     * 
     * @param str
     * @param attr
     * @param offset
     */
    public void insertString(String str, IAttributeSet attr, long offset);
    
    /**
     * 设置章节属性
     * @param start 开始Offset
     * @param len   长度
     * @param attr  属性集
     */
    public void setSectionAttr(long start, int len, IAttributeSet attr);
    
    /**
     * 设置段落属性
     * 
     * @param start 开始Offset
     * @param len   长度
     * @param attr  属性集
     */
    public void setParagraphAttr(long start, int len, IAttributeSet attr);
    
    /**
     * 设置leaf属性
     * @param start 开始Offset
     * @param len   长度
     * @param attr  属性集
     */
    public void setLeafAttr(long start, int len, IAttributeSet attr);
    
    /**
     * get 段落总数
     */
    public int getParaCount(long area);
    
    /**
     * get 字符串
     */
    public String getText(long start, long end);
    
    /**
     * 
     */
    public void dispose();
    
}
