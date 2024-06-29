/*
 * 文件名称:          TimeNodeContainer.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:11:19
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

/**
 * TODO: A container record that specifies a time node. This time node is
 *  used to store all information necessary to cause a time-based or an
 *  action-based effect to occur during a slide show. Each time node 
 *  effect has a corresponding object to which the effect applies.
 *  At most one of the following fields MUST exist: timeAnimateBehavior,
 *  timeColorBehavior, timeEffectBehavior, timeMotionBehavior, 
 *  timeRotationBehavior, timeScaleBehavior, timeSetBehavior, or timeCommandBehavior.
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
public class TimeNodeContainer extends PositionDependentRecordContainer
{
    private byte[] _header;
    public static long RECORD_ID = 0xF144;
    
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
    protected TimeNodeContainer(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}
