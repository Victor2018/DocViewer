/*
 * 文件名称:          PDFLib.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:32:43
 */
package com.nvqquy98.lib.doc.office.fc.pdf;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * reader PDF for native method
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
public class PDFLib
{

    private static PDFLib lib = new PDFLib();
    /**
     * load our native library 
     */
    static
    {
        System.loadLibrary("wxiweiPDF");
    }
    
    /**
     * 
     */
    public static PDFLib getPDFLib()
    {
        return lib;
    }
    
    /**
     * Construct
     * 
     * @param filename
     * 
     * @throws Exception
     */
    public synchronized void openFileSync(String filename) throws Exception
    {
        if (openFile(filename) <= 0)
        {
            throw new Exception("Format error");
        }
        pageCount = -1;
        currentPageIndex = -1;
    }
    
    /**
     * get page count
     * 
     * @return page count
     */
    public int getPageCountSync()
    {
        if (pageCount < 0)
        {
            pageCount = getPageCount();
        }
        return pageCount;
    }

    /**
     * Switch page to assign page index (base 0)
     * 
     * @param pageIndex    page index
     */
    private void showPageSync(int pageIndex)
    {
        if (pageCount == -1)
        {
            pageCount = getPageCount();
        }
        if (pageIndex > pageCount - 1)
        {
            pageIndex = pageCount - 1;
        }
        else if (pageIndex < 0)
        {
            pageIndex = 0;
        }
        if (this.currentPageIndex == pageIndex)
        {
            return;
        }
        this.currentPageIndex = pageIndex;
        showPage(pageIndex);
        this.pageWidth = getPageWidth();
        this.pageHeight = getPageHeight();
    }

    /**
     * get page size
     * 
     * @param pageIndex    page index (base 0)
     * @return
     */
    /*public synchronized Rectangle getPageSizeSync(int pageIndex)
    {
        showPageSync(pageIndex);
        return new Rectangle(0, 0, (int)pageWidth, (int)pageHeight);
    }*/
    
    /**
     * 
     * @return
     */
    public Rect[] getAllPagesSize()
    {
        return getPagesSize();
    }
    
    /**
     * 
     * @return
     */
    public boolean isDrawPageSyncFinished()
    {
    	return isDrawPDFFinished;
    }
    
    /**
     * draw page to bitmap
     * 
     * @param bitmap        Bitmap instance
     * @param pageIndex     The page index (base 0)
     * @param pageWidth     The page width of after scaling
     * @param pageHeight    The page height of after scaling
     * @param paintX        The paint X axis
     * @param paintY        The paint Y axis
     * @param paintWidth    The paint width
     * @param paintHeight   The paint height
     */
    public synchronized void drawPageSync(Bitmap bitmap, int pageIndex, float pageWidth, float pageHeight, 
        int paintX, int paintY, int paintWidth, int paintHeight, int drawObject)
    {
    	isDrawPDFFinished = false;
        showPageSync(pageIndex);
        drawPage(bitmap, pageWidth, pageHeight, paintX, paintY, paintWidth, paintHeight);
        isDrawPDFFinished = true;
    }

    /**
     * get hyperlink count assign location
     * 
     * @param pageIndex  page index (base 0)
     * @param x     x axis value
     * @param y     y axis value
     * @return hyperlink count 
     */
    public synchronized int getHyperlinkCountSync(int pageIndex, float x, float y)
    {
        return getHyperlinkCount(pageIndex, x, y);
    }

    /**
     * get hyperlink information assign page index
     * 
     * @param page      page index (base 0)
     * @return hyperlink information
     */
    public synchronized PDFHyperlinkInfo[] getHyperlinkInfoSync(int pageIndex)
    {
        return getHyperlinkInfo(pageIndex);
    }

    /**
     * search content is this PDF document
     * 
     * @param pageIndex     page index (base 0)
     * @param str  search content
     * @return  content is page location
     */
    public synchronized RectF[] searchContentSync(int pageIndex, String text)
    {
        showPageSync(pageIndex);
        return searchContent(text);
    }

    /**
     * is this PDF document outline?
     * 
     * @return      = true     have outline
     *               = false    no outline    
     */
    public synchronized boolean hasOutlineSync()
    {
        return hasOutline();
    }

    /**
     * get outline item this PDF document 
     * 
     * @return outline item
     */
    public synchronized PDFOutlineItem[] getOutlineSync()
    {
        return getOutline();
    }

