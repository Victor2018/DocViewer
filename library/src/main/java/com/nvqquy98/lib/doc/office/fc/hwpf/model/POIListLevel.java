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

package com.nvqquy98.lib.doc.office.fc.hwpf.model;

import java.util.Arrays;

import com.nvqquy98.lib.doc.office.fc.hwpf.sprm.SprmIterator;
import com.nvqquy98.lib.doc.office.fc.hwpf.sprm.SprmOperation;
import com.nvqquy98.lib.doc.office.fc.util.BitField;
import com.nvqquy98.lib.doc.office.fc.util.BitFieldFactory;
import com.nvqquy98.lib.doc.office.fc.util.Internal;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;


/**
 * "List LeVeL (on File) (LVLF)"
 * 
 * See page 170 for details.
 */
@ Internal
public final class POIListLevel
{
    private static final int RGBXCH_NUMS_SIZE = 9;

    private int _iStartAt;
    private byte _nfc;
    private byte _info;
    /*   */private static BitField _jc = BitFieldFactory.getInstance(0x1);
    /*   */private static BitField _fLegal;
    /*   */private static BitField _fNoRestart;
    /*   */private static BitField _fPrev;
    /*   */private static BitField _fPrevSpace;
    /*   */private static BitField _fWord6;
    private byte[] _rgbxchNums;
    private byte _ixchFollow;
    private int _dxaSpace;
    private int _dxaIndent;
    private int _speecialIndent;
    private int _cbGrpprlChpx;
    private int _cbGrpprlPapx;
    private short _reserved;
    private byte[] _grpprlPapx;
    private byte[] _grpprlChpx;
    private char[] _numberText = null;

    public POIListLevel(final byte[] buf, final int originalOffset)
    {
        int offset = originalOffset;

        _iStartAt = LittleEndian.getInt(buf, offset);
        offset += LittleEndian.INT_SIZE;
        _nfc = buf[offset++];
        _info = buf[offset++];

        _rgbxchNums = new byte[RGBXCH_NUMS_SIZE];
        System.arraycopy(buf, offset, _rgbxchNums, 0, RGBXCH_NUMS_SIZE);
        offset += RGBXCH_NUMS_SIZE;

        _ixchFollow = buf[offset++];
        _dxaSpace = LittleEndian.getInt(buf, offset);
        offset += LittleEndian.INT_SIZE;
        _dxaIndent = LittleEndian.getInt(buf, offset);
        offset += LittleEndian.INT_SIZE;
        _cbGrpprlChpx = LittleEndian.getUnsignedByte(buf, offset++);
        _cbGrpprlPapx = LittleEndian.getUnsignedByte(buf, offset++);
        _reserved = LittleEndian.getShort(buf, offset);
        offset += LittleEndian.SHORT_SIZE;

        _grpprlPapx = new byte[_cbGrpprlPapx];
        _grpprlChpx = new byte[_cbGrpprlChpx];
        System.arraycopy(buf, offset, _grpprlPapx, 0, _cbGrpprlPapx);
        offset += _cbGrpprlPapx;
        System.arraycopy(buf, offset, _grpprlChpx, 0, _cbGrpprlChpx);
        offset += _cbGrpprlChpx;

        int numberTextLength = LittleEndian.getShort(buf, offset);
        /* sometimes numberTextLength<0 */
        /* by derjohng */
        if (numberTextLength > 0)
        {
            _numberText = new char[numberTextLength];
            offset += LittleEndian.SHORT_SIZE;
            for (int x = 0; x < numberTextLength; x++)
            {
                if (offset >= buf.length)
                {
                    break;
                }
                _numberText[x] = (char)LittleEndian.getShort(buf, offset);
                offset += LittleEndian.SHORT_SIZE;
            }
        }
        
        // 
        SprmIterator sprmIt = new SprmIterator(_grpprlPapx, 0);
        while (sprmIt.hasNext())
        {
            SprmOperation sprm = sprmIt.next();
            // PAPXs can contain table sprms if the paragraph marks the end of a
            // table row
            if (sprm.getType() == SprmOperation.TYPE_PAP)
            {
                switch (sprm.getOperation())
                {
                    case 0xf:
                        _dxaIndent = sprm.getOperand();
                        break;
                    case 0x11:
                        _speecialIndent = sprm.getOperand();
                        break;
                }
            }
        }

    }

    public POIListLevel(int level, boolean numbered)
    {
        _iStartAt = 1;
        _grpprlPapx = new byte[0];
        _grpprlChpx = new byte[0];
        _numberText = new char[0];
        _rgbxchNums = new byte[RGBXCH_NUMS_SIZE];

        if (numbered)
        {
            _rgbxchNums[0] = 1;
            _numberText = new char[]{(char)level, '.'};
        }
        else
        {
            _numberText = new char[]{'\u2022'};
        }
    }

