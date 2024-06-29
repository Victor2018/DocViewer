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

import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.EvaluationException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.NumberEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.OperandResolver;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;

/**
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 */
public abstract class AggregateFunction extends MultiOperandNumericFunction {

	private static final class LargeSmall extends Fixed2ArgFunction {
		private final boolean _isLarge;
		protected LargeSmall(boolean isLarge) {
			_isLarge = isLarge;
		}

		public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0,
				ValueEval arg1) {
			double dn;
			try {
				ValueEval ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
				dn = OperandResolver.coerceValueToDouble(ve1);
			} catch (EvaluationException e1) {
				// all errors in the second arg translate to #VALUE!
				return ErrorEval.VALUE_INVALID;
			}
			// weird Excel behaviour on second arg
			if (dn < 1.0) {
				// values between 0.0 and 1.0 result in #NUM!
				return ErrorEval.NUM_ERROR;
			}
			// all other values are rounded up to the next integer
			int k = (int) Math.ceil(dn);

			double result;
			try {
				double[] ds = ValueCollector.collectValues(arg0);
				if (k > ds.length) {
					return ErrorEval.NUM_ERROR;
				}
				result = _isLarge ? StatsLib.kthLargest(ds, k) : StatsLib.kthSmallest(ds, k);
				NumericFunction.checkValue(result);
			} catch (EvaluationException e) {
				return e.getErrorEval();
			}

			return new NumberEval(result);
		}
	}
	static final class ValueCollector extends MultiOperandNumericFunction {
		private static final ValueCollector instance = new ValueCollector();
		public ValueCollector() {
			super(false, false);
		}
		public static double[] collectValues(ValueEval...operands) throws EvaluationException {
			return instance.getNumberArray(operands);
		}
		protected double evaluate(double[] values) {
			throw new IllegalStateException("should not be called");
		}
	}

    protected AggregateFunction() {
        super(false, false);
    }

    /**
     * Create an instance to use in the {@link Subtotal} function.
     *
     * <p>
     *     If there are other subtotals within argument refs (or nested subtotals),
     *     these nested subtotals are ignored to avoid double counting.
     * </p>
     *
     * @param   func  the function to wrap
     * @return  wrapped instance. The actual math is delegated to the argument function.
     */
    /*package*/ static Function subtotalInstance(Function func) {
        final AggregateFunction arg = (AggregateFunction)func;
        return new AggregateFunction() {
            @Override
            protected double evaluate(double[] values) throws EvaluationException {
                return arg.evaluate(values);
            }

            /**
             *  ignore nested subtotals.
             */
            @Override
            public boolean isSubtotalCounted(){
                return false;
            }

        };
    }

    public static final Function AVEDEV = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return StatsLib.avedev(values);
		}
	};
	public static final Function AVERAGE = new AggregateFunction() {
		protected double evaluate(double[] values) throws EvaluationException {
			if (values.length < 1) {
				throw new EvaluationException(ErrorEval.DIV_ZERO);
			}
			return MathX.average(values);
		}
	};
	public static final Function DEVSQ = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return StatsLib.devsq(values);
		}
	};
	public static final Function LARGE = new LargeSmall(true);
	public static final Function MAX = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return values.length > 0 ? MathX.max(values) : 0;
		}
	};
	public static final Function MEDIAN = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return StatsLib.median(values);
		}
	};
	public static final Function MIN = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return values.length > 0 ? MathX.min(values) : 0;
		}
	};
	public static final Function PRODUCT = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return MathX.product(values);
		}
	};
	public static final Function SMALL = new LargeSmall(false);
	public static final Function STDEV = new AggregateFunction() {
		protected double evaluate(double[] values) throws EvaluationException {
			if (values.length < 1) {
				throw new EvaluationException(ErrorEval.DIV_ZERO);
			}
			return StatsLib.stdev(values);
		}
	};
	public static final Function SUM = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return MathX.sum(values);
		}
	};
	public static final Function SUMSQ = new AggregateFunction() {
		protected double evaluate(double[] values) {
			return MathX.sumsq(values);
		}
	};
    public static final Function VAR = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.var(values);
        }
    };
    public static final Function VARP = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.varp(values);
        }
    };
    
    public static final Function DB = new AggregateFunction() 
    {
        protected double evaluate(double[] values) throws EvaluationException 
        {
            checkParas(values);
            
            int length = values.length;
            
            if(length == 4)
            {
                return db(values[0], values[1], values[2], values[3], 12);
            }
            else if(length == 5)
            {
                return db(values[0], values[1], values[2], values[3], values[4]);
            }
            else
            {
                throw new EvaluationException(ErrorEval.NA);
            }            
        }
        
        /**
         * 
         * @param values
         * @throws EvaluationException
         */
        private void checkParas(double[] values) throws EvaluationException
        {
            int length = values.length;
            
            if(length == 4 || length == 5)
            {
                if(values[2] <= 0 || values[3] <= 0 || values[3] - values[2] > 1)
                {                    
                    throw new EvaluationException(ErrorEval.NA);
                }
                
                if(length == 5 && (values[4] > 12 ||values[4] <= 0))
                {
                    throw new EvaluationException(ErrorEval.NA);
                }
            }
            else
            {
                throw new EvaluationException(ErrorEval.NA);
            }     
        }
        
        /**
         * 
         * @param cost
         * @param salvage
         * @param life
         * @param period need to be integer numeric now, float numeric maybe later.
         * @param month
         * @return
         * @throws EvaluationException
         */
        private double db(double cost, double salvage, double life, double period, double month) throws EvaluationException 
        {
            if(Math.abs(period - (int)period) > 0.001)
            {
                throw new EvaluationException(ErrorEval.NA);
            }
            
            //rate = 1 - ((salvage / cost) ^ (1 / life))，保留 3 位小数
            double rate = 1 - Math.pow(salvage / cost, 1 / life);
            rate = Math.round((float)rate* 1000) / 1000.0;
            
            if(Math.abs(period - 1) < 0.001)
            {
                //cost * rate * month / 12
                return cost * rate * month / 12;
            }
            else
            {
                /**
                 * (cost - 前期折旧总值 ) * rate
                 */
                double d = cost * rate * month / 12;
                cost -= d;
                
                if(period <= life)
                {
                    int i = 2;                   
                    while(i <= period)
                    {
                        d = cost * rate;
                        cost -= d;
                        i++;
                    }
                    return d;
                }
                else if(period - life <= 1)
                {
                    //last
                    if(Math.abs(month - 12) < 0.001)
                    {
                        return 0;
                    }
                    else
                    {
                        int i = 2;                   
                        while(i <= life)
                        {
                            d = cost * rate;
                            cost -= d;
                            i++;
                        }
                        return (cost * rate * (12 - month)) / 12;
                    }
                }
            }
            
            throw new EvaluationException(ErrorEval.NA);
        }       
    };
    
    public static final Function DDB = new AggregateFunction() 
    {
        protected double evaluate(double[] values) throws EvaluationException 
        {
            checkParas(values);
            
            int length = values.length;
            
            if(length == 4)
            {
                return ddb(values[0], values[1], values[2], values[3], 2);
            }
            else if(length == 5)
            {
                return ddb(values[0], values[1], values[2], values[3], values[4]);
            }
            else
            {
                throw new EvaluationException(ErrorEval.NA);
            }            
        }
        
        /**
         * 
         * @param values
         * @throws EvaluationException
         */
        private void checkParas(double[] values) throws EvaluationException
        {
            int length = values.length;
            
            if(length == 4 || length == 5)
            {
                if(values[2] <= 0 || values[3] <= 0 || values[3] > values[2])
                {                    
                    throw new EvaluationException(ErrorEval.NA);
                }
                
            }
            else
            {
                throw new EvaluationException(ErrorEval.NA);
            }     
        }
        
        /**
         * 
         * @param cost
         * @param salvage
         * @param life
         * @param period need to be integer numeric now, float numeric maybe later.
         * @param month
         * @return
         * @throws EvaluationException
         */
        private double ddb(double cost, double salvage, double life, double period, double factor) throws EvaluationException 
        {
            if(Math.abs(period - (int)period) > 0.001)
            {
                throw new EvaluationException(ErrorEval.NA);
            }
            
           /**
            * Min( (cost - total depreciation from prior periods) * (factor/life), 
            * (cost - salvage - total depreciation from prior periods) )
            */
            double rate = factor / life;
            rate = Math.round((float)rate* 1000) / 1000.0;
            
            int i = 2;
            double d = Math.min(cost * rate, cost - salvage);;
            while(i <= period)
            {
                d = Math.min(cost * rate, cost - salvage);
                cost -= d;
                i++;
            }
            return d;
        }       
    };
}
