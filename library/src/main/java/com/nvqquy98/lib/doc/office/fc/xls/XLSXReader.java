/*
 * 文件名称:          XLSXReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:26:38
 */
package com.nvqquy98.lib.doc.office.fc.xls;

import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import com.nvqquy98.lib.doc.bean.DocSourceType;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementHandler;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementPath;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipCollection;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.xls.Reader.WorkbookReader;
import com.nvqquy98.lib.doc.office.fc.xls.Reader.shared.StyleReader;
import com.nvqquy98.lib.doc.office.fc.xls.Reader.shared.ThemeColorReader;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.Palette;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;
import com.nvqquy98.lib.doc.office.system.AbortReaderError;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.StopReaderError;


/**
 * 解析xlsx文件
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-22
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class XLSXReader extends SSReader
{

    /**
     * 
     * @param filePath
     */
    public XLSXReader(IControl control, String filePath,int docSourceType)
    {
        this.control = control;
        this.filePath = filePath;
        this.docSourceType = docSourceType;
    }
    
    /**
     * 
     */
    public Object getModel() throws Exception
    {        
        book = new Workbook(false);

        InputStream is = null;
        switch (docSourceType) {
            case DocSourceType.URL:
                URL url = new URL(filePath);
                is = url.openStream();
                break;
            case DocSourceType.URI:
                Uri uri = Uri.parse(filePath);
                is = control.getActivity().getContentResolver().openInputStream(uri);
                break;
            case DocSourceType.PATH:
                is = new FileInputStream(filePath);
                break;
            case DocSourceType.ASSETS:
                is = control.getActivity().getAssets().open(filePath);
                break;
        }
        zipPackage = new ZipPackage(is);

        initPackagePart();
        
        processWorkbook();
        
        return book;
    }    
    
    /**
     * Construct POIXMLDocumentPart representing a "core document" package part.
     * @throws Exception 
     */
    private void initPackagePart() throws Exception
    {
        PackageRelationship coreRel = zipPackage.getRelationshipsByType(
            PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
        if (!coreRel.getTargetURI().toString().equals("/xl/workbook.xml"))
        {
            throw new Exception("Format error");
        }
        this.packagePart = zipPackage.getPart(coreRel);
    }

    /**
     * 
     */
    private void processWorkbook() throws Exception
    {
        getWorkBookSharedObjects();
              
        WorkbookReader.instance().read(zipPackage, packagePart, book, this);
    }

    /**
     * set workbook styles
     * @throws Exception
     */
    private void getWorkBookSharedObjects()  throws Exception
    {
        //convert Previous Version Color Palette
        getPaletteColor();
        
        //get theme color first
        getThemeColor(packagePart);

        //style part(numFmt,font ...)
        getStyles(packagePart);  
        
        //shared Strings part
        getSharedString(packagePart);        
        
    }
    
    /**
     * put palette color into color list
     */
    private void getPaletteColor()
    {
        Palette palette = new Palette();
        int index = Palette.FIRST_COLOR_INDEX;
        byte[] rgb = palette.getColor(index);
        
        while(rgb != null)
        {
            book.addColor(index++, ColorUtil.rgb(rgb[0], rgb[1], rgb[2]));       
        
            rgb = palette.getColor(index);
        }  
        
        palette.dispose();
        palette = null;
    }
    
    /**
     * 
     * @param documentPart
     * @throws Exception
     */
    private void getThemeColor(PackagePart documentPart) throws Exception
    {
        if(documentPart.getRelationshipsByType(PackageRelationshipTypes.THEME_PART).size() <= 0)
        {
            return;
        }
        
        //get theme part
        PackageRelationship styleRel = documentPart.getRelationshipsByType(
            PackageRelationshipTypes.THEME_PART).getRelationship(0);
        PackagePart  themeParts = zipPackage.getPart(styleRel.getTargetURI());
        
        //get theme color index 
        ThemeColorReader.instance().getThemeColor(themeParts, book);        
       
    }
    
    /**
     * get shared string
     * @param documentPart
     * @throws Exception
     */
    private void getSharedString(PackagePart documentPart) throws Exception
    {
        PackageRelationshipCollection sharedStringsRelCollection = documentPart.getRelationshipsByType(
            PackageRelationshipTypes.SHAREDSTRINGS_PART);
        if(sharedStringsRelCollection.size() <= 0)
        {
            return;
        }
        
        PackageRelationship sharedStringsRel = sharedStringsRelCollection.getRelationship(0);        
        PackagePart  sharedStringsParts = zipPackage.getPart(sharedStringsRel.getTargetURI());
        sharedStringIndex = 0;
        
        SAXReader saxreader = new SAXReader();
        
        try
        {
            saxreader.addHandler("/sst/si", new SharedStringSaxHandler());
            InputStream in = sharedStringsParts.getInputStream();
            saxreader.read(in);  
            in.close();            
        }
        finally
        {
            saxreader.resetHandlers();
        }
    }
    
    /**
     * get style
     * @param documentPart
     * @throws Exception
     */
    private void getStyles(PackagePart documentPart) throws Exception
    {
        if(documentPart.getRelationshipsByType(PackageRelationshipTypes.STYLE_PART).size() <= 0)
        {
            return;
        }
        
        //get style part
        PackageRelationship styleRel = documentPart.getRelationshipsByType(
            PackageRelationshipTypes.STYLE_PART).getRelationship(0);
        PackagePart  styleParts = zipPackage.getPart(styleRel.getTargetURI());
        
        StyleReader.instance().getWorkBookStyle(styleParts, book, this);
    }
    
    /**
     * 
     * @param file
     * @param key
     * @return
     */
    public boolean searchContent(File file, String key) throws Exception
    {
        key = key.toLowerCase();
        
        zipPackage = new ZipPackage(file.getAbsolutePath());
        PackageRelationship coreRel = zipPackage.getRelationshipsByType(
            PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
        this.packagePart = zipPackage.getPart(coreRel);
        
        boolean hasSearched;
        //search shared Strings part first
        if(searchContent_SharedString(packagePart, key))
        {
            hasSearched = true;
        }
        else
        {
            //search in sheet
            hasSearched = WorkbookReader.instance().searchContent(zipPackage, this, packagePart, key);
        }
        
        dispose();
        
        return hasSearched;
    }
    
    /**
     * 
     * @param documentPart
     * @param key
     * @return
     * @throws Exception
     */
    private boolean searchContent_SharedString(PackagePart documentPart, String key) throws Exception
    {
        PackageRelationshipCollection sharedStringsRelCollection = documentPart.getRelationshipsByType(
            PackageRelationshipTypes.SHAREDSTRINGS_PART);
        if(sharedStringsRelCollection.size() <= 0)
        {
            return false;
        }
        
        PackageRelationship sharedStringsRel = sharedStringsRelCollection.getRelationship(0);        
        PackagePart  sharedStringsParts = zipPackage.getPart(sharedStringsRel.getTargetURI());
        
        this.key = key;
        searched = false;
        
        SAXReader saxreader = new SAXReader();
        
        try
        {
            saxreader.addHandler("/sst/si", new SearchSharedStringSaxHandler());
            InputStream in = sharedStringsParts.getInputStream();
            saxreader.read(in);
            in.close();
        }
        catch(StopReaderError e)
        {
            return true;
        }
        finally
        {
            saxreader.resetHandlers();
        }
       
        return searched;
    }
    
    
    
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        
        filePath = null;
        book = null;
        zipPackage = null;
        packagePart = null;
        key = null;
    }
    
    
    /**
     * fix very large XML documents
     *
     */
    class SharedStringSaxHandler implements ElementHandler
    {
        
        /**
         * 
         *
         */
        public void onStart(ElementPath elementPath)
        {
        }

        /**
         * @throws Exception 
         * 
         *
         */
        public void onEnd(ElementPath elementPath)
        {
            if (abortReader)
            {
                throw new AbortReaderError("abort Reader");
            }
            
            Element elem = elementPath.getCurrent();
            String name = elem.getName();
            if(name.equals("si"))
            {
                Element ele;
                String str;
                ele = elem.element("t");
                if(ele != null)
                {
                    book.addSharedString(sharedStringIndex, ele.getText());
                }
                else
                {                  
                    book.addSharedString(sharedStringIndex, elem);
                }
                
                sharedStringIndex++;
            }
            
            elem.detach();
        }
        
    }
    
    /**
     * fix very large XML documents
     *
     */
    class SearchSharedStringSaxHandler implements ElementHandler
    {
        
        /**
         * 
         *
         */
        public void onStart(ElementPath elementPath)
        {
            
        }

        /**
         * @throws Exception 
         * 
         *
         */
        public void onEnd(ElementPath elementPath)
        {            
            if (abortReader)
            {
                throw new AbortReaderError("abort Reader");
            }
            
            Element stringItem = elementPath.getCurrent();
            String name = stringItem.getName();
            if(name.equals("si"))
            {
                Element ele = stringItem.element("t");
                if(ele != null)
                {
                    if(ele.getText().toLowerCase().contains(key))
                    {
                        searched = true;
                    }                
                }
                else
                {
                    @ SuppressWarnings("unchecked")
                    Iterator<Element> iter1 = stringItem.elementIterator("r");
                    String str = "";
                    while(iter1.hasNext())
                    {
                        ele = iter1.next();
                        str = str + ele.element("t").getText();
                    }
                    
                    if(str.toLowerCase().contains(key))
                    {
                        searched = true;
                    }   
                }                
            }            
            stringItem.detach();  
            if(searched)
            {
                throw new StopReaderError("stop");
            }
        }
        
    }
    
    //
    private String filePath;
    private int docSourceType;
    //
    private ZipPackage zipPackage;
    
    private Workbook book;
    //
    private PackagePart packagePart;
    
    private int sharedStringIndex;
    //search 
    private String key;
    
    private boolean searched;
}
