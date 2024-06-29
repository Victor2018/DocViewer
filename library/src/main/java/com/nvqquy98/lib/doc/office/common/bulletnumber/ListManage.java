package com.nvqquy98.lib.doc.office.common.bulletnumber;

import java.util.LinkedHashMap;
import java.util.Set;

public class ListManage
{
    
    public ListManage()
    {
        lists = new LinkedHashMap<Integer, ListData>();
    }
    
    /**
     * 
     */
    public int putListData(Integer key, ListData value)
    {
        lists.put(key, value);
        return lists.size() - 1;
    }
    
    /**
     * 
     */
    public ListData getListData(Integer key)
    {
        return lists.get(key);
    }
    
    /**
     * 
     */
    public void resetForNormalView()
    {
        Set<Integer> sets = lists.keySet();
        for (Integer key : sets)
        {
            ListData list = lists.get(key);
            if (list != null)
            {
                list.setNormalPreParaLevel((byte)0);
                list.resetForNormalView();
            }
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (lists != null)
        {
            Set<Integer> sets = lists.keySet();
            for (Integer key : sets)
            {
                lists.get(key).dispose();
            }
            lists.clear();
        }
    }
    
    //
    private LinkedHashMap<Integer, ListData> lists;
}
