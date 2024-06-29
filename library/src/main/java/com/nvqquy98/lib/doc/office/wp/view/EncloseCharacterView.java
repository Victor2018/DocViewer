/*
 * 文件名称:          	EcloseCharacterView.java
 *  
 * 编译器:            android2.2
 * 时间:             	上午10:54:01
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        	Office engine V1.0
 * <p>
 * 作者:            	ljj8494
 * <p>
 * 日期:            	2013-4-22
 * <p>
 * 负责人:          	ljj8494
 * <p>
 * 负责小组:        	TMC
 * <p>
 * <p>
 */
public class EncloseCharacterView extends LeafView
{    
    /**
     * 
     */
    public EncloseCharacterView()
    {
        
    }

    /**
     * 
     * @param paraElem
     * @param elem
     */
    public EncloseCharacterView(IElement paraElem, IElement elem)
    {
        super(paraElem, elem);
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPViewConstant.ENCLOSE_CHARACTER_VIEW;
    }    
    
    /**
     * 初始化leaf属性
     */
    public void initProperty(IElement elem, IElement paraElem)
    {
        super.initProperty(elem, paraElem);
        //paint.setColor(Color.BLACK);;
        enclosePaint = new Paint();
        enclosePaint.setColor(charAttr.fontColor);
        enclosePaint.setStyle(Style.STROKE);
        enclosePaint.setAntiAlias(true);
        path = new Path();
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
        super.draw(canvas, originX, originY, zoom);
        // 画圈
        drawEnclose(canvas, originX, originY, zoom);
    }
    
    /**
     * 
     */
    private void drawEnclose(Canvas canvas, int originX, int originY, float zoom)
    {
        int dX = (int)(x * zoom) + originX;
        int dY = (int)(y * zoom) + originY;
        int w = (int)(getWidth() * zoom);
        int h = (int)(getHeight() * zoom);
        // 圆
        if (charAttr.encloseType == WPModelConstant.ENCLOSURE_TYPE_ROUND)
        {
            canvas.drawArc(new RectF(dX, dY, dX + w, dY + h), 0, 360, false, enclosePaint);
        }
        // 正方形
        else if (charAttr.encloseType == WPModelConstant.ENCLOSURE_TYPE_SQUARE)
        {
            canvas.drawRect(dX, dY, dX + w, dY + h, enclosePaint);
        }
        // 三角形
        else if (charAttr.encloseType == WPModelConstant.ENCLOSURE_TYPE_TRIANGLE)
        {
            path.reset();
            path.moveTo(dX + w / 2, dY);
            path.lineTo(dX, dY + h);
            path.lineTo(dX + w, dY + h);
            path.close();
            canvas.drawPath(path, enclosePaint);
        }
        // 菱形
        else if (charAttr.encloseType == WPModelConstant.ENCLOSURE_TYPE_RHOMBUS)
        {
            path.reset();
            path.moveTo(dX + w / 2, dY);
            path.lineTo(dX, dY + h / 2);
            path.lineTo(dX + w / 2, dY + h);
            path.lineTo(dX + w, dY + h / 2);
            path.close();
            canvas.drawPath(path, enclosePaint);
        }
    }
    
    /**
     * 放回对象池
     */
    public void free()
    {
        /*parent = null;
        preView = null;
        nextView = null;
        child = null;
        ViewFactory.leafView.free(this);*/
    }
    
    /**
     * 复制对象
     * /
    public IMemObj getCopy()
    {
        return new LeafView();
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        paint = null;
        //free();
    }
    //
    protected Paint enclosePaint;
    //
    protected Path path;
}
