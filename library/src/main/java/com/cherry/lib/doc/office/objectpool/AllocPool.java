/*
 * 文件名称:          AllocMem.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:16:04
 */
package com.cherry.lib.doc.office.objectpool;

import java.util.Vector;

/**
 * 共享对象管理
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
public class AllocPool
{

    /**
     * 
     */
    public AllocPool(IMemObj prototype, int capacity, int capacityIncrement)
    { 
        this.prototype = prototype;
        free = new Vector<IMemObj>(capacity, capacityIncrement);
    }
    
    /**
     * 
     */
    public synchronized IMemObj allocObject()
    {
        if (free.size() > 0)
        {
            IMemObj obj = free.remove(0);
            return obj;
        }
        IMemObj obj = prototype.getCopy();
        return obj;
    }
    
    /**
     * 
     */
    public synchronized void free(IMemObj obj)
    {
        free.add(obj);
    }
    
    /**
     * 
     */
    public synchronized void dispose()
    {
        if (free != null)
        {
            free.clear();
        }
    }
    
    private IMemObj prototype;
    //
    private Vector<IMemObj> free;
}
