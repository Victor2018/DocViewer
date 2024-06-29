/*
 * 文件名称:           WedgeCallout.java
 *  
 * 编译器:             android2.2
 * 时间:               上午9:25:20
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.wedgecallout;

import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.autoshape.ExtendPath;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * draw wedgeCallout
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-9-26
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class WedgeCalloutDrawing
{ 
    private static RectF rectF = new RectF();
    
    private static Path path = new Path();
    
    private static List<ExtendPath> paths = new ArrayList<ExtendPath>();
    //
    private static final WedgeCalloutDrawing kit = new WedgeCalloutDrawing();

    /**
     * 
     * @return
     */
    public static WedgeCalloutDrawing instance()
    {
        return kit;
    }
    
    /**
     * 
     * @param canvas
     * @param shape
     * @param rect
     */
    public Object getWedgeCalloutPath(AutoShape shape, Rect rect)
    {
        paths.clear();
        path.reset();
        
        switch(shape.getShapeType())
        {
            case ShapeTypes.WedgeRectCallout:                               // 矩形标注
                return getWedgeRectCalloutPath(shape, rect);
                
            case ShapeTypes.WedgeRoundRectCallout:                          // 圆角矩形标注
                return getWedgeRoundRectCalloutPath(shape, rect);
                
            case ShapeTypes.WedgeEllipseCallout:                            // 椭圆形标注
                return getWedgeEllipseCalloutPath(shape, rect);
                
            case ShapeTypes.CloudCallout:                                   // 云形标注
                return getCloudCalloutPath(shape, rect);
                
            case ShapeTypes.BorderCallout1:                                 // 线性标注1
                if (shape.isAutoShape07())
                {
                    return getBorderCallout1Path(shape, rect);
                }
                else
                {
                    return get03BorderCallout2Path(shape, rect);
                }
                
            case ShapeTypes.BorderCallout2:                                 // 线性标注2
                if (shape.isAutoShape07())
                {
                    return getBorderCallout2Path(shape, rect);
                }
                else
                {
                    return get03BorderCallout2Path(shape, rect);
                }
                
            case ShapeTypes.BorderCallout3:                                 // 线性标注3
                if (shape.isAutoShape07())
                {
                    return getBorderCallout3Path(shape, rect);
                }
                else
                {
                    return get03BorderCallout3Path(shape, rect);
                }
                
            case ShapeTypes.BorderCallout4:                                 // 线性标注4
                return  get03BorderCallout4Path(shape, rect);
                
            case ShapeTypes.AccentCallout1:                                 // 线性标注1(带强调线)
                if (shape.isAutoShape07())
                {
                    return getAccentCallout1Path(shape, rect);
                }
                else
                {
                    return get03AccentCallout1Path(shape, rect);
                }
                
            case ShapeTypes.AccentCallout2:                                 // 线性标注2(带强调线)
                if (shape.isAutoShape07())
                {
                    return getAccentCallout2Path(shape, rect);
                }
                else
                {
                    return get03AccentCallout2Path(shape, rect);
                }
                
            case ShapeTypes.AccentCallout3:                                 // 线性标注3(带强调线)
                if (shape.isAutoShape07())
                {
                    return getAccentCallout3Path(shape, rect);
                }
                else
                {
                    return get03AccentCallout3(shape, rect);
                }
                
            case ShapeTypes.AccentCallout4:                                 // 线性标注4(带强调线)
                return get03AccentCallout4(shape, rect);
                
            case ShapeTypes.Callout1:                                       // 线性标注1(无边框)
                if (shape.isAutoShape07())
                {
                    return getCallout1(shape, rect);
                }
                else
                {
                    return get03AccentCallout1Path(shape, rect);
                }
                
            case ShapeTypes.Callout2:                                       // 线性标注2(无边框)
                if (shape.isAutoShape07())
                {
                    return getCallout2(shape, rect);
                }
                else
                {
                    return get03Callout2(shape, rect);
                }
                
            case ShapeTypes.Callout3:                                       // 线性标注3(无边框)
                if (shape.isAutoShape07())
                {
                    return getCallout3(shape, rect);
                }
                else
                {
                    return get03Callout3(shape, rect);
                }
                
            case ShapeTypes.Callout4:                                       // 线性标注4(无边框)
                return get03Callout4(shape, rect);
                
            case ShapeTypes.AccentBorderCallout1:                           // 线性标注1(带边框和强调线)
                if (shape.isAutoShape07())
                {
                    return getAccentBorderCallout1(shape, rect);
                }
                else
                {
                    return get03BorderCallout2Path(shape, rect);
                }
                
            case ShapeTypes.AccentBorderCallout2:                           // 线性标注2(带边框和强调线)
                if (shape.isAutoShape07())
                {
                    return getAccentBorderCallout2(shape, rect);
                }
                else
                {
                    return get03AccentBorderCallout2(shape, rect);
                }
                
            case ShapeTypes.AccentBorderCallout3:                           // 线性标注3(带边框和强调线)
                if (shape.isAutoShape07())
                {
                    return getAccentBorderCallout3(shape, rect);
                }
                else
                {
                    return get03AccentBorderCallout3(shape, rect);
                }
                
            case ShapeTypes.AccentBorderCallout4:                           // 线性标注4(带边框和强调线)
                return get03AccentBorderCallout4(shape, rect);
        }
        return null;
    }
    
    /**
     * 矩形标注
     * @param canvas
     * @param shape
     * @param rect
     */
    private static Path getWedgeRectCalloutPath(AutoShape shape, Rect rect)
    {
        float x = -rect.width() * 0.2f;
        float y = rect.height() * 0.6f;
        float z = (float)rect.width() / 12;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0];
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1];
                }
            }
        }
        else
        {
            x = -rect.width() * 0.433f;
            y = rect.height() * 0.7f;
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0] - rect.width() / 2;
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1] - rect.height() / 2;
                }
            }
        }
        
        if ((float)Math.abs(y / x) < (float)rect.height() / rect.width())
        {
            z = (float)rect.height() / 12;
            // right
            if (x >= 0)
            {
                path.moveTo(rect.left, rect.top);
                path.lineTo(rect.right, rect.top);
                if (y >= 0)
                {
                    path.lineTo(rect.right, rect.exactCenterY() + z);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.right, rect.bottom - z * 2);
                }
                else
                {
                    path.lineTo(rect.right, rect.top + z * 2);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.right, rect.exactCenterY() - z);
                }
                path.lineTo(rect.right, rect.bottom);
                path.lineTo(rect.left, rect.bottom);
            }
            // left
            else
            { 
                path.moveTo(rect.left, rect.top);
                path.lineTo(rect.right, rect.top);
                path.lineTo(rect.right, rect.bottom);
                path.lineTo(rect.left, rect.bottom);
                if (y >= 0)
                {
                    path.lineTo(rect.left, rect.bottom - z * 2);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.left, rect.exactCenterY() + z);
                }
                else
                {
                    path.lineTo(rect.left, rect.exactCenterY() - z);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.left, rect.top + z * 2);
                }
            }
        }
        else
        {
            // bottom
            if (y >= 0)
            {
                path.moveTo(rect.left, rect.top);
                path.lineTo(rect.right, rect.top);
                path.lineTo(rect.right, rect.bottom);
                if (x >= 0)
                {
                    path.lineTo(rect.right - z * 2, rect.bottom);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.exactCenterX() + z, rect.bottom);
                }
                else
                {
                    path.lineTo(rect.exactCenterX() - z, rect.bottom);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.left + z * 2, rect.bottom);
                }
                path.lineTo(rect.left, rect.bottom);
            }
            // top
            else
            {
                path.moveTo(rect.left, rect.top);
                if (x >= 0)
                {
                    path.lineTo(rect.exactCenterX() + z, rect.top);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.right - z * 2, rect.top);
                }
                else
                {
                    path.lineTo(rect.left + z * 2, rect.top);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.exactCenterX() - z, rect.top);
                }
                path.lineTo(rect.right, rect.top);
                path.lineTo(rect.right, rect.bottom);
                path.lineTo(rect.left, rect.bottom);
            }
        }
        path.close();
        
        return path;
    }
    
    /**
     * 圆角矩形标注
     * @param canvas
     * @param shape
     * @param rect
     */
    private static Path getWedgeRoundRectCalloutPath(AutoShape shape, Rect rect)
    {
        float x = -rect.width() * 0.2f;
        float y = rect.height() * 0.6f;
        float z = (float)rect.width() / 12;
        float r = Math.min(rect.width(), rect.height()) * 0.16667f;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            if (values != null && values.length >= 3)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0];
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1];
                }
                if (values[2] != null)
                {
                    r = Math.min(rect.width(), rect.height()) * values[2];
                }
            }
        }
        else
        {
            x = -rect.width() * 0.433f;
            y = rect.height() * 0.7f;
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0] - rect.width() / 2;
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1] - rect.height() / 2;
                }
            }
        }
        
        if ((float)Math.abs(y / x) < (float)(rect.height()) / (rect.width()))
        {
            z = (float)rect.height() / 12;
            // right
            if (x >= 0)
            {
                rectF.set(rect.left, rect.top, rect.left + r * 2, rect.top + r * 2);
                path.arcTo(rectF, 180, 90);
                rectF.set(rect.right - r * 2, rect.top, rect.right, rect.top + r * 2);
                path.arcTo(rectF, 270, 90);
                if (y >= 0)
                {
                    path.lineTo(rect.right, rect.exactCenterY() + z);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.right, rect.bottom - z * 2);
                }
                else
                {
                    path.lineTo(rect.right, rect.top + z * 2);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.right, rect.exactCenterY() - z);
                }
                rectF.set(rect.right - r * 2, rect.bottom - r * 2, rect.right, rect.bottom);
                path.arcTo(rectF, 0, 90);
                rectF.set(rect.left, rect.bottom - r * 2, rect.left + r * 2, rect.bottom);
                path.arcTo(rectF, 90, 90);
            }
            // left
            else
            { 
                rectF.set(rect.left, rect.top, rect.left + r * 2, rect.top + r * 2);
                path.arcTo(rectF, 180, 90);
                rectF.set(rect.right - r * 2, rect.top, rect.right, rect.top + r * 2);
                path.arcTo(rectF, 270, 90);
                rectF.set(rect.right - r * 2, rect.bottom - r * 2, rect.right, rect.bottom);
                path.arcTo(rectF, 0, 90);
                rectF.set(rect.left, rect.bottom - r * 2, rect.left + r * 2, rect.bottom);
                path.arcTo(rectF, 90, 90);
                if (y >= 0)
                {
                    path.lineTo(rect.left, rect.bottom - z * 2);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.left, rect.exactCenterY() + z);
                }
                else
                {
                    path.lineTo(rect.left, rect.exactCenterY() - z);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.left, rect.top + z * 2);
                }
            }
        }
        else
        {
            // bottom
            if (y >= 0)
            {
                rectF.set(rect.left, rect.top, rect.left + r * 2, rect.top + r * 2);
                path.arcTo(rectF, 180, 90);
                rectF.set(rect.right - r * 2, rect.top, rect.right, rect.top + r * 2);
                path.arcTo(rectF, 270, 90);
                rectF.set(rect.right - r * 2, rect.bottom - r * 2, rect.right, rect.bottom);
                path.arcTo(rectF, 0, 90);
                if (x >= 0)
                {
                    path.lineTo(rect.right - z * 2, rect.bottom);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.exactCenterX() + z, rect.bottom);
                }
                else
                {
                    path.lineTo(rect.exactCenterX() - z, rect.bottom);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.left + z * 2, rect.bottom);
                }
                rectF.set(rect.left, rect.bottom - r * 2, rect.left + r * 2, rect.bottom);
                path.arcTo(rectF, 90, 90);
            }
            // top
            else
            {
                rectF.set(rect.left, rect.top, rect.left + r * 2, rect.top + r * 2);
                path.arcTo(rectF, 180, 90);
                if (x >= 0)
                {
                    path.lineTo(rect.exactCenterX() + z, rect.top);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.right - z * 2, rect.top);
                }
                else
                {
                    path.lineTo(rect.left + z * 2, rect.top);
                    path.lineTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
                    path.lineTo(rect.exactCenterX() - z, rect.top);
                }
                rectF.set(rect.right - r * 2, rect.top, rect.right, rect.top + r * 2);
                path.arcTo(rectF, 270, 90);
                rectF.set(rect.right - r * 2, rect.bottom - r * 2, rect.right, rect.bottom);
                path.arcTo(rectF, 0, 90);
                rectF.set(rect.left, rect.bottom - r * 2, rect.left + r * 2, rect.bottom);
                path.arcTo(rectF, 90, 90);
            }
        }
        path.close();
        
        return path;
    }
    
    /**
     * 椭圆形标注
     * @param canvas
     * @param shape
     * @param rect
     */
    private static Path getWedgeEllipseCalloutPath(AutoShape shape, Rect rect)
    {
        float x = -rect.width() * 0.2f;
        float y = rect.height() * 0.6f;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0];
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1];
                }
            }
        }
        else
        {
            x = -rect.width() * 0.433f;
            y = rect.height() * 0.7f;
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0] - rect.width() / 2;
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1] - rect.height() / 2;
                }
            }
        }
        float angle1 = (float)Math.toDegrees(Math.atan2(rect.width(), rect.height())) / 2;
        float angle2 = (float)Math.toDegrees(Math.atan2(Math.abs(y), Math.abs(x)));
        float start = 0;
        
        
        path.moveTo(rect.exactCenterX() + x, rect.exactCenterY() + y);
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        if (y >= 0)
        {
            if (x >= 0)
            {
                start = angle2 + angle1 / 2;
            }
            else
            {
                start = 180 - angle2 + angle1 / 2;
            }
            
            path.arcTo(rectF, start, 360 - angle1);
        }
        else
        {
            if (x >= 0)
            {
                start = 360 - angle2 - angle1 / 2;
            }
            else
            {
                start = 180 + angle2 - angle1 / 2;
            }
            path.arcTo(rectF, start, -360 + angle1);
        }
        path.close();
        
        return path;
    }
    
    /**
     * 云形标注
     * @param canvas
     * @param shape
     * @param rect
     */
    private static Path getCloudCalloutPath(AutoShape shape, Rect rect)
    {
        float len = 468;
        

        rectF.set(0, 160, 90, 285);
        path.arcTo(rectF, 120, 148);
        
        rectF.set(41, 44, 188, 250);
        path.arcTo(rectF, 172.5f, 127.5f);
        
        rectF.set(140, 14, 264, 220);
        path.arcTo(rectF, 218, 90);
        
        rectF.set(230, 0, 340, 210);
        path.arcTo(rectF, 219, 92);
        
        rectF.set(296, 0, 428, 246);
        path.arcTo(rectF, 232, 101);
        
        
        rectF.set(342, 60, 454, 214);
        path.arcTo(rectF, 293, 89);
        
        rectF.set(324, 130, 468, 327);
        path.arcTo(rectF, 319, 119);
        
        
        rectF.set(280, 240, 405, 412);
        path.arcTo(rectF, 1, 122);
        
        rectF.set(168, 274, 312, 468);
        path.arcTo(rectF, 16, 130);
        
        rectF.set(57, 249, 213, 441);
        path.arcTo(rectF, 56, 74);
        
        rectF.set(11, 259, 99, 386);
        path.arcTo(rectF, 84, 140);
        
        path.close();        
        
        Matrix m = new Matrix();
        m.postScale(rect.width() / len, rect.height() / len);
        path.transform(m);
        
        path.offset(rect.left, rect.top);
        
        Float[] values = shape.getAdjustData();
        int adj1 = 0;
        int adj2 = 0;
        if (shape.isAutoShape07())
        {
            if(values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    adj1 = Math.round(rect.width() * values[0]);
                }
                if (values[1] != null)
                {
                    adj2 = Math.round(rect.height() * values[1]);
                }
            } 
            else
            {
                adj1 = Math.round(rect.width() * -0.2f);
                adj2 = Math.round(rect.height() * 0.6f);
            }
        }
        else
        {
            if(values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    adj1 = Math.round(rect.width() * values[0] - rect.width() / 2);
                }
                if (values[1] != null)
                {
                    adj2 = Math.round(rect.height() * values[1] - rect.height() / 2);
                }
            } 
            else
            {
                adj1 = Math.round(rect.width() * -0.433f);
                adj2 = Math.round(rect.height() * 0.7f);
            }
        }
        
        //intersection of circle centers line and outer cirlce
        double angle = getAngle(adj1, adj2);
        
        /**
         * y = x * tanθ
         * x = √(a^2* b^2 /(b^2 + a^2 * tanθ^2)
         */
        int a = rect.width() / 2;
        int b = rect.height() / 2;
        
        float outx = (float)(a * b / Math.sqrt(Math.pow(b, 2) + Math.pow(a * Math.tan(angle * Math.PI / 180), 2)));
        if(angle > 90 && angle < 270)
        {
            outx = -outx;
        }
        
        float outy = (float)( outx * Math.tan(angle * Math.PI / 180));
        
        //center of small circle
        float sx = rect.centerX() + adj1;
        float sy = rect.centerY() + adj2;
        
        outx = rect.centerX() + outx;
        outy = rect.centerY() + outy;
        
        float r = Math.min(rect.width(), rect.height()) / 468f;
        //small circle
        path.addCircle(sx, sy, 16 * r, Direction.CW);
        
        //middle circle
        float x = outx + 0.7f * (sx - outx);
        float y = outy + 0.7f * (sy - outy);
        path.addCircle(x, y, 24 * r, Direction.CW);
        
        //larger circle
        x = outx + 0.3f * (sx - outx);
        y = outy + 0.3f * (sy - outy);
        path.addCircle(x, y, 40 * r, Direction.CW);
        
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
    
    /**
     * 线性标注1
     * @param canvas
     * @param shape
     * @param rect
     */
    private static Path getBorderCallout1Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 1.125f;
        float x2 = rect.left + rect.width() * (-0.38333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 4)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
        }
        
        
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        
        return path;
    }
    
    /**
     * 线性标注2
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getBorderCallout2Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.125f;
        float x3 = rect.left + rect.width() * (-0.46667f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 6)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());       
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注3
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getBorderCallout3Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.0f;
        float x3 = rect.left + rect.width() * (-0.16667f);
        float y4 = rect.top + rect.height() * 1.12963f;
        float x4 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 8)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
            if (values[6] != null)
            {
                y4 = rect.top + rect.height() * values[6];
            }
            if (values[7] != null)
            {
                x4 = rect.left + rect.width() * values[7];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());       
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注1(带强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getAccentCallout1Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 1.125f;
        float x2 = rect.left + rect.width() * (-0.38333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 4)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);     
        extendPath.setPath(path);     
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, rect.top);
        path.lineTo(x1, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine()); 
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注2(带强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getAccentCallout2Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.125f;
        float x3 = rect.left + rect.width() * (-0.46667f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 6)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        
        extendPath.setPath(path);
        extendPath.setBackgroundAndFill(fill);   
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, rect.top);
        path.lineTo(x1, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注3(带强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getAccentCallout3Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.0f;
        float x3 = rect.left + rect.width() * (-0.16667f);
        float y4 = rect.top + rect.height() * 1.12963f;
        float x4 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 8)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
            if (values[6] != null)
            {
                y4 = rect.top + rect.height() * values[6];
            }
            if (values[7] != null)
            {
                x4 = rect.left + rect.width() * values[7];
            }
        }        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();       
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setPath(path);
        extendPath.setBackgroundAndFill(fill);        
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();        
        path.moveTo(x1, rect.top);
        path.lineTo(x1, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注1(无边框)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getCallout1(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 1.125f;
        float x2 = rect.left + rect.width() * (-0.38333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 4)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setPath(path);
        extendPath.setBackgroundAndFill(fill);     
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注2(无边框)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getCallout2(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.125f;
        float x3 = rect.left + rect.width() * (-0.46667f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 6)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
          
        extendPath.setPath(path);
        extendPath.setBackgroundAndFill(fill);        
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注3(无边框)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getCallout3(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.0f;
        float x3 = rect.left + rect.width() * (-0.16667f);
        float y4 = rect.top + rect.height() * 1.12963f;
        float x4 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 8)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
            if (values[6] != null)
            {
                y4 = rect.top + rect.height() * values[6];
            }
            if (values[7] != null)
            {
                x4 = rect.left + rect.width() * values[7];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setPath(path);
        extendPath.setBackgroundAndFill(fill);       
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注1(带边框和强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static Path getAccentBorderCallout1(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 1.125f;
        float x2 = rect.left + rect.width() * (-0.38333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 4)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
        }
        
        
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        
        path.moveTo(x1, rect.top);
        path.lineTo(x1, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        
        return path;
    }
    
    /**
     * 线性标注2(带边框和强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getAccentBorderCallout2(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.125f;
        float x3 = rect.left + rect.width() * (-0.46667f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 6)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();

        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());      
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, rect.top);
        path.lineTo(x1, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 线性标注3(带边框和强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> getAccentBorderCallout3(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 1.0f;
        float x3 = rect.left + rect.width() * (-0.16667f);
        float y4 = rect.top + rect.height() * 1.12963f;
        float x4 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 8)
        {
            if (values[0] != null)
            {
                y1 = rect.top + rect.height() * values[0];
            }
            if (values[1] != null)
            {
                x1 = rect.left + rect.width() * values[1];
            }
            if (values[2] != null)
            {
                y2 = rect.top + rect.height() * values[2];
            }
            if (values[3] != null)
            {
                x2 = rect.left + rect.width() * values[3];
            }
            if (values[4] != null)
            {
                y3 = rect.top + rect.height() * values[4];
            }
            if (values[5] != null)
            {
                x3 = rect.left + rect.width() * values[5];
            }
            if (values[6] != null)
            {
                y4 = rect.top + rect.height() * values[6];
            }
            if (values[7] != null)
            {
                x4 = rect.left + rect.width() * values[7];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());       
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, rect.top);
        path.lineTo(x1, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注2
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03BorderCallout2Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());     
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());   
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注3
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03BorderCallout3Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());   
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());  
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注4
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03BorderCallout4Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * 1.08333f;
        float y4 = rect.top + rect.height() * 0.1875f;
        float x4 = rect.left + rect.width() * 1.08333f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
            if (values.length >= 7 && values[6] != null)
            {
                x4 = rect.left + rect.width() * values[6];
            }
            if (values.length >= 8 && values[7] != null)
            {
                y4 = rect.top + rect.height() * values[7];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine()); 
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注1(带强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03AccentCallout1Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;;
        float x2 = rect.left + rect.width() * (-0.38333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 4)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
        }  
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);            
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        extendPath.setLine(shape.getLine());  
        extendPath.setPath(path);            
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注2(带强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03AccentCallout2Path(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);                
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();        
        path.moveTo(x2, rect.top);
        path.lineTo(x2, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注3(带强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03AccentCallout3(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
                
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();

        path.moveTo(x3, rect.top);
        path.lineTo(x3, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注4(带强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03AccentCallout4(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * 1.08333f;
        float y4 = rect.top + rect.height() * 0.1875f;
        float x4 = rect.left + rect.width() * 1.08333f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
            if (values.length >= 7 && values[6] != null)
            {
                x4 = rect.left + rect.width() * values[6];
            }
            if (values.length >= 8 && values[7] != null)
            {
                y4 = rect.top + rect.height() * values[7];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
              
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();

        path.moveTo(x4, rect.top);
        path.lineTo(x4, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());  
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注2(无边框)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03Callout2(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
               
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注3(无边框)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03Callout3(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
             
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine()); 
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注4(无边框)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03Callout4(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * 1.08333f;
        float y4 = rect.top + rect.height() * 0.1875f;
        float x4 = rect.left + rect.width() * 1.08333f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
            if (values.length >= 7 && values[6] != null)
            {
                x4 = rect.left + rect.width() * values[6];
            }
            if (values.length >= 8 && values[7] != null)
            {
                y4 = rect.top + rect.height() * values[7];
            }
        }
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
            
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine()); 
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注2(带边框和强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03AccentBorderCallout2(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());     
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();        
        path.moveTo(x2, rect.top);
        path.lineTo(x2, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注3(带边框和强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03AccentBorderCallout3(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.08333f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * (-0.08333f);
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());        
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();

        path.moveTo(x3, rect.top);
        path.lineTo(x3, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());  
        paths.add(extendPath);
        
        return paths;
    }
    
    /**
     * 03线性标注4(带边框和强调线)
     * @param canvas
     * @param shape
     * @param rect
     */
    private static List<ExtendPath> get03AccentBorderCallout4(AutoShape shape, Rect rect)
    {
        float y1 = rect.top + rect.height() * 0.1875f;
        float x1 = rect.left + rect.width() * (-0.08333f);
        float y2 = rect.top + rect.height() * 0.1875f;
        float x2 = rect.left + rect.width() * (-0.16667f);
        float y3 = rect.top + rect.height() * 0.1875f;
        float x3 = rect.left + rect.width() * 1.08333f;
        float y4 = rect.top + rect.height() * 0.1875f;
        float x4 = rect.left + rect.width() * 1.08333f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x1 = rect.left + rect.width() * values[0];
            }
            if (values.length >= 2 && values[1] != null)
            {
                y1 = rect.top + rect.height() * values[1];
            }
            if (values.length >= 3 && values[2] != null)
            {
                x2 = rect.left + rect.width() * values[2];
            }
            if (values.length >= 4 && values[3] != null)
            {
                y2 = rect.top + rect.height() * values[3];
            }
            if (values.length >= 5 && values[4] != null)
            {
                x3 = rect.left + rect.width() * values[4];
            }
            if (values.length >= 6 && values[5] != null)
            {
                y3 = rect.top + rect.height() * values[5];
            }
            if (values.length >= 7 && values[6] != null)
            {
                x4 = rect.left + rect.width() * values[6];
            }
            if (values.length >= 8 && values[7] != null)
            {
                y4 = rect.top + rect.height() * values[7];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());       
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(x4, rect.top);
        path.lineTo(x4, rect.bottom);
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
}
