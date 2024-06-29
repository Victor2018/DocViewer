// Copyright 2002-2007, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;

/**
 * EMF Text
 * 
 * @author Mark Donszelmann
 * @version $Id: TextW.java 10367 2007-01-22 19:26:48Z duns $
 */
public class TextW extends Text
{

    public TextW(Point pos, String string, int options, Rectangle bounds, int[] widths)
    {
        super(pos, string, options, bounds, widths);
    }

    public static TextW read(EMFInputStream emf) throws IOException
    {
        Point pos = emf.readPOINTL();
        int sLen = emf.readDWORD();
        /* int sOffset = */emf.readDWORD();
        int options = emf.readDWORD();
        Rectangle bounds = emf.readRECTL();
        /* int cOffset = */emf.readDWORD();
        // FIXME: nothing done with offsets
        String string = new String(emf.readBYTE(2 * sLen), "UTF-16LE");
        if ((2 * sLen) % 4 != 0)
            for (int i = 0; i < 4 - (2 * sLen) % 4; i++)
                emf.readBYTE();
        int[] widths = new int[sLen];
        for (int i = 0; i < sLen; i++)
            widths[i] = emf.readDWORD();
        return new TextW(pos, string, options, bounds, widths);
    }

    public String toString()
    {
        StringBuffer widthsS = new StringBuffer();
        for (int i = 0; i < string.length(); i++)
        {
            widthsS.append(",");
            widthsS.append(widths[i]);
        }
        widthsS.append(']');
        widthsS.setCharAt(0, '[');
        return "  TextW\n" + "    pos: " + pos + "\n    options: " + options + "\n    bounds: "
            + bounds + "\n    string: " + string + "\n    widths: " + widthsS;
    }
}
