/*
 * 文件名称:          FlagPathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:57:48
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.starAndBanner;

import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.autoshape.ExtendPath;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
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
 * 日期:            2012-10-12
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class BannerPathBuilder
{
    private static Matrix sm = new Matrix();
    
    private static RectF tempRect = new RectF();
    
    private  static List<ExtendPath> pathExList = new ArrayList<ExtendPath>(2);
    
    //picture fill color
    private static final int PICTURECOLOR = 0x8F555555;
    
    private static final float TINT = -0.3f;
    
    /**
     * get banner path
     * @param shape
     * @param rect
     * @return
     */
    public static List<ExtendPath> getFlagExtendPath(AutoShape shape, Rect rect)
    {
        pathExList.clear();
        
        switch(shape.getShapeType())
        {
            case ShapeTypes.Ribbon2:
                return getRibbon2Path(shape, rect);
                
            case ShapeTypes.Ribbon:
                return getRibbonPath(shape, rect);
             
            case ShapeTypes.EllipseRibbon2:
                return getEllipseRibbon2Path(shape, rect);
                
            case ShapeTypes.EllipseRibbon:            
            return getEllipseRibbonPath(shape, rect);
                
            case ShapeTypes.VerticalScroll:
                return getVerticalScrollPath(shape, rect);
                
            case ShapeTypes.HorizontalScroll:
                return getHorizontalScrollPath(shape, rect);
                
            case ShapeTypes.Wave:
                return getWavePath(shape, rect);
                
            case ShapeTypes.DoubleWave:
                return getDoubleWavePath(shape, rect);
                
            case ShapeTypes.LeftRightRibbon:
                return getLeftRightRibbon(shape, rect);
        }
        
        return null;
    }
    
    private static List<ExtendPath> getRibbon2Path(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int width = rect.width();
        int height = rect.height();
        int curleS = width / 8;
        
        int adj1 = 0;
        int adj2 = 0;
        if(shape.isAutoShape07())
        {
            if(values != null && values.length == 2)
            {
                //values[0]:[0, 1/3], values[1]:[0.25, 0.75]
                adj1 = Math.round(height * values[0]);
                adj2 = Math.round(width / 2 * values[1]);
            } 
            else
            {
                adj1 = Math.round(height * 0.16667f);
                adj2 = Math.round(width / 2 * 0.5f);
            }
        }
        else
        {
            if(values != null && values.length >= 1)
            {
                //values[1]:[0, 1/3], values[0]:[0.25, 0.75]
                if(values[0] != null)
                {
                    adj2 = Math.round(width * (0.5f - values[0]));
                }
                else
                {
                    adj2 = Math.round(width * 0.25f);
                }
                
                if(values.length >= 2 && values[1] != null)
                {
                    adj1 = Math.round(height * (1 - values[1]));
                }
                else
                {
                    adj1 = Math.round(height * 0.125f);
                }
                
            } 
            else
            {
                adj1 = Math.round(height * 0.125f);
                adj2 = Math.round(width * 0.25f);
            }
        }
        
        
        //small oval
        float a = curleS / 4;
        float b = adj1 / 4;
        
        //left notched shape
        ExtendPath pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        
        Path path = new Path();
        path.moveTo(rect.left, rect.top + adj1);
        path.lineTo(rect.left + curleS, rect.bottom - (height - adj1) / 2);
        path.lineTo(rect.left, rect.bottom);
        
        path.lineTo(rect.centerX() - adj2 + a * 3, rect.bottom);
        
        tempRect.set(rect.centerX() - adj2 + a * 2, rect.bottom - b * 2, rect.centerX() - adj2 + curleS, rect.bottom);
        path.arcTo(tempRect, 90, -180);
        
        path.lineTo(rect.centerX() - adj2 + a, rect.bottom - b * 2);
        
        tempRect.set(rect.centerX() - adj2 , rect.bottom - b * 4, rect.centerX() - adj2 + a * 2, rect.bottom - b * 2);
        path.arcTo(tempRect, 90, 90);
        
        path.lineTo(rect.centerX() - adj2, rect.top + adj1);
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //right notched shape
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.right, rect.top + adj1);
        path.lineTo(rect.right - curleS, rect.bottom - (height - adj1) / 2);
        path.lineTo(rect.right, rect.bottom);
        
        path.lineTo(rect.centerX() + adj2 - a * 3, rect.bottom);
        
        tempRect.set(rect.centerX() + adj2 - a * 4, rect.bottom - b * 2, rect.centerX() + adj2 - 2 * a, rect.bottom);
        path.arcTo(tempRect, 90, 180);
        
        path.lineTo(rect.centerX() + adj2 - a, rect.bottom - b * 2);
        
        tempRect.set(rect.centerX() + adj2 - 2 * a, rect.bottom - b * 4, rect.centerX() + adj2 , rect.bottom - b * 2);
        path.arcTo(tempRect, 90, -90);
        
        path.lineTo(rect.centerX() + adj2, rect.top + adj1);
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //middle shape        
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.centerX() - adj2, rect.top + b);
        
        tempRect.set(rect.centerX() - adj2, rect.top, rect.centerX() - adj2 + 2 * a , rect.top + b * 2);
        path.arcTo(tempRect, 180, 90);
        
        path.lineTo(rect.centerX() + adj2 - a, rect.top);
        
        tempRect.set(rect.centerX() + adj2 - a * 2, rect.top, rect.centerX() + adj2 , rect.top + b * 2);
        path.arcTo(tempRect, 270, 90);
        
        path.lineTo(rect.centerX() + adj2, rect.bottom - b * 3);
        
        tempRect.set(rect.centerX() + adj2 - a * 2, rect.bottom - b * 4, rect.centerX() + adj2 , rect.bottom - b * 2);
        path.arcTo(tempRect, 0, -90);
        
        path.lineTo(rect.centerX() - adj2 + a, rect.bottom - b * 4);        
        
        tempRect.set(rect.centerX() - adj2, rect.bottom - b * 4, rect.centerX() - adj2 + 2 * a , rect.bottom - b * 2);
        path.arcTo(tempRect, 270, -90);
        
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //left dark part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();  
        path.moveTo(rect.centerX() - adj2 + curleS, rect.bottom - b * 4);
        path.lineTo(rect.centerX() - adj2 + a, rect.bottom - b * 4);
        
        tempRect.set(rect.centerX() - adj2 , rect.bottom - b * 4, rect.centerX() - adj2 + a * 2, rect.bottom - b * 2);
        path.arcTo(tempRect, 270, -180);
        
        path.lineTo(rect.centerX() - adj2 + a * 3, rect.bottom - b * 2);
        
        tempRect.set(rect.centerX() - adj2 + a * 2 , rect.bottom - b * 2, rect.centerX() - adj2 + a * 4, rect.bottom);
        path.arcTo(tempRect, 270, 90);
        
        path.close();        
        
        BackgroundAndFill fill = new BackgroundAndFill();
        fill.setFillType(BackgroundAndFill.FILL_SOLID);
        
        BackgroundAndFill shapeFill = shape.getBackgroundAndFill();
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        
        pathExtend.setPath(path);
        pathExList.add(pathExtend);
        
        
        //right dark part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();  
        path.moveTo(rect.centerX() + adj2 - curleS, rect.bottom - b * 4);
        path.lineTo(rect.centerX() + adj2 - a, rect.bottom - b * 4);
        
        tempRect.set(rect.centerX() + adj2 - a * 2, rect.bottom - b * 4, rect.centerX() + adj2 , rect.bottom - b * 2);
        path.arcTo(tempRect, 270, 180);
        
        path.lineTo(rect.centerX() + adj2 - a * 3, rect.bottom - b * 2);
        
        tempRect.set(rect.centerX() + adj2 - a * 4 , rect.bottom - b * 2, rect.centerX() + adj2 - a * 2, rect.bottom);
        path.arcTo(tempRect, 270, -90);
        
        path.close(); 
        
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        
        pathExtend.setPath(path);
        pathExList.add(pathExtend);
        
        return pathExList;
    }    
    
    private static List<ExtendPath> getRibbonPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int width = rect.width();
        int height = rect.height();
        int curleS = width / 8;
        
        float adj1 = 0;
        float adj2 = 0;
        if(shape.isAutoShape07())
        {
            if(values != null && values.length == 2)
            {
                //values[0]:[0, 1/3], values[1]:[0.25, 0.75]
                adj1 = Math.round(height * values[0]);
                adj2 = Math.round(width / 2 * values[1]);
            } 
            else
            {
                adj1 = Math.round(height * 0.16667f);
                adj2 = Math.round(width / 2 * 0.5f);
            }
        }
        else
        {
            if(values != null && values.length >= 1)
            {
                //values[1]:[0, 1/3], values[0]:[0.25, 0.75]
                if(values[0] != null)
                {
                    adj2 = Math.round(width * (0.5f - values[0]));
                }
                else
                {
                    adj2 = Math.round(width * 0.25f);
                }
                
                if(values.length >= 2 && values[1] != null)
                {
                    adj1 = Math.round(height * values[1]);
                }
                else
                {
                    adj1 = Math.round(height * 0.125f);
                }
                
            } 
            else
            {
                adj1 = Math.round(height * 0.125f);
                adj2 = Math.round(width * 0.25f);
            }
        }
        
        
        //small oval
        float a = curleS / 4;
        float b = adj1 / 4;
        
        //left notched shape
        ExtendPath pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        
        Path path = new Path();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.left + curleS, rect.top + (height - adj1) / 2);
        path.lineTo(rect.left, rect.top + (height - adj1));
        
        path.lineTo(rect.centerX() - adj2 , rect.top + (height - adj1));
        
        tempRect.set(rect.centerX() - adj2, rect.top + b * 2, rect.centerX() - adj2 + a * 2, rect.top + b * 4);
        path.arcTo(tempRect, 180, 90);
        
        path.lineTo(rect.centerX() - adj2 + a * 3, rect.top + b * 2);
        
        tempRect.set(rect.centerX() - adj2 + a *2, rect.top, rect.centerX() - adj2 + a * 4, rect.top + b * 2);
        path.arcTo(tempRect, 90, -180);
        
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //middle part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.centerX() - adj2 + a, rect.bottom);
        
        tempRect.set(rect.centerX() - adj2, rect.bottom - b * 2, rect.centerX() - adj2 + a * 2, rect.bottom);
        path.arcTo(tempRect, 90, 90);
        
        path.lineTo(rect.centerX() - adj2, rect.top + b * 3);
        
        tempRect.set(rect.centerX() - adj2, rect.top + b * 2, rect.centerX() - adj2 + a * 2, rect.top + b * 4);
        path.arcTo(tempRect, 180, -90);
        
        path.lineTo(rect.centerX() + adj2 - a, rect.top + b * 4);
        
        tempRect.set(rect.centerX() + adj2 - a * 2, rect.top + b * 2, rect.centerX() + adj2, rect.top + b * 4);
        path.arcTo(tempRect, 90, -90);
        
        path.lineTo(rect.centerX() + adj2, rect.bottom - b);
        
        tempRect.set(rect.centerX() + adj2 - a * 2, rect.bottom - b * 2, rect.centerX() + adj2, rect.bottom);
        path.arcTo(tempRect, 0, 90);
        
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //right part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.right, rect.top);
        path.lineTo(rect.right - curleS, rect.top + (height - adj1) / 2);
        path.lineTo(rect.right, rect.top + (height - adj1));
        
        path.lineTo(rect.centerX() + adj2 , rect.top + (height - adj1));
        
        tempRect.set(rect.centerX() + adj2 - a * 2, rect.top + b * 2, rect.centerX() + adj2, rect.top + b * 4);
        path.arcTo(tempRect, 0, -90);
        
        path.lineTo(rect.centerX() + adj2 - a * 3, rect.top + b * 2);
        
        tempRect.set(rect.centerX() + adj2 - a * 4, rect.top, rect.centerX() + adj2 - a * 2, rect.top + b * 2);
        path.arcTo(tempRect, 90, 180);
        
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //left dark part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.centerX() - adj2 + a, rect.top + b * 4);
        
        tempRect.set(rect.centerX() - adj2, rect.top + b * 2, rect.centerX() - adj2 + a * 2, rect.top + b * 4);
        path.arcTo(tempRect, 90, 180);
        
        path.lineTo(rect.centerX() - adj2 + a * 3, rect.top + b * 2);
        
        tempRect.set(rect.centerX() - adj2 + a * 2, rect.top, rect.centerX() - adj2 + a * 4, rect.top + b * 2);
        path.arcTo(tempRect, 90, -90);
        
        path.lineTo(rect.centerX() - adj2 + curleS, rect.top + b * 4);
        path.close();
        
        BackgroundAndFill fill = new BackgroundAndFill();
        fill.setFillType(BackgroundAndFill.FILL_SOLID);
        
        BackgroundAndFill shapeFill = shape.getBackgroundAndFill();
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        
        pathExtend.setPath(path);
        pathExList.add(pathExtend);
        
        //right dark part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.centerX() + adj2 - a, rect.top + b * 4);
        
        tempRect.set(rect.centerX() + adj2 - a * 2, rect.top + b * 2, rect.centerX() + adj2, rect.top + b * 4);
        path.arcTo(tempRect, 90, -180);
        
        path.lineTo(rect.centerX() + adj2 - a * 3, rect.top + b * 2);
        
        tempRect.set(rect.centerX() + adj2 - a * 4, rect.top, rect.centerX() + adj2 - a * 2, rect.top + b * 2);
        path.arcTo(tempRect, 90, 90);
        
        path.lineTo(rect.centerX() + adj2 - curleS, rect.top + b * 4);
        path.close();
        
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        
        pathExtend.setPath(path);
        pathExList.add(pathExtend);
        
        return pathExList;
    }    
    
    private static List<ExtendPath> getEllipseRibbon2Path(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        float len = Math.min(rect.width(), rect.height());
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        
        float fU = 0.5f;
        
        if(shape.isAutoShape07())
        {            
            if(values != null && values.length == 3)
            {
                if(values[0] - values[2] > 0.2f)
                {
                    values[2] = values[0] - 0.2f;
                }
                
                if(values[1] > 0.75f)
                {
                    values[1] = 0.75f;
                }
                
                fU = 0.5f - values[1] / 2;
                
                adj1 = Math.round(len * values[0]);
                adj2 = Math.round(len / 2 * values[1]);
                adj3 = Math.round(len * values[2]);
            }
            else
            {
                adj1 = Math.round(len * 0.25f);
                adj2 = Math.round(len / 2 * 0.5f);
                adj3 = Math.round(len * 0.125f);
                
                fU = 0.25f;
            }
        }
        else
        {
            if(values != null && values.length >= 1)
            {
                if(values[0] != null)
                {
                    fU = values[0];
                    adj2 = Math.round(len * (0.5f - values[0]));
                }
                else
                {
                    fU = 0.25f;
                    adj2 = Math.round(len * 0.25f);
                }
                
                if(values.length >= 2 && values[1] != null)
                {
                    adj1 = Math.round(len * ( 1- values[1]));
                }
                else
                {
                    adj1 = Math.round(len * 0.25f);
                }
                
                if(values.length >= 3 && values[2] != null)
                {
                    adj3 = Math.round(len * values[2]);
                }
                else
                {
                    adj3 = Math.round(len * 0.125f);
                }
                
            }
            else
            {
                fU = 0.25f;
                
                adj1 = Math.round(len * 0.25f);
                adj2 = Math.round(len / 2 * 0.5f);
                adj3 = Math.round(len * 0.125f);
            }
        }
        
        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        
        if(adj3 >= adj1)
        {
            List<PointF> ctrlPoints = computeBezierCtrPoint(0, adj1, 
                len, adj1, 
                len / 2, 0, 0.5f);
            
            ExtendPath pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
            Path path = new Path();
            path.moveTo(0, adj1);

            path.cubicTo((ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, adj1);
            
            path.lineTo(len - len * 0.125f, len / 2);
            path.lineTo(len, len);
            
            path.cubicTo((ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y + len - adj1,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y + len - adj1,
                0, len);
            
            path.lineTo(len * 0.125f, len / 2);
            path.close();            
            
            path.transform(sm);        
            path.offset(rect.left, rect.top);
            
            pathExtend.setPath(path);   
            pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
            pathExList.add(pathExtend);
        }
        else
        {
            //left
            List<PointF> ctrlPoints = computeBezierCtrPoint(0, adj1,                
                len, adj1, 
                len / 2, adj1 - adj3, 0.5f);
            
            PointF p1 = BezierComputePoint(0, adj1,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, adj1 , 0.125f);
            
            PointF p2 = BezierComputePoint(0, adj1,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, adj1, fU);
            
            PointF end = BezierComputePoint(0, adj1,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, adj1, fU + 0.125f);
            
            ctrlPoints = computeBezierCtrPoint(0, adj1,
                end.x, end.y,                
                p1.x, p1.y, 0.125f / (fU + 0.125f));
            
            ExtendPath pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
            Path path = new Path();            
            path.moveTo(0, adj1);
            
            path.cubicTo(ctrlPoints.get(0).x, ctrlPoints.get(0).y,
                ctrlPoints.get(1).x, ctrlPoints.get(1).y,
                end.x, end.y);
            
            path.lineTo(end.x, end.y + len - adj1);
            
            path.cubicTo(ctrlPoints.get(1).x, ctrlPoints.get(1).y + len - adj1,
                ctrlPoints.get(0).x, ctrlPoints.get(0).y + len - adj1,
                0, len);
            
            ctrlPoints = computeBezierCtrPoint(0, (len + adj1 ) / 2,                
                len, (len + adj1 ) / 2, 
                len / 2, (len + adj1 ) / 2 - adj3, 0.5f);
            
            PointF notched = BezierComputePoint(0, (len + adj1 ) / 2,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, (len + adj1 ) / 2 , 0.125f);
            
            path.lineTo(notched.x, notched.y);
            path.close();
            
            //right
            ctrlPoints = computeBezierCtrPoint(len  - end.x, end.y,
                len, adj1,
                len - p1.x, p1.y, 1 - 0.125f / (fU + 0.125f));
            
            path.moveTo(len  - end.x, end.y);
            path.cubicTo(ctrlPoints.get(0).x, ctrlPoints.get(0).y,
                ctrlPoints.get(1).x, ctrlPoints.get(1).y,
                len, adj1);
            
            path.lineTo(len - notched.x, notched.y);
            path.lineTo(len, len);
            
            path.cubicTo(ctrlPoints.get(1).x, ctrlPoints.get(1).y + len - adj1,
                ctrlPoints.get(0).x, ctrlPoints.get(0).y + len - adj1,
                len  - end.x, end.y + len - adj1);
            
            path.close();
          
            path.transform(sm);        
            path.offset(rect.left, rect.top);
            
            pathExtend.setPath(path);   
            pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
            pathExList.add(pathExtend);
            
            //middle part
            ctrlPoints = computeBezierCtrPoint(0, adj3,                
                len, adj3, 
                len / 2, 0, 0.5f);
        
            
            p1 = BezierComputePoint(0, adj3,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, adj3 , fU);
            
            ctrlPoints = computeBezierCtrPoint(p1.x, p1.y,                
                len - p1.x, p1.y, 
                len / 2, 0, 0.5f);            
                
            pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
            path = new Path(); 
            path.moveTo(p1.x, p1.y);            
            path.cubicTo((ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len - p1.x, p1.y);
            
            path.lineTo(len - p1.x, p1.y + len - adj1);
            
            path.cubicTo((ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y + len - adj1,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y + len - adj1,
                p1.x, p1.y + len - adj1);
            
            path.close();
            
            
            //left and right dark part       
            path.moveTo(p1.x, p1.y + len - adj1);
            path.lineTo(end.x, end.y + len - adj1);
            
            path.moveTo(len - p1.x, p1.y + len - adj1);
            path.lineTo(len - end.x, end.y + len - adj1);
            
            pathExtend.setPath(path);
            path.transform(sm);        
            path.offset(rect.left, rect.top);
            
            pathExtend.setPath(path);   
            pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
            pathExList.add(pathExtend);
        }
        
        return pathExList;
    }
    
    private static List<ExtendPath> getEllipseRibbonPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        float len = Math.min(rect.width(), rect.height());
        int adj1 = 0;
        int adj2 = 0;
        int adj3 = 0;
        
        float fU = 0.5f;
        
        if(shape.isAutoShape07())
        {            
            if(values != null && values.length == 3)
            {
                if(values[0] - values[2] > 0.2f)
                {
                    values[2] = values[0] - 0.2f;
                }
                
                if(values[1] > 0.75f)
                {
                    values[1] = 0.75f;
                }
                
                fU = 0.5f - values[1] / 2;
                
                adj1 = Math.round(len * values[0]);
                adj2 = Math.round(len / 2 * values[1]);
                adj3 = Math.round(len * values[2]);
            }
            else
            {
                adj1 = Math.round(len * 0.25f);
                adj2 = Math.round(len / 2 * 0.5f);
                adj3 = Math.round(len * 0.125f);
                
                fU = 0.25f;
            }
        }
        else
        {
            if(values != null && values.length >= 1)
            {
                if(values[0] != null)
                {
                    fU = values[0];
                    adj2 = Math.round(len * (0.5f - values[0]));
                }
                else
                {
                    fU = 0.25f;
                    adj2 = Math.round(len * 0.25f);
                }
                
                if(values.length >= 2 && values[1] != null)
                {
                    adj1 = Math.round(len * values[1]);
                }
                else
                {
                    adj1 = Math.round(len * 0.25f);
                }
                
                if(values.length >= 3 && values[2] != null)
                {
                    adj3 = Math.round(len * ( 1- values[2]));
                }
                else
                {
                    adj3 = Math.round(len * 0.125f);
                }
                
            }
            else
            {
                fU = 0.25f;
                
                adj1 = Math.round(len * 0.25f);
                adj2 = Math.round(len / 2 * 0.5f);
                adj3 = Math.round(len * 0.125f);
            }
        }
        
        
        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        
        if(adj3 >= adj1)
        {
            List<PointF> ctrlPoints = computeBezierCtrPoint(0, 0, 
                len, 0, 
                len / 2, adj1, 0.5f);
            
            ExtendPath pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
           
            Path path = new Path();
            path.moveTo(0, 0);

            path.cubicTo((ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, 0);
            
            path.lineTo(len - len * 0.125f, len / 2);
            path.lineTo(len, len - adj1);
            
            path.cubicTo((ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y + len - adj1,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y + len - adj1,
                0, len - adj1);
            
            path.lineTo(len * 0.125f, len / 2);
            path.close();            
            
            path.transform(sm);        
            path.offset(rect.left, rect.top);
            
            pathExtend.setPath(path);   
            pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
            pathExList.add(pathExtend);
        }
        else
        {
            //left
            List<PointF> ctrlPoints = computeBezierCtrPoint(0, 0,                
                len, 0, 
                len / 2, adj3, 0.5f);
            
            PointF p1 = BezierComputePoint(0, 0,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, 0 , 0.125f);
            
            PointF p2 = BezierComputePoint(0, 0,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, 0, fU);
            
            PointF end = BezierComputePoint(0, 0,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, 0, fU + 0.125f);
            
            ctrlPoints = computeBezierCtrPoint(0, 0,
                end.x, end.y,                
                p1.x, p1.y, 0.125f / (fU + 0.125f));
            
            ExtendPath pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
            Path path = new Path();            
            path.moveTo(0, 0);
            
            path.cubicTo(ctrlPoints.get(0).x, ctrlPoints.get(0).y,
                ctrlPoints.get(1).x, ctrlPoints.get(1).y,
                end.x, end.y);
            
            path.lineTo(end.x, end.y + len - adj1);
            
            path.cubicTo(ctrlPoints.get(1).x, ctrlPoints.get(1).y + len - adj1,
                ctrlPoints.get(0).x, ctrlPoints.get(0).y + len - adj1,
                0, len - adj1);
            
            ctrlPoints = computeBezierCtrPoint(0, (len - adj1 ) / 2,                
                len, (len - adj1 ) / 2, 
                len / 2, (len - adj1 ) / 2 + adj3, 0.5f);
            
            PointF notched = BezierComputePoint(0, (len - adj1 ) / 2,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, (len - adj1 ) / 2 , 0.125f);
            
            path.lineTo(notched.x, notched.y);
            path.close();
            
            //right
            ctrlPoints = computeBezierCtrPoint(len  - end.x, end.y,
                len, 0,
                len - p1.x, p1.y, 1 - 0.125f / (fU + 0.125f));
            
            path.moveTo(len  - end.x, end.y);
            path.cubicTo(ctrlPoints.get(0).x, ctrlPoints.get(0).y,
                ctrlPoints.get(1).x, ctrlPoints.get(1).y,
                len, 0);
            
            path.lineTo(len - notched.x, notched.y);
            path.lineTo(len, len - adj1);
            
            path.cubicTo(ctrlPoints.get(1).x, ctrlPoints.get(1).y + len - adj1,
                ctrlPoints.get(0).x, ctrlPoints.get(0).y + len - adj1,
                len  - end.x, end.y + len - adj1);
            
            path.close();
          
            path.transform(sm);        
            path.offset(rect.left, rect.top);
            
            pathExtend.setPath(path);   
            pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
            pathExList.add(pathExtend);
            
            //middle part
            ctrlPoints = computeBezierCtrPoint(0, len - adj3,                
                len, len - adj3, 
                len / 2, len, 0.5f);
        
            
            p1 = BezierComputePoint(0, len - adj3,
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len, len - adj3 , fU);
            
            ctrlPoints = computeBezierCtrPoint(p1.x, p1.y,                
                len - p1.x, p1.y, 
                len / 2, len, 0.5f);            
                
            pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
            path = new Path(); 
            path.moveTo(p1.x, p1.y);            
            path.cubicTo((ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y,
                (ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y,
                len - p1.x, p1.y);
            
            path.lineTo(len - p1.x, p1.y - (len - adj1));
            
            path.cubicTo((ctrlPoints.get(1).x + len / 2) / 2, ctrlPoints.get(1).y - (len - adj1),
                (ctrlPoints.get(0).x + len / 2) / 2, ctrlPoints.get(0).y - (len - adj1),
                p1.x, p1.y - (len - adj1));
            
            path.close();
            
            
            //left dark part       
            path.moveTo(p1.x, p1.y - (len - adj1));
            path.lineTo(end.x, end.y);
            
            path.moveTo(len - p1.x, p1.y - (len - adj1));
            path.lineTo(len - end.x, end.y);
            
            pathExtend.setPath(path);
            path.transform(sm);        
            path.offset(rect.left, rect.top);
            
            pathExtend.setPath(path);   
            pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
            pathExList.add(pathExtend);
        }
        
        return pathExList;
    }
    
    /**
     * 
     * @param startX start point of bezier line
     * @param startY
     * @param p1X   p1, p2 are point in the bezier line(between start and end)
     * @param p1Y
     * @param p2X
     * @param p2Y
     * @param endX  end point of bezier line
     * @param endY
     * @return
     */
    private static List<PointF> computeBezierCtrPoint(float startX, float startY,
        float p1X, float p1Y,
        float p2X, float p2Y,
        float endX, float endY)
    {
        List<PointF> ctrlPoints = new ArrayList<PointF>(2);
        PointF ctrAdd1 = new PointF();
        PointF ctrAdd2 = new PointF();
        ctrlPoints.add(0, ctrAdd1);
        ctrlPoints.add(1, ctrAdd2);
        
        //P1=(-5*Q0+18*Q1-9*Q2+2*Q3)/6;
        //P2=(2*Q0-9*Q1+18*Q2-5*Q3)/6;
        ctrAdd1.x = (-5 * startX + 18 * p1X - 9 * p2X + 2 * endX) / 6 ;
        ctrAdd1.y = (-5 * startY + 18 * p1Y - 9 * p2Y + 2 * endY) / 6;
        ctrAdd2.x = (2 * startX - 9 * p1X + 18 * p2X - 5 * endX) / 6;
        ctrAdd2.y = (2 * startY - 9 * p1Y + 18 * p2Y - 5 * endY) / 6;

        return ctrlPoints;
    }
    
    /**
     * 
     * @param startX start of the bezier
     * @param startY
     * @param endX  end of the bezier
     * @param endY
     * @param pointX point in the bezier line
     * @param pointY
     * @param fU
     * @return
     */
    private static List<PointF> computeBezierCtrPoint(float startX, float startY, 
        float endX, float endY, 
        float pointX, float pointY, float fU)
    {
        final float  EPSINON   =   0.00001f;
        if (fU < EPSINON && fU - 1.0 > EPSINON)
        {
            return null;
        }       
        float a,b,c,d,s;
        float a1,c1;
        
        List<PointF> ctrlPoints = new ArrayList<PointF>(2);
        PointF ctrAdd1 = new PointF();
        PointF ctrAdd2 = new PointF();
        ctrlPoints.add(0, ctrAdd1);
        ctrlPoints.add(1, ctrAdd2);
        
        float fBlend = fU;
        float f1subu = 1.0f - fBlend;
        a = 3 * fBlend * f1subu * f1subu;
        b = 3 * fBlend * fBlend * f1subu;
        c = f1subu * f1subu * f1subu;
        d = fBlend * fBlend * fBlend;
        
        s = fU / f1subu;//note fu = 0 1
        a1 = a + 3 * d;
        c1 = (float)(pointX - c * startX - a * startX - b * endX -d * endX);
        if (a1 < EPSINON)
            return null;
        ctrAdd1.x = c1 / a1 + startX;
        ctrAdd2.x = s * c1 / a1 + endX;
        
        c1 = (float)(pointY - c * startY - a * startY - b * endY - d * endY);
        if (a1 < EPSINON)
            return null;
        ctrAdd1.y = c1 / a1 + startY;
        ctrAdd2.y = s * c1 / a1 + endY;
        
        return ctrlPoints;
    }
    
    /**
     * 
     * @param startX
     * @param startY
     * @param ctrl1X
     * @param ctrl1Y
     * @param ctrl2X
     * @param ctrl2Y
     * @param endX
     * @param endY
     * @param fU
     * @return
     */
    private static PointF BezierComputePoint(float startX, float startY,
         float ctrl1X, float ctrl1Y,
         float ctrl2X, float ctrl2Y,
         float endX, float endY, float fU)
    {
        PointF p = new PointF();
        //  
        //  Add up all the blending functions multiplied with the control points
        //
        float fBlend;
        float f1subu = 1.0f - fU;

        //  
        //  First blending function (1-u)^3
        //  
        fBlend = f1subu * f1subu * f1subu;
        p.x = fBlend * startX;
        p.y = fBlend * startY;

        //  
        //  Second blending function 3u(1-u)^2
        //
        fBlend = 3 * fU * f1subu * f1subu;
        p.x += fBlend * ctrl1X;
        p.y += fBlend * ctrl1Y;

        //  
        //  Third blending function 3u^2 * (1-u)
        //
        fBlend = 3 * fU * fU * f1subu;
        p.x += fBlend * ctrl2X;
        p.y += fBlend * ctrl2Y;

        //  
        //  Fourth blending function u^3
        //  
        fBlend = fU * fU * fU;
        p.x += fBlend * endX;
        p.y += fBlend * endY;
        
        return p;
    }    
    
    private static List<ExtendPath> getVerticalScrollPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int width = rect.width();
        int height = rect.height();
        int len = Math.min(width, height);
        int adj1 = 0;
        if(values != null && values.length == 1)
        {
            //values[0]:[0, 0.25]
            adj1 = Math.round(len * values[0]);
        } 
        else
        {
            adj1 = Math.round(len * 0.125f);
        }
        
        float radius = adj1 / 2f;
        
        //
        ExtendPath pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        
        Path path = new Path();
        path.moveTo(rect.left + radius, rect.bottom);
        
        tempRect.set(rect.left, rect.bottom - adj1, rect.left + adj1, rect.bottom);
        path.arcTo(tempRect, 90, -90);
        
        path.lineTo(rect.left + adj1, rect.top + radius);
        
        tempRect.set(rect.left + adj1, rect.top, rect.left + adj1 * 2, rect.top + adj1);
        path.arcTo(tempRect, 180, 270);
        
        path.lineTo(rect.right - adj1, rect.top + adj1);
        path.lineTo(rect.right - adj1, rect.bottom - radius);
        
        tempRect.set(rect.right - adj1 * 2, rect.bottom - adj1, rect.right - adj1, rect.bottom);
        path.arcTo(tempRect, 0, 90);       
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.left + radius * 3, rect.top);
        
        tempRect.set(rect.left + adj1, rect.top, rect.left + adj1 * 2, rect.top + adj1);
        path.arcTo(tempRect, 270, 180);
        
        path.lineTo(rect.right - radius, rect.top + adj1);
        
        tempRect.set(rect.right - adj1, rect.top, rect.right, rect.top + adj1);
        path.arcTo(tempRect, 90, -180);       
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.left + adj1, rect.bottom - adj1);
        
        path.lineTo(rect.left + adj1, rect.bottom - radius);
        path.lineTo(rect.left + radius, rect.bottom - radius);
        
        tempRect.set(rect.left + radius * 0.5f, rect.bottom - adj1, rect.left + radius * 1.5f, rect.bottom - radius);
        path.arcTo(tempRect, 90, -180);
           
        path.close();
        
        pathExtend.setPath(path);
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        
        //dark part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.left + adj1, rect.bottom - radius);
        
        tempRect.set(rect.left, rect.bottom - adj1, rect.left + adj1, rect.bottom);
        path.arcTo(tempRect, 0, 270);
        
        tempRect.set(rect.left + radius * 0.5f, rect.bottom - adj1, rect.left + radius * 1.5f, rect.bottom - radius);
        path.arcTo(tempRect, 270, 180);
           
        path.close();
        
        pathExtend.setPath(path);
        BackgroundAndFill fill = new BackgroundAndFill();
        fill.setFillType(BackgroundAndFill.FILL_SOLID);
        
        BackgroundAndFill shapeFill = shape.getBackgroundAndFill();
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        pathExList.add(pathExtend);
        
        //
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.left + adj1 * 2, rect.top + radius);
        
        tempRect.set(rect.left + adj1, rect.top, rect.left + adj1 * 2, rect.top + adj1);
        path.arcTo(tempRect, 0, 90);
        
        tempRect.set(rect.left + adj1 + radius * 0.5f, rect.top + radius, rect.left + adj1 + radius * 1.5f, rect.top + adj1);
        path.arcTo(tempRect, 90, 180);
           
        path.close();
        
        pathExtend.setPath(path);
       
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        pathExList.add(pathExtend);
        
        return pathExList;
    }
    
    private static List<ExtendPath> getHorizontalScrollPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int width = rect.width();
        int height = rect.height();
        int len = Math.min(width, height);
        int adj1 = 0;
        if(values != null && values.length == 1)
        {
            //values[0]:[0, 0.25]
            adj1 = Math.round(len * values[0]);
        } 
        else
        {
            adj1 = Math.round(len * 0.125f);
        }
        
        float radius = adj1 / 2f;
        
        //
        ExtendPath pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
       
        Path path = new Path();
        path.moveTo(rect.left, rect.top + radius * 3);
        
        tempRect.set(rect.left, rect.top + adj1, rect.left + adj1, rect.top + adj1 * 2);
        path.arcTo(tempRect, 180, -180);
        
        path.lineTo(rect.left + adj1, rect.bottom - radius);
        
        tempRect.set(rect.left, rect.bottom - adj1, rect.left + adj1, rect.bottom);
        path.arcTo(tempRect, 0, 180);             
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.left + adj1, rect.top + radius * 3);
        
        tempRect.set(rect.left, rect.top + adj1, rect.left + adj1, rect.top + adj1 * 2);
        path.arcTo(tempRect, 0, 270);
        
        path.lineTo(rect.right - radius, rect.top + adj1);
        
        tempRect.set(rect.right - adj1, rect.top, rect.right, rect.top + adj1);
        path.arcTo(tempRect, 90, -90);
        
        path.lineTo(rect.right, rect.bottom - adj1 - radius);
        
        tempRect.set(rect.right - adj1, rect.bottom - adj1 * 2, rect.right, rect.bottom - adj1);
        path.arcTo(tempRect, 0, 90);
        
        path.lineTo(rect.left + adj1, rect.bottom - adj1);
        
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.right - adj1, rect.top + radius);
        
        tempRect.set(rect.right - adj1, rect.top + radius * 0.5f, rect.right - radius, rect.top + radius * 1.5f);
        path.arcTo(tempRect, 180, -180);
        
        path.lineTo(rect.right - radius, rect.top + adj1);
        path.lineTo(rect.right - adj1, rect.top + adj1);
        path.close();
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        //dark part
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.left + radius, rect.top + adj1 * 2);
        
        tempRect.set(rect.left, rect.top + adj1, rect.left + adj1, rect.top + adj1 * 2);
        path.arcTo(tempRect, 90, -90);
        
        tempRect.set(rect.left + radius, rect.top + adj1 + radius * 0.5f, rect.left + adj1, rect.top + adj1 + radius * 1.5f);
        path.arcTo(tempRect, 0, -180);
        
        path.close();
        
        pathExtend.setPath(path);   
        BackgroundAndFill fill = new BackgroundAndFill();
        fill.setFillType(BackgroundAndFill.FILL_SOLID);
        
        BackgroundAndFill shapeFill = shape.getBackgroundAndFill();
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        pathExList.add(pathExtend);        
        
        //
        pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        path = new Path();
        path.moveTo(rect.right - radius, rect.top + radius);
        
        tempRect.set(rect.right - adj1, rect.top + radius  * 0.5f, rect.right - radius, rect.top + radius  * 1.5f);
        path.arcTo(tempRect, 0, 180);
        
        tempRect.set(rect.right - adj1, rect.top, rect.right, rect.top + adj1);
        path.arcTo(tempRect, 180, 270);
        
        path.close();
        
        pathExtend.setPath(path);  
        if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
        {
            fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
        }
        else
        {
            fill.setForegroundColor(PICTURECOLOR);
        } 
        pathExtend.setBackgroundAndFill(fill);
        pathExList.add(pathExtend);
        return pathExList;
    }    
    
    private static List<ExtendPath> getWavePath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int width = rect.width();
        int height = rect.height();
        int adj1 = 0;
        int adj2 = 0;
        if(shape.isAutoShape07())
        {
            if(values != null && values.length == 2)
            {
                //values[0]:[0, 0.25]   values[1];[-0.1,0.1]
                adj1 = Math.round(height * values[0]);
                adj2 = Math.round(width * values[1]);
            } 
            else
            {
                adj1 = Math.round(height * 0.125f);
                adj2 = 0;
            }
        }
        else
        {
            if(values != null && values.length >= 1)
            {
                //values[0]:[0, 0.25]   values[1];[-0.1,0.1]
                if(values[0] != null)
                {
                    adj1 = Math.round(height * values[0]);
                }
                else
                {
                    adj1 = Math.round(height * 0.125f);
                }
                
                if(values.length >= 2 && values[1] != null)
                {
                    adj2 = Math.round(width * (values[1] - 0.5f));
                }
                else
                {
                    adj2 = 0;
                }
            } 
            else
            {
                adj1 = Math.round(height * 0.125f);
                adj2 = 0;
            }
        }
        
        int waveW = width - Math.abs(adj2 * 2);
        int waveH = height - adj1;
        
        ExtendPath pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        Path path = new Path();
        if(adj2 > 0)
        {
            path.moveTo(rect.left, rect.top + adj1);
            
            path.cubicTo(rect.left + waveW * 0.3333f, rect.top + adj1 - adj1 * 3.3333f,
                rect.left + waveW * 0.6667f, rect.top + adj1 + adj1 * 3.3333f,
                rect.left + waveW, rect.top+ adj1);
            
            path.lineTo(rect.right, rect.bottom - adj1);
            
            path.cubicTo(rect.right - waveW * 0.333f, rect.bottom - adj1 + adj1 * 3.3333f,
                rect.right - waveW * 0.6667f, rect.bottom - adj1 - adj1 * 3.333f,
                rect.right - waveW, rect.bottom - adj1);
            
            path.close();
        }
        else
        {
            path.moveTo(rect.right - waveW, rect.top + adj1);
            
            path.cubicTo(rect.right - waveW * 0.6667f, rect.top + adj1 -adj1 * 3.333f,
                rect.right - waveW * 0.3333f, rect.top + adj1 + adj1 * 3.333f,
                rect.right, rect.top+ adj1);
            
            path.lineTo(rect.left + waveW, rect.bottom - adj1);
            
            path.cubicTo(rect.left + waveW * 0.6667f, rect.bottom - adj1 + adj1 * 3.333f,
                rect.left + waveW * 0.333f, rect.bottom - adj1 - adj1 * 3.333f,
                rect.left, rect.bottom - adj1);
            
            path.close();
        }
        
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        return pathExList;
    }    
    
    private static List<ExtendPath> getDoubleWavePath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int width = rect.width();
        int height = rect.height();
        int adj1 = 0;
        int adj2 = 0;
        if(shape.isAutoShape07())
        {
            if(values != null && values.length == 2)
            {
                //values[0]:[0, 0.25]   values[1];[-0.1,0.1]
                adj1 = Math.round(height * values[0]);
                adj2 = Math.round(width * values[1]);
            } 
            else
            {
                adj1 = Math.round(height * 0.125f);
                adj2 = 0;
            }
        }
        else
        {
            if(values != null && values.length >= 1)
            {
                //values[0]:[0, 0.25]   values[1];[-0.1,0.1]
                if(values[0] != null)
                {
                    adj1 = Math.round(height * values[0]);
                }
                else
                {
                    adj1 = Math.round(height * 0.125f);
                }
                
                if(values.length >= 2 && values[1] != null)
                {
                    adj2 = Math.round(width * (values[1] - 0.5f));
                }
                else
                {
                    adj2 = 0;
                }
            } 
            else
            {
                adj1 = Math.round(height * 0.125f);
                adj2 = 0;
            }
        }
        
        int waveW = (width - Math.abs(adj2 * 2)) / 2;
        
        ExtendPath pathExtend = new ExtendPath();
        if(shape.hasLine())
        {
        	 pathExtend.setLine(shape.getLine());
             pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
        }
        
        Path path = new Path();
        if(adj2 > 0)
        {
            path.moveTo(rect.left, rect.top + adj1);
            
            path.cubicTo(rect.left + waveW * 0.3333f, rect.top + adj1 - adj1 * 3.333f,
                rect.left + waveW * 0.6667f, rect.top + adj1 + adj1 * 3.333f,
                rect.left + waveW, rect.top+ adj1);
            
            path.cubicTo(rect.left + waveW * 1.3333f, rect.top + adj1 - adj1 * 3.333f,
                rect.left + waveW * 1.6667f, rect.top + adj1 + adj1 * 3.333f,
                rect.left + waveW * 2, rect.top+ adj1);
            
            path.lineTo(rect.right, rect.bottom - adj1);
            
            path.cubicTo(rect.right - waveW * 0.3333f, rect.bottom - adj1 + adj1 * 3.333f,
                rect.right - waveW * 0.6667f, rect.bottom - adj1 - adj1 * 3.333f,
                rect.right - waveW, rect.bottom - adj1);
            
            path.cubicTo(rect.right - waveW * 1.3333f, rect.bottom - adj1 + adj1 * 3.333f,
                rect.right - waveW * 1.6667f, rect.bottom - adj1 - adj1 * 3.333f,
                rect.right - waveW * 2, rect.bottom - adj1);
            
            path.close();
        }
        else
        {
            path.moveTo(rect.right - waveW * 2, rect.top + adj1);
            
            path.cubicTo(rect.right - waveW * 1.6667f, rect.top + adj1 - adj1 * 3.333f,
                rect.right - waveW * 1.3333f, rect.top + adj1 + adj1 * 3.333f,
                rect.right - waveW, rect.top+ adj1);
            
            path.cubicTo(rect.right - waveW * 0.6667f, rect.top + adj1 - adj1 * 3.333f,
                rect.right - waveW * 0.3333f, rect.top + adj1 + adj1 * 3.333f,
                rect.right, rect.top+ adj1);
            
            path.lineTo(rect.left + waveW * 2, rect.bottom - adj1);
            
            path.cubicTo(rect.left + waveW * 1.6667f, rect.bottom - adj1 + adj1 * 3.333f,
                rect.left + waveW * 1.3333f, rect.bottom - adj1 - adj1 * 3.333f,
                rect.left + waveW, rect.bottom - adj1);
            
            path.cubicTo(rect.left + waveW * 0.6667f, rect.bottom - adj1 + adj1 * 3.333f,
                rect.left + waveW * 0.3333f, rect.bottom - adj1 - adj1 * 3.333f,
                rect.left, rect.bottom - adj1);
            
            path.close();
        }
        
        pathExtend.setPath(path);   
        pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
        pathExList.add(pathExtend);
        
        return pathExList;
    }
    
    private static List<ExtendPath> getLeftRightRibbon(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        
        int len = Math.min(rect.width(), rect.height());
        int height = rect.height();
        int adj1 = 0;
        int adj2 = 0;
        int adj3V = 0;
        int adj3H = 0;
        if(shape.isAutoShape07())
        {
            if(values != null && values.length == 3)
            {
                //
                adj1 = Math.round(height * values[0]);
                adj2 = Math.round(len * values[1]);
                adj3H = Math.round(rect.width() * values[2]);
                adj3V = Math.round(height * values[2]);
            } 
            else
            {
                adj1 = Math.round(height * 0.5f);
                adj2 = Math.round(len * 0.5f);
                adj3H = Math.round(rect.width() * 0.16667f);
                adj3V = Math.round(height * 0.16667f);
            }
            
            int arrowHeight = height - adj3V;
            
            ExtendPath pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
            
            Path path = new Path();
            path.moveTo(rect.left, rect.top + arrowHeight / 2);
            path.lineTo(rect.left + adj2, rect.top);
            path.lineTo(rect.left + adj2, rect.top + (arrowHeight - adj1) / 2);
            path.lineTo(rect.centerX(), rect.top + (arrowHeight - adj1) / 2);
            
            path.arcTo(new RectF(rect.centerX() - adj3H / 4, rect.top + (arrowHeight - adj1) / 2, rect.centerX() + adj3H / 4, rect.top + (arrowHeight - adj1) / 2 + adj3V / 2), 
                270, 180);
            
            path.arcTo(new RectF(rect.centerX() - adj3H / 4, rect.top + (arrowHeight - adj1) / 2 + adj3V / 2, rect.centerX() + adj3H / 4, rect.top + (arrowHeight - adj1) / 2 + adj3V), 
                270, -180);
            
            path.lineTo(rect.right - adj2, rect.bottom - (arrowHeight - adj1) / 2 - adj1);
            path.lineTo(rect.right - adj2, rect.bottom - arrowHeight);
            path.lineTo(rect.right, rect.bottom - arrowHeight / 2);
            path.lineTo(rect.right - adj2, rect.bottom );
            path.lineTo(rect.right - adj2, rect.bottom - (arrowHeight - adj1) / 2);
            
            path.arcTo(new RectF(rect.centerX() - adj3H / 4, 
                rect.bottom - (arrowHeight - adj1) / 2 - adj3V / 2, 
                rect.centerX() + adj3H / 4, 
                rect.bottom - (arrowHeight - adj1) / 2 ), 
                90, 90);
            
            path.lineTo(rect.centerX() - adj3H / 4, rect.top + (arrowHeight - adj1) / 2 + adj1);
            path.lineTo(rect.left + adj2, rect.top + (arrowHeight - adj1) / 2 + adj1);
            path.lineTo(rect.left + adj2, rect.top + arrowHeight);
            path.close();
            
            pathExtend.setPath(path);   
            pathExtend.setBackgroundAndFill(shape.getBackgroundAndFill());
            pathExList.add(pathExtend);
            
            //dark part
            pathExtend = new ExtendPath();
            if(shape.hasLine())
            {
            	 pathExtend.setLine(shape.getLine());
                 pathExtend.setBackgroundAndFill(shape.getLine().getBackgroundAndFill());
            }
            path = new Path();
            path.arcTo(new RectF(rect.centerX() - adj3H / 4, rect.top + (arrowHeight - adj1) / 2 + adj3V / 2, rect.centerX() + adj3H / 4, rect.top + (arrowHeight - adj1) / 2 + adj3V), 
                270, -180);
            path.close();
            
            BackgroundAndFill fill = new BackgroundAndFill();
            fill.setFillType(BackgroundAndFill.FILL_SOLID);
            
            BackgroundAndFill shapeFill = shape.getBackgroundAndFill();
            if(shapeFill != null && shapeFill.getFillType() == BackgroundAndFill.FILL_SOLID)
            {
                fill.setForegroundColor(ColorUtil.instance().getColorWithTint(shapeFill.getForegroundColor(), TINT));
            }
            else
            {
                fill.setForegroundColor(PICTURECOLOR);
            } 
            pathExtend.setBackgroundAndFill(fill);
            
            pathExtend.setPath(path);
            pathExList.add(pathExtend);
            
            return pathExList;
        }
        
        return null;
    }
}
