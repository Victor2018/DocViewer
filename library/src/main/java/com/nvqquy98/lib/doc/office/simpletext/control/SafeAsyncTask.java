/*
 * 文件名称:          SafeAsyncTask.java
 *  
 * 编译器:            android2.2
 * 时间:              下午9:36:57
 */
package com.nvqquy98.lib.doc.office.simpletext.control;

import android.os.AsyncTask;
import java.util.concurrent.RejectedExecutionException;

/**
 * PDF document rending
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public abstract class SafeAsyncTask<Params, Progress, Result> extends
    AsyncTask<Params, Progress, Result>
{
    /**
     * 
     * @param params
     */
    public void safeExecute(Params...params)
    {
        try
        {
            execute(params);
        }
        catch(RejectedExecutionException e)
        {
            //MainControl.sysKit.getErrorKit().writerLog(e);
            onPreExecute();
            if (isCancelled())
            {
                onCancelled();
            }
            else
            {
                onPostExecute(doInBackground(params));
            }
        }
    }
}