    /**
     * is this PDF document password? 
     * 
     * @return      = true     have password
     *               = false    no password         
     */
    public synchronized boolean hasPasswordSync()
    {
        return hasPassword();
    }

    /**
     * Authenticate password the PDF document 
     * 
     * @param   password
     * @return  = true  correct
     *           = false    wrong
     */
    public synchronized boolean authenticatePasswordSync(String password)
    {
        return authenticatePassword(password);
    }
    
    /**
     * 
     * @param flag
     */
    public void setStopFlagSync(int flag)
    {
        setStopFlag(flag);
    }
    
    /**
     * convert wmf file to jpg
     * @param infilename
     * @param outfilename
     * @param width
     * @param height
     * @return
     */
    public int wmf2Jpg(String infilename, String outfilename, int width, int height)
    {
        return convertFile(infilename, outfilename, width, height);
    }
    
    /**
     * 
     * @param in
     * @param out
     * @param picType "png" or "jpeg"
     * @return
     */
    public boolean convertToPNG(String in, String out, String picType)
    {
    	if("png".equalsIgnoreCase(picType) || "jpeg".equalsIgnoreCase(picType))
    	{
    		return convertPicture2PNG(in, out, picType.toLowerCase()) != 0;
    	}
    	
    	return false;
    }
    
    /**
     * dispose memory
     */
    public synchronized void dispose()
    {
        //destroy();
    }

    /* The native functions */
    /**
     * open file 
     * 
     * @param filePath  file absolute path
     * @return
     */
    private static native int openFile(String filePath);
    /**
     * get page count
     * 
     * @return page count
     */
    private static native int getPageCount();
    /**
     * get pages size 
     * 
     * @return pages size array
     */
    private static native Rect[] getPagesSize();
    /**
     * Switch page to assign page index
     * 
     * @param pageIndex         page index (base 0)
     */
    private static native void showPage(int pageIndex);
    /**
     * get current display page width
     * 
     * @return page width
     */
    private static native float getPageWidth();
    /**
     * get current display page height
     * 
     * @return page height
     */
    private static native float getPageHeight();
    /**
     * draw page to bitmap
     * 
     * @param bitmap        Bitmap instance
     * @param pageWidth     The page width of after scaling
     * @param pageHeight    The page height of after scaling
     * @param paintX        The paint X axis
     * @param paintY        The paint Y axis
     * @param paintWidth    The paint width
     * @param paintHeight   The paint height
     */
    private static native void drawPage(Bitmap bitmap, float pageW, float pageH, int patchX, int patchY,
        int patchW, int patchH);
    /**
     * search content is this PDF document
     * 
     * @param str  search content
     * @return  content is page location
     */
    private static native RectF[] searchContent(String str);    
    /**
     * get hyperlink count assign location
     * 
     * @param pageIndex       page index (base 0)
     * @param x     x axis value
     * @param y     y axis value
     * @return hyperlink count 
     */
    private static native int getHyperlinkCount(int pageIndex, float x, float y);
    /**
     * get hyperlink information assign page index
     * 
     * @param page       page index (base 0)
     * @return hyperlink information
     */
    private static native PDFHyperlinkInfo[] getHyperlinkInfo(int pageIndex);
    /**
     * get outline item this PDF document 
     * 
     * @return outline item
     */
    private static native PDFOutlineItem[] getOutline();
    /**
     * is this PDF document outline?
     * 
     * @return      = true     have outline
     *               = false    no outline    
     */
    private static native boolean hasOutline();

    /**
     * is this PDF document password? 
     * 
     * @return      = true     have password
     *               = false    no password         
     */
    private static native boolean hasPassword();
    /**
     * Authenticate password the PDF document 
     * 
     * @param   password
     * @return  = true  correct
     *           = false    wrong
     */
    private static native boolean authenticatePassword(String password);
    
    /**
     * 
     */
    private static native int setStopFlag(int flag);
    
    /**
     * dispose memory
     */
    private static native void destroy();

    /**
     * wmf to jpg
     * @param infilename
     * @param outfilename
     * @param x
     * @param y
     * @return
     */
    private static native int convertFile(String infilename, String outfilename, int x, int y);
    
    /**
     * 
     * @param in
     * @param out
     * @param picType "png" or "jpeg"
     * @return
     */
    private static native int convertPicture2PNG(String in, String out, String picType);
    
    // current display page index (base 0)
    private int currentPageIndex = -1;;
    // page count
    private int pageCount = -1;
    // page width
    public float pageWidth;
    // page height;
    public float pageHeight;

    private boolean isDrawPDFFinished = true;
}
