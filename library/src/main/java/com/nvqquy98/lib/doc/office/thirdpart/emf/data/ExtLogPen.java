// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;

/**
 * EMF ExtLogPen
 * 
 * @author Mark Donszelmann
 * @version $Id: ExtLogPen.java 10515 2007-02-06 18:42:34Z duns $
 */
public class ExtLogPen extends AbstractPen
{

    private int penStyle;

    private int width;

    private int brushStyle;

    private Color color;

    private int hatch;

    private int[] style;

    public ExtLogPen(int penStyle, int width, int brushStyle, Color color, int hatch, int[] style)
    {

        this.penStyle = penStyle;
        this.width = width;
        this.brushStyle = brushStyle;
        this.color = color;
        this.hatch = hatch;
        this.style = style;
    }

    public ExtLogPen(EMFInputStream emf, int len) throws IOException
    {
        penStyle = emf.readDWORD();
        width = emf.readDWORD();
        brushStyle = emf.readUINT();
        color = emf.readCOLORREF();
        hatch = emf.readULONG();
        int nStyle = emf.readDWORD();
        // it seems we always have to read one!
        if (nStyle == 0 && len > 44)
            emf.readDWORD();
        style = emf.readDWORD(nStyle);
    }

    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("  ExtLogPen\n");
        s.append("    penStyle: ");
        s.append(Integer.toHexString(penStyle));
        s.append("\n");
        s.append("    width: ");
        s.append(width);
        s.append("\n");
        s.append("    brushStyle: ");
        s.append(brushStyle);
        s.append("\n");
        s.append("    color: ");
        s.append(color);
        s.append("\n");
        s.append("    hatch: ");
        s.append(hatch);
        s.append("\n");
        for (int i = 0; i < style.length; i++)
        {
            s.append("      style[");
            s.append(i);
            s.append("]: ");
            s.append(style[i]);
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        renderer.setUseCreatePen(false);
        renderer.setPenPaint(color);
        renderer.setPenStroke(createStroke(renderer, penStyle, style, width));
    }
}
