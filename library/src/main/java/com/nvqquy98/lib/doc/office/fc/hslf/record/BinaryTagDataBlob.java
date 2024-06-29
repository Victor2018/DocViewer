/*
 * 文件名称:          BinaryTagDataBlob.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:26:07
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

/**
 * TODO: An atom record that contains the value of the name-value pair 
 * in a programmable tag.
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-1-8
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class BinaryTagDataBlob extends PositionDependentRecordContainer
{
    private byte[] _header;
    public static long RECORD_ID = 0x138B;
    
    /**
     * We are of type 0xF144
     */
    public long getRecordType()
    {
        return RECORD_ID;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected BinaryTagDataBlob(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}

