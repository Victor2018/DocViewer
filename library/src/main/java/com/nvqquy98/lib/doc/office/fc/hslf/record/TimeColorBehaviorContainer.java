/*
 * 文件名称:          TimeColorBehaviorContainer.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:05:12
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

/**
 * TODO: A container record that specifies a behavior that changes the color of an object. 
 * This animation behavior is applied to the object specified 
 * by the behavior.clientVisualElement field and used to animate one property specified 
 * by the behavior.stringList field. The property MUST be one from the following list 
 * that is a subset of the properties specified in the TimeStringListContainer 
 * record: "ppt_c", "style.color", "imageData.chromakey", "fill.color", "fill.color2", 
 * "stroke.color", "stroke.color2", "shadow.color", "shadow.color2", "extrusion.color", 
 * and "fillcolor".
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
public class TimeColorBehaviorContainer extends PositionDependentRecordContainer
{
    private byte[] _header;
    public static long RECORD_ID = 0xF12C;
    
    //how to change the color of the object and which attributes within this field are valid
    private TimeColorBehaviorAtom colorBehaviorAtom;
    
    /**
     * We are of type 0xF144
     */
    public long getRecordType()
    {
        return RECORD_ID;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected TimeColorBehaviorContainer(byte[] source, int start, int len)
    {
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        colorBehaviorAtom = new TimeColorBehaviorAtom(source, start + 8, 60);
        // Find our children
        _children = Record.findChildRecords(source, start + 8, len - 8);
    }

}
