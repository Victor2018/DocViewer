/*
 * 文件名称:          LineArrowPathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:09:14
 */
package com.nvqquy98.lib.doc.office.common.autoshape;

import com.nvqquy98.lib.doc.office.common.shape.Arrow;

import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-10-25
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class LineArrowPathBuilder
{
    private final static int SMALL = 5;
    private final static int MEDIUM = 9;
    private final static int LARGE = 13;
    
    static PointF p = new PointF();
    
    private static int getArrowWidth(Arrow arrow, int lineWidth)
    {
        int width = MEDIUM;      
        
        if(lineWidth < 3)
        {
//            switch(arrow.getWidth())
//            {
//                case 0:
//                    width = SMALL;
//                    break;
//                case 1:
//                    width = MEDIUM;
//                    break;
//                case 2:
//                    width = LARGE;
//                    break;
//            }
        }
        else
        {
            width = lineWidth * 3;
//            switch(arrow.getWidth())
//            {
//                case 0:
//                    width = lineWidth * 2;
//                    break;
//                case 1:
//                    width = lineWidth * 3;
//                    break;
//                case 2:
//                    width = lineWidth * 5;
//                    break;                 
//            }
        }
        return width;
    }
    public static int getArrowLength(Arrow arrow, int lineWidth)
    {
        int length = MEDIUM;        
        
        if(lineWidth < 3)
        {
//            switch(arrow.getLength())
//            {
//                case 0:
//                    length = SMALL;
//                    break;
//                case 1:
//                    length = MEDIUM;
//                    break;
//                case 2:
//                    length = LARGE;
//                    break;                 
//            }
        }
        else
        {
            length = lineWidth * 3;
//            switch(arrow.getLength())
//            {
//                case 0:
//                    length = lineWidth * 2;
//                    break;
//                case 1:
//                    length = lineWidth * 3;
//                    break;
//                case 2:
//                    length = lineWidth * 5;
//                    break;                 
//            }
        }
        
        return length;
    }
    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param arrowWidth
     * @param arrowLength
     * @return
     */
    public static Path getDirectLineArrowPath(float startX, float startY,float endX, float endY, Arrow arrow, int lineWidth)
    {
        return getDirectLineArrowPath(startX, startY, endX, endY, arrow, lineWidth, 1.0f);
    }
    
    public static Path getDirectLineArrowPath(float startX, float startY,float endX, float endY, Arrow arrow, int lineWidth, float zoom)
    {
        int width = getArrowWidth(arrow, lineWidth);
        int length = getArrowLength(arrow, lineWidth);       
        
        float ratio = (float)(length * zoom / Math.sqrt(Math.pow(endX - startX, 2)+ Math.pow(endY - startY, 2)));
        startY = endY - (endY - startY) * ratio;
        startX = endX - (endX - startX) * ratio;
        return buildArrowPath(startX, startY, endX, endY, width * zoom, length * zoom, arrow.getType());
    }
    
    public static Path getQuadBezArrowPath(float startX, float startY,
        float ctrlX, float ctrlY,
        float endX, float endY, Arrow arrow, int lineWidth)
    {
        return getQuadBezArrowPath(startX, startY,
            ctrlX, ctrlY,
            endX, endY, 
            arrow, lineWidth, 1.f);
    }
    
    public static Path getQuadBezArrowPath(float startX, float startY,
        float ctrlX, float ctrlY,
        float endX, float endY, 
        Arrow arrow, int lineWidth, float zoom)
    {
    	float width = getArrowWidth(arrow, lineWidth) * zoom;
        float length = getArrowLength(arrow, lineWidth) * zoom;
        
        float r = 0.9f;
        float offRatio = 0.01f;
        PointF  end = quadBezComputePoint(startX, startY, ctrlX, ctrlY, endX, endY, r);
        int dist = (int)Math.round(Math.sqrt(Math.pow(end.x - endX, 2) + Math.pow(end.y - endY, 2)));
        Boolean inc = null;
        while(Math.abs(dist - length) > 1 && r < 1.0f && r > 0)
        {
            if(dist - length > 1)
            {
                r += offRatio;
                if(inc != null && !inc)
                {
                    offRatio *= 0.1;
                    r -= offRatio;
                }
                inc = true;
            }
            else
            {
                r -= offRatio;
                if(inc != null && inc)
                {
                    offRatio *= 0.1;
                    r += offRatio;
                }

                inc = false;
            }
            
            end = quadBezComputePoint(startX, startY, ctrlX, ctrlY, endX, endY, r);
            dist = (int)Math.round(Math.sqrt((end.x - endX) *(end.x - endX) + (end.y - endY) * (end.y - endY)));
        }
        
        
        return buildArrowPath(end.x, end.y, endX, endY, width, length, arrow.getType());
    }
    
    public static Path getCubicBezArrowPath(float startX, float startY,
        float ctrl1X, float ctrl1Y,
        float ctrl2X, float ctrl2Y,
        float endX, float endY,
        Arrow arrow, int lineWidth)
    {
        return getCubicBezArrowPath(startX, startY,
            ctrl1X, ctrl1Y,
            ctrl2X, ctrl2Y,
            endX, endY,
            arrow, lineWidth, 1.f);
    }
    
    public static Path getCubicBezArrowPath(float startX, float startY,
        float ctrl1X, float ctrl1Y,
        float ctrl2X, float ctrl2Y,
        float endX, float endY,
        Arrow arrow, int lineWidth, float zoom)
    {
        int width = getArrowWidth(arrow, lineWidth);
        int length = getArrowLength(arrow, lineWidth);
        
        float r = 0.9f;
        float offRatio = 0.01f;
        PointF  end = cubicBezComputePoint(startX, startY, ctrl1X, ctrl1Y, ctrl2X, ctrl2Y, endX, endY, r);
        int dist = (int)Math.round(Math.sqrt(Math.pow(end.x - endX, 2) + Math.pow(end.y - endY, 2)));
        Boolean inc = null;
        while(Math.abs(dist - length) > 1 && r < 1.0f && r > 0)
        {
            if(dist - length > 1)
            {
                r += offRatio;
                if(inc != null && !inc)
                {
                    offRatio *= 0.1;
                    r -= offRatio;
                }
                inc = true;
            }
            else
            {
                r -= offRatio;
                if(inc != null && inc)
                {
                    offRatio *= 0.1;
                    r += offRatio;
                }

                inc = false;
            }
            
            end = cubicBezComputePoint(startX, startY, ctrl1X, ctrl1Y, ctrl2X, ctrl2Y, endX, endY, r);
            dist = (int)Math.round(Math.sqrt((end.x - endX) *(end.x - endX) + (end.y - endY) * (end.y - endY)));
        }
        
        return buildArrowPath(end.x, end.y, endX, endY, width, length, arrow.getType());
    }
   
    private static PointF quadBezComputePoint(float startX, float startY,
        float ctrlX, float ctrlY,
        float endX, float endY, float fU)
   {
        //  
        //  Add up all the blending functions multiplied with the control points
        //
        float fBlend;
        float f1subu = 1.0f - fU;
        
        //  First blending function (1-u)^2
        fBlend = f1subu * f1subu;
        p.x = fBlend * startX;
        p.y = fBlend * startY;
        
        //  Second blending function 2u(1-u)
        fBlend = 2 * fU * f1subu;
        p.x += fBlend * ctrlX;
        p.y += fBlend * ctrlY;
        
        //  Fourth blending function u^2
        fBlend = fU * fU;
        p.x += fBlend * endX;
        p.y += fBlend * endY;
        
        return p;
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
    private static PointF cubicBezComputePoint(float startX, float startY,
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
    
    private static Path buildArrowPath(float startX, float startY, float endX, float endY, float zoom) 
    {
        Path path = new Path();
        path.moveTo(endX, endY);
        
        float pBaseX, pBaseY;
        float vecLineX, vecLineY;
        float vecLeftX, vecLeftY;
        float fLength;
        float th;
        float ta;
        
        int nWidth = (int)(15 * zoom);
        float fTheta = 1.0f; 
        
        
        // build the line vector
        vecLineX = (float) endX - startX;
        vecLineY = (float) endY - startY;
        
        // build the arrow base vector - normal to the line
        vecLeftX = -vecLineY;
        vecLeftY = vecLineX;
        
        // setup length parameters
        fLength = (float) Math.sqrt(vecLineX * vecLineX + vecLineY * vecLineY);
        th = nWidth / (2.0f * fLength);
        ta = (float)(nWidth / (2.0f * (Math.tan(fTheta) / 2.0f) * fLength));
        
        // find the base of the arrow
        pBaseX = (int) (endX + -ta * vecLineX);
        pBaseY = (int) (endY + -ta * vecLineY);
        
        // build the points on the sides of the arrow
        path.lineTo(pBaseX + th * vecLeftX , pBaseY + th * vecLeftY / 2);
        path.lineTo(pBaseX + -th * vecLeftX, pBaseY + -th * vecLeftY / 2);
        
        path.close();
        
        return path;
    }

    private static Path buildArrowPath(float startX, float startY, float endX, float endY, 
        float arrowWidth, float arrowLength, byte arrowType) 
    {
        switch(arrowType)
        {
            case Arrow.Arrow_Triangle:
                return buildTriangleArrowPath(startX, startY, endX, endY, 
                    arrowWidth);
                
            case Arrow.Arrow_Arrow:
                return buildArrowArrowPath(startX, startY, endX, endY, 
                    arrowWidth);
                
            case Arrow.Arrow_Diamond:
                return buildDiamondArrowPath(startX, startY, endX, endY, 
                    arrowWidth, arrowLength);
                
            case Arrow.Arrow_Stealth:
                return buildStealthArrowPath(startX, startY, endX, endY, 
                    arrowWidth, arrowLength);
                
            case Arrow.Arrow_Oval:
                return buildOvalArrowPath(endX, endY, arrowWidth, arrowLength);
        }
        
        return new Path();
    }
    
    private static Path buildTriangleArrowPath(float startX, float startY, float endX, float endY, 
        float arrowWidth)
    {
        Path path = new Path();
        path.moveTo(endX, endY);
        
      //line inclination
        if(endY == startY)
        {
            path.lineTo(startX, startY - arrowWidth / 2.0f);
            path.lineTo(startX, startY + arrowWidth / 2.0f);
        }
        else if(endX == startX)
        {
            path.lineTo(startX - arrowWidth / 2.0f, startY );
            path.lineTo(startX + arrowWidth / 2.0f, startY);
        }
        else
        {
            float k = (endY - startY) / (float)(endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float)(arrowWidth / 2.0f * Math.cos(angle));
            float offy = (float)(arrowWidth / 2.0f * Math.sin(angle));
            
            // build the points on the sides of the arrow
            path.lineTo(startX + offx, startY + offy);
            path.lineTo(startX - offx, startY - offy);
        }
        
        
        path.close();
        
        return path;
    }
    
    private static Path buildArrowArrowPath(float startX, float startY, float endX, float endY, 
        float arrowWidth)
    {
        Path path = new Path();
        
        //line inclination
        if(endY == startY)
        {
            path.moveTo(startX, startY - arrowWidth / 2.0f);
            path.lineTo(endX, endY);
            path.lineTo(startX, startY + arrowWidth / 2.0f);
        }
        else if(endX == startX)
        {
            path.moveTo(startX - arrowWidth / 2.0f, startY );
            path.lineTo(endX, endY);
            path.lineTo(startX + arrowWidth / 2.0f, startY);
        }
        else
        {
            float k = (endY - startY) / (float)(endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float)(arrowWidth / 2.0f * Math.cos(angle));
            float offy = (float)(arrowWidth / 2.0f * Math.sin(angle));
            
            // build the points on the sides of the arrow
            path.moveTo(startX + offx, startY + offy);
            path.lineTo(endX, endY);
            path.lineTo(startX - offx, startY - offy);
        }
        
        return path;
    }
    
    private static Path buildDiamondArrowPath(float startX, float startY, float endX, float endY, 
        float arrowWidth, float arrowLength)
    {
        Path path = new Path();
        //line inclination
        if(endY == startY || endX == startX)
        {
            path.moveTo(endX - arrowLength / 2.0f, endY);
            path.lineTo(endX, endY - arrowWidth / 2.0f);
            path.lineTo(endX + arrowLength / 2.0f, endY);
            path.lineTo(endX, endY + arrowWidth / 2.0f);
        }
        else
        {
            float k = (endY - startY) / (float)(endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float)(arrowLength / 2.0f * Math.cos(angle));
            float offy = (float)(arrowWidth / 2.0f * Math.sin(angle));
            
            // build the points on the sides of the arrow
            path.moveTo(startX, startY);
            path.lineTo(endX + offx, endY + offy);
            path.lineTo(endX + (endX - startX), endY + (endY - startY));
            path.lineTo(endX - offx, endY - offy);
        }
        
        path.close();
        return path;
    }
    
    private static Path buildStealthArrowPath(float startX, float startY, float endX, float endY, 
        float arrowWidth, float arrowLength)
    {
        Path path = new Path();
        path.moveTo(endX, endY);
        
      //line inclination
        if(endY == startY)
        {
            path.lineTo(startX, startY - arrowWidth / 2.0f);
            path.lineTo(startX + (endX - startX) / 4.f, endY);
            path.lineTo(startX, startY + arrowWidth / 2.0f);
        }
        else if(endX == startX)
        {
            path.lineTo(startX - arrowWidth / 2.0f, startY );
            path.lineTo(startX, startY + (endY - startY) / 4.f);
            path.lineTo(startX + arrowWidth / 2.0f, startY);
        }
        else
        {
            float k = (endY - startY) / (float)(endX - startX);
            k = -1 / k;
            double angle = Math.atan(k);
            float offx = (float)(arrowLength / 2.0f * Math.cos(angle));
            float offy = (float)(arrowWidth / 2.0f * Math.sin(angle));
            
            // build the points on the sides of the arrow
            path.lineTo(startX + offx, startY + offy);
            path.lineTo(startX + (endX - startX) / 4.f, startY + (endY - startY) / 4.f);
            path.lineTo(startX - offx, startY - offy);
        }        
        
        path.close();
        
        return path;
    }
    
    private static Path buildOvalArrowPath(float endX, float endY, float arrowWidth, float arrowLength)
    {
        Path path = new Path();
        path.addOval(new RectF(endX - arrowLength / 2.0f,  endY - arrowWidth / 2.0f,endX + arrowLength / 2.0f,  endY + arrowWidth / 2.0f),
            Direction.CCW);
        return path;
    }
}
