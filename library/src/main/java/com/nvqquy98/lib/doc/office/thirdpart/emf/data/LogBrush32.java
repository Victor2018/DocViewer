// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;
import java.util.logging.Logger;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;

/**
 * EMF LogBrush32
 *
 * @author Mark Donszelmann
 * @version $Id: LogBrush32.java 10377 2007-01-23 15:44:34Z duns $ see
 *          http://msdn.microsoft.com/library/default.asp?url=/library/en-us/gdi/brushes_8yk2.asp
 */
public class LogBrush32 implements EMFConstants, GDIObject
{

    private int style;

    private Color color;

    private int hatch;

    public LogBrush32(int style, Color color, int hatch)
    {
        this.style = style;
        this.color = color;
        this.hatch = hatch;
    }

    public LogBrush32(EMFInputStream emf) throws IOException
    {
        style = emf.readUINT();
        color = emf.readCOLORREF();
        hatch = emf.readULONG();
    }

    public String toString()
    {
        return "  LogBrush32\n" + "    style: " + style + "\n    color: " + color + "\n    hatch: "
            + hatch;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        if (style == EMFConstants.BS_SOLID)
        {
            renderer.setBrushPaint(color);
        }
        else if (style == EMFConstants.BS_NULL)
        {
            // note: same value as BS_HOLLOW
            // Should probably do this by making a paint implementation that does nothing,
            // but a 100% transparent color works just as well for now.
            renderer.setBrushPaint(new Color(0, 0, 0, 0));

            // TODO: Support pattern types
            // TODO: Support hatching
            // TODO: Support DIB types
        }
        else
        {
            Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");
            logger.warning("LogBrush32 style not supported: " + toString());
            renderer.setBrushPaint(color);
        }
    }
}
