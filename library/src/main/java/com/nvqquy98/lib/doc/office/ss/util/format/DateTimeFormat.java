/*
 * 文件名称:          DateTimeFormat.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:37:06
 */
package com.nvqquy98.lib.doc.office.ss.util.format;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

//// BEGIN android-added
//import com.ibm.icu4jni.util.LocaleData;
//import com.ibm.icu4jni.util.Resources;
//// END android-added


/**
 * TODO: refer to DataFormatter
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-7
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class DateTimeFormat
{
    static final String patternChars = "GyMdkHmsSEDFwWahKzZ"; //$NON-NLS-1$
    
    private static final String datePatternChars = "GyMdEDFwWazZ";
    
    private static final String timePatternChars = "HhsSkK";    //'m'
    
    /**
     * The format style constant defining the full style.
     */
    public final static int FULL = 0;

    /**
     * The format style constant defining the long style.
     */
    public final static int LONG = 1;

    /**
     * The format style constant defining the medium style.
     */
    public final static int MEDIUM = 2;

    /**
     * The format style constant defining the short style.
     */
    public final static int SHORT = 3;

    /**
     * The {@code FieldPosition} selector for 'G' field alignment, corresponds
     * to the {@link Calendar#ERA} field.
     */
    public final static int ERA_FIELD = 0;

    /**
     * The {@code FieldPosition} selector for 'y' field alignment, corresponds
     * to the {@link Calendar#YEAR} field.
     */
    public final static int YEAR_FIELD = 1;

    /**
     * The {@code FieldPosition} selector for 'M' field alignment, corresponds
     * to the {@link Calendar#MONTH} field.
     */
    public final static int MONTH_FIELD = 2;

    /**
     * The {@code FieldPosition} selector for 'd' field alignment, corresponds
     * to the {@link Calendar#DATE} field.
     */
    public final static int DATE_FIELD = 3;

    /**
     * The {@code FieldPosition} selector for 'k' field alignment, corresponds
     * to the {@link Calendar#HOUR_OF_DAY} field. {@code HOUR_OF_DAY1_FIELD} is
     * used for the one-based 24-hour clock. For example, 23:59 + 01:00 results
     * in 24:59.
     */
    public final static int HOUR_OF_DAY1_FIELD = 4;

    /**
     * The {@code FieldPosition} selector for 'H' field alignment, corresponds
     * to the {@link Calendar#HOUR_OF_DAY} field. {@code HOUR_OF_DAY0_FIELD} is
     * used for the zero-based 24-hour clock. For example, 23:59 + 01:00 results
     * in 00:59.
     */
    public final static int HOUR_OF_DAY0_FIELD = 5;

    /**
     * FieldPosition selector for 'm' field alignment, corresponds to the
     * {@link Calendar#MINUTE} field.
     */
    public final static int MINUTE_FIELD = 6;

    /**
     * FieldPosition selector for 's' field alignment, corresponds to the
     * {@link Calendar#SECOND} field.
     */
    public final static int SECOND_FIELD = 7;

    /**
     * FieldPosition selector for 'S' field alignment, corresponds to the
     * {@link Calendar#MILLISECOND} field.
     */
    public final static int MILLISECOND_FIELD = 8;

    /**
     * FieldPosition selector for 'E' field alignment, corresponds to the
     * {@link Calendar#DAY_OF_WEEK} field.
     */
    //public final static int DAY_OF_WEEK_FIELD = 9;

    /**
     * FieldPosition selector for 'D' field alignment, corresponds to the
     * {@link Calendar#DAY_OF_YEAR} field.
     */
    public final static int DAY_OF_YEAR_FIELD = 10;

    /**
     * FieldPosition selector for 'F' field alignment, corresponds to the
     * {@link Calendar#DAY_OF_WEEK_IN_MONTH} field.
     */
    public final static int DAY_OF_WEEK_IN_MONTH_FIELD = 11;

    /**
     * FieldPosition selector for 'w' field alignment, corresponds to the
     * {@link Calendar#WEEK_OF_YEAR} field.
     */
    public final static int WEEK_OF_YEAR_FIELD = 12;

    /**
     * FieldPosition selector for 'W' field alignment, corresponds to the
     * {@link Calendar#WEEK_OF_MONTH} field.
     */
    public final static int WEEK_OF_MONTH_FIELD = 13;

    /**
     * FieldPosition selector for 'a' field alignment, corresponds to the
     * {@link Calendar#AM_PM} field.
     */
    public final static int DAY_OF_WEEK_FIELD = 14;

    /**
     * FieldPosition selector for 'h' field alignment, corresponding to the
     * {@link Calendar#HOUR} field. {@code HOUR1_FIELD} is used for the
     * one-based 12-hour clock. For example, 11:30 PM + 1 hour results in 12:30
     * AM.
     */
    public final static int HOUR1_FIELD = 15;

    /**
     * The {@code FieldPosition} selector for 'z' field alignment, corresponds
     * to the {@link Calendar#ZONE_OFFSET} and {@link Calendar#DST_OFFSET}
     * fields.
     */
    public final static int HOUR0_FIELD = 16;

    /**
     * The {@code FieldPosition} selector for 'z' field alignment, corresponds
     * to the {@link Calendar#ZONE_OFFSET} and {@link Calendar#DST_OFFSET}
     * fields.
     */
    public final static int TIMEZONE_FIELD = 17;
    

   
    
    /**
     * Constructs a new {@code SimpleDateFormat} using the specified
     * non-localized pattern and the {@code DateFormatSymbols} and {@code
     * Calendar} for the default locale.
     * 
     * @param pattern
     *            the pattern.
     * @throws NullPointerException
     *            if the pattern is {@code null}.
     * @throws IllegalArgumentException
     *            if {@code pattern} is not considered to be usable by this
     *            formatter.
     */
    public DateTimeFormat(String pattern)
    {
        this(pattern, Locale.getDefault());
    }
    
    // BEGIN android-removed
    // SimpleDateFormat(Locale locale, com.ibm.icu.text.SimpleDateFormat icuFormat){
    // }
    // END android-removed
    
    private DateTimeFormat(Locale locale) 
    {
        numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setParseIntegerOnly(true);
        numberFormat.setGroupingUsed(false);
        calendar = new GregorianCalendar(locale);
        calendar.add(Calendar.YEAR, -80);
    }
    
    private String adjust(String template)
    {
        if(template.contains("AM/PM") || template.contains("上午/下午"))
        {
            template = template.replace("AM/PM", "").replace("上午/下午", "");
            ampm = true;
        }
        
        boolean date = isDate(template);
        boolean time = isTime(template);
        
        if(time && date)
        {//date and time
            
            //change "mmm..." to "MMM..."
            int index = template.indexOf("mmm");
            char[] chars = null;
            while(index > -1)
            {
                chars = template.toCharArray();
                int first = index;
                int last = index + 3;
                while(template.charAt(last) == 'm')
                {
                    last ++;
                }
                for(index = first; index < last; index ++)
                {
                    chars[index] = 'M';
                }
                template = String.valueOf(chars);
                index = template.indexOf("mmm");
            }
            
            //
            chars = template.toCharArray();
            List<Integer> indexList = new ArrayList<Integer>();
            index = template.indexOf('m');
            if(index > -1)
            {
                
            }
        }
        else if(date)
        {//date
            template = template.replace('m', 'M');
        }   
        else
        {//time
            if(!ampm)
            {
                template = template.replace('h', 'k');
            }
        }
        
        return template;
    }
    
    public DateTimeFormat(String template, Locale locale)
    {
        this(locale);
        
        template = adjust(template);
        
        validatePattern(template);
        
        // BEGIN android-removed
        // icuFormat = new com.ibm.icu.text.SimpleDateFormat(template, locale);
        // icuFormat.setTimeZone(com.ibm.icu.util.TimeZone.getTimeZone(tzId));
        // END android-removed       

        pattern = template;
        // BEGIN android-changed
        dateTimeFormatData = new DateTimeFormatSymbols(locale);
    }
    
    /**
     * Validates the format character.
     *
     * @param format
     *            the format character
     *
     * @throws IllegalArgumentException
     *             when the format character is invalid
     */
    private void validateFormat(char format)
    {
        int index = patternChars.indexOf(format);
        if (index == -1) 
        {
            // text.03=Unknown pattern character - '{0}'
            throw new IllegalArgumentException("invalidate char"); //$NON-NLS-1$
        }
    }
    
    /**
     * Validates the pattern.
     *
     * @param template
     *            the pattern to validate.
     *
     * @throws NullPointerException
     *             if the pattern is null
     * @throws IllegalArgumentException
     *             if the pattern is invalid
     */
    private void validatePattern(String template)
    {
        boolean quote = false;
        int next, last = -1, count = 0;

        final int patternLength = template.length();
        for (int i = 0; i < patternLength; i++) 
        {
            next = (template.charAt(i));
            if (next == '\'') 
            {
                if (count > 0) 
                {
                    validateFormat((char) last);
                    count = 0;
                }
                if (last == next)
                {
                    last = -1;
                } 
                else 
                {
                    last = next;
                }
                quote = !quote;
                continue;
            }
            if (!quote
                    && (last == next || (next >= 'a' && next <= 'z') || (next >= 'A' && next <= 'Z'))) 
            {
                if (last == next) 
                {
                    count++;
                } 
                else 
                {
                    if (count > 0) 
                    {
                        validateFormat((char) last);
                    }
                    last = next;
                    count = 1;
                }
            } 
            else 
            {
                if (count > 0)
                {
                    validateFormat((char) last);
                    count = 0;
                }
                last = -1;
            }
        }
        if (count > 0) 
        {
            validateFormat((char) last);
        }

        if (quote)
        {
            // text.04=Unterminated quote {0}
            throw new IllegalArgumentException("invalidate pattern"); //$NON-NLS-1$
        }

    }
    
    public String format(Date date)
    {
        return formatImpl(date, new StringBuffer()).toString();
    }
    /**
     * Formats the date.
     * <p>
     * If the FieldPosition {@code field} is not null, and the field
     * specified by this FieldPosition is formatted, set the begin and end index
     * of the formatted field in the FieldPosition.
     * <p>
     * If the Vector {@code fields} is not null, find fields of this
     * date, set FieldPositions with these fields, and add them to the fields
     * vector.
     * 
     * @param date
     *            Date to Format
     * @param buffer
     *            StringBuffer to store the resulting formatted String
     * @param field
     *            FieldPosition to set begin and end index of the field
     *            specified, if it is part of the format for this date
     * @param fields
     *            Vector used to store the FieldPositions for each field in this
     *            date
     * @return the formatted Date
     * @throws IllegalArgumentException
     *            if the object cannot be formatted by this Format.
     */
    private StringBuffer formatImpl(Date date, StringBuffer buffer)
    {

        boolean quote = false;
        int next, last = -1, count = 0;
        calendar.setTime(date);       

        final int patternLength = pattern.length();
        for (int i = 0; i < patternLength; i++) 
        {
            next = (pattern.charAt(i));
            if (next == '\'') 
            {
                if (count > 0)
                {
                    append(buffer,(char) last, count);
                    count = 0;
                }
                if (last == next) 
                {
                    buffer.append('\'');
                    last = -1;
                } 
                else 
                {
                    last = next;
                }
                quote = !quote;
                continue;
            }
            if (!quote
                    && (last == next || (next >= 'a' && next <= 'z') || (next >= 'A' && next <= 'Z'))) 
            {
                if (last == next) 
                {
                    count++;
                } 
                else 
                {
                    if (count > 0) 
                    {
                        append(buffer, (char) last, count);
                    }
                    last = next;
                    count = 1;
                }
            } 
            else 
            {
                if (count > 0) 
                {
                    append(buffer, (char) last, count);
                    count = 0;
                }
                last = -1;
                buffer.append((char) next);
            }
        }
        if (count > 0) 
        {
            append(buffer, (char) last, count);
        }
        
        if(ampm)
        {
            String[] strAMPM = dateTimeFormatData.formatData.getAmPmStrings();
            buffer.append(strAMPM[calendar.get(Calendar.AM_PM)]);
        }
        
        return buffer;
    }
    
    private void append(StringBuffer buffer, char format, int count)
    {
        int field = -1;
        int index = patternChars.indexOf(format);
        if (index == -1) 
        {
            // text.03=Unknown pattern character - '{0}'
            throw new IllegalArgumentException("invalidate char"); //$NON-NLS-1$
        }

        switch (index) 
        {
            case ERA_FIELD:    //G
                String strERAS[] = dateTimeFormatData.formatData.getEras();
                buffer.append(strERAS[calendar.get(Calendar.ERA)]);
                break;
            case YEAR_FIELD:   //y
                int year = calendar.get(Calendar.YEAR);
                // BEGIN android-changed
                // According to Unicode CLDR TR35(http://unicode.org/reports/tr35/) :
                // If date pattern is "yy", display the last 2 digits of year.
                // Otherwise, display the actual year with minimum digit count.
                // Therefore, if the pattern is "y", the display value for year 1234  is '1234' not '34'.
                if (count == 2) 
                {
                    appendNumber(buffer, 2, year %= 100);
                } else 
                {
                    appendNumber(buffer, count, year);
                }
               // END android-changed
                break;
            case MONTH_FIELD:    //M
                int month = calendar.get(Calendar.MONTH);
                if (count <= 2) {
                    appendNumber(buffer, count, month + 1);
                }
                else if (count == 3) 
                {
                    String[] strMonths = dateTimeFormatData.formatData.getShortMonths();
                    buffer.append(strMonths[month]);
                } 
                else 
                {
                    String[] strMonths = dateTimeFormatData.formatData.getMonths();
                    buffer.append(strMonths[month]);
                }
                break;
            case DATE_FIELD:    //d                
                int weekday = calendar.get(Calendar.DAY_OF_WEEK);
                if(weekday < dateTimeFormatData.stdShortWeekdays.length)
                {
                    if (count == 3) 
                    {
                        buffer.append(dateTimeFormatData.stdShortWeekdays[weekday]);
                    } 
                    else if(count > 3)
                    {
                        buffer.append(dateTimeFormatData.stdWeekdays[weekday]);
                    }
                    else
                    {
                        field = Calendar.DATE;
                    }
                }    
                
                
                break;
            case HOUR_OF_DAY1_FIELD: // k
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                appendNumber(buffer, count, hour == 0 ? 24 : hour);
                break;
            case HOUR_OF_DAY0_FIELD: // H
                if(ampm)
                {
                    hour = calendar.get(Calendar.HOUR);
                    appendNumber(buffer, count, hour == 0 ? 12 : hour);
                }
                else
                {
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    appendNumber(buffer, count, hour);
                }
                
                break;
            case MINUTE_FIELD:   //m
                if(count == 3 || count > 5)
                {
                    buffer.append(dateTimeFormatData.stdShortMonths[calendar.get(Calendar.MONTH)]);
                }
                else if(count == 4)
                {
                    buffer.append(dateTimeFormatData.stdMonths[calendar.get(Calendar.MONTH)]);
                }
                else if(count == 5)
                {
                    buffer.append(dateTimeFormatData.stdShortestMonths[calendar.get(Calendar.MONTH)]);
                }
                else
                {
                    field = Calendar.MINUTE;
                }
                break;
            case SECOND_FIELD:  //s
                field = Calendar.SECOND;
                break;
            case MILLISECOND_FIELD:   //S
                int value = calendar.get(Calendar.MILLISECOND);
                appendNumber(buffer, count, value);
                break;
            case DAY_OF_YEAR_FIELD:  //D
                field = Calendar.DAY_OF_YEAR;
                break;
            case DAY_OF_WEEK_IN_MONTH_FIELD:  //F
                field = Calendar.DAY_OF_WEEK_IN_MONTH;
                break;
            case WEEK_OF_YEAR_FIELD: //w
                field = Calendar.WEEK_OF_YEAR;
                break;
            case WEEK_OF_MONTH_FIELD:   //W
                field = Calendar.WEEK_OF_MONTH;
                break;

            case DAY_OF_WEEK_FIELD:   //a
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if (count == 3) 
                {
                    String[] strWeekdays = dateTimeFormatData.formatData.getShortWeekdays();
                    buffer.append(strWeekdays[day]);
                } 
                else if(count > 3)
                {
                    String[] strWeekdays = dateTimeFormatData.formatData.getWeekdays();
                    buffer.append(strWeekdays[day]);
                }
                break;
            case HOUR1_FIELD: // h
                if(ampm)
                {
                    hour = calendar.get(Calendar.HOUR);
                    appendNumber(buffer, count, hour == 0 ? 12 : hour);
                }
                else
                {
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    appendNumber(buffer, count, hour);
                }
                
                break;
            case HOUR0_FIELD: // K
                field = Calendar.HOUR;
                break;
            case TIMEZONE_FIELD: // z
                appendTimeZone(buffer, count, true);
                break;
            // BEGIN android-changed
            case (TIMEZONE_FIELD + 1): // Z
                appendNumericTimeZone(buffer, false);
                break;
            // END android-changed
        }
        if (field != -1) {
            appendNumber(buffer, count, calendar.get(field));
        }
    }
    

    /**
     * Append a representation of the time zone of 'calendar' to 'buffer'.
     * 
     * @param count the number of z or Z characters in the format string; "zzz" would be 3,
     * for example.
     * @param generalTimeZone true if we should use a display name ("PDT") if available;
     * false implies that we should use RFC 822 format ("-0800") instead. This corresponds to 'z'
     * versus 'Z' in the format string.
     */
    private void appendTimeZone(StringBuffer buffer, int count, boolean generalTimeZone)
    {
        // BEGIN android-changed: optimized.
        if (generalTimeZone) 
        {
            TimeZone tz = calendar.getTimeZone();
            boolean daylight = (calendar.get(Calendar.DST_OFFSET) != 0);
            int style = count < 4 ? TimeZone.SHORT : TimeZone.LONG;
           //if (!formatData.customZoneStrings) 
            {
                buffer.append(tz.getDisplayName(daylight, style, Locale.getDefault()));
                return;
            }
            // We can't call TimeZone.getDisplayName() because it would not use
            // the custom DateFormatSymbols of this SimpleDateFormat.
///////////// String custom = Resources.lookupDisplayTimeZone(formatData.zoneStrings, tz.getID(), daylight, style);
/*            if (custom != null) 
            {
                buffer.append(custom);
                return;
            }*/////////////////////////////////////////////////////////////////////////////////////
        }
        // We didn't find what we were looking for, so default to a numeric time zone.
        appendNumericTimeZone(buffer, generalTimeZone);
        // END android-changed
    }
    
 // BEGIN android-added: factored out duplication.
    /**
     * @param generalTimeZone "GMT-08:00" rather than "-0800".
     */
    private void appendNumericTimeZone(StringBuffer buffer, boolean generalTimeZone) 
    {
        int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
        char sign = '+';
        if (offset < 0)
        {
            sign = '-';
            offset = -offset;
        }
        if (generalTimeZone)
        {
            buffer.append("GMT");
        }
        buffer.append(sign);
        appendNumber(buffer, 2, offset / 3600000);
        if (generalTimeZone) 
        {
            buffer.append(':');
        }
        appendNumber(buffer, 2, (offset % 3600000) / 60000);
    }
    // END android-added
    
    private void appendNumber(StringBuffer buffer, int count, int value)
    {
        int minimumIntegerDigits = numberFormat.getMinimumIntegerDigits();
        numberFormat.setMinimumIntegerDigits(count);
        numberFormat.format(Integer.valueOf(value), buffer, new FieldPosition(0));
        numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
    }
    
