/*
 * 文件名称:          Sheet.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:15:38
 */
package com.nvqquy98.lib.doc.office.ss.model.baseModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.simpletext.view.STRoot;
import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.interfacePart.IReaderListener;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.ColumnInfo;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.PaneInformation;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTable;


/**
 * Sheet 表
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-16
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class Sheet
{
    /**
     * normal sheet
     */
    public final static short TYPE_WORKSHEET = 0;
    
    
    /**
     * chart sheet
     */
    public final static short TYPE_CHARTSHEET = 1;
    
    
    /**
     * Used for compile-time optimization.  This is the initial size for the collection of
     * rows.  It is currently set to 20.  If you generate larger sheets you may benefit
     * by setting this to a higher number and recompiling a custom edition of Sheet.
     */
    public final static int INITIAL_CAPACITY = 20;
    
    //current active cell is single
    public final static short ACTIVECELL_SINGLE = 0;
    
    //current active cells are all cells of a row
    public final static short ACTIVECELL_ROW = 1;
    
    //current active cells are all cells of a column
    public final static short ACTIVECELL_COLUMN = 2;
    
    /**
     * not initialize
     */
    public final static short State_NotAccomplished = 0;
    
    /**
     * 
     */
    public final static short State_Reading = 1;
    
    /**
     * initialized
     */
    public final static short State_Accomplished = 2;
    
    
    /**
     * 
     */
    public Sheet()
    {
        activeCellType = ACTIVECELL_SINGLE;
        
       rows = new HashMap<Integer, Row>(); 
       merges = new ArrayList<CellRangeAddress>();
       
       maxScrollX = Integer.MAX_VALUE;
       maxScrollY = Integer.MAX_VALUE;
       
       shapesList = new ArrayList<IShape>();
    }
    
    /**
     * 
     * @param book
     */
    public void setWorkbook(Workbook book)
    {
        this.book = book;
    }
    
    /**
     * 
     * @return
     */
    public Workbook getWorkbook()
    {
        return book;
    }
    
    /**
     * add a row to the sheet
     *
     */
    public void addRow(Row row)
    {
        if(row == null)
        {
            return;
        }
        
        rows.put(Integer.valueOf(row.getRowNumber()), row);
        if(rows.size() == 1)
        {
            firstRow = row.getRowNumber();
            lastRow = row.getRowNumber();
        }
        else
        {
            firstRow = Math.min(firstRow, row.getRowNumber());
            lastRow = Math.max(lastRow, row.getRowNumber());
        }
    }
    
    /**
     * 
     * @param range
     * @return current size of list
     */
    public int addMergeRange(CellRangeAddress range)
    {
        merges.add(range);
        return merges.size();
    }
    
    /**
     * get merge range count of this sheet
     */
    public int getMergeRangeCount()
    {
        return merges.size();
    }
    
    /**
     * get merge range for index 
     */
    public CellRangeAddress getMergeRange(int index)
    {
        if (index < 0 || index >= merges.size())
        {
            return null;
        }
        return merges.get(index);
    }
    

    /**
     * Returns the logical row (not physical) 0-based.  If you ask for a row that is not
     * defined you get a null.  This is to say row 4 represents the fifth row on a sheet.
     * @param rowIndex  row to get
     * @return Row representing the row number or null if its not defined on the sheet
     */
    public Row getRow(int rowIndex)
    {
        return rows.get(Integer.valueOf(rowIndex));        
    }
    
    /**
     * 
     * @param rowIndex
     * @return
     */
    public Row getRowByColumnsStyle(int rowIndex)
    {
        Row row = rows.get(Integer.valueOf(rowIndex));
        if(row != null)
        {
            return row;
        }
        
        if(columnInfoList == null || columnInfoList.size() == 0)
        {
            return null;
        }
        
        ColumnInfo columnInfo;
        int index = 0;
        while(index < columnInfoList.size())
        {
            columnInfo = columnInfoList.get(index++);
            CellStyle cellStyle = book.getCellStyle(columnInfo.getStyle());
            if(cellStyle != null 
                && ((cellStyle.getFillPatternType() == BackgroundAndFill.FILL_SOLID && (cellStyle.getFgColor() & 0xFFFFFF) != 0xFFFFFF)
                    || cellStyle.getBorderLeft() > 0
                    || cellStyle.getBorderTop() > 0
                    || cellStyle.getBorderRight() > 0
                    || cellStyle.getBorderBottom() > 0))
            {
                row = new Row(1);        
                row.setRowNumber(rowIndex);
                row.setRowPixelHeight(defaultRowHeight);
                row.setSheet(this);
                row.completed();
                
                rows.put(rowIndex, row);
                
                return row;                
            }            
        }
        
        return null;
    }
    
    /**
     * Returns the number of physically defined rows (NOT the number of rows in the sheet)
     */
    public int getPhysicalNumberOfRows()
    {
        return rows.size();
    }

    
    /**
     * @return Returns the sheetName.
     */
    public String getSheetName()
    {
        return sheetName;
    }

    /**
     * @param sheetName The sheetName to set.
     */
    public void setSheetName(String sheetName)
    {
        this.sheetName = sheetName;
    }

    /**
     * @return Returns the zoom.
     */
    public float getZoom()
    {
        return zoom;
    }

    /**
     * @param zoom The zoom to set.
     */
    public void setZoom(float zoom)
    {
        this.zoom = zoom;
    }

    /**
     * 
     */
    public float getMaxScrollX()
    {
        return maxScrollX;
    }
    
    /**
     * 
     * @return
     */
    public float getMaxScrollY()
    {
        return maxScrollY;
    }
    
    /**
     * @return Returns the scrollX.
     */
    public int getScrollX()
    {
        return scrollX;
    }

    /**
     * @param scrollX The scrollX to set.
     */
    public void setScrollX(int scrollX)
    {
        this.scrollX = scrollX;
    }

    /**
     * @return Returns the scrollY.
     */
    public int getScrollY()
    {
        return scrollY;
    }

    /**
     * @param scrollY The scrollY to set.
     */
    public void setScrollY(int scrollY)
    {
        this.scrollY = scrollY;
    }
    
    /**
     * 
     * @param scrollX
     * @param scrollY
     */
    public void setScroll(int scrollX, int scrollY)
    {
        this.scrollX = scrollX;
        this.scrollY = scrollY;
    } 
    
    /**
     * @return Returns the firstRow.
     */
    public int getFirstRowNum()
    {
        return firstRow;
    }

    /**
     * @param firstRow The firstRow to set.
     */
    public void setFirstRowNum(int firstRow)
    {
        this.firstRow = firstRow;
    }

    /**
     * @return Returns the lastRow.
     */
    public int getLastRowNum()
    {
        return lastRow;
    }

    /**
     * @param lastRow The lastRow to set.
     */
    public void setLastRowNum(int lastRow)
    {
        this.lastRow = lastRow;
    }
    
    /**
     * 
     * @param column
     * @param style
     */
    public void addColumnInfo(ColumnInfo columnInfo)
    {
        if(columnInfoList == null)
        {
            columnInfoList = new ArrayList<ColumnInfo>();
        }
        columnInfoList.add(columnInfo);
    }
    
    /**
     * Returns the CellStyle index that applies to the given
     *  (0 based) column, or 0 if no style has been
     *  set for that column
     */
    public int getColumnStyle(int column)
    {         
        if(columnInfoList != null)
        {
            ColumnInfo columnInfo;
            int index = 0;
            while(index < columnInfoList.size())
            {
                columnInfo = columnInfoList.get(index++);
                if(columnInfo.getFirstCol() <= column && columnInfo.getLastCol() >=column)
                {
                    return columnInfo.getStyle();
                }     
            }
                     
        }
        return 0;
    }
    
    public void setColumnPixelWidth(int column, int width)
    {
        if(columnInfoList != null)
        {
            ColumnInfo columnInfo;
            int index = 0;
            while(index < columnInfoList.size())
            {
                columnInfo = columnInfoList.get(index++);
                if(columnInfo.getFirstCol() == column && columnInfo.getLastCol() == column)
                {
                    columnInfo.setColWidth(width);
                    return;
                }
                else if(columnInfo.getFirstCol() == column)
                {
                    ColumnInfo columnInfo3 = 
                        new ColumnInfo(column + 1, columnInfo.getLastCol(), columnInfo.getColWidth(), columnInfo.getStyle(), columnInfo.isHidden());
                    
                    columnInfo.setColWidth(width);
                    columnInfo.setLastCol(column);
                    
                    columnInfoList.add(columnInfo3);
                    return;
                }
                else if(columnInfo.getLastCol() == column)
                {
                    ColumnInfo columnInfo1 = 
                        new ColumnInfo(columnInfo.getFirstCol(), column - 1, columnInfo.getColWidth(), columnInfo.getStyle(), columnInfo.isHidden());
                    
                    columnInfo.setColWidth(width);
                    columnInfo.setFirstCol(column);
                    
                    columnInfoList.add(columnInfo1);
                    return;
                }
                else if(columnInfo.getFirstCol() < column && columnInfo.getLastCol() > column)
                {
                    ColumnInfo columnInfo1 = 
                        new ColumnInfo(columnInfo.getFirstCol(), column - 1, columnInfo.getColWidth(), columnInfo.getStyle(), columnInfo.isHidden());
                    
                    
                    ColumnInfo columnInfo2 = 
                        new ColumnInfo(column + 1, columnInfo.getLastCol(), columnInfo.getColWidth(), columnInfo.getStyle(), columnInfo.isHidden());
                    
                    columnInfo.setFirstCol(column);
                    columnInfo.setLastCol(column);
                    columnInfo.setColWidth(width);
                    
                    columnInfoList.add(columnInfo1);
                    columnInfoList.add(columnInfo2);
                    return;
                }    
            }
            
            columnInfoList.add(new ColumnInfo(column, column, width, 0, false));
            
            
        }
        else
        {
            columnInfoList = new ArrayList<ColumnInfo>();
            
            columnInfoList.add(new ColumnInfo(column, column, width, 0, false));
        }
    }
    
    /**
     * @return Returns the columnPixelWidth.
     */
    public float getColumnPixelWidth(int column)
    {
        if(columnInfoList != null)
        {
            ColumnInfo columnInfo;
            int index = 0;
            while(index < columnInfoList.size())
            {
                columnInfo = columnInfoList.get(index++);
                if(columnInfo.getFirstCol() <= column && columnInfo.getLastCol() >=column)
                {
                    return columnInfo.getColWidth();
                }     
            }
        }
        
        return defaultColWidth;        
    }

    public ColumnInfo getColumnInfo(int column)
    {
        if(columnInfoList != null)
        {
            ColumnInfo columnInfo;
            int index = 0;
            while(index < columnInfoList.size())
            {
                columnInfo = columnInfoList.get(index++);
                if(columnInfo.getFirstCol() <= column && columnInfo.getLastCol() >=column)
                {
                    return columnInfo;
                }     
            }
        }
        
        return null;
    }
    
    /**
     * @return Returns the isGridsPrinted.
     */
    public boolean isGridsPrinted()
    {
        return isGridsPrinted;
    }

    /**
     * @param isGridsPrinted The isGridsPrinted to set.
     */
    public void setGridsPrinted(boolean isGridsPrinted)
    {
        this.isGridsPrinted = isGridsPrinted;
    }

    /**
     * @return Returns the paneInformation.
     */
    public PaneInformation getPaneInformation()
    {
        return null/*paneInformation*/;
    }

    /**
     * @param paneInformation The paneInformation to set.
     */
    public void setPaneInformation(PaneInformation paneInformation)
    {
        this.paneInformation = paneInformation;
    }

    /**
     * @return Returns the ColumnHidden.
     */
    public boolean isColumnHidden(int column)
    {
        if(columnInfoList != null)
        {
            ColumnInfo columnInfo;
            int index = 0;
            while(index < columnInfoList.size())
            {
                columnInfo = columnInfoList.get(index++);
                if(columnInfo.getFirstCol() <= column && columnInfo.getLastCol() >=column)
                {
                    return columnInfo.isHidden();
                }     
            }       
        }
        return false;
    }

    /**
     * @param isColumnHidden The ColumnHidden to set.
     */
    public void setColumnHidden(int columnNumber, boolean isColumnHidden)
    {
        
    }
    
    /**
     * ACTIVECELL_SINGLE 
     * ACTIVECELL_ROW
     * ACTIVECELL_COLUMN;
     * @param type
     */
    public void setActiveCellType(short type)
    {
        this.activeCellType = type;
    }
    
    public short getActiveCellType()
    {
        return activeCellType;
    }
    
    /**
     * 
     */
    private void checkActiveRowAndColumnBounds()
    {
    	if(book.isBefore07Version())
    	{
    		//03 and before version
    		activeCellRow = Math.min(activeCellRow, Workbook.MAXROW_03 - 1);
    		activeCellColumn = Math.min(activeCellColumn, Workbook.MAXCOLUMN_03 - 1);
    	}
    	else
    	{
    		//07,10 and later version
    		activeCellRow = Math.min(activeCellRow, Workbook.MAXROW_07 - 1);
    		activeCellColumn = Math.min(activeCellColumn, Workbook.MAXCOLUMN_07 - 1);
    	}
    }
    
    public void setActiveCellRow(int activeCellRow)
    {
        this.activeCellRow = activeCellRow;
        checkActiveRowAndColumnBounds();
    }
    
    /**
     * @return Returns the activeCellRow.
     */
    public int getActiveCellRow()
    {
        return activeCellRow;
    }    

    public void setActiveCellColumn(int activeCellColumn)
    {
        this.activeCellColumn = activeCellColumn;
        checkActiveRowAndColumnBounds();
    }
    
    /**
     * @return Returns the activeCellColumn.
     */
    public int getActiveCellColumn()
    {
        return activeCellColumn;
    }
    
    /**
     * 
     */
    public void setActiveCellRowCol(int row, int col)
    {
        activeCellType = ACTIVECELL_SINGLE;
        activeCellRow = row;
        activeCellColumn = col;        
        checkActiveRowAndColumnBounds();
        
        CellRangeAddress cellRangeAddress;
        int index = 0;
        while(index < merges.size())
        {
            cellRangeAddress = merges.get(index++);
            if(cellRangeAddress.isInRange(row, col))
            {
                activeCellRow = cellRangeAddress.getFirstRow();
                activeCellColumn = cellRangeAddress.getFirstColumn();
            }
        }
        
        
        if(getRow(row) != null)
        {
            activeCell = getRow(row).getCell(col);
        }
        else
        {
            activeCell = null;
        }
        
    }

    /**
     * @return Returns the activeCell.
     */
    public Cell getActiveCell()
    {
        return activeCell;
    }

    /**
     * @param activeCell The activeCell to set.
     */
    public void setActiveCell(Cell activeCell)
    {
        this.activeCell = activeCell;
        if(activeCell != null)
        {
            activeCellRow = activeCell.getRowNumber();
            activeCellColumn = activeCell.getColNumber();
        }
        else
        {
            activeCellRow = -1;
            activeCellColumn = -1;
        }
    }

    /**
     * append shape of this sheet
     */
    public void appendShapes(IShape shape)
    {
        this.shapesList.add(shape);
    }
    
    /**
     * get all shapes of this sheet
     */
    public IShape[] getShapes()
    {
        return shapesList.toArray(new IShape[shapesList.size()]);
    }
    
    /**
     * get shape count of this sheet
     */
    public int getShapeCount()
    {
        return shapesList.size();
    }
    
    /**
     * get shape with index
     */
    public IShape getShape(int index)
    {
        if (index < 0 || index >= shapesList.size())
        {
            return null;
        }
        return shapesList.get(index);
    }
    
    /**
     * 
     * @param defaultRowHeight
     */
    public void setDefaultRowHeight(int defaultRowHeight)
    {
        this.defaultRowHeight = defaultRowHeight;
    }
    
    /**
     * 
     * @return
     */
    public int getDefaultRowHeight()
    {
        return defaultRowHeight;
    }
    
    /**
     * 
     * @param defaultColWidth
     */
    public void setDefaultColWidth(int defaultColWidth)
    {
        this.defaultColWidth = defaultColWidth;
    }
    
    /**
     * 
     * @return
     */
    public int getDefaultColWidth()
    {
        return defaultColWidth;
    }
    
    /**
     * TYPE_WORKSHEET 
     * TYPE_CHARTSHEET
     * @param type
     */
    public void setSheetType(short type)
    {
        this.type = type;
    }
    
    /**
     * TYPE_WORKSHEET 
     * TYPE_CHARTSHEET
     * @return
     */
    public short getSheetType()
    {
        return type;
    }
    /**
     * 
     * @param state
     */
    public void setState(short state)
    {
        this.state = state;
        if(state == State_Accomplished && iReaderListener !=  null)
        {
            iReaderListener.OnReadingFinished();
        }
        
        maxScrollX = 0;
        maxScrollY = 0;
        
        int columnsCnt = 0;
        if(columnInfoList != null)
        {
            Iterator<ColumnInfo> iter = columnInfoList.iterator();
            ColumnInfo info;
            while(iter.hasNext())
            {
                info = iter.next();
                columnsCnt += info.getLastCol() - info.getFirstCol() + 1;
                if(info.isHidden())
                {
                    continue;
                }
                maxScrollX += info.getColWidth() * (info.getLastCol() - info.getFirstCol() + 1);
            }
        }                
        
        int rowCnt = rows.size();
        Iterator<Row> iter = rows.values().iterator();
        while(iter.hasNext())
        {
        	maxScrollY += iter.next().getRowPixelHeight();
        }
        
        if(!book.isBefore07Version())
        {
            //version after 2007
            maxScrollX += (Workbook.MAXCOLUMN_07 - columnsCnt) * defaultColWidth;
            maxScrollY += (Workbook.MAXROW_07 - rowCnt) * defaultRowHeight;
        }
        else 
        {
            //version before 2007 (97/2000/XP/2003)
            maxScrollX += (Workbook.MAXCOLUMN_03 - columnsCnt) * defaultColWidth;
            maxScrollY += (Workbook.MAXROW_03 - rowCnt) * defaultRowHeight;
        }
        
    }
    
    /**
     * 
     * @return
     */
    public synchronized short getState()
    {
        return state;
    }
    
    /**
     * 
     * @return
     */
    public boolean isAccomplished()
    {
        return state == State_Accomplished;
    }
    

    /**
     * send notifications to caller
     * @param iReaderListener
     */
    public void setReaderListener(IReaderListener iReaderListener)
    {
        this.iReaderListener = iReaderListener;
    }
    
    /**
     * 
     * @param root
     * @return root position
     */
    public int addSTRoot(STRoot root)
    {
        if(rootViewMap == null)
        {
            rootViewMap = new ArrayList<STRoot>();
        }
        int id = rootViewMap.size();
        rootViewMap.add(id, root);
        
        return id;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public STRoot getSTRoot(int id)
    {
        if(id < 0 || id >= rootViewMap.size())
        {
            return null;
        }
        return rootViewMap.get(id);
    }
    
    /**
     * called when changed sheet
     */
    public void removeSTRoot()
    {
        if(rootViewMap != null)
        {
            int cnt = rootViewMap.size();
            int index = 0;
            while(index < cnt)
            {
                STRoot root = rootViewMap.get(index++);
                if (root != null)
                {
                    root.dispose();
                }
            }
            rootViewMap.clear();
        }
        
        int rowIndex = firstRow;
        while(rowIndex <= lastRow)
        {       
            Row row = getRow(rowIndex++);
            if(row == null || (row != null && row.isZeroHeight()))
            {
                continue;
            } 
            row.setInitExpandedRangeAddress(false);
            
            Iterator<Cell> iter = row.cellCollection().iterator();
            while(iter.hasNext())
            {
                iter.next().removeSTRoot();
            }
        }
    }
    
    /**
     * 
     * @param table
     */
    public void addTable(SSTable table)
    {
        if(tableList == null)
        {
            tableList = new ArrayList<SSTable>();
        }
        
        tableList.add(table);
    }
    
    /**
     * 
     * @return
     */
    public SSTable[] getTables()
    {
        if(tableList != null)
        {            
            return tableList.toArray(new SSTable[tableList.size()]);
        }
        
        return null;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        book = null;        
        sheetName = null;
        paneInformation = null;

        iReaderListener = null; 
        
        if(activeCell != null)
        {
            activeCell.dispose();
            activeCell = null;
        }
        
        if(rows != null)
        {           
            Collection<Row> rowCollection = rows.values();
            for(Row row: rowCollection)
            {
                row.dispose();
            }
            rows.clear();
            rows = null;
        }
        
        if(merges != null)
        {
            Iterator<CellRangeAddress> iter = merges.iterator();
            while(iter.hasNext())
            {
                (iter.next()).dispose();                
            }
            
            merges.clear();
            merges = null;
        }
        
        if(columnInfoList != null)
        {
            columnInfoList.clear();
            columnInfoList = null;
        }
        
        if(shapesList != null)
        {            
            Iterator<IShape> iter = shapesList.iterator();
            while(iter.hasNext())
            {
                (iter.next()).dispose();                
            }
            
            shapesList.clear();
            shapesList = null;
        }
        
        if(rootViewMap != null)
        {
            removeSTRoot();
            rootViewMap = null;
        }
        
        if(tableList != null)
        {
            tableList.clear();
            tableList = null;
        }
    }
    
    protected Workbook book;
    //
    private boolean isGridsPrinted;
    //
    private int firstRow;
    //
    private int lastRow;
    //
    private int activeCellRow;
    //
    private int activeCellColumn;
    
    //max scroll XY
    private float maxScrollX;
    private float maxScrollY;
    //current scroll XY
    private int scrollX;
    private int scrollY;
    //sheet type
    private short type;
    
    private short activeCellType;
    //zoom
    private float zoom = 1;    
    // sheet name
    private String sheetName;    
    //
    private Cell activeCell;
    //
    protected Map<Integer, Row> rows;
    //
    private List<CellRangeAddress> merges;
    //
    private PaneInformation paneInformation; 

    //column style
    private List<ColumnInfo> columnInfoList;
    
    protected List<IShape> shapesList;
    
    private  int defaultRowHeight = SSConstant.DEFAULT_ROW_HEIGHT;
    private int defaultColWidth = SSConstant.DEFAULT_COLUMN_WIDTH;
    
    private short state;
    //
    private IReaderListener iReaderListener;
    
    //
    private List<STRoot> rootViewMap;
    
    //table
    private List<SSTable> tableList;
}
