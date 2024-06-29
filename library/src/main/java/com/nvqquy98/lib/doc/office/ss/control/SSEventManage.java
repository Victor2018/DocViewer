/*
 * 文件名称:          SSEventManage.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:43:24
 */

package com.nvqquy98.lib.doc.office.ss.control;

import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.other.DrawingCell;
import com.nvqquy98.lib.doc.office.ss.other.FocusCell;
import com.nvqquy98.lib.doc.office.ss.view.SheetView;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.ITimerListener;
import com.nvqquy98.lib.doc.office.system.beans.AEventManage;
import com.nvqquy98.lib.doc.office.system.beans.ATimer;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * SS的事件管理，包括触摸、手型等事件
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-9
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class SSEventManage extends AEventManage  implements ITimerListener
{ 
    private static final int MINDISTANCE = 10;
    
    /**
     * 
     * @param spreadsheet
     */
    public SSEventManage(Spreadsheet spreadsheet, IControl control)
    {
        super(spreadsheet.getContext(), control);
        this.spreadsheet = spreadsheet;
        this.timer = new ATimer(1000, this);
    }
    
    /**
     * 定时器
     */
    public void actionPerformed()
    {
        timer.stop();
        control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
    }
    
    /**
     * 
     * @param event
     */
    private void changingHeader(MotionEvent event)
    {
        //changing head width or height
        if(newHeaderArea == null)
        {
            return;
        }
        
        scrolling = true;
        
        float x = event.getX();
        float y = event.getY();
        
        Rect area;
        switch(newHeaderArea.getType())
        {
            case FocusCell.ROWHEADER:
                area = newHeaderArea.getRect();                    
                area.bottom = Math.round(oldHeaderArea.getRect().bottom + (y - oldY));
                if(area.bottom <= area.top + MINDISTANCE)
                {
                    area.bottom = area.top + MINDISTANCE;
                }
                break;
            case FocusCell.COLUMNHEADER:
                area = newHeaderArea.getRect();                    
                area.right = Math.round(oldHeaderArea.getRect().right + (x - oldX));
                if(area.right <= area.left + MINDISTANCE)
                {
                    area.right = area.left + MINDISTANCE;
                }
                break;
            default:
                break;
        }                    
        
        spreadsheet.getSheetView().changeHeaderArea(newHeaderArea);
        
    }
    
    /**
     * 
     * @param event
     * @return
     */
    private boolean checkClickedCell(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        
        if(spreadsheet.getSheetView().getColumnHeaderHeight() > y
            || spreadsheet.getSheetView().getRowHeaderWidth() > x)
        {
            //not in body area
            return false;
        }
        
        SheetView sheetView = spreadsheet.getSheetView();
        DrawingCell cellInfor = new DrawingCell();
        
        cellInfor.setLeft(sheetView.getRowHeaderWidth());
        cellInfor.setTop(sheetView.getColumnHeaderHeight());
        cellInfor.setRowIndex(sheetView.getMinRowAndColumnInformation().getMinRowIndex());
        cellInfor.setColumnIndex(sheetView.getMinRowAndColumnInformation().getMinColumnIndex());  
        
        // check row by row
        int maxRows = sheetView.getCurrentSheet().getWorkbook().isBefore07Version() ? Workbook.MAXROW_03 : Workbook.MAXROW_07;
        while (cellInfor.getTop() <= y && cellInfor.getRowIndex() <= maxRows)
        {       
            Row row = sheetView.getCurrentSheet().getRow(cellInfor.getRowIndex());
            if(row != null && row.isZeroHeight())
            {
                cellInfor.setRowIndex(cellInfor.getRowIndex() + 1);
                continue;
            }            
            
            cellInfor.setHeight(row == null ? sheetView.getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight());
            cellInfor.setHeight(Math.round(cellInfor.getHeight() * sheetView.getZoom()));
            
            if(cellInfor.getRowIndex() == sheetView.getMinRowAndColumnInformation().getMinRowIndex() 
                && !sheetView.getMinRowAndColumnInformation().isRowAllVisible())
            {
                cellInfor.setVisibleHeight(
                    Math.round(
                        sheetView.getMinRowAndColumnInformation().getVisibleRowHeight() 
                            * sheetView.getZoom()));
            }
            else
            {
                cellInfor.setVisibleHeight(cellInfor.getHeight());
            }          

            
            cellInfor.setTop(cellInfor.getTop() + cellInfor.getVisibleHeight());
            cellInfor.setRowIndex(cellInfor.getRowIndex() + 1);
        }       
        
        //then check cell by cell        
        int maxColumns = sheetView.getCurrentSheet().getWorkbook().isBefore07Version() ? Workbook.MAXCOLUMN_03 : Workbook.MAXCOLUMN_07;
        while (cellInfor.getLeft() <= x && cellInfor.getColumnIndex() <= maxColumns)
        {
            if(sheetView.getCurrentSheet().isColumnHidden(cellInfor.getColumnIndex()))
            {
                cellInfor.setColumnIndex(cellInfor.getColumnIndex() + 1);
                continue;
            }
            cellInfor.setWidth(Math.round(sheetView.getCurrentSheet().getColumnPixelWidth(cellInfor.getColumnIndex()) * sheetView.getZoom()));
            //
                                 
            
            if(cellInfor.getColumnIndex() == sheetView.getMinRowAndColumnInformation().getMinColumnIndex()
                && !sheetView.getMinRowAndColumnInformation().isColumnAllVisible())
            {
                cellInfor.setVisibleWidth(Math.round(sheetView.getMinRowAndColumnInformation().getVisibleColumnWidth() * sheetView.getZoom()));
            }
            else
            {
                cellInfor.setVisibleWidth(cellInfor.getWidth());
                
            }
            
           
            //Rect rect = cellView.draw(canvas, row.getCell(cellInfor.getColumnIndex()), cellInfor);
            
            cellInfor.setLeft(cellInfor.getLeft() + cellInfor.getVisibleWidth());
            cellInfor.setColumnIndex(cellInfor.getColumnIndex() + 1);
        }
        
        spreadsheet.getSheetView().getCurrentSheet().setActiveCellType(Sheet.ACTIVECELL_SINGLE);
        spreadsheet.getSheetView().selectedCell(cellInfor.getRowIndex() - 1, cellInfor.getColumnIndex() - 1);
        
        //action to SSControl
        spreadsheet.getControl().actionEvent(EventConstant.APP_CONTENT_SELECTED, null);
        
        spreadsheet.abortDrawing();
        spreadsheet.postInvalidate();
        return true;
    }
    
    /**
     * 
     */
    private boolean changeHeaderEnd()
    {
        boolean ret = false;
        
        scrolling = false;
        
        if(oldHeaderArea != null)
        {
            float off;
            Sheet sheet = spreadsheet.getSheetView().getCurrentSheet();
            Row row;
            Cell cell;
            int index;
            switch(oldHeaderArea.getType())
            {
                case FocusCell.ROWHEADER:                    
                    ret = true;
                    index = newHeaderArea.getRow();
                    row = sheet.getRow(index);
                    if(row == null)
                    {
                        row = new Row(0);
                        row.setRowNumber(index);
                        row.setSheet(sheet);
                        sheet.addRow(row);
                    }
                    else
                    {
                        
                        while(sheet.getRow(index) != null && sheet.getRow(index).isZeroHeight())
                        {
                            index--;
                        }
                        row = sheet.getRow(index);
                        if(row == null)
                        {
                            row = new Row(0);
                            row.setRowNumber(index);
                            row.setSheet(sheet);
                            sheet.addRow(row);
                        }
                    }
                    
                    //if(row != null)
                    {
                        off = (newHeaderArea.getRect().bottom - newHeaderArea.getRect().top) - (float)(oldHeaderArea.getRect().bottom - oldHeaderArea.getRect().top);
                        
                        row.setRowPixelHeight(Math.round(row.getRowPixelHeight() + off / spreadsheet.getSheetView().getZoom()));
                        
                        //clear STRoot after the changed row
                        index = row.getRowNumber();
                        while(index <= sheet.getLastRowNum())
                        {
                            row = sheet.getRow(index++);
                            if(row == null)
                            {
                                continue;
                            }
                            for(int i = row.getFirstCol(); i <= row.getLastCol(); i++)
                            {
                                if((cell = row.getCell(i)) != null)
                                {
                                    if (cell.getRangeAddressIndex() >= 0)
                                    {
                                        //find the width and height of not show cell which include in merged cell 
                                        CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                                        Row tRow = sheet.getRow(cr.getFirstRow());
                                        cell = tRow.getCell(cr.getFirstColumn());
                                    }
                                    cell.removeSTRoot();
                                }
                            }
                            
                            row.setInitExpandedRangeAddress(false);
                        }
                    }                            
                    break;
                case FocusCell.COLUMNHEADER:
                    ret = true;
                    off = (newHeaderArea.getRect().right - newHeaderArea.getRect().left) - (oldHeaderArea.getRect().right - oldHeaderArea.getRect().left);
                    index = newHeaderArea.getColumn();
                    while(sheet.isColumnHidden(index))
                    {
                        index--;
                    }
                    int width = Math.round(sheet.getColumnPixelWidth(index) + off / spreadsheet.getSheetView().getZoom());
                    sheet.setColumnPixelWidth(index, width);
                    
                    //clear STRoot after the changed column
                    index = sheet.getFirstRowNum();
                    while(index <= sheet.getLastRowNum())
                    {
                        row = sheet.getRow(index++);
                        if(row == null)
                        {
                            continue;
                        }
                        
                        for(int i =  Math.max(row.getFirstCol(), oldHeaderArea.getColumn()); i <= row.getLastCol(); i++)
                        {
                            if((cell = row.getCell(i)) != null)
                            {
                                if (cell.getRangeAddressIndex() >= 0)
                                {
                                    //find the width and height of not show cell which include in merged cell 
                                    CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                                    Row tRow = sheet.getRow(cr.getFirstRow());
                                    cell = tRow.getCell(cr.getFirstColumn());
                                }
                                cell.removeSTRoot();
                            }                           
                        }
                        row.setInitExpandedRangeAddress(false);
                    }
                    
                    break;
                default:
                    break;
            }
            
            spreadsheet.getSheetView().updateMinRowAndColumnInfo();
            
            spreadsheet.getSheetView().setDrawMovingHeaderLine(false);
            
            oldHeaderArea = null;
            newHeaderArea = null;
        } 
        
        return ret;
    }
    
    /**
     * find clicked row header
     * @param event
     * @return
     */
    private int findClickedRowHeader(MotionEvent event)
    {
        float y = event.getY();        
        
        SheetView sheetView = spreadsheet.getSheetView();
        DrawingCell cellInfor = new DrawingCell();
        
        cellInfor.setTop(sheetView.getColumnHeaderHeight());
        cellInfor.setRowIndex(sheetView.getMinRowAndColumnInformation().getMinRowIndex());
        
        // chech by row
        int maxRows = sheetView.getCurrentSheet().getWorkbook().isBefore07Version() ? Workbook.MAXROW_03 : Workbook.MAXROW_07;
        while (cellInfor.getTop() <= y && cellInfor.getRowIndex() <= maxRows)
        {       
            Row row = sheetView.getCurrentSheet().getRow(cellInfor.getRowIndex());
            if(row != null && row.isZeroHeight())
            {
                cellInfor.setRowIndex(cellInfor.getRowIndex() + 1);
                continue;
            }            
            
            cellInfor.setHeight(row == null ? sheetView.getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight());
            cellInfor.setHeight(Math.round(cellInfor.getHeight() * sheetView.getZoom()));
            
            if(cellInfor.getRowIndex() == sheetView.getMinRowAndColumnInformation().getMinRowIndex() 
                && !sheetView.getMinRowAndColumnInformation().isRowAllVisible())
            {
                cellInfor.setVisibleHeight(
                    Math.round(
                        sheetView.getMinRowAndColumnInformation().getVisibleRowHeight() 
                            * sheetView.getZoom()));
            }
            else
            {
                cellInfor.setVisibleHeight(cellInfor.getHeight());
            }          

            
            cellInfor.setTop(cellInfor.getTop() + cellInfor.getVisibleHeight());
            cellInfor.setRowIndex(cellInfor.getRowIndex() + 1);
        }         
        
        
        return cellInfor.getRowIndex() - 1;
    }
    
    /**
     * find clicked column header
     * @param event
     * @return
     */
    private int findClickedColumnHeader(MotionEvent event)
    {
        float x = event.getX();
        
        SheetView sheetView = spreadsheet.getSheetView();
        DrawingCell cellInfor = new DrawingCell();
        
        cellInfor.setLeft(sheetView.getRowHeaderWidth());
        cellInfor.setColumnIndex(sheetView.getMinRowAndColumnInformation().getMinColumnIndex()); 
       
        //then check by cell        
        int maxColumns = sheetView.getCurrentSheet().getWorkbook().isBefore07Version() ? Workbook.MAXCOLUMN_03 : Workbook.MAXCOLUMN_07;
        while (cellInfor.getLeft() <= x && cellInfor.getColumnIndex() <= maxColumns)
        {
            if(sheetView.getCurrentSheet().isColumnHidden(cellInfor.getColumnIndex()))
            {
                cellInfor.setColumnIndex(cellInfor.getColumnIndex() + 1);
                continue;
            }
            cellInfor.setWidth(Math.round(sheetView.getCurrentSheet().getColumnPixelWidth(cellInfor.getColumnIndex()) * sheetView.getZoom()));
            //
                                 
            
            if(cellInfor.getColumnIndex() == sheetView.getMinRowAndColumnInformation().getMinColumnIndex()
                && !sheetView.getMinRowAndColumnInformation().isColumnAllVisible())
            {
                cellInfor.setVisibleWidth(Math.round(sheetView.getMinRowAndColumnInformation().getVisibleColumnWidth() * sheetView.getZoom()));
            }
            else
            {
                cellInfor.setVisibleWidth(cellInfor.getWidth());
                
            }
            
            cellInfor.setLeft(cellInfor.getLeft() + cellInfor.getVisibleWidth());
            cellInfor.setColumnIndex(cellInfor.getColumnIndex() + 1);
        }
        
        return cellInfor.getColumnIndex() - 1;
        
    }
    
    /**
     * check clicked header
     * @param event
     */
    private boolean checkClickedHeader(MotionEvent event)
    {
        boolean ret = false;
        float x = event.getX();
        float y = event.getY();
        
        SheetView sheetView = spreadsheet.getSheetView();
        if(sheetView.getRowHeaderWidth() > x 
            && sheetView.getColumnHeaderHeight() < y)
        {
            ret = true;
            sheetView.getCurrentSheet().setActiveCellType(Sheet.ACTIVECELL_ROW);
            sheetView.getCurrentSheet().setActiveCellRow(findClickedRowHeader(event));
            
        }
        else if(sheetView.getRowHeaderWidth() < x 
            && sheetView.getColumnHeaderHeight() > y)
        {
            ret = true;
            sheetView.getCurrentSheet().setActiveCellType(Sheet.ACTIVECELL_COLUMN);
            sheetView.getCurrentSheet().setActiveCellColumn(findClickedColumnHeader(event));
        } 

        spreadsheet.getControl().actionEvent(EventConstant.APP_CONTENT_SELECTED, null);
        
        return ret;
    }
    
    /**
     * 
     * @param event
     */
    private void actionUp(MotionEvent event)
    {     
        if(!actionDown)
        {
            return;
        }
        
        actionDown = false;
        
        boolean ret = false;
        //scrolling
        if(scrolling)
        {
            ret = changeHeaderEnd();
        }
        else if(!longPress && checkClickedCell(event))
        {
            ret = true;
        }   
        else
        {
            ret = checkClickedHeader(event);
        }
        
        longPress = false;
        
        if(ret)
        {
            if(!timer.isRunning())
            {
                timer.start();
            }
            else
            {
                timer.restart();
            }
        }
        
    }
    
    /**
     * 触摸事件
     *
     */
    public boolean onTouch(View v, MotionEvent event)
    {
        if(this.spreadsheet == null)
        {
            return false;
        }
        
        super.onTouch(v, event);
        int action = event.getAction();
        
        if(event.getPointerCount() == 2)
        {  
            scrolling = true;
            actionDown = false;
            if(newHeaderArea != null)
            {
                spreadsheet.getSheetView().setDrawMovingHeaderLine(false);
                oldHeaderArea = null;
                newHeaderArea = null;
            }
            return true;
        }
        
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                actionDown = true;
                break;            
                
            case MotionEvent.ACTION_MOVE:
                changingHeader(event);  
                spreadsheet.abortDrawing();
                spreadsheet.postInvalidate();
                break;
                
            case MotionEvent.ACTION_UP:
                actionUp(event);
                scrolling = false;
                actionDown = false;
                spreadsheet.postInvalidate();
                break;
                
            default:
                break;
        }
        return false;
    }
    
    /**
     * find the row whose height will be changed
     * @param event
     * @return
     */
    private void findChangingRowHeader(MotionEvent event)
    {
        float y = event.getY();        
        
        SheetView sheetView = spreadsheet.getSheetView();
        DrawingCell cellInfor = new DrawingCell();
        
        cellInfor.setTop(sheetView.getColumnHeaderHeight());
        cellInfor.setRowIndex(sheetView.getMinRowAndColumnInformation().getMinRowIndex());
        
        int oldTop = Math.round(cellInfor.getTop());
        Rect rect = new Rect();
        rect.top = rect.bottom = oldTop;
        // chech by row
        int maxRows = sheetView.getCurrentSheet().getWorkbook().isBefore07Version() ? Workbook.MAXROW_03 : Workbook.MAXROW_07;
        while (cellInfor.getTop() <= y && cellInfor.getRowIndex() <= maxRows)
        {       
            Row row = sheetView.getCurrentSheet().getRow(cellInfor.getRowIndex());
            if(row != null && row.isZeroHeight())
            {
                cellInfor.setRowIndex(cellInfor.getRowIndex() + 1);
                continue;
            }            
            
            cellInfor.setHeight(row == null ? sheetView.getCurrentSheet().getDefaultRowHeight() : row.getRowPixelHeight());
            cellInfor.setHeight(Math.round(cellInfor.getHeight() * sheetView.getZoom()));
            
            if(cellInfor.getRowIndex() == sheetView.getMinRowAndColumnInformation().getMinRowIndex() 
                && !sheetView.getMinRowAndColumnInformation().isRowAllVisible())
            {
                cellInfor.setVisibleHeight(
                    Math.round(
                        sheetView.getMinRowAndColumnInformation().getVisibleRowHeight() 
                            * sheetView.getZoom()));
            }
            else
            {
                cellInfor.setVisibleHeight(cellInfor.getHeight());
            }          

            rect.top = rect.bottom;
            rect.bottom = Math.round(cellInfor.getTop());
            
            oldTop = Math.round(cellInfor.getTop());
            cellInfor.setTop(cellInfor.getTop() + cellInfor.getVisibleHeight());
            cellInfor.setRowIndex(cellInfor.getRowIndex() + 1);
        }         
        
        
        if(oldHeaderArea == null)
        {                
            oldHeaderArea = new FocusCell();
        }
        oldHeaderArea.setType(FocusCell.ROWHEADER);
        
        int row = 0;
        if(y > (oldTop + cellInfor.getTop()) / 2)
        {            
            oldHeaderArea.setRow(cellInfor.getRowIndex() - 1); 
            rect.top = rect.bottom;
            rect.bottom = Math.round(cellInfor.getTop());
            oldHeaderArea.setRect(rect);
        }
        else
        {
            row =  cellInfor.getRowIndex() - 2;
            oldHeaderArea.setRow(row >= 0 ? row : 0); 
            oldHeaderArea.setRect(rect);
        }        
    }
    
    /**
     * find column header whose width will be changed
     * @param event
     * @return
     */
    private void findChangingColumnHeader(MotionEvent event)
    {
        float x = event.getX();
        
        SheetView sheetView = spreadsheet.getSheetView();
        DrawingCell cellInfor = new DrawingCell();
        
        cellInfor.setLeft(sheetView.getRowHeaderWidth());
        cellInfor.setColumnIndex(sheetView.getMinRowAndColumnInformation().getMinColumnIndex()); 
       
        int oldLeft = Math.round(cellInfor.getLeft());
        Rect rect = new Rect();
        rect.left = rect.right = Math.round(cellInfor.getLeft());
        //then check by cell        
        int maxColumns = sheetView.getCurrentSheet().getWorkbook().isBefore07Version() ? Workbook.MAXCOLUMN_03 : Workbook.MAXCOLUMN_07;
        while (cellInfor.getLeft() <= x && cellInfor.getColumnIndex() <= maxColumns)
        {
            if(sheetView.getCurrentSheet().isColumnHidden(cellInfor.getColumnIndex()))
            {
                cellInfor.setColumnIndex(cellInfor.getColumnIndex() + 1);
                continue;
            }
            cellInfor.setWidth(Math.round(sheetView.getCurrentSheet().getColumnPixelWidth(cellInfor.getColumnIndex()) * sheetView.getZoom()));
            //
                                 
            
            if(cellInfor.getColumnIndex() == sheetView.getMinRowAndColumnInformation().getMinColumnIndex()
                && !sheetView.getMinRowAndColumnInformation().isColumnAllVisible())
            {
                cellInfor.setVisibleWidth(Math.round(sheetView.getMinRowAndColumnInformation().getVisibleColumnWidth() * sheetView.getZoom()));
            }
            else
            {
                cellInfor.setVisibleWidth(cellInfor.getWidth());
                
            }
            
            rect.left = rect.right;
            rect.right = Math.round(cellInfor.getLeft());
            
            oldLeft = Math.round(cellInfor.getLeft());
            
            cellInfor.setLeft(cellInfor.getLeft() + cellInfor.getVisibleWidth());
            cellInfor.setColumnIndex(cellInfor.getColumnIndex() + 1);
        }
        
        

        if(oldHeaderArea == null)
        {                
            oldHeaderArea = new FocusCell();
        }
        oldHeaderArea.setType(FocusCell.COLUMNHEADER);
        
       
        if(x > (oldLeft + cellInfor.getLeft()) / 2)
        {            
            oldHeaderArea.setColumn(cellInfor.getColumnIndex() - 1); 
            rect.left = rect.right;
            rect.right = Math.round(cellInfor.getLeft());
            oldHeaderArea.setRect(rect);
        }
        else
        {
            int col = cellInfor.getColumnIndex() - 2;
            oldHeaderArea.setColumn(col >= 0 ? col : 0); 
            oldHeaderArea.setRect(rect);
        }
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see com.nvqquy98.lib.doc.office.system.beans.AEventManage#onLongPress(android.view.MotionEvent)
     *
     */
    public void onLongPress(MotionEvent e)
    {
        super.onLongPress(e);
        
        longPress = true;
        
        float x = e.getX();
        float y = e.getY();
        // Y方向滚动
        oldY = Math.round(y);
        oldX =Math.round(x);
        
        SheetView sheetView = spreadsheet.getSheetView();
        if(sheetView.getRowHeaderWidth() > x 
            && sheetView.getColumnHeaderHeight() < y)
        {
            findChangingRowHeader(e);            
        }
        else if(sheetView.getRowHeaderWidth() < x 
            && sheetView.getColumnHeaderHeight() > y)
        {
            findChangingColumnHeader(e);            
        } 
        
        if(oldHeaderArea != null)
        {
            newHeaderArea = oldHeaderArea.clone();
            
            spreadsheet.getSheetView().changeHeaderArea(newHeaderArea);
            spreadsheet.getSheetView().setDrawMovingHeaderLine(true);
            
            spreadsheet.abortDrawing();
            spreadsheet.postInvalidate();
        }
       
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see com.nvqquy98.lib.doc.office.system.beans.AEventManage#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
     *
     */
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        super.onScroll(e1, e2, distanceX, distanceY);
        SheetView sheetview = spreadsheet.getSheetView();
        // 向右
        boolean change = false;
        if ( Math.abs(distanceX) > 2)
        {
            change = true;
        }
        else
        {
            distanceX = 0;
        }
        
        if (Math.abs(distanceY) > 2)
        {
            change = true;
        }
        else
        {
            distanceY = 0;
        }
        if (change)
        {
            isScroll = true;
            scrolling = true;
            sheetview.getRowHeader().calculateRowHeaderWidth(sheetview.getZoom());
            sheetview.scrollBy(Math.round(distanceX), Math.round(distanceY));
            
            spreadsheet.abortDrawing();
            spreadsheet.postInvalidate();
        }
        return true;
    }

//    /**
//     * 
//     *
//     */
//    public boolean onDoubleTap(MotionEvent e)
//    {
//        boolean r = super.onDoubleTap(e);
//        float x = e.getX();
//        float y = e.getY();
//        
//        float zoom = spreadsheet.getZoom();
//        float fitzoom = 1.0f;//spreadsheet.getFitZoom();
//        if(Math.abs(zoom - fitzoom) < 0.01)
//        {
//        	spreadsheet.setZoom(fitzoom * 2, x, y);
//        }
//        else if(Math.abs(zoom - fitzoom * 2) < 0.01)
//        {
//        	spreadsheet.setZoom(fitzoom, x, y);
//        }
//        else
//        {
//        	spreadsheet.setZoom(fitzoom, x, y);
//        }
//        return r;
//    }
    
    /**
     * Fling the scroll view
     *
     * @param velocityX  X方向速率
     * @param velocityY  Y方向速率 
     */
    public void fling(int velocityX, int velocityY)
    {
        super.fling(velocityX, velocityY);
        float zoom = spreadsheet.getSheetView().getZoom();
        int scrollX = Math.round(spreadsheet.getSheetView().getScrollX() * zoom);
        int scrollY = Math.round(spreadsheet.getSheetView().getScrollY() * zoom);
        
        // Y方向滚动
        oldY = 0;
        oldX = 0;
        if (Math.abs(velocityY) > Math.abs(velocityX))
        {
            oldY = scrollY;
            mScroller.fling(scrollX, scrollY, 0, velocityY, 0, 0, 0, spreadsheet.getSheetView().getMaxScrollY());
        }
        // X方向流动
        else
        {
            oldX = scrollX;
            mScroller.fling(scrollX, scrollY, velocityX, 0, 0, spreadsheet.getSheetView().getMaxScrollX(), 0, 0);
        }
        
        spreadsheet.abortDrawing();
        spreadsheet.postInvalidate();
    }

    /**
     * 
     *
     */
    public void computeScroll()
    {
        super.computeScroll();
        if (mScroller.computeScrollOffset())
        {
            isFling = true;
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            // 如果x和y方向都没有发生变化，那就结束吧
            if (x == oldX && oldY == y)
            {
                mScroller.abortAnimation();
                spreadsheet.abortDrawing();
                spreadsheet.postInvalidate();
                return;
            }
            SheetView sheetview = spreadsheet.getSheetView();
            boolean isDraw = false;
            // 流动X方向
            if (x != oldX && oldY == 0)
            {
                if ( Math.abs(x - oldX) > 2)
                {
                    isDraw = true;
                }
                else
                {
                    oldX = x;
                }  
            }
            // 流动Y方向
            if (y != oldY && oldX == 0)
            {  
                if (Math.abs(oldY - y) > 2)
                {
                    isDraw = true;
                }
                else
                {
                    oldY = y;
                }
            }
            if (isDraw)
            {
                scrolling = true;
                sheetview.getRowHeader().calculateRowHeaderWidth(sheetview.getZoom());
                sheetview.scrollBy(Math.round(x - oldX), Math.round(y - oldY));
            }
            
            spreadsheet.abortDrawing();
            spreadsheet.postInvalidate();
            oldX = x;
            oldY = y;
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        spreadsheet = null;
        
        if(oldHeaderArea != null)
        {
            oldHeaderArea.dispose();
            oldHeaderArea = null;
        }
        
        if(newHeaderArea != null)
        {
            newHeaderArea.dispose();
            newHeaderArea = null;
        }
        
        if(timer != null)
        {
            timer.dispose();
            timer =  null;
        }
    }

    private boolean longPress;
    //
    private int oldX;
    //
    private int oldY;
    // Spreadsheet
    private Spreadsheet spreadsheet;
    
    //used by changing header
    private FocusCell oldHeaderArea;
    private FocusCell newHeaderArea;
    
    //
    private boolean actionDown;
    
    //scroll state， used to distinguish ACTION_UP state(end of scrolling or end of click)
    private boolean scrolling;
    //timer
    private ATimer timer;
}
