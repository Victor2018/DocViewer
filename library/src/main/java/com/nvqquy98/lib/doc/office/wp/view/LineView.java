/*
 * 文件名称:          LineView.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:35:04
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.AbstractView;
import com.nvqquy98.lib.doc.office.simpletext.view.CharAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewKit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * word 行视图
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-20
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class LineView extends AbstractView/* implements IMemObj*/
{
	/**
	 * line height except shape(autoshape and object)
	 */
	private int heightExceptShape;
	
    /**
     * 
     */
    public LineView()
    {
    }
    /**
     * 
     * @param elem
     */
    public LineView(IElement elem)
    {
       this.elem = elem; 
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPViewConstant.LINE_VIEW;
    }
    
    /**
     * 对齐方式布局
     * 
     * @param hAlignment
     * @param vAlignment
     * @param span
     */
    public void layoutAlignment(DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr, BNView bnView, int span, int flag)
    {
        layoutAlignment(docAttr, pageAttr, paraAttr, bnView, span, flag, true);
    }    
    
    public void setHeightExceptShape(int height)
    {
    	this.heightExceptShape = height;
    }
    
    public int getHeightExceptShape()
    {
    	return heightExceptShape;
    }
    
    /**
     * 对齐方式布局
     * 
     * @param hAlignment
     * @param vAlignment
     * @param span
     */
    public void layoutAlignment(DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr, BNView bnView, int span, int flag, boolean isLayoutVertical)
    {
        // 水平
        if (!ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_NOT_WRAP_LINE))
        {
            layoutHorizontal(docAttr, pageAttr, paraAttr, bnView, span, flag);            
        }
        // 垂直
        if (isLayoutVertical)
        {
            layoutVertical(docAttr, pageAttr, paraAttr, bnView, span , flag);            
        }
    }
    
    /**
     * 水平对齐布局
     * 
     * @param docAttr   文档属性       
     * @param pageAttr  页面属性
     * @param paraAttr  段落属性
     * @param span      行布局宽度
     * @param flag      布局标记
     */
    public void layoutHorizontal(DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr, BNView bnView, int span, int flag)
    {
        // 水平对齐
        switch (paraAttr.horizontalAlignment)
        {
            case WPAttrConstant.PARA_HOR_ALIGN_CENTER: // 居中
                x += (span - width) / 2;
                break;
            case WPAttrConstant.PARA_HOR_ALIGN_RIGHT:// 居右
                x += (span - width);
                break;

            default:
                break;
        }
    }
    
    /**
     * 垂直对齐布局
     * 
     * @param docAttr   文档属性       
     * @param pageAttr  页面属性
     * @param paraAttr  段落属性
     * @param span      行布局宽度
     * @param flag      布局标记
     */
    private void layoutVertical(DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr, BNView bnView, int span, int flag)
    {
        LeafView leaf = (LeafView)getChildView();
        if (leaf == null)
        {
            return;
        }
        int maxBaseline = bnView == null ? 0 : bnView.getBaseline();
        while (leaf != null)
        {
            maxBaseline = Math.max(maxBaseline, leaf.getBaseline());
            leaf = (LeafView)leaf.getNextView();
        }
        // 基线对齐
        leaf = (LeafView)getChildView();
        while (leaf != null)
        {
            if (leaf.getType() == WPViewConstant.SHAPE_VIEW)
            {
                if (!((ShapeView)leaf).isInline())
                {
                    leaf = (LeafView)leaf.getNextView();
                    continue;
                }
            }
            else if(leaf.getType() == WPViewConstant.OBJ_VIEW)
            {
                if (!((ObjView)leaf).isInline())
                {
                    leaf = (LeafView)leaf.getNextView();
                    continue;
                }
            }
            
            int baseline = maxBaseline - leaf.getBaseline();
            leaf.setTopIndent(baseline);
            leaf.setY(leaf.getY() + baseline);
            leaf = (LeafView)leaf.getNextView();
        }
        float value = 0;
        boolean processline = false;
        // 行距
        switch (paraAttr.lineSpaceType)
        {
            case WPAttrConstant.LINE_SPACE_SINGLE: // 单倍行距
            case WPAttrConstant.LINE_SPACE_ONE_HALF: // 1.5倍行距
            case WPAttrConstant.LINE_SPACE_DOUBLE: // 2倍行距
            case WPAttrConstant.LINE_SAPCE_MULTIPLE: // 多倍行距
            	processline = true;
                if(pageAttr.pageLinePitch > 0)
                {
                	if(heightExceptShape > pageAttr.pageLinePitch * paraAttr.lineSpaceValue)
                	{
                		value = (float)(Math.ceil(heightExceptShape / pageAttr.pageLinePitch) * pageAttr.pageLinePitch);
                	}
                	else
                	{
                		value = pageAttr.pageLinePitch * paraAttr.lineSpaceValue;
                	}
                }
                else
                {
                	value = paraAttr.lineSpaceValue * heightExceptShape;
                }
                break;
                
            case WPAttrConstant.LINE_SAPCE_LEAST: // 最小行距
            	processline = true;
                if (paraAttr.lineSpaceValue > heightExceptShape)
                {
                	processline = true;
                	if(pageAttr.pageLinePitch > 0)
                    {
                    	value = Math.max(paraAttr.lineSpaceValue, pageAttr.pageLinePitch);
                    }
                    else
                    {
                    	value = paraAttr.lineSpaceValue;
                    }
                }
                else
                {
                	if(pageAttr.pageLinePitch > 0)
                    {
                		processline = true;
                    	value = (float)(Math.ceil(heightExceptShape / pageAttr.pageLinePitch) * pageAttr.pageLinePitch);
                    }
                    else
                    {
                    	value = heightExceptShape;
                    }
                }
                break;
                
            /*case WPAttrConstant.LINE_SPACE_EXACTLY: // 固定行距
                value = (paraAttr.lineSpaceValue - getHeight()) / 2;
                setTopIndent((int)value);
                setBottomIndent((int)value);
                setY(getY() + (int)value);
                if (bnView != null)
                {
                    bnView.setTopIndent((int)value);
                    bnView.setBottomIndent((int)value);
                    bnView.setY((int)value);
                }
                break;*/
                
            default:
                break;
        }
        
        if(processline)
        {
        	value = (value - heightExceptShape) / 2;
            setTopIndent((int)value);
            setBottomIndent((int)value);
            setY(getY() + (int)value);
            if (bnView != null)
            {
                bnView.setTopIndent((int)value);
                bnView.setBottomIndent((int)value);
                bnView.setY((int)value);
            }
        }        
    }
    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        IView view = getView(offset, WPViewConstant.LEAF_VIEW, isBack);
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
        IView view = getView(x, y, WPViewConstant.LEAF_VIEW, isBack);
        if (view == null)
        {
            if (x > getWidth())
            {
                view = getLastView();
            }
            else
            {
                view = getChildView();
            }            
        }
        if (view != null)
        {
            return view.viewToModel(x, y, isBack);
        }
        return -1;
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
        canvas.save();
        IWord word = (IWord)getContainer();
        int dX = (int)(x * zoom) + originX;
        int dY = (int)(y * zoom) + originY;
        IView view = getChildView();
        Rect clip = canvas.getClipBounds();
        if (getTopIndent() < 0
            && word != null && word.getEditType() == MainConstant.APPLICATION_TYPE_WP)
        {
            canvas.clipRect(dX, dY - getTopIndent() * zoom, dX + getLayoutSpan(WPViewConstant.X_AXIS) * zoom, 
                dY - getTopIndent() * zoom + getLayoutSpan(WPViewConstant.Y_AXIS) * zoom);
        }
        while (view != null)
        {
            if (view.intersection(clip, dX, dY, zoom))
            {
                view.draw(canvas, dX, dY, zoom);
            }            
            view = view.getNextView();
        }
        canvas.restore();
        // draw underline 
        drawUnderline(canvas, originX, originY, zoom);
        // 绘制高亮
        if (word != null && word.getHighlight() != null)
        {
            word.getHighlight().draw(canvas, this, dX, dY, getStartOffset(null), getEndOffset(null), zoom);
        }
    }
    
    /**
     * draw underline
     * 
     * @param canvas
     * @param originX
     * @param originY
     * @param zoom
     */
    private void drawUnderline(Canvas canvas, int originX, int originY, float zoom)
    {
        Paint paint = new Paint();
        int dX = (int)(x * zoom) + originX;
        //int dY = (int)(y * zoom + originY + (getLayoutSpan(WPViewConstant.Y_AXIS) - getBottomIndent()) * zoom);        
        int dY = (int)(y * zoom + originY + getTopIndent() * zoom);
        LeafView leaf = (LeafView)getChildView();
        int w = 0;
        int color = Integer.MAX_VALUE;
        int baseline = 0;
        while (leaf != null)
        {
            CharAttr charAttr = leaf.getCharAttr();
            if (charAttr == null)
            {
                leaf = (LeafView)leaf.getNextView();
                continue;
            }
            if (charAttr.underlineType > 0)
            {
                // difference color
                if (color != Integer.MAX_VALUE && color != charAttr.underlineColor)
                {
                	//draw last underline
                    paint.setColor(color);
                    canvas.drawRect(dX, dY + baseline + 1, dX + w, dY + baseline + 2, paint);
                    dX += w;
                    
                    //new underline begin
                    color = charAttr.underlineColor;
                    w = 0;
                    baseline = 0;
                }
                else if(color == Integer.MAX_VALUE)
                {
                    color = charAttr.underlineColor;
                }
                w += (int)(leaf.getWidth() * zoom);
                baseline = Math.max(baseline, (int)(leaf.getUnderlinePosition() * zoom));
            }
            else
            {
                if (color != Integer.MAX_VALUE)
                {
                	//draw last underline
                    paint.setColor(color);
                    canvas.drawRect(dX, dY + baseline + 1, dX + w, dY + baseline + 2, paint);
                    
                    dX += w;
                    w = 0;
                    baseline = 0;
                }
                dX += (int)(leaf.getWidth() * zoom);
                color = Integer.MAX_VALUE;
            }
            leaf = (LeafView)leaf.getNextView();
        }
        if (color != Integer.MAX_VALUE)
        {
            paint.setColor(color);
            canvas.drawRect(dX, dY + baseline + 1, dX + w, dY + baseline + 2, paint);
        }
        
    }
    
    /**
     * 放回对象池
     */
    public void free()
    {
        /*parent = null;
        elem = null;
        IView temp = child;
        IView next;
        while (temp != null)
        {
            next = temp.getNextView();
            temp.free();
            temp = next;
        }
        preView = null;
        nextView = null;
        child = null;
        ViewFactory.lineView.free(this);*/
    }
    
    /**
     * 复制对象
     * /
    public IMemObj getCopy()
    {
        return new LineView();
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        //free();
    }
    
}
