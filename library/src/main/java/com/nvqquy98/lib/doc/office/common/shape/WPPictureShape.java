/*
 * 文件名称:          WPPictureShape.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:11:57
 */
package com.nvqquy98.lib.doc.office.common.shape;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-5-29
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class WPPictureShape extends WPAutoShape
{

    /**
     * 
     *
     */
    public short getType()
    {
        return SHAPE_PICTURE;
    }
    
    public void setPictureShape(PictureShape pictureShape)
    {
        this.pictureShape = pictureShape;
        
        if(rect == null)
        {
        	rect = pictureShape.getBounds();
        }
    }
    
    public PictureShape getPictureShape()
    {
        return pictureShape;
    }
    

    /**
     * 
     */
    public boolean isWatermarkShape()
    {
        return false;
    }
    
    public void dispose()
    {
        if(pictureShape != null)
        {
            pictureShape.dispose();
            pictureShape = null;
        }
    }
    
    private PictureShape pictureShape;
}
