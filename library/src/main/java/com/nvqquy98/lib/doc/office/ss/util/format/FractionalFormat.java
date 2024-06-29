/*
 * 文件名称:          CellFormatter.java
 *  
 * 编译器:            android2.2
 * 时间:              下午8:38:58
 */

package com.nvqquy98.lib.doc.office.ss.util.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * 分数格式化
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-9
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
@ SuppressWarnings("serial")
public class FractionalFormat extends Format
{

    /** 
     * Constructs a new FractionalFormatter
     *
     *  # ?/? Up to one digit
     *  # ??/?? Up to two digits
     *  # ???/??? Up to three digits
     *  # ?/2 In halves
     *  # ?/4 In quarters
     *  # ?/8 In eighths
     *  # ?/16 In sixteenths
     *  # ?/10 In tenths
     *  # ?/100 In hundredths
     */
    public FractionalFormat(String formatStr)
    {
        if ("# ?/?".equals(formatStr))
        {
            mode = ONE_DIGIT;
        }
        else if ("# ??/??".equals(formatStr))
        {
            mode = TWO_DIGIT;
        }
        else if ("# ???/???".equals(formatStr))
        {
            mode = THREE_DIGIT;
        }
        else if ("# ?/2".equals(formatStr))
        {
            mode = UNITS;
            units = 2;
        }
        else if ("# ?/4".equals(formatStr))
        {
            mode = UNITS;
            units = 4;
        }
        else if ("# ?/8".equals(formatStr))
        {
            mode = UNITS;
            units = 8;
        }
        else if ("# ??/16".equals(formatStr))
        {
            mode = UNITS;
            units = 16;
        }
        else if ("# ?/10".equals(formatStr))
        {
            mode = UNITS;
            units = 10;
        }
        else if ("# ??/100".equals(formatStr))
        {
            mode = UNITS;
            units = 100;
        }
    }

    /**
     * @param  f       Description of the Parameter
     * @param  MaxDen  Description of the Parameter
     * @return         Description of the Return Value
     */
    private String format(final double f, final int maxDen)
    {
        long whole = (long)f;
        int sign = 1;
        if (f < 0)
        {
            sign = -1;
        }
        double precision = 0.00001;
        double allowedError = precision;
        double d = Math.abs(f);
        d -= whole;
        double frac = d;
        double diff = frac;
        long num = 1;
        long den = 0;
        long a = 0;
        long b = 0;
        long i = 0;
        if (frac > precision)
        {
            while (true)
            {
                d = 1.0 / d;
                i = (long)(d + precision);
                d -= i;
                if (a > 0)
                {
                    num = i * num + b;
                }
                den = (long)(num / frac + 0.5);
                diff = Math.abs((double)num / den - frac);
                if (den > maxDen)
                {
                    if (a > 0)
                    {
                        num = a;
                        den = (long)(num / frac + 0.5);
                        diff = Math.abs((double)num / den - frac);
                    }
                    else
                    {
                        den = maxDen;
                        num = 1;
                        diff = Math.abs((double)num / den - frac);
                        if (diff > frac)
                        {
                            num = 0;
                            den = 1;
                            // Keeps final check below from adding 1 and keeps Den from being 0
                            diff = frac;
                        }
                    }
                    break;
                }
                if ((diff <= allowedError) || (d < precision))
                {
                    break;
                }
                precision = allowedError / diff;
                // This calcualtion of Precision does not always provide results within
                // Allowed Error. It compensates for loss of significant digits that occurs.
                // It helps to round the inprecise reciprocal values to i.
                b = a;
                a = num;
            }
        }
        if (num == den)
        {
            whole++;
            num = 0;
            den = 0;
        }
        else if (den == 0)
        {
            num = 0;
        }
        if (sign < 0)
        {
            if (whole == 0)
            {
                num = -num;
            }
            else
            {
                whole = -whole;
            }
        }
        String value ="";
        
        if(whole != 0)
        {
            value = value.concat(String.valueOf(whole));
        }
        if(num != 0 && den != 0)
        {
            value = value.concat(" " + num + "/" + den);
        }     
        
        return value;
    }

    /** This method formats the double in the units specified.
     *  The usints could be any number but in this current implementation it is
     *  halves (2), quaters (4), eigths (8) etc
     */
    private String formatUnit(double f, int units)
    {
        long whole = (long)f;
        f -= whole;
        long num = Math.round(f * units);
        
        String value ="";        
        if(whole != 0)
        {
            value = value.concat(String.valueOf(whole));
        }
        if(num != 0)
        {
            value = value.concat(" " + num + "/" + units);
        }     
        
        return value;
    }

    /**
     * 
     * @param val
     * @return
     */
    public final String format(double val)
    {
        if (mode == ONE_DIGIT)
        {
            return format(val, 9);
        }
        else if (mode == TWO_DIGIT)
        {
            return format(val, 99);
        }
        else if (mode == THREE_DIGIT)
        {
            return format(val, 999);
        }
        else if (mode == UNITS)
        {
            return formatUnit(val, units);
        }
        throw new RuntimeException("Unexpected Case");
    }

    /**
     * 
     *
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        if (obj instanceof Number)
        {
            toAppendTo.append(format(((Number)obj).doubleValue()));
            return toAppendTo;
        }
        throw new IllegalArgumentException("Can only handle Numbers");
    }

    /**
     * 
     *
     */
    public Object parseObject(String source, ParsePosition status)
    {
        return null;
    }

    /**
     * 
     *
     */
    public Object parseObject(String source) throws ParseException
    {
        return null;
    }

    /**
     *
     */
    public Object clone()
    {
        return null;
    }


    private short ONE_DIGIT = 1;
    private short TWO_DIGIT = 2;
    private short THREE_DIGIT = 3;
    private short UNITS = 4;
    private int units = 1;
    private short mode = -1;
}
