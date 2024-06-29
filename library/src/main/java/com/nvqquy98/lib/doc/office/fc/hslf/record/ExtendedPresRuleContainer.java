/*
 * 文件名称:           ExtendedPresRuleContainer.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:10:49
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

/**
 * be made up of ExtendedParagraphHeaderAtom and ExtendedParagraphAtom
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
public class ExtendedPresRuleContainer extends PositionDependentRecordContainer
{
    //
    private static long _type = 4014;
    
    /**
     * Create a new holder for a boring record with children, but with
     *  position dependent characteristics
     */
    protected ExtendedPresRuleContainer(byte[] source, int start, int len)
    {
        // Just grab the header, not the whole contents
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
        Vector<ExtendedParaAtomsSet> sets = new Vector<ExtendedParaAtomsSet>();
        for (int i = 0; i < _children.length; i++)
        {
            if (_children[i] instanceof ExtendedParagraphAtom)
            {
                for (int j = i - 1; j >= 0; j--)
                {
                    if (_children[j] instanceof ExtendedParagraphHeaderAtom)
                    {
                        ExtendedParaAtomsSet set = new ExtendedParaAtomsSet((ExtendedParagraphHeaderAtom)_children[j], 
                            (ExtendedParagraphAtom)_children[i]);
                        sets.add(set);
                        break;
                    }
                }
            }
        }
        // Turn the vector into an array
        _extendedAtomsSets = sets.toArray(new ExtendedParaAtomsSet[sets.size()]);
    }
    
    /**
     * 
     * @return
     */
    public ExtendedParaAtomsSet[] getExtendedParaAtomsSets()
    {
        return _extendedAtomsSets;
    }

    /**
     * 
     *
     */
    public long getRecordType()
    {
        return _type;
    }

    /**
     * 
     *
     */
    public void writeOut(OutputStream o) throws IOException
    {
        
    }
    
    /**
     * Inner class to wrap up a matching set of records that hold the
     *  text for a given sheet. This includes sets of ExtendedParagraphHeaderAtom
     *  and ExtendedParagraphAtom.
     */
    public class ExtendedParaAtomsSet
    {
        /**
         * 
         * @param extendedParaHeaderAtom
         * @param extendedParaAtom
         */
        public ExtendedParaAtomsSet(ExtendedParagraphHeaderAtom extendedParaHeaderAtom, 
            ExtendedParagraphAtom extendedParaAtom)
        {
            _extendedParaHeaderAtom = extendedParaHeaderAtom;
            _extendedParaAtom = extendedParaAtom;
        }
        /**
         * 
         * @return
         */
        public ExtendedParagraphHeaderAtom getExtendedParaHeaderAtom()
        {
            return _extendedParaHeaderAtom;
        }
        
        /**
         * 
         * @return
         */
        public ExtendedParagraphAtom getExtendedParaAtom()
        {
            return _extendedParaAtom;
        }
        
        /**
         * 
         */
        public void dispose()
        {
            if (_extendedParaHeaderAtom != null)
            {
                _extendedParaHeaderAtom.dispose();
                _extendedParaHeaderAtom = null;
            }
            if (_extendedParaAtom != null)
            {
                _extendedParaAtom.dispose();
                _extendedParaAtom = null;
            }
        }
        
        //
        private ExtendedParagraphHeaderAtom _extendedParaHeaderAtom;
        //
        private ExtendedParagraphAtom _extendedParaAtom;
    }

    /**
     *
     */
    public void dispose()
    {
        _header = null;
        if (_extendedAtomsSets != null)
        {
            for (ExtendedParaAtomsSet eps : _extendedAtomsSets)
            {
                eps.dispose();
            }
            _extendedAtomsSets = null;
        }
    }
    
    //
    private byte[] _header;
    //
    private ExtendedParaAtomsSet[] _extendedAtomsSets;
}
