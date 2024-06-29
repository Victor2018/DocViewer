/*
 * 文件名称:          SmartArtReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:34:47
 */
package com.nvqquy98.lib.doc.office.fc.xls.Reader.drawing;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.autoshape.AutoShapeDataKit;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.SmartArt;
import com.nvqquy98.lib.doc.office.common.shape.TextBox;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.fc.LineKit;
import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.ParaAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.RunAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.SectionAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.ReaderKit;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.LeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-12
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SmartArtReader
{
    private static SmartArtReader reader = new SmartArtReader();
    
    /**
     * 
     */
    public static SmartArtReader instance()
    {
        return reader;
    }
    
    /**
     * get AbstractChart
     * @param chartPart
     * @param sheet
     * @return
     */
    public SmartArt read(IControl control, ZipPackage zipPackage, PackagePart  slidePart, PackagePart dataPart, 
        Map<String, Integer> schemeColor, Sheet sheet) throws Exception
    {
        this.sheet = sheet;
        
        SAXReader saxreader = new SAXReader();
        InputStream in = dataPart.getInputStream();
        Document dataDoc = saxreader.read(in);
        in.close();
        Element root = dataDoc.getRootElement();
        
        BackgroundAndFill fill = AutoShapeDataKit.processBackground(control, zipPackage,  dataPart, root.element("bg"), schemeColor);;
        Line line = LineKit.createLine(control, zipPackage, dataPart, root.element("whole").element("ln"), schemeColor);
        PackagePart drawingPart = null;
        Element e = null;
        if((e = root.element("extLst")) != null && (e = e.element("ext")) != null && (e = e.element("dataModelExt")) != null)
        {
        	String relId = e.attributeValue("relId");
        	if(relId != null)
        	{
        		PackageRelationship smartArDrawingRel = slidePart.getRelationship(relId);
        		drawingPart = zipPackage.getPart(smartArDrawingRel.getTargetURI());
        	}
        	
        }
        if(drawingPart == null)
        {
        	return null;
        }
        
        in = drawingPart.getInputStream();        
        Document smartArtDoc = saxreader.read(in);
        in.close();
        
        SmartArt smartArt = new SmartArt();
        smartArt.setBackgroundAndFill(fill);
        smartArt.setLine(line);
        
        root = smartArtDoc.getRootElement();
        Element spTree = root.element("spTree");
        
        for (Iterator< ? > it = spTree.elementIterator("sp"); it.hasNext();)
        {
            e = (Element)it.next();
            AbstractShape shape = null;
            Element temp = e.element("spPr");
            Rectangle rect = null;
            if (temp != null)
            {
                rect = ReaderKit.instance().getShapeAnchor(temp.element("xfrm"), 1.f, 1.f);
            }
            
            shape = AutoShapeDataKit.getAutoShape(control, zipPackage, dataPart, e, 
                rect, schemeColor, MainConstant.APPLICATION_TYPE_SS);
            if (shape != null)
            {
                smartArt.appendShapes(shape);
            }
            
            shape = getTextBoxData(control, e);
            if (shape != null)
            {
                smartArt.appendShapes(shape);
            }
        }
        
        sheet = null;
        
        return smartArt;
    }
    
    /**
     * 
     * @param shapeElement
     * @return
     */
    private TextBox getTextBoxData(IControl control, Element sp)
    {
        Element temp = sp.element("txXfrm");
        Rectangle rect = null;
        if (temp != null)
        {
            rect = ReaderKit.instance().getShapeAnchor(temp, 1.f, 1.f);
        }        
        
        Element txBody = sp.element("txBody");
        if (txBody != null)
        {
            TextBox tb = new TextBox();
            

            // ======== 处理文本 ========
            // 建立章节
            SectionElement secElem = new SectionElement();
            // 开始Offset
            secElem.setStartOffset(0);
            tb.setElement(secElem);
            // 属性
            IAttributeSet attr = secElem.getAttribute();
            // 宽度
            AttrManage.instance().setPageWidth(attr, Math.round(rect.width * MainConstant.PIXEL_TO_TWIPS));
            // 高度
            AttrManage.instance().setPageHeight(attr, Math.round(rect.height * MainConstant.PIXEL_TO_TWIPS));
            
            // 左边距
            AttrManage.instance().setPageMarginLeft(attr, Math.round(SSConstant.SHEET_SPACETOBORDER * MainConstant.PIXEL_TO_TWIPS));
            // 右边距
            AttrManage.instance().setPageMarginRight(attr, Math.round(SSConstant.SHEET_SPACETOBORDER * MainConstant.PIXEL_TO_TWIPS));
            // 上边距
            AttrManage.instance().setPageMarginTop(attr, 0);
            // 下边框
            AttrManage.instance().setPageMarginBottom(attr, 0);
            
            Element bodyPr = temp.element("bodyPr");
            SectionAttr.instance().setSectionAttribute(bodyPr, attr, null, null, false);
            if (bodyPr != null)
            {
                // 文本框内自动换行
                String value = bodyPr.attributeValue("wrap");
                tb.setWrapLine(value == null || "square".equalsIgnoreCase(value));
            }
            int offset = processParagraph(control, secElem, txBody);
            secElem.setEndOffset(offset);
            
            tb.setBounds(rect);
            
            if (tb.getElement() != null && tb.getElement().getText(null) != null 
                && tb.getElement().getText(null).length() > 0
                && !"\n".equals(tb.getElement().getText(null)))
            {
                ReaderKit.instance().processRotation(tb, sp.element("txXfrm"));
            }
            return tb;
        }
        
        return null;
    }
    
   
    private int processParagraph(IControl control, SectionElement secElem, Element txBody)
    {
        offset = 0;
        List<Element> ps = txBody.elements("p");
        for (Element p : ps)
        {   
            Element pPr = p.element("pPr");
            
            ParagraphElement paraElem = new ParagraphElement();
            paraElem.setStartOffset(offset);
            IAttributeSet attrLayout = null;
           
            ParaAttr.instance().setParaAttribute(control, pPr, paraElem.getAttribute(), attrLayout, -1, -1, 0, false, false);
            
            paraElem = processRun(control, secElem, paraElem, p, attrLayout);
            
            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
        }
        return offset;        
    } 
    
    private ParagraphElement processRun(IControl control, SectionElement secElem, ParagraphElement paraElem, Element p, 
        IAttributeSet attrLayout)
    {
        
        List<Element> rs = p.elements("r");
        LeafElement leaf = null;
        // 如果没有 r 元素，说明只有一个回车符的段落
        if (rs.size() == 0)
        {
            leaf = new LeafElement("\n");
            Element ele = p.element("pPr");
            if (ele != null)
            {
                ele = ele.element("rPr");
                if (ele != null)
                {
                    // 属性
                    RunAttr.instance().setRunAttribute(sheet, ele, leaf.getAttribute(), attrLayout);
                }
            }
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
                    if (len >= 0)
                    {
                        leaf = new LeafElement(text);
                        // 属性
                        RunAttr.instance().setRunAttribute(sheet, r.element("rPr"), leaf.getAttribute(), attrLayout);
                        // 开始 offset
                        leaf.setStartOffset(offset);
                        offset += len;
                        // 结束 offset
                        leaf.setEndOffset(offset);
                        paraElem.appendLeaf(leaf);
                    }
                } 
            }
            else if(r.getName().equalsIgnoreCase("br"))
            {
                //Text Line Break, for last paragrapha
                if (leaf != null)
                {
                    leaf.setText(leaf.getText(null) + "\n");
                    offset++;
                }                
                paraElem.setEndOffset(offset);
                secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
                
                paraElem = new ParagraphElement();
                paraElem.setStartOffset(offset);
                attrLayout = null;
                Element pPr = p.element("pPr");
                ParaAttr.instance().setParaAttribute(control, pPr, paraElem.getAttribute(), attrLayout, -1, -1, 0, false, false);
            }
            
        }
        if (leaf != null)
        {
            leaf.setText(leaf.getText(null) + "\n");
            offset++;
        }
        return paraElem;
    }    
    
    private Sheet sheet;
    private int offset;
}
