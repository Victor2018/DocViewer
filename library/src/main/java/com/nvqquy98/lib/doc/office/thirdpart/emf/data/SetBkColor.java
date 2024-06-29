// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * SetBkColor TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetBkColor.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetBkColor extends EMFTag
{

    private Color color;

    public SetBkColor()
    {
        super(25, 1);
    }

    public SetBkColor(Color color)
    {
        this();
        this.color = color;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetBkColor(emf.readCOLORREF());
    }

    public String toString()
    {
        return super.toString() + "\n  color: " + color;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // This function fills the gaps between styled lines drawn using a
        // pen created by the CreatePen function; it does not fill the gaps
        // between styled lines drawn using a pen created by the ExtCreatePen
        // function. The SetBKColor function also sets the background colors
        // for TextOut and ExtTextOut.

        // If the background mode is OPAQUE, the background color is used to
        // fill gaps between styled lines, gaps between hatched lines in brushes,
        // and character cells. The background color is also used when converting
        // bitmaps from color to monochrome and vice versa.

        // TODO: affects TextOut and ExtTextOut, CreatePen
    }
}
