/*
 * 文件名称:          WPConstant.java
 *
 * 编译器:            android2.2
 * 时间:              下午3:15:30
 */
package com.nvqquy98.lib.doc.office.constant.wp;

/**
 * Word用到常量
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            梁金晶
 * <p>
 * 日期:            2011-10-27
 * <p>
 * 负责人:          梁金晶
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public final class WPViewConstant {
    // 布局断行标记，没有断行
    public static final byte BREAK_NO = 0;
    // 受限断行
    public static final byte BREAK_LIMIT = 1;
    // 换行符断行
    public static final byte BREAK_ENTER = 2;
    // 分节符
    public static final byte BREAK_PAGE = 3;

    // 页与页的间距
    public static final short PAGE_SPACE = 2;

    /* ============ 布局标记位 ============= */
    // 孤行控制，控制View是否必须有一个子View
    public static final byte LAYOUT_FLAG_KEEPONE = 0;
    // 最后不完全可见的一行是否删除
    public static final byte LAYOUT_FLAG_DELLELINEVIEW = LAYOUT_FLAG_KEEPONE + 1; // 1
    // 布局表格中段落
    public static final byte LAYOUT_PARA_IN_TABLE = LAYOUT_FLAG_DELLELINEVIEW + 1; // 2
    // 是布局水平对式
    public static final byte LAYOUT_NOT_WRAP_LINE = LAYOUT_PARA_IN_TABLE + 1; // 3 

    /* ============ 定义视图类型 ============ */
    // page root
    public static final short PAGE_ROOT = 0; // 0
    // normal root
    public static final short NORMAL_ROOT = PAGE_ROOT + 1; // 1
    //
    public static final short PRINT_ROOT = NORMAL_ROOT + 1;// 2
    // simple root
    public static final short SIMPLE_ROOT = PRINT_ROOT + 1; // 3
    // page view
    public static final short PAGE_VIEW = SIMPLE_ROOT + 1;// 4
    // paragraph  view
    public static final short PARAGRAPH_VIEW = PAGE_VIEW + 1;// 5
    // line view
    public static final short LINE_VIEW = PARAGRAPH_VIEW + 1; // 6
    // leaf view
    public static final short LEAF_VIEW = LINE_VIEW + 1; // 7
    // objv iew
    public static final short OBJ_VIEW = LEAF_VIEW + 1; // 8
    // table view
    public static final short TABLE_VIEW = OBJ_VIEW + 1; // 9
    // table row view
    public static final short TABLE_ROW_VIEW = TABLE_VIEW + 1; // 10
    // table cell view
    public static final short TABLE_CELL_VIEW = TABLE_ROW_VIEW + 1; // 11
    // title view (for header and footer)
    public static final short TITLE_VIEW = TABLE_CELL_VIEW + 1; // 12
    // bullet and number view
    public static final short BN_VIEW = TITLE_VIEW + 1; // 13
    // shape view
    public static final short SHAPE_VIEW = TITLE_VIEW + 1; // 14
    // enclose character view
    public static final short ENCLOSE_CHARACTER_VIEW = SHAPE_VIEW + 1; //15

    // 视图坐标X方向
    public static final byte X_AXIS = 0;
    // 视图坐标Y方向
    public static final byte Y_AXIS = X_AXIS + 1;
}
