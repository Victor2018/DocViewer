/*
 * 文件名称:          BorderStyle.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:51:05
 */
package com.nvqquy98.lib.doc.office.ss.model.style;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-22
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class BorderStyle
{
    
    /**
     * No border
     */

    public final static short BORDER_NONE = 0x0;

    /**
     * Thin border
     */

    public final static short BORDER_THIN = 0x1;

    /**
     * Medium border
     */

    public final static short BORDER_MEDIUM = 0x2;

    /**
     * dash border
     */

    public final static short BORDER_DASHED = 0x3;

    /**
     * dot border
     */

    public final static short BORDER_HAIR = 0x4;

    /**
     * Thick border
     */

    public final static short BORDER_THICK = 0x5;

    /**
     * double-line border
     */

    public final static short BORDER_DOUBLE = 0x6;

    /**
     * hair-line border
     */

    public final static short BORDER_DOTTED = 0x7;

    /**
     * Medium dashed border
     */

    public final static short BORDER_MEDIUM_DASHED = 0x8;

    /**
     * dash-dot border
     */

    public final static short BORDER_DASH_DOT = 0x9;

    /**
     * medium dash-dot border
     */

    public final static short BORDER_MEDIUM_DASH_DOT = 0xA;

    /**
     * dash-dot-dot border
     */

    public final static short BORDER_DASH_DOT_DOT = 0xB;

    /**
     * medium dash-dot-dot border
     */

    public final static short BORDER_MEDIUM_DASH_DOT_DOT = 0xC;

    /**
     * slanted dash-dot border
     */

    public final static short BORDER_SLANTED_DASH_DOT = 0xD;
    
    public BorderStyle()
    {
        
    }
    
    public BorderStyle(short style, short colorIdx)
    {
        this.style = style;
        this.colorIdx = colorIdx;        
    }
    
    public BorderStyle(String style, short colorIdx)
    {
        this.style = getStyle(style);
        this.colorIdx = colorIdx;        
    }
    
    /**
     * 
     * @param style
     * @return
     */
    private short getStyle(String style)
    {      
        if(style == null || style.equals("none"))
        {
            return BORDER_NONE;
        }
        else if(style.equals("thin"))
        {
            return BORDER_THIN;
        }
        else if(style.equals("medium"))
        {
            return BORDER_MEDIUM;
        }
        else if(style.equals("dashed"))
        {
            return BORDER_DASHED;
        }
        else if(style.equals("dotted"))
        {
            return BORDER_DOTTED;
        }        
        else if(style.equals("thick"))
        {
            return BORDER_THICK;
        } 
        else if(style.equals("double"))
        {
            return BORDER_DOUBLE;
        }
        else if(style.equals("hair"))
        {
            return BORDER_HAIR;
        }
        else if(style.equals("mediumDashed"))
        {
            return BORDER_MEDIUM_DASHED;
        }
        else if(style.equals("dashDot"))
        {
            return BORDER_DASH_DOT;
        }
        else if(style.equals("mediumDashDot"))
        {
            return BORDER_MEDIUM_DASH_DOT;
        }
        else if(style.equals("dashDotDot"))
        {
            return BORDER_DASH_DOT_DOT;
        }
        else if(style.equals("mediumDashDotDot"))
        {
            return BORDER_MEDIUM_DASH_DOT_DOT;
        }
        else if(style.equals("slantDashDot"))
        {
            return BORDER_SLANTED_DASH_DOT;
        }
        
        return BORDER_NONE;
        
    }
    
    /**
     * 
     * @param style
     */
    public void setStyle(short style)
    {
        this.style = style;
    }
    
    /**
     * 
     * @return
     */
    public short getStyle()
    {
        return style;
    }
    
    /**
     * 
     * @param colorIdx
     */
    public void setColor(short colorIdx)
    {
        this.colorIdx = colorIdx;
    }
    
    /**
     * 
     * @return
     */
    public short getColor()
    {
        return colorIdx;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }
    
    private short style = BORDER_NONE;
    private short colorIdx = 0/*Palette.FIRST_COLOR_INDEX*/;
}
