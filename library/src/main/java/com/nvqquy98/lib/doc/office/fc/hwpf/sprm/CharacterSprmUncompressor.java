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

package com.nvqquy98.lib.doc.office.fc.hwpf.sprm;

import com.nvqquy98.lib.doc.office.fc.hwpf.model.Colorref;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.Hyphenation;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.BorderCode;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.CharacterProperties;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.DateAndTime;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.ShadingDescriptor;
import com.nvqquy98.lib.doc.office.fc.util.Internal;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;


@ Internal
public final class CharacterSprmUncompressor extends SprmUncompressor
{
    //private static final POILogger logger = POILogFactory.getLogger( CharacterSprmUncompressor.class );

    public CharacterSprmUncompressor()
    {
    }

    public static CharacterProperties uncompressCHP(CharacterProperties parent, byte[] grpprl,
        int offset)
    {
        CharacterProperties newProperties = null;
        try
        {
            newProperties = (CharacterProperties)parent.clone();
        }
        catch(CloneNotSupportedException cnse)
        {
            throw new RuntimeException("There is no way this exception should happen!!");
        }
        SprmIterator sprmIt = new SprmIterator(grpprl, offset);

        while (sprmIt.hasNext())
        {
            SprmOperation sprm = sprmIt.next();

            if (sprm.getType() != 2)
            {
                //logger.log(POILogger.WARN, "Non-CHP SPRM returned by SprmIterator: " + sprm);
                continue;
            }

            unCompressCHPOperation(parent, newProperties, sprm);
        }

        return newProperties;
    }

