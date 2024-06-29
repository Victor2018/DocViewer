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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherChildAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpgrRecord;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.Rectangle2D;

/**
 *  Represents a group of shapes.
 *
 * @author Yegor Kozlov
 */
public class ShapeGroup extends Shape
{
    /**
      * Create a new ShapeGroup. This constructor is used when a new shape is created.
      *
      */
    public ShapeGroup()
    {
        this(null, null);
        _escherContainer = createSpContainer(false);
    }

    /**
      * Create a ShapeGroup object and initilize it from the supplied Record container.
      *
      * @param escherRecord       <code>EscherSpContainer</code> container which holds information about this shape
      * @param parent    the parent of the shape
      */
    protected ShapeGroup(EscherContainerRecord escherRecord, Shape parent)
    {
        super(escherRecord, parent);
    }

    /**
     * @return id for the shape.
     */
    public int getShapeId()
    {
        Iterator<EscherRecord> iter = _escherContainer.getChildIterator();

        // group shape ID
        int grpShapeID = 0;
        if (iter.hasNext())
        {
            EscherRecord r = iter.next();
            if (r instanceof EscherContainerRecord)
            {
                // Create the Shape for it
                EscherContainerRecord container = (EscherContainerRecord)r;
                EscherSpRecord spRecord = container.getChildById(EscherSpRecord.RECORD_ID);
                grpShapeID = spRecord.getShapeId();
            }
        }      
        return grpShapeID;
    }
    
    /**
     * @return the shapes contained in this group container
     */
    public Shape[] getShapes()
    {
        // Out escher container record should contain several
        //  SpContainers, the first of which is the group shape itself
        Iterator<EscherRecord> iter = _escherContainer.getChildIterator();
        
        // Don't include the first SpContainer, it is always NotPrimitive
        if (iter.hasNext())
        {
            iter.next();
        }
        List<Shape> shapeList = new ArrayList<Shape>();
        while (iter.hasNext())
        {
            EscherRecord r = iter.next();
            if (r instanceof EscherContainerRecord)
            {
                // Create the Shape for it
                EscherContainerRecord container = (EscherContainerRecord)r;
                Shape shape = ShapeFactory.createShape(container, this);
                shape.setSheet(getSheet());
                shapeList.add(shape);
            }
        }

        // Put the shapes into an array, and return
        Shape[] shapes = shapeList.toArray(new Shape[shapeList.size()]);
        return shapes;
    }

    /**
     * Sets the anchor (the bounding box rectangle) of this shape.
     * All coordinates should be expressed in Master units (576 dpi).
     *
     * @param anchor new anchor
     */
    public void setAnchor(Rectangle anchor)
    {
        EscherContainerRecord spContainer = (EscherContainerRecord)_escherContainer.getChild(0);

        EscherClientAnchorRecord clientAnchor = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
            spContainer, EscherClientAnchorRecord.RECORD_ID);
        //hack. internal variable EscherClientAnchorRecord.shortRecord can be
        //initialized only in fillFields(). We need to set shortRecord=false;
        byte[] header = new byte[16];
        LittleEndian.putUShort(header, 0, 0);
        LittleEndian.putUShort(header, 2, 0);
        LittleEndian.putInt(header, 4, 8);
        clientAnchor.fillFields(header, 0, null);

