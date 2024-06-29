// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;

/**
 * EMF TriVertex
 * 
 * @author Mark Donszelmann
 * @version $Id: TriVertex.java 10140 2006-12-07 07:50:41Z duns $
 */
public class TriVertex
{

    private int x, y;

    private Color color;

    public TriVertex(int x, int y, Color color)
    {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public TriVertex(EMFInputStream emf) throws IOException
    {
        x = emf.readLONG();
        y = emf.readLONG();
        color = emf.readCOLOR16();
    }

    public String toString()
    {
        return "[" + x + ", " + y + "] " + color;
    }
}
