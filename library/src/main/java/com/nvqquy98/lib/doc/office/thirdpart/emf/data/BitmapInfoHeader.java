// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;

/**
 * EMF BitmapInfoHeader
 * 
 * @author Mark Donszelmann
 * @version $Id: BitmapInfoHeader.java 10515 2007-02-06 18:42:34Z duns $
 */
public class BitmapInfoHeader implements EMFConstants
{

    public static final int size = 40;

    private int width;

    private int height;

    private int planes;

    private int bitCount;

    private int compression;

    private int sizeImage;

    private int xPelsPerMeter;

    private int yPelsPerMeter;

    private int clrUsed;

    private int clrImportant;

    public BitmapInfoHeader(int width, int height, int bitCount, int compression, int sizeImage,
        int xPelsPerMeter, int yPelsPerMeter, int clrUsed, int clrImportant)
    {
        this.width = width;
        this.height = height;
        this.planes = 1;
        this.bitCount = bitCount;
        this.compression = compression;
        this.sizeImage = sizeImage;
        this.xPelsPerMeter = xPelsPerMeter;
        this.yPelsPerMeter = yPelsPerMeter;
        this.clrUsed = clrUsed;
        this.clrImportant = clrImportant;
    }

    public BitmapInfoHeader(EMFInputStream emf) throws IOException
    {
        /*int len = */emf.readDWORD(); // seems fixed
        width = emf.readLONG();
        height = emf.readLONG();
        planes = emf.readWORD();
        bitCount = emf.readWORD();
        compression = emf.readDWORD();
        sizeImage = emf.readDWORD();
        xPelsPerMeter = emf.readLONG();
        yPelsPerMeter = emf.readLONG();
        clrUsed = emf.readDWORD();
        clrImportant = emf.readDWORD();
    }

    public String toString()
    {
        return "    size: " + size + "\n    width: " + width + "\n    height: " + height
            + "\n    planes: " + planes + "\n    bitCount: " + bitCount + "\n    compression: "
            + compression + "\n    sizeImage: " + sizeImage + "\n    xPelsPerMeter: "
            + xPelsPerMeter + "\n    yPelsPerMeter: " + yPelsPerMeter + "\n    clrUsed: " + clrUsed
            + "\n    clrImportant: " + clrImportant;
    }

    public int getBitCount()
    {
        return bitCount;
    }

    public int getCompression()
    {
        return compression;
    }

    public int getClrUsed()
    {
        return clrUsed;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
