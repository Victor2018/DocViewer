/*
 * 文件名称:          CellBorder.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:55:45
 */
package com.nvqquy98.lib.doc.office.ss.model.style;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-22
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class CellBorder
{
    public CellBorder()
    {   
        left = new BorderStyle();
        top = new BorderStyle();
        right = new BorderStyle();
        bottom = new BorderStyle();
    }
    
    public void setLeftBorder(BorderStyle left)
    {
        this.left = left;
    }
    
    public BorderStyle getLeftBorder()
    {
        return left;
    }
    
    public void setTopBorder(BorderStyle top)
    {
        this.top = top;
    }
    
    public BorderStyle getTopBorder()
    {
        return top;
    }
    
    public void setRightBorder(BorderStyle right)
    {
        this.right = right;
    }
    
    public BorderStyle getRightBorder()
    {
        return right;
    }
    
    public void setBottomBorder(BorderStyle bottom)
    {
        this.bottom = bottom;
    }
    
    public BorderStyle getBottomBorder()
    {
        return bottom;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if(left != null)
        {
            left.dispose();
            left = null;
        }
        
        if(top != null)
        {
            top.dispose();
            top = null; 
        }
        
        if(right != null)
        {
            right.dispose();
            right = null;
        }
        
        if(bottom != null)
        {
            bottom.dispose();
            bottom = null;
        }
    }
    
    private BorderStyle left;
    private BorderStyle top;
    private BorderStyle right;
    private BorderStyle bottom;
}
