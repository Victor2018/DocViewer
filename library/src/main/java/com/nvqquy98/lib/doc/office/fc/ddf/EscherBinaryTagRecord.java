/*
 * 文件名称:           EscherBinaryTagRecord.java
 *  
 * 编译器:             android2.2
 * 时间:               下午3:55:44
 */
package com.nvqquy98.lib.doc.office.fc.ddf;

/**
 * holds data of extended pagraph style(bullets and number ruler)
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-7-18
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class EscherBinaryTagRecord extends EscherTextboxRecord
{
    //
    public static final short RECORD_ID = (short)0x138B;
    
    //
    public EscherBinaryTagRecord()
    {
        
    }

    /**
     *
     */
    public String getRecordName()
    {
        return "BinaryTagData";
    }
}
