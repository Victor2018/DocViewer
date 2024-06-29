/*
 * 文件名称:           PGStyle.java
 *  
 * 编译器:             android2.2
 * 时间:               上午9:41:27
 */
package com.nvqquy98.lib.doc.office.pg.model;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;

/**
 * 一个 shape 的 style 数据
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-3-3
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PGStyle
{
    /**
     * 
     */
    public PGStyle()
    {
        lvlStyleIDs = new HashMap<Integer, Integer>();
    }
    
    /**
     * get shape anchor
     */
    public Rectangle getAnchor()
    {
        return anchor;
    }
 
    /**
     * set shape anchor
     * @param anchor
     */
    public void setAnchor(Rectangle anchor)
    {
        this.anchor = anchor;
    }
    
    /**
     * get section attribute
     */
    public IAttributeSet getSectionAttr()
    {
        return attr;
    }
    
    /**
     * set section attribute
     * @param attr
     */
    public void setSectionAttr(IAttributeSet attr)
    {
        this.attr = attr;
    }
 
    /**
     * get style based lvl
     */
    public int getStyle(int lvl)
    {
        if (!lvlStyleIDs.isEmpty())
        {
            Integer index = lvlStyleIDs.get(lvl);
            if (index != null)
            {
                return index;
            }
        }
        return -1;
    }
    
    /**
     * set style based lvl
     * @param lvl
     * @param style
     */
    public void addStyle(int lvl, int style)
    {
        lvlStyleIDs.put(lvl, style);
    }
    
    public void addDefaultFontColor(int lvl, String fontColor)
    {
        if (lvl > 0 && fontColor != null)
        {
            if (defaultFontColor == null)
            {
                defaultFontColor = new Hashtable<Integer, String>();
            }
            defaultFontColor.put(lvl, fontColor);
        }
    }
    
    public String getDefaultFontColor(int lvl)
    {
        if (defaultFontColor != null)
        {
            return defaultFontColor.get(lvl);
        }
        return null;
    }
    
    /**
     * dispose
     */
    public void dispose()
    {
        anchor = null;
        if (attr != null)
        {
            attr.dispose();
            attr = null;
        }
        if (lvlStyleIDs != null)
        {
            lvlStyleIDs.clear();
            lvlStyleIDs = null;
        }
        if (defaultFontColor != null)
        {
            defaultFontColor.clear();
            defaultFontColor = null;
        }
    }

    // shape anchor
    private Rectangle anchor;
    // shape section attribute
    private IAttributeSet attr;
    // level, styleID
    private Map<Integer, Integer> lvlStyleIDs;
    //default font color
    private Map<Integer, String>defaultFontColor;
}
