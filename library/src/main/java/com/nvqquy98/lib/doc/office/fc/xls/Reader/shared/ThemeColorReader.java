/*
 * 文件名称:          Theme.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:36:17
 */
package com.nvqquy98.lib.doc.office.fc.xls.Reader.shared;

import java.io.InputStream;

import com.nvqquy98.lib.doc.office.constant.SchemeClrConstant;
import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;

import android.graphics.Color;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-23
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ThemeColorReader
{
    private static ThemeColorReader reader = new ThemeColorReader();
    
    /**
     * 
     */
    public static ThemeColorReader instance()
    {
        return reader;
    }
    
    /**
     * read theme color and add it to workbook
     * @param themeParts
     * @param book
     * @return theme color index
     */
    public void getThemeColor(PackagePart themeParts, Workbook book) throws Exception
    {
        SAXReader saxreader = new SAXReader();
        InputStream in = themeParts.getInputStream();
        Document poiTheme = saxreader.read(in);
        in.close();
        // 
        Element root = poiTheme.getRootElement();
        
        //theme elements            
        Element themeElements = root.element("themeElements");
        
        //theme color
        Element themeColorElement = themeElements.element("clrScheme");
        
        Element ele = themeColorElement.element(SchemeClrConstant.SCHEME_LT1);
        int color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_LT1, color);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_BG1, color);            
        //put theme index
        book.addThemeColorIndex(0, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_DK1);
        color = getColorIndex(ele, book);            
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_DK1, color);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_TX1, color);            
        book.addThemeColorIndex(1, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_LT2);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_LT2, color);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_BG2, color);  
        book.addThemeColorIndex(2, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_DK2);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_DK2, color);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_TX2, color);   
        book.addThemeColorIndex(3, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_ACCENT1);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_ACCENT1, color);
        book.addThemeColorIndex(4, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_ACCENT2);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_ACCENT2, color);
        book.addThemeColorIndex(5, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_ACCENT3);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_ACCENT3, color);
        book.addThemeColorIndex(6, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_ACCENT4);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_ACCENT4, color);
        book.addThemeColorIndex(7, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_ACCENT5);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_ACCENT5, color);
        book.addThemeColorIndex(8, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_ACCENT6);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_ACCENT6, color);
        book.addThemeColorIndex(9, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_HLINK);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_HLINK, color);
        book.addThemeColorIndex(10, color);
        
        ele = themeColorElement.element(SchemeClrConstant.SCHEME_FOLHLINK);
        color = getColorIndex(ele, book);
        book.addSchemeColorIndex(SchemeClrConstant.SCHEME_FOLHLINK, color);
        book.addThemeColorIndex(11, color);
    }
    
    
    private int getColorIndex(Element colorEle, Workbook book)
    {
        //rgb
        int color = Color.BLACK;
        
        if(colorEle.element("srgbClr") != null)
        {
            color = Integer.parseInt(colorEle.element("srgbClr").attributeValue("val"), 16);
            
        }
        else if(colorEle.element("sysClr") != null)
        {
            color = Integer.parseInt(colorEle.element("sysClr").attributeValue("lastClr"), 16);
        }
        
        //rgb composed with alpha
        color = color | (0xFF << 24);
        
        color = book.addColor(color);
        return color;
    }    
}
