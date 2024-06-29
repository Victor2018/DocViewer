/*
 * 文件名称:          Cell.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:19:10
 */
package com.nvqquy98.lib.doc.office.ss.model.baseModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.simpletext.view.STRoot;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTable;


/**
 * Cell of this Row
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
public class Cell
{
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static final int SECONDS_PER_DAY = (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE);
    private static final long   DAY_MILLISECONDS = SECONDS_PER_DAY * 1000L;
    
    // Numeric Cell type (0)
    public final static short CELL_TYPE_NUMERIC = 0;
    // String Cell type (1)
    public final static short CELL_TYPE_STRING = CELL_TYPE_NUMERIC + 1;
    // Formula Cell type (2)
    public final static short CELL_TYPE_FORMULA = CELL_TYPE_STRING + 1;
    // Blank Cell type (3)
    public final static short CELL_TYPE_BLANK = CELL_TYPE_FORMULA + 1;
    // Boolean Cell type (4)
    public final static short CELL_TYPE_BOOLEAN = CELL_TYPE_BLANK + 1;
    // Error Cell type (5)
    public final static short CELL_TYPE_ERROR = CELL_TYPE_BOOLEAN + 1;
    
    
    /**
     * GENERAL Cell type (6)
     */
    public final static short CELL_TYPE_NUMERIC_GENERAL = CELL_TYPE_ERROR + 1;
    
    /**
     * DecimalFormat Cell type (7)
     */
    public final static short CELL_TYPE_NUMERIC_DECIMAL = CELL_TYPE_NUMERIC_GENERAL + 1;
    
    
    /**
     * accounting Cell type (8)
     */
    public final static short CELL_TYPE_NUMERIC_ACCOUNTING = CELL_TYPE_NUMERIC_DECIMAL + 1;
    
    /**
     * FractionalFormat Cell type (9)
     */
    public final static short CELL_TYPE_NUMERIC_FRACTIONAL = CELL_TYPE_NUMERIC_ACCOUNTING + 1;    
    
    /**
     * SimpleDateFormat Cell type (10)
     */
    public final static short CELL_TYPE_NUMERIC_SIMPLEDATE = CELL_TYPE_NUMERIC_FRACTIONAL + 1; 
    
    /**
     * StringFormat Cell type (11)
     */
    public final static short CELL_TYPE_NUMERIC_STRING = CELL_TYPE_NUMERIC_SIMPLEDATE + 1; 
    
    //
    private static Calendar CALENDAR = new GregorianCalendar();
    
    
    
    /**
     * 构造器
     * 
     * @param cellType  type of this cell
     * @see #CELL_TYPE_BLANK
     * @see #CELL_TYPE_BOOLEAN
     * @see #CELL_TYPE_ERROR
     * @see #CELL_TYPE_FORMULA
     * @see #CELL_TYPE_STRING
     * @see #CELL_TYPE_NUMERIC
     */
    public Cell(short cellType)
    {
        this.cellType = cellType;
        
        prop = new CellProperty();
    }    
    
    /**
     * 
     * @param sheet
     */
    public void setSheet(Sheet sheet)
    {
        this.sheet = sheet;
    }
    
    /**
     * 
     * @return
     */
    public Sheet getSheet()
    {
        return sheet;
    }
    
    /**
     * 
     * @param cellType  type of this cell
     * @see #CELL_TYPE_BLANK
     * @see #CELL_TYPE_BOOLEAN
     * @see #CELL_TYPE_ERROR
     * @see #CELL_TYPE_FORMULA
     * @see #CELL_TYPE_STRING
     * @see #CELL_TYPE_NUMERIC
     */
    public void setCellType(short cellType)
    {
        this.cellType = cellType;
    }
    
    /**
     * 
     */
    public short getCellType()
    {
        return this.cellType;
    }

    public void setCellNumericType(short numericType)
    {
        if(cellType == CELL_TYPE_NUMERIC)
        {
            prop.setCellProp(CellProperty.CELLPROPID_NUMERICTYPE, numericType);
        }
    }
    
    public short getCellNumericType()
    {
    	return prop.getCellNumericType();
    }
    
    /**
     * set value of this cell
     * 
     * @param value
     */
    public void setCellValue(Object value)
    {
        this.value = value;
    }
    /**
     * get string value of this cell
     * 
     * @return
     */
    public int getStringCellValueIndex()
    {
        if (cellType == CELL_TYPE_STRING && value != null)
        {
            return (Integer)value;
        }
        return -1;
    }
    
    /**
     * get number value of this cell
     * 
     * @return Returns the numberValue.
     */
    public double getNumberValue()
    {
        if (cellType == CELL_TYPE_NUMERIC && value != null)
        {
            return ((Double)value).doubleValue(); 
        }
        return Double.NaN;
    }
    
    public int getErrorValue()
    {
        if (cellType == CELL_TYPE_ERROR && value != null)
        {
            return (Byte)value;
        }
        return -1;
    }
    /**
     * get error code of this cell
     */
    public byte getErrorCodeValue()
    {
        if (cellType == CELL_TYPE_ERROR && value != null)
        {
            return ((Byte)value).byteValue();
        }
        return Byte.MIN_VALUE;
    }
    
    /**
     * get formula of this cell
     * 
     * @return
     */
    public String getCellFormulaValue()
    {
        if (cellType == CELL_TYPE_FORMULA && value != null)
        {
            return ((String)value);
        }
        return null;
    }
    
    /**
     * get boolean value of this cell
     */
    public boolean getBooleanValue()
    {
        if (cellType == CELL_TYPE_BOOLEAN && value != null)
        {
            return ((Boolean)value).booleanValue();
        }
        return  false;
    }
    
    /**
     * get data value of this cell
     * 
     * @return
     */
    public Date getDateCellValue(boolean use1904windowing)
    {
        if (cellType == CELL_TYPE_NUMERIC && value != null)
        {
            double date = ((Double)value).doubleValue();
            int wholeDays = (int)Math.floor(date);
            int millisecondsInDay = (int)((date - wholeDays) * DAY_MILLISECONDS + 0.5);
                        
            int startYear = use1904windowing ? 1904 : 1900;
            // 1904 date windowing uses 1/2/1904 as the first day
            // Date is prior to 3/1/1900, so adjust because Excel thinks 2/29/1900 exists
            // If Excel date == 2/29/1900, will become 3/1/1900 in Java representation
            int dayAdjust = use1904windowing ? 1 : (wholeDays < 61 ? 0 : -1);
            
            CALENDAR.clear();
            CALENDAR.set(startYear,0, wholeDays + dayAdjust, 0, 0, 0);
            CALENDAR.set(GregorianCalendar.MILLISECOND, millisecondsInDay);
            return CALENDAR.getTime();
        }
        return null;
    }
    
    /**
     * @return Returns the rangeAddressIndex.
     */
    public int getRangeAddressIndex()
    {
        return prop.getCellMergeRangeAddressIndex();
    }

    /**
     * @param rangeAddressIndex The rangeAddressIndex to set.
     */
    public void setRangeAddressIndex(int rangeAddressIndex)
    {
        prop.setCellProp(CellProperty.CELLPROPID_MERGEDRANGADDRESS, rangeAddressIndex);
    }

    /**
     * @return Returns the rowNumber.
     */
    public int getRowNumber()
    {
        return rowNumber;
    }

    /**
     * @param rowNumber The rowNumber to set.
     */
    public void setRowNumber(int rowNumber)
    {
        this.rowNumber = rowNumber;       
    }

    /**
     * @return Returns the colNumber.
     */
    public int getColNumber()
    {
        return colNumber;
    }

    /**
     * @param colNumber The colNumber to set.
     */
    public void setColNumber(int colNumber)
    {
        this.colNumber = colNumber;
    }

    /**
     * @return Returns the link.
     */
    public Hyperlink getHyperLink()
    {
        return prop.getCellHyperlink();
    }

    /**
     * @param link The link to set.
     */
    public void setHyperLink(Hyperlink link)
    {
        prop.setCellProp(CellProperty.CELLPROPID_HYPERLINK, link);
    }

    /**
     * @return Returns the cellStyle.
     */
    public CellStyle getCellStyle()
    {
        return sheet.getWorkbook().getCellStyle(styleIndex);
    }

    /**
     * @param cellStyle The cellStyle to set.
     */
    public void setCellStyle(int styleIndex)
    {
        this.styleIndex = styleIndex;
    }

    public boolean hasValidValue()
    {
        return value != null;
    }
    
    public void setSTRoot(STRoot root)
    {
        if(sheet.getState() == Sheet.State_Accomplished)
        {            
            prop.setCellProp(CellProperty.CELLPROPID_STROOT, sheet.addSTRoot(root));
        }
    }
    
    public STRoot getSTRoot()
    {
        return sheet.getSTRoot(prop.getCellSTRoot());
    }
    
    /**
     * 
     */
    public void removeSTRoot()
    {
        prop.removeCellSTRoot();
    }
    
    /**
     * 
     * @param index
     */
    public void setExpandedRangeAddressIndex(int index)
    {
        prop.setCellProp(CellProperty.CELLPROPID_EXPANDRANGADDRESS, index);
    }
    
    /**
     * 
     * @return
     */
    public int getExpandedRangeAddressIndex()
    {
        return prop.getExpandCellRangeAddressIndex();
    }
    
    /**
     * table infomation
     */
    public void setTableInfo(SSTable table)
    {
        prop.setCellProp(CellProperty.CELLPROPID_TABLEINFO, table);
    }
    
    /**
     * 
     * @return
     */
    public SSTable getTableInfo()
    {
        return prop.getTableInfo();
    }
    
    /**
     * dispose
     */
    public void dispose()
    {       
        sheet = null;        
        value = null;
        
        if(prop != null)
        {
            prop.dispose();
            prop = null;
        }
    }
    
    protected Sheet sheet;
    
    // cell type
    protected short cellType;
    //
    protected int rowNumber;
    //
    protected int colNumber;
    //
    protected int styleIndex;
    // value of this cell
    protected Object value;
    
    private CellProperty prop;
}
