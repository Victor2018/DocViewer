/*
 * 文件名称:          RowView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:09:35
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.AbstractView;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;

import android.graphics.Rect;

/**
 * table row view
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-9
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class RowView extends AbstractView
{

    /**
     * 
     * @param elem
     */
    public RowView(IElement elem)
    {
        this.elem = elem;
    }

    /**
     * 
     */
    public short getType()
    {
        return WPViewConstant.TABLE_ROW_VIEW;
    }
    
    /**
     * 视图是否在指定区相交
     * 
     * @param rect
     */
    public boolean intersection(Rect rect, int originX, int originY, float zoom)
    {
        return true;
    }
    
    /**
     * @return Returns the isExactlyHeight.
     */
    public boolean isExactlyHeight()
    {
        return isExactlyHeight;
    }

    /**
     * @param isExactlyHeight The isExactlyHeight to set.
     */
    public void setExactlyHeight(boolean isExactlyHeight)
    {
        this.isExactlyHeight = isExactlyHeight;
    }
    
    /**
     * get cell view for index this is row
     */
    public CellView getCellView(short index)
    {
        int t = 0;
        CellView cellView = (CellView)getChildView();
        while (cellView !=  null)
        {
            if (t == index)
            {
                break;
            }
            t++;
            cellView = (CellView)cellView.getNextView();
        }
        return cellView;
    }

    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        IView view = getView(offset, WPViewConstant.TABLE_CELL_VIEW, isBack);
        if (view != null)
        {
            view.modelToView(offset, rect, isBack);
        }
        rect.x += getX();
        rect.y += getY();
        return rect;        
    }    
    
    /**
     * @param x
     * @param y
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public long viewToModel(int x, int y, boolean isBack)
    {
        x -= getX();
        y -= getY();
        //IView view = getView(x, y, WPViewConstant.LINE_VIEW, isBack);
        IView view = getChildView();
        if (view != null && y > view.getY())
        {
            while (view != null)
            {
                if (y >= view.getY() && y < view.getY() + view.getLayoutSpan(WPViewConstant.Y_AXIS) 
                    && x >= view.getX() && x <= view.getX() + view.getLayoutSpan(WPViewConstant.X_AXIS))
                {
                    break;
                }
                view = view.getNextView();
            }
        }
        view = view == null ? getChildView() : view;
        if (view != null)
        {
            return view.viewToModel(x, y, isBack);
        }
        return -1;
    }
    
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
    }
    
    //
    private boolean isExactlyHeight;
    
}
