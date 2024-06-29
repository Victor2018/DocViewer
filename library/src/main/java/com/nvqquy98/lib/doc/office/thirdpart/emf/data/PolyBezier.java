// Copyright 2002, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.GeneralPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * PolyBezier TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: PolyBezier.java 10367 2007-01-22 19:26:48Z duns $
 */
public class PolyBezier extends AbstractPolygon {

    public PolyBezier() {
        super(2, 1, null, 0, null);
    }

    public PolyBezier(Rectangle bounds, int numberOfPoints, Point[] points) {
        super(2, 1, bounds, numberOfPoints, points);
    }

    protected PolyBezier (int id, int version, Rectangle bounds, int numberOfPoints, Point[] points) {
        super(id, version, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new PolyBezier(r, n, emf.readPOINTL(n));
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        Point[] points = getPoints();
        int numberOfPoints = getNumberOfPoints();

        if (points != null && points.length > 0) {
            GeneralPath gp = new GeneralPath(
                renderer.getWindingRule());
            Point p = points[0];
            gp.moveTo((float) p.x,  (float)p.y);

            for (int point = 1; point < numberOfPoints; point = point + 3) {
                // add a point to gp
                Point p1 = points[point];
                Point p2 = points[point + 1];
                Point p3 = points[point + 2];
                if (point > 0) {
                    gp.curveTo(
                        (float)p1.x, (float)p1.y,
                        (float)p2.x, (float)p2.y,
                        (float)p3.x, (float)p3.y);
                }
            }
            renderer.fillAndDrawOrAppend(gp);
        }
    }
}
