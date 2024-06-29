/*
 * 文件名称:          IControl.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:50:27
 */
package com.nvqquy98.lib.doc.office.system;

import com.nvqquy98.lib.doc.office.common.ICustomDialog;
import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.ISlideShow;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import org.jetbrains.annotations.Nullable;

public interface IControl
{
    /**
     * 布局视图
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void layoutView(int x, int y, int w, int h);
    
    /**
     * action派发
     *  @param actionID 动作ID
     * @param obj 动作ID的Value
     */
    public void actionEvent(int actionID, @Nullable Object obj);
    
    /**
     * 得到action的状态的值
     * 
     * @return obj
     */
    public @Nullable Object getActionValue(int actionID, @Nullable Object obj);
       
    /**
     * current view index
     * @return
     */
    public int getCurrentViewIndex();
    
    /**
     * 获取应用组件
     */
    public View getView();
    
    /**
     * 
     */
    public Dialog getDialog(Activity activity, int id);
    
    /**
     * 
     */
    public IMainFrame getMainFrame();
    
    /**
     * 
     */
    public Activity getActivity();
    
    /**
     * get find instance
     */
    public IFind getFind();
    
    /**
     * 
     */
    public boolean isAutoTest();
    
    /**
     * 
     */
    public IOfficeToPicture getOfficeToPicture();
    
    /**
     * 
     */
    public ICustomDialog getCustomDialog();
    
    /**
     * 
     */
    public boolean isSlideShow();
    
    /**
     * 
     * @return
     */
    public ISlideShow getSlideShow();
    
    /**
     * 
     */
    public IReader getReader();
    
    /**
     * 
     */
    public boolean openFile(String filePath);
    
    /**
     * 
     */
    public int getApplicationType();
    
    /**
     * 
     */
    public SysKit getSysKit();
    
    /**
     * 释放内存
     */
    public void dispose();
}
