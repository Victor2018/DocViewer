/*
 * 文件名称:          SheetView.java
 *
 * 编译器:            android2.2
 * 时间:              下午2:37:48
 */
package com.nvqquy98.lib.doc.office.ss.view;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.font.FontKit;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElementCollection;
import com.nvqquy98.lib.doc.office.simpletext.model.STDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.STRoot;
import com.nvqquy98.lib.doc.office.ss.control.Spreadsheet;
import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.ColumnInfo;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTable;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTableCellStyle;
import com.nvqquy98.lib.doc.office.ss.other.DrawingCell;
import com.nvqquy98.lib.doc.office.ss.other.ExpandedCellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.other.FindingMgr;
import com.nvqquy98.lib.doc.office.ss.other.FocusCell;
import com.nvqquy98.lib.doc.office.ss.other.SheetScroller;
import com.nvqquy98.lib.doc.office.ss.util.ModelUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 负责sheet表绘制和布局计算
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-8
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class SheetView {
    public final static int MAXROW_03 = 65536;
    public final static int MAXCOLUMN_03 = 256;

    public final static int MAXROW_07 = 1048576;
    public final static int MAXCOLUMN_07 = 16384;

    /**
     * @param sheet model中值
     */
    public SheetView(Spreadsheet spreadsheet, Sheet sheet) {
        this.spreadsheet = spreadsheet;
        this.sheet = sheet;
        rowHeader = new RowHeader(this);
        columnHeader = new ColumnHeader(this);
        shapeView = new ShapeView(this);
        tableFormatView = new TableFormatView(this);
        cellView = new CellView(this);
        selecetedCellsRange = new CellRangeAddress(0, 0, 0, 0);
        cellInfor = new DrawingCell();
        initForDrawing();
    }

    /**
     *
     */
    private void initForDrawing() {
        scrollX = sheet.getScrollX();
        scrollY = sheet.getScrollY();
        sheetScroller.update(sheet, Math.round(scrollX), Math.round(scrollY));
        setZoom(sheet.getZoom(), true);
        //selected cell
        selectedCell(sheet.getActiveCellRow(), sheet.getActiveCellColumn());
        spreadsheet.getControl().actionEvent(EventConstant.APP_CONTENT_SELECTED, cellInfor);
    }

    private void resizeCalloutView() {
        if (spreadsheet.getCalloutView() != null) {
            spreadsheet.getCalloutView().setZoom(zoom);
            int left = (int) (scrollX * zoom);
            int top = (int) (scrollY * zoom);
            spreadsheet.getCalloutView().layout(getRowHeaderWidth() - left,
                    getColumnHeaderHeight() - top,
                    spreadsheet.getCalloutView().getRight(),
                    spreadsheet.getCalloutView().getBottom());
            spreadsheet.getCalloutView().setClip(left, top);
        }
    }

    /**
     * 改变显示的sheet
     *
     * @param sheet 需要显示的sheet
     */
    public void changeSheet(Sheet sheet) {
        synchronized (this) {
            this.sheet.removeSTRoot();
            this.sheet = sheet;
            initForDrawing();
            resizeCalloutView();
            // to picture
            spreadsheet.post(new Runnable() {

                @Override
                public void run() {
                    spreadsheet.getControl().actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                }
            });
        }
    }

    /**
     * get sheet thumbnail
     *
     * @param sheet     sheet
     * @param width     thumbnail width when zoomValue is 100
     * @param height    thumbnail height when zoomValue is 100
     * @param zoomValue zoom value
     * @return
     */
    public Bitmap getThumbnail(Sheet sheet, int width, int height, float zoomValue) {
        synchronized (this) {
            Bitmap bitmap = Bitmap.createBitmap((int) (width * zoomValue), (int) (height * zoomValue), Config.ARGB_8888);
            if (bitmap == null) {
                return null;
            }
            boolean b = PictureKit.instance().isDrawPictrue();
            PictureKit.instance().setDrawPictrue(true);
            Canvas picCanvas = new Canvas(bitmap);
            picCanvas.drawColor(Color.WHITE);
            //save sheet(not current sheet) info
            int oldScrollX = sheet.getScrollX();
            int oldScrollY = sheet.getScrollY();
            float oldZoom = sheet.getZoom();
            Sheet oldSheet = this.sheet;
            //set specific sheet thumbnail info
            this.sheet = sheet;
            scrollX = 0;
            scrollY = 0;
            sheet.setScroll(0, 0);
            setZoom(zoomValue, true);
            sheetScroller.update(sheet, Math.round(scrollX), Math.round(scrollY));
            drawThumbnail(picCanvas);
            //restore specific sheet info
            sheet.setScroll(oldScrollX, oldScrollY);
            sheet.setZoom(oldZoom);
            //restore to current sheet
            this.sheet = oldSheet;
            scrollX = oldSheet.getScrollX();
            scrollY = oldSheet.getScrollY();
            setZoom(oldSheet.getZoom(), true);
            sheetScroller.update(sheet, Math.round(scrollX), Math.round(scrollY));
            PictureKit.instance().setDrawPictrue(b);
            return bitmap;
        }
    }

    private void drawThumbnail(Canvas canvas) {
        spreadsheet.startDrawing();
        clipRect = canvas.getClipBounds();
        int colRightBound = columnHeader.getColumnRightBound(canvas, zoom);
        int rowBottomBound = rowHeader.getRowBottomBound(canvas, zoom);
        int rightPos = clipRect.right + 10;
        if (colRightBound < clipRect.right) {
            rightPos = colRightBound;
        }
        int bottomPos = clipRect.bottom + 50;
        if (rowBottomBound < clipRect.bottom) {
            bottomPos = rowBottomBound;
        }
        //draw rowheader and columnheader
        rowHeader.draw(canvas, rightPos, zoom);
        columnHeader.draw(canvas, bottomPos, zoom);
        float rowWidth = rowHeader.getRowHeaderWidth();
        float columnHeight = columnHeader.getColumnHeaderHeight();
        canvas.save();
        canvas.clipRect(rowWidth, columnHeight, rightPos, bottomPos);
        //draw cell
        drawRows(canvas);
        //table format
        tableFormatView.draw(canvas);
        //draw shape(textbox, pict, chart)
        shapeView.draw(canvas);
        canvas.restore();
    }

    public int getMaxScrollY() {
        return Math.round(sheet.getMaxScrollY() * zoom);
    }

    public int getMaxScrollX() {
        return Math.round(sheet.getMaxScrollX() * zoom);
    }

    /**
     * @param canvas
     */
    public void drawSheet(Canvas canvas) {
        synchronized (this) {
            //reset drawing flag
            spreadsheet.startDrawing();
            clipRect = canvas.getClipBounds();
            int colRightBound = columnHeader.getColumnRightBound(canvas, zoom);
            int rowBottomBound = rowHeader.getRowBottomBound(canvas, zoom);
            int rightPos = clipRect.right + 10;
            if (colRightBound < clipRect.right) {
                rightPos = colRightBound;
            }
            int bottomPos = clipRect.bottom + 50;
            if (rowBottomBound < clipRect.bottom) {
                bottomPos = rowBottomBound;
            }
            //draw rowheader and columnheader
            rowHeader.draw(canvas, rightPos, zoom);
            columnHeader.draw(canvas, bottomPos, zoom);
            float rowWidth = rowHeader.getRowHeaderWidth();
            float columnHeight = columnHeader.getColumnHeaderHeight();
            canvas.save();
            canvas.clipRect(rowWidth, columnHeight, rightPos, bottomPos);
            //draw cell
            drawRows(canvas);
            //table format
            tableFormatView.draw(canvas);
            //draw active cell border
            drawActiveCellBorder(canvas);
            //if(!moving)
            {
                //draw shape(textbox, pict, chart)
                shapeView.draw(canvas);
            }
            //draw moving header line when changing header height or width
            drawMovingHeaderLine(canvas);
            canvas.restore();
        }
    }

    /**
     * @param canvas
     */
    private void drawActiveCellBorder(Canvas canvas) {
        RectF area = ModelUtil.instance().getCellAnchor(this,
                sheet.getActiveCellRow(), sheet.getActiveCellColumn());
        cellView.drawActiveCellBorder(canvas, area, sheet.getActiveCellType());
    }

    /**
     * draw moving header line when changing header height or width
     *
     * @param canvas
     */
    private void drawMovingHeaderLine(Canvas canvas) {
        if (isDrawMovingHeaderLine && selectedHeaderInfor != null) {
            Paint paint = PaintKit.instance().getPaint();
            //save paint property      
            int oldColor = paint.getColor();
            PathEffect oldPathEffect = paint.getPathEffect();
            Rect clipRect = canvas.getClipBounds();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            Path path = new Path();
            if (selectedHeaderInfor.getType() == FocusCell.ROWHEADER) {
                //Rect rect = ModelUtil.instance().getCellAnchor(this, selectedHeaderInfor.getRow(), 0);
                path.moveTo(0, selectedHeaderInfor.getRect().bottom);
                path.lineTo(clipRect.right, selectedHeaderInfor.getRect().bottom);
            } else if (selectedHeaderInfor.getType() == FocusCell.COLUMNHEADER) {
                path.moveTo(selectedHeaderInfor.getRect().right, 0);
                path.lineTo(selectedHeaderInfor.getRect().right, clipRect.bottom);
            }
            paint.setPathEffect(effects);
            canvas.drawPath(path, paint);
            //restore
            paint.setPathEffect(oldPathEffect);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(oldColor);
        }
    }

    /**
     * @param row
     * @param currentCol
     * @param textWidth
     * @return
     */
    private int getExtendTextRightBound(Row row, int currentCol, float textWidth) {
        Cell cell;
        int columnIndex = currentCol + 1;
        while (textWidth > 0) {
            cell = row.getCell(columnIndex, false);
            String content;
            if (cell == null ||
                    (cell.getRangeAddressIndex() < 0 &&
                            ((content = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), cell)) == null || content.length() == 0))) {
                textWidth -= sheet.getColumnPixelWidth(columnIndex) * zoom;
            } else {
                return columnIndex - 1;
            }
            columnIndex++;
        }
        return columnIndex - 1;
    }

    /**
     * @param row
     * @param currentCol
     * @param textWidth
     * @return
     */
    private int getExtendTextLeftBound(Row row, int currentCol, float textWidth) {
        int columnIndex = currentCol - 1;
        Cell cell;
        while (columnIndex >= 0 && textWidth > 0) {
            cell = row.getCell(columnIndex, false);
            String content;
            if (cell == null ||
                    (cell.getRangeAddressIndex() < 0 &&
                            ((content = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), cell)) == null || content.length() == 0))) {
                textWidth -= sheet.getColumnPixelWidth(columnIndex) * zoom;
            } else {
                return columnIndex + 1;
            }
            columnIndex--;
        }
        return columnIndex + 1;
    }

    public int getIndentWidth(int indent) {
        double fontSize = sheet.getWorkbook().getFont(0).getFontSize();
        return (int) Math.round(2 * fontSize * indent * MainConstant.POINT_TO_PIXEL);
    }

    public int getIndentWidthWithZoom(int indent) {
        double fontSize = sheet.getWorkbook().getFont(0).getFontSize();
        return (int) Math.round(2 * fontSize * indent * MainConstant.POINT_TO_PIXEL * zoom);
    }

    /**
     *
     */
    private void initRowExtendedRangeAddress(Row row) {
        float colsWidth;
        Collection<Cell> cells = row.cellCollection();
        for (Cell cell : cells) {
            colsWidth = sheet.getColumnPixelWidth(cell.getColNumber()) * zoom;
            //          
            if ((cell.getCellStyle() != null && cell.getCellStyle().isWrapText())                 //wrapText cell
                    || cell.getCellType() == Cell.CELL_TYPE_BOOLEAN     //not string cell
                    || cell.getCellNumericType() == Cell.CELL_TYPE_NUMERIC_SIMPLEDATE
                    || (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.getCellNumericType() != Cell.CELL_TYPE_NUMERIC_STRING)) {
                continue;
            }
            float restWidth = 0;
            CellStyle style = cell.getCellStyle();
            float indent = style != null ? getIndentWidth(style.getIndent()) : 0;
            if (CellView.isComplexText(cell)) {
                Rect rect = ModelUtil.instance().getCellAnchor(
                        sheet, cell.getRowNumber(), cell.getColNumber());
                SectionElement elem = (SectionElement) (cell.getSheet().getWorkbook().getSharedItem(cell.getStringCellValueIndex()));
                if (elem == null || elem.getEndOffset() - elem.getStartOffset() == 0) {
                    continue;
                }
                IElementCollection elemCollection = elem.getParaCollection();
                List<Integer> paraHorAlignList = new ArrayList<Integer>(elemCollection.size());
                for (int i = 0; i < elemCollection.size(); i++) {
                    paraHorAlignList.add(AttrManage.instance().getParaHorizontalAlign(elemCollection.getElementForIndex(i).getAttribute()));
                    AttrManage.instance().setParaHorizontalAlign(elemCollection.getElementForIndex(i).getAttribute(), WPAttrConstant.PARA_HOR_ALIGN_LEFT);
                }
                IAttributeSet attr = elem.getAttribute();
                // 宽度
                AttrManage.instance().setPageWidth(attr, Math.round(Integer.MAX_VALUE * MainConstant.PIXEL_TO_TWIPS));
                // 高度
                AttrManage.instance().setPageHeight(attr, Math.round(rect.height() * MainConstant.PIXEL_TO_TWIPS));
                IDocument doc = new STDocument();
                doc.appendSection(elem);
                STRoot root = new STRoot(spreadsheet.getEditor(), doc);
                root.setWrapLine(false);
                root.doLayout();
                //just one lineview
                IView paraView = root.getChildView();
                int lineWidth = paraView.getLayoutSpan(WPViewConstant.X_AXIS);
                root.dispose();
                // 宽度
                AttrManage.instance().setPageWidth(attr, Math.round((lineWidth + indent + SSConstant.SHEET_SPACETOBORDER * 2) * MainConstant.PIXEL_TO_TWIPS));
                root = new STRoot(spreadsheet.getEditor(), doc);
                root.doLayout();
//                view = root.getChildView();
//                if(view != null)
//                {
//                	switch (style.getHorizontalAlign())
//                    {
//                        case CellStyle.ALIGN_GENERAL:
//                        case CellStyle.ALIGN_LEFT:
//                        case CellStyle.ALIGN_FILL:
//                        case CellStyle.ALIGN_JUSTIFY:
//                        case CellStyle.ALIGN_CENTER_SELECTION:
//                        	view.setLeftIndent(Math.round(indent));                      
//                            break;                
//                        case CellStyle.ALIGN_RIGHT:
//                        	view.setRightIndent(Math.round(indent));
//                            break;
//                    }
//             	   
//                }
                for (int i = 0; i < elemCollection.size(); i++) {
                    AttrManage.instance().setParaHorizontalAlign(elemCollection.getElementForIndex(i).getAttribute(), paraHorAlignList.get(i));
                }
                restWidth = (int) ((lineWidth + indent + SSConstant.SHEET_SPACETOBORDER * 2) * zoom) - colsWidth;
                cell.setSTRoot(root);
            } else if (cell.getRangeAddressIndex() < 0) {
                //simple and not merged cell
                String content = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), cell);
                if (content == null || content.length() == 0) {
                    continue;
                }
                //save paint text size
                Paint paint = FontKit.instance().getCellPaint(cell, sheet.getWorkbook(), null);
                float textSize = paint.getTextSize();
                paint.setTextSize(textSize * zoom);
                restWidth = paint.measureText(content) + indent + SSConstant.SHEET_SPACETOBORDER - colsWidth;
                //restore paint style
                paint.setTextSize(textSize);
            }
            if (restWidth > 0 && cell.getRangeAddressIndex() < 0) {
                //ATOD   
                int right = cell.getColNumber();
                int left = cell.getColNumber();
                switch (style.getHorizontalAlign()) {
                    case CellStyle.ALIGN_GENERAL:
                    case CellStyle.ALIGN_LEFT:
                    case CellStyle.ALIGN_FILL:
                    case CellStyle.ALIGN_JUSTIFY:
                    case CellStyle.ALIGN_CENTER_SELECTION:
                        right = getExtendTextRightBound(row, cell.getColNumber(), restWidth);
                        break;
                    case CellStyle.ALIGN_RIGHT:
                        left = getExtendTextLeftBound(row, cell.getColNumber(), restWidth);
                        break;
                    case CellStyle.ALIGN_CENTER:
                        left = getExtendTextLeftBound(row, cell.getColNumber(), restWidth / 2);
                        right = getExtendTextRightBound(row, cell.getColNumber(), restWidth / 2);
                        break;
                    default:
                        break;
                }
                if (left == right) {
                    continue;
                }
                ExpandedCellRangeAddress rangeAddr = new ExpandedCellRangeAddress(cell, row.getRowNumber(), left, row.getRowNumber(), right);
                int rangeAddrIndex = row.getExpandedCellCount();
                row.addExpandedRangeAddress(rangeAddrIndex, rangeAddr);
            }
        }
        //validate the Extended Cell Range
        int cnt = row.getExpandedCellCount();
        for (int i = 0; i < cnt; i++) {
            ExpandedCellRangeAddress rangeAddr = row.getExpandedRangeAddress(i);
            for (int j = rangeAddr.getRangedAddress().getFirstColumn(); j <= rangeAddr.getRangedAddress().getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = new Cell(Cell.CELL_TYPE_BLANK);
                    cell.setColNumber(j);
                    cell.setRowNumber(row.getRowNumber());
                    cell.setSheet(sheet);
                    cell.setCellStyle(row.getRowStyle());
                    row.addCell(cell);
                }
                cell.setExpandedRangeAddressIndex(i);
            }
        }
    }

    /**
     *
     */
    private void drawCells(Canvas canvas, Row row) {
        float height = (row == null ? sheet.getDefaultRowHeight() : row.getRowPixelHeight());
        cellInfor.setHeight(height * zoom);
        if (cellInfor.getRowIndex() != sheetScroller.getMinRowIndex()
                || sheetScroller.isRowAllVisible()) {
            cellInfor.setVisibleHeight(cellInfor.getHeight());
        } else {
            cellInfor.setVisibleHeight((float) sheetScroller.getVisibleRowHeight() * zoom);
        }
        if (row == null && sheet.isAccomplished()) {
            row = sheet.getRowByColumnsStyle(cellInfor.getRowIndex());
        }
        if (row == null || (!sheet.isAccomplished() && !row.isCompleted())) {
            return;
        }
        //reset when at row begin
        cellInfor.setLeft(rowHeader.getRowHeaderWidth());
        cellInfor.setColumnIndex(sheetScroller.getMinColumnIndex());
        Iterator<ExtendCell> iter = extendCell.iterator();
        while (iter.hasNext()) {
            iter.next().dispose();
        }
        extendCell.clear();
        if (sheet.isAccomplished() && !row.isInitExpandedRangeAddress()) {
            initRowExtendedRangeAddress(row);
            row.setInitExpandedRangeAddress(true);
        }
        Rect clip = canvas.getClipBounds();
        int maxColumn = sheet.getWorkbook().getMaxColumn();
        ColumnInfo columnInfo;
        float colWidth;
        while (cellInfor.getLeft() <= clip.right && cellInfor.getColumnIndex() < maxColumn) {
            columnInfo = sheet.getColumnInfo(cellInfor.getColumnIndex());
            if (columnInfo != null && (columnInfo.isHidden() /*|| columnInfo.getColWidth() <= 2 * SSConstant.SHEET_SPACETOBORDER*/)) {
                cellInfor.increaseColumn();
                continue;
            }
            colWidth = (columnInfo != null ? columnInfo.getColWidth() : sheet.getDefaultColWidth());
            cellInfor.setWidth(colWidth * zoom);
            //
            if (cellInfor.getColumnIndex() != sheetScroller.getMinColumnIndex()
                    || sheetScroller.isColumnAllVisible()) {
                cellInfor.setVisibleWidth(cellInfor.getWidth());
            } else {
                cellInfor.setVisibleWidth((float) sheetScroller.getVisibleColumnWidth() * zoom);
            }
            cellView.draw(canvas, row.getCell(cellInfor.getColumnIndex()), cellInfor);
            cellInfor.increaseLeftWithVisibleWidth();
            cellInfor.increaseColumn();
        }
        //draw extend cell contents
        iter = extendCell.iterator();
        ExtendCell extendCell;
        Paint paint;
        while (iter.hasNext()) {
            extendCell = iter.next();
            Cell cell = extendCell.getCell();
            SSTable table = cell.getTableInfo();
            SSTableCellStyle tableCellStyle = null;
            if (table != null) {
                tableCellStyle = cellView.getTableCellStyle(table,
                        sheet.getWorkbook(),
                        cell.getRowNumber(), cell.getColNumber());
            }
            paint = FontKit.instance().getCellPaint(cell, getSpreadsheet().getWorkbook(), tableCellStyle);
            canvas.save();
            canvas.clipRect(extendCell.getRect());
            Object content = extendCell.getContent();
            if (content instanceof String) {
                //save paint text size
                float textSize = paint.getTextSize();
                paint.setTextSize(textSize * zoom);
                canvas.drawText((String) content, extendCell.getX(), extendCell.getY(), paint);
                //restore paint style
                paint.setTextSize(textSize);
            } else {
                ((STRoot) content).draw(canvas, (int) extendCell.getX(), (int) extendCell.getY(), zoom);
            }
            canvas.restore();
        }
    }

    /**
     * 绘制单元格式
     */
    private void drawRows(Canvas canvas) {
        Rect clip = canvas.getClipBounds();
        cellInfor.setTop(columnHeader.getColumnHeaderHeight());
        cellInfor.setRowIndex(sheetScroller.getMinRowIndex());
        // 逐行绘制
        int maxRow = sheet.getWorkbook().getMaxRow();
        while (!spreadsheet.isAbortDrawing() && cellInfor.getTop() <= clip.bottom && cellInfor.getRowIndex() < maxRow) {
            Row row = sheet.getRow(cellInfor.getRowIndex());
            if (row != null && row.isZeroHeight()) {
                cellInfor.increaseRow();
                continue;
            }
            drawCells(canvas, row);
            cellInfor.increaseTopWithVisibleHeight();
            cellInfor.increaseRow();
        }
    }

    /**
     * 得到当前显示sheet
     */
    public Sheet getCurrentSheet() {
        return sheet;
    }

    /**
     * 得到列标题高度
     */
    public int getColumnHeaderHeight() {
        return columnHeader.getColumnHeaderHeight();
    }

    /**
     * 得到行标题宽度
     */
    public int getRowHeaderWidth() {
        return getRowHeader().getRowHeaderWidth();
    }

    /**
     * @param x the amount of pixels to scroll by horizontally
     * @param y the amount of pixels to scroll by vertically
     */
    public void scrollBy(float x, float y) {
        synchronized (this) {
            scrollX += x / zoom;
            scrollX = Math.min(sheet.getMaxScrollX(), Math.max(0, scrollX));
            scrollY += y / zoom;
            scrollY = Math.min(sheet.getMaxScrollY(), Math.max(0, scrollY));
            sheet.setScroll(Math.round(scrollX), Math.round(scrollY));
            sheetScroller.update(sheet, Math.round(scrollX), Math.round(scrollY));
            resizeCalloutView();
        }
    }

    /**
     * @param x the amount of pixels to scroll by horizontally
     * @param y the amount of pixels to scroll by vertically
     */
    public void scrollTo(float x, float y) {
        synchronized (this) {
            scrollX = x;
            scrollX = Math.min(sheet.getMaxScrollX(), Math.max(0, scrollX));
            scrollY = y;
            scrollY = Math.min(sheet.getMaxScrollY(), Math.max(0, scrollY));
            sheet.setScroll(Math.round(scrollX), Math.round(scrollY));
            sheetScroller.update(sheet, Math.round(scrollX), Math.round(scrollY));
        }
    }

    /**
     * called when changed header size by SSEventManage
     */
    public void updateMinRowAndColumnInfo() {
        sheetScroller.update(sheet, Math.round(scrollX), Math.round(scrollY));
    }

    public SheetScroller getMinRowAndColumnInformation() {
        return sheetScroller;
    }

    public float getScrollX() {
        return scrollX;
    }

    public float getScrollY() {
        return scrollY;
    }

    /**
     * @return Returns the current zoom rate
     */
    public float getZoom() {
        return zoom;
    }

    /**
     * set current zoom rate
     *
     * @param zoomRate
     */
    public void setZoom(float zoom) {
        synchronized (this) {
            setZoom(zoom, false);
            resizeCalloutView();
        }
    }

    /**
     * @param zoom
     * @param isInit
     */
    public synchronized void setZoom(float zoom, boolean isInit) {
        int bottom = 0;
        boolean checkActiveCellVisible = false;
        if (this.zoom < zoom && !isInit) {
            bottom = clipRect.bottom - spreadsheet.getBottomBarHeight();
            switch (sheet.getActiveCellType()) {
                case Sheet.ACTIVECELL_ROW:
                    float yPostion = ModelUtil.instance().getValueY(this,
                            sheet.getActiveCellRow() + 1,
                            (float) sheetScroller.getVisibleRowHeight());
                    if (yPostion < bottom) {
                        checkActiveCellVisible = true;
                    }
                    break;
                case Sheet.ACTIVECELL_COLUMN:
                    float xPostion = ModelUtil.instance().getValueX(this,
                            sheet.getActiveCellColumn() + 1,
                            (float) sheetScroller.getVisibleColumnWidth());
                    if (xPostion < clipRect.right) {
                        checkActiveCellVisible = true;
                    }
                    break;
                case Sheet.ACTIVECELL_SINGLE:
                    RectF activeArea = ModelUtil.instance().getCellAnchor(this,
                            sheet.getActiveCellRow(),
                            sheet.getActiveCellColumn());
                    if (activeArea.width() > 1 && activeArea.height() > 1
                            && activeArea.intersect(clipRect.left, clipRect.top, clipRect.right, bottom)) {
                        checkActiveCellVisible = true;
                    }
                    break;
            }
        }
        this.zoom = zoom;
        sheet.setZoom(zoom);
        rowHeader.calculateRowHeaderWidth(zoom);
        columnHeader.calculateColumnHeaderHeight(zoom);
        if (checkActiveCellVisible && clipRect != null) {
            float bodyWidth = clipRect.right - rowHeader.getRowHeaderWidth();
            float bodyHeight = bottom - columnHeader.getColumnHeaderHeight();
            switch (sheet.getActiveCellType()) {
                case Sheet.ACTIVECELL_ROW:
                    float yPostion = ModelUtil.instance().getValueY(this, sheet.getActiveCellRow() + 1, Math.round(sheetScroller.getVisibleRowHeight()));
                    while (yPostion > bottom && Math.abs(yPostion - bottom) > 1) {
                        Row row = sheet.getRow(sheetScroller.getMinRowIndex());
                        float h = row == null ? getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight();
                        if (h * zoom > bodyHeight) {
                            //row height is larger than sheet body height
                            break;
                        }
                        scrollY += h;
                        sheetScroller.setMinRowIndex(sheetScroller.getMinRowIndex() + 1);
                        yPostion = ModelUtil.instance().getValueY(this, sheet.getActiveCellRow() + 1, Math.round(sheetScroller.getVisibleRowHeight()));
                    }
                    break;
                case Sheet.ACTIVECELL_COLUMN:
                    float xPostion = ModelUtil.instance().getValueX(this, sheet.getActiveCellColumn() + 1, Math.round(sheetScroller.getVisibleColumnWidth()));
                    while (xPostion > clipRect.right && Math.abs(xPostion - clipRect.right) > 1) {
                        float columnWidth = sheet.getColumnPixelWidth(sheetScroller.getMinColumnIndex());
                        if (columnWidth * zoom > bodyWidth) {
                            //column width is larger than sheet body width
                            break;
                        }
                        scrollX += columnWidth;
                        sheetScroller.setMinColumnIndex(sheetScroller.getMinColumnIndex() + 1);
                        xPostion = ModelUtil.instance().getValueX(this, sheet.getActiveCellColumn() + 1, Math.round(sheetScroller.getVisibleColumnWidth()));
                    }
                    break;
                case Sheet.ACTIVECELL_SINGLE:
                    RectF activeArea = ModelUtil.instance().getCellAnchor(this, sheet.getActiveCellRow(), sheet.getActiveCellColumn());
                    while (Math.abs(activeArea.right - rowHeader.getRowHeaderWidth()) < 1
                            || activeArea.right > clipRect.right
                            || Math.abs(activeArea.bottom - columnHeader.getColumnHeaderHeight()) < 1
                            || activeArea.bottom > bottom) {
                        if (Math.abs(activeArea.right - rowHeader.getRowHeaderWidth()) < 1) {
                            float columnWidth = sheet.getColumnPixelWidth(sheetScroller.getMinColumnIndex());
                            if (columnWidth * zoom > bodyWidth) {
                                //column width is larger than sheet body width
                                break;
                            }
                            scrollX -= columnWidth;
                            sheetScroller.setMinColumnIndex(sheetScroller.getMinColumnIndex() - 1);
                        } else if (activeArea.right > clipRect.right) {
                            float columnWidth = sheet.getColumnPixelWidth(sheetScroller.getMinColumnIndex());
                            if (columnWidth * zoom > bodyWidth) {
                                //column width is larger than sheet body width
                                break;
                            }
                            scrollX += columnWidth;
                            sheetScroller.setMinColumnIndex(sheetScroller.getMinColumnIndex() + 1);
                        }
                        if (Math.abs(activeArea.bottom - columnHeader.getColumnHeaderHeight()) < 1) {
                            Row row = sheet.getRow(sheetScroller.getMinRowIndex());
                            float h = row == null ? getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight();
                            if (h * zoom > bodyHeight) {
                                //row height is larger than sheet body height
                                break;
                            }
                            scrollY -= h;
                            sheetScroller.setMinRowIndex(sheetScroller.getMinRowIndex() - 1);
                        } else if (activeArea.bottom > bottom) {
                            Row row = sheet.getRow(sheetScroller.getMinRowIndex());
                            float h = row == null ? getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight();
                            if (h * zoom > bodyHeight) {
                                //row height is larger than sheet body height
                                break;
                            }
                            scrollY += h;
                            sheetScroller.setMinRowIndex(sheetScroller.getMinRowIndex() + 1);
                        }
                        activeArea = ModelUtil.instance().getCellAnchor(this, sheet.getActiveCellRow(), sheet.getActiveCellColumn());
                        boolean stop = false;
                        if (Math.abs(activeArea.left - rowHeader.getRowHeaderWidth()) < 1
                                && activeArea.right >= clipRect.right) {
                            stop = true;
                        } else if (activeArea.right != rowHeader.getRowHeaderWidth()
                                && Math.abs(activeArea.right - activeArea.left) < 1) {
                            sheetScroller.setMinColumnIndex(sheetScroller.getMinColumnIndex() - 1);
                            stop = true;
                        }
                        if (Math.abs(activeArea.top - columnHeader.getColumnHeaderHeight()) < 1
                                && activeArea.bottom >= clipRect.bottom) {
                            stop = true;
                        } else if (activeArea.bottom != columnHeader.getColumnHeaderHeight()
                                && activeArea.bottom < activeArea.top) {
                            sheetScroller.setMinRowIndex(sheetScroller.getMinRowIndex() - 1);
                            stop = true;
                        }
                        if (stop) {
                            return;
                        }
                    }
                    break;
            }
            sheet.setScroll((int) scrollX, (int) scrollY);
        }
    }

    public void setZoom(float zoom, float pointX, float pointY) {
        int viewWidth = spreadsheet.getWidth();
        int viewHeight = spreadsheet.getHeight();
        float normalizedX = (pointX - rowHeader.getRowHeaderWidth()) / this.zoom;
        float normalizedY = (pointY - columnHeader.getColumnHeaderHeight()) / this.zoom;
        //width and height to top-left corner of the whole sheet
        normalizedX = Math.min(sheet.getMaxScrollX(), normalizedX + sheet.getScrollX());
        normalizedY = Math.min(sheet.getMaxScrollY(), normalizedY + sheet.getScrollY());
        this.zoom = zoom;
        sheet.setZoom(zoom);
        rowHeader.calculateRowHeaderWidth(zoom);
        columnHeader.calculateColumnHeaderHeight(zoom);
        normalizedX = (normalizedX * zoom - viewWidth / 2) / zoom;
        normalizedY = (normalizedY * zoom - viewHeight / 2) / zoom;
        scrollTo((int) normalizedX, (int) normalizedY);
    }

    /**
     * @return Returns the spreadsheet.
     */
    public Spreadsheet getSpreadsheet() {
        return spreadsheet;
    }

    /**
     * @param spreadsheet The spreadsheet to set.
     */
    public void setSpreadsheet(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    /**
     * @return Returns the rowHeader.
     */
    public RowHeader getRowHeader() {
        return rowHeader;
    }

    /**
     * @return
     */
    public int getCurrentMinRow() {
        return sheetScroller.getMinRowIndex();
    }

    public int getCurrentMinColumn() {
        return sheetScroller.getMinColumnIndex();
    }

    public void selectedCell(int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row != null && row.getCell(colIndex) != null
                && row.getCell(colIndex).getRangeAddressIndex() >= 0) {
            CellRangeAddress cellRangeAddress = sheet.getMergeRange(row.getCell(colIndex).getRangeAddressIndex());
            selecetedCellsRange.setFirstRow(cellRangeAddress.getFirstRow());
            selecetedCellsRange.setLastRow(cellRangeAddress.getLastRow());
            selecetedCellsRange.setFirstColumn(cellRangeAddress.getFirstColumn());
            selecetedCellsRange.setLastColumn(cellRangeAddress.getLastColumn());
        } else {
            selecetedCellsRange.setFirstRow(rowIndex);
            selecetedCellsRange.setLastRow(rowIndex);
            selecetedCellsRange.setFirstColumn(colIndex);
            selecetedCellsRange.setLastColumn(colIndex);
        }
        //set to sheet
        getCurrentSheet().setActiveCellRowCol(selecetedCellsRange.getFirstRow(), selecetedCellsRange.getFirstColumn());
    }

    /**
     * set the flag which used to draw moving header dotted line
     *
     * @param draw
     */
    public void setDrawMovingHeaderLine(boolean draw) {
        isDrawMovingHeaderLine = draw;
    }

    /**
     * change current header information when changing header width or height
     *
     * @param headerInfor
     */
    public void changeHeaderArea(FocusCell headerInfor) {
        this.selectedHeaderInfor = headerInfor;
    }

    public void goToFindedCell(Cell cell) {
        if (cell == null) {
            return;
        }
        int col = cell.getColNumber();
        int row = cell.getRowNumber();
        if (cell.getColNumber() > 0) {
            col = cell.getColNumber() - 1;
        }
        if (cell.getRowNumber() > 0) {
            row = cell.getRowNumber() - 1;
        }
        sheet.setActiveCellRowCol(cell.getRowNumber(), cell.getColNumber());
        selectedCell(cell.getRowNumber(), cell.getColNumber());
        goToCell(row, col);
        spreadsheet.postInvalidate();
        //
        spreadsheet.getControl().actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);
        //
        spreadsheet.getControl().actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
    }

    public void goToCell(int row, int column) {
        Rect area = ModelUtil.instance().getCellAnchor(sheet, row, column, true);
        scrollTo(area.left, area.top);
        ;
    }

    /**
     * @param findValue
     * @return true: finded   false: not finded
     */
    public boolean find(String findValue) {
        if (findingMgr == null) {
            findingMgr = new FindingMgr();
        }
        Cell cell = findingMgr.findCell(sheet, findValue);
        if (cell != null) {
            goToFindedCell(cell);
            return true;
        } else {
            return false;
        }
    }

    public boolean findBackward() {
        if (findingMgr == null) {
            return false;
        }
        Cell cell = findingMgr.findBackward();
        if (cell != null) {
            goToFindedCell(cell);
            return true;
        }
        return false;
    }

    public boolean findForward() {
        if (findingMgr == null) {
            return false;
        }
        Cell cell = findingMgr.findForward();
        if (cell != null) {
            goToFindedCell(cell);
            return true;
        }
        return false;
    }

    /**
     * @param cell
     * @param x
     * @param y
     * @param content
     */
    public void addExtendCell(Cell cell, RectF rect, float x, float y, Object content) {
        extendCell.add(new ExtendCell(cell, rect, x, y, content));
    }

    public int getSheetIndex() {
        return sheet.getWorkbook().getSheetIndex(sheet) + 1;
    }

    public void dispose() {
        spreadsheet = null;
        sheet = null;
        if (rowHeader != null) {
            rowHeader.dispose();
            rowHeader = null;
        }
        if (columnHeader != null) {
            columnHeader.dispose();
            columnHeader = null;
        }
        if (cellView != null) {
            cellView.dispose();
            cellView = null;
        }
        if (shapeView != null) {
            shapeView.dispose();
            shapeView = null;
        }
        if (sheetScroller != null) {
            sheetScroller.dispose();
            sheetScroller = null;
        }
        if (cellInfor != null) {
            cellInfor.dispose();
            cellInfor = null;
        }
        if (findingMgr != null) {
            findingMgr.dispose();
            findingMgr = null;
        }
        if (extendCell != null) {
            extendCell.clear();
            extendCell = null;
        }
        selectedHeaderInfor = null;
        clipRect = null;
        effects = null;
    }

    // 当前显示Sheet
    private Sheet sheet;
    //
    private RowHeader rowHeader;
    //
    private ColumnHeader columnHeader;
    //
    private Spreadsheet spreadsheet;

    //current zoom rate
    private float zoom = 1F;

    //scroll postion in horizental and vertical based on zoom = 1
    private Rect clipRect;
    private float scrollX;
    private float scrollY;

    //
    private ShapeView shapeView;

    private TableFormatView tableFormatView;

    //
    private CellView cellView = null;

    //current min row and column information of current sheet
    private SheetScroller sheetScroller = new SheetScroller();

    //single cell information of current drawing
    private DrawingCell cellInfor;

    private CellRangeAddress selecetedCellsRange;

    //only draw it when changing header height or width
    private boolean isDrawMovingHeaderLine;

    //row header, column header
    private FocusCell selectedHeaderInfor;

    //used to draw dotted line
    private PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);

    //find data you're searching for
    private FindingMgr findingMgr;

    private List<ExtendCell> extendCell = new ArrayList<ExtendCell>();

    public class ExtendCell {
        public ExtendCell(Cell cell, RectF rect, float x, float y, Object content) {
            this.cell = cell;
            this.setRect(rect);
            this.x = x;
            this.y = y;
            if (content instanceof String) {
                this.content = ((String) content).intern();
            } else {
                this.content = content;
            }
        }

        public Cell getCell() {
            return cell;
        }

        /**
         * @return Returns the x.
         */
        public float getX() {
            return x;
        }

        /**
         * @return Returns the y.
         */
        public float getY() {
            return y;
        }

        public Object getContent() {
            return content;
        }

        /**
         * @return Returns the rect.
         */
        private RectF getRect() {
            return rect;
        }

        /**
         * @param rect The rect to set.
         */
        private void setRect(RectF rect) {
            this.rect = rect;
        }

        public void dispose() {
            cell = null;
            rect = null;
            content = null;
        }

        private Cell cell;
        private RectF rect;
        private float x;
        private float y;
        private Object content;
    }

    ;
}
