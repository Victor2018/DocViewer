/*
 * 文件名称:          TimeConditionContainer.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:44:29
 */
package com.cherry.lib.doc.office.fc.hslf.record;

import com.cherry.lib.doc.office.fc.hslf.record.PositionDependentRecordContainer;
import com.cherry.lib.doc.office.fc.hslf.record.Record;

/**
 * TODO: A container record that specifies a time condition of a time node.
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-1-7
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class TimeConditionContainer extends PositionDependentRecordContainer
{
    private byte[] _header;
    private static long _type = 0xF125;
    
    /**
     * We are of type 0xF13D
     */
    public long getRecordType()
    {
        return _type;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected TimeConditionContainer(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}
