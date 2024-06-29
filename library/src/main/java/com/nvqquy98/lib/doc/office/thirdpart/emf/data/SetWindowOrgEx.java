// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Point;
import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * SetWindowOrgEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetWindowOrgEx.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetWindowOrgEx extends EMFTag
{

    private Point point;

    public SetWindowOrgEx()
    {
        super(10, 1);
    }

    public SetWindowOrgEx(Point point)
    {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetWindowOrgEx(emf.readPOINTL());
    }

    public String toString()
    {
        return super.toString() + "\n  point: " + point;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The SetWindowOrgEx function specifies which window point maps to
        // the window origin (0,0).
        renderer.setWindowOrigin(point);
        //renderer.resetTransformation();
    }
}
