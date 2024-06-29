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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ConcatEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.FunctionEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.IntersectionEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.PercentEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.RangeEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.RelationalOperationEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.TwoOperandNumericOperation;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.UnaryMinusEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.UnaryPlusEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.Function;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.FunctionMetadataRegistry;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.function.Indirect;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.AbstractFunctionPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.AddPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.ConcatPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.DividePtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.EqualPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.GreaterEqualPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.GreaterThanPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.IntersectionPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.LessEqualPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.LessThanPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.MultiplyPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.NotEqualPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.OperationPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.PercentPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.PowerPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.RangePtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.SubtractPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.UnaryMinusPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.UnaryPlusPtg;



/**
 * This class creates <tt>OperationEval</tt> instances to help evaluate <tt>OperationPtg</tt>
 * formula tokens.
 *
 * @author Josh Micich
 */
final class OperationEvaluatorFactory {

	private static final Map<OperationPtg, Function> _instancesByPtgClass = initialiseInstancesMap();

	private OperationEvaluatorFactory() {
		// no instances of this class
	}

	private static Map<OperationPtg, Function> initialiseInstancesMap() {
		Map<OperationPtg, Function> m = new HashMap<OperationPtg, Function>(32);

		put(m, EqualPtg.instance, RelationalOperationEval.EqualEval);
		put(m, GreaterEqualPtg.instance, RelationalOperationEval.GreaterEqualEval);
		put(m, GreaterThanPtg.instance, RelationalOperationEval.GreaterThanEval);
		put(m, LessEqualPtg.instance, RelationalOperationEval.LessEqualEval);
		put(m, LessThanPtg.instance, RelationalOperationEval.LessThanEval);
		put(m, NotEqualPtg.instance, RelationalOperationEval.NotEqualEval);

		put(m, ConcatPtg.instance, ConcatEval.instance);
		put(m, AddPtg.instance, TwoOperandNumericOperation.AddEval);
		put(m, DividePtg.instance, TwoOperandNumericOperation.DivideEval);
		put(m, MultiplyPtg.instance, TwoOperandNumericOperation.MultiplyEval);
		put(m, PercentPtg.instance, PercentEval.instance);
		put(m, PowerPtg.instance, TwoOperandNumericOperation.PowerEval);
		put(m, SubtractPtg.instance, TwoOperandNumericOperation.SubtractEval);
		put(m, UnaryMinusPtg.instance, UnaryMinusEval.instance);
		put(m, UnaryPlusPtg.instance, UnaryPlusEval.instance);
		put(m, RangePtg.instance, RangeEval.instance);
		put(m, IntersectionPtg.instance, IntersectionEval.instance);
		return m;
	}

	private static void put(Map<OperationPtg, Function> m, OperationPtg ptgKey,
			Function instance) {
		// make sure ptg has single private constructor because map lookups assume singleton keys
		Constructor[] cc = ptgKey.getClass().getDeclaredConstructors();
		if (cc.length > 1 || !Modifier.isPrivate(cc[0].getModifiers())) {
			throw new RuntimeException("Failed to verify instance ("
					+ ptgKey.getClass().getName() + ") is a singleton.");
		}
		m.put(ptgKey, instance);
	}

	/**
	 * returns the OperationEval concrete impl instance corresponding
	 * to the supplied operationPtg
	 */
	public static ValueEval evaluate(OperationPtg ptg, ValueEval[] args,
			OperationEvaluationContext ec) {
		if(ptg == null) {
			throw new IllegalArgumentException("ptg must not be null");
		}
		Function result = _instancesByPtgClass.get(ptg);

		if (result != null) {
			return  result.evaluate(args, ec.getRowIndex(), (short) ec.getColumnIndex());
		}

		if (ptg instanceof AbstractFunctionPtg) {
			AbstractFunctionPtg fptg = (AbstractFunctionPtg)ptg;
			int functionIndex = fptg.getFunctionIndex();
			switch (functionIndex) {
				case FunctionMetadataRegistry.FUNCTION_INDEX_INDIRECT:
					return Indirect.instance.evaluate(args, ec);
				case FunctionMetadataRegistry.FUNCTION_INDEX_EXTERNAL:
					return UserDefinedFunction.instance.evaluate(args, ec);
			}

			return FunctionEval.getBasicFunction(functionIndex).evaluate(args, ec.getRowIndex(), (short) ec.getColumnIndex());
		}
		throw new RuntimeException("Unexpected operation ptg class (" + ptg.getClass().getName() + ")");
	}
}
