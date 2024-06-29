/*
 * 文件名称:           PGBulletNumber.java
 *  
 * 编译器:             android2.2
 * 时间:               下午3:20:37
 */
package com.nvqquy98.lib.doc.office.pg.model;

import java.util.ArrayList;
import java.util.List;

/**
 * manage pg bullet text
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-7-12
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PGBulletText
{   
    /**
     * 
     */
    public PGBulletText()
    {
        bulletTexts = new ArrayList<String>();
    }
    
    /**
     * 
     * @param text
     * @return
     */
    public int addBulletText(String text)
    {
        int size = bulletTexts.size();
        bulletTexts.add(text);
        return size;
    }
    
    /**
     * 
     */
    public String getBulletText(int index)
    {
        if (index < 0 || index >= bulletTexts.size())
        {
            return null;
        }
        return bulletTexts.get(index);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (bulletTexts != null)
        {
            bulletTexts.clear();
            bulletTexts = null;
        }
    }
    
    //
    private List<String> bulletTexts;
}
