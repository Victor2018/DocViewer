/*
 * 文件名称:          LayoutKit.java
 *
 * 编译器:            android2.2
 * 时间:              下午4:16:55
 */
package com.nvqquy98.lib.doc.office.wp.view;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.font.FontKit;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewKit;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.wp.control.Word;

/**
 * 布局工具类
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-13
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class LayoutKit {
    private int screenWidthPixels = 0;
    private int screenHeightPixels = 0;
    private static LayoutKit kit = new LayoutKit();

    private LayoutKit() {
    }

    /**
     * @return
     */
    public static LayoutKit instance() {
        return kit;
    }

    /**
     * 布局页面坐标
     *
     * @param root
     * @param zoom
     */
    public void layoutAllPage(PageRoot root, float zoom) {
        if (root == null || root.getChildView() == null) {
            return;
        }
        Word word = (Word) root.getContainer();
        if (word.getContext() != null && (screenWidthPixels == 0 || screenHeightPixels == 0)) {
            //获取资源对象
            Resources resources = word.getContext().getResources();
            //获取屏幕数据
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            //获取屏幕宽高，单位是像素
            screenWidthPixels = displayMetrics.widthPixels;
            screenHeightPixels = displayMetrics.heightPixels;
        }
        int dx = WPViewConstant.PAGE_SPACE;
        int dy = WPViewConstant.PAGE_SPACE;
        IView pv = root.getChildView();
        int width = pv.getWidth();
        float scale = 1;
        if (screenWidthPixels != 0 && width != 0) {
            scale = screenWidthPixels * 1f / width;
            if (zoom == 1f) {
                word.setZoom(scale, 0, 0);
            }
        }
        // int visibleWidth = word.getWidth();
        // visibleWidth = visibleWidth == 0 ? word.getWordWidth() : visibleWidth;
        // if (visibleWidth > width * zoom) {
        //     dx += (int) (visibleWidth / zoom - width - WPViewConstant.PAGE_SPACE * 2) / 2;
        // }
        while (pv != null) {
            pv.setLocation(dx, dy);
            dy += pv.getHeight() + WPViewConstant.PAGE_SPACE;
            pv = pv.getNextView();
        }
        root.setSize(width + WPViewConstant.PAGE_SPACE * 2, dy);
        ((Word) root.getContainer()).setSize(width + WPViewConstant.PAGE_SPACE * 2, dy);
    }

    /**
     * 布局段浇
     *
     * @param docAttr     文档属性
     * @param pageAttr    页面属性
     * @param paraAttr    段浇属性
     * @param para        布局段落视图
     * @param startOffset 布局开始Offset
     * @param x           布局开始x值
     * @param y           布局开始y值
     * @param w           布局的宽度
     * @param h           布局的高度
     * @param flag        布局标记
     * @return
     */
    public int layoutPara(IControl control, IDocument doc, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr,
                          ParagraphView para, long startOffset, int x, int y, int w, int h, int flag) {
        // get paragraph token
        //ParaToken token = TokenManage.instance().allocToken(para);
        int breakType = WPViewConstant.BREAK_NO;
        int dx = paraAttr.leftIndent;
        int dy = 0;
        int spanW = w - paraAttr.leftIndent - paraAttr.rightIndent;
        spanW = spanW < 0 ? w : spanW;
        int spanH = h;
        int paraHeight = 0;
        int maxWidth = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_NOT_WRAP_LINE) ? 0 : w;
        boolean firstLine = true;
        IElement elem = para.getElement();
        long lineStart = startOffset;
        long elemEnd = elem.getEndOffset();
        // 处理段前段后间距
        IView prePara = para.getPreView();
        if (prePara == null) // 页面第一个段落
        {
            spanH -= paraAttr.beforeSpace;
            para.setTopIndent(paraAttr.beforeSpace);
            para.setBottomIndent(paraAttr.afterSpace);
            para.setY(para.getY() + paraAttr.beforeSpace);
        } else {
            if (paraAttr.beforeSpace > 0) {
                int beforeSpace = paraAttr.beforeSpace - prePara.getBottomIndent();
                beforeSpace = Math.max(0, beforeSpace);
                spanH -= beforeSpace;
                para.setTopIndent(beforeSpace);
                para.setY(para.getY() + beforeSpace);
            }
            spanH -= paraAttr.afterSpace;
            para.setBottomIndent(paraAttr.afterSpace);
        }
        boolean keepOne = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE);
        if (spanH < 0 && !keepOne) {
            return WPViewConstant.BREAK_LIMIT;
        }
        LineView line = (LineView) ViewFactory.createView(control, elem, elem, WPViewConstant.LINE_VIEW);
        line.setStartOffset(lineStart);
        para.appendChlidView(line);
        flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, true);
        boolean ss = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_DELLELINEVIEW);
        BNView bnView = null;
        int bnViewWidth = -1;
        while (spanH > 0 && lineStart < elemEnd && breakType != WPViewConstant.BREAK_PAGE) {
            // layout bullet and number
            if (firstLine && startOffset == elem.getStartOffset()) {
                bnView = createBNView(control, doc, docAttr, pageAttr, paraAttr, para, dx, dy, spanW, spanH, flag);
                if (bnView != null) {
                    bnViewWidth = bnView.getWidth();
                }
            }
            int lineIndent = getLineIndent(control, bnViewWidth, paraAttr, firstLine);
            if (bnView != null && lineIndent + paraAttr.leftIndent == paraAttr.tabClearPosition) {
                if ((AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.PARA_SPECIALINDENT_ID)
                        && AttrManage.instance().getParaSpecialIndent(elem.getAttribute()) < 0)
                        || AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.PARA_INDENT_LEFT_ID)) {
                    bnView.setX(0);
                    lineIndent = bnViewWidth;
                    dx = 0;
                }
            }
            line.setLeftIndent(lineIndent);
            line.setLocation(dx + lineIndent, dy);
            breakType = layoutLine(control, doc, docAttr, pageAttr, paraAttr, line, bnView, dx, dy, spanW - lineIndent, spanH, elemEnd, flag);
            int lineHeight = line.getLayoutSpan(WPViewConstant.Y_AXIS);
            if (!ss && !keepOne
                    /*&& breakType == WPViewConstant.BREAK_LIMIT*/
                    && ((spanH - lineHeight < 0 || line.getChildView() == null)
                    || spanW - lineIndent <= 0)) {
                breakType = WPViewConstant.BREAK_LIMIT;
                para.deleteView(line, true);
                break;
            }
            paraHeight += lineHeight;
            dy += lineHeight;
            spanH -= lineHeight;
            lineStart = line.getEndOffset(null);
            maxWidth = Math.max(maxWidth, line.getLayoutSpan(WPViewConstant.X_AXIS));
            if (lineStart < elemEnd && spanH > 0) {
                line = (LineView) ViewFactory.createView(control, elem, elem, WPViewConstant.LINE_VIEW);
                line.setStartOffset(lineStart);
                para.appendChlidView(line);
            }
            keepOne = false;
            //flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, keepOne);
            firstLine = false;
            bnView = null;
        }
        para.setSize(maxWidth, paraHeight);
        para.setEndOffset(lineStart);
        //
        //token.setFree(true);
        return breakType;
    }

    /**
     * @param doc
     * @param para
     * @return
     */
    public int buildLine(IDocument doc, ParagraphView para) {
        int breakType = WPViewConstant.BREAK_NO;
        //get paragraph token
        //ParaToken token = TokenManage.instance().allocToken(para);
        //
        /*AttrManage.instance().fillPageAttr(pageAttr, doc.getSection(0).getAttribute());
        //
        AttrManage.instance().fillParaAttr(paraAttr, para.getElement().getAttribute());

        int breakType = WPViewConstant.BREAK_NO;
        int dx = paraAttr.leftIndent;
        int dy = 0;
        int spanW = para.getWidth();
        int spanH = para.getHeight();
        int paraHeight = 0;
        int maxWidth = para.getWidth();
        boolean firstLine = true;
        IElement elem = para.getElement();
        long startOffset = para.getStartOffset(doc);
        long lineStart = startOffset;
        int flag = 0;
        long elemEnd = elem.getEndOffset();
        LineView line = (LineView)ViewFactory.createView(elem, elem, WPViewConstant.LINE_VIEW);
        line.setStartOffset(lineStart);
        para.appendChlidView(line);
        boolean keepOne = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE);
        flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, true);
        boolean ss = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_DELLELINEVIEW);
        BNView bnView = null;
        while (spanH > 0 && lineStart < elemEnd)
        {
            // layout bullet and number
            if (firstLine && startOffset == elem.getStartOffset())
            {
                bnView = createBNView(doc, docAttr, pageAttr, paraAttr, para, dx, dy, spanW, spanH, flag);
            }
            int lineIndent = getLineIndent(bnView, paraAttr, firstLine);
            line.setLeftIndent(lineIndent);
            line.setLocation(dx + lineIndent , dy);
            breakType = layoutLine(doc, docAttr, pageAttr, paraAttr, line, bnView, dx, dy, spanW - lineIndent, spanH, elemEnd, flag);
            int lineHeight = line.getLayoutSpan(WPViewConstant.Y_AXIS);
            if (!ss && !keepOne
                && breakType == WPViewConstant.BREAK_LIMIT
                && (spanH - lineHeight < 0 || line.getChildView() == null)
                || spanW - lineIndent <= 0)
            {
                breakType = WPViewConstant.BREAK_LIMIT;
                para.deleteView(line, true);
                break;
            }
            paraHeight += lineHeight;
            dy += lineHeight;
            spanH -= lineHeight;
            lineStart = line.getEndOffset(null);
            maxWidth = Math.max(maxWidth, line.getLayoutSpan(WPViewConstant.X_AXIS));
            if (lineStart < elemEnd && spanH > 0)
            {
                line = (LineView)ViewFactory.createView(elem, elem, WPViewConstant.LINE_VIEW);
                line.setStartOffset(lineStart);
                para.appendChlidView(line);
            }
            keepOne = false;
            //flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, keepOne);
            firstLine = false;
            bnView = null;
        }
        para.setSize(maxWidth, paraHeight);
        para.setEndOffset(lineStart);
        //
        //token.setFree(true);*/
        return breakType;
    }

    /**
     * 布局行
     *
     * @param docAttr  文档属性
     * @param pageAttr 页面属性
     * @param paraAttr 段落属性
     * @param line     布局的行
     * @param x        布局开始x值
     * @param y        布局开始y值
     * @param w        布局的宽度
     * @param h        布局的高度
     * @param maxEnd   布局的最大结束位置
     * @param flag     布局标记
     * @return
     */
    public int layoutLine(IControl control, IDocument doc, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr, LineView line,
                          BNView bnView, int x, int y, int w, int h, long maxEnd, int flag) {
        int breakType = WPViewConstant.BREAK_NO;
        int dx = 0;
        int dy = 0;
        int spanW = w;
        long start = line.getStartOffset(null);
        long pos = start;
        IElement elem = line.getElement();
        LeafView leaf = null;
        IElement run;
        int lineWidth = 0;
        int lineHeigth = 0;
        int lineHeigthExceptShape = 0;
        boolean keepOne = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE);
        while (spanW > 0 && pos < maxEnd || keepOne) {
            run = doc.getLeaf(pos);
            if (run == null) {
                break;
            }
            leaf = (LeafView) ViewFactory.createView(control, run, elem, WPViewConstant.LEAF_VIEW);
            line.appendChlidView(leaf);
            leaf.setStartOffset(pos);
            leaf.setLocation(dx, dy);
            breakType = leaf.doLayout(docAttr, pageAttr, paraAttr, dx, dy, spanW, h, maxEnd, flag);
            if ((leaf.getType() == WPViewConstant.OBJ_VIEW || leaf.getType() == WPViewConstant.SHAPE_VIEW)
                    && breakType == WPViewConstant.BREAK_LIMIT) {
                line.deleteView(leaf, true);
                breakType = WPViewConstant.BREAK_NO;
                break;
            }
            pos = leaf.getEndOffset(null);
            line.setEndOffset(pos);
            int leafWidth = leaf.getLayoutSpan(WPViewConstant.X_AXIS);
            lineWidth += leafWidth;
            dx += leafWidth;
            lineHeigth = Math.max(lineHeigth, leaf.getLayoutSpan(WPViewConstant.Y_AXIS));
            if (leaf.getType() != WPViewConstant.OBJ_VIEW && leaf.getType() != WPViewConstant.SHAPE_VIEW) {
                lineHeigthExceptShape = Math.max(lineHeigthExceptShape, leaf.getLayoutSpan(WPViewConstant.Y_AXIS));
            }
            spanW -= leafWidth;
            if (breakType == WPViewConstant.BREAK_LIMIT
                    || breakType == WPViewConstant.BREAK_ENTER
                    || breakType == WPViewConstant.BREAK_PAGE) {
                break;
            }
            flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, false);
            keepOne = false;
        }
        line.setSize(lineWidth, lineHeigth);
        line.setHeightExceptShape(lineHeigthExceptShape);
        // 布局宽度受限，需要进行
        if (breakType == WPViewConstant.BREAK_LIMIT) {
            String str = elem.getText(doc);
            long paraStart = elem.getStartOffset();
            str = str.substring((int) (start - paraStart));
            long newPos = FontKit.instance().findBreakOffset(str, (int) (pos - start)) + start;
            adjustLine(line, newPos);
        }
        line.layoutAlignment(docAttr, pageAttr, paraAttr, bnView, w, flag);
        return breakType;
    }

    /**
     * @param doc
     * @param docAttr
     * @param pageAttr
     * @param paraAttr
     * @param para
     * @param startOffset
     * @param x
     * @param y
     * @param w
     * @param h
     * @param flag
     * @return
     */
    private BNView createBNView(IControl control, IDocument doc, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr,
                                ParagraphView para, int x, int y, int w, int h, int flag) {
        if (paraAttr.listID >= 0 && paraAttr.listLevel >= 0
                || paraAttr.pgBulletID >= 0) {
            BNView bnView = (BNView) ViewFactory.createView(control, null, null, WPViewConstant.BN_VIEW);
            bnView.doLayout(doc, docAttr, pageAttr, paraAttr, para, x, y, w, h, flag);
            para.setBNView(bnView);
            return bnView;
        }
        return null;
    }

    /**
     * 根据新的断行位置，调整视图
     *
     * @param line
     * @param newPos
     */
    private void adjustLine(LineView line, long newPos) {
        IView view = line.getLastView();
        IView temp;
        int lineWidth = line.getWidth();
        while (view != null && view.getStartOffset(null) >= newPos) {
            temp = view.getPreView();
            lineWidth -= view.getWidth();
            line.deleteView(view, true);
            view = temp;
        }
        // 同一leaf，需要折分
        int leafWidth = 0;
        if (view != null && view.getEndOffset(null) > newPos) {
            view.setEndOffset(newPos);
            lineWidth -= view.getWidth();
            leafWidth = (int) ((LeafView) view).getTextWidth();
            // 重置Leaf的宽度
            view.setWidth(leafWidth);
            lineWidth += leafWidth;
        }
        line.setEndOffset(newPos);
        line.setWidth(lineWidth);
    }

    /**
     *
     */
    private int getLineIndent(IControl control, int bnViewWidth, ParaAttr paraAttr, boolean firstLine) {
        // 首先缩进
        if (firstLine) {
            int bnWidth = bnViewWidth <= 0 ? 0 : bnViewWidth;
            if (paraAttr.specialIndentValue > 0) {
                return paraAttr.specialIndentValue + bnWidth;
            } else {
                return bnWidth;
            }
        }
        // 悬挂缩进
        else if (!firstLine && paraAttr.specialIndentValue < 0) {
            if (bnViewWidth > 0 && control.getApplicationType() == MainConstant.APPLICATION_TYPE_PPT) {
                return bnViewWidth;
            }
            // 悬挂缩进 值也设置到左缩进，左缩进需要减去悬挂缩进
            return -paraAttr.specialIndentValue;
        }
        return 0;
    }
    //
    //private DocAttr docAttr = new DocAttr();
    //
    //private PageAttr pageAttr = new PageAttr();
    //
    //private ParaAttr paraAttr = new ParaAttr(); 
}
