/*
 * 文件名称:          AbstrctElement.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:46:46
 */
package com.nvqquy98.lib.doc.office.simpletext.model;


/**
 * 元素的抽象类
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
public abstract class AbstractElement implements IElement
{
    /**
     * 
     */
    public AbstractElement()
    {
        attr = new AttributeSetImpl();
    }
    
    /**
     * 
     */
    public short getType()
    {
        return -1;
    }
    
    /**
     * 
     *
     */
    public void setStartOffset(long start)
    {
        this.start = start;
    }

    /**
     * 
     *
     */
    public long getStartOffset()
    {
        return start;
    }

    /**
     * 
     *
     */
    public void setEndOffset(long end)
    {
        this.end = end;
    }

    /**
     * 
     *
     */
    public long getEndOffset()
    {
        return this.end;
    }

    /**
     * 
     */
    public void setAttribute(IAttributeSet attrSet)
    {
        this.attr = attrSet;
    }
    /**
     * 
     *
     */
    public IAttributeSet getAttribute()
    {
        return this.attr;
    }

    /**
     * 
     *
     */
    public String getText(IDocument doc)
    {
        return null;
    }
    
    /**
     * 
     *
     */
    public String toString()
    {
        return "[" + start + ", " + end  + "]：" + getText(null);
    }
        
    /**
     * 
     */
    public void dispose()
    {
        if (attr != null)
        {
            attr.dispose();
            attr = null;
        }
    }
    //
    protected long start;
    //
    protected long end;
    // 属性集
    protected IAttributeSet attr;
}
