/*
 * 文件名称:          LayoutThread.java
 *  
 * 编译器:            android2.2
 * 时间:              下午9:23:20
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.simpletext.view.IRoot;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;

/**
 * 后台布局线程
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-17
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class LayoutThread extends Thread
{
    private boolean isDied;
    /**
     * 
     * @param root
     */
    public LayoutThread(IRoot root)
    {
        this.root = root;
    }
    
    /**
     * 
     */
    public void run()
    {
        while (true)
        {
            if (isDied)
            {
                break;
            }
            try
            {
                if (root.canBackLayout())
                {
                    root.backLayout();
                    sleep(50);
                    continue;
                }
                else
                {
                    sleep(1000);
                }
            }
            catch (Exception e)
            {
                if (root != null)
                {
                    IWord word = ((IView)root).getContainer();
                    if (word != null && word.getControl() != null)
                    {
                        word.getControl().getSysKit().getErrorKit().writerLog(e);
                    }
                }
                break;
            }
        }
    }
    
    /**
     * 
     */
    public void setDied()
    {
        isDied = true;
    }
    
    /**
     * 
     */
    public void dispose()
    {       
        root = null;
        isDied = true;
    }
    
    private IRoot root;
}
