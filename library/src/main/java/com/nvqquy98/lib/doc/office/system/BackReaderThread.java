/*
 * 文件名称:          BackReaderThread.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:57:21
 */
package com.nvqquy98.lib.doc.office.system;

import com.nvqquy98.lib.doc.office.constant.EventConstant;

/**
 * 后台读取数据线程
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-4-20
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class BackReaderThread extends Thread
{

    /**
     * 
     * @param reader
     */
    public BackReaderThread(IReader reader, IControl control)
    {
        super();
        this.reader = reader;
        this.control = control;
    }
    
    /**
     * 
     *
     */
    public void run()
    {
        control.actionEvent(EventConstant.SYS_START_BACK_READER_ID, true);
        while (true)
        {
            if (die)
            {
                break;
            }
            try
            {
                if (!reader.isReaderFinish())
                {
                    reader.backReader();                    
                    sleep(50);
                }
                else
                {
                    control.actionEvent(EventConstant.SYS_READER_FINSH_ID, true);
                    control = null;
                    reader = null;
                    break;
                }
            }
            catch(OutOfMemoryError e)
            {
                control.getSysKit().getErrorKit().writerLog(e, true);
                control.actionEvent(EventConstant.SYS_READER_FINSH_ID, true);
                control = null;
                reader = null;

                break;
            }
            catch(Exception e)
            {
                if(!reader.isAborted())
                {
                    control.getSysKit().getErrorKit().writerLog(e, true);
                    control.actionEvent(EventConstant.SYS_READER_FINSH_ID, true);
                    control = null;
                    reader = null;
                }
                break;
            }
        }
    }
    
    /**
     * 
     */
    public void setDie(boolean die)
    {
        this.die = die;
    }
    //
    private boolean die;
    //
    private IReader reader;
    //
    private IControl control;
}
