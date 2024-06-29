/*
 * 文件名称:          SectionElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:03:46
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;

/**
 * 单节元素
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
public class SectionElement extends AbstractElement
{
    /**
     * 
     */
    public SectionElement()
    {
        super();
        paraCollection = new ElementCollectionImpl(10);
    }

    /**
     * 
     */
    public short getType()
    {
        return WPModelConstant.SECTION_ELEMENT;
    }
    
    /**
     * 
     * @param element
     * @param offset
     */
    public void appendParagraph(IElement element, long offset)
    {
        ((ElementCollectionImpl)paraCollection).addElement(element);
    }
    /**
     * get paragraph collection of this sectionElement
     */
    public IElementCollection getParaCollection()
    {
        return this.paraCollection;
    }
    
    /**
     * 
     *
     */
    public String getText(IDocument doc)
    {        
        int count = paraCollection.size();
        String text = "";
        for (int i = 0; i < count; i++)
        {
            text += paraCollection.getElementForIndex(i).getText(null);
        }
        return text;
    }
    
    /**
     * 
     */
    public IElement getElement(long offset)
    {
        return paraCollection.getElement(offset);
    }
    
    /**
     * 
     *
     */
    public void dispose()
    {
       super.dispose();
       if (paraCollection != null)
       {
           paraCollection.dispose();
           paraCollection = null;
       }
    }
    //
    private IElementCollection paraCollection;
}
