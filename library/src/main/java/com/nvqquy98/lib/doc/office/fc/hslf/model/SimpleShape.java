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

import java.io.ByteArrayOutputStream;

import com.nvqquy98.lib.doc.office.constant.AutoShapeConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.DefaultEscherRecordFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherChildAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientDataRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.hslf.exceptions.HSLFException;
import com.nvqquy98.lib.doc.office.fc.hslf.record.InteractiveInfo;
import com.nvqquy98.lib.doc.office.fc.hslf.record.InteractiveInfoAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.Record;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;
import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.java.awt.geom.Rectangle2D;


/**
 *  An abstract simple (non-group) shape.
 *  This is the parent class for all primitive shapes like Line, Rectangle, etc.
 *
 *  @author Yegor Kozlov
 */
public abstract class SimpleShape extends Shape
{
    /**
     * Create a SimpleShape object and initialize it from the supplied Record container.
     *
     * @param escherRecord    <code>EscherSpContainer</code> container which holds information about this shape
     * @param parent    the parent of the shape
     */
    protected SimpleShape(EscherContainerRecord escherRecord, Shape parent)
    {
        super(escherRecord, parent);
    }
    
    public Rectangle2D getLogicalAnchor2D()
    {
        Rectangle2D anchor = getAnchor2D();

        //if it is a groupped shape see if we need to transform the coordinates
        if (_parent != null)
        {
            /*Shape top = _parent;
            while (top.getParent() != null)
                top = top.getParent();*/

            // child anchor in msofbtClientAnchor container
            Rectangle2D clientAnchor = ((ShapeGroup)_parent).getClientAnchor2D(_parent);
            // group anchor in msofbtSpgr container
            Rectangle2D spgrAnchor = ((ShapeGroup)_parent).getCoordinates();

//            double scale = Math.max(spgrAnchor.getWidth() / clientAnchor.getWidth(), 
//                                    spgrAnchor.getHeight() / clientAnchor.getHeight());
//
//            double x = clientAnchor.getX() + (anchor.getX() - spgrAnchor.getX()) / scale;
//            double y = clientAnchor.getY() + (anchor.getY() - spgrAnchor.getY()) / scale;
//            double width = anchor.getWidth() / scale;
//            double height = anchor.getHeight() / scale;
            
            double scalex = spgrAnchor.getWidth() / clientAnchor.getWidth();
            double scaley = spgrAnchor.getHeight() / clientAnchor.getHeight();

            double x = clientAnchor.getX() + (anchor.getX() - spgrAnchor.getX()) / scalex;
            double y = clientAnchor.getY() + (anchor.getY() - spgrAnchor.getY()) / scaley;
            double width = anchor.getWidth() / scalex;
            double height = anchor.getHeight() / scaley;
            

            anchor = new Rectangle2D.Double(x, y, width, height);

        }

        int angle = getRotation();
        if (angle != 0)
        {
            double centerX = anchor.getX() + anchor.getWidth() / 2;
            double centerY = anchor.getY() + anchor.getHeight() / 2;

            AffineTransform trans = new AffineTransform();
            trans.translate(centerX, centerY);
            trans.rotate(Math.toRadians(angle));
            trans.translate(-centerX, -centerY);

            Rectangle2D rect = trans.createTransformedShape(anchor).getBounds2D();
            if ((anchor.getWidth() < anchor.getHeight() && rect.getWidth() > rect.getHeight())
                || (anchor.getWidth() > anchor.getHeight() && rect.getWidth() < rect.getHeight()))
            {
                trans = new AffineTransform();
                trans.translate(centerX, centerY);
                trans.rotate(Math.PI / 2);
                trans.translate(-centerX, -centerY);
                anchor = trans.createTransformedShape(anchor).getBounds2D();
            }
        }
        return anchor;
    }
    
    /**
     *  Find a record in the underlying EscherClientDataRecord
     *
     * @param recordType type of the record to search
     */
    protected Record getClientDataRecord(int recordType)
    {
        Record[] records = getClientRecords();
        if (records != null)
        {
            for (int i = 0; i < records.length; i++)
            {
                if (records[i].getRecordType() == recordType)
                {
                    return records[i];
                }
            }
        }
        return null;
    }

