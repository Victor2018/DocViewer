package com.nvqquy98.lib.doc.office.common.bg;

import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Rect;
import android.graphics.Shader;

public abstract class AShader
{
	public Shader getShader() 
	{		
		return shader;
	}

	public Shader createShader(IControl control, int viewIndex, Rect rect)
	{
		return shader;
	}
	
	public int getAlpha()
	{
		return alpha;
	}

	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
	}

	 /**
     * 
     *
     */
    public void dispose()
    {
    	shader = null;
    }
    
	protected int alpha = 255;
	protected Shader shader = null;
}
