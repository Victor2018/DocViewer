// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.geom.GeneralPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFTag;

import android.graphics.Point;

/**
 * LineTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: LineTo.java 10367 2007-01-22 19:26:48Z duns $
 */
public class LineTo extends EMFTag
{

    private Point point;

    public LineTo()
    {
        super(54, 1);
    }

    public LineTo(Point point)
    {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new LineTo(emf.readPOINTL());
    }

    public String toString()
    {
        return super.toString() + "\n  point: " + point;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The LineTo function draws a line from the current position up to,
        // but not including, the specified point.
        // The line is drawn by using the current pen and, if the pen is a
        // geometric pen, the current brush.
    	GeneralPath currentFigure = renderer.getFigure();
    	if (currentFigure != null)
    	{
    		currentFigure.lineTo((float)point.x, (float)point.y);
	        renderer.drawShape(currentFigure);
    	}
    	else
    	{
            currentFigure = new GeneralPath(renderer.getWindingRule());
            currentFigure.moveTo((float)point.x, (float)point.y);
            renderer.setFigure(currentFigure);
    	}
    }
}