    /**
     * Search for EscherClientDataRecord, if found, convert its contents into an array of HSLF records
     *
     * @return an array of HSLF records contained in the shape's EscherClientDataRecord or <code>null</code>
     */
    protected Record[] getClientRecords()
    {
        if (_clientData == null)
        {
            EscherRecord r = ShapeKit.getEscherChild(getSpContainer(),
                EscherClientDataRecord.RECORD_ID);
            //ddf can return EscherContainerRecord with recordId=EscherClientDataRecord.RECORD_ID
            //convert in to EscherClientDataRecord on the fly
            if (r != null && !(r instanceof EscherClientDataRecord))
            {
                byte[] data = r.serialize();
                r = new EscherClientDataRecord();
                r.fillFields(data, 0, new DefaultEscherRecordFactory());
            }
            _clientData = (EscherClientDataRecord)r;
        }
        if (_clientData != null && _clientRecords == null)
        {
            byte[] data = _clientData.getRemainingData();
            _clientRecords = Record.findChildRecords(data, 0, data.length);
        }
        return _clientRecords;
    }
    

    /***********************************************************/
    /**
     * Create a new Shape
     *
     * @param isChild   <code>true</code> if the Line is inside a group, <code>false</code> otherwise
     * @return the record container which holds this shape
     */
    protected EscherContainerRecord createSpContainer(boolean isChild)
    {
        _escherContainer = new EscherContainerRecord();
        _escherContainer.setRecordId(EscherContainerRecord.SP_CONTAINER);
        _escherContainer.setOptions((short)15);

        EscherSpRecord sp = new EscherSpRecord();
        int flags = EscherSpRecord.FLAG_HAVEANCHOR | EscherSpRecord.FLAG_HASSHAPETYPE;
        if (isChild)
            flags |= EscherSpRecord.FLAG_CHILD;
        sp.setFlags(flags);
        _escherContainer.addChildRecord(sp);

        EscherOptRecord opt = new EscherOptRecord();
        opt.setRecordId(EscherOptRecord.RECORD_ID);
        _escherContainer.addChildRecord(opt);

        EscherRecord anchor;
        if (isChild)
            anchor = new EscherChildAnchorRecord();
        else
        {
            anchor = new EscherClientAnchorRecord();

            //hack. internal variable EscherClientAnchorRecord.shortRecord can be
            //initialized only in fillFields(). We need to set shortRecord=false;
            byte[] header = new byte[16];
            LittleEndian.putUShort(header, 0, 0);
            LittleEndian.putUShort(header, 2, 0);
            LittleEndian.putInt(header, 4, 8);
            anchor.fillFields(header, 0, null);
        }
        _escherContainer.addChildRecord(anchor);

        return _escherContainer;
    }

