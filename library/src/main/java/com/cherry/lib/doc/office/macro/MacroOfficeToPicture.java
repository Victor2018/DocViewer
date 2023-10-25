/*
 * 文件名称:          MacroOfficeToPicture.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:18:23
 */
package com.cherry.lib.doc.office.macro;

import com.cherry.lib.doc.office.common.IOfficeToPicture;

import android.graphics.Bitmap;

class MacroOfficeToPicture implements IOfficeToPicture
{

    protected MacroOfficeToPicture(OfficeToPictureListener listener)
    {
        this.officeToPictureListener = listener;
    }
    
    /**
     * set mode type
     * @param modeType
     */
    public void setModeType(byte modeType)
    {
        this.modeType = modeType;
    }
    
    /**
     * 
     *
     */
    public byte getModeType()
    {        
        return modeType;
    }

    /**
     * Get converter to of picture Bitmap instance, if the return is empty, is not generated picture
     * 
     * @param componentWidth  engine component width
     * @param componentHeight engine component height
     * 
     * @return Bitmap instance
     */
    public Bitmap getBitmap(int componentWidth, int componentHeight)
    {
        if (officeToPictureListener != null)
        {
            return officeToPictureListener.getBitmap(componentWidth, componentHeight);
            //return officeToPictureListener.getBitmap(845, 480);
        }
        return null;
    }

    /**
     * picture generated, the callback method
     * 
     * @param bitmap  generated picture bitmap  
     */
    public void callBack(Bitmap bitmap)
    {
        if (officeToPictureListener != null)
        {
            officeToPictureListener.callBack(bitmap);
        }
    }

    /**
     *     
     *
     */
    public boolean isZoom()
    {        
        return true;
    }

    /**
     *
     *
     */
    public void dispose()
    {
        officeToPictureListener = null;
    }
    
    //
    private OfficeToPictureListener officeToPictureListener;
    private byte modeType = VIEW_CHANGE_END;
}
