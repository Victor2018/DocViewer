/*
 * 文件名称:          TimeNodeAtom.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:18:30
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * TODO: An atom record that specifies the attributes of a time node.
 *  Let the corresponding time node be specified by the TimeNodeContainer 
 *  record or the SlaveContainer record that contains this TimeNodeAtom record.
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
public class TimeNodeAtom extends PositionDependentRecordAtom
{
    /**
     * time node type
     */
    //Parallel time node whose child nodes can start simultaneously
    public static final int TNT_Parallel = 0;
    /**
     * Sequential time node whose child nodes can only start sequentially
     *  and each child can only start after its previous sibling has started
     */
    public static final int TNT_Sequential = 1;
    //Behavior time node that contains a behavior
    public static final int TNT_Behavior = 2;
    //Media time node that contains a media object
    public static final int TNT__Media = 3;    
    
    
    private byte[] _header;
    private static long _type = 0xF127;
    
    private int reserved1;
    /**
     * how the corresponding time node restarts when it completes its action.
     *  It MUST be ignored if fRestartProperty is FALSE 
     *  and a value of 0x00000000 MUST be used instead.It MUST be a value from the following table:
     * 0x00000000 Does not restart.
     * 0x00000001 Can restart at any time.
     * 0x00000002 Can restart when the corresponding time node is not active.
     * 0x00000003 Same as 0x00000000.
     */
    private int restart;
    
    /**
     * the type of the corresponding time node. 
     * It MUST be ignored if fGroupingTypeProperty is FALSE 
     * and a value of TL_TNT_Parallel MUST be used instead.
     */
    private int timeNodeType;
    
    /**
     * the state of the target object's properties when the animation ends. 
     * It MUST be ignored if fFillProperty is FALSE 
     * and a value of 0x00000000 MUST be used instead.It MUST be a value from the following table:
     * 0x00000000 The properties remain at their ending values while the parent time node is still running or holding. After which, the properties reset to their original values.
     * 0x00000001 The properties reset to their original values after the time node becomes inactive.
     * 0x00000002  The properties remain at their ending values while the parent time node is still running or holding, or until another sibling time node is started under a sequential time node as specified in the type field. After which, the properties reset to their original values.
     * 0x00000003  Same as 0x00000000.
     * 0x00000004  Same as 0x00000001.
     */
    private int fill;
    
    private int reserved2;
    private byte reserved3;
    private int unused;
    
    //the duration of the corresponding time node in milliseconds.
    //It MUST be ignored if fDurationProperty is FALSE 
    //and a value of 0x00000000 MUST be used instead.
    private int duration;
    
    //whether fill was explicitly set by a user interface action
    private boolean fFillProperty;
    //whether restart was explicitly set by a user interface action
    private boolean fRestartProperty;
    //
    private boolean reserved4;
    //whether type was explicitly set by a user interface action
    private boolean fGroupingTypeProperty;
    //whether duration was explicitly set by a user interface action
    private boolean fDurationProperty;
    
    private byte[] reserved5;
    
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
    protected TimeNodeAtom(byte[] source, int start, int len)
    {
        if(len < 40)
        {
            len = 40;
        }
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        reserved1 = LittleEndian.getInt(source, start + 8);
        restart = LittleEndian.getInt(source, start + 12);
        
        timeNodeType = LittleEndian.getInt(source, start + 16);
        fill = LittleEndian.getInt(source, start + 20);
        
        duration = LittleEndian.getInt(source, start + 32);        
       
        byte b = source[start + 36];
        fDurationProperty = ((b & 0x10)) >> 4 > 0;
        fGroupingTypeProperty = ((b & 0x8) >> 3) > 0;

        fRestartProperty = ((b & 0x2) >> 1) > 0;
        fFillProperty = ((b & 0x1)) > 0;
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
