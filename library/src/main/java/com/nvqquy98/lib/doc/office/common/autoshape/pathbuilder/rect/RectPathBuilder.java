/*
 * 文件名称:          RectPathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:36:05
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.rect;

import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-11-2
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class RectPathBuilder
{
    private static RectF rectF = new RectF();
    
    private static Path path = new Path();
    
    /**
     * get rectangle path
     * @param shape
     * @param rect
     * @return
     */
    public static Path getRectPath(AutoShape shape, Rect rect)
    {
        path.reset();
        
        switch(shape.getShapeType())
        {
            case ShapeTypes.Rectangle:                                      // 矩形 ，文本框，垂直文本框
            case ShapeTypes.TextBox:
            case ShapeTypes.TextPlainText:
                return getRectanglePath(shape, rect);
                
            case ShapeTypes.RoundRectangle:                                 // 圆角矩形
                return getRoundRectanglePath(shape, rect);
                
            case ShapeTypes.Round1Rect:                                     // 单圆角矩形
                return getRound1Path(shape, rect);
                
            case ShapeTypes.Round2SameRect:                                 // 同侧圆角矩形
                return getRound2Path(shape, rect);
                
            case ShapeTypes.Round2DiagRect:                                 // 对角圆角矩形
                return getRound2DiagRectPath(shape, rect);
                
            case ShapeTypes.Snip1Rect:                                      // 剪去单角的矩形
                return getSnip1RectPath(shape, rect);
                
            case ShapeTypes.Snip2SameRect:                                  // 剪去同侧角的矩形
                return getSnip2SameRectPath(shape, rect);
                
            case ShapeTypes.Snip2DiagRect:                                  // 剪去对角的矩形
                return getSnip2DiagPath(shape, rect);
                
            case ShapeTypes.SnipRoundRect:                                  // 剪去单角的单圆角矩形
                return getSnipRoundPath(shape, rect);
        }
        
        return null;
    }
    
    private static Path getRectanglePath(AutoShape shape, Rect rect)
    {
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        return path;
    }
    
    private static Path getRoundRectanglePath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addRoundRect(rectF, new float[]{x, x, x, x, x, x, x, x}, Path.Direction.CW);
        return path;
    }
    
    private static Path getRound1Path(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addRoundRect(rectF, new float[]{0, 0, x, x, 0, 0, 0, 0}, Path.Direction.CW);
        return path;
    }
    
    private static Path getRound2Path(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        float y = 0;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            if (values[1] != null)
            {
                y = Math.min(rect.width(), rect.height()) * values[1];
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addRoundRect(rectF, new float[]{x, x, x, x, y, y, y, y}, Path.Direction.CW);
        return path;
    }
    
    private static Path getRound2DiagRectPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        float y = 0;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            if (values[1] != null)
            {
                y = Math.min(rect.width(), rect.height()) * values[1];
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addRoundRect(rectF, new float[]{x, x, y, y, x, x, y, y}, Path.Direction.CW);
        return path;
    }
    
    private static Path getSnip1RectPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
        }
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right - x, rect.top);
        path.lineTo(rect.right, rect.top + x);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getSnip2SameRectPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        float y = 0;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            if (values[1] != null)
            {
                y = Math.min(rect.width(), rect.height()) * values[1];
            }
        }
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right - x, rect.top);
        path.lineTo(rect.right, rect.top + x);
        path.lineTo(rect.right, rect.bottom - y);
        path.lineTo(rect.right - y, rect.bottom);
        path.lineTo(rect.left + y, rect.bottom);
        path.lineTo(rect.left, rect.bottom - y);
        path.lineTo(rect.left, rect.top + x);
        path.close();
        return path;
    }
    
    private static Path getSnip2DiagPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        float y = Math.min(rect.width(), rect.height()) * 0.18f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            if (values[1] != null)
            {
                y = Math.min(rect.width(), rect.height()) * values[1];
            }
        }
        
        path.reset();
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right - y, rect.top);
        path.lineTo(rect.right, rect.top + y);
        path.lineTo(rect.right, rect.bottom - x);
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left + y, rect.bottom);
        path.lineTo(rect.left, rect.bottom - y);
        path.lineTo(rect.left, rect.top + x);
        path.close();
        return path;
    }
    
    private static Path getdrawSnipRoundRectPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        float y = Math.min(rect.width(), rect.height()) * 0.18f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            if (values[1] != null)
            {
                y = Math.min(rect.width(), rect.height()) * values[1];
            }
        }
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right - y, rect.top);
        path.lineTo(rect.right, rect.top + y);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + x);
        rectF.set(rect.left, rect.top, rect.left + x * 2, rect.top + x * 2);
        path.arcTo(rectF, 180, 90);
        path.close();
        return path;
    }
    
    private static Path getSnipRoundPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        float y = Math.min(rect.width(), rect.height()) * 0.18f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            if (values[1] != null)
            {
                y = Math.min(rect.width(), rect.height()) * values[1];
            }
        }
        
        path.reset();
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right - y, rect.top);
        path.lineTo(rect.right, rect.top + y);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + x);
        rectF.set(rect.left, rect.top, rect.left + x * 2, rect.top + x * 2);
        path.arcTo(rectF, 180, 90);
        path.close();
        
        return path;
    }
}
