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
import java.util.Arrays;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherArrayProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.java.awt.geom.GeneralPath;
import com.nvqquy98.lib.doc.office.java.awt.geom.PathIterator;
import com.nvqquy98.lib.doc.office.java.awt.geom.Point2D;
import com.nvqquy98.lib.doc.office.java.awt.geom.Rectangle2D;

import android.graphics.Path;
import android.graphics.PointF;


/**
 * A "Freeform" shape.
 *
 * <p>
 * Shapes drawn with the "Freeform" tool have cubic bezier curve segments in the smooth sections
 * and straight-line segments in the straight sections. This object closely corresponds to <code>java.awt.geom.GeneralPath</code>.
 * </p>
 * @author Yegor Kozlov
 */
public final class Freeform extends AutoShape
{
    public static final byte[] SEGMENTINFO_MOVETO = new byte[]{0x00, 0x40};
    public static final byte[] SEGMENTINFO_LINETO = new byte[]{0x00, (byte)0xAC};
    public static final byte[] SEGMENTINFO_LINETO2 = new byte[]{0x00, (byte)0xB0};
    public static final byte[] SEGMENTINFO_ESCAPE = new byte[]{0x01, 0x00};
    public static final byte[] SEGMENTINFO_ESCAPE1 = new byte[]{0x03, 0x00};
    public static final byte[] SEGMENTINFO_ESCAPE2 = new byte[]{0x01, 0x20};
    public static final byte[] SEGMENTINFO_CUBICTO = new byte[]{0x00, (byte)0xAD};
    public static final byte[] SEGMENTINFO_CUBICTO1 = new byte[]{0x00, (byte)0xAF};
    public static final byte[] SEGMENTINFO_CUBICTO2 = new byte[]{0x00, (byte)0xB3}; //OpenOffice inserts 0xB3 instead of 0xAD.
    public static final byte[] SEGMENTINFO_CLOSE = new byte[]{0x01, (byte)0x60};
    public static final byte[] SEGMENTINFO_END = new byte[]{0x00, (byte)0x80};
    
    /**
     * Create a Freeform object and initialize it from the supplied Record container.
     *
     * @param escherRecord       <code>EscherSpContainer</code> container which holds information about this shape
     * @param parent    the parent of the shape
     */
    protected Freeform(EscherContainerRecord escherRecord, Shape parent)
    {
        super(escherRecord, parent);
    }
    
    /**
     * Gets the freeform path
     * @param rect
     * @param startArrowTailCenter
     * @param endArrowTailCenter
     * @return
     */
    public Path[] getFreeformPath(Rectangle rect, PointF startArrowTailCenter, byte startArrowType,  PointF endArrowTailCenter, byte endArrowType)
    {
        return ShapeKit.getFreeformPath(getSpContainer(), rect, startArrowTailCenter, startArrowType, endArrowTailCenter, endArrowType);
    }

    
    /************************************/
    /**
     * Create a new Freeform. This constructor is used when a new shape is created.
     *
     * @param parent    the parent of this Shape. For example, if this text box is a cell
     * in a table then the parent is Table.
     */
    public Freeform(Shape parent)
    {
        super(null, parent);
        _escherContainer = createSpContainer(ShapeTypes.NotPrimitive, parent instanceof ShapeGroup);
    }

    /**
     * Create a new Freeform. This constructor is used when a new shape is created.
     *
     */
    public Freeform()
    {
        this(null);
    }