    /**
     *  Sets the width of line in in points
     *  @param width  the width of line in in points
     */
    public void setLineWidth(double width)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        setEscherProperty(opt, EscherProperties.LINESTYLE__LINEWIDTH, (int)(width * ShapeKit.EMU_PER_POINT));
    }

    /**
     * Sets the color of line
     *
     * @param color new color of the line
     */
    public void setLineColor(Color color)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        if (color == null)
        {
            setEscherProperty(opt, EscherProperties.LINESTYLE__NOLINEDRAWDASH, 0x80000);
        }
        else
        {
            int rgb = new Color(color.getBlue(), color.getGreen(), color.getRed(), 0).getRGB();
            setEscherProperty(opt, EscherProperties.LINESTYLE__COLOR, rgb);
            setEscherProperty(opt, EscherProperties.LINESTYLE__NOLINEDRAWDASH, color == null
                ? 0x180010 : 0x180018);
        }
    }

    /**
     * Gets line dashing. One of the PEN_* constants defined in this class.
     *
     * @return dashing of the line.
     */
    public int getLineDashing()
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);

        EscherSimpleProperty prop = (EscherSimpleProperty)ShapeKit.getEscherProperty(opt,
            EscherProperties.LINESTYLE__LINEDASHING);
        return prop == null ? AutoShapeConstant.LINESTYLE_SOLID : prop.getPropertyValue();
    }

    /**
     * Sets line dashing. One of the PEN_* constants defined in this class.
     *
     * @param pen new style of the line.
     */
    public void setLineDashing(int pen)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);

        setEscherProperty(opt, EscherProperties.LINESTYLE__LINEDASHING, pen);
    }

    /**
     * Sets line style. One of the constants defined in this class.
     *
     * @param style new style of the line.
     */
    public void setLineStyle(int style)
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        setEscherProperty(opt, EscherProperties.LINESTYLE__LINESTYLE, style == AutoShapeConstant.LINE_SIMPLE
            ? -1 : style);
    }

    /**
     * Returns line style. One of the constants defined in this class.
     *
     * @return style of the line.
     */
    public int getLineStyle()
    {
        EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(_escherContainer,
            EscherOptRecord.RECORD_ID);
        EscherSimpleProperty prop = (EscherSimpleProperty)ShapeKit.getEscherProperty(opt,
            EscherProperties.LINESTYLE__LINESTYLE);
        return prop == null ? AutoShapeConstant.LINE_SIMPLE : prop.getPropertyValue();
    }

    /**
     * The color used to fill this shape.
     *
     * @param color the background color
     */
    public void setFillColor(Color color)
    {
        getFill().setForegroundColor(color);
    }

    /**
     * Rotate this shape
     *
     * @param theta the rotation angle in degrees
     */
    public void setRotation(int theta)
    {
        setEscherProperty(EscherProperties.TRANSFORM__ROTATION, (theta << 16));
    }

    /*public void draw(Graphics2D graphics)
    {
        AffineTransform at = graphics.getTransform();
        ShapePainter.paint(this, graphics);
        graphics.setTransform(at);
    }*/

    protected void updateClientData()
    {
        if (_clientData != null && _clientRecords != null)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try
            {
                for (int i = 0; i < _clientRecords.length; i++)
                {
                    //_clientRecords[i].writeOut(out);
                }
            }
            catch(Exception e)
            {
                throw new HSLFException(e);
            }
            _clientData.setRemainingData(out.toByteArray());
        }
    }

    public void setHyperlink(Hyperlink link)
    {
        if (link.getId() == -1)
        {
            throw new HSLFException("You must call SlideShow.addHyperlink(Hyperlink link) first");
        }

        EscherClientDataRecord cldata = new EscherClientDataRecord();
        cldata.setOptions((short)0xF);
        getSpContainer().addChildRecord(cldata); // TODO - junit to prove getChildRecords().add is wrong

        InteractiveInfo info = new InteractiveInfo();
        InteractiveInfoAtom infoAtom = info.getInteractiveInfoAtom();

        switch (link.getType())
        {
            case Hyperlink.LINK_FIRSTSLIDE:
                infoAtom.setAction(InteractiveInfoAtom.ACTION_JUMP);
                infoAtom.setJump(InteractiveInfoAtom.JUMP_FIRSTSLIDE);
                infoAtom.setHyperlinkType(InteractiveInfoAtom.LINK_FirstSlide);
                break;
            case Hyperlink.LINK_LASTSLIDE:
                infoAtom.setAction(InteractiveInfoAtom.ACTION_JUMP);
                infoAtom.setJump(InteractiveInfoAtom.JUMP_LASTSLIDE);
                infoAtom.setHyperlinkType(InteractiveInfoAtom.LINK_LastSlide);
                break;
            case Hyperlink.LINK_NEXTSLIDE:
                infoAtom.setAction(InteractiveInfoAtom.ACTION_JUMP);
                infoAtom.setJump(InteractiveInfoAtom.JUMP_NEXTSLIDE);
                infoAtom.setHyperlinkType(InteractiveInfoAtom.LINK_NextSlide);
                break;
            case Hyperlink.LINK_PREVIOUSSLIDE:
                infoAtom.setAction(InteractiveInfoAtom.ACTION_JUMP);
                infoAtom.setJump(InteractiveInfoAtom.JUMP_PREVIOUSSLIDE);
                infoAtom.setHyperlinkType(InteractiveInfoAtom.LINK_PreviousSlide);
                break;
            case Hyperlink.LINK_URL:
                infoAtom.setAction(InteractiveInfoAtom.ACTION_HYPERLINK);
                infoAtom.setJump(InteractiveInfoAtom.JUMP_NONE);
                infoAtom.setHyperlinkType(InteractiveInfoAtom.LINK_Url);
                break;
        }

        infoAtom.setHyperlinkID(link.getId());

        /*ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            info.writeOut(out);
        }
        catch(Exception e)
        {
            throw new HSLFException(e);
        }
        cldata.setRemainingData(out.toByteArray());*/

    }
    
    /**
     * 
     *
     */
    public void dispose()
    {
       super.dispose();
       if (_clientRecords != null)
       {
           for (Record rec : _clientRecords)
           {
               rec.dispose();
           }
           _clientRecords = null;
       }
       if (_clientData != null)
       {
           _clientData.dispose();
           _clientData = null;
       }
    }
    
    /**
     * Records stored in EscherClientDataRecord
     */
    protected Record[] _clientRecords;
    protected EscherClientDataRecord _clientData;

}
