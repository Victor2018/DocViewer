/*
 * 文件名称:          BaseShapePathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:01:08
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.baseshape;

import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.autoshape.ExtendPath;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Path.Direction;
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
public class BaseShapePathBuilder
{
    private static final float TODEGREE_07 = 18000000f / 10800000f;
    private static final float TODEGREE_03 = 18000000f / 54620000f;
    
    private static RectF rectF = new RectF();
    
    private static Path path = new Path();
    
    private static List<ExtendPath> paths = new ArrayList<ExtendPath>();
    
    private static Matrix m = new Matrix();
    
    /**
     * get base shape path
     * @param shape
     * @param rect
     * @return
     */
    public static Object getBaseShapePath(AutoShape shape, Rect rect)
    {
        path.reset();
        paths.clear();
        
        switch(shape.getShapeType())
        {
            case ShapeTypes.Ellipse:                                        // 椭圆
                return getEllipsePath(shape, rect);
                
            case ShapeTypes.Triangle:                                       // 等腰三角形
                return getTrianglePath(shape, rect);
                
            case ShapeTypes.RtTriangle:                                     // 直角三角形
                return getRtTrianglePath(shape, rect);
                
            case ShapeTypes.Parallelogram:                                  // 平行四边形
                return getParallelogramPath(shape, rect);
                
            case ShapeTypes.Trapezoid:                                      // 梯形
                return getTrapezoidPath(shape, rect);
                
            case ShapeTypes.Diamond:                                        // 菱形
                return getDiamondPath(shape, rect);
                
            case ShapeTypes.Pentagon:                                       // 正五边形
                return getPentagonPath(shape, rect);
                
            case ShapeTypes.Hexagon:                                        // 六边形
                return getHexagonPath(shape, rect);
                
            case ShapeTypes.Heptagon:                                       // 七边形
                return getHeptagonPath(shape, rect);
                
            case ShapeTypes.Octagon:                                        // 八边形
                return getOctagonPath(shape, rect);
                
            case ShapeTypes.Decagon:                                        // 十边形
                return getDecagonPath(shape, rect);
                
            case ShapeTypes.Dodecagon:                                      // 十二边形
                return getDodecagonPath(shape, rect);
                
            case ShapeTypes.Pie:                                            // 饼形
                return getPiePath(shape, rect);
                
            case ShapeTypes.Chord:                                          // 弦形
                return getChordPath(shape, rect);
                
            case ShapeTypes.Teardrop:                                       // 泪滴形
                return getTeardropPath(shape, rect);
                
            case ShapeTypes.Frame:                                          // 图文框
                return getFramePath(shape, rect);
                
            case ShapeTypes.HalfFrame:                                      // 半闭框
                return getHalfFramePath(shape, rect);
                
            case ShapeTypes.Corner:                                         // L形
                return getCornerPath(shape, rect);
                
            case ShapeTypes.DiagStripe:                                     // 斜纹
                return getDiagStripePath(shape, rect);
                
            case ShapeTypes.Plus:                                           // 十字形
                return getPlusPath(shape, rect);
                
            case ShapeTypes.Plaque:                                         // 缺角矩形
                return getPlaquePath(shape, rect);
                
            case ShapeTypes.Can:                                            // 圆柱形
                return getCanPath(shape, rect);
                
            case ShapeTypes.Cube:                                           // 立方体
                return getCubePath(shape, rect);
                
            case ShapeTypes.Bevel:                                          // 棱台
                return getBevelPath(shape, rect);
                
            case ShapeTypes.Donut:                                          // 同心圆
                return getDonutPath(shape, rect);
                
            case ShapeTypes.NoSmoking:                                      // 禁止符
                return getNoSmokingPath(shape, rect);
                
            case ShapeTypes.BlockArc:                                       // 空心弧
                return getBlockArcPath(shape, rect);
                
            case ShapeTypes.FoldedCorner:                                   // 折角形
                return getFoldedCornerPath(shape, rect);
                
            case ShapeTypes.SmileyFace:                                     // 笑脸
                return getSmileyFacePath(shape, rect);
                
            case ShapeTypes.Sun:                                            // 太阳形
                return getSunPath(shape, rect);
                
            case ShapeTypes.Heart:                                          // 心形
                return getHeartPath(shape, rect);
                
            case ShapeTypes.LightningBolt:                                  // 闪电形
                return getLightningBoltPath(shape, rect);
                
            case ShapeTypes.Moon:                                           // 新月形
                return getMoonPath(shape, rect);
                
            case ShapeTypes.Cloud:                                         // 云形
                return getCloudPath(shape, rect);               
                
            case ShapeTypes.Arc:                                            // 弧形
                return getArcPath(shape, rect);
                
            case ShapeTypes.BracketPair:                                    // 双括号
                return getBracketPairPath(shape, rect);
                
            case ShapeTypes.BracePair:                                      // 双大括号
                return getBracePairPath(shape, rect);
                
            case ShapeTypes.LeftBracket:                                    // 左中括号
                return getLeftBracketPath(shape, rect);
                
            case ShapeTypes.RightBracket:                                   // 右中括号
                return getRightBracketPath(shape, rect);
                
            case ShapeTypes.LeftBrace:                                      // 左大括号
                return getLeftBracePath(shape, rect);
                
            case ShapeTypes.RightBrace:                                     // 右大括号
                return getRightBracePath(shape, rect);     
        }
        
        return null;
    }
    
    private static Path getEllipsePath(AutoShape shape, Rect rect)
    {
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        
        return path;
    }
    
    private static Path getTrianglePath(AutoShape shape, Rect rect)
    {
        float x = rect.width() * 0.5f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = rect.width() * values[0];
            }
        }
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getRtTrianglePath(AutoShape shape, Rect rect)
    {
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getParallelogramPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.width(), rect.height()) * 0.2f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.width(), rect.height()) * values[0];
                }
            }
        }
        else
        {
            x = rect.width() * 0.25f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0];
                }
            }
        }
        
        path.reset();
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getTrapezoidPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.width(), rect.height()) * 0.2f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.width(), rect.height()) * values[0];
                }
            }
            
            path.moveTo(rect.left + x, rect.top);
            path.lineTo(rect.right - x, rect.top);
            path.lineTo(rect.right, rect.bottom);
            path.lineTo(rect.left, rect.bottom);
            path.close();
        }
        else
        {
            x = rect.width() * 0.25f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0];
                }
            }
            
            path.moveTo(rect.left, rect.top);
            path.lineTo(rect.right, rect.top);
            path.lineTo(rect.right - x, rect.bottom);
            path.lineTo(rect.left + x, rect.bottom);
            path.close();
        }
        
        return path;
    }
    
    private static Path getDiamondPath(AutoShape shape, Rect rect)
    {
        path.moveTo(rect.exactCenterX(), rect.top);
        path.lineTo(rect.right, rect.exactCenterY());
        path.lineTo(rect.exactCenterX(), rect.bottom);
        path.lineTo(rect.left, rect.exactCenterY());
        path.close();
        
        return path;
    }
    
    private static Path getPentagonPath(AutoShape shape, Rect rect)
    {
        float x = (float)rect.width() / 2;
        float y = x * (float)Math.tan(Math.toRadians(36));
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right, rect.top + y);
        path.lineTo(rect.right - (rect.height() - y) * (float)Math.tan(Math.toRadians(18)), rect.bottom);
        path.lineTo(rect.left + (rect.height() - y) * (float)Math.tan(Math.toRadians(18)), rect.bottom);
        path.lineTo(rect.left, rect.top + y);
        path.close();
        
        return path;
    }
    
    private static Path getHexagonPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.width(), rect.height()) * 0.25f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.width(), rect.height()) * values[0];
                }
            }
        }
        else
        {
            x = rect.width() * 0.25f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0];
                }
            }
        }
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right - x, rect.top);
        path.lineTo(rect.right, rect.exactCenterY());
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left + x, rect.bottom);
        path.lineTo(rect.left, rect.exactCenterY());
        path.close();
        
        return path;
    }
    
    private static Path getHeptagonPath(AutoShape shape, Rect rect)
    {
        float x1 = rect.width() * 0.1f;
        float x2 = rect.width() * 0.275f;
        float y1 = rect.height() * 0.2f;
        float y2 = rect.height() * 0.35f;
        
        path.reset();
        path.moveTo(rect.exactCenterX(), rect.top);
        path.lineTo(rect.right - x1, rect.top + y1);
        path.lineTo(rect.right, rect.bottom - y2);
        path.lineTo(rect.right - x2, rect.bottom);
        path.lineTo(rect.left + x2, rect.bottom);
        path.lineTo(rect.left, rect.bottom - y2);
        path.lineTo(rect.left + x1, rect.top + y1);
        path.close();
        
        return path;
    }
    
    private static Path getOctagonPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.25f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
        }
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right - x, rect.top);
        path.lineTo(rect.right, rect.top + x);
        path.lineTo(rect.right, rect.bottom - x);
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left + x, rect.bottom);
        path.lineTo(rect.left, rect.bottom - x);
        path.lineTo(rect.left, rect.top + x);
        path.close();
        
        return path;
    }
    
    private static Path getDecagonPath(AutoShape shape, Rect rect)
    {
        float x1 = rect.width() * 0.1f;
        float x2 = rect.width() * 0.35f;
        float y = rect.height() * 0.2f;
        
        path.moveTo(rect.left + x2, rect.top);
        path.lineTo(rect.right - x2, rect.top);
        path.lineTo(rect.right - x1, rect.top + y);
        path.lineTo(rect.right, rect.exactCenterY());
        path.lineTo(rect.right - x1, rect.bottom - y);
        path.lineTo(rect.right - x2, rect.bottom);
        path.lineTo(rect.left + x2, rect.bottom);
        path.lineTo(rect.left + x1, rect.bottom - y);
        path.lineTo(rect.left, rect.exactCenterY());
        path.lineTo(rect.left + x1, rect.top + y);
        path.close();
        
        return path;
    }
    
    private static Path getDodecagonPath(AutoShape shape, Rect rect)
    {
        float x1 = rect.width() * 0.133f;
        float x2 = rect.width() * 0.35f;
        float y1 = rect.height() * 0.133f;
        float y2 = rect.height() * 0.35f;        
        
        path.moveTo(rect.left + x2, rect.top);
        path.lineTo(rect.right - x2, rect.top);
        path.lineTo(rect.right - x1, rect.top + y1);
        path.lineTo(rect.right, rect.top + y2);
        path.lineTo(rect.right, rect.bottom - y2);
        path.lineTo(rect.right -x1, rect.bottom - y1);
        path.lineTo(rect.right - x2, rect.bottom);
        path.lineTo(rect.left + x2, rect.bottom);
        path.lineTo(rect.left + x1, rect.bottom - y1);
        path.lineTo(rect.left, rect.bottom - y2);
        path.lineTo(rect.left, rect.top + y2);
        path.lineTo(rect.left + x1, rect.top + y1);
        path.close();
        
        return path;
    }
    
    private static Path getPiePath(AutoShape shape, Rect rect)
    {
        float start = 0;
        float end = 270;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                start = values[0] * TODEGREE_07;
            }
            if (values[1] != null)
            {
                end = values[1] * TODEGREE_07;
            }
        }
        
        path.moveTo(rect.centerX(), rect.centerY());
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.arcTo(rectF, start, (end - start + 360) % 360);
        path.close();
        
        return path;
    }
    
    private static Path getChordPath(AutoShape shape, Rect rect)
    {
        float start = 45;
        float end = 270;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                start = values[0] * 10 / 6;
            }
            if (values[1] != null)
            {
                end = values[1] * 10 / 6;
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.arcTo(rectF, start, end - start);
        path.close();
        
        return path;
    }
    
    private static Path getTeardropPath(AutoShape shape, Rect rect)
    {
        float x = 0, y = 0;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1 && values[0] != null)
        {
            x = (float)rect.width() / 2 * values[0];
            y = (float)rect.height() / 2 * values[0];
        }
        else
        {
            x = (float)rect.width() / 2;
            y = (float)rect.height() / 2;
        }        
        
        path.moveTo(rect.right, rect.centerY());
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.arcTo(rectF, 0, 270);
        
        path.quadTo(rect.centerX() + x / 2, rect.top, rect.centerX() + x, rect.centerY() - y);
        path.quadTo( rect.right, rect.centerY() - y / 2, rect.right, rect.centerY());
        
        path.close();
        return path;
    }
    
    private static Path getFramePath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.height(), rect.width()) * 0.1f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.height(), rect.width()) * values[0];
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addRect(rectF, Path.Direction.CW);
        rectF.set(rect.left + x, rect.top + x, rect.right - x, rect.bottom - x);
        path.addRect(rectF, Path.Direction.CCW);
        
        return path;
    }
    
    private static Path getHalfFramePath(AutoShape shape, Rect rect)
    {
        float x1 = Math.min(rect.height(), rect.width()) * 0.33333f;
        float y1 = Math.min(rect.height(), rect.width()) * 0.33333f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                y1 = Math.min(rect.height(), rect.width()) * values[0];
            }
            if (values[1] != null)
            {
                x1 = Math.min(rect.height(), rect.width()) * values[1];
            }
        }
        float x2 = y1 * rect.width() / rect.height();
        float y2 = x1 * rect.height() / rect.width();
        
        path.reset();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right - x2, rect.top + y1);
        path.lineTo(rect.left + x1, rect.top + y1);
        path.lineTo(rect.left + x1, rect.bottom - y2);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getCornerPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.height(), rect.width()) * 0.5f;
        float y = Math.min(rect.height(), rect.width()) * 0.5f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 2)
        {
            if (values[0] != null)
            {
                y = Math.min(rect.height(), rect.width()) * values[0];
            }
            if (values[1] != null)
            {
                x = Math.min(rect.height(), rect.width()) * values[1];
            }
        }
        y = rect.height() - y;
        
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.left + x, rect.top);
        path.lineTo(rect.left + x, rect.top + y);
        path.lineTo(rect.right, rect.top + y);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    private static Path getDiagStripePath(AutoShape shape, Rect rect)
    {
        float y = rect.height() * 0.5f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                y = rect.height() * values[0];
            }
        }
        float x = y * rect.width() / rect.height();
        
        path.moveTo(rect.left, rect.top + y);
        path.lineTo(rect.left + x, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        return path;
    }
    
    private static Path getPlusPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.height(), rect.width()) * 0.25f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.height(), rect.width()) * values[0];
            }
        }
        
        path.moveTo(rect.left, rect.top + x);
        path.lineTo(rect.left + x, rect.top + x);
        path.lineTo(rect.left + x, rect.top);
        path.lineTo(rect.right - x, rect.top);
        path.lineTo(rect.right -x, rect.top + x);
        path.lineTo(rect.right, rect.top + x);
        path.lineTo(rect.right, rect.bottom - x);
        path.lineTo(rect.right - x, rect.bottom - x);
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left + x, rect.bottom);
        path.lineTo(rect.left + x, rect.bottom - x);
        path.lineTo(rect.left, rect.bottom - x);
        path.close();
        
        return path;
    }
    
    private static Path getPlaquePath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.height(), rect.width()) * 0.16f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.height(), rect.width()) * values[0];
            }
        }
        
        rectF.set(rect.right - x, rect.top - x, rect.right + x, rect.top + x);
        path.arcTo(rectF, 180, -90);
        rectF.set(rect.right - x, rect.bottom - x, rect.right + x, rect.bottom + x);
        path.arcTo(rectF, 270, -90);
        rectF.set(rect.left - x, rect.bottom - x, rect.left + x, rect.bottom + x);
        path.arcTo(rectF, 0, -90);
        rectF.set(rect.left - x, rect.top - x, rect.left + x, rect.top + x);
        path.arcTo(rectF, 90, -90);
        path.close();
        
        return path;
    }
    
    private static List<ExtendPath> getCanPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.height(), rect.width()) * 0.175f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.height(), rect.width()) * values[0];
                }
            }
        }
        else
        {
            x = rect.height() * 0.25f;
            if (values != null && values.length > 0)
            {
                if (values[0] != null)
                {
                    x = rect.height() * values[0];
                }
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        BackgroundAndFill bgFill =  fill;
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), 0.4));
        }
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        rectF.set(rect.left, rect.top, rect.right, rect.top + x);
        path.addOval(rectF, Path.Direction.CW);
        
        extendPath.setBackgroundAndFill(bgFill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        
        paths.add(extendPath);
        
        
        extendPath = new ExtendPath();
        path = new Path();
        path.arcTo(rectF, 180, -180);
        rectF.set(rect.left, rect.bottom - x, rect.right, rect.bottom);
        path.arcTo(rectF, 0, 180);
        path.close();
        
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        paths.add(extendPath);
        
        return paths;
    }
    
    private static List<ExtendPath> getCubePath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.height(), rect.width()) * 0.25f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.height(), rect.width()) * values[0];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.addRect(rect.left, rect.top + x, rect.right - x, rect.bottom, Path.Direction.CW);
        extendPath.setBackgroundAndFill(fill);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        BackgroundAndFill bgFill =  fill;
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), 0.2));
        }
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right - x, rect.top + x);
        path.lineTo(rect.left, rect.top + x);
        path.close();
        
        extendPath.setBackgroundAndFill(bgFill);        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), -0.2));
        }
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(rect.right - x, rect.top + x);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom - x);
        path.lineTo(rect.right - x, rect.bottom);
        path.close();
        
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(bgFill);        
        extendPath.setPath(path);
        paths.add(extendPath);
        
        return paths;
    }
    
    private static List<ExtendPath> getBevelPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.height(), rect.width()) * 0.125f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.height(), rect.width()) * values[0];
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        BackgroundAndFill bgFill = fill;
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), 0.2));
        }
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right - x, rect.top + x);
        path.lineTo(rect.left + x, rect.top + x);
        path.close();        
        
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(bgFill);        
        extendPath.setPath(path);
        paths.add(extendPath);
        
       
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), -0.4));
        }
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(rect.right - x, rect.top + x);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.right - x, rect.bottom - x);
        path.close();
        
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(bgFill);        
        extendPath.setPath(path);
        paths.add(extendPath);
        
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), -0.2));
        }
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(rect.left + x, rect.bottom - x);
        path.lineTo(rect.right - x, rect.bottom - x);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
       
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(bgFill);        
        extendPath.setPath(path);
        paths.add(extendPath);
        
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), 0.4));
        }
        
        extendPath = new ExtendPath();
        path = new Path();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.left + x, rect.top + x);
        path.lineTo(rect.left + x, rect.bottom - x);
        path.lineTo(rect.left, rect.bottom);
        path.close();        
        
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(bgFill);        
        extendPath.setPath(path);
        paths.add(extendPath);
        
        extendPath = new ExtendPath();
        path = new Path();
        path.addRect(rect.left + x, rect.top + x, rect.right - x, rect.bottom - x, Path.Direction.CW);
                
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(fill);        
        extendPath.setPath(path);
        paths.add(extendPath);
        
        return paths;
    }
    
    private static Path getDonutPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        float y = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.height(), rect.width()) * 0.25f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.height(), rect.width()) * values[0];
                }
            }
            y = x;
        }
        else
        {
            x = rect.width() * 0.25f;
            y = rect.height() * 0.25f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = rect.width() * values[0];
                    y = rect.height() * values[0];
                }
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        rectF.set(rect.left + x, rect.top + y, rect.right - x, rect.bottom - y);
        path.addOval(rectF, Path.Direction.CCW);
        
        return path;
    }
    
    private static Path getNoSmokingPath(AutoShape shape, Rect rect)
    {
        float len = Math.min(rect.width(), rect.height());
        float rate = 0.2f;
        float x = len * 0.2f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1 && values[0] != null)
        {
            rate = values[0];
        }
       
        x = len * rate;
      
      rectF.set(0, 0, len, len);
      path.addOval(rectF, Direction.CCW);      
          
      double degree = 45;
      if(rate <= 0.25f)
      {
          rate = rate * 0.5f / (0.5f - rate); 
          degree = Math.acos(rate);
      }
      else
      {
          degree = Math.PI / 3 * (0.5f - rate) / 0.25f;
      }

      rectF.set(x, x, len - x, len - x);
      
      path.arcTo(rectF, (float)((Math.PI * 1.75f - degree)/ Math.PI * 180), 
          (float)(degree * 2 / Math.PI * 180), true);
      path.close();
      
     
      path.arcTo(rectF, (float)((Math.PI * 0.75f - degree)/ Math.PI * 180), 
          (float)(degree * 2 / Math.PI * 180), true);
      path.close();

      m.reset();
      m.postScale(rect.width() / (float)len, rect.height() / (float)len);
      path.transform(m);
      path.offset(rect.left, rect.top);
      
      
      return path;
    }
    
    private static Path getBlockArcPath(AutoShape shape, Rect rect)
    {
        float start = 180;
        float end = 0;
        float x = Math.min(rect.width(), rect.height()) * 0.25f;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            if (values != null && values.length >= 3)
            {
                if (values[0] != null)
                {
                    start = values[0] * 10 / 6;
                }
                if (values[1] != null)
                {
                    end = values[1] * 10 / 6;
                }
                if (values[2] != null)
                {
                    x = Math.min(rect.width(), rect.height()) * values[2];
                }
            }
        }
        else
        {
            float angle = 90;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    angle = values[0] * TODEGREE_03;
                }
                else
                {
                    angle = 0;
                }
                
                if (values.length >= 2 && values[1] != null)
                {
                    x = rect.width() * values[1];
                }
                else
                {
                    x = rect.width() * 0.25f;
                }
            }
            else
            {
                angle = 180;
                x = rect.width() * 0.25f;
            }
            
            if (angle >= 0)
            {
                start = angle;
                end = 90 + (90 - angle);
            }
            else
            {
                start = angle +360;
                end = 360 - (start - 180);
            }
        }
        
        if (end >= start)
        {
            rectF.set(rect.left, rect.top, rect.right, rect.bottom);
            path.arcTo(rectF, start, end - start);
            rectF.set(rect.left + x, rect.top + x, rect.right - x, rect.bottom - x);
            path.arcTo(rectF, end, start - end);
        }
        else
        {
            rectF.set(rect.left, rect.top, rect.right, rect.bottom);
            path.arcTo(rectF, start, 360 + end - start);
            rectF.set(rect.left + x, rect.top + x, rect.right - x, rect.bottom - x);
            path.arcTo(rectF, end, start - end - 360);
        }
        path.close();
        
        return path;
    }
    
    private static List<ExtendPath> getFoldedCornerPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.width(), rect.height()) * 0.25f;
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.width(), rect.height()) * values[0];
                }
            }
            
            BackgroundAndFill fill = shape.getBackgroundAndFill();
            
            ExtendPath extendPath = new ExtendPath();
            Path path = new Path();
            path.moveTo(rect.left, rect.top);
            path.lineTo(rect.right, rect.top);
            path.lineTo(rect.right, rect.bottom - x);
            path.lineTo(rect.right - x, rect.bottom);
            path.lineTo(rect.left, rect.bottom);
            path.close();
            
            extendPath.setLine(shape.getLine());
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(fill);
            paths.add(extendPath);
            
            
            BackgroundAndFill bgFill =  fill;
            if (fill != null)
            {
                bgFill = new BackgroundAndFill();
                bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), -0.2));
            }
            
            extendPath = new ExtendPath();
            path = new Path();
            path.moveTo(rect.right - x * (float)Math.sin(Math.toRadians(75)) * (float)Math.sqrt(6) / 3, 
                rect.bottom - x * (float)Math.sin(Math.toRadians(75)) * (float)Math.sqrt(6) / 3);
            path.lineTo(rect.right, rect.bottom - x);
            path.lineTo(rect.right - x, rect.bottom);
            path.close();
            
            extendPath.setLine(shape.getLine());
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }
        else
        {
            float adjust = 0;
            x = Math.min(rect.width(), rect.height()) * 0.125f;
            if (values != null && values.length >= 1)
            {
                x = Math.min(rect.width(), rect.height()) * (1 - values[0]);
            }
            if (rect.height() > rect.width())
            {
                x *= 1.4286;
                adjust = 0.7f;
            }
            else
            {
                adjust = 1.4286f;
            }
            
            ExtendPath extendPath = new ExtendPath();
            Path path = new Path();
            path.moveTo(rect.left, rect.top);
            path.lineTo(rect.right, rect.top);
            path.lineTo(rect.right, rect.bottom - x);
            path.lineTo(rect.right - x * adjust, rect.bottom);
            path.lineTo(rect.left, rect.bottom);
            path.close();            
            BackgroundAndFill fill = shape.getBackgroundAndFill();
            
            extendPath.setLine(shape.getLine());
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(fill);
            paths.add(extendPath);
            
            BackgroundAndFill bgFill =  fill;
            if (fill != null)
            {
                bgFill = new BackgroundAndFill();
                bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), -0.2));
            }
            
            extendPath = new ExtendPath();
            path = new Path();
            path.moveTo(rect.right - x * adjust * (float)Math.sin(Math.toRadians(75)) * (float)Math.sqrt(6) / 3, 
                rect.bottom - x * (float)Math.sin(Math.toRadians(75)) * (float)Math.sqrt(6) / 3);
            path.lineTo(rect.right, rect.bottom - x);
            path.lineTo(rect.right - x * adjust, rect.bottom);
            path.close();
            
            extendPath.setLine(shape.getLine());
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }
        
        return paths;
    }
    
    private static List<ExtendPath> getSmileyFacePath(AutoShape shape, Rect rect)
    {
        float x = rect.height() * 0.04653f * 2;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (shape.isAutoShape07())
            {
                if (values[0] != null)
                {
                    x = rect.height() * values[0] * 2;
                }
            }
            else
            {
                if (values[0] != null)
                {
                    x = rect.height() * (values[0] - 0.77f) * 2;
                }
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(fill);
        paths.add(extendPath);
        
        
        float left = rect.left + (float)rect.width() / 4;
        float right = rect.right - (float)rect.width() / 4;
        float top = rect.bottom - (float)rect.height() / 4 - Math.abs(x);
        float bottom = rect.bottom - (float)rect.height() / 4 + Math.abs(x);
        extendPath = new ExtendPath();
        path = new Path();
        rectF.set(left, top, right, bottom);
        if (x >= 0)
        {
            path.arcTo(rectF, 15, 150);
        }
        else
        {
            path.arcTo(rectF, 195, 150);
        }
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(fill);
        paths.add(extendPath);
        
       
        BackgroundAndFill bgFill =  fill;
        if (fill != null)
        {
            bgFill = new BackgroundAndFill();
            bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
            bgFill.setForegroundColor(ColorUtil.instance().getColorWithTint(fill.getForegroundColor(), -0.2));
        }
        
        left = rect.exactCenterX() - (float)rect.width() / 5;
        right = rect.exactCenterX() - (float)rect.width() / 10;
        top = rect.exactCenterY() - (float)rect.height() / 5;
        bottom = rect.exactCenterY() - (float)rect.height() / 10;

        extendPath = new ExtendPath();
        path = new Path();
        rectF.set(left, top, right, bottom);
        path.addOval(rectF, Path.Direction.CW);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(bgFill);
        paths.add(extendPath);
        
        left = rect.exactCenterX() + (float)rect.width() / 10;
        right = rect.exactCenterX() + (float)rect.width() / 5;

        extendPath = new ExtendPath();
        path = new Path();        
        rectF.set(left, top, right, bottom);
        path.addOval(rectF, Path.Direction.CW);
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        extendPath.setBackgroundAndFill(bgFill);
        paths.add(extendPath);
        
        return paths;
    }
    
    private static Path getSunPath(AutoShape shape, Rect rect)
    {
        Float[] values = shape.getAdjustData();
        float offX = 0;
        float offY = 0;
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                offX = rect.width() * values[0];
                offY = rect.height() * values[0]; 
            }
        }
        else
        {
            offX = rect.width() * 0.25f;
            offY = rect.height() * 0.25f;   
        }
             
        
        //inner oval
        rectF.set(rect.left + offX, rect.top + offY, rect.right - offX, rect.bottom - offY);
        path.addOval(rectF, Direction.CW);
        
        
        //up and down arrow
        path.moveTo( rect.centerX(), rect.top);
        path.lineTo(rect.centerX() + rect.width() / 14, rect.top + offY * 0.75f);
        path.lineTo(rect.centerX() - rect.width() / 14, rect.top + offY * 0.75f);
        path.close();
        
        path.moveTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.centerX() - rect.width() / 14, rect.bottom - offY * 0.75f);
        path.lineTo(rect.centerX() + rect.width() / 14, rect.bottom - offY * 0.75f);
        path.close();
        
        //left and right arrow
        path.moveTo(rect.left, rect.centerY());
        path.lineTo(rect.left + offX * 0.75f, rect.centerY() - rect.height() / 14);
        path.lineTo(rect.left + offX * 0.75f, rect.centerY() + rect.height() / 14);
        path.close();
        
        path.moveTo(rect.right, rect.centerY());
        path.lineTo(rect.right - offX * 0.75f, rect.centerY() + rect.height() / 14);
        path.lineTo(rect.right - offX * 0.75f, rect.centerY() - rect.height() / 14);
        path.close();
        
        //right-bottom arrow
        //arrow header
        float hx = (float)(Math.sqrt(0.5) * rect.width()) / 2;
        float hy = (float)(Math.sqrt(0.5) * rect.height()) / 2;
        
        float mx_tail = (float)(Math.sqrt(0.5) * (rect.width() - offX * 0.75f * 2)) / 2;
        float my_tail = (float)(Math.sqrt(0.5) * (rect.height() - offY * 0.75f * 2)) / 2;
        
        float offLen = (rect.width() + rect.height()) / 28;
        //tangent line of oval: x0x/a^2 + y0y/ b^2 = 1    (x0, y0)---(a/2^0.5, b/2^0.5)        
        offX = (float)(offLen * rect.width() / Math.sqrt(Math.pow(rect.width(), 2) + Math.pow(rect.height(), 2)));
        offY = (float)(offLen * rect.height() / Math.sqrt(Math.pow(rect.width(), 2) + Math.pow(rect.height(), 2)));
        
        float tailX1 = mx_tail + offX;
        float tailY1 = my_tail - offY;
        
        float tailX2 = mx_tail - offX;
        float tailY2 = my_tail + offY;
        
        path.moveTo(rect.centerX() + hx, rect.centerY() + hy);
        path.lineTo(rect.centerX() + tailX1, rect.centerY() + tailY1);
        path.lineTo(rect.centerX() + tailX2, rect.centerY() + tailY2);
        path.close();
        
        
        //left-top arrow
        path.moveTo(rect.centerX() - hx, rect.centerY() - hy);
        path.lineTo(rect.centerX() - tailX1, rect.centerY() - tailY1);
        path.lineTo(rect.centerX() - tailX2, rect.centerY() - tailY2);
        path.close();
        
        // right-top
        path.moveTo(rect.centerX() + hx, rect.centerY() - hy);
        path.lineTo(rect.centerX() + tailX1, rect.centerY() - tailY1);
        path.lineTo(rect.centerX() + tailX2, rect.centerY() - tailY2);
        path.close();
        
        //left-bottom
        path.moveTo(rect.centerX() - hx, rect.centerY() + hy);
        path.lineTo(rect.centerX() - tailX1, rect.centerY() + tailY1);
        path.lineTo(rect.centerX() - tailX2, rect.centerY() + tailY2);
        path.close();
        
        return path;
    }
    
    private static Path getHeartPath(AutoShape shape, Rect rect)
    {
        path.moveTo(0, 30);
        path.cubicTo(0, -10,
            40, 0 ,
            50, 20);
        
        path.cubicTo(60, 0,
            100, -10,
            100, 30);
        
        path.cubicTo(100, 60,
            60, 100,
            50, 100);
        
        path.cubicTo(40, 100,
            0, 60,
            0, 30);
        path.close();
        
        m.reset();
        m.postScale(rect.width() / 100f, rect.height() / 100f);
        path.transform(m);
        
        path.offset(rect.left, rect.top);
        
        return path;
    }
    
    private static Path getLightningBoltPath(AutoShape shape, Rect rect)
    {
        float w = rect.width();
        float h = rect.height();
        
        path.moveTo(rect.left + w * 0.4f, rect.top);
        path.lineTo(rect.left + w * 0.6f, rect.top + h * 0.2857f);
        path.lineTo(rect.left + w * 0.5167f, rect.top + h * 0.3f);
        path.lineTo(rect.right - w * 0.23f, rect.bottom - h * 0.44f);
        path.lineTo(rect.right - w * 0.3448f, rect.bottom - h * 0.4f);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left + w * 0.4615f, rect.bottom - h * 0.3167f);
        path.lineTo(rect.left + w * 0.5455f, rect.bottom - h * 0.35f);
        path.lineTo(rect.left + w * 0.25f, rect.top + h * 0.4545f);
        path.lineTo(rect.left + w * 0.35f, rect.top + h * 0.3921f);
        path.lineTo(rect.left, rect.top + h * 0.19f);
        path.close();
        
        return path;
    }
    
    private static Path getMoonPath(AutoShape shape, Rect rect)
    {
        float x = rect.width() * 0.5f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = rect.width() * (1 - values[0]);
            }
        }
        
        rectF.set(rect.left, rect.top, rect.right * 2 - rect.left, rect.bottom);
        path.arcTo(rectF, 90, 180);
        rectF.set(rect.right - x, rect.top, rect.right + x, rect.bottom);
        path.arcTo(rectF, 270, -180);
        
        return path;
    }
    
    private static Path getCloudPath(AutoShape shape, Rect rect)
    {
        float len = 468;
        path.reset();
        
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
        
        m.reset();
        m.postScale(rect.width() / len, rect.height() / len);
        path.transform(m);
        
        path.offset(rect.left, rect.top);
        return path;
    }
    
    private static List<ExtendPath> getArcPath(AutoShape shape, Rect rect)
    {
        float start = 0;
        float end = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            if (values != null && values.length >= 2)
            {
                start = values[0] * TODEGREE_07;
                end = values[1] * TODEGREE_07;
            }
            else
            {
                start = 270;
                end = 0;
            }
        }
        else
        {
            if (values != null && values.length >= 1)
            {
                if (values[0] != null)
                {
                    start = values[0] / 3;
                }
                if (values.length >= 2 && values[1] != null)
                {
                    end = values[1] / 3;
                }
            }
            else
            {
                start = 270;
                end = 0;
            }
            
            if (start < 0)
            {
                start += 360;
            }
            if (end < 0)
            {
                end += 360;
            }
        }
        
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();
        if(fill != null)
        {
            extendPath = new ExtendPath();
            path = new Path();
            
            path.moveTo(rect.exactCenterX(), rect.exactCenterY());
            rectF.set(rect.left, rect.top, rect.right, rect.bottom);
            path.arcTo(rectF, start, (end - start + 360) % 360);
            path.close();
            
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(fill);
            paths.add(extendPath);
        }
        
        if(shape.hasLine())
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.left, rect.top, rect.right, rect.bottom);
            path.arcTo(rectF, start, (end - start + 360) % 360);
            
            extendPath.setPath(path);
            extendPath.setLine(shape.getLine());
            paths.add(extendPath);
        }
        return paths;
    }
    
    private static List<ExtendPath> getBracketPairPath(AutoShape shape, Rect rect)
    {
        float x = 0f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1 && values[0] !=  null)
        {
            x = Math.min(rect.width(), rect.height()) * values[0];
        }
        else
        {
            x = Math.min(rect.width(), rect.height()) * 0.18f;
        }
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        ExtendPath extendPath = null;
        Path path = null;
        if(bgFill != null)
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.left, rect.top, rect.right, rect.bottom);
            float[] radii = new float[]{x, x, x, x, x, x, x, x};
            path.addRoundRect(rectF, radii, Path.Direction.CW);
            
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }
        
        if(shape.hasLine())
        {
            extendPath = new ExtendPath();
            path = new Path();
            
            rectF.set(rect.right - x * 2, rect.top, rect.right, rect.top + x * 2);
            path.arcTo(rectF, 270, 90);
            rectF.set(rect.right - x * 2, rect.bottom - x * 2, rect.right, rect.bottom);
            path.arcTo(rectF, 0, 90);
            path.moveTo(rect.left + x, rect.bottom);
            rectF.set(rect.left, rect.bottom - x * 2, rect.left + x * 2, rect.bottom);
            path.arcTo(rectF, 90, 90);
            rectF.set(rect.left, rect.top, rect.left + x * 2, rect.top + x * 2);
            path.arcTo(rectF, 180, 90);
            
            extendPath.setPath(path);
            extendPath.setLine(shape.getLine());
            paths.add(extendPath);
        }
        
        return paths;
    }
    
    private static List<ExtendPath> getLeftBracketPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            
            if (values != null && values.length >= 1)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            else
            {
                x = Math.min(rect.width(), rect.height()) * 0.08f;
            }
        }
        else
        {            
            if (values != null && values.length >= 1 && values[0] !=  null)
            {
                if (values[0] != null)
                {
                    x = rect.height() * values[0];
                }
            }
            else
            {
                x = rect.height() * 0.08f;
            }
        }
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = null;
        Path path = null;
        if(bgFill != null)
        {
            extendPath = new ExtendPath();
            path = new Path();
            
            rectF.set(rect.left, rect.bottom - x * 2, rect.right * 2 - rect.left, rect.bottom);
            path.arcTo(rectF, 90, 90);
            rectF.set(rect.left, rect.top, rect.right * 2 - rect.left, rect.top + x * 2);
            path.arcTo(rectF, 180, 90);
            path.close();
            
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }
        
        if(shape.hasLine())
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.left, rect.bottom - x * 2, rect.right * 2 - rect.left, rect.bottom);
            path.arcTo(rectF, 90, 90);
            rectF.set(rect.left, rect.top, rect.right * 2 - rect.left, rect.top + x * 2);
            path.arcTo(rectF, 180, 90);
            
            extendPath.setPath(path);
            extendPath.setLine(shape.getLine());
            paths.add(extendPath);
            
        }
       
        return paths;
    }
    
    private static List<ExtendPath> getRightBracketPath(AutoShape shape, Rect rect)
    {
        float x = 0;
        Float[] values = shape.getAdjustData();
        
        if(shape.isAutoShape07())
        {
            if (values != null && values.length >= 1&& values[0] !=  null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
            else
            {
                x = Math.min(rect.width(), rect.height()) * 0.08f;
            }
        }
        else
        {
            if (values != null && values.length >= 1 && values[0] !=  null)
            {
                x = rect.height() * values[0];
            }
            else
            {
                x = rect.height() * 0.08f;
            }
        }
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = null;
        Path path = null;
        if(bgFill != null)
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.left * 2 - rect.right, rect.top, rect.right, rect.top + x * 2);
            path.arcTo(rectF, 270, 90);
            rectF.set(rect.left * 2 - rect.right, rect.bottom - x * 2, rect.right, rect.bottom);
            path.arcTo(rectF, 0, 90);
            path.close();
            
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }

        if(shape.hasLine())
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.left * 2 - rect.right, rect.top, rect.right, rect.top + x * 2);
            path.arcTo(rectF, 270, 90);
            rectF.set(rect.left * 2 - rect.right, rect.bottom - x * 2, rect.right, rect.bottom);
            path.arcTo(rectF, 0, 90);
            
            extendPath.setPath(path);
            extendPath.setLine(shape.getLine());
            paths.add(extendPath);
        }
        
        
        return paths;
    }
    
    private static List<ExtendPath> getBracePairPath(AutoShape shape, Rect rect)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.08f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = Math.min(rect.width(), rect.height()) * values[0];
            }
        }
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = null;
        Path path = null;
        if(bgFill != null)
        {
            extendPath = new ExtendPath();
            path = new Path();
            
            rectF.set(rect.right - x * 3, rect.top, rect.right - x, rect.top + x * 2);
            path.arcTo(rectF, 270, 90);
            rectF.set(rect.right - x, rect.exactCenterY() - x * 2, rect.right + x, 
                rect.exactCenterY());
            path.arcTo(rectF, 180, -90);
            rectF.set(rect.right - x, rect.exactCenterY(), rect.right + x, 
                rect.exactCenterY() + x * 2);
            path.arcTo(rectF, 270, -90);
            rectF.set(rect.right - x * 3, rect.bottom - x * 2, rect.right - x, rect.bottom);
            path.arcTo(rectF, 0, 90);
            
            rectF.set(rect.left + x, rect.bottom - x * 2, rect.left + x * 3, rect.bottom);
            path.arcTo(rectF, 90, 90);
            rectF.set(rect.left - x, rect.exactCenterY(), rect.left + x, 
                rect.exactCenterY() + x * 2);
            path.arcTo(rectF, 0, -90);
            rectF.set(rect.left - x, rect.exactCenterY() - x * 2, rect.left + x, 
                rect.exactCenterY());
            path.arcTo(rectF, 90, -90);
            rectF.set(rect.left + x, rect.top, rect.left + x * 3, rect.top + x * 2);
            path.arcTo(rectF, 180, 90);
            path.close();
            
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }
        
        if(shape.hasLine())
        {
            extendPath = new ExtendPath();
            path = new Path();
            
            path.moveTo(rect.right - x * 2, rect.top);
            rectF.set(rect.right - x * 3, rect.top, rect.right - x, rect.top + x * 2);
            path.arcTo(rectF, 270, 90);
            rectF.set(rect.right - x, rect.exactCenterY() - x * 2, rect.right + x, 
                rect.exactCenterY());
            path.arcTo(rectF, 180, -90);
            rectF.set(rect.right - x, rect.exactCenterY(), rect.right + x, 
                rect.exactCenterY() + x * 2);
            path.arcTo(rectF, 270, -90);
            rectF.set(rect.right - x * 3, rect.bottom - x * 2, rect.right - x, rect.bottom);
            path.arcTo(rectF, 0, 90);
            
            path.moveTo(rect.left + x * 2, rect.bottom);
            rectF.set(rect.left + x, rect.bottom - x * 2, rect.left + x * 3, rect.bottom);
            path.arcTo(rectF, 90, 90);
            rectF.set(rect.left - x, rect.exactCenterY(), rect.left + x, 
                rect.exactCenterY() + x * 2);
            path.arcTo(rectF, 0, -90);
            rectF.set(rect.left - x, rect.exactCenterY() - x * 2, rect.left + x, 
                rect.exactCenterY());
            path.arcTo(rectF, 90, -90);
            rectF.set(rect.left + x, rect.top, rect.left + x * 3, rect.top + x * 2);
            path.arcTo(rectF, 180, 90);
            
            extendPath.setPath(path);
            extendPath.setLine(shape.getLine());
            paths.add(extendPath);
        }
        
        return paths;
    }
    
    private static List<ExtendPath> getLeftBracePath(AutoShape shape, Rect rect)
    {
        float x = 0;
        float y = rect.height() * 0.5f;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.width(), rect.height()) * 0.08333f;
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.width(), rect.height()) * values[0];
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1];
                }
            }
        }
        else
        {
            x = rect.height() * 0.08333f;
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = rect.height() * values[0];
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1];
                }
            }
        }
       
        if(rect.top + y + x * 2 > rect.bottom)
        {
            x = (rect.height() - y) / 2;
        }
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = null;
        Path path = null;
        if(bgFill != null)
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.exactCenterX(), rect.bottom - x * 2, 
                rect.right + (float)rect.width() / 2, rect.bottom);
            path.arcTo(rectF, 90, 90);
            rectF.set(rect.left - (float)rect.width() / 2, rect.top + y, 
                rect.exactCenterX(), rect.top + y + x * 2);
            path.arcTo(rectF, 0, -90);
            rectF.set(rect.left - (float)rect.width() / 2, rect.top + y - x * 2, 
                rect.exactCenterX(), rect.top + y);
            path.arcTo(rectF, 90, -90);
            rectF.set(rect.exactCenterX(), rect.top, 
                rect.right + (float)rect.width() / 2, rect.top + x * 2);
            path.arcTo(rectF, 180, 90);
            path.close();
            
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }
        
        if(shape.hasLine())
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.exactCenterX(), rect.bottom - x * 2, 
                rect.right + (float)rect.width() / 2, rect.bottom);
            path.arcTo(rectF, 90, 90);
            rectF.set(rect.left - (float)rect.width() / 2, rect.top + y, 
                rect.exactCenterX(), rect.top + y + x * 2);
            path.arcTo(rectF, 0, -90);
            rectF.set(rect.left - (float)rect.width() / 2, rect.top + y - x * 2, 
                rect.exactCenterX(), rect.top + y);
            path.arcTo(rectF, 90, -90);
            rectF.set(rect.exactCenterX(), rect.top, 
                rect.right + (float)rect.width() / 2, rect.top + x * 2);
            path.arcTo(rectF, 180, 90);
            
            extendPath.setPath(path);
            extendPath.setLine(shape.getLine());
            paths.add(extendPath);
        }
        
        return paths;
    }
    
    private static List<ExtendPath> getRightBracePath(AutoShape shape, Rect rect)
    {
        float x = 0;
        float y = rect.height() * 0.5f;
        Float[] values = shape.getAdjustData();
        if (shape.isAutoShape07())
        {
            x = Math.min(rect.width(), rect.height()) * 0.08333f;
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = Math.min(rect.width(), rect.height()) * values[0];
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1];
                }
            }
        }
        else
        {
            x = rect.height() * 0.08333f;
            if (values != null && values.length >= 2)
            {
                if (values[0] != null)
                {
                    x = rect.height() * values[0];
                }
                if (values[1] != null)
                {
                    y = rect.height() * values[1];
                }
            }
        }
        
        if(rect.top + y + x * 2 > rect.bottom)
        {
            x = (rect.height() - y) / 2;
        }
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        
        ExtendPath extendPath = null;
        Path path = null;
        if(bgFill != null)
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.left - (float)rect.width() / 2, rect.top, 
                (float)(rect.right + rect.left) / 2, rect.top + x * 2);
            path.arcTo(rectF, 270, 90);
            rectF.set((float)(rect.right + rect.left) / 2, rect.top + y - x * 2, 
                rect.right + (float)rect.width() / 2, rect.top + y);
            path.arcTo(rectF, 180, -90);
            rectF.set((float)(rect.right + rect.left) / 2, rect.top + y, 
                rect.right + (float)rect.width() / 2, rect.top + y + x * 2);
            path.arcTo(rectF, 270, -90);
            rectF.set(rect.left - (float)rect.width() / 2, rect.bottom - x * 2, 
                (float)(rect.right + rect.left) / 2, rect.bottom);
            path.arcTo(rectF, 0, 90);
            path.close();
            
            extendPath.setPath(path);
            extendPath.setBackgroundAndFill(bgFill);
            paths.add(extendPath);
        }
        
        if(shape.hasLine())
        {
            extendPath = new ExtendPath();
            path = new Path();
            rectF.set(rect.left - (float)rect.width() / 2, rect.top, 
                (float)(rect.right + rect.left) / 2, rect.top + x * 2);
            path.arcTo(rectF, 270, 90);
            rectF.set((float)(rect.right + rect.left) / 2, rect.top + y - x * 2, 
                rect.right + (float)rect.width() / 2, rect.top + y);
            path.arcTo(rectF, 180, -90);
            rectF.set((float)(rect.right + rect.left) / 2, rect.top + y, 
                rect.right + (float)rect.width() / 2, rect.top + y + x * 2);
            path.arcTo(rectF, 270, -90);
            rectF.set(rect.left - (float)rect.width() / 2, rect.bottom - x * 2, 
                (float)(rect.right + rect.left) / 2, rect.bottom);
            path.arcTo(rectF, 0, 90);
            
            extendPath.setPath(path);
            extendPath.setLine(shape.getLine());
            paths.add(extendPath);
        }
        return paths;
    }    
}
