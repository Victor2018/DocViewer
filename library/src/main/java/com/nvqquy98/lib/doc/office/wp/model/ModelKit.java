/*
 * 文件名称:          ModelKit.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:16:27
 */
package com.nvqquy98.lib.doc.office.wp.model;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;

/**
 * model kit
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-9
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ModelKit
{
    private static ModelKit kit = new ModelKit();
    
    /**
     * 
     */
    public static ModelKit instance()
    {
        return kit;
    }
    
    /**
     * 
     * @return
     */
    public long getArea(long offset)
    {
        return offset & WPModelConstant.AREA_MASK;
    }

}