        clientAnchor.setFlag((short)(anchor.y * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        clientAnchor.setCol1((short)(anchor.x * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        clientAnchor.setDx1((short)((anchor.width + anchor.x) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        clientAnchor.setRow1((short)((anchor.height + anchor.y) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));

        EscherSpgrRecord spgr = (EscherSpgrRecord)ShapeKit.getEscherChild(spContainer,
            EscherSpgrRecord.RECORD_ID);

        spgr.setRectX1((int)(anchor.x * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        spgr.setRectY1((int)(anchor.y * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        spgr.setRectX2((int)((anchor.x + anchor.width) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        spgr.setRectY2((int)((anchor.y + anchor.height) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
    }

    /**
     * Sets the coordinate space of this group.  All children are constrained
     * to these coordinates.
     *
     * @param anchor the coordinate space of this group
     */
    public void setCoordinates(Rectangle2D anchor)
    {
        EscherContainerRecord spContainer = (EscherContainerRecord)_escherContainer.getChild(0);
        EscherSpgrRecord spgr = (EscherSpgrRecord)ShapeKit.getEscherChild(spContainer,
            EscherSpgrRecord.RECORD_ID);

        int x1 = (int)Math.round(anchor.getX() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI);
        int y1 = (int)Math.round(anchor.getY() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI);
        int x2 = (int)Math.round((anchor.getX() + anchor.getWidth()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI);
        int y2 = (int)Math.round((anchor.getY() + anchor.getHeight()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI);

        spgr.setRectX1(x1);
        spgr.setRectY1(y1);
        spgr.setRectX2(x2);
        spgr.setRectY2(y2);
    }

    /**
     * Gets the coordinate space of this group.  All children are constrained
     * to these coordinates.
     *
     * @return the coordinate space of this group
     */
    public Rectangle2D getCoordinates()
    {
        EscherContainerRecord spContainer = (EscherContainerRecord)_escherContainer.getChild(0);
        EscherSpgrRecord spgr = (EscherSpgrRecord)ShapeKit.getEscherChild(spContainer,
            EscherSpgrRecord.RECORD_ID);

        Rectangle2D.Float anchor = new Rectangle2D.Float();
        anchor.x = (float)spgr.getRectX1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI;
        anchor.y = (float)spgr.getRectY1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI;
        anchor.width = (float)(spgr.getRectX2() - spgr.getRectX1()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI;
        anchor.height = (float)(spgr.getRectY2() - spgr.getRectY1()) * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI;

        return anchor;
    }
    
    /**
     * 
     * @return
     */
    public Rectangle2D getClientAnchor2D(Shape shape)
    {
        Rectangle2D anchor = shape.getAnchor2D();
        if (shape != null && shape.getParent() != null)
        {
            Rectangle2D clientAnchor = ((ShapeGroup)shape.getParent()).getClientAnchor2D(shape.getParent());
            Rectangle2D spgrAnchor = ((ShapeGroup)shape.getParent()).getCoordinates();

            double scalex = spgrAnchor.getWidth() / clientAnchor.getWidth();
            double scaley = spgrAnchor.getHeight() / clientAnchor.getHeight();

            double x = clientAnchor.getX() + (anchor.getX() - spgrAnchor.getX()) / scalex;
            double y = clientAnchor.getY() + (anchor.getY() - spgrAnchor.getY()) / scaley;
            double width = anchor.getWidth() / scalex;
            double height = anchor.getHeight() / scaley;

            anchor = new Rectangle2D.Double(x, y, width, height);
        }
        return anchor;
    }

    /**
     * Create a new ShapeGroup and create an instance of <code>EscherSpgrContainer</code> which represents a group of shapes
     */
    protected EscherContainerRecord createSpContainer(boolean isChild)
    {
        EscherContainerRecord spgr = new EscherContainerRecord();
        spgr.setRecordId(EscherContainerRecord.SPGR_CONTAINER);
        spgr.setOptions((short)15);

        //The group itself is a shape, and always appears as the first EscherSpContainer in the group container.
        EscherContainerRecord spcont = new EscherContainerRecord();
        spcont.setRecordId(EscherContainerRecord.SP_CONTAINER);
        spcont.setOptions((short)15);

        EscherSpgrRecord spg = new EscherSpgrRecord();
        spg.setOptions((short)1);
        spcont.addChildRecord(spg);

        EscherSpRecord sp = new EscherSpRecord();
        short type = (ShapeTypes.NotPrimitive << 4) + 2;
        sp.setOptions(type);
        sp.setFlags(EscherSpRecord.FLAG_HAVEANCHOR | EscherSpRecord.FLAG_GROUP);
        spcont.addChildRecord(sp);

        EscherClientAnchorRecord anchor = new EscherClientAnchorRecord();
        spcont.addChildRecord(anchor);

        spgr.addChildRecord(spcont);
        return spgr;
    }

    /**
     * Add a shape to this group.
     *
     * @param shape - the Shape to add
     */
    public void addShape(Shape shape)
    {
        _escherContainer.addChildRecord(shape.getSpContainer());

        Sheet sheet = getSheet();
        shape.setSheet(sheet);
        shape.setShapeId(sheet.allocateShapeId());
        shape.afterInsert(sheet);
    }

    /**
     * Moves this <code>ShapeGroup</code> to the specified location.
     * <p>
     * @param x the x coordinate of the top left corner of the shape in new location
     * @param y the y coordinate of the top left corner of the shape in new location
     */
    public void moveTo(int x, int y)
    {
        Rectangle anchor = getAnchor();
        int dx = x - anchor.x;
        int dy = y - anchor.y;
        anchor.translate(dx, dy);
        setAnchor(anchor);

        Shape[] shape = getShapes();
        for (int i = 0; i < shape.length; i++)
        {
            Rectangle chanchor = shape[i].getAnchor();
            chanchor.translate(dx, dy);
            shape[i].setAnchor(chanchor);
        }
    }

    /**
     * Returns the anchor (the bounding box rectangle) of this shape group.
     * All coordinates are expressed in points (72 dpi).
     *
     * @return the anchor of this shape group
     */
    public Rectangle2D getAnchor2D()
    {
        EscherContainerRecord spContainer = (EscherContainerRecord)_escherContainer.getChild(0);
        EscherClientAnchorRecord clientAnchor = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
            spContainer, EscherClientAnchorRecord.RECORD_ID);
        Rectangle2D.Float anchor = new Rectangle2D.Float();
        if (clientAnchor == null)
        {
            /*logger
                .log(POILogger.INFO,
                    "EscherClientAnchorRecord was not found for shape group. Searching for EscherChildAnchorRecord.");*/
            EscherChildAnchorRecord rec = (EscherChildAnchorRecord)ShapeKit.getEscherChild(spContainer,
                EscherChildAnchorRecord.RECORD_ID);
            anchor = new Rectangle2D.Float((float)rec.getDx1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI,
                (float)rec.getDy1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI, (float)(rec.getDx2() - rec.getDx1())
                    * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI, (float)(rec.getDy2() - rec.getDy1()) * MainConstant.POINT_DPI
                    / ShapeKit.MASTER_DPI);
        }
        else
        {
            anchor.x = (float)clientAnchor.getCol1() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI;
            anchor.y = (float)clientAnchor.getFlag() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI;
            anchor.width = (float)(clientAnchor.getDx1() - clientAnchor.getCol1()) * MainConstant.POINT_DPI
                / ShapeKit.MASTER_DPI;
            anchor.height = (float)(clientAnchor.getRow1() - clientAnchor.getFlag()) * MainConstant.POINT_DPI
                / ShapeKit.MASTER_DPI;
        }

        return anchor;
    }

    /**
     * Return type of the shape.
     * In most cases shape group type is {@link com.nvqquy98.lib.doc.office.fc.hslf.model.ShapeTypes#NotPrimitive}
     *
     * @return type of the shape.
     */
    public int getShapeType()
    {
        EscherContainerRecord groupInfoContainer = (EscherContainerRecord)_escherContainer
            .getChild(0);
        EscherSpRecord spRecord = groupInfoContainer.getChildById(EscherSpRecord.RECORD_ID);
        return spRecord.getOptions() >> 4;
    }

    /**
     * Returns <code>null</code> - shape groups can't have hyperlinks
     *
     * @return <code>null</code>.
     */
    public Hyperlink getHyperlink()
    {
        return null;
    }

    /**
     * Whether the shape is horizontally flipped
     *
     * @return whether the shape is horizontally flipped
     */
    public boolean getFlipHorizontal()
    {
        return ShapeKit.getGroupFlipHorizontal(getSpContainer());
    }

    /**
     * Whether the shape is vertically flipped
     *
     * @return whether the shape is vertically flipped
     */
    public boolean getFlipVertical()
    {
        return ShapeKit.getGroupFlipVertical(getSpContainer());
    }
    
    /**
     * Rotation angle in degrees
     *
     * @return rotation angle in degrees
     */
    public int getRotation()
    {
        return ShapeKit.getGroupRotation(getSpContainer());
    }
    
    public void dispose()
    {
        super.dispose();
    }
}
