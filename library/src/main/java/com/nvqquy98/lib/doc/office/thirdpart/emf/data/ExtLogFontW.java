// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFRenderer;

/**
 * EMF ExtLogFontW
 * 
 * @author Mark Donszelmann
 * @version $Id: ExtLogFontW.java 10367 2007-01-22 19:26:48Z duns $
 */
public class ExtLogFontW implements EMFConstants, GDIObject
{

    private LogFontW font;

    private String fullName;

    private String style;

    private int version;

    private int styleSize;

    private int match;

    private byte[] vendorID;

    private int culture;

    private Panose panose;

    public ExtLogFontW(LogFontW font, String fullName, String style, int version, int styleSize,
        int match, byte[] vendorID, int culture, Panose panose)
    {
        this.font = font;
        this.fullName = fullName;
        this.style = style;
        this.version = version;
        this.styleSize = styleSize;
        this.match = match;
        this.vendorID = vendorID;
        this.culture = culture;
        this.panose = panose;
    }

    public ExtLogFontW(Font font)
    {
        this.font = new LogFontW(font);
        this.fullName = "";
        this.style = "";
        this.version = 0;
        this.styleSize = 0;
        this.match = 0;
        this.vendorID = new byte[]{0, 0, 0, 0};
        this.culture = 0;
        this.panose = new Panose();
    }

    public ExtLogFontW(EMFInputStream emf) throws IOException
    {
        font = new LogFontW(emf);
        fullName = emf.readWCHAR(64);
        style = emf.readWCHAR(32);
        version = emf.readDWORD();
        styleSize = emf.readDWORD();
        match = emf.readDWORD();
        emf.readDWORD();
        vendorID = emf.readBYTE(4);
        culture = emf.readDWORD();
        panose = new Panose(emf);
        emf.readWORD(); // Pad to 4-byte boundary
        // to avoid an eception
        emf.popBuffer();
    }

    public String toString()
    {
        return super.toString() + "\n  LogFontW\n" + font.toString() + "\n    fullname: "
            + fullName + "\n    style: " + style + "\n    version: " + version
            + "\n    stylesize: " + styleSize + "\n    match: " + match + "\n    vendorID: "
            + vendorID + "\n    culture: " + culture + "\n" + panose.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        renderer.setFont(font.getFont());
        renderer.setEscapement(font.getEscapement());
    }
}
