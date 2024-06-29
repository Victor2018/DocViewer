/*
 * 文件名称:          StyleSheet.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:10:35
 */
package com.nvqquy98.lib.doc.office.fc.xls.Reader.shared;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.bg.AShader;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.LinearGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.RadialGradientShader;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementHandler;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementPath;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.sheetProperty.Palette;
import com.nvqquy98.lib.doc.office.ss.model.style.BorderStyle;
import com.nvqquy98.lib.doc.office.ss.model.style.BuiltinFormats;
import com.nvqquy98.lib.doc.office.ss.model.style.CellBorder;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.style.NumberFormat;
import com.nvqquy98.lib.doc.office.ss.model.table.TableFormatManager;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;
import com.nvqquy98.lib.doc.office.system.AbortReaderError;
import com.nvqquy98.lib.doc.office.system.IReader;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-22
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class StyleReader
{    
    private static StyleReader reader = new StyleReader();
    
    /**
     * 
     */
    public static StyleReader instance()
    {
        return reader;
    }
    
    public void getWorkBookStyle(PackagePart styleParts, Workbook book, IReader iReader) throws Exception
    {

        this.book = book;
        this.iReader = iReader;
        
        fontIndex = 0;
        fillIndex = 0;
        borderIndex = 0;        
        styleIndex = 0;
        indexedColor = 0;
        
        fills = new HashMap<Integer, BackgroundAndFill>(5);
        cellBorders = new HashMap<Integer, CellBorder>(5);
        
        getBuiltinNumberFormats();
        
        SAXReader saxreader = new SAXReader();
        try
        {
            StyleSaxHandler handler = new StyleSaxHandler();
            saxreader.addHandler("/styleSheet/numFmts/numFmt", handler);
            saxreader.addHandler("/styleSheet/fonts/font", handler);
            saxreader.addHandler("/styleSheet/fills/fill", handler);
            saxreader.addHandler("/styleSheet/borders/border", handler);
            saxreader.addHandler("/styleSheet/cellXfs/xf", handler);        
            saxreader.addHandler("/styleSheet/colors/indexedColors/rgbColor", handler);
            saxreader.addHandler("/styleSheet/dxfs/dxf", handler);
            
            InputStream in = styleParts.getInputStream();
            saxreader.read(in);        
            in.close();
            dispose();
        }
        finally
        {
            saxreader.resetHandlers();
        }
    }    
    
    /**
     * get number format
     * @param numFmtsElement
     */
    private void getBuiltinNumberFormats()
    {
        //get BuiltinFormats
        final String[] formats = BuiltinFormats.getAll();
        int count = formats.length;
        numFmts = new HashMap<Integer, NumberFormat>(count + 20);
        
        for(int i =0; i < count; i++)
        {
            numFmts.put(i, new NumberFormat((short)i, formats[i]));
        }       
    }
    
    private short getColorIndex( Element clrElement)
    {
        int index = 0;
        if(clrElement != null)
        {
            if(clrElement.attribute("theme") != null)
            {
                //theme color, get from theme xml(clrScheme element)
                int themeIndex = Integer.parseInt(clrElement.attributeValue("theme"));
                
                index = book.getThemeColorIndex(themeIndex);
                //tint
                if(clrElement.attribute("tint") != null)
                {
                    //new =  (255 - previousValue) * tint + previousValue
                    double tint = Double.parseDouble(clrElement.attributeValue("tint"));
                    //get color(argb)
                    int color = book.getColor(index);
                    color = ColorUtil.instance().getColorWithTint(color, tint);
                    
                    index = book.addColor(color);
                }
                
            }
            else if(clrElement.attribute("rgb") != null)
            {
                //just parse last 6 char of value string
                String val = clrElement.attributeValue("rgb");
                if(val.length() > 6)
                {
                    val = val.substring(val.length() - 6);
                }
                index = Integer.parseInt(val, 16);
                //put argb to workbook color list
                index = book.addColor((0xFF << 24) | index);
                
                
            }
            else if(clrElement.attribute("indexed") != null)
            {
                //get from paletter
                index = Integer.parseInt(clrElement.attributeValue("indexed")); 
                if(index == Palette.FIRST_COLOR_INDEX + Palette.STANDARD_PALETTE_SIZE)
                {
                    index = 0/*Palette.FIRST_COLOR_INDEX*/;
                }
                else if(index > Palette.FIRST_COLOR_INDEX + Palette.STANDARD_PALETTE_SIZE)
                {
                    index = Palette.FIRST_COLOR_INDEX + 1;
                }
            }
        }

        return (short)index;
    }
    
    /**
     * 
     * @param numFmt
     * @return
     */
    private NumberFormat processNumberFormat(Element numFmt)
    {
    	//number format ID
        int Id = Integer.parseInt(numFmt.attribute("numFmtId").getValue());
        
        //formate code
        String formatCode = numFmt.attribute("formatCode").getValue();
        
        return new NumberFormat((short)Id, formatCode);
    }
    
    /**
     * get font list
     * @param fontsElement
     */
    private Font processFont(Element fontElement)
    {
        double sz = 12;
        String name;
        Font font;
        Element e;
        
        font = new Font();
        
        //index
        font.setIndex(fontIndex);
        //vertAlign
        e = fontElement.element("fontElement");
        if(e != null)
        {
            String val = e.attributeValue("val");
            if (val.equalsIgnoreCase("superscript"))
            {
                font.setSuperSubScript((byte)Font.SS_SUPER);
            }
            else if (val.equalsIgnoreCase("subscript"))
            {
                font.setSuperSubScript((byte)Font.SS_SUB);
            }
            else
            {
                font.setSuperSubScript((byte)Font.SS_NONE);
            }
        }
        else
        {
            font.setSuperSubScript((byte)Font.SS_NONE);
        }
        //font sz
        e = fontElement.element("sz");
        if (e != null)
        {
            sz = Double.parseDouble(e.attributeValue("val"));
        }
        font.setFontSize(sz);

              
        //color        
        e = fontElement.element("color");
        font.setColorIndex(getColorIndex(e));
          
        //name
        if(fontElement.element("name") != null)
        {
            name = fontElement.element("name").attributeValue("val");
            font.setName(name);
        }
                
        //bold
        e = fontElement.element("b");
        if(e != null)
        {
            font.setBold(
                e.attributeValue("val") == null ? true : Boolean.parseBoolean(e.attributeValue("val")));                       
        }
        
        //italic
        e = fontElement.element("i");
        if(e != null)
        {
            font.setItalic(
                e.attributeValue("val") == null ? true : Boolean.parseBoolean(e.attributeValue("val")));
        }
        
        //underline
        e = fontElement.element("u");
        if(e != null)
        {
            if(e.attributeValue("val") != null)
            {
                font.setUnderline(e.attributeValue("val"));
            }
            else
            {
                font.setUnderline(Font.U_SINGLE);
            }                
        }
        
        
        //strike out
        e = fontElement.element("strike");
        if(e != null)
        {
            font.setStrikeline(
                e.attributeValue("val") == null ? true : Boolean.parseBoolean(e.attributeValue("val")));
                         
        }  
        
        return font;        
    }
    
    private BackgroundAndFill processFill(Element fillElement)
    {
        Element patternFill = fillElement.element("patternFill");
        if(patternFill != null)
        {
        	BackgroundAndFill fill = new BackgroundAndFill();
            
            //pattern type
            String str = patternFill.attributeValue("patternType");
            byte type = BackgroundAndFill.FILL_SOLID;
            if("none".equalsIgnoreCase(str))
            {
                return null;
            }
            //fgclr
            Element ele = patternFill.element("fgColor");
            if(ele != null)
            {
                fill.setForegroundColor(book.getColor(getColorIndex(ele)));
                //
                fill.setFillType(type);
            }
            
            //bgClr
            ele = patternFill.element("bgColor");
            if(ele != null)
            {
                fill.setBackgoundColor(book.getColor(getColorIndex(ele)));
            }
            return fill;
        }
        else if(fillElement.element("gradientFill") != null)
        {
        	Element gradientFill = fillElement.element("gradientFill");
            
        	//Gradient stops list
        	List<Element> gsLst = gradientFill.elements("stop");
        	int[] colors = new int[gsLst.size()];
        	float[] positions = new float[gsLst.size()];
        	for(int i = 0; i < gsLst.size(); i++)
        	{
        		Element stop = gsLst.get(i);
        		positions[i] = Float.parseFloat(stop.attributeValue("position"));
        		colors[i] = book.getColor(getColorIndex(stop.element("color")));
        	}
        	
        	BackgroundAndFill fill = new BackgroundAndFill();
            AShader shader = null;
            if(!"path".equalsIgnoreCase(gradientFill.attributeValue("type")))
        	{
            	fill.setFillType(BackgroundAndFill.FILL_SHADE_LINEAR);
            	//degree
                int degree = 0;
                if(gradientFill.attributeValue("degree") != null)
                {
                	degree = Integer.parseInt(gradientFill.attributeValue("degree"));
                }  
                shader = new LinearGradientShader(degree, colors, positions);
        	}
        	else
        	{
        		fill.setFillType(BackgroundAndFill.FILL_SHADE_RADIAL);
        		shader = new RadialGradientShader(getRadialCenterType(gradientFill), colors, positions);
        	}
            
            fill.setShader(shader);
            return fill;
        }
        
        return null;
    }
    
    /**
     * 
     * @param fillToRect
     * @return
     */
    private static int getRadialCenterType(Element fillToRect)
    {
    	if(fillToRect != null)
    	{
    		String l = fillToRect.attributeValue("left");
        	String t = fillToRect.attributeValue("top");
        	String r = fillToRect.attributeValue("right");
        	String b = fillToRect.attributeValue("bottom");
        	
        	if("1".equalsIgnoreCase(l) && "1".equalsIgnoreCase(r) && "1".equalsIgnoreCase(b) && "1".equalsIgnoreCase(t))
        	{
        		return RadialGradientShader.Center_BR;
        	}        	
        	else if("1".equalsIgnoreCase(b) && "1".equalsIgnoreCase(t))
        	{
        		return RadialGradientShader.Center_BL;
        	}  
        	else if("1".equalsIgnoreCase(l) && "1".equalsIgnoreCase(r))
        	{
        		return RadialGradientShader.Center_TR;
        	}
        	else if("0.5".equalsIgnoreCase(l) && "0.5".equalsIgnoreCase(t)
        			&& "0.5".equalsIgnoreCase(r) && "0.5".equalsIgnoreCase(b))
        	{
        		return RadialGradientShader.Center_Center;
        	}
    	}    	
    	
    	return RadialGradientShader.Center_TL;
    }
    
    private CellBorder processBorder(Element cellBorderElement)
    {
        Element ele;
        String style;
        short colorIdx;
        BorderStyle boderStyle;
        CellBorder cellBorder = new CellBorder();
        
        //left
        ele = cellBorderElement.element("left");
        if(ele != null)
        {
            style = ele.attributeValue("style");
            colorIdx = getColorIndex(ele.element("color"));
            boderStyle = new BorderStyle(style, colorIdx);  
            cellBorder.setLeftBorder(boderStyle);
        }
        
        //top
        ele = cellBorderElement.element("top");
        if(ele != null)
        {
            style = ele.attributeValue("style");
            colorIdx = getColorIndex(ele.element("color"));
            boderStyle = new BorderStyle(style, colorIdx);  
            cellBorder.setTopBorder(boderStyle);               
        }
        
        //right
        ele = cellBorderElement.element("right");
        if(ele != null)
        {
            style = ele.attributeValue("style");
            colorIdx = getColorIndex(ele.element("color"));
            boderStyle = new BorderStyle(style, colorIdx);  
            cellBorder.setRightBorder(boderStyle);               
        }
        
        //bottom
        ele = cellBorderElement.element("bottom");
        if(ele != null)
        {
            style = ele.attributeValue("style");
            colorIdx = getColorIndex(ele.element("color"));
            boderStyle = new BorderStyle(style, colorIdx);  
            cellBorder.setBottomBorder(boderStyle);               
        }
        
        return cellBorder;
    }
    
    private void processCellStyleAlignment(CellStyle cellStyle, Element alignment)
    {
    	if(alignment.attributeValue("vertical") != null)
        {
            cellStyle.setVerticalAlign(alignment.attributeValue("vertical"));
        }
        
        if(alignment.attributeValue("horizontal") != null)
        {
            cellStyle.setHorizontalAlign(alignment.attributeValue("horizontal"));
        }        
        
        //rotation
        if(alignment.attributeValue("textRotation") != null)
        {
            try {
                cellStyle.setRotation((short)Integer.parseInt(alignment.attributeValue("textRotation")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        //wrapText
        if(alignment.attributeValue("wrapText") != null)
        {
            try {
                cellStyle.setWrapText(Integer.parseInt(alignment.attributeValue("wrapText")) != 0);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        //indent
        if(alignment.attributeValue("indent") != null)
        {
            try {
                cellStyle.setIndent((short)Integer.parseInt(alignment.attributeValue("indent")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    
    private CellStyle processCellStyle(Element styleElement)
    {
        Element ele;
        int idx;
        CellStyle cellStyle= new CellStyle();        
        
        //numFmtId
        String value = styleElement.attributeValue("numFmtId");
        idx = (value == null ? 0 : Integer.parseInt(value));
        if(numFmts.get(idx) != null)
        {
            cellStyle.setNumberFormat(numFmts.get(idx));
        }
        
        //fontId
        value = styleElement.attributeValue("fontId");
        idx = (value == null ? 0 : Integer.parseInt(value));
        cellStyle.setFontIndex((short)idx);
        
        //fillId
        value = styleElement.attributeValue("fillId");
        idx = (value == null ? 0 : Integer.parseInt(value));
        cellStyle.setFillPattern(fills.get(idx));
        
        //borderId
        value = styleElement.attributeValue("borderId");
        idx = (value == null ? 0 : Integer.parseInt(value));
        cellStyle.setBorder(cellBorders.get(idx));
        
        //alignment
        ele = styleElement.element("alignment");
        if(ele != null)
        {
        	processCellStyleAlignment(cellStyle, ele);
        }
        return cellStyle;
    }    
    
    /**
     * get indexed colors
     * @param rgbColor
     */
    private int processIndexedColors(Element rgbColor)
    {
        String val = rgbColor.attributeValue("rgb");
        if(val.length() > 6)
        {
            val = val.substring(val.length() - 6);
        }
        int color = Integer.parseInt(val, 16);
        //put argb to workbook color list
        return (0xFF << 24) | color;        
    }
    
    private void processTableFormat(Element dfx)
    {
    	if(tableFormatManager == null)
    	{
    		tableFormatManager = new TableFormatManager(5);
    		book.setTableFormatManager(tableFormatManager);
    	}
    	
        CellStyle cellStyle= new CellStyle();        
        
        //numFmtId
        Element ele = dfx.element("numFmt");
        if(ele != null)
        {
        	cellStyle.setNumberFormat(processNumberFormat(ele));
        }
        
        //font
        ele = dfx.element("font");
        if(ele != null)
        {
        	book.addFont(fontIndex, processFont(ele));
        	cellStyle.setFontIndex((short)fontIndex++);
        }        
        
        //fill
        ele = dfx.element("fill");
        if(ele != null)
        {
        	cellStyle.setFillPattern(processFill(ele));
        }
        
        //border
        ele = dfx.element("border");
        if(ele != null)
        {
        	cellStyle.setBorder(processBorder(ele));
        }
        
        //alignment
        ele = dfx.element("alignment");
        if(ele != null)
        {
        	processCellStyleAlignment(cellStyle, ele);
        }
        
        tableFormatManager.addFormat(cellStyle);
    }
    
    private void dispose()
    {
        book = null;
        iReader = null;
        tableFormatManager = null;
        
        if(numFmts != null)
        {
            numFmts.clear();
            numFmts = null;
        }
        
        if(cellBorders != null)
        {
            cellBorders.clear();
            cellBorders = null;
        }
        if(fills != null)
        {
            fills.clear();
            fills = null;
        }
        
    }
    
    /**
     * fix very large XML documents
     *
     */
    class StyleSaxHandler implements ElementHandler
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
            if(iReader.isAborted())
            {
                throw new AbortReaderError("abort Reader");
            }
            
            Element elem = elementPath.getCurrent();
            String name = elem.getName();
            if(name.equals("numFmt"))
            {
            	NumberFormat numFmt = processNumberFormat(elem);               
                
                numFmts.put((int)numFmt.getNumberFormatID(), numFmt);
            }
            else if(name.equals("font"))
            {
            	book.addFont(fontIndex++, processFont(elem));
            }
            else if(name.equals("fill"))
            {
            	fills.put(fillIndex++,  processFill(elem));
            } 
            else if(name.equals("border"))
            {
                cellBorders.put(borderIndex++, processBorder(elem));
            } 
            else if(name.equals("xf"))
            {
                book.addCellStyle(styleIndex++, processCellStyle(elem)); 
            } 
            else if(name.equals("rgbColor"))
            {
            	book.addColor(indexedColor++, processIndexedColors(elem));
            }
            else if(name.equals("dxf"))
            {
            	processTableFormat(elem);
            }
            
            elem.detach();            
        }
        
    }
    
    
    private Workbook book;
    private IReader iReader;
    
    private Map<Integer, NumberFormat> numFmts;    
    private Map<Integer, CellBorder> cellBorders;    
    private Map<Integer, BackgroundAndFill> fills;
    private TableFormatManager tableFormatManager;
    
    private int fontIndex;    
    private int fillIndex;
    private int borderIndex;
    
    private int styleIndex;
    
    private int indexedColor;
}
