/*
 * 文件名称:          TimeRotationBehaviorContainer.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:32:11
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

/**
 * TODO: A container record that specifies a rotation behavior 
 * that rotates an object. This animation behavior is applied to the object 
 * specified by the behavior.clientVisualElement field and used to animate 
 * one property specified by the behavior.stringList field. 
 * The property MUST be "r" or "ppt_r" from the list that is specified 
 * in the TimeStringListContainer record.
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
public class TimeRotationBehaviorContainer extends PositionDependentRecordContainer
{
    private byte[] _header;
    public static long RECORD_ID = 0xF12F;
    
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
    protected TimeRotationBehaviorContainer(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}
