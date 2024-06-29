/*
 * 文件名称:          ICustomDialog.java
 *  
 * 编译器:            android2.2
 * 时间:              下午12:47:14
 */
package com.nvqquy98.lib.doc.office.common;

public interface ICustomDialog
{
    //password dialog
    public static final byte DIALOGTYPE_PASSWORD = 0;
    //txt encode dialog
    public static final byte DIALOGTYPE_ENCODE = 1;
    //loading dialog
    public static final byte DIALOGTYPE_LOADING = 2;
    //error dialog
    public static final byte DIALOGTYPE_ERROR = 3;
    //
    public static final byte DIALOGTYPE_FIND = 4;
    
    
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
