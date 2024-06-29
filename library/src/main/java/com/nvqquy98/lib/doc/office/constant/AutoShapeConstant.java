/*
 * 文件名称:           AutoShapeConstant.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:58:58
 */
package com.nvqquy98.lib.doc.office.constant;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-4-16
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public final class AutoShapeConstant
{
    public static final int LINEWIDTH_ONE_PT = 12700;
    public static final int LINEWIDTH_DEFAULT = 9525;
    public static final int LINESTYLE_SOLID = 0;              // Solid (continuous) pen
    public static final int LINESTYLE_DASHSYS = 1;            // PS_DASH system   dash style
    public static final int LINESTYLE_DOTSYS = 2;             // PS_DOT system   dash style
    public static final int LINESTYLE_DASHDOTSYS = 3;         // PS_DASHDOT system dash style
    public static final int LINESTYLE_DASHDOTDOTSYS = 4;      // PS_DASHDOTDOT system dash style
    public static final int LINESTYLE_DOTGEL = 5;             // square dot style
    public static final int LINESTYLE_DASHGEL = 6;            // dash style
    public static final int LINESTYLE_LONGDASHGEL = 7;        // long dash style
    public static final int LINESTYLE_DASHDOTGEL = 8;         // dash short dash
    public static final int LINESTYLE_LONGDASHDOTGEL = 9;     // long dash short dash
    public static final int LINESTYLE_LONGDASHDOTDOTGEL = 10; // long dash short dash short dash
    public static final int LINESTYLE_NONE = -1;
    
    /**
     * Solid (continuous) pen
     */
     public static final int PEN_SOLID = 1;
     /**
      *  PS_DASH system   dash style
      */
     public static final int PEN_PS_DASH = 2;
     /**
      *  PS_DOT system   dash style
      */
     public static final int PEN_DOT = 3;
     /**
      * PS_DASHDOT system dash style
      */
     public static final int PEN_DASHDOT = 4;
     /**
      * PS_DASHDOTDOT system dash style
      */
     public static final int PEN_DASHDOTDOT = 5;
     /**
      *  square dot style
      */
     public static final int PEN_DOTGEL = 6;
     /**
      *  dash style
      */
     public static final int PEN_DASH = 7;
     /**
      *  long dash style
      */
     public static final int PEN_LONGDASHGEL = 8;
     /**
      * dash short dash
      */
     public static final int PEN_DASHDOTGEL = 9;
     /**
      * long dash short dash
      */
     public static final int PEN_LONGDASHDOTGEL = 10;
     /**
      * long dash short dash short dash
      */
     public static final int PEN_LONGDASHDOTDOTGEL = 11;

     /**
      *  Single line (of width lineWidth)
      */
     public static final int LINE_SIMPLE = 0;
     /**
      * Double lines of equal width
      */
     public static final int LINE_DOUBLE = 1;
     /**
      * Double lines, one thick, one thin
      */
     public static final int LINE_THICKTHIN = 2;
     /**
      *  Double lines, reverse order
      */
     public static final int LINE_THINTHICK = 3;
     /**
      * Three lines, thin, thick, thin
      */
     public static final int LINE_TRIPLE = 4;
}
