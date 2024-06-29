/*
 * 文件名称:          WPLayouter.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:13:54
 */
package com.nvqquy98.lib.doc.office.wp.view;

import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewKit;
import com.nvqquy98.lib.doc.office.wp.model.WPDocument;


/**
 * Word 布局器
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
public class WPLayouter
{
    /**
     * 
     * @param root
     */
    public WPLayouter(PageRoot root)
    {
        this.root = root;
        docAttr = new DocAttr();
        docAttr.rootType = WPViewConstant.PAGE_ROOT;
        pageAttr = new PageAttr();
        paraAttr = new ParaAttr();
        tableLayout = new TableLayoutKit();
        hfTableLayout = new TableLayoutKit();
    }
    
    /**
     * 
     */
    public void doLayout()
    {
        tableLayout.clearBreakPages();
        doc = root.getDocument();
        // 正文区或
        // mainRange = doc.getRange();
        section = doc.getSection(0);
        // 
        AttrManage.instance().fillPageAttr(pageAttr, section.getAttribute());
        //
        PageView pv = (PageView)ViewFactory.createView(root.getControl(), section, null, WPViewConstant.PAGE_VIEW);
        root.appendChlidView(pv);
        layoutPage(pv);
        LayoutKit.instance().layoutAllPage(root, 1.0f);
    }
    
    /**
     * 
     */
    public int layoutPage(PageView pageView)
    {
    	pageView.setPageNumber(currentPageNumber++);
    	
        layoutHeaderAndFooter(pageView);
        int breakType = WPViewConstant.BREAK_NO;
        pageView.setSize(pageAttr.pageWidth, pageAttr.pageHeight);
        pageView.setIndent(pageAttr.leftMargin, pageAttr.topMargin, pageAttr.rightMargin, pageAttr.bottomMargin);
        pageView.setStartOffset(currentLayoutOffset);
        
        int dx = pageAttr.leftMargin;
        int dy = pageAttr.topMargin;
        int spanW = pageAttr.pageWidth - pageAttr.leftMargin - pageAttr.rightMargin;
        int spanH = pageAttr.pageHeight - pageAttr.topMargin - pageAttr.bottomMargin;
        int flag = ViewKit.instance().setBitValue(0, WPViewConstant.LAYOUT_FLAG_KEEPONE, true);
        long maxEnd = doc.getAreaEnd(WPModelConstant.MAIN);
       
        IElement elem = breakPara != null ? breakPara.getElement() : doc.getParagraph(currentLayoutOffset);
        
        ParagraphView para = null;
        if (breakPara != null)
        {
            para = breakPara;
            // process table break;
            if (breakPara.getType() == WPViewConstant.TABLE_VIEW)
            {
                pageView.setHasBreakTable(true);
                ((TableView)breakPara).setBreakPages(true);
            }
        }
        else if (AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.PARA_LEVEL_ID))
        {
            elem = ((WPDocument)doc).getParagraph0(currentLayoutOffset);
            para = (ParagraphView)ViewFactory.createView(root.getControl(), elem, null, WPViewConstant.TABLE_VIEW);
        }
        else
        {   
            para = (ParagraphView)ViewFactory.createView(root.getControl(), elem, null, WPViewConstant.PARAGRAPH_VIEW);
        }
        pageView.appendChlidView(para);
        
        para.setStartOffset(currentLayoutOffset);
        para.setEndOffset(elem.getEndOffset());
        boolean keepOne = true;
        while (spanH > 0 && currentLayoutOffset < maxEnd && breakType != WPViewConstant.BREAK_LIMIT
            && breakType != WPViewConstant.BREAK_PAGE)
        {
            para.setLocation(dx, dy);
            // 表格段落
            if (para.getType() == WPViewConstant.TABLE_VIEW)
            {             
                if (para.getPreView() != null)
                {
                    if (para.getPreView().getElement() != elem)
                    {
                        tableLayout.clearBreakPages();
                    }
                }
                breakType = tableLayout.layoutTable(root.getControl(), doc, root, docAttr, pageAttr, paraAttr, 
                    (TableView)para, currentLayoutOffset, dx, dy, spanW, spanH, flag, breakPara != null);
            }
            else
            {
                tableLayout.clearBreakPages();
                AttrManage.instance().fillParaAttr(root.getControl(), paraAttr, elem.getAttribute());
                breakType = LayoutKit.instance().layoutPara(root.getControl(), doc, docAttr, pageAttr, paraAttr, 
                    para, currentLayoutOffset, dx, dy, spanW, spanH, flag);
            }
            int paraHeight = para.getLayoutSpan(WPViewConstant.Y_AXIS);
            if (!keepOne && para.getChildView() == null)
            {
                if (breakPara == null)
                {
                    elem = doc.getParagraph(currentLayoutOffset - 1);
                }
                pageView.deleteView(para, true);
                break;
            }
            //
            if (para.getType() != WPViewConstant.TABLE_VIEW)
            {
                root.getViewContainer().add(para);
            }
            // 收集段落中的 shape view
            collectShapeView(pageView, para, false);
            
            dy += paraHeight;
            currentLayoutOffset = para.getEndOffset(null);
            spanH -= paraHeight;
            if (spanH > 0 && currentLayoutOffset < maxEnd && breakType != WPViewConstant.BREAK_LIMIT
                && breakType != WPViewConstant.BREAK_PAGE)
            {                
                elem = doc.getParagraph(currentLayoutOffset);
                if (AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.PARA_LEVEL_ID))
                {
                    if (elem != para.getElement())
                    {
                        tableLayout.clearBreakPages(); 
                    }
                    elem = ((WPDocument)doc).getParagraph0(currentLayoutOffset);
                    para = (ParagraphView)ViewFactory.createView(root.getControl(), elem, null, WPViewConstant.TABLE_VIEW);                    
                }
                else
                {
                    para = (ParagraphView)ViewFactory.createView(root.getControl(), elem, null, WPViewConstant.PARAGRAPH_VIEW);
                }
                para.setStartOffset(currentLayoutOffset);
                pageView.appendChlidView(para);
            }
            flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, false);
            breakPara = null;
            keepOne = false;
        }
        // table
        if (para.getType() == WPViewConstant.TABLE_VIEW && tableLayout.isTableBreakPages())
        {
            breakPara = (ParagraphView)ViewFactory.createView(root.getControl(), elem, null, WPViewConstant.TABLE_VIEW);
            pageView.setHasBreakTable(true);
            ((TableView)para).setBreakPages(true);
        }
        // 
        else if (elem != null && currentLayoutOffset < elem.getEndOffset())
        {
            breakPara = (ParagraphView)ViewFactory.createView(root.getControl(), elem, null, WPViewConstant.PARAGRAPH_VIEW);
        }
        
        pageView.setEndOffset(currentLayoutOffset);
        //
        root.getViewContainer().sort();
        //
        root.addPageView(pageView);
        //
        pageView.setPageBackgroundColor(pageAttr.pageBRColor);
        //
        pageView.setPageBorder(pageAttr.pageBorder);
        
        return breakType;
    }
    
    
    /**
     * 
     */
    private void layoutHeaderAndFooter(PageView pageView)
    {
        if (header == null)
        {
            header = layoutHFParagraph(pageView, true);
            if (header != null)
            {
                int h = header.getLayoutSpan(WPViewConstant.Y_AXIS);
                if (pageAttr.headerMargin + h > pageAttr.topMargin)
                {
                    pageAttr.topMargin = pageAttr.headerMargin + h; 
                }
                header.setParentView(pageView);
            }
        }
        else
        {
            for (LeafView sv :shapeViews)
            {                
                if (WPViewKit.instance().getArea(sv.getStartOffset(null)) == WPModelConstant.HEADER)
                {
                    pageView.addShapeView(sv);
                }
            }
        }
        pageView.setHeader(header);
        if (footer == null)
        {
            footer = layoutHFParagraph(pageView, false);
            if (footer != null)
            {                
                if (footer.getY() < pageAttr.pageHeight - pageAttr.bottomMargin)
                {
                    pageAttr.bottomMargin =  pageAttr.pageHeight - footer.getY(); 
                }
                footer.setParentView(pageView);
            }
        }
        else
        {
            for (LeafView sv :shapeViews)
            {                
                if (WPViewKit.instance().getArea(sv.getStartOffset(null)) == WPModelConstant.FOOTER)
                {
                    pageView.addShapeView(sv);
                }
            }
        }
        
        pageView.setFooter(footer);
    }
    
    /**
     * 
     */
    private TitleView layoutHFParagraph(PageView pageView, boolean isHeader)
    {
        long offset = isHeader ? WPModelConstant.HEADER : WPModelConstant.FOOTER;
        int breakType = WPViewConstant.BREAK_NO;
        IElement hfElem = doc.getHFElement(offset, WPModelConstant.HF_ODD);
        if (hfElem == null)
        {
            return null;
        }
        
        //ignore line pitch for header and footer layout
    	float oldLinePitch = pageAttr.pageLinePitch;
    	pageAttr.pageLinePitch = -1;
    	
        TitleView titleView = (TitleView)ViewFactory.createView(root.getControl(), hfElem, null, WPViewConstant.TITLE_VIEW);
        titleView.setPageRoot(root);
        titleView.setLocation(pageAttr.leftMargin, pageAttr.headerMargin);
        
        long maxEnd = hfElem.getEndOffset();
        int spanW = pageAttr.pageWidth - pageAttr.leftMargin - pageAttr.rightMargin;
        int spanH = (pageAttr.pageHeight - pageAttr.topMargin - pageAttr.bottomMargin - 100) / 2;
        int flag = ViewKit.instance().setBitValue(0, WPViewConstant.LAYOUT_FLAG_KEEPONE, true);
        ParagraphView para = null;
        IElement paraElem = doc.getParagraph(offset);
        if (AttrManage.instance().hasAttribute(paraElem.getAttribute(), AttrIDConstant.PARA_LEVEL_ID))
        {
            paraElem = ((WPDocument)doc).getParagraph0(offset);
            para = (ParagraphView)ViewFactory.createView(root.getControl(), paraElem, null, WPViewConstant.TABLE_VIEW);
        }
        else
        {   
            para = (ParagraphView)ViewFactory.createView(root.getControl(), paraElem, null, WPViewConstant.PARAGRAPH_VIEW);
        }        
        titleView.appendChlidView(para);
        
        para.setStartOffset(offset);
        para.setEndOffset(paraElem.getEndOffset());
        boolean keepOne = true;
        int dx = 0;
        int dy = 0;
        int titleHeight = 0;
        while (spanH > 0 && offset < maxEnd && breakType != WPViewConstant.BREAK_LIMIT)
        {
            para.setLocation(dx, dy);
            // 表格段落
            if (para.getType() == WPViewConstant.TABLE_VIEW)
            {
                breakType = hfTableLayout.layoutTable(root.getControl(), doc, root, docAttr, pageAttr, paraAttr, 
                    (TableView)para, offset, dx, dy, spanW, spanH, flag, breakPara != null);
            }
            else
            {
                hfTableLayout.clearBreakPages();
                AttrManage.instance().fillParaAttr(root.getControl(), paraAttr, paraElem.getAttribute());
                breakType = LayoutKit.instance().layoutPara(root.getControl(), doc, docAttr, pageAttr, paraAttr, 
                    para, offset, dx, dy, spanW, spanH, flag);
            }
            int paraHeight = para.getLayoutSpan(WPViewConstant.Y_AXIS);
            if (!keepOne && para.getChildView() == null)
            {
                titleView.deleteView(para, true);
                break;
            }
            dy += paraHeight;
            titleHeight += paraHeight;
            offset = para.getEndOffset(null);
            spanH -= paraHeight;
            // 收集段落中的 shape view
            collectShapeView(pageView, para, true);
            if (spanH > 0 && offset < maxEnd && breakType != WPViewConstant.BREAK_LIMIT)
            {
                paraElem = doc.getParagraph(offset);
                if (AttrManage.instance().hasAttribute(paraElem.getAttribute(), AttrIDConstant.PARA_LEVEL_ID))
                {
                    paraElem = ((WPDocument)doc).getParagraph0(offset);
                    para = (ParagraphView)ViewFactory.createView(root.getControl(), paraElem, null, WPViewConstant.TABLE_VIEW);
                }
                else
                {
                    para = (ParagraphView)ViewFactory.createView(root.getControl(), paraElem, null, WPViewConstant.PARAGRAPH_VIEW);
                }
                para.setStartOffset(offset);
                titleView.appendChlidView(para);
            }
            flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, false);
            keepOne = false;
        }
        titleView.setSize(spanW, titleHeight);
        if (!isHeader)
        {
            titleView.setY(pageAttr.pageHeight - titleHeight - pageAttr.footerMargin);
        }
        
        //restore line pitch
    	pageAttr.pageLinePitch = oldLinePitch;
    	
        return titleView;
    }
    
    /**
     * 
     */
    public void backLayout()
    {
        PageView pv = (PageView)ViewFactory.createView(root.getControl(), section, null, WPViewConstant.PAGE_VIEW);
        root.appendChlidView(pv);
        layoutPage(pv);
    }

    /**
     * @return Returns the currentLayoutOffset.
     */
    public long getCurrentLayoutOffset()
    {
        return currentLayoutOffset;
    }

    /**
     * @param currentLayoutOffset The currentLayoutOffset to set.
     */
    public void setCurrentLayoutOffset(long currentLayoutOffset)
    {
        this.currentLayoutOffset = currentLayoutOffset;
    }
    
    /**
     * 
     */
    public boolean isLayoutFinish()
    {
        return currentLayoutOffset >= doc.getAreaEnd(WPModelConstant.MAIN) && breakPara == null;
    }
    
    /**
     * 
     */
    private void collectShapeView(PageView page, ParagraphView para, boolean isHF)
    {
        if (para.getType() == WPViewConstant.PARAGRAPH_VIEW)
        {
            collectShapeViewForPara(page, para, isHF);
        }
        else if (para.getType() == WPViewConstant.TABLE_VIEW)
        {
            IView row = para.getChildView();
            while (row != null)
            {
                IView cell = row.getChildView();
                while (cell != null)
                {
                    IView paraView = cell.getChildView();
                    while (paraView != null)
                    {
                        collectShapeViewForPara(page, (ParagraphView)para, isHF);
                        paraView = paraView.getNextView();
                    }
                    cell = cell.getNextView();
                }
                row = row.getNextView();
            }
        }
    }
    
    /**
     * 
     */
    private void collectShapeViewForPara(PageView page, ParagraphView para, boolean isHF)
    {
        IView line = para.getChildView();
        while (line != null)
        {
            IView leaf = line.getChildView();
            while (leaf != null)
            {                    
                if (leaf.getType() == WPViewConstant.SHAPE_VIEW)
                {
                    ShapeView shapeView = ((ShapeView)leaf);
                    if (!shapeView.isInline())
                    {
                        page.addShapeView(shapeView);
                        if (isHF)
                        {
                            shapeViews.add(shapeView);
                        }
                    }                        
                }
                else if(leaf.getType() == WPViewConstant.OBJ_VIEW)
                {
                    ObjView objView = ((ObjView)leaf);
                    if (!objView.isInline())
                    {
                        page.addShapeView(objView);
                        if (isHF)
                        {
                            shapeViews.add(objView);
                        }
                    } 
                }
                leaf = leaf.getNextView();
            }
            line = line.getNextView();
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        docAttr.dispose();
        docAttr = null;
        pageAttr.dispose();
        pageAttr = null;
        paraAttr.dispose();
        paraAttr = null;
        root = null;
        doc = null;
        breakPara = null;
        header = null;
        footer = null;
        tableLayout = null;
        hfTableLayout = null;
        shapeViews.clear();
    }
    

    // 文档属性集
    private DocAttr docAttr;
    // 章节属性集
    private PageAttr pageAttr;
    // 段落
    private ParaAttr paraAttr; 
    //
    private PageRoot root;
    //
    private IDocument doc;
    
    
    // ======== 布局过程用到一些布局状态的值 ==========
    private IElement section;
    // 当前布局的页码数
    private int currentPageNumber = 1;
    // 当前需要布局的开始的Offset，主要为了段落切页用到。
    private long currentLayoutOffset;
    // 段落分页
    private ParagraphView breakPara;
    // header
    private TitleView header;
    // footer
    private TitleView footer;
    //
    private TableLayoutKit tableLayout;
    //
    private TableLayoutKit hfTableLayout;
    //
    private List<LeafView> shapeViews = new ArrayList<LeafView>();
    
}
