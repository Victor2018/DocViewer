// Copyright 2007, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

import android.graphics.Point;

/**
 * @author Steffen Greiffenberg
 * @version $Id$
 */
public abstract class AbstractPolygon extends EMFTag
{

    private Rectangle bounds;

    private int numberOfPoints;

    private Point[] points;

    protected AbstractPolygon(int id, int version)
    {
        super(id, version);
    }

    protected AbstractPolygon(int id, int version, Rectangle bounds, int numberOfPoints,
        Point[] points)
    {
        super(id, version);
        this.bounds = bounds;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public String toString()
    {
        String result = super.toString() + "\n  bounds: " + bounds + "\n  #points: "
            + numberOfPoints;
        if (points != null)
        {
            result += "\n  points: ";
            for (int i = 0; i < points.length; i++)
            {
                result += "[" + points[i].x + "," + points[i].y + "]";
                if (i < points.length - 1)
                {
                    result += ", ";
                }
            }
        }
        return result;
    }

    protected Rectangle getBounds()
    {
        return bounds;
    }

    protected int getNumberOfPoints()
    {
        return numberOfPoints;
    }

    protected Point[] getPoints()
    {
        return points;
    }
}
