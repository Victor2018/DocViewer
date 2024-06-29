/*
 * 文件名称:          TimeMotionBehaviorContainer.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:27:32
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

/**
 * TODO: A container record that specifies a motion behavior that moves an object 
 * along a path. This animation behavior is applied to the object specified 
 * by the timeBehavior.clientVisualElement field and used to animate two 
 * properties specified by the timeBehavior.stringList field. The properties MUST 
 * be ones from the list that is specified in the TimeStringListContainer record. 
 * If no properties are specified, "ppt_x" and "ppt_y" will be used. If only one 
 * property is specified, "ppt_y" will be used as the second property.
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
public class TimeMotionBehaviorContainer extends PositionDependentRecordContainer
{
    private byte[] _header;
    public static long RECORD_ID = 0xF12E;
    
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
    protected TimeMotionBehaviorContainer(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}