    /**
     * Set the shape path
     *
     * @param path
     */
    public void setPath(GeneralPath path)
    {
        Rectangle2D bounds = path.getBounds2D();
        PathIterator it = path.getPathIterator(new AffineTransform());

        List<byte[]> segInfo = new ArrayList<byte[]>();
        List<Point2D.Double> pntInfo = new ArrayList<Point2D.Double>();
        boolean isClosed = false;
        while (!it.isDone())
        {
            double[] vals = new double[6];
            int type = it.currentSegment(vals);
            switch (type)
            {
                case PathIterator.SEG_MOVETO:
                    pntInfo.add(new Point2D.Double(vals[0], vals[1]));
                    segInfo.add(ShapeKit.SEGMENTINFO_MOVETO);
                    break;
                case PathIterator.SEG_LINETO:
                    pntInfo.add(new Point2D.Double(vals[0], vals[1]));
                    segInfo.add(ShapeKit.SEGMENTINFO_LINETO);
                    segInfo.add(ShapeKit.SEGMENTINFO_ESCAPE);
                    break;
                case PathIterator.SEG_CUBICTO:
                    pntInfo.add(new Point2D.Double(vals[0], vals[1]));
                    pntInfo.add(new Point2D.Double(vals[2], vals[3]));
                    pntInfo.add(new Point2D.Double(vals[4], vals[5]));
                    segInfo.add(ShapeKit.SEGMENTINFO_CUBICTO);
                    segInfo.add(ShapeKit.SEGMENTINFO_ESCAPE2);
                    break;
                case PathIterator.SEG_QUADTO:
                    //TODO: figure out how to convert SEG_QUADTO into SEG_CUBICTO
                    break;
                case PathIterator.SEG_CLOSE:
                    pntInfo.add(pntInfo.get(0));
                    segInfo.add(ShapeKit.SEGMENTINFO_LINETO);
                    segInfo.add(ShapeKit.SEGMENTINFO_ESCAPE);
                    segInfo.add(ShapeKit.SEGMENTINFO_LINETO);
                    segInfo.add(ShapeKit.SEGMENTINFO_CLOSE);
                    isClosed = true;
                    break;
            }

            it.next();
        }
        if (!isClosed)
            segInfo.add(ShapeKit.SEGMENTINFO_LINETO);
        segInfo.add(new byte[]{0x00, (byte)0x80});

        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GEOMETRY__SHAPEPATH, 0x4));

        EscherArrayProperty verticesProp = new EscherArrayProperty(
            (short)(EscherProperties.GEOMETRY__VERTICES + 0x4000), false, null);
        verticesProp.setNumberOfElementsInArray(pntInfo.size());
        verticesProp.setNumberOfElementsInMemory(pntInfo.size());
        verticesProp.setSizeOfElements(0xFFF0);
        for (int i = 0; i < pntInfo.size(); i++)
        {
            Point2D.Double pnt = pntInfo.get(i);
            byte[] data = new byte[4];
            LittleEndian.putShort(data, 0,
                (short)((pnt.getX() - bounds.getX()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
            LittleEndian.putShort(data, 2,
                (short)((pnt.getY() - bounds.getY()) * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
            verticesProp.setElement(i, data);
        }
        opt.addEscherProperty(verticesProp);

        EscherArrayProperty segmentsProp = new EscherArrayProperty(
            (short)(EscherProperties.GEOMETRY__SEGMENTINFO + 0x4000), false, null);
        segmentsProp.setNumberOfElementsInArray(segInfo.size());
        segmentsProp.setNumberOfElementsInMemory(segInfo.size());
        segmentsProp.setSizeOfElements(0x2);
        for (int i = 0; i < segInfo.size(); i++)
        {
            byte[] seg = segInfo.get(i);
            segmentsProp.setElement(i, seg);
        }
        opt.addEscherProperty(segmentsProp);

        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GEOMETRY__RIGHT,
            (int)(bounds.getWidth() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI)));
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GEOMETRY__BOTTOM,
            (int)(bounds.getHeight() * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI)));

        opt.sortProperties();

        setAnchor(bounds);
    }

    /**
     * Gets the freeform path
     *
     * @return the freeform path
     */
    public GeneralPath getPath()
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GEOMETRY__SHAPEPATH, 0x4));

        EscherArrayProperty verticesProp = (EscherArrayProperty)ShapeKit.getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__VERTICES + 0x4000));
        if (verticesProp == null)
            verticesProp = (EscherArrayProperty)ShapeKit.getEscherProperty(opt,
                EscherProperties.GEOMETRY__VERTICES);

        EscherArrayProperty segmentsProp = (EscherArrayProperty)ShapeKit.getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__SEGMENTINFO + 0x4000));
        if (segmentsProp == null)
            segmentsProp = (EscherArrayProperty)ShapeKit.getEscherProperty(opt,
                EscherProperties.GEOMETRY__SEGMENTINFO);

        //sanity check
        if (verticesProp == null)
        {
            return null;
        }
        if (segmentsProp == null)
        {
            return null;
        }

        GeneralPath path = new GeneralPath();
        int numPoints = verticesProp.getNumberOfElementsInArray();
        int numSegments = segmentsProp.getNumberOfElementsInArray();
        for (int i = 0, j = 0; i < numSegments && j < numPoints; i++)
        {
            byte[] elem = segmentsProp.getElement(i);
            if (Arrays.equals(elem, ShapeKit.SEGMENTINFO_MOVETO))
            {
                byte[] p = verticesProp.getElement(j++);
                short x = LittleEndian.getShort(p, 0);
                short y = LittleEndian.getShort(p, 2);
                path.moveTo(((float)x * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI),
                    ((float)y * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI));
            }
            else if (Arrays.equals(elem, ShapeKit.SEGMENTINFO_CUBICTO)
                || Arrays.equals(elem, ShapeKit.SEGMENTINFO_CUBICTO2))
            {
                i++;
                byte[] p1 = verticesProp.getElement(j++);
                short x1 = LittleEndian.getShort(p1, 0);
                short y1 = LittleEndian.getShort(p1, 2);
                byte[] p2 = verticesProp.getElement(j++);
                short x2 = LittleEndian.getShort(p2, 0);
                short y2 = LittleEndian.getShort(p2, 2);
                byte[] p3 = verticesProp.getElement(j++);
                short x3 = LittleEndian.getShort(p3, 0);
                short y3 = LittleEndian.getShort(p3, 2);
                path.curveTo(((float)x1 * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI),
                    ((float)y1 * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI), ((float)x2 * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI),
                    ((float)y2 * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI), ((float)x3 * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI),
                    ((float)y3 * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI));

            }
            else if (Arrays.equals(elem, ShapeKit.SEGMENTINFO_LINETO))
            {
                i++;
                byte[] pnext = segmentsProp.getElement(i);
                if (Arrays.equals(pnext, ShapeKit.SEGMENTINFO_ESCAPE))
                {
                    if (j + 1 < numPoints)
                    {
                        byte[] p = verticesProp.getElement(j++);
                        short x = LittleEndian.getShort(p, 0);
                        short y = LittleEndian.getShort(p, 2);
                        path.lineTo(((float)x * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI),
                            ((float)y * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI));
                    }
                }
                else if (Arrays.equals(pnext, ShapeKit.SEGMENTINFO_CLOSE))
                {
                    path.closePath();
                }
            }
        }
        return path;
    }

    public com.nvqquy98.lib.doc.office.java.awt.Shape getOutline()
    {
        GeneralPath path = getPath();
        Rectangle2D anchor = getAnchor2D();
        Rectangle2D bounds = path.getBounds2D();
        AffineTransform at = new AffineTransform();
        at.translate(anchor.getX(), anchor.getY());
        at.scale(anchor.getWidth() / bounds.getWidth(), anchor.getHeight() / bounds.getHeight());
        return at.createTransformedShape(path);
    }
    
    public ArrowPathAndTail getStartArrowPathAndTail(Rectangle rect)
    {
        return ShapeKit.getStartArrowPathAndTail(_escherContainer, rect);
    }
    
    public ArrowPathAndTail getEndArrowPathAndTail(Rectangle rect)
    {
        return ShapeKit.getEndArrowPathAndTail(_escherContainer, rect);
    }
    
    public void dispose()
    {
        super.dispose();
    }
}
