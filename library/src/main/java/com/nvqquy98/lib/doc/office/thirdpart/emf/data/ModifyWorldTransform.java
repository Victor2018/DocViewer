// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * ModifyWorldTransform TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ModifyWorldTransform.java 10377 2007-01-23 15:44:34Z duns $
 */
public class ModifyWorldTransform extends EMFTag implements EMFConstants
{

    private AffineTransform transform;

    private int mode;

    public ModifyWorldTransform()
    {
        super(36, 1);
    }

    public ModifyWorldTransform(AffineTransform transform, int mode)
    {
        this();
        this.transform = transform;
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new ModifyWorldTransform(emf.readXFORM(), emf.readDWORD());
    }
    
    public String toString()
    {
        return super.toString() + "\n  transform: " + transform + "\n  mode: " + mode;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // MWT_IDENTITY 	Resets the current world transformation by using
        // the identity matrix. If this mode is specified, the XFORM structure
        // pointed to by lpXform is ignored.
        if (mode == EMFConstants.MWT_IDENTITY)
        {
            if (renderer.getPath() != null)
            {
                renderer.setPathTransform(new AffineTransform());
            }
            else
            {
                renderer.resetTransformation();
            }
        }

        // MWT_LEFTMULTIPLY 	Multiplies the current transformation by the
        // data in the XFORM structure. (The data in the XFORM structure becomes
        // the left multiplicand, and the data for the current transformation
        // becomes the right multiplicand.)
        else if (mode == EMFConstants.MWT_LEFTMULTIPLY)
        {
            if (renderer.getPath() != null)
            {
                renderer.getPathTransform().concatenate(transform);
                renderer.transform(transform);
            }
            else
            {
                renderer.transform(transform);
            }
        }

        // MWT_RIGHTMULTIPLY 	Multiplies the current transformation by the data
        // in the XFORM structure. (The data in the XFORM structure becomes the
        // right multiplicand, and the data for the current transformation
        // becomes the left multiplicand.)
        else if (mode != EMFConstants.MWT_RIGHTMULTIPLY)
        {
            // TODO expected that this should work but it doesn't
            // doing nothing renders the right emf embedding

            /* if (renderer.getPath() != null) {
                AffineTransform transform = new AffineTransform(this.transform);
                transform.concatenate(renderer.getPathTransform());
                renderer.setPathTransform(transform);
            } else {
                AffineTransform transform = new AffineTransform(this.transform);
                transform.concatenate(renderer.getTransform());
                renderer.resetTransformation();
                renderer.transform(transform);
            }*/
        }

        // Unknown mode
        else
        {
        }
    }
}
