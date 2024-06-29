/*
 * 閺傚洣娆㈤崥宥囆�:          NumericCellFormatter.java
 * 閻楀牊娼堥幍?婀丂2001-2014 閾忕钂嬮敍鍫熸線瀹哥儑绱氱粔鎴炲Η閺堝妾洪崗顒�寰�
 * 缂傛牞鐦ч崳?            android2.2
 * 閺冨爼妫�:              娑撳﹤宕�10:20:18
 */
package com.nvqquy98.lib.doc.office.ss.util.format;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nvqquy98.lib.doc.office.fc.ss.usermodel.DataFormatter;
import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.Palette;

import android.graphics.Color;



/**
 * TODO: 閺傚洣娆㈠▔銊╁櫞
 * <p>
 * <p>
 * Read閻楀牊婀�:        Read V1.0
 * <p>
 * 娴ｆ粏?:            jqin
 * <p>
 * 閺冦儲婀�:            2011-12-2
 * <p>
 * 鐠愮喕鐭楁禍?           jqin
 * <p>
 * 鐠愮喕鐭楃亸蹇曠矋:           
 * <p>
 * <p>
 */
public class NumericFormatter
{
     
    private static NumericFormatter cf = new NumericFormatter();

    /** Pattern to find "AM/PM" marker */
    private static final Pattern amPmPattern = Pattern.compile("((A|P)[M/P]*)",
        Pattern.CASE_INSENSITIVE);
    
    /**
     * A regex to match the colour formattings rules.
     * Allowed colours are: Black, Blue, Cyan, Green,
     *  Magenta, Red, White, Yellow, "Color n" (1<=n<=56)
     */
    private static final Pattern colorPattern = Pattern.compile(
        "(\\[BLACK\\])|(\\[BLUE\\])|(\\[CYAN\\])|(\\[GREEN\\])|"
            + "(\\[MAGENTA\\])|(\\[RED\\])|(\\[WHITE\\])|(\\[YELLOW\\])|"
            + "(\\[COLOR\\s*\\d\\])|(\\[COLOR\\s*[0-5]\\d\\])", Pattern.CASE_INSENSITIVE);
    
    /**
     * A map to cache formats.
     *  Map<String,Format> formats
     */
    private final Map<String, Format> formats;
    
    /**
     * 
     */
    private NumericFormatter()
    {
        formats = new HashMap<String, Format>();

        // init built-in formats

        Format zipFormat = ZipPlusFourFormat.instance;
        addFormat("00000\\-0000", zipFormat);
        addFormat("00000-0000", zipFormat);

        Format phoneFormat = PhoneFormat.instance;
        // allow for format string variations
        addFormat("[<=9999999]###\\-####;\\(###\\)\\ ###\\-####", phoneFormat);
        addFormat("[<=9999999]###-####;(###) ###-####", phoneFormat);
        addFormat("###\\-####;\\(###\\)\\ ###\\-####", phoneFormat);
        addFormat("###-####;(###) ###-####", phoneFormat);
        
        addFormat("[<=9999999]000\\-0000;\\(000\\)\\ 000\\-0000", phoneFormat);
        addFormat("[<=9999999]000-0000;(000) 000-0000", phoneFormat);
        addFormat("000\\-0000;\\(000\\)\\ 000\\-0000", phoneFormat);
        addFormat("000-0000;(000) 000-0000", phoneFormat);

        Format ssnFormat = SSNFormat.instance;
        addFormat("000\\-00\\-0000", ssnFormat);
        addFormat("000-00-0000", ssnFormat);
    }

    /**
     * 
     * @return
     */
    public static NumericFormatter instance()
    {
        return cf;
    }  
    
    /**
     * Adds a new format to the available formats.
     * <p>
     * The value that will be passed to the Format's format method (specified
     * by <code>java.text.Format#format</code>) will be a double value from a
     * numeric cell. Therefore the code in the format method should expect a
     * <code>Number</code> value.
     * </p>
     * @param excelFormatStr The data format string
     * @param format A Format instance
     */
    public void addFormat(String excelFormatStr, Format format)
    {
        formats.put(excelFormatStr, format);
    }
    
