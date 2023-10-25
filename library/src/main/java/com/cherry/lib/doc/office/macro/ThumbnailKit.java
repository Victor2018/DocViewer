/*
 * 文件名称:          ThumbnailKit.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:52:03
 */
package com.cherry.lib.doc.office.macro;

import com.cherry.lib.doc.office.constant.MainConstant;
import com.cherry.lib.doc.office.fc.ReaderThumbnail;

import android.graphics.Bitmap;

/**
 * get Thumbnail kit
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2013-2-25
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ThumbnailKit
{
    private static ThumbnailKit kit = new ThumbnailKit();
    
    /**
     * 
     */
    public static ThumbnailKit instance()
    {
        return kit;
    }
    
    /**
     * 
     * @param filePath
     * @param width thumbnail width
     * @param height thumbnail height
     * @return
     */
    public Bitmap getPPTThumbnail(String filePath, int width, int height)
    {
        try
        {
            String lowerCase = filePath.toLowerCase();
            if (lowerCase.indexOf(".") > 0
                && width > 0
                && height > 0
                && (lowerCase.endsWith(MainConstant.FILE_TYPE_PPT)
                    || lowerCase.endsWith(MainConstant.FILE_TYPE_POT)))
            {
                return ReaderThumbnail.instance().getThumbnailForPPT(filePath, width, height);
            } 
        }
        catch(Exception e)
        {
            
        }
        
        return null;
    }
    
    /**
     * 
     * @param filePath
     * @return
     */
    public Bitmap getPPTXThumbnail(String filePath)
    {
        try
        {
            String lowerCase = filePath.toLowerCase();
            if (lowerCase.indexOf(".") > 0
                && (lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX)
                    || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM)
                    || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX)
                    || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM)))
            {
                return ReaderThumbnail.instance().getThumbnailForPPTX(filePath);
            }
        }
        catch(Exception e)
        {
            
        }
        
        return null;
    }
    
    /**
     * 
     * @param filePath
     * @param zoom (0 < thumbnail zoom value <= MAXZOOM_THUMBNAIL )
     * @see com.cherry.lib.doc.office.macro.Application #MAXZOOM_THUMBNAIL
     * @return
     */
    public Bitmap getPDFThumbnail(String filePath, int zoom)
    {
        try
        {
            
            String lowerCase = filePath.toLowerCase();
            if (lowerCase.indexOf(".") > 0
                && lowerCase.endsWith(MainConstant.FILE_TYPE_PDF)
                && zoom > 0 && zoom <= Application.MAXZOOM_THUMBNAIL)
            {
                return ReaderThumbnail.instance().getThumbnailForPDF(filePath, zoom / (float)MainConstant.STANDARD_RATE);
            }
        }
        catch(Exception e)
        {
            
        }
        return  null;
    }
}
