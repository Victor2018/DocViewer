/*
 * 文件名称:           AutoShape.java
 *  
 * 编译器:             android2.2
 * 时间:               下午2:13:13
 */
package com.nvqquy98.lib.doc.office.common.shape;


/**
 * Represents an AutoShape.
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-8-24
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AutoShape extends AbstractShape
{
    /**
     * 
     */
    public AutoShape()
    {
        
    }
    
    /**
     * 
     * @param type
     */
    public AutoShape(int type)
    {
        this.type = type;
    }
    
    /**
     * 
     *
     */
    public short getType()
    {
        return SHAPE_AUTOSHAPE;
    }
    
    /**
     * get autoShape type
     * @return
     */
    public int getShapeType()
    {
        return type;
    }
    
    /**
     * set autoShape type
     * @return
     */
    public void setShapeType(int type)
    {
        this.type = type;
    }
    
    
    
    /**
     * set values of adjust points
     * @param values
     */
    public void setAdjustData(Float[] values)
    {
        this.values = values;
    }
    
    /**
     * get values of adjust points
     * @return
     */
    public Float[] getAdjustData()
    {
        return values;
    }
    
    /**
     * 
     * @param shape07
     */
    public void setAuotShape07(boolean shape07)
    {
        this.shape07 = shape07;
    }
    
    /**
     * 
     * @return
     */
    public boolean isAutoShape07()
    {
        return shape07;
    }
    
    /**
     * dispose
     */
    public void dispose()
    {
        super.dispose();        
    }
    
    //
    private int type;
    
    // adjust values by clockwise(percent)
    private Float[] values;    
    
    // autoShape is 07 or 03
    private boolean shape07 = true;
}
