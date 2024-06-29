/*
 * 文件名称:          IHighlight.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:57:12
 */

package com.nvqquy98.lib.doc.office.simpletext.control;

import com.nvqquy98.lib.doc.office.simpletext.view.IView;

import android.graphics.Canvas;

/**
 * highlight
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-7-27
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IHighlight
{

    /**
     * draw highlight
     */
    public void draw(Canvas canvas, IView line, int originX, int originY, long start, long end, float zoom);

    /**
     * 
     */
    public String getSelectText();

    /**
     * 
     */
    public boolean isSelectText();

    /**
     * remove all selection
     */
    public void removeHighlight();

    /**
     * 
     */
    public void addHighlight(long start, long end);

    /**
     * @return Returns the selectStart.
     */
    public long getSelectStart();

    /**
     * @param selectStart The selectStart to set.
     */
    public void setSelectStart(long selectStart);

    /**
     * @return Returns the selectEnd.
     */
    public long getSelectEnd();

    /**
     * @param selectEnd The selectEnd to set.
     */
    public void setSelectEnd(long selectEnd);
    
    /**
     * @param isPaintHighlight The isPaintHighlight to set.
     */
    public void setPaintHighlight(boolean isPaintHighlight);

    /**
     * 
     */
    public void dispose();

}
