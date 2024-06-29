/*
 * 文件名称:          CellFormatter.java
 *  
 * 编译器:            android2.2
 * 时间:              下午8:38:58
 */

package com.nvqquy98.lib.doc.office.ss.util.format;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;


/**
 * 单元格式数字格式
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
public class CellFormatter
{
    private static CellFormatter cf = new CellFormatter();

    /**
     * 
     */
    public CellFormatter()
    {
        textFormatter = new Format[0x31];
        textFormatter[0x01] = new DecimalFormat("0");
        textFormatter[0x02] = new DecimalFormat("0.00");
        textFormatter[0x03] = new DecimalFormat("#,##0");
        textFormatter[0x04] = new DecimalFormat("#,##0.00");
        textFormatter[0x05] = new DecimalFormat("$#,##0;$#,##0");
        textFormatter[0x06] = new DecimalFormat("$#,##0;$#,##0");
        textFormatter[0x07] = new DecimalFormat("$#,##0.00;$#,##0.00");
        textFormatter[0x08] = new DecimalFormat("$#,##0.00;$#,##0.00");
        textFormatter[0x09] = new DecimalFormat("0%");
        textFormatter[0x0A] = new DecimalFormat("0.00%");
        textFormatter[0x0B] = new DecimalFormat("0.00E0");
        textFormatter[0x0C] = new FractionalFormat("# ?/?");
        textFormatter[0x0D] = new FractionalFormat("# ??/??");
        textFormatter[0x0E] = new SimpleDateFormat("M/d/yy");
        textFormatter[0x0F] = new SimpleDateFormat("d-MMM-yy");
        textFormatter[0x10] = new SimpleDateFormat("d-MMM");
        textFormatter[0x11] = new SimpleDateFormat("MMM-yy");
        textFormatter[0x12] = new SimpleDateFormat("h:mm a");
        textFormatter[0x13] = new SimpleDateFormat("h:mm:ss a");
        textFormatter[0x14] = new SimpleDateFormat("h:mm");
        textFormatter[0x15] = new SimpleDateFormat("h:mm:ss");
        textFormatter[0x16] = new SimpleDateFormat("M/d/yy h:mm");
        // 0x17 - 0x24 reserved for international and undocumented 0x25, "(#,##0_);(#,##0)"
        //start at 0x26
        //jmh need to do colour
        //"(#,##0_);[Red](#,##0)"
        textFormatter[0x26] = new DecimalFormat("#,##0;#,##0");
        //jmh need to do colour
        //(#,##0.00_);(#,##0.00)
        textFormatter[0x27] = new DecimalFormat("#,##0.00;#,##0.00");
        textFormatter[0x28] = new DecimalFormat("#,##0.00;#,##0.00");
        //??        textFormatter[0x29] = new DecimalFormat("_(*#,##0_);_(*(#,##0);_(* \"-\"_);_(@_)");
        //??        textFormatter[0x2A] = new DecimalFormat("_($*#,##0_);_($*(#,##0);_($* \"-\"_);_(@_)");
        //??        textFormatter[0x2B] = new DecimalFormat("_(*#,##0.00_);_(*(#,##0.00);_(*\"-\"??_);_(@_)");
        //??        textFormatter[0x2C] = new DecimalFormat("_($*#,##0.00_);_($*(#,##0.00);_($*\"-\"??_);_(@_)");
        textFormatter[0x2D] = new SimpleDateFormat("mm:ss");
        //??        textFormatter[0x2E] = new SimpleDateFormat("[h]:mm:ss");
        textFormatter[0x2F] = new SimpleDateFormat("mm:ss.0");
        textFormatter[0x30] = new DecimalFormat("##0.0E0");
    }

    /**
     * 
     * @return
     */
    public static CellFormatter instance()
    {
        return cf;
    }

    /**
     * 
     * @param index
     * @param value
     * @return
     */
    public String format(short index, Object value)
    {
        if (index == 0)
        {
            return value.toString();
        }
        if (textFormatter[index] == null)
        {
            throw new RuntimeException("Sorry. I cant handle the format code :"
                + Integer.toHexString(index));
        }
        return textFormatter[index].format(value);
    }

    /**
     * 
     * @param index
     * @param value
     * @return
     */
    public String format(short index, double value)
    {
        if (index <= 0 || index >= textFormatter.length)
        {
            return generalNumberFormat.format(value);
        }
        if (textFormatter[index] == null)
        {
            return generalNumberFormat.format(value);
        }
        if (textFormatter[index] instanceof DecimalFormat)
        {
            return ((DecimalFormat)textFormatter[index]).format(value);
        }
        if (textFormatter[index] instanceof FractionalFormat)
        {
            return ((FractionalFormat)textFormatter[index]).format(value);
        }
        return String.valueOf(value);
    }

    /**
     * 
     * @param index
     * @param value
     * @return
     */
    public boolean useRedColor(short index, double value)
    {
        return (((index == 0x06) || (index == 0x08) || (index == 0x26) || (index == 0x27)) && (value < 0));
    }

    public void dispose()
    {
        textFormatter = null;
        generalNumberFormat = null;
    }
    
    
    // 文本格式
    private Format[] textFormatter;
    // 数值格式 
    private DecimalFormat generalNumberFormat = new DecimalFormat("0");
}
