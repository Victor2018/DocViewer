/*
 * 文件名称:          TimeSequenceDataAtom.java
 *  
 * 编译器:            android2.2
 * 时间:              下午8:00:26
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * TODO: sequencing information for the child nodes of a time node. 
 * Each child can only be activated after its prior sibling has been activated.
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
public class TimeSequenceDataAtom extends PositionDependentRecordAtom
{
    private byte[] _header;
    private static long _type = 0xF141;

    /**
     * the concurrency behavior of the child nodes of the corresponding time node. 
     * It MUST be ignored if fConcurrencyPropertyUsed is FALSE 
     * and a value of 0x00000000 MUST be used instead. 
     * It MUST be a value from the following table.
     * 0x00000000 No concurrency: the next child is activated only after the current 
     *            child ends and conditions in the corresponding next time condition
     *            list are met.
     * 0x00000001 Concurrency enabled: the next child can be activated after the current 
     *            child is activated and conditions in the corresponding next time 
     *            condition list are met.
     */
    private int concurrency;
    
    /**
     * actions when traversing forward in the sequence of child nodes of the corresponding 
     * time node. It MUST be ignored if fNextActionPropertyUsed is FALSE and a value 
     * of 0x00000000 MUST be used instead. It MUST be a value from the following table.
     * 0x00000000 Take no action.
     * 0x00000001 Traverse forward the current child node along the timeline to a natural 
     *            end time.The natural end time of a child node is the time when the child 
     *            node will end without interventions. If the end time is infinite, 
     *            the child node will never stop. The natural end time of the child node 
     *            is specified as the latest non-infinite end time of its child nodes.
     */
    private int nextAction;
    
    /**
     * actions when traversing backward in the sequence of child nodes of the corresponding 
     * time node. It MUST be ignored if fPreviousActionPropertyUsed is FALSE and a value 
     * of 0x0000000 MUST be used instead. It MUST be a value from the following table.
     * 0x00000000 Take no action.
     * 0x00000001 Continue backwards in the sequence until reaching a child that starts 
     *            only on the next time condition as specified in the corresponding next 
     *            time condition list.
     */
    private int previousAction;
    
    private int reserved1;
    
    private boolean fConcurrencyPropertyUsed;
    private boolean fNextActionPropertyUsed;
    private boolean fPreviousActionPropertyUsed;
    
    private byte[] reserved2;
    
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
    protected TimeSequenceDataAtom(byte[] source, int start, int len)
    {
        if(len < 28)
        {
            len = 28;
        }
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        concurrency = LittleEndian.getInt(source, start + 8);
        nextAction = LittleEndian.getInt(source, start + 12);
        previousAction = LittleEndian.getInt(source, start + 16);
        reserved1 = LittleEndian.getInt(source, start + 20);
        
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
