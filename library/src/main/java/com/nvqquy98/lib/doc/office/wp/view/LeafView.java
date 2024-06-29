/*
 * 文件名称:          LeafView.java
 *
 * 编译器:            android2.2
 * 时间:              上午10:36:21
 */
package com.nvqquy98.lib.doc.office.wp.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.macro.UpdateStatusListener;
import com.nvqquy98.lib.doc.office.simpletext.font.FontTypefaceManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.AbstractView;
import com.nvqquy98.lib.doc.office.simpletext.view.CharAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewKit;

/**
 * word Leaf 视图
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
public class LeafView extends AbstractView/* implements IMemObj*/ {

    private static StringBuffer title = new StringBuffer();

    /**
     *
     */
    public LeafView() {
    }

    /**
     * @param paraElem
     * @param elem
     */
    public LeafView(IElement paraElem, IElement elem) {
        this.elem = elem;
        initProperty(elem, paraElem);
    }

    /**
     *
     */
    public short getType() {
        return WPViewConstant.LEAF_VIEW;
    }

    /**
     * 初始化leaf属性
     */
    public void initProperty(IElement elem, IElement paraElem) {
        this.elem = elem;
        if (paint == null) {
            paint = new Paint();
        } else {
            paint.reset();
        }
        paint.setAntiAlias(true);
        if (charAttr == null) {
            charAttr = new CharAttr();
        }
        AttrManage.instance().fillCharAttr(charAttr, paraElem.getAttribute(), elem.getAttribute());
        // 粗斜体
        if (charAttr.isBold && charAttr.isItalic) {
            paint.setTextSkewX(-0.2f);
            paint.setFakeBoldText(true);
        }
        // 粗体
        else if (charAttr.isBold) {
            paint.setFakeBoldText(true);
        }
        // 斜体
        else if (charAttr.isItalic) {
            paint.setTextSkewX(-0.25f);
        }
        // 字体没有什么好改变的，用统一的吧
        //paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        paint.setTypeface(FontTypefaceManage.instance().getFontTypeface(charAttr.fontIndex));
        // 字号
        if (charAttr.subSuperScriptType > 0) {
            paint.setTextSize(charAttr.fontSize * (charAttr.fontScale / 100.f) * MainConstant.POINT_TO_PIXEL / 2);
        } else {
            paint.setTextSize(charAttr.fontSize * (charAttr.fontScale / 100.f) * MainConstant.POINT_TO_PIXEL);
        }
        // 颜色
        paint.setColor(charAttr.fontColor);
        //paint.setColor(Color.BLACK);;
    }

    /**
     * 视图布局
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public int doLayout(DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr, int x, int y, int w, int h, long maxEnd, int flag) {
        //CharacterRun cr = (CharacterRun)elem;
        long start = getStartOffset(null);
        long startElem = elem.getStartOffset();
        String text = elem.getText(null);
        if (start > startElem) {
            text = text.substring((int) (start - startElem), (int) (elem.getEndOffset() - startElem));
        }
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float tW = 0;
        int i = 0;
        boolean layoutInTable = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_PARA_IN_TABLE);
        int breakType = WPViewConstant.BREAK_NO;
        boolean keepOne = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE);
        char ch;
        for (; i < text.length(); i++) {
            ch = text.charAt(i);
            tW += widths[i];
            if (ch == '\u0007'
                    || ch == '\n' || ch == '\r') {
                tW -= widths[i];
                i++;
                breakType = WPViewConstant.BREAK_ENTER;
                break;
            } else if (!layoutInTable && ch == '\f') {
                i++;
                breakType = WPViewConstant.BREAK_PAGE;
                break;
            } else if (ch == '\u000b') {
                i++;
                breakType = WPViewConstant.BREAK_ENTER;
                break;
            } else if (tW > w) {
                tW -= widths[i];
                breakType = WPViewConstant.BREAK_LIMIT;
                if (keepOne && i == 0) {
                    tW += widths[i];
                    i++;
                    breakType = WPViewConstant.BREAK_NO;
                }
                break;
            }
        }
//        // 如果是上下标，则宽度需要 / 2
//        if (charAttr.subSuperScriptType > 0)
//        {
//            tW /= 2;
//        }
        setEndOffset(i + start);
        setSize((int) tW, (int) Math.ceil((paint.descent() - paint.ascent())));
        return breakType;
    }

    /**
     * 得到指定结束位置字符宽度
     *
     * @param maxEnd
     * @return
     */
    public float getTextWidth() {
        //CharacterRun cr = (CharacterRun)elem;
        String text = elem.getText(null);
        int s = (int) (start - elem.getStartOffset());
        int e = (int) (end - elem.getStartOffset());
        text = text.substring(s, e);
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float tW = 0;
        for (int i = 0; i < text.length(); i++) {
            tW += widths[i];
        }
        return tW;
    }

