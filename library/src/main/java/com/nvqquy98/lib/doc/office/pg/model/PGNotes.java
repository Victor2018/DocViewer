/*
 * 文件名称:          Notes.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:39:13
 */
package com.nvqquy98.lib.doc.office.pg.model;

/**
 * note data of this slide 
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-13
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PGNotes
{
    /**
     * 
     */
    public PGNotes(String notes)
    {
        this.notes = notes;
    }
    
    /**
     * 
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    
    /**
     * 
     */
    public String getNotes()
    {
        return notes;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        notes = null;
    }
    
    //
    private String notes; 
}
