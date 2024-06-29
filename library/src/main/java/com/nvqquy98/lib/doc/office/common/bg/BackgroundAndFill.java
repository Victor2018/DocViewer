/*
 * 文件名称:          Background.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:34:21
 */
package com.nvqquy98.lib.doc.office.common.bg;

import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureStretchInfo;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.system.IControl;
public class BackgroundAndFill
{
	// no Fill
    public static final byte FILL_NO = -1;
    // Fill with a solid color
    public static final byte FILL_SOLID = 0;
    // Fill with a pattern (bitmap)
    public static final byte FILL_PATTERN = 1;
    // shade to title
    public static final byte FILL_SHADE_TILE = 2;
    // Center a picture in the shape
    public static final byte FILL_PICTURE = 3;
    // Similar to FILL_SHADE, but the fill angle
    // is additionally scaled by the aspect ratio of
    // the shape. If shape is square, it is the same as FILL_SHADE
    public static final byte FILL_SHADE_RECT = 5;
    // Shade from bounding rectangle to end point
    public static final byte FILL_SHADE_RADIAL = 4;
    // Shade from shape outline to end point
    public static final byte FILL_SHADE_SHAPE = 6;    
    // Shade from start to end points
    public static final byte FILL_SHADE_LINEAR = 7;
    // A texture (pattern with its own color map)
    public static final byte FILL_TEXTURE = 8;
    // Use the background fill color/pattern
    public static final byte FILL_BACKGROUND = 9;
    
    /**
     * 
     *
     */
    public short getType()
    {
        return AbstractShape.SHAPE_BG_FILL;
    }
    
    public boolean isSlideBackgroundFill()
    {
		return isSlideBackgroundFill;
	}

	public void setSlideBackgroundFill(boolean isSlideBackgroundFill)
	{
		this.isSlideBackgroundFill = isSlideBackgroundFill;
	}
	
    /**
     * @return Returns the fillType.
     */
    public byte getFillType()
    {
        return fillType;
    }
    /**
     * @param fillType The fillType to set.
     */
    public void setFillType(byte fillType)
    {
        this.fillType = fillType;
    }

    /**
     * @return Returns the fgColor.
     */
    public int getForegroundColor()
    {
        return fgColor;
    }

    /**
     * @param fgColor The fgColor to set.
     */
    public void setForegroundColor(int fgColor)
    {
        this.fgColor = fgColor;
    }

    /**
     * @return Returns the bgColor.
     */
    public int getBackgoundColor()
    {
        return bgColor;
    }

    /**
     * @param bgColor The bgColor to set.
     */
    public void setBackgoundColor(int bgColor)
    {
        this.bgColor = bgColor;
    }

    /**
     * @return Returns the picture index.
     */
    public int getPictureIndex()
    {
        return pictureIndex;
    }

    /**
     * @param picture index The picture index to set.
     */
    public void setPictureIndex(int pictureIndex)
    {
        this.pictureIndex = pictureIndex;
    }
    
    /**
     * 
     */
    public Picture getPicture(IControl control)
    {
        return control.getSysKit().getPictureManage().getPicture(pictureIndex);
    }
    
    public AShader getShader() 
    {
		return shader;
	}

	public void setShader(AShader shader) 
	{
		this.shader = shader;
	}
	
	public PictureStretchInfo getStretch() 
	{
		return stretch;
	}

	public void setStretch(PictureStretchInfo stretch) 
	{
		this.stretch = stretch;
	}
	
	 /**
     * 
     *
     */
    public void dispose()
    {
    	stretch =  null;
    	if(shader != null)
    	{
    		shader.dispose();
    		shader = null;
    	}
    }
    
    //is slide background fill
    private boolean isSlideBackgroundFill;

	private PictureStretchInfo stretch;
	//
    private byte fillType;
    // BackgroundColor;
    private int bgColor;
    // filled by color
    private int fgColor;
    //filled by picture
    private int pictureIndex;
    //filled by gradient color, tile
    private AShader shader;
}
