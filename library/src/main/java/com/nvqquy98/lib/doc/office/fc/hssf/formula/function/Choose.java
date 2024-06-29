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

import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.BlankEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.EvaluationException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.MissingArgEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.OperandResolver;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;

/**
 * @author Josh Micich
 */
public final class Choose implements Function {

	public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
		if (args.length < 2) {
			return ErrorEval.VALUE_INVALID;
		}

		try {
			int ix = evaluateFirstArg(args[0], srcRowIndex, srcColumnIndex);
			if (ix < 1 || ix >= args.length) {
				return ErrorEval.VALUE_INVALID;
			}
			ValueEval result = OperandResolver.getSingleValue(args[ix], srcRowIndex, srcColumnIndex);
			if (result == MissingArgEval.instance) {
				return BlankEval.instance;
			}
			return result;
		} catch (EvaluationException e) {
			return e.getErrorEval();
		}
	}

	public static int evaluateFirstArg(ValueEval arg0, int srcRowIndex, int srcColumnIndex)
			throws EvaluationException {
		ValueEval ev = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
		return OperandResolver.coerceValueToInt(ev);
	}
}
