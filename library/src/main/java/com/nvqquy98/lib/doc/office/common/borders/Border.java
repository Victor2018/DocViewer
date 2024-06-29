/*
 * 文件名称:            Borders.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:40:44
 */

package com.nvqquy98.lib.doc.office.common.borders;

/**
 * border
 * <p>
 * <p>
 * Read版本:          Office engine V1.0
 * <p>
 * 作者:              ljj8494
 * <p>
 * 日期:              2013-3-18
 * <p>
 * 负责人:             ljj8494
 * <p>
 * 负责小组:            TMC
 * <p>
 * <p>
 */

public class Border
{
    /**
     * 
     * @return
     */
    public int getColor()
    {
        return color;
    }

    /**
     * 
     */
    public void setColor(int color)
    {
        this.color = color;
    }

    /**
     * 
     * @return
     */
    public int getLineWidth()
    {
        return lineWidth;
    }

    /**
     */
    public void setLineWidth(int lineWidth)
    {
        this.lineWidth = lineWidth;
    }

    /**
     * 
     * @return
     */
    public byte getLineType()
    {
        return lineType;
    }

    /**
     * 
     */
    public void setLineType(byte lineType)
    {
        this.lineType = lineType;
    }

    /**
     * 
     * @return
     */
    public short getSpace()
    {
        return space;
    }

    /**
     */
    public void setSpace(short space)
    {
        this.space = space;
    }

    //
    private int color;
    //
    private int lineWidth;
    //
    private byte lineType;
    //
    private short space;

}
