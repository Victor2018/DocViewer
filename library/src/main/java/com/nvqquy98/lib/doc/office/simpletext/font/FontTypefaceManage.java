/*
 * 文件名称:          FontNameManage.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:17:42
 */
package com.nvqquy98.lib.doc.office.simpletext.font;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.graphics.Typeface;

/**
 * font name manage
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-10-16
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class FontTypefaceManage
{
    //
    private static FontTypefaceManage kit;
    
    /**
     * 
     */
    public FontTypefaceManage()
    {
        /*sysFont = new LinkedHashMap<String, Integer>();
        sysFontName = new ArrayList<String>();
        File[] files = new File("/system/fonts").listFiles();
        if (files != null)
        {
            String name;
            int index;
            for (int i = 0; i < files.length; i++)
            {                
                name = files[i].getName().toLowerCase();
                index = name.lastIndexOf(".");
                if (index > 0)
                {
                    name = name.substring(0, index);
                }
                sysFont.put(name, i);
                sysFontName.add(name);
            }
        }*/        
    }
    
    /**
     * 
     */
    public static FontTypefaceManage instance()
    {
        if (kit == null)
        {
            kit = new FontTypefaceManage();
        }
        return kit;
    }
    
    /**
     * 
     */
    public int addFontName(String fontName)
    {
        /*Integer a = sysFont.get(fontName.toLowerCase());
        return a == null ? -1 : a;*/
        if (sysFontName == null)
        {
            sysFontName = new ArrayList<String>();
        }
        int a = sysFontName.indexOf(fontName);
        if (a < 0)
        {
            a = sysFontName.size();
            sysFontName.add(fontName);
        }
        return a;
            
    }
    
    /**
     * 
     */
    public Typeface getFontTypeface(int index)
    {
        /*Typeface ty = index == -1 ? Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL) :
                Typeface.create(sysFontName.get(index), Typeface.NORMAL);
        return ty;*/
        if (tfs == null)
        {
            tfs = new LinkedHashMap<String, Typeface>();
        }
        String fontName = index < 0 ? "sans-serif" : sysFontName.get(index);
        if (fontName == null)
        {
            fontName = "sans-serif";
        }
        //fontName = "Arial";
        Typeface tf = tfs.get(fontName);
        if (tf == null)
        {
            tf = Typeface.create(fontName, Typeface.NORMAL);
            if (tf == null)
            {
                tf = Typeface.DEFAULT;
            }
            tfs.put(fontName, tf);
        }
        return tf;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }
    //
    //private LinkedHashMap<String, Integer> sysFont;
    //
    private List<String> sysFontName;
    //
    private LinkedHashMap<String, Typeface> tfs;
    
}
