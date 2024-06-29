/*
 * 文件名称:          ISearch.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:36:19
 */
package com.nvqquy98.lib.doc.office.system;

public interface IFind
{
    /**
     * 
     * @param value 
     * @return true: finded  false: not finded
     */
    public boolean find(String value);
    
    /**
     * need call function find first and finded
     * @return
     */
    public boolean findBackward();
    
    /**
     * need call function find first and finded
     * @return
     */
    public boolean findForward();
    
    
    /**
     * 
     */
    public int getPageIndex();    
    /**
     * 
     */
    public void resetSearchResult();
    
    /**
     * 
     */
    public void dispose();
}
