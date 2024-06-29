/*
 * 文件名称:          Picture.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:01:51
 */

package com.nvqquy98.lib.doc.office.common.shape;

import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfo;
import com.nvqquy98.lib.doc.office.system.IControl;


/**
 * picture data class
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PictureShape extends AbstractShape
{    
    /**
     * get type of this shape
     */
    public short getType()
    {
        return SHAPE_PICTURE;
    }
    
    /**
     * 
     * @param pictureIndex
     */
    public void setPictureIndex(int pictureIndex)
    {
        this.pictureIndex = pictureIndex;
    }

    /**
     * 
     * @return
     */
    public int getPictureIndex()
    {
        return pictureIndex;
    }
    
    /**
     * 
     */
    public Picture getPicture(IControl control)
    {
        if (control == null)
        {
            return null;
        }
        return control.getSysKit().getPictureManage().getPicture(pictureIndex);
    }
    
    public static Picture getPicture(IControl control, int pictureIndex)
    {
    	if (control == null)
        {
            return null;
        }
        return control.getSysKit().getPictureManage().getPicture(pictureIndex);
    }
    
    /**
     * 
     */
    public void setZoomX(short zoomX)
    {
        this.zoomX = zoomX;
    }
    
    /**
     * 
     */
    public void setZoomY(short zoomY)
    {
        this.zoomY = zoomY;
    }
    
//    /**
//     * 
//     * @return
//     */
//    public int getRealWidth()
//    {
//       return  (int)(rect.width * zoomX / 1000.f);//rect.width * 1000.f / zoomX
//    }
//    
//    /**
//     * 
//     */
//    public int getRealHeight()
//    {
//       return  (int)(rect.height * zoomY / 1000.f);
//    } 
    

    /**
     * 
     * @param effectInfor
     */
    public void setPictureEffectInfor(PictureEffectInfo effectInfor)
    {
        this.effectInfor = effectInfor;
    }
    
    /**
     * 
     */
    public PictureEffectInfo getPictureEffectInfor()
    {
        return effectInfor;
    }
    
    public void dispose()
    {
        super.dispose();
    }
    
    private int pictureIndex;
    // Horizontal scaling factor supplied by user expressed in .001% units
    private short zoomX;
    // Vertical scaling factor supplied by user expressed in .001% units
    private short zoomY;

    //picture effect property
    private PictureEffectInfo effectInfor;
}
