// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;

/**
 * EMF BitmapInfo
 * 
 * @author Mark Donszelmann
 * @version $Id: BitmapInfo.java 10363 2007-01-20 15:30:50Z duns $
 */
public class BitmapInfo
{

    private BitmapInfoHeader header;

    public BitmapInfo(BitmapInfoHeader header)
    {
        this.header = header;
    }

    public BitmapInfo(EMFInputStream emf) throws IOException
    {
        header = new BitmapInfoHeader(emf);
        // colormap not necessary for true color image
    }

    public String toString()
    {
        return "  BitmapInfo\n" + header.toString();
    }

    public BitmapInfoHeader getHeader()
    {
        return header;
    }
}
