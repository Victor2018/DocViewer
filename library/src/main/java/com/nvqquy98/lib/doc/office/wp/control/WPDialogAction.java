/*
 * 文件名称:          WPDialogAction.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:19:57
 */
package com.nvqquy98.lib.doc.office.wp.control;

import java.util.Vector;

import com.nvqquy98.lib.doc.office.constant.DialogConstant;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IDialogAction;


/**
 * WP用到dialogAction
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
public class WPDialogAction implements IDialogAction
{

    public WPDialogAction(IControl control)
    {
        this.control = control;
    }
    /**
     * 
     * @param id    对话框的ID
     * @param obj   回调action需要数据
     */
    public void doAction(int id, Vector<Object> model)
    {
        switch (id)
        {
            case DialogConstant.ENCODING_DIALOG_ID:
                break;

            default:
                break;
        }
    }
    
    /**
     * 
     */
    public IControl getControl()
    {
        return this.control;
    }

    /**
     * 
     */
    public void dispose()
    {
        control = null;
    }
    //
    public IControl control;
}
