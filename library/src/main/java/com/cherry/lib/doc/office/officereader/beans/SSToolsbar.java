/*
 * 文件名称:          SSToolsBar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:05:23
 */
package com.cherry.lib.doc.office.officereader.beans;

import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.R;
import com.cherry.lib.doc.office.ss.control.ExcelView;
import com.cherry.lib.doc.office.ss.control.Spreadsheet;
import com.cherry.lib.doc.office.ss.model.baseModel.Sheet;
import com.cherry.lib.doc.office.ss.util.ModelUtil;
import com.cherry.lib.doc.office.system.IControl;

import android.content.Context;
import android.util.AttributeSet;

/**
 * SS工具条
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-3
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class SSToolsbar extends AToolsbar
{

    /**
     * 
     * @param context
     * @param attrs
     */
    public SSToolsbar(Context context, IControl control)
    {
        super(context, control);
        init();
    }
    
    /**
     * 
     * @param context
     * @param attrs
     */
    public SSToolsbar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 
     * @param context
     */
    private void init()
    {
        //copy
        addButton(R.drawable.file_copy, R.drawable.file_copy_disable,
            R.string.file_toolsbar_copy, EventConstant.FILE_COPY_ID, true);
        //goto
        addButton(R.drawable.app_internet_hyperlink, R.drawable.app_internet_hyperlink_disable,
            R.string.app_toolsbar_hyperlink, EventConstant.APP_HYPERLINK, true);
        // 查找
        addButton(R.drawable.app_find, R.drawable.app_find_disable,
            R.string.app_toolsbar_find, EventConstant.APP_FIND_ID, true);
        
        // 分享
        addButton(R.drawable.file_share, R.drawable.file_share_disable, 
            R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        
        // 联网搜索
        addButton(R.drawable.app_internet_search, R.drawable.app_internet_search_disable, 
            R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        
        // 标星
        addCheckButton(R.drawable.file_star_check, R.drawable.file_star_uncheck,R.drawable.file_star_disable,
            R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star,
            EventConstant.FILE_MARK_STAR_ID, true);
        
        // 标签
        addButton(R.drawable.app_drawing, R.drawable.app_drawing_disable, 
            R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
        
//        // 朗读
//        addButton(R.drawable.app_read, R.drawable.app_read_disable, 
//            R.string.app_toolsbar_read, EventConstant.APP_READ_ID, true);
//        
//        // 签批
//        addButton(R.drawable.app_approve, R.drawable.app_approve_disable, 
//            R.string.app_toolsbar_approve, EventConstant.APP_APPROVE_ID, true);
        
        // 生成图片 
        //addButton(R.drawable.app_approve, R.drawable.app_approve_disable, 
        //    R.string.app_toolsbar_generated_picture, EventConstant.APP_GENERATED_PICTURE_ID, true);
        
        // 帮助
        //addButton(R.drawable.file_help, -1, R.string.sys_menu_help, EventConstant.SYS_HELP_ID, false);

    }
    
    /**
     * 
     */
    public void updateStatus()
    {
        Spreadsheet spreadSheet = ((ExcelView)control.getView()).getSpreadsheet();
        
        if(spreadSheet.getSheetView() == null)
        {
            return;
        }
        //find
        setEnabled(EventConstant.APP_FIND_ID, true);
        //share
        setEnabled(EventConstant.APP_SHARE_ID, true);
        //help
        setEnabled(EventConstant.SYS_HELP_ID, true);
        
        Sheet sheet = spreadSheet.getSheetView().getCurrentSheet();
        if(sheet.getActiveCellType() == Sheet.ACTIVECELL_SINGLE  && sheet.getActiveCell() != null)
        {            
            //copy
            String content = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), sheet.getActiveCell());
            setEnabled(EventConstant.FILE_COPY_ID, content != null && content.length() > 0);
            
            //internet search
            setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, content != null && content.length() > 0);
            
            //hyperlink            
            setEnabled(EventConstant.APP_HYPERLINK, 
                sheet.getActiveCell().getHyperLink() != null && sheet.getActiveCell().getHyperLink().getAddress() != null);
           
            
        }
        else
        {
            setEnabled(EventConstant.FILE_COPY_ID, false);
            setEnabled(EventConstant.APP_HYPERLINK,false);
           
            //internet search
            setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, false);
        }        
        
        
        postInvalidate();
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
    }
}
