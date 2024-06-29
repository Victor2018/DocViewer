/*
 * 文件名称:          CellView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:11:50
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.AbstractView;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;

import android.graphics.Canvas;

/**
 * table cell view
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
public class CellView extends AbstractView
{

    /**
     * 
     */
    public CellView(IElement elem)
    {
        this.elem = elem;
    }
    
    
    /**
     * 
     * @param canvas
     * @param x
     * @param y
     * @param zoom
     */
    public void draw(Canvas canvas, int originX, int originY, float zoom)
    {
//        if (isMergedCell() && !isFirstMergedCell())
//        {
//            return;
//        }
//        Paint paint = new Paint();
//        int rowHeight = getParentView().getHeight();
//        paint.setStyle(Style.STROKE);
//        int dX = (int)(x * zoom) + originX;
//        int dY = (int)(y * zoom) + originY;
//        int w = (int)(getLayoutSpan(WPViewConstant.X_AXIS) * zoom);
//        int h = (int)(Math.max(height, rowHeight) * zoom);
//        //
//        canvas.drawRect(dX, dY,  dX + w, dY + h, paint);
//        
//        if (background != -1)
//        {
//            int old =  paint.getColor();
//            paint.setColor(background);
//            paint.setStyle(Style.FILL);
//            canvas.drawRect(dX + 1, dY + 1,  dX + w, dY + h, paint);
//            
//            paint.setColor(old);
//        }
        
        super.draw(canvas, originX, originY, zoom);
    }
    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        IView view = getView(offset, WPViewConstant.LINE_VIEW, isBack);
        if (view != null)
        {
            view.modelToView(offset, rect, isBack);
        }
        rect.x += getX() + getLeftIndent();
        rect.y += getY() + getTopIndent();
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
                if (y >= view.getY() && y < view.getY() + view.getLayoutSpan(WPViewConstant.Y_AXIS))
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
    public short getType()
    {
        return WPViewConstant.TABLE_CELL_VIEW;
    }

    /**
     * @return Returns the isFirstMergedCell.
     */
    public boolean isFirstMergedCell()
    {
        return isFirstMergedCell;
    }


    /**
     * @param isFirstMergedCell The isFirstMergedCell to set.
     */
    public void setFirstMergedCell(boolean isFirstMergedCell)
    {
        this.isFirstMergedCell = isFirstMergedCell;
    }


    /**
     * @return Returns the isMergedCell.
     */
    public boolean isMergedCell()
    {
        return isMergedCell;
    }


    /**
     * @param isMergedCell The isMergedCell to set.
     */
    public void setMergedCell(boolean isMergedCell)
    {
        this.isMergedCell = isMergedCell;
    }

    /**
     * 
     * @return
     */
    public boolean isValidLastCell()
    {
        if (getNextView() == null)
        {
            return true;
        }
        CellView nextCell = ((CellView)getNextView());
        if (isMergedCell())
        {
            return nextCell.isValidLastCell();
        }
        if (nextCell.getStartOffset(null) == 0 && nextCell.getEndOffset(null) == 0)
        {
            return nextCell.isValidLastCell();
        }
        
        return false;
    }
    
    /**
     * @return Returns the index.
     */
    public short getColumn()
    {
        return column;
    }


    /**
     * @param index The index to set.
     */
    public void setColumn(short column)
    {
        this.column = column;
    }


    public void setBackground(int color)
    {
        this.background = color;
    }
    
    protected int getBackground()
    {
        return this.background;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
    }
    
    // first merge cell
    private boolean isFirstMergedCell;
    // merge cell 
    private boolean isMergedCell;
    // index in row
    private short column;
    //
    private int background = -1;
}
