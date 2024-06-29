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

import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ss.usermodel.RichTextString;

/**
 * A textbox is a shape that may hold a rich text string.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public class HSSFTextbox extends HSSFSimpleShape
{
    public final static short OBJECT_TYPE_TEXT = 6;

    /**
     * How to align text horizontally
     */
    public final static short  HORIZONTAL_ALIGNMENT_LEFT = 1;
    public final static short  HORIZONTAL_ALIGNMENT_CENTERED = 2;
    public final static short  HORIZONTAL_ALIGNMENT_RIGHT = 3;
    public final static short  HORIZONTAL_ALIGNMENT_JUSTIFIED = 4;
    public final static short  HORIZONTAL_ALIGNMENT_DISTRIBUTED = 7;

    /**
     * How to align text vertically
     */
    public final static short  VERTICAL_ALIGNMENT_TOP    = 1;
    public final static short  VERTICAL_ALIGNMENT_CENTER = 2;
    public final static short  VERTICAL_ALIGNMENT_BOTTOM = 3;
    public final static short  VERTICAL_ALIGNMENT_JUSTIFY = 4;
    public final static short  VERTICAL_ALIGNMENT_DISTRIBUTED= 7;


    HSSFRichTextString string = new HSSFRichTextString("");

    /**
     * Construct a new textbox with the given parent and anchor.
     * @param parent
     * @param anchor  One of HSSFClientAnchor or HSSFChildAnchor
     */
    public HSSFTextbox(EscherContainerRecord escherContainer, HSSFShape parent, HSSFAnchor anchor )
    {
        super(escherContainer, parent, anchor );
        setShapeType(OBJECT_TYPE_TEXT);

        halign = HORIZONTAL_ALIGNMENT_LEFT;
        valign = VERTICAL_ALIGNMENT_TOP;
        
        marginLeft = Math.round(ShapeKit.getTextboxMarginLeft(escherContainer));
        marginTop = Math.round(ShapeKit.getTextboxMarginTop(escherContainer));
        marginRight = Math.round(ShapeKit.getTextboxMarginRight(escherContainer));
        marginBottom = Math.round(ShapeKit.getTextboxMarginBottom(escherContainer));
        isWrapLine = ShapeKit.isTextboxWrapLine(escherContainer);
    }

    /**
     * @return  the rich text string for this textbox.
     */
    public HSSFRichTextString getString()
    {
        return string;
    }

    /**
     * @param string    Sets the rich text string used by this object.
     */
    public void setString( RichTextString string )
    {
        HSSFRichTextString rtr = (HSSFRichTextString)string;

        // If font is not set we must set the default one
        if (rtr.numFormattingRuns() == 0) rtr.applyFont((short)0);

        this.string = rtr;
    }

    /**
     * word wrap text in AutoShape
     * @param escherContainer
     * @return
     */
    public boolean isTextboxWrapLine()
    {
        return isWrapLine;
    }
    /**
     * 
     */
    /**
     * @return  Returns the left margin within the textbox.
     */
    public int getMarginLeft()
    {
        return marginLeft;
    }

    /**
     * Sets the left margin within the textbox.
     */
    public void setMarginLeft( int marginLeft )
    {
        this.marginLeft = marginLeft;
    }

    /**
     * @return    returns the right margin within the textbox.
     */
    public int getMarginRight()
    {
        return marginRight;
    }

    /**
     * Sets the right margin within the textbox.
     */
    public void setMarginRight( int marginRight )
    {
        this.marginRight = marginRight;
    }

    /**
     * @return  returns the top margin within the textbox.
     */
    public int getMarginTop()
    {
        return marginTop;
    }

    /**
     * Sets the top margin within the textbox.
     */
    public void setMarginTop( int marginTop )
    {
        this.marginTop = marginTop;
    }

    /**
     * Gets the bottom margin within the textbox.
     */
    public int getMarginBottom()
    {
        return marginBottom;
    }

    /**
     * Sets the bottom margin within the textbox.
     */
    public void setMarginBottom( int marginBottom )
    {
        this.marginBottom = marginBottom;
    }

    /**
     * Gets the horizontal alignment.
     */
    public short getHorizontalAlignment()
    {
        return halign;
    }

    /**
     * Sets the horizontal alignment.
     */
    public void setHorizontalAlignment( short align )
    {
        this.halign = align;
    }

    /**
     * Gets the vertical alignment.
     */
    public short getVerticalAlignment()
    {
        return valign;
    }

    /**
     * Sets the vertical alignment.
     */
    public void setVerticalAlignment( short align )
    {
        this.valign = align;
    }
    
    public boolean isWordArt()
    {
    	return isWordArt;
    }
    
    public void setWordArt(boolean isWordArt)
    {
    	this.isWordArt = isWordArt;
    }
    
    public void setFontColor(int fontColor)
    {
    	this.fontColor = fontColor;
    }
    
    public int getFontColor()
    {
    	return fontColor;
    }
    
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBottom;
    private short halign;
    private short valign;
    private boolean isWrapLine;
    private boolean isWordArt;
    private int fontColor;
}
