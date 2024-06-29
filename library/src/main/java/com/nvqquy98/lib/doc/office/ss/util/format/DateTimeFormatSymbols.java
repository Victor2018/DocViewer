/*
 * 文件名称:          DateTimeFormatSymbols.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:46:57
 */
package com.nvqquy98.lib.doc.office.ss.util.format;

import java.text.DateFormatSymbols;
import java.util.Locale;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-8
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class DateTimeFormatSymbols
{
    public final String[] stdWeekdays = 
    {
        "", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };
    
    public final String[] stdShortWeekdays = 
    {
        "", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    };
    
    public final String[] stdMonths = 
    {
        "January", "February", "March", "April", "May", "June", 
        "July", "August", "September", "October", "November", "December", ""
    };
    
    public final String[] stdShortMonths = 
    {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
        "July", "Aug", "Sep", "Oct", "Nov", "Dec", ""
    };
    
    public final String[] stdShortestMonths = 
    {
        "J", "F", "M", "A", "M", "J", 
        "J", "A", "S", "O", "N", "D"
    };
    
    
    public DateTimeFormatSymbols(Locale locale)
    {
        formatData = new DateFormatSymbols(locale);
    }    
    
    /**
     * 
     */
    public void dispose()
    {
        formatData = null;
    }
    
    public DateFormatSymbols formatData;
}
