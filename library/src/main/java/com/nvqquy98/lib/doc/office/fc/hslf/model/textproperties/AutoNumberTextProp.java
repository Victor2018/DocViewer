/*
 * 文件名称:           AutoNumberTextProp.java
 *  
 * 编译器:             android2.2
 * 时间:               下午1:33:12
 */
package com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties;

/**
 * a kind of auto number data
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-7-19
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AutoNumberTextProp
{
    /**
     * 
     */
    public AutoNumberTextProp()
    {
        
    }
    
    /**
     * 
     * @param numberingType
     * @param start
     */
    public AutoNumberTextProp(int numberingType, int start)
    {
        this.numberingType = numberingType;
        this.start = start;
    }
    
    /**
     * 
     * @return
     */
    public int getNumberingType()
    {
        return numberingType;
    }
    
    /**
     * 
     * @param numberingType
     */
    public void setNumberingType(int numberingType)
    {
        this.numberingType = numberingType;
    }
    
    /**
     * 
     * @return
     */
    public int getStart()
    {
        return start;
    }
    
    /**
     * 
     * @param start
     */
    public void setStart(int start)
    {
        this.start = start;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }

    //
    private int numberingType = -1;
    //
    private int start = 0;
}
