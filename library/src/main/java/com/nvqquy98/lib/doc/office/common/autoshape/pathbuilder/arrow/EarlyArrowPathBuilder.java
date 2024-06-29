/*
 * 文件名称:          EarlyArrowPathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:42:46
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
public class EarlyArrowPathBuilder extends ArrowPathBuilder
{
    private static final float TODEGREE = 18000000f / 54620000f;
    
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
                
            case ShapeTypes.UpDownArrowCallout:
                return getUpDownArrowCalloutPath(shape, rect);
                
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
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {                
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.75f);
            }            
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.75f);
            adj2 = Math.round(rect.height() * 0.25f);
        }
        
        path.moveTo(rect.left, rect.top + adj2);
        path.lineTo(rect.left + adj1, rect.top + adj2);
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.left + adj1, rect.bottom);
        path.lineTo(rect.left + adj1, rect.bottom - adj2);
        path.lineTo(rect.left, rect.bottom - adj2);
        path.close();
        return path;
    }
    
    private static Path getLeftArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        int adj1 = 0;
        int adj2 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {                
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.25f);
            }            
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.25f);
            adj2 = Math.round(rect.height() * 0.25f);
        }
        
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.left + adj1, rect.top + adj2);
        path.lineTo(rect.right, rect.top + adj2);
        path.lineTo(rect.right, rect.bottom - adj2);
        path.lineTo(rect.left + adj1, rect.bottom - adj2);
        path.lineTo(rect.left + adj1, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        int adj1 = 0;
        int adj2 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {                
                adj1 = Math.round(rect.height() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.height() * 0.25f);
            }            
            
            if(values.length >= 2 && values[1] != null)
            {                    
                adj2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.25f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.height() * 0.25f);
            adj2 = Math.round(rect.width() * 0.25f);
        }
        
        path.moveTo(rect.centerX(), rect.top);
        path.lineTo(rect.right, rect.top + adj1);
        path.lineTo(rect.right - adj2, rect.top + adj1);
        path.lineTo(rect.right - adj2, rect.bottom);
        path.lineTo(rect.left + adj2, rect.bottom);
        path.lineTo(rect.left + adj2, rect.top + adj1);
        path.lineTo(rect.left, rect.top + adj1);
        path.close();
        
        return path;
    }
    
    private static Path getDownArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        int adj1 = 0;
        int adj2 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {                
                adj1 = Math.round(rect.height() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.height() * 0.75f);
            }            
            
            if(values.length >= 2 && values[1] != null)
            {                    
                adj2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.25f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.height() * 0.75f);
            adj2 = Math.round(rect.width() * 0.25f);
        }
        
        path.moveTo(rect.left + adj2, rect.top);
        path.lineTo(rect.right - adj2, rect.top);
        path.lineTo(rect.right - adj2, rect.top + adj1);
        path.lineTo(rect.right, rect.top + adj1);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.left, rect.top + adj1);
        path.lineTo(rect.left + adj2, rect.top + adj1);
        path.close();
        
        return path;
    }
    
    private static Path getLeftRightArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        int adj1 = 0;
        int adj2 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {                
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.2f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height()* values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
            
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.2f);
            adj2 = Math.round(rect.height() * 0.25f);
        }
        
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.left + adj1, rect.top + adj2);
        path.lineTo(rect.right - adj1, rect.top + adj2);
        path.lineTo(rect.right - adj1, rect.top);
        
        path.lineTo(rect.right, rect.centerY());
        
        path.lineTo(rect.right - adj1, rect.bottom);
        path.lineTo(rect.right - adj1, rect.bottom - adj2);
        path.lineTo(rect.left + adj1, rect.bottom - adj2);
        path.lineTo(rect.left + adj1, rect.bottom);
        
        path.close();
        return path;
    }
    
    private static Path getUpDownArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        int adj1 = 0;
        int adj2 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {                
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.25f);
            }
            
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height() * values[1]); 
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
            
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.25f);
            adj2 = Math.round(rect.height() * 0.25f);
        }
        
        path.moveTo(rect.centerX(), rect.top);
        path.lineTo(rect.right, rect.top + adj2);
        path.lineTo(rect.right - adj1, rect.top + adj2);
        path.lineTo(rect.right - adj1, rect.bottom - adj2);
        path.lineTo(rect.right, rect.bottom - adj2);
        
        path.lineTo(rect.centerX(), rect.bottom);
        
        path.lineTo(rect.left, rect.bottom - adj2);
        path.lineTo(rect.left + adj1, rect.bottom - adj2);            
        path.lineTo(rect.left + adj1, rect.top + adj2);
        path.lineTo(rect.left, rect.top + adj2);
        path.close();
        
        return path;
    }
    
    private static Path getQuadArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        int adjLR1 = 0;
        int adjLR2 = 0;
        int adjLR3 = 0;
        
        int adjUD1 = 0;
        int adjUD2 = 0;
        int adjUD3 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adjLR1 = Math.round(rect.height() * values[0]);
                adjUD1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adjLR1 = Math.round(rect.height() * 0.3f);
                adjUD1 = Math.round(rect.width() * 0.3f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adjLR2 = Math.round(rect.height() * values[1]);
                adjUD2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                adjLR2 = Math.round(rect.height() * 0.4f);
                adjUD2 = Math.round(rect.width() * 0.4f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adjLR3 = Math.round(rect.width() * values[2]);
                adjUD3 = Math.round(rect.height() * values[2]);
            }
            else
            {
                adjLR3 = Math.round(rect.width() * 0.2f);
                adjUD3 = Math.round(rect.height() * 0.2f);
            }
        } 
        else
        {
            adjLR1 = Math.round(rect.height() * 0.3f);
            adjLR2 = Math.round(rect.height() * 0.4f);
            adjLR3 = Math.round(rect.width() * 0.2f);
            
            adjUD1 = Math.round(rect.width() * 0.3f);
            adjUD2 = Math.round(rect.width() * 0.4f);
            adjUD3 = Math.round(rect.height() * 0.2f);
        }
        
        //left
        path.moveTo(rect.left + adjUD2, rect.bottom - adjLR2);
        path.lineTo(rect.left + adjLR3, rect.bottom - adjLR2);
        path.lineTo(rect.left + adjLR3, rect.bottom - adjLR1);
        path.lineTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adjLR3, rect.top + adjLR1);
        path.lineTo(rect.left + adjLR3, rect.top + adjLR2);
        
        //up
        path.lineTo(rect.left + adjUD2, rect.top + adjLR2);
        path.lineTo(rect.left + adjUD2, rect.top + adjUD3);
        path.lineTo(rect.left + adjUD1, rect.top + adjUD3);
        path.lineTo(rect.centerX(), rect.top);
        path.lineTo(rect.right - adjUD1, rect.top + adjUD3);
        path.lineTo(rect.right - adjUD2, rect.top + adjUD3);
        
        //right
        path.lineTo(rect.right - adjUD2, rect.top + adjLR2);
        path.lineTo(rect.right - adjLR3, rect.top + adjLR2);
        path.lineTo(rect.right - adjLR3, rect.top + adjLR1);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adjLR3, rect.bottom - adjLR1);
        path.lineTo(rect.right - adjLR3, rect.bottom - adjLR2);
        
        //down
        path.lineTo(rect.right - adjUD2, rect.bottom - adjLR2);
        path.lineTo(rect.right - adjUD2, rect.bottom - adjUD3);
        path.lineTo(rect.right - adjUD1, rect.bottom - adjUD3);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.left + adjUD1, rect.bottom - adjUD3);
        path.lineTo(rect.left + adjUD2, rect.bottom - adjUD3);
        
        path.close();
        
        return path;
    }
    
    private static Path getLeftRightUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adjLR1 = 0;
        int adjLR2 = 0;
        int adjLR3 = 0;
        
        int adjUD1 = 0;
        int adjUD2 = 0;
        int adjUD3 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adjLR1 = Math.round(rect.height() * (0.5f - values[0]) * 10 / 7);
                adjUD1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adjLR1 = Math.round(rect.height() * 0.2f * 10 / 7);
                adjUD1 = Math.round(rect.width() * 0.3f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adjLR2 = Math.round(rect.height() * (0.5f - values[1]) * 10 / 7);
                adjUD2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                adjLR2 = Math.round(rect.height() * 0.1f * 10 / 7);
                adjUD2 = Math.round(rect.width() * 0.4f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adjLR3 = Math.round(rect.width() * values[2] * 0.7f);
                adjUD3 = Math.round(rect.height() * values[2]);
            }
            else
            {
                adjLR3 = Math.round(rect.width() * 0.2f * 0.7f);
                adjUD3 = Math.round(rect.height() * 0.2f);
            }
        } 
        else
        {
            adjLR1 = Math.round(rect.height() * 0.2f * 10 / 7);
            adjLR2 = Math.round(rect.height() * 0.1f * 10 / 7);
            adjLR3 = Math.round(rect.width() *  0.3f * 0.7f);
            
            adjUD1 = Math.round(rect.width() * 0.3f);
            adjUD2 = Math.round(rect.width() * 0.4f);
            adjUD3 = Math.round(rect.height() * 0.2f);
        }
        
        //left
        path.moveTo(rect.left + adjLR3, rect.bottom - adjLR1 + adjLR2);
        path.lineTo(rect.left + adjLR3, rect.bottom);
        path.lineTo(rect.left, rect.bottom - adjLR1);
        path.lineTo(rect.left + adjLR3, rect.bottom - adjLR1 * 2);
        path.lineTo(rect.left + adjLR3, rect.bottom - adjLR1 - adjLR2);
        
        //up
        path.lineTo(rect.left + adjUD2, rect.bottom - adjLR1 - adjLR2);
        path.lineTo(rect.left + adjUD2, rect.top + adjUD3);
        path.lineTo(rect.left + adjUD1, rect.top + adjUD3);
        path.lineTo(rect.centerX(), rect.top);
        path.lineTo(rect.right - adjUD1, rect.top + adjUD3);
        path.lineTo(rect.right - adjUD2, rect.top + adjUD3);
        
        //right
        path.lineTo(rect.right - adjUD2, rect.bottom - adjLR1 - adjLR2);
        path.lineTo(rect.right - adjLR3, rect.bottom - adjLR1 - adjLR2);
        path.lineTo(rect.right - adjLR3, rect.bottom - adjLR1 * 2);
        path.lineTo(rect.right, rect.bottom - adjLR1);
        path.lineTo(rect.right - adjLR3, rect.bottom);
        path.lineTo(rect.right - adjLR3, rect.bottom - adjLR1 + adjLR2);
        
        path.close();
        
        return path;
    }

    private static Path getBentArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        if(values != null&& values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.7f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.125f);
            }
            
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.7f);
            adj2 = Math.round(rect.height() * 0.125f);
        }
        
        float arrowHeight = rect.height() * 0.57f;
        
        path.moveTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + arrowHeight);
        
        s_rect.set(rect.left, rect.top + adj2, rect.left + rect.width() * 1.04f, rect.top + arrowHeight + (arrowHeight - adj2));
        path.arcTo(s_rect, 180, 90);
        
        path.lineTo(rect.left + adj1, rect.top + adj2);
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right, rect.top + arrowHeight / 2);
        path.lineTo(rect.left + adj1, rect.top + arrowHeight);
        path.lineTo(rect.left + adj1, rect.top + arrowHeight / 2 + (arrowHeight - adj2 * 2) / 2);
        
        float smallOvalAdj = (arrowHeight - adj2 * 2) / (float)rect.height();
        s_rect.set(rect.left + rect.width() * smallOvalAdj, rect.top + arrowHeight / 2 + (arrowHeight - adj2 * 2) / 2,
            rect.left + rect.width() * (0.57f + 0.57f - smallOvalAdj), rect.top + arrowHeight + arrowHeight - (arrowHeight / 2 + (arrowHeight - adj2 * 2) / 2));
        path.arcTo(s_rect, 270, -90);
        
        path.lineTo(rect.left + rect.width() * smallOvalAdj, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getUturnArrowPath(AutoShape shape, Rect rect)
    {
        int width = rect.width();
        int height = rect.height();
        
        path.moveTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + height * 0.38f);
        
        s_rect.set(rect.left, rect.top, rect.right - width * (0.28f - 0.14f), rect.top + height * 0.76f);
        path.arcTo(s_rect, 180, 180);
        
        path.lineTo(rect.right, rect.top + height * 0.38f);
        path.lineTo(rect.right - width * 0.28f, rect.top + height * 0.66f);
        path.lineTo(rect.right - width * 0.56f, rect.top + height * 0.38f);
        path.lineTo(rect.right - width * (0.28f + 0.14f), rect.top + height * 0.38f);
        
        s_rect.set(rect.left + width * 0.28f, rect.top + height * 0.28f, rect.right - width * (0.28f + 0.14f), rect.top + height * (0.38f + 0.38f - 0.28f));
        path.arcTo(s_rect, 0, -180);
        
        path.lineTo(rect.left + width * 0.28f, rect.bottom);
        
        path.close();
        
        return path;
    }
    
    private static Path getLeftUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        float upAdj1  = 0;
        float upAdj2 = 0;
        float upAdj3 = 0;
        
        float downAdj1 = 0;
        float downAdj2 = 0;
        float downAdj3 = 0;
        
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                upAdj1 = rect.width() * values[0];
                downAdj1 = rect.height() * values[0];
            }
            else
            {
                upAdj1 = Math.round(rect.width() * 0.43f);
                downAdj1 = rect.height() * 0.43f;
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                upAdj2 = rect.width() * values[1];
                downAdj2 = rect.height() * values[1];
            }
            else
            {
                upAdj2 = Math.round(rect.width() * 0.86f);
                downAdj2 = rect.height() * 0.86f;
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                upAdj3 = rect.height() * values[2];
                downAdj3 = rect.width() * values[2];
            }
            else
            {
                upAdj3 = Math.round(rect.height() * 0.28f);
                downAdj3 = rect.width() * 0.28f;
            }
        } 
        else
        {
            upAdj1 = Math.round(rect.width() * 0.43f);
            upAdj2 = Math.round(rect.width() * 0.86f);
            upAdj3 = Math.round(rect.height() * 0.28f);
            
            downAdj1 = rect.height() * 0.43f;
            downAdj2 = rect.height() * 0.86f;
            downAdj3 = rect.width() * 0.28f;
        }
       
        //left
        float arrowHeaderWidth = (rect.height() - downAdj1);
        float arrowTailWidth = arrowHeaderWidth - (rect.height() - downAdj2) * 2;
        path.moveTo(rect.left + downAdj3, rect.top + downAdj2);
        path.lineTo(rect.left + downAdj3, rect.bottom);
        path.lineTo(rect.left, rect.bottom - arrowHeaderWidth / 2);
        path.lineTo(rect.left + downAdj3, rect.top + downAdj1);
        path.lineTo(rect.left + downAdj3, rect.top + downAdj2 - arrowTailWidth);
        
        //up
        arrowHeaderWidth = (rect.width() - upAdj1);
        path.lineTo(rect.left + upAdj2 - (arrowHeaderWidth - (rect.width() - upAdj2) * 2), rect.top + downAdj2 - arrowTailWidth);
        arrowTailWidth = arrowHeaderWidth - (rect.width() - upAdj2) * 2;
        
        path.lineTo(rect.left + upAdj2 - arrowTailWidth, rect.top + upAdj3);
        path.lineTo(rect.left + upAdj1, rect.top + upAdj3);
        path.lineTo(rect.right - arrowHeaderWidth / 2, rect.top);
        path.lineTo(rect.right, rect.top + upAdj3);
        path.lineTo(rect.left + upAdj2 , rect.top + upAdj3);        
        path.lineTo(rect.left + upAdj2 , rect.top + downAdj2);
        path.close();
        
        return path;
    }    
    
    private static Path getBentUpArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        float adj1  = 0;
        float adj2 = 0;
        float adj3 = 0;
        
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = rect.width() * values[0];
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.43f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = rect.width() * values[1];
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.86f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = rect.height() * values[2];
            }
            else
            {
                adj3 = Math.round(rect.height() * 0.28f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.43f);
            adj2 = Math.round(rect.width() * 0.86f);
            adj3 = Math.round(rect.height() * 0.28f);
        }
       
        //up
        float arrowHeaderWidth = (rect.width() - adj1);
        float arrowTailWidth = arrowHeaderWidth - (rect.width() - adj2) * 2;
        path.moveTo(rect.left + adj2 - arrowTailWidth, rect.top + adj3);
        path.lineTo(rect.left + adj1, rect.top + adj3);
        path.lineTo(rect.right - arrowHeaderWidth / 2, rect.top);
        path.lineTo(rect.right, rect.top + adj3);        
        path.lineTo(rect.left + adj2, rect.top + adj3);
        
        path.lineTo(rect.left + adj2, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        
        arrowTailWidth = rect.height() * arrowTailWidth / rect.width();
        path.lineTo(rect.left, rect.bottom - arrowTailWidth);
        path.lineTo(rect.left + adj2 - (arrowHeaderWidth - (rect.width() - adj2) * 2), rect.bottom - arrowTailWidth);
        
        path.close();
        
        return path;
    }
    
    private static List<Path> getCurvedRightArrowPath(AutoShape shape, Rect rect)
    {
        List<Path> pathList = new ArrayList<Path>(2);
        
        Float[] values = shape.getAdjustData();        
        Path path = new Path();
        
        float adj1  = 0;
        float adj2 = 0;
        float adj3 = 0;        
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = rect.height() * values[0];
            }
            else
            {
                adj1 = Math.round(rect.height() * 0.6f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = rect.height() * values[1];
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.9f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = rect.width() * values[2];
            }
            else
            {
                adj3 = Math.round(rect.width() * 0.66667f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.height() * 0.6f);
            adj2 = Math.round(rect.height() * 0.9f);
            adj3 = Math.round(rect.width() * 0.66667f);
        }
        
        float arrowHeight = rect.height() * 0.4f;
        float d2 = rect.height() - adj1;
        float d1 = arrowHeight - (arrowHeight - (adj2 - adj1)) * 2;        
        if(d1 < 0)
        {
            d1 = 0;
        }
        
        adj1 = d1;
        adj2 = d2;
        adj3 = rect.width() - adj3;
        
        float ovalWidth = 2 * rect.width();
        float ovalHeight = (rect.bottom - adj2 / 2 - adj1 / 2 - rect.top);
        
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
        
        float adj1  = 0;
        float adj2 = 0;
        float adj3 = 0;        
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = rect.height() * values[0];
            }
            else
            {
                adj1 = Math.round(rect.height() * 0.6f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = rect.height() * values[1];
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.9f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = rect.width() * values[2];
            }
            else
            {
                adj3 = Math.round(rect.width() * 0.66667f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.height() * 0.6f);
            adj2 = Math.round(rect.height() * 0.9f);
            adj3 = Math.round(rect.width() * 0.66667f);
        }
        
        float arrowHeight = rect.height() * 0.4f;
        float d2 = rect.height() - adj1;
        float d1 = arrowHeight - (arrowHeight - (adj2 - adj1)) * 2;        
        if(d1 < 0)
        {
            d1 = 0;
        }
        
        adj1 = d1;
        adj2 = d2;
        
        float ovalWidth = 2 * rect.width();
        float ovalHeight = (rect.bottom - adj2 / 2 - adj1 / 2 - rect.top);
        
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
        
        float adj1  = 0;
        float adj2 = 0;
        float adj3 = 0;        
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = rect.width() * values[0];
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.6f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = rect.width() * values[1];
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.9f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = rect.height() * values[2];
            }
            else
            {
                adj3 = Math.round(rect.height() * 0.66667f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.6f);
            adj2 = Math.round(rect.width() * 0.9f);
            adj3 = Math.round(rect.height() * 0.33333f);
        }
        
        float arrowWidth = rect.width() * 0.4f;
        float d2 = rect.width() - adj1;
        float d1 = arrowWidth - (arrowWidth - (adj2 - adj1)) * 2;        
        if(d1 < 0)
        {
            d1 = 0;
        }
        
        adj1 = d1;
        adj2 = d2;
        
        float ovalHalfWidth = (rect.width() - adj2 / 2 - adj1 / 2) / 2;
        float ovalHalfHeight = rect.height();        
        
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
        
        float adj1  = 0;
        float adj2 = 0;
        float adj3 = 0;        
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = rect.width() * values[0];
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.6f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = rect.width() * values[1];
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.9f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = rect.height() * values[2];
            }
            else
            {
                adj3 = Math.round(rect.height() * 0.66667f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.6f);
            adj2 = Math.round(rect.width() * 0.9f);
            adj3 = Math.round(rect.height() * 0.666667f);
        }
        
        float arrowWidth = rect.width() * 0.4f;
        float d2 = rect.width() - adj1;
        float d1 = arrowWidth - (arrowWidth - (adj2 - adj1)) * 2;        
        if(d1 < 0)
        {
            d1 = 0;
        }
        
        adj1 = d1;
        adj2 = d2;
        adj3 = rect.height() - adj3;
        
        float ovalHalfWidth = (rect.width() - adj2 / 2 - adj1 / 2) / 2;
        float ovalHalfHeight = rect.height();
        path.moveTo(rect.left, rect.bottom);
        s_rect.set(rect.left, rect.top, rect.left + ovalHalfWidth * 2, rect.top + ovalHalfHeight * 2);
        path.arcTo(s_rect, 180, 90);
        path.lineTo(rect.left + ovalHalfWidth + adj1, rect.top);
        s_rect.set(rect.left + adj1, rect.top, rect.left + ovalHalfWidth * 2 + adj1, rect.top + ovalHalfHeight * 2);
        path.arcTo(s_rect, 270, -90);
        path.close();        
        pathList.add(path);
        
        //
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
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {                
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.75f);
            }
            
            if(values.length >= 2 && values[1]!= null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.75f);
            adj2 = Math.round(rect.height() * 0.25f);
        }
        
        float len = rect.width() * 0.03f;
        
        path.addRect(rect.left, rect.top + adj2, rect.left + len, rect.bottom - adj2, Direction.CW);
        
        path.addRect(rect.left + len * 2, rect.top + adj2, rect.left + len * 4, rect.bottom - adj2, Direction.CW);
        
        path.moveTo(rect.left + len * 5, rect.top + adj2);
        path.lineTo(rect.left + adj1, rect.top + adj2);
        
        //right arrow
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.left + adj1, rect.bottom);
        
        path.lineTo(rect.left + adj1, rect.bottom - adj2);
        path.lineTo(rect.left + len * 5, rect.bottom - adj2);
        path.close();
        
        return  path;
    }
    
    private static Path getNotchedRightArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.75f);
            }
            
            if(values.length >= 2 && values[1]!= null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.75f);
            adj2 = Math.round(rect.height() * 0.25f);
        }
        
        path.moveTo(rect.left, rect.bottom - adj2);
        path.lineTo(rect.left + (rect.height() - adj2 * 2) * (rect.width() - adj1) / (float)rect.height(), rect.centerY());
        path.lineTo(rect.left, rect.top + adj2);
        
        path.lineTo(rect.left + adj1, rect.top + adj2);
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.left + adj1, rect.bottom);
        path.lineTo(rect.left + adj1, rect.bottom - adj2);
        path.close();
        
        return path;
    }
    
    private static Path getHomePlatePath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        if(values != null && values.length == 1 && values[0] != null)
        {
            adj1 = Math.round(rect.width() * values[0]);
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.75f);
        }
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.left + adj1, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getChevronPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        if(values != null && values.length == 1 && values[0] != null)
        {
            
            adj1 = Math.round(rect.width() * values[0]);
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.75f);
        }
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.left + adj1, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.left + rect.width() - adj1, rect.centerY());
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
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.67f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = Math.round(rect.width() * values[2]);
            }
            else
            {
                adj3 = Math.round(rect.width() * 0.83f);
            }
            
            if(values.length >= 4 && values[3] != null)
            {
                adj4 = Math.round(rect.height() * values[3]);
            }
            else
            {
                adj4 = Math.round(rect.height() * 0.375f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.67f);
            adj2 = Math.round(rect.height() * 0.25f);
            adj3 = Math.round(rect.width() * 0.83f);
            adj4 = Math.round(rect.height() * 0.375f);
        } 
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.left + adj1, rect.top + adj4);
        path.lineTo(rect.left + adj3, rect.top + adj4);
        path.lineTo(rect.left + adj3, rect.top + adj2);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.left + adj3, rect.bottom - adj2);
        path.lineTo(rect.left + adj3, rect.bottom - adj4);
        path.lineTo(rect.left + adj1, rect.bottom - adj4);
        path.lineTo(rect.left + adj1, rect.bottom);
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
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.33f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = Math.round(rect.width() * values[2]);
            }
            else
            {
                adj3 = Math.round(rect.width() * 0.17f);
            }
            
            if(values.length >= 4 && values[3] != null)
            {
                adj4 = Math.round(rect.height() * values[3]);
            }
            else
            {
                adj4 = Math.round(rect.height() * 0.375f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.33f);
            adj2 = Math.round(rect.height() * 0.25f);
            adj3 = Math.round(rect.width() * 0.17f);
            adj4 = Math.round(rect.height() * 0.375f);
        } 
        
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj3, rect.top + adj2);
        path.lineTo(rect.left + adj3, rect.top + adj4);
        path.lineTo(rect.left + adj1, rect.top + adj4);
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left + adj1, rect.bottom);
        path.lineTo(rect.left + adj1, rect.bottom - adj4);
        path.lineTo(rect.left + adj3, rect.bottom - adj4);
        path.lineTo(rect.left + adj3, rect.bottom - adj2);
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
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.height() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.height() * 0.33f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.25f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = Math.round(rect.height() * values[2]);
            }
            else
            {
                adj3 = Math.round(rect.height() * 0.17f);
            }
            
            if(values.length >= 4 && values[3] != null)
            {
                adj4 = Math.round(rect.width() * values[3]);
            }
            else
            {
                adj4 = Math.round(rect.width() * 0.375f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.height() * 0.33f);
            adj2 = Math.round(rect.width() * 0.25f);
            adj3 = Math.round(rect.height() * 0.17f);
            adj4 = Math.round(rect.width() * 0.375f);
        } 
        
        path.moveTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + adj1);
        path.lineTo(rect.left + adj4, rect.top + adj1);
        path.lineTo(rect.left + adj4, rect.top + adj3);
        path.lineTo(rect.left + adj2, rect.top + adj3);
        path.lineTo(rect.centerX(), rect.top);
        path.lineTo(rect.right - adj2, rect.top + adj3);
        path.lineTo(rect.right - adj4, rect.top + adj3);
        path.lineTo(rect.right -  adj4, rect.top + adj1);
        path.lineTo(rect.right, rect.top + adj1);
        path.lineTo(rect.right, rect.bottom);
        
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
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.height() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.height() * 0.67f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.25f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = Math.round(rect.height() * values[2]);
            }
            else
            {
                adj3 = Math.round(rect.height() * 0.83f);
            }
            
            if(values.length >= 4 && values[3] != null)
            {
                adj4 = Math.round(rect.width() * values[3]);
            }
            else
            {
                adj4 = Math.round(rect.width() * 0.375f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.height() * 0.67f);
            adj2 = Math.round(rect.width() * 0.25f);
            adj3 = Math.round(rect.height() * 0.83f);
            adj4 = Math.round(rect.width() * 0.375f);
        } 
        
        path.moveTo(rect.left, rect.top + adj1);
        path.lineTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.top + adj1);
        path.lineTo(rect.right - adj4, rect.top + adj1);
        path.lineTo(rect.right - adj4, rect.top + adj3);
        path.lineTo(rect.right - adj2, rect.top + adj3);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.left + adj2, rect.top + adj3);
        path.lineTo(rect.left + adj4, rect.top + adj3);
        path.lineTo(rect.left + adj4, rect.top + adj1);
        
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
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.width() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.width() * 0.35f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.height() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.height() * 0.25f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = Math.round(rect.width() * values[2]);
            }
            else
            {
                adj3 = Math.round(rect.width() * 0.13f);
            }
            
            if(values.length >= 4 && values[3] != null)
            {
                adj4 = Math.round(rect.height() * values[3]);
            }
            else
            {
                adj4 = Math.round(rect.height() * 0.375f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.width() * 0.35f);
            adj2 = Math.round(rect.height() * 0.25f);
            adj3 = Math.round(rect.width() * 0.13f);
            adj4 = Math.round(rect.height() * 0.375f);
        }
        
        //left arrow
        path.moveTo(rect.left + adj1, rect.bottom - adj4);
        path.lineTo(rect.left + adj3, rect.bottom - adj4);
        path.lineTo(rect.left + adj3, rect.bottom - adj2);
        path.lineTo(rect.left, rect.centerY());
        path.lineTo(rect.left + adj3, rect.top + adj2);
        path.lineTo(rect.left + adj3, rect.top + adj4);
        path.lineTo(rect.left + adj1, rect.top + adj4);
        
        path.lineTo(rect.left + adj1, rect.top);
        path.lineTo(rect.right - adj1, rect.top);
        
        //right arrow
        path.lineTo(rect.right - adj1, rect.top + adj4);
        path.lineTo(rect.right - adj3, rect.top + adj4);
        path.lineTo(rect.right - adj3, rect.top + adj2);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - adj3, rect.bottom - adj2);
        path.lineTo(rect.right - adj3, rect.bottom - adj4);
        path.lineTo(rect.right - adj1, rect.bottom - adj4);
        
        path.lineTo(rect.right - adj1, rect.bottom);
        path.lineTo(rect.left + adj1, rect.bottom);
        path.close();
        
        return path;
    }    
    
    private static Path getUpDownArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        int adj4 = 0;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = Math.round(rect.height() * values[0]);
            }
            else
            {
                adj1 = Math.round(rect.height() * 0.25f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                adj2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                adj2 = Math.round(rect.width() * 0.25f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = Math.round(rect.height() * values[2]);
            }
            else
            {
                adj3 = Math.round(rect.height() * 0.125f);
            }
            
            if(values.length >= 4 && values[3] != null)
            {
                adj4 = Math.round(rect.width() * values[3]);
            }
            else
            {
                adj4 = Math.round(rect.width() * 0.375f);
            }
        } 
        else
        {
            adj1 = Math.round(rect.height() * 0.25f);
            adj2 = Math.round(rect.width() * 0.25f);
            adj3 = Math.round(rect.height() * 0.125f);
            adj4 = Math.round(rect.width() * 0.375f);
        }
        
        //up part
        path.moveTo(rect.left, rect.top + adj1);
        path.lineTo(rect.left + adj4, rect.top + adj1);
        path.lineTo(rect.left + adj4, rect.top + adj3);
        path.lineTo(rect.left + adj2, rect.top + adj3);
        path.lineTo(rect.centerX(), rect.top);
        path.lineTo(rect.right - adj2, rect.top + adj3);
        path.lineTo(rect.right - adj4, rect.top + adj3);
        path.lineTo(rect.right - adj4, rect.top + adj1);
        path.lineTo(rect.right, rect.top + adj1);
        
        path.lineTo(rect.right, rect.bottom - adj1);
        path.lineTo(rect.right - adj4, rect.bottom - adj1);
        path.lineTo(rect.right - adj4, rect.bottom - adj3);
        path.lineTo(rect.right - adj2, rect.bottom - adj3);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.left + adj2, rect.bottom - adj3);
        path.lineTo(rect.left + adj4, rect.bottom - adj3);
        path.lineTo(rect.left + adj4, rect.bottom - adj1);
        path.lineTo(rect.left, rect.bottom - adj1);
        
        path.close();
        
        return path;
    }    
    
    private static Path getQuadArrowCalloutPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int lrAdj1 = 0;
        int lrAdj2 = 0;
        int lrAdj3 = 0;
        int lrAdj4 = 0;
        
        int udAdj1 = 0;
        int udAdj2 = 0;
        int udAdj3 = 0;
        int udAdj4 = 0;
        
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                lrAdj1 = Math.round(rect.width() * values[0]);
                udAdj1 = Math.round(rect.height() * values[0]);
            }
            else
            {
                lrAdj1 = Math.round(rect.width() * 0.25f);
                udAdj1 = Math.round(rect.height() * 0.25f);
            }
            
            if(values.length >= 2 && values[1] != null)
            {
                lrAdj2 = Math.round(rect.height() * values[1]);
                udAdj2 = Math.round(rect.width() * values[1]);
            }
            else
            {
                lrAdj2 = Math.round(rect.height() * 0.375f);
                udAdj2 = Math.round(rect.width() * 0.375f);
            }
            
            if(values.length >= 3 && values[2] != null)
            {
                lrAdj3 = Math.round(rect.width() * values[2]);
                udAdj3 = Math.round(rect.height() * values[2]);
            }
            else
            {
                lrAdj3 = Math.round(rect.width() * 0.125f);
                udAdj3 = Math.round(rect.height() * 0.125f);
            }
            
            if(values.length >= 4 && values[3] != null)
            {
                lrAdj4 = Math.round(rect.height() * values[3]);
                udAdj4 = Math.round(rect.width() * values[3]);
            }
            else
            {
                lrAdj4 = Math.round(rect.height() * 0.45f);
                udAdj4 = Math.round(rect.width() * 0.45f);
            }
            
        } 
        else
        {
            lrAdj1 = Math.round(rect.width() * 0.25f);
            lrAdj2 = Math.round(rect.height() * 0.375f);
            lrAdj3 = Math.round(rect.width() * 0.125f);
            lrAdj4 = Math.round(rect.height() * 0.45f);
            
            udAdj1 = Math.round(rect.height() * 0.25f);
            udAdj2 = Math.round(rect.width() * 0.375f);
            udAdj3 = Math.round(rect.height() * 0.125f);
            udAdj4 = Math.round(rect.width() * 0.45f);
        }
        
        //left
        path.moveTo(rect.left + lrAdj1, rect.bottom - lrAdj4);
        path.lineTo(rect.left + lrAdj3, rect.bottom - lrAdj4);
        path.lineTo(rect.left + lrAdj3, rect.bottom - lrAdj2);
        path.lineTo(rect.left, rect.centerY());
        path.lineTo(rect.left + lrAdj3, rect.top + lrAdj2);
        path.lineTo(rect.left + lrAdj3, rect.top + lrAdj4);
        path.lineTo(rect.left + lrAdj1, rect.top + lrAdj4);
        path.lineTo(rect.left + lrAdj1, rect.top + udAdj1);
        
        //up
        path.lineTo(rect.left + udAdj4, rect.top + udAdj1);
        path.lineTo(rect.left + udAdj4, rect.top + udAdj3);
        path.lineTo(rect.left + udAdj2, rect.top + udAdj3);
        path.lineTo(rect.centerX(), rect.top );
        path.lineTo(rect.right - udAdj2, rect.top + udAdj3);
        path.lineTo(rect.right - udAdj4, rect.top + udAdj3);
        path.lineTo(rect.right - udAdj4, rect.top + udAdj1);
        path.lineTo(rect.right - lrAdj1, rect.top + udAdj1);
        
        //right
        path.lineTo(rect.right - lrAdj1, rect.top + lrAdj4);
        path.lineTo(rect.right - lrAdj3, rect.top + lrAdj4);
        path.lineTo(rect.right - lrAdj3, rect.top + lrAdj2);
        path.lineTo(rect.right, rect.centerY());
        path.lineTo(rect.right - lrAdj3, rect.bottom - lrAdj2);
        path.lineTo(rect.right - lrAdj3, rect.bottom - lrAdj4);
        path.lineTo(rect.right - lrAdj1, rect.bottom - lrAdj4);
        path.lineTo(rect.right - lrAdj1, rect.bottom - udAdj1);
        
        //down
        path.lineTo(rect.right - udAdj4, rect.bottom - udAdj1);
        path.lineTo(rect.right - udAdj4, rect.bottom - udAdj3);
        path.lineTo(rect.right - udAdj2, rect.bottom - udAdj3);
        path.lineTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.left + udAdj2, rect.bottom - udAdj3);
        path.lineTo(rect.left + udAdj4, rect.bottom - udAdj3);
        path.lineTo(rect.left + udAdj4, rect.bottom - udAdj1);
        path.lineTo(rect.left + lrAdj1, rect.bottom - udAdj1);
        
        path.close();
        return path;
    }
    
    private static Path getCircularArrowPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        float adj1 = 0;
        float adj2 = 0;
        float adj3 = 0;
        int len = 100;
        if(values != null && values.length >= 1)
        {
            if(values[0] != null)
            {
                adj1 = values[0] * TODEGREE;
                if(adj1 < 0)
                {
                    adj1 += 360;
                }
            }
            else
            {
                adj1 = 180;
            }
            
            if(values.length >= 2 && values[1] != null)
            {                
                adj2 = values[1] * TODEGREE;
                if(adj2 < 0)
                {
                    adj2 += 360;
                }
            }
            else
            {
                adj2 = 0;

            }
            
            if(values.length >= 3 && values[2] != null)
            {
                adj3 = values[2] * len;
            }
            else
            {
                adj3 = len * 0.25f;
            }
        } 
        else
        {
            adj1 = 180;
            adj2 = 0;
            adj3 = len * 0.25f;
        }
        
        float outerRadius = len / 2;
        path.moveTo((float)(outerRadius * Math.cos(adj1 * Math.PI / 180)),
            (float)(outerRadius * Math.sin(adj1 * Math.PI / 180)));
        
        s_rect.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
        path.arcTo(s_rect, adj1, (adj2 - adj1 + 360) % 360);
        
        //arrow
        path.lineTo((float)((outerRadius + len * 0.125f) * Math.cos(adj2 * Math.PI / 180)),
            (float)((outerRadius + len * 0.125f) * Math.sin(adj2 * Math.PI / 180)));
        
        path.lineTo((float)((outerRadius + adj3) * 0.5f * Math.cos((adj2 + 30) * Math.PI / 180)),
            (float)((outerRadius + adj3) * 0.5f * Math.sin((adj2 + 30) * Math.PI / 180)));
        
        path.lineTo((float)((adj3 - len * 0.125f) * Math.cos(adj2 * Math.PI / 180)),
            (float)((adj3 - len * 0.125f) * Math.sin(adj2 * Math.PI / 180)));
        
        
        s_rect.set(-adj3, -adj3, adj3, adj3);
        path.arcTo(s_rect, adj2, -(adj2 - adj1 + 360) % 360);

        path.close();
        
        Matrix m = new Matrix();
        m.postScale(rect.width() / 100f, rect.height() / 100f);
        path.transform(m);
        
        path.offset(rect.centerX(), rect.centerY());
        
        return path;
    }
}
