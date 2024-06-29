/*
 * 文件名称:          IDialogAction.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:44:51
 */
package com.nvqquy98.lib.doc.office.system;

import java.util.Vector;


/**
 * 对话框的Action接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-6
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IDialogAction
{
    /**
     * 
     * @param id    对话框的ID
     * @param obj   回调action需要数据
     */
    public void doAction(int id, Vector<Object> model);
    
    /**
     * 
     * @return
     */
    public IControl getControl();
    
    /**
     * 
     */
    public void dispose();
}
