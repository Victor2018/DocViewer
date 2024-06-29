/*
 * 文件名称:          CellReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:36:38
 */
package com.nvqquy98.lib.doc.office.fc.xls.Reader;

import java.util.List;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.ParaAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.RunAttr;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttributeSetImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.LeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.util.ReferenceUtil;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-24
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class CellReader
{
    private final static short CELLTYPE_BOOLEAN = 0;
    private final static short CELLTYPE_NUMBER = 1;
    private final static short CELLTYPE_ERROR = 2;
    private final static short CELLTYPE_SHAREDSTRING = 3;
    private final static short CELLTYPE_STRING = 4;
    private final static short CELLTYPE_INLINESTRING = 5;
    
    private static CellReader reader = new CellReader();
    
    /**
     * 
     */
    public static CellReader instance()
    {
        return reader;
    }
    
    private boolean isValidateCell(Sheet sheet, Element cellElement)
    {        
        if(cellElement.attributeValue("t") != null)
        {
            return true;
        }
        
        //cell content        
        Element v = cellElement.element("v");
        if(v != null)
        {
            return true;
        }
        

        Workbook book = sheet.getWorkbook();
        //style index
        if(cellElement.attributeValue("s") != null)
        {
            int styleIndex = Integer.parseInt(cellElement.attributeValue("s"));
            CellStyle cellStyle = book.getCellStyle(styleIndex);
            if(Workbook.isValidateStyle(cellStyle))
            {
                //has border
                return true;
            }  
        }    
        else
        {
            //Reference
            String ref = cellElement.attributeValue("r");
            int col = ReferenceUtil.instance().getColumnIndex(ref);
            Row row = sheet.getRow(ReferenceUtil.instance().getRowIndex(ref));
            
            if((row != null && Workbook.isValidateStyle(book.getCellStyle(row.getRowStyle())))
                || Workbook.isValidateStyle(book.getCellStyle(col)))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 
     * @param book
     * @param cellElement
     * @return
     */
    public  Cell getCell(Sheet sheet, Element cellElement)
    {
        Cell cell;
        
        if(!isValidateCell(sheet, cellElement))
        {
            return null;
        }
        
        //cell type
        short cellType = getCellType(cellElement.attributeValue("t"));        
        switch(cellType)
        {
            case CELLTYPE_BOOLEAN:
                cell = new Cell(Cell.CELL_TYPE_BOOLEAN);
                break;
            case CELLTYPE_NUMBER:
                cell = new Cell(Cell.CELL_TYPE_NUMERIC);
                break;
            case CELLTYPE_INLINESTRING:
            case CELLTYPE_SHAREDSTRING:
            case CELLTYPE_STRING:
            case CELLTYPE_ERROR:
                cell = new Cell(Cell.CELL_TYPE_STRING);
                break;
            default:
                cell = new Cell(Cell.CELL_TYPE_BLANK);
                break;                    
        }    
        
        //Reference
        String ref = cellElement.attributeValue("r");
        cell.setColNumber(ReferenceUtil.instance().getColumnIndex(ref));
        cell.setRowNumber(ReferenceUtil.instance().getRowIndex(ref));
        
        Workbook book = sheet.getWorkbook();
        //style index
        int styleIndex = 0;
        if(cellElement.attributeValue("s") != null)
        {
            styleIndex = Integer.parseInt(cellElement.attributeValue("s"));
        }
        else
        {
            styleIndex = sheet.getColumnStyle(cell.getColNumber());
        }
        cell.setCellStyle(styleIndex);
        
        //cell content        
        Element v = cellElement.element("v");
        if(v != null)
        {
            String value = v.getText();
            //cell value
            if(cellType == CELLTYPE_SHAREDSTRING)
            {
                int sstIndex = Integer.parseInt(value);
                
                Object sst = book.getSharedItem(sstIndex);
                if(sst instanceof Element)
                {
                    //process complex cell text
                    cell.setSheet(sheet);
                    SectionElement secElement = processComplexSST(cell, (Element)sst);
                    sstIndex = book.addSharedString(secElement);
                } 
                cell.setCellValue(sstIndex);
            }
            else if(cellType == CELLTYPE_STRING || cellType == CELLTYPE_ERROR)
            {
                cell.setCellValue(book.addSharedString(value));
            }
            else if(cellType == CELLTYPE_NUMBER)
            {
                cell.setCellValue(Double.parseDouble(value));
            }
            else if(cellType ==CELLTYPE_BOOLEAN)
            {
                cell.setCellValue(Integer.parseInt(value) != 0);                
            }
            else
            {
                cell.setCellValue(value);
            }
        }
       
        
        return cell;
    }  
    
    /**
     * 
     * @param type
     * @return
     */
    private  short getCellType(String type)
    {
        if(type == null || type.equalsIgnoreCase("n"))
        {
            return CELLTYPE_NUMBER;
        }
        else if(type.equalsIgnoreCase("b"))
        {
            return CELLTYPE_BOOLEAN;
        }
        else if(type.equalsIgnoreCase("s"))
        {
            return CELLTYPE_SHAREDSTRING;
        }
        else if(type.equalsIgnoreCase("str"))
        {
            return CELLTYPE_STRING;
        }
        else if(type.equalsIgnoreCase("inlineStr"))
        {
            return CELLTYPE_INLINESTRING;
        }
        else 
        {
            return CELLTYPE_ERROR;
        }
    }

    private SectionElement processComplexSST(Cell cell, Element si)
    {
        SectionElement secElem = new SectionElement();
        // 开始Offset
        secElem.setStartOffset(0);
        // 属性
        IAttributeSet attr = secElem.getAttribute();
        
        // 左边距
        AttrManage.instance().setPageMarginLeft(attr, Math.round(SSConstant.SHEET_SPACETOBORDER * MainConstant.PIXEL_TO_TWIPS));
        // 右边距
        AttrManage.instance().setPageMarginRight(attr, Math.round(SSConstant.SHEET_SPACETOBORDER * MainConstant.PIXEL_TO_TWIPS));
        // 上边距
        AttrManage.instance().setPageMarginTop(attr, 0);
        // 下边框
        AttrManage.instance().setPageMarginBottom(attr, 0);
        
        byte verAlign;
        switch(cell.getCellStyle().getVerticalAlign())
        {
            case CellStyle.VERTICAL_TOP:
                verAlign = WPAttrConstant.PAGE_V_TOP;
                break;
            case CellStyle.VERTICAL_CENTER:
                verAlign = WPAttrConstant.PAGE_V_CENTER;
                break;
            case CellStyle.VERTICAL_JUSTIFY:
                verAlign = WPAttrConstant.PAGE_V_JUSTIFIED;
                break;
            case CellStyle.VERTICAL_BOTTOM:
                verAlign = WPAttrConstant.PAGE_V_BOTTOM;
                break;
            default:
                verAlign = WPAttrConstant.PAGE_V_TOP;
                break;
        }
        AttrManage.instance().setPageVerticalAlign(attr, verAlign);
        //font id
        int font = cell.getCellStyle().getFontIndex();
        
        offset = 0;
        paraElem = new ParagraphElement();
        paraElem.setStartOffset(offset);
        attrLayout =  new AttributeSetImpl(); 
        
        ParaAttr.instance().setParaAttribute(cell.getCellStyle(), paraElem.getAttribute(), attrLayout); 
        
        paraElem = processRun(cell,secElem, si, font);
        
        paraElem.setEndOffset(offset);
        secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
        secElem.setEndOffset(offset);
        
        offset = 0;
        paraElem = null;
        attrLayout = null;
        leaf = null;
        
        return secElem;
    }
    

    private ParagraphElement processRun(Cell cell, SectionElement secElem, Element si, int fontID)
    {
        Workbook book = cell.getSheet().getWorkbook();
        List<Element> rs = si.elements();
        CellStyle cellStyle = cell.getCellStyle();
        
        //ignore newline character
        boolean ignoreNewline = false;
        if(!cellStyle.isWrapText())
        {
            ignoreNewline = true;
        }
        
        // 如果没有 r 元素，说明只有一个回车符的段落
        if (rs.size() == 0)
        {
            leaf = new LeafElement("\n");
            RunAttr.instance().setRunAttribute(book, fontID, null, leaf.getAttribute(), attrLayout);
            
            leaf.setStartOffset(offset);
            offset++;
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            return paraElem;
        }
        
        for (Element r : rs)
        {
            if(r.getName().equalsIgnoreCase("r"))
            {
                Element t = r.element("t");
                if (t != null)
                {
                    String text = t.getText();
                    int len = text.length();
                    if (len > 0)
                    {
                        if(ignoreNewline)
                        {
                            text = text.replace("\n", "");
                            leaf = new LeafElement(text);
                            // 属性
                            RunAttr.instance().setRunAttribute(book, fontID, r.element("rPr"), leaf.getAttribute(), attrLayout);
                            // 开始 offset
                            leaf.setStartOffset(offset);
                            offset += text.length();
                            // 结束 offset
                            leaf.setEndOffset(offset);
                            paraElem.appendLeaf(leaf);
                        }
                        else
                        {
                            if(!text.contains("\n"))
                            {
                                leaf = new LeafElement(text);
                                // 属性
                                RunAttr.instance().setRunAttribute(book, fontID, r.element("rPr"), leaf.getAttribute(), attrLayout);
                                // 开始 offset
                                leaf.setStartOffset(offset);
                                offset += text.length();
                                // 结束 offset
                                leaf.setEndOffset(offset);
                                paraElem.appendLeaf(leaf);
                            }
                            else
                            {
                                processBreakLine(cell, secElem, fontID, r, text);
                            }
                        }
                        
                    }
                } 
            }
            
        }
        if (leaf != null)
        {
            leaf.setText(leaf.getText(null) + "\n");
            offset++;
        }
        
        return paraElem;
    }
    
    /**
     * 
     * @param cell
     * @param secElem
     * @param paraElem
     * @param attrLayout
     * @param fontID
     * @param r
     * @param leaf
     */    
    private void processBreakLine(Cell cell, SectionElement secElem, int fontID, Element r, String text)
    {
        Workbook book = cell.getSheet().getWorkbook();
        if(text == null || text.length() == 0)
        {
            return;
        }
        
        int len = text.length();
        if(text.charAt(0) == '\n')
        {
            //Text Line Break, for last paragrapha
            if (leaf != null)
            {
                leaf.setText(leaf.getText(null) + "\n");
                offset++;
            }
            else
            {
                leaf = new LeafElement("\n");
                // 属性
                RunAttr.instance().setRunAttribute(book, fontID, r.element("rPr"), leaf.getAttribute(), attrLayout);
                // 开始 offset
                leaf.setStartOffset(offset);
                offset++;
                leaf.setEndOffset(offset);
                paraElem.appendLeaf(leaf);
            }
            
            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);                
            leaf = null;
            
            text = text.substring(1);
            
            paraElem = new ParagraphElement();
            paraElem.setStartOffset(offset);
            attrLayout =  new AttributeSetImpl(); 
            ParaAttr.instance().setParaAttribute(cell.getCellStyle(), paraElem.getAttribute(), attrLayout);
            
            processBreakLine(cell, secElem, fontID, r, text);
        }
        else if(text.charAt(len - 1) == '\n')
        {
            
            leaf = new LeafElement(text.substring(0, text.indexOf("\n") + 1));
            // 属性
            RunAttr.instance().setRunAttribute(book, fontID, r.element("rPr"), leaf.getAttribute(), attrLayout);
            // 开始 offset
            leaf.setStartOffset(offset);
            offset += leaf.getText(null).length();
            // 结束 offset
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            
            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
            
            text = text.substring(text.indexOf("\n") + 1);
            processBreakLine(cell, secElem, fontID, r, text);
        }
        else
        {
            String[] items = text.split("\n");
            int cnt = items.length;

            //last leaf of first paragraph
            text = items[0] + "\n";
            leaf = new LeafElement(text);
            // 属性
            RunAttr.instance().setRunAttribute(book, fontID, r.element("rPr"), leaf.getAttribute(), attrLayout);
            // 开始 offset
            leaf.setStartOffset(offset);
            offset += text.length();
            // 结束 offset
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
            
            //cnt - 2 new paragraph
            int index = 1;
            while(index < cnt - 1)
            {
                paraElem = new ParagraphElement();
                paraElem.setStartOffset(offset);
                attrLayout =  new AttributeSetImpl(); 
                ParaAttr.instance().setParaAttribute(cell.getCellStyle(), paraElem.getAttribute(), attrLayout);
                text = items[index] + "\n";
                leaf = new LeafElement(text);
                // 属性
                RunAttr.instance().setRunAttribute(book, fontID, r.element("rPr"), leaf.getAttribute(), attrLayout);
                // 开始 offset
                leaf.setStartOffset(offset);
                offset += text.length();
                // 结束 offset
                leaf.setEndOffset(offset);
                paraElem.appendLeaf(leaf);
                paraElem.setEndOffset(offset);
                secElem.appendParagraph(paraElem, WPModelConstant.MAIN);

                index++;
            }
            
            //first leaf of last paragraph
            paraElem = new ParagraphElement();
            paraElem.setStartOffset(offset);
            attrLayout =  new AttributeSetImpl(); 
            ParaAttr.instance().setParaAttribute(cell.getCellStyle(), paraElem.getAttribute(), attrLayout);
            text = items[cnt - 1];
            leaf = new LeafElement(text);
            // 属性
            RunAttr.instance().setRunAttribute(book, fontID, r.element("rPr"), leaf.getAttribute(), attrLayout);
            // 开始 offset
            leaf.setStartOffset(offset);
            offset += text.length();
            // 结束 offset
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
        }
    }
    
    /**
     * 
     * @param cellElement
     * @param key
     * @return
     */
    public boolean searchContent(Element cellElement, String key)
    {       
        Element v = cellElement.element("v");        
        if(v != null && getCellType(cellElement.attributeValue("t")) != CELLTYPE_SHAREDSTRING && v.getText().toLowerCase().contains(key))
        {
            return true;
        }
        
        return false;
    }
    

    private int offset;
    private ParagraphElement paraElem;
    private AttributeSetImpl attrLayout;
    private LeafElement leaf;
 }
