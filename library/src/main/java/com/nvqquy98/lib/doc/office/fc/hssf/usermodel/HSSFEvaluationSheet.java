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

package com.nvqquy98.lib.doc.office.fc.hssf.usermodel;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.EvaluationCell;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.EvaluationSheet;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ACell;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ARow;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ASheet;

/**
 * HSSF wrapper for a sheet under evaluation
 * 
 * @author Josh Micich
 */
/*final*/ class HSSFEvaluationSheet implements EvaluationSheet {

	private /*final*/ ASheet _hs;

	public HSSFEvaluationSheet(ASheet hs) {
		_hs = hs;
	}

	public void setASheet(ASheet hs)
	{
	    _hs = hs;
	}
	
	public ASheet getASheet() {
		return _hs;
	}
	public EvaluationCell getCell(int rowIndex, int columnIndex) {
		ARow row = (ARow)_hs.getRow(rowIndex);
		if (row == null) {
			return null;
		}
		ACell cell = (ACell)row.getCell(columnIndex);
		if (cell == null) {
			return null;
		}
		return new HSSFEvaluationCell(cell, this);
	}
}
