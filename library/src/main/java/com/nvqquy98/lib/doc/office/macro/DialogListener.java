/*
 * 文件名称:          DialogListener.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:35:58
 */
package com.nvqquy98.lib.doc.office.macro;

import com.nvqquy98.lib.doc.office.common.ICustomDialog;

public interface DialogListener
{
    //password dialog
    public static final byte DIALOGTYPE_PASSWORD = ICustomDialog.DIALOGTYPE_PASSWORD;
    //txt encode dialog
    public static final byte DIALOGTYPE_ENCODE = ICustomDialog.DIALOGTYPE_ENCODE;
    //loading dialog
    public static final byte DIALOGTYPE_LOADING = ICustomDialog.DIALOGTYPE_LOADING;
    //error dialog
    public static final byte DIALOGTYPE_ERROR = ICustomDialog.DIALOGTYPE_ERROR;
    //
    public static final byte DIALOGTYPE_FIND = ICustomDialog.DIALOGTYPE_FIND;
    
    
    /**
     * 
     * @param type dialog type
     */
    public void showDialog(byte type);
    
    /**
     * 
     * @param type
     */
    public void dismissDialog(byte type);
}
