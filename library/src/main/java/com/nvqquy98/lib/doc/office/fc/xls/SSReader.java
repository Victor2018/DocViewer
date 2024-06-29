/*
 * 文件名称:          SSReader.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:23:56
 */
package com.nvqquy98.lib.doc.office.fc.xls;

import com.nvqquy98.lib.doc.office.system.AbstractReader;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-8-13
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SSReader extends AbstractReader
{
    /**
     * 
     */
    public void abortCurrentReading()
    {
        abortReader = false;
    }
}
