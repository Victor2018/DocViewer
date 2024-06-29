/*
 * 文件名称:          PDFOutlineItem.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:40:25
 */

package com.nvqquy98.lib.doc.office.fc.pdf;

/**
 * PDF document outline information
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PDFOutlineItem
{
    /**
     * 
     * @param _level
     * @param _title
     * @param _page
     */
    public PDFOutlineItem(int _level, String _title, int pageNumber)
    {
        level = _level;
        title = _title;
        this.pageNumber = pageNumber;
    }
    
    
    /**
     * @return Returns the level.
     */
    public int getLevel()
    {
        return level;
    }


    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * @return Returns the pageNumber.
     */
    public int getPageNumber()
    {
        return pageNumber;
    }


    //
    private final int level;
    private final String title;
    private final int pageNumber;
}
