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
 * Polyline TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: Polyline.java 10367 2007-01-22 19:26:48Z duns $
 */
public class Polyline extends AbstractPolygon {

    public Polyline() {
        super(4, 1, null, 0, null);
    }

    public Polyline(Rectangle bounds, int numberOfPoints, Point[] points) {
        super(4, 1, bounds, numberOfPoints, points);
    }

    protected Polyline (int id, int version, Rectangle bounds, int numberOfPoints, Point[] points) {
        super(id, version, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new Polyline(r, n, emf.readPOINTL(n));
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
            Point p;
            for (int point = 0; point < numberOfPoints; point ++) {
                // add a point to gp
                p = points[point];
                if (point > 0) {
                    gp.lineTo((float) p.x,  (float)p.y);
                } else {
                    gp.moveTo((float) p.x,  (float)p.y);
                }
            }
            renderer.drawOrAppend(gp);
        }
    }
}
