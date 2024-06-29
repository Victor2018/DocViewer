/*
 * 文件名称:          TableView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:47:18
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;

/**
 * 表格视图
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
public class TableView extends ParagraphView
{
    /**
     * 
     * @param elem
     */
    public TableView(IElement elem)
    {
        super(elem);
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
        //int dX = (int)(x * zoom) + originX;
        //int dY = (int)(y * zoom) + originY;
        float tX = (x * zoom) + originX;
        float tY = (y * zoom) + originY; 
        RowView row  = (RowView)getChildView();
        Rect clip = canvas.getClipBounds();
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        while (row != null)
        {
            float rX = 0;
            float rY = 0;
            boolean isFirstRow = true;
            float rowHeight = 0;
            if (row.intersection(clip, (int)tX, (int)tY, zoom))
            {
                rX = tX + row.getX() * zoom;
                if (isFirstRow)
                {
                    rY = tY + row.getY() * zoom;
                    isFirstRow = false;
                }
                else 
                {
                    rY += rowHeight;
                }
                rowHeight = row.getHeight() * zoom;
                CellView cell = (CellView)row.getChildView();
                float cX = 0;
                float cY = 0;
                float cW = 0;
                float cH = 0;
                float cRight = 0;
                boolean isFirstCell = true;
                while (cell != null)
                {
                    if (cell.intersection(clip, (int)rX, (int)rY, zoom))
                    {                        
                        if (cell.isMergedCell() && !cell.isFirstMergedCell())
                        {
                            cell = (CellView)cell.getNextView();
                            isFirstCell = true;
                            continue;
                        }                         
                        cY = rY + cell.getY() * zoom;
                        if (isFirstCell)
                        {
                           cX = rX + cell.getX() * zoom;
                           isFirstCell = false;
                        }
                        else
                        {
                            cX += cW;                          
                        }
                        cW = cell.getLayoutSpan(WPViewConstant.X_AXIS) * zoom;
                        cH = Math.max(cell.getHeight()* zoom, rowHeight);
                        cRight = cX + cW;
                        // 最后一个单元格
                        if (cell.isValidLastCell())
                        {
                            if (Math.abs(cRight - (tX + getWidth() * zoom)) <= 10)
                            {
                                cRight = tX + getWidth() * zoom;
                            }
                        }
                        // background
                        if (cell.getBackground() != -1)
                        {
                              int old =  paint.getColor();
                              paint.setColor(cell.getBackground());
                              paint.setStyle(Style.FILL);
                              canvas.drawRect(cX, cY,  cRight, cY + cH, paint);                              
                              paint.setColor(old);
                         }
                        //
                        paint.setStyle(Style.STROKE);
                        canvas.drawRect(cX, cY, cRight, cY + cH, paint); 
                        
                        canvas.save();
                        canvas.clipRect(cX, cY, cRight, cY + cH);
                        cell.draw(canvas, (int)rX, (int)rY, zoom);
                        canvas.restore();
                    }
                    cell = (CellView)cell.getNextView();
                }
            }
            row = (RowView)row.getNextView();
        }
    }
    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        IView view = getView(offset, WPViewConstant.TABLE_ROW_VIEW, isBack);
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
        return WPViewConstant.TABLE_VIEW;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
    }
    
    /**
     * @return Returns the isBreakPages.
     */
    public boolean isBreakPages()
    {
        return isBreakPages;
    }

    /**
     * @param isBreakPages The isBreakPages to set.
     */
    public void setBreakPages(boolean isBreakPages)
    {
        this.isBreakPages = isBreakPages;
    }

    //
    private boolean isBreakPages;

}
