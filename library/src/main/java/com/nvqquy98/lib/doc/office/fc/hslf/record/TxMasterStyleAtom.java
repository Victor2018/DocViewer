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

package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.io.IOException;
import java.io.OutputStream;

import com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties.AlignmentTextProp;
import com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties.CharFlagsTextProp;
import com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties.ParagraphFlagsTextProp;
import com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties.TextProp;
import com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties.TextPropCollection;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;


/**
 * TxMasterStyleAtom atom (4003).
 * <p>
 * Stores default character and paragraph styles.
 * The atom instance value is the text type and is encoded like the txstyle field in
 * TextHeaderAtom. The text styles are located in the MainMaster container,
 * except for the "other" style, which is in the Document.Environment container.
 * </p>
 * <p>
 *  This atom can store up to 5 pairs of paragraph+character styles,
 *  each pair describes an indent level. The first pair describes
 *  first-level paragraph with no indentation.
 * </p>
 *
 *  @author Yegor Kozlov
 */
public final class TxMasterStyleAtom extends RecordAtom
{

    /**
     * Maximum number of indentatio levels allowed in PowerPoint documents
     */
    private static final int MAX_INDENT = 5;

    protected TxMasterStyleAtom(byte[] source, int start, int len)
    {
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        _data = new byte[len - 8];
        System.arraycopy(source, start + 8, _data, 0, _data.length);

        //read available styles
        try
        {
            init();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * We are of type 4003
     *
     * @return type of this record
     * @see RecordTypes#TxMasterStyleAtom
     */
    public long getRecordType()
    {
        return _type;
    }

    /**
     * Write the contents of the record back, so it can be written
     *  to disk
     */
    public void writeOut(OutputStream out) throws IOException
    {
        // Write out the (new) header
        out.write(_header);

        // Write out the record data
        out.write(_data);

    }

    /**
     * Returns array of character styles defined in this record.
     *
     * @return character styles defined in this record
     */
    public TextPropCollection[] getCharacterStyles()
    {
        return chstyles;
    }

    /**
     * Returns array of paragraph styles defined in this record.
     *
     * @return paragraph styles defined in this record
     */
    public TextPropCollection[] getParagraphStyles()
    {
        return prstyles;
    }

    /**
     * Return type of the text.
     * Must be a constant defined in <code>TextHeaderAtom</code>
     *
     * @return type of the text
     * @see TextHeaderAtom
     */
    public int getTextType()
    {
        //The atom instance value is the text type
        return LittleEndian.getShort(_header, 0) >> 4;
    }

    /**
     * parse the record data and initialize styles
     */
    protected void init()
    {
        //type of the text
        int type = getTextType();

        int mask;
        int pos = 0;

        //number of indentation levels
        short levels = LittleEndian.getShort(_data, 0);
        pos += LittleEndian.SHORT_SIZE;

        prstyles = new TextPropCollection[levels];
        chstyles = new TextPropCollection[levels];

        for (short j = 0; j < levels; j++)
        {

            if (type >= TextHeaderAtom.CENTRE_BODY_TYPE)
            {
                // Fetch the 2 byte value, that is safe to ignore for some types of text
                short val = LittleEndian.getShort(_data, pos);
                pos += LittleEndian.SHORT_SIZE;
            }

            mask = LittleEndian.getInt(_data, pos);
            pos += LittleEndian.INT_SIZE;
            TextPropCollection prprops = new TextPropCollection(0);
            pos += prprops.buildTextPropList(mask, getParagraphProps(type, j), _data, pos);
            prstyles[j] = prprops;

            mask = LittleEndian.getInt(_data, pos);
            pos += LittleEndian.INT_SIZE;
            TextPropCollection chprops = new TextPropCollection(0);
            pos += chprops.buildTextPropList(mask, getCharacterProps(type, j), _data, pos);
            chstyles[j] = chprops;
        }

    }

    /**
     * Paragraph properties for the specified text type and
     *  indent level
     * Depending on the level and type, it may be our special
     *  ones, or the standard StyleTextPropAtom ones
     */
    protected TextProp[] getParagraphProps(int type, int level)
    {
        if (level != 0 || type >= MAX_INDENT)
        {
            //return StyleTextPropAtom.paragraphTextPropTypes;
            return new TextProp[]
                {
                    new TextProp(0, 0x1, "hasBullet"), new TextProp(0, 0x2, "hasBulletFont"),
                    new TextProp(0, 0x4, "hasBulletColor"), new TextProp(0, 0x8, "hasBulletSize"),
                    new ParagraphFlagsTextProp(), new TextProp(2, 0x80, "bullet.char"),
                    new TextProp(2, 0x10, "bullet.font"), new TextProp(2, 0x40, "bullet.size"),
                    new TextProp(4, 0x20, "bullet.color"), new AlignmentTextProp(),
                    new TextProp(2, 0x1000, "linespacing"), new TextProp(2, 0x2000, "spacebefore"),
                    new TextProp(2, 0x100, "text.offset"), new TextProp(2, 0x400, "bullet.offset"),
                    new TextProp(2, 0x4000, "spaceafter"), new TextProp(2, 0x8000, "defaultTabSize"),
                    new TextProp(2, 0x100000, "tabStops"), new TextProp(2, 0x10000, "fontAlign"),
                    new TextProp(2, 0xE0000, "wrapFlags"), new TextProp(2, 0x200000, "textDirection"),
                    new TextProp(2, 0x1000000, "buletScheme"), new TextProp(2, 0x2000000, "bulletHasScheme")
                };
            
        }
        return new TextProp[]
            {
                new ParagraphFlagsTextProp(), new TextProp(2, 0x80, "bullet.char"),
                new TextProp(2, 0x10, "bullet.font"), new TextProp(2, 0x40, "bullet.size"),
                new TextProp(4, 0x20, "bullet.color"), new TextProp(2, 0xD00, "alignment"),
                new TextProp(2, 0x1000, "linespacing"), new TextProp(2, 0x2000, "spacebefore"),
                new TextProp(2, 0x4000, "spaceafter"), new TextProp(2, 0x8000, "text.offset"),
                new TextProp(2, 0x10000, "bullet.offset"), new TextProp(2, 0x20000, "defaulttab"),
                new TextProp(2, 0x40000, "tabStops"), new TextProp(2, 0x80000, "fontAlign"), 
                new TextProp(2, 0x100000, "para_unknown_1"),new TextProp(2, 0x200000, "para_unknown_2"),
            };
    }

    /**
     * Character properties for the specified text type and
     *  indent level.
     * Depending on the level and type, it may be our special
     *  ones, or the standard StyleTextPropAtom ones
     */
    protected TextProp[] getCharacterProps(int type, int level)
    {
        if (level != 0 || type >= MAX_INDENT)
        {
            return StyleTextPropAtom.characterTextPropTypes;
        }
        return new TextProp[]
            {
                new CharFlagsTextProp(), new TextProp(2, 0x10000, "font.index"),
                new TextProp(2, 0x20000, "char_unknown_1"), new TextProp(4, 0x40000, "char_unknown_2"),
                new TextProp(2, 0x80000, "font.size"), new TextProp(2, 0x100000, "char_unknown_3"),
                new TextProp(4, 0x200000, "font.color"), new TextProp(2, 0x800000, "char_unknown_4")
            };
    }
    

    /**
     * 
     */
    public void dispose()
    {
        _header = null;
        _data = null;
        if (prstyles != null)
        {
            for(TextPropCollection ptc : prstyles)
            {
                ptc.dispose();
            }
            prstyles = null;
        }
        if (chstyles != null)
        {
            for(TextPropCollection ptc : chstyles)
            {
                ptc.dispose();
            }
            chstyles = null;
        }
    }
    
    private byte[] _header;
    private static long _type = 4003;
    private byte[] _data;

    private TextPropCollection[] prstyles;
    private TextPropCollection[] chstyles;
}
