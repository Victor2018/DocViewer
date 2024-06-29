/*
 * 文件名称:           ExtendedParagraphAtom.java
 *  
 * 编译器:             android2.2
 * 时间:               下午5:33:25
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.LinkedList;

import com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties.AutoNumberTextProp;
import com.nvqquy98.lib.doc.office.fc.hslf.model.textproperties.TextProp;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * get extended paragraph property(auto number)
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-7-18
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public final class ExtendedParagraphAtom extends RecordAtom
{ 
    //
    private static long _type = 4012;
    // All the different kinds of extended paragraph properties we might handle
    public static TextProp[] extendedParagraphPropTypes = new TextProp[]
        {
            //new TextProp(2, 0x02000000, "BuInstance"), 
            new TextProp(2, 0x01000000, "NumberingType"),
            new TextProp(2, 0x00800000, "Start")
        };
    
    /**
     * mask == 0x03000000是需特殊处理为0x01800000
     * @param source
     * @param start
     * @param len
     */
    public ExtendedParagraphAtom(byte[] source, int start, int len)
    {
        // Sanity Checking
        if(len < 8) 
        {
            len = 8;
        }
        // Get the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Grab the text
        int pos = start + 8;
        while (pos < (start + len) && len >= 28)
        {
            if ((len - pos) < 4)
            {
                break;
            }
            AutoNumberTextProp paraProp = new AutoNumberTextProp();
            int mask = LittleEndian.getInt(source, pos);
            if (mask == 0x03000000)
            {
                mask >>= 1;
            }
            pos += 4;

            if (mask != 0)
            {
                // for BuInstance
                if (mask == 0x01800000)
                {
                    pos += 2;
                }
                else
                {
                    pos += 4;
                }
                for (int i = 0; i < extendedParagraphPropTypes.length; i++)
                {
                    int val = 0;
                    if ((mask & extendedParagraphPropTypes[i].getMask()) != 0)
                    {
                        val = LittleEndian.getShort(source, pos);
                        if ("NumberingType".equals(extendedParagraphPropTypes[i].getName()))
                        {
                            paraProp.setNumberingType(val);
                        }
                        else if ("Start".equals(extendedParagraphPropTypes[i].getName()))
                        {
                            paraProp.setStart(val);
                        }
                        pos += extendedParagraphPropTypes[i].getSize();
                    }
                    else
                    {
                        break;
                    }
                }
                if (mask == 0x01800000)
                {
                    pos += 2;
                }
            }
            autoNumberList.add(paraProp);
            pos += 8;
        }
    }
    
    /**
     * 
     */
    protected ExtendedParagraphAtom()
    {
        
    }
    
    /**
     * 
     * @return
     */
    public LinkedList<AutoNumberTextProp> getExtendedParagraphPropList()
    {
        return autoNumberList;
    }
    
    /**
     * We are of type 4012
     */
    public long getRecordType()
    {
        return _type;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        _header = null;
        if (autoNumberList != null)
        {
            for (AutoNumberTextProp  an : autoNumberList)
            {
                an.dispose();
            }
            autoNumberList.clear();
            autoNumberList = null;
        }
    }
    //
    private byte[] _header;
    //
    private LinkedList<AutoNumberTextProp> autoNumberList = new LinkedList<AutoNumberTextProp>();
}
