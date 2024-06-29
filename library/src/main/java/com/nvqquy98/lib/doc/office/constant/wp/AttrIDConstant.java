/*
 * 文件名称:          AttrIDConstant.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:10:41
 */
package com.nvqquy98.lib.doc.office.constant.wp;

/**
 * 定义属性ID值 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-31
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AttrIDConstant
{
    /* ========== 字符属性 =========== */
    // 字符样式
    public static final short FONT_STYLE_ID = 0x0000;
    // 字号
    public static final short FONT_SIZE_ID = FONT_STYLE_ID + 1; // 00001
    // 字体
    public static final short FONT_NAME_ID = FONT_SIZE_ID + 1; //0x0002;
    // 字符颜色
    public static final short FONT_COLOR_ID = FONT_NAME_ID + 1; // 0x0003;    
    // 粗体
    public static final short FONT_BOLD_ID = FONT_COLOR_ID + 1; //0x0004;
    // 斜体
    public static final short FONT_ITALIC_ID = FONT_BOLD_ID + 1; //0x0005;
    // 删除线
    public static final short FONT_STRIKE_ID = FONT_ITALIC_ID + 1; //0x0006;
    // 双删除线
    public static final short FONT_DOUBLESTRIKE_ID = FONT_STRIKE_ID + 1;//0x0007;
    // 下划线
    public static final short FONT_UNDERLINE_ID = FONT_DOUBLESTRIKE_ID + 1; //0x0008;
    // 下划线颜色
    public static final short FONT_UNDERLINE_COLOR_ID = FONT_UNDERLINE_ID + 1; // 0x0009 
    // 上下标
    public static final short FONT_SCRIPT_ID = FONT_UNDERLINE_COLOR_ID + 1 ;//0x000A;
    // 高亮
    public static final short FONT_HIGHLIGHT_ID = FONT_SCRIPT_ID + 1; //0x000B;
    // hyperlink
    public static final short FONT_HYPERLINK_ID = FONT_HIGHLIGHT_ID + 1; // 0x000C
    // shape index
    public static final short FONT_SHAPE_ID = FONT_HYPERLINK_ID + 1; // 0x000D
    // font scale
    public static final short FONT_SCALE_ID = FONT_SHAPE_ID + 1; // 0x000E
    // page number type
    public static final short FONT_PAGE_NUMBER_TYPE_ID = FONT_SCALE_ID + 1; // 0x000F  
    // enclose character
    public static final short FONT_ENCLOSE_CHARACTER_TYPE_ID = FONT_PAGE_NUMBER_TYPE_ID + 1; // 0x0010;
    
    /* ========== 段落属性 =========== */
    // 段落样式
    public static final short PARA_STYLE_ID = 0x1000;
    // 段落左缩进
    public static final short PARA_INDENT_LEFT_ID = PARA_STYLE_ID + 1; //0x1001;
    public static final short PARA_INDENT_INITLEFT_ID = PARA_INDENT_LEFT_ID + 1; //0x1001;
    // 段落右缩进
    public static final short PARA_INDENT_RIGHT_ID = PARA_INDENT_INITLEFT_ID + 1; //0x1003;
    // 段前间距
    public static final short PARA_BEFORE_ID = PARA_INDENT_RIGHT_ID + 1; //0x1004;
    // 段后间距
    public static final short PARA_AFTER_ID = PARA_BEFORE_ID + 1; //0x1005;
    // 水平对齐
    public static final short PARA_HORIZONTAL_ID = PARA_AFTER_ID + 1; //0x1006;
    // 垂直对齐
    public static final short PARA_VERTICAL_ID = PARA_HORIZONTAL_ID + 1; // 0x1007;
    // 特殊缩进
    public static final short PARA_SPECIALINDENT_ID = PARA_VERTICAL_ID + 1; //0x1008;
    // 行距
    public static final short PARA_LINESPACE_ID = PARA_SPECIALINDENT_ID + 1 ; //0x1009;
    // 行距类型
    public static final short PARA_LINESPACE_TYPE_ID = PARA_LINESPACE_ID + 1; //0x100A;
    // 段落级别，用于表格
    public static final short PARA_LEVEL_ID = PARA_LINESPACE_TYPE_ID + 1 ;//0x100B
    // list level
    public static final short PARA_LIST_LEVEL_ID = + PARA_LEVEL_ID + 1; // 0x100C
    // list ID
    public static final short PARA_LIST_ID = PARA_LIST_LEVEL_ID + 1; // 0x100D
    // pg bullet text ID
    public static final short PARA_PG_BULLET_ID = PARA_LIST_ID + 1; // 0x100E
    // 
    public static final short PARA_TABS_CLEAR_POSITION_ID = PARA_PG_BULLET_ID + 1; //0x100F
    
    /* ========= 章节属性 ========= */
    // 页面宽度
    public static final short PAGE_WIDTH_ID = 0x2000;
    // 页面高度
    public static final short PAGE_HEIGHT_ID = PAGE_WIDTH_ID + 1; //0x2001;
    // 页面左边距
    public static final short PAGE_LEFT_ID = PAGE_HEIGHT_ID + 1; //0x2002;
    // 页面右边距
    public static final short PAGE_RIGHT_ID = PAGE_LEFT_ID + 1; //0x2003;
    // 页面上边距
    public static final short PAGE_TOP_ID = PAGE_RIGHT_ID + 1; //0x2004;
    // 页面下边距
    public static final short PAGE_BOTTOM_ID = PAGE_TOP_ID + 1; //0x2005;
    // 垂直对齐
    public static final short PAGE_VERTICAL_ID = PAGE_BOTTOM_ID + 1; // 0x2006;
    // 页眉到上边界的距离
    public static final short PAGE_HEADER_ID = PAGE_VERTICAL_ID + 1 ;// 0x2007
    // 页脚到下边界的距离
    public static final short PAGE_FOOTER_ID = PAGE_HEADER_ID + 1; // 0x2008 
    // 页面水平对齐
    public static final short PAGE_HORIZONTAL_ID = PAGE_FOOTER_ID + 1; // 0x2009; 
    // page background color
    public static final short PAGE_BACKGROUND_COLOR_ID = PAGE_HORIZONTAL_ID+ 1; // 0x200A
    // page border
    public static final short PAGE_BORDER_ID = PAGE_BACKGROUND_COLOR_ID + 1; // 0x200B
    //line pitch
    public static final short PAGE_LINEPITCH_ID = PAGE_BORDER_ID + 1; // 0x200C
    
    /* ======== 表格属性 ======== */
    // 上边框
    public static final short TABLE_TOP_BORDER_ID = 0x3000; 
    // 上边框颜色
    public static final short TABLE_TOP_BORDER_COLOR_ID = TABLE_TOP_BORDER_ID + 1; // 0x3001
    // 下边框
    public static final short TABLE_BOTTOM_BORDER_ID = TABLE_TOP_BORDER_COLOR_ID + 1; // 0x3002 
    // 下边框颜色
    public static final short TABLE_BOTTOM_BORDER_COLOR_ID = TABLE_BOTTOM_BORDER_ID + 1; // 0x3003
    // 左边框
    public static final short TABLE_LEFT_BORDER_ID = TABLE_BOTTOM_BORDER_COLOR_ID + 1; // 0x3004
    // 左边框颜色
    public static final short TABLE_LEFT_BORDER_COLOR_ID = TABLE_LEFT_BORDER_ID + 1; // 0x3005
    // 左边框
    public static final short TABLE_RIGHT_BORDER_ID = TABLE_LEFT_BORDER_COLOR_ID + 1; // 0x3006
    // 左边框颜色
    public static final short TABLE_RIGHT_BORDER_COLOR_ID = TABLE_RIGHT_BORDER_ID + 1; // 0x3007
    // 行高
    public static final short TABLE_ROW_HEIGHT_ID = TABLE_RIGHT_BORDER_COLOR_ID + 1; // 0x3008
    // 列宽
    public static final short TABLE_CELL_WIDTH_ID = TABLE_ROW_HEIGHT_ID + 1; // 0x3009 
    // 行标题
    public static final short TABLE_ROW_HEADER_ID = TABLE_CELL_WIDTH_ID + 1; // 0x300A
    // 行跨页
    public static final short TABLE_ROW_SPLIT_ID = TABLE_ROW_HEADER_ID + 1; // 0x300B
    // 第一个水平合并单元格
    public static final short TABLE_CELL_HOR_FIRST_MERGED_ID = TABLE_ROW_SPLIT_ID + 1; // 0x300C
    // 水平合并单元格
    public static final short TABLE_CELL_HORIZONTAL_MERGED_ID = TABLE_CELL_HOR_FIRST_MERGED_ID + 1; // 0x300D
    // 第一个垂直合并单元格
    public static final short TABLE_CELL_VER_FIRST_MERGED_ID = TABLE_CELL_HORIZONTAL_MERGED_ID + 1; // 0x300E
    // 垂直合并单元格
    public static final short TABLE_CELL_VERTICAL_MERGED_ID = TABLE_CELL_VER_FIRST_MERGED_ID + 1; // 0x300F
    // 垂直对齐方式
    public static final short TABLE_CELL_VERTICAL_ALIGN_ID = TABLE_CELL_VERTICAL_MERGED_ID + 1; // 0x3010
    // 上边距
    public static final short TABLE_TOP_MARGIN_ID = TABLE_CELL_VERTICAL_ALIGN_ID + 1; // 0x3011
    // 下边距
    public static final short TABLE_BOTTOM_MARGIN_ID = TABLE_TOP_MARGIN_ID + 1; // 0x3012
    // 左边距
    public static final short TABLE_LEFT_MARGIN_ID = TABLE_BOTTOM_MARGIN_ID + 1; // 0x3013
    // 右边距
    public static final short TABLE_RIGHT_MARGIN_ID = TABLE_LEFT_MARGIN_ID + 1; // 03014
}
