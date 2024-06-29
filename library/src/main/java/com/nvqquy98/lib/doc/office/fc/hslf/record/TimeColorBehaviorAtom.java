/*
 * 文件名称:          TimeColorBehaviorAtom.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:10:06
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

/**
 * TODO: An atom record that specifies the information for an animation that changes the color of an object
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
public class TimeColorBehaviorAtom extends PositionDependentRecordAtom
{
    private byte[] _header;
    private static long _type = 0xF135;
    
    //A TimeColorBehaviorPropertyUsedFlag structure that specifies which fields are valid.
    private int flag;
    /**
     * We are of type 0x2AFB
     */
    public long getRecordType()
    {
        return _type;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected TimeColorBehaviorAtom(byte[] source, int start, int len)
    {
        if(len < 60)
        {
            len = 60;
        }
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
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
