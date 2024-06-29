// Copyright 2001, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * ExtCreatePen TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ExtCreatePen.java 10367 2007-01-22 19:26:48Z duns $
 */
public class ExtCreatePen extends EMFTag
{

    private int index;

    private ExtLogPen pen;

    public ExtCreatePen()
    {
        super(95, 1);
    }

    public ExtCreatePen(int index, ExtLogPen pen)
    {
        this();
        this.index = index;
        this.pen = pen;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        int index = emf.readDWORD();
        /* int bmiOffset = */emf.readDWORD();
        /* int bmiSize = */emf.readDWORD();
        /* int brushOffset = */emf.readDWORD();
        /* int brushSize = */emf.readDWORD();
        return new ExtCreatePen(index, new ExtLogPen(emf, len));
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
        renderer.storeGDIObject(index, pen);
    }
}
