/*
 * 文件名称:          SlideTimeAtom.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:54:11
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * TODO: An atom record that specifies the slide creation time stamp
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
public class SlideTimeAtom extends PositionDependentRecordAtom
{
    private byte[] _header;
    private static long _type = 12011;
    /**
     * the time of slide creation.
     */
    private long fileTime;
    
    /**
     * For the UserEdit Atom
     */
    protected SlideTimeAtom(byte[] source, int start, int len)
    {
        // Sanity Checking
        if(len < 16) { len = 16; }
        
        // Get the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        fileTime = LittleEndian.getLong(source, start + 8);
    }

    /**
     * 
     * @return
     */
    public long getSlideCreateTime()
    {
        return fileTime;
    }
    
    /**
     * We are of type 12011
     */
    public long getRecordType()
    { 
        return _type; 
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
