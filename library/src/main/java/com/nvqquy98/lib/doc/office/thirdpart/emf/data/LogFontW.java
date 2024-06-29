// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;


/**
 * EMF LogFontW
 * 
 * @author Mark Donszelmann
 * @version $Id: LogFontW.java 10367 2007-01-22 19:26:48Z duns $
 */
public class LogFontW implements EMFConstants, GDIObject
{

    private int height;

    private int width;

    private int escapement;

    private int orientation;

    private int weight;

    private boolean italic;

    private boolean underline;

    private boolean strikeout;

    private int charSet;

    private int outPrecision;

    private int clipPrecision;

    private int quality;

    private int pitchAndFamily;

    private String faceFamily;

    /**
     * cache for getFont()
     */
    private Font font;

    public LogFontW(int height, int width, int escapement, int orientation, int weight,
        boolean italic, boolean underline, boolean strikeout, int charSet, int outPrecision,
        int clipPrecision, int quality, int pitchAndFamily, String faceFamily)
    {
        this.height = height;
        this.width = width;
        this.escapement = escapement;
        this.orientation = orientation;
        this.weight = weight;
        this.italic = italic;
        this.underline = underline;
        this.strikeout = strikeout;
        this.charSet = charSet;
        this.outPrecision = outPrecision;
        this.clipPrecision = clipPrecision;
        this.quality = quality;
        this.pitchAndFamily = pitchAndFamily;
        this.faceFamily = faceFamily;
    }

    public LogFontW(Font font)
    {
        this.height = (int) -font.getFontSize();
        this.width = 0;
        this.escapement = 0;
        this.orientation = 0;
        this.weight = font.isBold() ? FW_BOLD : FW_NORMAL;
        this.italic = font.isItalic();
        this.underline = false;
        this.strikeout = false;
        this.charSet = 0; // ANSI_CHARSET;
        this.outPrecision = 0; // OUT_DEFAULT_PRECIS;
        this.clipPrecision = 0; // CLIP_DEFAULT_PRECIS;
        this.quality = 4; // ANTIALIASED_QUALITY;
        this.pitchAndFamily = 0;
        this.faceFamily = font.getName();
    }

    public LogFontW(EMFInputStream emf) throws IOException
    {
        height = emf.readLONG();
        width = emf.readLONG();
        escapement = emf.readLONG();
        orientation = emf.readLONG();
        weight = emf.readLONG();
        italic = emf.readBOOLEAN();
        underline = emf.readBOOLEAN();
        strikeout = emf.readBOOLEAN();
        charSet = emf.readBYTE();
        outPrecision = emf.readBYTE();
        clipPrecision = emf.readBYTE();
        quality = emf.readBYTE();
        pitchAndFamily = emf.readBYTE();
        faceFamily = emf.readWCHAR(32);
    }

    public Font getFont()
    {
        if (font == null)
        {
            int style = 0;
            if (italic)
            {
                style |= Font.ITALIC;
            }

            // 400 is considered to be normal.
            if (weight > 400)
            {
                style |= Font.BOLD;
            }

            int size = Math.abs(height);
            font = new Font(faceFamily, style, size);

        }
        return font;
    }
    
    public int getEscapement()
    {
    	return escapement;
    }

    public String toString()
    {
        return "  LogFontW\n" + "    height: " + height + "\n    width: " + width
            + "\n    orientation: " + orientation + "\n    weight: " + weight + "\n    italic: "
            + italic + "\n    underline: " + underline + "\n    strikeout: " + strikeout
            + "\n    charSet: " + charSet + "\n    outPrecision: " + outPrecision
            + "\n    clipPrecision: " + clipPrecision + "\n    quality: " + quality
            + "\n    pitchAndFamily: " + pitchAndFamily + "\n    faceFamily: " + faceFamily;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // TODO: See if this ever happens.
        renderer.setFont(font);
    }
}