    /**
     * 
     * @param formatString
     * @return
     */
    public short getNumericCellType(String formatString)
    {
        short cellType = -1;
        int len = formatString.length();
        
        if(formatString == null || formatString.length() == 0 || formatString.equalsIgnoreCase("General"))
        {
            cellType = Cell.CELL_TYPE_NUMERIC_GENERAL;
        }
        else if("@".equals(formatString))
        {
            cellType = Cell.CELL_TYPE_NUMERIC_STRING;
        }
        else if(formatString.replace("?/", "").length() < len)
        {
            cellType = Cell.CELL_TYPE_NUMERIC_FRACTIONAL;
        }  
        else if(formatString.indexOf('*') > -1)
        {
            cellType = Cell.CELL_TYPE_NUMERIC_ACCOUNTING;
        } 
        else
        {
            formatString = validateDatePattern(formatString);
            
            if(formatString == null || formatString.length() == 0 || formatString.equalsIgnoreCase("General"))
            {
                cellType = Cell.CELL_TYPE_NUMERIC_GENERAL;
            }
            else if(DateTimeFormat.isDateTimeFormat(formatString))
            {
                cellType = Cell.CELL_TYPE_NUMERIC_SIMPLEDATE;
            }
            else
            {
                cellType = Cell.CELL_TYPE_NUMERIC_DECIMAL;   //CELL_TYPE_NUMERIC_GENERAL
            }
        }
        
        return cellType;
    }    
    
    /**
     * 
     * @param formatValue
     * @return
     */
    private String validatePattern(String formatValue)
    {
    	 String format = formatValue.replace(";@", "");
         
         formatValue = "";        
         String str = "";
         int s = format.indexOf('\"');
         while(s >= 0)
         {
         	str = format.substring(0, s);
         	format = format.substring(s + 1, format.length());
         	s = format.indexOf('\"');
         	
         	//Double quotes
         	if(s >= 0)
         	{
         		//process format brfore '\"'
         		str = deleteInvalidateChars(str);
         	}
         	//concat with text between ""
         	formatValue += str + format.substring(0, s);
         	
         	//process rest text
         	format = format.substring(s + 1, format.length());
         	
         	s = format.indexOf('\"');
         }
         
         return formatValue + deleteInvalidateChars(format);
    }
    
