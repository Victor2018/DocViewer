/*
 * 文件名称:           ThemeReader.java
 *  
 * 编译器:             android2.2
 * 时间:               下午2:29:30
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;

import android.graphics.Color;

/**
 * 解析 theme color
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-3-2
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ThemeReader
{
    private static ThemeReader themeReader = new ThemeReader();
    
    /**
     * 
     */
    public static ThemeReader instance()
    {
        return themeReader;
    }
    
    /**
     * 
     */
    public Map<String, Integer> getThemeColorMap(PackagePart themePart) throws Exception
    {
        // theme xml
        SAXReader saxreader = new SAXReader();
        InputStream in = themePart.getInputStream();
        Document poiTheme = saxreader.read(in);
        Element root = poiTheme.getRootElement();
        if (root != null)
        {
            Element themeElements = root.element("themeElements");
            if (themeElements != null)
            {
                Element clrScheme = themeElements.element("clrScheme");
                
                // color map
                Map<String, Integer> colorMap = new HashMap<String, Integer>();
                for (Iterator< ? > it = clrScheme.elementIterator(); it.hasNext();)
                {
                    Element clr = (Element)it.next();
                    String name = clr.getName();
                    Element srgbClr = clr.element("srgbClr");
                    Element sysClr = clr.element("sysClr");
                    if (srgbClr != null)
                    {
                        colorMap.put(name, Color.parseColor("#" + srgbClr.attributeValue("val")));
                    }
                    else if (sysClr != null)
                    {
                        colorMap.put(name, Color.parseColor("#" + sysClr.attributeValue("lastClr")));
                    }
                    else
                    {
                        colorMap.put(name, Color.WHITE);
                    }
                }
                return colorMap;
            }
        }
        in.close();
        return null;
    }
}
