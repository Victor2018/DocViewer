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

package com.nvqquy98.lib.doc.office.fc.hslf.model;

import java.util.Iterator;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherChildAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ColorSchemeAtom;
import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.Rectangle2D;

/**
 *  <p>
  * Represents a Shape which is the elemental object that composes a drawing.
 *  This class is a wrapper around EscherSpContainer which holds all information
 *  about a shape in PowerPoint document.
 *  </p>
 *  <p>
 *  When you add a shape, you usually specify the dimensions of the shape and the position
 *  of the upper'left corner of the bounding box for the shape relative to the upper'left
 *  corner of the page, worksheet, or slide. Distances in the drawing layer are measured
 *  in points (72 points = 1 inch).
 *  </p>
 * <p>
  *
  * @author Yegor Kozlov
 */
public abstract class Shape
{
    /**
     * Create a Shape object. This constructor is used when an existing Shape is read from from a PowerPoint document.
     *
     * @param escherRecord       <code>EscherSpContainer</code> container which holds information about this shape
     * @param parent             the parent of this Shape
     */
    protected Shape(EscherContainerRecord escherRecord, Shape parent)
    {
        _escherContainer = escherRecord;
        _parent = parent;
    }
    
    /**
     *  @return the parent of this shape
     */
    public Shape getParent()
    {
        return _parent;
    }
    
    /**
     * @return type of the shape.
     */
    public int getShapeType()
    {
        return ShapeKit.getShapeType(_escherContainer);
    }
    
    /**
     * @return id for the shape.
     */
    public int getShapeId()
    {
        return ShapeKit.getShapeId(_escherContainer);
    }
    
    /**
     * 
     * @return
     */
    public boolean isHidden()
    {
        return ShapeKit.isHidden(_escherContainer);
    }
    
    /**
     * 
     * @return
     */
    public int getMasterShapeID()
    {
    	return ShapeKit.getMasterShapeID(_escherContainer);
    }
    
