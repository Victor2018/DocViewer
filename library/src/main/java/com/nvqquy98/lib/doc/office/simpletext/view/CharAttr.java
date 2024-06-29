/*
 * 文件名称:          CharAttr.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:38:58
 */
package com.nvqquy98.lib.doc.office.simpletext.view;

/**
 * 字符属性
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-15
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class CharAttr
{
    // 字号
    public int fontSize;
    // 字体
    public int fontIndex;
    // font scale(of percent)
    public int fontScale;
    //
    public int fontColor;
    // 粗体
    public boolean isBold;
    // 斜体
    public boolean isItalic;
    // 下划线类型
    public int underlineType;
    // 下划线颜色 
    public int  underlineColor;
    // 是否有删除线
    public boolean isStrikeThrough;
    // 是否有双删除线
    public boolean isDoubleStrikeThrough;
    /*
     * 上下标类型 = 0 正常文本， =1 上标，=2 下标 
     */
    public short subSuperScriptType;
    // 高亮颜色
    public int highlightedColor;
    //
    public byte encloseType;
    //field code type
    public byte pageNumberType;
    
    /**
     * 
     */
    public void reset()
    {
        underlineType = 0;
        fontColor = -1;
        underlineColor = -1;
        isStrikeThrough = false;
        isDoubleStrikeThrough = false;
        subSuperScriptType = 0;
        fontIndex = -1;
        encloseType = -1;
        pageNumberType = -1;
    }
}
