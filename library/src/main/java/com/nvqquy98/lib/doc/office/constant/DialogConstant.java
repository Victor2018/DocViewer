/*
 * 文件名称:          DialogConstant.java
 * 版权所有@ 2011-2014 虹软（杭州）科技有限公司
 * 编译器:           JDK1.5.0_01
 * 时间:             上午10:14:58
 */
package com.nvqquy98.lib.doc.office.constant;

/**
 * Dialog ID
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2011-11-23
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public final class DialogConstant
{
    // Warning dialog ID
    public static final int MESSAGE_DIALOG_ID = 0; // 0
    // text encoding dialog ID
    public static final int ENCODING_DIALOG_ID = MESSAGE_DIALOG_ID + 1; // 1
    // create a new folder dialog ID
    public static final int CREATEFOLDER_DIALOG_ID = ENCODING_DIALOG_ID + 1; // 2
    // rename a file dialog ID
    public static final int RENAMEFILE_DIALOG_ID = CREATEFOLDER_DIALOG_ID + 1; // 3
    // delete file dialog ID
    public static final int DELETEFILE_DIALOG_ID = RENAMEFILE_DIALOG_ID + 1; // 4
    // ask overwrite file dialog ID
    public static final int OVERWRITEFILE_DIALOG_ID = DELETEFILE_DIALOG_ID + 1; // 5
    // sort file dialog ID
    public static final int FILESORT_DIALOG_ID = OVERWRITEFILE_DIALOG_ID + 1; // 6
    // set maximum number of recently opened documents
    public static final int SET_MAX_RECENT_NUMBER = FILESORT_DIALOG_ID + 1; // 7
    // ppt show notes
    public static final int SHOW_PG_NOTE_ID = SET_MAX_RECENT_NUMBER + 1; // 8
}
