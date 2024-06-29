/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.nvqquy98.lib.doc.office.fc.ss.util;


import com.nvqquy98.lib.doc.office.fc.hssf.formula.SheetNameFormatter;
import com.nvqquy98.lib.doc.office.fc.hssf.record.RecordInputStream;
import com.nvqquy98.lib.doc.office.fc.hssf.record.SelectionRecord;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndianByteArrayOutputStream;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndianOutput;


/**
 * See OOO documentation: excelfileformat.pdf sec 2.5.14 - 'Cell Range Address'<p/>
 * 
 * <p>In the Microsoft documentation, this is also known as a 
 *  Ref8U - see page 831 of version 1.0 of the documentation.
 *
 * Note - {@link SelectionRecord} uses the BIFF5 version of this structure
 * @author Dragos Buleandra (dragos.buleandra@trade2b.ro)
 */
public class HSSFCellRangeAddress extends CellRangeAddressBase {
	/*
	 * TODO - replace  org.apache.poi.hssf.util.Region
	 */
	public static final int ENCODED_SIZE = 8;

	public HSSFCellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
		super(firstRow, lastRow, firstCol, lastCol);
	}

	/**
	 * @deprecated use {@link #serialize(LittleEndianOutput)}
	 */
	public int serialize(int offset, byte[] data) {
		serialize(new LittleEndianByteArrayOutputStream(data, offset, ENCODED_SIZE));
		return ENCODED_SIZE;
	}
	public void serialize(LittleEndianOutput out) {
		out.writeShort(getFirstRow());
		out.writeShort(getLastRow());
		out.writeShort(getFirstColumn());
		out.writeShort(getLastColumn());
	}

	public HSSFCellRangeAddress(RecordInputStream in) {
		super(readUShortAndCheck(in), in.readUShort(), in.readUShort(), in.readUShort());
	}

	private static int readUShortAndCheck(RecordInputStream in) {
		if (in.remaining() < ENCODED_SIZE) {
			// Ran out of data
			throw new RuntimeException("Ran out of data reading CellRangeAddress");
		}
		return in.readUShort();
	}

	public HSSFCellRangeAddress copy() {
		return new HSSFCellRangeAddress(getFirstRow(), getLastRow(), getFirstColumn(), getLastColumn());
	}

	public static int getEncodedSize(int numberOfItems) {
		return numberOfItems * ENCODED_SIZE;
	}

    /**
     * @return the text format of this range.  Single cell ranges are formatted
     *         like single cell references (e.g. 'A1' instead of 'A1:A1').
     */
    public String formatAsString() {
        return formatAsString(null, false);
    }

    /**
     * @return the text format of this range using specified sheet name.
     */
    public String formatAsString(String sheetName, boolean useAbsoluteAddress) {
        StringBuffer sb = new StringBuffer();
        if (sheetName != null) {
            sb.append(SheetNameFormatter.format(sheetName));
            sb.append("!");
        }
        CellReference cellRefFrom = new CellReference(getFirstRow(), getFirstColumn(),
                useAbsoluteAddress, useAbsoluteAddress);
        CellReference cellRefTo = new CellReference(getLastRow(), getLastColumn(),
                useAbsoluteAddress, useAbsoluteAddress);
        sb.append(cellRefFrom.formatAsString());

        //for a single-cell reference return A1 instead of A1:A1
        if(!cellRefFrom.equals(cellRefTo)){
            sb.append(':');
            sb.append(cellRefTo.formatAsString());
        }
        return sb.toString();
    }

    /**
     * @param ref usually a standard area ref (e.g. "B1:D8").  May be a single cell
     *            ref (e.g. "B5") in which case the result is a 1 x 1 cell range.
     */
    public static HSSFCellRangeAddress valueOf(String ref) {
        int sep = ref.indexOf(":");
        CellReference a;
        CellReference b;
        if (sep == -1) {
            a = new CellReference(ref);
            b = a;
        } else {
            a = new CellReference(ref.substring(0, sep));
            b = new CellReference(ref.substring(sep + 1));
        }
        return new HSSFCellRangeAddress(a.getRow(), b.getRow(), a.getCol(), b.getCol());
    }
}
