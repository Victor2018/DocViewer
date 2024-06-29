/*
 * 文件名称:          PageAttr.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:17:24
 */
package com.nvqquy98.lib.doc.office.simpletext.view;


/**
 * 页面属性集
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-20
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PageAttr
{ 
	
	public static final byte GRIDTYPE_NONE = 0;
	public static final byte GRIDTYPE_LINE_AND_CHAR =1;
	public static final byte GRIDTYPE_LINE = 2;
	public static final byte GRIDTYPE_CHAR = 3;
	
    // 页面宽度
    public int pageWidth;
    // 页面高度
    public int pageHeight;
    // 上边距
    public int topMargin;
    // 下边距
    public int bottomMargin;
    // 左边距
    public int leftMargin;
    // 右边距
    public int rightMargin;
    // page vertical alignment
    public byte verticalAlign;
    //
    public byte horizontalAlign;
    //
    public int headerMargin;
    //
    public int footerMargin;
    //
    public int pageBRColor;
    //
    public int pageBorder;
    //
    public float pageLinePitch;
    /**
     * 
     */
    public void reset()
    {
        verticalAlign = 0;
        horizontalAlign = 0;
        pageWidth = 0;
        pageHeight = 0;
        topMargin = 0;
        bottomMargin = 0;
        leftMargin = 0;
        rightMargin = 0;
        headerMargin = 0;
        footerMargin = 0;
        pageBorder = 0;
        pageBRColor = 0xFFFFFFFF;
        pageLinePitch = 0;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }
}
