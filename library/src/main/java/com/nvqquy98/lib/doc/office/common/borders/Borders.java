
package com.nvqquy98.lib.doc.office.common.borders;

public class Borders
{

    public Borders()
    {
        
    }
    
    /**
     * 
     * @return
     */
    public Border getTopBorder()
    {
        return this.top;
    }
    /**
     *
     */
    public void setTopBorder(Border b)
    {
        this.top = b;
    }
    
    
    /**
     * 
     * @return
     */
    public Border getLeftBorder()
    {
        return this.left;
    }
    /**
     *
     */
    public void setLeftBorder(Border b)
    {
        this.left = b;
    }
    
    
    /**
     * 
     * @return
     */
    public Border getRightBorder()
    {
        return this.right;
    }
    /**
     *
     */
    public void setRightBorder(Border b)
    {
        this.right = b;
    }
    
    /**
     * 
     * @return
     */
    public Border getBottomBorder()
    {
        return this.bottom;
    }
    /**
     *
     */
    public void setBottomBorder(Border b)
    {
        this.bottom = b;
    }
    
    /**
     * 
     * @return
     */
    public byte getOnType()
    {
        return onType;
    }
    /**
     * 
     */
    public void setOnType(byte onType)
    {
        this.onType = onType;
    }
    
    //
    private Border left;
    //
    private Border top;
    //
    private Border right;
    //
    private Border bottom;
    // on page or on text
    private byte onType;
}
