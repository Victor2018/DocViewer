/*
 * 文件名称:          SchemeColorUtil.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:37:43
 */
package com.nvqquy98.lib.doc.office.fc.xls.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.constant.SchemeClrConstant;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-18
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SchemeColorUtil
{
    public static int getThemeColor(Workbook workbook, int index)
    {
        init(workbook);
        if(index < 0 || index >= schemeClrName.size())
        {
            return -1;
        }
        
        return workbook.getSchemeColor(schemeClrName.get(index));
    }
    
    /**
     * get workbook scheme color
     * @param sheet
     * @return scheme color name and color
     */
    public static Map<String, Integer> getSchemeColor(Workbook workbook)
    {
        init(workbook);
        
        return schemeColor;
    }
    
    private static void init(Workbook workbook)
    {
        if(schemeColor == null)
        {
            schemeClrName = new ArrayList<String>();
            schemeClrName.add(SchemeClrConstant.SCHEME_BG1);
            schemeClrName.add(SchemeClrConstant.SCHEME_TX1);
            schemeClrName.add(SchemeClrConstant.SCHEME_BG2);
            schemeClrName.add(SchemeClrConstant.SCHEME_TX2);
            
            schemeClrName.add(SchemeClrConstant.SCHEME_ACCENT1);
            schemeClrName.add(SchemeClrConstant.SCHEME_ACCENT2);
            schemeClrName.add(SchemeClrConstant.SCHEME_ACCENT3);
            schemeClrName.add(SchemeClrConstant.SCHEME_ACCENT4);
            schemeClrName.add(SchemeClrConstant.SCHEME_ACCENT5);
            schemeClrName.add(SchemeClrConstant.SCHEME_ACCENT6);
            
            schemeClrName.add(SchemeClrConstant.SCHEME_HLINK);
            schemeClrName.add(SchemeClrConstant.SCHEME_FOLHLINK);
            
            schemeClrName.add(SchemeClrConstant.SCHEME_DK1);
            schemeClrName.add(SchemeClrConstant.SCHEME_LT1);
            schemeClrName.add(SchemeClrConstant.SCHEME_DK2);
            schemeClrName.add(SchemeClrConstant.SCHEME_LT2);
            
            schemeColor = new HashMap<String, Integer>();            
        }
        
        //multi file, scheme maybe not same, so update workbook scheme color
        schemeColor.clear();
        
        Iterator<String> iter = schemeClrName.iterator();
        String key;
        while(iter.hasNext())
        {
            key = iter.next();
            schemeColor.put(key, workbook.getSchemeColor(key));
        }
    }
    
    private static List<String> schemeClrName;
    private static Map<String, Integer> schemeColor;
}
