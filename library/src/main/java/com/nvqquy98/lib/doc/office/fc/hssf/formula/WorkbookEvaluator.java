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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.CollaboratingWorkbooksEnvironment.WorkbookNotFoundException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.AreaEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.BlankEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.BoolEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.EvaluationException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.MissingArgEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.NameEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.NotImplementedException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.NumberEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.OperandResolver;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.RefEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.StringEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.Choose;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.FreeRefFunction;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.IfFunc;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Area3DPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.AreaErrPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.AreaPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.AttrPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.BoolPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.ControlPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.DeletedArea3DPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.DeletedRef3DPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.ErrPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.ExpPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.FuncVarPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.IntPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.MemAreaPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.MemErrPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.MemFuncPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.MissingArgPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.NamePtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.NameXPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.NumberPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.OperationPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Ptg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Ref3DPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.RefErrorPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.RefPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.StringPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.UnionPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.UnknownPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.udf.AggregatingUDFFinder;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.udf.UDFFinder;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFEvaluationWorkbook;
import com.nvqquy98.lib.doc.office.fc.ss.usermodel.ICell;
import com.nvqquy98.lib.doc.office.fc.ss.util.CellReference;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ACell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;



/**
 * Evaluates formula cells.<p/>
 *
 * For performance reasons, this class keeps a cache of all previously calculated intermediate
 * cell values.  Be sure to call {@link #clearAllCachedResultValues()} if any workbook cells are changed between
 * calls to evaluate~ methods on this class.<br/>
 *
 * For POI internal use only
 *
 * @author Josh Micich
 */
public final class WorkbookEvaluator {

	private final EvaluationWorkbook _workbook;
	private EvaluationCache _cache;
	/** part of cache entry key (useful when evaluating multiple workbooks) */
	private int _workbookIx;

	private final IEvaluationListener _evaluationListener;
	private final Map<EvaluationSheet, Integer> _sheetIndexesBySheet;
	private final Map<String, Integer> _sheetIndexesByName;
	private CollaboratingWorkbooksEnvironment _collaboratingWorkbookEnvironment;
	private final IStabilityClassifier _stabilityClassifier;
	private final AggregatingUDFFinder _udfFinder;
	private EvaluationTracker tracker ;
	   
	/**
	 * @param udfFinder pass <code>null</code> for default (AnalysisToolPak only)
	 */
	public WorkbookEvaluator(EvaluationWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
		this (workbook, null, stabilityClassifier, udfFinder);
	}
	/* package */ WorkbookEvaluator(EvaluationWorkbook workbook, IEvaluationListener evaluationListener,
			IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
		_workbook = workbook;
		_evaluationListener = evaluationListener;
		_cache = new EvaluationCache(evaluationListener);
		_sheetIndexesBySheet = new IdentityHashMap<EvaluationSheet, Integer>();
		_sheetIndexesByName = new IdentityHashMap<String, Integer>();
		_collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
		_workbookIx = 0;
		_stabilityClassifier = stabilityClassifier;

        AggregatingUDFFinder defaultToolkit = // workbook can be null in unit tests
                workbook == null ? null : (AggregatingUDFFinder)workbook.getUDFFinder();
        if(defaultToolkit != null && udfFinder != null) {
            defaultToolkit.add(udfFinder);
        }
        _udfFinder = defaultToolkit;
	}

	/**
	 * also for debug use. Used in toString methods
	 */
	/* package */ String getSheetName(int sheetIndex) {
		return _workbook.getSheetName(sheetIndex);
	}

	/* package */ EvaluationSheet getSheet(int sheetIndex) 
	{
	    Iterator<EvaluationSheet> iter = _sheetIndexesBySheet.keySet().iterator();
	    while(iter.hasNext())
	    {
	        EvaluationSheet sheet = (EvaluationSheet)iter.next();
	        if(_sheetIndexesBySheet.get(sheet) == sheetIndex)
	        {
	            return sheet;
	        }
	    }
	    EvaluationSheet sheet = _workbook.getSheet(sheetIndex);
	    _sheetIndexesBySheet.put(sheet, sheetIndex);
		return sheet;
	}
	
