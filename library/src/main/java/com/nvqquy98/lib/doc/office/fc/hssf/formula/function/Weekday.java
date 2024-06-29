/*
 * 文件名称:          Weekday.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:04:51
 */
package com.nvqquy98.lib.doc.office.fc.hssf.formula.function;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ErrorEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.NumberEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;
import com.nvqquy98.lib.doc.office.fc.ss.usermodel.ErrorConstants;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-6-7
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class Weekday extends Var1or2ArgFunction
{
    private static final ValueEval DEFAULT_ARG1 = new NumberEval(1.0);
    
    public Weekday() 
    {
       
    }
    
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) 
    {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, DEFAULT_ARG1);
    }
    
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) 
    {
        ValueEval eval = ((CalendarFieldFunction)CalendarFieldFunction.WEEKDAY).evaluate(srcRowIndex, srcColumnIndex, arg0);
        if(arg1 instanceof NumberEval)
        {
            NumberEval numEval = (NumberEval)arg1;
            int w = (int)Math.round(((NumberEval)eval).getNumberValue());
            
            switch((int)Math.round(numEval.getNumberValue()))
            {
                case 1:
                    break;
                case 2:
                    w = (w - 1);
                    w = (w == 0 ? 7 : w);
                    eval = new NumberEval(w);
                    break;
                case 3:
                    w = (w - 2);
                    w = (w >= 0 ? w : 6);
                    eval = new NumberEval(w);
                    break;
                default:
                    eval =  ErrorEval.valueOf(ErrorConstants.ERROR_NUM);                        
            }            
        }
        return eval;
    }
}
