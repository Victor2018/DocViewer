// Copyright 2002-2007, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFImageLoader;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

import android.graphics.Bitmap;

/**
 * PNG and JPG seem not to work.
 * 
 * @author Mark Donszelmann
 * @version $Id: AlphaBlend.java 10367 2007-01-22 19:26:48Z duns $
 */
public class AlphaBlend extends EMFTag implements EMFConstants
{

    private final static int size = 108;

    private Rectangle bounds;

    private int x, y, width, height;

    private BlendFunction dwROP;

    private int xSrc, ySrc;

    private AffineTransform transform;

    private Color bkg;

    private int usage;

    private BitmapInfo bmi;

    //private BufferedImage image;
    private Bitmap image;

    public AlphaBlend()
    {
        super(114, 1);
    }

    public AlphaBlend(Rectangle bounds, int x, int y, int width, int height,
        AffineTransform transform, Bitmap image, Color bkg)
    {

        this();
        this.bounds = bounds;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dwROP = new BlendFunction(AC_SRC_OVER, 0, 0xFF, AC_SRC_ALPHA);
        this.xSrc = 0;
        this.ySrc = 0;
        this.transform = transform;
        this.bkg = (bkg == null) ? new Color(0, 0, 0, 0) : bkg;
        this.usage = DIB_RGB_COLORS;
        this.image = image;
        this.bmi = null;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        AlphaBlend tag = new AlphaBlend();
        tag.bounds = emf.readRECTL(); // 16
        tag.x = emf.readLONG(); // 20
        tag.y = emf.readLONG(); // 24
        tag.width = emf.readLONG(); // 28
        tag.height = emf.readLONG(); // 32
        tag.dwROP = new BlendFunction(emf); // 36
        tag.xSrc = emf.readLONG(); // 40
        tag.ySrc = emf.readLONG(); // 44
        tag.transform = emf.readXFORM(); // 68
        tag.bkg = emf.readCOLORREF(); // 72
        tag.usage = emf.readDWORD(); // 76

        // ignored
        /* int bmiOffset = */emf.readDWORD(); // 80
        int bmiSize = emf.readDWORD(); // 84
        /* int bitmapOffset = */emf.readDWORD(); // 88
        int bitmapSize = emf.readDWORD(); // 92

        /* int width = */emf.readLONG(); // 96
        /* int height = */emf.readLONG(); // 100

        // FIXME: this size can differ and can be placed somewhere else
        tag.bmi = (bmiSize > 0) ? new BitmapInfo(emf) : null;

        tag.image = EMFImageLoader.readImage(tag.bmi.getHeader(), tag.width, tag.height, emf,
            /*bitmapSize*/len - 100 - BitmapInfoHeader.size, tag.dwROP);

        return tag;
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  x, y, w, h: " + x + " " + y + " "
            + width + " " + height + "\n  dwROP: " + dwROP + "\n  xSrc, ySrc: " + xSrc + " " + ySrc
            + "\n  transform: " + transform + "\n  bkg: " + bkg + "\n  usage: " + usage + "\n"
            + ((bmi != null) ? bmi.toString() : "  bitmap: null");
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // This function displays bitmaps that have transparent or semitransparent pixels.
        if (image != null)
        {
            renderer.drawImage(image, x, y, width, height);
        }
    }
}