	/* package */ EvaluationWorkbook getWorkbook() {
		return _workbook;
	}

	/* package */ EvaluationName getName(String name, int sheetIndex) {
        NamePtg namePtg = _workbook.getName(name, sheetIndex).createPtg();

		if(namePtg == null) {
			return null;
		} else {
			return _workbook.getName(namePtg);
		}
	}

	private static boolean isDebugLogEnabled() {
		return false;
	}
	private static void logDebug(String s) {
		if (isDebugLogEnabled()) {
			System.out.println(s);
		}
	}
	/* package */ void attachToEnvironment(CollaboratingWorkbooksEnvironment collaboratingWorkbooksEnvironment, EvaluationCache cache, int workbookIx) {
		_collaboratingWorkbookEnvironment = collaboratingWorkbooksEnvironment;
		_cache = cache;
		_workbookIx = workbookIx;
	}
	/* package */ CollaboratingWorkbooksEnvironment getEnvironment() {
		return _collaboratingWorkbookEnvironment;
	}

	/**
	 * Discards the current workbook environment and attaches to the default 'empty' environment.
	 * Also resets evaluation cache.
	 */
	/* package */ void detachFromEnvironment() {
		_collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
		_cache = new EvaluationCache(_evaluationListener);
		_workbookIx = 0;
	}
	/**
	 * @return the evaluator for another workbook which is part of the same {@link CollaboratingWorkbooksEnvironment}
	 */
	/* package */ WorkbookEvaluator getOtherWorkbookEvaluator(String workbookName) throws WorkbookNotFoundException {
		return _collaboratingWorkbookEnvironment.getWorkbookEvaluator(workbookName);
	}

	/* package */ IEvaluationListener getEvaluationListener() {
		return _evaluationListener;
	}

	/**
	 * Should be called whenever there are changes to input cells in the evaluated workbook.
	 * Failure to call this method after changing cell values will cause incorrect behaviour
	 * of the evaluate~ methods of this class
	 */
	public void clearAllCachedResultValues() {
		_cache.clear();
		_sheetIndexesBySheet.clear();
	}

	/**
	 * Should be called to tell the cell value cache that the specified (value or formula) cell
	 * has changed.
	 */
	public void notifyUpdateCell(EvaluationCell cell) {
		int sheetIndex = getSheetIndex(cell.getSheet());
		_cache.notifyUpdateCell(_workbookIx, sheetIndex, cell);
	}
	/**
	 * Should be called to tell the cell value cache that the specified cell has just been
	 * deleted.
	 */
	public void notifyDeleteCell(EvaluationCell cell) {
		int sheetIndex = getSheetIndex(cell.getSheet());
		_cache.notifyDeleteCell(_workbookIx, sheetIndex, cell);
	}
	
	private int getSheetIndex(EvaluationSheet sheet) {
		Integer result = _sheetIndexesBySheet.get(sheet);
		if (result == null) {
			int sheetIndex = _workbook.getSheetIndex(sheet);
			if (sheetIndex < 0) {
				throw new RuntimeException("Specified sheet from a different book");
			}
			result = Integer.valueOf(sheetIndex);
			_sheetIndexesBySheet.put(sheet, result);
		}
		return result.intValue();
	}

	
	public ValueEval evaluate(EvaluationCell srcCell) {
		int sheetIndex = getSheetIndex(srcCell.getSheet());
		if(tracker == null)
		{
		    tracker = new EvaluationTracker(_cache);
		}
		return evaluateAny(srcCell, sheetIndex, srcCell.getRowIndex(), srcCell.getColumnIndex(), tracker/*new EvaluationTracker(_cache)*/);
	}

