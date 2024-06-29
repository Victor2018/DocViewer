/*
 * 文件名称:          ARow.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:12:40
 */
package com.nvqquy98.lib.doc.office.ss.model.XLSModel;

import java.util.Iterator;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.hssf.record.CellValueRecordInterface;
import com.nvqquy98.lib.doc.office.fc.hssf.record.RowRecord;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;

/**
 * TODO: 文件注释
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
public class ARow extends Row
{

    // used for collections
    public final static int INITIAL_CAPACITY = 5;
    
    /**
     * Creates an ARow from a low level RowRecord object.  Only ASheet should do
     * this.  ASheet uses this when an existing file is read in.
     *
     * @param book low-level Workbook object containing the sheet that contains this row
     * @param sheet low-level Sheet object that contains this Row
     * @param record the low level api object this row should represent
     * @see com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFSheet#createRow(int)
     */
    public ARow(Workbook book, Sheet sheet, RowRecord record)
    {
        super(record.getLastCol() - record.getFirstCol() + INITIAL_CAPACITY);
        
        setSheet(sheet);
        
        record.setEmpty();
        // row number
        rowNumber = record.getRowNumber();
        // first column
        firstCol = record.getFirstCol();
        // last column
        lastCol = Math.max(lastCol, record.getLastCol());        
        
        this.styleIndex = record.getXFIndex();
        
//        if((styleIndex & 0xFFFF) > book.getNumStyles())
//        {
//            styleIndex = 15;
//        }
        
        int t =0;
        while((styleIndex & (0xFFFF >> t)) > book.getNumStyles())
        {
            t += 1;
        }
        styleIndex &= (0xFFFF >> t);  

        // zero height
        setZeroHeight(record.getZeroHeight());
        // row height of pixel
        short height = record.getHeight();
        //The low-order 15 bits contain the row height.
        //The 0x8000 bit indicates that the row is standard height (optional)
        if ((height & 0x8000) != 0)
        {            
            height = 0xFF;
        }
        else
        {            
            height &= 0x7FFF;
        }
        setRowPixelHeight((int)(height /20 * MainConstant.POINT_TO_PIXEL));
    }    
   
    
    private boolean isValidateCell(CellValueRecordInterface cval)
    {        
        int cellType =  ACell.determineType(cval);
        if(cellType != Cell.CELL_TYPE_BLANK)
        {
            return true;
        }        
        
        Workbook book = sheet.getWorkbook();
        return (Workbook.isValidateStyle(book.getCellStyle(cval.getXFIndex()))
            || Workbook.isValidateStyle(book.getCellStyle(getRowStyle()))
            || Workbook.isValidateStyle(book.getCellStyle(sheet.getColumnStyle(cval.getColumn()))));
    }
    
    /**
     * create a high level ACell object from an existing low level record.  Should
     * only be called from ASheet or AFRow itself.
     * @param cell low level cell to create the high level representation from
     * @return ACell representing the low level record passed in
     */
    ACell createCellFromRecord(CellValueRecordInterface cellRec) 
    {
        Cell cell = cells.get(cellRec.getColumn());
        if(cell != null)
        {
            return (ACell)cell;
        }
        
        if(isValidateCell(cellRec))
        {
            ACell acell = new ACell( sheet, cellRec);
            
            
            int colIx = cellRec.getColumn();
            if (colIx < firstCol) 
            {
                firstCol = colIx;
            } 
            else if (colIx > lastCol) 
            {
                lastCol = colIx;                
            }
            
            addCell(acell);        
            
            // TODO - RowRecord column boundaries need to be updated for cell comments too
            return acell;
        }
        
        return null;
    }
    
    /**
     * @return an iterator of the PHYSICAL rows.  Meaning the 3rd element may not
     * be the third row if say for instance the second row is undefined.
     * Call getRowNum() on each row if you care which one it is.
     */
    public Iterator<Cell> cellIterator()
    {
        @ SuppressWarnings("unchecked")
        // can this clumsy generic syntax be improved?
        Iterator<Cell> result = (Iterator<Cell>)(Iterator< ? extends Cell>)cells.values().iterator();
        return result;
    }
}
