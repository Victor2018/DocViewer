/*
 * 文件名称:          	Bookmark.java
 *  
 * 编译器:            android2.2
 * 时间:             	下午4:55:48
 */
package com.nvqquy98.lib.doc.office.common.bookmark;

/**
 * book mark
 * <p>
 * <p>
 * Read版本:        	Office engine V1.0
 * <p>
 * 作者:            	ljj8494
 * <p>
 * 日期:            	2013-5-9
 * <p>
 * 负责人:          	ljj8494
 * <p>
 * 负责小组:        	TMC
 * <p>
 * <p>
 */
public class Bookmark
{
    /**
     * 
     * @param name
     * @param start
     * @param end
     */
    public Bookmark(String name, long start, long end)
    {
        this.name = name;
        this.start = start;
        this.end = end;
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
     * @return Returns the start.
     */
    public long getStart()
    {
        return start;
    }
    /**
     * @param start The start to set.
     */
    public void setStart(long start)
    {
        this.start = start;
    }

    /**
     * @return Returns the end.
     */
    public long getEnd()
    {
        return end;
    }
    /**
     * @param end The end to set.
     */
    public void setEnd(long end)
    {
        this.end = end;
    }

    //
    private long start;
    //
    private long end;
    //
    private String name;

}