	/**
	 * Case-insensitive.
	 * @return -1 if sheet with specified name does not exist
	 */
	/* package */ int getSheetIndex(String sheetName) {
		Integer result = _sheetIndexesByName.get(sheetName);
		if (result == null) {
			int sheetIndex = _workbook.getSheetIndex(sheetName);
			if (sheetIndex < 0) {
				return -1;
			}
			result = Integer.valueOf(sheetIndex);
			_sheetIndexesByName.put(sheetName, result);
		}
		return result.intValue();
	}
	
	/* package */ int getSheetIndexByExternIndex(int externSheetIndex) {
	   return _workbook.convertFromExternSheetIndex(externSheetIndex);
	}


	/**
	 * @return never <code>null</code>, never {@link BlankEval}
	 */
	private ValueEval evaluateAny(EvaluationCell srcCell, int sheetIndex,
				int rowIndex, int columnIndex, EvaluationTracker tracker)
	{

		// avoid tracking dependencies to cells that have constant definition
		boolean shouldCellDependencyBeRecorded = _stabilityClassifier == null ? true
					: !_stabilityClassifier.isCellFinal(sheetIndex, rowIndex, columnIndex);
		if (srcCell == null || srcCell.getCellType() != ICell.CELL_TYPE_FORMULA)
		{
			ValueEval result = getValueFromNonFormulaCell(srcCell);
			if (shouldCellDependencyBeRecorded)
			{
				tracker.acceptPlainValueDependency(_workbookIx, sheetIndex, rowIndex, columnIndex, result);
			}
			return result;
		}

		FormulaCellCacheEntry cce = _cache.getOrCreateFormulaCellEntry(srcCell);
		if (shouldCellDependencyBeRecorded || cce.isInputSensitive())
		{
			tracker.acceptFormulaDependency(cce);
		}
		IEvaluationListener evalListener = _evaluationListener;
		ValueEval result;
		if (cce.getValue() == null) 
		{
			if (!tracker.startEvaluate(cce)) 
			{
				return ErrorEval.CIRCULAR_REF_ERROR;
			}
			OperationEvaluationContext ec = new OperationEvaluationContext(this, _workbook, sheetIndex, rowIndex, columnIndex, tracker);

			try 
			{

				Ptg[] ptgs = _workbook.getFormulaTokens(srcCell);
				if (evalListener == null) 
				{
					result = evaluateFormula(ec, ptgs);
					if(result == null)
					{
					    return null;
					}
					
				} 
				else
				{
					evalListener.onStartEvaluate(srcCell, cce);
					result = evaluateFormula(ec, ptgs);
					evalListener.onEndEvaluate(cce, result);
				}

				tracker.updateCacheResult(result);
			} 
			catch (NotImplementedException e)
			{
				throw addExceptionInfo(e, sheetIndex, rowIndex, columnIndex);
			} 
			catch(Exception e)
            {
			    ACell cell = (ACell)srcCell.getIdentityKey();
			    cell.setCellType(Cell.CELL_TYPE_ERROR, false);
	            cell.setCellErrorValue((byte)ErrorEval.NA.getErrorCode());
	            return null;
            }
			finally 
			{
				tracker.endEvaluate(cce);
			}
		} 
		else 
		{
			if(evalListener != null) 
			{
				evalListener.onCacheHit(sheetIndex, rowIndex, columnIndex, cce.getValue());
			}
			return cce.getValue();
		}
		if (isDebugLogEnabled()) 
		{
			String sheetName = getSheetName(sheetIndex);
			CellReference cr = new CellReference(rowIndex, columnIndex);
			logDebug("Evaluated " + sheetName + "!" + cr.formatAsString() + " to " + result.toString());
		}
		// Usually (result === cce.getValue())
		// But sometimes: (result==ErrorEval.CIRCULAR_REF_ERROR, cce.getValue()==null)
		// When circular references are detected, the cache entry is only updated for
		// the top evaluation frame
		
		ACell cell = (ACell)srcCell.getIdentityKey();
		if (result instanceof NumberEval) 
        {
            NumberEval ne = (NumberEval) result;                
            cell.setCellType(Cell.CELL_TYPE_NUMERIC, false);
            cell.setCellValue(ne.getNumberValue());
        }
        else if (result instanceof BoolEval) 
        {
            BoolEval be = (BoolEval) result;                
            cell.setCellType(Cell.CELL_TYPE_BOOLEAN, false);
            cell.setCellValue(be.getBooleanValue());
        }
        else if (result instanceof StringEval) 
        {
            StringEval ne = (StringEval) result;                
            cell.setCellType(Cell.CELL_TYPE_STRING, false);
            cell.setCellValue(ne.getStringValue());
        }
        else if (result instanceof ErrorEval)
        {
            cell.setCellType(Cell.CELL_TYPE_ERROR, false);
            cell.setCellErrorValue((byte)((ErrorEval)result).getErrorCode());
            
        }
		return result;
	}

