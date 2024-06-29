/*
 * 文件名称:          Style.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:14:40
 */
package com.nvqquy98.lib.doc.office.simpletext.model;


/**
 * 样式
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-27
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class Style
{

    public Style()
    {
        attr = new AttributeSetImpl();
    }
    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return Returns the baseID.
     */
    public int getBaseID()
    {
        return baseID;
    }

    /**
     * @param baseID The baseID to set.
     */
    public void setBaseID(int baseID)
    {
        this.baseID = baseID;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the type.
     */
    public byte getType()
    {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(byte type)
    {
        this.type = type;
    }

    /**
     * @return Returns the attr.
     */
    public IAttributeSet getAttrbuteSet()
    {
        return attr;
    }

    /**
     * @param attr The attr to set.
     */
    public void setAttrbuteSet(IAttributeSet attr)
    {
        this.attr = attr;
    }
    
    public void dispose()
    {
        name = null;
        if (attr != null)
        {
            attr.dispose();
            attr = null;
        }
    }

    // style id
    private int id = -1;
    // style base id;
    private int baseID = -1;
    // style name
    private String name;
    // = 0 paragraph; = 1 character 
    private byte type;
    // attribute set 
    private IAttributeSet attr;
    
}
