// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.font;

import java.io.IOException;

/**
 * NAME Table.
 * 
 * @author Mark Donszelmann
 * @version $Id: TTFNameTable.java 8584 2006-08-10 23:06:37Z duns $
 */
public class TTFNameTable extends TTFTable {

    private int format;

    private int numberOfNameRecords;

    private int stringStorage;

    private String[][] name = new String[4][19]; // 18 NameIDs according to
                                                    // OpenType

    public String getTag() {
        return "name";
    }

    // FIXME: fixed decoding for lucida files
    // PID = 0, -> UnicodeBig (Apple-Unicode-English)
    // PID = 1, EID = 0, LID = 0; -> Default Encoding (Mac-Roman-English)
    // PID = 3, EID = 1, LID = 1033; -> UnicodeBig (Win-UGL-ENU)
    // LID english, other languages ignored
    public void readTable() throws IOException {

        format = ttf.readUShort();
        numberOfNameRecords = ttf.readUShort();
        stringStorage = ttf.readUShort();

        for (int i = 0; i < numberOfNameRecords; i++) {
            int pid = ttf.readUShort();
            int eid = ttf.readUShort();
            int lid = ttf.readUShort();
            int nid = ttf.readUShort();
            int stringLen = ttf.readUShort();
            int stringOffset = ttf.readUShort();
            // long pos = ttf.getFilePointer();
            ttf.pushPos();
            ttf.seek(stringStorage + stringOffset);
            byte[] b = new byte[stringLen];
            ttf.readFully(b);
            if (pid == 0) {
                // Apple Unicode
                name[pid][nid] = new String(b, "UnicodeBig");
            } else if ((pid == 1) && (eid == 0)) {
                if (lid == 0) {
                    // Mac-Roman-English
                    name[pid][nid] = new String(b, "ISO8859-1");
                }
                // ignore other languages
            } else if ((pid == 3) && (eid == 1)) {
                // Win-UGL
                if (lid == 0x0409) {
                    // ENU
                    name[pid][nid] = new String(b, "UnicodeBig");
                }
                // ignore other languages
            } else {
                System.out.println("Unimplemented PID, EID, LID scheme: " + pid
                        + ", " + eid + ", " + lid);
                System.out.println("NID = " + nid);
                name[pid][nid] = new String(b, "Default");
            }
            ttf.popPos();
            // ttf.seek(pos);
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  format: " + format);
        for (int i = 0; i < name.length; i++) {
            for (int j = 0; j < name[i].length; j++) {
                if (name[i][j] != null) {
                    s.append("\n  name[" + i + "][" + j + "]: " + name[i][j]);
                }
            }
        }
        return s.toString();
    }
}
