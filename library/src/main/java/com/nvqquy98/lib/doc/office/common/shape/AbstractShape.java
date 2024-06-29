/*
 * 文件名称:          AbstractShape.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:07:59
 */
package com.nvqquy98.lib.doc.office.common.shape;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.animate.IAnimation;

/**
 * shape的抽象类
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AbstractShape implements IShape
{
    // picture
    public static final short SHAPE_PICTURE = 0;
    // text box
    public static final short SHAPE_TEXTBOX = SHAPE_PICTURE + 1; // 1 
    // auto shape
    public static final short SHAPE_AUTOSHAPE = SHAPE_TEXTBOX + 1 ;// 2 
    //
    public static final short SHAPE_BG_FILL = SHAPE_AUTOSHAPE + 1; // 3
    //
    public static final short SHAPE_LINE = SHAPE_BG_FILL + 1; // 4 
    // chart
    public static final short SHAPE_CHART = SHAPE_LINE + 1; // 5
    // table
    public static final short SHAPE_TABLE = SHAPE_CHART + 1; // 6
    //group shape
    public static final short SHAPE_GROUP = SHAPE_TABLE + 1; // 7
    //smart art
    public static final short SHAPE_SMARTART = SHAPE_GROUP + 1; // 8
    
    /**
     * 
     *
     */
    public short getType()
    {
        return -1;
    }

    /**
     * 
     *
     */
    public IShape getParent()
    {
        return parent;
    }
    
    /**
     * set parent of this shape;
     */
    public void setParent(IShape shape)
    {
        this.parent = shape;
    }
    
    /**
     * 
     * @param id
     */
    public void setGroupShapeID(int id)
    {
        grpSpID = id;
    }
    
    /**
     * 
     * @return
     */
    public int getGroupShapeID()
    {
        return grpSpID;
    }
    
    public void setShapeID(int id)
    {
        this.id = id;
    }
    
    public int getShapeID()
    {
        return id;
    }
    
    /**
     *
     *
     */ 
    public Rectangle getBounds()
    {
        return rect;
    }

    /**
     * 
     *
     */
    public void setBounds(Rectangle rect)
    {
        this.rect = rect;
    }

    /**
     * 
     *
     */
    public Object getData()
    {
        return null;
    }

    /**
     * 
     */
    public void setData(Object data)
    {
        
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
     * get horizontal flip of this shape
     */
    public boolean getFlipHorizontal()
    {
        return flipH;
    }
    
    /**
     * set horizontal flip of this shape
     */
    public void setFlipHorizontal(boolean flipH)
    {
        this.flipH = flipH;
    }
    
    /**
     * get vertical flip of this shape
     */
    public boolean getFlipVertical()
    {
        return flipV;
    }
    
    /**
     * set vertical flip of this shape
     */
    public void setFlipVertical(boolean flipV)
    {
        this.flipV = flipV;
    }
    
    /**
     * get rotation of this shape
     */
    public float getRotation()
    {
        return angle;
    }
    
    /**
     * set rotation of this shape
     */
    public void setRotation(float angle)
    {
        this.angle = angle;
    }
    
    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }
    
    public boolean isHidden()
    {
        return hidden;
    }
    
    /**
     * set shape animation
     * @param animation
     */
    public void setAnimation(IAnimation animation)
    {
        this.animation = animation;
    }
    
    public IAnimation getAnimation()
    {
        return animation;
    }
    
    /*
     * 
     */
    public boolean hasLine()
    {
        return line != null;
    }
    
    public void setLine(boolean hasLine)
    {
    	this.hasLine = hasLine;
    	if(hasLine && line == null)
    	{
    		line = new Line();
    	}
    }
    public void setLine(Line line)
    {
    	this.line = line;
    	if(line !=  null)
    	{
    		this.hasLine = true;
    	}
    }
    
    /**
     * 
     * @param border
     */
    public Line createLine()
    {
    	line = new Line();
    	return line;
    }
    
    /**
     * get line width
     * @return
     */
    public Line getLine()
    {
        return line;
    }
    
    /**
     * 
     * @return
     */
    public int getPlaceHolderID()
    {
        return placeHolderID;
    }
    
    /**
     * 
     * @param placeHolderID
     */
    public void setPlaceHolderID(int placeHolderID)
    {
        this.placeHolderID = placeHolderID;
    }
    
    /**
     * 
     *
     */
    public void dispose()
    {
        if (parent != null)
        {
            parent.dispose();
            parent = null;
        }
        rect = null;
        if(animation != null)
        {
            animation.dispose();
            animation = null;
        }
        if (bgFill != null)
        {
            bgFill.dispose();
            bgFill = null;
        }
        
        if(line != null)
        {
        	line.dispose();
        	line = null;
        }
    }
    
    // parent of this shape
    private IShape parent;
    //group shape id
    private int grpSpID = -1;
    //shape id
    private int id;
    
    // background
    private BackgroundAndFill bgFill;
    // size of this shape
    protected Rectangle rect;
    // 
    private boolean flipH;
    //
    private boolean flipV;
    //
    private float angle;
    
    private boolean hidden;
    private IAnimation animation;
    
    private boolean hasLine;
    private Line line;
    private int placeHolderID;
}
