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

package com.nvqquy98.lib.doc.office.fc.hssf.formula.atp;


import com.nvqquy98.lib.doc.office.fc.hssf.formula.OperationEvaluationContext;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.BoolEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.EvaluationException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.OperandResolver;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.FreeRefFunction;

/**
 * Implementation of Excel 'Analysis ToolPak' function ISEVEN() ISODD()<br/>
 *
 * @author Josh Micich
 */
final class ParityFunction implements FreeRefFunction {

	public static final FreeRefFunction IS_EVEN = new ParityFunction(0);
	public static final FreeRefFunction IS_ODD = new ParityFunction(1);
	private final int _desiredParity;

	private ParityFunction(int desiredParity) {
		_desiredParity = desiredParity;
	}

	public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
		if (args.length != 1) {
			return ErrorEval.VALUE_INVALID;
		}

		int val;
		try {
			val = evaluateArgParity(args[0], ec.getRowIndex(), ec.getColumnIndex());
		} catch (EvaluationException e) {
			return e.getErrorEval();
		}

		return BoolEval.valueOf(val == _desiredParity);
	}

	private static int evaluateArgParity(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
		ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, (short)srcCellCol);

		double d = OperandResolver.coerceValueToDouble(ve);
		if (d < 0) {
			d = -d;
		}
		long v = (long) Math.floor(d);
		return (int) (v & 0x0001);
	}
}
