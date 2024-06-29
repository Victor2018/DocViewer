/*
 * 文件名称:           HWPFShape.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:40:11
 */
package com.nvqquy98.lib.doc.office.fc.hwpf.usermodel;

import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.constant.AutoShapeConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherChildAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;

import android.graphics.Path;
import android.graphics.PointF;

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
public abstract class HWPFShape
{
	/**
	 * value that MAY be used to determine how a shape is horizontally positioned
	 */
	public static final byte POSH_ABS = 0;
	public static final byte POSH_LEFT = 1;
    public static final byte POSH_CENTER = 2;
    public static final byte POSH_RIGHT = 3;
    /**
     * The shape is horizontally positioned like msophLeft on odd-numbered pages 
     * and like msophRight on even-numbered pages.
     */
    public static final byte POSH_INSIDE = 4;
    /**
     * The shape is horizontally positioned like msophRight on odd-numbered pages 
     * and like msophLeft on even-numbered pages.
     */
    public static final byte POSH_OUTSIDE = 5;
    
    /**
     * value that MAY<20> be used to determine the page element that the horizontal 
     * position of a shape is relative to
     */
    
    public static final byte POSRELH_MARGIN = 0;
    public static final byte POSRELH_PAGE = 1;
    public static final byte POSRELH_COLUMN = 2;
    public static final byte POSRELH_CHAR = 3;
    
    /**
     * value that MAY be used to determine how a shape is vertically positioned
     */
    public static final byte POSV_ABS = 0;
	public static final byte POSV_TOP = 1;
    public static final byte POSV_CENTER = 2;
    public static final byte POSV_BOTTOM = 3;
    /**
     * The shape is vertically positioned like msopvTop on odd-numbered pages 
     * and like msopvBottom on even-numbered pages.
     */
    public static final byte POSV_INSIDE = 4;
    /**
     * The shape is vertically positioned like msopvBottom on odd-numbered pages 
     * and like msopvTop on even-numbered pages.
     */
    public static final byte POSV_OUTSIDE = 5;
    
    /**
     * value that MAY be used to determine the page element that the horizontal 
     * position of a shape is relative to.
     */
    public static final byte POSRELV_MARGIN = 0;
    public static final byte POSRELV_PAGE = 1;
    public static final byte POSRELV_TEXT = 2;
    public static final byte POSRELV_LINE = 3;
    
    /**
     * 
     * @param escherRecord
     * @param parent
     */
    public HWPFShape(EscherContainerRecord escherRecord, HWPFShape parent)
    {
        this.escherContainer = escherRecord;
        this.parent = parent;
    }
    
    public EscherContainerRecord getSpContainer()
    {
        return this.escherContainer;
    }
    
    public HWPFShape getParent()
    {
        return this.parent;
    }
    
    public int getShapeType()
    {
        return ShapeKit.getShapeType(escherContainer);
    }
    
