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

package com.nvqquy98.lib.doc.office.fc.hssf.formula.function;


import com.nvqquy98.lib.doc.office.fc.hssf.formula.TwoDEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.BoolEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.EvaluationException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.OperandResolver;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.RefEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;


/**
 * Here are the general rules concerning Boolean functions:
 * <ol>
 * <li> Blanks are ignored (not either true or false) </li>
 * <li> Strings are ignored if part of an area ref or cell ref, otherwise they must be 'true' or 'false'</li>
 * <li> Numbers: 0 is false. Any other number is TRUE </li>
 * <li> Areas: *all* cells in area are evaluated according to the above rules</li>
 * </ol>
 *
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 */
public abstract class BooleanFunction implements Function {

	public final ValueEval evaluate(ValueEval[] args, int srcRow, int srcCol) {
		if (args.length < 1) {
			return ErrorEval.VALUE_INVALID;
		}
		boolean boolResult;
		try {
			boolResult = calculate(args);
		} catch (EvaluationException e) {
			return e.getErrorEval();
		}
		return BoolEval.valueOf(boolResult);
	}

	private boolean calculate(ValueEval[] args) throws EvaluationException {

		boolean result = getInitialResultValue();
		boolean atleastOneNonBlank = false;

		/*
		 * Note: no short-circuit boolean loop exit because any ErrorEvals will override the result
		 */
		for (int i=0, iSize=args.length; i<iSize; i++) {
			ValueEval arg = args[i];
			if (arg instanceof TwoDEval) {
				TwoDEval ae = (TwoDEval) arg;
				int height = ae.getHeight();
				int width = ae.getWidth();
				for (int rrIx=0; rrIx<height; rrIx++) {
					for (int rcIx=0; rcIx<width; rcIx++) {
						ValueEval ve = ae.getValue(rrIx, rcIx);
						Boolean tempVe = OperandResolver.coerceValueToBoolean(ve, true);
						if (tempVe != null) {
							result = partialEvaluate(result, tempVe.booleanValue());
							atleastOneNonBlank = true;
						}
					}
				}
				continue;
			}
			Boolean tempVe;
			if (arg instanceof RefEval) {
				ValueEval ve = ((RefEval) arg).getInnerValueEval();
				tempVe = OperandResolver.coerceValueToBoolean(ve, true);
			} else {
				tempVe = OperandResolver.coerceValueToBoolean(arg, false);
			}


			if (tempVe != null) {
				result = partialEvaluate(result, tempVe.booleanValue());
				atleastOneNonBlank = true;
			}
		}

		if (!atleastOneNonBlank) {
			throw new EvaluationException(ErrorEval.VALUE_INVALID);
		}
		return result;
	}


	protected abstract boolean getInitialResultValue();
	protected abstract boolean partialEvaluate(boolean cumulativeResult, boolean currentValue);


	public static final Function AND = new BooleanFunction() {
		protected boolean getInitialResultValue() {
			return true;
		}
		protected boolean partialEvaluate(boolean cumulativeResult, boolean currentValue) {
			return cumulativeResult && currentValue;
		}
	};
	public static final Function OR = new BooleanFunction() {
		protected boolean getInitialResultValue() {
			return false;
		}
		protected boolean partialEvaluate(boolean cumulativeResult, boolean currentValue) {
			return cumulativeResult || currentValue;
		}
	};
	public static final Function FALSE = new Fixed0ArgFunction() {
		public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
			return BoolEval.FALSE;
		}
	};
	public static final Function TRUE = new Fixed0ArgFunction() {
		public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
			return BoolEval.TRUE;
		}
	};
	public static final Function NOT = new Fixed1ArgFunction() {
		public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
			boolean boolArgVal;
			try {
				ValueEval ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
				Boolean b = OperandResolver.coerceValueToBoolean(ve, false);
				boolArgVal = b == null ? false : b.booleanValue();
			} catch (EvaluationException e) {
				return e.getErrorEval();
			}

			return BoolEval.valueOf(!boolArgVal);
		}
	};
}
