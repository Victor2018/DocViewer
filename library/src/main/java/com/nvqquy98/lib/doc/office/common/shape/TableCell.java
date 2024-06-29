/*
 * 文件名称:           TableCell.java
 *  
 * 编译器:             android2.2
 * 时间:               下午3:02:39
 */
package com.nvqquy98.lib.doc.office.common.shape;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.java.awt.Rectanglef;

/**
 * Represents a table cell
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-4-11
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class TableCell
{
    /**
     * 
     */
    public TableCell()
    {
        super();
    }
    
    /**
     * 
     * @return
     */
    public Line getLeftLine()
    {
        return left;
    }
    
    /**
     * 
     * @param borderLColor
     */
    public void setLeftLine(Line left)
    {
        this.left = left;
    }
    
    /**
     * 
     * @return
     */
    public Line getRightLine()
    {
        return right;
    }
    
    /**
     * 
     * @param borderRColor
     */
    public void setRightLine(Line right)
    {
        this.right = right;
    }
    
    /**
     * 
     * @return
     */
    public Line getTopLine()
    {
        return top;
    }
    
    /**
     * 
     * @param borderTColor
     */
    public void setTopLine(Line top)
    {
        this.top = top;
    }
    
    /**
     * 
     * @return
     */
    public Line getBottomLine()
    {
        return bottom;
    }
    
    /**
     * 
     * @param borderBColor
     */
    public void setBottomLine(Line bottom)
    {
        this.bottom = bottom;
    }
    
    /**
     * 
     * @return
     */
    public TextBox getText()
    {
        return textBox;
    }
    
    /**
     * 
     * @param textBox
     */
    public void setText(TextBox textBox)
    {
        this.textBox = textBox;
    }
    
    /**
    *
    *
    */ 
   public Rectanglef getBounds()
   {
       return rect;
   }

   /**
    * 
    *
    */
   public void setBounds(Rectanglef rect)
   {
       this.rect = rect;
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
    
    /**
     * 
     */
    public void dispose()
    {
        if (textBox != null)
        {
            textBox.dispose();
            textBox = null;
        }
        rect = null;
        if (bgFill != null)
        {
            bgFill.dispose();
            bgFill = null;
        }
    }

    // border left color
    private Line left;
    // border right color
    private Line right;
    // border top color
    private Line top;
    // border bottom color
    private Line bottom;
    // text
    private TextBox textBox;
    // size of this cell
    protected Rectanglef rect;
    // background
    private BackgroundAndFill bgFill;
}
