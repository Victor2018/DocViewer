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
import java.util.List;

import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientDataRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherPropertyFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.hslf.record.InteractiveInfo;
import com.nvqquy98.lib.doc.office.fc.hslf.record.OEShapeAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.Record;
import com.nvqquy98.lib.doc.office.fc.hslf.record.RecordTypes;


/**
 * Create a <code>Shape</code> object depending on its type
 *
 * @author Yegor Kozlov
 */
public final class ShapeFactory
{
    // For logging
    //protected static POILogger logger = POILogFactory.getLogger(ShapeFactory.class);

    /**
     * Create a new shape from the data provided.
     */
    public static Shape createShape(EscherContainerRecord spContainer, Shape parent)
    {
        if (spContainer.getRecordId() == EscherContainerRecord.SPGR_CONTAINER)
        {
            return createShapeGroup(spContainer, parent);
        }
        return createSimpeShape(spContainer, parent);
    }

    public static ShapeGroup createShapeGroup(EscherContainerRecord spContainer, Shape parent)
    {
        ShapeGroup group = null;
        EscherRecord opt = ShapeKit.getEscherChild((EscherContainerRecord)spContainer.getChild(0),
            (short)0xF122);
        if (opt != null)
        {
            try
            {
                EscherPropertyFactory f = new EscherPropertyFactory();
                List props = f.createProperties(opt.serialize(), 8, opt.getInstance());
                EscherSimpleProperty p = (EscherSimpleProperty)props.get(0);
                if (p.getPropertyNumber() == 0x39F && p.getPropertyValue() == 1)
                {
                    group = new Table(spContainer, parent);
                }
                else
                {
                    group = new ShapeGroup(spContainer, parent);
                }
            }
            catch(Exception e)
            {
                //logger.log(POILogger.WARN, e.getMessage());
                group = new ShapeGroup(spContainer, parent);
            }
        }
        else
        {
            group = new ShapeGroup(spContainer, parent);
        }

        return group;
    }

    public static Shape createSimpeShape(EscherContainerRecord spContainer, Shape parent)
    {
        Shape shape = null;
        EscherSpRecord spRecord = spContainer.getChildById(EscherSpRecord.RECORD_ID);
        
        int type = spRecord.getOptions() >> 4;
        switch (type)
        {
            case ShapeTypes.TextBox:
                shape = new TextBox(spContainer, parent);
                break;
                
            case ShapeTypes.HostControl:
            case ShapeTypes.PictureFrame:
                InteractiveInfo info = (InteractiveInfo)getClientDataRecord(spContainer,
                    RecordTypes.InteractiveInfo.typeID);
                OEShapeAtom oes = (OEShapeAtom)getClientDataRecord(spContainer,
                    RecordTypes.OEShapeAtom.typeID);
                if (info != null && info.getInteractiveInfoAtom() != null)
                {
                    /*switch (info.getInteractiveInfoAtom().getAction())
                    {
                        case InteractiveInfoAtom.ACTION_OLE:
                            shape = new OLEShape(spContainer, parent);
                            break;
                        case InteractiveInfoAtom.ACTION_MEDIA:
                            shape = new MovieShape(spContainer, parent);
                            break;
                        default:
                            break;
                    }*/
                }
                else if (oes != null)
                {
                    shape = new OLEShape(spContainer, parent);
                }
                if (shape == null)
                {
                    shape = new Picture(spContainer, parent);
                }
                break;
            
            case ShapeTypes.Line:
            case ShapeTypes.StraightConnector1:
            case ShapeTypes.BentConnector2:
            case ShapeTypes.BentConnector3:
            case ShapeTypes.CurvedConnector3:
                shape = new Line(spContainer, parent);
                break;
                
            case ShapeTypes.NotPrimitive:
            case ShapeTypes.NotchedCircularArrow:
                EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(spContainer,
                    EscherOptRecord.RECORD_ID);
                if (opt != null)
                {
                    EscherProperty prop = ShapeKit.getEscherProperty(opt,
                        EscherProperties.GEOMETRY__VERTICES);
                    if (prop != null)
                    {
                        shape = new Freeform(spContainer, parent);
                    }
                    /*else
                    {
    
                        shape = new AutoShape(spContainer, parent);
                    }*/
                }
                break;
            
            default:
                shape = new AutoShape(spContainer, parent);
                break;
        }
        //shape id
        shape.setShapeId(spRecord.getShapeId());
        
        return shape;
    }

    protected static Record getClientDataRecord(EscherContainerRecord spContainer, int recordType)
    {
        Record oep = null;
        for (Iterator<EscherRecord> it = spContainer.getChildIterator(); it.hasNext();)
        {
            EscherRecord obj = it.next();
            if (obj.getRecordId() == EscherClientDataRecord.RECORD_ID)
            {
                byte[] data = obj.serialize();
                Record[] records = Record.findChildRecords(data, 8, data.length - 8);
                for (int j = 0; j < records.length; j++)
                {
                    if (records[j].getRecordType() == recordType)
                    {
                        return records[j];
                    }
                }
            }
        }
        return oep;
    }
}