	/**
	 * Adds the current cell reference to the exception for easier debugging.
	 * Would be nice to get the formula text as well, but that seems to require
	 * too much digging around and casting to get the FormulaRenderingWorkbook.
	 */
	private NotImplementedException addExceptionInfo(NotImplementedException inner, int sheetIndex, int rowIndex, int columnIndex) {

		try {
			String sheetName = _workbook.getSheetName(sheetIndex);
			CellReference cr = new CellReference(sheetName, rowIndex, columnIndex, false, false);
			String msg =  "Error evaluating cell " + cr.formatAsString();
			return new NotImplementedException(msg, inner);
		} catch (Exception e) {
			// avoid bombing out during exception handling
			e.printStackTrace();
			return inner; // preserve original exception
		}
	}
	/**
	 * Gets the value from a non-formula cell.
	 * @param cell may be <code>null</code>
	 * @return {@link BlankEval} if cell is <code>null</code> or blank, never <code>null</code>
	 */
	/* package */ static ValueEval getValueFromNonFormulaCell(EvaluationCell cell) {
		if (cell == null) {
			return BlankEval.instance;
		}
		int cellType = cell.getCellType();
		switch (cellType) {
			case ICell.CELL_TYPE_NUMERIC:
				return new NumberEval(cell.getNumericCellValue());
			case ICell.CELL_TYPE_STRING:
				return new StringEval(cell.getStringCellValue());
			case ICell.CELL_TYPE_BOOLEAN:
				return BoolEval.valueOf(cell.getBooleanCellValue());
			case ICell.CELL_TYPE_BLANK:
				return BlankEval.instance;
			case ICell.CELL_TYPE_ERROR:
				return ErrorEval.valueOf(cell.getErrorCellValue());
		}
		throw new RuntimeException("Unexpected cell type (" + cellType + ")");
	}
	

