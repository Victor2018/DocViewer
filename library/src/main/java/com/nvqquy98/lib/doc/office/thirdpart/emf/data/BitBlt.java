// Copyright 2002-2003, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.java.awt.geom.GeneralPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFImageLoader;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

import android.graphics.Bitmap;

/**
 * BitBlt TAG. Encoded as plain RGB rather than the not-yet-working PNG format.
 * The BI_code for BI_PNG and BI_JPG seems to be missing from the WINGDI.H file
 * of visual C++.
 * 
 * @author Mark Donszelmann
 * @version $Id: BitBlt.java 10367 2007-01-22 19:26:48Z duns $
 */
public class BitBlt extends EMFTag implements EMFConstants
{

    private final static int size = 100;

    private Rectangle bounds;

    private int x, y, width, height;

    private int dwROP;

    private int xSrc, ySrc;

    private AffineTransform transform;

    private Color bkg;

    private int usage;

    private BitmapInfo bmi;

    //private BufferedImage image;
    private Bitmap image;

    public BitBlt()
    {
        super(76, 1);
    }

    public BitBlt(Rectangle bounds, int x, int y, int width, int height, AffineTransform transform,
    		Bitmap image, Color bkg)
    {
        this();
        this.bounds = bounds;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dwROP = SRCCOPY;
        this.xSrc = 0;
        this.ySrc = 0;
        this.transform = transform;
        this.bkg = bkg;
        this.usage = DIB_RGB_COLORS;
        this.image = image;
        this.bmi = null;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        BitBlt tag = new BitBlt();

        tag.bounds = emf.readRECTL(); // 16
        tag.x = emf.readLONG(); // 20
        tag.y = emf.readLONG(); // 24
        tag.width = emf.readLONG(); // 28
        tag.height = emf.readLONG(); // 32
        tag.dwROP = emf.readDWORD(); // 36
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

        // read bmi
        if (bmiSize > 0)
        {
            tag.bmi = new BitmapInfo(emf);
        }
        else
        {
            tag.bmi = null;
        }

        if (bitmapSize > 0 && tag.bmi != null)
        {
            tag.image = EMFImageLoader.readImage(tag.bmi.getHeader(), tag.width, tag.height, emf,
                bitmapSize, null);
        }
        else
        {
            tag.image = null;
        }

        return tag;
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  x, y, w, h: " + x + " " + y + " "
            + width + " " + height + "\n  dwROP: 0x" + Integer.toHexString(dwROP)
            + "\n  xSrc, ySrc: " + xSrc + " " + ySrc + "\n  transform: " + transform + "\n  bkg: "
            + bkg + "\n  usage: " + usage + "\n"
            + ((bmi != null) ? bmi.toString() : "  bitmap: null");
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        if (image != null)
        {
            renderer.drawImage(image, transform);
        }
        else if (!bounds.isEmpty() && dwROP == 0x00F00021)
        {
        	bounds.x = x;
        	bounds.y = y;
        	//renderer.setTextBkColor();
        	renderer.fillShape(bounds);
        }
        GeneralPath currentFigure = renderer.getFigure();
        // fills the current path
        if (currentFigure != null)
        {
            renderer.fillAndDrawShape(currentFigure);
        }
    }
}
