/*
 * 文件名称:          ParagraphElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:03:17
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

/**
 * 段落元素
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
public class ParagraphElement extends AbstractElement
{

    /**
     * 
     */
    public ParagraphElement()
    {   
        super();
        leaf = new ElementCollectionImpl(10);
    }
    
    /**
     * 
     *
     */
    public String getText(IDocument doc)
    {
        int count = leaf.size();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            text.append(leaf.getElementForIndex(i).getText(null));
        }
        return text.toString();
    }
    
    /**
     * 
     */
    public void appendLeaf(LeafElement leafElem)
    {
        leaf.addElement(leafElem);
    }
    
    /**
     * 
     */
    public IElement getLeaf(long offset)
    {
        return leaf.getElement(offset);
    }
    
    /**
     * 得到指定index的Offset
     */
    public IElement getElementForIndex(int index)
    {
        return leaf.getElementForIndex(index);
    }
    
    /**
     *
     *
     */
    public void dispose()
    {
        super.dispose();
        if (leaf != null)
        {
            leaf.dispose();
            leaf = null;
        }
    }
    
    //
    private ElementCollectionImpl leaf;
    //
}
