/*
 * 文件名称:          ADialog.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:41:07
 */
package com.nvqquy98.lib.doc.office.system.beans;

import java.util.Vector;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IDialogAction;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

/**
 * dialog父类
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
public class ADialog extends Dialog implements OnClickListener
{
    //
    protected static final int GAP = MainConstant.GAP;
    // 对话框与屏幕左、右边的距离
    protected static final int MARGIN = 30;
    /**
     * 
     * @param context
     */
    public ADialog(IControl control, Context context, IDialogAction action, Vector<Object> model, 
        int dialogID, int titleResID)
    {
        this(control, context, action, model, dialogID, context.getResources().getString(titleResID));
    }    
    
    /**
     * 
     * @param context
     */
    public ADialog(IControl control, Context context, IDialogAction action, Vector<Object> model, 
        int dialogID, String title)
    {
        super(context);
        this.control = control;
        this.dialogID = dialogID;
        this.model = model;
        this.action = action;
        dialogFrame = new ADialogFrame(context, this);
        setTitle(title);
        //View view = new View(context);
        //view.setBackgroundColor(Color.GRAY);
        //dialogFrame.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
    }
    
    /**
     * 
     *
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(dialogFrame);
        dialogFrame.post(new Runnable()
        {
            public void run()
            {   
                doLayout();
            }
        });
    }
    
    /**
     * 
     *
     */
    public void onConfigurationChanged(Configuration newConfig)
    {
        
    }
    
    /**
     * 
     *
     */
    public void onClick(View v)
    {
        
    }
    
    /**
     * 
     */
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    
    /**
     * 
     */
    public void dismiss()
    {
        super.dismiss();
        dispose();
    }
    
    /**
     * 
     */
    public void doLayout()
    {   
    }   
    
    /**
     * 此方法尽量不要使用，因为设置大小后就需要考虑横竖的问题
     * 
     * @param w
     * @param h
     */
    protected void setSize(int w, int h)
    {
        dialogFrame.setLayoutParams(new LayoutParams(w, h));
    }
    
    /**
     * 
     */
    public void dispose()
    {
        control = null;
        if (model != null)
        {
            model.clear();
            model = null;
        }
        action = null;
        if (dialogFrame != null)
        {
            dialogFrame.dispose();
            dialogFrame = null;
        }
        ok = null;
        cancel = null;
    }
    
    protected IControl control;
    //
    protected int dialogID;
    // 对话框显示、操作过程中用到数据
    protected Vector<Object> model;
    // 对话框关闭后需要回调的action
    protected IDialogAction  action;
    //
    protected ADialogFrame dialogFrame;
    // 确定或是
    protected Button ok;
    // 取消或否
    protected Button cancel; 
}
