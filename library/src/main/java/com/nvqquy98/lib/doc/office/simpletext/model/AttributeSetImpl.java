/*
 * 文件名称:          AttributeSet.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:57:35
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;


/**
 * 属性集集合
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-28
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AttributeSetImpl implements IAttributeSet
{
    // 数组扩容值
    public static final int CAPACITY = 5;

    /**
     * 
     */
    public AttributeSetImpl()
    {
        arrayID = new short[10];
        arrayValue = new int[10];
    }
    
    /**
     * 得到属性集ID
     */
    public int getID()
    {
        return this.ID;
    }

    /**
     * 添加属性
     * @param attrID
     * @param value
     */
    public void setAttribute(short attrID, int value)
    {
        if (size >= arrayID.length)
        {
            ensureCapacity();
        }
        int index = getIDIndex(attrID);
        if (index >= 0)
        {
            arrayValue[index] = value;
        }
        else
        {
            arrayID[size] = attrID;
            arrayValue[size] = value;
            size++;
        }
        
    }

    /**
     * 删除属性
     * 
     * @param attrID
     */
    public void removeAttribute(short attrID)
    {
        int index = getIDIndex(attrID);
        if (index >= 0)
        {
            for (int i = index + 1; i < size; i++)
            {
                arrayID[i - 1] = arrayID[i];
                arrayValue[i - 1] = arrayValue[i];
            }
            size--;
        }
    }

    /**
     * 得到属性
     * @param attrID
     * @param value
     */
    public int getAttribute(short attrID)
    {
        return getAttribute(attrID, true);
    }
    
    /**
     * 得到属性
     * @param attrID
     * @param pStyle process style
     * @param value
     */
    private int getAttribute(short attrID, boolean pStyle)
    {        
        int index = getIDIndex(attrID);
        if (index >= 0)
        {
            return arrayValue[index];
        }
        if (!pStyle)
        {
            return Integer.MIN_VALUE;
        }
        //  
        Style style = null;
        int value = Integer.MIN_VALUE;
        // character attribute
        if (attrID < 0x0fff)
        {
            index = getIDIndex(AttrIDConstant.FONT_STYLE_ID);
            if (index >= 0)
            {
                style = StyleManage.instance().getStyle(arrayValue[index]);
                value = getAttributeForStyle(style, attrID);
            }
        }
        if (value != Integer.MIN_VALUE)
        {
            return value;
        }
        // paragraph attribute
        index = getIDIndex(AttrIDConstant.PARA_STYLE_ID);
        if (index >= 0)
        {
            style = StyleManage.instance().getStyle(arrayValue[index]);
            value = getAttributeForStyle(style, attrID);
        } 
        // paragraph attribute
        return value;
    }
    
    /**
     * 
     */
    private int getAttributeForStyle(Style style, short attrID)
    {   

        AttributeSetImpl attr = (AttributeSetImpl)style.getAttrbuteSet();
        int value = attr.getAttribute(attrID, false);
        if (value != Integer.MIN_VALUE)
        {
            return value;
        }
        if (style.getBaseID() >= 0)
        {
            style = StyleManage.instance().getStyle(style.getBaseID());
            return getAttributeForStyle(style, attrID);
        }
        return Integer.MIN_VALUE;
    }
    
    /**
     * 合并属性
     */
    public void mergeAttribute(IAttributeSet attr)
    {   
        if (!(attr instanceof AttributeSetImpl))
        {
            return;
        }
        AttributeSetImpl attrSet = (AttributeSetImpl)attr;
        int len = attrSet.arrayID.length;
        int index;
        for (int i = 0; i < len; i++)
        {
            index = getIDIndex(attrSet.arrayID[i]);
            if (index > 0)
            {
                arrayValue[index] = attrSet.arrayValue[i];
                continue;
            }
            if (size >= arrayID.length)
            {
                ensureCapacity();
            }
            arrayID[size] = attrSet.arrayID[i];
            arrayValue[size] = attrSet.arrayValue[i];
            size++;
        }
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see java.lang.Object#clone()
     *
     */
    public IAttributeSet clone()
    {
        AttributeSetImpl attr = new AttributeSetImpl();
        attr.size = size;
        short[] aID = new short[size];
        System.arraycopy(arrayID, 0, aID, 0, size);
        attr.arrayID = aID;
        int[] aValue = new int[size];
        System.arraycopy(arrayValue, 0, aValue, 0, size);
        attr.arrayValue = aValue;
        return attr;
    }
    
    /**
     * 得到attrID的index
     */
    private int getIDIndex(int attrID)
    {
        for (int i = 0; i < size; i++)
        {
            if (arrayID[i] == attrID)
            {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 
     */
    private void ensureCapacity()
    {
        int len = size + CAPACITY;
        short[] aID = new short[len];
        System.arraycopy(arrayID, 0, aID, 0, size);
        arrayID = aID;
        int[] aValue = new int[len];
        System.arraycopy(arrayValue, 0, aValue, 0, size);
        arrayValue = aValue;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        arrayID = null;
        arrayValue = null;    
    }
    
    // 属性值个数
    private int size = 0;;
    //
    private int ID;
    // ID
    private short[] arrayID;
    // value
    private int[] arrayValue;
    
    
}
