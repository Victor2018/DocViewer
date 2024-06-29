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

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherBSERecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.hslf.record.Document;
import com.nvqquy98.lib.doc.office.fc.hslf.usermodel.PictureData;
import com.nvqquy98.lib.doc.office.fc.hslf.usermodel.SlideShow;
import com.nvqquy98.lib.doc.office.java.awt.Color;


/**
 * Represents functionality provided by the 'Fill Effects' dialog in PowerPoint.
 *
 * @author Yegor Kozlov
 */
public final class Fill
{
    /**
     * Construct a <code>Fill</code> object for a shape.
     * Fill information will be read from shape's escher properties.
     *
     * @param shape the shape this background applies to
     */
    public Fill(Shape shape)
    {
        this.shape = shape;
    }

    /**
     * Returns fill type.
     * Must be one of the <code>FILL_*</code> constants defined in this class.
     *
     * @return type of fill
     */
    public int getFillType()
    {
        return ShapeKit.getFillType(shape.getSpContainer());
    }
    
    /**
     * 
     * @return
     */
    public int getFillAngle()
    {
    	return ShapeKit.getFillAngle(shape.getSpContainer());
    }
    
    /**
     * 
     * @return
     */
    public int getFillFocus()
    {
    	return ShapeKit.getFillFocus(shape.getSpContainer());
    }
    
    public boolean isShaderPreset()
    {
    	return ShapeKit.isShaderPreset(shape.getSpContainer());
    }
    
    public int[] getShaderColors()
    {
    	return ShapeKit.getShaderColors(shape.getSpContainer());
    }    
    
    public float[] getShaderPositions()
    {
    	return ShapeKit.getShaderPositions(shape.getSpContainer());
    }
    
    /**
     * 
     * @return
     */
    public int getRadialGradientPositionType()
    {
    	return ShapeKit.getRadialGradientPositionType(shape.getSpContainer());
    }
    
    /**
     * Foreground color
     */
    public Color getForegroundColor()
    {
        return ShapeKit.getForegroundColor(shape.getSpContainer(), shape.getSheet(), MainConstant.APPLICATION_TYPE_PPT);
    }
    
    /**
     * Background color
     */
    public Color getFillbackColor()
    {
        return ShapeKit.getFillbackColor(shape.getSpContainer(), shape.getSheet(), MainConstant.APPLICATION_TYPE_PPT);
    }
    
    /**
     * <code>PictureData</code> object used in a texture, pattern of picture fill.
     */
    public PictureData getPictureData()
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(shape.getSpContainer(),
            EscherOptRecord.RECORD_ID);
        
        EscherProperty ep = ShapeKit.getEscherProperty(opt,
            EscherProperties.FILL__PATTERNTEXTURE);
        if (ep == null || !(ep instanceof EscherSimpleProperty))
        {
            return null;
        }
        
        EscherSimpleProperty p = (EscherSimpleProperty)ep;

        SlideShow ppt = shape.getSheet().getSlideShow();
        PictureData[] pict = ppt.getPictureData();
        Document doc = ppt.getDocumentRecord();

        EscherContainerRecord dggContainer = doc.getPPDrawingGroup().getDggContainer();
        EscherContainerRecord bstore = (EscherContainerRecord)ShapeKit.getEscherChild(dggContainer,
            EscherContainerRecord.BSTORE_CONTAINER);

        if (bstore != null)
        {
            java.util.List<EscherRecord> lst = bstore.getChildRecords();
            int idx = (p.getPropertyValue() & 0xFFFF);
            if (idx == 0)
            {
                //logger.log(POILogger.WARN, "no reference to picture data found ");
            }
            else
            {
                EscherBSERecord bse = (EscherBSERecord)lst.get(idx - 1);
                for (int i = 0; i < pict.length; i++)
                {
                    if (pict[i].getOffset() == bse.getOffset())
                    {
                        return pict[i];
                    }
                }
            }
        }
        return null;
    }

    /*******************************************/
    /**
     * Sets fill type.
     * Must be one of the <code>FILL_*</code> constants defined in this class.
     *
     * @param type type of the fill
     */
    public void setFillType(int type)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(shape.getSpContainer(),
            EscherOptRecord.RECORD_ID);
        Shape.setEscherProperty(opt, EscherProperties.FILL__FILLTYPE, type);
    }

    /**
     * Foreground color
     */
    public void setForegroundColor(Color color)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(shape.getSpContainer(),
            EscherOptRecord.RECORD_ID);
        if (color == null)
        {
            Shape.setEscherProperty(opt, EscherProperties.FILL__NOFILLHITTEST, 0x150000);
        }
        else
        {
            int rgb = new Color(color.getBlue(), color.getGreen(), color.getRed(), 0).getRGB();
            Shape.setEscherProperty(opt, EscherProperties.FILL__FILLCOLOR, rgb);
            Shape.setEscherProperty(opt, EscherProperties.FILL__NOFILLHITTEST, 0x150011);
        }
    }

    /**
     * Background color
     */
    public void setBackgroundColor(Color color)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(shape.getSpContainer(),
            EscherOptRecord.RECORD_ID);
        if (color == null)
        {
            Shape.setEscherProperty(opt, EscherProperties.FILL__FILLBACKCOLOR, -1);
        }
        else
        {
            int rgb = new Color(color.getBlue(), color.getGreen(), color.getRed(), 0).getRGB();
            Shape.setEscherProperty(opt, EscherProperties.FILL__FILLBACKCOLOR, rgb);
        }
    }

    /**
     * Assign picture used to fill the underlying shape.
     *
     * @param idx 0-based index of the picture added to this ppt by <code>SlideShow.addPicture</code> method.
     */
    public void setPictureData(int idx)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(shape.getSpContainer(),
            EscherOptRecord.RECORD_ID);
        Shape.setEscherProperty(opt, (short)(EscherProperties.FILL__PATTERNTEXTURE + 0x4000), idx);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        shape = null;
    }
    
    /**
     * The shape this background applies to
     */
    protected Shape shape;
}