	// visibility raised for testing
	/* package */ ValueEval evaluateFormula(OperationEvaluationContext ec, Ptg[] ptgs) {

	    List<ValueEval> stack = new ArrayList<ValueEval>();
	    
		for (int i = 0, iSize = ptgs.length; i < iSize; i++) {

			// since we don't know how to handle these yet :(
			Ptg ptg = ptgs[i];
			if (ptg instanceof AttrPtg) {
				AttrPtg attrPtg = (AttrPtg) ptg;
				if (attrPtg.isSum()) {
					// Excel prefers to encode 'SUM()' as a tAttr token, but this evaluator
					// expects the equivalent function token
					ptg = FuncVarPtg.SUM;
				}
				if (attrPtg.isOptimizedChoose()) {
					ValueEval arg0 = stack.remove(stack.size() - 1);
					int[] jumpTable = attrPtg.getJumpTable();
					int dist;
					int nChoices = jumpTable.length;
					try {
						int switchIndex = Choose.evaluateFirstArg(arg0, ec.getRowIndex(), ec.getColumnIndex());
						if (switchIndex<1 || switchIndex > nChoices) {
							stack.add(ErrorEval.VALUE_INVALID);
							dist = attrPtg.getChooseFuncOffset() + 4; // +4 for tFuncFar(CHOOSE)
						} else {
							dist = jumpTable[switchIndex-1];
						}
					} catch (EvaluationException e) {
						stack.add(e.getErrorEval());
						dist = attrPtg.getChooseFuncOffset() + 4; // +4 for tFuncFar(CHOOSE)
					}
					// Encoded dist for tAttrChoose includes size of jump table, but
					// countTokensToBeSkipped() does not (it counts whole tokens).
					dist -= nChoices*2+2; // subtract jump table size
					i+= countTokensToBeSkipped(ptgs, i, dist);
					continue;
				}
				if (attrPtg.isOptimizedIf()) {
					ValueEval arg0 = stack.remove(stack.size() - 1);
					boolean evaluatedPredicate;
					try {
						evaluatedPredicate = IfFunc.evaluateFirstArg(arg0, ec.getRowIndex(), ec.getColumnIndex());
					} catch (EvaluationException e) {
						stack.add(e.getErrorEval());
						int dist = attrPtg.getData();
						i+= countTokensToBeSkipped(ptgs, i, dist);
						attrPtg = (AttrPtg) ptgs[i];
						dist = attrPtg.getData()+1;
						i+= countTokensToBeSkipped(ptgs, i, dist);
						continue;
					}
					if (evaluatedPredicate) {
						// nothing to skip - true param folows
					} else {
						int dist = attrPtg.getData();
						i+= countTokensToBeSkipped(ptgs, i, dist);
						Ptg nextPtg = ptgs[i+1];
						if (ptgs[i] instanceof AttrPtg && nextPtg instanceof FuncVarPtg) {
							// this is an if statement without a false param (as opposed to MissingArgPtg as the false param)
							i++;
							stack.add(BoolEval.FALSE);
						}
					}
					continue;
				}
				if (attrPtg.isSkip()) {
					int dist = attrPtg.getData()+1;
					i+= countTokensToBeSkipped(ptgs, i, dist);
					if (stack.get(stack.size() - 1) == MissingArgEval.instance) {
						stack.remove(stack.size() - 1);
						stack.add(BlankEval.instance);
					}
					continue;
				}
			}
			if (ptg instanceof ControlPtg) {
				// skip Parentheses, Attr, etc
				continue;
			}
			if (ptg instanceof MemFuncPtg || ptg instanceof MemAreaPtg) {
				// can ignore, rest of tokens for this expression are in OK RPN order
				continue;
			}
			if (ptg instanceof MemErrPtg) {
				continue;
			}

			ValueEval opResult;
			if (ptg instanceof OperationPtg) {
				OperationPtg optg = (OperationPtg) ptg;

				if (optg instanceof UnionPtg) { continue; }


				int numops = optg.getNumberOfOperands();
				ValueEval[] ops = new ValueEval[numops];

				// storing the ops in reverse order since they are popping
				for (int j = numops - 1; j >= 0; j--) {
					ValueEval p = stack.remove(stack.size() - 1);;
					ops[j] = p;
				}
//				logDebug("invoke " + operation + " (nAgs=" + numops + ")");
				opResult = OperationEvaluatorFactory.evaluate(optg, ops, ec);
			} else {
				opResult = getEvalForPtg(ptg, ec);
			}
			if (opResult == null) {
			    return null;
				//throw new RuntimeException("Evaluation result must not be null");
			}
//			logDebug("push " + opResult);
			stack.add(opResult);
		}
		
		ValueEval value = stack.remove(stack.size() - 1);;
		if (!stack.isEmpty()) {
			throw new IllegalStateException("evaluation stack not empty");
		}
		
		if(value instanceof AreaEval || value instanceof RefEval)
		{
		    return value;
		}
		
		return dereferenceResult(value, ec.getRowIndex(), ec.getColumnIndex());
	}

