// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * ScaleViewportExtEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ScaleViewportExtEx.java 10367 2007-01-22 19:26:48Z duns $
 */
public class ScaleViewportExtEx extends EMFTag
{

    private int xNum, xDenom, yNum, yDenom;

    public ScaleViewportExtEx()
    {
        super(31, 1);
    }

    public ScaleViewportExtEx(int xNum, int xDenom, int yNum, int yDenom)
    {
        this();
        this.xNum = xNum;
        this.xDenom = xDenom;
        this.yNum = yNum;
        this.yDenom = yDenom;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        /* int[] bytes = */emf.readUnsignedByte(len);
        return new ScaleViewportExtEx(emf.readLONG(), emf.readLONG(), emf.readLONG(),
            emf.readLONG());
    }

    public String toString()
    {
        return super.toString() + "\n  xNum: " + xNum + "\n  xDenom: " + xDenom + "\n  yNum: "
            + yNum + "\n  yDenom: " + yDenom;
    }
}