    private String deleteInvalidateChars(String str)
    {
    	if(str != null)
    	{
    		str = str.replaceAll("\\\\-", "-");
        	str = str.replaceAll("\\\\,", ",");
            str = str.replaceAll("\\\\\\.", "."); // . is a special regexp char
            str = str.replaceAll("\\\\ ", " ");
            str = str.replaceAll("\\\\/", "/"); // weird: m\\/d\\/yyyy  
            str = str.replaceAll("\"/\"", "/"); // "/" is escaped for no reason in: mm"/"dd"/"yyyy
            
            str = str.replace("_-", " ");
            str = str.replace("_(", " ");
            str = str.replace("_)", "");
            str = str.replace("\\(", "(");
            str = str.replace("\\)", ")");
            str = str.replace("\\", "");
            str = str.replace("_", "");            
    	}
    	
        return str;
    }
    /**
     * 
     * @param formatValue
     * @return
     */
    private String validateDatePattern(String formatStr)
    {
        String format = validatePattern(formatStr);
        
      //refer to DataFormatter
        // Convert excel date format to SimpleDateFormat.
        // Excel uses lower and upper case 'm' for both minutes and months.
        // From Excel help:
        /*
            The "m" or "mm" code must appear immediately after the "h" or"hh"
            code or immediately before the "ss" code; otherwise, Microsoft
            Excel displays the month instead of minutes."
          */
        
        boolean hasAmPm = false;
        Matcher amPmMatcher = amPmPattern.matcher(format);
        while (amPmMatcher.find())
        {            
            hasAmPm = true;
        }
        
        StringBuffer sb = new StringBuffer();
        char[] chars = format.toCharArray();
        boolean mIsMonth = true;
        List<Integer> ms = new ArrayList<Integer>();
        boolean isElapsed = false;
        for (int j = 0; j < chars.length; j++)
        {
            char c = chars[j];
            if (c == '[' && !isElapsed)
            {
                isElapsed = true;
                mIsMonth = false;
                sb.append(c);
            }
            else if (c == ']' && isElapsed)
            {
                isElapsed = false;
                sb.append(c);
            }
            else if (isElapsed)
            {
                if (c == 'h' || c == 'H')
                {
                    sb.append('H');
                }
                else if (c == 'm' || c == 'M')
                {
                    sb.append('m');
                }
                else if (c == 's' || c == 'S')
                {
                    sb.append('s');
                }
                else
                {
                    sb.append(c);
                }
            }
            else if (c == 'h' || c == 'H')
            {
                mIsMonth = false;
                if (hasAmPm)
                {
                    sb.append('h');
                }
                else
                {
                    sb.append('H');
                }
            }
            else if (c == 'm' || c == 'M')
            {
                if (mIsMonth)
                {
                    sb.append('M');
                    ms.add(Integer.valueOf(sb.length() - 1));
                }
                else
                {
                    sb.append('m');
                }
            }
            else if (c == 's' || c == 'S')
            {
                sb.append('s');
                // if 'M' precedes 's' it should be minutes ('m')
                for (int i = 0; i < ms.size(); i++)
                {
                    int index = ms.get(i).intValue();
                    if (sb.charAt(index) == 'M')
                    {
                        sb.replace(index, index + 1, "m");
                    }
                }
                mIsMonth = true;
                ms.clear();
            }
            else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')/*Character.isLetter(c)*/)
            {
                mIsMonth = true;
                ms.clear();
                if (c == 'y' || c == 'Y')
                {
                    sb.append('y');
                }
                else if (c == 'd' || c == 'D')
                {
                    sb.append('d');
                }
                else
                {
                    sb.append(c);
                }
            }
            else
            {
                sb.append(c);
            }
        }
        
        format = sb.toString();
        int index = format.indexOf('[');
        while(index > -1)
        {
            int next = format.indexOf(']');
            format = format.substring(0, index) + format.substring(next + 1, format.length());
            index = format.indexOf('[');
        }        
        
        return format;
    }    
    
    /**     
     * A regex to match the colour formattings rules.
     * Allowed colours are: Black, Blue, Cyan, Green,
     *  Magenta, Red, White, Yellow, "Color n" (1<=n<=56)
     * @param book
     * @param formatStr
     * @return
     */
    public static int getNegativeColor(Cell cell)
    {
        String formatCode = cell.getCellStyle().getFormatCode();
        Workbook book = cell.getSheet().getWorkbook();
        
        // Remove color formatting if present
        Matcher colorM = colorPattern.matcher(formatCode);
        if(colorM.find())
        {
            String color = colorM.group();

            // Paranoid replacement...
            int at = formatCode.indexOf(color);
            if (at == -1)
                return Color.BLACK;
            
 
            if(color.equals("[Red]"))
            {
                return Color.RED;
            }
            else if(color.equals("[Blue]"))
            {
                return Color.BLUE;
            }
            else if(color.equals("[Cyan]"))
            {
                return Color.CYAN;
            }
            else if(color.equals("[Green]"))
            {
                return Color.GREEN;
            }
            else if(color.equals("[Magenta]"))
            {
                return Color.MAGENTA;
            }
            else if(color.equals("[Black]"))
            {
                return Color.BLACK;
            }
            else if(color.equals("[White]"))
            {
                return Color.WHITE;
            }
            else if(color.equals("[Yellow]"))
            {
                return Color.YELLOW;
            }
            else if(color.equals("[Color n]"))
            {
                // n:[1:56]
                color = color.replace("[Color ", "").replace("]", "");
                int index = Integer.parseInt(color);
                return book.getColor(index + Palette.FIRST_COLOR_INDEX - 1);
            }
        }
        
        Font font = book.getFont(cell.getCellStyle().getFontIndex());
        return book.getColor(font.getColorIndex());
    }
    