	/**
	 * Calculates the number of tokens that the evaluator should skip upon reaching a tAttrSkip.
	 *
	 * @return the number of tokens (starting from <tt>startIndex+1</tt>) that need to be skipped
	 * to achieve the specified <tt>distInBytes</tt> skip distance.
	 */
	private static int countTokensToBeSkipped(Ptg[] ptgs, int startIndex, int distInBytes) {
		int remBytes = distInBytes;
		int index = startIndex;
		while (remBytes != 0) {
			index++;
			remBytes -= ptgs[index].getSize();
			if (remBytes < 0) {
				throw new RuntimeException("Bad skip distance (wrong token size calculation).");
			}
			if (index >= ptgs.length) {
				throw new RuntimeException("Skip distance too far (ran out of formula tokens).");
			}
		}
		return index-startIndex;
	}

	/**
	 * Dereferences a single value from any AreaEval or RefEval evaluation
	 * result. If the supplied evaluationResult is just a plain value, it is
	 * returned as-is.
	 *
	 * @return a {@link NumberEval}, {@link StringEval}, {@link BoolEval}, or
	 *         {@link ErrorEval}. Never <code>null</code>. {@link BlankEval} is
	 *         converted to {@link NumberEval#ZERO}
	 */
	public static ValueEval dereferenceResult(ValueEval evaluationResult, int srcRowNum, int srcColNum) {
		ValueEval value;
		try {
			value = OperandResolver.getSingleValue(evaluationResult, srcRowNum, srcColNum);
		} catch (EvaluationException e) {
			return e.getErrorEval();
		}
		if (value == BlankEval.instance) {
			// Note Excel behaviour here. A blank final final value is converted to zero.
			return NumberEval.ZERO;
			// Formulas _never_ evaluate to blank.  If a formula appears to have evaluated to
			// blank, the actual value is empty string. This can be verified with ISBLANK().
		}
		return value;
	}


