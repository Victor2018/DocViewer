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

package com.nvqquy98.lib.doc.office.fc.hssf.formula;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.CollaboratingWorkbooksEnvironment.WorkbookNotFoundException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.EvaluationWorkbook.ExternalName;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.EvaluationWorkbook.ExternalSheet;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.AreaEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.NameXEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.RefEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.FreeRefFunction;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Area3DPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.NameXPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Ptg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Ref3DPtg;
import com.nvqquy98.lib.doc.office.fc.ss.SpreadsheetVersion;
import com.nvqquy98.lib.doc.office.fc.ss.util.CellReference;
import com.nvqquy98.lib.doc.office.fc.ss.util.CellReference.NameType;


/**
 * Contains all the contextual information required to evaluate an operation
 * within a formula
 *
 * For POI internal use only
 *
 * @author Josh Micich
 */
public final class OperationEvaluationContext {
	public static final FreeRefFunction UDF = UserDefinedFunction.instance;
	private final EvaluationWorkbook _workbook;
	private final int _sheetIndex;
	private final int _rowIndex;
	private final int _columnIndex;
	private final EvaluationTracker _tracker;
	private final WorkbookEvaluator _bookEvaluator;

	private  SheetRefEvaluator _sre;
	
	public OperationEvaluationContext(WorkbookEvaluator bookEvaluator, EvaluationWorkbook workbook, int sheetIndex, int srcRowNum,
			int srcColNum, EvaluationTracker tracker) {
		_bookEvaluator = bookEvaluator;
		_workbook = workbook;
		_sheetIndex = sheetIndex;
		_rowIndex = srcRowNum;
		_columnIndex = srcColNum;
		_tracker = tracker;
		
		_sre = new SheetRefEvaluator(_bookEvaluator, _tracker, _sheetIndex);
	}

	public EvaluationWorkbook getWorkbook() {
		return _workbook;
	}

	public int getRowIndex() {
		return _rowIndex;
	}

	public int getColumnIndex() {
		return _columnIndex;
	}

	SheetRefEvaluator createExternSheetRefEvaluator(ExternSheetReferenceToken ptg) {
		return createExternSheetRefEvaluator(ptg.getExternSheetIndex());
	}
	SheetRefEvaluator createExternSheetRefEvaluator(int externSheetIndex) {
		ExternalSheet externalSheet = _workbook.getExternalSheet(externSheetIndex);
		WorkbookEvaluator targetEvaluator;
		int otherSheetIndex;
		if (externalSheet == null) {
			// sheet is in same workbook
			otherSheetIndex = _workbook.convertFromExternSheetIndex(externSheetIndex);
			targetEvaluator = _bookEvaluator;
		} else {
			// look up sheet by name from external workbook
			String workbookName = externalSheet.getWorkbookName();
			try {
				targetEvaluator = _bookEvaluator.getOtherWorkbookEvaluator(workbookName);
			} catch (WorkbookNotFoundException e) {
				throw new RuntimeException(e.getMessage());
			}
			otherSheetIndex = targetEvaluator.getSheetIndex(externalSheet.getSheetName());
			if (otherSheetIndex < 0) {
				throw new RuntimeException("Invalid sheet name '" + externalSheet.getSheetName()
						+ "' in bool '" + workbookName + "'.");
			}
		}
		return new SheetRefEvaluator(targetEvaluator, _tracker, otherSheetIndex);
	}

	/**
	 * @return <code>null</code> if either workbook or sheet is not found
	 */
	private SheetRefEvaluator createExternSheetRefEvaluator(String workbookName, String sheetName) {
		WorkbookEvaluator targetEvaluator;
		if (workbookName == null) {
			targetEvaluator = _bookEvaluator;
		} else {
			if (sheetName == null) {
				throw new IllegalArgumentException("sheetName must not be null if workbookName is provided");
			}
			try {
				targetEvaluator = _bookEvaluator.getOtherWorkbookEvaluator(workbookName);
			} catch (WorkbookNotFoundException e) {
				return null;
			}
		}
		int otherSheetIndex = sheetName == null ? _sheetIndex : targetEvaluator.getSheetIndex(sheetName);
		if (otherSheetIndex < 0) {
			return null;
		}
		return new SheetRefEvaluator(targetEvaluator, _tracker, otherSheetIndex);
	}

