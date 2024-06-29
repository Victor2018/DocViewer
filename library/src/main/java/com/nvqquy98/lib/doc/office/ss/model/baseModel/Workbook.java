/*
 * 文件名称:          WorkBooks.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:07:27
 */
package com.nvqquy98.lib.doc.office.ss.model.baseModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.TableFormatManager;
import com.nvqquy98.lib.doc.office.system.ReaderHandler;

import android.graphics.Color;
import android.os.Message;

/**
 * Excel model 数据
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
public class Workbook
{  
    public final static int MAXROW_03 = 65536;
    public final static int MAXCOLUMN_03 = 256;
    
    public final static int MAXROW_07 = 1048576;
    public final static int MAXCOLUMN_07 = 16384;
    
    /**
     * 
     */
    public Workbook(boolean before07)
    {
        sheets = new HashMap<Integer, Sheet>(5);
        fonts = new HashMap<Integer, Font>(20);
        colors = new HashMap<Integer, Integer>(20);
        cellStyles = new HashMap<Integer, CellStyle>(80);
        sharedString = new HashMap<Integer, Object>(20);
        schemeColor = new HashMap<String, Integer>(20);
        themeColor = new HashMap<Integer, Integer>(20);
        pictures= new HashMap<Integer, Picture>();
        
        this.before07 = before07;
    }
        
    public static boolean isValidateStyle(CellStyle cellStyle)
    {
        if(cellStyle == null)
        {
            return false;
        }
        
        if(cellStyle.getBorderLeft() > 0 
            || cellStyle.getBorderTop() > 0
            || cellStyle.getBorderRight() > 0
            || cellStyle.getBorderBottom() > 0)
        {
            //has border
            return true;
        }
        
        //not special AUTOMATIC case, not white
        if(cellStyle.getFillPatternType() != BackgroundAndFill.FILL_NO)
        {
            //foregrond color was  not white
            return true;
        }
       
        return false;
    }
    
    /**
     * add sheet on this workbook
     * @param index
     * @param sheet
     */
    public void addSheet(int index, Sheet sheet)
    {
        sheets.put(index, sheet);
    }
    
    /**
     * set reader handler
     * @param readerHandler
     */
    public void setReaderHandler(ReaderHandler readerHandler)
    {
        this.readerHandler = readerHandler;
    }
    
    /**
     * 
     * @return
     */
    public ReaderHandler getReaderHandler()
    {
        return readerHandler;
    }
    
    /**
     * get sheet for sheet name
     */
    public Sheet getSheet(String sheetName)
    {
        Collection<Sheet> sheetCol = sheets.values();
        for(Sheet sheet : sheetCol)
        {
            if(sheet.getSheetName().equals(sheetName))
            {
                return sheet;
            }
        }
        return null;
    }
    
    /**
     * get sheet for sheet name
     */
    public int getSheetIndex(Sheet sheet)
    {
        Iterator<Integer> iter = sheets.keySet().iterator();
        while(iter.hasNext())
        {
            int index = iter.next();
            if (sheets.get(index).equals(sheet))
            {
                return index;
            }            
        }        
        return -1;
    }
    
    /**
     * get sheet for sheet index; 
     */
    public Sheet getSheet(int index)
    {
        if (index < 0 || index >= sheets.size())
        {
            return null;
        }       
        return sheets.get(index);
    }
    
    /**
     * get sheet count of this workbook
     */
    public int getSheetCount()
    {
        return sheets.size();
    }
    
    /**
     * add font of this workbook
     */
    public void addFont(int index, Font font)
    {
        fonts.put(index, font);
    }
    
    /**
     * get font for index
     * @param index
     * @return
     */
    public Font getFont(int index)
    {
        return fonts.get(index);
    }
    
    /**
     *  put paletter color before add new color
     * @param argb
     * @return index of color list
     */
    public synchronized int addColor(int argb)
    {
        if(colors.containsValue(argb))
        {
            Iterator<Integer> iter = colors.keySet().iterator();
            int index = 0;
            while(iter.hasNext())
            {
                index = iter.next();
                if(colors.get(index) == argb)
                {
                    break;
                }
            }
            
            return index;
        }
        else
        {
            int index = colors.size() - 1;
            while(colors.get(index) != null)
            {
                index++;
            }
            colors.put(index, argb);
            return index;
        }
    }
    
    /**
     * add color of this workbook
     */
    public synchronized void addColor(int index, int rgba)
    {
        colors.put(index, rgba);
    }
    
    /**
     * get color of index
     */
    public int getColor(int index)
    {
        return getColor(index, false);
    }
    
    /**
     * get color of index
     */
    public synchronized int getColor(int index, boolean line)
    {
    	Integer t = colors.get(index);
        if(t == null && (index >= 0 && index <= 7))
        {
            t = colors.get(8);
        }
         
        if (t == null)
        {
            if (line)
            {
                return Color.BLACK;
            }
            else
            {
                return Color.WHITE;
            }
        }
        return t;
    }
    
    /**
     * 
     * @param index
     * @param cellStyle
     */
    public void addCellStyle(int index, CellStyle cellStyle)
    {
        cellStyles.put(index, cellStyle);
    }
    
    public int getNumStyles()
    {
        return cellStyles.size();
    }
    
    
    /**
     * 
     * @param index
     * @return
     */
    public CellStyle getCellStyle(int index)
    {
        return cellStyles.get(index);
    }
    
    /**
     * 
     * @param item
     * @return
     */
    public int addSharedString(Object item)
    {
        if(item == null)
        {
            return -1;
        }
        
//        if(sharedString.containsValue(item))
//        {
//            Iterator<Integer> iter = sharedString.keySet().iterator();
//            int index;
//            while(iter.hasNext())
//            {
//                index = iter.next();
//                if(sharedString.get(index).equals(item))
//                {
//                    return index;
//                }
//            }
//        }
//        else
        {
            //add to end
            sharedString.put(sharedString.size(), item);
            
            return sharedString.size() - 1;
        }  
    }
    
    /**
     * 
     * @param index
     * @param item
     */
    public void addSharedString(int index, Object item)
    {
        sharedString.put(index, item);
    }
    
    /**
     * 
     * @param index
     * @return
     */
    public String getSharedString(int index)
    {
        Object si = sharedString.get(index);
        String value = null;
        if(si instanceof SectionElement)
        {
            value = ((SectionElement)si).getText(null);
        }
        else if(si instanceof String)
        {
            value = (String)si;
        }
        
        return value;
    }
    
    /**
     * 
     * @param index
     * @return string or SectionElement
     */
    public Object getSharedItem(int index)
    {
        return sharedString.get(index);
    }
    
    /**
     * 
     * @param key
     * @param colorIndex
     */
    public synchronized void addThemeColorIndex(int index, int colorIndex)
    {
        themeColor.put(index, colorIndex);
    }
    
    /**
     * 
     * @param key
     * @return color index
     */
    public synchronized int getThemeColorIndex(int index)
    {
        Integer t = themeColor.get(index);
        if( t != null)
        {
            return t;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * 
     * @param key
     * @return color
     */
    public synchronized int getThemeColor(int index)
    {
        Integer t = colors.get(themeColor.get(index));
        if(t != null)
        {
            return t;
        }
        else
        {
            return Color.BLACK;
        }
    }
    
    /**
     * 
     * @param key
     * @param colorIndex
     */
    public synchronized void addSchemeColorIndex(String key, int colorIndex)
    {
        schemeColor.put(key, colorIndex);
    }
    
    /**
     * 
     * @param key
     * @return color index
     */
    public synchronized int getSchemeColorIndex(String key)
    {
        Integer t = schemeColor.get(key);
        if(t != null)
        {
            return t;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * 
     * @param key
     * @return color
     */
    public synchronized int getSchemeColor(String key)
    {
        Integer t = colors.get(schemeColor.get(key));
        if(t != null)
        {
            return t;
        }
        else
        {
            return Color.BLACK;
        }
    }
    
    /**
     * @return Returns the isUsing1904DateWindowing.
     */
    public boolean isUsing1904DateWindowing()
    {
        return isUsing1904DateWindowing;
    }


    /**
     * @param isUsing1904DateWindowing The isUsing1904DateWindowing to set.
     */
    public void setUsing1904DateWindowing(boolean isUsing1904DateWindowing)
    {
        this.isUsing1904DateWindowing = isUsing1904DateWindowing;
    }
    
    /**
     * 
     * @param index
     * @param pic
     */
    public void addPicture(int index, Picture pic)
    {
        pictures.put(index, pic);
    }
    
    /**
     * 
     * @param pic
     * @return add postion
     */
    public int addPicture(Picture pic)
    {
        //check exist 
        Iterator<Integer> iter = pictures.keySet().iterator();
        int index = 0;
        while(iter.hasNext())
        {
            index = iter.next();
            if(pictures.get(index).getTempFilePath().equals(pic.getTempFilePath()))
            {
                //has exist
                return index;
            }
        }
        
        pictures.put(index + 1, pic);
        return index + 1;
    }    
    
    /**
     * 
     * @param index
     * @return
     */
    public Picture getPicture(int index)
    {
        return pictures.get(index);
    }
    
    /**
     * 
     * @return
     */
    public boolean isBefore07Version()
    {
        return before07;            
    }
    
    
    public int getMaxRow()
    {
        if(before07)
        {
            return MAXROW_03;
        }
        else
        {
            return MAXROW_07;
        }
    }
    
    public int getMaxColumn()
    {
        if(before07)
        {
            return MAXCOLUMN_03;
        }
        else
        {
            return MAXCOLUMN_07;
        }
    }
    
    public void setTableFormatManager(TableFormatManager tableFormatManager)
    {
    	this.tableFormatManager = tableFormatManager;
    }
    
    public TableFormatManager getTableFormatManager()
    {
    	return tableFormatManager;
    }
    
    public void destroy()
    {
        
        if(readerHandler != null)
        {
            Message msg = new Message();
            msg.what = MainConstant.HANDLER_MESSAGE_DISPOSE;
            readerHandler.handleMessage(msg);
            
            readerHandler = null;
        }
        
        if(sheets != null)
        {  
            Collection<Sheet> sheetCollection = sheets.values();
            for(Sheet sheet: sheetCollection)
            {
                sheet.dispose();
            }
            sheets.clear();
            sheets = null;
        }
        
        if(fonts != null)
        {
            Collection<Font> fontCollection = fonts.values();
            for(Font font: fontCollection)
            {
                font.dispose();
            }
            fonts.clear();
            fonts = null;
        }
        
        if(colors != null)
        {
            colors.clear();
            colors = null;
        }
        
        if(pictures != null)
        {
            pictures.clear();
            pictures = null;
        }
        
        if(cellStyles != null)
        {            
            Collection<CellStyle> styleCollection = cellStyles.values();
            for(CellStyle cellStyle: styleCollection)
            {
                cellStyle.dispose();
            }
            cellStyles.clear();
            cellStyles = null;
        }
        
        if(sharedString != null)
        {  
            sharedString.clear();
            sharedString = null;
        }
        
        if(themeColor != null)
        {  
            themeColor.clear();
            themeColor = null;
        }
        
        if(schemeColor != null)
        {  
            schemeColor.clear();
            schemeColor = null;
        }
    }
    

    /**
     * 
     */
    public void dispose()
    {
        
        synchronized(this)
        {    
            destroy();
        } 
    }   
    
    protected ReaderHandler readerHandler;
    //
    protected boolean isUsing1904DateWindowing;
    // this holds the Sheet objects attached to this workbook, sheet index is continuous
    protected Map<Integer,Sheet> sheets;
    // this holds the Font objects attached to this workbook, index is continuous
    protected Map<Integer, Font> fonts;
    // this holds the Font objects attached to this workbook
    protected Map<Integer, Integer> colors;
    
    //image data
    private Map<Integer, Picture> pictures;
    //cell styles, index is continuous
    protected Map<Integer, CellStyle> cellStyles;    
    //shared strings, index is continuous
    protected Map<Integer, Object> sharedString;
    
    //theme Color index
    private Map<Integer, Integer> themeColor;
    
    //scheme Color index
    private Map<String, Integer> schemeColor;
    
    //table format manager
    private TableFormatManager tableFormatManager;
    //version 
    private boolean before07;
}
