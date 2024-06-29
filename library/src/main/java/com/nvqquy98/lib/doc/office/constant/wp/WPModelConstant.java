/*
 * 文件名称:          WPModelConstant.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:25:50
 */
package com.nvqquy98.lib.doc.office.constant.wp;

/**
 * word model定义的一些常量
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-8
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public final class WPModelConstant
{

    /* ============ 文本区域 ================ */
    // 正文 
    public static final long MAIN = 0x0000000000000000L;
    // 页眉
    public static final long HEADER = 0x1000000000000000L;
    // 页脚
    public static final long FOOTER = 0x2000000000000000L;
    // 脚注
    public static final long FOOTNOTE = 0x3000000000000000L;
    // 尾注
    public static final long ENDNOTE = 0x4000000000000000L;
    // 文本框，高32位区分不同的文本框
    public static final long TEXTBOX = 0x5000000000000000L;
    // 区域Mask
    public static final long AREA_MASK = 0xF000000000000000L;
    // 文本框mask
    public static final long TEXTBOX_MASK = 0x0FFFFFFF00000000L;
    
    /* ============ model的Element类型 ========= */
    // 章节Element
    public static final short SECTION_ELEMENT = 0; // 0
    // 段落Element
    public static final short PARAGRAPH_ELEMENT = SECTION_ELEMENT + 1; // 1
    // Leaf Element
    public static final short LEAF_ELEMENT = SECTION_ELEMENT + 1; // 2
    // table element
    public static final short TABLE_ELEMENT = LEAF_ELEMENT + 1;// 3
    // table row element
    public static final short TABLE_ROW_ELEMENT = TABLE_ELEMENT + 1; // 4
    // table cell element
    public static final short TABLE_CELL_ELEMENT = TABLE_ROW_ELEMENT + 1; // 5
    // header element
    public static final short HEADER_ELEMENT = TABLE_CELL_ELEMENT + 1; // 6
    // footer element
    public static final short FOOTER_ELEMENT = HEADER_ELEMENT + 1; // 7
    
    
    /* =========== element的Collection类型 ============ */
    // 章节集合
    public static final short SECTION_COLLECTION = 0;
    // 页眉
    public static final short HEADER_COLLECTION = SECTION_COLLECTION + 1;// 1
    // 页脚
    public static final short FOOTER_COLLECTION = HEADER_COLLECTION + 1;// 2
    // 脚注
    public static final short FOOTNOTE_COLLECTION = FOOTER_COLLECTION + 1; // 3
    // 尾注
    public static final short ENDNOTE_COLLECTION = FOOTNOTE_COLLECTION + 1; // 4
    // 文本框
    public static final short TEXTBOX_COLLECTION = ENDNOTE_COLLECTION + 1; // 5
    
    /* =========== 页眉页脚类型 ==========  */
    // 首页
    public static final byte HF_FIRST = 0; 
    // 奇数页
    public static final byte HF_ODD = HF_FIRST + 1; // 1
    // 偶数页
    public static final byte HF_EVEN = HF_ODD + 1; // 2 
    
    /* =========== 页码类型 ============ */
    // page number
    public static final byte PN_PAGE_NUMBER = 1;
    // total pages
    public static final byte PN_TOTAL_PAGES = 2;
    
    /* =========== 圈号类型 =========== */
    // 圆
    public static final byte ENCLOSURE_TYPE_ROUND = 0;
    // 正方
    public static final byte ENCLOSURE_TYPE_SQUARE = ENCLOSURE_TYPE_ROUND + 1;
    // 三角
    public static final byte ENCLOSURE_TYPE_TRIANGLE = ENCLOSURE_TYPE_SQUARE + 1;
    // 菱形
    public static final byte ENCLOSURE_TYPE_RHOMBUS = ENCLOSURE_TYPE_TRIANGLE + 1;
    
}
