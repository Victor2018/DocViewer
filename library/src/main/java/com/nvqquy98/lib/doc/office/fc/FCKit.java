/*
 * 文件名称:          FCKkit.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:51:48
 */
package com.nvqquy98.lib.doc.office.fc;

import android.graphics.Color;

/**
 * 此类只能放一些转换类算法，不能转换任何形式的数据
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2014年1月23日
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class FCKit
{
    /**
     * 
     * @param val
     * @return
     */
    public static int convertColor(String val)
    {
        if ("yellow".equals(val))
        {
            return Color.YELLOW;
        }
        else if ("green".equals(val))
        {
            return Color.GREEN;
        }
        else if ("cyan".equals(val))
        {
            return Color.CYAN;
        }
        else if ("magenta".equals(val))
        {
            return Color.MAGENTA;
        }
        else if ("blue".equals(val))
        {
            return Color.BLUE;
        }
        else if ("red".equals(val))
        {
            return Color.RED;
        }
        else if ("darkBlue".equals(val))
        {
            return 0xFF00008B;
        }
        else if ("darkCyan".equals(val))
        {
            return 0xFF008B8B;
        }
        else if ("darkGreen".equals(val))
        {
            return 0xFF006400;
        }
        else if ("darkMagenta".equals(val))
        {
            return 0xFF800080;
        }
        else if ("darkRed".equals(val))
        {
            return 0xFF8B0000;
        }
        else if ("darkYellow".equals(val))
        {
            return 0xFF808000;
        }  
        else if ("darkGray".equals(val))
        {
            return Color.DKGRAY;
        }  
        else if ("lightGray".equals(val))
        {
            return Color.LTGRAY;
        }  
        else if ("black".equals(val))
        {
            return Color.BLACK;
        }
        return  -1;
    }
    
    /**
     * BGR to RGB
     */
    public static int BGRtoRGB(int color)
    {
        // 颜色 
        int argbValue = color;
        if (argbValue == -1 || argbValue == 0xFFFFFF)
        {
            argbValue = Color.BLACK;
        }
        else
        {
            int bgrValue = argbValue & 0x00FFFFFF;
            argbValue = 0xFF000000 | (bgrValue & 0x0000FF ) << 16 | ( bgrValue & 0x00FF00 ) | ( bgrValue & 0xFF0000 ) >> 16;
        }
        return argbValue;
    }
}