    public POIListLevel(int startAt, int numberFormatCode, int alignment, byte[] numberProperties,
        byte[] entryProperties, String numberText)
    {
        _iStartAt = startAt;
        _nfc = (byte)numberFormatCode;
        _jc.setValue(_info, alignment);
        _grpprlChpx = numberProperties;
        _grpprlPapx = entryProperties;
        _numberText = numberText.toCharArray();
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;

        POIListLevel lvl = (POIListLevel)obj;
        return _cbGrpprlChpx == lvl._cbGrpprlChpx && lvl._cbGrpprlPapx == _cbGrpprlPapx
            && lvl._dxaIndent == _dxaIndent && lvl._dxaSpace == _dxaSpace
            && Arrays.equals(lvl._grpprlChpx, _grpprlChpx)
            && Arrays.equals(lvl._grpprlPapx, _grpprlPapx) && lvl._info == _info
            && lvl._iStartAt == _iStartAt && lvl._ixchFollow == _ixchFollow && lvl._nfc == _nfc
            && Arrays.equals(lvl._numberText, _numberText)
            && Arrays.equals(lvl._rgbxchNums, _rgbxchNums) && lvl._reserved == _reserved;
    }

    /**
     * "Alignment (left, right, or centered) of the paragraph number."
     */
    public int getAlignment()
    {
        return _jc.getValue(_info);
    }

    public byte[] getLevelProperties()
    {
        return _grpprlPapx;
    }

    /**
     * "Number format code (see anld.nfc for a list of options)"
     */
    public int getNumberFormat()
    {
        return _nfc;
    }

    public String getNumberText()
    {
        if (_numberText == null)
            return null;

        return new String(_numberText);
    }
    
    public char[] getNumberChar()
    {
        return _numberText;
    }
    
    /**
     * 
     * @return
     */
    public int getTextIndent()
    {
        return _dxaIndent;
    }
    
    /**
     * 
     * @return
     */
    public int getSpecialIndnet()
    {
        return _speecialIndent;
    }

    public int getSizeInBytes()
    {
        int result = 6 // int byte byte
            + RGBXCH_NUMS_SIZE + 13 // byte int int byte byte short
            + _cbGrpprlChpx + _cbGrpprlPapx + 2; // numberText length
        if (_numberText != null)
        {
            result += _numberText.length * LittleEndian.SHORT_SIZE;
        }
        return result;
    }

    public int getStartAt()
    {
        return _iStartAt;
    }

    /**
     * "The type of character following the number text for the paragraph: 0 == tab, 1 == space, 2 == nothing."
     */
    public byte getTypeOfCharFollowingTheNumber()
    {
        return this._ixchFollow;
    }

    public void setAlignment(int alignment)
    {
        _jc.setValue(_info, alignment);
    }

    public void setLevelProperties(byte[] grpprl)
    {
        _grpprlPapx = grpprl;
    }

    public void setNumberFormat(int numberFormatCode)
    {
        _nfc = (byte)numberFormatCode;
    }

    public void setNumberProperties(byte[] grpprl)
    {
        _grpprlChpx = grpprl;

    }

    public void setStartAt(int startAt)
    {
        _iStartAt = startAt;
    }

    public void setTypeOfCharFollowingTheNumber(byte value)
    {
        this._ixchFollow = value;
    }

    public byte[] toByteArray()
    {
        byte[] buf = new byte[getSizeInBytes()];
        int offset = 0;
        LittleEndian.putInt(buf, offset, _iStartAt);
        offset += LittleEndian.INT_SIZE;
        buf[offset++] = _nfc;
        buf[offset++] = _info;
        System.arraycopy(_rgbxchNums, 0, buf, offset, RGBXCH_NUMS_SIZE);
        offset += RGBXCH_NUMS_SIZE;
        buf[offset++] = _ixchFollow;
        LittleEndian.putInt(buf, offset, _dxaSpace);
        offset += LittleEndian.INT_SIZE;
        LittleEndian.putInt(buf, offset, _dxaIndent);
        offset += LittleEndian.INT_SIZE;

        buf[offset++] = (byte)_cbGrpprlChpx;
        buf[offset++] = (byte)_cbGrpprlPapx;
        LittleEndian.putShort(buf, offset, _reserved);
        offset += LittleEndian.SHORT_SIZE;

        System.arraycopy(_grpprlPapx, 0, buf, offset, _cbGrpprlPapx);
        offset += _cbGrpprlPapx;
        System.arraycopy(_grpprlChpx, 0, buf, offset, _cbGrpprlChpx);
        offset += _cbGrpprlChpx;

        if (_numberText == null)
        {
            // TODO - write junit to test this flow
            LittleEndian.putUShort(buf, offset, 0);
        }
        else
        {
            LittleEndian.putUShort(buf, offset, _numberText.length);
            offset += LittleEndian.SHORT_SIZE;
            for (int x = 0; x < _numberText.length; x++)
            {
                LittleEndian.putUShort(buf, offset, _numberText[x]);
                offset += LittleEndian.SHORT_SIZE;
            }
        }
        return buf;
    }

}
