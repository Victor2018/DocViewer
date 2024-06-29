/*
 * 文件名称:          Hyperlink.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:12:13
 */
package com.nvqquy98.lib.doc.office.common.hyperlink;

/**
 * Hyperlink 类
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class Hyperlink
{
    // Link to a existing file or web page
    public static final int LINK_URL = 1;
    // Link to a place in this document
    public static final int LINK_DOCUMENT = 2;
    // Link to an E-mail address
    public static final int LINK_EMAIL = 3;
    // Link to a file
    public static final int LINK_FILE = 4;
    // Link to a book mark
    public static final int LINK_BOOKMARK = 5;

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
     * @return Returns the type.
     */
    public int getLinkType()
    {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setLinkType(int type)
    {
        this.type = type;
    }

    /**
     * @return Returns the address.
     */
    public String getAddress()
    {
        return address;
    }
    /**
     * @param address The address to set.
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        address = null;
        title = null;
    }

    // ID
    private int id = -1;
    // hyperlink type
    private int type;
    // hyperlink address;
    private String address;
    // hyperlink title
    private String title;
}
