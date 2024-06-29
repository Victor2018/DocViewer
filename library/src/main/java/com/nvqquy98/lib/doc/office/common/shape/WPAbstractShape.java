/*
 * 文件名称:          WPAbstractShape.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:08:14
 */
package com.nvqquy98.lib.doc.office.common.shape;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-5-29
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class WPAbstractShape extends ArbitraryPolygonShape
{

    // 紧密型
    public static final short WRAP_TIGHT = 0;
    // 四周型
    public static final short WRAP_SQUARE = 1;
    // 嵌入型
    public static final short WRAP_OLE = 2;
    // 浮于文字上方
    public static final short WRAP_TOP = 3;
    // 穿越型
    public static final short WRAP_THROUGH = 4;
    // 上下型
    public static final short WRAP_TOPANDBOTTOM = 5;
    // 浮于文字下方
    public static final short WRAP_BOTTOM = 6;
    
    // 相对于柆
    public static final byte RELATIVE_COLUMN = 0;
    // 相对于页边距
    public static final byte RELATIVE_MARGIN = RELATIVE_COLUMN + 1;
    // 相对于页面
    public static final byte RELATIVE_PAGE = RELATIVE_MARGIN + 1;
    // 相对于字符
    public static final byte RELATIVE_CHARACTER = RELATIVE_PAGE + 1;
    // 相对左边距
    public static final byte RELATIVE_LEFT = RELATIVE_CHARACTER + 1;
    // 相对右边距
    public static final byte RELATIVE_RIGHT = RELATIVE_LEFT + 1;
    // 相对上边距
    public static final byte RELATIVE_TOP = RELATIVE_RIGHT + 1;
    // 相对下边距
    public static final byte RELATIVE_BOTTOM = RELATIVE_TOP + 1;
    // 相对内边距
    public static final byte RELATIVE_INNER = RELATIVE_BOTTOM + 1;
    // 相对外边距
    public static final byte RELATIVE_OUTER = RELATIVE_INNER + 1;
    // 相对于段落
    public static final byte RELATIVE_PARAGRAPH = RELATIVE_OUTER + 1;
    // 相对于行
    public static final byte RELATIVE_LINE = RELATIVE_PARAGRAPH + 1;
    
    //
    public static final byte ALIGNMENT_ABSOLUTE = 0;
    // 左对齐
    public static final byte ALIGNMENT_LEFT = 1;
    // 居中
    public static final byte ALIGNMENT_CENTER = 2;
    // 右对齐
    public static final byte ALIGNMENT_RIGHT = 3;
    // 顶端对齐
    public static final byte ALIGNMENT_TOP = 4;
    // 底端
    public static final byte ALIGNMENT_BOTTOM = 5;
    // 内部
    public static final byte ALIGNMENT_INSIDE = 6;
    // 外部
    public static final byte ALIGNMENT_OUTSIDE = 7;
    
    /**
     * position type
     */
    //absolute position
    public static final byte POSITIONTYPE_ABSOLUTE = 0;
    //relative position
    public static final byte POSITIONTYPE_RELATIVE = 1;
    
    
	/**
	 * POSITION_ABSOLUTE or POSITION_RELATIVE
	 * @return
	 */
	public byte getHorPositionType() 
	{
		return horPositionType;
	}

	/**
	 * POSITION_ABSOLUTE or POSITION_RELATIVE
	 * @param horPositionType
	 */
	public void setHorPositionType(byte horPositionType) 
	{
		this.horPositionType = horPositionType;
	}

	/**
	 * POSITION_ABSOLUTE or POSITION_RELATIVE
	 * @return
	 */
	public byte getVerPositionType() 
	{
		return verPositionType;
	}

	/**
	 * POSITION_ABSOLUTE or POSITION_RELATIVE
	 * @param verPositionType
	 */
	public void setVerPositionType(byte verPositionType)
	{
		this.verPositionType = verPositionType;
	}
	
    /**
     * wrap type
     * @return
     */
    public int getWrap()
    {
        return wrapType;
    }
    
    /**
     * 
     * @param wrapType
     */
    public void setWrap(short wrapType)
    {
        this.wrapType = wrapType;
    }
    
    /**
     * @return Returns the horRelative.
     */
    public byte getHorizontalRelativeTo()
    {
        return horRelativeTo;
    }
    /**
     * @param horRelative The horRelative to set.
     */
    public void setHorizontalRelativeTo(byte horRelativeTo)
    {
        this.horRelativeTo = horRelativeTo;
    }

    /**
     * @return Returns the horizontal alignment;
     */
    public byte getHorizontalAlignment()
    {
        return horAlignment;
    }

    /**
     * @param horAlignment The horizontal alignment to set.
     */
    public void setHorizontalAlignment(byte horAlignment)
    {
        this.horAlignment = horAlignment;
    }

    /**
     * @return Returns the verRelativeTo.
     */
    public byte getVerticalRelativeTo()
    {
        return verRelativeTo;
    }

    /**
     * @param verRelative The verRelativeTo to set.
     */
    public void setVerticalRelativeTo(byte verRelativeTo)
    {
        this.verRelativeTo = verRelativeTo;
    }

    /**
     * @return Returns the vertical Alignment value.
     */
    public byte getVerticalAlignment()
    {
        return verAlignment;
    }

    /**
     * @param verAlignment The vertical Alignment type to set.
     */
    public void setVerticalAlignment(byte verAlignment)
    {
        this.verAlignment = verAlignment;
    }
    
    /**
     *  horizontal relative position value(one in a thousand)
     * @return
     */
    public int getHorRelativeValue() 
    {
		return horRelativeValue;
	}

	public void setHorRelativeValue(int horRelativeValue)
	{
		this.horRelativeValue = horRelativeValue;
	}

	/**
	 * vertical relative position value(one in a thousand)
	 * @return
	 */
	public int getVerRelativeValue() 
	{
		return verRelativeValue;
	}

	public void setVerRelativeValue(int verRelativeValue)
	{
		this.verRelativeValue = verRelativeValue;
	}
	   /**
     * @return Returns the elementIndex.
     */
    public int getElementIndex()
    {
        return elementIndex;
    }

    /**
     * @param elementIndex The elementIndex to set.
     */
    public void setElementIndex(int elementIndex)
    {
        this.elementIndex = elementIndex;
    }


    /**
     * @return Returns the isTextWrapLine.
     */
    public boolean isTextWrapLine()
    {
        return isTextWrapLine;
    }

    /**
     * @param isTextWrapLine The isTextWrapLine to set.
     */
    public void setTextWrapLine(boolean isTextWrapLine)
    {
        this.isTextWrapLine = isTextWrapLine;
    }
	
	//relative or absolute
	private byte horPositionType;
	// horizontal relative
    private byte horRelativeTo;
    //horizontal relative position(one in a thousand)
    private int horRelativeValue;
	// horizontal position
    private byte horAlignment = ALIGNMENT_ABSOLUTE;
    
    //relative or absolute
    private byte verPositionType;
    // vertical relative
    private byte verRelativeTo = RELATIVE_PARAGRAPH;
    //vertical relative position(one in a thousand)
    private int verRelativeValue;
    // vertical position type
    private byte verAlignment = ALIGNMENT_ABSOLUTE;
    
    //default is top of text;
    private short wrapType = 3;
    
    // text box element index
    private int elementIndex = -1;
    //    
    private boolean isTextWrapLine = true;
    
}
