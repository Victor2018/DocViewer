package com.nvqquy98.lib.doc.office.common.bg;

import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;

public class RadialGradientShader extends Gradient
{
	//the center of the radius is in the top and left corner of rect
	public static final int Center_TL = 0;
	//the center of the radius is in the top and right corner of rect
	public static final int Center_TR = 1;
	//the center of the radius is in the bottom and left corner of rect
	public static final int Center_BL = 2;
	//the center of the radius is in the bottom and right corner of rect
	public static final int Center_BR = 3;
	//the center of the radius is in the center of rect
	public static final int Center_Center = 4;
	
	public RadialGradientShader(int positionType, int[] colors, float[] positions)
	{
		super(colors, positions);
		this.positionType = positionType;
	}
	
	public int getGradientType() 
	{
		return BackgroundAndFill.FILL_SHADE_RADIAL;
	}
	
	public Shader createShader(IControl control, int viewIndex, Rect rect)
    {
		int[] coordinate = getCircleCoordinate();
		if(positionType == Center_Center && getFocus() == 0)
		{
			int size = colors.length;
			int nTem = 0;
			for(int i = 0; i < size / 2; i++)
			{
				nTem = colors[i];
				colors[i] = colors[size - 1 - i];
				colors[size - 1 - i] = nTem;
			}
		}
    	shader = new RadialGradient(coordinate[0], coordinate[1], coordinate[2],
    			colors, positions, Shader.TileMode.REPEAT);
    	return shader;
    }
	
	 /**
     * 0:The x-coordinate of the center of the radius
     * 1:The y-coordinate of the center of the radius
     * 2:Must be positive. The radius of the circle for this gradient
     * @param angle
     * @return
     */
	private int[] getCircleCoordinate()
	{
		int radius = (int)Math.ceil(Math.sqrt(Math.pow(COORDINATE_LENGTH, 2) * 2));
		switch(positionType)
		{			
			case Center_TR:
				return new int[]{COORDINATE_LENGTH, 0, radius};
			case Center_BL:
				return new int[]{0, COORDINATE_LENGTH, radius};
			case Center_BR:
				return new int[]{COORDINATE_LENGTH, COORDINATE_LENGTH, radius};
			case Center_Center:
				return new int[]{COORDINATE_LENGTH / 2, COORDINATE_LENGTH / 2, radius / 2};
			default:
				return new int[]{0, 0, radius};
		}
	}
	
	private int positionType;
}
