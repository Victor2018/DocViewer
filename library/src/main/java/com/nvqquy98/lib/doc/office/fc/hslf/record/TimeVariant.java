/*
 * 文件名称:          TimeVariant.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:40:39
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;
import com.nvqquy98.lib.doc.office.fc.util.StringUtil;

/**
 * TODO: A variable type record that specifies an attribute of a time node 
 * and whose type and meaning are specified by the value of the rh.recInstance field
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
public class TimeVariant extends PositionDependentRecordAtom
{
    //Display type in UI.
    public static final byte TPID_Display = 2;
    //Relationship to the master time node
    public static final byte TPID_MasterPos = 5;
    //Type of the subordinate time node.
    public static final byte TPID_SlaveType = 6;
    //Identifier of an animation effect.
    public static final byte TPID__EffectID = 9;
    //Direction of an animation effect.
    public static final byte TPID_EffectDir = 10;
    //Type of an animation effect.
    public static final byte TPID_EffectType = 0x0B;
    //Whether the time node is an after effect.
    public static final byte TPID_AfterEffect = 0x0D;
    //The number of slides that a media will play across.
    public static final byte TPID_SlideCount = 0x0F;
    //Time filtering for the time node.
    public static final byte TPID__TimeFilter = 0x10;
    //Event filtering for the time node..
    public static final byte TPID__EventFilter = 0x11;
    //Whether to display the media when it is stopped..
    public static final byte TPID_HideWhenStopped = 0x12;
    //Build identifier.
    public static final byte TPID__GroupID = 0x13;
    //The role of the time node in the timing structure.
    public static final byte TPID__EffectNodeType = 0x14;
    //Whether the time node is a placeholder
    public static final byte TPID_PlaceholderNode = 0x15;
    //The volume of a media.
    public static final byte TPID__MediaVolume = 0x16;
    //Whether a media object is mute.
    public static final byte TPID_MediaMute = 0x17;
    //Whether to zoom a media object to full screen.
    public static final byte TPID__ZoomToFullScreen = 0x1A;
    
    ///////////////////////////////////////////////////////////////TimeEffectType
    public static final byte TimeEffectType__Entrance = 1;
    public static final byte TimeEffectType__Exit = 2;
    public static final byte TimeEffectType__Emphasis = 3;
    public static final byte TimeEffectType__MotionPath = 4;
    public static final byte TimeEffectType__ActionVerb = 5;
    public static final byte TimeEffectType__MediaCommand = 6;
    
    
    private static final byte TVT_Bool = 0;
    private static final byte TVT_Int = 1;
    private static final byte TVT_TVT_Float = 2;
    private static final byte TVT_String = 3;
    
    private byte[] _header;
    private static long _type = 0xF142;    

    //the type of attributes for a time node.
    private int tpID;
    
    private Object value;
    
    /**
     * We are of type 0xF142
     */
    public long getRecordType()
    {
        return _type;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    public TimeVariant(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);        
        tpID = (LittleEndian.getShort(_header, 0) & 0xFFF0) >> 4;
        
        int t = source[start + 8];
        switch(t)
        {
            case TVT_Bool:
                value = (source[start + 9] == 1);
                break;
            case TVT_Int:
                value = LittleEndian.getInt(source, start + 9);
                break;
            case TVT_TVT_Float:
                value = LittleEndian.getFloat(source, start + 9);
                break;
            case TVT_String:
                int strLen = LittleEndian.getInt(_header, 4) - 1;
                byte[] textBytes = new byte[strLen];
                System.arraycopy(source, start + 9, textBytes, 0, strLen);
                value = StringUtil.getFromUnicodeLE(textBytes);
                break;
        }
    }
    
    /**
     * get type of attributes for a time node.
     * @return
     */
    public int getAttributeType()
    {
        return tpID;
    }
    
    public Object getValue()
    {
        return value;
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
