/*
 * 文件名称:          IReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:58:11
 */
package com.nvqquy98.lib.doc.office.system;

import java.io.File;

public interface IReader
{

    /**
     * get model data
     * @param filePath
     * @return
     */
    public Object getModel() throws Exception;
    
    /**
     * search content 
     * @param file
     * @param key
     * @return
     */
    public boolean searchContent(File file, String key) throws Exception;
    
    /**
     * 
     * @return
     */
    public boolean isReaderFinish();
    
    /**
     * 后台读取数据
     * @throws Exception
     */    
    public void backReader() throws Exception;
    
    /**
     * 中断文档解析
     */
    public void abortReader();
    
    /**
     * 
     * @return
     */
    public boolean isAborted();    
    
    /**
     * 
     * @param password password of document
     * @return true: succeed authenticate False: fail authenticate
     */
    //public boolean authenticate(String password);
    
    /**
     * cancel authenticate
     */
     //public void cancelAuthenticate();
    /**
     * 
     */
    public IControl getControl();
     
    /**
     * 
     */
    public void dispose();
}
