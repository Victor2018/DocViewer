// Copyright 2002-2007, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import android.graphics.Bitmap;

//import java.awt.image.BufferedImage;
import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFImageLoader;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * StretchDIBits TAG. Encoded as plain RGB rather than the not-yet-working PNG
 * format. The BI_code for BI_PNG and BI_JPG seems to be missing from the
 * WINGDI.H file of visual C++.
 * 
 * @author Mark Donszelmann
 * @version $Id: StretchDIBits.java 10510 2007-01-30 23:58:16Z duns $
 */
public class StretchDIBits extends EMFTag implements EMFConstants
{

    public final static int size = 80;

    private Rectangle bounds;

    private int x, y, width, height;

    private int xSrc, ySrc, widthSrc, heightSrc;

    private int usage, dwROP;

    private Color bkg;

    private BitmapInfo bmi;

    //private BufferedImage image;
    private Bitmap image;

    public StretchDIBits()
    {
        super(81, 1);
    }

    public StretchDIBits(Rectangle bounds, int x, int y, int width, int height,
        Bitmap image, Color bkg)
    {
        this();
        this.bounds = bounds;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xSrc = 0;
        this.ySrc = 0;
        this.widthSrc = image.getWidth();
        this.heightSrc = image.getHeight();
        this.usage = DIB_RGB_COLORS;
        this.dwROP = SRCCOPY;

        this.bkg = bkg;
        this.image = image;
        this.bmi = null;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        StretchDIBits tag = new StretchDIBits();
        tag.bounds = emf.readRECTL(); // 16
        tag.x = emf.readLONG(); // 20
        tag.y = emf.readLONG(); // 24
        tag.xSrc = emf.readLONG(); // 28
        tag.ySrc = emf.readLONG(); // 32
        tag.width = emf.readLONG(); // 36
        tag.height = emf.readLONG(); // 40
        // ignored
        emf.readDWORD(); // 44
        emf.readDWORD(); // 48
        emf.readDWORD(); // 52
        emf.readDWORD(); // 56

        tag.usage = emf.readDWORD(); // 60
        tag.dwROP = emf.readDWORD(); // 64
        tag.widthSrc = emf.readLONG(); // 68
        tag.heightSrc = emf.readLONG(); // 72

        // FIXME: this size can differ and can be placed somewhere else
        tag.bmi = new BitmapInfo(emf);

        tag.image = EMFImageLoader.readImage(tag.bmi.getHeader(), tag.width, tag.height, emf, len
            - 72 - BitmapInfoHeader.size, null);

        return tag;
    }


    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  x, y, w, h: " + x + " " + y + " "
            + width + " " + height + "\n  xSrc, ySrc, widthSrc, heightSrc: " + xSrc + " " + ySrc
            + " " + widthSrc + " " + heightSrc + "\n  usage: " + usage + "\n  dwROP: " + dwROP
            + "\n  bkg: " + bkg + "\n" + bmi.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The StretchDIBits function copies the color data for a rectangle of pixels in a
        // DIB to the specified destination rectangle. If the destination rectangle is larger
        // than the source rectangle, this function stretches the rows and columns of color
        // data to fit the destination rectangle. If the destination rectangle is smaller
        // than the source rectangle, this function compresses the rows and columns by using
        // the specified raster operation.
        if (image != null)
        {
            renderer.drawImage(image, x, y, widthSrc, heightSrc);
        }
    }
}
