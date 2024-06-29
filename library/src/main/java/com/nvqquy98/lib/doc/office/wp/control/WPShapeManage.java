/*
 * 文件名称:          WPShapeManage.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:05:04
 */
package com.nvqquy98.lib.doc.office.wp.control;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;

/**
 * WP管理器
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-4-5
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class WPShapeManage
{
    /**
     * 
     */
    public WPShapeManage()
    {
        shapes = new HashMap<Integer, AbstractShape>(20);
    }
    
    /**
     * 
     */
    public int addShape(AbstractShape shape)
    {
        int size = shapes.size();
        shapes.put(size, shape);
        return size;
    }
    
    /**
     * 
     */
    public AbstractShape getShape(int index)
    {
        if (index < 0 || index >= shapes.size())
        {
            return null;
        }
        return shapes.get(index);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (shapes != null)
        {
            Collection<AbstractShape> ass = shapes.values();
            if (ass != null)
            {
                for (AbstractShape as : ass)
                {
                    as.dispose();
                }
                shapes.clear();
            }
        }
    }
    
    
    //
    private Map<Integer, AbstractShape> shapes;
    
}
