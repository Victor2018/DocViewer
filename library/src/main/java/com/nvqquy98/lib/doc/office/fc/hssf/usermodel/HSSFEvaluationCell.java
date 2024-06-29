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
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ASheet;
/**
 * HSSF wrapper for a cell under evaluation
 * 
 * @author Josh Micich
 */
final class HSSFEvaluationCell implements EvaluationCell {

	private /*final*/ EvaluationSheet _evalSheet;
	private /*final*/ ACell _cell;

	public HSSFEvaluationCell(ACell cell, EvaluationSheet evalSheet) {
		_cell = cell;
		_evalSheet = evalSheet;
	}
	public HSSFEvaluationCell(ACell cell) 
	{
		this(cell, new HSSFEvaluationSheet((ASheet)cell.getSheet()));
	}
	public Object getIdentityKey() {
		// save memory by just using the cell itself as the identity key
		// Note - this assumes HSSFCell has not overridden hashCode and equals
		return _cell;
	}

	public void setHSSFCell(ACell cell) 
	{
         _cell = cell;
         if(_evalSheet != null)
         {
             ((HSSFEvaluationSheet)_evalSheet).setASheet((ASheet)cell.getSheet());
         }
         else
         {
             _evalSheet = new HSSFEvaluationSheet((ASheet)cell.getSheet());
         }
    }
	
	public ACell getACell() {
		return _cell;
	}
	public boolean getBooleanCellValue() {
		return _cell.getBooleanCellValue();
	}
	public int getCellType() {
		return _cell.getCellType();
	}
	public int getColumnIndex() {
		return _cell.getColNumber();
	}
	public int getErrorCellValue() {
		return _cell.getErrorCellValue();
	}
	public double getNumericCellValue() {
		return _cell.getNumericCellValue();
	}
	public int getRowIndex() {
		return _cell.getRowNumber();
	}
	public EvaluationSheet getSheet() {
		return _evalSheet;
	}
	public String getStringCellValue() {
		//return _cell.getRichStringCellValue().getString();
	    
	    return _cell.getStringCellValue();
	}
}
