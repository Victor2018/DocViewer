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

package com.nvqquy98.lib.doc.office.fc.hwpf.usermodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfo;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfoFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.DefaultEscherRecordFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherBSERecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherBlipRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherMetafileBlip;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecordFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherTertiaryOptRecord;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.EscherRecordHolder;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.FSPA;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.FSPATable;
import com.nvqquy98.lib.doc.office.system.IControl;

public class OfficeDrawingsImpl implements OfficeDrawings
{
    private final EscherRecordHolder _escherRecordHolder;
    private final FSPATable _fspaTable;
    private final byte[] _mainStream;

    public OfficeDrawingsImpl(FSPATable fspaTable, EscherRecordHolder escherRecordHolder,
        byte[] mainStream)
    {
        this._fspaTable = fspaTable;
        this._escherRecordHolder = escherRecordHolder;
        this._mainStream = mainStream;
    }

    public EscherBlipRecord getBitmapRecord(IControl control, int bitmapIndex)
    {
        List< ? extends EscherContainerRecord> bContainers = _escherRecordHolder
            .getBStoreContainers();
        if (bContainers == null || bContainers.size() != 1)
            return null;

        EscherContainerRecord bContainer = bContainers.get(0);
        final List<EscherRecord> bitmapRecords = bContainer.getChildRecords();

        if (bitmapRecords.size() < bitmapIndex)
            return null;

        EscherRecord imageRecord = bitmapRecords.get(bitmapIndex - 1);

        if (imageRecord instanceof EscherBlipRecord)
        {
            return (EscherBlipRecord)imageRecord;
        }

        if (imageRecord instanceof EscherBSERecord)
        {
            EscherBSERecord bseRecord = (EscherBSERecord)imageRecord;

            EscherBlipRecord blip = bseRecord.getBlipRecord();
            if (blip != null)
            {
                return blip;
            }

            if (bseRecord.getOffset() > 0)
            {
                /*
                 * Blip stored in delay stream, which in a word doc, is the main
                 * stream
                 */
                EscherRecordFactory recordFactory = new DefaultEscherRecordFactory();
                EscherRecord record = recordFactory
                    .createRecord(_mainStream, bseRecord.getOffset());

                if (record instanceof EscherBlipRecord)
                {
                    EscherBlipRecord blipRecord =(EscherBlipRecord)record;
                    if (blipRecord instanceof EscherMetafileBlip)
                    {
                        blipRecord.fillFields(_mainStream, bseRecord.getOffset(), recordFactory);
                        blipRecord.setTempFilePath(control.getSysKit().getPictureManage().writeTempFile(blipRecord.getPicturedata()));
                    }
                    else
                    {
                        int bytesAfterHeader = blipRecord.readHeader(_mainStream, bseRecord.getOffset());
                        int pos = bseRecord.getOffset() + 8;
                        int skip = 17;
                        byte[] b = new byte[Math.min(64, bytesAfterHeader)];
                        
                        System.arraycopy(_mainStream, pos + skip, b, 0, b.length);
                        blipRecord.setPictureData(b);
                        // 放到临时文件中
                        blipRecord.setTempFilePath(control.getSysKit().getPictureManage().writeTempFile(_mainStream, pos + skip, bytesAfterHeader - skip));
                    }
                    //field_pictureData = new byte[bytesAfterHeader];
                    //System.arraycopy(data, pos, field_pictureData, 0, bytesAfterHeader);
                    
                    //record.fillFields(_mainStream, bseRecord.getOffset(), recordFactory);
                    return blipRecord;
                }
            }
        }

        return null;
    }
    
    private boolean findEscherShapeRecordContainer(EscherContainerRecord spContainer, final int shapeId)
    {
        if (spContainer.getRecordId() == EscherContainerRecord.SPGR_CONTAINER)
        {
            for (EscherRecord escherRecord : spContainer.getChildRecords())
            {
                return findEscherShapeRecordContainer((EscherContainerRecord)escherRecord, shapeId);
            }
        }
        else
        {
            EscherSpRecord escherSpRecord = spContainer.getChildById(EscherSpRecord.RECORD_ID);
            if (escherSpRecord != null && escherSpRecord.getShapeId() == shapeId)
            {
                return true;
            }
        }
        return false;
    }

    private EscherContainerRecord getEscherShapeRecordContainer(final int shapeId)
    {
        for (EscherContainerRecord spContainer : _escherRecordHolder.getSpContainers())
        {
            if (spContainer.getRecordId() == EscherContainerRecord.SPGR_CONTAINER)
            {
                if (findEscherShapeRecordContainer(spContainer, shapeId))
                {
                    return spContainer;
                }
            }
            else
            {
                EscherSpRecord escherSpRecord = spContainer.getChildById(EscherSpRecord.RECORD_ID);
                if (escherSpRecord != null && escherSpRecord.getShapeId() == shapeId)
                {
                    return spContainer;
                }
            }
        }

        return null;
    }

    /**
     * 
     * @param fspa
     * @return
     */
    private OfficeDrawing getOfficeDrawing(final FSPA fspa)
    {
        return new OfficeDrawingImpl(fspa, this);
    }

    public OfficeDrawing getOfficeDrawingAt(int characterPosition)
    {
        final FSPA fspa = _fspaTable.getFspaFromCp(characterPosition);
        if (fspa == null)
            return null;

        return getOfficeDrawing(fspa);
    }

