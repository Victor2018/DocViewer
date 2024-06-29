/*
 * 文件名称:          DialogFrame.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:35:26
 */
package com.nvqquy98.lib.doc.office.system.beans;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.LinearLayout;

/**
 * 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-8
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ADialogFrame extends LinearLayout
{

    /**
     * 
     */
    public ADialogFrame(Context context, ADialog dialog)
    {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.dialog = dialog;
    }
    

    /**
     * 
     *
     */
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        dialog.onConfigurationChanged(newConfig);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        dialog = null;
    }
    
    private ADialog dialog;
}