    private String getMoneySymbol(String formatString)
    {
    	int index1 = formatString.indexOf("[");
        int index2 = formatString.indexOf("]");
        String moneysymbol = null;
        while(index1 >= 0 && index2 >= 0)
        {
            String removedStr = formatString.substring(index1, index2 + 1);                        
            if((index1 = removedStr.indexOf("$")) >= 0)
            {
                //eg."[[$鎷�-809]]", [$鈧�-2], not "[$-809]"
            	index2 = removedStr.indexOf('-');
            	if(index2 < 0)
            	{
            		index2 = removedStr.indexOf("]");
            	}
                moneysymbol = removedStr.substring(index1 + 1, index2);
                if(moneysymbol != null)
                {
                	return moneysymbol;
                }
            }
            
            formatString = formatString.replace(removedStr, "");
            
            index1 = formatString.indexOf("[");
            index2 = formatString.indexOf("]");                        
        }
        
        return null;
    }
    
    /**
     * money symbol is first or negative symbol is first
     * @param formatString
     * @return
     */
    private boolean isNegativeFirst(String formatString)
    {
    	int index1 = formatString.indexOf("[");
        int index2 = formatString.indexOf("]");
        String moneySymbol = null;
        if(index1 >= 0 && index2 >= 0)
        {
            String str = formatString.substring(index1, index2 + 1);                        
            if((index1 = str.indexOf("$")) >= 0 && str.length() == 8)
            {
                //eg."[[$鎷�-809]]", not "[$-809]"
            	moneySymbol = str;
            }                    
        }
        
        if(moneySymbol != null)
        {
        	index1 = formatString.indexOf(';');
        	if(index1 >= 0)
        	{
        		formatString = formatString.substring(index1);
            	
            	index1 = formatString.indexOf(moneySymbol);
            	if(index1 > 0 && formatString.charAt(index1 - 1) == '-')
            	{
            		return true;
            	}
        	}   
        }
        
        return false;
    }
    
    /**
     * 
     * @param formatString  eg [$楼-804]#,##0.00_);[Red]([$楼-804]#,##0.00)
     * @return
     */
    private String processMoneyAndNegative(String formatString)
    {
    	int index1 = formatString.indexOf("[");
        int index2 = formatString.indexOf("]");
        String moneysymbol = null;
        while(index1 >= 0 && index2 >= 0)
        {
            String removedStr = formatString.substring(index1, index2 + 1);            
            formatString = formatString.replace(removedStr, "");
            
            index1 = formatString.indexOf("[");
            index2 = formatString.indexOf("]");                        
        }
        
        return formatString;
    }
    
    /**
     * 
     * @param formatString
     * @param date
     * @return
     */
    public String getFormatContents(String formatString, Date date)
    {
        try 
        {            
            DateTimeFormat format = new DateTimeFormat(validateDatePattern(formatString));
            return  format.format(date);
        }
        catch (Exception ex) 
        {            
            DateTimeFormat format = new DateTimeFormat("m/d/yy");
            return  format.format(date);
        }
    }
    
