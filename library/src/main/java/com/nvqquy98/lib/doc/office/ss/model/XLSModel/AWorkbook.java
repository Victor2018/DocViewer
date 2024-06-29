/*
 * 文件名称:          AWorkbook.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:48:53
 */
package com.nvqquy98.lib.doc.office.ss.model.XLSModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.hssf.OldExcelFormatException;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.udf.UDFFinder;
import com.nvqquy98.lib.doc.office.fc.hssf.model.InternalSheet;
import com.nvqquy98.lib.doc.office.fc.hssf.model.InternalWorkbook;
import com.nvqquy98.lib.doc.office.fc.hssf.model.RecordStream;
import com.nvqquy98.lib.doc.office.fc.hssf.record.ExtendedFormatRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.FontRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.LabelRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.NameRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.PaletteRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.Record;
import com.nvqquy98.lib.doc.office.fc.hssf.record.RecordFactory;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFDataFormat;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFName;
import com.nvqquy98.lib.doc.office.fc.poifs.filesystem.DirectoryNode;
import com.nvqquy98.lib.doc.office.fc.poifs.filesystem.POIFSFileSystem;
import com.nvqquy98.lib.doc.office.fc.xls.SSReader;
import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;
import com.nvqquy98.lib.doc.office.system.AbstractReader;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.ReaderHandler;

