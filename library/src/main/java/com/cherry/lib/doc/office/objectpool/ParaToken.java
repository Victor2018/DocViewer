/*
 * 文件名称:          ParaToken.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:26:51
 */
package com.cherry.lib.doc.office.objectpool;

import java.util.Vector;

/**
 * 段落视图的令牌
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-8-22
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ParaToken
{    
    
    /**
     * 
     * @param obj
     */
    public ParaToken(IMemObj obj)
    {
        this.obj = obj;
    }

    /**
     * 
     *
     */
    public void free()
    {
       obj.free(); 
    }
    
    /**
     * @return Returns the using.
     */
    public boolean isFree()
    {
        return isFree;
    }

    /**
     * @param using The using to set.
     */
    public void setFree(boolean free)
    {
        this.isFree = free;
    }

    /**
     * 
     */
    public void dispose()
    {
        obj =  null;
    }
    
    
    private boolean isFree;
    //
    private IMemObj obj;
}
