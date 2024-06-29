package com.nvqquy98.lib.doc.office.common.borders;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;

public class Line extends Border
{

	public Line()
	{
		setLineWidth((short)1);
	}
	
    /**
     * @return Returns the bgFill.
     */
    public BackgroundAndFill getBackgroundAndFill()
    {
        return bgFill;
    }

    /**
     * @param bgFill The bgFill to set.
     */
    public void setBackgroundAndFill(BackgroundAndFill bgFill)
    {
        this.bgFill = bgFill;
    }
    
    public void setDash(boolean dash)
    {
    	this.dash = dash;
    }
    
    public boolean isDash()
    {
    	return dash;
    }
    
    public void dispose()
    {
    	if(bgFill != null)
    	{
    		bgFill = null;
    	}
    }
    
	// background
    private BackgroundAndFill bgFill;
    
	/**
     * this patheffect only affects drawing with the paint's style is set
     * to STROKE or STROKE_AND_FILL. It is ignored if the drawing is done with
     * style == FILL. app ignores dash type and only as dash
     * 
     *solid 		Solid 
     *dot 			Dot 
     *dash 			Dash 
     *lgDash 		Large Dash 
     *dashDot 		Dash Dot 
     *lgDashDot 	Large Dash Dot 
     *lgDashDotDot 	Large Dash Dot Dot 
     *sysDash 		System Dash 
     *sysDot 		System Dot 
     *sysDashDot 	System Dash Dot 
     *sysDashDotDot System Dash Dot Dot 
     */
    private boolean dash;
}
