/*
 * 文件名称:           ExtendedParagraphHeaderAtom.java
 *  
 * 编译器:             android2.2
 * 时间:               下午5:33:25
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * get extended paragraph property
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
public final class ExtendedParagraphHeaderAtom extends RecordAtom
{ 
    //
    private static long _type = 4015;
    
    /**
     * @param source
     * @param start
     * @param len
     */
    public ExtendedParagraphHeaderAtom(byte[] source, int start, int len)
    {
        // Sanity Checking
        if(len < 8) 
        {
            len = 8;
        }
        // Get the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        if(len >= 16)
        {
            refSlideID = LittleEndian.getInt(source, start + 8);
            textType = LittleEndian.getInt(source, start + 12);
        }
    }
    
    /**
     * 
     * @return
     */
    public int getRefSlideID()
    {
        return refSlideID;
    }
    
    /**
     * 
     * @return
     */
    public int getTextType()
    {
        return textType;
    }
    
    /**
     * We are of type 4015
     */
    public long getRecordType()
    {
        return _type;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        _header = null;
    }
   
    //
    private byte[] _header;
    //
    private int refSlideID;
    //
    private int textType;
}
