/*
 * 文件名称:          TimeNodeAttributeContainer.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:26:17
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

/**
 * TODO: A container record that specifies a list of attributes for a time node.
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-1-6
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class TimeNodeAttributeContainer extends PositionDependentRecordContainer
{   
    private byte[] _header;
    public static long RECORD_ID = 0xF13D;
    
    /**
     * We are of type 0xF13D
     */
    public long getRecordType()
    {
        return RECORD_ID;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected TimeNodeAttributeContainer(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}
