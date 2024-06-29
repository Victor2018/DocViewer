/*
 * 文件名称:          AbstractReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:43:10
 */
package com.nvqquy98.lib.doc.office.system;

import java.io.File;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-4-24
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AbstractReader implements IReader
{

    /**
     * 
     *
     */
    public Object getModel() throws Exception
    {
        return null;
    }

    /**
     * 
     */
    public boolean searchContent(File file, String key) throws Exception
    {
        return false;
    }

    /**
     * 
     *
     */
    public boolean isReaderFinish()
    {
        return true;
    }

    /**
     * 
     *
     */
    public void backReader() throws Exception
    {
    }
    
    /**
     * 中断文档解析
     */
    public void abortReader()
    {
        abortReader = true;
    }

    /**
     * 
     * @return
     */
    public boolean isAborted()
    {
        return abortReader;
    }    
    
    /**
     * 
     * @param password password of document
     * @return true: succeed authenticate False: fail authenticate
     */
    /*public boolean authenticate(String password)
    {
        return true;
    }
    
    *//**
     * cancel authenticate
     *//*
     public void cancelAuthenticate()
     {
         
     }*/
    /**
     * 
     */
    public IControl getControl()
    {
        return this.control;
    }
    
    /**
     * 
     *
     */
    public void dispose()
    {
        control = null;
    }
    
    //
    protected boolean abortReader;
    //
    protected IControl control;

}
