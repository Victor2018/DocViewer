/*
 * 文件名称:          RefUtil.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:54:34
 */
package com.nvqquy98.lib.doc.office.ss.util;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-27
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ReferenceUtil
{
    //
    private static ReferenceUtil util = new ReferenceUtil();
    //
    public static ReferenceUtil instance()
    {
        return util;
    }
    
    public int getColumnIndex(String ref)
    {
        int end = 0;
        for(; end < ref.length(); end++)
        {
            if(ref.charAt(end) >= '0' && ref.charAt(end) <= '9')
            {
                break;
            }
        }
        String colString = ref.substring(0, end);
        return HeaderUtil.instance().getColumnHeaderIndexByText(colString);
    }
    
    public int getRowIndex(String ref)
    {
        if(ref.indexOf(":") > 0)
        {
            ref = ref.substring(0, ref.indexOf(":"));
        }
        
        int end = 0;
        for(; end < ref.length(); end++)
        {
            if(ref.charAt(end) >= '0' && ref.charAt(end) <= '9')
            {
                break;
            }
        }
        String rowString = ref.substring(end, ref.length());
        return Integer.parseInt(rowString) - 1;
    }
}
