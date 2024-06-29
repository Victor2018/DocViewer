/*
 * 文件名称:          WPDocument.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:56:40
 */

package com.nvqquy98.lib.doc.office.wp.model;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.ElementCollectionImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.model.STDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;


/**
 * word model
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
public class WPDocument extends STDocument
{

    public WPDocument()
    {
        root = new ElementCollectionImpl[6];
        para = new ElementCollectionImpl[6];
        table = new ElementCollectionImpl[4];
        //
        initRoot();
    }

    /**
     * 
     */
    private void initRoot()
    {
        // 正文
        root[0] = new ElementCollectionImpl(5);
        // 页眉
        root[1] = new ElementCollectionImpl(3);
        // 页脚
        root[2] = new ElementCollectionImpl(3);
        // 脚注
        root[3] = new ElementCollectionImpl(5);
        // 尾注
        root[4] = new ElementCollectionImpl(5);
        // 文本框
        root[5] = new ElementCollectionImpl(5);
        
        // 正文段落
        para[0] = new ElementCollectionImpl(100);
        // 页眉段落
        para[1] = new ElementCollectionImpl(3);
        // 页脚段落
        para[2] = new ElementCollectionImpl(3);
        // 脚注段落
        para[3] = new ElementCollectionImpl(5);
        // 尾注段落
        para[4] = new ElementCollectionImpl(5);
        // 文本框段落
        para[5] = new ElementCollectionImpl(5);
        
        // 正文
        table[0] = new ElementCollectionImpl(5);
        // 页眉
        table[1] = new ElementCollectionImpl(5);
        // 页脚
        table[2] = new ElementCollectionImpl(5);
        // 文本框
        table[3] = new ElementCollectionImpl(5);
    }

    /**
     * 得到章节元素
     */
    public IElement getSection(long offset)
    {
        return root[0].getElement(offset);
    }
    
    /**
     * 
     */
    public void appendSection(IElement elem)
    {
        root[0].addElement(elem);
    }
    
    /**
     * 
     */
    public void appendElement(IElement elem, long offset)
    {
        if (elem.getType() == WPModelConstant.PARAGRAPH_ELEMENT)
        {
            appendParagraph(elem, offset);
        }
        ElementCollectionImpl collection = getRootCollection(offset);
        if (collection != null)
        {
            collection.addElement(elem);
        }
    }
    
    /**
     * 得到页眉、页脚元素
     * @param area 区域
     * @param type Element类型，首页、奇数页、偶数页
     */    
    public IElement getHFElement(long offset, byte type)
    {
        ElementCollectionImpl collection = getRootCollection(offset);
        if (collection != null)
        {
            return collection.getElement(offset);
        }
        return null;
    }
    
    /**
     * 得到脚注、尾注元素
     * @param area 区域
     */
    public IElement getFEElement(long offset)
    {
        /*IElementCollection collection = getParaCollection(offset);
        if (collection != null)
        {
            return collection.getElement(offset);
        }*/
        return null;
    }
    
    /**
     * 得到段落元素 
     */
    public IElement getParagraph(long offset)
    {
        // 文本框
        if ((offset & WPModelConstant.AREA_MASK) == WPModelConstant.TEXTBOX)
        {
            IElement e = getTextboxSectionElement(offset);
            if (e != null)
            {
                return ((SectionElement)e).getParaCollection().getElement(offset);
            }
        }
        ElementCollectionImpl collection = getParaCollection(offset);
        if (collection != null)
        {
            return collection.getElement(offset);
        }
        return null;
    }
    
    /**
     * 得到最顶层，如果指定office是自由表格，则返回TableElement
     */
    public IElement getParagraph0(long offset)
    {
        IElement elem = getParagraph(offset);
        if (AttrManage.instance().getParaLevel(elem.getAttribute()) >= 0)
        {
            ElementCollectionImpl collection = getTableCollection(offset);
            if (collection != null)
            {
                return collection.getElement(offset);
            }
        }
        return elem;
    }
    
    /**
     * 
     */
    public IElement getParagraphForIndex(int index, long area)
    {
        // 文本框
        if ((area & WPModelConstant.AREA_MASK)== WPModelConstant.TEXTBOX)
        {
            IElement e = getTextboxSectionElement(area);
            if (e != null)
            {
                return ((SectionElement)e).getParaCollection().getElementForIndex(index);
            }
        }
        ElementCollectionImpl collection = getParaCollection(area);
        if (collection != null)
        {
            return collection.getElementForIndex(index);
        }
        return null;
    }
    
    /**
     * 添加段落
     * @param sectionElement    章节元素
     * @param paraElement       段落无素       
     */
    public void appendParagraph(IElement element, long area)
    {
        if (element.getType() == WPModelConstant.TABLE_ELEMENT)
        {
            ElementCollectionImpl collection = getTableCollection(area);
            if (collection != null)
            {
                collection.addElement(element);
            }
            return;
        }
        // 文本框
        if ((area & WPModelConstant.AREA_MASK) == WPModelConstant.TEXTBOX)
        {
            IElement e = getTextboxSectionElement(area);
            if (e != null)
            {
                ((SectionElement)e).appendParagraph(element, area);
                return;
            }
        }
        ElementCollectionImpl collection = getParaCollection(area);
        if (collection != null)
        {
            collection.addElement(element);
        }
    }
    
    /**
     * get 段落总数
     */
    public int getParaCount(long area)
    {
        // 文本框
        if ((area & WPModelConstant.AREA_MASK) == WPModelConstant.TEXTBOX)
        {
            IElement e = getTextboxSectionElement(area);
            if (e != null)
            {
                return ((SectionElement)e).getParaCollection().size();
            }
        }
        ElementCollectionImpl collection = getParaCollection(area);
        if (collection != null)
        {
            return collection.size();
        }
        return 0;
    }
    
    /**
     * 
     * @return
     */
    private ElementCollectionImpl getRootCollection(long offset)
    {
        long area = offset & WPModelConstant.AREA_MASK;        
        // 正文
        if (area == WPModelConstant.MAIN)
        {
            return root[0];
        }
        // 页眉
        else if (area == WPModelConstant.HEADER)
        {
            return root[1];
        }
        // 页脚
        else if (area == WPModelConstant.FOOTER)
        {
            return root[2];
        }
        // 脚注
        else if (area == WPModelConstant.FOOTNOTE)
        {
            return root[3];
        }
        // 尾注
        else if(area == WPModelConstant.ENDNOTE)
        {
            return root[4];
        }
        // 文本框
        else if (area == WPModelConstant.TEXTBOX)
        {
            return root[5];
        }
        return null;
    }
    
    /**
     * 
     * @return
     */
    public ElementCollectionImpl getParaCollection(long offset)
    {
        long area = offset & WPModelConstant.AREA_MASK;        
        // 正文
        if (area == WPModelConstant.MAIN)
        {
            return para[0];
        }
        // 页眉
        else if (area == WPModelConstant.HEADER)
        {
            return para[1];
        }
        // 页脚
        else if (area == WPModelConstant.FOOTER)
        {
            return para[2];
        }
        // 脚注
        else if (area == WPModelConstant.FOOTNOTE)
        {
            return para[3];
        }
        // 尾注
        else if(area == WPModelConstant.ENDNOTE)
        {
            return para[4];
        }
        // 文本框
        else if (area == WPModelConstant.TEXTBOX)
        {
            return para[5];
        }
        return null;
    }
    
    /**
     * 
     * @return
     */
    public ElementCollectionImpl getTableCollection(long offset)
    {
        long area = offset & WPModelConstant.AREA_MASK;        
        // 正文
        if (area == WPModelConstant.MAIN)
        {
            return table[0];
        }
        // 页眉
        else if (area == WPModelConstant.HEADER)
        {
            return table[1];
        }
        // 页脚
        else if (area == WPModelConstant.FOOTER)
        {
            return table[2];
        }
        // 脚注
        /*else if (area == WPModelConstant.FOOTNOTE)
        {
            return table[3];
        }
        // 尾注
        else if(area == WPModelConstant.ENDNOTE)
        {
            return table[4];
        }*/
        // 文本框
        else if (area == WPModelConstant.TEXTBOX)
        {
            return table[3];
        }
        return null;
    }

    /**
     * 得到区域文本长度
     */
    public long getLength(long offset)
    {
        ElementCollectionImpl root = getRootCollection(offset);
        if (root != null)
        {
            // 文本框
            if ((offset & WPModelConstant.AREA_MASK) == WPModelConstant.TEXTBOX)
            {
                IElement e = getTextboxSectionElement(offset);
                if (e != null)
                {
                    return e.getEndOffset() - e.getStartOffset();
                }
            }
            return root.getElementForIndex(root.size() - 1).getEndOffset() - 
            root.getElementForIndex(0).getStartOffset();
        }
        return 0;
    }
    
    /**
     * 
     * @param offset
     * @return
     */
    private IElement getTextboxSectionElement(long offset)
    {
        long index = (offset & WPModelConstant.TEXTBOX_MASK) >> 32;
        if (root[5] != null)
        {
            return root[5].getElementForIndex((int)index);
        }
        return null;
    }
    
    /**
     * 
     * @param offset
     * @return
     */
    public IElement getTextboxSectionElementForIndex(int index)
    {
        if (root[5] != null)
        {
            return root[5].getElementForIndex((int)index);
        }
        return null;
    }
    
    
    public void setPageBackground(BackgroundAndFill pageBG)
    {
    	this.pageBG = pageBG;
    }
    
    public BackgroundAndFill getPageBackground()
    {
    	return pageBG;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        //
        if (root != null)
        {
            for (int i = 0; i < root.length; i++)
            {
                root[i].dispose();
                root[i] = null;
            }
            root = null;
        }
        //
        if (para != null)
        {
            for (int i = 0; i < para.length; i++)
            {
                para[i].dispose();
                para[i] = null;
            }
            para = null;
        }
        //
        if (table != null)
        {
            for (int i = 0; i < table.length; i++)
            {
                table[i].dispose();
                table[i] = null;
            }
            table = null;
        }
    }
    
    
    /*
     * root[0]，正文
     * root[1]，页眉
     * root[2]，页脚
     * root[3]，脚注
     * root[4]，尾注
     * root[5]，文本框
     */
    private ElementCollectionImpl root[];
    /*
     * para[0]，正文段落
     * para[1]，页眉段落
     * para[2]，页脚段落
     * para[3]，脚注段落
     * para[4]，尾注段落
     * para[5]，文本框段落    
     */
    private ElementCollectionImpl para[];
    /*
     * 
     */
    private ElementCollectionImpl table[];
    
    private BackgroundAndFill pageBG;
}
