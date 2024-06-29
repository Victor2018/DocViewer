/*
 * 文件名称:          CellAnchor.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:26:18
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
 * 日期:            2012-3-1
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class CellAnchor
{
    public final static short ONECELLANCHOR = 0x0;
    
    public final static short TWOCELLANCHOR = 0x1;
    
    
    public CellAnchor(short type)
    {
        this.type = type;
    }    
    
    public short getType()
    {
        return type;
    }
    
    public void setStart(AnchorPoint start)
    {
        this.start = start;
    }
    
    public AnchorPoint getStart()
    {
        return start;
    }
    
    public void setEnd(AnchorPoint end)
    {
        this.end = end;
    }
    
    public AnchorPoint getEnd()
    {
        return end;
    }
    
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void dispose()
    {
        if(start != null)
        {
            start.dispose();
            start = null;
        }
        
        if(end != null)
        {
            end.dispose();
            end = null;
        }
    }
    
    private short type = TWOCELLANCHOR;
    
    protected AnchorPoint start;
    private AnchorPoint end;
    
    //
    private int width;
    private int height;
    
}
