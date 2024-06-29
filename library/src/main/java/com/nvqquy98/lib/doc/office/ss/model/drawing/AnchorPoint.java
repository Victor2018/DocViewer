/*
 * 文件名称:          CellAnchor.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:32:00
 */
package com.nvqquy98.lib.doc.office.ss.model.drawing;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-29
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class AnchorPoint
{

    public AnchorPoint()
    {        
    }
    
    public AnchorPoint(short col, int row, int dx, int dy)
    {        
        this.row = row;
        this.col = col;
        this.dx = dx;
        this.dy = dy;
    }
    
    /**
     * 
     * @param row
     */
    public void setRow(int row)
    {
        this.row = row;
    }
    
    /**
     * 
     * @return
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * 
     * @param col
     */
    public void setColumn(short col)
    {
        this.col = col;
    }
    
    /**
     * 
     * @return
     */
    public short getColumn()
    {
        return col;
    }
    
    /**
     * 
     * @param dx
     */
    public void setDX(int dx)
    {
        this.dx = dx;
    }
    
    /**
     * 
     * @return
     */
    public int getDX()
    {
        return dx;
    }
    
    public void setDY(int dy)
    {
        this.dy = dy;
    }
    
    /**
     * 
     * @return
     */
    public int getDY()
    {
        return dy;
    }
    
    public void dispose()
    {
        
    }
    
    //
    protected int row;
    //
    protected short col;
    //
    protected int dx;
    //
    protected int dy;
}
