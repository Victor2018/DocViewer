/*
 * 文件名称:          IMemObj.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:12:25
 */
package com.nvqquy98.lib.doc.office.objectpool;

/**
 * 共享对象接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-8-21
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IMemObj
{

    /**
     * 放回对象池
     */
    public void free();
    
    /**
     * 复制对象
     */
    public IMemObj getCopy();
}
