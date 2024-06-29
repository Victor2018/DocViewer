// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * SetViewportExtEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetViewportExtEx.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetViewportExtEx extends EMFTag
{

    private Dimension size;

    public SetViewportExtEx()
    {
        super(11, 1);
    }

    public SetViewportExtEx(Dimension size)
    {
        this();
        this.size = size;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetViewportExtEx(emf.readSIZEL());
    }

    public String toString()
    {
        return super.toString() + "\n  size: " + size;
    }

    // The SetViewportExtEx function sets the horizontal and vertical
    // extents of the viewport for a device context by using the specified values.

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The viewport refers to the device coordinate system of the device space.
        // The extent is the maximum value of an axis. This function sets the maximum
        // values for the horizontal and vertical axes of the viewport in device
        // coordinates (or pixels). When mapping between page space and device space,
        // SetWindowExtEx and SetViewportExtEx determine the scaling factor between
        // the window and the viewport.
        renderer.setViewportSize(size);
    }
}
