// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * SetWorldTransform TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetWorldTransform.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetWorldTransform extends EMFTag
{

    private AffineTransform transform;

    public SetWorldTransform()
    {
        super(35, 1);
    }

    public SetWorldTransform(AffineTransform transform)
    {
        this();
        this.transform = transform;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetWorldTransform(emf.readXFORM());
    }

    public String toString()
    {
        return super.toString() + "\n  transform: " + transform;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        if (renderer.getPath() != null)
        {
            renderer.setPathTransform(transform);
        }
        else
        {
            renderer.resetTransformation();
            renderer.transform(transform);
        }
    }
}
