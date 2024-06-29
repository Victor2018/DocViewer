/*
 * 文件名称:          LinePathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:03:57
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.line;

import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.autoshape.ExtendPath;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.LineArrowPathBuilder;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.Arrow;
import com.nvqquy98.lib.doc.office.common.shape.LineShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;

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
public class LinePathBuilder
{
    private static List<ExtendPath> paths = new ArrayList<ExtendPath>();    
    
    /**
     * 
     * @param shape
     * @param rect
     * @param zoom
     * @return
     */
    public static List<ExtendPath> getLinePath(LineShape shape, Rect rect, float zoom)
    {
        paths.clear();
        
        switch(shape.getShapeType())
        {
            case ShapeTypes.Line:                                           // 直线
            case ShapeTypes.StraightConnector1:                            // 箭头，双箭头
                return getStraightConnectorPath(shape, rect, zoom);
                
            case ShapeTypes.BentConnector2:
            	return getBentConnectorPath2(shape, rect, zoom);
            	
            case ShapeTypes.BentConnector3:                                 // 肘形连接符，肘形箭头连接符，肘形双箭头连接符
                return getBentConnectorPath3(shape, rect, zoom);
                
            case ShapeTypes.CurvedConnector2:
            	return getCurvedConnector2Path(shape, rect, zoom);
            	
            case ShapeTypes.CurvedConnector3:
            	return getCurvedConnector3Path(shape, rect, zoom);
            	
            case ShapeTypes.CurvedConnector4:
            	return getCurvedConnector4Path(shape, rect, zoom);
            	
            case ShapeTypes.CurvedConnector5:                               // 曲线连接符，曲线箭头连接符，曲线双箭头连接符
                return getCurvedConnector4Path(shape, rect, zoom);
        }
        return null;
    }
    
    private static List<ExtendPath> getStraightConnectorPath(LineShape shape, Rect rect, float zoom)
    {
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path();       

        
        int x0 = rect.left;
        int y0 = rect.top;
        int x1 = rect.right;
        int y1 = rect.bottom;
        double lineLength = Math.sqrt(rect.width() * rect.width() + rect.height() * rect.height());
        if(shape.getStartArrowhead() && 
            (shape.getStartArrow().getType() == Arrow.Arrow_Triangle || shape.getStartArrow().getType() == Arrow.Arrow_Stealth))
        {
            int arrowLength = LineArrowPathBuilder.getArrowLength(shape.getStartArrow(), shape.getLine().getLineWidth());
            if(Math.abs(x1 - x0) >= 1)
            {
                x0 += (arrowLength * zoom) / lineLength * (x1 - x0) * 0.75f;
            }
            if(Math.abs(y1 - y0) >= 1)
            {
                y0 += (arrowLength * zoom) / lineLength * (y1 - y0) * 0.75f;
            }
        }
        
        if(shape.getEndArrowhead() && 
            (shape.getEndArrow().getType() == Arrow.Arrow_Triangle || shape.getEndArrow().getType() == Arrow.Arrow_Stealth))
        {
            int arrowLength = LineArrowPathBuilder.getArrowLength(shape.getEndArrow(), shape.getLine().getLineWidth());
            if(Math.abs(x1 - x0) >= 1)
            {                
                x1 += (arrowLength * zoom) / lineLength * (x0 - x1) * 0.75f;
            }
            if(Math.abs(y1 - y0) >= 1)
            {
                y1 += (arrowLength * zoom) / lineLength * (y0 - y1) * 0.75f;
            }
        }
        
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        if(bgFill == null)
        {
            bgFill = shape.getLine().getBackgroundAndFill();
        }
        extendPath.setBackgroundAndFill(bgFill);
        extendPath.setLine(shape.getLine());
        extendPath.setPath(path);
        paths.add(extendPath);        
        
        if (shape.getEndArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getDirectLineArrowPath(                
                rect.left,
                rect.top,
                rect.right,
                rect.bottom,
                shape.getEndArrow(),
                shape.getLine().getLineWidth(),
                zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getEndArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        
        if (shape.getStartArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getDirectLineArrowPath(
                rect.right, 
                rect.bottom, 
                rect.left, 
                rect.top,
                shape.getStartArrow(),
                shape.getLine().getLineWidth(),
                zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getStartArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        return paths;
    }
    
    private static List<ExtendPath> getBentConnectorPath2(LineShape shape, Rect rect, float zoom)
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
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path(); 
        
        int x0 = rect.left;
        int y0 = rect.top;
        int x1 = rect.right;
        int y1 = rect.bottom;
        if(shape.getStartArrowhead() && 
                (shape.getStartArrow().getType() == Arrow.Arrow_Triangle || shape.getStartArrow().getType() == Arrow.Arrow_Stealth))
            {
                int length = LineArrowPathBuilder.getArrowLength(shape.getStartArrow(), shape.getLine().getLineWidth());
                x0 += Math.ceil((length * zoom) / Math.abs(x1 - x0) * (x1- x0) * 0.75f);
            }
            
            if(shape.getEndArrowhead() && 
                (shape.getEndArrow().getType() == Arrow.Arrow_Triangle || shape.getEndArrow().getType() == Arrow.Arrow_Stealth))
            {
                int length = LineArrowPathBuilder.getArrowLength(shape.getEndArrow(), shape.getLine().getLineWidth());
                y1 += Math.ceil((length * zoom) / Math.abs(y1 - y0) * (y0- y1) * 0.75f);
            }
            
        //isBentConnector2
        path.moveTo(x0, y0);
        path.lineTo(rect.right, rect.top);
        path.lineTo(x1, y1);
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        if(bgFill == null)
        {
        	 bgFill = shape.getLine().getBackgroundAndFill();
        }       
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        if (shape.getEndArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getDirectLineArrowPath(
                rect.right, 
                y1, 
                rect.right, 
                rect.bottom,
                shape.getEndArrow(),
                shape.getLine().getLineWidth(),
                zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getEndArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        if (shape.getStartArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getDirectLineArrowPath(
                x0, 
                rect.top, 
                rect.left, 
                rect.top,
                shape.getStartArrow(),
                shape.getLine().getLineWidth(),
                zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getStartArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        return paths;
    }
    
    private static List<ExtendPath> getBentConnectorPath3(LineShape shape, Rect rect, float zoom)
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
        
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path(); 
        
        int x0 = rect.left;
        int y0 = rect.top;
        int x1 = rect.right;
        int y1 = rect.bottom;
        
        if(shape.getStartArrowhead() && 
                (shape.getStartArrow().getType() == Arrow.Arrow_Triangle || shape.getStartArrow().getType() == Arrow.Arrow_Stealth))
            {
                int length = LineArrowPathBuilder.getArrowLength(shape.getStartArrow(), shape.getLine().getLineWidth());
                x0 += Math.ceil((length * zoom) / Math.abs(x1 - x0) * (x1- x0) * 0.75f);
            }
            
            if(shape.getEndArrowhead() && 
                (shape.getEndArrow().getType() == Arrow.Arrow_Triangle || shape.getEndArrow().getType() == Arrow.Arrow_Stealth))
            {
                int length = LineArrowPathBuilder.getArrowLength(shape.getEndArrow(), shape.getLine().getLineWidth());
                x1 += Math.ceil((length * zoom) / Math.abs(x1 - x0) * (x0- x1) * 0.75f);
            }
            
        
            
        path.moveTo(x0, y0);
        path.lineTo(rect.left + x, rect.top);
        path.lineTo(rect.left + x, rect.bottom);
        path.lineTo(x1, y1);
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        if(bgFill == null)
        {
        	 bgFill = shape.getLine().getBackgroundAndFill();
        }       
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        if (shape.getEndArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getDirectLineArrowPath(
                rect.left + x, 
                rect.bottom, 
                rect.right, 
                rect.bottom,
                shape.getEndArrow(),
                shape.getLine().getLineWidth(),
                zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getEndArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        if (shape.getStartArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getDirectLineArrowPath(
                rect.left + x, 
                rect.top, 
                rect.left, 
                rect.top,
                shape.getStartArrow(),
                shape.getLine().getLineWidth(),
                zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getStartArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        
        return paths;
    }
    
    private static List<ExtendPath> getCurvedConnector2Path(LineShape shape, Rect rect, float zoom)
    {
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path(); 
        path.reset();
        path.moveTo(rect.left, rect.top);
        path.quadTo(rect.right, rect.top, rect.right, rect.bottom);
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        if(bgFill == null)
        {
        	 bgFill = shape.getLine().getBackgroundAndFill();
        }               
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        if (shape.getEndArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getQuadBezArrowPath(
                    rect.left, 
                    rect.top, 
                    rect.right, 
                    rect.top, 
                    rect.right, 
                    rect.bottom,
                    shape.getEndArrow(),
                    shape.getLine().getLineWidth(),
                    zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getEndArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        if (shape.getStartArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path= LineArrowPathBuilder.getQuadBezArrowPath(
                    rect.right,
                    rect.bottom,
                    rect.right, 
                    rect.top, 
                    rect.left, 
                    rect.top, 
                    shape.getStartArrow(),
                    shape.getLine().getLineWidth(),
                    zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getStartArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        
        return paths;
    }
    
    private static List<ExtendPath> getCurvedConnector3Path(LineShape shape, Rect rect, float zoom)
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
        
        ExtendPath extendPath = null;
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        if(bgFill == null)
        {
        	 bgFill = shape.getLine().getBackgroundAndFill();
        }
        
        PointF startArrowTailCenter = null;
        PointF endArrowTailCenter = null;
        
        if (shape.getEndArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            ArrowPathAndTail arrowPathAndTail= LineArrowPathBuilder.getQuadBezArrowPath(
                    rect.left + x, 
                    rect.exactCenterY(), 
                    rect.left + x, 
                    rect.bottom, 
                    rect.right, 
                    rect.bottom,
                    shape.getEndArrow(),
                    shape.getLine().getLineWidth(),
                    zoom);
            
            byte arrowType = shape.getEndArrow().getType();
            if(arrowType == Arrow.Arrow_Triangle || arrowType == Arrow.Arrow_Stealth)
            {
            	endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
            }
            
            extendPath.setPath(arrowPathAndTail.getArrowPath());
            if(arrowType != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        
        if (shape.getStartArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            ArrowPathAndTail arrowPathAndTail = LineArrowPathBuilder.getQuadBezArrowPath(
                    rect.left + x,
                    rect.exactCenterY(),
                    rect.left + x, 
                    rect.top, 
                    rect.left, 
                    rect.top, 
                    shape.getStartArrow(),
                    shape.getLine().getLineWidth(),
                    zoom);
            
            byte arrowType = shape.getStartArrow().getType();
            if(arrowType == Arrow.Arrow_Triangle || arrowType == Arrow.Arrow_Stealth)
            {
            	startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
            }
            
            extendPath.setPath(arrowPathAndTail.getArrowPath());
            if(arrowType != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        
        extendPath = new ExtendPath();
        Path path = new Path(); 
        path.reset();
        if(startArrowTailCenter != null)
        {
        	startArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(rect.left, rect.top, startArrowTailCenter.x, startArrowTailCenter.y, shape.getStartArrow().getType());
        	path.moveTo(startArrowTailCenter.x, startArrowTailCenter.y);
        }
        else
        {
        	path.moveTo(rect.left, rect.top);
        }
        
        path.quadTo(rect.left + x, rect.top, rect.left + x, rect.exactCenterY());
        path.moveTo(rect.left + x, rect.exactCenterY());
        
        if(endArrowTailCenter != null)
        {
        	endArrowTailCenter = LineArrowPathBuilder.getReferencedPosition(rect.right, rect.bottom, endArrowTailCenter.x, endArrowTailCenter.y, shape.getEndArrow().getType());
        	path.quadTo(rect.left + x, rect.bottom, endArrowTailCenter.x, endArrowTailCenter.y);
        }
        else
        {
        	path.quadTo(rect.left + x, rect.bottom, rect.right, rect.bottom);
        }
        
        
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        return paths;
    }
    
    private static List<ExtendPath> getCurvedConnector4Path(LineShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.5f;
        float y = rect.height() * 0.5f;
        Float[] values = shape.getAdjustData();
        if (values != null && values.length >= 1)
        {
            if (values[0] != null)
            {
                x = rect.width() * values[0];
            }
            
            if(values[1] != null)
            {
            	y = rect.height() * values[1];
            }
        }
        
        float x0 = rect.left + x;
        float y0 = rect.top + y / 2;
        
        float x1 = (x0 + rect.right) / 2f;
        float y1 = rect.top + y ;
        ExtendPath extendPath = new ExtendPath();
        Path path = new Path(); 
        path.reset();
        
        path.moveTo(rect.left, rect.top);
        path.quadTo(x0, rect.top, x0, y0);
        
        path.moveTo(x0, y0);
        path.quadTo(x0, y1, x1, y1);
        
        path.moveTo(x1, y1);
        path.quadTo(rect.right, y1, rect.right, rect.bottom);
        
        BackgroundAndFill bgFill = shape.getBackgroundAndFill();
        if(bgFill == null)
        {
        	 bgFill = shape.getLine().getBackgroundAndFill();
        }               
        extendPath.setPath(path);
        extendPath.setLine(shape.getLine());
        paths.add(extendPath);
        
        if (shape.getEndArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path = LineArrowPathBuilder.getQuadBezArrowPath(
                    x1, 
                    y1, 
                    rect.right, 
                    y1, 
                    rect.right, 
                    rect.bottom,
                    shape.getEndArrow(),
                    shape.getLine().getLineWidth(),
                    zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getEndArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        if (shape.getStartArrowhead())
        {
            extendPath = new ExtendPath();
            extendPath.setArrowFlag(true);
            path= LineArrowPathBuilder.getQuadBezArrowPath(
                    x0,
                    y0,
                    x0, 
                    rect.top, 
                    rect.left, 
                    rect.top, 
                    shape.getStartArrow(),
                    shape.getLine().getLineWidth(),
                    zoom).getArrowPath();
            extendPath.setPath(path);
            if(shape.getStartArrow().getType() != Arrow.Arrow_Arrow)
            {
                extendPath.setBackgroundAndFill(bgFill);
            }
            else
            {
                extendPath.setLine(shape.getLine());
            }
            paths.add(extendPath);
        }
        
        return paths;
    } 
}
