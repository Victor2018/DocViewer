/*
 * 文件名称:          CellStyle.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:56:43
 */

package com.nvqquy98.lib.doc.office.ss.model.style;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;


/**
 * Cell style
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-16
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class CellStyle
{
    // general (normal) horizontal alignment
    public final static short ALIGN_GENERAL = 0x00;
    // left-justified horizontal alignment
    public final static short ALIGN_LEFT = 0x01;
    // center horizontal alignment
    public final static short ALIGN_CENTER = 0x02;
    // right-justified horizontal alignment
    public final static short ALIGN_RIGHT = 0x03;
    // fill? horizontal alignment
    public final static short ALIGN_FILL = 0x04;
    // justified horizontal alignment
    public final static short ALIGN_JUSTIFY = 0x05;
    // center-selection? horizontal alignment
    public final static short ALIGN_CENTER_SELECTION = 0x6;
    // top-aligned vertical alignment
    public final static short VERTICAL_TOP = 0x0;
    // center-aligned vertical alignment
    public final static short VERTICAL_CENTER = 0x1;
    // bottom-aligned vertical alignment
    public final static short VERTICAL_BOTTOM = 0x2;
    // vertically justified vertical alignment
    public final static short VERTICAL_JUSTIFY = 0x3;
    // No border
    public final static short BORDER_NONE = 0x0;
    // Thin border
    public final static short BORDER_THIN = 0x1;
    // Medium border
    public final static short BORDER_MEDIUM = 0x2;
    // dash border
    public final static short BORDER_DASHED = 0x3;
    // dot border
    public final static short BORDER_HAIR = 0x4;
    // Thick border
    public final static short BORDER_THICK = 0x5;
    // double-line border
    public final static short BORDER_DOUBLE = 0x6;
    // hair-line border
    public final static short BORDER_DOTTED = 0x7;
    // Medium dashed border
    public final static short BORDER_MEDIUM_DASHED = 0x8;
    // dash-dot border
    public final static short BORDER_DASH_DOT = 0x9;
    // medium dash-dot border
    public final static short BORDER_MEDIUM_DASH_DOT = 0xA;
    // dash-dot-dot border
    public final static short BORDER_DASH_DOT_DOT = 0xB;
    // medium dash-dot-dot border
    public final static short BORDER_MEDIUM_DASH_DOT_DOT = 0xC;
    // slanted dash-dot border
    public final static short BORDER_SLANTED_DASH_DOT = 0xD;
    //  No background
    public final static short NO_FILL = 0;
    // Solidly filled
    public final static short SOLID_FOREGROUND = 1;
    // Small fine dots
    public final static short FINE_DOTS = 2;
    // Wide dots
    public final static short ALT_BARS = 3;
    // Sparse dots
    public final static short SPARSE_DOTS = 4;
    // Thick horizontal bands
    public final static short THICK_HORZ_BANDS = 5;
    // Thick vertical bands
    public final static short THICK_VERT_BANDS = 6;
    // Thick backward facing diagonals
    public final static short THICK_BACKWARD_DIAG = 7;
    // Thick forward facing diagonals
    public final static short THICK_FORWARD_DIAG = 8;
    // Large spots
    public final static short BIG_SPOTS = 9;
    // Brick-like layout
    public final static short BRICKS = 10;
    // Thin horizontal bands
    public final static short THIN_HORZ_BANDS = 11;
    // Thin vertical bands
    public final static short THIN_VERT_BANDS = 12;
    // Thin backward diagonal
    public final static short THIN_BACKWARD_DIAG = 13;
    // Thin forward diagonal
    public final static short THIN_FORWARD_DIAG = 14;
    // Squares
    public final static short SQUARES = 15;
    // Diamonds
    public final static short DIAMONDS = 16;
    // Less Dots
    public final static short LESS_DOTS = 17;
    // Least Dots
    public final static short LEAST_DOTS = 18;
    
    /**
     * 构造器
     */
    public CellStyle()
    {   
        
    }

    /**
     * @return Returns the index.
     */
    public short getIndex()
    {
        return index;
    }

    /**
     * @param index The index to set.
     */
    public void setIndex(short index)
    {
        this.index = index;
    }

    private void checkDataFormat()
    {
        if(numFmt == null)
        {
            numFmt = new NumberFormat();
        }
    }
    
    /**
     * 
     * @param numFmt
     */
    public void setNumberFormat(NumberFormat numFmt)
    {
        this.numFmt = numFmt;
    }
    
    /**
     * 
     * @param id
     */
    public void setNumberFormatID(short id)
    {
        checkDataFormat();
        numFmt.setNumberFormatID(id);
    }
    
    /**
     * @return Returns the dataFormatIndex.
     */
    public short getNumberFormatID()
    {
        checkDataFormat();
        return numFmt.getNumberFormatID();
    }
    
    /**
     * 
     * @param formatCode
     */
    public void setFormatCode(String formatCode)
    {
        checkDataFormat();
        numFmt.setFormatCode(formatCode);
    }
    
    /**
     * @return Returns the dataFormatString.
     */
    public String getFormatCode()
    {
        checkDataFormat();
        return numFmt.getFormatCode();
    }

    /**
     * @return Returns the fontIndex.
     */
    public short getFontIndex()
    {
        return fontIndex;
    }

    /**
     * @param fontIndex The fontIndex to set.
     */
    public void setFontIndex(short fontIndex)
    {
        this.fontIndex = fontIndex;
    }
    /**
     * @return Returns the isHidden.
     */
    public boolean isHidden()
    {
        return isHidden;
    }

    /**
     * @param isHidden The isHidden to set.
     */
    public void setHidden(boolean isHidden)
    {
        this.isHidden = isHidden;
    }
    /**
     * @return Returns the isLocked.
     */
    public boolean isLocked()
    {
        return isLocked;
    }

    /**
     * @param isLocked The isLocked to set.
     */
    public void setLocked(boolean isLocked)
    {
        this.isLocked = isLocked;
    }
    
    private void checkAlignmeng()
    {
        if(alignment == null)
        {
            alignment = new Alignment();
        }
    }
    
    /**
     * @return Returns the isWrapText.
     */
    public boolean isWrapText()
    {
        checkAlignmeng();
        return alignment.isWrapText() 
        		|| alignment.getHorizontalAlign() == ALIGN_JUSTIFY
        		|| alignment.getVerticalAlign() == VERTICAL_JUSTIFY;
    }

    /**
     * @param isWrapText The isWrapText to set.
     */
    public void setWrapText(boolean isWrapText)
    {
        checkAlignmeng();
        alignment.setWrapText(isWrapText);
    }
    
    /**
     * @return Returns the horAlign.
     */
    public short getHorizontalAlign()
    {
        checkAlignmeng();
        return alignment.getHorizontalAlign();
    }

    /**
     * @param horAlign The horAlign to set.
     */
    public void setHorizontalAlign(String horAlign)
    {
        checkAlignmeng();
        if(horAlign == null || horAlign.equalsIgnoreCase("general"))
        {
            alignment.setHorizontalAlign(ALIGN_GENERAL);
        }
        else if(horAlign.equalsIgnoreCase("left"))
        {
            alignment.setHorizontalAlign(ALIGN_LEFT);
        }
        else if(horAlign.equalsIgnoreCase("center"))
        {
            alignment.setHorizontalAlign(ALIGN_CENTER);
        }
        else if(horAlign.equalsIgnoreCase("right"))
        {
            alignment.setHorizontalAlign(ALIGN_RIGHT);
        }
        else if(horAlign.equalsIgnoreCase("fill"))
        {
            alignment.setHorizontalAlign(ALIGN_FILL);
        }
        else if(horAlign.equalsIgnoreCase("justify"))
        {
            alignment.setHorizontalAlign(ALIGN_JUSTIFY);
        }
        else if(horAlign.equalsIgnoreCase("distributed"))
        {
            alignment.setHorizontalAlign(ALIGN_JUSTIFY);
        }
        
    }
    
    /**
     * @param horAlign The horAlign to set.
     */
    public void setHorizontalAlign(short horAlign)
    {
        checkAlignmeng();
        alignment.setHorizontalAlign(horAlign);
    }
    
    /**
     * @return Returns the verAlign.
     */
    public short getVerticalAlign()
    {
        checkAlignmeng();
        return alignment.getVerticalAlign();
    }

    /**
     * @param verAlign The verAlign to set.
     */
    public void setVerticalAlign(String verAlign)
    {
        checkAlignmeng();
        if(verAlign.equalsIgnoreCase("top"))
        {
            alignment.setVerticalAlign(VERTICAL_TOP);
        }
        else if(verAlign == null || verAlign.equalsIgnoreCase("center"))
        {
            alignment.setVerticalAlign(VERTICAL_CENTER);
        }
        else if(verAlign.equalsIgnoreCase("bottom"))
        {
            alignment.setVerticalAlign(VERTICAL_BOTTOM);
        }
        else if(verAlign.equalsIgnoreCase("justify"))
        {
            alignment.setVerticalAlign(VERTICAL_JUSTIFY);
        }
        else if(verAlign.equalsIgnoreCase("distributed"))
        {
            alignment.setVerticalAlign(VERTICAL_JUSTIFY);
        }
    }
    
    /**
     * @param verAlign The verAlign to set.
     */
    public void setVerticalAlign(short verAlign)
    {
        checkAlignmeng();
        alignment.setVerticalAlign(verAlign);
    }
    /**
     * @return Returns the rotation.
     */
    public short getRotation()
    {
        checkAlignmeng();
        return alignment.getRotaion();
    }

    /**
     * @param rotation The rotation to set.
     */
    public void setRotation(short rotation)
    {
        checkAlignmeng();
        alignment.setRotation(rotation);
    }
    /**
     * @return Returns the indent.
     */
    public short getIndent()
    {
        checkAlignmeng();
        return alignment.getIndent();
    }

    /**
     * @param indent The indent to set.
     */
    public void setIndent(short indent)
    {
        checkAlignmeng();
        alignment.setIndent(indent);
    }
    
    private void checkBorder()
    {
        if(cellBorder == null)
        {
            cellBorder = new CellBorder();
        }
    }
    /**
     * 
     * @param cellBorder
     */
    public void setBorder(CellBorder cellBorder)
    {
        this.cellBorder = cellBorder;
    }
    
    /**
     * @return Returns the borderLeft.
     */
    public short getBorderLeft()
    {
        checkBorder();
        return cellBorder.getLeftBorder().getStyle();
    }

    /**
     * @param borderLeft The borderLeft to set.
     */
    public void setBorderLeft(short borderLeft)
    {
        checkBorder();
        cellBorder.getLeftBorder().setStyle(borderLeft);
    }
    /**
     * @return Returns the borderLeftColorIdx.
     */
    public short getBorderLeftColorIdx()
    {
        checkBorder();
        return cellBorder.getLeftBorder().getColor();
    }

    /**
     * @param borderLeftColorIdx The borderLeftColorIdx to set.
     */
    public void setBorderLeftColorIdx(short borderLeftColorIdx)
    {
        checkBorder();
        cellBorder.getLeftBorder().setColor(borderLeftColorIdx);
    }
    /**
     * @return Returns the borderRight.
     */
    public short getBorderRight()
    {
        checkBorder();
        return cellBorder.getRightBorder().getStyle();
    }

    /**
     * @param borderRight The borderRight to set.
     */
    public void setBorderRight(short borderRight)
    {
        checkBorder();
        cellBorder.getRightBorder().setStyle(borderRight);
    }

    /**
     * @return Returns the borderRightColorIdx.
     */
    public short getBorderRightColorIdx()
    {
        checkBorder();
        return cellBorder.getRightBorder().getColor();
    }

    /**
     * @param borderRightColorIdx The borderRightColorIdx to set.
     */
    public void setBorderRightColorIdx(short borderRightColorIdx)
    {
        checkBorder();
        cellBorder.getRightBorder().setColor(borderRightColorIdx);
    }
    /**
     * @return Returns the borderTop.
     */
    public short getBorderTop()
    {
        checkBorder();
        return cellBorder.getTopBorder().getStyle();
    }

    /**
     * @param borderTop The borderTop to set.
     */
    public void setBorderTop(short borderTop)
    {
        checkBorder();
        cellBorder.getTopBorder().setStyle(borderTop);
    }
    /**
     * @return Returns the borderTopColorIdx.
     */
    public short getBorderTopColorIdx()
    {
        checkBorder();
        return cellBorder.getTopBorder().getColor();
    }

    /**
     * @param borderTopColorIdx The borderTopColorIdx to set.
     */
    public void setBorderTopColorIdx(short borderTopColorIdx)
    {
        checkBorder();
        cellBorder.getTopBorder().setColor(borderTopColorIdx);
    }
    /**
     * @return Returns the borderBottom.
     */
    public short getBorderBottom()
    {
        checkBorder();
        return cellBorder.getBottomBorder().getStyle();
    }

    /**
     * @param borderBottom The borderBottom to set.
     */
    public void setBorderBottom(short borderBottom)
    {
        checkBorder();
        cellBorder.getBottomBorder().setStyle(borderBottom);
    }
    /**
     * @return Returns the borderBottomColorIdx.
     */
    public short getBorderBottomColorIdx()
    {
        checkBorder();
        return cellBorder.getBottomBorder().getColor();
    }

    /**
     * @param borderBottomColorIdx The borderBottomColorIdx to set.
     */
    public void setBorderBottomColorIdx(short borderBottomColorIdx)
    {
        checkBorder();
        cellBorder.getBottomBorder().setColor(borderBottomColorIdx);
    }
    
    private void checkFillPattern()
    {
        if(fill == null)
        {
            fill = new BackgroundAndFill();
            fill.setFillType(BackgroundAndFill.FILL_NO);
        }
    }
    
    /**
     * @return Returns the filePattern.
     */
    public void setFillPattern(BackgroundAndFill fill)
    {
        this.fill = fill;
    }
    
    public BackgroundAndFill getFillPattern()
    {
    	return fill;
    }
    
    /**
     * 
     * @param type
     */
    public void setFillPatternType(byte type)
    {
        checkFillPattern();
        fill.setFillType(type);
    }
    
    /**
     * @return Returns the filePattern.
     */
    public byte getFillPatternType()
    {
        checkFillPattern();
        return fill.getFillType();
    }
    
    /**
     * 
     * @param colorIndex
     */
    public void setBgColor(int color)
    {
        checkFillPattern();
        fill.setBackgoundColor(color);
    }
    
    /**
     * @return Returns the bgColorIndex.
     */
    public int getBgColor()
    {
        checkFillPattern();
        return fill.getBackgoundColor();
    }

    public void setFgColor(int color)
    {
        checkFillPattern();
        fill.setForegroundColor(color);
    }
    
    /**
     * @return Returns the fgColorIndex.
     */
    public int getFgColor()
    {
        checkFillPattern();
        return fill.getForegroundColor();
    }
    
    
    /**
     * 
     */
    public void dispose()
    {
        numFmt = null;    
        fill = null; 
        
        if(cellBorder != null)
        {
            cellBorder.dispose();
            cellBorder = null;    
        }
        if(alignment != null)
        {
            alignment.dispose();
            alignment = null;
        }
    }
    
    // style index 
    private short index;

    private NumberFormat numFmt;
    
    // font index of this cell
    private short fontIndex;
    
    // hidden of this cell
    private boolean isHidden;
    // locked of this cell
    private boolean isLocked;
    
    private Alignment alignment;
    
    private CellBorder cellBorder;
    
   private BackgroundAndFill fill;
}
