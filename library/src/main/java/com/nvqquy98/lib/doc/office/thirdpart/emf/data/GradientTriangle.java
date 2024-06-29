// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;

/**
 * EMF GradientTriangle
 * 
 * @author Mark Donszelmann
 * @version $Id: GradientTriangle.java 10140 2006-12-07 07:50:41Z duns $
 */
public class GradientTriangle extends Gradient
{

    private int vertex1, vertex2, vertex3;

    public GradientTriangle(int vertex1, int vertex2, int vertex3)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    public GradientTriangle(EMFInputStream emf) throws IOException
    {
        vertex1 = emf.readULONG();
        vertex2 = emf.readULONG();
        vertex3 = emf.readULONG();
    }

    public String toString()
    {
        return "  GradientTriangle: " + vertex1 + ", " + vertex2 + ", " + vertex3;
    }
}
