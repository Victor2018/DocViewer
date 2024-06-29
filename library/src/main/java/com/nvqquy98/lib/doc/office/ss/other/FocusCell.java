/*
 * 文件名称:          HeaderInformation.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:39:22
 */
package com.nvqquy98.lib.doc.office.ss.other;

import android.graphics.Rect;

/**
 * TODO: cell include header cell  and sheet cell, used when changing row or column size
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-13
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class FocusCell
{
    public final static short UNKNOWN = 0;
    
    public final static short ROWHEADER = UNKNOWN + 1;
    
    public final static short COLUMNHEADER = ROWHEADER + 1;
    
    public final static short CELL = COLUMNHEADER + 1;
    
        
    public FocusCell()
    {
        
    }
    
    public FocusCell(short type, Rect area, int row, int col)
    {
        this.headerType = type;
        this.area = area;
        if(type == ROWHEADER)
        {
            this.row = row;
        }
        else if(type == COLUMNHEADER)
        {
            this.col = col;
        }
    }
    
    public FocusCell clone()
    {
        Rect rect = new Rect(area);
        return new FocusCell(headerType, rect, row, col);
    }
    
    /**
     * 
     * @param type 
     * ROWHEADER
     * COLUMNHEADER
     */
    public void setType(short type)
    {
        headerType = type;        
    }
    
    /**
     * 
     * @return
     * * ROWHEADER
     * COLUMNHEADER
     */
    public int getType()
    {
        return headerType;        
    }
    
    
    
    /**
     * set row index
     * @param row
     */
    public void setRow(int row)
    {
        if(headerType == ROWHEADER || headerType == CELL)
        {
            this.row = row;
        }
        
    }
    
    /**
     * get row index
     * @return
     */
    public int getRow()
    {
        if(headerType == ROWHEADER || headerType == CELL)
        {
            return row;
        }
        return -1;
    }
    
    /**
     * set column index
     * @param col
     */
    public void setColumn(int col)
    {
        if(headerType == COLUMNHEADER || headerType == CELL)
        {
            this.col = col;
        }
        
    }
    
    /**
     * get column index
     * @return
     */
    public int getColumn()
    {
        if(headerType == COLUMNHEADER || headerType == CELL)
        {
            return col;
        }
        return -1;
    }
    
    public void setRect(Rect area)
    {
        this.area = area;
    }
    
    public Rect getRect()
    {
        return area;
    }
    
    public void dispose()
    {
    }
    
    //
    private short headerType = UNKNOWN;
    //row or column index
    private int row;
    private int col;
    
    private Rect area;
}
