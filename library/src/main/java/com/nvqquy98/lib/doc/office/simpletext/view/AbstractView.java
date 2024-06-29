/*
 * 文件名称:          AbstractView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:36:32
 */
package com.nvqquy98.lib.doc.office.simpletext.view;

import java.util.List;

import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 视图实现的抽象类
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-11
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public abstract class AbstractView implements IView
{
    /**
     * 
     */
    public IElement getElement()
    {
        return this.elem;
    }
    
    /**
     * 
     */
    public void setElement(IElement elem)
    {
        this.elem = elem;
    }
    
    /**
     * 视图类型，页、栏、段落、行、leaf等
     */
    public short getType()
    {
        return -1;
    }
    
    /**
     * @return Returns the x.
     */
    public int getX()
    {
        return x;
    }
    /**
     * @param x The x to set.
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * @return Returns the y.
     */
    public int getY()
    {
        return y;
    }

    /**
     * @param y The y to set.
     */
    public void setY(int y)
    {
        this.y = y;
    }

    /**
     * @return Returns the width.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * @param width The width to set.
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * @return Returns the height.
     */
    public int getHeight()
    {
    	return height;
    }

    /**
     * @param height The height to set.
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * @return Returns the start.
     */
    public long getStartOffset(IDocument doc)
    {
        return start;
    }

    /**
     * @param start The start to set.
     */
    public void setStartOffset(long start)
    {
        this.start = start;
    }
    
    /**
     * 
     */
    public long getElemStart(IDocument doc)
    {
        return elem.getStartOffset();
    }

    /**
     * @return Returns the end.
     */
    public long getEndOffset(IDocument doc)
    {
        return end;
    }
    
    /**
     * 
     */
    public long getElemEnd(IDocument doc)
    {
        return elem.getEndOffset();
    }
    

    /**
     * @return Returns the parent.
     */
    public IView getParentView()
    {
        return parent;
    }

    /**
     * @param parent The parent to set.
     */
    public void setParentView(IView parent)
    {
        this.parent = parent;
    }
    
    

    /**
     * @return Returns the child.
     */
    public IView getChildView()
    {
        return child;
    }

    /**
     * @param child The child to set.
     */
    public void setChildView(IView child)
    {
        this.child = child;
    }



    /**
     * @return Returns the preView.
     */
    public IView getPreView()
    {
        return preView;
    }

    /**
     * @param preView The preView to set.
     */
    public void setPreView(IView preView)
    {
        this.preView = preView;
    }



    /**
     * @return Returns the nextView.
     */
    public IView getNextView()
    {
        return nextView;
    }

    /**
     * @param nextView The nextView to set.
     */
    public void setNextView(IView nextView)
    {
        this.nextView = nextView;
    }

    /**
     * 设置大小
     */
    public void setSize(int w, int h)
    {
        this.width = w;
        this.height = h;
    }

    /**
     * 设置位置
     */
    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * 设置范围
     */
    public void setBound(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = y;
        
    }

    /**
     * 得到组件
     */
    public IWord getContainer()
    {
        IView parent = getParentView();
        if (parent != null)
        {
            return parent.getContainer();
        }
        return null;
    }
    
    /**
     * 得到组件
     */
    public IControl getControl()
    {
        IView parent = getParentView();
        if (parent != null)
        {
            return parent.getControl();
        }
        return null;
    }

    /**
     * 得到model
     */
    public IDocument getDocument()
    {
        IView parent = getParentView();
        if (parent != null)
        {
            return parent.getDocument();
        }
        return null;
    }

    /**
     * 得到最后一个子视图，此方法尽量少用，非常耗时
     */
    public IView getLastView()
    {
        IView temp = getChildView();
        if (temp == null)
        {
            return null;
        }
        while (temp.getNextView() != null)
        {
            temp = temp.getNextView();
        }
        return temp;
    }

    /**
     * 得到子视图数量，此方法尽量少调用，非常耗时
     */
    public int getChildCount()
    {
        int count = 0;
        IView temp = getChildView();
        while (temp != null)
        {
            count++;
            temp = temp.getNextView();
        }
        return count;
    }

    /**
     * 追加一个子视图
     */
    public void appendChlidView(IView view)
    {
        view.setParentView(this);
        if (child == null)
        {
            child = view;
            return;
        }
        IView lastView = getLastView();
        view.setPreView(lastView);
        lastView.setNextView(view);
    }

    /**
     * 指定视图后面插入一个子视图
     * 
     * @param view 指定视图，如果 = null, 则把newView设置成第一个子视图
     * @param newView 要插入的视图
     */
    public void insertView(IView view, IView newView)
    {
        newView.setParentView(this);
        // newView设置第一个子图
        if (view == null)
        {
            if (child == null)
            {
                child = newView;
            }
            else
            {
                newView.setNextView(child);
                child.setPreView(newView);
                child = newView;
            }
        }
        
    }

    /**
     * 删除一个指定视图
     * 
     * @param idDeleteChlid = ture，则连子视图也删除。
     */
    public void deleteView(IView view, boolean isDeleteChild)
    {
        view.setParentView(null);
        if (view == child)
        {
            child = null;
        }
        else
        {
            IView pre = view.getPreView();
            IView next = view.getNextView();
            pre.setNextView(next);
            if (next != null)
            {
                next.setPreView(pre);
            }
        }
        if (isDeleteChild)
        {
            view.dispose();
        }
    }

    /**
     * 视图结束位置
     * @param end 相对于model的element开始位置的
     */
    public void setEndOffset(long end)
    {
        this.end = end;
        
    }

    /**
     * get view rect
     * @param originX
     * @param originY
     * @param zoom
     * @return
     */
    public Rect getViewRect(int originX, int originY, float zoom)
    {
        int tw = (int)(getLayoutSpan(WPViewConstant.X_AXIS) * zoom);
        int th = (int)(getLayoutSpan(WPViewConstant.Y_AXIS)  * zoom);
        
        int tx = (int)(x * zoom) + originX;
        int ty = (int)(y * zoom) + originY;
        return new Rect(tx, ty, tx + tw, ty + th);
    }
    
    /**
     * 视图是否在指定区相交
     * 
     * @param rect
     */
    public boolean intersection(Rect rect, int originX, int originY, float zoom)
    {
        int tw = (int)(getLayoutSpan(WPViewConstant.X_AXIS) * zoom);
        int th = (int)(getLayoutSpan(WPViewConstant.Y_AXIS)  * zoom);
        int rw = rect.right - rect.left;
        int rh = rect.bottom - rect.top;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) 
        {
            return false;
        }
        int tx = (int)(x * zoom) + originX;
        int ty = (int)(y * zoom) + originY;
        int rx = rect.left;
        int ry = rect.top;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx)
                && (rh < ry || rh > ty)
                && (tw < tx || tw > rx)
                && (th < ty || th > ry));
    }

    /**
     * 视图是否包含指定 offset
     * 
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public boolean contains(long offset, boolean isBack)
    {
        IDocument doc = getDocument();
        long start = getStartOffset(doc);
        long end = getEndOffset(doc);
        return offset >= start && (offset < end || (offset == end  && isBack));
    }
    
    /**
     * 视图是否包含指定 x，y值
     * @param x
     * @param y
     * @param isBack
     * @return
     */
    public boolean contains(int x, int y, boolean isBack)
    {
        return x >= this.x && x < this.x + this.width
                && y >= this.y && y < this.y + getHeight();
    }

    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        return null;
    }

    /**
     * @param x
     * @param y
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public long viewToModel(int x, int y, boolean isBack)
    {
        return 0;
    }

    /**
     * 得到下一个Offset位置，根据坐标
     * 
     * @param offset 当前Offset
     * @param dir 方向，上、下、左、右
     * @param x
     * @param y
     */
    public long getNextForCoordinate(long offset, int dir, int x, int y)
    {
        return 0;
    }

    /**
     * 得到下一个Offset位置，根据Offset
     * @param offset 当前Offset
     * @param dir 方向，上、下、左、右
     * @param x
     * @param y
     */
    public long getNextForOffset(long offset, int dir, int x, int y)
    {
        return 0;
    }

    /**
     * 视图布局
     * @param x
     * @param y
     * @param w
     * @param h
     * @param maxEnd 
     * @param flag 布局标记，传递一些布尔值，位操作
     */
    public int doLayout(int x, int y, int w, int h, long maxEnd, int flag)
    {
        return WPViewConstant.BREAK_NO;
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
        int dX = (int)(x * zoom) + originX;
        int dY = (int)(y * zoom) + originY;
        IView view = getChildView();
        Rect clip = canvas.getClipBounds();
        while (view != null)
        {
            if (view.intersection(clip, dX, dY, zoom))
            {
                view.draw(canvas, dX, dY, zoom);
            }
            view = view.getNextView();
        }
        
    }

    public void getLineHeight(List<Integer> linesHeight)
    {       
        IView view = getChildView();
        if(view != null)
        {
            while (view != null)
            {
                linesHeight.add(view.getHeight());
                view = view.getNextView();
            }
        } 
    }
    
    /**
     * @return Returns the topIndent.
     */
    public int getTopIndent()
    {
        return topIndent;
    }

    /**
     * @param topIndent The topIndent to set.
     */
    public void setTopIndent(int topIndent)
    {
        this.topIndent = topIndent;
    }
    /**
     * @return Returns the bottomIndent.
     */
    public int getBottomIndent()
    {
        return bottomIndent;
    }

    /**
     * @param bottomIndent The bottomIndent to set.
     */
    public void setBottomIndent(int bottomIndent)
    {
        this.bottomIndent = bottomIndent;
    }

    /**
     * @return Returns the leftIndentt.
     */
    public int getLeftIndent()
    {
        return leftIndent;
    }

    /**
     * @param leftIndentt The leftIndentt to set.
     */
    public void setLeftIndent(int leftIndent)
    {
        this.leftIndent = leftIndent;
    }

    /**
     * @return Returns the rightIndent.
     */
    public int getRightIndent()
    {
        return rightIndent;
    }

    /**
     * @param rightIndent The rightIndent to set.
     */
    public void setRightIndent(int rightIndent)
    {
        this.rightIndent = rightIndent;
    }
    
    
    /**
     * 得视图布局大小，包括上下左右Indent
     * 
     * @param axis 方向
     * @see WPViewConstant.X_AXIS
     * @see WPViewConstant.Y_AXIS
     */
    public int getLayoutSpan(byte axis)
    {
        // 宽度
        if (axis == WPViewConstant.X_AXIS)
        {
            return rightIndent + width + leftIndent;
        }
        //
        else
        {
            return topIndent + getHeight() + bottomIndent;
        }
    }
    
    /**
     * 设置边距
     */
    public void setIndent(int left, int top, int right, int bottom)
    {
        this.leftIndent = left;
        this.topIndent = top;
        this.rightIndent = right;
        this.bottomIndent = bottom;
    }
    
    /**
     * 得到包括offset的指定视图
     * 
     * @param offset
     * @param type
     * @param isBack
     * @return
     */
    public IView getView(long offset, int type, boolean isBack)
    {
        IView view = child;
        while (view != null && !view.contains(offset, isBack))
        {
            view = view.getNextView();
        }
        if (view != null && view.getType() != type)
        {
            return view.getView(offset, type, isBack);
        }
        return view;
    }
    
    /**
     * 得到包括x, y值的指定视图
     */
    public IView getView(int x, int y, int type, boolean isBack)
    {
        IView view = child;
        while (view != null && !view.contains(x, y, isBack))
        {
            view = view.getNextView();
        }
        if (view != null && view.getType() !=  type)
        {
            x -= this.x;
            y -= this.y;
            return view.getView(x, y, type, isBack);
        }
        return view;
    }
    
    /**
     * 释放内存
     */
    public void dispose()
    {
        this.parent = null;
        elem = null;
        IView temp = child;
        IView next;
        while (temp != null)
        {
            next = temp.getNextView();
            temp.dispose();
            temp = next;
        }
        this.preView = null;
        this.nextView = null;
        this.child = null;
    }
    
    /**
     * 
     */
    public void free()
    {
        
    }

    // Model的Element
    protected IElement elem;
    // x值
    protected int x;
    // y值
    protected int y;
    // 高度
    protected int width;
    // 宽度
    protected int height;
    // 上边距
    protected int topIndent;
    // 下边距
    protected int bottomIndent;
    // 左边距
    protected int leftIndent;
    // 右边距
    protected int rightIndent;
    // 视图开始Offset，相对model的Element
    protected long start;
    // 视图结束Offset，相对Model的Element
    protected long end;
    // 父视图
    protected IView parent;
    // 子视图
    protected IView child;
    // 前一个视图
    protected IView preView;
    // 后一个视图
    protected IView nextView;

}