    /**
     * Returns the anchor (the bounding box rectangle) of this shape.
     * All coordinates are expressed in points (72 dpi).
     *
     * @return the anchor of this shape
     */
    public Rectangle2D getAnchor2D()
    {
        Rectangle2D anchor = null;
        EscherSpRecord spRecord = _escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        int flags = spRecord.getFlags();
        if ((flags & EscherSpRecord.FLAG_CHILD) != 0)
        {
            EscherChildAnchorRecord rec = (EscherChildAnchorRecord)ShapeKit.getEscherChild(_escherContainer,
                EscherChildAnchorRecord.RECORD_ID);
            if (rec == null)
            {
                EscherClientAnchorRecord clrec = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                    _escherContainer, EscherClientAnchorRecord.RECORD_ID);
                anchor = new Rectangle2D.Float((float)clrec.getCol1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                    (float)clrec.getFlag() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                    (float)(clrec.getDx1() - clrec.getCol1()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                    (float)(clrec.getRow1() - clrec.getFlag()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI);
            }
            else
            {
                anchor = new Rectangle2D.Float((float)rec.getDx1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                    (float)rec.getDy1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                    (float)(rec.getDx2() - rec.getDx1()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                    (float)(rec.getDy2() - rec.getDy1()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI);
            }
        }
        else
        {
            EscherClientAnchorRecord rec = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                _escherContainer, EscherClientAnchorRecord.RECORD_ID);
            anchor = new Rectangle2D.Float((float)rec.getCol1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                (float)rec.getFlag() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                (float)(rec.getDx1() - rec.getCol1()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                (float)(rec.getRow1() - rec.getFlag()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI);
        }
        return anchor;
    }
    
    /**
     * Returns the anchor (the bounding box rectangle) of this shape.
     * All coordinates are expressed in points (72 dpi).
     *
     * @return the anchor of this shape
     */
    public Rectangle getAnchor()
    {
        Rectangle2D anchor2d = getAnchor2D();
        return anchor2d.getBounds();
    }

    /**
     * @return  The shape container and it's children that can represent this
     *          shape.
     */
    public EscherContainerRecord getSpContainer()
    {
        return _escherContainer;
    }
    
    /**
     *  @return the <code>SlideShow</code> this shape belongs to
     */
    public Sheet getSheet()
    {
        return _sheet;
    }

    /**
     * Assign the <code>SlideShow</code> this shape belongs to
     *
     * @param sheet owner of this shape
     */
    public void setSheet(Sheet sheet)
    {
        _sheet = sheet;
    }
    
    protected Color getColor(int rgb, int alpha)
    {
        if (rgb >= 0x8000000)
        {
            int idx = rgb - 0x8000000;
            ColorSchemeAtom ca = getSheet().getColorScheme();
            if (idx >= 0 && idx <= 7)
            {
                rgb = ca.getColor(idx);
            }
        }
        Color tmp = new Color(rgb, true);
        return new Color(tmp.getBlue(), tmp.getGreen(), tmp.getRed(), alpha);
    }
    
    /**
     * Fill properties of this shape
     *
     * @return fill properties of this shape
     */
    public Fill getFill()
    {
        if (_fill == null)
        {
            _fill = new Fill(this);
        }
        return _fill;
    }

    /**
     * Returns the hyperlink assigned to this shape
     *
     * @return the hyperlink assigned to this shape
     * or <code>null</code> if not found.
     */
    public Hyperlink getHyperlink()
    {
        return Hyperlink.find(this);
    }
    
    /**
     * Helper method to return escher child by record ID
     *
     * @return escher record or <code>null</code> if not found.
     */
    public static EscherRecord getEscherChild(EscherContainerRecord owner, int recordId)
    {
        for (Iterator<EscherRecord> iterator = owner.getChildIterator(); iterator.hasNext();)
        {
            EscherRecord escherRecord = iterator.next();
            if (escherRecord.getRecordId() == recordId)
                return escherRecord;
        }
        return null;
    }
    
    /**
     * Returns  escher property by id.
     *
     * @return escher property or <code>null</code> if not found.
     */
    public static EscherProperty getEscherProperty(EscherOptRecord opt, int propId)
    {
        if (opt != null)
            for (Iterator iterator = opt.getEscherProperties().iterator(); iterator.hasNext();)
            {
                EscherProperty prop = (EscherProperty)iterator.next();
                if (prop.getPropertyNumber() == propId)
                    return prop;
            }
        return null;
    }

    /**
     * Set an escher property for this shape.
     *
     * @param opt       The opt record to set the properties to.
     * @param propId    The id of the property. One of the constants defined in EscherOptRecord.
     * @param value     value of the property. If value = -1 then the property is removed.
     */
    public static void setEscherProperty(EscherOptRecord opt, short propId, int value)
    {
        java.util.List props = opt.getEscherProperties();
        for (Iterator iterator = props.iterator(); iterator.hasNext();)
        {
            EscherProperty prop = (EscherProperty)iterator.next();
            if (prop.getId() == propId)
            {
                iterator.remove();
            }
        }
        if (value != -1)
        {
            opt.addEscherProperty(new EscherSimpleProperty(propId, value));
            opt.sortProperties();
        }
    }
    
    /**
     * Set an simple escher property for this shape.
     *
     * @param propId    The id of the property. One of the constants defined in EscherOptRecord.
     * @param value     value of the property. If value = -1 then the property is removed.
     */
    public void setEscherProperty(short propId, int value)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        setEscherProperty(opt, propId, value);
    }

    /**
     * Get the value of a simple escher property for this shape.
     *
     * @param propId    The id of the property. One of the constants defined in EscherOptRecord.
     */
    public int getEscherProperty(short propId)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, propId);
        return prop == null ? 0 : prop.getPropertyValue();
    }

    /**
     * Get the value of a simple escher property for this shape.
     *
     * @param propId    The id of the property. One of the constants defined in EscherOptRecord.
     */
    public int getEscherProperty(short propId, int defaultValue)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, propId);
        return prop == null ? defaultValue : prop.getPropertyValue();
    }

    
    public Float[] getAdjustmentValue()
    {
        return ShapeKit.getAdjustmentValue(_escherContainer);
    }
    
    public int getStartArrowType()
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINESTARTARROWHEAD);
            if (prop != null && prop.getPropertyValue() > 0)
            {
                return prop.getPropertyValue();
            }
        }
        return 0;
    }
    
    public int getStartArrowWidth()
    {
        return ShapeKit.getStartArrowWidth(_escherContainer);
    }
    
    public int getStartArrowLength()
    {
        return ShapeKit.getStartArrowLength(_escherContainer);
    }    
    
    public int getEndArrowType()
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINEENDARROWHEAD);
            if (prop != null && prop.getPropertyValue() > 0)
            {
                return prop.getPropertyValue();
            }
        }
        return 0;
    }
    
    public int getEndArrowWidth()
    {
        return ShapeKit.getEndArrowWidth(_escherContainer);
    }
    
    public int getEndArrowLength()
    {
        return ShapeKit.getEndArrowLength(_escherContainer);
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
        return ShapeKit.getLineColor(getSpContainer(), getSheet(), MainConstant.APPLICATION_TYPE_PPT);
    }
    
    /**
     * The color used to fill this shape.
     */
    public Color getFillColor()
    {
        return getFill().getForegroundColor();
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
     * 
     */
    public void dispose()
    {
        _parent = null;
        _sheet = null;
        if (_escherContainer != null)
        {
            _escherContainer.dispose();
            _escherContainer = null;
        }
        if (_fill != null)
        {
            _fill.dispose();
            _fill = null;
        }
    }

    
    /*******************************************************/
    /**
     * Creates the lowerlevel escher records for this shape.
     */
    protected abstract EscherContainerRecord createSpContainer(boolean isChild);

    /**
     * @return name of the shape.
     */
    public String getShapeName()
    {
        return ShapeTypes.typeName(getShapeType());
    }
    
    /**
     * @param type type of the shape.
     * @see com.nvqquy98.lib.doc.office.fc.hslf.record.RecordTypes
     */
    public void setShapeType(int type)
    {
        EscherSpRecord spRecord = _escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        spRecord.setOptions((short)(type << 4 | 0x2));
    }

    public Rectangle2D getLogicalAnchor2D()
    {
        return getAnchor2D();
    }

    /**
     * Sets the anchor (the bounding box rectangle) of this shape.
     * All coordinates should be expressed in points (72 dpi).
     *
     * @param anchor new anchor
     */
    public void setAnchor(Rectangle2D anchor)
    {
        EscherSpRecord spRecord = _escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        int flags = spRecord.getFlags();
        if ((flags & EscherSpRecord.FLAG_CHILD) != 0)
        {
            EscherChildAnchorRecord rec = (EscherChildAnchorRecord)ShapeKit.getEscherChild(_escherContainer,
                EscherChildAnchorRecord.RECORD_ID);
            rec.setDx1((int)(anchor.getX() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
            rec.setDy1((int)(anchor.getY() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
            rec.setDx2((int)((anchor.getWidth() + anchor.getX()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
            rec.setDy2((int)((anchor.getHeight() + anchor.getY()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        }
        else
        {
            EscherClientAnchorRecord rec = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                _escherContainer, EscherClientAnchorRecord.RECORD_ID);
            rec.setFlag((short)(anchor.getY() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
            rec.setCol1((short)(anchor.getX() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
            rec.setDx1((short)(((anchor.getWidth() + anchor.getX()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI)));
            rec.setRow1((short)(((anchor.getHeight() + anchor.getY()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI)));
        }

    }

    /**
     * Moves the top left corner of the shape to the specified point.
     *
     * @param x the x coordinate of the top left corner of the shape
     * @param y the y coordinate of the top left corner of the shape
     */
    public void moveTo(float x, float y)
    {
        Rectangle2D anchor = getAnchor2D();
        anchor.setRect(x, y, anchor.getWidth(), anchor.getHeight());
        setAnchor(anchor);
    }

    /**
     * Event which fires when a shape is inserted in the sheet.
     * In some cases we need to propagate changes to upper level containers.
     * <br>
     * Default implementation does nothing.
     *
     * @param sh - owning shape
     */
    protected void afterInsert(Sheet sh)
    {

    }

    /**
     * Sets shape ID
     *
     * @param id of the shape
     */
    public void setShapeId(int id)
    {
        EscherSpRecord spRecord = _escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        if (spRecord != null)
        {
            spRecord.setShapeId(id);
        }
    }
    
    /**
     * Return shape outline as a java.awt.Shape object
     *
     * @return the shape outline
     */
    public com.nvqquy98.lib.doc.office.java.awt.Shape getOutline()
    {
        return getLogicalAnchor2D();
    }
    
    /**
     * Either EscherSpContainer or EscheSpgrContainer record
     * which holds information about this shape.
     */
    protected EscherContainerRecord _escherContainer;

    /**
     * Parent of this shape.
     * <code>null</code> for the topmost shapes.
     */
    protected Shape _parent;

    /**
     * The <code>Sheet</code> this shape belongs to
     */
    protected Sheet _sheet;

    /**
     * Fill
     */
    protected Fill _fill;
}
