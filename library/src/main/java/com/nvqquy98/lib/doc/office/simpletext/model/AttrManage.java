/*
 * 文件名称:          AttrKit.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:51:06
 */
package com.nvqquy98.lib.doc.office.simpletext.model;

import com.nvqquy98.lib.doc.office.common.bulletnumber.ListData;
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListLevel;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.simpletext.view.CharAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.TableAttr;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Color;

/**
 * 属性管理器
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-28
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AttrManage
{
    public static AttrManage am = new AttrManage();
    
    /**
     * 
     * @param doc
     */
    public static AttrManage instance()
    {
        return am;
    }
    
    /**
     * 指定的AttributeSet 是否有指定的attrID属性
     *  
     */
    public boolean hasAttribute(IAttributeSet attr, short attrID)
    {
        return attr.getAttribute(attrID) != Integer.MIN_VALUE;  
    }
    
    
    /* ============= 字符属性 ========== */
    /**
     * get character style id 
     */
    public int getFontStyleID(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.FONT_STYLE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * get style id 
     */
    public void setFontStyleID(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_STYLE_ID, value);    
    }
    
    
    /**
     * get fontSize
     */
    public int getFontSize(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_SIZE_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_SIZE_ID);
            if (a == Integer.MIN_VALUE)
            {
                return 12;
            }
        }
        return a;
    }
    
    /**
     * set font size
     */
    public void setFontSize(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_SIZE_ID, value);
    }
    
    /**
     * get fontSize
     */
    public int getFontName(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_NAME_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_NAME_ID);
            if (a == Integer.MIN_VALUE)
            {
                return -1;
            }
        }
        return a;
    }
    
    /**
     * set fontSize
     */
    public void setFontName(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_NAME_ID, value);
    }
    
    /**
     * get fontScale
     */
    public int getFontScale(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_SCALE_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_SCALE_ID);
            if (a == Integer.MIN_VALUE)
            {
                return 100;
            }
        }
        return a;
    }    
    /**
     * set fontScale
     */
    public void setFontScale(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_SCALE_ID, value);
    }
    
    /**
     * get FontColor
     */
    public int getFontColor(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_COLOR_ID);
            if (a == Integer.MIN_VALUE)
            {
                return Color.BLACK;
            }
        }
        return a;
    }    
    /**
     * set FontColor 
     */
    public void setFontColor(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_COLOR_ID, value);
    }    
    
    /**
     * get Bold
     */
    public boolean getFontBold(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_BOLD_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_BOLD_ID);
            if (a == Integer.MIN_VALUE)
            {
                return false;
            }
        }
        return a == 1;
    }
    /**
     * set Bold
     */
    public void setFontBold(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.FONT_BOLD_ID, b ? 1 : 0);
    }
    
    /**
     * get Italic
     */
    public boolean getFontItalic(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_ITALIC_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_ITALIC_ID);
            if (a == Integer.MIN_VALUE)
            {
                return false;
            }
        }
        return a == 1;
    }
    /**
     * set Italic 
     */
    public void setFontItalic(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.FONT_ITALIC_ID, b ? 1 : 0);
    }
    
    /**
     * get 删除线
     */
    public boolean getFontStrike(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_STRIKE_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_STRIKE_ID);
            if (a == Integer.MIN_VALUE)
            {
                return false;
            }
        }
        return a == 1;
    }
    /**
     * set 删除线
     */
    public void setFontStrike(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.FONT_STRIKE_ID, b ? 1 : 0);
    }
    
    /**
     * get 双删除线
     */
    public boolean getFontDoubleStrike(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_DOUBLESTRIKE_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_DOUBLESTRIKE_ID);
            if (a == Integer.MIN_VALUE)
            {
                return false;
            }
        }
        return a == 1;
    }    
    /**
     * set 双删除线
     */
    public void setFontDoubleStrike(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.FONT_DOUBLESTRIKE_ID, b ? 1 : 0);
    }
    
    /**
     * get 下划线
     */
    public int getFontUnderline(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_UNDERLINE_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_UNDERLINE_ID);
            if (a == Integer.MIN_VALUE)
            {
                return 0;
            }
        }
        return a;
    }
    /**
     * set 下划线
     */
    public void setFontUnderline(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_UNDERLINE_ID, value);
    }
    
    /**
     * get 下划线Color
     */
    public int getFontUnderlineColor(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_UNDERLINE_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_UNDERLINE_COLOR_ID);
            if (a == Integer.MIN_VALUE)
            {
                return getFontColor(paraAttr, leafAttr);
            }
        }
        return a;
    }
    /**
     * set 下划线Color
     */
    public void setFontUnderlineColr(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_UNDERLINE_COLOR_ID, value);
    }
    
    /**
     * get 上下标
     */
    public int getFontScript(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_SCRIPT_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_SCRIPT_ID);
            if (a == Integer.MIN_VALUE)
            {
                return 0;
            }
        }
        return a;
    }
    /**
     * set 上下标
     */
    public void setFontScript(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_SCRIPT_ID, value);
    }
    
    /**
     * get 高亮
     */
    public int getFontHighLight(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_HIGHLIGHT_ID);
        if (a == Integer.MIN_VALUE)
        {
            a = paraAttr.getAttribute(AttrIDConstant.FONT_HIGHLIGHT_ID);
            if (a == Integer.MIN_VALUE)
            {
                return -1;
            }
        }
        return a;
    }
    /**
     * set 高亮
     */
    public void setFontHighLight(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_HIGHLIGHT_ID, value);
    }
    
    /**
     * get hyperlink id 
     */
    public int getHperlinkID(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.FONT_HYPERLINK_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * set hyperlink id 
     */
    public void setHyperlinkID(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_HYPERLINK_ID, value);    
    }
    
    
    /**
     * get shape id
     */
    public int getShapeID(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.FONT_SHAPE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }    
    /**
     * 
     */
    public void setShapeID(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_SHAPE_ID, value);
    }
    
    
    /**
     * get page number type 
     */
    public int getFontPageNumberType(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.FONT_PAGE_NUMBER_TYPE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * @param attr
     * @param value  = 1, page number
     *               = 2， total pages
     */
    public void setFontPageNumberType(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_PAGE_NUMBER_TYPE_ID, value);
    }
    
    /**
     * 
     */
    /**
     * get page number type 
     */
    public int getFontEncloseChanacterType(IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        int a = leafAttr.getAttribute(AttrIDConstant.FONT_ENCLOSE_CHARACTER_TYPE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * @param attr
     * @param value  = 1, page number
     *               = 2， total pages
     */
    public void setEncloseChanacterType(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.FONT_ENCLOSE_CHARACTER_TYPE_ID, value);
    }
    
    /* ============ 段落属性 =========== */
    /**
     * get character style id 
     */
    public int getParaStyleID(IAttributeSet attr)
    {

        int a = attr.getAttribute(AttrIDConstant.PARA_STYLE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * get style id 
     */
    public void setParaStyleID(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_STYLE_ID, value);    
    }
     /**
      * get 左缩进 
      */
    public int getParaIndentLeft(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_INDENT_LEFT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set 左缩进
     */
    public void setParaIndentLeft(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_INDENT_LEFT_ID, value);
    }
    
    public int getParaIndentInitLeft(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_INDENT_INITLEFT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set 左缩进
     */
    public void setParaIndentInitLeft(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_INDENT_INITLEFT_ID, value);
    }
    
    /**
     * get 右缩进
     */
    public int getParaIndentRight(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_INDENT_RIGHT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set 右缩进
     */
    public void setParaIndentRight(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_INDENT_RIGHT_ID, value);
    }
    
    /**
     * get 段前间距
     */
    public int getParaBefore(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_BEFORE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;        
    }
    /**
     * set 段前间距 
     */
    public void setParaBefore(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_BEFORE_ID, value);
    }
    
    /**
     * get 段后间距
     */
    public int getParaAfter(IAttributeSet attr)
    {        
        int a = attr.getAttribute(AttrIDConstant.PARA_AFTER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;         
    }
    /**
     * set 段后间距
     */
    public void setParaAfter(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_AFTER_ID, value);
    }
    
    /**
     * get 特殊缩进
     */
    public int getParaSpecialIndent(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_SPECIALINDENT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;   
    }
    /**
     * set 特殊缩进
     */
    public void setParaSpecialIndent(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_SPECIALINDENT_ID, value);
    }
    
    /**
     * get 行距
     */
    public float getParaLineSpace(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_LINESPACE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1;
        }
        return a / 100.f;
    }
    /**
     * set 行距
     */
    public void setParaLineSpace(IAttributeSet attr, float value)
    {
        attr.setAttribute(AttrIDConstant.PARA_LINESPACE_ID, (int)(value * 100));
    }
    
    /**
     * get 行距类型
     */
    public int getParaLineSpaceType(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_LINESPACE_TYPE_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1;
        }
        return a;
    }    
    /**
     * set 行距类型
     */
    public void setParaLineSpaceType(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_LINESPACE_TYPE_ID, value);
    }
    
    /**
     * get 水平对齐
     */
    public int getParaHorizontalAlign(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_HORIZONTAL_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set 水平对齐
     */
    public void setParaHorizontalAlign(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_HORIZONTAL_ID, value);
    }
    
    /**
     * get 垂直对齐
     */
    public int getParaVerticalAlign(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_VERTICAL_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set 垂直对齐
     */
    public void setParaVerticalAlign(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_VERTICAL_ID, value);
    }
    
    /**
     * get paragraph level
     */
    public int getParaLevel(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_LEVEL_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * set paragraph level
     */
    public void setParaLevel(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_LEVEL_ID, value);
    }
    
    /**
     * get paragraph level
     */
    public int getParaListLevel(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_LIST_LEVEL_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * set paragraph level
     */
    public void setParaListLevel(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_LIST_LEVEL_ID, value);
    }
    
    
    /**
     * get paragraph level
     */
    public int getParaListID(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_LIST_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * set paragraph level
     */
    public void setParaListID(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_LIST_ID, value);
    }
    
    /**
     * get pg bullet text ID
     */
    public int getPGParaBulletID(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_PG_BULLET_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * set pg bullet text ID
     */
    public void setPGParaBulletID(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_PG_BULLET_ID, value);
    }
    /**
     * get paragraph tabs clear position
     */
    public int getParaTabsClearPostion(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PARA_TABS_CLEAR_POSITION_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * set paragraph tabs clear position
     */
    public void setParaTabsClearPostion(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PARA_TABS_CLEAR_POSITION_ID, value);
    }
    
    /* =========== 章节属性 =========== */
    /**
     * get页面宽度
     */
    public int getPageWidth(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_WIDTH_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1000;
        }
        return a;
    }
    /**
     * set 页面宽度
     */
    public void setPageWidth(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_WIDTH_ID, value);
    }
    
    /**
     * get页面高度
     */
    public int getPageHeight(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_HEIGHT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1200;
        }
        return a;
    }
    
    /**
     * set 页面高度
     */
    public void setPageHeight(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_HEIGHT_ID, value);
    }
    
    /**
     * 
     * @param attr
     * @param verAlign
     */
    public void setPageVerticalAlign(IAttributeSet attr, byte verAlign)
    {
        attr.setAttribute(AttrIDConstant.PAGE_VERTICAL_ID, verAlign);
    }
    
    /**
     * 
     * @param attr
     * @return
     */
    public byte getPageVerticalAlign(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_VERTICAL_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return (byte)a;
    }
    
    /**
     * 
     * @param attr
     * @param verAlign
     */
    public void setPageHorizontalAlign(IAttributeSet attr, byte verAlign)
    {
        attr.setAttribute(AttrIDConstant.PAGE_HORIZONTAL_ID, verAlign);
    }
    
    /**
     * 
     * @param attr
     * @return
     */
    public byte getPageHorizontalAlign(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_HORIZONTAL_ID);
        if (a == Integer.MIN_VALUE)
        {
            return WPAttrConstant.PAGE_H_LEFT;
        }
        return (byte)a;
    }
    
    /**
     * get 页面左边距
     */
    public int getPageMarginLeft(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_LEFT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1800;
        }
        return a;
    }
    /**
     * set 页面左边距
     */
    public void setPageMarginLeft(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_LEFT_ID, value);
    }

    /**
     * get 页面右边距
     */
    public int getPageMarginRight(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_RIGHT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1800;
        }
        return a;
    }
    /**
     * set 页面右边距
     */
    public void setPageMarginRight(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_RIGHT_ID, value);
    }
        
    /**
     * get 页面上边距
     */
    public int getPageMarginTop(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_TOP_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1440;
        }
        return a;
    }
    /**
     * set 页面上边距
     */
    public void setPageMarginTop(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_TOP_ID, value);
    }
    
    /**
     * get 页面下边距
     */
    public int getPageMarginBottom(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_BOTTOM_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 1440;
        }
        return a;
    }
    /**
     * set 页面下边距
     */
    public void setPageMarginBottom(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_BOTTOM_ID, value);
    }
    
    
    /**
     * get header margin
     */
    public int getPageHeaderMargin(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_HEADER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 850;
        }
        return a;
    }
    /**
     * set header margin
     */
    public void setPageHeaderMargin(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_HEADER_ID, value);
    }
    
    
    /**
     * get footer margin
     */
    public int getPageFooterMargin(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_FOOTER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 850;
        }
        return a;
    }
    /**
     * set footer margin
     */
    public void setPageFooterMargin(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_FOOTER_ID, value);
    }
    
    
    /**
     * get page background color
     */
    public int getPageBackgroundColor(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_BACKGROUND_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            return Color.WHITE;
        }
        return a;
    }
    
    /**
     * get page background color
     */
    public void setPageBackgroundColor(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_BACKGROUND_COLOR_ID, value);
    }
    
    
    /**
     * get page background color
     */
    public int getPageBorder(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_BORDER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * get page background color
     */
    public void setPageBorder(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_BORDER_ID, value);
    }
    
    /**
     * line pitch
     * @param attr
     * @return
     */
    public int getPageLinePitch(IAttributeSet attr)
    {
    	int a = attr.getAttribute(AttrIDConstant.PAGE_LINEPITCH_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    
    /**
     * line pitch
     * @param attr
     * @param value
     */
    public void setPageLinePitch(IAttributeSet attr, int value)
    {
    	attr.setAttribute(AttrIDConstant.PAGE_LINEPITCH_ID, value);
    }
    
    // =========== 表格属性 ================
    /**
     * get top border
     */
    public int getTableTopBorder(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_TOP_BORDER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set top border
     */
    public void setTableTopBorder(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_TOP_BORDER_ID, value);
    }
    
    /**
     * get top border color
     */
    public int getTableTopBorderColor(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_TOP_BORDER_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            return Color.BLACK;
        }
        return a;
    }
    /**
     * set top border color
     */
    public void setTableTopBorderColor(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_TOP_BORDER_COLOR_ID, value);
    }
    
    /**
     * get left border
     */
    public int getTableLeftBorder(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_LEFT_BORDER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set left border
     */
    public void setTableLeftBorder(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_LEFT_BORDER_ID, value);
    }
    
    /**
     * get left border color
     */
    public int getTableLeftBorderColor(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_LEFT_BORDER_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            return Color.BLACK;
        }
        return a;
    }
    /**
     * set left border color
     */
    public void setTableLeftBorderColor(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_LEFT_BORDER_COLOR_ID, value);
    }    
    
    /**
     * get bottom border
     */
    public int getTableBottomBorder(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_BOTTOM_BORDER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set bottom border
     */
    public void setTableBottomBorder(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_BOTTOM_BORDER_ID, value);
    }
    
    /**
     * get bottom border color
     */
    public int getTableBottomBorderColor(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_BOTTOM_BORDER_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            return Color.BLACK;
        }
        return a;
    }
    /**
     * set bottom border color
     */
    public void setTableBottomBorderColor(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_BOTTOM_BORDER_COLOR_ID, value);
    }
    
    /**
     * get right border
     */
    public int getTableRightBorder(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_RIGHT_BORDER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set rith border
     */
    public void setTableRightBorder(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_RIGHT_BORDER_ID, value);
    }
    
    /**
     * get right border color
     */
    public int getTableRightBorderColor(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_RIGHT_BORDER_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            return Color.BLACK;
        }
        return a;
    }
    /**
     * set right border color
     */
    public void setTableRightBorderColor(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_RIGHT_BORDER_COLOR_ID, value);
    }
    
    /**
     * get table row height
     */
    public int getTableRowHeight(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_ROW_HEIGHT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set table row height 
     */
    public void setTableRowHeight(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_ROW_HEIGHT_ID, value);
    }
    
    /**
     * get table cell width 
     */
    public int getTableCellWidth(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_CELL_WIDTH_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * 
     */
    public void setTableCellWidth(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_CELL_WIDTH_ID, value);
    }
    
    /**
     * get table row split
     */
    public boolean isTableRowSplit(IAttributeSet attr, int value)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_ROW_SPLIT_ID);
        if (a == Integer.MIN_VALUE)
        {
            return true;
        }
        return a == 1;
    }
    /**
     * set table row split
     */
    public void setTableRowSplit(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.TABLE_ROW_SPLIT_ID, b ? 1 : 0);
    }
    
    /**
     * get table header row
     */
    public boolean isTableHeaderRow(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_ROW_HEADER_ID);
        if (a == Integer.MIN_VALUE)
        {
            return true;
        }
        return a == 1;
    }
    /**
     * set table header row
     */
    public void setTableHeaderRow(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.TABLE_ROW_HEADER_ID, b ? 1 : 0);
    }    
    
    /**
     * get the first horizontal merged cell
     */
    public boolean isTableHorFirstMerged(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_CELL_HOR_FIRST_MERGED_ID);
        if (a == Integer.MIN_VALUE)
        {
            return false;
        }
        return a == 1;
    }
    /**
     * set first horizontal merged cell
     */
    public void setTableHorFirstMerged(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.TABLE_CELL_HOR_FIRST_MERGED_ID, b ? 1 : 0);
    }
    
    /**
     * get horizontal merged cell
     */
    public boolean isTableHorMerged(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_CELL_HORIZONTAL_MERGED_ID);
        if (a == Integer.MIN_VALUE)
        {
            return false;
        }
        return a == 1;
    }
    /**
     * set horizontal merged cell
     */
    public void setTableHorMerged(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.TABLE_CELL_HORIZONTAL_MERGED_ID, b ? 1 : 0);
    }
    
    /**
     * get the first horizontal merged cell
     */
    public boolean isTableVerFirstMerged(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_CELL_VER_FIRST_MERGED_ID);
        if (a == Integer.MIN_VALUE)
        {
            return false;
        }
        return a == 1;
    }
    /**
     * set first horizontal merged cell
     */
    public void setTableVerFirstMerged(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.TABLE_CELL_VER_FIRST_MERGED_ID, b ? 1 : 0);
    }
    
    /**
     * get horizontal merged cell
     */
    public boolean isTableVerMerged(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_CELL_VERTICAL_MERGED_ID);
        if (a == Integer.MIN_VALUE)
        {
            return false;
        }
        return a == 1;
    }
    /**
     * set horizontal merged cell
     */
    public void setTableVerMerged(IAttributeSet attr, boolean b)
    {
        attr.setAttribute(AttrIDConstant.TABLE_CELL_VERTICAL_MERGED_ID, b ? 1 : 0);
    }
    
    /**
     * get table cell vertical alignment 
     */
    public int getTableCellVerAlign(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_CELL_VERTICAL_ALIGN_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set table cell vertical alignment
     */
    public void setTableCellVerAlign(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_CELL_VERTICAL_ALIGN_ID, value);
    }
    
    /**
     * get table top MARGIN 
     */
    public int getTableTopMargin(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_TOP_MARGIN_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set table top MARGIN 
     */
    public void setTableTopMargin(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_TOP_MARGIN_ID, value);
    }
    
    /**
     * get table bottom MARGIN 
     */
    public int getTableBottomMargin(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_BOTTOM_MARGIN_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set table bottom MARGIN 
     */
    public void setTableBottomMargin(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_BOTTOM_MARGIN_ID, value);
    }
    
    /**
     * get table left MARGIN 
     */
    public int getTableLeftMargin(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_LEFT_MARGIN_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set table left MARGIN 
     */
    public void setTableLeftMargin(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_LEFT_MARGIN_ID, value);
    }
    
    
    /**
     * get table left MARGIN 
     */
    public int getTableRightMargin(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.TABLE_RIGHT_MARGIN_ID);
        if (a == Integer.MIN_VALUE)
        {
            return 0;
        }
        return a;
    }
    /**
     * set table left MARGIN 
     */
    public void setTableRightMargin(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.TABLE_RIGHT_MARGIN_ID, value);
    }
    
    
    /**
     * get table left MARGIN 
     */
    public int getTableCellTableBackground(IAttributeSet attr)
    {
        int a = attr.getAttribute(AttrIDConstant.PAGE_BACKGROUND_COLOR_ID);
        if (a == Integer.MIN_VALUE)
        {
            return -1;
        }
        return a;
    }
    /**
     * 
     */
    public void setTableCellBackground(IAttributeSet attr, int value)
    {
        attr.setAttribute(AttrIDConstant.PAGE_BACKGROUND_COLOR_ID, value);
    }
    
    
    //=========================  非属性操作 ==================    
    /**
     * 填充页面属性
     * @param pageAttr
     * @param section
     */
    public void fillPageAttr(PageAttr pageAttr, IAttributeSet attr)
    {
        pageAttr.reset();

        pageAttr.verticalAlign = getPageVerticalAlign(attr);
        pageAttr.horizontalAlign = getPageHorizontalAlign(attr); 
        
        pageAttr.pageWidth = (int)(getPageWidth(attr) * MainConstant.TWIPS_TO_PIXEL);
        pageAttr.pageHeight = (int)(getPageHeight(attr) * MainConstant.TWIPS_TO_PIXEL);
        pageAttr.topMargin = (int)(getPageMarginTop(attr) * MainConstant.TWIPS_TO_PIXEL);
        pageAttr.bottomMargin = (int)(getPageMarginBottom(attr) * MainConstant.TWIPS_TO_PIXEL);
        pageAttr.rightMargin = (int)(getPageMarginRight(attr) * MainConstant.TWIPS_TO_PIXEL);
        pageAttr.leftMargin = (int)(getPageMarginLeft(attr) * MainConstant.TWIPS_TO_PIXEL);
        pageAttr.headerMargin = (int)(getPageHeaderMargin(attr) * MainConstant.TWIPS_TO_PIXEL);
        pageAttr.footerMargin = (int)(getPageFooterMargin(attr) * MainConstant.TWIPS_TO_PIXEL);
        
        pageAttr.pageBRColor = getPageBackgroundColor(attr);
        pageAttr.pageBorder = getPageBorder(attr);
        pageAttr.pageLinePitch =  (getPageLinePitch(attr) * MainConstant.TWIPS_TO_PIXEL);
    }
    
    
    /**
     * 填充段落属性
     * 
     * @param paraAttr
     * @param para
     */
    public void fillParaAttr(IControl control, ParaAttr paraAttr, IAttributeSet attr)
    {
        paraAttr.reset();
        paraAttr.tabClearPosition = (int)(getParaTabsClearPostion(attr) * MainConstant.TWIPS_TO_PIXEL);
        paraAttr.leftIndent = (int)(getParaIndentLeft(attr) * MainConstant.TWIPS_TO_PIXEL);
        paraAttr.rightIndent = (int)(getParaIndentRight(attr) * MainConstant.TWIPS_TO_PIXEL);
        paraAttr.beforeSpace = (int)(getParaBefore(attr) * MainConstant.TWIPS_TO_PIXEL);
        paraAttr.afterSpace = (int)(getParaAfter(attr) * MainConstant.TWIPS_TO_PIXEL);
        // 行距类型
        paraAttr.lineSpaceType = (byte)getParaLineSpaceType(attr);
        // 行距值
        paraAttr.lineSpaceValue = getParaLineSpace(attr);
        //
        if (paraAttr.lineSpaceType == WPAttrConstant.LINE_SAPCE_LEAST
            || paraAttr.lineSpaceType == WPAttrConstant.LINE_SPACE_EXACTLY)
        {
            paraAttr.lineSpaceValue *= MainConstant.TWIPS_TO_PIXEL;
        }
        // 水平对齐
        paraAttr.horizontalAlignment = (byte)getParaHorizontalAlign(attr);
        /*
         * 特殊缩进
         * 规则：firstLineIndent > 0，表示首行缩进
         *       firstLineIndent < 0，表示悬挂缩进
         */
        paraAttr.specialIndentValue = (int)(getParaSpecialIndent(attr) * MainConstant.TWIPS_TO_PIXEL);
        // listID
        paraAttr.listID = getParaListID(attr);
        //
        paraAttr.listLevel = (byte)getParaListLevel(attr);
        //
        paraAttr.pgBulletID = getPGParaBulletID(attr);
        
        // 左缩进和特殊缩进需要考虑 bullet and number 因素
        if (paraAttr.listID >= 0 && paraAttr.listLevel >= 0 && control != null)
        {
            ListData listData = control.getSysKit().getListManage().getListData(paraAttr.listID);
            if (listData != null)
            {
                ListLevel listLevel = listData.getLevel(paraAttr.listLevel);
                if (listLevel != null)
                {
                    // 文本缩进
                    paraAttr.listTextIndent = (int)(listLevel.getTextIndent() * MainConstant.TWIPS_TO_PIXEL);
                    // bn 对齐位置
                    paraAttr.listAlignIndent = paraAttr.listTextIndent + (int)(listLevel.getSpecialIndent() * MainConstant.TWIPS_TO_PIXEL);
                    // 段落没有左缩进
                    if (paraAttr.leftIndent - paraAttr.listTextIndent == 0
                        || paraAttr.leftIndent == 0)
                    {
                        // 如果段落的特殊 = 0，则需要把 bn 的文本缩进设置为段落的悬挂缩进
                        if (paraAttr.specialIndentValue == 0)
                        {
                            paraAttr.specialIndentValue = -(paraAttr.listTextIndent - paraAttr.listAlignIndent);
                        }
                        // 如果是悬挂缩进，需要把对齐位置设置到段浇左缩时
                        if (paraAttr.specialIndentValue < 0)
                        {
                            paraAttr.leftIndent = paraAttr.listAlignIndent;
                            paraAttr.listAlignIndent = 0;
                        } 
                        //
                        else if (paraAttr.listAlignIndent > paraAttr.specialIndentValue)
                        {
                        	paraAttr.specialIndentValue += paraAttr.listAlignIndent;
                        	//paraAttr.listAlignIndent = 0;
                        }
                    }
                     // 段落有左缩进
                    else
                    {
                        // 如果段浇缩进　+ BN 对齐位置　= BN 文本缩时，则段落缩进　=　BN 对齐位置
                        if (paraAttr.leftIndent + paraAttr.listAlignIndent == paraAttr.listTextIndent)
                        {
                            paraAttr.leftIndent = paraAttr.listAlignIndent;
                        }
                        // 如果是是首行缩进，则需要把首行缩进值设置给 bn 对齐位置
                        if (paraAttr.specialIndentValue >= 0)
                        {
                            paraAttr.listAlignIndent = paraAttr.specialIndentValue; 
                        }
                        // 如果是悬挂缩进，则 bn 对齐位置 0
                        else
                        {
                            paraAttr.listAlignIndent = 0;
                        }
                        // 如果　
                        if (paraAttr.specialIndentValue == 0 && paraAttr.listTextIndent - paraAttr.leftIndent > 0)
                        {
                            paraAttr.specialIndentValue -= paraAttr.listTextIndent - paraAttr.leftIndent;
                        }                        
                    }
                    // 如果制表位清除位置大于等于左缩进，则左缩进设置为0
                    /*if (tabClearPosition >= paraAttr.leftIndent && tabClearPosition > 0)
                    {
                        paraAttr.leftIndent = 0;                             
                    }
                    // 如果制表位清除位置大于等于左缩进，则左缩进设置为0
                    if (tabClearPosition >= paraAttr.listAlignIndent && tabClearPosition > 0)
                    {
                        paraAttr.listAlignIndent = 0;                             
                    }
                    // 如果制表位清除位置大于等于左缩进，则左缩进设置为0
                    if (tabClearPosition >= paraAttr.listTextIndent && tabClearPosition > 0)
                    {
                        paraAttr.listTextIndent = 0;                             
                    }*/                    
                }
            }
        }
    }
    
    /**
     * 填充段落属性
     * 
     * @param paraAttr
     * @param para
     */
    public void fillCharAttr(CharAttr charAttr, IAttributeSet paraAttr, IAttributeSet leafAttr)
    {
        charAttr.reset();
        charAttr.fontIndex = getFontName(paraAttr, leafAttr);
        charAttr.fontSize = getFontSize(paraAttr, leafAttr);
        charAttr.fontScale = getFontScale(paraAttr, leafAttr);
        charAttr.fontColor = getFontColor(paraAttr, leafAttr);
        charAttr.isBold = getFontBold(paraAttr, leafAttr);
        charAttr.isItalic = getFontItalic(paraAttr, leafAttr);
        charAttr.isStrikeThrough = getFontStrike(paraAttr, leafAttr);
        charAttr.isDoubleStrikeThrough = getFontDoubleStrike(paraAttr, leafAttr);
        charAttr.underlineType = getFontUnderline(paraAttr, leafAttr);
        charAttr.underlineColor = getFontUnderlineColor(paraAttr, leafAttr);
        charAttr.subSuperScriptType = (short)getFontScript(paraAttr, leafAttr);
        charAttr.highlightedColor = getFontHighLight(paraAttr, leafAttr);
        charAttr.encloseType = (byte)getFontEncloseChanacterType(paraAttr, leafAttr);
        charAttr.pageNumberType = (byte)getFontPageNumberType(leafAttr);
    }
    

    /**
     * 
     * @param tabelAttr
     * @param attr
     */
    public void fillTableAttr(TableAttr tableAttr, IAttributeSet attr)
    {
        // 由于POI无法没有解析出表格上、下、左、右边距，故采用默认值
        tableAttr.topMargin = 0;//(int)(AttrManage.instance().getTableTopMargin(attr) * MainConstant.TWIPS_TO_PIXEL);
        tableAttr.leftMargin = 7;//(int)(AttrManage.instance().getTableLeftMargin(attr) * MainConstant.TWIPS_TO_PIXEL);
        tableAttr.rightMargin = 7;//(int)(AttrManage.instance().getTableRightMargin(attr) * MainConstant.TWIPS_TO_PIXEL);
        tableAttr.bottomMargin = 0;//(int)(AttrManage.instance().getTableBottomMargin(attr) * MainConstant.TWIPS_TO_PIXEL);
        tableAttr.cellWidth = (int)(getTableCellWidth(attr) * MainConstant.TWIPS_TO_PIXEL);
        tableAttr.cellVerticalAlign = (byte)getTableCellVerAlign(attr);
        tableAttr.cellBackground = getTableCellTableBackground(attr);
    }
    
    /**
     * 
     */
    public void dispose()
    {
    }
}