	public SheetRefEvaluator getRefEvaluatorForCurrentSheet()
	{
	    if(_sre == null)
	    {
	        _sre = new SheetRefEvaluator(_bookEvaluator, _tracker, _sheetIndex);
	    }
		return _sre;
	}



	/**
	 * Resolves a cell or area reference dynamically.
	 * @param workbookName the name of the workbook containing the reference.  If <code>null</code>
	 * the current workbook is assumed.  Note - to evaluate formulas which use multiple workbooks,
	 * a {@link CollaboratingWorkbooksEnvironment} must be set up.
	 * @param sheetName the name of the sheet containing the reference.  May be <code>null</code>
	 * (when <tt>workbookName</tt> is also null) in which case the current workbook and sheet is
	 * assumed.
	 * @param refStrPart1 the single cell reference or first part of the area reference.  Must not
	 * be <code>null</code>.
	 * @param refStrPart2 the second part of the area reference. For single cell references this
	 * parameter must be <code>null</code>
	 * @param isA1Style specifies the format for <tt>refStrPart1</tt> and <tt>refStrPart2</tt>.
	 * Pass <code>true</code> for 'A1' style and <code>false</code> for 'R1C1' style.
	 * TODO - currently POI only supports 'A1' reference style
	 * @return a {@link RefEval} or {@link AreaEval}
	 */
	public ValueEval getDynamicReference(String workbookName, String sheetName, String refStrPart1,
			String refStrPart2, boolean isA1Style) {
		if (!isA1Style) {
			throw new RuntimeException("R1C1 style not supported yet");
		}
		SheetRefEvaluator sre = createExternSheetRefEvaluator(workbookName, sheetName);
		if (sre == null) {
			return ErrorEval.REF_INVALID;
		}
		// ugly typecast - TODO - make spreadsheet version more easily accessible
		SpreadsheetVersion ssVersion = ((FormulaParsingWorkbook)_workbook).getSpreadsheetVersion();

		NameType part1refType = classifyCellReference(refStrPart1, ssVersion);
		switch (part1refType) {
			case BAD_CELL_OR_NAMED_RANGE:
				return ErrorEval.REF_INVALID;
			case NAMED_RANGE:
                EvaluationName nm = ((FormulaParsingWorkbook)_workbook).getName(refStrPart1, _sheetIndex);
                if(!nm.isRange()){
                    throw new RuntimeException("Specified name '" + refStrPart1 + "' is not a range as expected.");
                }
                return _bookEvaluator.evaluateNameFormula(nm.getNameDefinition(), this);
		}
		if (refStrPart2 == null) {
			// no ':'
			switch (part1refType) {
				case COLUMN:
				case ROW:
					return ErrorEval.REF_INVALID;
				case CELL:
					CellReference cr = new CellReference(refStrPart1);
					return new LazyRefEval(cr.getRow(), cr.getCol(), sre);
			}
			throw new IllegalStateException("Unexpected reference classification of '" + refStrPart1 + "'.");
		}
		NameType part2refType = classifyCellReference(refStrPart1, ssVersion);
		switch (part2refType) {
			case BAD_CELL_OR_NAMED_RANGE:
				return ErrorEval.REF_INVALID;
			case NAMED_RANGE:
				throw new RuntimeException("Cannot evaluate '" + refStrPart1
						+ "'. Indirect evaluation of defined names not supported yet");
		}

		if (part2refType != part1refType) {
			// LHS and RHS of ':' must be compatible
			return ErrorEval.REF_INVALID;
		}
		int firstRow, firstCol, lastRow, lastCol;
		switch (part1refType) {
			case COLUMN:
				firstRow =0;
				lastRow = ssVersion.getLastRowIndex();
				firstCol = parseColRef(refStrPart1);
				lastCol = parseColRef(refStrPart2);
				break;
			case ROW:
				firstCol = 0;
				lastCol = ssVersion.getLastColumnIndex();
				firstRow = parseRowRef(refStrPart1);
				lastRow = parseRowRef(refStrPart2);
				break;
			case CELL:
				CellReference cr;
				cr = new CellReference(refStrPart1);
				firstRow = cr.getRow();
				firstCol = cr.getCol();
				cr = new CellReference(refStrPart2);
				lastRow = cr.getRow();
				lastCol = cr.getCol();
				break;
			default:
				throw new IllegalStateException("Unexpected reference classification of '" + refStrPart1 + "'.");
		}
		return new LazyAreaEval(firstRow, firstCol, lastRow, lastCol, sre);
	}

