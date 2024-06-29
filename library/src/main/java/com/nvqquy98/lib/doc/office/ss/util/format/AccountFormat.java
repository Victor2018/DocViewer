/*
 * 文件名称:          AccountFormat.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:21:42
 */
package com.nvqquy98.lib.doc.office.ss.util.format;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-5
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class AccountFormat
{

    private static final double ZERO = 0.000001;
    private static AccountFormat af = new AccountFormat();

    /**
     * 
     */
    private AccountFormat()
    {
    }

    /**
     * 
     * @return
     */
    public static AccountFormat instance()
    {
        return af;
    }  
    
    /**
     * 
     * @param pattern
     * @param value
     * @return
     */
    public String format(String pattern, double value)
    {
        String[] subFormat = pattern.split(";");
        String contents = "";
        switch(subFormat.length)
        {
            case 1:
                contents = parse(subFormat[0], value, false);
                break;
                
            case 2:
                contents = parse(subFormat[0] + ";" + subFormat[1], value, false);
                break;
                
            case 3:
            case 4:
                if(Math.abs(value) > ZERO)
                {
                    contents = parse(subFormat[0] + ";" + subFormat[1], value, false);
                }
                else
                {
                    contents = parse(subFormat[2], 0, true);
                }
                break;
        }
        return contents;
    }  
    
    /**
     * 
     * @param pattern
     * @param value
     * @param isZero
     * @return
     */
    private String parse(String pattern, double value, boolean isZero)
    {
        String contents = "";
        String[] subFormat = pattern.split(";");
        int index = pattern.indexOf("*");
        if(Math.abs(value) < ZERO && subFormat.length == 1)
        { 
            String header = pattern.substring(0, index + 1);
            index = pattern.indexOf('-');
            pattern = pattern.replace("#", "");
            pattern = pattern.replace("?", " ");
            contents = header + pattern.substring(index - 1, pattern.length());
        }
        else
        {
            pattern = pattern.replace("*", "");
            Format format = new DecimalFormat(pattern);
            contents = format.format(value);  
          //rounding
            if(value > 0)
            {
            	value += 0.000000001d;
            }
            else if(value < 0)
            {
            	value -= 0.000000001d;
            }            
            contents = format.format(value);  
            contents = contents.substring(0, index) + "*" + contents.substring(index, contents.length());
        }
        return contents;
    }
}
