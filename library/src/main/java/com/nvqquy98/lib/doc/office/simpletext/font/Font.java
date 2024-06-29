/*
 * 文件名称:          AFont.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:48:59
 */
package com.nvqquy98.lib.doc.office.simpletext.font;

/**
 * Font
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-22
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class Font
{
    // Normal boldness (not bold)
    public final static short BOLDWEIGHT_NORMAL = 0x190;
    // Bold boldness (bold)
    public final static short BOLDWEIGHT_BOLD = 0x2bc;
    // normal type of black color.
    public final static short COLOR_NORMAL = 0x7fff;
    // Dark Red color
    public final static short COLOR_RED = 0xa;
    // no type offsetting (not super or subscript)
    public final static short SS_NONE = 0;
    // superscript
    public final static short SS_SUPER = 1;
    // subscript
    public final static short SS_SUB = 2;
    // not underlined
    public final static byte U_NONE = 0;
    // single (normal) underline
    public final static byte U_SINGLE = 1;
    // double underlined
    public final static byte U_DOUBLE = 2;
    // accounting style single underline
    public final static byte U_SINGLE_ACCOUNTING = 0x21;
    // accounting style double underline
    public final static byte U_DOUBLE_ACCOUNTING = 0x22;
    // ANSI character set
    public final static byte ANSI_CHARSET = 0;
    // Default character set.
    public final static byte DEFAULT_CHARSET = 1;
    // Symbol character set
    public final static byte SYMBOL_CHARSET = 2;
    /**
     * The plain style constant.
     */
    public static final int PLAIN       = 0;
    /**
     * The bold style constant.  This can be combined with the other style
     * constants (except PLAIN) for mixed styles.
     */
    public static final int BOLD        = 1;

    /**
     * The italicized style constant.  This can be combined with the other
     * style constants (except PLAIN) for mixed styles.
     */
    public static final int ITALIC      = 2;
    
    public Font() {
    }
    
    public Font(String name, int style, int size) {
        this.name = (name != null) ? name : "Default";
        this.style = (style & ~0x03) == 0 ? style : 0;
        this.fontSize = size;
    }
    
    /**
     * Returns the style of this <code>Font</code>.  The style can be
     * PLAIN, BOLD, ITALIC, or BOLD+ITALIC.
     * @return the style of this <code>Font</code>
     * @see #isPlain
     * @see #isBold
     * @see #isItalic
     * @since JDK1.0
     */
    public int getStyle() {
        return style;
    }

    /**
     * @return Returns the index.
     */
    public int getIndex()
    {
        return index;
    }
    /**
     * @param index The index to set.
     */
    public void setIndex(int index)
    {
        this.index = index;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the fontSize.
     */
    public double getFontSize()
    {
        return fontSize;
    }
    /**
     * @param fontSize The fontSize to set.
     */
    public void setFontSize(double fontSize)
    {
        this.fontSize = fontSize;
    }
    /**
     * @return Returns the isItalic.
     */
    public boolean isItalic()
    {
        return isItalic;
    }
    /**
     * @param isItalic The isItalic to set.
     */
    public void setItalic(boolean isItalic)
    {
        this.isItalic = isItalic;
    }

    /**
     * @return Returns the isBold.
     */
    public boolean isBold()
    {
        return isBold;
    }
    /**
     * @param isBold The isBold to set.
     */
    public void setBold(boolean isBold)
    {
        this.isBold = isBold;
    }

    /**
     * @return Returns the colorIndex.
     */
    public int getColorIndex()
    {
        return colorIndex;
    }
    /**
     * @param colorIndex The colorIndex to set.
     */
    public void setColorIndex(int colorIndex)
    {
        this.colorIndex = colorIndex;
    }

    /**
     * @return Returns the superSubScript.
     */
    public byte getSuperSubScript()
    {
        return superSubScript;
    }
    /**
     * @param superSubScript The superSubScript to set.
     */
    public void setSuperSubScript(byte superSubScript)
    {
        this.superSubScript = superSubScript;
    }

    /**
     * @return Returns the underline.
     */
    public int getUnderline()
    {
        return underline;
    }
    
    public void setUnderline(String underline)
    {
        if(underline.equalsIgnoreCase("none"))
        {
            setUnderline(U_NONE);
        }
        else if(underline.equalsIgnoreCase("single"))
        {
            setUnderline(U_SINGLE);
        }
        else if(underline.equalsIgnoreCase("double"))
        {
            setUnderline(U_DOUBLE);
        }
        else if(underline.equalsIgnoreCase("singleAccounting"))
        {
            setUnderline(U_SINGLE_ACCOUNTING);
        }
        else if(underline.equalsIgnoreCase("doubleAccounting"))
        {
            setUnderline(U_DOUBLE_ACCOUNTING);
        }
    }
    
    /**
     * @param underline The underline to set.
     */
    public void setUnderline(int underline)
    {
        this.underline = underline;
    }

    /**
     * @return Returns the strikeline.
     */
    public boolean isStrikeline()
    {
        return strikeline;
    }
    /**
     * @param strikeline The strikeline to set.
     */
    public void setStrikeline(boolean strikeline)
    {
        this.strikeline = strikeline;
    }

    public void dispose()
    {
        name = null;
    }
    
    // 字体index
    private int index;
    // 字体名称
    private String name;
    //
    private double fontSize;
    // 斜体
    private boolean isItalic;
    // 粗体
    private boolean isBold;
    // 颜色 index
    private int colorIndex;
    // byte， = 1 Super, = 2 sub
    private byte superSubScript;
    // 下划线
    private int underline;
    // 删除线
    private boolean strikeline;
    // 风格
    protected int style;
}