    public Collection<OfficeDrawing> getOfficeDrawings()
    {
        List<OfficeDrawing> result = new ArrayList<OfficeDrawing>();
        for (FSPA fspa : _fspaTable.getShapes())
        {
            result.add(getOfficeDrawing(fspa));
        }
        return Collections.unmodifiableList(result);
    }
    
    /**
     * 
     * @author ljj8494
     *
     */
    private static class OfficeDrawingImpl implements OfficeDrawing
    {
        /**
         * 
         * @param fspa
         */
        public OfficeDrawingImpl(FSPA fspa, OfficeDrawingsImpl drawings)
        {
            this.fspa = fspa;
            this.darwings = drawings;
        }
        
        /**
         * 
         *
         */
        public byte getHorizontalPositioning()
        {
            return (byte)getTertiaryPropertyValue(EscherProperties.GROUPSHAPE__POSH, HWPFShape.POSH_ABS);
        }

        /**
         * 
         *
         */
        public byte getHorizontalRelative()
        {
        	return (byte) getTertiaryPropertyValue(EscherProperties.GROUPSHAPE__POSRELH, HWPFShape.POSRELH_COLUMN);
        }

        /**
         * 
         *
         */
        public byte[] getPictureData(IControl control)
        {
            if (blipRecord != null)
            {
                return blipRecord.getPicturedata();
            }
            EscherContainerRecord shapeDescription = darwings.getEscherShapeRecordContainer(getShapeId());
            if (shapeDescription == null)
                return null;

            EscherOptRecord escherOptRecord = shapeDescription
                .getChildById(EscherOptRecord.RECORD_ID);
            if (escherOptRecord == null)
                return null;

            EscherSimpleProperty escherProperty = escherOptRecord
                .lookup(EscherProperties.BLIP__BLIPTODISPLAY);
            if (escherProperty == null)
                return null;

            int bitmapIndex = escherProperty.getPropertyValue();
            blipRecord = darwings.getBitmapRecord(control, bitmapIndex);
            if (blipRecord == null)
                return null;

            return blipRecord.getPicturedata();
        }
        
        /**
         * 
         *
         */
        public byte[] getPictureData(IControl control, int index)
        {
            if (index > 0)
            {
                blipRecord = darwings.getBitmapRecord(control, index);
                if (blipRecord != null)
                {
                    return blipRecord.getPicturedata();
                }
            }
            return null;
        }
        
        public HWPFShape getAutoShape()
        {
            EscherContainerRecord spContainer = darwings.getEscherShapeRecordContainer(getShapeId());
            if (spContainer != null)
            {
                return HWPFShapeFactory.createShape(spContainer, null);
            }
           return null;
        }

        public int getRectangleBottom()
        {
            return fspa.getYaBottom();
        }

        public int getRectangleLeft()
        {
            return fspa.getXaLeft();
        }

        public int getRectangleRight()
        {
            return fspa.getXaRight();
        }

        public int getRectangleTop()
        {
            return fspa.getYaTop();
        }

        public int getShapeId()
        {
            return fspa.getSpid();
        }
        
        public int getWrap()
        {
            return fspa.getWr();
        }
        
        public boolean isBelowText()
        {
            return fspa.isFBelowText();
        }
        
        public boolean isAnchorLock()
        {
            return fspa.isFAnchorLock();
        }

        /**
         * 
         * @return
         */
        public PictureEffectInfo getPictureEffectInfor()
        {
        	EscherContainerRecord shapeDescription = darwings.getEscherShapeRecordContainer(getShapeId());
            if (shapeDescription == null)
                return null;

            EscherOptRecord optRecord = shapeDescription
                .getChildById(EscherOptRecord.RECORD_ID);
            
            return PictureEffectInfoFactory.getPictureEffectInfor(optRecord);
        }
        
        /**
         * 
         * @param propertyId
         * @param defaultValue
         * @return
         */
        private int getTertiaryPropertyValue(int propertyId, int defaultValue)
        {
            EscherContainerRecord shapeDescription = darwings.getEscherShapeRecordContainer(getShapeId());
            if (shapeDescription == null)
                return defaultValue;

            EscherTertiaryOptRecord escherTertiaryOptRecord = shapeDescription
                .getChildById(EscherTertiaryOptRecord.RECORD_ID);
            if (escherTertiaryOptRecord == null)
                return defaultValue;

            EscherSimpleProperty escherProperty = escherTertiaryOptRecord.lookup(propertyId);
            if (escherProperty == null)
                return defaultValue;
            int value = escherProperty.getPropertyValue();

            return value;
        }

        /**
         * 
         *
         */
        public byte getVerticalPositioning()
        {
            return (byte)getTertiaryPropertyValue(EscherProperties.GROUPSHAPE__POSV, HWPFShape.POSV_ABS);
        }

        /**
         * 
         *
         */
        public byte getVerticalRelativeElement()
        {
        	 return (byte)getTertiaryPropertyValue(EscherProperties.GROUPSHAPE__POSRELV, HWPFShape.POSRELV_TEXT);
        }
        
        /**
         * 
         *
         */
        public String getTempFilePath(IControl control)
        {
            if (blipRecord == null)
            {
                getPictureData(control);
            }
            if (blipRecord != null)
            {
                return blipRecord.getTempFilePath();
            }
            return null;
        }

        @ Override
        public String toString()
        {
            return "OfficeDrawingImpl: " + fspa.toString();
        } 
        
        //
        private FSPA fspa;
        private OfficeDrawingsImpl darwings;
        private EscherBlipRecord blipRecord;
    }
}
