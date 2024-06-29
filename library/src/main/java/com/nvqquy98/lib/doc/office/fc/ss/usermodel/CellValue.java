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

package com.nvqquy98.lib.doc.office.fc.ss.usermodel;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;

/**
 * Mimics the 'data view' of a cell. This allows formula evaluator
 * to return a CellValue instead of precasting the value to String
 * or Number or boolean type.
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 */
public final class CellValue {
	public static final CellValue TRUE = new CellValue(ICell.CELL_TYPE_BOOLEAN, 0.0, true,  null, 0);
	public static final CellValue FALSE= new CellValue(ICell.CELL_TYPE_BOOLEAN, 0.0, false, null, 0);

	private final int _cellType;
	private final double _numberValue;
	private final boolean _booleanValue;
	private final String _textValue;
	private final int _errorCode;

	private CellValue(int cellType, double numberValue, boolean booleanValue,
			String textValue, int errorCode) {
		_cellType = cellType;
		_numberValue = numberValue;
		_booleanValue = booleanValue;
		_textValue = textValue;
		_errorCode = errorCode;
	}


	public CellValue(double numberValue) {
		this(ICell.CELL_TYPE_NUMERIC, numberValue, false, null, 0);
	}
	public static CellValue valueOf(boolean booleanValue) {
		return booleanValue ? TRUE : FALSE;
	}
	public CellValue(String stringValue) {
		this(ICell.CELL_TYPE_STRING, 0.0, false, stringValue, 0);
	}
	public static CellValue getError(int errorCode) {
		return new CellValue(ICell.CELL_TYPE_ERROR, 0.0, false, null, errorCode);
	}


	/**
	 * @return Returns the booleanValue.
	 */
	public boolean getBooleanValue() {
		return _booleanValue;
	}
	/**
	 * @return Returns the numberValue.
	 */
	public double getNumberValue() {
		return _numberValue;
	}
	/**
	 * @return Returns the stringValue.
	 */
	public String getStringValue() {
		return _textValue;
	}
	/**
	 * @return Returns the cellType.
	 */
	public int getCellType() {
		return _cellType;
	}
	/**
	 * @return Returns the errorValue.
	 */
	public byte getErrorValue() {
		return (byte) _errorCode;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer(64);
		sb.append(getClass().getName()).append(" [");
		sb.append(formatAsString());
		sb.append("]");
		return sb.toString();
	}

	public String formatAsString() {
		switch (_cellType) {
			case ICell.CELL_TYPE_NUMERIC:
				return String.valueOf(_numberValue);
			case ICell.CELL_TYPE_STRING:
				return '"' + _textValue + '"';
			case ICell.CELL_TYPE_BOOLEAN:
				return _booleanValue ? "TRUE" : "FALSE";
			case ICell.CELL_TYPE_ERROR:
				return ErrorEval.getText(_errorCode);
		}
		return "<error unexpected cell type " + _cellType + ">";
	}
}
