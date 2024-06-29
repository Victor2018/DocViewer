/*
 * 文件名称:          ViewKit.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:00:00
 */
package com.nvqquy98.lib.doc.office.simpletext.view;

/**
 * 视图布局用到工具类
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-17
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ViewKit
{
    private static final ViewKit kit = new ViewKit();
    //
    public static ViewKit instance()
    {
        return kit;
    }
    
    /**
     * 位操作，把指定位 置为0或1
     * 
     * @param flag
     * @param pos 指的位
     * @param b = true 置1，= false 置0
     */
    public int setBitValue(int flag, int pos, boolean b)
    {
        int temp = b ? flag : ~flag;
        temp = (temp >>> pos) | 1;
        temp = (temp << pos);
        temp = b ? temp | flag : (~temp) & flag; 
        return temp;
    }
    

    /**
     * 位操作，把指定位 置为0或1
     * 
     * @param flag
     * @param pos 指的位
     * @param b = true 置1，= false 置0
     */
    public boolean getBitValue(int flag, int pos)
    {
        return ((flag >>> pos) & 1) == 1;
    }
    
    /**
     * 得到类型的上层View，没有则返回空
     */
    public IView getParentView(IView view, short viewType)
    {
        IView p = view.getParentView();
        while (p != null && p.getType() != viewType)
        {
            p = p.getParentView();
        }
        return p;
    }
}
