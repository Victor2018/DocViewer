/*
 * 文件名称:          SmartArtReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:47:12
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.autoshape.ArbitraryPolygonShapePath;
import com.nvqquy98.lib.doc.office.common.autoshape.AutoShapeTypes;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.common.shape.ArbitraryPolygonShape;
import com.nvqquy98.lib.doc.office.common.shape.Arrow;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.common.shape.LineShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.common.shape.SmartArt;
import com.nvqquy98.lib.doc.office.common.shape.TextBox;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.LineKit;
import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.ParaAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.SectionAttr;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.model.PGLayout;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.pg.model.PGPlaceholderUtil;
import com.nvqquy98.lib.doc.office.pg.model.PGSlide;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.system.IControl;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-27
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
    public SmartArt read(IControl control, ZipPackage zipPackage, PGModel pgModel, PGMaster pgMaster, 
        PGLayout pgLayout, PGSlide pgSlide, PackagePart  slidePart, PackagePart dataPart) throws Exception
    {
        SAXReader saxreader = new SAXReader();
        InputStream in = dataPart.getInputStream();
        Document dataDoc = saxreader.read(in);
        in.close();
        Element root = dataDoc.getRootElement();
        
        BackgroundAndFill fill = BackgroundReader.instance().processBackground(control, zipPackage, dataPart, pgMaster, root.element("bg"));;
        Line line = LineKit.createLine(control, zipPackage, dataPart, pgMaster, root.element("whole").element("ln"));
        PackagePart drawingPart = null;
        Element e = null;
        if((e = root.element("extLst")) != null && (e = e.element("ext")) != null && (e = e.element("dataModelExt")) != null)
        {
        	String relId = e.attributeValue("relId");
        	if(relId != null)
        	{
        		PackageRelationship smartArDrawingRel = slidePart.getRelationship(relId);
        		if(smartArDrawingRel != null)
        		{
        			drawingPart = zipPackage.getPart(smartArDrawingRel.getTargetURI());
        		}
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
        if (spTree != null)
        {
            for (Iterator< ? > it = spTree.elementIterator("sp"); it.hasNext();)
            {
                Element sp = (Element)it.next();
                IShape shape = null;
                
                shape = processAutoShape(control, zipPackage, drawingPart, pgModel, pgMaster, 
                    pgLayout, pgSlide, sp);
                if (shape != null)
                {
                    shape.setParent(smartArt);
                    smartArt.appendShapes(shape);
                }
                
                shape = getTextBoxData(control, pgMaster, pgLayout, sp);
                if (shape != null)
                {
                    smartArt.appendShapes(shape);
                }
            }   
        }
        
        return smartArt;
    }
    
    private BackgroundAndFill getBackgrouond(IControl control, ZipPackage zipPackage, PackagePart smartArtPart, PGModel pgModel, PGMaster pgMaster, 
        PGLayout pgLayout, PGSlide pgSlide, Element sp, int shapeType) throws Exception
    {
        // fill
        BackgroundAndFill fill = null;
        if (sp.attribute("useBgFill") != null)
        {
            String val = sp.attributeValue("useBgFill");
            if (val != null && val.length() > 0 && Integer.parseInt(val) > 0)
            {
                fill = pgSlide.getBackgroundAndFill();
                if (fill == null)
                {
                    if (pgLayout != null)
                    {
                        fill = pgLayout.getBackgroundAndFill();
                    }
                    if (fill == null && pgMaster != null)
                    {
                        fill = pgMaster.getBackgroundAndFill();
                    }
                }
            }
        }
        
        Element spPr = sp.element("spPr");
        String spName = sp.getName();
        if (fill == null && spPr.element("noFill") == null && !spName.equals("cxnSp"))
        {
            fill = BackgroundReader.instance().processBackground(control, zipPackage, smartArtPart, pgMaster, spPr);
            if (fill == null && shapeType != ShapeTypes.Arc && shapeType != ShapeTypes.BracketPair 
                && shapeType != ShapeTypes.LeftBracket && shapeType != ShapeTypes.RightBracket
                && shapeType != ShapeTypes.BracePair && shapeType != ShapeTypes.LeftBrace
                && shapeType != ShapeTypes.RightBrace && shapeType != ShapeTypes.ArbitraryPolygon)
            {
                fill = BackgroundReader.instance().processBackground(control, zipPackage, smartArtPart, pgMaster, sp.element("style"));
            }
        }        
        
        return fill;
    }
    
    /**
     * process group rotate
     * @param parent
     * @param shape
     * @return
     */
    private void processGrpRotation(IShape shape, Element spPr)
    {
        ReaderKit.instance().processRotation(spPr, shape);
    }
    
    private IShape processAutoShape(IControl control, ZipPackage zipPackage, PackagePart smartArtPart, PGModel pgModel, PGMaster pgMaster, 
        PGLayout pgLayout, PGSlide pgSlide, Element sp) throws Exception
    {
        Element spPr = sp.element("spPr");
        Rectangle rect = null;
        if (spPr == null)
        {
            return  null;
        }
        rect = ReaderKit.instance().getShapeAnchor(spPr.element("xfrm"), 1.f, 1.f);
        
        int shapeType = ShapeTypes.NotPrimitive;
//        if (spPr != null)
        {
            String val;
            Float[] values = null;
            boolean border = true;
            String name = ReaderKit.instance().getPlaceholderName(sp);
            String spName = sp.getName();
            if (spName.equals("cxnSp"))
            {
                border = true;
                shapeType = ShapeTypes.Line;
            }
            else if (name.contains("Text Box") || name.contains("TextBox"))
            {
                shapeType = ShapeTypes.Rectangle;
            }
            
            // type
            Element prstGeom = spPr.element("prstGeom");
            if (prstGeom != null)
            {
                if (prstGeom.attribute("prst") != null)
                {
                    val = prstGeom.attributeValue("prst");
                    if (val != null && val.length() > 0)
                    {
                        shapeType = AutoShapeTypes.instance().getAutoShapeType(val);
                    }
                }
                
                // adjust data
                Element avLst = prstGeom.element("avLst");
                if (avLst != null)
                {
                    List<Element> gds = avLst.elements("gd");
                    if (gds != null && gds.size() > 0)
                    {
                        values = new Float[gds.size()];
                        for (int i = 0; i < gds.size(); i++)
                        {
                            Element gd = gds.get(i);
                            val = gd.attributeValue("fmla");
                            val = val.substring(4);
                            values[i] = Float.parseFloat(val) / 100000;
                        }
                    }
                }
            }
            else if(spPr.element("custGeom") != null)
            {
                //beizer line or direct line
                shapeType = ShapeTypes.ArbitraryPolygon;                
            }
            
            BackgroundAndFill fill = getBackgrouond(control, zipPackage, smartArtPart, pgModel, pgMaster, pgLayout, pgSlide, sp, shapeType);
            Line line = LineKit.createShapeLine(control, zipPackage, smartArtPart, pgMaster, sp); 
            
            Element ln = spPr.element("ln");
            Element style = sp.element("style");
            if (ln != null)
            {                
                if (ln.element("noFill") != null)
                {
                    border = false;
                }
            }
            else if (border)
            {
                if (style == null || style.element("lnRef") == null)
                {
                    border = false;
                }
            }
            
            // lineShape or autoShape
            if (shapeType == ShapeTypes.Line || shapeType == ShapeTypes.StraightConnector1
                || shapeType == ShapeTypes.BentConnector3 || shapeType == ShapeTypes.CurvedConnector3)
            {
                LineShape lineShape = new LineShape();
                lineShape.setShapeType(shapeType);
                lineShape.setBounds(rect); 
                lineShape.setAdjustData(values);
                lineShape.setLine(line);
                
                if (ln != null)
                {
                    Element temp = ln.element("headEnd");
                    if (temp != null && temp.attribute("type") != null)
                    {
                        byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                        if (arrowType != Arrow.Arrow_None)
                        {
                            lineShape.createStartArrow(arrowType, 
                                Arrow.getArrowSize(temp.attributeValue("w")), 
                                Arrow.getArrowSize(temp.attributeValue("len")));
                        }
                    }
                    temp = ln.element("tailEnd");
                    if (temp != null && temp.attribute("type") != null)
                    {
                        byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                        if (arrowType != Arrow.Arrow_None)
                        {
                            lineShape.createStartArrow(arrowType, 
                                Arrow.getArrowSize(temp.attributeValue("w")), 
                                Arrow.getArrowSize(temp.attributeValue("len")));
                        }
                    }
                }
                processGrpRotation(lineShape, spPr);                
                
                return lineShape;
            }
            else if(shapeType == ShapeTypes.ArbitraryPolygon)
            {
                ArbitraryPolygonShape arbitraryPolygonShape = new ArbitraryPolygonShape();
                BackgroundAndFill lineFill = null;
                if(line != null)
                {
                	lineFill = line.getBackgroundAndFill();
                }
                ArbitraryPolygonShapePath.processArbitraryPolygonShape(arbitraryPolygonShape, sp, fill, border, lineFill, ln, rect);
                
                arbitraryPolygonShape.setShapeType(shapeType);
                processGrpRotation(arbitraryPolygonShape, spPr);
                arbitraryPolygonShape.setLine(line);
                
                return arbitraryPolygonShape;
            }
            else if (fill != null || line != null)
            {
                AutoShape autoShape = new AutoShape(shapeType);
                autoShape.setBounds(rect);
                if (fill != null)
                {
                    autoShape.setBackgroundAndFill(fill);
                }
                if (line != null)
                {
                	autoShape.setLine(line);
                }
                autoShape.setAdjustData(values);
                processGrpRotation(autoShape, spPr);
                return autoShape;
            }
        }
        return null;
    }

    private IShape getTextBoxData(IControl control, PGMaster pgMaster, PGLayout pgLayout,Element sp)
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
            // anchor 
            tb.setBounds(rect);
            
            // 建立章节
            SectionElement secElem = new SectionElement();
            // 开始Offset
            secElem.setStartOffset(0);
            tb.setElement(secElem);
            // 属性
            IAttributeSet attr = secElem.getAttribute();
            // 宽度
            AttrManage.instance().setPageWidth(attr, (int)(rect.width * MainConstant.PIXEL_TO_TWIPS));
            // 高度
            AttrManage.instance().setPageHeight(attr, (int)(rect.height * MainConstant.PIXEL_TO_TWIPS));
            
            IAttributeSet attrLayout = null;
            IAttributeSet attrMaster = null;
            
            String type = PGPlaceholderUtil.DIAGRAM;
            int idx = 0;
            if (pgLayout != null)
            {
                attrLayout = pgLayout.getSectionAttr(null, idx);
            }
            if (pgMaster != null)
            {
                attrMaster = pgMaster.getSectionAttr(null, idx);
            }           
            
            SectionAttr.instance().setSectionAttribute(txBody.element("bodyPr"), attr, attrLayout, attrMaster, false);
            int offset = ParaAttr.instance().processParagraph(control, pgMaster, pgLayout, null, 
                secElem, sp.element("style"), txBody, type, idx);
            
            secElem.setEndOffset(offset);
            if (tb.getElement() != null && tb.getElement().getText(null) != null 
                && tb.getElement().getText(null).length() > 0
                && !"\n".equals(tb.getElement().getText(null)))
            {
                ReaderKit.instance().processRotation(tb, sp.element("txXfrm"));
            }
            
            // wrap line
            Element wrap = txBody.element("bodyPr");
            if (wrap != null)
            {
                // 文本框内自动换行
                String value = wrap.attributeValue("wrap");
                tb.setWrapLine(value == null || "square".equalsIgnoreCase(value));
            }
            
            return tb;
        }
        
        return null;
    }
}
