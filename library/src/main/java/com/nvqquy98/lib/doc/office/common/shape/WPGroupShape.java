/*
 * 文件名称:          WPGroupShape.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:00:30
 */
package com.nvqquy98.lib.doc.office.common.shape;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-5-31
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class WPGroupShape extends GroupShape
{

    public short getWrapType()
    {
        return wrapType;
    }

    public void setWrapType(short wrapType)
    {
        this.wrapType = wrapType;
    }
    
    private short wrapType;
}
