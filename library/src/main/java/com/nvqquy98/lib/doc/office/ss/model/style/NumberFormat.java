/*
 * 文件名称:          NumberFormat.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:26:49
 */
package com.nvqquy98.lib.doc.office.ss.model.style;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-22
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class NumberFormat
{
    
    public NumberFormat()
    {
        numFmtId = 0;
        formatCode = "General";
    }
    
    public NumberFormat(short numFmtId, String formatCode)
    {
        this.numFmtId = numFmtId;
        this.formatCode = formatCode;
    }
    
    public void setNumberFormatID(short id)
    {
        numFmtId = id;
    }
    
    /**
     * get Number Format Id
     * @return
     */
    public short getNumberFormatID()
    {
        return numFmtId;
    }
    
    /**
     * 
     * @param formatCode
     */
    public void setFormatCode(String formatCode)
    {
        this.formatCode = formatCode;
    }
    
    /**
     * get Number Format Code
     * @return
     */
    public String getFormatCode()
    {
        return formatCode;
    }
    
    
    /**
     * 
     */
    public void dispose()
    {
        formatCode = null;
    }
    
    //Number Format Id
    private short numFmtId;
    //Number Format Code
    private String formatCode;
}
