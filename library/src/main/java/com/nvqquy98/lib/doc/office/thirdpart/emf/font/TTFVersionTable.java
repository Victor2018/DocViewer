// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.font;

import java.io.IOException;

/**
 * VERSION Table.
 * 
 * @author Simon Fischer
 * @version $Id: TTFVersionTable.java 8584 2006-08-10 23:06:37Z duns $
 */
public abstract class TTFVersionTable extends TTFTable {

    public int minorVersion;

    public int majorVersion;

    public void readVersion() throws IOException {
        majorVersion = ttf.readUShort();
        minorVersion = ttf.readUShort();
    }

    public String toString() {
        return super.toString() + " v" + majorVersion + "." + minorVersion;
    }

}
