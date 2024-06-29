/*
 * 文件名称:          IShape.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:23:12
 */
package com.nvqquy98.lib.doc.office.common.shape;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.animate.IAnimation;


/**
 * PowerPoint的shape的接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-13
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IShape
{
    /**
     * 
     * @param id
     */
    public void setGroupShapeID(int id);
    
    /**
     * 
     * @return
     */
    public int getGroupShapeID();
    
    /**
     * 
     * @param id
     */
    public void setShapeID(int id);
    
    /**
     * 
     * @return
     */
    public int getShapeID();
    
    /**
     * 
     * @return
     */
    public short getType();
    /**
     *  @return the parent of this shape
     */
    public IShape getParent();
    
    /**
     * set parent of this shape;
     */
    public void setParent(IShape shape);
    
    /**
     * get size of this shape
     */
    public Rectangle getBounds();
    /**
     * set size of this shape
     */
    public void setBounds(Rectangle rect);
    /**
     * get data of this shape
     */
    public Object getData();
    /**
     * set data of this shape
     */
    public void setData(Object data);
    /**
     * get horizontal flip of this shape
     */
    public boolean getFlipHorizontal();
    /**
     * set horizontal flip of this shape
     */
    public void setFlipHorizontal(boolean flipH);
    /**
     * get vertical flip of this shape
     */
    public boolean getFlipVertical();
    /**
     * set vertical flip of this shape
     */
    public void setFlipVertical(boolean flipV);
    /**
     * get rotation of this shape
     */
    public float getRotation();
    /**
     * set rotation of this shape
     */
    public void setRotation(float angle);
    
    /**
     * 
     * @param hidden
     */
    public void setHidden(boolean hidden);
    
    /**
     * 
     * @return
     */
    public boolean isHidden();
    
    /**
     * set shape animation
     * @param animation
     */
    public void setAnimation(IAnimation animation);
    
    /**
     * 
     * @return
     */
    public IAnimation getAnimation();
    
    /**
     * 
     * @return
     */
    public int getPlaceHolderID();
    
    /**
     * 
     * @param placeHolderID
     */
    public void setPlaceHolderID(int placeHolderID);
    
    /**
     * dispose
     */
    public void dispose();
    
}