import android.os.Message;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-7-20
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class AWorkbook extends Workbook implements com.nvqquy98.lib.doc.office.fc.ss.usermodel.Workbook
{
    
    static class ShapesThread extends Thread
    {
        public ShapesThread(AWorkbook book, Map<Integer,Sheet> sheets, int sheetIndex, SSReader iAbortListener)
        {
            this.book = book;
            this.sheets = sheets;
            this.sheetIndex = sheetIndex;
            this.iAbortListener = iAbortListener;
            this.control = iAbortListener.getControl();
        }        
        
        public void run()
        {
            try
            {
                if(sheetIndex >= 0 && iAbortListener != null)
                {
                    iAbortListener.abortCurrentReading();
                    sleep(50);
                    
                    ((ASheet)book.getSheet(sheetIndex)).processSheet(iAbortListener);
                    
                    processOtherSheets();
                }                
            } 
            catch(OutOfMemoryError e)
            { 
                book.dispose();
                iAbortListener.dispose();
                iAbortListener.getControl().getSysKit().getErrorKit().writerLog(e, true);
            }  
            catch(Exception e)
            { 
                book.dispose();
                iAbortListener.dispose();
                iAbortListener.getControl().getSysKit().getErrorKit().writerLog(e, true);
            } 
            finally
            {
                book = null;
                sheets = null;
                
                iAbortListener = null;
                control = null;
            }
           
        };
        
        private void processOtherSheets()
        {
            Iterator<Integer> iter = sheets.keySet().iterator();
            while(iter.hasNext())
            {
                ((ASheet)book.getSheet(iter.next())).processSheet(iAbortListener);
            }
            
            iter = sheets.keySet().iterator();
            while(iter.hasNext())
            {
                book.processShapesBySheetIndex(control, iter.next());
            }
        }
        
        private AWorkbook book;
        private Map<Integer,Sheet> sheets;
        private SSReader iAbortListener;
        private int sheetIndex;
        private IControl control;
    };
    
    /**
     * used for compile-time performance/memory optimization.  This determines the
     * initial capacity for the sheet collection.  Its currently set to 3.
     * Changing it in this release will decrease performance
     * since you're never allowed to have more or less than three sheets!
     */

    public final static int INITIAL_CAPACITY = 3;    
   
    public final static int AUTOMATIC_COLOR = 0x40;
    
    

    public AWorkbook(InputStream s, SSReader iAbortListener) throws IOException
    {
        super(true);
        
        this.iAbortListener = iAbortListener;
        
        DirectoryNode directory = new POIFSFileSystem(s).getRoot();
        
        String workbookName = getWorkbookDirEntryName(directory);

        // Grab the data from the workbook stream, however
        //  it happens to be spelled.
        InputStream stream = directory.createDocumentInputStream(workbookName);

        List<Record> records = RecordFactory.createRecords(stream, iAbortListener);

        workbook = InternalWorkbook.createWorkbook(records, iAbortListener);
        
        int recOffset = workbook.getNumRecords();
        // shared string
        int size = workbook.getSSTUniqueStringSize();
        for(int i = 0; i < size; i++)
        {
           addSharedString(i, workbook.getSSTString(i));
        }
        
        convertLabelRecords(records, recOffset);
        
        isUsing1904DateWindowing = workbook.isUsing1904DateWindowing();
        
        //color
        PaletteRecord palette = workbook.getCustomPalette();
        int index = PaletteRecord.FIRST_COLOR_INDEX;        
        addColor(index++, ColorUtil.rgb(0, 0, 0));
        
        byte[] color = palette.getColor(index);
        while(color != null)
        {
            addColor(index++, ColorUtil.rgb(color[0], color[1], color[2]));            
            color = palette.getColor(index);
        }
        
        //cell style 
        processCellStyle(workbook);
        
        RecordStream rs = new RecordStream(records, recOffset);
        int sheetIndex = 0;
        while (rs.hasNext())
        {            
            InternalSheet internalSheet = InternalSheet.createSheet(rs, iAbortListener);
            ASheet sheet = new ASheet(this, internalSheet);
            sheet.setSheetName(workbook.getSheetName(sheetIndex));
            if(internalSheet.isChartSheet())
            {
            	sheet.setSheetType(Sheet.TYPE_CHARTSHEET);
            }
            
            sheets.put(sheetIndex++, sheet);
        }
        records.clear();
        records = null;
        
        names = new ArrayList<HSSFName>(INITIAL_CAPACITY);
        for (int i = 0; i < workbook.getNumNames(); ++i)
        {
            NameRecord nameRecord = workbook.getNameRecord(i);
            HSSFName name = new HSSFName(this, nameRecord,
                workbook.getNameCommentRecord(nameRecord));
            
            names.add(name);
        }
        
        //rows and shapes processing
        processSheet();
        
    }
    
    /**
     * process the index sheet
     * @param sheetIndex
     */
    private void processShapesBySheetIndex(IControl control, int sheetIndex)
    {        
        ASheet sheet = (ASheet)sheets.get(sheetIndex);
        try
        {   
            if(sheet.getState() != Sheet.State_Accomplished)
            {
                sheet.processSheetShapes(control);
                
                sheet.setState(Sheet.State_Accomplished);
            }            
        }
        catch(Exception e)
        {
        	sheet.setState(Sheet.State_Accomplished);
        }
       
    }
    
    /**
     * 
     */
    private void processSheet()
    {
        class WorkbookReaderHandler extends ReaderHandler
        {
            public WorkbookReaderHandler(AWorkbook book)
            {
                this.book = book;
            }
            
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case MainConstant.HANDLER_MESSAGE_SUCCESS:
                        currentSheet = (Integer)msg.obj;
                        if(sheets.get(currentSheet).getState() != Sheet.State_Accomplished)
                        {
                            new ShapesThread(book, sheets, currentSheet, iAbortListener).start();  
                        }
                                          
                        break;
                        
                    case MainConstant.HANDLER_MESSAGE_ERROR:
                    case MainConstant.HANDLER_MESSAGE_DISPOSE:
                        book = null;
                        break;
                }
            }
            
            private AWorkbook book;
        }
         
        readerHandler = new WorkbookReaderHandler(this);
        
        Message msg = new Message();
        msg.what = MainConstant.HANDLER_MESSAGE_SUCCESS;
        msg.obj= (Integer)0;
        
        readerHandler.handleMessage(msg);
    }
    
    /**
     * Normally, the Workbook will be in a POIFS Stream
     * called "Workbook". However, some weird XLS generators use "WORKBOOK"
     */
    private static final String[] WORKBOOK_DIR_ENTRY_NAMES = {"Workbook", // as per BIFF8 spec
        "WORKBOOK",};
    
    public static String getWorkbookDirEntryName(DirectoryNode directory)
    {

        String[] potentialNames = WORKBOOK_DIR_ENTRY_NAMES;
        for (int i = 0; i < potentialNames.length; i++)
        {
            String wbName = potentialNames[i];
            try
            {
                directory.getEntry(wbName);
                return wbName;
            }
            catch(FileNotFoundException e)
            {
                // continue - to try other options
            }
        }

        // check for previous version of file format
        try
        {
            directory.getEntry("Book");
            throw new OldExcelFormatException(
                "The supplied spreadsheet seems to be Excel 5.0/7.0 (BIFF5) format. "
                    + "POI only supports BIFF8 format (from Excel versions 97/2000/XP/2003)");
        }
        catch(FileNotFoundException e)
        {
            // fall through
        }

        throw new IllegalArgumentException(
            "The supplied POIFSFileSystem does not contain a BIFF8 'Workbook' entry. "
                + "Is it really an excel file?");
    }
    
    /**
     * This is basically a kludge to deal with the now obsolete Label records.  If
     * you have to read in a sheet that contains Label records, be aware that the rest
     * of the API doesn't deal with them, the low level structure only provides read-only
     * semi-immutable structures (the sets are there for interface conformance with NO
     * impelmentation).  In short, you need to call this function passing it a reference
     * to the Workbook object.  All labels will be converted to LabelSST records and their
     * contained strings will be written to the Shared String tabel (SSTRecord) within
     * the Workbook.
     *
     * @param records a collection of sheet's records.
     * @param offset the offset to search at 
     * @see com.nvqquy98.lib.doc.office.fc.hssf.record.LabelRecord
     * @see com.nvqquy98.lib.doc.office.fc.hssf.record.LabelSSTRecord
     * @see com.nvqquy98.lib.doc.office.fc.hssf.record.SSTRecord
     */

   private void convertLabelRecords(List records, int offset)
   {
     
       for (int k = offset; k < records.size(); k++)
       {
           Record rec = (Record)records.get(k);

           if (rec.getSid() == LabelRecord.sid)
           {
               LabelRecord oldrec = (LabelRecord)rec;               
               sharedString.put(sharedString.size(), oldrec.getValue());               
           }
       }
   }
   
   /**
    * 
    * @param workbook
    */
   private void processCellStyle(InternalWorkbook workbook)
   {
       processFont(workbook);
       
       short styleIndex = 0;
       short cellStyleCnt = (short)workbook.getNumExFormats();
       ExtendedFormatRecord format;
       while(styleIndex < cellStyleCnt)
       {
           format = workbook.getExFormatAt(styleIndex);
           if(format == null)
           {
               continue;
           }
           
           CellStyle style = new CellStyle();            
           // style index;
           style.setIndex(styleIndex);
           // data format Index
           style.setNumberFormatID(format.getFormatIndex());
           // data format string
           style.setFormatCode(HSSFDataFormat.getFormatCode(workbook, format.getFormatIndex()));
           // fontIndex
           style.setFontIndex(format.getFontIndex());           
           // hidden
           style.setHidden(format.isHidden());
           // locked
           style.setLocked(format.isLocked());
           // wrap text
           style.setWrapText(format.getWrapText());
           // horizontal alignment
           style.setHorizontalAlign(format.getAlignment());
           // vertical alignment
           style.setVerticalAlign(format.getVerticalAlignment());
           // rotation
           style.setRotation(format.getRotation());
           // indent
           style.setIndent(format.getIndent());
           
           // border left and color        
           style.setBorderLeft(format.getBorderLeft());
           short colorIndex = format.getLeftBorderPaletteIdx();
           if(colorIndex == AUTOMATIC_COLOR)
           {
               colorIndex = PaletteRecord.FIRST_COLOR_INDEX;
           }
           style.setBorderLeftColorIdx(colorIndex);
           
           // border right and color
           style.setBorderRight(format.getBorderRight());
           colorIndex = format.getRightBorderPaletteIdx();
           if(colorIndex == AUTOMATIC_COLOR)
           {
               colorIndex = PaletteRecord.FIRST_COLOR_INDEX;
           }
           style.setBorderRightColorIdx(colorIndex);
           
           // border top and color
           style.setBorderTop(format.getBorderTop());            
           colorIndex = format.getTopBorderPaletteIdx();
           if(colorIndex == AUTOMATIC_COLOR)
           {
               colorIndex = PaletteRecord.FIRST_COLOR_INDEX;
           }
           style.setBorderTopColorIdx(colorIndex);
          
           // border bottom and color
           style.setBorderBottom(format.getBorderBottom());            
           colorIndex = format.getBottomBorderPaletteIdx();
           if(colorIndex == AUTOMATIC_COLOR)
           {
               colorIndex = PaletteRecord.FIRST_COLOR_INDEX;
           }
           style.setBorderBottomColorIdx(colorIndex);
          
           
           
           // background color index
           colorIndex = format.getFillBackground();
           style.setBgColor(getColor(colorIndex));
           
           // foreground color index
           colorIndex = format.getFillForeground();
           if(colorIndex == AUTOMATIC_COLOR)
           {
               colorIndex = PaletteRecord.FIRST_COLOR_INDEX + 1;
           }
           style.setFgColor(getColor(colorIndex));
           
           // fill color index
           style.setFillPatternType((byte)(format.getAdtlFillPattern() - 1));
           
           addCellStyle(styleIndex++, style);
       }
   }
   
   /**
    * process font fontIndex 
    */
   private void processFont(InternalWorkbook workbook)
   {
       int numFont = workbook.getNumberOfFontRecords();
       if(numFont <= 4)
       {
           numFont -= 1;
       }
       
       int idx = 0;
       while(idx <= numFont)
       {
           FontRecord fontRec = workbook.getFontRecordAt(idx);
           
           Font font = new Font();
           // font index;
           font.setIndex(idx);
           // font name
           font.setName(fontRec.getFontName());
           // font size
           font.setFontSize(( short )(fontRec.getFontHeight() / 20));
           // color index;
           short index = fontRec.getColorPaletteIndex();
           if(index == 32767)
           {
               index = PaletteRecord.FIRST_COLOR_INDEX ;
           }
           font.setColorIndex(index);
           // Italic
           font.setItalic(fontRec.isItalic());
           // bold
           font.setBold(fontRec.getBoldWeight() > Font.BOLDWEIGHT_NORMAL);
           // superSubScript
           font.setSuperSubScript((byte)fontRec.getSuperSubScript());
           // strike
           font.setStrikeline(fontRec.isStruckout());
           // underline
           font.setUnderline(fontRec.getUnderline());
           
           addFont(idx++, font);
       }
       
   }
   
   /**
    * 
    * @return
    */
   public InternalWorkbook getInternalWorkbook()
   {
       return workbook;
   }
   
   /**
   *
   * Returns the locator of user-defined functions.
   * The default instance extends the built-in functions with the Analysis Tool Pack
   *
   * @return the locator of user-defined functions
   */
  public UDFFinder getUDFFinder()
  {
      return _udfFinder;
  }
  
   /**
    * Get the number of spreadsheets in the workbook
    *
    * @return the number of sheets
    */
   public int getNumberOfSheets()
   {
       return sheets.size();       
   }
   
   /**
    * get sheet for sheet index; 
    */
   public ASheet getSheetAt(int index)
   {
       if (index < 0 || index >= sheets.size())
       {
           return null;
       }       
       return (ASheet)sheets.get(index);
   }
   
   /** Returns the index of the sheet by his name
    * @param name the sheet name
    * @return index of the sheet (0 based)
    */
   public int getSheetIndex(String name)
   {
       return workbook.getSheetIndex(name);
   }

   /** Returns the index of the given sheet
    * @param sheet the sheet to look up
    * @return index of the sheet (0 based). <tt>-1</tt> if not found
    */
   public int getSheetIndex(Sheet sheet)
   {
       for (int i = 0; i < sheets.size(); i++)
       {
           if (sheets.get(i) == sheet)
           {
               return i;
           }
       }
       return -1;
   }
   
   public int getNumberOfNames()
   {
       int result = names.size();
       return result;
   }

   public int getNameIndex(String name)
   {

       for (int k = 0; k < names.size(); k++)
       {
           String nameName = getNameName(k);

           if (nameName.equalsIgnoreCase(name))
           {
               return k;
           }
       }
       return -1;
   }
   
   public HSSFName getName(String name)
   {
       int nameIndex = getNameIndex(name);
       if (nameIndex < 0)
       {
           return null;
       }
       return (HSSFName)names.get(nameIndex);
   }

   public HSSFName getNameAt(int nameIndex)
   {
       int nNames = names.size();
       if (nNames < 1)
       {
           throw new IllegalStateException("There are no defined names in this workbook");
       }
       if (nameIndex < 0 || nameIndex > nNames)
       {
           throw new IllegalArgumentException("Specified name index " + nameIndex
               + " is outside the allowable range (0.." + (nNames - 1) + ").");
       }
       return (HSSFName)names.get(nameIndex);
   }

   public NameRecord getNameRecord(int nameIndex)
   {
       return workbook.getNameRecord(nameIndex);
   }

   /** gets the named range name
    * @param index the named range index (0 based)
    * @return named range name
    */
   public String getNameName(int index)
   {
       String result = getNameAt(index).getNameName();

       return result;
   }
   
   /**
    * 
    * @return
    */
   public AbstractReader getAbstractReader()
   {
       return iAbortListener;
   }
   /**
    * 
    */
   public void dispose()
   {
       destroy();
       
       workbook = null;
       
       if(names != null && names.size() > 0)
       {
           Iterator<HSSFName> iter = names.iterator();
           while(iter.hasNext())
           {
               iter.next().dispose();
           }
           
           names.clear();
           names = null;
       }
       _udfFinder = null;
       iAbortListener =  null;
   }   
   
   /**
    * The locator of user-defined functions.
    * By default includes functions from the Excel Analysis Toolpack
    */
   private UDFFinder _udfFinder = UDFFinder.DEFAULT;
   
   private InternalWorkbook workbook;
   
   /**
    * this holds the HSSFName objects attached to this workbook
    */

   private ArrayList<HSSFName> names;
   
   private int currentSheet;  

   private SSReader iAbortListener;
}
