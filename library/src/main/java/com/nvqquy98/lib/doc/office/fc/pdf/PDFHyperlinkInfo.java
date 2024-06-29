/*
 * 文件名称:          HyperlinkInfo.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:34:55
 */
package com.nvqquy98.lib.doc.office.fc.pdf;

import android.graphics.RectF;

/**
 * PDF document hyperlink information 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PDFHyperlinkInfo extends RectF
{

    /**
     * 
     * @param l
     * @param t
     * @param r
     * @param b
     * @param pageNumber
     */
    public PDFHyperlinkInfo(float l, float t, float r, float b, int pageNumber, String uri)
    {
        super(l, t, r, b);
        this.pageNumber = pageNumber;
        this.strURI = uri;
    }
    
    /**
     * 
     */
    public int getPageNumber()
    
    {
        return this.pageNumber;
    }
    
    /**
     * 
     */
    public String getURL()
    {
        return this.strURI;
    }
    
    // PDF page number
    private int pageNumber;
    
    private String strURI;
}
