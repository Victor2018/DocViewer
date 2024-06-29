// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * SelectObject TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SelectObject.java 10515 2007-02-06 18:42:34Z duns $
 */
public class SelectObject extends EMFTag
{

    private int index;

    public SelectObject()
    {
        super(37, 1);
    }

    public SelectObject(int index)
    {
        this();
        this.index = index;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SelectObject(emf.readDWORD());
    }

    public String toString()
    {
        return super.toString() + "\n  index: 0x" + Integer.toHexString(index);
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        GDIObject gdiObject;

        if (index < 0)
        {
            gdiObject = StockObjects.getStockObject(index);
        }
        else
        {
            gdiObject = renderer.getGDIObject(index);
        }

        if (gdiObject != null)
        {
            // render that object
            gdiObject.render(renderer);
        }
        else
        {
        }
    }
}
