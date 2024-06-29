/*
 * 文件名称:          IView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:16:53
 */
package com.nvqquy98.lib.doc.office.simpletext.view;

import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 视图接口
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
public interface IView
{
    /**
     * 得到视图对应Model的Element
     */
    public IElement getElement();
    /**
     * 
     */
    public void setElement(IElement elem);
    /**
     * 视图类型，页、栏、段落、行、leaf等
     */
    public short getType();
    /**
     * x值，相对父视图
     */
    public void setX(int x);
    public int getX();
    /**
     * y值, 相对父视图
     */
    public void setY(int y);
    public int getY();
    /**
     * 宽度
     */
    public void setWidth(int w);
    public int getWidth();
    /**
     * 高度
     */
    public void setHeight(int h);
    public int getHeight();
    /**
     * 设置大小
     */
    public void setSize(int w, int h);
    /**
     * 设置位置
     */
    public void setLocation(int x, int y);
    /**
     * 设置范围
     */
    public void setBound(int x, int y, int w, int h);
    /**
     * 上边距
     */
    public void setTopIndent(int top);
    public int getTopIndent();
    /**
     * 下边距
     */
    public void setBottomIndent(int bottom);
    public int getBottomIndent();
    /**
     * 左边距 
     */
    public void setLeftIndent(int left);
    public int getLeftIndent();
    /**
     * 左边距
     */
    public void setRightIndent(int right);
    public int getRightIndent();
    /**
     * 设置边距
     */
    public void setIndent(int left, int top, int right, int bottom);
    /**
     * 得到组件
     */
    public IWord getContainer();
    
    /**
     * 
     */
    public IControl getControl();
    
    /**
     * 得到model
     */
    public IDocument getDocument();
    /**
     * 第一个子视图
     */
    public void setChildView(IView view);
    public IView getChildView();
    /**
     * 父视图
     */
    public void setParentView(IView view);
    public IView getParentView();
    /**
     * 前一个视图
     */
    public void setPreView(IView view);
    public IView getPreView();
    /**
     * 后一个视图
     */
    public void setNextView(IView view);
    public IView getNextView();
    /**
     * 得到最后一个子视图，此方法尽量少用，非常耗时
     */
    public IView getLastView();
    /**
     * 得到子视图数量，此方法尽量少调用，非常耗时
     */
    public int getChildCount();
    /**
     * 追加一个子视图
     */
    public void appendChlidView(IView view);
    /**
     * 指定视图后面插入一个子视图
     * 
     * @param view 指定视图，如果 = null, 则把newView设置成第一个视图
     * @param newView 要插入的视图
     */
    public void insertView(IView view, IView newView);
    /**
     * 删除一个指定视图
     * 
     * @param idDeleteChlid = ture，则连子视图也删除。
     */
    public void deleteView(IView view, boolean isDeleteChild);
    
    /**
     * 视图开始位置
     */
    public void setStartOffset(long start);
    public long getStartOffset(IDocument doc);
    /**
     * 视图结束位置
     * @param end 相对于model的element开始位置的
     */
    public void setEndOffset(long end);
    public long getEndOffset(IDocument doc);
    
    /**
     * 得到Element开始位置
     */
    public long getElemStart(IDocument doc);
    /**
     * 得到Element结束位置
     */
    public long getElemEnd(IDocument doc);
    
    /**
     * get view rect
     * @param originX
     * @param originY
     * @param zoom
     * @return
     */
    public Rect getViewRect(int originX, int originY, float zoom);
    
    /**
     * 视图是否在指定区间内
     * 
     * @param rect
     */
    public boolean intersection(Rect rect, int originX, int originY, float zoom);
    /**
     * 视图是否包含指定 offset
     * 
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public boolean contains(long offset, boolean isBack);
    
    /**
     * 视图是否包含指定 x，y值
     * @param x
     * @param y
     * @param isBack
     * @return
     */
    public boolean contains(int x, int y, boolean isBack);
    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack);
    /**
     * @param x
     * @param y
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public long viewToModel(int x, int y, boolean isBack);
    
    /**
     * 得到下一个Offset位置，根据坐标
     * 
     * @param offset 当前Offset
     * @param dir 方向，上、下、左、右
     * @param x
     * @param y
     */
    public long getNextForCoordinate(long offset, int dir, int x, int y);
    /**
     * 得到下一个Offset位置，根据Offset
     * @param offset 当前Offset
     * @param dir 方向，上、下、左、右
     * @param x
     * @param y
     */
    public long getNextForOffset(long offset, int dir, int x, int y);    
    /**
     * 
     * @param canvas
     * @param originX
     * @param originY
     * @param zoom
     */
    public void draw(Canvas canvas, int originX, int originY, float zoom);
    
    /**
     * 视图布局
     * @param x
     * @param y
     * @param w
     * @param h
     * @param maxEnd 
     * @param flag 布局标记，传递一些布尔值，位操作
     */
    public int doLayout(int x, int y, int w, int h, long maxEnd, int flag);
    
    /**
     * 得视图布局大小，包括上下左右Indent
     * 
     * @param axis 方向
     * @see WPViewConstant.X_AXIS
     * @see WPViewConstant.Y_AXIS
     */
    public int getLayoutSpan(byte axis);
    
    /**
     * 得到包括offset的指定视图
     * 
     * @param offset
     * @param type
     * @param isBack
     * @return
     */
    public IView getView(long offset, int type, boolean isBack);
    
    /**
     * 得到包括x, y值的指定视图
     */
    public IView getView(int x, int y, int type, boolean isBack);
    
    /**
     * 释放内存
     */
    public void dispose();
    
    /**
     * 
     */
    public void free();
     
}
