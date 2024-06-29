// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.RoundRectangle2D;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * RoundRect TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: RoundRect.java 10377 2007-01-23 15:44:34Z duns $
 */
public class RoundRect extends EMFTag
{

    private Rectangle bounds;

    private Dimension corner;

    public RoundRect()
    {
        super(44, 1);
    }

    public RoundRect(Rectangle bounds, Dimension corner)
    {
        this();
        this.bounds = bounds;
        this.corner = corner;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new RoundRect(emf.readRECTL(), emf.readSIZEL());
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  corner: " + corner;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        renderer.fillAndDrawOrAppend(new RoundRectangle2D.Double(bounds.x, bounds.x,
            bounds.getWidth(), bounds.getHeight(), corner.getWidth(), corner.getHeight()));
    }
}
