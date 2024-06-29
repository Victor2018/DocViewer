/*
 * 文件名称:          AUncaughtExceptionHandler.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:27:24
 */
package com.nvqquy98.lib.doc.office.system;

import java.lang.Thread.UncaughtExceptionHandler;


public class AUncaughtExceptionHandler implements UncaughtExceptionHandler
{
    
    /**
     * 
     */
    public AUncaughtExceptionHandler(IControl control)
    {
        this.control = control;
    }

    /**
     * 
     *
     */
    public void uncaughtException(Thread thread, final Throwable ex)
    {
        control.getSysKit().getErrorKit().writerLog(ex);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        control = null;
    }

    /**
     * 
     */
    private IControl control;
}
