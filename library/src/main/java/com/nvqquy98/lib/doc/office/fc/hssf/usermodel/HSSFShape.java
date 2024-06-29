/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.nvqquy98.lib.doc.office.fc.hssf.usermodel;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.Gradient;
import com.nvqquy98.lib.doc.office.common.bg.LinearGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.RadialGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.TileShader;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.common.shape.Arrow;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.constant.AutoShapeConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherBSERecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherBlipRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherChildAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.hssf.model.InternalWorkbook;
import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.AWorkbook;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * An abstract shape.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public abstract class HSSFShape 
{
    /**
     * Create a new shape with the specified parent and anchor.
     */
    HSSFShape(EscherContainerRecord escherContainer, HSSFShape parent, HSSFAnchor anchor)
    {
    	this.escherContainer = escherContainer;
        this.parent = parent;
        this.anchor = anchor;
    }

    public boolean checkPatriarch()
    {
    	HSSFShape topParent = parent;
    	while(_patriarch == null && topParent != null)
    	{
    		_patriarch = topParent._patriarch;
    		topParent =  topParent.getParent();
    	}
    	
    	return _patriarch != null;
    }
    
    public void processLineWidth()
    {
        _lineWidth = ShapeKit.getLineWidth(escherContainer);
    }
    
    /**
     * Gets the parent shape.
     */
    public HSSFShape getParent()
    {
        return parent;
    }

    /**
     * @return  the anchor that is used by this shape.
     */
    public HSSFAnchor getAnchor()
    {
        return anchor;
    }

    /**
     * Sets a particular anchor.  A top-level shape must have an anchor of
     * HSSFClientAnchor.  A child anchor must have an anchor of HSSFChildAnchor
     *
     * @param anchor    the anchor to use.
     * @throws IllegalArgumentException     when the wrong anchor is used for
     *                                      this particular shape.
     *
     * @see HSSFChildAnchor
     * @see HSSFClientAnchor
     */
    public void setAnchor( HSSFAnchor anchor )
    {
        if ( parent == null )
        {
            if ( anchor instanceof HSSFChildAnchor )
                throw new IllegalArgumentException( "Must use client anchors for shapes directly attached to sheet." );
        }
        else
        {
            if ( anchor instanceof HSSFClientAnchor )
                throw new IllegalArgumentException( "Must use child anchors for shapes attached to groups." );
        }

        this.anchor = anchor;
    }
    
    /**
     * 
     * @return
     */
    public boolean isNoBorder()
    {
        return _noBorder;
    }
    
    /**
     * 
     * @param noBorder
     */
    public void setNoBorder(boolean noBorder)
    {
        this._noBorder = noBorder;
    }

    /**
     * The color applied to the lines of this shape.
     */
    public int getLineStyleColor() 
    {
        return _lineStyleColor;
    }

    /**
     * The color applied to the lines of this shape.
     */
    public void setLineStyleColor(int lineStyleColor) 
    {
        _lineStyleColor = lineStyleColor;
        _lineStyleColor = (0xFFFFFF & _lineStyleColor) | (255 << 24);
    }

    /**
     * The color applied to the lines of this shape.
     */
    public void setLineStyleColor(int red, int green, int blue) 
    {   
        this._lineStyleColor = ((255 & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
    }

    /**
     * The color used to fill this shape.
     */
    public int getFillColor()
    {
        return _fillColor;
    }

    /**
     * The color used to fill this shape.
     */
    public void setFillColor(int fillColor, int alpha) 
    {
        _fillColor = fillColor;
        _fillColor = (0xFFFFFF & _fillColor) | (alpha << 24);
    }

    /**
     * The color used to fill this shape.
     */
    public void setFillColor( int red, int green, int blue, int alpha)
    {
        this._fillColor = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
    }

    /**
     * @return  returns with width of the line in EMUs.  12700 = 1 pt.
     */
    public int getLineWidth() 
    {
        return _lineWidth;
    }

    /**
     * Sets the width of the line.  12700 = 1 pt.
     *
     * @param lineWidth width in EMU's.  12700EMU's = 1 pt
     *
     * @see HSSFShape#LINEWIDTH_ONE_PT
     */
    public void setLineWidth(int lineWidth) 
    {
        _lineWidth = lineWidth;
    }

    /**
     * @return One of the constants in LINESTYLE_*
     */
    public int getLineStyle() 
    {
        return _lineStyle;
    }

    /**
     * Sets the line style.
     *
     * @param lineStyle     One of the constants in LINESTYLE_*
     */
    public void setLineStyle(int lineStyle) 
    {
        _lineStyle = lineStyle;
    }

    /**
     * @return <code>true</code> if this shape is not filled with a color.
     */
    public boolean isNoFill() 
    {
        return _noFill;
    }

    /**
     * Sets whether this shape is filled or transparent.
     */
    public void setNoFill(boolean noFill) 
    {
        _noFill = noFill;
    }
    
    /**
     * 
     * @return
     */
    public int getFillType()
    {
        return _fillType;
    }
    
    /**
     * 
     * @param fillType
     */
    public void setFillType(int fillType)
    {
        this._fillType = fillType;
    }
    
    public byte[] getBGPictureData()
    {
        return bgPictureData;
    }
    
    public void setBGPictureData(byte[] bgPictureData)
    {
        this.bgPictureData = bgPictureData;
    }
    
    public int getShapeType() 
    { 
        return shapeType; 
    }

    public void setShapeType( int shapeType )
    { 
        this.shapeType = shapeType;
    }
    
    public int getRotation()
    {
        return angle;
    }
    
    public void setRotation(int angle)
    {
        this.angle = angle;
    }
    
    public boolean getFlipH()
    {
        return flipH;
    }
    
    public void setFilpH(boolean flipH)
    {
        this.flipH = flipH;
    }

    public boolean getFlipV()
    {
        return flipV;
    }
    
    public void setFlipV(boolean flipV)
    {
        this.flipV = flipV;
    }
    
    public void setStartArrow(byte type, int width, int length)
    {
        startArrow = new Arrow(type, width, length);
    }
    
    public void setEndArrow(byte type, int width, int length)
    {
        endArrow = new Arrow(type, width, length);
    }
    
    public int getStartArrowType()
    {
        return startArrow.getType();
    }
    
    public int getStartArrowWidth()
    {
        return startArrow.getWidth();
    }
    
    public int getStartArrowLength()
    {
        return startArrow.getLength();
    }
    
    public int getEndArrowType()
    {
        return endArrow.getType();
    }
    
    public int getEndArrowWidth()
    {
        return endArrow.getWidth();
    }
    
    public int getEndArrowLength()
    {
        return endArrow.getLength();
    }
    
    /**
     * 
     * Count of all children and their children's children.
     */
    public int countOfAllChildren() 
    {
        return 1;
    }
    
    public static HSSFClientAnchor toClientAnchor(EscherClientAnchorRecord anchorRecord)
    {
        HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setAnchorType(anchorRecord.getFlag());
        anchor.setCol1(anchorRecord.getCol1());
        anchor.setCol2(anchorRecord.getCol2());
        anchor.setDx1(anchorRecord.getDx1());
        anchor.setDx2(anchorRecord.getDx2());
        anchor.setDy1(anchorRecord.getDy1());
        anchor.setDy2(anchorRecord.getDy2());
        anchor.setRow1(anchorRecord.getRow1());
        anchor.setRow2(anchorRecord.getRow2());
        return anchor;
    }
    
    public static HSSFChildAnchor toChildAnchor(EscherChildAnchorRecord anchorRecord)
    {
        HSSFChildAnchor anchor = new HSSFChildAnchor(anchorRecord.getDx1(), 
            anchorRecord.getDy1(), 
            anchorRecord.getDx2(), 
            anchorRecord.getDy2());
        
        return anchor;
    }
    
    public void processSimpleBackground(EscherContainerRecord escherContainer, AWorkbook workbook)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        
        int type = ShapeKit.getFillType(escherContainer);
        if (type == BackgroundAndFill.FILL_PICTURE)
        {
            EscherSimpleProperty p4 = (EscherSimpleProperty)ShapeKit.getEscherProperty(opt,
                EscherProperties.FILL__PATTERNTEXTURE);
            if (p4 != null)
            {
                InternalWorkbook iwb = workbook.getInternalWorkbook();
                EscherBSERecord bseRecord = iwb.getBSERecord(p4.getPropertyValue());
                if (bseRecord != null)
                {
                    EscherBlipRecord blipRecord = bseRecord.getBlipRecord();
                    if (blipRecord != null)
                    {
                        setFillType(BackgroundAndFill.FILL_PICTURE);
                        setBGPictureData(blipRecord.getPicturedata());
                        return;
                    }
                }
            }
        }        
        else if (type == BackgroundAndFill.FILL_PATTERN)
        {
            Color color = ShapeKit.getFillbackColor(escherContainer, workbook, MainConstant.APPLICATION_TYPE_SS);
            if (color != null)
            {
                setFillType(BackgroundAndFill.FILL_SOLID);
                setFillColor(color.getRGB(), 255);
                return;
            }
        }
        else if(isGradientTile())
        {
        	setFillType(type);
            return;
        }
        else
        {
            Color color = ShapeKit.getForegroundColor(escherContainer, workbook, MainConstant.APPLICATION_TYPE_SS);
            if (color != null)
            {
                setFillType(BackgroundAndFill.FILL_SOLID);
                setFillColor(color.getRGB(), 255);
                return;
            }
        }
        setNoFill(true);
    }
    
    public boolean isGradientTile()
    {
    	int type = ShapeKit.getFillType(escherContainer);
    	return type == BackgroundAndFill.FILL_SHADE_LINEAR
        		|| type == BackgroundAndFill.FILL_SHADE_RADIAL 
        		|| type == BackgroundAndFill.FILL_SHADE_RECT 
                || type == BackgroundAndFill.FILL_SHADE_SHAPE
                || type == BackgroundAndFill.FILL_SHADE_TILE;
    }
    

    public BackgroundAndFill getGradientTileBackground(AWorkbook workbook, IControl control)
    {
    	BackgroundAndFill bgFill = null;
    	
    	int type = getFillType();
    	if(type == BackgroundAndFill.FILL_SHADE_LINEAR
        		|| type == BackgroundAndFill.FILL_SHADE_RADIAL 
        		|| type == BackgroundAndFill.FILL_SHADE_RECT 
                || type == BackgroundAndFill.FILL_SHADE_SHAPE)
        {
        	bgFill = new BackgroundAndFill();
        	
        	int angle = ShapeKit.getFillAngle(escherContainer);
        	switch(angle)
    		{
    			case -90:
    			case 0:
    				angle += 90;
    				break;
    			case -45:
    				angle = 135;
    				break;
    			case -135:
    				angle = 45;
    				break;
    		}
        	
        	int focus = ShapeKit.getFillFocus(escherContainer);
        	com.nvqquy98.lib.doc.office.java.awt.Color fillColor =
        			ShapeKit.getForegroundColor(escherContainer, workbook, MainConstant.APPLICATION_TYPE_SS);
        	com.nvqquy98.lib.doc.office.java.awt.Color fillbackColor =
        			ShapeKit.getFillbackColor(escherContainer, workbook, MainConstant.APPLICATION_TYPE_SS);
        	
        	int[] colors = null;
        	float[] positions = null; 
        	if(ShapeKit.isShaderPreset(escherContainer))
        	{
        		colors = ShapeKit.getShaderColors(escherContainer);
        		positions = ShapeKit.getShaderPositions(escherContainer);
        	}
        	
        	if(colors == null)
        	{
        		colors = new int[]{fillColor == null ? 0xFFFFFFFF : fillColor.getRGB(), 
            			fillbackColor == null ? 0xFFFFFFFF : fillbackColor.getRGB()};
        	}
        	if(positions == null)
        	{
        		positions = new float[]{0f, 1f};
        	}
        	
        	Gradient gradient = null; 
        	if(type == BackgroundAndFill.FILL_SHADE_LINEAR)
        	{
        		gradient = new LinearGradientShader(angle, colors, positions);
        	}
        	else if(type == BackgroundAndFill.FILL_SHADE_RADIAL 
            		|| type == BackgroundAndFill.FILL_SHADE_RECT 
                    || type == BackgroundAndFill.FILL_SHADE_SHAPE )
            {
    			gradient = 
    					new RadialGradientShader(ShapeKit.getRadialGradientPositionType(escherContainer), colors, positions);
            }
        	
        	if(gradient != null)
        	{
        		gradient.setFocus(focus);
        	}
        	
        	bgFill.setFillType((byte)type);
        	bgFill.setShader(gradient);
        }            
        else if(type == BackgroundAndFill.FILL_SHADE_TILE)
        {
        	bgFill = new BackgroundAndFill();
        	
        	EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(escherContainer,
                    EscherOptRecord.RECORD_ID);
        	// 背景为图片
            EscherSimpleProperty p4 = (EscherSimpleProperty)ShapeKit.getEscherProperty(opt,
                    EscherProperties.FILL__PATTERNTEXTURE);
            if (p4 != null)
            {
                InternalWorkbook iwb = workbook.getInternalWorkbook();
                EscherBSERecord bseRecord = iwb.getBSERecord(p4.getPropertyValue());
                if (bseRecord != null)
                {
                    EscherBlipRecord blipRecord = bseRecord.getBlipRecord();
                    if (blipRecord != null)
                    {
                    	bgFill.setFillType(BackgroundAndFill.FILL_SHADE_TILE);
                    	Picture pic = new Picture(); 
                        pic.setData(blipRecord.getPicturedata());
                        
                        control.getSysKit().getPictureManage().addPicture(pic);
                        
                    	bgFill.setShader(
                				new TileShader(pic, TileShader.Flip_None, 1f, 1.0f));
                    }
                }
            }            
        }
    	
    	return bgFill;
    }
    
    /**
     * 
     * @return
     */
    public Line getLine()
    {
    	BackgroundAndFill lineFill = new BackgroundAndFill();
		lineFill.setForegroundColor(_lineStyleColor);
		
		Line line = new Line();
		line.setBackgroundAndFill(lineFill);
		line.setLineWidth(_lineWidth);
		line.setDash(_lineStyle > AutoShapeConstant.LINESTYLE_SOLID);
		return line;
    }
    
    final HSSFShape parent;  
    HSSFPatriarch _patriarch;
    protected EscherContainerRecord escherContainer;
    private HSSFAnchor anchor; 
    
    private int shapeType = ShapeTypes.NotPrimitive;
    
    private boolean _noBorder = false;
    private int _lineStyleColor = 0x08000040;
    private int _lineWidth = AutoShapeConstant.LINEWIDTH_DEFAULT;    // 12700 = 1pt
    private int _lineStyle = AutoShapeConstant.LINESTYLE_SOLID;
    
    private boolean _noFill = false;
    private int _fillType;
    private int _fillColor = 0x08000009;
    
    private byte[] bgPictureData;
    
    private int angle;
    private boolean flipH;
    private boolean flipV;
    
    // start arror or not
    private Arrow startArrow;
    // end arror or not
    private Arrow endArrow;
}