    /**
     * Used in decompression of a chpx. This performs an operation defined by
     * a single sprm.
     *
     * @param oldCHP The base CharacterProperties.
     * @param newCHP The current CharacterProperties.
     * @param operand The operand defined by the sprm (See Word file format spec)
     * @param param The parameter defined by the sprm (See Word file format spec)
     * @param varParam The variable length parameter defined by the sprm. (See
     *        Word file format spec)
     * @param grpprl The entire chpx that this operation is a part of.
     * @param offset The offset in the grpprl of the next sprm
     * @param styleSheet The StyleSheet for this document.
     */
    static void unCompressCHPOperation(CharacterProperties oldCHP, CharacterProperties newCHP,
        SprmOperation sprm)
    {

        switch (sprm.getOperation())
        {
            case 0:
                newCHP.setFRMarkDel(getFlag(sprm.getOperand()));
                break;
            case 0x1:
                newCHP.setFRMark(getFlag(sprm.getOperand()));
                break;
            case 0x2:
                newCHP.setFFldVanish(getFlag(sprm.getOperand()));
                break;
            case 0x3:
                // sprmCPicLocation -- 0x6A03
                /*
                 * Microsoft Office Word 97-2007 Binary File Format (.doc)
                 * Specification
                 * 
                 * Page 75 of 210
                 * 
                 * sprmCPicLocation (opcode 0x6A03) is used ONLY IN CHPX FKPs. This
                 * sprm moves the 4-byte operand of the sprm into the chp.fcPic
                 * field. It simultaneously sets chp.fSpec to 1. This sprm is also
                 * used when the chp.lTagObj field that is unioned with chp.fcPic is
                 * to be set for OLE objects.
                 */
                newCHP.setFcPic(sprm.getOperand());
                newCHP.setFSpec(true);
                break;
            case 0x4:
                newCHP.setIbstRMark((short)sprm.getOperand());
                break;
            case 0x5:
                newCHP.setDttmRMark(new DateAndTime(sprm.getGrpprl(), sprm.getGrpprlOffset()));
                break;
            case 0x6:
                newCHP.setFData(getFlag(sprm.getOperand()));
                break;
            case 0x7:
                //don't care about this
                break;
            case 0x8:
                //short chsDiff = (short)((param & 0xff0000) >>> 16);
                int operand = sprm.getOperand();
                short chsDiff = (short)(operand & 0x0000ff);
                newCHP.setFChsDiff(getFlag(chsDiff));
                newCHP.setChse((short)(operand & 0xffff00));
                break;
            case 0x9:
                newCHP.setFSpec(true);
                newCHP.setFtcSym(LittleEndian.getShort(sprm.getGrpprl(), sprm.getGrpprlOffset()));
                newCHP
                    .setXchSym(LittleEndian.getShort(sprm.getGrpprl(), sprm.getGrpprlOffset() + 2));
                break;
            case 0xa:
                newCHP.setFOle2(getFlag(sprm.getOperand()));
                break;
            case 0xb:

                // Obsolete
                break;
            case 0xc:
                newCHP.setIcoHighlight((byte)sprm.getOperand());
                newCHP.setFHighlight(getFlag(sprm.getOperand()));
                break;
            case 0xd:

                //	undocumented
                break;
            case 0xe:
                newCHP.setFcObj(sprm.getOperand());
                break;
            case 0xf:

                // undocumented
                break;
            case 0x10:

                // undocumented
                break;

            // undocumented till 0x30

            case 0x11:
                // sprmCFWebHidden
                break;
            case 0x12:
                break;
            case 0x13:
                break;
            case 0x14:
                break;
            case 0x15:
                // sprmCRsidProp
                break;
            case 0x16:
                // sprmCRsidText
                break;
            case 0x17:
                // sprmCRsidRMDel
                break;
            case 0x18:
                // sprmCFSpecVanish
                break;
            case 0x19:
                break;
            case 0x1a:
                // sprmCFMathPr
                break;
            case 0x1b:
                break;
            case 0x1c:
                break;
            case 0x1d:
                break;
            case 0x1e:
                break;
            case 0x1f:
                break;
            case 0x20:
                break;
            case 0x21:
                break;
            case 0x22:
                break;
            case 0x23:
                break;
            case 0x24:
                break;
            case 0x25:
                break;
            case 0x26:
                break;
            case 0x27:
                break;
            case 0x28:
                break;
            case 0x29:
                break;
            case 0x2a:
                break;
            case 0x2b:
                break;
            case 0x2c:
                break;
            case 0x2d:
                break;
            case 0x2e:
                break;
            case 0x2f:
                break;
            case 0x30:
                newCHP.setIstd(sprm.getOperand());
                break;
            case 0x31:

                //permutation vector for fast saves, who cares!
                break;
            case 0x32:
                newCHP.setFBold(false);
                newCHP.setFItalic(false);
                newCHP.setFOutline(false);
                newCHP.setFStrike(false);
                newCHP.setFShadow(false);
                newCHP.setFSmallCaps(false);
                newCHP.setFCaps(false);
                newCHP.setFVanish(false);
                newCHP.setKul((byte)0);
                newCHP.setIco((byte)0);
                break;
            case 0x33:
                try
                {
                    // preserve the fSpec setting from the original CHP
                    boolean fSpec = newCHP.isFSpec();
                    newCHP = (CharacterProperties)oldCHP.clone();
                    newCHP.setFSpec(fSpec);

                }
                catch(CloneNotSupportedException e)
                {
                    //do nothing
                }
                return;
            case 0x34:
                // sprmCKcd
                break;
            case 0x35:
                newCHP.setFBold(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFBold()));
                break;
            case 0x36:
                newCHP.setFItalic(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFItalic()));
                break;
            case 0x37:
                newCHP.setFStrike(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFStrike()));
                break;
            case 0x38:
                newCHP.setFOutline(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFOutline()));
                break;
            case 0x39:
                newCHP.setFShadow(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFShadow()));
                break;
            case 0x3a:
                newCHP.setFSmallCaps(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFSmallCaps()));
                break;
            case 0x3b:
                newCHP.setFCaps(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFCaps()));
                break;
            case 0x3c:
                newCHP.setFVanish(getCHPFlag((byte)sprm.getOperand(), oldCHP.isFVanish()));
                break;
            case 0x3d:
                newCHP.setFtcAscii((short)sprm.getOperand());
                break;
            case 0x3e:
                newCHP.setKul((byte)sprm.getOperand());
                break;
            case 0x3f:
                operand = sprm.getOperand();
                int hps = operand & 0xff;
                if (hps != 0)
                {
                    newCHP.setHps(hps);
                }

                //byte cInc = (byte)(((byte)(param & 0xfe00) >>> 4) >> 1);
                byte cInc = (byte)((operand & 0xff00) >>> 8);
                cInc = (byte)(cInc >>> 1);
                if (cInc != 0)
                {
                    newCHP.setHps(Math.max(newCHP.getHps() + (cInc * 2), 2));
                }

                //byte hpsPos = (byte)((param & 0xff0000) >>> 8);
                byte hpsPos = (byte)((operand & 0xff0000) >>> 16);
                if (hpsPos != 0x80)
                {
                    newCHP.setHpsPos(hpsPos);
                }
                boolean fAdjust = (operand & 0x0100) > 0;
                if (fAdjust && hpsPos != 128 && hpsPos != 0 && oldCHP.getHpsPos() == 0)
                {
                    newCHP.setHps(Math.max(newCHP.getHps() + (-2), 2));
                }
                if (fAdjust && hpsPos == 0 && oldCHP.getHpsPos() != 0)
                {
                    newCHP.setHps(Math.max(newCHP.getHps() + 2, 2));
                }
                break;
            case 0x40:
                newCHP.setDxaSpace(sprm.getOperand());
                break;
            case 0x41:
                newCHP.setLidDefault((short)sprm.getOperand());
                break;
            case 0x42:
                newCHP.setIco((byte)sprm.getOperand());
                break;
            case 0x43:
                newCHP.setHps(sprm.getOperand());
                break;
            case 0x44:
                byte hpsLvl = (byte)sprm.getOperand();
                newCHP.setHps(Math.max(newCHP.getHps() + (hpsLvl * 2), 2));
                break;
            case 0x45:
                newCHP.setHpsPos((short)sprm.getOperand());
                break;
            case 0x46:
                if (sprm.getOperand() != 0)
                {
                    if (oldCHP.getHpsPos() == 0)
                    {
                        newCHP.setHps(Math.max(newCHP.getHps() + (-2), 2));
                    }
                }
                else
                {
                    if (oldCHP.getHpsPos() != 0)
                    {
                        newCHP.setHps(Math.max(newCHP.getHps() + 2, 2));
                    }
                }
                break;
            case 0x47:
                /*CharacterProperties genCHP = new CharacterProperties ();
                genCHP.setFtcAscii (4);
                genCHP = (CharacterProperties) unCompressProperty (varParam, genCHP,
                  styleSheet);
                CharacterProperties styleCHP = styleSheet.getStyleDescription (oldCHP.
                  getBaseIstd ()).getCHP ();
                if (genCHP.isFBold () == newCHP.isFBold ())
                {
                  newCHP.setFBold (styleCHP.isFBold ());
                }
                if (genCHP.isFItalic () == newCHP.isFItalic ())
                {
                  newCHP.setFItalic (styleCHP.isFItalic ());
                }
                if (genCHP.isFSmallCaps () == newCHP.isFSmallCaps ())
                {
                  newCHP.setFSmallCaps (styleCHP.isFSmallCaps ());
                }
                if (genCHP.isFVanish () == newCHP.isFVanish ())
                {
                  newCHP.setFVanish (styleCHP.isFVanish ());
                }
                if (genCHP.isFStrike () == newCHP.isFStrike ())
                {
                  newCHP.setFStrike (styleCHP.isFStrike ());
                }
                if (genCHP.isFCaps () == newCHP.isFCaps ())
                {
                  newCHP.setFCaps (styleCHP.isFCaps ());
                }
                if (genCHP.getFtcAscii () == newCHP.getFtcAscii ())
                {
                  newCHP.setFtcAscii (styleCHP.getFtcAscii ());
                }
                if (genCHP.getFtcFE () == newCHP.getFtcFE ())
                {
                  newCHP.setFtcFE (styleCHP.getFtcFE ());
                }
                if (genCHP.getFtcOther () == newCHP.getFtcOther ())
                {
                  newCHP.setFtcOther (styleCHP.getFtcOther ());
                }
                if (genCHP.getHps () == newCHP.getHps ())
                {
                  newCHP.setHps (styleCHP.getHps ());
                }
                if (genCHP.getHpsPos () == newCHP.getHpsPos ())
                {
                  newCHP.setHpsPos (styleCHP.getHpsPos ());
                }
                if (genCHP.getKul () == newCHP.getKul ())
                {
                  newCHP.setKul (styleCHP.getKul ());
                }
                if (genCHP.getDxaSpace () == newCHP.getDxaSpace ())
                {
                  newCHP.setDxaSpace (styleCHP.getDxaSpace ());
                }
                if (genCHP.getIco () == newCHP.getIco ())
                {
                  newCHP.setIco (styleCHP.getIco ());
                }
                if (genCHP.getLidDefault () == newCHP.getLidDefault ())
                {
                  newCHP.setLidDefault (styleCHP.getLidDefault ());
                }
                if (genCHP.getLidFE () == newCHP.getLidFE ())
                {
                  newCHP.setLidFE (styleCHP.getLidFE ());
                }*/
                break;
            case 0x48:
                newCHP.setIss((byte)sprm.getOperand());
                break;
            case 0x49:
                newCHP.setHps(LittleEndian.getShort(sprm.getGrpprl(), sprm.getGrpprlOffset()));
                break;
            case 0x4a:
                int increment = LittleEndian.getShort(sprm.getGrpprl(), sprm.getGrpprlOffset());
                newCHP.setHps(Math.max(newCHP.getHps() + increment, 8));
                break;
            case 0x4b:
                newCHP.setHpsKern(sprm.getOperand());
                break;
            // Microsoft Office Word 97-2007 Binary File Format (.doc) Specification
            // Page 59 of 210
            case 0x4c:
                // sprmCMajority50 -- 0xCA4C
                // unCompressCHPOperation (oldCHP, newCHP, 0x47, param, varParam,
                // styleSheet, opSize);
                break;
            case 0x4d:
                // sprmCHpsMul -- 0x4A4D
                float percentage = sprm.getOperand() / 100.0f;
                int add = (int)(percentage * newCHP.getHps());
                newCHP.setHps(newCHP.getHps() + add);
                break;
            case 0x4e:
                // sprmCHresi -- 0x484e
                Hyphenation hyphenation = new Hyphenation((short)sprm.getOperand());
                newCHP.setHresi(hyphenation);
                break;
            case 0x4f:
                newCHP.setFtcAscii((short)sprm.getOperand());
                break;
            case 0x50:
                newCHP.setFtcFE((short)sprm.getOperand());
                break;
            case 0x51:
                newCHP.setFtcOther((short)sprm.getOperand());
                break;
            case 0x52:
                // sprmCCharScale
                break;
            case 0x53:
                newCHP.setFDStrike(getFlag(sprm.getOperand()));
                break;
            case 0x54:
                newCHP.setFImprint(getFlag(sprm.getOperand()));
                break;
            case 0x55:
                // sprmCFSpec -- 0x0855
                newCHP.setFSpec(getFlag(sprm.getOperand()));
                break;
            case 0x56:
                newCHP.setFObj(getFlag(sprm.getOperand()));
                break;
            case 0x57:
                // sprmCPropRMark -- 0xCA57
                /*
                 * Microsoft Office Word 97-2007 Binary File Format (.doc)
                 * Specification
                 * 
                 * Page 78 of 210
                 * 
                 * sprmCPropRMark (opcode 0xCA57) is interpreted by moving the first
                 * parameter byte to chp.fPropRMark, the next two bytes to
                 * chp.ibstPropRMark, and the remaining four bytes to
                 * chp.dttmPropRMark.
                 */
                byte[] buf = sprm.getGrpprl();
                int offset = sprm.getGrpprlOffset();
                newCHP.setFPropRMark(buf[offset] != 0);
                newCHP.setIbstPropRMark(LittleEndian.getShort(buf, offset + 1));
                newCHP.setDttmPropRMark(new DateAndTime(buf, offset + 3));
                break;
            case 0x58:
                newCHP.setFEmboss(getFlag(sprm.getOperand()));
                break;
            case 0x59:
                newCHP.setSfxtText((byte)sprm.getOperand());
                break;
            case 0x5a:
                // sprmCFBiDi
                break;
            case 0x5b:
                break;
            case 0x5c:
                // sprmCFBoldBi
                break;
            case 0x5d:
                // sprmCFItalicBi
                break;
            case 0x5e:
                // sprmCFtcBi
                break;
            case 0x5f:
                // sprmCLidBi 
                break;
            case 0x60:
                // sprmCIcoBi
                break;
            case 0x61:
                // sprmCHpsBi
                break;
            case 0x62:
                // sprmCDispFldRMark -- 0xCA62
                /*
                 * Microsoft Office Word 97-2007 Binary File Format (.doc)
                 * Specification
                 * 
                 * Page 78 of 210
                 * 
                 * sprmCDispFldRMark (opcode 0xCA62) is interpreted by moving the
                 * first parameter byte to chp.fDispFldRMark, the next two bytes to
                 * chp.ibstDispFldRMark, the next four bytes to
                 * chp.dttmDispFldRMark, and the remaining 32 bytes to
                 * chp.xstDispFldRMark.
                 */
                byte[] xstDispFldRMark = new byte[32];
                buf = sprm.getGrpprl();
                offset = sprm.getGrpprlOffset();
                newCHP.setFDispFldRMark(0 != buf[offset]);
                newCHP.setIbstDispFldRMark(LittleEndian.getShort(buf, offset + 1));
                newCHP.setDttmDispFldRMark(new DateAndTime(buf, offset + 3));
                System.arraycopy(buf, offset + 7, xstDispFldRMark, 0, 32);
                newCHP.setXstDispFldRMark(xstDispFldRMark);
                break;
            case 0x63:
                newCHP.setIbstRMarkDel((short)sprm.getOperand());
                break;
            case 0x64:
                newCHP.setDttmRMarkDel(new DateAndTime(sprm.getGrpprl(), sprm.getGrpprlOffset()));
                break;
            case 0x65:
                newCHP.setBrc(new BorderCode(sprm.getGrpprl(), sprm.getGrpprlOffset()));
                break;
            case 0x66:
                newCHP.setShd(new ShadingDescriptor(sprm.getGrpprl(), sprm.getGrpprlOffset()));
                break;
            case 0x67:
                // Obsolete
                break;
            case 0x68:
                //  sprmCFUsePgsuSettings
                break;
            case 0x69:
                break;
            case 0x6a:
                break;
            case 0x6b:
                break;
            case 0x6c:
                break;
            case 0x6d:
                newCHP.setLidDefault((short)sprm.getOperand());
                break;
            case 0x6e:
                newCHP.setLidFE((short)sprm.getOperand());
                break;
            case 0x6f:
                newCHP.setIdctHint((byte)sprm.getOperand());
                break;
            case 0x70:
                // sprmCCv -- 0x6870
                newCHP.setCv(new Colorref(sprm.getOperand()));
                break;
            case 0x71:
                // sprmCShd
                break;
            case 0x72:
                // sprmCBrc
                break;
            case 0x73:
                // sprmCRgLid0
                break;
            case 0x74:
                // sprmCRgLid1
            	break;
            case 0x77:
            	newCHP.setUnderlineColor(new Colorref(sprm.getOperand()));
                break;
        }
    }

    private static boolean getCHPFlag(byte x, boolean oldVal)
    {
        /*
             switch(x)
             {
         case 0:
           return false;
         case 1:
           return true;
         case (byte)0x80:
           return oldVal;
         case (byte)0x81:
           return !oldVal;
         default:
           return false;
             }
         */
        if (x == 0)
        {
            return false;
        }
        else if (x == 1)
        {
            return true;
        }
        else if ((x & 0x81) == 0x80)
        {
            return oldVal;
        }
        else if ((x & 0x81) == 0x81)
        {
            return !oldVal;
        }
        else
        {
            return false;
        }
    }

}
