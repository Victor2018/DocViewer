/*
 * 文件名称:           PGLayout.java
 *  
 * 编译器:             android2.2
 * 时间:               下午12:50:03
 */
package com.nvqquy98.lib.doc.office.pg.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;

/**
 * layout 数据
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-3-3
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PGLayout
{
    /**
     * 
     */
    public PGLayout()
    {
        styleByType = new HashMap<String, PGStyle>();
        styleByIdx = new HashMap<Integer, PGStyle>();
    }
    
    /**
     * get anchor by shape type or index
     */
    public Rectangle getAnchor(String type, int idx)
    {
        if (!PGPlaceholderUtil.instance().isBody(type))
        {
            PGStyle style = styleByType.get(type);
            if (style != null)
            {
                return style.getAnchor();
            }
        }
        else if (idx > 0)
        {
            PGStyle style = styleByIdx.get(idx);
            if (style != null)
            {
                return style.getAnchor();
            }
        }
        return null;
    }
 
    /**
     * get shape section attribute by type or index
     */
    public IAttributeSet getSectionAttr(String type, int idx)
    {       
        if (!PGPlaceholderUtil.instance().isBody(type))
        {
            PGStyle style = styleByType.get(type);
            if (style != null)
            {
                return style.getSectionAttr();
            }
        }
        else if (idx > 0)
        {
            PGStyle style = styleByIdx.get(idx);
            if (style != null)
            {
                return style.getSectionAttr();
            }
        }
        return null;
    }
    
    /**
     * get style ID
     */
    public int getStyleID(String type, int idx, int lvl)
    {
        if (!PGPlaceholderUtil.instance().isBody(type))
        {
            PGStyle style = styleByType.get(type);
            if (style != null)
            {
                return style.getStyle(lvl);
            }
        }
        else if (idx > 0)
        {
            PGStyle style = styleByIdx.get(idx);
            if (style != null)
            {
                return style.getStyle(lvl);
            }
        }
        return -1;
    }
    
    /**
     * set style by type
     */
    public void setStyleByType(String type, PGStyle style)
    {
        styleByType.put(type, style);
    }
    
    /**
     * set style by index
     */
    public void setStyleByIdx(int idx, PGStyle style)
    {
        styleByIdx.put(idx, style);
    }
    
    /**
     * get background
     */
    public BackgroundAndFill getBackgroundAndFill()
    {
        return bgFill;
    }
   
    /**
     * set background
     */
    public void setBackgroundAndFill(BackgroundAndFill bgFill)
    {
        this.bgFill = bgFill;
    }
    
    /**
     * 
     * @return
     */
    public int getSlideMasterIndex()
    {
        return index;
    }
    
    /**
     * 
     * @param index
     */
    public void setSlideMasterIndex(int index)
    {
        this.index = index;
    }
    
    /**
     * 
     * @return
     */
    public boolean isAddShapes()
    {
        return addShapes;
    }
    
    /**
     * 
     * @param masterShape
     */
    public void setAddShapes(boolean addShapes)
    {
        this.addShapes = addShapes;
    }  

    /**
     * 
     * @param idx
     * @param type title, crtTitle, subTitle, body
     */
    public void addShapeType(int idx, String type)
    {
        if(shapeType == null)
        {
            shapeType = new HashMap<Integer, String>();
        }
        shapeType.put(idx, type);
    }
    
    /**
     * 
     * @param idx
     * @return
     */
    public String getShapeType(int idx)
    {
        if(shapeType != null)
        {
            return shapeType.get(idx);
        }     
        return null;
    }
    
    public void addTitleBodyID(int idx, int id)
    {
        if(titlebodyID == null)
        {
            titlebodyID = new HashMap<Integer, Integer>();
        }
        titlebodyID.put(idx, id);
    }
    
    public Integer getTitleBodyID(int idx)
    {
        if(titlebodyID != null)
        {
            return titlebodyID.get(idx);
        }  
        return null;
    }
    
    /**
     * 
     */
    public void disposs()
    {
        if (bgFill != null)
        {
            bgFill.dispose();
            bgFill = null;
        }
        if (styleByType != null)
        {
            Iterator<String> iter = styleByType.keySet().iterator();
            while(iter.hasNext())
            {
                styleByType.get(iter.next()).dispose();
            }
            styleByType.clear();
            styleByType = null;
        }
        if (styleByIdx != null)
        {
            Iterator<Integer> iter = styleByIdx.keySet().iterator();
            while(iter.hasNext())
            {
                styleByIdx.get(iter.next()).dispose();
            }
            styleByIdx.clear();
            styleByIdx = null;
        }       

        if(shapeType != null)
        {
            shapeType.clear();
            shapeType =  null;
        }
        
        if(titlebodyID != null)
        {
            titlebodyID.clear();
            titlebodyID =  null;
        }
    }
 
    // background
    private BackgroundAndFill bgFill;
    // title style
    private Map<String, PGStyle> styleByType;
    // body style
    private Map<Integer, PGStyle> styleByIdx;
    //shape(just for title and body) idx, shape type
    private Map<Integer, String> shapeType;
    //shape(just for title and body) idx, shape id
    private Map<Integer, Integer> titlebodyID;
    // masterslide index
    private int index = -1;
    // add masterShape or not
    private boolean addShapes = true;
}
