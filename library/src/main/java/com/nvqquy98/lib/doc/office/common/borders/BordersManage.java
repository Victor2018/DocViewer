/*
 * 文件名称:          	BordersManage.java
 *  
 * 编译器:            android2.2
 * 时间:             	上午9:38:51
 */
package com.nvqquy98.lib.doc.office.common.borders;

import java.util.ArrayList;
import java.util.List;

public class BordersManage
{
    /**
     * 
     */
    public int addBorders(Borders bs)
    {
        int size = borders.size();
        borders.add(bs);
        return size;
    }
    
    /**
     * 
     * @param index
     * @return
     */
    public Borders getBorders(int index)
    {
        return borders.get(index);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (borders != null)
        {
            borders.clear();
            borders = null;
        }
    }
    
    private List<Borders> borders = new ArrayList<Borders>();
}
