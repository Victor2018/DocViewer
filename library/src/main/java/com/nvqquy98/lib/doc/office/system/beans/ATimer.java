/*
 * 文件名称:          ATimer.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:30:06
 */
package com.nvqquy98.lib.doc.office.system.beans;

import java.util.Timer;
import java.util.TimerTask;

import com.nvqquy98.lib.doc.office.system.ITimerListener;

/**
 * 定时器
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-12
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ATimer
{
    /**
     * 
     * @param delay
     * @param listener 时间监听器
     */
    public ATimer(int delay, ITimerListener listener)
    {
        super();
        this.delay = delay;
        this.listener = listener;
    }
    
    /**
     * Starts the <code>Timer</code>,
     * causing it to start sending action events
     * to its listeners.
     *
     * @see #stop
     */
    public void start() 
    {
        if (isRunning)
        {
            return;
        }
        timer = new Timer();
        timer.schedule(new ATimerTask(), delay);
        isRunning = true;
    }


    /**
     * Returns <code>true</code> if the <code>Timer</code> is running.
     *
     * @see #start
     */
    public boolean isRunning()
    {        
        return this.isRunning;
    }

    /**
     * Stops the <code>Timer</code>,
     * causing it to stop sending action events
     * to its listeners.
     *
     * @see #start
     */
    public void stop()
    {
        if (isRunning)
        {
            timer.cancel();
            timer.purge();
            isRunning = false;
        }
    }


    /**
     * Restarts the <code>Timer</code>,
     * canceling any pending firings and causing
     * it to fire with its initial delay.
     */
    public void restart() 
    {   
        stop();
        start();
    }
    
    class ATimerTask extends TimerTask
    {
        public ATimerTask()
        {   
        }
        
        public void run()
        {
            try
            {
                timer.schedule(new ATimerTask(), delay);
                listener.actionPerformed();
            }
            catch (Exception e)
            {
            }
        }
        
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (isRunning)
        {
            timer.cancel();
            timer.purge();
            isRunning = false;
        }
        timer = null;
        listener = null;
    }
    // 定时器是否处于运行状态
    private boolean isRunning;
    // 间隔
    private int delay;
    // 
    private Timer timer;
    //
    private ITimerListener listener;
}