    /**
     * 
     * @param formatString
     * @param cellType
     * @return
     */
    public String getFormatContents(String formatString, double value, short cellType)
    {
        Format format = formats.get(formatString);
        if (format != null)
        {
            return format.format(value);
        }
        
        formatString = validatePattern(formatString);        
        String contents = "";
        try 
        {
            switch(cellType)
            {
                case Cell.CELL_TYPE_NUMERIC_GENERAL:
                case Cell.CELL_TYPE_NUMERIC_STRING:
                    contents = String.valueOf(value);                    
                if(!contents.contains("E"))
                {
                    int index = contents.indexOf('.');
                    if(index > 0 && contents.length() - index > 10)
                    {
                        contents = contents.substring(0, index + 10);
                    }
                }
                contents = delLastZero(contents);
                    break;
                case Cell.CELL_TYPE_NUMERIC_FRACTIONAL:
                    format = new FractionalFormat(formatString);
                    contents = format.format(value);
                    if(contents.length() == 0)
                    {
                    	contents = String.valueOf(0);
                    }
                    break;                
                case Cell.CELL_TYPE_NUMERIC_DECIMAL: 
                	String moneysymbol = getMoneySymbol(formatString);
                    boolean isNegativeFirst = isNegativeFirst(formatString);
                    formatString = processMoneyAndNegative(formatString);
                                  
                       
                    if(value < 0 )
                    {
                    	String[] formats = formatString.split(";");
                        if(formats.length == 2 && formats[0].equals( formats[1]))
                        {
                        	value = -value;
                        }
                    }
                    
                    format = new DecimalFormat(formatString);
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
                    if(moneysymbol != null)
                    {
                    	if(contents.charAt(0) == '(')
                    	{
                    		//contains "()"
                    		contents ="(" + moneysymbol + contents.substring(1);
                    	}
                    	else
                    	{
                    		if(value < 0 && isNegativeFirst)
                        	{
                    			//money char is after "-"
                    			contents ="-" + moneysymbol + contents.replace("-", "");
                        	}
                    		else
                    		{
                    			contents = moneysymbol.concat(contents);
                    		}
                    	}
                    }
                    break;                    
                case Cell.CELL_TYPE_NUMERIC_ACCOUNTING: 
                	moneysymbol = getMoneySymbol(formatString);
                    isNegativeFirst = isNegativeFirst(formatString);
                    formatString = processMoneyAndNegative(formatString);
                    
                    if(value > 0)
                    {
                    	value += 0.000000001d;
                    }
                    else if(value < 0)
                    {
                    	value -= 0.000000001d;
                    }
                    
                    contents = AccountFormat.instance().format(formatString, value);
                    if(moneysymbol != null)
                    {
                    	if(value < 0 && isNegativeFirst)
                    	{
                			//money char is after "-"
                			contents ="-" + moneysymbol + contents.replace("-", "");
                    	}
                		else
                		{
                			contents = moneysymbol.concat(contents);
                		}
                    }
                    break;
            }
        } 
        catch(IllegalArgumentException e)
        {
            //format like "000-0000"
            if(formatString.replace("0", "").replace("-", "").length() == 0)
            {       
                format = new DecimalFormat(formatString.replace("-", ""));
                contents = format.format(value); 
                StringBuilder strBuilder = new StringBuilder(contents);
                String[] ps = formatString.split("-");
                int cnt = 0;
                for(int i = ps.length - 1; i > 0; i--)
                {
                    cnt += ps[i].length();
                    strBuilder.insert(strBuilder.length() - cnt, "-");
                    cnt += 1;
                }                
                contents = strBuilder.toString();
            }
            else
            {
                contents =  String.valueOf(value);
            }
        }        
        
        return contents;
    }   
    
    private String delLastZero(String contents)
    {
        if(contents != null && contents.length() > 1 && !contents.contains("E") && contents.charAt(contents.length() - 1) == '0')
        {
            int index = contents.indexOf('.');
            if(index > 0)
            {
                char[] chars = contents.toCharArray();
                
                int i = chars.length - 1;
                while(i > index && chars[i] == '0')
                {
                    i--;
                }
                if(chars[i] == '.')
                {
                    i--;
                }
                
                return String.valueOf(chars, 0, i + 1);
            }
        }
        
        return contents;
    }
    
