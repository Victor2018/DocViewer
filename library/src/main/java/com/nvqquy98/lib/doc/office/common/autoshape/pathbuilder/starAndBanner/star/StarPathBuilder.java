/*
 * 文件名称:          StarAndFlagPathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:02:27
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.starAndBanner.star;

import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-10-11
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class StarPathBuilder
{
    private static Matrix sm = new Matrix();
    
    private static Path path = new Path();
    
    /**
     * get star path
     * @param shape
     * @param rect
     * @return
     */
    public static Path getStarPath(AutoShape shape, Rect rect)
    {
        path.reset();
        
        switch(shape.getShapeType())
        {
            case ShapeTypes.IrregularSeal1:
                return getIrregularSeal1Path(shape, rect);
                
            case ShapeTypes.IrregularSeal2:
                return getIrregularSeal2Path(shape, rect);
                
            case ShapeTypes.Star4:
            case ShapeTypes.Star5:
            case ShapeTypes.Star:
            case ShapeTypes.Star6:
            case ShapeTypes.Star7:
            case ShapeTypes.Star8:
            case ShapeTypes.Star10:
            case ShapeTypes.Star12:
            case ShapeTypes.Star16:
            case ShapeTypes.Star24:                
            case ShapeTypes.Star32:
                if(shape.isAutoShape07())
                {
                    return LaterStarPathBuilder.getStarPath(shape, rect);
                }
                else
                {
                    return EarlyStarPathBuilder.getStarPath(shape, rect);
                }
        }
        
        return path;
    }
    
    private static Path getIrregularSeal1Path(AutoShape shape, Rect rect)
    {
        float len = 380;
        path.moveTo(66, 206);
        path.lineTo(0, 150);
        
        path.lineTo(83, 134);        
        path.lineTo(8, 41);
        
        path.lineTo(128, 112);        
        path.lineTo(147, 42);
        
        path.lineTo(190, 103);        
        path.lineTo(255, 0);
        
        path.lineTo(250, 93);        
        path.lineTo(323, 78);
        
        path.lineTo(294, 128);        
        path.lineTo(370, 142);
        
        path.lineTo(310, 185);        
        path.lineTo(380, 233);
        
        path.lineTo(296, 228);        
        path.lineTo(319, 318);
        
        path.lineTo(247, 255);        
        path.lineTo(233, 346);
        
        path.lineTo(185, 263);        
        path.lineTo(149, 380);
        
        path.lineTo(135, 275);        
        path.lineTo(84, 309);
        
        path.lineTo(99, 245);        
        path.lineTo(0, 256);
        
        path.close();
        
        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        
        path.offset(rect.left, rect.top);
        return path;
    }
    
    private static Path getIrregularSeal2Path(AutoShape shape, Rect rect)
    {
        float len = 380;
        
        path.moveTo(70, 203);
        path.lineTo(20, 143);
        
        path.lineTo(95, 137);
        path.lineTo(79, 64);
        
        path.lineTo(151, 113);
        path.lineTo(170, 32);
        
        path.lineTo(202, 76);
        path.lineTo(260, 0);
        
        path.lineTo(255, 101);
        path.lineTo(316, 55);
        
        path.lineTo(287, 114);
        path.lineTo(380, 115);
        
        path.lineTo(298, 164);
        path.lineTo(321, 198);
        
        path.lineTo(287, 215);
        path.lineTo(331, 273);
        
        path.lineTo(257, 251);
        path.lineTo(262, 304);
        
        path.lineTo(215, 280);
        path.lineTo(204, 330);
        
        path.lineTo(174, 304);
        path.lineTo(153, 345);
        
        path.lineTo(132, 317);
        path.lineTo(86, 380);
        
        path.lineTo(85, 319);
        path.lineTo(23, 313);
        
        path.lineTo(58, 269);
        path.lineTo(0, 225);
        path.close();
        
        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        
        path.offset(rect.left, rect.top);
        return path;
    }
    

    /**
     * the rect of the star must be square
     * 
     * @param A half  horizontal width of the outer oval 
     * @param B half  vertical width of the outer oval 
     * @param a half  horizontal width of the inner oval 
     * @param b half  vertical width of the inner oval 
     * @param starPoints points count of the star
     * @return
     */
    public static Path getStarPath(int A, int B, int a, int b, int starPoints)
    {
        float offDegree = 360f / (starPoints * 2);
       
        //270 degree
        path.moveTo(0, -B);
        
        float x = 0;
        float y = 0; 
        float degree = 270;
        if(a > 0 && b > 0)
        {
            int index = 1;
            while(index++ < starPoints)
            {
                degree =  (degree + offDegree) % 360;
                if(degree == 90)
                {
                    x = 0;
                    y = b;
                }
                else
                {
                    x = (float)(a * b / Math.sqrt(Math.pow(b, 2) + Math.pow(a * Math.tan(degree * Math.PI / 180), 2)));
                    if(degree > 90 && degree < 270)
                    {
                        x= -x;
                    }
                    
                    y = (float)(x * Math.tan(degree * Math.PI / 180));
                }                
                path.lineTo( x, y);
                
                degree =  (degree + offDegree) % 360;
                if(degree == 90)
                {
                    x = 0;
                    y = B;
                }
                else
                {
                    x = (float)(A * B / Math.sqrt(Math.pow(B, 2) + Math.pow(A * Math.tan(degree * Math.PI / 180), 2)));
                    if(degree > 90 && degree < 270)
                    {
                        x= -x;
                    }
                    
                    y = (float)(x * Math.tan(degree * Math.PI / 180));
                }
                path.lineTo(x, y);
            }  
            
            //the last point
            degree = 270 - offDegree;
            x = -(float)(a * b / Math.sqrt(Math.pow(b, 2) + Math.pow(a * Math.tan(degree * Math.PI / 180), 2)));        
            y = (float)(x * Math.tan(degree * Math.PI / 180));
            path.lineTo(x, y);
        }
        else
        {
            int index = 1;
            while(index++ < starPoints)
            {
                degree =  (degree + offDegree) % 360;
                path.lineTo( 0, 0);
                
                degree =  (degree + offDegree) % 360;
                if(degree == 90)
                {
                    x = 0;
                    y = B;
                }
                else
                {
                    x = (float)(A * B / Math.sqrt(Math.pow(B, 2) + Math.pow(A * Math.tan(degree * Math.PI / 180), 2)));
                    if(degree > 90 && degree < 270)
                    {
                        x= -x;
                    }
                    
                    y = (float)(x * Math.tan(degree * Math.PI / 180));
                }
                path.lineTo(x, y);
            }
            
            //the last point
            path.lineTo(0, 0);
        }
        
        path.close();
        
        return path;
    }
}
