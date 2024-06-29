// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * SetMapMode TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetMapMode.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetMapMode extends EMFTag implements EMFConstants
{

    private int mode;

    public SetMapMode()
    {
        super(17, 1);
    }

    public SetMapMode(int mode)
    {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetMapMode(emf.readDWORD());
    }


    public String toString()
    {
        return super.toString() + "\n  mode: " + mode;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // MM_ANISOTROPIC 	Logical units are mapped to arbitrary
        // units with arbitrarily scaled axes. Use the SetWindowExtEx
        // and SetViewportExtEx functions to specify the units,
        // orientation, and scaling.
        if (mode == EMFConstants.MM_ANISOTROPIC)
        {
            renderer.setMapModeIsotropic(false);
        }

        // MM_HIENGLISH 	Each logical unit is mapped to 0.001 inch.
        // Positive x is to the right; positive y is up.
        else if (mode == EMFConstants.MM_HIENGLISH)
        {
            // TODO not sure
            double scale = 0.001 * 25.4;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        }

        // MM_HIMETRIC 	Each logical unit is mapped to 0.01 millimeter.
        // Positive x is to the right; positive y is up.
        else if (mode == EMFConstants.MM_HIMETRIC)
        {
            // TODO not sure
            double scale = 0.01;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        }

        // MM_ISOTROPIC 	Logical units are mapped to arbitrary units
        // with equally scaled axes; that is, one unit along the x-axis
        // is equal to one unit along the y-axis. Use the SetWindowExtEx
        // and SetViewportExtEx functions to specify the units and the
        // orientation of the axes. Graphics device interface (GDI) makes
        // adjustments as necessary to ensure the x and y units remain
        // the same size (When the window extent is set, the viewport will
        // be adjusted to keep the units isotropic).
        else if (mode == EMFConstants.MM_ISOTROPIC)
        {
            renderer.setMapModeIsotropic(true);
            renderer.fixViewportSize();
        }

        // MM_LOENGLISH 	Each logical unit is mapped to 0.01 inch.
        // Positive x is to the right; positive y is up.
        else if (mode == EMFConstants.MM_LOENGLISH)
        {
            // TODO not sure
            double scale = 0.01 * 25.4;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        }

        // MM_LOMETRIC 	Each logical unit is mapped to 0.1 millimeter.
        // Positive x is to the right; positive y is up.
        else if (mode == EMFConstants.MM_LOMETRIC)
        {
            // TODO not sure
            double scale = 0.1;
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(scale, scale));
        }

        // MM_TEXT 	Each logical unit is mapped to one device pixel. Positive
        // x is to the right; positive y is down.
        else if (mode == EMFConstants.MM_TEXT)
        {
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(1, -1));
        }

        // MM_TWIPS 	Each logical unit is mapped to one twentieth of a
        // printer's point (1/1440 inch, also called a twip). Positive x
        // is to the right; positive y is up.
        else if (mode == EMFConstants.MM_TWIPS)
        {
            renderer.setMapModeTransform(AffineTransform.getScaleInstance(EMFRenderer.TWIP_SCALE,
                EMFRenderer.TWIP_SCALE));
        }
    }
}
