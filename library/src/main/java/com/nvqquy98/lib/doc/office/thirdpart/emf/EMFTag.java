// Copyright 2001, FreeHEP.

package com.nvqquy98.lib.doc.office.thirdpart.emf;

import java.io.IOException;

import com.nvqquy98.lib.doc.office.thirdpart.emf.data.GDIObject;
import com.nvqquy98.lib.doc.office.thirdpart.emf.io.Tag;
import com.nvqquy98.lib.doc.office.thirdpart.emf.io.TaggedInputStream;

/**
 * EMF specific tag, from which all other EMF Tags inherit.
 * 
 * @author Mark Donszelmann
 * @version $Id: EMFTag.java 10367 2007-01-22 19:26:48Z duns $
 */
public abstract class EMFTag extends Tag implements GDIObject
{

    /**
     * Constructs a EMFTag.
     * 
     * @param id id of the element
     * @param version emf version in which this element was first supported
     */
    protected EMFTag(int id, int version)
    {
        super(id, version);
    }

    public Tag read(int tagID, TaggedInputStream input, int len) throws IOException
    {

        return read(tagID, (EMFInputStream)input, len);
    }

    public abstract EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException;

    /**
     * @return a description of the tagName and tagID
     */
    public String toString()
    {
        return "EMFTag " + getName() + " (" + getTag() + ")";
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
    }
}
