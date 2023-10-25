/*
 * 文件名称:          ErrorListener.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:55:51
 */
package com.cherry.lib.doc.office.macro;

import com.cherry.lib.doc.office.system.ErrorUtil;

public interface ErrorListener
{

    public static final int INSUFFICIENT_MEMORY = ErrorUtil.INSUFFICIENT_MEMORY;//"Unable to complete operation due to insufficient memory";
    //
    public static final int SYSTEM_CRASH = ErrorUtil.SYSTEM_CRASH; //"System crash, terminate running";
    //
    public static final int BAD_FILE = ErrorUtil.BAD_FILE; //"Bad file";
    //
    public static final int OLD_DOCUMENT = ErrorUtil.OLD_DOCUMENT;//"The document is too old - Office 95 or older, which is not supported";
    //
    public static final int PARSE_ERROR = ErrorUtil.PARSE_ERROR; //"File parsing error";
    //
    public static final int RTF_DOCUMENT = ErrorUtil.RTF_DOCUMENT; //"The document is really a RTF file, which is not supported";
    //
    public static final int PASSWORD_DOCUMENT = ErrorUtil.PASSWORD_DOCUMENT; //"It's a document with password which cannot parsed";
    //
    public static final int PASSWORD_INCORRECT = ErrorUtil.PASSWORD_INCORRECT; //"document's password sent to engine is error";
    //
    public static final int SD_CARD_ERROR = ErrorUtil.SD_CARD_ERROR; //"SD Card read or write error"
    //
    public static final int SD_CARD_WRITEDENIED = ErrorUtil.SD_CARD_WRITEDENIED; //"SD Card Write Permission denied" 
    //
    public static final int SD_CARD_NOSPACELEFT = ErrorUtil.SD_CARD_NOSPACELEFT; //"SD Card has no space left"
    /**
     * when engine error occurred callback this method
     * 
     * @param   codeCode  error code 
     *          @see ErrorListener#INSUFFICIENT_MEMORY
     *          @see ErrorListener#SYSTEM_CRASH
     *          @see ErrorListener#BAD_FILE
     *          @see ErrorListener#OLD_DOCUMENT
     *          @see ErrorListener#RTF_DOCUMENT
     */
    public void error(int errorCode);
    
    /**
     * when need destroy office engine instance callback this method
     */
    //public void destroyEngine();
}
