package com.nvqquy98.lib.doc.office.common.bg;

import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;

public class LinearGradientShader extends Gradient
{
	public LinearGradientShader(float angle, int[] colors, float[] positions)
	{
		super(colors, positions);
		this.angle = angle;
	}
	
	public int getGradientType() 
	{
		return BackgroundAndFill.FILL_SHADE_LINEAR;
	}
	
	public int getAngle()
	{
		return (int)angle;
	}
	
	public Shader createShader(IControl control, int viewIndex, Rect rect)
    {
		try
		{
			int[] coordinate = getLinearGradientCoordinate();
	    	shader = new LinearGradient(coordinate[0],coordinate[1],coordinate[2],coordinate[3],
	    			colors, positions, Shader.TileMode.MIRROR);
	    	return shader;
		}
		catch(Exception e)
		{
			return null;
		}
    	
    }
	
	 /**
     * 0:The x-coordinate for the start of the gradient line
     * 1:The y-coordinate for the start of the gradient line
     * 2:x1 The x-coordinate for the end of the gradient line
     * 3:y1 The y-coordinate for the end of the gradient line
     * @param angle
     * @return
     */
	private int[] getLinearGradientCoordinate()
	{
		switch(Math.round((angle+22) % 360 / 45))
		{
			case 0:				//338-0-22
				return new int[]{0, 0, COORDINATE_LENGTH, 0};
			case 1:				//23-45-67
				return new int[]{0, 0, COORDINATE_LENGTH, COORDINATE_LENGTH};
			case 2:				//68-90-112
				return new int[]{0, 0, 0, COORDINATE_LENGTH};
			case 3:				//113-135-157
				return new int[]{COORDINATE_LENGTH, 0, 0, COORDINATE_LENGTH};
			case 4:				//158-180-202
				return new int[]{COORDINATE_LENGTH, 0, 0, 0};
			case 5:				//203-225-247
				return new int[]{COORDINATE_LENGTH, COORDINATE_LENGTH, 0, 0};
			case 6:				//248-270-292
				return new int[]{0, COORDINATE_LENGTH, 0, 0};
			default:				//293-315-337
				return new int[]{0, COORDINATE_LENGTH, COORDINATE_LENGTH, 0};
		}
	}
	
	
	private float angle;
}
