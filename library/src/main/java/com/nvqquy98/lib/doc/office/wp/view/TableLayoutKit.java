/*
 * 文件名称:          TableLayoutKit.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:26:53
 */
package com.nvqquy98.lib.doc.office.wp.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.IRoot;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.TableAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewKit;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.wp.model.CellElement;
import com.nvqquy98.lib.doc.office.wp.model.RowElement;
import com.nvqquy98.lib.doc.office.wp.model.TableElement;

/**
 * 表格布局算法
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class TableLayoutKit
{   
    
    /**
     * 
     */
    public TableLayoutKit()
    {
        breakPagesCell = new LinkedHashMap<Integer, BreakPagesCell>();
    }
    /**
     * 布局段浇
     * @param docAttr       文档属性
     * @param pageAttr      页面属性
     * @param paraAttr      段浇属性
     * @param para          布局段落视图
     * @param startOffset   布局开始Offset
     * @param x             布局开始x值
     * @param y             布局开始y值
     * @param w             布局的宽度
     * @param h             布局的高度
     * @param flag          布局标记
     * @return
     */
    public int layoutTable(IControl control, IDocument doc, IRoot root, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr,
        TableView tableView, long startOffset, int x, int y, int w, int h, int flag, boolean isBreakPages)
    {
        mergedCell.clear();
        
        int span = h;
        int dx = 0;
        int dy = 0;
        int breakType =  WPViewConstant.BREAK_NO;
        TableElement tableElem = (TableElement)tableView.getElement();
        AttrManage.instance().fillTableAttr(tableAttr, tableElem.getAttribute()); 
        flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_PARA_IN_TABLE, true);
        boolean keepOne = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE);
        long maxEnd = tableElem.getEndOffset();
        int rowHeight = 0;
        int tableHeight = 0;
        int tableWidth = 0;
        RowView rowView = null;
        while (startOffset < maxEnd && span > 0
            || (breakRowElement != null && isBreakPages))
        {
            isRowBreakPages = false;
            IElement rowElem;
            if (breakRowElement != null && isBreakPages)
            {
                rowElem = breakRowElement;
                breakRowElement = null;
            }
            else
            {
                rowElem = tableElem.getElementForIndex(rowIndex++);
            }
            if (rowElem == null)
            {
                break;
            }
            if (rowView != null)
            {
                layoutMergedCell(rowView, (RowElement)rowElem, false);
                dy = rowView.getY() + rowView.getLayoutSpan(WPViewConstant.Y_AXIS);
                tableHeight = dy;
                span = h - tableHeight;
                if (/*breakType == WPViewConstant.BREAK_LIMIT || */span <= 0)
                {   
                    if (isBreakPages(rowView))
                    {
                        rowIndex--;
                        breakRowElement = (RowElement)rowView.getElement();
                    }
                    else
                    {
                        breakRowElement = (RowElement)rowElem;
                    }
                    
                    break;
                }
            }
            rowView = (RowView)ViewFactory.createView(control, rowElem, null, WPViewConstant.TABLE_ROW_VIEW);
            tableView.appendChlidView(rowView);
            rowView.setStartOffset(startOffset);
            rowView.setLocation(dx, dy);
            
            // layout row
            breakType = layoutRow(control, doc, root, docAttr, pageAttr, paraAttr, rowView, startOffset, dx, dy, w, span, flag, isBreakPages);
            
            rowHeight = rowView.getLayoutSpan(WPViewConstant.Y_AXIS);
            if ((rowHeight == 0 || span - rowHeight < 0) && !keepOne)
            {
                RowView preView = (RowView)rowView.getPreView();
                if (preView != null && isBreakPages(preView))
                {
                    rowIndex--;
                    breakRowElement = (RowElement)preView.getElement();
                    // 清除当行中跨行cellElem
                    clearCurrentRowBreakPageCell(rowElem);
                }
                else
                {
                    breakRowElement = (RowElement)rowView.getElement();
                    // 清除当行中跨行cellElem
                    clearCurrentRowBreakPageCell(rowElem);
                }
                tableView.deleteView(rowView, true);
                rowView = preView;
                breakType =  WPViewConstant.BREAK_LIMIT;
                break;
            }
            else if (breakType == WPViewConstant.BREAK_LIMIT && breakPagesCell.size() > 0
                || isRowBreakPages)
            {
                breakRowElement = (RowElement)rowView.getElement();
            }
            tableWidth = Math.max(tableWidth, rowView.getLayoutSpan(WPViewConstant.X_AXIS));
            dy += rowHeight;
            tableHeight += rowHeight;
            startOffset =  rowView.getEndOffset(null);
            isBreakPages = false;
            keepOne = false;
        }
        layoutMergedCell(rowView, null, true);
        ///
        tableView.setEndOffset(startOffset);
        //
        tableView.setSize(tableWidth, tableHeight);
        //
        if (docAttr.rootType == WPViewConstant.PAGE_ROOT)
        {            
            // table horizontal alignment
            byte hor = (byte)AttrManage.instance().getParaHorizontalAlign(tableElem.getAttribute());
            int want = w - tableWidth;
            if (hor == WPAttrConstant.PARA_HOR_ALIGN_CENTER ||
                hor == WPAttrConstant.PARA_HOR_ALIGN_RIGHT)
            {
                if (hor == WPAttrConstant.PARA_HOR_ALIGN_CENTER)
                {
                    want /= 2;
                }
                tableView.setX(tableView.getX() + want);
            }
            else
            {
                tableView.setX(tableView.getX() - tableAttr.leftMargin
                    + (int)(AttrManage.instance().getParaIndentLeft(tableElem.getAttribute()) * MainConstant.TWIPS_TO_PIXEL));    
            }
        }
        breakRowView = rowView;
        return breakType;
    }
    
    /**
     * 
     */
    private void clearCurrentRowBreakPageCell(IElement currentElem)
    {
        Vector<Integer> keys = new Vector<Integer>();
        for (Integer key : breakPagesCell.keySet())
        {
            BreakPagesCell bc = breakPagesCell.get(key);
            if (bc.getCell().getStartOffset() >= currentElem.getStartOffset()
                && bc.getCell().getEndOffset() <= currentElem.getEndOffset())
            {
                keys.add(key);
            }
        }
        for (Integer key : keys)
        {
            breakPagesCell.remove(key);
        }
    }
    
    /**
     * 布局段浇
     * @param docAttr       文档属性
     * @param pageAttr      页面属性
     * @param paraAttr      段浇属性
     * @param para          布局段落视图
     * @param startOffset   布局开始Offset
     * @param x             布局开始x值
     * @param y             布局开始y值
     * @param w             布局的宽度
     * @param h             布局的高度
     * @param flag          布局标记
     * @return
     */
    public int layoutRow(IControl control, IDocument doc, IRoot root, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr,
        RowView rowView, long startOffset, int x, int y, int w, int h, int flag, boolean isBreakPages)
    {
        int dx = 0;
        int dy = 0;
        int breakType =  WPViewConstant.BREAK_NO;
        
        RowElement rowElem = (RowElement)rowView.getElement();
        long maxEnd = rowElem.getEndOffset();
        int rowHeight = (int)(AttrManage.instance().getTableRowHeight(rowElem.getAttribute()) * MainConstant.TWIPS_TO_PIXEL);
        int rowWidth = 0;
        int cellWidth;
        int cellHeight;
        int maxCellHeight = 0;
        int maxRowHeight = rowHeight;
        int cellIndex = 0;
        boolean isNullCell;
        boolean isInvalid = true;
        
        while (cellIndex < rowElem.getCellNumber()
            /*|| breakPagesCell.size() > 0 && isBreakPages*/)
        {
            IElement cellElem = null;
            isNullCell = false;
            // 表格跨页
            if (isBreakPages && breakPagesCell.size() > 0)
            {
                if (breakPagesCell.containsKey(cellIndex))
                {
                    BreakPagesCell breakCell = breakPagesCell.remove(cellIndex);
                    cellElem =  breakCell.getCell();
                    startOffset = breakCell.getBreakOffset();
                }
                else
                {
                    cellElem = rowElem.getElementForIndex(cellIndex);
                    isNullCell = true;
                }
            }
            else
            {   
                cellElem = rowElem.getElementForIndex(cellIndex);
                if (cellElem == null)
                {
                    break;
                }
                startOffset = cellElem.getStartOffset();
                isNullCell = startOffset ==  cellElem.getEndOffset();
                if (!isNullCell && breakRowView != null && isBreakPages)
                {
                    CellView temp = breakRowView.getCellView((short)cellIndex);
                    if (temp != null)
                    {
                        isNullCell = temp.getEndOffset(null) == cellElem.getEndOffset();
                    }
                }
            }
            
            CellView cellView = (CellView)ViewFactory.createView(control, cellElem, null, WPViewConstant.TABLE_CELL_VIEW);
            rowView.appendChlidView(cellView);
            cellView.setStartOffset(startOffset);
            cellView.setLocation(dx, dy);
            cellView.setColumn((short)cellIndex);
            
            // cell跨页，但又没有补切到下一页
            if (isNullCell)
            {
                cellView.setFirstMergedCell(isBreakPages);
                breakType = layoutCellForNull(doc, root, docAttr, pageAttr, paraAttr, cellView, startOffset, dx, dy, w, h, flag, cellIndex, isBreakPages);
            }
            else
            {
                // first mergeCell
                cellView.setFirstMergedCell(isBreakPages || AttrManage.instance().isTableVerFirstMerged(cellElem.getAttribute()));
                cellView.setMergedCell(AttrManage.instance().isTableVerMerged(cellElem.getAttribute()));
                //
                breakType = layoutCell(control, doc, root, docAttr, pageAttr, paraAttr, cellView, startOffset, dx, dy, w, h, flag, cellIndex, isBreakPages);
            }
            //
            cellWidth = cellView.getLayoutSpan(WPViewConstant.X_AXIS);
            cellHeight = cellView.getLayoutSpan(WPViewConstant.Y_AXIS); 
            isInvalid = isInvalid && cellHeight == 0;
            dx += cellWidth;
            rowWidth += cellWidth;
            w -= cellWidth;
            if (!cellView.isMergedCell())
            {
                maxRowHeight = Math.max(maxRowHeight, cellHeight);
            }
            if (cellView.isFirstMergedCell())
            {
                mergedCell.add(cellView);
            }
            //
            maxCellHeight = Math.max(maxCellHeight, cellHeight);
            //
            startOffset = cellView.getEndOffset(null);
            cellIndex++;
        }
        //
        CellView cellView = (CellView)rowView.getChildView();
        while (cellView != null)
        {
            if (!cellView.isMergedCell()
                && cellView.getLayoutSpan(WPViewConstant.Y_AXIS) < maxRowHeight)
            {
                cellView.setHeight(maxRowHeight - cellView.getTopIndent() - cellView.getBottomIndent());
                //
                CellElement cellElem = (CellElement)cellView.getElement();
                if(cellElem != null)
                {
                	tableAttr.cellVerticalAlign = (byte)AttrManage.instance().getTableCellVerAlign(cellElem.getAttribute());
                }
                
                layoutCellVerticalAlign(cellView);                
            }
            cellView = (CellView)cellView.getNextView();
        }
        rowView.setEndOffset(maxEnd);
        if (isInvalid)
        {
            maxRowHeight = Integer.MAX_VALUE;
        }
        rowView.setSize(rowWidth, maxRowHeight);
        breakRowView =  null;
        return breakType;
    }
    
    
    /**
     * 布局段浇
     * @param docAttr       文档属性
     * @param pageAttr      页面属性
     * @param paraAttr      段浇属性
     * @param para          布局段落视图
     * @param startOffset   布局开始Offset
     * @param x             布局开始x值
     * @param y             布局开始y值
     * @param w             布局的宽度
     * @param h             布局的高度
     * @param flag          布局标记
     * @return
     */
    public int layoutCell(IControl control, IDocument doc, IRoot root, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr,
        CellView cellView, long startOffset, int x, int y, int w, int h, int flag, int cellIndex, boolean isBreakPages)
    {        
        CellElement cellElem = (CellElement)cellView.getElement();
        AttrManage.instance().fillTableAttr(tableAttr, cellElem.getAttribute());
        cellView.setBackground(tableAttr.cellBackground);
        
        cellView.setIndent(tableAttr.leftMargin, tableAttr.topMargin, tableAttr.rightMargin, tableAttr.bottomMargin);
        int dx = tableAttr.leftMargin;
        int dy = tableAttr.topMargin;
        
        int breakType = WPViewConstant.BREAK_NO;
        
        long maxEnd = cellElem.getEndOffset();
        int cellHeight = 0;
        int spanH = h - tableAttr.topMargin - tableAttr.bottomMargin;
        int cellWidth = tableAttr.cellWidth - tableAttr.leftMargin - tableAttr.rightMargin;
        
        while (startOffset < maxEnd && spanH > 0 && breakType != WPViewConstant.BREAK_LIMIT)
        {
            IElement paraElem = doc.getParagraph(startOffset);
            ParagraphView paraView = (ParagraphView)ViewFactory.createView(control, paraElem, null, WPViewConstant.PARAGRAPH_VIEW);
            cellView.appendChlidView(paraView);
            paraView.setStartOffset(startOffset);
            paraView.setLocation(dx, dy);
            //
            AttrManage.instance().fillParaAttr(cellView.getControl(), paraAttr, paraElem.getAttribute());
            breakType = LayoutKit.instance().layoutPara(control, doc, docAttr, pageAttr, paraAttr, paraView, 
                startOffset, dx, dy, cellWidth, spanH, flag);
            //
            int paraHeight = paraView.getLayoutSpan(WPViewConstant.Y_AXIS);
            if (paraView.getChildView() == null)
            {
                cellView.deleteView(paraView, true);
                break;
            }
            if (root.getViewContainer() != null)
            {
                root.getViewContainer().add(paraView);
            }
            
            dy += paraHeight;
            cellHeight += paraHeight;
            spanH -= paraHeight;
            
            startOffset = paraView.getEndOffset(null);
            paraView.setEndOffset(startOffset);
            flag = ViewKit.instance().setBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE, false);
        }
        if (startOffset < maxEnd)
        {
            if (!breakPagesCell.containsKey(cellIndex) && cellWidth > 0)
            {
                breakPagesCell.put(cellIndex, new BreakPagesCell(cellElem, startOffset));
                isRowBreakPages = true;
            }
        }
        cellView.setEndOffset(startOffset);
        cellView.setSize(cellWidth, cellHeight);
        return breakType;
    }
    
    /**
     * 布局段浇
     * @param docAttr       文档属性
     * @param pageAttr      页面属性
     * @param paraAttr      段浇属性
     * @param para          布局段落视图
     * @param startOffset   布局开始Offset
     * @param x             布局开始x值
     * @param y             布局开始y值
     * @param w             布局的宽度
     * @param h             布局的高度
     * @param flag          布局标记
     * @return
     */
    public int layoutCellForNull(IDocument doc, IRoot root, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr,
        CellView cellView, long startOffset, int x, int y, int w, int h, int flag, int cellIndex, boolean isBreakPages)
    {
        CellElement cellElem = (CellElement)cellView.getElement();
        AttrManage.instance().fillTableAttr(tableAttr, cellElem.getAttribute());        
        cellView.setIndent(tableAttr.leftMargin, tableAttr.topMargin, tableAttr.rightMargin, tableAttr.bottomMargin);
        int cellHeight = 0;
        int cellWidth = tableAttr.cellWidth - tableAttr.leftMargin - tableAttr.rightMargin;
        cellView.setSize(cellWidth, cellHeight);
        return WPViewConstant.BREAK_NO;
    }
    
    /**
     * 
     */
    private void layoutMergedCell(RowView row, RowElement nextRowElem, boolean isLastRow)
    {       
        if (row == null)
        {
            return;
        }
        int maxY = row.getY() + row.getLayoutSpan(WPViewConstant.Y_AXIS);
        if (isLastRow)
        {
            for (CellView cell : mergedCell)
            {
                if (cell.getParentView() != null)
                {
                    cell.setHeight(maxY - cell.getParentView().getY());
                    //
                    layoutCellVerticalAlign(cell);
                }
            }
            mergedCell.clear();
            return;
        }
        for (CellView cell : mergedCell)
        {
            maxY = Math.max(maxY, cell.getParentView().getY() + cell.getLayoutSpan(WPViewConstant.Y_AXIS));
        }
        Vector<CellView> vector = new Vector<CellView>(); 
        for(CellView cell : mergedCell)
        {            
            IElement cellElem = nextRowElem.getElementForIndex(cell.getColumn());
            if (cellElem == null)
            {
                continue;
            }
            // 如果下一个单元格式不是合并单元格
            if (!AttrManage.instance().isTableVerMerged(cellElem.getAttribute())
                || AttrManage.instance().isTableVerFirstMerged(cellElem.getAttribute()))
            {
                int cellHeight = cell.getLayoutSpan(WPViewConstant.Y_AXIS);
                if (cell.getParentView().getY() + cellHeight < maxY)
                {
                    cell.setHeight( maxY - cell.getParentView().getY());
                    //
                    layoutCellVerticalAlign(cell);
                }
                else
                {
                    row.setHeight(maxY - row.getY());
                    CellView cellView = (CellView)row.getChildView();
                    while (cellView != null)
                    {
                        if (!cellView.isMergedCell())
                        {
                            int oldHeight = cellView.getHeight();
                            cellView.setHeight(maxY - cellView.getParentView().getY());// + cellView.getLayoutSpan(WPViewConstant.Y_AXIS)));
                            //
                            if(oldHeight != cellView.getHeight())
                            {
                                layoutCellVerticalAlign(cellView);
                            }
                        }
                        cellView = (CellView)cellView.getNextView();
                    }
                }
                vector.add(cell);
            }
        }
        //
        for (CellView cell : vector)
        {
            maxY = cell.getParentView().getY() + cell.getLayoutSpan(WPViewConstant.Y_AXIS);
            if (maxY > row.getY() + row.getLayoutSpan(WPViewConstant.Y_AXIS))
            {
                cell.setHeight(row.getY() + row.getLayoutSpan(WPViewConstant.Y_AXIS) - cell.getY());
            }
            mergedCell.remove(cell);
        }
    }
    
    /**
     * 判断给行是否跨行
     */
    private boolean isBreakPages(RowView rowView)
    {
        IView view = rowView.getChildView();
        while (view != null)
        {
            IElement elem = view.getElement();
            if (view.getEndOffset(null) != elem.getEndOffset()
                && view.getWidth() > 0)
            {
                return true;
            }
            view = view.getNextView();
        }
        return false;
    }
    
    /**
     * 
     */
    private void layoutCellVerticalAlign(CellView cellView)
    {
        if (tableAttr.cellVerticalAlign ==  WPAttrConstant.PARA_VER_ALIGN_TOP)
        {
            return;
        }
        int textHeight = 0;
        IView para = cellView.getChildView();
        while (para != null)
        {
            textHeight += para.getLayoutSpan(WPViewConstant.Y_AXIS);
            para = para.getNextView();
        }
        int want = cellView.getLayoutSpan(WPViewConstant.Y_AXIS) - textHeight;
        int verAlignmnet = AttrManage.instance().getTableCellVerAlign(cellView.getElement().getAttribute());
        // vertical center alignment
        if (verAlignmnet == WPAttrConstant.PARA_VER_ALIGN_CENTER
            || verAlignmnet == WPAttrConstant.PARA_VER_ALIGN_BOTTOM)
        {
            if (verAlignmnet == WPAttrConstant.PARA_VER_ALIGN_CENTER)
            {
                want /= 2;
            }
            para = cellView.getChildView();
            while (para != null)
            {
                para.setY(para.getY() + want);
                para = para.getNextView();
            }
        }
    }
    
    /**
     * 表格是否跨页
     */
    public boolean isTableBreakPages()
    {
        return breakPagesCell.size() > 0
            || breakRowElement !=  null;
    }
    
    /**
     * 
     */
    public void clearBreakPages()
    {
        rowIndex = 0;
        breakRowElement = null;
        breakPagesCell.clear();
        breakRowView = null;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        breakRowElement = null;
        breakPagesCell.clear();
        breakRowView = null;
    }
    
    private boolean isRowBreakPages;
    // 
    private short rowIndex;
    // 跨页的row element
    private RowElement breakRowElement;
    //
    private RowView breakRowView;
    // 跨页的 cell element
    private Map<Integer, BreakPagesCell> breakPagesCell;
    //
    private TableAttr tableAttr = new TableAttr();
    //
    private Vector<CellView> mergedCell = new Vector<CellView>(); 
}