    public Rectangle getAnchor(Rectangle parent, float zoomX, float zoomY)
    {
        Rectangle anchor = null;
        EscherSpRecord spRecord = escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        if (spRecord != null)
        {
            int flags = spRecord.getFlags();
            if ((flags & EscherSpRecord.FLAG_CHILD) != 0)
            {
                EscherChildAnchorRecord rec = (EscherChildAnchorRecord)ShapeKit.getEscherChild(escherContainer,
                    EscherChildAnchorRecord.RECORD_ID);
                if (rec == null)
                {
                    EscherClientAnchorRecord clrec = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                        escherContainer, EscherClientAnchorRecord.RECORD_ID);
                    if (clrec != null)
                    {
                        anchor = new Rectangle();
                        anchor.x = (int)(clrec.getCol1() * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                        anchor.y = (int)(clrec.getFlag() * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                        anchor.width = (int)((clrec.getDx1() - clrec.getCol1()) * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                        anchor.height = (int)((clrec.getRow1() - clrec.getFlag()) * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    }
                }
                else
                {
                    anchor = new Rectangle();
                    anchor.x = (int)(rec.getDx1() * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.y = (int)(rec.getDy1() * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.width = (int)((rec.getDx2() - rec.getDx1()) * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.height = (int)((rec.getDy2() - rec.getDy1()) * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                }
            }
            else
            {
                EscherClientAnchorRecord clrec = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                    escherContainer, EscherClientAnchorRecord.RECORD_ID);
                if (clrec != null)
                {
                    anchor = new Rectangle();
                    anchor.x = (int)(clrec.getCol1() * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.y = (int)(clrec.getFlag() * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.width = (int)((clrec.getDx1() - clrec.getCol1()) * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.height = (int)((clrec.getRow1() - clrec.getFlag()) * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                }
            }
        }
        
        if(parent != null)
        {
        	anchor.x = anchor.x - parent.x;
        	anchor.y = anchor.y - parent.y;
        }
        return anchor;
    }
    
    /**
     * 
     * @return
     */
    public boolean isHidden()
    {
        return ShapeKit.isHidden(getSpContainer());
    }
    
    /**
     * shape has line or not
     * @return
     */
    public boolean hasLine()
    {
    	return ShapeKit.hasLine(getSpContainer());
    }
    
    /**
     *  Returns width of the line in in points
     */
    public double getLineWidth()
    {
        return ShapeKit.getLineWidth(getSpContainer());
    }
    
    /**
     * @return color of the line. If color is not set returns <code>java.awt.Color.black</code>
     */
    public Color getLineColor()
    {
        return ShapeKit.getLineColor(getSpContainer(), null, MainConstant.APPLICATION_TYPE_WP);
    }
    
    /**
     *   Returns line dashing type
     */
    public int getLineDashing()
    {
    	return ShapeKit.getLineDashing(getSpContainer());
    }
    
    /**
     * Returns fill type.
     * Must be one of the <code>FILL_*</code> constants defined in this class.
     *
     * @return type of fill
     */
    public int getFillType()
    {
        return ShapeKit.getFillType(getSpContainer());
    }
    
    /**
     * Foreground color
     */
    public Color getForegroundColor()
    {
        return ShapeKit.getForegroundColor(getSpContainer(), null, MainConstant.APPLICATION_TYPE_WP);
    }
    
    /**
     * Background color
     */
    public Color getFillbackColor()
    {
        return ShapeKit.getFillbackColor(getSpContainer(), null, MainConstant.APPLICATION_TYPE_WP);
    }
    
    public int getBackgroundPictureIdx()
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(getSpContainer(),
            EscherOptRecord.RECORD_ID);
        
        int type = ShapeKit.getFillType(escherContainer);
        if (type == BackgroundAndFill.FILL_PICTURE 
        		|| type == BackgroundAndFill.FILL_SHADE_TILE
        		|| type == BackgroundAndFill.FILL_PATTERN)
        {
            EscherSimpleProperty property = (EscherSimpleProperty)ShapeKit.getEscherProperty(opt,
                EscherProperties.FILL__PATTERNTEXTURE);
            if (property != null)
            {
                return property.getPropertyValue();
            }
        }
        return -1;
    }
    
    /**
     * Rotation angle in degrees
     *
     * @return rotation angle in degrees
     */
    public int getRotation()
    {
        return ShapeKit.getRotation(getSpContainer());
    }
    
    /**
     * Whether the shape is horizontally flipped
     *
     * @return whether the shape is horizontally flipped
     */
    public boolean getFlipHorizontal()
    {
        return ShapeKit.getFlipHorizontal(getSpContainer());
    }

    /**
     * Whether the shape is vertically flipped
     *
     * @return whether the shape is vertically flipped
     */
    public boolean getFlipVertical()
    {
        return ShapeKit.getFlipVertical(getSpContainer());
    }
    
    public Float[] getAdjustmentValue()
    {
        return ShapeKit.getAdjustmentValue(getSpContainer());
    }
    
    public int getStartArrowType()
    {
        return ShapeKit.getStartArrowType(getSpContainer());
    }
    
    public int getStartArrowWidth()
    {
        return ShapeKit.getStartArrowWidth(getSpContainer());
    }
    
    public int getStartArrowLength()
    {
        return ShapeKit.getStartArrowLength(getSpContainer());
    }    
    
    public int getEndArrowType()
    {
        return ShapeKit.getEndArrowType(getSpContainer());
    }
    
    public int getEndArrowWidth()
    {
        return ShapeKit.getEndArrowWidth(getSpContainer());
    }
    
    public int getEndArrowLength()
    {
        return ShapeKit.getEndArrowLength(getSpContainer());
    }
    
    /**
     * Gets the freeform path
     *
     * @return the freeform path
     */
    public Path[] getFreeformPath(Rectangle rect, PointF startArrowTailCenter, byte startArrowType,  PointF endArrowTailCenter, byte endArrowType)
    {
        return ShapeKit.getFreeformPath(getSpContainer(), rect, startArrowTailCenter, startArrowType, endArrowTailCenter, endArrowType);
    }
    
    public ArrowPathAndTail getStartArrowPath(Rectangle rect)
    {
        return ShapeKit.getStartArrowPathAndTail(getSpContainer(), rect);
    }
    
    public ArrowPathAndTail getEndArrowPath(Rectangle rect)
    {
        return ShapeKit.getEndArrowPathAndTail(getSpContainer(), rect);
    }
    
    /**
     * 
     * @return
     */
    public int getFillAngle()
    {
    	return ShapeKit.getFillAngle(getSpContainer());
    }
    
    /**
     * 
     * @return
     */
    public int getFillFocus()
    {
    	return ShapeKit.getFillFocus(getSpContainer());
    }
    
    public boolean isShaderPreset()
    {
    	return ShapeKit.isShaderPreset(getSpContainer());
    }
    
    public int[] getShaderColors()
    {
    	return ShapeKit.getShaderColors(getSpContainer());
    }    
    
    public float[] getShaderPositions()
    {
    	return ShapeKit.getShaderPositions(getSpContainer());
    }
    
    /**
     * 
     * @return
     */
    public int getRadialGradientPositionType()
    {
    	return ShapeKit.getRadialGradientPositionType(getSpContainer());
    }
    
    public Line getLine(boolean isLineShape)
    {
    	if(isLineShape || hasLine())
    	{
    		int lineWidth = (int)Math.round(getLineWidth() * MainConstant.POINT_TO_PIXEL);
            boolean dash = getLineDashing() > AutoShapeConstant.LINESTYLE_SOLID;
            com.nvqquy98.lib.doc.office.java.awt.Color lineColor = getLineColor();
            if(lineColor != null)
            {            	
            	BackgroundAndFill lineFill = new BackgroundAndFill();
            	lineFill.setForegroundColor(lineColor.getRGB());
            	Line line = new Line();
                line.setBackgroundAndFill(lineFill);
                line.setLineWidth(lineWidth);
                line.setDash(dash);
                return line;
            }
    	}    	
        
        return null;
    }
    
    public int getPosition_H()
    {
    	return ShapeKit.getPosition_H(getSpContainer());
    }
    
    public int getPositionRelTo_H()
    {
    	return ShapeKit.getPositionRelTo_H(getSpContainer());
    }
    
    public int getPosition_V()
    {
    	return ShapeKit.getPosition_V(getSpContainer());
    }
    
    public int getPositionRelTo_V()
    {
    	return ShapeKit.getPositionRelTo_V(getSpContainer());
    }
    
    
    public void dispose()
    {
        parent = null;
        if (escherContainer != null)
        {
            escherContainer.dispose();
            escherContainer = null;
        }
    }
    
    /**
     * Either EscherSpContainer or EscheSpgrContainer record
     * which holds information about this shape.
     */
    protected EscherContainerRecord escherContainer;
    // parent
    protected HWPFShape parent;
}
