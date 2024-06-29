/*
 * 文件名称:          ExcelView.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:21:00
 */
package com.nvqquy98.lib.doc.office.ss.control;

import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.sheetbar.SheetBar;
import com.nvqquy98.lib.doc.office.ss.view.SheetView;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * excel engine component
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2013-2-18
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ExcelView extends RelativeLayout
{
    /**
     * 
     * @param context
     */
    public ExcelView(Context context, String filepath, Workbook book, IControl control)
    {
        super(context);
        this.control = control;
        ss = new Spreadsheet(context, filepath, book, control, this);
       
        addView(ss, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    
    /**
     * 
     */
    public void init()
    {
        ss.init();
        initSheetbar();
    }
    
    
    /**
     * 
     */
    private void initSheetbar()
    {
        if (!isDefaultSheetBar)
        {
            return;
        }
        bar = new SheetBar(getContext(), control, getResources().getDisplayMetrics().widthPixels);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        addView(bar, params);
    }
    
    /**
     * 
     */
    public Spreadsheet getSpreadsheet()
    {
        return ss;
    }
    
    /**
     * 显示指定的sheet
     * 
     * @param sheetIndex 要显示的sheet名称
     */
    public void showSheet(int sheetIndex)
    {
        ss.showSheet(sheetIndex);
        //change focused button
        if(isDefaultSheetBar)
        {
            bar.setFocusSheetButton(sheetIndex);
        }
        else
        {
            control.getMainFrame().doActionEvent(EventConstant.SS_CHANGE_SHEET, sheetIndex);
        }
    }
    
    /**
     * 显示指定的sheet
     * 
     * @param sheetName 要显示的sheet名称
     */
    public void showSheet(String sheetName)
    {
        ss.showSheet(sheetName);
        
        Sheet sheet = ss.getWorkbook().getSheet(sheetName);
        if (sheet == null)
        {
            return;
        }        
        int sheetIndex = ss.getWorkbook().getSheetIndex(sheet);
        if(isDefaultSheetBar)
        {
            bar.setFocusSheetButton(sheetIndex);
        }
        else
        {
            control.getMainFrame().doActionEvent(EventConstant.SS_CHANGE_SHEET, sheetIndex);
        }
    }
    
    /**
     * 得到sheetView视图
     */
    public SheetView getSheetView()
    {
        return ss.getSheetView();
    }
    
    /**
     * 
     */
    public void removeSheetBar()
    {
        isDefaultSheetBar = false;
        removeView(bar);
    }
    
    /**
     * get sheet bar height
     * @return
     */
    public int getBottomBarHeight()
    {
        if(isDefaultSheetBar)
        {
            return bar.getHeight();
        }
        else
        {
            return control.getMainFrame().getBottomBarHeight();
        }
    }
    
    /**
     * current view index
     * @return
     */
    public int getCurrentViewIndex()
    {
    	return ss.getCurrentSheetNumber();
    }
    
    /**
     * 
     */
    public void dispose()
    {
        control = null;
        if (ss != null)
        {
            ss.dispose();
        }
        bar = null;
    }
    
    
    //
    private boolean isDefaultSheetBar = true;
    //
    private Spreadsheet ss;
    //
    private SheetBar bar;
    //
    private IControl control;
    
}
