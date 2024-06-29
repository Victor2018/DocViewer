/*
 * 文件名称:          CriticalLock.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:22:11
 */
package com.nvqquy98.lib.doc.office.common;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-12
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class CriticalLock
{
    private CriticalLock()
    {
    }    
    
    public static void lock()
    {
        reentrantLock.lock();
    }
    
    public static void unlock()
    {
        reentrantLock.unlock();
    }
    
    private static Lock reentrantLock = new ReentrantLock();
}
