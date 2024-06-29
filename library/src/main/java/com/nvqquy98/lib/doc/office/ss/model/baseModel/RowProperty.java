/*
 * 文件名称:          RowProperty.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:10:46
 */
package com.nvqquy98.lib.doc.office.ss.model.baseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.ss.other.ExpandedCellRangeAddress;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-9-3
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class RowProperty
{
    public static final short ROWPROPID_ZEROHEIGHT = 0;
    
    public static final short ROWPROPID_COMPLETED = 1;
    
    public static final short ROWPROPID_INITEXPANDEDRANGEADDR = 2;
    
    public static final short ROWPROPID_EXPANDEDRANGEADDRLIST = 3;
    
    public RowProperty()
    {
        rowPropMap = new HashMap<Short, Object>();
    }
    /**
     * 
     * @param rowPropID
     * @param value
     */
    public void setRowProperty(short rowPropID, Object value)
    {
        if(rowPropID != ROWPROPID_EXPANDEDRANGEADDRLIST)
        {
            rowPropMap.put(rowPropID, value);
        }
        else
        {
            List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>)rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
            if(expandedRangeAddr == null)
            {
                expandedRangeAddr = new ArrayList<ExpandedCellRangeAddress>();
                rowPropMap.put(rowPropID, expandedRangeAddr);
            }
            expandedRangeAddr.add((ExpandedCellRangeAddress)value);
            
        }
    }
    
    /**
     * 
     * @return
     */
    public boolean isZeroHeight()
    {
        Object obj = rowPropMap.get(ROWPROPID_ZEROHEIGHT);
        if(obj != null)
        {
            return (Boolean)obj;
        }
        
        return false;
    }
    
    /**
     * 
     * @return
     */
    public boolean isCompleted()
    {
        Object obj = rowPropMap.get(ROWPROPID_COMPLETED);
        if(obj != null)
        {
            return (Boolean)obj;
        }
        
        return false;
    }
    
    /**
     * 
     * @return
     */
    public boolean isInitExpandedRangeAddr()
    {
        Object obj = rowPropMap.get(ROWPROPID_INITEXPANDEDRANGEADDR);
        if(obj != null)
        {
            return (Boolean)obj;
        }
        
        return false;
    }
    
    /**
     * 
     * @return
     */
    public int getExpandedCellCount()
    {
        List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>)rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
        if(expandedRangeAddr == null)
        {
            return 0;
        }
        return expandedRangeAddr.size();
    }
    /**
     * 
     * @param index
     * @return
     */
    public ExpandedCellRangeAddress getExpandedCellRangeAddr(int index)
    {
        List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>)rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
        if(expandedRangeAddr == null)
        {
            return null;
        }
        return expandedRangeAddr.get(index);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>)rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
        if(expandedRangeAddr != null)
        {
            Iterator<ExpandedCellRangeAddress> iter = expandedRangeAddr.iterator();
            while(iter.hasNext())
            {
                iter.next().dispose();
            }
        }
        
    }
    
    /**
     * 
     */
    private Map<Short, Object> rowPropMap;
    
}
