// Copyright 2002-2007, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.Arc2D;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * Arc TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: Arc.java 10377 2007-01-23 15:44:34Z duns $
 */
public class Arc extends AbstractArc {

    public Arc() {
        super(45, 1, null, null, null);
    }

    public Arc(Rectangle bounds, Point start, Point end) {
        super(45, 1, bounds, start, end);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new Arc(
            emf.readRECTL(), 
            emf.readPOINTL(), 
            emf.readPOINTL());
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The Arc function draws an elliptical arc.
        //
        // BOOL Arc(
        // HDC hdc, // handle to device context
        // int nLeftRect, // x-coord of rectangle's upper-left corner
        // int nTopRect, // y-coord of rectangle's upper-left corner
        // int nRightRect, // x-coord of rectangle's lower-right corner
        // int nBottomRect, // y-coord of rectangle's lower-right corner
        // int nXStartArc, // x-coord of first radial ending point
        // int nYStartArc, // y-coord of first radial ending point
        // int nXEndArc, // x-coord of second radial ending point
        // int nYEndArc // y-coord of second radial ending point
        // );
        // The points (nLeftRect, nTopRect) and (nRightRect, nBottomRect)
        // specify the bounding rectangle.
        // An ellipse formed by the specified bounding rectangle defines the
        // curve of the arc.
        // The arc extends in the current drawing direction from the point
        // where it intersects the
        // radial from the center of the bounding rectangle to the
        // (nXStartArc, nYStartArc) point.
        // The arc ends where it intersects the radial from the center of
        // the bounding rectangle to
        // the (nXEndArc, nYEndArc) point. If the starting point and ending
        // point are the same,
        // a complete ellipse is drawn.

        renderer.fillAndDrawOrAppend(
            getShape(renderer, Arc2D.OPEN));
    }
}
