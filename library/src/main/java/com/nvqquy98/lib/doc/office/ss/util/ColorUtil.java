/*
 * 文件名称:          ColorUtil.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:36:23
 */
package com.nvqquy98.lib.doc.office.ss.util;

import android.graphics.Color;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-3-19
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ColorUtil
{
    private static ColorUtil util = new ColorUtil();
    //
    public static ColorUtil instance()
    {
        return util;
    }
    
    /**
     * Return a color-int from red, green, blue components.
     * The alpha component is implicity 255 (fully opaque).
     * These component values should be [0..255], but there is no
     * range check performed, so if they are out of range, the
     * returned color is undefined.
     * @param red  Red component [0..255] of the color
     * @param green Green component [0..255] of the color
     * @param blue  Blue component [0..255] of the color
     */
    public static int rgb(int red, int green, int blue) 
    {
        return (0xFF << 24) | (red << 16 & 0xFF0000) | (green << 8 & 0xFF00) | (blue & 0xFF);
    }
    
    public static int rgb(byte red, byte green, byte blue) 
    {
        return (0xFF << 24) | (red << 16 & 0xFF0000) | (green << 8 & 0xFF00) | (blue & 0xFF);
    }
    
    /**
     * http://poi.apache.org/apidocs/org/apache/poi/xssf/usermodel/XSSFColor.html
     * @param lum
     * @param tint
     * @return
     */
    private static int applyTint(int lum, double tint)
    {
        if(tint > 0)
        {
            lum = (int)(lum  + (255 - lum) * tint);
        } 
        else if (tint < 0)
        {
            lum =  (int)(lum*(1+tint));
        } 
        
        lum = lum > 255 ? lum = 255 : lum;
        
        return lum;
    }
    
    /**
     * Standard Red Green Blue ctColor value (RGB) with applied tint.
     * Alpha values are ignored.
     */
    public int getColorWithTint(int color, double tint)
    {
        int r = applyTint(Color.red(color) & 0xFF, tint);        
        int g = applyTint(Color.green(color) & 0xFF, tint);
        int b = applyTint(Color.blue(color) & 0xFF, tint);
        
        return Color.rgb(r, g, b);
    }
}
