// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * PolyPolygon16 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: PolyPolygon16.java 10367 2007-01-22 19:26:48Z duns $
 */
public class PolyPolygon16 extends AbstractPolyPolygon
{

    private int numberOfPolys;

    public PolyPolygon16()
    {
        super(91, 1, null, null, null);
    }

    public PolyPolygon16(Rectangle bounds, int numberOfPolys, int[] numberOfPoints, Point[][] points)
    {

        super(91, 1, bounds, numberOfPoints, points);
        this.numberOfPolys = numberOfPolys;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        Rectangle bounds = emf.readRECTL();
        int np = emf.readDWORD();
        /* int totalNumberOfPoints = */emf.readDWORD();
        int[] pc = new int[np];
        Point[][] points = new Point[np][];

        for (int i = 0; i < np; i++)
        {
            pc[i] = emf.readDWORD();
            points[i] = new Point[pc[i]];
        }

        for (int i = 0; i < np; i++)
        {
            points[i] = emf.readPOINTS(pc[i]);
        }

        return new PolyPolygon16(bounds, np, pc, points);
    }
}
