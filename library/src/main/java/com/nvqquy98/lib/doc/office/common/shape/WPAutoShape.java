/*
 * 文件名称:           WPAutoshape.java
 *  
 * 编译器:             android2.2
 * 时间:               上午9:25:32
 */
package com.nvqquy98.lib.doc.office.common.shape;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-3-22
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class WPAutoShape extends WPAbstractShape
{
    /*// 绝对值
    public static final short HEIGHT_ABSOLUTE = 0;
    // 相对于页面
    public static final short HEIGHT_RELATIVE = 1;
    // 相对于页边距
    public static final short HEIGHT_RELATIVE_MARGIN = 2;
    // 相对于上边距
    public static final short HEIGHT_RELATIVE_TOP = 3;
    // 相对于下边距
    public static final short HEIGHT_RELATIVE_BOTTOM = 4;
    // 相对于内边距
    public static final short HEIGHT_RELATIVE_INNER = 5;
    // 相对于外边距
    public static final short HEIGHT_RELATIVE_OUTER = 6;
    
    // 绝对值
    public static final short WIDTH_ABSOLUTE = 0;
    // 相对于页面
    public static final short WIDTH_RELATIVE = 1;
    // 相对于页边距
    public static final short WIDTH_RELATIVE_MARGIN = 2;
    // 相对于左边距
    public static final short WIDTH_RELATIVE_LFET = 3;
    // 相对于右边距
    public static final short WIDTH_RELATIVE_RIGHT = 4;
    // 相对于内边距
    public static final short WIDTH_RELATIVE_INNER = 5;
    // 相对于外边距
    public static final short WIDTH_RELATIVE_OUTER = 6;*/
    
    
    
    /**
     * 
     */
    public WPAutoShape()
    {
        
    }
    
    /**
     * 
     *
     */
    public short getType()
    {
        return SHAPE_AUTOSHAPE;
    }
    
    /**
     * 
     */
    public boolean isWatermarkShape()
    {
        return false;
    }
    
    /**
    *
    *
    */ 
   public Rectangle getBounds()
   {
       if (groupShape != null)
       {
          return groupShape.getBounds();
       }
       return super.getBounds();
   }
    
    /**
     * 
     * @param groupShape
     */
    public void addGroupShape(WPGroupShape groupShape)
    {
        this.groupShape = groupShape;
    }
    
    /**
     * 
     * @return
     */
    public WPGroupShape getGroupShape()
    {
        return groupShape;
    }

    /**
     * dispose
     */
    public void dispose()
    {
        super.dispose();
        if (groupShape != null)
        {
            groupShape.dispose();
            groupShape = null;
        }
    }    
    
    //
    private WPGroupShape groupShape;
}
