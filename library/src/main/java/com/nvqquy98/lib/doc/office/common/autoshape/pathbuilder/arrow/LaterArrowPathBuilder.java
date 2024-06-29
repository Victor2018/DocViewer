/*
 * 文件名称:          LaterArrowPathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:43:56
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.arrow;

import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Path.Direction;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-10-17
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class LaterArrowPathBuilder
{
    private static final float TODEGREE = 36000000f / 21600000f;
    
    private static RectF s_rect = new RectF();
    
    private static Path path = new Path();
    
    /**
     * get autoshape path
     * @param shape
     * @param rect
     * @return
     */
    public static Object getArrowPath(AutoShape shape, Rect rect)
    {
        path.reset();
        
        switch(shape.getShapeType())
        {
            case ShapeTypes.RightArrow:
                return getRightArrowPath(shape, rect);
                
            case ShapeTypes.LeftArrow:
                return getLeftArrowPath(shape, rect);
                
            case ShapeTypes.UpArrow:
                return getUpArrowPath(shape, rect);
                
            case ShapeTypes.DownArrow:
                return getDownArrowPath(shape, rect);
                
            case ShapeTypes.LeftRightArrow:
                return getLeftRightArrowPath(shape, rect);
                
            case ShapeTypes.UpDownArrow:
                return getUpDownArrowPath(shape, rect);
                
            case ShapeTypes.QuadArrow:
                return getQuadArrowPath(shape, rect);
                
            case ShapeTypes.LeftRightUpArrow:
                return getLeftRightUpArrowPath(shape, rect);
                
            case ShapeTypes.BentArrow:
                return getBentArrowPath(shape, rect);
                
            case ShapeTypes.UturnArrow:
                return getUturnArrowPath(shape, rect);
                
            case ShapeTypes.LeftUpArrow:
                return getLeftUpArrowPath(shape, rect);
                
            case ShapeTypes.BentUpArrow:
                return getBentUpArrowPath(shape, rect);
                
            case ShapeTypes.CurvedRightArrow:
                return getCurvedRightArrowPath(shape, rect);
                
            case ShapeTypes.CurvedLeftArrow:
                return getCurvedLeftArrowPath(shape, rect);
                
            case ShapeTypes.CurvedUpArrow:
                return getCurvedUpArrowPath(shape, rect);
                
            case ShapeTypes.CurvedDownArrow:
                return getCurvedDownArrowPath(shape, rect);
                
            case ShapeTypes.StripedRightArrow:
                return getStripedRightArrowPath(shape, rect);
                
            case ShapeTypes.NotchedRightArrow:
                return getNotchedRightArrowPath(shape, rect);
                
            case ShapeTypes.HomePlate:
                return getHomePlatePath(shape, rect);
                
            case ShapeTypes.Chevron:
                return getChevronPath(shape, rect);
                
            case ShapeTypes.RightArrowCallout:
                return getRightArrowCalloutPath(shape, rect);
                
            case ShapeTypes.LeftArrowCallout:
                return getLeftArrowCalloutPath(shape, rect);
                
            case ShapeTypes.UpArrowCallout:
                return getUpArrowCalloutPath(shape, rect);
                
            case ShapeTypes.DownArrowCallout:
                return getDownArrowCalloutPath(shape, rect);
                
            case ShapeTypes.LeftRightArrowCallout:
                return getLeftRightArrowCalloutPath(shape, rect);
                
            case ShapeTypes.QuadArrowCallout:
                return getQuadArrowCalloutPath(shape, rect);
                
            case ShapeTypes.CircularArrow:
                return getCircularArrowPath(shape, rect);
        }
        
        return new Path();
    }
    
    private static Path getRightArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.height() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        path.moveTo(rect.left, rect.centerY() - adj1);
        path.lineTo(rect.right - adj2, rect.centerY() - adj1);
        path.lineTo(rect.right - adj2, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj2, rect.bottom);
        path.lineTo(rect.right - adj2, rect.centerY() + adj1);
        path.lineTo(rect.left, rect.centerY() + adj1);
        path.close();        
        
        return path;
    }
    
    private static Path getLeftArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.height() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj2, rect.top);
        path.lineTo(rect.left + adj2, rect.centerY() - adj1);
        path.lineTo(rect.right, rect.centerY() - adj1);
        path.lineTo(rect.right, rect.centerY() + adj1);
        path.lineTo(rect.left + adj2, rect.centerY() + adj1);
        path.lineTo(rect.left + adj2, rect.bottom);
        path.close();
        return path;
    }
    
    private static Path getUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.width() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        path.moveTo(rect.centerX(), rect.top);
        path.lineTo(rect.right, rect.top + adj2);
        path.lineTo(rect.centerX() + adj1, rect.top + adj2);
        path.lineTo(rect.centerX() + adj1, rect.bottom);
        path.lineTo(rect.centerX() - adj1, rect.bottom);
        path.lineTo(rect.centerX() - adj1, rect.top + adj2);
        path.lineTo(rect.left, rect.top + adj2);
        path.close();
        return path;
    }
    
    private static Path getDownArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.width() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        path.moveTo(rect.centerX() - adj1, rect.top);
        path.lineTo(rect.centerX() + adj1, rect.top);
        path.lineTo(rect.centerX() + adj1, rect.bottom - adj2);
        path.lineTo(rect.right, rect.bottom - adj2);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.left, rect.bottom - adj2);
        path.lineTo(rect.centerX() - adj1, rect.bottom - adj2);
        path.close();
        return path;
    }
    
    private static Path getLeftRightArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.height() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
            
            if(adj2 * 2 > rect.width())
            {
                adj2 = len2 * 2;
            }
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj2, rect.top);
        path.lineTo(rect.left + adj2, rect.centerY() - adj1);
        path.lineTo(rect.right - adj2, rect.centerY() - adj1);
        path.lineTo(rect.right - adj2, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj2, rect.bottom);
        path.lineTo(rect.right - adj2, rect.centerY() + adj1);
        path.lineTo(rect.left + adj2, rect.centerY() + adj1);
        path.lineTo(rect.left + adj2, rect.bottom);
        
        path.close();
        return path;
    }
    
    private static Path getUpDownArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.width() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
            
            if(adj2 * 2 > rect.height())
            {
                adj2 = len2 * 2;
            }
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        path.moveTo(rect.centerX(), rect.top);
        path.lineTo(rect.right, rect.top + adj2);
        path.lineTo(rect.centerX() + adj1, rect.top + adj2);
        path.lineTo(rect.centerX() + adj1, rect.bottom - adj2);
        path.lineTo(rect.right, rect.bottom - adj2);
        path.lineTo(rect.exactCenterX(), rect.bottom);
        path.lineTo(rect.left, rect.bottom - adj2);
        path.lineTo(rect.centerX() - adj1, rect.bottom - adj2);
        path.lineTo(rect.centerX() - adj1, rect.top + adj2);
        path.lineTo(rect.left, rect.top + adj2);
        
        path.close();
        return path;
    }
    
    private static Path getQuadArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len1 = Math.min(rect.width(),rect.height()) / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
            adj3 = Math.round(len2 * values[2]);
            
            if(adj1 > adj2)
            {
                adj1 = adj2;
            }
            
            if(adj2 + adj3 > len2 / 2)
            {
                adj3 = len2 / 2 - adj2;
            }            
        } 
        else
        {
            adj1 = Math.round(len1 * 0.225f);
            adj2 = Math.round(len2 * 0.225f);
            adj3 = Math.round(len2 * 0.225f);
        }
        
        //left arrow
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj3, rect.centerY() - adj2);
        path.lineTo(rect.left + adj3, rect.centerY() - adj1);
        
        //up
        path.lineTo(rect.centerX() - adj1, rect.centerY() - adj1);
        path.lineTo(rect.centerX() - adj1, rect.top + adj3);
        path.lineTo(rect.centerX() - adj2, rect.top + adj3);
        path.lineTo(rect.centerX(), rect.top);
        path.lineTo(rect.centerX() + adj2, rect.top + adj3);
        path.lineTo(rect.centerX() + adj1, rect.top + adj3);
        
        //right
        path.lineTo(rect.centerX() + adj1, rect.centerY() - adj1);
        path.lineTo(rect.right - adj3, rect.centerY() - adj1);
        path.lineTo(rect.right - adj3, rect.centerY() - adj2);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj3, rect.centerY() + adj2);
        path.lineTo(rect.right - adj3, rect.centerY() + adj1);
        path.lineTo(rect.centerX() + adj1, rect.centerY() + adj1);
        
        //down
        path.lineTo(rect.centerX() + adj1, rect.bottom - adj3);
        path.lineTo(rect.centerX() + adj2, rect.bottom - adj3);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.centerX() - adj2, rect.bottom - adj3);
        path.lineTo(rect.centerX() - adj1, rect.bottom -adj3);
        path.lineTo(rect.centerX() - adj1, rect.centerY() + adj1);
        
        path.lineTo(rect.left + adj3, rect.centerY() + adj1);
        path.lineTo(rect.left + adj3, rect.centerY() + adj2);
        
        path.close();
        return path;
    }
    
    private static Path getLeftRightUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len * values[0] / 2);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            
            if(adj1 > adj2)
            {
                adj1 = adj2;
            }
            
            if(adj2 + adj3 > rect.width())
            {
                adj3 = len / 2 - adj2;
            }
            
            if(adj2 * 2 + adj3 > rect.height())
            {
                adj3 = rect.height() - adj2 * 2;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.225f / 2);
            adj2 = Math.round(len * 0.225f);
            adj3 = Math.round(len * 0.225f);
        }        
        
        //left arrow
        path.moveTo(rect.left + adj3, rect.bottom - adj2 + adj1);
        path.lineTo(rect.left + adj3, rect.bottom);
        path.lineTo(rect.left, rect.bottom - adj2);
        path.lineTo(rect.left + adj3, rect.bottom - adj2 * 2);
        path.lineTo(rect.left + adj3, rect.bottom - adj2 - adj1);
        path.lineTo(rect.centerX() - adj1, rect.bottom - adj2  - adj1);
        
        //up arrow
        path.lineTo(rect.centerX() - adj1, rect.top + adj3);
        path.lineTo(rect.centerX() - adj2, rect.top + adj3);
        path.lineTo(rect.centerX(), rect.top);
        path.lineTo(rect.centerX() + adj2, rect.top + adj3);
        path.lineTo(rect.centerX() + adj1, rect.top + adj3);
        path.lineTo(rect.centerX() + adj1, rect.bottom - adj2 - adj1);
        
        //right arrow
        path.lineTo(rect.right - adj3, rect.bottom - adj2 - adj1);
        path.lineTo(rect.right - adj3, rect.bottom - adj2 * 2);
        path.lineTo(rect.right, rect.bottom - adj2);
        path.lineTo(rect.right - adj3, rect.bottom);
        path.lineTo(rect.right - adj3, rect.bottom - adj2 + adj1);
        
        path.close();
        
        return path;
    }
    
    private static Path getBentArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 4)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(len * values[3]);
            
            if(adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if(adj3 + adj4 > rect.width())
            {
                adj4 = rect.width() - adj3;
            }
            if(adj4 > rect.height())
            {
                adj4 = rect.height();
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
            adj4 = Math.round(len * 0.4375f);
        }
        
        path.moveTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + adj2 - adj1 / 2 + adj4);
        s_rect.set(rect.left, rect.top + adj2 - adj1 / 2, rect.left + 2 * adj4, rect.top + adj2 - adj1 / 2 +  2 * adj4);
        path.arcTo(s_rect, 180, 90);
        
        path.lineTo(rect.right - adj3, rect.top + adj2 - adj1 / 2);
        path.lineTo(rect.right - adj3, rect.top);
        path.lineTo(rect.right, rect.top + adj2);
        path.lineTo(rect.right - adj3, rect.top + adj2 * 2);
        path.lineTo(rect.right - adj3, rect.top + adj2 + adj1 / 2);
        
        if(adj4 >= adj1)
        {
            path.lineTo(rect.left + adj4, rect.top + adj2 + adj1 / 2);
            
            s_rect.set(rect.left + adj1, rect.top + adj2 + adj1 / 2, rect.left + 2 * (adj4 - adj1), rect.top + adj2 + adj1 / 2 + 2 * (adj4 - adj1));
            path.arcTo(s_rect, 270, -90);
            path.lineTo(rect.left + adj1, rect.top + adj2 - adj1 / 2 + adj4);
        }
        else
        {
            path.lineTo(rect.left + adj1, rect.top + adj2 + adj1 / 2);
        }
        
        path.lineTo(rect.left + adj1, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getUturnArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int adj5 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 5)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(len * values[3]);
            adj5 = Math.round(rect.height() * values[4]);
            
            if(adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if(adj4 + adj3 >= adj5)
            {
                adj5 = adj3 + adj4;
            }
            
            if(adj5 > rect.height())
            {
                adj5 = rect.height();
                adj3 = adj5 - adj4;
            }
            
            if(adj5 - adj3 < adj1)
            {
                adj3 = adj5 - adj1;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
            adj4 = Math.round(len * 0.4375f);
            adj5 = Math.round(rect.height() * 0.75f);
        }
        
        path.moveTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + adj4);
        
        s_rect.set(rect.left, rect.top, rect.left + 2 * adj4, rect.top +  2 * adj4);
        path.arcTo(s_rect, 180, 90);
        path.lineTo(rect.right - adj2 + adj1 / 2 - adj4, rect.top);
        
        s_rect.set(rect.right - adj2 + adj1 / 2 - 2 * adj4, rect.top, rect.right - adj2 + adj1 / 2, rect.top + 2 * adj4);
        path.arcTo(s_rect, 270, 90);
        
        path.lineTo(rect.right - adj2 + adj1 / 2, rect.top + adj5 - adj3);
        path.lineTo(rect.right, rect.top + adj5 - adj3);
        path.lineTo(rect.right - adj2, rect.top + adj5);
        path.lineTo(rect.right - adj2 * 2, rect.top + adj5 - adj3);
        path.lineTo(rect.right - adj2 - adj1 / 2, rect.top + adj5 - adj3);
        
        
        if(adj4 >= adj1)
        {
            path.lineTo(rect.right - adj2 - adj1 / 2, rect.top + adj4);
            s_rect.set(rect.right - adj2 - adj1 / 2 - 2 * (adj4 - adj1), rect.top + adj1, rect.right - adj2 - adj1 / 2, rect.top + adj1 + 2 * (adj4 - adj1));
            path.arcTo(s_rect, 0, -90);
            path.lineTo(rect.right - adj2 + adj1 / 2 - adj4, rect.top + adj1);
            
            path.lineTo(rect.left + adj4, rect.top + adj1);
            
            s_rect.set(rect.left + adj1, rect.top + adj1, rect.left + adj1 + 2 * (adj4 - adj1), rect.top + adj1 + 2 * (adj4 - adj1));
            path.arcTo(s_rect, 270, -90);
            path.lineTo(rect.left + adj1, rect.top + adj4);
            
        }
        else
        {
            path.lineTo(rect.right - adj2 - adj1 / 2, rect.top + adj1);
            path.lineTo(rect.left + adj1, rect.top + adj1);
        }
        
        path.lineTo(rect.left + adj1, rect.bottom);
        path.close();
        
        return path;
    }

    private static Path getLeftUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            
            if( adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if(adj2 * 2 + adj3 > len)
            {
                adj3 = len - adj2 * 2;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
        }
       
       
        
        //right arrow
        path.moveTo(rect.left + adj3, rect.bottom - adj2 + adj1 / 2);
        path.lineTo(rect.left + adj3, rect.bottom);
        path.lineTo(rect.left, rect.bottom - adj2);
        path.lineTo(rect.left + adj3, rect.bottom - 2 * adj2);
        path.lineTo(rect.left + adj3, rect.bottom - adj2 - adj1 / 2);
        path.lineTo(rect.right - adj2 - adj1 / 2, rect.bottom - adj2 - adj1 / 2);
        
        
        //up arrow
        path.lineTo(rect.right - adj2 - adj1 / 2, rect.top + adj3);
        path.lineTo(rect.right - adj2 * 2, rect.top + adj3);
        path.lineTo(rect.right - adj2, rect.top);
        path.lineTo(rect.right, rect.top + adj3);
        path.lineTo(rect.right - adj2 + adj1 / 2, rect.top + adj3);
        path.lineTo(rect.right - adj2 + adj1 / 2, rect.bottom - adj2 + adj1 / 2);
        path.close();
        
        return path;
    }
    
    private static Path getBentUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
        }
        
        path.moveTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.bottom - adj1);
        path.lineTo(rect.right - adj2 - adj1 /2, rect.bottom - adj1);        
        path.lineTo(rect.right - adj2 - adj1 / 2, rect.top + adj3);
        path.lineTo(rect.right - adj2 * 2, rect.top + adj3);
        path.lineTo(rect.right - adj2, rect.top);
        path.lineTo(rect.right, rect.top + adj3);
        path.lineTo(rect.right - adj2 + adj1 / 2, rect.top + adj3);
        path.lineTo(rect.right - adj2 + adj1 / 2, rect.bottom);
        
        path.close();
        
        
        return path;
    }
    
    private static List<Path> getCurvedRightArrowPath(AutoShape shape, Rect rect)
    {
        List<Path> pathList = new ArrayList<Path>(2);
        
        Float[] values = shape.getAdjustData();        
        Path path = new Path();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.5f);
            adj3 = Math.round(len * 0.25f);
        }
        
        int ovalWidth = 2 * rect.width();
        int ovalHeight = (rect.bottom - adj2 / 2 - adj1 / 2 - rect.top);
        
        path.moveTo(rect.right, rect.top);
        s_rect.set(rect.left, rect.top, rect.left + ovalWidth, rect.top + ovalHeight);
        path.arcTo(s_rect, 270, -90);
        path.lineTo(rect.left, rect.top + ovalHeight / 2 + adj1);
        s_rect.set(rect.left, rect.top + adj1, rect.left + ovalWidth, rect.top + ovalHeight + adj1);
        path.arcTo(s_rect, 180, 90);
        path.close();
        
        pathList.add(path);
        
        path = new Path();
        path.moveTo(rect.left, rect.top + ovalHeight / 2);
        
        double y = Math.sqrt(Math.pow(ovalHeight / 2, 2)  * (Math.pow(ovalWidth / 2, 2) - Math.pow(adj3, 2)) / Math.pow(ovalWidth / 2, 2));
  
        int angle = (int)(Math.atan(y / adj3) * 180 / Math.PI);       
        
        s_rect.set(rect.left, rect.top, rect.left + ovalWidth, rect.top + ovalHeight);
        path.arcTo(s_rect, 180, -angle);
        path.setLastPoint(rect.right - adj3, rect.top + ovalHeight / 2 + (int)y);
        
        path.lineTo(rect.right - adj3, rect.top + ovalHeight / 2 + (int)y + adj1/ 2 - adj2 / 2);
        path.lineTo(rect.right, rect.bottom - adj2 / 2);
        path.lineTo(rect.right - adj3, rect.top + ovalHeight / 2 + (int)y + adj1/ 2 + adj2 / 2);        
        path.lineTo(rect.right - adj3, rect.top + ovalHeight / 2 + (int)y + adj1);
        
        s_rect.set(rect.left, rect.top + adj1, rect.left + ovalWidth, rect.top + ovalHeight + adj1);
        path.arcTo(s_rect, 180 - angle, angle);
        path.close(); 
      
        pathList.add(path);
        
        return pathList;
    }
    
    private static List<Path> getCurvedLeftArrowPath(AutoShape shape, Rect rect)
    {
        List<Path> pathList = new ArrayList<Path>(2);
        
        Float[] values = shape.getAdjustData();        
        Path path = new Path();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.5f);
            adj3 = Math.round(len * 0.25f);
        }
        
        int ovalWidth = 2 * rect.width();
        int ovalHeight = (rect.bottom - adj2 / 2 - adj1 / 2 - rect.top);
        
        path.moveTo(rect.right, rect.top + ovalHeight / 2);
        
        double y = Math.sqrt(Math.pow(ovalHeight / 2, 2)  * (Math.pow(ovalWidth / 2, 2) - Math.pow(adj3, 2)) / Math.pow(ovalWidth / 2, 2));
  
        int angle = (int)(Math.atan(y / adj3) * 180 / Math.PI);       
        
        s_rect.set(rect.right - ovalWidth, rect.top, rect.right, rect.top + ovalHeight);
        path.arcTo(s_rect, 0, angle);
        path.setLastPoint(rect.left + adj3, rect.top + ovalHeight / 2 + (int)y);
        path.lineTo(rect.left + adj3, rect.top + ovalHeight / 2 + (int)y + adj1/ 2 - adj2 / 2);
        path.lineTo(rect.left, rect.bottom - adj2 / 2);
        path.lineTo(rect.left + adj3, rect.top + ovalHeight / 2 + (int)y + adj1/ 2 + adj2 / 2);
        path.lineTo(rect.left + adj3, rect.top + ovalHeight / 2 + (int)y + adj1);
        s_rect.set(rect.right - ovalWidth, rect.top + adj1, rect.right, rect.top + ovalHeight + adj1);
        path.arcTo(s_rect, angle, -angle);
        path.close();
      
        pathList.add(path);
        
        //        
        path = new Path();
        path.moveTo(rect.left, rect.top);
        s_rect.set(rect.right - ovalWidth, rect.top, rect.right, rect.top + ovalHeight);
        path.arcTo(s_rect, 270, 90);
        path.lineTo(rect.right, rect.top + ovalHeight / 2 + adj1);
        s_rect.set(rect.right - ovalWidth, rect.top + adj1, rect.right, rect.top + ovalHeight + adj1);
        path.arcTo(s_rect, 0, -90);
        path.close();
        
        pathList.add(path);
        
        return pathList;
    }
    
    private static List<Path> getCurvedUpArrowPath(AutoShape shape, Rect rect)
    {
        List<Path> pathList = new ArrayList<Path>(2);
        
        Float[] values = shape.getAdjustData();        
        Path path = new Path();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.5f);
            adj3 = Math.round(len * 0.25f);
        }
        
        int ovalHalfWidth = (rect.width() - adj2 / 2 - adj1 / 2) / 2;
        int ovalHalfHeight = rect.height();       
        
        path.moveTo(rect.left + ovalHalfWidth, rect.bottom);
        
        double x = Math.sqrt(Math.pow(ovalHalfWidth, 2)  * (Math.pow(ovalHalfHeight, 2) - Math.pow(adj3, 2)) / Math.pow(ovalHalfHeight, 2));
        int angle = (int)(Math.atan(x / adj3) * 180 / Math.PI);
        s_rect.set(rect.left, rect.top - ovalHalfHeight, rect.left + ovalHalfWidth * 2, rect.top + ovalHalfHeight);
        path.arcTo(s_rect, 90, -angle);
        path.setLastPoint(rect.left + ovalHalfWidth + (float)x, rect.top + adj3);
        path.lineTo(rect.left + ovalHalfWidth + (float)x + adj1 / 2 - adj2 / 2, rect.top + adj3);
        path.lineTo(rect.right - adj2 / 2, rect.top);
        path.lineTo(rect.left + ovalHalfWidth + (float)x + adj1 / 2 + adj2 / 2, rect.top + adj3);
        path.lineTo(rect.left + ovalHalfWidth + (float)x + adj1, rect.top + adj3);
        
        s_rect.set(rect.left + adj1, rect.top - ovalHalfHeight, rect.left + ovalHalfWidth * 2 + adj1, rect.top + ovalHalfHeight);
        path.arcTo(s_rect, 90 - angle, angle);
        path.close(); 
        
        pathList.add(path);
        
        //        
        path = new Path();
        path.moveTo(rect.left, rect.top);
        s_rect.set(rect.left, rect.top - ovalHalfHeight, rect.left + ovalHalfWidth * 2, rect.top + ovalHalfHeight);
        path.arcTo(s_rect, 180, -90);
        path.lineTo(rect.left + ovalHalfWidth + adj1, rect.bottom);
        s_rect.set(rect.left + adj1, rect.top - ovalHalfHeight, rect.left + ovalHalfWidth * 2 + adj1, rect.top + ovalHalfHeight);
        path.arcTo(s_rect, 90, 90);
        path.close();        
        pathList.add(path);
        return pathList;
    }
    
    private static List<Path> getCurvedDownArrowPath(AutoShape shape, Rect rect)
    {
        List<Path> pathList = new ArrayList<Path>(2);
        
        Float[] values = shape.getAdjustData();        
        Path path = new Path();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 3)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.5f);
            adj3 = Math.round(len * 0.25f);
        }
        
        int ovalHalfWidth = (rect.width() - adj2 / 2 - adj1 / 2) / 2;
        int ovalHalfHeight = rect.height();
        
        path.moveTo(rect.left, rect.bottom);
        s_rect.set(rect.left, rect.top, rect.left + ovalHalfWidth * 2, rect.top + ovalHalfHeight * 2);
        path.arcTo(s_rect, 180, 90);
        path.lineTo(rect.left + ovalHalfWidth + adj1, rect.top);
        s_rect.set(rect.left + adj1, rect.top, rect.left + ovalHalfWidth * 2 + adj1, rect.top + ovalHalfHeight * 2);
        path.arcTo(s_rect, 270, -90);
        path.close();        
        pathList.add(path);
        
        path = new Path();
        path.moveTo(rect.left + ovalHalfWidth, rect.top);
        
        double x = Math.sqrt(Math.pow(ovalHalfWidth, 2)  * (Math.pow(ovalHalfHeight, 2) - Math.pow(adj3, 2)) / Math.pow(ovalHalfHeight, 2));
        int angle = (int)(Math.atan(x / adj3) * 180 / Math.PI);
        
        s_rect.set(rect.left, rect.top , rect.left + ovalHalfWidth * 2, rect.top + ovalHalfHeight * 2);
        path.arcTo(s_rect, 270, angle);
        path.setLastPoint(rect.left + ovalHalfWidth + (float)x, rect.bottom - adj3);
        path.lineTo(rect.left + ovalHalfWidth + (float)x + adj1 / 2 - adj2 / 2, rect.bottom - adj3);
        path.lineTo(rect.right - adj2 / 2, rect.bottom);
        path.lineTo(rect.left + ovalHalfWidth + (float)x + adj1 / 2 + adj2 / 2, rect.bottom - adj3);
        path.lineTo(rect.left + ovalHalfWidth + (float)x + adj1, rect.bottom - adj3);
        
        s_rect.set(rect.left + adj1, rect.top, rect.left + ovalHalfWidth * 2 + adj1, rect.top + ovalHalfHeight * 2);
        path.arcTo(s_rect, 270 + angle, -angle);
        path.close();
        
        pathList.add(path);
        
        return pathList;
    }
    
    private static Path getStripedRightArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.height() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        len1 = len2 / 32;
        path.addRect(rect.left, rect.centerY() - adj1, rect.left + len1, rect.centerY() + adj1, Direction.CW);
        
        path.addRect(rect.left + len1 * 2, rect.centerY() - adj1, rect.left + len1 * 4, rect.centerY() + adj1, Direction.CW);
        
        path.moveTo(rect.left + len1 * 5, rect.centerY() - adj1);
        path.lineTo(rect.right - adj2, rect.centerY() - adj1);
        
        //right arrow
        path.lineTo(rect.right - adj2, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj2, rect.bottom);
        
        path.lineTo(rect.right - adj2, rect.centerY() + adj1);        
        path.lineTo(rect.left + len1 * 5, rect.centerY() + adj1);
        path.close();
        
        return  path;
    }
    
    private static Path getNotchedRightArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int len1 = rect.height() / 2;
        int len2 = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 2)
        {
            adj1 = Math.round(len1 * values[0]);
            adj2 = Math.round(len2 * values[1]);
        } 
        else
        {
            adj1 = Math.round(len1 * 0.5f);
            adj2 = Math.round(len2 * 0.5f);
        }
        
        //notch is similar with right arrow
        int adj3 = 2 * adj1 * adj2 / rect.height();
        
        path.moveTo(rect.left, rect.centerY() - adj1);
        path.lineTo(rect.right - adj2, rect.centerY() - adj1);
        path.lineTo(rect.right - adj2, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj2, rect.bottom);
        path.lineTo(rect.right - adj2, rect.centerY() + adj1);
        path.lineTo(rect.left, rect.centerY() + adj1);
        path.lineTo(rect.left + adj3, rect.centerY());
        path.close();
        
        
        return path;
    }
    
    private static Path getHomePlatePath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 1)
        {
            adj1 = Math.round(len * values[0]);
        } 
        else
        {
            adj1 = Math.round(len * 0.5f);
        }
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right - adj1, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj1, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getChevronPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 1)
        {
            adj1 = Math.round(len * values[0]);
        } 
        else
        {
            adj1 = Math.round(len * 0.5f);
        } 
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right - adj1, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj1, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.left + adj1, rect.centerY());
        path.close();
        
        return path;
    }
        
    private static Path getRightArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 4)
        {
            for(int i = 0; i < 4; i++)
            {
                if(values[i] > 1 && i != 2)
                {
                    values[i] = (float)1;
                }
            }
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(rect.width() * values[3]);
            
            if(adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if(adj3 > rect.width())
            {
                adj3 = rect.width();
            }
            
            if(adj4 + adj3 > rect.width())
            {
                adj4 = rect.width() - adj3;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
            adj4 = Math.round(rect.width() * 0.65f);
        } 
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.left + adj4, rect.top);
        
        path.lineTo(rect.left + adj4, rect.centerY() - adj1 / 2);
        path.lineTo(rect.right - adj3, rect.centerY() - adj1 / 2);
        
        path.lineTo(rect.right - adj3, rect.centerY() - adj2);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj3, rect.centerY() + adj2);
        
        path.lineTo(rect.right - adj3, rect.centerY() + adj1 / 2);
        path.lineTo(rect.left + adj4, rect.centerY() + adj1 / 2);
        
        path.lineTo(rect.left + adj4, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        
        path.close();
        
        return path;
    }
    
    private static Path getLeftArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 4)
        {
            for(int i = 0; i < 4; i++)
            {
                if(values[i] > 1 && i != 2)
                {
                    values[i] = (float)1;
                }
            }
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(rect.width() * values[3]);
            
            if(adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if(adj3 > rect.width())
            {
                adj3 = rect.width();
            }
            
            if(adj4 + adj3 > rect.width())
            {
                adj4 = rect.width() - adj3;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
            adj4 = Math.round(rect.width() * 0.65f);
        } 
        
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj3, rect.centerY() - adj2);
        
        path.lineTo(rect.left + adj3, rect.centerY() - adj1 / 2);
        path.lineTo(rect.right - adj4, rect.centerY() - adj1 / 2);
        
        path.lineTo(rect.right - adj4, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.right - adj4, rect.bottom);
        
        path.lineTo(rect.right - adj4, rect.centerY() + adj1 / 2);
        path.lineTo(rect.left + adj3, rect.centerY() + adj1 / 2);
        
        path.lineTo(rect.left + adj3, rect.centerY() + adj2);
        
        path.close();
        
        return path;
    }
    
    private static Path getUpArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 4)
        {
            for(int i = 0; i < 4; i++)
            {
                if(values[i] > 1 && i != 2)
                {
                    values[i] = (float)1;
                }
            }
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(rect.height() * values[3]);
            
            if(adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if(adj3 > rect.height())
            {
                adj3 = rect.width();
            }
            
            if(adj4 + adj3 > rect.height())
            {
                adj4 = rect.height() - adj3;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
            adj4 = Math.round(rect.height() * 0.65f);
        } 
        
        path.moveTo(rect.centerX(), rect.top);
        path.lineTo(rect.centerX() + adj2, rect.top + adj3);
        path.lineTo(rect.centerX() + adj1 / 2, rect.top + adj3);
        path.lineTo(rect.centerX() + adj1 / 2, rect.bottom - adj4);
        path.lineTo(rect.right, rect.bottom - adj4);
        path.lineTo(rect.right, rect.bottom);
        
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.bottom - adj4);
        path.lineTo(rect.centerX() - adj1 / 2, rect.bottom - adj4);
        path.lineTo(rect.centerX() - adj1 / 2, rect.top + adj3);        
        path.lineTo(rect.centerX() - adj2, rect.top + adj3);
        
        path.close();
        
        return path;
    }
    
    private static Path getDownArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 4)
        {
            for(int i = 0; i < 4; i++)
            {
                if(values[i] > 1 && i != 2)
                {
                    values[i] = (float)1;
                }
            }
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(rect.height() * values[3]);
            
            if(adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if(adj3 > rect.height())
            {
                adj3 = rect.width();
            }
            
            if(adj4 + adj3 > rect.height())
            {
                adj4 = rect.height() - adj3;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
            adj4 = Math.round(rect.height() * 0.65f);
        } 
        
        path.moveTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.centerX() - adj2, rect.bottom - adj3);
        path.lineTo(rect.centerX() - adj1 / 2, rect.bottom - adj3);
        path.lineTo(rect.centerX() - adj1 / 2, rect.top + adj4);
        path.lineTo(rect.left, rect.top + adj4);
        path.lineTo(rect.left, rect.top);
        
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.top + adj4);
        path.lineTo(rect.centerX() + adj1 / 2, rect.top + adj4);
        path.lineTo(rect.centerX() + adj1 / 2, rect.bottom - adj3);
        path.lineTo(rect.centerX() + adj2, rect.bottom - adj3);
        
        path.close();
        return path;
    }
    
    private static Path getLeftRightArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 4)
        {
            for(int i = 0; i < 4; i++)
            {
                if(values[i] > 1 && i != 2)
                {
                    values[i] = (float)1;
                }
            }
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(rect.width() * values[3]);
            
            if(  2 * adj3 >= rect.width())
            {
                adj3 = rect.width() / 2;
            }
            
            if(2 * adj3 + adj4 >= rect.width())
            {
                adj4 = rect.width() - 2 * adj3;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.25f);
            adj2 = Math.round(len * 0.25f);
            adj3 = Math.round(len * 0.25f);
            adj4 = Math.round(rect.width() * 0.5f);
        }
       
        //left arrow
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj3, rect.centerY() - adj2);
        
        path.lineTo(rect.left + adj3, rect.centerY() - adj1 / 2);
        path.lineTo(rect.centerX() - adj4 / 2, rect.centerY() - adj1 / 2);
        path.lineTo(rect.centerX() - adj4 / 2, rect.top);
        
        path.lineTo(rect.centerX() + adj4 / 2, rect.top);
        path.lineTo(rect.centerX() + adj4 / 2, rect.centerY() - adj1 / 2);
        path.lineTo(rect.right - adj3, rect.centerY() - adj1 / 2);
        
        //right arrow
        path.lineTo(rect.right - adj3, rect.centerY() - adj2);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj3, rect.centerY() + adj2);
        
        path.lineTo(rect.right - adj3, rect.centerY() + adj1 / 2);
        path.lineTo(rect.centerX() + adj4 / 2, rect.centerY() + adj1 / 2);
        path.lineTo(rect.centerX() + adj4 / 2, rect.bottom);
        path.lineTo(rect.centerX() - adj4 / 2, rect.bottom);
        path.lineTo(rect.centerX() - adj4 / 2, rect.centerY() + adj1 / 2);
        
        path.lineTo(rect.left + adj3, rect.centerY() + adj1 / 2);
        path.lineTo(rect.left + adj3, rect.centerY() + adj2);
        path.close();
        
        return path;
    }
    
    private static Path getQuadArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int len = Math.min(rect.width(),rect.height());
        if(values != null && values.length == 4)
        {
            for(int i = 0; i < 4; i++)
            {
                if(values[i] > 1 && i != 2)
                {
                    values[i] = (float)1;
                }
            }
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(len * values[1]);
            adj3 = Math.round(len * values[2]);
            adj4 = Math.round(len * values[3]);
            
            if( adj1 > adj2 * 2)
            {
                adj1 = adj2 * 2;
            }
            
            if( adj4 > adj2 * 2)
            {
                adj4 = adj2 * 2;
            }
            
            if(adj2 * 2 >= len)
            {
                adj2 = len / 2;
                adj3 = 0;
            }
            
            if(adj3 * 2 >= len)
            {
                adj3 = len / 2;
            }
            
            if(adj2 + adj3 > len / 2)
            {
                adj3 = len / 2 - adj2;
            }
        } 
        else
        {
            adj1 = Math.round(len * 0.18515f);
            adj2 = Math.round(len * 0.18515f);
            adj3 = Math.round(len * 0.18515f);
            adj4 = Math.round(len * 0.48f);
        }
        
        //left arrow
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj3, rect.centerY() - adj2);
        
        path.lineTo(rect.left + adj3, rect.centerY() - adj1 / 2);
        path.lineTo(rect.centerX() - adj4 / 2, rect.centerY() - adj1 / 2);
        path.lineTo(rect.centerX() - adj4 / 2, rect.centerY() - adj4 / 2);
        path.lineTo(rect.centerX() - adj1 / 2, rect.centerY() - adj4 / 2);
        path.lineTo(rect.centerX() - adj1 / 2, rect.top + adj3);
        
        //up arrow
        path.lineTo(rect.centerX() - adj2, rect.top + adj3);
        path.lineTo(rect.centerX(), rect.top);
        path.lineTo(rect.centerX() + adj2, rect.top + adj3);
        
        path.lineTo(rect.centerX() + adj1 / 2, rect.top + adj3);
        path.lineTo(rect.centerX() + adj1 / 2, rect.centerY() - adj4 / 2);
        path.lineTo(rect.centerX() + adj4 / 2, rect.centerY() - adj4 / 2);
        path.lineTo(rect.centerX() + adj4 / 2, rect.centerY() - adj1 / 2);
        path.lineTo(rect.right - adj3, rect.centerY() - adj1 / 2);
        
        //right arrow
        path.lineTo(rect.right - adj3, rect.centerY() - adj2);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj3, rect.centerY() + adj2);
       
        path.lineTo(rect.right - adj3, rect.centerY()  + adj1 / 2);
        path.lineTo(rect.centerX() + adj4 / 2, rect.centerY() + adj1 / 2);
        path.lineTo(rect.centerX() + adj4 / 2, rect.centerY() + adj4 / 2);
        path.lineTo(rect.centerX() + adj1 / 2, rect.centerY() + adj4 / 2);
        path.lineTo(rect.centerX() + adj1 / 2, rect.bottom - adj3);
            
        //down arrow
        path.lineTo(rect.centerX() + adj2, rect.bottom - adj3);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.centerX() - adj2, rect.bottom - adj3);
        
        path.lineTo(rect.centerX() - adj1 / 2, rect.bottom - adj3);
        path.lineTo(rect.centerX() - adj1 / 2, rect.centerY() + adj4 / 2);
        path.lineTo(rect.centerX() - adj4 / 2, rect.centerY() + adj4 / 2);
        path.lineTo(rect.centerX() - adj4 / 2, rect.centerY() + adj1 / 2);
        path.lineTo(rect.left + adj3, rect.centerY() + adj1 / 2);
        
        
        path.lineTo(rect.left + adj3, rect.centerY() + adj2);
        path.close();
        return path;
    }
    
    private static Path getCircularArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        int adj5 = 0;
        int len = 100;
        if(values != null && values.length == 5)
        {
            adj1 = Math.round(len * values[0]);
            adj2 = Math.round(values[1] * TODEGREE);
            adj3 = Math.round(values[2] * TODEGREE);
            adj4 = Math.round( values[3] * TODEGREE);
            adj5 = Math.round(len * values[4]);
        } 
        else
        {
            adj1 = Math.round(len * 0.125f);
            adj2 = 20;
            adj3 = 340;
            adj4 = 180;            
            adj5 = Math.round(len * 0.125F);
        }
        
        //radius of circal between outer and inner
        int insideRadius = len / 2 - adj5;
        
        //outer arc line
        //path.moveTo((insideRadius + adj1 / 2) * (float)Math.cos(adj4 *  Math.PI / 180f), (insideRadius + adj1 / 2) * (float)Math.sin(adj4 *  Math.PI / 180f));
        
        //point of the arrow tail line
        double y = insideRadius * Math.sin(adj3 *  Math.PI / 180f);
        double x = insideRadius * Math.cos(adj3 *  Math.PI / 180f);
        
        
        //arrow tail line  y = kx + b  
        double k = Math.tan((adj3 + adj2) * Math.PI / 180f);
        double b = y - k * x;
        
        //The distance between arrow tail center and tail endpoint
        double offX1 = Math.sqrt(Math.pow(adj5, 2) / (Math.pow(k, 2) + 1));
        //The distance between arrow tail center and intersetion of arrow tail and circle
        double offX2 = Math.sqrt(Math.pow(adj1 / 2, 2) / (Math.pow(k, 2) + 1));
        
        if(adj3 > 90 && adj3 < 270)
        {
            offX1 = - offX1;
            offX2 = - offX2;
        }
        
        double outerDegree = getAngle( x + offX2, k * (x + offX2) + b);
        double innerDegree = getAngle(x - offX2, k * (x - offX2) + b);       
        
        
        s_rect.set(adj5 - adj1 / 2 -  len / 2, adj5 - adj1 / 2 -  len / 2, len / 2 - adj5 + adj1 / 2, len / 2 - adj5 + adj1 / 2);
        path.arcTo(s_rect, adj4, (float)(outerDegree - adj4 + 360) % 360);
       
        path.lineTo((float)(x + offX1), (float)( k * (x + offX1) + b));
        path.lineTo((float)(insideRadius * Math.cos((adj3 + adj2) * Math.PI / 180f)),
            (float)(insideRadius * Math.sin((adj3 + adj2) * Math.PI / 180f)));
        path.lineTo((float)(x - offX1), (float)( k * (x - offX1) + b));  

        s_rect.set(adj5 + adj1 / 2 - len / 2, adj5 + adj1 / 2 - len / 2, len / 2 - adj5 - adj1 / 2, len / 2 - adj5 - adj1 / 2);
        path.arcTo(s_rect, (float)innerDegree, (float)(adj4 - innerDegree - 360) % 360);
        
        path.close();
        
        Matrix m = new Matrix();
        m.postScale(rect.width() / 100f, rect.height() / 100f);
        path.transform(m);
        
        path.offset(rect.centerX(), rect.centerY());
        
        return path;
    }
    
    private static double getAngle(double x, double y)
    {
        double angle = Math.acos(x / Math.sqrt(x * x + y * y)) * 180 / Math.PI;
        
        if(y < 0 )
        {
            angle = 360 - angle;
        }        
        
        return angle;
    }
}