//    /**
//     * 
//     * @param cell
//     * @return
//     */
//    public static boolean isADateFormat(double value, int formatIndex, String formatString)
//    {        
//        // Is it a date?
//        if (DateUtil.isADateFormat(formatIndex, formatString))
//        {
//            if (DateUtil.isValidExcelDate(value))
//            {
//                return true;
//            }
//        }
//        
//        return false;
//    }
    
    public static boolean isDateTimeFormat(String format)
    {       
        
       //remove "scientific symbol E+"
       format = format.replace("E+", "");
       
        int len = patternChars.length();
        boolean value = false;
        int index = 0;
        while(index < len)
        {
            if(format.indexOf(patternChars.charAt(index)) > -1)
            {
                value = true;
                break;
            }
            index ++;
        }
        return value;
    }      

    
    /**
     * 
     * @param formatString
     * @return
     */
    private boolean isDate(String formatString)
    {
        String subString = formatString.replace("AM", "");
        subString = formatString.replace("PM", "");
        
        int len = datePatternChars.length();
        boolean value = false;
        int index = 0;
        while(index < len)
        {
            if(subString.indexOf(datePatternChars.charAt(index)) > -1)
            {
                value = true;
                break;
            }
            index ++;
        }
        return value;
    }
    
    /**
     * 
     * @param formatString
     * @return
     */
    private boolean isTime(String formatString)
    {
        int len = timePatternChars.length();
        boolean value = false;
        int index = 0;
        while(index < len)
        {
            if(formatString.indexOf(timePatternChars.charAt(index)) > -1)
            {
                value = true;
                break;
            }
            index ++;
        }
        return value;
    }    


    /**
     * 
     */
    public void dispose()
    {
        calendar = null;
        numberFormat = null;
        dateTimeFormatData.dispose();
        dateTimeFormatData = null;
    }
    
    protected Calendar calendar;

    /**
     * The number format used to format a number.
     */
    protected NumberFormat numberFormat;
    
    private String pattern;

    private DateTimeFormatSymbols dateTimeFormatData;
    
    private boolean ampm = false;
}
