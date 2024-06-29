/*
 * 文件名称:          I.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:44:20
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

/**
 * 属性集接口
 * 
 * 属性以ID，Value方式记录，ID类型是short，value类型是int
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-28
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IAttributeSet
{

    /**
     * 得到属性集ID
     */
    public int getID();
    /**
     * 添加属性
     * @param attrID
     * @param value
     */
    public void setAttribute(short attrID, int value);
    
    /**
     * 删除属性
     * 
     * @param attrID
     */
    public void removeAttribute(short attrID);
    
    /**
     * 得到属性
     * @param attrID
     * @param value
     */
    public int getAttribute(short attrID);
    /**
     * 合并属性
     */
    public void mergeAttribute(IAttributeSet attr);
    /**
     * 
     */
    public IAttributeSet clone();
    /**
     * 
     */
    public void dispose();
    
}
