/*
 * 文件名称:          IRoot.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:19:22
 */
package com.nvqquy98.lib.doc.office.simpletext.view;


/**
 * 定义视图接口方法
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IRoot
{
	//the min layout width
	public static final int MINLAYOUTWIDTH = 5;
	
    /**
     * 是否可以后台
     * 
     * @return
     */
    public boolean canBackLayout();
    
    /**
     * 后台布局
     */
    public void backLayout();
    
    /**
     * 
     */
    public ViewContainer getViewContainer();
}
