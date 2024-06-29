/*
 * 文件名称:          VisualShapeAtom.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:04:36
 */
package com.nvqquy98.lib.doc.office.fc.hslf.record;

import java.util.Hashtable;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * TODO: 文件注释
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
public class VisualShapeAtom extends PositionDependentRecordAtom
{
    //the part of a slide or shape to which the animation is applied
    public static final int TVET_Shape = 0;         //Applies to the shape and all its text.
    public static final int TVET_Page = 1;          //Applies to the slide.
    public static final int TVET_TextRange = 2;     //Applies to a specified range of text of the shape.
    public static final int TVET_Audio = 3;         //Applies to the audio of the shape.
    public static final int TVET_Video = 4;         //Applies to the video of the shape.
    public static final int TVET_ChartElement = 5;  //Applies to the elements of the chart.
    public static final int TVET_ShapeOnly = 6;     //Applies to the shape but not its text.
    public static final int TVET_AllTextRange = 8;  //Applies to all text of the shape.
    
    //the element type of an animation target.
    //The animation targets a shape or some part of a shape.
    public static final int ET_ShapeType = 1;
    //The animation targets a sound file that does not correspond to a shape.
    public static final int ET_SoundType = 2;
    
    
    private byte[] _header;
    public static long RECORD_ID = 0x2AFB;
    
    //the target element type in the shape to which the animation is applied. 
    //It MUST NOT be TVET_Page.
    private int animType;
    //the target element type of the animation. It MUST be ET_ShapeType
    private int refType;
    //the target shape on the slide to animate
    private int shapeIdRef;
    
    /**
     * For VisualShapeGeneralAtom
     * data1:A signed integer that specifies the zero-based character 
     * index of the beginning of a text range.
     * It MUST be ignored unless type is TVET_TextRange
     * data2:A signed integer that specifies the zero-based character
     *  index of the end of a text range. 
     *  It MUST be ignored unless type is TVET_TextRange
     */
    /**
     * For VisualShapeChartElementAtom
     * data1 : An unsigned integer that specifies how the chart is built
     * during its animation. It MUST be a value from the following table. 
     * Value Meaning
     * 0x00000000 The entire chart.
     * 0x00000001 By series.
     * 0x00000002 By category.
     * 0x00000003 By series element.
     * 0x00000004 By category element.
     * 0x00000005 Custom chart element.
     * data2 (4 bytes): A signed integer that specifies a chart element to animate. 
     * It MUST be greater than or equal to 0xFFFFFFFF (-1).
     * The value 0xFFFFFFFF specifies that this record is invalid 
     * and SHOULD be ignored. The value 0x00000000 specifies the chart background.
     * Values greater than 0x0000000 specify a one-based index in the list of chart
     * elements specified by data1.
     */
    private int data1;
    private int data2;
    
    /**
     * We are of type 0x2AFB
     */
    public long getRecordType()
    {
        return RECORD_ID;
    }
    
    /**
     * Set things up, and find our more interesting children
     */
    protected VisualShapeAtom(byte[] source, int start, int len)
    {
        if(len < 28)
        {
            len = 28;
        }
        // Grab the header
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);
        
        animType = LittleEndian.getInt(source, start + 8);
        refType = LittleEndian.getInt(source, start + 12);
        shapeIdRef = LittleEndian.getInt(source, start + 16);
        
        data1 = LittleEndian.getInt(source, start + 20);
        data2 = LittleEndian.getInt(source, start + 24);
    }
    
    /**
     * get the target element type in the shape to which the animation is applied
     * @return
     */
    public int getTargetElementType()
    {
        return animType;
    }
    
    /**
     * get the target shape on the slide to animate
     * @return
     */
    public int getTargetElementID()
    {
        return shapeIdRef;
    }
    
    public int getData1()
    {
        return data1;        
    }
    
    public int getData2()
    {
        return data2;        
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
