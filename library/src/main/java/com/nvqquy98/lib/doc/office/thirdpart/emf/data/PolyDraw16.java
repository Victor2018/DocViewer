// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * PolyDraw16 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: PolyDraw16.java 10367 2007-01-22 19:26:48Z duns $
 */
public class PolyDraw16 extends EMFTag implements EMFConstants
{

    private Rectangle bounds;

    private Point[] points;

    private byte[] types;

    public PolyDraw16()
    {
        super(92, 1);
    }

    public PolyDraw16(Rectangle bounds, Point[] points, byte[] types)
    {
        this();
        this.bounds = bounds;
        this.points = points;
        this.types = types;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        int n;
        return new PolyDraw16(emf.readRECTL(), emf.readPOINTS(n = emf.readDWORD()), emf.readBYTE(n));
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  #points: " + points.length;
    }
}