	private static int parseRowRef(String refStrPart) {
		return CellReference.convertColStringToIndex(refStrPart);
	}

	private static int parseColRef(String refStrPart) {
		return Integer.parseInt(refStrPart) - 1;
	}

	private static NameType classifyCellReference(String str, SpreadsheetVersion ssVersion) {
		int len = str.length();
		if (len < 1) {
			return CellReference.NameType.BAD_CELL_OR_NAMED_RANGE;
		}
		return CellReference.classifyCellReference(str, ssVersion);
	}

	public FreeRefFunction findUserDefinedFunction(String functionName) {
		return _bookEvaluator.findUserDefinedFunction(functionName);
	}

	public ValueEval getRefEval(int rowIndex, int columnIndex) {
		SheetRefEvaluator sre = getRefEvaluatorForCurrentSheet();
		return new LazyRefEval(rowIndex, columnIndex, sre);
	}
	public ValueEval getRef3DEval(int rowIndex, int columnIndex, int extSheetIndex) {
		SheetRefEvaluator sre = createExternSheetRefEvaluator(extSheetIndex);
		return new LazyRefEval(rowIndex, columnIndex, sre);
	}
	public ValueEval getAreaEval(int firstRowIndex, int firstColumnIndex,
			int lastRowIndex, int lastColumnIndex) {
		SheetRefEvaluator sre = getRefEvaluatorForCurrentSheet();
		return new LazyAreaEval(firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex, sre);
	}
	public ValueEval getArea3DEval(int firstRowIndex, int firstColumnIndex,
			int lastRowIndex, int lastColumnIndex, int extSheetIndex) {
		SheetRefEvaluator sre = createExternSheetRefEvaluator(extSheetIndex);
		return new LazyAreaEval(firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex, sre);
	}
	public ValueEval getNameXEval(NameXPtg nameXPtg) {
      ExternalSheet externSheet = _workbook.getExternalSheet(nameXPtg.getSheetRefIndex());
      if(externSheet == null)
         return new NameXEval(nameXPtg);
      String workbookName = externSheet.getWorkbookName();
      ExternalName externName = _workbook.getExternalName(
            nameXPtg.getSheetRefIndex(), 
            nameXPtg.getNameIndex()
      );
      try{
         WorkbookEvaluator refWorkbookEvaluator = _bookEvaluator.getOtherWorkbookEvaluator(workbookName);
         EvaluationName evaluationName = refWorkbookEvaluator.getName(externName.getName(),externName.getIx()-1);
         if(evaluationName != null && evaluationName.hasFormula()){
            if (evaluationName.getNameDefinition().length > 1) {
               throw new RuntimeException("Complex name formulas not supported yet");
            }
            Ptg ptg = evaluationName.getNameDefinition()[0];
            if(ptg instanceof Ref3DPtg){
               Ref3DPtg ref3D = (Ref3DPtg)ptg;
               int sheetIndex = refWorkbookEvaluator.getSheetIndexByExternIndex(ref3D.getExternSheetIndex());
               String sheetName = refWorkbookEvaluator.getSheetName(sheetIndex);
               SheetRefEvaluator sre = createExternSheetRefEvaluator(workbookName, sheetName);
               return new LazyRefEval(ref3D.getRow(), ref3D.getColumn(), sre);
            }else if(ptg instanceof Area3DPtg){
               Area3DPtg area3D = (Area3DPtg)ptg;
               int sheetIndex = refWorkbookEvaluator.getSheetIndexByExternIndex(area3D.getExternSheetIndex());
               String sheetName = refWorkbookEvaluator.getSheetName(sheetIndex);
               SheetRefEvaluator sre = createExternSheetRefEvaluator(workbookName, sheetName);
               return new LazyAreaEval(area3D.getFirstRow(), area3D.getFirstColumn(), area3D.getLastRow(), area3D.getLastColumn(), sre);
            }
         }
         return ErrorEval.REF_INVALID;
      }catch(WorkbookNotFoundException wnfe){
         return ErrorEval.REF_INVALID;
      }
   }
}
