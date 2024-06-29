/*
 * 文件名称:          ResKit.java
 *
 * 编译器:            android2.2
 * 时间:              下午9:03:08
 */

package com.nvqquy98.lib.doc.office.res;

import android.content.res.Resources;

import com.blankj.utilcode.util.Utils;
import com.nvqquy98.lib.doc.R;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-20
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class ResKit {
    //
    private static ResKit kit = new ResKit();

    /**
     *
     */
    public ResKit() {
        Resources resources = Utils.getApp().getResources();
        res = new HashMap<>();
        res.put("BUTTON_OK", resources.getString(R.string.sys_button_ok));
        res.put("BUTTON_CANCEL", resources.getString(R.string.sys_button_cancel));
        res.put("DIALOG_ENCODING_TITLE", resources.getString(R.string.text_encoding));
        res.put("DIALOG_LOADING", resources.getString(R.string.sys_progress_message_loading));
        res.put("DIALOG_INSUFFICIENT_MEMORY", resources.getString(R.string.dialog_insufficient_memory));
        res.put("DIALOG_SYSTEM_CRASH", resources.getString(R.string.dialog_system_crash));
        res.put("DIALOG_FORMAT_ERROR", resources.getString(R.string.bad_file));
        res.put("DIALOG_OLD_DOCUMENT", resources.getString(R.string.dialog_old_document));
        res.put("DIALOG_PARSE_ERROR", resources.getString(R.string.dialog_parse_error));
        res.put("DIALOG_RTF_FILE", resources.getString(R.string.dialog_rtf_file));
        res.put("DIALOG_PDF_SEARCHING", resources.getString(R.string.searching));
        res.put("DIALOG_FIND_NOT_FOUND", resources.getString(R.string.dialog_find_not_found));
        res.put("DIALOG_FIND_TO_BEGIN", resources.getString(R.string.dialog_find_to_begin));
        res.put("DIALOG_FIND_TO_END", resources.getString(R.string.dialog_find_to_begin));
        res.put("DIALOG_ENTER_PASSWORD", resources.getString(R.string.dialog_enter_password));
        res.put("DIALOG_CANNOT_ENCRYPTED_FILE", resources.getString(R.string.dialog_cannot_encrypted_file));
        res.put("DIALOG_PASSWORD_INCORRECT", resources.getString(R.string.dialog_password_incorrect));
        res.put("EXIT_SLIDESHOW", resources.getString(R.string.exit_slideshow));
        res.put("SD_CARD_ERROR", resources.getString(R.string.sd_card_error));
        res.put("SD_CARD_WRITEDENIED", resources.getString(R.string.sd_card_writedenied));
        res.put("SD_CARD_NOSPACELEFT", resources.getString(R.string.sd_card_nospaceleft));
    }

    /**
     *
     */
    public static ResKit instance() {
        return kit;
    }

    /**
     *
     */
    public boolean hasResName(String resName) {
        return res.containsKey(resName);
    }

    /**
     * @param resName
     * @return
     */
    public String getLocalString(String resName) {
        return res.get(resName);
    }

    //
    private Map<String, String> res;
}