    /**
     * @return a <tt>DecimalFormat</tt> with parseIntegerOnly set <code>true</code>
     */
    /* package */static DecimalFormat createIntegerOnlyFormat(String fmt)
    {
        DecimalFormat result = new DecimalFormat(fmt);
        result.setParseIntegerOnly(true);
        return result;
    }
    
    
    /**
     * Format class for Excel's SSN format. This class mimics Excel's built-in
     * SSN formatting.
     *
     * @author James May
     */
    @ SuppressWarnings("serial")
    private static final class SSNFormat extends Format
    {
        public static final Format instance = new SSNFormat();
        private static final DecimalFormat df = createIntegerOnlyFormat("000000000");

        private SSNFormat()
        {
            // enforce singleton
        }

        /** Format a number as an SSN */
        public static String format(Number num)
        {
            String result = df.format(num);
            StringBuffer sb = new StringBuffer();
            sb.append(result.substring(0, 3)).append('-');
            sb.append(result.substring(3, 5)).append('-');
            sb.append(result.substring(5, 9));
            return sb.toString();
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
        {
            return toAppendTo.append(format((Number)obj));
        }

        public Object parseObject(String source, ParsePosition pos)
        {
            return df.parseObject(source, pos);
        }
    }
    
    /**
     * Format class for Excel Zip + 4 format. This class mimics Excel's
     * built-in formatting for Zip + 4.
     * @author James May
     */
    @ SuppressWarnings("serial")
    private static final class ZipPlusFourFormat extends Format
    {
        public static final Format instance = new ZipPlusFourFormat();
        private static final DecimalFormat df = createIntegerOnlyFormat("000000000");

        private ZipPlusFourFormat()
        {
            // enforce singleton
        }

        /** Format a number as Zip + 4 */
        public static String format(Number num)
        {
            String result = df.format(num);
            StringBuffer sb = new StringBuffer();
            sb.append(result.substring(0, 5)).append('-');
            sb.append(result.substring(5, 9));
            return sb.toString();
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
        {
            return toAppendTo.append(format((Number)obj));
        }

        public Object parseObject(String source, ParsePosition pos)
        {
            return df.parseObject(source, pos);
        }
    }

    /**
     * Format class for Excel phone number format. This class mimics Excel's
     * built-in phone number formatting.
     * @author James May
     */
    @ SuppressWarnings("serial")
    private static final class PhoneFormat extends Format
    {
        public static final Format instance = new PhoneFormat();
        private static final DecimalFormat df = createIntegerOnlyFormat("##########");

        private PhoneFormat()
        {
            // enforce singleton
        }

        /** Format a number as a phone number */
        public static String format(Number num)
        {
            String result = df.format(num);
            StringBuffer sb = new StringBuffer();
            String seg1, seg2, seg3;
            int len = result.length();
            if (len <= 4)
            {
                return result;
            }

            seg3 = result.substring(len - 4, len);
            seg2 = result.substring(Math.max(0, len - 7), len - 4);
            seg1 = result.substring(Math.max(0, len - 10), Math.max(0, len - 7));

            if (seg1 != null && seg1.trim().length() > 0)
            {
                sb.append('(').append(seg1).append(") ");
            }
            if (seg2 != null && seg2.trim().length() > 0)
            {
                sb.append(seg2).append('-');
            }
            sb.append(seg3);
            return sb.toString();
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
        {
            return toAppendTo.append(format((Number)obj));
        }

        public Object parseObject(String source, ParsePosition pos)
        {
            return df.parseObject(source, pos);
        }
    }

    /**
     * Format class that does nothing and always returns a constant string.
     *
     * This format is used to simulate Excel's handling of a format string
     * of all # when the value is 0. Excel will output "", Java will output "0".
     *
     * @see DataFormatter#createFormat(double, int, String)
     */
    @ SuppressWarnings("serial")
    private static final class ConstantStringFormat extends Format
    {
        private static final DecimalFormat df = createIntegerOnlyFormat("##########");
        private final String str;

        public ConstantStringFormat(String s)
        {
            str = s;
        }

        @ Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
        {
            return toAppendTo.append(str);
        }

        @ Override
        public Object parseObject(String source, ParsePosition pos)
        {
            return df.parseObject(source, pos);
        }
    }
}
