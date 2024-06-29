/*
 * 文件名称:          STDocument.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:55:26
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;

/**
 * 简单文本Model
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
public class STDocument implements IDocument
{
    /**
     * 
     */
    public STDocument()
    {
        
    }
    
    /**
     * 得到指定的offset的区域
     * @see com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant
     */
    public long getArea(long offset)
    {
        return offset & WPModelConstant.AREA_MASK;
    }

    /**
     * 得到区域开始位置
     * @see com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant
     */
    public long getAreaStart(long offset)
    {
        long area = offset & WPModelConstant.AREA_MASK;
        // 文本框需要特殊处理
        if (area == WPModelConstant.TEXTBOX)
        {
            return offset & WPModelConstant.TEXTBOX_MASK;
        }
        return area;
    }

    /**
     * 得到区域结束位置
     * @see com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant
     */
    public long getAreaEnd(long offset)
    {
        return getAreaStart(offset) + getLength(offset);
    }

    /**
     * 得到区域文本长度
     */
    public long getLength(long offset)
    {
        return section.getEndOffset() - section.getStartOffset();
    }

    /**
     * 得到章节元素
     */
    public IElement getSection(long offset)
    {
        return section;
    }
    
    /**
     * 
     */
    public void appendSection(IElement elem)
    {
        section = (SectionElement)elem;
    }
    
    /**
     * 
     */
    public void appendElement(IElement elem, long offset)
    {
        
    }
    
    /**
     * 得到页眉、页脚元素
     * @param area 区域
     * @param type Element类型，首页、奇数页、偶数页
     */    
    public IElement getHFElement(long area, byte type)
    {
        return null;
    }
    
    /**
     * 得到脚注、尾注元素
     * @param area 区域
     */
    public IElement getFEElement(long offset)
    {
        return null;
    }

    /**
     * 得到段落元素 
     */
    public IElement getParagraph(long offset)
    {
        return section.getParaCollection().getElement(offset);
    }
    
    /**
     * 
     */
    public IElement getParagraphForIndex(int index, long area)
    {
        return section.getParaCollection().getElementForIndex(index);
    }
    
    /**
     * 添加段落
     * @param sectionElement    章节元素
     * @param paraElement       段落无素       
     */
    public void appendParagraph(IElement element, long offset)
    {
        section.appendParagraph(element, offset);
    }
    
    /**
     * 插入文本
     * 
     * @param str
     * @param attr
     * @param offset
     */
    public void insertString(String str, IAttributeSet attr, long offset)
    {
        
    }

    /**
     * 
     *
     */
    public IElement getLeaf(long offset)
    {
        IElement para = getParagraph(offset);
        if (para != null)
        {
            // leaf的offset的是相对于段落开始位置的
            return ((ParagraphElement)para).getLeaf(offset);
        }
        return null;
    }

    /**
     * 
     *
     */
    public void setSectionAttr(long start, int len, IAttributeSet attr)
    {
        section.getAttribute().mergeAttribute(attr);        
    }

    /**
     * 
     */
    public void setParagraphAttr(long start, int len, IAttributeSet attr)
    {
        long end = start + len;
        //
        while (start < end)
        {
            IElement para = section.getParaCollection().getElement(start);
            para.getAttribute().mergeAttribute(attr);
            start = para.getEndOffset();
        }
    }

    /**
     * 
     */
    public void setLeafAttr(long start, int len, IAttributeSet attr)
    {
        long end = start + len;
        //
        while (start < end)
        {
            IElement leaf = getLeaf(start);
            leaf.getAttribute().mergeAttribute(attr);
            start = leaf.getEndOffset();
        }
    } 
    
    /**
     * get 段落总数
     */
    public int getParaCount(long area)
    {
        return section.getParaCollection().size();
    }
    
    /**
     * 
     */
    public String getText(long start, long end)
    {
        String str = "";
        long len = end - start;
        if (len == 0 || getArea(start) != getArea(end))
        {
            return str;
        }
        IElement leaf = getLeaf(start);
        String t = leaf.getText(null);
        int sIndex = (int)(start - leaf.getStartOffset());
        int eIndex = (int)(end >= leaf.getEndOffset() ? t.length() : end - leaf.getStartOffset());
        str = t.substring(sIndex, eIndex);
        //
        start = leaf.getEndOffset();
        while (start < end)
        {
            leaf = getLeaf(start);
            t = leaf.getText(null);
            eIndex = (int)(end >= leaf.getEndOffset() ? t.length() : end - leaf.getStartOffset());
            str = t.substring(0, eIndex);            
            start = leaf.getEndOffset();
        }
        return str;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (section != null)
        {
            section.dispose();
            section = null;
        }
    }
    
    // 简单文本只有一个section
    private SectionElement section;
}
