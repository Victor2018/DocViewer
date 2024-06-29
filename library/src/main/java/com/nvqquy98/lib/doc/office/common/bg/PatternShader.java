package com.nvqquy98.lib.doc.office.common.bg;

import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

public class PatternShader extends AShader
{
	public PatternShader(Picture picture, int backgroundColor, int foregroundColor)
	{
		this.picture = picture;
		this.backgroundColor =  backgroundColor;
		this.foregroundColor = foregroundColor;
	}
	
	public Shader createShader(IControl control, int viewIndex, Rect rect)
	{
		try
		{
			Bitmap bmp = TileShader.getBitmap(control, viewIndex, picture, rect, null);
			if(bmp != null)
			{
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				int[] pixels = new int[width * height];
		    	bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		    	for(int i = 0; i < width * height; i++)
		    	{
		    		if((pixels[i] & 0xFFFFFF) == 0)
		    		{
		    			pixels[i] = backgroundColor;
		    		}
		    		else
		    		{
		    			pixels[i] = foregroundColor;
		    		}
		    	}
		    	
		    	bmp = Bitmap.createBitmap(pixels, width, height, Config.ARGB_8888);
				TileMode tileX = Shader.TileMode.REPEAT;
				TileMode tileY = Shader.TileMode.REPEAT;
				
				shader = new BitmapShader(bmp, tileX, tileY);
			}
			
			return shader;
		}
		catch(Exception e)
		{
			return null;
		}		
	}
	
	private Picture picture;
	private int backgroundColor;
	private int foregroundColor;
}