    private String getFieldTextReplacedByPage(String text, int page) {
        if (text != null) {
            char[] chars = text.toCharArray();
            title.delete(0, title.length());
            for (int i = 0; i < chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    title.append(chars[i]);
                }
            }
            if (title.length() > 0) {
                return text.replace(title.toString(), String.valueOf(page));
            }
        }
        return text;
    }

    /**
     * @return
     */
    public int getPageNumber() {
        try {
            IView view = getParentView().getParentView().getParentView();
            if (view instanceof CellView) {
                view = view.getParentView().getParentView().getParentView();
            }
            if (view instanceof PageView) {
                return ((PageView) view).getPageNumber();
            } else if (view instanceof TitleView) {
                return UpdateStatusListener.ALLPages;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * @param canvas
     * @param x
     * @param y
     * @param zoom
     */
    public synchronized void draw(Canvas canvas, int originX, int originY, float zoom) {
        float dX = (x * zoom) + originX;
        float dY = (y * zoom) + originY;
        int oldColor = paint.getColor();
        //CharacterRun cr = (CharacterRun)elem;
        // 高亮
        if (charAttr.highlightedColor != -1) {
            IView line = getParentView();
            if (line != null) {
                paint.setColor(charAttr.highlightedColor);
                canvas.drawRect(dX, originY, dX + getWidth() * zoom, originY + line.getHeight() * zoom, paint);
                paint.setColor(oldColor);
            }
        }
        float oldFontSize = paint.getTextSize();
        //if (zoom != 1.0)
        {
            float newSize = (/*charAttr.subSuperScriptType > 0 ? oldFontSize / 2 :  */oldFontSize) * zoom;
            paint.setTextSize(newSize);
        }
        // 下标
        if (charAttr.subSuperScriptType == 1) {
            dY -= (int) Math.ceil((paint.descent() - paint.ascent()));
        }
        // 绘制文本
        String text = elem.getText(null);
        int s = (int) (start - elem.getStartOffset());
        int e = (int) (end - elem.getStartOffset());
        boolean adjustFieldText = false;
        //total pages
        if (charAttr.pageNumberType == WPModelConstant.PN_PAGE_NUMBER) {
            try {
                IView view = getParentView().getParentView().getParentView();
                if (view != null) {
                    PageView pageView = null;
                    while (view != null) {
                        if (view instanceof TitleView && view.getParentView() != null) {
                            pageView = (PageView) view.getParentView();
                            break;
                        } else if (view instanceof PageView) {
                            pageView = (PageView) view;
                            break;
                        } else if (view instanceof WPSTRoot) {
                            view = view.getParentView().getParentView().getParentView().getParentView();
                        } else {
                            view = view.getParentView();
                        }
                    }
                    if (pageView != null) {
                        adjustFieldText = true;
                        text = getFieldTextReplacedByPage(text, pageView.getPageNumber());
                        s = 0;
                        e = text.length();
                    }
                }
            } catch (Exception exc) {
            }
        } else if (charAttr.pageNumberType == WPModelConstant.PN_TOTAL_PAGES && numPages > 0) {
            adjustFieldText = true;
            text = getFieldTextReplacedByPage(text, numPages);
            s = 0;
            e = text.length();
        }
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float extX = 0;
        if (!adjustFieldText && zoom != 1.0) {
            float cw = 0;
            ;
            for (int i = s; i < e; i++) {
                cw += widths[i];
            }
            char ch = text.charAt(e - 1);
            if (ch == '\u0007'
                    || ch == '\n' || ch == '\r') {
                cw -= widths[e - 1];
            }
            float extW = 0;
            IView nextView = getNextView();
            if (nextView != null
                    && (nextView.getType() == WPViewConstant.LEAF_VIEW
                    || (nextView.getType() == WPViewConstant.SHAPE_VIEW && ((ShapeView) nextView).isInline()))) {
                float nextX = getNextView().getX() * zoom;
                extW = cw - ((nextX + originX) - dX);
            } else {
                extW = cw - getLayoutSpan(WPViewConstant.X_AXIS) * zoom;
            }
            if (extW != 0) {
                extX = extW / (e - s);
            }
        }
        float drawX = dX;
        float drawY = dY - paint.ascent();
        for (int i = s; i < e; i++) {
            char c = text.charAt(i);
            if (c == '\n' || c == '\r' || c == 0x07 || c == '\u000b'
                    || c == 0x0c || c == '\t' || c == ' ' || c == '\u0002'
                    || c == '\f') {
                drawX += widths[i] - extX;
                continue;
            }
            // 处理连字符问题
            int skip = 0;
            for (int j = i + 1; j < e; j++) {
                if (widths[j] != 0) {
                    break;
                }
                skip++;
            }
            canvas.drawText(text, i, i + 1 + skip, drawX, drawY, paint);
            drawX += widths[i] - extX;
            i += skip;
        }
        // 绘制删除线
        dY += (int) Math.ceil((paint.descent() - paint.ascent())) / 2;
        // 单删除线
        if (charAttr.isStrikeThrough) {
            canvas.drawRect(dX, dY - 1, dX + getWidth() * zoom, dY + 1, paint);
        }
        // 双删除线
        else if (charAttr.isDoubleStrikeThrough) {
            canvas.drawRect(dX, dY - 3, dX + getWidth() * zoom, dY - 1, paint);
            canvas.drawRect(dX, dY, dX + getWidth() * zoom, dY + 2, paint);
        }
        paint.setTextSize(oldFontSize);
    }

    /**
     * model到视图
     *
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack) {
        String text = elem.getText(null);
        int s = (int) (start - elem.getStartOffset());
        int e = (int) (offset - elem.getStartOffset());
        text = text.substring(s, e);
        rect.x = (int) paint.measureText(text);
        rect.x += getX();
        rect.y += getY();
        rect.height = getLayoutSpan(WPViewConstant.Y_AXIS);
        return rect;
    }

    /**
     * @param x
     * @param y
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public long viewToModel(int x, int y, boolean isBack) {
        x -= this.x;
        String text = elem.getText(null);
        int s = (int) (start - elem.getStartOffset());
        int e = (int) (end - elem.getStartOffset());
        text = text.substring(s, e);
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            x -= widths[i];
            if (x <= 0) {
                // 如果x值小于最后一个字符宽度的一半，则取前一个offset
                if (x + widths[i] >= widths[i] / 2) {
                    count++;
                }
                break;
            }
            count++;
        }
        return start + count;
    }

    /**
     * 得到基线
     */
    public int getBaseline() {
        if (!"\n".equals(elem.getText(null))) {
            return (int) -paint.ascent();
        }
        return 0;
    }

    /**
     * @return
     */
    public int getUnderlinePosition() {
        return (int) (getY() + getHeight() - (getHeight() - paint.getTextSize()));
    }

    /**
     *
     */
    public CharAttr getCharAttr() {
        return this.charAttr;
    }

    /**
     * 放回对象池
     */
    public void free() {
        /*parent = null;
        preView = null;
        nextView = null;
        child = null;
        ViewFactory.leafView.free(this);*/
    }

    /**
     * current leafview has total page number field code or not
     *
     * @return
     */
    public boolean hasUpdatedFieldText() {
        if (charAttr != null && charAttr.pageNumberType == WPModelConstant.PN_TOTAL_PAGES) {
            return true;
        }
        return false;
    }

    /**
     * @param numPages
     * @return
     */
    public void setNumPages(int numPages) {
        if (hasUpdatedFieldText()) {
            this.numPages = numPages;
        }
    }

    /**
     * 复制对象
     * /
     * public IMemObj getCopy()
     * {
     * return new LeafView();
     * }
     * <p>
     * /**
     */
    public void dispose() {
        super.dispose();
        paint = null;
        charAttr = null;
        //free();
    }

    // 字符属性
    protected CharAttr charAttr;
    //
    protected Paint paint;
    //
    protected int numPages = -1;
}
