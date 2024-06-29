/*
 * 文件名称:          ColumnInfo.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:17:41
 */
package com.nvqquy98.lib.doc.office.ss.model.sheetProperty;

/**
 * TODO: columns information
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-5-15
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ColumnInfo
{
    public ColumnInfo(int _firstCol, int _lastCol, float _colWidth, int style, boolean hidden)
    {
        this._firstCol = _firstCol;
        this._lastCol = _lastCol;
        this._colWidth = _colWidth;
        this.style = style;
        this.hidden = hidden;
    }
    
    /**
     * @return Returns the _firstCol.
     */
    public int getFirstCol()
    {
        return _firstCol;
    }
    /**
     * @param _firstCol The _firstCol to set.
     */
    public void setFirstCol(int _firstCol)
    {
        this._firstCol = _firstCol;
    }
    /**
     * @return Returns the _lastCol.
     */
    public int getLastCol()
    {
        return _lastCol;
    }
    /**
     * @param _lastCol The _lastCol to set.
     */
    public void setLastCol(int _lastCol)
    {
        this._lastCol = _lastCol;
    }
    /**
     * @return Returns the _colWidth.
     */
    public float getColWidth()
    {
        return _colWidth;
    }
    /**
     * @param _colWidth The _colWidth to set.
     */
    public void setColWidth(float _colWidth)
    {
        this._colWidth = _colWidth;
    }
    /**
     * @return Returns the hidden.
     */
    public boolean isHidden()
    {
        return hidden;
    }
    /**
     * @param hidden The hidden to set.
     */
    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }
    
    
    /**
     * @return Returns the style.
     */
    public int getStyle()
    {
        return style;
    }

    /**
     * @param style The style to set.
     */
    public void setStyle(int style)
    {
        this.style = style;
    }


    private int _firstCol;
    private int _lastCol;
    private float _colWidth;
    private boolean hidden;
    private int style;
}
