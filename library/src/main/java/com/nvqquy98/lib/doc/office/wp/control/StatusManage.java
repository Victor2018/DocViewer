/*
 * 文件名称:          WPStatus.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:48:58
 */
package com.nvqquy98.lib.doc.office.wp.control;

/**
 * word application status
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-28
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class StatusManage
{

    /**
     * @return Returns the selectText.
     */
    public boolean isSelectTextStatus()
    {
        return selectText;
    }

    /**
     * @param selectText The selectText to set.
     */
    public void setSelectTextStatus(boolean selectText)
    {
        this.selectText = selectText;
    }
    
    /**
     * @return Returns the pressOffset.
     */
    public long getPressOffset()
    {
        return pressOffset;
    }

    /**
     * @param pressOffset The pressOffset to set.
     */
    public void setPressOffset(long pressOffset)
    {
        this.pressOffset = pressOffset;
    }

    /**
     * @return Returns the isTouchDown.
     */
    public boolean isTouchDown()
    {
        return isTouchDown;
    }

    /**
     * @param isTouchDown The isTouchDown to set.
     */
    public void setTouchDown(boolean isTouchDown)
    {
        this.isTouchDown = isTouchDown;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }

    // select text status
    private boolean selectText;
    // touch down offset 
    private long pressOffset;
    // touch down status
    private boolean isTouchDown;

}
