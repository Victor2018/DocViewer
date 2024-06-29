/*
 * 文件名称:          TimeConditionAtom.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:49:07
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * TODO: An atom record that specifies the information used to evaluate when a condition will be true.
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
public class TimeConditionAtom extends PositionDependentRecordAtom
{
    //the type of a target that participates in the evaluation of a time condition
    public static final int TOT_None = 0;               //None.
    public static final int TOT_VisualElement = 1;      //An animatable object.
    public static final int TOT_TimeNode = 2;           //A time node.
    public static final int TOT_RuntimeNodeRef = 3;     //Runtime child time nodes.
    
    private byte[] _header;
    private static long _type = 0xF128;
    
    //the type of target that participates in the evaluation of the condition
    private int triggerObject;
    /**
     * event that causes the condition to be TRUE.
     *  It MUST be a value from the following table.
     *  0x00000000 None.
     *  0x00000001 OnBegin event that occurs on the specified target.
     *  0x00000003 Start of the time node that is specified by id.
     *  0x00000004 End of the time node that is specified by id.
     *  0x00000005 Mouse click.
     *  0x00000007 Mouse over.
     *  0x00000009 OnNext event that occurs on the specified target.
     *  0x0000000A OnPrev event that occurs on the specified target.
     *  0x0000000B Stop audio event that occurs when an "onstopaudio" command is fired.
     */
    private int triggerEvent;
    
    /**
     * the target that participates in the evaluation of the condition
     * When triggerObject is TOT_TimeNode, this field specifies the time node identifier.
     * When triggerObject is TOT_RuntimeNodeRef, this field MUST be 0x00000002 
     * that specifies that all child time node of the ExtTimeNodeContainer record
     *  or SlaveContainer record that contains this record are the target.
     */
    private int id;
    
    //the offset time, in milliseconds, that sets when the condition will become TRUE.
    private int delay;
    
    /**
     * We are of type 1000
     */
    public long getRecordType()
    {
        return _type;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected TimeConditionAtom(byte[] source, int start, int len)
    {
        if(len < 40)
        {
            len = 40;
        }
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        triggerObject = LittleEndian.getInt(source, start + 8);
        triggerEvent = LittleEndian.getInt(source, start + 12);
        id = LittleEndian.getInt(source, start + 16);
        delay =LittleEndian.getInt(source, start + 20);
    }
    
    /**
     * At write-out time, update the references to PersistPtrs and
     *  other UserEditAtoms to point to their new positions
     */
    public void updateOtherRecordReferences(Hashtable<Integer,Integer> oldToNewReferencesLookup)
    {
       
    }
    
    /**
     * 
     */
    public void dispose()
    {
        _header = null;
    }
}
