/*
 * 文件名称:          TableStyleReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:33:41
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import java.io.InputStream;

import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementHandler;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementPath;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.pg.model.tableStyle.TableCellBorders;
import com.nvqquy98.lib.doc.office.pg.model.tableStyle.TableCellStyle;
import com.nvqquy98.lib.doc.office.pg.model.tableStyle.TableStyle;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttributeSetImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-3-22
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class TableStyleReader
{
    private static TableStyleReader tableStyleReader = new TableStyleReader();
    private PGModel pgModel = null;
    private int  defaultFontSize = 12;
    
    /**
     * 
     */
    public static TableStyleReader instance()
    {
        return tableStyleReader;
    }
    
    public void read(PGModel pgModel, PackagePart tableStyle, int defaultFontSize) throws Exception
    {
        this.pgModel = pgModel;
        this.defaultFontSize = defaultFontSize;
        
        // get table style xml
        SAXReader saxreader = new SAXReader();
        try
        {
            InputStream in = tableStyle.getInputStream();
            
            TableStyleSaxHandler preSaxHandler = new TableStyleSaxHandler();
            
            saxreader.addHandler("/tblStyleLst/tblStyle", preSaxHandler);
            
            saxreader.read(in);        
            in.close();
            pgModel = null;
        }
        catch (Exception  e)
        {
            throw e;
        }
        finally
        {
            saxreader.resetHandlers();
        }
    }
    
    private void processTableStyle(Element tablestyleElement)
    {
        TableStyle tableStyle = new TableStyle();
        
        String styleId = tablestyleElement.attributeValue("styleId");
        //whole table
        Element element = tablestyleElement.element("wholeTbl");
        if(element != null)
        {
            tableStyle.setWholeTable(processTableCellStyle(element));
        }        
        
        //band1 horizontal
        element = tablestyleElement.element("band1H");
        if(element != null)
        {
            tableStyle.setBand1H(processTableCellStyle(element));
        } 
        
        //band2 horizontal
        element = tablestyleElement.element("band2H");
        if(element != null)
        {
            tableStyle.setBand2H(processTableCellStyle(element));
        } 
        
        //band1 vertical
        element = tablestyleElement.element("band1V");
        if(element != null)
        {
            tableStyle.setBand1V(processTableCellStyle(element));
        } 
        
        //band2 vertical
        element = tablestyleElement.element("band2V");
        if(element != null)
        {
            tableStyle.setBand2V(processTableCellStyle(element));
        } 
        
        //last column
        element = tablestyleElement.element("lastCol");
        if(element != null)
        {
            tableStyle.setLastCol(processTableCellStyle(element));
        } 
        
        //first column
        element = tablestyleElement.element("firstCol");
        if(element != null)
        {
            tableStyle.setFirstCol(processTableCellStyle(element));
        } 
        
        //last row
        element = tablestyleElement.element("lastRow");
        if(element != null)
        {
            tableStyle.setLastRow(processTableCellStyle(element));
        } 
        
        //first row
        element = tablestyleElement.element("firstRow");
        if(element != null)
        {
            tableStyle.setFirstRow(processTableCellStyle(element));
        } 
        
        pgModel.putTableStyle(styleId, tableStyle);
    }
    
    private TableCellStyle processTableCellStyle(Element tableStyleElement)
    {
    	TableCellStyle tableCellStyle = new TableCellStyle();
    	//cell text style
    	Element cellTextStyleElement = tableStyleElement.element("tcTxStyle");
    	if(cellTextStyleElement != null)
    	{
    		IAttributeSet attr = new AttributeSetImpl();
    		//bold
    		String str = cellTextStyleElement.attributeValue("b");
    		if("on".equals(str))
    		{
    			AttrManage.instance().setFontBold(attr, true);
    		}
    		
    		//Italic
    		str = cellTextStyleElement.attributeValue("i");
    		if("on".equals(str))
    		{
    			AttrManage.instance().setFontItalic(attr, true);
    		}
    		
    		//TTOD: font color
            
    		//set default font size
    		AttrManage.instance().setFontSize(attr, defaultFontSize);
    		
    		tableCellStyle.setFontAttributeSet(attr);
    	}
    	//cell style
    	Element cellStyleElement = tableStyleElement.element("tcStyle");
        //borders
        Element ele = cellStyleElement.element("tcBdr");
        if(ele != null)
        {
            tableCellStyle.setTableCellBorders(getTableCellBorders(ele));
        }
        
        //fill
        tableCellStyle.setTableCellBgFill(cellStyleElement.element("fill"));
        
        
        return tableCellStyle;
    }
    
    private TableCellBorders getTableCellBorders(Element tcBrdElement)
    {
        TableCellBorders tableCellBorders = new TableCellBorders();
        //left
        Element ele = tcBrdElement.element("left");
        if(ele != null)
        {
            tableCellBorders.setLeftBorder(ele.element("ln"));
        }
        
        //right
        ele = tcBrdElement.element("right");
        if(ele != null)
        {
            tableCellBorders.setRightBorder(ele.element("ln"));
        }
        
        //top
        ele = tcBrdElement.element("top");
        if(ele != null)
        {
            tableCellBorders.setTopBorder(ele.element("ln"));
        }
        
        //bottom
        ele = tcBrdElement.element("bottom");
        if(ele != null)
        {
            tableCellBorders.setBottomBorder(ele.element("ln"));
        }
        
        return tableCellBorders;
    }
    
    /**
     * fix very large XML documents
     *
     */
    class TableStyleSaxHandler implements ElementHandler
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
            
            Element elem = elementPath.getCurrent();
            String name = elem.getName();
            try
            {
                if(name.equals("tblStyle"))
                {
                    processTableStyle(elem);
                }                            
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }      
            
            elem.detach();            
        } 
    }
}
