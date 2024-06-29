/*
 * 文件名称:          FunnelPathBuilder.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:28:01
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.smartArt;

import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;

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
 * 日期:            2013-5-10
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SmartArtPathBuilder
{
    private static final float TODEGREE = 36000000f / 21600000f;
    
    private static Matrix sm = new Matrix();
    private static RectF s_rect = new RectF();
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
            case ShapeTypes.Funnel:
                return getFunnelPath(shape, rect);
            case ShapeTypes.Gear6:
                return getGear6Path(shape, rect);
                
            case ShapeTypes.Gear9:
                return getGear9Path(shape, rect);
            case ShapeTypes.LeftCircularArrow:
                return getLeftCircularArrowPath(shape, rect);
            case ShapeTypes.PieWedge:
                return getPieWedgePath(shape, rect);
            case ShapeTypes.SwooshArrow:
                return getSwooshArrowPath(shape, rect);
        }        
        
        return null;
    }
    
    private static Path getFunnelPath(AutoShape shape, Rect rect)
    {
        float width = 716f;
        float height = 536f;
        path.addOval(new RectF(28, 22, 688, 238), Direction.CCW);
        
        path.moveTo(0, 130);            
        path.arcTo(new RectF(0, 0, 716, 260), 180, 180);
        path.arcTo(new RectF(258, 444, 458, 536), 30, 150);
        path.close();
        
        sm.reset();
        sm.postScale(rect.width() / width, rect.height() / height);
        path.transform(sm);
        
        path.offset(rect.left, rect.top);
        
        return path;
    }
    
    private static Path getGear6Path(AutoShape shape, Rect rect)
    {
        float len = 6858000f;
        
        path.moveTo(5131482, 1736961);
        path.lineTo(6143269, 1432030);
        path.lineTo(6515568, 2076873);
        path.lineTo(5745593, 2800638);
        path.cubicTo(5857203, 3212114, 5857203, 3645892, 5745592, 4057368);
        
        path.lineTo(6515568, 4781127);
        path.lineTo(6143269, 5425970);
        path.lineTo(5131482, 5121039);
        path.cubicTo(4830937, 5423437, 4455271, 5640328, 4043114, 5749407);
        
        path.lineTo(3801303, 6778110);
        path.lineTo(3056697, 6778110);
        path.lineTo(2814884, 5749410);
        path.cubicTo(2402727, 5640330, 2027062, 5423438, 1726518, 5121040);
        
        path.lineTo(714731, 5425970);
        path.lineTo(342432, 4781127);
        path.lineTo(1112407, 4057362);
        path.cubicTo(1000796, 3645886, 1000796, 3212108, 1112407, 2800632);
        
        path.lineTo(342432, 2076873);
        path.lineTo(714731, 1432030);
        path.lineTo(1726518, 1736961);
        path.cubicTo(2027063, 1434563, 2402729, 1217673, 2814886, 1108594);
        
        path.lineTo(3056697, 79890);
        path.lineTo(3801303, 79890);
        path.lineTo(4043116, 1108590);
        path.cubicTo(4455273, 1217671, 4830938, 1434562, 5131482, 1736961);
        
        path.close();
        
        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        
        path.offset(rect.left, rect.top);
        
        return path;
    }
    
    private static Path getGear9Path(AutoShape shape, Rect rect)
    {
        float len = 5715040;
        
        path.moveTo(4056564, 911200);
        path.lineTo(4501105, 538168);
        path.lineTo(4856239, 836163);
        path.lineTo(4566066, 1338725);
        path.cubicTo(4772395, 1570831, 4929267, 1842544, 5027111, 2137283);
        
        path.lineTo(5607429, 2137269);
        path.lineTo(5687931, 2593823);
        path.lineTo(5142605, 2792288);
        path.cubicTo(5151467, 3102716, 5096985, 3411694, 4982485, 3700369);
        
        path.lineTo(5427044, 4073378);
        path.lineTo(5195245, 4474864);
        path.lineTo(4649930, 4276370);
        path.cubicTo(4457179, 4519870, 4216835, 4721542, 3943563, 4869081);
        
        path.lineTo(4044350, 5440580);
        path.lineTo(3608711, 5599139);
        path.lineTo(3318566, 5096561);
        path.cubicTo(3014392, 5159194, 2700646, 5159194, 2396472, 5096561);
        
        path.lineTo(2106329, 5599139);
        path.lineTo(1670690, 5440580);
        path.lineTo(1771476, 4869081);
        path.cubicTo(1498205, 4721541, 1257861, 4519869, 1065110, 4276369);
        
        path.lineTo(519795, 4474864);
        path.lineTo(287996, 4073378);
        path.lineTo(732555, 3700369);
        path.cubicTo(618055, 3411694, 563574, 3102715, 572436, 2792288);
        
        path.lineTo(27109, 2593823);
        path.lineTo(107611, 2137269);
        path.lineTo(687928, 2137283);
        path.cubicTo(785773, 1842544, 942647, 1570832, 1148976, 1338726);
        
        path.lineTo(858801, 836163);
        path.lineTo(1213935, 538168);
        path.lineTo(1658476, 911200);
        path.cubicTo(1922884, 748311, 2217710, 641003, 2524962, 595826);
        
        path.lineTo(2625719, 24319);
        path.lineTo(3089321, 24319);
        path.lineTo(3190077, 595823);
        path.cubicTo(3497329, 641001, 3792154, 748309, 4056562, 911199);
        
        path.cubicTo(4056563, 911199, 4056563, 911200, 4056564, 911200);
        
        path.close();
        
        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        
        path.offset(rect.left, rect.top);
        
        return path;
    }
    
    private static Path getLeftCircularArrowPath(AutoShape shape, Rect rect)
    {
        shape.setFlipVertical(true);
        
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
            adj4 = -Math.round( values[3] * TODEGREE);
            adj5 = Math.round(len * values[4]);
        } 
        else
        {
            adj1 = Math.round(len * 0.125f);
            adj2 = 20;
            adj3 = 340;
            adj4 = -180;            
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
    
    private static Path getPieWedgePath(AutoShape shape, Rect rect)
    {
        path.moveTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.arcTo(new RectF(rect.left, rect.top, rect.left + rect.width() * 2, rect.top + rect.height() *  2), 180, 90);        
        path.close();
        return path;
    }
    
    private static Path getSwooshArrowPath(AutoShape shape, Rect rect)
    {
        float len = 3600000;
        path.moveTo(0, 3600000);
        path.cubicTo(400000, 2000000, 1300000, 950000, 2700000, 450000);
        path.lineTo(2649297, 0);
        path.lineTo(3600000, 720000);
        path.lineTo(2852109, 1800000);
        path.lineTo(2801406, 1350000);
        path.cubicTo(1533802, 1550000, 600000, 2300000, 0, 3600000);
        path.close();
        
        sm.reset();
        sm.postScale(rect.width() / len, rect.height() / len);
        path.transform(sm);
        
        path.offset(rect.left, rect.top);
        
        return path;
    }
}
