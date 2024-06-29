// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Image;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * SetStretchBltMode TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetStretchBltMode.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetStretchBltMode extends EMFTag implements EMFConstants
{

    private int mode;

    public SetStretchBltMode()
    {
        super(21, 1);
    }

    public SetStretchBltMode(int mode)
    {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetStretchBltMode(emf.readDWORD());
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
        // The stretching mode defines how the system combines rows or columns of a
        // bitmap with existing pixels on a display device when an application calls
        // the StretchBlt function.
        renderer.setScaleMode(getScaleMode(mode));
    }

    /**
     * converts a SetStretchBltMode to a TWIP_SCALE constat of class Image
     * @return e.g. {@link java.awt.Image#SCALE_FAST}
     * @param mode EMFTag SetStretchBltMode
     */
    private int getScaleMode(int mode)
    {
        //     COLORONCOLOR 	Deletes the pixels. This mode deletes all
        // eliminated lines of pixels without trying to preserve their information.
        if (mode == EMFConstants.COLORONCOLOR /*||
                                              mode == EMFConstants.STRETCH_DELETESCANS*/)
        {
            return Image.SCALE_FAST;
        }
        //     HALFTONE 	Maps pixels from the source rectangle into blocks
        // of pixels in the destination rectangle. The average color over the
        // destination block of pixels approximates the color of the source pixels.
        else if (mode == EMFConstants.HALFTONE /*||
                                               mode == EMFConstants.STRETCH_HALFTONE*/)
        {
            return Image.SCALE_SMOOTH;
        }
        //     BLACKONWHITE 	Performs a Boolean AND operation using the
        // color values for the eliminated and existing pixels. If the bitmap
        // is a monochrome bitmap, this mode preserves black pixels at the
        // expense of white pixels.
        else if (mode == EMFConstants.BLACKONWHITE /*||
                                                   mode == EMFConstants.STRETCH_ANDSCANS*/)
        {
            // TODO not sure
            return Image.SCALE_REPLICATE;
        }
        //     WHITEONBLACK 	Performs a Boolean OR operation using the
        // color values for the eliminated and existing pixels. If the bitmap
        // is a monochrome bitmap, this mode preserves white pixels at the
        // expense of black pixels.
        else if (mode == EMFConstants.WHITEONBLACK /*||
                                                   mode == EMFConstants.STRETCH_ORSCANS*/)
        {
            // TODO not sure
            return Image.SCALE_REPLICATE;
        }
        else
        {
            return Image.SCALE_DEFAULT;
        }
    }
}
