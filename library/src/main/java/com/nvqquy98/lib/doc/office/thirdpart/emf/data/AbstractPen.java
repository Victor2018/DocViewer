// Copyright 2007, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.data;


import java.util.logging.Logger;

import com.nvqquy98.lib.doc.office.java.awt.Shape;
import com.nvqquy98.lib.doc.office.java.awt.Stroke;
import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.java.awt.geom.Rectangle2D;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;

/**
 * @author Steffen Greiffenberg
 * @version $Id$
 */
public abstract class AbstractPen implements EMFConstants, GDIObject {

    /**
     * Represents a stroke that draws inside the given shape.
     * Used if EMFConstants.PS_INSIDEFRAME is used
     */
    private class InsideFrameStroke implements Stroke {

        private BasicStroke stroke;

        public InsideFrameStroke (
            float width,
            int cap,
            int join,
            float miterlimit,
            float dash[],
            float dash_phase) {

            stroke = new BasicStroke(width, cap, join, miterlimit, dash, dash_phase);
        }

        public Shape createStrokedShape(Shape shape) {
            if (shape == null) {
                return null;
            }

            Rectangle2D oldBounds = shape.getBounds2D();
            float witdh = stroke.getLineWidth();

            // calcute a transformation for inside drawing
            // based on the stroke width
            AffineTransform at = new AffineTransform();
            if (oldBounds.getWidth() > 0) {
                at.scale(
                    (oldBounds.getWidth() - witdh) /
                        oldBounds.getWidth(), 1);
            }

            if (oldBounds.getHeight() > 0) {
                at.scale(1,
                    (oldBounds.getHeight() - witdh)
                        / oldBounds.getHeight());
            }

            // recalculate shape and its oldBounds
            shape = at.createTransformedShape(shape);
            Rectangle2D newBounds = shape.getBounds2D();

            // move the shape to the old origin + the line width offset
            AffineTransform moveBackTransform = AffineTransform.getTranslateInstance(
                oldBounds.getX() - newBounds.getX() + witdh / 2,
                oldBounds.getY() - newBounds.getY() + witdh / 2);
            shape = moveBackTransform.createTransformedShape(shape);

            // outline the shape using the simple basic stroke
            return stroke.createStrokedShape(shape);
        }
    }

    /**
     * logger for all instances
     */
    private static final Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");

    /**
     * returns a BasicStroke JOIN for an EMF pen style
     * @param penStyle penstyle
     * @return e.g. {@link java.awt.BasicStroke#JOIN_MITER}
     */
    protected int getJoin(int penStyle) {
        switch (penStyle & 0xF000) {
            case EMFConstants.PS_JOIN_ROUND:
                return BasicStroke.JOIN_ROUND;
            case EMFConstants.PS_JOIN_BEVEL:
                return BasicStroke.JOIN_BEVEL;
            case EMFConstants.PS_JOIN_MITER:
                return BasicStroke.JOIN_MITER;
            default:
                logger.warning("got unsupported pen style " + penStyle);
                return BasicStroke.JOIN_ROUND;
        }
    }

    /**
     * returns a BasicStroke JOIN for an EMF pen style
     * @param penStyle Style to convert
     * @return asicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE, BasicStroke.CAP_BUTT
     */
    protected int getCap(int penStyle) {
        switch (penStyle & 0xF00) {
            case EMFConstants.PS_ENDCAP_ROUND:
                return BasicStroke.CAP_ROUND;
            case EMFConstants.PS_ENDCAP_SQUARE:
                return BasicStroke.CAP_SQUARE;
            case EMFConstants.PS_ENDCAP_FLAT:
                return BasicStroke.CAP_BUTT;
            default:
                logger.warning("got unsupported pen style " + penStyle);
                return BasicStroke.CAP_ROUND;
        }
    }

    /**
     * returns a Dash for an EMF pen style
     * @param penStyle Style to convert
     * @param style used if EMFConstants#PS_USERSTYLE is set
     * @return float[] representing a dash
     */
    protected float[] getDash(int penStyle, int[] style) {
        switch (penStyle & 0xFF) {
            case EMFConstants.PS_SOLID:
                // do not use float[] { 1 }
                // it's _slow_
                return null;
            case EMFConstants.PS_DASH:
                return new float[] { 5, 5 };
            case EMFConstants.PS_DOT:
                return new float[] { 1, 2 };
            case EMFConstants.PS_DASHDOT:
                return new float[] { 5, 2, 1, 2 };
            case EMFConstants.PS_DASHDOTDOT:
                return new float[] { 5, 2, 1, 2, 1, 2 };
            case EMFConstants.PS_INSIDEFRAME:
                // Represents a pen style that consists of a solid
                // pen that is drawn from within any given bounding rectangle
                return null;
            case EMFConstants.PS_NULL:
                // do not use float[] { 1 }
                // it's _slow_
                return null;
            case EMFConstants.PS_USERSTYLE:
                if (style != null && style.length > 0) {
                    float[] result = new float[style.length];
                    for (int i = 0; i < style.length; i++) {
                        result[i] = style[i];
                    }
                    return result;
                } else {
                    return null;
                }
            default:
                logger.warning("got unsupported pen style " + penStyle);
                // do not use float[] { 1 }
                // it's _slow_
                return null;
        }
    }

    /**
     * @param penStyle stored pen style
     * @return true if PS_INSIDEFRAME is set
     */
    private boolean isInsideFrameStroke(int penStyle) {
        return (penStyle & 0xFF) == EMFConstants.PS_INSIDEFRAME;
    }

    protected Stroke createStroke(
        EMFRenderer renderer,
        int penStyle,
        int[] style,
        float width) {

        if (isInsideFrameStroke(penStyle)) {
            return new InsideFrameStroke(
                width,
                getCap(penStyle),
                getJoin(penStyle),
                renderer.getMeterLimit(),
                getDash(penStyle, style),
                0);
        } else {
            return new BasicStroke(
                width,
                getCap(penStyle),
                getJoin(penStyle),
                renderer.getMeterLimit(),
                getDash(penStyle, style),
                0);
        }
    }
}
