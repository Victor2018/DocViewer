/*
 * 文件名称:          ListLevel.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:49:59
 */
package com.nvqquy98.lib.doc.office.common.bulletnumber;

/**
 * bullet and number level object
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-6-18
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ListLevel
{
    
    /**
     * @return Returns the startAt.
     */
    public int getStartAt()
    {
        return startAt;
    }
    /**
     * @param startAt The startAt to set.
     */
    public void setStartAt(int startAt)
    {
        this.startAt = startAt;
    }

    /**
     * @return Returns the numberFormat.
     */
    public int getNumberFormat()
    {
        return numberFormat;
    }

    /**
     * @param numberFormat The numberFormat to set.
     */
    public void setNumberFormat(int numberFormat)
    {
        this.numberFormat = numberFormat;
    }

    /**
     * @return Returns the numberText.
     */
    public char[] getNumberText()
    {
        return numberText;
    }

    /**
     * @param numberText The numberText to set.
     */
    public void setNumberText(char[] numberText)
    {
        this.numberText = numberText;
    }

    /**
     * @return Returns the align.
     */
    public byte getAlign()
    {
        return align;
    }

    /**
     * @param align The align to set.
     */
    public void setAlign(byte align)
    {
        this.align = align;
    }

    /**
     * @return Returns the followChar.
     */
    public byte getFollowChar()
    {
        return followChar;
    }

    /**
     * @param followChar The followChar to set.
     */
    public void setFollowChar(byte followChar)
    {
        this.followChar = followChar;
    }

    /**
     * @return Returns the textIndent.
     */
    public int getTextIndent()
    {
        return textIndent;
    }

    /**
     * @param textIndent The textIndent to set.
     */
    public void setTextIndent(int textIndent)
    {
        this.textIndent = textIndent;
    }

    /**
     * @return Returns the specialIndent.
     */
    public int getSpecialIndent()
    {
        return specialIndent;
    }

    /**
     * @param specialIndent The specialIndent to set.
     */
    public void setSpecialIndent(int specialIndent)
    {
        this.specialIndent = specialIndent;
    }


    /**
     * @return Returns the paraCount.
     */
    public int getParaCount()
    {
        return paraCount;
    }

    /**
     * @param paraCount The paraCount to set.
     */
    public void setParaCount(int paraCount)
    {
        this.paraCount = paraCount;
    }

    /**
     * @return Returns the normalParaCount.
     */
    public int getNormalParaCount()
    {
        return normalParaCount;
    }
    /**
     * @param normalParaCount The normalParaCount to set.
     */
    public void setNormalParaCount(int normalParaCount)
    {
        this.normalParaCount = normalParaCount;
    }
    /**
     * 
     */
    public void dispose()
    {
        numberText = null;
    }
    

    // start number
    private int startAt;
    /*
     * = 0    decimal                           1、2、3、...
     * = 1    upperRoman                        I、II、III、...
     * = 2    lowerRoman                        i、ii、iii、...
     * = 3    upperLetter                       A、B、C、...                      
     * = 4    lowerLetter                       a、b、c、...
     * = 39   chineseCountingThousand           一、二、三、...
     * = 38   chineseLegalSimplified            壹、贰、叁、...
     * = 30   ideographTraditional              甲、乙、丙、...
     * = 31   ideographZodiac                   子、丑、寅、...
     * = 5    ordinal                           1st、2st、3st、...
     * = 6    cardinalText                      one、two、three、...
     * = 7    ordinalText                       First、Second、Third、...
     * = 22   decimalZero                       01、02、03、...o
     */
    private int numberFormat;
    // number text
    private char[] numberText;
    // horizontal alignment
    private byte align;
    // The type of character following the number text for the paragraph: 0 == tab, 1 == space, 2 == nothing
    private byte followChar;
    //
    private int textIndent;
    //
    private int specialIndent;
    // previous paragraph count of same level
    private int paraCount;
    //
    private int normalParaCount;
    
}
