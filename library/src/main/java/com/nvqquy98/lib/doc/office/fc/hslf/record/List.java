/*
 * 文件名称:           List.java
 *  
 * 编译器:             android2.2
 * 时间:               上午10:51:23
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-7-30
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public final class List extends PositionDependentRecordContainer
{   
    /**
     * 
     * @param source
     * @param start
     * @param len
     */
    protected List(byte[] source, int start, int len)
    {
        // Get the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
        findExtendedPreRuleRecord(_children);
    }
    
    /**
     * Look for ExtendedPreRule
     */
    private void findExtendedPreRuleRecord(Record[] toSearch)
    {
        for (int i = 0; i < toSearch.length; i++)
        {
            if (toSearch[i] instanceof ExtendedPresRuleContainer)
            {
                _extendedPresRuleContainer = (ExtendedPresRuleContainer)toSearch[i];
            }
            else
            {
                // If it has children, walk them
                if (!toSearch[i].isAnAtom())
                {
                    findExtendedPreRuleRecord(toSearch[i].getChildRecords());
                }
            }
        }
    }
    
    /**
     * 
     * @return
     */
    public ExtendedPresRuleContainer getExtendedPresRuleContainer()
    {
        return _extendedPresRuleContainer;
    }

    /**
     * 
     *
     */
    public long getRecordType()
    {
        return RecordTypes.List.typeID;
    }

    /**
     * 
     *
     */
    public void writeOut(OutputStream o) throws IOException
    {
        
    }
    
    /**
     * 
     */
    public void dispose()
    {
        _header = null;
        if (_extendedPresRuleContainer != null)
        {
            _extendedPresRuleContainer.dispose();
            _extendedPresRuleContainer = null;
        }
    }

    //
    private byte[] _header;
    //
    private ExtendedPresRuleContainer _extendedPresRuleContainer;;
}
