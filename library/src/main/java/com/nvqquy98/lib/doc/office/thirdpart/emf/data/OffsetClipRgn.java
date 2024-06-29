// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Point;
import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * OffsetClipRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: OffsetClipRgn.java 10367 2007-01-22 19:26:48Z duns $
 */
public class OffsetClipRgn extends EMFTag
{

    private Point offset;

    public OffsetClipRgn()
    {
        super(26, 1);
    }

    public OffsetClipRgn(Point offset)
    {
        this();
        this.offset = offset;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new OffsetClipRgn(emf.readPOINTL());
    }

    public String toString()
    {
        return super.toString() + "\n  offset: " + offset;
    }
}
