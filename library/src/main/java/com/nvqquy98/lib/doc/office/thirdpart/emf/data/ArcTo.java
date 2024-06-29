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
 * ArcTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ArcTo.java 10377 2007-01-23 15:44:34Z duns $
 */
public class ArcTo extends AbstractArc {

    public ArcTo() {
        super(55, 1, null, null, null);
    }

    public ArcTo(Rectangle bounds, Point start, Point end) {
        super(55, 1, bounds, start, end);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ArcTo(
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
        // The ArcTo function draws an elliptical arc.
        //
        // BOOL ArcTo(
        // HDC hdc, // handle to device context
        // int nLeftRect, // x-coord of rectangle's upper-left corner
        // int nTopRect, // y-coord of rectangle's upper-left corner
        // int nRightRect, // x-coord of rectangle's lower-right corner
        // int nBottomRect, // y-coord of rectangle's lower-right corner
        // int nXRadial1, // x-coord of first radial ending point
        // int nYRadial1, // y-coord of first radial ending point
        // int nXRadial2, // x-coord of second radial ending point
        // int nYRadial2 // y-coord of second radial ending point
        // );
        // ArcTo is similar to the Arc function, except that the current
        // position is updated.
        //
        // The points (nLeftRect, nTopRect) and (nRightRect, nBottomRect)
        // specify the bounding rectangle.
        // An ellipse formed by the specified bounding rectangle defines the
        // curve of the arc. The arc extends
        // counterclockwise from the point where it intersects the radial
        // line from the center of the bounding
        // rectangle to the (nXRadial1, nYRadial1) point. The arc ends where
        // it intersects the radial line from
        // the center of the bounding rectangle to the (nXRadial2,
        // nYRadial2) point. If the starting point and
        // ending point are the same, a complete ellipse is drawn.
        //
        // A line is drawn from the current position to the starting point
        // of the arc.
        // If no error occurs, the current position is set to the ending
        // point of the arc.
        //
        // The arc is drawn using the current pen; it is not filled.

        renderer.getFigure().append(
            getShape(renderer, Arc2D.OPEN), true);
    }
}
