/*
 * 文件名称:          ACell.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:18:24
 */
package com.nvqquy98.lib.doc.office.ss.model.XLSModel;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Ptg;
import com.nvqquy98.lib.doc.office.fc.hssf.record.BlankRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.BoolErrRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.CellValueRecordInterface;
import com.nvqquy98.lib.doc.office.fc.hssf.record.FormulaRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.LabelRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.LabelSSTRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.NumberRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.Record;
import com.nvqquy98.lib.doc.office.fc.hssf.record.StringRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.aggregates.FormulaRecordAggregate;
import com.nvqquy98.lib.doc.office.fc.hssf.record.common.UnicodeString;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFDataFormatter;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFRichTextString;
import com.nvqquy98.lib.doc.office.fc.ss.SpreadsheetVersion;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.util.SectionElementFactory;

/**
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-7-23
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ACell extends Cell
{
    /**
     * Creates an ACell from a CellValueRecordInterface.  ASheet uses this when
     * reading in cells from an existing sheet.
     *
     * @param book - Workbook record of the workbook containing this cell
     * @param sheet - Sheet record of the sheet containing this cell
     * @param cval - the Cell Value Record we wish to represent
     */
    public ACell(Sheet sheet, CellValueRecordInterface cval) 
    {
        super(Cell.CELL_TYPE_BLANK);
        record      = cval;
        
        cellType    = (short)determineType(cval);        
        
        this.sheet = sheet;
        
        this.rowNumber = cval.getRow();
        this.colNumber = cval.getColumn();
        this.styleIndex = cval.getXFIndex(); 
        
        switch (cellType)
        {
            case CELL_TYPE_NUMERIC:
                value = getNumericCellValue();
                break;
                
            case CELL_TYPE_STRING:
            	if(cval instanceof LabelSSTRecord)
            	{
            		value = ((LabelSSTRecord ) cval).getSSTIndex();
                    processSST();
            	}
            	else if(cval instanceof LabelRecord)
            	{
            		value = sheet.getWorkbook().addSharedString(((LabelRecord ) cval).getValue());            		
            	}
                break;
                
            case CELL_TYPE_FORMULA: 
                procellFormulaCellValue((FormulaRecordAggregate) cval);
                break;
            case CELL_TYPE_BLANK:
                break;
            case CELL_TYPE_BOOLEAN:
                value = getBooleanCellValue();
                break;
            case CELL_TYPE_ERROR:
                value = getErrorCellValue();
                break;
        } 
    }
    
    private void processSST()
    {
        Workbook book = sheet.getWorkbook();
        Object obj = book.getSharedItem((Integer)value);
        if(obj instanceof UnicodeString)
        {
            UnicodeString unicodeString = (UnicodeString)obj;
//            if(unicodeString.getFormatRunCount() > 0)
            {            
                value = book.addSharedString(SectionElementFactory.getSectionElement(book, unicodeString, this));
            }
//            else
//            {
//                book.addSharedString((Integer)value, unicodeString.getString());
//            }
        }        
    }
    
    /**
     * 
     * @param cval
     */
    private void procellFormulaCellValue(FormulaRecordAggregate cval)
    {
        StringRecord strRec = cval.getStringRecord();
        if(strRec != null)
        {
            cellType = Cell.CELL_TYPE_STRING;
            value = sheet.getWorkbook().addSharedString(strRec.getString());
        }
        else
        {
            FormulaRecord formulaRec = cval.getFormulaRecord();
            cellType = (short)formulaRec.getCachedResultType();
            switch (cellType)
            {
                case CELL_TYPE_NUMERIC:
                    value = formulaRec.getValue();
                    break;
                case CELL_TYPE_STRING:  
                    //Log.w("formula CachedResultType is string", String.valueOf(cval.getRow())+""+String.valueOf(cval.getColumn()));
                    break;
                    
                case CELL_TYPE_BOOLEAN:
                    value = formulaRec.getCachedBooleanValue();
                    break;
                case CELL_TYPE_ERROR:
                    value = (byte)formulaRec.getCachedErrorValue();
                    break;
            } 
        }
    }
    /**
     * 
     * Creates new Cell - Should only be called by HSSFRow.  This creates a cell
     * from scratch.
     * <p>
     * When the cell is initially created it is set to CELL_TYPE_BLANK. Cell types
     * can be changed/overwritten by calling setCellValue with the appropriate
     * type as a parameter although conversions from one type to another may be
     * prohibited.
     *
     * @param book - Workbook record of the workbook containing this cell
     * @param sheet - Sheet record of the sheet containing this cell
     * @param row   - the row of this cell
     * @param col   - the column for this cell
     *
     * @see com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFRow#createCell(short)
     */
    public ACell(AWorkbook book, ASheet sheet, int row, short col)
    {
        super(CELL_TYPE_ERROR);
        this.sheet   = sheet;

        // Relying on the fact that by default the cellType is set to 0 which
        // is different to CELL_TYPE_BLANK hence the following method call correctly
        // creates a new blank cell.
        short xfindex = sheet.getInternalSheet().getXFIndexForColAt(col);
        setCellType(CELL_TYPE_BLANK, false, row, col,xfindex);
    }
    
    /**
     * 
     * @param ptgs
     */
    public void setCellFormula(Ptg[] ptgs) 
    {
        int row = record.getRow();
        short col = record.getColumn();
        short styleIndex = record.getXFIndex();
        
        setCellType(CELL_TYPE_FORMULA, false, row, col, styleIndex);
        FormulaRecordAggregate agg = (FormulaRecordAggregate)record;
        FormulaRecord frec = agg.getFormulaRecord();
        frec.setOptions((short) 2);
        frec.setValue(0);

        //only set to default if there is no extended format index already set
        if (agg.getXFIndex() == (short)0) 
        {
            agg.setXFIndex((short) 0x0f);
        }
        agg.setParsedExpression(ptgs);
    }
    
    /**
     * get the value of the cell as a string - for numeric cells we throw an exception.
     * For blank cells we return an empty string.
     * For formulaCells that are not string Formulas, we throw an exception
     */
    public String getStringCellValue()
    {
        switch(cellType)
        {
            case CELL_TYPE_BLANK:
                return "";
            case CELL_TYPE_STRING:
                if(record instanceof LabelSSTRecord)
                {
                    return sheet.getWorkbook().getSharedString(((LabelSSTRecord)record).getSSTIndex());
                }
                break;
            default:
                throw typeMismatch(CELL_TYPE_STRING, cellType, false);
            case CELL_TYPE_FORMULA:
                break;
        }
        FormulaRecordAggregate fra = ((FormulaRecordAggregate)record);
        checkFormulaCachedValueType(CELL_TYPE_STRING, fra.getFormulaRecord());
        String strVal = fra.getStringValue();
        return strVal;
    }
    
    /**
     * Get the value of the cell as a number.
     * For strings we throw an exception.
     * For blank cells we return a 0.
     * See {@link HSSFDataFormatter} for turning this
     *  number into a string similar to that which
     *  Excel would render this number as.
     */
    public double getNumericCellValue() 
    {
        switch(cellType) 
        {
            case CELL_TYPE_BLANK:
                return 0.0;
            case CELL_TYPE_NUMERIC:
                return ((NumberRecord)record).getValue();
            default:
                throw typeMismatch(CELL_TYPE_NUMERIC, cellType, false);
            case CELL_TYPE_FORMULA:
                break;
        }
        FormulaRecord fr = ((FormulaRecordAggregate)record).getFormulaRecord();
        checkFormulaCachedValueType(CELL_TYPE_NUMERIC, fr);
        return fr.getValue();
    }
    
    /**
     * get the value of the cell as a boolean.  For strings, numbers, and errors, we throw an exception.
     * For blank cells we return a false.
     */
    public boolean getBooleanCellValue() 
    {

        switch(cellType)
        {
            case CELL_TYPE_BLANK:
                return false;
            case CELL_TYPE_BOOLEAN:
                return (( BoolErrRecord )record).getBooleanValue();
            default:
                throw typeMismatch(CELL_TYPE_BOOLEAN, cellType, false);
            case CELL_TYPE_FORMULA:
                break;
        }
        FormulaRecord fr = ((FormulaRecordAggregate)record).getFormulaRecord();
        checkFormulaCachedValueType(CELL_TYPE_BOOLEAN, fr);
        return fr.getCachedBooleanValue();
    }
    
    /**
     * get the value of the cell as an error code.  For strings, numbers, and booleans, we throw an exception.
     * For blank cells we return a 0.
     */
    public byte getErrorCellValue()
    {
        switch(cellType) 
        {
            case CELL_TYPE_ERROR:
                return (( BoolErrRecord ) record).getErrorValue();
            default:
                throw typeMismatch(CELL_TYPE_ERROR, cellType, false);
            case CELL_TYPE_FORMULA:
                break;
        }
        FormulaRecord fr = ((FormulaRecordAggregate)record).getFormulaRecord();
        checkFormulaCachedValueType(CELL_TYPE_ERROR, fr);
        return (byte) fr.getCachedErrorValue();
    }
    
    /**
     * Used to help format error messages
     */
    private static String getCellTypeName(int cellTypeCode)
    {
        switch (cellTypeCode) 
        {
            case CELL_TYPE_BLANK:
                return "blank";
            case CELL_TYPE_STRING: 
                return "text";
            case CELL_TYPE_BOOLEAN:
                return "boolean";
            case CELL_TYPE_ERROR: 
                return "error";
            case CELL_TYPE_NUMERIC:
                return "numeric";
            case CELL_TYPE_FORMULA:
                return "formula";
        }
        return "#unknown cell type (" + cellTypeCode + ")#";
    }
    
    /**
     * get Formula Cached Value Type
     * @see #CELL_TYPE_STRING
     * @see #CELL_TYPE_NUMERIC
     * @see #CELL_TYPE_FORMULA
     * @see #CELL_TYPE_BOOLEAN
     * @see #CELL_TYPE_ERROR
     * @return
     */
    public int getFormulaCachedValueType()
    {
        return ((FormulaRecordAggregate)record).getFormulaRecord().getCachedResultType();
    }
    
    private static RuntimeException typeMismatch(int expectedTypeCode, int actualTypeCode, boolean isFormulaCell)
    {
        String msg = "Cannot get a "
            + getCellTypeName(expectedTypeCode) + " value from a "
            + getCellTypeName(actualTypeCode) + " " + (isFormulaCell ? "formula " : "") + "cell";
        return new IllegalStateException(msg);
    }
    
    private static void checkFormulaCachedValueType(int expectedTypeCode, FormulaRecord fr) 
    {
        int cachedValueType = fr.getCachedResultType();
        if (cachedValueType != expectedTypeCode)
        {
            throw typeMismatch(expectedTypeCode, cachedValueType, true);
        }
    }
    
    /**
     * used internally -- given a cell value record, figure out its type
     */
    public static int determineType(CellValueRecordInterface cval) 
    {
        if (cval instanceof FormulaRecordAggregate)
        {
            return CELL_TYPE_FORMULA;
        }
        // all others are plain BIFF records
        Record record = (Record) cval;
        switch (record.getSid())
        {
            case NumberRecord.sid :
                return CELL_TYPE_NUMERIC;
            case BlankRecord.sid : 
                return CELL_TYPE_BLANK;
            case LabelSSTRecord.sid :
            case LabelRecord.sid:
                return CELL_TYPE_STRING;
            case BoolErrRecord.sid :
                BoolErrRecord boolErrRecord = ( BoolErrRecord ) record;

                return boolErrRecord.isBoolean()
                         ? CELL_TYPE_BOOLEAN
                         : CELL_TYPE_ERROR;
        }
        throw new RuntimeException("Bad cell value rec (" + cval.getClass().getName() + ")");
    }
    
    /**
     * Should only be used by HSSFSheet and friends.  Returns the low level CellValueRecordInterface record
     *
     * @return CellValueRecordInterface representing the cell via the low level api.
     */

    public CellValueRecordInterface getCellValueRecord()
    {
        return record;
    }
    
    /**
     * set the cells type (numeric, formula or string)
     * @see #CELL_TYPE_NUMERIC
     * @see #CELL_TYPE_STRING
     * @see #CELL_TYPE_FORMULA
     * @see #CELL_TYPE_BLANK
     * @see #CELL_TYPE_BOOLEAN
     * @see #CELL_TYPE_ERROR
     */
    public void setCellType(int cellType, boolean setValue) 
    {
        int row=record.getRow();
        short col=record.getColumn();
        short styleIndex= record.getXFIndex();
        setCellType(cellType, setValue, row, col, styleIndex);
    }
    
    /**
     * sets the cell type. The setValue flag indicates whether to bother about
     *  trying to preserve the current value in the new record if one is created.
     *  <p>
     *  The @see #setCellValue method will call this method with false in setValue
     *  since it will overwrite the cell value later
     *
     */

    private void setCellType(int cellType, boolean setValue, int row,short col, short styleIndex)
    {

        if (cellType > CELL_TYPE_ERROR)
        {
            throw new RuntimeException("I have no idea what type that is!");
        }
        switch (cellType)
        {

            case CELL_TYPE_FORMULA :
                FormulaRecordAggregate frec;

                if (this.cellType != cellType) 
                {
                    frec = ((ASheet)sheet).getInternalSheet().getRowsAggregate().createFormula(row, col);
                } 
                else 
                {
                    frec = (FormulaRecordAggregate)record;
                    frec.setRow(row);
                    frec.setColumn(col);
                }
                frec.setXFIndex(styleIndex);
                record = frec;
                break;

            case CELL_TYPE_NUMERIC :
                NumberRecord nrec = null;

                if (cellType != this.cellType)
                {
                    nrec = new NumberRecord();
                }
                else
                {
                    nrec = ( NumberRecord )record;
                }
                nrec.setColumn(col);
               
                nrec.setXFIndex(styleIndex);
                nrec.setRow(row);
                record = nrec;
                break;

            case CELL_TYPE_STRING :
                LabelSSTRecord lrec;

                if (cellType == this.cellType) 
                {
                    lrec = (LabelSSTRecord) this.record;
                }
                else 
                {
                    lrec = new LabelSSTRecord();
                    lrec.setColumn(col);
                    lrec.setRow(row);
                    lrec.setXFIndex(styleIndex);
                }
               
                record = lrec;
                break;

            case CELL_TYPE_BLANK :
                BlankRecord brec = null;

                if (this.cellType != cellType)
                {
                    brec = new BlankRecord();
                }
                else
                {
                    brec = ( BlankRecord )record;
                }
                brec.setColumn(col);

                // During construction the cellStyle may be null for a Blank cell.
                brec.setXFIndex(styleIndex);
                brec.setRow(row);
                record = brec;
                break;

            case CELL_TYPE_BOOLEAN :
                BoolErrRecord boolRec = null;

                if (cellType != this.cellType)
                {
                    boolRec = new BoolErrRecord();
                }
                else
                {
                    boolRec = ( BoolErrRecord ) record;
                }
                boolRec.setColumn(col);
                
                boolRec.setXFIndex(styleIndex);
                boolRec.setRow(row);
                record = boolRec;
                break;

            case CELL_TYPE_ERROR :
                BoolErrRecord errRec = null;

                if (cellType != this.cellType)
                {
                    errRec = new BoolErrRecord();
                }
                else
                {
                    errRec = ( BoolErrRecord ) record;
                }
                errRec.setColumn(col);
               
                errRec.setXFIndex(styleIndex);
                errRec.setRow(row);
                record = errRec;
                break;
        }
        
//        if (cellType != cellType &&
//            cellType!= -1 )  // Special Value to indicate an uninitialized Cell
//        {
//            ((ASheet)sheet).getInternalSheet().replaceValueRecord(record);
//        }
        
        this.cellType = (short)cellType;
    }
    
    /**
     * set a numeric value for the cell
     *
     * @param value  the numeric value to set this cell to.  For formulas we'll set the
     *        precalculated value, for numerics we'll set its value. For other types we
     *        will change the cell to a numeric cell and set its value.
     */
    public void setCellValue(double value) 
    {
        switch (cellType)
        {
            case CELL_TYPE_STRING:
                this.value = (Integer)Math.round((float)value);
                break;
            case CELL_TYPE_NUMERIC:
                (( NumberRecord ) record).setValue(value);
                this.value = value;
                break;
            case CELL_TYPE_FORMULA:
                break;
        }
    }
    
    /**
     * set a boolean value for the cell
     *
     * @param value the boolean value to set this cell to.  For formulas we'll set the
     *        precalculated value, for booleans we'll set its value. For other types we
     *        will change the cell to a boolean cell and set its value.
     */
    public void setCellValue(boolean value) 
    {
        switch (cellType)
        {
            case CELL_TYPE_BOOLEAN:
                (( BoolErrRecord ) record).setValue(value);
                this.value = value;
                break;
            case CELL_TYPE_FORMULA:
                break;
        }
    }
    
    /**
     * set a string value for the cell.
     *
     * @param value value to set the cell to.  For formulas we'll set the formula
     * cached string result, for String cells we'll set its value. For other types we will
     * change the cell to a string cell and set its value.
     * If value is null then we will change the cell to a Blank cell.
     */
    public void setCellValue(String value)
    {
        HSSFRichTextString richString = value == null ? null :  new HSSFRichTextString(value);
        
        int row = record.getRow();
        short col= record.getColumn();
        short styleIndex = record.getXFIndex();
        if (richString == null)
        {            
            setCellType(CELL_TYPE_BLANK, false, row, col, styleIndex);
            return;
        }

        if(richString.length() > SpreadsheetVersion.EXCEL97.getMaxTextLength())
        {
            throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
        }
        int index = 0;

        UnicodeString str = richString.getUnicodeString();
        index = ((AWorkbook)sheet.getWorkbook()).getInternalWorkbook().addSSTString(str);
        (( LabelSSTRecord ) record).setSSTIndex(index);
        
        this.value = index;
    }
    
    /**
     * set a error value for the cell
     *
     * @param errorCode the error value to set this cell to.  For formulas we'll set the
     *        precalculated value , for errors we'll set
     *        its value. For other types we will change the cell to an error
     *        cell and set its value.
     */
    public void setCellErrorValue(byte errorCode) 
    {
        switch (cellType)
        {
            case CELL_TYPE_ERROR:
                (( BoolErrRecord ) record).setValue(errorCode);
                value = errorCode;
                break;
            case CELL_TYPE_FORMULA:
                break;
        }
    }
    
    public void dispose()
    {
        super.dispose();
        record = null;
    }
    
    
    private CellValueRecordInterface record;
}
