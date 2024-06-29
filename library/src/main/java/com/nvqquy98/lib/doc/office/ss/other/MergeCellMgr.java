/*
 * 文件名称:          MergedCellMgr.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:51:22
 */
package com.nvqquy98.lib.doc.office.ss.other;

import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.PaneInformation;
import com.nvqquy98.lib.doc.office.ss.view.SheetView;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-8-20
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class MergeCellMgr
{
    /**
     * 
     * @param sheet
     * @param cr
     * @return
     */
    public CellRangeAddress getVisibleCellRangeAddress(Sheet sheet, CellRangeAddress cr)
    {
        int firstCol = cr.getFirstColumn();
        int lastCol = cr.getLastColumn();
        int firstRow = cr.getFirstRow();
        int lastRow = cr.getLastRow();
        
        
        //adjust visible merged cell range address
        while(firstCol <= lastCol && sheet.isColumnHidden(firstCol) )
        {
            firstCol ++;
        }
        
        while(lastCol >= firstCol && sheet.isColumnHidden(lastCol))
        {
            lastCol--;
        }
        
        while(firstRow <= lastRow && sheet.getRow(firstRow).isZeroHeight())
        {
            firstRow++;
        }
        while(lastRow >= firstRow && sheet.getRow(lastRow).isZeroHeight())
        {
            lastRow--;
        }
        
        cellRangeAddress.setFirstColumn(firstCol);
        cellRangeAddress.setFirstRow(firstRow);
        cellRangeAddress.setLastColumn(lastCol);
        cellRangeAddress.setLastRow(lastRow);
        
        return cellRangeAddress;
    }
    

    /**
     * 
     * @param cr
     * @param row
     * @param col
     * @return true:draw current cell borders  false:not draw
     */
    public boolean isDrawMergeCell(SheetView sheetView, CellRangeAddress cr, int row, int col)
    {
        boolean isDraw = false;
        int minColumn = sheetView.getMinRowAndColumnInformation().getMinColumnIndex();
        int minRow = sheetView.getMinRowAndColumnInformation().getMinRowIndex();
        PaneInformation paneInfo = sheetView.getCurrentSheet().getPaneInformation();
        //except hiden row and column
        getVisibleCellRangeAddress(sheetView.getCurrentSheet(), cr);
        
        if(paneInfo != null)
        {
            if(row < paneInfo.getHorizontalSplitTopRow() && cr.getLastRow() >= paneInfo.getHorizontalSplitTopRow())
            {
                cellRangeAddress.setLastRow(paneInfo.getHorizontalSplitTopRow() - 1);
                minRow = 0;
            }           
            else if(row >= paneInfo.getHorizontalSplitTopRow() && cr.getFirstRow() < paneInfo.getHorizontalSplitTopRow())
            {
                cellRangeAddress.setFirstRow(paneInfo.getHorizontalSplitTopRow());
            }
            
            if(col < paneInfo.getVerticalSplitLeftColumn() && cr.getLastColumn() >= paneInfo.getVerticalSplitLeftColumn())
            {
                cellRangeAddress.setLastColumn(paneInfo.getVerticalSplitLeftColumn() - 1);
                minColumn = 0;
            }            
            else if(col >= paneInfo.getVerticalSplitLeftColumn() && cr.getFirstColumn() < paneInfo.getVerticalSplitLeftColumn())
            {
                cellRangeAddress.setFirstColumn(paneInfo.getVerticalSplitLeftColumn());
            } 
        }
        
        // 首行首列，必须绘制
        if (cellRangeAddress.getFirstColumn() == col && cellRangeAddress.getFirstRow() == row)
        {
            isDraw = true;
        }
        // 首行且非首列，只当列号==显示的最小列号才需要绘制
        else if (row == cellRangeAddress.getFirstRow() && col > cellRangeAddress.getFirstColumn())
        {
            isDraw = col == minColumn;
        }
        // 首列且非首行，只当行号==显示的最小行号才需要绘制
        else if (col == cellRangeAddress.getFirstColumn() && row > cellRangeAddress.getFirstRow())
        {
            isDraw = row == minRow;
        }
        // 非首行首列，只当列号==显示的最小列号 and 行号==最小行号才需要绘制
        else if (row > cellRangeAddress.getFirstRow() && col > cellRangeAddress.getFirstColumn())
        {
            isDraw = col == minColumn && row == minRow;
        }
        return isDraw;
    }
    
    /**
     * 
     * @param sheetView
     * @param cellRangeAddress
     * @return
     */
    public MergeCell getMergedCellSize(SheetView sheetView, CellRangeAddress cellRangeAddress, int row, int col)
    {
        //MergedCellSize mergedCell = new MergedCellSize();
        mergedCell.reset();
        
        int minColumn = sheetView.getMinRowAndColumnInformation().getMinColumnIndex();
        int minRow = sheetView.getMinRowAndColumnInformation().getMinRowIndex();
        PaneInformation paneInfo = sheetView.getCurrentSheet().getPaneInformation();
        
        if(paneInfo == null)
        {
            for (int i = cellRangeAddress.getFirstColumn(); i <= cellRangeAddress.getLastColumn(); i++)
            {
                if(!sheetView.getCurrentSheet().isColumnHidden(i))
                {
                    float tW = (sheetView.getCurrentSheet().getColumnPixelWidth(i) * sheetView.getZoom());
                    mergedCell.setWidth(mergedCell.getWidth() + tW);
                    if (i < minColumn)
                    {
                        mergedCell.setNovisibleWidth(mergedCell.getNovisibleWidth() + tW);
                    }
                }            
            }   
            for (int i = cellRangeAddress.getFirstRow(); i <= cellRangeAddress.getLastRow(); i++)
            {
                if(!sheetView.getCurrentSheet().getRow(i).isZeroHeight())
                {
                    float tH = (sheetView.getCurrentSheet().getRow(i).getRowPixelHeight() * sheetView.getZoom());
                    mergedCell.setHeight(mergedCell.getHeight() + tH);
                    if (i < minRow)
                    {
                        mergedCell.setNoVisibleHeight(mergedCell.getNoVisibleHeight() + tH);
                    }
                }
            }  
        }
        else 
        {
            ///merged cell width
            if(col >= paneInfo.getVerticalSplitLeftColumn())
            {
                //free columns
                for (int i = cellRangeAddress.getFirstColumn(); i <= cellRangeAddress.getLastColumn(); i++)
                {
                    if(!sheetView.getCurrentSheet().isColumnHidden(i))
                    {
                        float tW = (sheetView.getCurrentSheet().getColumnPixelWidth(i) * sheetView.getZoom());
                        mergedCell.setWidth(mergedCell.getWidth() + tW);
                        if (i < minColumn)
                        {
                            mergedCell.setNovisibleWidth(mergedCell.getNovisibleWidth() + tW);
                        }
                    }            
                }  
            }
            else
            {
                //frozen columns
                mergedCell.setFrozenColumn(true);
                for (int i = cellRangeAddress.getFirstColumn(); i <= cellRangeAddress.getLastColumn(); i++)
                {
                    if(!sheetView.getCurrentSheet().isColumnHidden(i))
                    {
                        float tW = (sheetView.getCurrentSheet().getColumnPixelWidth(i) * sheetView.getZoom());
                        mergedCell.setWidth(mergedCell.getWidth() + tW);
                        if (i >= paneInfo.getVerticalSplitLeftColumn())
                        {
                            mergedCell.setNovisibleWidth(mergedCell.getNovisibleWidth() + tW);
                        }
                    }            
                }  
            }
            
            //////merged cell height
            if(row>= paneInfo.getHorizontalSplitTopRow())
            {
                //free rows
                for (int i = cellRangeAddress.getFirstRow(); i <= cellRangeAddress.getLastRow(); i++)
                {
                    if(!sheetView.getCurrentSheet().getRow(i).isZeroHeight())
                    {
                        float tH = (sheetView.getCurrentSheet().getRow(i).getRowPixelHeight() * sheetView.getZoom());
                        mergedCell.setHeight(mergedCell.getHeight() + tH);
                        if (i < minRow)
                        {
                            mergedCell.setNoVisibleHeight(mergedCell.getNoVisibleHeight() + tH);
                        }
                    }
                }  
            }
            else
            {
                //frozen rows
                mergedCell.setFrozenRow(true);
                for (int i = cellRangeAddress.getFirstRow(); i <= cellRangeAddress.getLastRow(); i++)
                {
                    if(!sheetView.getCurrentSheet().getRow(i).isZeroHeight())
                    {
                        float tH = (sheetView.getCurrentSheet().getRow(i).getRowPixelHeight() * sheetView.getZoom());
                        mergedCell.setHeight(mergedCell.getHeight() + tH);
                        if (i >= paneInfo.getHorizontalSplitTopRow())
                        {
                            mergedCell.setNoVisibleHeight(mergedCell.getNoVisibleHeight() + tH);
                        }
                    }
                }  
            }
        }
        
        return  mergedCell;
    }
    
    
    public void dispose()
    {
        if(cellRangeAddress != null)
        {
            cellRangeAddress.dispose();
            cellRangeAddress = null;
        }
        
        if(mergedCell != null)
        {
            mergedCell.dispose();
            mergedCell = null;
        }
        
    }
    
    //
    private CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 0);
    private MergeCell mergedCell = new MergeCell();
}
