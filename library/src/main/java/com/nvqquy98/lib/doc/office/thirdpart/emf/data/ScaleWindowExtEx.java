// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

/**
 * ScaleWindowExtEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ScaleWindowExtEx.java 10367 2007-01-22 19:26:48Z duns $
 */
public class ScaleWindowExtEx extends EMFTag
{

    private int xNum, xDenom, yNum, yDenom;

    public ScaleWindowExtEx()
    {
        super(32, 1);
    }

    public ScaleWindowExtEx(int xNum, int xDenom, int yNum, int yDenom)
    {
        this();
        this.xNum = xNum;
        this.xDenom = xDenom;
        this.yNum = yNum;
        this.yDenom = yDenom;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new ScaleWindowExtEx(emf.readLONG(), emf.readLONG(), emf.readLONG(), emf.readLONG());
    }

    public String toString()
    {
        return super.toString() + "\n  xNum: " + xNum + "\n  xDenom: " + xDenom + "\n  yNum: "
            + yNum + "\n  yDenom: " + yDenom;
    }
}
