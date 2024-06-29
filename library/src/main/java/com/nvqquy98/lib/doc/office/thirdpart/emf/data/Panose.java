// Copyright 2002, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf.data;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFConstants;
import com.nvqquy98.lib.doc.office.thirdpart.emf.EMFInputStream;

/**
 * EMF Panose
 * 
 * @author Mark Donszelmann
 * @version $Id: Panose.java 10367 2007-01-22 19:26:48Z duns $
 */
public class Panose implements EMFConstants
{

    private int familyType;

    private int serifStyle;

    private int weight;

    private int proportion;

    private int contrast;

    private int strokeVariation;

    private int armStyle;

    private int letterForm;

    private int midLine;

    private int xHeight;

    public Panose()
    {
        // FIXME, fixed
        this.familyType = PAN_NO_FIT;
        this.serifStyle = PAN_NO_FIT;
        this.proportion = PAN_NO_FIT;
        this.weight = PAN_NO_FIT;
        this.contrast = PAN_NO_FIT;
        this.strokeVariation = PAN_NO_FIT;
        this.armStyle = PAN_ANY;
        this.letterForm = PAN_ANY;
        this.midLine = PAN_ANY;
        this.xHeight = PAN_ANY;
    }

    public Panose(EMFInputStream emf) throws IOException
    {
        familyType = emf.readBYTE();
        serifStyle = emf.readBYTE();
        proportion = emf.readBYTE();
        weight = emf.readBYTE();
        contrast = emf.readBYTE();
        strokeVariation = emf.readBYTE();
        armStyle = emf.readBYTE();
        letterForm = emf.readBYTE();
        midLine = emf.readBYTE();
        xHeight = emf.readBYTE();
    }


    public String toString()
    {
        return "  Panose\n" + "    familytype: " + familyType + "\n    serifStyle: " + serifStyle
            + "\n    weight: " + weight + "\n    proportion: " + proportion + "\n    contrast: "
            + contrast + "\n    strokeVariation: " + strokeVariation + "\n    armStyle: "
            + armStyle + "\n    letterForm: " + letterForm + "\n    midLine: " + midLine
            + "\n    xHeight: " + xHeight;
    }
}
