/*
 * 文件名称:          	WatermarkShape.java
 *  
 * 编译器:            android2.2
 * 时间:             	下午5:06:47
 */
package com.nvqquy98.lib.doc.office.common.shape;

import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfo;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        	Office engine V1.0
 * <p>
 * 作者:            	ljj8494
 * <p>
 * 日期:            	2013-4-24
 * <p>
 * 负责人:          	ljj8494
 * <p>
 * 负责小组:        	TMC
 * <p>
 * <p>
 */
public class WatermarkShape extends WPAutoShape
{
	private final float OPACITY = 0.2f;
			
    public final static byte Watermark_Text = 0;
    
    public final static byte Watermark_Picture = 1;

    /**
     * 
     *
     */
    public short getType()
    {
    	if(watermarkType == Watermark_Text)
    	{
    		return SHAPE_AUTOSHAPE;
    	}
    	else
    	{
    		return SHAPE_PICTURE;
    	}
    }
    
    /**
     * 
     */
    public boolean isWatermarkShape()
    {
        return true;
    }    

    public void setWatermarkType(byte type)
    {
    	this.watermarkType = type;
    }
    
    public byte getWatermarkType()
    {
    	return watermarkType;
    }
    
    /**
     * 
     * @param watermartString
     */
    public void setWatermartString(String watermartString)
    {
        this.watermartString = watermartString;
    }  
    
    /**
     * 
     * @return
     */
    public String getWatermartString()
    {
        return watermartString;
    }
    
    public boolean isAutoFontSize() 
    {
		return isAutoFontSize;
	}

	public void setAutoFontSize(boolean isAutoFontSize)
	{
		this.isAutoFontSize = isAutoFontSize;
	}
	
    public int getFontSize()
    {
		return fontSize;
	}
	public void setFontSize(int fontSize)
	{
		this.fontSize = fontSize;
	}
	public int getFontColor() 
	{
		return fontColor;
	}
	public void setFontColor(int fontColor) 
	{
		this.fontColor = fontColor;
	}
	public float getOpacity() 
	{
		return opacity;
	}
	public void setOpacity(float opacity)
	{
		this.opacity = OPACITY * opacity;
	}
	
	public int getPictureIndex()
	{
		return pictureIndex;
	}

	public void setPictureIndex(int pictureIndex) 
	{
		this.pictureIndex = pictureIndex;
	}

	public float getBlacklevel() 
	{
		return blacklevel;
	}

	public void setBlacklevel(float blacklevel) 
	{
		this.blacklevel = blacklevel;
	}

	public float getGain()
	{
		return gain;
	}

	public void setGain(float gain)
	{
		this.gain = gain;
	}
	
	public PictureEffectInfo getEffectInfor()
	{
		if(watermarkType == Watermark_Picture)
		{
			if(effect == null)
			{
				effect = new PictureEffectInfo();
				effect.setAlpha(Math.round(255 * opacity));
				effect.setBrightness(Math.round(255 * blacklevel));
			}
			
			return effect;					
		}
		
		return  null;
	}
	
	/**
	 * 
	 */
	public void dispose()
	{
		watermartString = null;
		if(effect != null)
		{
			effect.dispose();
			effect = null;
		}
	}
	
    private byte watermarkType;
    
    //for text watermark
    private String watermartString;    
    private boolean isAutoFontSize = false;
	private int fontSize = 36; 
	private int fontColor = 0xFF000000; 
	
	//for picture watermark
	private int pictureIndex = -1;
	private float blacklevel;
	private float gain;	
	private PictureEffectInfo effect;
	
    private float opacity = OPACITY;
}
