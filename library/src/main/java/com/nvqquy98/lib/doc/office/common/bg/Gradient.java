package com.nvqquy98.lib.doc.office.common.bg;


public abstract class Gradient extends AShader
{
	
	public static final int COORDINATE_LENGTH = 100;
	
	public Gradient(int[] colors, float[] positions) 
	{
		if(colors != null && colors.length >= 2)
		{
			this.colors = colors;
		}
		
		this.positions = positions;
	}
	
	public int getGradientType() 
	{
		return type;
	}

	public void setGradientType(int type)
	{
		this.type = type;
	}
	
	public int getFocus() 
	{
		return focus;
	}

	public void setFocus(int focus) 
	{
		this.focus = focus;
	}
	
	
	private int type;
	protected int[] colors = null;
	protected float[] positions = null;

	//percent
	private int focus = 100;	
}
