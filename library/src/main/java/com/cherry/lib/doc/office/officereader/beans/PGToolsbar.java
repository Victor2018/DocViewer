/*
 * 文件名称:          PGToolsBar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:03:30
 */

package com.cherry.lib.doc.office.officereader.beans;

import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.R;
import com.cherry.lib.doc.office.pg.control.Presentation;
import com.cherry.lib.doc.office.system.IControl;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-2
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PGToolsbar extends AToolsbar
{

    /**
     * 
     * @param context
     * @param attrs
     */
    public PGToolsbar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PGToolsbar(Context context, IControl control)
    {
        super(context, control);
        init();
    }

    /**
     * 
     * @param context
     */
    private void init()
    {
        //copy
        //addButton(R.drawable.file_copy, R.drawable.file_copy_disable,
            //R.string.file_toolsbar_copy, EventConstant.FILE_COPY_ID, true);
        
        //goto
        //addButton(R.drawable.app_internet_hyperlink, R.drawable.app_internet_hyperlink_disable,
            //R.string.app_toolsbar_hyperlink, EventConstant.APP_HYPERLINK, true);
        
        //slideshow
        addButton(R.drawable.file_slideshow, R.drawable.file_slideshow,
            R.string.pg_slideshow, EventConstant.PG_SLIDESHOW_GEGIN, true);
        
        // 查找
        addButton(R.drawable.app_find, R.drawable.app_find_disable,            
            R.string.app_toolsbar_find, EventConstant.APP_FIND_ID, true);
        
        // 备注
        addButton(R.drawable.ppt_node,R.drawable.ppt_node_disable, 
            R.string.pg_toolsbar_note, EventConstant.PG_NOTE_ID, true);
        
        // 分享
        addButton(R.drawable.file_share, R.drawable.file_share_disable, 
            R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        
        // 联网搜索
        addButton(R.drawable.app_internet_search, R.drawable.app_internet_search_disable, 
            R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        
        // 标星
        addCheckButton(R.drawable.file_star_check, R.drawable.file_star_uncheck, 
            R.drawable.file_star_disable, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, 
            EventConstant.FILE_MARK_STAR_ID, true);
        
        // 标签
        addButton(R.drawable.app_drawing, R.drawable.app_drawing_disable, 
            R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
        
        // 朗读
        //addButton(R.drawable.app_read, R.drawable.app_read_disable, 
            //R.string.app_toolsbar_read, EventConstant.APP_READ_ID, true);
        
        // 签批
        //addButton(R.drawable.app_approve, R.drawable.app_approve_disable, 
            //R.string.app_toolsbar_approve, EventConstant.APP_APPROVE_ID, true);
        
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
        Presentation pgView = (Presentation)control.getView();
        // copy
        /*if (pgView.getEditor().getHighlight().isSelectText())
        {
            setEnabled(EventConstant.FILE_COPY_ID, true);
        }
        else
        {
            setEnabled(EventConstant.FILE_COPY_ID, false);
        }
        
        if (pgView.isHyperlink())
        {
            setEnabled(EventConstant.APP_HYPERLINK, true);
        }
        else
        {
            setEnabled(EventConstant.APP_HYPERLINK, false);
        }*/

        // 备注
        if (pgView.getCurrentSlide() != null && pgView.getCurrentSlide().getNotes() != null)
        {
            setEnabled(EventConstant.PG_NOTE_ID, true);
        }
        else
        {
            setEnabled(EventConstant.PG_NOTE_ID, false);
        }
        
        // 分享
        //setEnabled(EventConstant.APP_SHARE_ID, true);
        
        // 联网搜索
        //setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, true);
        
        // 标星
        /*boolean marked = pgView.getMarked();
        if(marked)
        {
            setCheckState(EventConstant.FILE_MARK_STAR_ID, AImageCheckButton.CHECK);
        }
        else
        {
            setCheckState(EventConstant.FILE_MARK_STAR_ID, AImageCheckButton.UNCHECK);
        }*/
        
        // 朗读
        //setEnabled(EventConstant.APP_READ_ID, true);
        
        // 签批
        //setEnabled( EventConstant.APP_APPROVE_ID, true);
        
        // 帮助
        //setEnabled(EventConstant.SYS_HELP_ID, true);
        
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
