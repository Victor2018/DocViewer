/*
 * 文件名称:           ShapeKit.java
 *  
 * 编译器:             android2.2
 * 时间:               下午1:41:20
 */
package com.nvqquy98.lib.doc.office.fc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.LineArrowPathBuilder;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.RadialGradientShader;
import com.nvqquy98.lib.doc.office.common.shape.Arrow;
import com.nvqquy98.lib.doc.office.constant.AutoShapeConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ddf.AbstractEscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherArrayProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherComplexProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherTertiaryOptRecord;
import com.nvqquy98.lib.doc.office.fc.hslf.model.Sheet;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ColorSchemeAtom;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.HWPFShape;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;
import com.nvqquy98.lib.doc.office.fc.util.StringUtil;
import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.AWorkbook;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class ShapeKit
{
    /**
     * In Escher absolute distances are specified in
     * English Metric Units (EMUs), occasionally referred to as A units;
     * there are 360000 EMUs per centimeter, 914400 EMUs per inch, 12700 EMUs per point.
     */
    public static final int EMU_PER_INCH = 914400;
    public static final int EMU_PER_POINT = 12700;
    public static final int EMU_PER_CENTIMETER = 360000;
    public static final int DefaultMargin_Twip = 72;
    public static final float DefaultMargin_Pixel = DefaultMargin_Twip * MainConstant.TWIPS_TO_PIXEL;
    
    /**
     * Master DPI (576 pixels per inch).
     * Used by the reference coordinate system in PowerPoint.
     */
    public static final int MASTER_DPI = 576;
    
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
            {
                return escherRecord;
            }
        }
        return null;
    }
    
    /**
     * Returns  escher property by id.
     *
     * @return escher property or <code>null</code> if not found.
     */
    public static EscherProperty getEscherProperty(AbstractEscherOptRecord opt, int propId)
    {
        if (opt != null)
        {
            for (Iterator<EscherProperty> iterator = opt.getEscherProperties().iterator(); iterator.hasNext();)
            {
                EscherProperty prop = (EscherProperty)iterator.next();
                if (prop.getPropertyNumber() == propId)
                {
                    return prop;
                }
            }
        }
        return null;
    }
    
    /**
     * Get the value of a simple escher property for this shape.
     *
     * @param propId    The id of the property. One of the constants defined in EscherOptRecord.
     */
    public static int getEscherProperty(EscherContainerRecord escherContainer, short propId, int defaultValue)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, propId);
            if (prop != null)
            {
                return prop.getPropertyValue();
            }
        }
        return defaultValue;
    }
    
    /**
     * Get the value of a simple escher property for this shape.
     *
     * @param propId    The id of the property. One of the constants defined in EscherOptRecord.
     */
    public static int getEscherProperty(EscherContainerRecord escherContainer, short propId)
    {
        return getEscherProperty(escherContainer, propId, 0);
    }
    
    /**
     * @return type of the shape.
     */
    public static int getShapeType(EscherContainerRecord escherContainer)
    {
        EscherSpRecord spRecord = escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        if (spRecord != null)
        {
            return spRecord.getOptions() >> 4;
        }
        return -1;
    }
    
    /**
     * @return id for the shape.
     */
    public static int getShapeId(EscherContainerRecord escherContainer)
    {
        EscherSpRecord spRecord = escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        return spRecord == null ? 0 : spRecord.getShapeId();
    }
    
    /**
     * shape of normal slide to master shape ID
     * @param escherContainer
     * @return
     */
    public static int getMasterShapeID(EscherContainerRecord escherContainer)
    {    	
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
                EscherOptRecord.RECORD_ID);
    	if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, 
                (short)(EscherProperties.SHAPE__MASTER));
            if (prop != null)
            {
                return prop.getPropertyValue();
            }
        }
    	
    	return 0;
    }
    
    /**
     * 
     * @return
     */
    public static boolean isHidden(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, 
                (short)(EscherProperties.GROUPSHAPE__PRINT));
            if (prop != null)
            {
                return prop.getPropertyValue() == 0x00020002 ? true : false;
            }
        }
        return false;
    }
    
    /**
     *  has line or not
     */
    public static boolean hasLine(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
            EscherProperties.LINESTYLE__NOLINEDRAWDASH);
        if(prop != null)
        {
        	return (prop.getPropertyValue() & 0xFF) != 0;
        }
        
        return true;
    }
    
    /**
     *  Returns width of the line in in points
     */
    public static int getLineWidth(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
            EscherProperties.LINESTYLE__LINEWIDTH);
        int width = prop == null ? 1 : prop.getPropertyValue()
            / AutoShapeConstant.LINEWIDTH_DEFAULT;
        return width;
    }
    
    /**
     *  Returns line dashing type
     */
    public static int getLineDashing(EscherContainerRecord _escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
                EscherOptRecord.RECORD_ID);

            EscherSimpleProperty prop = (EscherSimpleProperty)ShapeKit.getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINEDASHING);
            return prop == null ? AutoShapeConstant.LINESTYLE_SOLID : prop.getPropertyValue();
    }
    
    public static int getStartArrowType(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINESTARTARROWHEAD);
            if (prop != null)
            {
                return prop.getPropertyValue();
            }
        }
        return 0;
    }
    
    public static int getStartArrowWidth(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINESTARTARROWWIDTH);
            if (prop != null)
            {
                return prop.getPropertyValue();
            }
        }
        return 1;
    }
    
    public static int getStartArrowLength(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINEESTARTARROWLENGTH);
            if (prop != null)
            {
                return prop.getPropertyValue();
            }
        }
        return 1;
    }    
    
    public static ArrowPathAndTail getStartArrowPathAndTail(EscherContainerRecord _escherContainer, Rectangle rect)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GEOMETRY__SHAPEPATH, 0x4));

        EscherArrayProperty verticesProp = (EscherArrayProperty)getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__VERTICES + 0x4000));
        if (verticesProp == null)
        {
            verticesProp = (EscherArrayProperty)getEscherProperty(opt,
                EscherProperties.GEOMETRY__VERTICES);
        }

        EscherArrayProperty segmentsProp = (EscherArrayProperty)getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__SEGMENTINFO + 0x4000));
        if (segmentsProp == null)
        {
            segmentsProp = (EscherArrayProperty)getEscherProperty(opt,
                EscherProperties.GEOMETRY__SEGMENTINFO);
        }

        //sanity check
        if (verticesProp == null)
        {
            return null;
        }
        if (segmentsProp == null)
        {
            return null;
        }
        
        float w = 0;
        float h = 0;
        EscherSimpleProperty rightProp = 
            (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__RIGHT));        
        EscherSimpleProperty bottomProp = 
            (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__BOTTOM));
        
        if (rightProp != null)
        {
            w = rightProp.getPropertyValue();
        }
        if (bottomProp != null)
        {
            h = bottomProp.getPropertyValue();
        }
        Matrix m = new Matrix();
        if (w > 0 && h > 0)
        {
            m.postScale(rect.width / w, rect.height / h);
        }
        
        ArrowPathAndTail arrowPathAndTail = null;
        //start arrow head
        EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, 
            (short)(EscherProperties.LINESTYLE__LINESTARTARROWHEAD));
        if (prop != null && prop.getPropertyValue() > 0)
        {
            Arrow arrow = new Arrow((byte)prop.getPropertyValue(), 
                getStartArrowWidth(_escherContainer), 
                getStartArrowLength(_escherContainer));
            
            int lineWidth = (int)Math.round(getLineWidth(_escherContainer) * MainConstant.POINT_TO_PIXEL);
            int numPoints = verticesProp.getNumberOfElementsInArray();
            int numSegments = segmentsProp.getNumberOfElementsInArray();
            
            float p0X = 0;
            float p0Y = 0;
            
            byte[] p = verticesProp.getElement(0);
            if(p.length == 8)
            {
            	p0X = LittleEndian.getInt(p, 0);
            	p0Y = LittleEndian.getInt(p, 4);
            }
            else
            {
            	p0X = LittleEndian.getShort(p, 0);
            	p0Y = LittleEndian.getShort(p, 2);
            }    
            
            byte[] elem = segmentsProp.getElement(1);
            if (Arrays.equals(elem, SEGMENTINFO_CUBICTO)
                || Arrays.equals(elem, SEGMENTINFO_CUBICTO1)
                || Arrays.equals(elem, SEGMENTINFO_CUBICTO2)
                || Arrays.equals(elem, SEGMENTINFO_ESCAPE2))
            {
                //i++;
                if (1 + 3 <= numPoints)
                {
                	 byte[] p1 = verticesProp.getElement(1);
                	 byte[] p2 = verticesProp.getElement(2);
                	 byte[] p3 = verticesProp.getElement(3);
                	 float x1 = 0;
                	 float y1 = 0;                     
                     float x2 = 0;
                     float y2 = 0;                     
                     float x3 = 0;
                     float y3 = 0;
                     
                	if(p1.length == 8 && p2.length == 8 && p3.length == 8)
                    {
                        x1 = LittleEndian.getInt(p1, 0);
                        y1 = LittleEndian.getInt(p1, 4);
                        x2 = LittleEndian.getInt(p2, 0);
                        y2 = LittleEndian.getInt(p2, 4);
                        x3 = LittleEndian.getInt(p3, 0);
                        y3 = LittleEndian.getInt(p3, 4);
                    }
                    else
                    {
                        x1 = LittleEndian.getShort(p1, 0);
                        y1 = LittleEndian.getShort(p1, 2);
                        x2 = LittleEndian.getShort(p2, 0);
                        y2 = LittleEndian.getShort(p2, 2);
                        x3 = LittleEndian.getShort(p3, 0);
                        y3 = LittleEndian.getShort(p3, 2);
                    }                   
                    arrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(x3, y3, x2, y2, x1, y1, p0X, p0Y, arrow, (int)(lineWidth * w /rect.width));
                }
            }
            else if (Arrays.equals(elem, SEGMENTINFO_LINETO)
                    || Arrays.equals(elem, SEGMENTINFO_LINETO2)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE1))
            {
                if (2 <= numPoints)
                {
                    p = verticesProp.getElement(1);
                    float x, y;
                    if(p.length == 8)
                    {
                        x = LittleEndian.getInt(p, 0);
                        y = LittleEndian.getInt(p, 4);
                    }
                    else
                    {
                        x = LittleEndian.getShort(p, 0);
                        y = LittleEndian.getShort(p, 2);
                    }
                    arrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(x, y, p0X, p0Y, arrow,  (int)(lineWidth * w /rect.width));
                }
            }
        }
        
        if(arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null)
        {
        	arrowPathAndTail.getArrowPath().transform(m);
        }
        
        return arrowPathAndTail;
    }
    
    public static int getEndArrowType(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
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
    
    public static int getEndArrowWidth(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINEENDARROWWIDTH);
            if (prop != null)
            {
                return prop.getPropertyValue();
            }
        }
        return 1;
    }
    
    public static int getEndArrowLength(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.LINESTYLE__LINEENDARROWLENGTH);
            if (prop != null)
            {
                return prop.getPropertyValue();
            }
        }
        return 1;
    }
    
    private static int getRealSegmentsCount(EscherArrayProperty verticesProp, EscherArrayProperty segmentsProp)
    {
        int numPoints = verticesProp.getNumberOfElementsInArray();
        int numSegments = segmentsProp.getNumberOfElementsInArray();
        int realSegments = 0;
        for (int i = 0, j = 0; i < numSegments && j < numPoints; i++)
        {
            byte[] elem = segmentsProp.getElement(i);
            if (Arrays.equals(elem, SEGMENTINFO_MOVETO))
            {
                j++;
            }
            else if (Arrays.equals(elem, SEGMENTINFO_CUBICTO)
                || Arrays.equals(elem, SEGMENTINFO_CUBICTO1)
                || Arrays.equals(elem, SEGMENTINFO_CUBICTO2)
                || Arrays.equals(elem, SEGMENTINFO_ESCAPE2))
            {
                //i++;
                if (j + 3 <= numPoints)
                {
                    j += 3;
                    realSegments++;
                }
            }
            else if (Arrays.equals(elem, SEGMENTINFO_LINETO)
                    || Arrays.equals(elem, SEGMENTINFO_LINETO2)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE1))
            {
                if (j + 1 <= numPoints)
                {
                    j++;
                    realSegments++;
                }
            }
        }
        
        return realSegments + 1;
    }
    
    public static ArrowPathAndTail getEndArrowPathAndTail(EscherContainerRecord _escherContainer, Rectangle rect)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GEOMETRY__SHAPEPATH, 0x4));

        EscherArrayProperty verticesProp = (EscherArrayProperty)getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__VERTICES + 0x4000));
        if (verticesProp == null)
        {
            verticesProp = (EscherArrayProperty)getEscherProperty(opt,
                EscherProperties.GEOMETRY__VERTICES);
        }

        EscherArrayProperty segmentsProp = (EscherArrayProperty)getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__SEGMENTINFO + 0x4000));
        if (segmentsProp == null)
        {
            segmentsProp = (EscherArrayProperty)getEscherProperty(opt,
                EscherProperties.GEOMETRY__SEGMENTINFO);
        }

        //sanity check
        if (verticesProp == null)
        {
            return null;
        }
        if (segmentsProp == null)
        {
            return null;
        }
        
        float w = 0;
        float h = 0;
        EscherSimpleProperty rightProp = 
            (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__RIGHT));        
        EscherSimpleProperty bottomProp = 
            (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__BOTTOM));
        
        if (rightProp != null)
        {
            w = rightProp.getPropertyValue();
        }
        if (bottomProp != null)
        {
            h = bottomProp.getPropertyValue();
        }
        Matrix m = new Matrix();
        if (w > 0 && h > 0)
        {
            m.postScale(rect.width / w, rect.height / h);
        }
        
        ArrowPathAndTail arrowPathAndTail = null;
        //start arrow head
        EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, 
            (short)(EscherProperties.LINESTYLE__LINEENDARROWHEAD));
        if (prop != null && prop.getPropertyValue() > 0)
        {
            Arrow arrow = new Arrow((byte)prop.getPropertyValue(), 
                getEndArrowWidth(_escherContainer), 
                getEndArrowLength(_escherContainer));
            
            int lineWidth = (int)Math.round(getLineWidth(_escherContainer) * MainConstant.POINT_TO_PIXEL);
            
            int numPoints = verticesProp.getNumberOfElementsInArray();
            int numSegments = segmentsProp.getNumberOfElementsInArray();
            int i = 0;
            for (int j = 0; i < numSegments && j < numPoints; i++)
            {
                byte[] elem = segmentsProp.getElement(i);
                if (Arrays.equals(elem, SEGMENTINFO_MOVETO))
                {
                    j++;
                }
                else if (Arrays.equals(elem, SEGMENTINFO_CUBICTO)
                    || Arrays.equals(elem, SEGMENTINFO_CUBICTO1)
                    || Arrays.equals(elem, SEGMENTINFO_CUBICTO2)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE2))
                {
                    //i++;
                    if (j + 3 <= numPoints)
                    {
                        j += 3;
                    }
                }
                else if (Arrays.equals(elem, SEGMENTINFO_LINETO)
                        || Arrays.equals(elem, SEGMENTINFO_LINETO2)
                        || Arrays.equals(elem, SEGMENTINFO_ESCAPE)
                        || Arrays.equals(elem, SEGMENTINFO_ESCAPE1))
                {
                    if (j + 1 <= numPoints)
                    {
                        j++;
                    }
                }
            }
            //start point
            byte[] elem = segmentsProp.getElement(i); 
            while((Arrays.equals(elem, SEGMENTINFO_CLOSE) || Arrays.equals(elem, SEGMENTINFO_END)) && i > 0)
            {
            	elem = segmentsProp.getElement(i--);
            }
            
            if (Arrays.equals(elem, SEGMENTINFO_CUBICTO)
                || Arrays.equals(elem, SEGMENTINFO_CUBICTO1)
                || Arrays.equals(elem, SEGMENTINFO_CUBICTO2)
                || Arrays.equals(elem, SEGMENTINFO_ESCAPE2))
            {
                byte[] p0 = verticesProp.getElement(numPoints - 4);
                byte[] p1 = verticesProp.getElement(numPoints - 3);
                byte[] p2 = verticesProp.getElement(numPoints - 2);
                byte[] p3 = verticesProp.getElement(numPoints - 1);
                
                float x0 = 0;
           	 	float y0 = 0;  
           	 	float x1 = 0;
           	 	float y1 = 0;                     
                float x2 = 0;
                float y2 = 0;                     
                float x3 = 0;
                float y3 = 0;
                
                if(p0.length == 8 && p1.length == 8 && p2.length == 8 && p3.length == 8)
               {
                	x0 = LittleEndian.getInt(p0, 0);
                    y0 = LittleEndian.getInt(p0, 4);
                    x1 = LittleEndian.getInt(p1, 0);
                    y1 = LittleEndian.getInt(p1, 4);
                    x2 = LittleEndian.getInt(p2, 0);
                    y2 = LittleEndian.getInt(p2, 4);
                    x3 = LittleEndian.getInt(p3, 0);
                    y3 = LittleEndian.getInt(p3, 4);
               }
               else
               {
            	   x0 = LittleEndian.getShort(p0, 0);
                   y0 = LittleEndian.getShort(p0, 2);
                   x1 = LittleEndian.getShort(p1, 0);
                   y1 = LittleEndian.getShort(p1, 2);
                   x2 = LittleEndian.getShort(p2, 0);
                   y2 = LittleEndian.getShort(p2, 2);
                   x3 = LittleEndian.getShort(p3, 0);
                   y3 = LittleEndian.getShort(p3, 2);
               }      
           	
                arrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(x0, y0, x1, y1, x2, y2, x3, y3, arrow, (int)(lineWidth * w /rect.width));
                
            }
            else if (Arrays.equals(elem, SEGMENTINFO_MOVETO)
                    ||Arrays.equals(elem, SEGMENTINFO_LINETO)
                    || Arrays.equals(elem, SEGMENTINFO_LINETO2)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE1))
            {
                byte[] p0 = verticesProp.getElement(numPoints - 2);
                byte[] p1 = verticesProp.getElement(numPoints - 1);

                float x0 = 0;
                float y0 = 0;
                float x1 = 0;
                float y1 = 0;
                if(p0.length == 8 && p1.length == 8)
                {
                 	x0 = LittleEndian.getInt(p0, 0);
                    y0 = LittleEndian.getInt(p0, 4);
                    x1 = LittleEndian.getInt(p1, 0);
                    y1 = LittleEndian.getInt(p1, 4);
                }
                else
                {
             	   	x0 = LittleEndian.getShort(p0, 0);
                    y0 = LittleEndian.getShort(p0, 2);
                    x1 = LittleEndian.getShort(p1, 0);
                    y1 = LittleEndian.getShort(p1, 2);
                }
                
                arrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(x0, y0, x1, y1, arrow, (int)(lineWidth * w /rect.width));
                
            }            
        }
            
        
        if(arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null)
        {
        	arrowPathAndTail.getArrowPath().transform(m);
        }
        
        return arrowPathAndTail;
    }
    
    /**
     * shape has background fill
     * @param escherContainer
     * @return
     */
    public static boolean hasBackgroundFill(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
    	EscherSimpleProperty p2 = (EscherSimpleProperty)getEscherProperty(opt,
	            EscherProperties.FILL__NOFILLHITTEST);
    	
    	int p2val = p2 == null ? 0 : p2.getPropertyValue();
    	
    	return (p2val & 0x10) != 0;
    }
    
    /**
     * Returns fill type.
     * Must be one of the <code>FILL_*</code> constants defined in this class.
     *
     * @return type of fill
     */
    public static int getFillType(EscherContainerRecord escherContainer)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
        EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, EscherProperties.FILL__FILLTYPE);
        return prop == null ? BackgroundAndFill.FILL_SOLID : prop.getPropertyValue();
    }
    
    /**
     * get fill rotation
     * @param escherContainer
     * @return
     */
    public static int getFillAngle(EscherContainerRecord escherContainer)
    {
    	int angle = 0;
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
    	EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, EscherProperties.FILL__ANGLE);
    	if(prop != null)
    	{    		
    		angle = (prop.getPropertyValue() >> 16) % 360;
    	}
    	
    	return angle;
    }
    
    /**
     * 
     * @param escherContainer
     * @return
     */
    public static int getFillFocus(EscherContainerRecord escherContainer)
    {
    	int focus = 0;
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
    	EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, EscherProperties.FILL__FOCUS);
    	if(prop != null)
    	{    		
    		focus = prop.getPropertyValue();
    	}
    	
    	return focus;
    }
    
    public static boolean isShaderPreset(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
    	EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, EscherProperties.FILL__SHADEPRESET);
    	if(prop != null && prop.getPropertyValue() > 0)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    public static int[] getShaderColors(EscherContainerRecord escherContainer)
    {
    	if(isShaderPreset(escherContainer))
    	{
    		EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
    		 EscherArrayProperty shaderColorsProp = (EscherArrayProperty)getEscherProperty(opt,
    		            EscherProperties.FILL__SHADECOLORS);
    		 EscherSimpleProperty p2 = (EscherSimpleProperty)getEscherProperty(opt,
    		            EscherProperties.FILL__NOFILLHITTEST);
    		 int p2val = p2 == null ? 0 : p2.getPropertyValue();
    		        
    		 if(shaderColorsProp != null)
    		 {
    			 int numberOfElements = shaderColorsProp.getNumberOfElementsInArray();
    			 
    			 int[] colors = new int[numberOfElements];
    			 for(int i = 0; i < numberOfElements; i++)
    			 {
    				 byte[] gradienStops = shaderColorsProp.getElement(i);
    				 if(gradienStops.length == 8)
    				 {
    					 //ignore alpha    					 
    					 if((p2val & 0x10) == 0)
    				     {
    						 colors[i] = 0xffffffff;
    				     }
    					 else
    					 {
    						 colors[i] = ColorUtil.rgb(gradienStops[0], gradienStops[1], gradienStops[2]); 
    					 }
    				 }
    			 }
    			 
    			 return colors;
    		 }
    	}
    	
    	return null;
    } 
    
    public static float[] getShaderPositions(EscherContainerRecord escherContainer)
    {
    	if(isShaderPreset(escherContainer))
    	{
    		EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, EscherOptRecord.RECORD_ID);
    		 EscherArrayProperty shaderColorsProp = (EscherArrayProperty)getEscherProperty(opt,
    		            EscherProperties.FILL__SHADECOLORS);
    		 if(shaderColorsProp != null)
    		 {
    			 int numberOfElements = shaderColorsProp.getNumberOfElementsInArray();
    			 
    			 float[] positions = new float[numberOfElements];
    			 for(int i = 0; i < numberOfElements; i++)
    			 {
    				 byte[] gradienStops = shaderColorsProp.getElement(i);
    				 if(gradienStops.length == 8)
    				 {
    					 positions[i] = LittleEndian.getInt(gradienStops, 4) / 65536f;
    				 }
    			 }
    			 
    			 return positions;
    		 }
    	}
    	
    	return null;
    } 
    /**
     * 
     * @param escherContainer
     * @param obj
     * @param type
     * @return
     */
    public static Color getFillbackColor(EscherContainerRecord escherContainer, Object obj, int type)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
                EscherOptRecord.RECORD_ID);
            EscherSimpleProperty p1 = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.FILL__FILLBACKCOLOR);
            EscherSimpleProperty p2 = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.FILL__NOFILLHITTEST);
            EscherSimpleProperty p3 = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.FILL__BACKOPACITY);

            int p2val = p2 == null ? 0 : p2.getPropertyValue();
            int alpha = p3 == null ? 255 : ((p3.getPropertyValue() >> 8) & 0xFF);
            Color clr = null;
            if (p1 != null && ((p2val & 0x10) != 0 || p2val == 0))
            {
            	clr = new Color(getColorByIndex(obj, p1.getPropertyValue(), type, false), alpha);
            }
            else if (p2val == 0 && type == MainConstant.APPLICATION_TYPE_SS)
            {
                clr = new Color(255, 255, 255);
            }
            return clr;
    }
    
    public static int getRadialGradientPositionType(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
                EscherOptRecord.RECORD_ID);
    	EscherSimpleProperty toLeft = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.FILL__TOLEFT);
    	EscherSimpleProperty toTop = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.FILL__TOTOP);
    	EscherSimpleProperty toRight = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.FILL__TORIGHT);
    	EscherSimpleProperty toBottom = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.FILL__TOBOTTOM);
    	
    	
    	if(toLeft != null && toLeft.getPropertyValue() == 65536
    			&& toRight != null && toRight.getPropertyValue() == 65536
    			&& toTop != null && toTop.getPropertyValue() == 65536
    			&& toBottom != null && toBottom.getPropertyValue() == 65536)
    	{
    		return RadialGradientShader.Center_BR;
    	} 
    	else if(toLeft != null && toLeft.getPropertyValue() == 32768
    			&& toRight != null && toRight.getPropertyValue() == 32768
    			&& toTop != null && toTop.getPropertyValue() == 32768
    			&& toBottom != null && toBottom.getPropertyValue() == 32768)
    	{
    		return RadialGradientShader.Center_Center;
    	}
    	else if(toLeft != null && toLeft.getPropertyValue() == 65536
    			&& toRight != null && toRight.getPropertyValue() == 65536)
    	{
    		return RadialGradientShader.Center_TR;
    	}
    	else if(toTop != null && toTop.getPropertyValue() == 65536
    			&& toBottom != null && toBottom.getPropertyValue() == 65536)
    	{
    		return RadialGradientShader.Center_BL;
    	}
    	
    	return RadialGradientShader.Center_TL;
    }
    
    /**
     * Rotation angle in degrees
     *
     * @return rotation angle in degrees
     */
    public static int getRotation(EscherContainerRecord escherContainer)
    {
        int rot = getEscherProperty(escherContainer, EscherProperties.TRANSFORM__ROTATION);
        return (rot >> 16) % 360;
    }
    
    /**
     * Whether the shape is horizontally flipped
     *
     * @return whether the shape is horizontally flipped
     */
    public static boolean getFlipHorizontal(EscherContainerRecord escherContainer)
    {
        EscherSpRecord spRecord = escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        return (spRecord.getFlags() & EscherSpRecord.FLAG_FLIPHORIZ) != 0;
    }

    /**
     * Whether the shape is vertically flipped
     *
     * @return whether the shape is vertically flipped
     */
    public static boolean getFlipVertical(EscherContainerRecord escherContainer)
    {
        EscherSpRecord spRecord = escherContainer.getChildById(EscherSpRecord.RECORD_ID);
        return (spRecord.getFlags() & EscherSpRecord.FLAG_FLIPVERT) != 0;
    }
    
    public static Float[] getAdjustmentValue(EscherContainerRecord escherContainer)
    {
        Float adjusts[] = null;
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        if (opt != null)
        {
            int i = 0;
            int maxIndex = 0;
            Map<Integer, Integer> indexAdjusts = new HashMap<Integer, Integer>();
            EscherSimpleProperty prop = null;
            while (i < 10)
            {
                prop = (EscherSimpleProperty)ShapeKit.getEscherProperty(opt, 
                    (short)(EscherProperties.GEOMETRY__ADJUSTVALUE) + i);
                if (prop != null)
                {
                    indexAdjusts.put(i, prop.getPropertyValue());
                    if (i > maxIndex)
                    {
                        maxIndex = i;
                    }
                }
                i++;
            }
            
            if (indexAdjusts.size() > 0)
            {
                i = 0;
                adjusts = new Float[maxIndex + 1];
                while (i <= maxIndex)
                {
                    Integer value = indexAdjusts.get(i);
                    if (value != null)
                    {
                        adjusts[i] = (float)value / 21600;
                    }
                    i++;
                }
            }
        }
        return adjusts;
    }
    
    /**
     * Gets the freeform path
     *
     * @return the freeform path
     */
    public static Path[] getFreeformPath(EscherContainerRecord escherContainer, Rectangle rect, 
    		PointF startArrowTailCenter, byte startArrowType,  PointF endArrowTailCenter, byte endArrowType)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        opt.addEscherProperty(new EscherSimpleProperty(EscherProperties.GEOMETRY__SHAPEPATH, 0x4));

        EscherArrayProperty verticesProp = (EscherArrayProperty)getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__VERTICES + 0x4000));
        if (verticesProp == null)
        {
            verticesProp = (EscherArrayProperty)getEscherProperty(opt,
                EscherProperties.GEOMETRY__VERTICES);
        }

        EscherArrayProperty segmentsProp = (EscherArrayProperty)getEscherProperty(opt,
            (short)(EscherProperties.GEOMETRY__SEGMENTINFO + 0x4000));
        if (segmentsProp == null)
        {
            segmentsProp = (EscherArrayProperty)getEscherProperty(opt,
                EscherProperties.GEOMETRY__SEGMENTINFO);
        }

        //sanity check
        if (verticesProp == null)
        {
            return null;
        }
        if (segmentsProp == null)
        {
            return null;
        }
        
        float w = 0;
        float h = 0;
        EscherSimpleProperty rightProp = (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__RIGHT));
        
        EscherSimpleProperty bottomProp = (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__BOTTOM));
       
        if(rightProp != null && rightProp.getPropertyValue() == 1000
            && bottomProp != null && bottomProp.getPropertyValue() == 1000)
        {
            float a1 = 0f;
            float a3 =0f;

            EscherSimpleProperty prop = (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__ADJUSTVALUE));
            if(prop != null)
            {
                a1 = prop.getPropertyValue() / 1000f;
            }
            prop = (EscherSimpleProperty)getEscherProperty(opt, (short)(EscherProperties.GEOMETRY__ADJUST3VALUE));
            if(prop != null)
            {
                a3 = prop.getPropertyValue() / 1000f;
            }
            
            List<Path> paths = new ArrayList<Path>();
            Path path = new Path();
            path.moveTo(0, 0);
            path.lineTo(rect.width, 0);
            path.lineTo(rect.width, rect.height * a3);
            path.lineTo(rect.width * a1, rect.height * a3);
            path.lineTo(rect.width * a1, rect.height);
            path.lineTo(0, rect.height);
            path.close();
                       
            paths.add(path);
            
            return paths.toArray(new Path[paths.size()]);
        }
        
        if (rightProp != null)
        {
            w = rightProp.getPropertyValue();
        }
        if (bottomProp != null)
        {
            h = bottomProp.getPropertyValue();
        }
        Matrix m = new Matrix();
        if (w > 0 && h > 0)
        {
            m.postScale(rect.width / w, rect.height / h);
        }

        List<Path> paths = new ArrayList<Path>();
        Path path = null;
        int numPoints = verticesProp.getNumberOfElementsInArray();
        int numSegments = segmentsProp.getNumberOfElementsInArray();
        path = new Path();
        for (int i = 0, j = 0; i < numSegments && j <= numPoints; i++)
        {        	
            byte[] elem = segmentsProp.getElement(i);
            
            if(j == 0 && startArrowTailCenter != null)
        	{
            	if (Arrays.equals(elem, SEGMENTINFO_MOVETO))
            	{
            		byte[] p = verticesProp.getElement(j++);
                    if(p.length == 8)
                    {
                        int x = LittleEndian.getInt(p, 0);
                        int y = LittleEndian.getInt(p, 4);
                        startArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(x, y, 
                        		startArrowTailCenter.x, startArrowTailCenter.y, startArrowType);
                        		
                    }
                    else
                    {
                        short x = LittleEndian.getShort(p, 0);
                        short y = LittleEndian.getShort(p, 2);
                        startArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(x, y, 
                        		startArrowTailCenter.x, startArrowTailCenter.y, startArrowType);
                    }
            		path.moveTo(startArrowTailCenter.x, startArrowTailCenter.y);
            		continue;
            	}
        	}
        	else if(endArrowTailCenter != null)
        	{
        		if((Arrays.equals(elem, SEGMENTINFO_CUBICTO) 
        				|| Arrays.equals(elem, SEGMENTINFO_CUBICTO1) 
        				|| Arrays.equals(elem, SEGMENTINFO_CUBICTO2)
        				|| Arrays.equals(elem, SEGMENTINFO_ESCAPE2))
        				&& j == numPoints - 3)
                {
                    //i++;
                    if (j + 3 <= numPoints)
                    {
                        byte[] p1 = verticesProp.getElement(j++);
                        byte[] p2 = verticesProp.getElement(j++);
                        byte[] p3 = verticesProp.getElement(j++);

                        if(p1.length == 8 && p2.length == 8 && p3.length == 8)
                        {
                            int x1 = LittleEndian.getInt(p1, 0);
                            int y1 = LittleEndian.getInt(p1, 4);
                            int x2 = LittleEndian.getInt(p2, 0);
                            int y2 = LittleEndian.getInt(p2, 4);
                            int x3 = LittleEndian.getInt(p3, 0);
                            int y3 = LittleEndian.getInt(p3, 4);
                            endArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(x3, y3, 
                            		endArrowTailCenter.x, endArrowTailCenter.y, endArrowType);
                            if (path != null)
                            {
                                path.cubicTo(x1, y1, x2, y2, endArrowTailCenter.x, endArrowTailCenter.y);
                                continue;
                            }
                        }
                        else
                        {
                            short x1 = LittleEndian.getShort(p1, 0);
                            short y1 = LittleEndian.getShort(p1, 2);
                            short x2 = LittleEndian.getShort(p2, 0);
                            short y2 = LittleEndian.getShort(p2, 2);
                            short x3 = LittleEndian.getShort(p3, 0);
                            short y3 = LittleEndian.getShort(p3, 2);
                            endArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(x3, y3, 
                            		endArrowTailCenter.x, endArrowTailCenter.y, endArrowType);
                            if (path != null)
                            {
                                path.cubicTo(x1, y1, x2, y2, endArrowTailCenter.x, endArrowTailCenter.y);
                                continue;
                            }
                        }
                    }
                }
                else if ((Arrays.equals(elem, SEGMENTINFO_LINETO) 
                		|| Arrays.equals(elem, SEGMENTINFO_LINETO2)
                		|| Arrays.equals(elem, SEGMENTINFO_ESCAPE)
                        || Arrays.equals(elem, SEGMENTINFO_ESCAPE1))
                		&& j == numPoints - 1)
                {
                    if (j + 1 <= numPoints)
                    {
                    	byte[] p = verticesProp.getElement(j++);
                        if(p.length == 8)
                        {
                            int x = LittleEndian.getInt(p, 0);
                            int y = LittleEndian.getInt(p, 4);
                            endArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(x, y, 
                            		endArrowTailCenter.x, endArrowTailCenter.y, endArrowType);
                            if (path != null)
                            {
                                path.lineTo(endArrowTailCenter.x, endArrowTailCenter.y);
                                continue;
                            }
                        }
                        else
                        {
                            short x = LittleEndian.getShort(p, 0);
                            short y = LittleEndian.getShort(p, 2);
                            endArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(x, y, 
                            		endArrowTailCenter.x, endArrowTailCenter.y, endArrowType);
                            if (path != null)
                            {
                                path.lineTo(endArrowTailCenter.x, endArrowTailCenter.y);
                                continue;
                            }
                        }
                    }
                }
        	}
        	
            
        	{
        		if (Arrays.equals(elem, SEGMENTINFO_MOVETO))
                {
                    byte[] p = verticesProp.getElement(j++);
                    if(p.length == 8)
                    {
                        int x = LittleEndian.getInt(p, 0);
                        int y = LittleEndian.getInt(p, 4);
                        path.moveTo(x, y);
                    }
                    else
                    {
                        short x = LittleEndian.getShort(p, 0);
                        short y = LittleEndian.getShort(p, 2);
                        path.moveTo(x, y);
                    }                
                }
                else if (Arrays.equals(elem, SEGMENTINFO_CUBICTO)
                    || Arrays.equals(elem, SEGMENTINFO_CUBICTO1)
                    || Arrays.equals(elem, SEGMENTINFO_CUBICTO2)
                    || Arrays.equals(elem, SEGMENTINFO_ESCAPE2))
                {
                    //i++;
                    if (j + 3 <= numPoints)
                    {
                        byte[] p1 = verticesProp.getElement(j++);
                        byte[] p2 = verticesProp.getElement(j++);
                        byte[] p3 = verticesProp.getElement(j++);

                        if(p1.length == 8 && p2.length == 8 && p3.length == 8)
                        {
                            int x1 = LittleEndian.getInt(p1, 0);
                            int y1 = LittleEndian.getInt(p1, 4);
                            int x2 = LittleEndian.getInt(p2, 0);
                            int y2 = LittleEndian.getInt(p2, 4);
                            int x3 = LittleEndian.getInt(p3, 0);
                            int y3 = LittleEndian.getInt(p3, 4);
                            if (path != null)
                            {
                                path.cubicTo(x1, y1, x2, y2, x3,y3);
                            }
                        }
                        else
                        {
                            short x1 = LittleEndian.getShort(p1, 0);
                            short y1 = LittleEndian.getShort(p1, 2);
                            short x2 = LittleEndian.getShort(p2, 0);
                            short y2 = LittleEndian.getShort(p2, 2);
                            short x3 = LittleEndian.getShort(p3, 0);
                            short y3 = LittleEndian.getShort(p3, 2);
                            if (path != null)
                            {
                                path.cubicTo(x1, y1, x2, y2, x3,y3);
                            }
                        }
                    }
                }
                else if (Arrays.equals(elem, SEGMENTINFO_LINETO)
                        || Arrays.equals(elem, SEGMENTINFO_LINETO2)
                        || Arrays.equals(elem, SEGMENTINFO_ESCAPE)
                        || Arrays.equals(elem, SEGMENTINFO_ESCAPE1))
                {
                    if (j + 1 <= numPoints)
                    {
                        byte[] p = verticesProp.getElement(j++);
                        if(p.length == 8)
                        {
                            int x = LittleEndian.getInt(p, 0);
                            int y = LittleEndian.getInt(p, 4);
                            if (path != null)
                            {
                                path.lineTo(x, y);
                            }
                        }
                        else
                        {
                            short x = LittleEndian.getShort(p, 0);
                            short y = LittleEndian.getShort(p, 2);
                            if (path != null)
                            {
                                path.lineTo(x, y);
                            }
                        }
                    }
                }
                else if (Arrays.equals(elem, SEGMENTINFO_CLOSE))
                {
                    if (path != null)
                    {
                        path.close();
                    }
                }
                else if (Arrays.equals(elem, SEGMENTINFO_END))
                {
                    continue;
                }
        	}            
        }
        
        if (path != null)
        {
            if(Math.abs(w) < 1 || Math.abs(h) < 1)
            {
                RectF pathBounds = new RectF();
                path.computeBounds(pathBounds, false);
                m.postScale(rect.width / pathBounds.width(), rect.height / pathBounds.height());
            }
            path.transform(m);
            paths.add(path);
        }
        
        return paths.toArray(new Path[paths.size()]);
    }
    
    /**
     * 
     * @param obj
     * @param rgb
     * @param type
     * @param lineColor
     * @return
     */
    public static int getColorByIndex(Object obj, int rgb, int type, boolean lineColor)
    {
        if(rgb >= 0x100001F0)
        {
        	 if (type == MainConstant.APPLICATION_TYPE_PPT)
        	 {
        		 return 0xFFFFFFFF;
        	 }
        	 else
        	 {
        		 return 0xFF000000;
        	 }
        	
        }
        else if (rgb >= 0x8000000)
        {
        	int index = rgb % 0x8000000;
        	
        	// ppt
            if (type == MainConstant.APPLICATION_TYPE_PPT)
            {
                Sheet sheet = (Sheet)obj;
                if (sheet != null && index >= 0 && index <= 7)
                {
                    ColorSchemeAtom ca = sheet.getColorScheme();
                    if (ca != null)
                    {
                    	index = ca.getColor(index);
                    }
                    if(index <= 0xFFFFFF)
                    {
                         int r = index & 0xFF;
                         int g = (index & 0xFF00) >> 8;
                         int b = (index & 0xFF0000) >>16;
                         rgb = ((0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
                    }
                }
            }
            // excel
            else if (type == MainConstant.APPLICATION_TYPE_SS)
            {
            	if(rgb <= 0x8000041)
            	{
            		if(index >= 64)
                	{
                		index = index % 64 + 8;
                	}
                    AWorkbook workbook = (AWorkbook)obj;
                    if (workbook != null)
                    {
                        rgb = workbook.getColor(index, lineColor);
                    }
            	}
            	else
            	{
            		if(lineColor)
            		{
            			rgb = 0xFF000000;
            		}
            		else
            		{
            			rgb = 0xFFFFFFFF;
            		}            		
            	}
            	
            }
            // word
            else
            {
                switch (index)
                {
                    case 1:
                        rgb = android.graphics.Color.BLACK;
                        break;
                        
                    case 2:
                        rgb = android.graphics.Color.BLUE;
                        break;
                        
                    case 3:
                        rgb = android.graphics.Color.CYAN;
                        break;
                        
                    case 4:
                        rgb = android.graphics.Color.GREEN;
                        break;
                        
                    case 5:
                        rgb = android.graphics.Color.MAGENTA;
                        break;
                        
                    case 6:
                        rgb = android.graphics.Color.RED;
                        break;
                        
                    case 7:
                        rgb = android.graphics.Color.YELLOW;
                        break;
                        
                    case 8:
                        rgb = android.graphics.Color.WHITE;
                        break;
                        
                    case 9:
                        rgb = android.graphics.Color.BLUE;
                        break;
                        
                    case 10:
                        rgb = android.graphics.Color.DKGRAY;
                        break;
                        
                    case 11:
                        rgb = android.graphics.Color.GREEN;
                        break;
                        
                    case 12:
                        rgb = android.graphics.Color.MAGENTA;
                        break;
                        
                    case 13:
                        rgb = android.graphics.Color.RED;
                        break;
                        
                    case 14:
                        rgb = android.graphics.Color.YELLOW;
                        break;
                        
                    case 15:
                        rgb = android.graphics.Color.GRAY;
                        break;
                        
                    case 16:
                        rgb = android.graphics.Color.LTGRAY;
                        break;
                        
                    default:
                        break;
                }            
            }
        }
        else if(rgb <= 0xFFFFFF)
        {
            int r = rgb & 0xFF;
            int g = (rgb & 0xFF00) >> 8;
            int b = (rgb & 0xFF0000) >>16;
            rgb = ((0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
       }
        
        return rgb;
    }
    
    /**
     * @return color of the line. If color is not set returns <code>java.awt.Color.black</code>
     */
    public static Color getLineColor(EscherContainerRecord escherContainer, Object obj, int type)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);

        EscherSimpleProperty p1 = (EscherSimpleProperty)getEscherProperty(opt,
            EscherProperties.LINESTYLE__COLOR);
        EscherSimpleProperty p2 = (EscherSimpleProperty)getEscherProperty(opt,
            EscherProperties.LINESTYLE__NOLINEDRAWDASH);
        int p2val = p2 == null ? 0 : p2.getPropertyValue();
        Color clr = null;        
        if ((p2val & 0x8) != 0)
        {
        	if(p1 !=  null)
        	{
        		clr = new Color(getColorByIndex(obj, p1.getPropertyValue(), type, true));
        	}
        	else
        	{
        		clr = new Color(0, 0, 0);
        	}
        }
        else if(type != MainConstant.APPLICATION_TYPE_PPT)
        {
        	 if(p1 == null && type != MainConstant.APPLICATION_TYPE_WP)
        	 {
        		 return clr;
        	 }
        	 
        	 int rgb = p1 == null ? 0 : p1.getPropertyValue();
         	if (rgb >= 0x8000000)
 	        {
 	            rgb = getColorByIndex(obj, rgb % 0x8000000, type, true);
 	        }
 	        Color tmp = new Color(rgb, true);
 	        clr = new Color(tmp.getBlue(), tmp.getGreen(), tmp.getRed());
        }
        return clr;
    }
    
    /**
     * Foreground color
     */
    public static Color getForegroundColor(EscherContainerRecord escherContainer, Object obj, int type)
    {
        EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
            EscherOptRecord.RECORD_ID);
        EscherSimpleProperty p1 = (EscherSimpleProperty)getEscherProperty(opt,
            EscherProperties.FILL__FILLCOLOR);
        EscherSimpleProperty p2 = (EscherSimpleProperty)getEscherProperty(opt,
            EscherProperties.FILL__NOFILLHITTEST);
        EscherSimpleProperty p3 = (EscherSimpleProperty)getEscherProperty(opt,
            EscherProperties.FILL__FILLOPACITY);

        EscherComplexProperty p4 = (EscherComplexProperty)getEscherProperty(opt,
        		EscherProperties.BLIP__BLIPFILENAME);
        
        int p2val = p2 == null ? 0 : p2.getPropertyValue();
        int alpha = p3 == null ? 255 : (p3.getPropertyValue() * 255 / 65536);
        Color clr = null;
        if (p1 != null && ((p2val & 0x10) != 0 || p2val == 0))
        {
            clr = new Color(getColorByIndex(obj, p1.getPropertyValue(), type, false), alpha);
        }
        else if ((p2val & 0x10) != 0
        		&& p4 == null)
        {
            clr = new Color(255, 255, 255, alpha);
        }
        
        return clr;
    }
    
    /**
     * Whether the shape is horizontally flipped
     *
     * @return whether the shape is horizontally flipped
     */
    public static boolean getGroupFlipHorizontal(EscherContainerRecord escherContainer)
    {
        EscherContainerRecord groupInfoContainer = (EscherContainerRecord)escherContainer.getChild(0);
        if (groupInfoContainer != null)
        {
            EscherSpRecord spRecord = groupInfoContainer.getChildById(EscherSpRecord.RECORD_ID);
            if (spRecord != null)
            {
                return (spRecord.getFlags() & EscherSpRecord.FLAG_FLIPHORIZ) != 0;
            }
        }
        return false;
    }

    /**
     * Whether the shape is vertically flipped
     *
     * @return whether the shape is vertically flipped
     */
    public static boolean getGroupFlipVertical(EscherContainerRecord escherContainer)
    {
        EscherContainerRecord groupInfoContainer = (EscherContainerRecord)escherContainer.getChild(0);
        if (groupInfoContainer != null)
        {
            EscherSpRecord spRecord = groupInfoContainer.getChildById(EscherSpRecord.RECORD_ID);
            if (spRecord != null)
            {
                return (spRecord.getFlags() & EscherSpRecord.FLAG_FLIPVERT) != 0;
            }
        }
        return false;
    }
    
    /**
     * Rotation angle in degrees
     *
     * @return rotation angle in degrees
     */
    public static int getGroupRotation(EscherContainerRecord escherContainer)
    {
        EscherContainerRecord groupInfoContainer = (EscherContainerRecord)escherContainer.getChild(0);
        if (groupInfoContainer != null)
        {
            int rot = getEscherProperty(groupInfoContainer, EscherProperties.TRANSFORM__ROTATION);
            return (rot >> 16) % 360;
        }
        return 0;
    }
    
    ////////////////////////////////for wp shape //////////////////////////////////////////////////////////
    /**
     * 
     * @param escherContainer
     * @return
     */
    public static int getPosition_H(EscherContainerRecord escherContainer)
    {
    	EscherTertiaryOptRecord opt = (EscherTertiaryOptRecord)getEscherChild(escherContainer, 
    			EscherTertiaryOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.GROUPSHAPE__POSH);
    	if(p != null)
    	{
    		return p.getPropertyValue();
    	}
    	
    	return HWPFShape.POSH_ABS;
    }
    
    /**
     * 
     * @param escherContainer
     * @return
     */
    public static int getPositionRelTo_H(EscherContainerRecord escherContainer)
    {
    	EscherTertiaryOptRecord opt = (EscherTertiaryOptRecord)getEscherChild(escherContainer, 
    			EscherTertiaryOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.GROUPSHAPE__POSRELH);
    	if(p != null)
    	{
    		return p.getPropertyValue();
    	}
    	
    	return HWPFShape.POSRELH_COLUMN;
    }
    
    /**
     * 
     * @param escherContainer
     * @return
     */
    public static int getPosition_V(EscherContainerRecord escherContainer)
    {
    	EscherTertiaryOptRecord opt = (EscherTertiaryOptRecord)getEscherChild(escherContainer, 
    			EscherTertiaryOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.GROUPSHAPE__POSV);
    	if(p != null)
    	{
    		return p.getPropertyValue();
    	}
    	
    	return HWPFShape.POSV_ABS;
    }
    
    /**
     * 
     * @param escherContainer
     * @return
     */
    public static int getPositionRelTo_V(EscherContainerRecord escherContainer)
    {
    	EscherTertiaryOptRecord opt = (EscherTertiaryOptRecord)getEscherChild(escherContainer, 
    			EscherTertiaryOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.GROUPSHAPE__POSRELV);
    	if(p != null)
    	{
    		return p.getPropertyValue();
    	}
    	
    	return HWPFShape.POSRELV_TEXT;
    }
    
    public static String getShapeName(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
                EscherOptRecord.RECORD_ID);
    	
    	EscherComplexProperty complexProp = (EscherComplexProperty)getEscherProperty(opt, 
    			EscherProperties.GROUPSHAPE__SHAPENAME);
    	if(complexProp != null)
    	{
    		return StringUtil.getFromUnicodeLE(complexProp.getComplexData());
    	}
    	return null;
    }
    
    /**
     * word wrap text in AutoShape
     * @param escherContainer
     * @return
     */
    public static boolean isTextboxWrapLine(EscherContainerRecord escherContainer)
    {

    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, 
    			EscherOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.TEXT__WRAPTEXT);
    	
    	if(p != null)
    	{
    		return p.getPropertyValue() != 2;
    	}
    	
    	return true;
    }
    
    /**
     * in pixel
     * @param escherContainer
     * @return
     */
    public static float getTextboxMarginLeft(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, 
    			EscherOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.TEXT__TEXTLEFT);
    	if(p != null)
    	{
    		return p.getPropertyValue() / (float)AutoShapeConstant.LINEWIDTH_DEFAULT;
    	}
    	
    	return DefaultMargin_Pixel * 2;
    }
    
    /**
     * in pixel
     * @param escherContainer
     * @return
     */
    public static float getTextboxMarginTop(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, 
    			EscherOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.TEXT__TEXTTOP);
    	if(p != null)
    	{
    		return p.getPropertyValue() / (float)AutoShapeConstant.LINEWIDTH_DEFAULT;
    	}
    	
    	return DefaultMargin_Pixel;
    }
    
    /**
     * in pixel
     * @param escherContainer
     * @return
     */
    public static float getTextboxMarginRight(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, 
    			EscherOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.TEXT__TEXTRIGHT);
    	if(p != null)
    	{
    		return p.getPropertyValue() / (float)AutoShapeConstant.LINEWIDTH_DEFAULT;
    	}
    	
    	return DefaultMargin_Pixel * 2;
    }
    
    /**
     * in pixel
     * @param escherContainer
     * @return
     */
    public static float getTextboxMarginBottom(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer, 
    			EscherOptRecord.RECORD_ID);
    	
    	EscherSimpleProperty p = (EscherSimpleProperty)getEscherProperty(opt,
                EscherProperties.TEXT__TEXTBOTTOM);
    	if(p != null)
    	{
    		return p.getPropertyValue() / (float)AutoShapeConstant.LINEWIDTH_DEFAULT;
    	}
    	
    	return DefaultMargin_Pixel;
    }
    
    /**
     * get unicode geotext
     * @param escherContainer
     * @return
     */
    public static String getUnicodeGeoText(EscherContainerRecord escherContainer)
    {
    	EscherOptRecord opt = (EscherOptRecord)getEscherChild(escherContainer,
                EscherOptRecord.RECORD_ID);
    	
    	EscherComplexProperty complexProp = (EscherComplexProperty)getEscherProperty(opt, 
    			EscherProperties.GEOTEXT__UNICODE);
    	if(complexProp != null)
    	{
    		return StringUtil.getFromUnicodeLE(complexProp.getComplexData());
    	}
    	return null;
    }
}
