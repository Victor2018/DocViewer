/*
 * 文件名称:          NormalRoot.java
 *  
 * 编译器:            android2.2
 * 时间:              上午8:56:38
 */

package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.AbstractView;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.IRoot;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewContainer;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewKit;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.wp.control.Word;
import com.nvqquy98.lib.doc.office.wp.model.WPDocument;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

/**
 * 普通视图
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-23
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class NormalRoot extends AbstractView implements IRoot
{
    // 一次线程布局的段落数
    private static final int LAYOUT_PARA = 20;
    
    /**
     * 
     * @param word
     */
    public NormalRoot(Word word)
    {
        this.word = word;
        this.doc =  word.getDocument();
        layoutThread = new LayoutThread(this);
        canBackLayout = true;
        docAttr = new DocAttr();
        docAttr.rootType = WPViewConstant.NORMAL_ROOT;
        pageAttr = new PageAttr();
        paraAttr = new ParaAttr();
        viewContainer = new ViewContainer();
        tableLayout = new TableLayoutKit();
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPViewConstant.NORMAL_ROOT;
    }

    /**
     * 
     */
    public synchronized int layoutAll()
    {        
        super.dispose();
        tableLayout.clearBreakPages();
        word.getControl().getSysKit().getListManage().resetForNormalView();
        viewContainer.clear();
        maxParaWidth = 0;
        prePara = null;
        currentLayoutOffset = 0;
        layoutPara();
        if (currentLayoutOffset < doc.getAreaEnd(WPModelConstant.MAIN))
        {
            canBackLayout = true;
            if (layoutThread.getState() == Thread.State.NEW)
            {                
                layoutThread.start();
            }
            word.getControl().actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);
        }
        layoutRoot();
        if (word.isExportImageAfterZoom())
        {
            if (getHeight() * word.getZoom() >= word.getScrollY() + word.getHeight()
                || currentLayoutOffset >= doc.getAreaEnd(WPModelConstant.MAIN))
            {
                word.setExportImageAfterZoom(false);
                word.getControl().actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
            }
        }
        return WPViewConstant.BREAK_NO;
    }
    
    /**
     * 视图布局
     * @param x
     * @param y
     * @param w
     * @param h
     * @param maxEnd 
     * @param flag 布局标记，传递一些布尔值，位操作
     */
    public int doLayout(int x, int y, int w, int h, int maxEnd, int flag)
    {
        IDocument doc = getDocument();
        viewContainer.clear();
        //mainRange = doc.getRange();
        layoutPara();
        if (currentLayoutOffset < doc.getAreaEnd(WPModelConstant.MAIN))
        {
            if (layoutThread.getState() == Thread.State.NEW)
            {                
                layoutThread.start();                
            }
            word.getControl().actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);
            //word.getControl().getMainFrame().getActivity().setProgressBarIndeterminateVisibility(true);
        }
        layoutRoot();
        return WPViewConstant.BREAK_NO;
    }

    /**
     * 
     */
    private int layoutPara()
    {
        relayout = true;
        int dx = WPViewConstant.PAGE_SPACE;
        int dy = prePara == null ? WPViewConstant.PAGE_SPACE : prePara.getY() + prePara.getHeight();
        int spanW = 0;
        if (word.getControl().getMainFrame().isZoomAfterLayoutForWord())
        {
            spanW = (int)(word.getResources().getDisplayMetrics().widthPixels / word.getZoom()) - WPViewConstant.PAGE_SPACE * 2;
        }
        else
        {
            spanW = word.getResources().getDisplayMetrics().widthPixels - WPViewConstant.PAGE_SPACE * 2;
        }
        int spanH = Integer.MAX_VALUE;
        int flag = ViewKit.instance().setBitValue(0, WPViewConstant.LAYOUT_FLAG_KEEPONE, true);
        int count = 0;
        long areaEnd = doc.getAreaEnd(WPModelConstant.MAIN);
        long start;
        IElement elem;
        IDocument doc = word.getDocument();
        while (count < LAYOUT_PARA && currentLayoutOffset < areaEnd && relayout)
        {
            elem = doc.getParagraph(currentLayoutOffset);
            ParagraphView para = null;
            if (AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.PARA_LEVEL_ID))
            {
                elem = ((WPDocument)doc).getParagraph0(currentLayoutOffset);
                para = (ParagraphView)ViewFactory.createView(word.getControl(), elem, null, WPViewConstant.TABLE_VIEW);
                if (prePara != null && elem != prePara.getElement())
                {
                    tableLayout.clearBreakPages(); 
                }
            }
            else
            {   
                para = (ParagraphView)ViewFactory.createView(word.getControl(), elem, null, WPViewConstant.PARAGRAPH_VIEW);
            }
            para.setParentView(this);
            start = elem.getStartOffset();
            para.setStartOffset(start);
            para.setEndOffset(elem.getEndOffset());
            if (prePara == null)
            {
                setChildView(para);
            }
            else
            {
                prePara.setNextView(para);
                para.setPreView(prePara);                
            }            
            para.setLocation(dx, dy);
            // 表格段落
            if (para.getType() == WPViewConstant.TABLE_VIEW)
            {
                 tableLayout.layoutTable(word.getControl(), doc, this, docAttr, pageAttr, paraAttr, 
                    (TableView)para, currentLayoutOffset, dx, dy, spanW, spanH, flag, false);
            }
            else
            {
                tableLayout.clearBreakPages();
                AttrManage.instance().fillParaAttr(word.getControl(), paraAttr, elem.getAttribute());
                filteParaAttr(paraAttr);
                
                LayoutKit.instance().layoutPara(word.getControl(), doc, docAttr, pageAttr, paraAttr, 
                    para, currentLayoutOffset, dx, dy, spanW, spanH, flag);
            }
            int paraHeight = para.getLayoutSpan(WPViewConstant.Y_AXIS);
            maxParaWidth = Math.max(para.getLayoutSpan(WPViewConstant.X_AXIS) + WPViewConstant.PAGE_SPACE, maxParaWidth);
            dy += paraHeight;
            spanH -= paraHeight;
            currentLayoutOffset = para.getEndOffset(null);
            count++;
            prePara = para;
            //
            viewContainer.add(para);
        }
        // 
        return WPViewConstant.BREAK_NO;    
    }
    
    /**
     * 
     */
    private void filteParaAttr(ParaAttr paraAttr)
    {
        paraAttr.rightIndent = paraAttr.rightIndent < 0 ? 0 : paraAttr.rightIndent;
        paraAttr.leftIndent = paraAttr.leftIndent < 0 ? 0 : paraAttr.leftIndent;
        //paraAttr.specialIndentValue = paraAttr.specialIndentValue < 0 ? 0 : paraAttr.specialIndentValue;
    }
    /**
     * 
     * @param canvas
     * @param x
     * @param y
     * @param zoom
     */
    public void draw(Canvas canvas, int originX, int originY, float zoom)
    {
        canvas.drawColor(Color.WHITE);
        //super.draw(canvas, originX, originY, zoom);
        int dX = (int)(x * zoom) + originX;
        int dY = (int)(y * zoom) + originY;
        IView view = getChildView();
        Rect clip = canvas.getClipBounds();
        boolean b = false;
        while (view != null)
        {
            if (view.intersection(clip, dX, dY, zoom))
            {
                view.draw(canvas, dX, dY, zoom);
                b = true;
            }
            else if (b)
            {
                break;
            }
            view = view.getNextView();
        }
    }
    
    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        //IView view = getView(offset, WPViewConstant.PARAGRAPH_VIEW, isBack);
        IView view = viewContainer.getParagraph(offset, isBack);
        if (view != null)
        {
            view.modelToView(offset, rect, isBack);
            
            IView p = view.getParentView();
            while (p!= null && p.getType() != WPViewConstant.NORMAL_ROOT)
            {
                /*if (p.getType() == WPViewConstant.TABLE_CELL_VIEW)
                {
                    rect.x += p.getX() + p.getLeftIndent();
                    rect.y += p.getY() + p.getTopIndent();
                }
                else*/
                {
                    rect.x += p.getX();
                    rect.y += p.getY();
                }
                p = p.getParentView();
            }
        }
        rect.x += getX();
        rect.y += getY();
        return rect;
        
    }
    
    
    /**
     * @param x
     * @param y
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public long viewToModel(int x, int y, boolean isBack)
    {
        x -= getX();
        y -= getY();
        IView view = getChildView();
        if (view == null)
        {
            return -1;
        }
        if (y > view.getY())
        {
            while (view != null)
            {
                if (y >= view.getY() && y < view.getY() + view.getLayoutSpan(WPViewConstant.Y_AXIS))
                {
                    break;
                }
                view = view.getNextView();
            }
        }
        view = view == null ? getChildView() : view;
        if (view != null)
        {
            return view.viewToModel(x, y, isBack);
        }
        return -1;
    }

    /**
     * 得到model
     */
    public IDocument getDocument()
    {
        return word.getDocument();
    }

    /**
     * 
     */
    public IWord getContainer()
    {
        return word;
    }
    
    /**
     * 
     */
    public IControl getControl()
    {
        return word.getControl();
    }

    /**
     *
     *
     */
    public boolean canBackLayout()
    {
        return canBackLayout && currentLayoutOffset < doc.getAreaEnd(WPModelConstant.MAIN);
    }

    /**
     * 
     */
    public synchronized void backLayout()
    {
        layoutPara();
        layoutRoot();
        if (currentLayoutOffset >= doc.getAreaEnd(WPModelConstant.MAIN))
        {
            word.getControl().actionEvent(EventConstant.SYS_AUTO_TEST_FINISH_ID, true);
            word.getControl().actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
            
            Rectangle r = word.getVisibleRect();
            int sX = r.x;
            int sY = r.y;
            int wW = (int)(getWidth() * word.getZoom());
            int wH = (int)(getHeight() * word.getZoom());
            if (r.x + r.width > wW)
            {
                sX = wW - r.width;
            }
            if (r.y + r.height > wH)
            {
                sY = wH - r.height;
            }
            final int sTx = sX;
            final int sTy = sY;
            if (sX != r.x || sY != r.y)
            {
                word.post(new Runnable()
                {
                    
                    @ Override
                    public void run()
                    {
                        word.scrollTo(Math.max(0, sTx), Math.max(0, sTy));
                    }
                });
            }
        }
        word.postInvalidate();
        if (word.isExportImageAfterZoom())
        {
            if (getHeight() * word.getZoom() >= word.getScrollY() + word.getHeight()
                || currentLayoutOffset >= doc.getAreaEnd(WPModelConstant.MAIN))
            {
                word.setExportImageAfterZoom(false);
                word.getControl().actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
            }
        }
    }
    
    /**
     * 
     */
    public void layoutRoot()
    {
        if (prePara != null)
        {
            setSize(Math.max(word.getWidth(), maxParaWidth), prePara.getY() + prePara.getHeight());
        }
    }
    
    /**
     * 
     */
    public void stopBackLayout()
    {
        canBackLayout = false;
        relayout = false;
    }   
    
    /**
     * 
     */
    public ViewContainer getViewContainer()
    {
        return this.viewContainer;
    }

    /**
     * 
     *
     */
    public synchronized void dispose()
    {
        super.dispose();
        canBackLayout = false;
        layoutThread.dispose();
        layoutThread = null;
        word = null;
        docAttr.dispose();
        docAttr = null;
        pageAttr.dispose();
        pageAttr = null;
        paraAttr.dispose();
        paraAttr = null;
        prePara = null;
        doc = null;
        tableLayout = null;
    }
    
    //
    private boolean relayout = true;
    // model
    private IDocument doc;
    // 视图组件
    private Word word;
    // 后台布局线程
    private LayoutThread layoutThread;
    // 文档属性集
    private DocAttr docAttr;
    // 章节属性集
    private PageAttr pageAttr;
    // 段落
    private ParaAttr paraAttr;
    // 当前布局前一个段落 
    private ParagraphView prePara;
    //
    private ViewContainer viewContainer;
    // 文档的段落总数
    //private int paraCount;
    // 当前需要布局的开始的Offset，主要为了段落切页用到。
    private long currentLayoutOffset;
    //
    private int maxParaWidth;
    // 是否可后台布局
    private boolean canBackLayout;
    //
    private TableLayoutKit tableLayout;
}
