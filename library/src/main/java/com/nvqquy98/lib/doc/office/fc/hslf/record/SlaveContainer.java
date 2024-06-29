/*
 * 文件名称:          SlaveContainer.java
 *  
 * 编译器:            android2.2
 * 时间:              下午8:13:03
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

/**
 * TODO: A container record that specifies a subordinate time node whose start time 
 * depends on the relation to its master time node.At most one of the following fields 
 * MUST exist: timeColorBehavior, timeSetBehavior, or timeCommandBehavior.
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
public class SlaveContainer extends PositionDependentRecordContainer
{
    private byte[] _header;
    private static long _type = 0xF145;
    
    /**
     * We are of type 0xF144
     */
    public long getRecordType()
    {
        return _type;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected SlaveContainer(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}

