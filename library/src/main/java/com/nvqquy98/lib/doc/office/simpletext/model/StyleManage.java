/*
 * 文件名称:          StyleManager.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:47:13
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


/**
 * style manage
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-27
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class StyleManage
{
    private static StyleManage kit = new StyleManage();
    
    /**
     * 
     */
    public static StyleManage instance()
    {
        return kit;
    }
    
    /**
     * get style for styleID 
     */
    public Style getStyle(int styleID)
    {
        return styles.get(styleID);
    }
    
    /**
     * get style for style name 
     *  This method as little as possible, because the very collapse of time-consuming
     */
    public Style getStyleForName(String styleName)
    {
        Iterator<Style> itor = styles.values().iterator();
        while (itor.hasNext())
        {
            Style s = itor.next();
            if (s.getName().equals(styleName))
            {
                return s;
            }
        }
        return null;
    }
    
    /**
     * add style  
     */
    public void addStyle(Style style)
    {
        styles.put(style.getId(), style);
    }
    
    public void dispose()
    {
        Iterator<Style> itor = styles.values().iterator();
        while (itor.hasNext())
        {
            ((Style)(itor.next())).dispose();
        }
        styles.clear();
        styles = null;
    }
    

    private Map<Integer, Style> styles = new Hashtable<Integer, Style>();
}
