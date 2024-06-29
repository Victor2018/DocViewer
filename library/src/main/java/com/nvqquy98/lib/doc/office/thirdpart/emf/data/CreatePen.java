// Copyright 2001, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * CreatePen TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: CreatePen.java 10367 2007-01-22 19:26:48Z duns $
 */
public class CreatePen extends EMFTag
{

    private int index;

    private LogPen pen;

    public CreatePen()
    {
        super(38, 1);
    }

    public CreatePen(int index, LogPen pen)
    {
        this();
        this.index = index;
        this.pen = pen;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new CreatePen(emf.readDWORD(), new LogPen(emf));
    }

    public String toString()
    {
        return super.toString() + "\n  index: 0x" + Integer.toHexString(index) + "\n"
            + pen.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // ExtCreatePen
        //
        // The ExtCreatePen function creates a logical cosmetic or
        // geometric pen that has the specified style, width,
        // and brush attributes.
        //
        // HPEN ExtCreatePen(
        //  DWORD dwPenStyle,      // pen style
        //  DWORD dwWidth,         // pen width
        //  CONST LOGBRUSH *lplb,  // brush attributes
        //  DWORD dwStyleCount,    // length of custom style array
        //  CONST DWORD *lpStyle   // custom style array
        //);
        renderer.storeGDIObject(index, pen);
    }
}
