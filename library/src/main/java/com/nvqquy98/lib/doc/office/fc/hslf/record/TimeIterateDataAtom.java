/*
 * 文件名称:          TimeIterateDataAtom.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:42:31
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * TODO: An atom record that specifies how an animation is applied to sub-elements 
 * of the target object for a repeated effect. It can be applied to the letters, 
 * words, or shapes within a target object
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
public class TimeIterateDataAtom extends PositionDependentRecordAtom
{
    private byte[] _header;
    private static long _type = 0xF140;
    
    /**
     * the interval time of iterations, which can be either absolute time 
     * or a percentage as specified in iterateIntervalType. It MUST be ignored 
     * if fIterateIntervalPropertyUsed is FALSE and a value of 0x00000000 
     * MUST be used instead
     */
    private int iterateInterval;
    
    /**
     * the type of iteration behavior. It MUST be ignored if fIterateTypePropertyUsed 
     * is FALSE and a value of 0x00000000 MUST be used instead. It MUST be a value 
     * from the following table.
     * 0x00000000 All at once: all sub-elements animate together with no interval time.
     * 0x00000001 By word: sub-elements are words.
     * 0x00000002 By letter: sub-elements are letters.
     */
    private int iterateType;
    
    /**
     * the direction of the iteration behavior. It MUST be ignored if fIterateDirectionPropertyUsed 
     * is FALSE and a value of 0x00000001 MUST be used instead. It MUST be a value from the 
     * following table
     * 0x00000000 Backwards: from the last sub-element to the first sub-element
     * 0x00000001 Forwards: from the first sub-element to the last sub-element
     */
    private int iterateDirection;
    
    /**
     * the type of interval time as specified in iterateInterval. It MUST be ignored 
     * if fIterateIntervalTypePropertyUsed is FALSE and a value of 0x00000000 MUST be 
     * used instead. It MUST be a value from the following table.
     * 0x00000000 Seconds: iterateInterval is absolute time in milliseconds.
     * 0x00000001 Percentage: iterateInterval is a percentage of animation duration, in tenths of a percent.
     */
    private int iterateIntervalType;
    
    private boolean fIterateDirectionPropertyUsed;
    private boolean fIterateTypePropertyUsed;
    private boolean fIterateIntervalPropertyUsed;
    private boolean fIterateIntervalTypePropertyUsed;
    
    private byte[] reserved;
    
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
    protected TimeIterateDataAtom(byte[] source, int start, int len)
    {
        if(len < 28)
        {
            len = 28;
        }
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        iterateInterval = LittleEndian.getInt(source, start + 8);
        iterateType = LittleEndian.getInt(source, start + 12);
        iterateDirection = LittleEndian.getInt(source, start + 16);
        iterateIntervalType = LittleEndian.getInt(source, start + 20);
        
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
