/*
 * 文件名称:           LineShape.java
 *  
 * 编译器:             android2.2
 * 时间:               下午3:37:58
 */
package com.nvqquy98.lib.doc.office.common.shape;

/**
 * lineShape
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-9-3
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class LineShape extends AutoShape
{
    /**
     * 
     */
    public LineShape()
    {
        
    }
    
    /**
     * 
     *
     */
    public short getType()
    {
        return SHAPE_LINE;
    }
    
    public void createStartArrow(byte type, int width, int length)
    {
        if(startArrow == null)
        {
            startArrow = new Arrow(type, width, length);
        }
        else
        {
            startArrow.setType(type);
            startArrow.setWidth(width);
            startArrow.setLength(length);  
        }        
    }
    
    public void createEndArrow(byte type, int width, int length)
    {
        if(endArrow == null)
        {
            endArrow = new Arrow(type, width, length);
        }
        else
        {
            endArrow.setType(type);
            endArrow.setWidth(width);
            endArrow.setLength(length);
        }        
    }
    
    /**
     * 
     * @return
     */
    public boolean getStartArrowhead()
    {
        return startArrow != null;
    }
    
    public int getStartArrowWidth()
    {
        if(startArrow != null)
        {
            return startArrow.getWidth();
        }
        return -1;
    }

    public void setStartArrowWidth(int startArrowWidth)
    {
        if(startArrow != null)
        {
            startArrow.setWidth(startArrowWidth);
        }
    }

    public int getStartArrowLength()
    {
        if(startArrow != null)
        {
            return startArrow.getLength();
        }
        return -1;
    }

    public void setStartArrowLength(int startArrowLength)
    {
        if(startArrow != null)
        {
            startArrow.setLength(startArrowLength);
        }        
    }    
    
    /**
     * 
     * @return
     */
    public byte getStartArrowType()
    {
        if(startArrow != null)
        {
            return startArrow.getType();
        }
        
        return Arrow.Arrow_None;
    }
    
    /**
     * 
     * @param type
     */
    public void setStartArrowType(byte type)
    {
        if(startArrow != null)
        {
            startArrow.setType(type);
        }
    }
    
    /**
     * 
     * @return
     */
    public boolean getEndArrowhead()
    {
        return endArrow !=  null;
    }
    
    public int getEndArrowWidth()
    {
        if(endArrow != null)
        {
            return endArrow.getWidth();
        }
        return -1;
    }

    public void setEndArrowWidth(int endArrowWidth)
    {
        if(endArrow != null)
        {
            endArrow.setWidth(endArrowWidth);
        }
    }

    public int getEndArrowLength()
    {
        if(endArrow != null)
        {
            return endArrow.getLength();
        }
        return -1;
    }

    public void setEndArrowLength(int endArrowLength)
    {
        if(endArrow != null)
        {
            endArrow.setLength(endArrowLength);
        }
    }
    
    /**
     * 
     * @return
     */
    public byte getEndArrowType()
    {
        if(endArrow != null)
        {
            return endArrow.getType();                
        }
        return Arrow.Arrow_None;
    }
    
    /**
     * 
     * @param type
     */
    public void setEndArrowType(byte type)
    {
        if(endArrow != null)
        {
            endArrow.setType(type);
        }
    }
    
    public Arrow getStartArrow()
    {
        return startArrow;
    }
    
    public Arrow getEndArrow()
    {
        return endArrow;
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see com.nvqquy98.lib.doc.office.common.shape.AutoShape#dispose()
     *
     */
    public void dispose()
    {
        startArrow = null;
        endArrow = null;
    }    
   
    // start arror or not
    private Arrow startArrow;
    // end arror or not
    private Arrow endArrow;
}