	/**
	 * returns an appropriate Eval impl instance for the Ptg. The Ptg must be
	 * one of: Area3DPtg, AreaPtg, ReferencePtg, Ref3DPtg, IntPtg, NumberPtg,
	 * StringPtg, BoolPtg <br/>special Note: OperationPtg subtypes cannot be
	 * passed here!
	 */
	private ValueEval getEvalForPtg(Ptg ptg, OperationEvaluationContext ec) {
		//  consider converting all these (ptg instanceof XxxPtg) expressions to (ptg.getClass() == XxxPtg.class)

		if (ptg instanceof NamePtg) {
			// named ranges, macro functions
			NamePtg namePtg = (NamePtg) ptg;
			EvaluationName nameRecord = _workbook.getName(namePtg);
			if (nameRecord.isFunctionName()) {
				return new NameEval(nameRecord.getNameText());
			}
			if (nameRecord.hasFormula()) {
				return evaluateNameFormula(nameRecord.getNameDefinition(), ec);
			}

			throw new RuntimeException("Don't now how to evalate name '" + nameRecord.getNameText() + "'");
		}
		if (ptg instanceof NameXPtg) {
		    NameXPtg nameXPtg = (NameXPtg) ptg;
            EvaluationName nameRecord = ((HSSFEvaluationWorkbook)_workbook).getName(nameXPtg);
            if (nameRecord.isFunctionName()) {
                return new NameEval(nameRecord.getNameText());
            }
            if (nameRecord.hasFormula()) {
                return evaluateNameFormula(nameRecord.getNameDefinition(), ec);
            }

            throw new RuntimeException("Don't now how to evalate name '" + nameRecord.getNameText() + "'");
            
		   //return ec.getNameXEval(((NameXPtg) ptg));
		}

		if (ptg instanceof IntPtg) {
			return new NumberEval(((IntPtg)ptg).getValue());
		}
		if (ptg instanceof NumberPtg) {
			return new NumberEval(((NumberPtg)ptg).getValue());
		}
		if (ptg instanceof StringPtg) {
			return new StringEval(((StringPtg) ptg).getValue());
		}
		if (ptg instanceof BoolPtg) {
			return BoolEval.valueOf(((BoolPtg) ptg).getValue());
		}
		if (ptg instanceof ErrPtg) {
			return ErrorEval.valueOf(((ErrPtg) ptg).getErrorCode());
		}
		if (ptg instanceof MissingArgPtg) {
			return MissingArgEval.instance;
		}
		if (ptg instanceof AreaErrPtg ||ptg instanceof RefErrorPtg
				|| ptg instanceof DeletedArea3DPtg || ptg instanceof DeletedRef3DPtg) {
				return ErrorEval.REF_INVALID;
		}
		if (ptg instanceof Ref3DPtg) {
			Ref3DPtg rptg = (Ref3DPtg) ptg;
			return ec.getRef3DEval(rptg.getRow(), rptg.getColumn(), rptg.getExternSheetIndex());
		}
		if (ptg instanceof Area3DPtg) {
			Area3DPtg aptg = (Area3DPtg) ptg;
			return ec.getArea3DEval(aptg.getFirstRow(), aptg.getFirstColumn(), aptg.getLastRow(), aptg.getLastColumn(), aptg.getExternSheetIndex());
		}
		if (ptg instanceof RefPtg) {
			RefPtg rptg = (RefPtg) ptg;
			return ec.getRefEval(rptg.getRow(), rptg.getColumn());
		}
		if (ptg instanceof AreaPtg) {
			AreaPtg aptg = (AreaPtg) ptg;
			return ec.getAreaEval(aptg.getFirstRow(), aptg.getFirstColumn(), aptg.getLastRow(), aptg.getLastColumn());
		}

		if (ptg instanceof UnknownPtg) {
			// POI uses UnknownPtg when the encoded Ptg array seems to be corrupted.
			// This seems to occur in very rare cases (e.g. unused name formulas in bug 44774, attachment 21790)
			// In any case, formulas are re-parsed before execution, so UnknownPtg should not get here
			throw new RuntimeException("UnknownPtg not allowed");
		}
		if (ptg instanceof ExpPtg) {
			// ExpPtg is used for array formulas and shared formulas.
			// it is currently unsupported, and may not even get implemented here
			throw new RuntimeException("ExpPtg currently not supported");
		}

		throw new RuntimeException("Unexpected ptg class (" + ptg.getClass().getName() + ")");
	}
    /**
     * YK: Used by OperationEvaluationContext to resolve indirect names.
     */
	/*package*/ ValueEval evaluateNameFormula(Ptg[] ptgs, OperationEvaluationContext ec)
	{
//		if (ptgs.length > 1) {
//			throw new RuntimeException("Complex name formulas not supported yet");
//		}
//		return getEvalForPtg(ptgs[0], ec);
	    if(ptgs.length == 1)
	    {
	        return getEvalForPtg(ptgs[0], ec);
	    }
	    else
	    {
	        return evaluateFormula(ec, ptgs);
	    }
	    
	}

	/**
	 * Used by the lazy ref evals whenever they need to get the value of a contained cell.
	 */
	/* package */ ValueEval evaluateReference(EvaluationSheet sheet, int sheetIndex, int rowIndex,
			int columnIndex, EvaluationTracker tracker) {

		EvaluationCell cell = sheet.getCell(rowIndex, columnIndex);
		return evaluateAny(cell, sheetIndex, rowIndex, columnIndex, tracker);
	}
	public FreeRefFunction findUserDefinedFunction(String functionName) {
		return _udfFinder.findFunction(functionName);
	}
}
