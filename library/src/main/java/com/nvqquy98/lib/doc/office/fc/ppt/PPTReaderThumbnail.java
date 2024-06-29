/*
 * 文件名称:          PTTReaderThumbnail.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:18:41
 */
package com.nvqquy98.lib.doc.office.fc.ppt;

import com.nvqquy98.lib.doc.office.constant.MainConstant;

import android.graphics.Bitmap;

/**
 * get thumbnail of PPT document
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-12-13
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PPTReaderThumbnail
{
    //
    private static PPTReaderThumbnail kit = new PPTReaderThumbnail();
    
    public static  PPTReaderThumbnail instance()
    {
        return kit;
    }
    
    /**
     * 
     */
    public Bitmap getThumbnail(String filePath)
    {
        try
        {
            String fileName = filePath.toLowerCase();
            // ppt
            if (fileName.endsWith(MainConstant.FILE_TYPE_PPT)
                     || fileName.endsWith(MainConstant.FILE_TYPE_POT))
            {
                return getThumbnailForPPT(filePath);
            }
            // pptx
            else if (fileName.endsWith(MainConstant.FILE_TYPE_PPTX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_PPTM)
                     || fileName.endsWith(MainConstant.FILE_TYPE_POTX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_POTM))
            {
                return getThumbnailForPPT(filePath);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }
    
    /**
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    private Bitmap getThumbnailForPPT(String filePath) throws Exception
    {
        
        return null;
    }
    
    /**
     * 
     * @param file
     * @return
     */
    private Bitmap getThumbnailForPPTX(String filePath) throws Exception
    {
        return null;
    }
}
