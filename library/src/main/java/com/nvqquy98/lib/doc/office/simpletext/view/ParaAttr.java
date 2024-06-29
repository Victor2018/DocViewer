/*
 * 文件名称:          ParaAttr.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:21:35
 */
package com.nvqquy98.lib.doc.office.simpletext.view;

/**
 * 段落属性
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-20
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ParaAttr
{
    // 左缩进
    public int leftIndent;
    // 右缩进
    public int rightIndent;
    // 特殊缩进格式， =0 首行缩进， = 1 悬挂缩进
    //public byte specialIndentType;
    // 特殊缩进值
    public int specialIndentValue;
    // 行距type
    public byte lineSpaceType;
    // 行距值 
    public float lineSpaceValue;
    // 段前间距
    public int beforeSpace;
    // 段后问题
    public int afterSpace;
    // 重直对齐方式
    public byte verticalAlignment;
    // 水平对齐方式
    public byte horizontalAlignment;
    // list ID
    public int listID;
    // list level
    public byte listLevel;
    // list 文本缩进
    public int listTextIndent;
    // 
    public int listAlignIndent;
    // pg bullet text ID
    public int pgBulletID;
    //
    public int tabClearPosition;
    /**
     * 
     */
    public void reset()
    {
        leftIndent = 0;
        rightIndent = 0;
        //specialIndentType = -1;
        specialIndentValue = 0;
        lineSpaceType = -1;
        lineSpaceValue = 0;
        beforeSpace = 0;
        afterSpace = 0;
        verticalAlignment = 0;
        horizontalAlignment = 0;
        listID = -1;
        listLevel = -1;
        listTextIndent = 0;
        listAlignIndent = 0;
        pgBulletID = -1;
        tabClearPosition = 0;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }
    
}
