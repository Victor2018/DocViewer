/*
 * 文件名称:          	BookmarkManage.java
 *  
 * 编译器:            android2.2
 * 时间:             	下午5:05:13
 */
package com.nvqquy98.lib.doc.office.common.bookmark;

import java.util.HashMap;
import java.util.Map;

public class BookmarkManage
{
    /**
     * 
     */
    public BookmarkManage()
    {
        bms = new HashMap<String, Bookmark>();
    }
    
    /**
     * 
     */
    public void addBookmark(Bookmark bm)
    {
        bms.put(bm.getName(), bm);
    }
    
    /**
     * 
     */
    public Bookmark getBookmark(String name)
    {
        return bms.get(name);
    }
    
    /**
     * 
     */
    public int getBookmarkCount()
    {
        return bms.size();
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (bms != null)
        {
            bms.clear();
            bms = null;
        }
    }
    
    //
    private Map<String, Bookmark> bms;
}
