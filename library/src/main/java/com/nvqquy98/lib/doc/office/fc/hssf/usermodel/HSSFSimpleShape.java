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

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.AWorkbook;


/**
 * Represents a simple shape such as a line, rectangle or oval.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public class HSSFSimpleShape extends HSSFShape
{
    // The commented out ones haven't been tested yet or aren't supported
    // by HSSFSimpleShape.

    public final static short       OBJECT_TYPE_LINE               = 1;
    public final static short       OBJECT_TYPE_RECTANGLE          = 2;
    public final static short       OBJECT_TYPE_OVAL               = 3;
//    public final static short       OBJECT_TYPE_ARC                = 4;
//    public final static short       OBJECT_TYPE_CHART              = 5;
//    public final static short       OBJECT_TYPE_TEXT               = 6;
//    public final static short       OBJECT_TYPE_BUTTON             = 7;
    public final static short       OBJECT_TYPE_PICTURE            = 8;
//    public final static short       OBJECT_TYPE_POLYGON            = 9;
//    public final static short       OBJECT_TYPE_CHECKBOX           = 11;
//    public final static short       OBJECT_TYPE_OPTION_BUTTON      = 12;
//    public final static short       OBJECT_TYPE_EDIT_BOX           = 13;
//    public final static short       OBJECT_TYPE_LABEL              = 14;
//    public final static short       OBJECT_TYPE_DIALOG_BOX         = 15;
//    public final static short       OBJECT_TYPE_SPINNER            = 16;
//    public final static short       OBJECT_TYPE_SCROLL_BAR         = 17;
//    public final static short       OBJECT_TYPE_LIST_BOX           = 18;
//    public final static short       OBJECT_TYPE_GROUP_BOX          = 19;
    public final static short       OBJECT_TYPE_COMBO_BOX          = 20;
    public final static short       OBJECT_TYPE_COMMENT            = 25;
//    public final static short       OBJECT_TYPE_MICROSOFT_OFFICE_DRAWING = 30;

    public HSSFSimpleShape(EscherContainerRecord escherContainer, HSSFShape parent, HSSFAnchor anchor )
    {
        super(escherContainer, parent, anchor );
    }
    
    public void processLine(EscherContainerRecord escherContainer, AWorkbook workbook)
    {
    	if(ShapeKit.hasLine(escherContainer))
    	{
    		Color color = ShapeKit.getLineColor(escherContainer, workbook, MainConstant.APPLICATION_TYPE_SS);
            if (color != null)
            {
                setLineStyleColor(color.getRGB());
            }
            else
            {
                setNoBorder(true);
            }
            
            setLineStyle(ShapeKit.getLineDashing(escherContainer));
    	}
    	else
    	{
    		setNoBorder(true);
    	}        
    }
    
    public void processArrow(EscherContainerRecord escherContainer)
    {
        setStartArrow((byte)ShapeKit.getStartArrowType(escherContainer), 
            ShapeKit.getStartArrowWidth(escherContainer), 
            ShapeKit.getStartArrowLength(escherContainer));
        
        setEndArrow((byte)ShapeKit.getEndArrowType(escherContainer), 
            ShapeKit.getEndArrowWidth(escherContainer), 
            ShapeKit.getEndArrowLength(escherContainer));
    }
    
    public void processRotationAndFlip(EscherContainerRecord escherContainer)
    {
        setRotation(ShapeKit.getRotation(escherContainer));
        setFilpH(ShapeKit.getFlipHorizontal(escherContainer));
        setFlipV(ShapeKit.getFlipVertical(escherContainer));
    }
}
