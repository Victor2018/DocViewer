/*
 * 文件名称:          DOCXReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:32:45
 */

package com.nvqquy98.lib.doc.office.fc.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.bean.DocSourceType;
import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.common.autoshape.AutoShapeDataKit;
import com.nvqquy98.lib.doc.office.common.autoshape.ExtendPath;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.LineArrowPathBuilder;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.Gradient;
import com.nvqquy98.lib.doc.office.common.bg.LinearGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.PatternShader;
import com.nvqquy98.lib.doc.office.common.bg.RadialGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.TileShader;
import com.nvqquy98.lib.doc.office.common.bookmark.Bookmark;
import com.nvqquy98.lib.doc.office.common.borders.Border;
import com.nvqquy98.lib.doc.office.common.borders.Borders;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListData;
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListLevel;
import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfo;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfoFactory;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.Arrow;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.GroupShape;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.common.shape.PictureShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.common.shape.WPAbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.WPAutoShape;
import com.nvqquy98.lib.doc.office.common.shape.WPChartShape;
import com.nvqquy98.lib.doc.office.common.shape.WPGroupShape;
import com.nvqquy98.lib.doc.office.common.shape.WPPictureShape;
import com.nvqquy98.lib.doc.office.common.shape.WatermarkShape;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.SchemeClrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.fc.FCKit;
import com.nvqquy98.lib.doc.office.fc.LineKit;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementHandler;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementPath;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.exceptions.InvalidFormatException;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.ReaderKit;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.ThemeReader;
import com.nvqquy98.lib.doc.office.fc.xls.Reader.drawing.ChartReader;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.font.FontTypefaceManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.model.LeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.simpletext.model.Style;
import com.nvqquy98.lib.doc.office.simpletext.model.StyleManage;
import com.nvqquy98.lib.doc.office.system.AbortReaderError;
import com.nvqquy98.lib.doc.office.system.AbstractReader;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.AbstractChart;
import com.nvqquy98.lib.doc.office.wp.model.CellElement;
import com.nvqquy98.lib.doc.office.wp.model.HFElement;
import com.nvqquy98.lib.doc.office.wp.model.RowElement;
import com.nvqquy98.lib.doc.office.wp.model.TableElement;
import com.nvqquy98.lib.doc.office.wp.model.WPDocument;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PointF;
import android.net.Uri;

/**
 * docx reader
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-1-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         p
 * <p>
 * <p>
 */
@ SuppressWarnings("unchecked")
public class DOCXReader extends AbstractReader
{
    // picture高度、宽度转换到磅单位的一个值，我也知道是什么意思，只是是通过大量文档分得出来的值
    //private final int PICTURE_CONVERSION_VALUE = 0x7F * 100;
    
    public DOCXReader(IControl control, String filePath,int docSourceType)
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
        if (wpdoc != null)
        {
            return wpdoc;
        }
        wpdoc = new WPDocument();
        openFile();
        return wpdoc;
    }
    
    /**
     * 
     */
    private void openFile() throws Exception
    {
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
        
        if (zipPackage.getParts().size() == 0) {
            throw new Exception("Format error");
        }
        PackageRelationship coreRel = zipPackage.getRelationshipsByType(
            PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
        if (!coreRel.getTargetURI().toString().equals("/word/document.xml")) {
            throw new Exception("Format error");
        }
            
        packagePart = zipPackage.getPart(coreRel);
        // get theme color
        processThemeColor();
        // bullet and number
        processBulletNumber();
        // style 
        processStyle();
        
        // section
        secElem = new SectionElement();
        // document
        offset = WPModelConstant.MAIN;
        SAXReader saxreader = new SAXReader();
        DOCXSaxHandler docxHandler = new DOCXSaxHandler(); 
        saxreader.addHandler("/document/body/tbl", docxHandler);
        saxreader.addHandler("/document/body/p", docxHandler);
        saxreader.addHandler("/document/body/sdt", docxHandler);
        
        Document doc = saxreader.read(packagePart.getInputStream());
        // page background color
        Element br = doc.getRootElement().element("background");
        if (br != null) {
        	BackgroundAndFill fill = null;
        	if(br.element("background") != null) {
        		//gradient background or tile
        		fill = processBackgroundAndFill(br.element("background"));
        	} else {
        		String value = br.attributeValue("color");
                if (value != null) {
                	fill = new BackgroundAndFill();
                	fill.setForegroundColor(Color.parseColor("#aabb" + value));
                }
        	}
        	
        	wpdoc.setPageBackground(fill);            
        }
        
        processSection(doc.getRootElement().element("body"));
        //
        processRelativeShapeSize();
    }
    
    /**
     * reader style
     */
    private void processStyle() throws Exception
    {
        PackageRelationship styleRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.STYLE_PART).getRelationship(0);
        //styleRel.get
        if (styleRel != null)
        {
            PackagePart part = zipPackage.getPart(styleRel.getTargetURI());
            if (part != null)
            {
                SAXReader saxreader = new SAXReader();
                InputStream in = part.getInputStream();
                Document doc = saxreader.read(in);
                Element root = doc.getRootElement();
                List<Element> styles = root.elements("style");
                
                //docDefaults
                Element docDefaults = root.element("docDefaults");
                if(docDefaults != null)
                {
                	Style style = new Style();
                    // id
                    styleStrID.put("docDefaults", styleID);
                    style.setId(styleID);
                    styleID++;
                    
                    // type
                    style.setType((byte)0);
                    style.setName("docDefaults");
                    
                    // paragraph attribute set
                    Element pPr = docDefaults.element("pPrDefault");
                    if(pPr != null && ( pPr = pPr.element("pPr")) != null)
                    {
                    	processParaAttribute(pPr, style.getAttrbuteSet(), 0);
                    }
                    
                    // character attribute set
                    Element rPr = docDefaults.element("rPrDefault");
                    if(rPr != null && ( rPr = rPr.element("rPr")) != null)
                    {
                    	processRunAttribute(rPr, style.getAttrbuteSet());                    	
                    }
                    
                    StyleManage.instance().addStyle(style);
                }
                
                for (Element styleEle : styles)
                {
                    if (abortReader)
                    {
                        break;
                    }
                    if ("table".equals(styleEle.attributeValue("type")))
                    {
                        Element tlb = styleEle.element("tblStylePr");
                        if (tlb != null && "firstRow".equals(tlb.attributeValue("type")))
                        {
                            Element tcPr = tlb.element("tcPr");
                            if (tcPr != null)
                            {
                                Element shd = tcPr.element("shd");
                                if (shd != null)
                                {
                                    String val = shd.attributeValue("fill");
                                    if (val != null)
                                    {
                                        tableStyle.put(styleEle.attributeValue("styleId"), Color.parseColor("#" + val));
                                    }
                                }
                            }
                        }
                    }
                    Style style = new Style();
                    // id
                    String val = styleEle.attributeValue("styleId");
                    if (val != null)
                    {
                        //style.setId(Integer.parseInt(val, 16));
                        Integer a = styleStrID.get(val);
                        if (a == null)
                        {
                            styleStrID.put(val, styleID);
                            style.setId(styleID);
                            styleID++;
                        }
                        else
                        {
                            style.setId(a.intValue());
                        }
                    }
                    // type
                    val = styleEle.attributeValue("type");
                    style.setType((byte)(val.equals("paragraph") ? 0 : 1));
                    // name
                    Element temp = styleEle.element("name");
                    if (temp != null)
                    {
                        style.setName(temp.attributeValue("val"));
                    }
                    // base id
                    temp = styleEle.element("basedOn");
                    if (temp != null)
                    {
                        // id
                        val = temp.attributeValue("val");
                        if (val != null)
                        {
                            Integer a = styleStrID.get(val);
                            if (a == null)
                            {
                                styleStrID.put(val, styleID);
                                style.setBaseID(styleID);
                                styleID++;
                            }
                            else
                            {
                                style.setBaseID(a.intValue());
                            }
                        }
                    }
                    else if("1".equals(styleEle.attributeValue("default")))
                    {
                    	style.setBaseID(0);
                    }
                    
                    // paragraph attribute set
                    temp = styleEle.element("pPr");
                    if (temp != null)
                    {
                        processParaAttribute(temp, style.getAttrbuteSet(), 0);
                    }
                    // character attribute set
                    temp = styleEle.element("rPr");
                    if (temp != null)
                    {
                        processRunAttribute(temp, style.getAttrbuteSet());
                    } 
                    StyleManage.instance().addStyle(style);
                }
                
                in.close();
            }
        }      
    }
    
    /**
     * 
     */
    private void processBulletNumber() throws Exception
    {
        PackageRelationship styleRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.BULLET_NUMBER_PART).getRelationship(0);
        //styleRel.get
        if (styleRel != null)
        {
            PackagePart part = zipPackage.getPart(styleRel.getTargetURI());
            if (part != null)
            {
                SAXReader saxreader = new SAXReader();
                InputStream in = part.getInputStream();
                Document doc = saxreader.read(in);
                Element root = doc.getRootElement();
                List<Element> nums = root.elements("num");
                Element temp;
                for (Element num : nums)
                {
                    temp = num.element("abstractNumId");
                    if (temp != null)
                    {
                        String val = temp.attributeValue("val");
                        String numID = num.attributeValue("numId");
                        bulletNumbersID.put(numID, val);
                    }
                }                
                // bullet and number object
                nums = root.elements("abstractNum");
                for (Element num : nums)
                {
                    ListData listData = new ListData();
                    // ID
                    String abstractNumId = num.attributeValue("abstractNumId");
                    if (abstractNumId != null)
                    {
                        listData.setListID(Integer.parseInt(abstractNumId));
                    }
                    // list level
                    List<Element> levels = num.elements("lvl");
                    int len = levels.size();
                    ListLevel[] listLevels = new ListLevel[len];
                    listData.setSimpleList((byte)len);
                    for (int i = 0; i < len; i++)
                    {
                        listLevels[i] = new ListLevel();
                        processListLevel(listLevels[i], levels.get(i));
                    }
                    listData.setLevels(listLevels);
                    // simple list
                    listData.setSimpleList((byte)len);
                    if (len == 0)
                    {
                        Element styleLink = num.element("numStyleLink");
                        if (styleLink != null)
                        {
                            String styleID = styleLink.attributeValue("val");
                            if (styleID != null)
                            {
                                Integer a = styleStrID.get(styleID);
                                if (a != null)
                                {
                                    listData.setLinkStyleID(a.shortValue());
                                    // change style 
                                    Style style = StyleManage.instance().getStyle(a.intValue());
                                    int styleListNumID = AttrManage.instance().getParaListID(style.getAttrbuteSet());
                                    if (styleListNumID >= 0)
                                    {
                                        styleListNumID = Integer.parseInt(bulletNumbersID.get(String.valueOf(styleListNumID)));
                                        AttrManage.instance().setParaListID(style.getAttrbuteSet(), styleListNumID);
                                    }
                                }                                
                            }
                        }
                    }
                    //
                    control.getSysKit().getListManage().putListData(listData.getListID(), listData);
                }
                in.close();
            }
        }
    }
    
    /**
     * 
     */
    private void processListLevel(ListLevel level, Element elem)
    {
        // start at
        String val;
        Element temp = elem.element("start");
        if (temp != null)
        {
            level.setStartAt(Integer.parseInt(temp.attributeValue("val")));
        }        
        // horizontal alignment;
        temp = elem.element("lvlJc");
        if (temp != null)
        {
            val = temp.attributeValue("val");
            if ("left".equals(val))
            {
                level.setAlign(WPAttrConstant.PARA_HOR_ALIGN_LEFT);
            }
            else if ("center".equals(val))
            {
                level.setAlign(WPAttrConstant.PARA_HOR_ALIGN_CENTER);
            }
            else if ("right".equals(val))
            {
                level.setAlign(WPAttrConstant.PARA_HOR_ALIGN_RIGHT);
            }
                
        }
        // follow char
        temp = elem.element("suff");
        if (temp != null)
        {
            val = temp.attributeValue("val");
            if ("space".equals(val))
            {
                level.setFollowChar((byte)1);
            }
            else if("nothing".equals(val))
            {
                level.setFollowChar((byte)2);
            }
        }
        // number format
        temp = elem.element("numFmt");
        if (temp != null)
        {
            level.setNumberFormat(convertedNumberFormat(temp.attributeValue("val")));
        }
        // number text
        temp = elem.element("lvlText");
        if (temp != null)
        {
            StringBuilder sb = new StringBuilder();
            val = temp.attributeValue("val");
            for (int i = 0; i < val.length(); i++)
            {
                char c = val.charAt(i);
                if (c == '%')
                {
                    int a = Integer.parseInt(val.substring(i + 1, i + 2));
                    sb.append((char)(a - 1));
                    i++;
                }
                else
                {
                    if (c == 0xF06C)
                    {
                        c = 0x25CF;
                    }
                    else if (c == 0xF06E)
                    {
                        c = 0x25A0;
                    } 
                    else if (c == 0xF075)
                    {
                        c = 0x25C6;
                    }  
                    else if (c == 0xF0FC)
                    {
                        c = 0x221A;
                    }
                    else if (c == 0xF0D8)
                    {
                        c = 0x2605;
                    }
                    else if (c == 0xF0B2)
                    {
                        c = 0x2606;
                    }
                    else if (c >= 0xF000)
                    {
                        c = 0x25CF;
                    } 
                    sb.append(c);
                }
            }
            char[] c = new char[sb.length()];
            sb.getChars(0, sb.length(), c, 0);
            level.setNumberText(c);
        }
        // indent
        temp = elem.element("pPr");
        if (temp != null)
        {
            temp = temp.element("ind");
            if (temp != null)
            {
                // special indent, default 21 POINT
                val = temp.attributeValue("hanging");
                if (val != null)
                {
                    level.setSpecialIndent(-Integer.parseInt(val));
                }            
                // left text indent, default 21 point * level
                val = temp.attributeValue("left");
                if (val != null)
                {
                    level.setTextIndent(Integer.parseInt(val));
                }
            }
        }
    }
    
    /**
     * = 0    decimal                           1、2、3、...
     * = 1    upperRoman                        I、II、III、...
     * = 2    lowerRoman                        i、ii、iii、...
     * = 3    upperLetter                       A、B、C、...                      
     * = 4    lowerLetter                       a、b、c、...
     * = 39   chineseCountingThousand           一、二、三、...
     * = 38   chineseLegalSimplified            壹、贰、叁、...
     * = 30   ideographTraditional              甲、乙、丙、...
     * = 31   ideographZodiac                   子、丑、寅、...
     * = 5    ordinal                           1st、2st、3st、...
     * = 6    cardinalText                      one、two、three、...
     * = 7    ordinalText                       First、Second、Third、...
     * = 22   decimalZero                       01、02、03、...
     */
    private int convertedNumberFormat(String numFormat)
    {
        if ("decimal".equalsIgnoreCase(numFormat))
        {
            return 0;
        }
        else if ("upperRoman".equalsIgnoreCase(numFormat))
        {
            return 1;
        }
        else if ("lowerRoman".equalsIgnoreCase(numFormat))
        {
            return 2;
        }
        else if ("upperLetter".equalsIgnoreCase(numFormat))
        {
            return 3;
        }
        else if ("lowerLetter".equalsIgnoreCase(numFormat))
        {
            return 4;
        }
        else if ("chineseCountingThousand".equalsIgnoreCase(numFormat))
        {
            return 39;
        }
        else if ("chineseLegalSimplified".equalsIgnoreCase(numFormat))
        {
            return 38;
        }
        else if ("ideographTraditional".equalsIgnoreCase(numFormat))
        {
            return 30;
        }
        else if ("ideographZodiac".equalsIgnoreCase(numFormat))
        {
            return 31;
        }
        else if ("ordinal".equalsIgnoreCase(numFormat))
        {
            return 5;
        }
        else if ("cardinalText".equalsIgnoreCase(numFormat))
        {
            return 6;
        }
        else if ("ordinalText".equalsIgnoreCase(numFormat))
        {
            return 7;
        }
        else if ("decimalZero".equalsIgnoreCase(numFormat))
        {
            return 22;
        }
        return 0;
    }
    
    /**
     * reader 
     */
    private void processHeaderAndFooter(PackageRelationship hfRel, boolean isHeader) throws Exception
    {
        if (hfRel != null)
        {
            hfPart = zipPackage.getPart(hfRel.getTargetURI());
            if (hfPart != null)
            {
                isProcessHF = true;
                offset = isHeader ? WPModelConstant.HEADER : WPModelConstant.FOOTER;
                SAXReader saxreader = new SAXReader();
                InputStream in = hfPart.getInputStream();
                Document doc = saxreader.read(in);
                Element root = doc.getRootElement();                
                List<Element> paras = root.elements();
                
                HFElement hfElem = new HFElement(isHeader ? WPModelConstant.HEADER_ELEMENT : WPModelConstant.FOOTER_ELEMENT, 
                    WPModelConstant.HF_ODD);
                hfElem.setStartOffset(offset);
                
                processParagraphs(paras);
                
                hfElem.setEndOffset(offset);
                wpdoc.appendElement(hfElem, offset);
                
                in.close();
                isProcessHF = false;
            }
        }
    }
    
    /**
     * 
     */
    private void processSection(Element body) throws Exception
    {
        // 建立章节
        //SectionElement secElem = new SectionElement();
        //IAttributeSet attr = secElem.getAttribute();
        // 开始Offset
        secElem.setStartOffset(0);
        // 属性
        // 宽度 default a4 paper
        /*AttrManage.instance().setPageWidth(attr, 11906);
        // 高度 default a4 paper
        AttrManage.instance().setPageHeight(attr, 16838);
        // 左边距 default a4 paper
        AttrManage.instance().setPageMarginLeft(attr, 1800);
        // 右边距 default a4 paper
        AttrManage.instance().setPageMarginRight(attr, 1800);
        // 上边距 default a4 paper
        AttrManage.instance().setPageMarginTop(attr, 1440);
        // 下边框 default a4 paper
        AttrManage.instance().setPageMarginBottom(attr, 1440);*/
        // 结束Offset
        secElem.setEndOffset(offset);
        wpdoc.appendSection(secElem);
        
        processSectionAttribute(body.element("sectPr"));
    }
    
    /**
     * 
     */
    private void processSectionAttribute(Element sectPr)
    {
        if (sectPr == null || isProcessSectionAttribute)
        {
            return;
        }
        IAttributeSet attr = secElem.getAttribute();
        // 纸张
        Element pgSz = sectPr.element("pgSz");
        if (pgSz != null)
        {
            // 宽度
            AttrManage.instance().setPageWidth(attr,
                Integer.parseInt(pgSz.attributeValue("w")));
            // 高度
            AttrManage.instance().setPageHeight(attr,
                Integer.parseInt(pgSz.attributeValue("h")));
        }
        // 边距 
        Element margin = sectPr.element("pgMar");
        if (margin != null)
        {
            // 左边距
            AttrManage.instance().setPageMarginLeft(attr,
                Integer.parseInt(margin.attributeValue("left")));
            // 右边距
            AttrManage.instance().setPageMarginRight(attr,
                Integer.parseInt(margin.attributeValue("right")));
            // 上边距
            AttrManage.instance().setPageMarginTop(attr,
                Integer.parseInt(margin.attributeValue("top")));
            // 下边距
            AttrManage.instance().setPageMarginBottom(attr,
                Integer.parseInt(margin.attributeValue("bottom")));
            // 页眉边距
            if (margin.attributeValue("header") != null)
            {
                AttrManage.instance().setPageHeaderMargin(attr,
                    Integer.parseInt(margin.attributeValue("header")));
            }
            // 页脚边距
            if (margin.attributeValue("footer") != null)
            {
                AttrManage.instance().setPageFooterMargin(attr,
                    Integer.parseInt(margin.attributeValue("footer")));
            }
        }
        // page border
        Element borderElem = sectPr.element("pgBorders");
        if (borderElem != null)
        {
            Borders borders = new Borders();
            if ("page".equals(borderElem.attributeValue("offsetFrom")))
            {
                borders.setOnType((byte)1);
            }
            Border border;
            // topBorder
            Element bElem = borderElem.element("top");
            if (bElem != null)
            {                
                border = new Border();
                processBorder(bElem, border);
                borders.setTopBorder(border);
            }            
            // leftBorder
            bElem = borderElem.element("left");
            if (bElem != null)
            {
                border = new Border();
                processBorder(bElem, border);
                borders.setLeftBorder(border);
            }
            
            // rightBorder
            bElem = borderElem.element("right");
            if (bElem != null)
            {
                border = new Border();
                processBorder(bElem, border);
                borders.setRightBorder(border);
            }            
            // bottomBorder
            bElem = borderElem.element("bottom");
            if (bElem != null)
            {
                border = new Border();
                processBorder(bElem, border);
                borders.setBottomBorder(border);
            }
            AttrManage.instance().setPageBorder(attr, control.getSysKit().getBordersManage().addBorders(borders));
        }
        //line pitch
        Element docGrid = sectPr.element("docGrid");
        if(docGrid != null)
        {
        	String type = docGrid.attributeValue("type");
        	if("lines".equals(type) || "linesAndChars".equals(type) || "snapToChars".equals(type))
        	{
        		String linePitch = docGrid.attributeValue("linePitch");
        		if(linePitch != null && linePitch.length() > 0)
        		{        			
        			AttrManage.instance().setPageLinePitch(attr, Integer.parseInt(linePitch));
        			for(int i = 0; i < textboxIndex; i++)
        			{
        				IElement textboxSec = wpdoc.getTextboxSectionElementForIndex(i);
        				IAttributeSet secElemAttr = secElem.getAttribute();
        	            AttrManage.instance().setPageLinePitch(textboxSec.getAttribute(), 
        	            		AttrManage.instance().getPageLinePitch(secElemAttr));
        			}
        		}
        	}
        }
        // header
        long a = offset;
        //
        List<Element> headers = sectPr.elements("headerReference");
        if (headers != null && headers.size() > 0)
        {
            String id = "";
            if (headers.size() == 1)
            {
                id = headers.get(0).attributeValue("id");
            }
            else
            {
                for (Element header : headers)
                {
                    if ("default".equals(header.attributeValue("type")))
                    {
                        id = header.attributeValue("id");
                        break;
                    }
                }   
            }
            if (id != null && id.length() > 0)
            {
                try
                {
                    PackageRelationship hfRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.HEADER_PART).getRelationshipByID(id);
                    if (hfRel != null)
                    {
                        processHeaderAndFooter(hfRel, true);
                    }
                }
                catch (Exception e)
                {
                    control.getSysKit().getErrorKit().writerLog(e, true);
                }
            }
        }
        
        // footer
        List<Element> footers = sectPr.elements("footerReference");
        if (footers != null && footers.size() > 0)
        {
            String id = "";
            if (footers.size() == 1)
            {
                id = footers.get(0).attributeValue("id");
            }
            else
            {
                for (Element footer : footers)
                {
                    if ("default".equals(footer.attributeValue("type")))
                    {
                        id = footer.attributeValue("id");
                        break;
                    }
                }
            }
            if (id != null && id.length() > 0)
            {
                try
                {
                    PackageRelationship hfRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.FOOTER_PART).getRelationshipByID(id);
                    if (hfRel != null)
                    {
                        processHeaderAndFooter(hfRel, false);
                    }
                }
                catch (Exception e)
                {
                    control.getSysKit().getErrorKit().writerLog(e, true);
                }
            }
        }
        offset = a;
        isProcessSectionAttribute = true;
    }
    
    /**
     * 
     */
    private void processBorder(Element borElem, Border border)
    {
        String value = borElem.attributeValue("color");
        if (value == null || "auto".equals(value))
        {
            border.setColor(Color.BLACK);
        }
        else
        {
            border.setColor(Color.parseColor("#" + value));
        }
        value = borElem.attributeValue("space");
        if (value == null)
        {
            border.setSpace((short)32);
        }
        else
        {
            border.setSpace((short)(Integer.parseInt(value) * MainConstant.POINT_TO_PIXEL));
        }
    }
    
    /**
     * 
     */
    private void processTable(Element table)
    {
        TableElement tableElem = new TableElement();
        tableElem.setStartOffset(offset);
        
        Element trPr = table.element("tblPr");
        String tableStyleId = "";
        if (trPr != null)
        {
            processTableAttribute(trPr, tableElem.getAttribute());
            Element tStyleId = trPr.element("tblStyle");
            if (tStyleId != null)
            {
                tableStyleId = tStyleId.attributeValue("val"); 
                if (tableStyleId == null)
                {
                    tableStyleId = "";
                }
            }
        }
        // table grid column width
        Element tblGrid = table.element("tblGrid");
        if (tblGrid != null)
        {
            List<Element> grids = tblGrid.elements("gridCol");
            if (grids != null)
            {
                for (int i = 0; i < grids.size(); i++)
                {
                    tableGridCol.put(i, Integer.parseInt(grids.get(i).attributeValue("w")));
                }
            }
        }
        
        List<Element> rows = table.elements("tr");
        boolean firstRow = true;
        for (Element row : rows)
        {
            processRow(row, tableElem, firstRow, tableStyleId);
            firstRow = false;
        }
        
        tableElem.setEndOffset(offset);
        wpdoc.appendParagraph(tableElem, offset);
    }
    
    /**
     * 
     */
    private void processTableAttribute(Element tblPr, IAttributeSet attr)
    {
        String val;
        // table horizontal alignment
        Element temp = tblPr.element("jc");
        if (temp != null)
        {
            val = temp.attributeValue("val");
            if ("center".equals(val))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_CENTER);
            }
            else if ("right".equals(val))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_RIGHT);
            }
        }
        // table left indent 
        temp = tblPr.element("tblInd");
        if (temp != null)
        {
            val = temp.attributeValue("w");
            if (val != null)
            {
                AttrManage.instance().setParaIndentLeft(attr, Integer.parseInt(val));
            }
        }
        
    }
    
    /**
     * 
     */
    private void processRow(Element row, TableElement tableElem, boolean firstRow, String tableStyleId)
    {
        RowElement rowElem = new RowElement();        
        rowElem.setStartOffset(offset);
        
        Element trPr = row.element("trPr");
        if (trPr != null)
        {
            processRowAttribute(trPr, rowElem.getAttribute());
        }
        
        List<Element> cells = row.elements("tc");
        int i = 0;
        
        for (Element cell : cells)
        {
            i += processCell(cell, rowElem, i, firstRow, tableStyleId);
        }
        
        rowElem.setEndOffset(offset);
        tableElem.appendRow(rowElem);
    }
    
    /**
     * 
     */
    private void processRowAttribute(Element trPr, IAttributeSet attr)
    {
        // 行高
        Element temp = trPr.element("trHeight");
        if (temp != null)
        {
            AttrManage.instance().setTableRowHeight(attr, 
                Integer.parseInt(temp.attributeValue("val")));
        }
    }
    
    /**
     * 
     */
    private int processCell(Element cell, RowElement row, int gridColIndex, boolean firstRow, String tableStyleId)
    {
        CellElement cellElem = new CellElement();
        cellElem.setStartOffset(offset);
        
        int gridSpan = 0;
        Element tcPr = cell.element("tcPr");
        if (tcPr != null)
        {
            gridSpan = processCellAttribute(tcPr, cellElem.getAttribute(), gridColIndex);
        }
        
        processParagraphs_Table(cell.elements(), 1);
        
        cellElem.setEndOffset(offset);
        row.appendCell(cellElem);
        // cell background
        if (firstRow)
        {
            if (tableStyle.containsKey(tableStyleId))
            {
                AttrManage.instance().setTableCellBackground(cellElem.getAttribute(), tableStyle.get(tableStyleId));
            }
        }
        // 水平合并单元格
        if (gridSpan > 0)
        {
            for (int i = 1; i < gridSpan; i++)
            {
                row.appendCell(new CellElement());
            }
        }
        return gridSpan;
    }
    
    /**
     * 
     */
    private int processCellAttribute(Element tcPr, IAttributeSet attr, int gridColIndex)
    {
        int gridSpan = 1;
        Element temp = tcPr.element("gridSpan");
        if (temp != null)
        {
            gridSpan = Integer.parseInt(temp.attributeValue("val"));
        }
        // 宽度
        temp = tcPr.element("tcW");
        if (temp != null)
        {
            int w = Integer.parseInt(temp.attributeValue("w"));
            String type = temp.attributeValue("type");
            if ("pct".equals(type) || "auto".equals(type))
            {   
                int tW = 0;
                for (int i = gridColIndex; i < gridColIndex + gridSpan; i++)
                {
                    tW += tableGridCol.get(i);
                }
                w = Math.max(tW, w);
            }
            AttrManage.instance().setTableCellWidth(attr, w);
        }
        else
        {
        	int tW = 0;
            for (int i = gridColIndex; i < gridColIndex + gridSpan; i++)
            {
                tW += tableGridCol.get(i);
            }
            AttrManage.instance().setTableCellWidth(attr, tW);
        }
        
        // 合并单元格
        temp = tcPr.element("vMerge");
        if (temp != null)
        {
            AttrManage.instance().setTableVerMerged(attr, true);
            if (temp.attributeValue("val") != null)
            {
                AttrManage.instance().setTableVerFirstMerged(attr, true);
            }
        }
        temp = tcPr.element("vAlign");
        if (temp != null)
        {
            String val = temp.attributeValue("val");
            // vertical center alignment
            if ("center".equals(val))
            {
                AttrManage.instance().setTableCellVerAlign(attr, WPAttrConstant.PARA_VER_ALIGN_CENTER);
            }
            else if("bottom".equals(val))
            {
                AttrManage.instance().setTableCellVerAlign(attr, WPAttrConstant.PARA_VER_ALIGN_BOTTOM);
            }
        }
        return gridSpan;
    }
    
    private void processParagraphs_Table(List<Element> elems, int level)
    {
    	for (Element elem : elems)
        {
        	if ("sdt".equals(elem.getName()))
            {
        		elem = elem.element("sdtContent");
                if (elem != null)
                {
                	processParagraphs_Table(elem.elements(), level);
                }
            }
        	
            if ("p".equals(elem.getName()))
            {
                processParagraph(elem, level);
            }
            else if ("tbl".equals(elem.getName()))
            {
            	processEmbeddedTable(elem, level);
            }
        }
    }
    
    /**
     * 
     */
    private void processEmbeddedTable(Element table, int level)
    {
        List<Element> rows = table.elements("tr");
        for (Element row : rows)
        {
            List<Element> cells = row.elements("tc");
            for (Element cell : cells)
            {
            	processParagraphs_Table(cell.elements(), level);
            }
        }
    }

    /**
     * 
     */
    private void processParagraph(Element para, int level)
    {   
        ParagraphElement paraElem = new ParagraphElement();
        long t = offset;
        paraElem.setStartOffset(offset);
        // 段落属性
        processParaAttribute(para.element("pPr"), paraElem.getAttribute(), level);      
        // 处理leaf
        processRun(para, paraElem, true);

        paraElem.setEndOffset(offset);
        if (offset > t)
        {
            //
            wpdoc.appendParagraph(paraElem, offset);
        }
    }
    
    /**
     * 
     * @param attr
     */
    private void processParaAttribute(Element pPr, IAttributeSet attr, int level)
    {
        if (level > 0)
        {
            AttrManage.instance().setParaLevel(attr, level);
        }
        if (pPr == null)
        {
            return;
        }
        
     // 样式
        Element temp = pPr.element("pStyle");
        if (temp != null)
        {
            String val = temp.attributeValue("val");
            if (val != null && val.length() > 0)
            {
                AttrManage.instance().setParaStyleID(attr, styleStrID.get(val));
            }
        }
        else
        {
        	AttrManage.instance().setParaStyleID(attr, 0);
        }
        
        // 段前段后
        temp = pPr.element("spacing");
        if (temp != null)
        {
            // 行单位
            String val = temp.attributeValue("line");
            if (val != null)
            {
                int lineUnit = Integer.parseInt(val);
                // 默认行单位是12磅
                lineUnit = lineUnit == 0 ? 240 : lineUnit;
                // 段前
                val = temp.attributeValue("beforeLines");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaBefore(attr, (int)(Integer.parseInt(val) / 100.f * lineUnit));
                }
                if (val == null)
                {
                    // 段前
                    val = temp.attributeValue("before");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaBefore(attr, Integer.parseInt(val));
                    }
                }
                // 段后
                val = temp.attributeValue("afterLines");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaAfter(attr, (int)(Integer.parseInt(val) / 100.f * lineUnit));
                }
                if (val == null)
                {
                    // 段后
                    val = temp.attributeValue("after");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaAfter(attr, Integer.parseInt(val));
                    }
                }
            }
            // 非单位
            else
            {
                // 段前
                val = temp.attributeValue("before");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaBefore(attr, Integer.parseInt(val));
                }
                else
                {
                	val = temp.attributeValue("beforeLines");
                	if(val != null && val.length() > 0)
                	{
                		AttrManage.instance().setParaBefore(attr, (int)(Integer.parseInt(val) / 100.f * 240));
                	}
                }
                
                // 段后
                val = temp.attributeValue("after");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaAfter(attr, Integer.parseInt(val));
                }
                else
                {
                	val = temp.attributeValue("afterLines");
                	if(val != null && val.length() > 0)
                	{
                		AttrManage.instance().setParaAfter(attr, (int)(Integer.parseInt(val) / 100.f * 240));
                	}
                }
            }
            // 行距
            val = temp.attributeValue("lineRule");
            float lineSpace = 0;
            if ((temp.attributeValue("line") != null))
            {
                lineSpace = Float.parseFloat(temp.attributeValue("line"));
            }
            // 多倍行距
            if ("auto".equals(val))
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE); 
                // 行距
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr,lineSpace / 240.f);
                }
            }
            // 最小值
            else if("atLeast".equals(val))
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_LEAST); 
                // 行距
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr, lineSpace);
                }
            }
            // 固定值
            else if ("exact".equals(val))
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SPACE_EXACTLY); 
                // 行距
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr, -lineSpace);
                }
            }
            else
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE);
                // 行距默认值
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr, -lineSpace / 240.f);
                }
            }                    
            
        }
        // 左右缩进
        temp = pPr.element("ind");
        if (temp != null)
        {
        	int left = 0;
        	int fontSize = 12;
        	Style style = StyleManage.instance().getStyle(styleStrID.get("docDefaults"));
        	if(style != null)
        	{
        		IAttributeSet  defaultAttr = style.getAttrbuteSet(); 
        		if(defaultAttr != null)
        		{
        			fontSize = AttrManage.instance().getFontSize(defaultAttr,defaultAttr);
        		}
        	}
        	
            // 左缩进
            String val = temp.attributeValue("leftChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
            	float leftChars = Integer.parseInt(val) / 100f;
            	AttrManage.instance().setParaIndentLeft(attr, Math.round(fontSize * leftChars * MainConstant.POINT_TO_TWIPS));
            }
            else if((val = temp.attributeValue("left")) != null)
            {
            	if (val != null && val.length() > 0)
                {
                    left = Integer.parseInt(val);
                    AttrManage.instance().setParaIndentLeft(attr, left);
                }
            }
            
            // 右缩进
            val = temp.attributeValue("rightChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
            	float rightChars = Integer.parseInt(val) / 100f;
            	AttrManage.instance().setParaIndentRight(attr, Math.round(fontSize * rightChars * MainConstant.POINT_TO_TWIPS));
            }
            else if((val = temp.attributeValue("right")) != null)
            {
            	if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaIndentRight(attr, Integer.parseInt(val));
                }
            }
            
            // 首行缩进
            val = temp.attributeValue("firstLineChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
            	float firstLineChars = Integer.parseInt(val) / 100f;
            	AttrManage.instance().setParaSpecialIndent(attr, Math.round(fontSize * firstLineChars * MainConstant.POINT_TO_TWIPS));
            }
            else if((val = temp.attributeValue("firstLine")) != null)
            {
            	if (val != null && val.length() > 0)
            	{
            		AttrManage.instance().setParaSpecialIndent(attr, Integer.parseInt(val));
            	}
                
            }
            // 悬挂缩进
            val = temp.attributeValue("hangingChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
            	float hangingChars = Integer.parseInt(val) / 100f;            	
            	int hanging = -Math.round(fontSize * hangingChars * MainConstant.POINT_TO_TWIPS);
            	AttrManage.instance().setParaSpecialIndent(attr, hanging);
                // 悬挂缩进值也设置到左缩进，左缩进需要减去悬挂缩进
            	if(left == 0)
            	{
            		//left indent maybe not init, so get it repeatly
            		left = AttrManage.instance().getParaIndentLeft(attr);
            	}
                if (hanging < 0)
                {
                    AttrManage.instance().setParaIndentLeft(attr, left + hanging);
                }
            }
            else if((val = temp.attributeValue("hanging")) != null)
            {
            	if (val != null && val.length() > 0)
                {
                    int sp = -Integer.parseInt(val);
                    AttrManage.instance().setParaSpecialIndent(attr, sp);
                    // 悬挂缩进值也设置到左缩进，左缩进需要减去悬挂缩进
                    if (sp < 0)
                    {
                        AttrManage.instance().setParaIndentLeft(attr, left + sp);
                    }
                }
            }
        }
        // 对齐方式
        temp = pPr.element("jc");
        if(temp != null)
        {
            String value = temp.attributeValue("val");
            // left
            if ("left".equals(value)
                || "both".equals(value)
                || "distribute".equals(value))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_LEFT);
            }
            else if ("center".equals(value))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_CENTER);
            }
            else if ("right".equals(value))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_RIGHT);
            }
        }
        
        
        // bullet and number
        temp = pPr.element("numPr");
        if (temp != null)
        {
            Element t = temp.element("ilvl");
            // level
            if (t != null)
            {
                AttrManage.instance().setParaListLevel(attr, Integer.parseInt(t.attributeValue("val")));
            }            
            // list ID
            t = temp.element("numId");
            if (t != null)
            {
                String val = t.attributeValue("val");
                if (val != null)
                {
                    String bnID = bulletNumbersID.get(val);
                    if (bnID != null)
                    {
                        AttrManage.instance().setParaListID(attr, Integer.parseInt(bnID));
                    }
                    else
                    {
                        //AttrManage.instance().setParaListID(attr, Integer.parseInt(val));
                    }
                }                
            }
        }
        else
        {
        	//check from paragraph style
        	Style style = StyleManage.instance().getStyle(AttrManage.instance().getParaStyleID(attr));
        	if(style != null)
        	{
        		int paraListLevel = AttrManage.instance().getParaListLevel(style.getAttrbuteSet());
            	int paraListID = AttrManage.instance().getParaListID(style.getAttrbuteSet());
            	if(paraListID > -1)
            	{
            		if(paraListLevel < 0)
            		{
            			paraListLevel = 0;
            		}
            		
            		AttrManage.instance().setParaListID(attr, paraListID);
            	}
            	
            	if(paraListLevel > -1)
            	{
            		AttrManage.instance().setParaListLevel(attr, paraListLevel);
            	}
        	}
        }
        
        temp =  pPr.element("tabs");
        if (temp != null)
        {
            List<Element> tabs = temp.elements("tab");
            if (tabs != null)
            {
                for (Element tab : tabs)
                {
                    if ("clear".equals(tab.attributeValue("val")))
                    {
                        String val = tab.attributeValue("pos");
                        if (val != null && val.length() > 0)
                        {
                            AttrManage.instance().setParaTabsClearPostion(attr, Integer.parseInt(val));
                        }
                    }
                }
            }
        }
        if (!isProcessSectionAttribute)
        {
            processSectionAttribute(pPr.element("sectPr"));
        }
    }

    private boolean processRun(Element para, ParagraphElement paraElem, boolean addBreakPage)
    {
    	return processRun(para, paraElem, (byte)-1, addBreakPage) ;
    }
    
    /**
     * 
     * @param para
     * @param paraElem
     * @param pgNumberType
     * @param addBreakPage
     * @return
     */
    private boolean processRun(Element para, ParagraphElement paraElem, byte pgNumberType, boolean addBreakPage) 
    {
        List<Element> runs = para.elements();
        LeafElement leaf = null;
        boolean hasLeaf = false;
        String fieldCodeStr ="";
        String fieldTextStr = "";
        boolean hasField = false;
        boolean pageBreak = false;
        for (Element run : runs)
        {
            // hyper
            String name = run.getName();
            if("smartTag".equals(name))
            {
            	hasLeaf = processRun(run, paraElem, false);
            }
            else if ("hyperlink".equals(name))
            {
                leaf = processHyperlinkRun(run, paraElem);
                hasLeaf = leaf != null;
                continue;
            }
            else if ("bookmarkStart".equals(name))
            {
                String val = run.attributeValue("name");
                if (val != null)
                {
                    control.getSysKit().getBookmarkManage().addBookmark(new Bookmark(val, offset, offset));
                }
                continue;
            }
            else if ("fldSimple".equals(name))
            {
            	String instr = run.attributeValue("instr");
            	byte pageNumberType = -1;
            	if(instr != null)
            	{
            		if(instr.contains("NUMPAGES"))
                    {
                    	pageNumberType = WPModelConstant.PN_TOTAL_PAGES;
                    }
                    else if(instr.contains("PAGE"))
                    {
                    	pageNumberType = WPModelConstant.PN_PAGE_NUMBER;
                    }
            	}
            	
            	hasLeaf = processRun(run, paraElem, pageNumberType, false);
        		
        		leaf = null;
            }
            else if ("sdt".equals(name))
            {                      
                Element sdtContent = run.element("sdtContent");
                if (sdtContent != null)
                {
                	Element ele =  null;
            		if((ele = run.element("sdtPr")) != null 
            				&& (ele = ele.element("docPartObj")) != null
            				&& (ele = ele.element("docPartGallery")) != null)
            		{
            			String docPartGallery = ele.attributeValue("val");
            			if(docPartGallery != null)
            			{
            				if(isProcessHF && docPartGallery.contains("Margins"))
                			{
                				return false;
                			}
            				else if(docPartGallery.contains("Watermarks"))
                			{
                				isProcessWatermark = true;
                			}
            			}            			
            		}
            		
            		hasLeaf = processRun(sdtContent, paraElem, false);
            		
            		leaf = null;
                }
            }
            else if("ins".equals(name))
            {
            	hasLeaf = processRun(run, paraElem, false);
            }
            else if ("r".equals(name))
            {                
                // field 
            	
                Element fld = run.element("fldChar");
                if (fld != null)
                {
                    String fldType = fld.attributeValue("fldCharType");
                    if ("begin".equals(fldType))
                    {
                        hasField = true;
                        continue;
                    }
                    else if ("separate".equals(fldType))
                    {
//                        hasField = false;
                        continue;
                    }
                    else if("end".equals(fldType))
                    {
                        hasField = false;
                        if (fieldCodeStr != null)
                        {
                            String str = "";
                            byte encloseType = -1;
                            byte pageNumberType = -1;
                            // 圆
                            if (fieldCodeStr.indexOf('\u25cb') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_ROUND;
                            }
                            // 正方形
                            else if (fieldCodeStr.indexOf('\u25a1') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_SQUARE; 
                            }
                            // 三角形
                            else if (fieldCodeStr.indexOf('\u25b3') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_TRIANGLE;
                            }
                            // 菱形
                            else if (fieldCodeStr.indexOf('\u25c7') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_RHOMBUS;
                            }
                            //////////////////////////////////////////////////////page number property
                            else if(fieldCodeStr.contains("NUMPAGES"))
                            {
                            	str = fieldTextStr;
                            	pageNumberType = WPModelConstant.PN_TOTAL_PAGES;
                            }
                            else if(fieldCodeStr.contains("PAGE"))
                            {
                            	str = fieldTextStr;
                            	pageNumberType = WPModelConstant.PN_PAGE_NUMBER;
                            }
                            else
                            {
                            	str = fieldTextStr;
                            }
                            
                            if (str.length() > 0)
                            {
                                hasLeaf = true;
                                leaf = new LeafElement(str);
                                // 属性
                                Element rPr = run.element("rPr"); 
                                if (rPr != null)
                                {
                                    processRunAttribute(rPr, leaf.getAttribute());
                                }
                
                                leaf.setStartOffset(offset);
                                offset += str.length();
                                leaf.setEndOffset(offset);                                
                                	
                                if(encloseType >= 0)
                                {
                                	AttrManage.instance().setEncloseChanacterType(leaf.getAttribute(), encloseType);
                                }
                                else if(isProcessHF && hfPart !=  null && pageNumberType > 0)
                                {
                                	AttrManage.instance().setFontPageNumberType(leaf.getAttribute(), pageNumberType);
                                }
                                
                                paraElem.appendLeaf(leaf);
                            }                        
                            fieldCodeStr = "";
                            fieldTextStr = "";
                        }
                        continue;
                    }
                }
                if(hasField)
                {
                    Element fldCode = run.element("instrText");
                    if (fldCode != null)
                    {
                        fieldCodeStr += fldCode.getText();
                    }
                    else
                    {
                    	Element text = run.element("t");
                        if (text != null)
                        {
                            fieldTextStr += text.getText();
                        }
                    }
                    continue;
                }
                
                //Inline Embedded Object
                Element object = run.element("object");
                if(object != null)
                {
                	for (Iterator< ? > it = object.elementIterator(); it.hasNext();)
                    {
                        processAutoShapeForPict((Element)it.next(), paraElem, null, 1.0f, 1.0f);
                    }
                    leaf = null;
                    continue;
                }
                
                //picture and diagram
                Element drawing = run.element("drawing");
                if (drawing != null)
                {
                    processPictureAndDiagram(drawing, paraElem);
                    leaf = null;
                    continue;
                } 
                
                //autoshape for 2007
                Element pict = run.element("pict");
                if (pict != null)
                {
                    for (Iterator< ? > it = pict.elementIterator(); it.hasNext();)
                    {
                        processAutoShapeForPict((Element)it.next(), paraElem, null, 1.0f, 1.0f);
                    }
                    leaf = null;
                    continue;
                }
                
                //autoshape for 2010
                Element alternateContent = run.element("AlternateContent");
                if (alternateContent != null)
                {
                    processAlternateContent(alternateContent, paraElem);
                    leaf = null;
                    continue;
                }
                Element ruby = run.element("ruby");
                if (ruby != null)
                {
                    ruby = ruby.element("rubyBase");
                    if (ruby != null)
                    {
                        run = ruby.element("r");
                        if (run == null)
                        {
                            continue;
                        }
                    }
                }
                String str = "";
                Element br = run.element("br");
                Element text = run.element("t");
                if (text != null)
                {
                    str = text.getText();
                    if (br != null)
                    {
                        str = String.valueOf('\u000b') + str;
                    }
                }
                else if (br != null)
                {
                    // 分页符
                    if ("page".equals(br.attributeValue("type")))
                    {
                        pageBreak = true;
                        str = String.valueOf('\f');
                    }
                    else
                    {
                        str = String.valueOf('\u000b');
                    }
                }
                int len = str.length();
                if (len > 0)
                {
                    hasLeaf = true;
                    leaf = new LeafElement(str);
                    // 属性
                    Element rPr = run.element("rPr"); 
                    if (rPr != null)
                    {
                        processRunAttribute(rPr, leaf.getAttribute());
                    }
    
                    if(isProcessHF && hfPart !=  null && pgNumberType > 0)
                    {
                    	AttrManage.instance().setFontPageNumberType(leaf.getAttribute(), pgNumberType);
                    }
                    
                    leaf.setStartOffset(offset);
                    offset += len;
                    leaf.setEndOffset(offset);
                    paraElem.appendLeaf(leaf);
                    // 
                    if (fieldCodeStr != null)
                    { 
                        String linkURL = null;
                        if (fieldCodeStr.indexOf("mailto") >= 0)
                        {
                            linkURL = fieldCodeStr.substring(fieldCodeStr.indexOf("mailto"));
                            int index = linkURL.indexOf("\"");
                            if (index > 0)
                            {
                                linkURL = linkURL.substring(0, index);
                            }
                        }
                        else if (fieldCodeStr.indexOf("HYPERLINK") >= 0)
                        {
                            linkURL = fieldCodeStr.substring(fieldCodeStr.indexOf("\"") + 1);
                            int index = linkURL.lastIndexOf("/");
                            if (index > 0)
                            {
                                linkURL = linkURL.substring(0, index);
                            }
                        }
                        if (linkURL != null)
                        {                            
                            int hyIndex = control.getSysKit().getHyperlinkManage().addHyperlink(linkURL, Hyperlink.LINK_URL);
                            if (hyIndex >= 0)
                            {
                                AttrManage.instance().setFontColor(leaf.getAttribute(), Color.BLUE);
                                AttrManage.instance().setFontUnderline(leaf.getAttribute(), 1);
                                AttrManage.instance().setFontUnderlineColr(leaf.getAttribute(), Color.BLUE);
                                AttrManage.instance().setHyperlinkID(leaf.getAttribute(), hyIndex);
                            }
                        }
                        fieldCodeStr = "";
                        fieldTextStr = "";
                    }
                }
            }
        }
        // 如果没有 r 元素，说明只有一个回车符的段落
        if (!hasLeaf)
        {
            leaf = new LeafElement("\n");
            Element ele = para.element("pPr");
            if (ele != null)
            {
                ele = ele.element("rPr");
            }
            if (ele != null)
            {
                processRunAttribute(ele, leaf.getAttribute());
            }
            leaf.setStartOffset(offset);
            offset += 1;
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            return hasLeaf;
        }
        if (addBreakPage && leaf != null && !pageBreak)
        {
            leaf.setText(leaf.getText(wpdoc) + "\n");
            offset++;
        }
        return hasLeaf;
    }
    
    
    /**
     * 
     */
    private LeafElement processHyperlinkRun(Element hyperlink, ParagraphElement paraElem)
    {
        PackageRelationship hyRel = null;
        try
        {
            String id = hyperlink.attributeValue("id");
            if (id != null)
            {
                hyRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.HYPERLINK_PART).getRelationshipByID(id);
            }
        }
        catch (InvalidFormatException e)
        {
            control.getSysKit().getErrorKit().writerLog(e, true);
        }
        int hyIndex = -1;
        if (hyRel != null)            
        {
            hyIndex = control.getSysKit().getHyperlinkManage().addHyperlink(hyRel.getTargetURI().toString(), Hyperlink.LINK_URL);
        }
        if (hyIndex == -1)
        {
            String anchor = hyperlink.attributeValue("anchor");
            if (anchor != null)
            {
                hyIndex = control.getSysKit().getHyperlinkManage().addHyperlink(anchor, Hyperlink.LINK_BOOKMARK);
            }
        }
        List<Element> runs = hyperlink.elements("r");
        LeafElement leaf = null;        
        for (Element run : runs)
        {
            Element fldCode = run.element("instrText");
            if (fldCode != null)
            {
                String text = fldCode.getText();
                if (text != null && text.contains("PAGEREF"))
                {
                    hyIndex = -1;
                }
            }
            Element ruby = run.element("ruby");
            if (ruby != null)
            {
                ruby = ruby.element("rubyBase");
                if (ruby != null)
                {
                    run = ruby.element("r");
                    if (run == null)
                    {
                        continue;
                    }
                }
            }
            Element text = run.element("t");
            if (text == null)
            {
                Element drawing = run.element("drawing");
                if (drawing != null)
                {
                    processPictureAndDiagram(drawing, paraElem);
                    leaf = null;
                }
                continue;
            }
            String str = text.getText();
            int len = str.length()  ;
            if (len > 0)
            {
                leaf = new LeafElement(str);
                IAttributeSet attr = leaf.getAttribute();
                // 属性
                Element rPr = run.element("rPr"); 
                if (rPr != null)
                {
                    processRunAttribute(rPr, attr);
                }

                leaf.setStartOffset(offset);
                offset += len;
                leaf.setEndOffset(offset);
                paraElem.appendLeaf(leaf);
                
                if (hyIndex >= 0)
                {
                    AttrManage.instance().setFontColor(attr, Color.BLUE);
                    AttrManage.instance().setFontUnderline(attr, 1);
                    AttrManage.instance().setFontUnderlineColr(attr, Color.BLUE);
                    AttrManage.instance().setHyperlinkID(attr, hyIndex);
                }
            }
        }
        return leaf;
    }
   
    /**
     * add shape
     * @param shape
     * @param paraElem
     */
    private void addShape(AbstractShape shape, ParagraphElement paraElem)
    {
        if (shape != null && paraElem != null)
        {
            LeafElement leaf = new LeafElement(String.valueOf(1));
            leaf.setStartOffset(offset);
            offset++;
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            
            AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(shape));
        }
    }
    
    private PictureShape addPicture(Element pic, Rectangle rect)
    {
        PictureShape shape = null;
        if (pic != null && rect != null)
        {
            Element blipFill = pic.element("blipFill");
            if (blipFill != null)
            {
                PictureEffectInfo effectInfor = PictureEffectInfoFactory.getPictureEffectInfor(blipFill);
                
                Element blip = blipFill.element("blip");
                if (blip != null)
                {
                    String embed = blip.attributeValue("embed");
                    if (embed != null)
                    {
                        PackagePart picPart = null;
                        if (isProcessHF && hfPart != null)
                        {
                            picPart = zipPackage.getPart(hfPart.getRelationship(embed).getTargetURI());
                        }
                        else
                        {
                            picPart = zipPackage.getPart(packagePart.getRelationship(embed).getTargetURI());
                        }
                        if (picPart != null)
                        {
                            shape = new PictureShape();
                            try
                            {
                                shape.setPictureIndex(control.getSysKit().getPictureManage().addPicture(picPart));
                            }
                            catch (Exception e)
                            {
                                control.getSysKit().getErrorKit().writerLog(e);
                            }
                            
                            shape.setZoomX((short)1000);
                            shape.setZoomY((short)1000);
                            shape.setPictureEffectInfor(effectInfor);
                            shape.setBounds(rect);
                        }
                    }        
                }
            }
        }
        return shape;
    }
    
    private byte getRelative(String relativeFrom)
    {
        // 相对于页边距
        if ("margin".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_MARGIN;
        }
        // 相对于页面
        else if ("page".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_PAGE;
        }
        // Column
        else if ("column".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_COLUMN;
        }
        //character
        else if ("character".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_CHARACTER;
        }
        //leftMargin
        else if ("leftMargin".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_LEFT;
        }
        // rightMargin
        else if ("rightMargin".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_RIGHT;
        }
        //insideMargin
        else if ("insideMargin".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_INNER;
        }
        //outsideMargin
        else if ("outsideMargin".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_OUTER;
        }
        //paragraph
        else if ("paragraph".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_PARAGRAPH;
        }
        //line
        else if ("line".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_LINE;
        }
        //topMargin
        else if ("topMargin".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_TOP;
        }
        //bottomMargin
        else if ("bottomMargin".equalsIgnoreCase(relativeFrom))
        {
            return WPAbstractShape.RELATIVE_BOTTOM;
        }
        
        return WPAbstractShape.RELATIVE_COLUMN;
    }
    
    private byte getAlign(String align)
    {
        // 左对齐
        if ("left".equalsIgnoreCase(align))
        {
            return WPAbstractShape.ALIGNMENT_LEFT;
        }
        // 右对齐
        else if ("right".equalsIgnoreCase(align))
        {
            return WPAbstractShape.ALIGNMENT_RIGHT;
        }
        // 上对齐
        if ("top".equalsIgnoreCase(align))
        {
            return WPAbstractShape.ALIGNMENT_TOP;
        }
        // 下对齐
        else if ("bottom".equalsIgnoreCase(align))
        {
            return WPAbstractShape.ALIGNMENT_BOTTOM;
        }
        // 居中
        else if ("center".equalsIgnoreCase(align))
        {
            return WPAbstractShape.ALIGNMENT_CENTER;
        }
        // 内部
        else if ("inside".equalsIgnoreCase(align))
        {
            return WPAbstractShape.ALIGNMENT_INSIDE;
        }
        // 外部
        else if ("outside".equalsIgnoreCase(align))
        {
            return WPAbstractShape.ALIGNMENT_OUTSIDE;
        }
        
        return WPAbstractShape.ALIGNMENT_ABSOLUTE;
    }
    
    private void processWrapAndPosition_Drawing(WPAbstractShape shape, Element anchor, Rectangle rect)
    {
        //behindDoc or not
        String behindDoc = anchor.attributeValue("behindDoc");
        if("1".equalsIgnoreCase(behindDoc))
        {
            shape.setWrap(WPAbstractShape.WRAP_BOTTOM);
        }
        shape.setWrap(getDrawingWrapType(anchor));
        
        //horizontal position and horizontal relative position
        Element positionHElement = null;
        Element positionVElement = null;
        
        //relative position for word 2010
        List<Element> alternateContents = anchor.elements("AlternateContent");
        for(Element item : alternateContents)
        {
        	Element choice = item.element("Choice");
        	if(choice != null)
        	{
        		if(choice.element("positionH") != null)
        		{
        			positionHElement = choice.element("positionH");
        		}
        		
        		if(choice.element("positionV") != null)
        		{
        			positionVElement = choice.element("positionV");
        		}
        	}
        }
        // 水平
        if(positionHElement == null)
        {                        	
        	positionHElement = anchor.element("positionH");
        }
        if(positionHElement !=  null)
        {
            shape.setHorizontalRelativeTo( getRelative(positionHElement.attributeValue("relativeFrom")));
            if(positionHElement.element("align") != null)
            {
                shape.setHorizontalAlignment(getAlign(positionHElement.element("align").getText()));
            }
            else if(positionHElement.element("posOffset") != null)
            {
            	rect.translate(Math.round(Integer.parseInt(positionHElement.element("posOffset").getText()) 
                    * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH), 0);
            }
            else if(positionHElement.element("pctPosHOffset") != null)
            {
            	//horizontal relative position
            	shape.setHorRelativeValue(Integer.parseInt(positionHElement.element("pctPosHOffset").getText()) / 100);
            	
            	shape.setHorPositionType(WPAbstractShape.POSITIONTYPE_RELATIVE);
            }
        }
        
        //vertical position and vertical relative position
        if(positionVElement == null)
        {
        	positionVElement = anchor.element("positionV");
        }         
        if(positionVElement !=  null)
        {
            shape.setVerticalRelativeTo(getRelative(positionVElement.attributeValue("relativeFrom")));
            if(positionVElement.element("align") != null)
            {
                shape.setVerticalAlignment(getAlign(positionVElement.element("align").getText()));
            }
            else if(positionVElement.element("posOffset") != null)
            {
            	rect.translate(0, Math.round(Integer.parseInt(positionVElement.element("posOffset").getText())
                        * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH));
            }
            else if(positionVElement.element("pctPosVOffset") != null)
            {
            	shape.setVerRelativeValue(Integer.parseInt(positionVElement.element("pctPosVOffset").getText()) / 100);
            	
            	shape.setVerPositionType(WPAbstractShape.POSITIONTYPE_RELATIVE);
            }
        }
    }
    
    /**
     * 
     */
    private void processPictureAndDiagram(Element drawing, ParagraphElement paraElem)
    {   
        // embed picture
        Element inline = drawing.element("inline");
        boolean isInline = true;
        if (inline == null)
        {
            isInline = false;
            // wrap picture
            inline = drawing.element("anchor");
        }
        if (inline != null)
        {  
            Element graphic = inline.element("graphic");
            if (graphic != null)
            {
                Element graphicdata = graphic.element("graphicData");
                if (graphicdata != null)
                {
                    Element pic = graphicdata.element("pic");
                    if (pic != null)
                    {
                        Element spPr = pic.element("spPr");
                        if (spPr != null)
                        {
                        	BackgroundAndFill fill = null;
                        	PackagePart picPart = null;
                        	if(spPr.element("blipFill") != null)
                        	{
                        		Element blipFill = spPr.element("blipFill");
                        		Element blip = blipFill.element("blip");
                                if (blip != null)
                                {
                                    String embed = blip.attributeValue("embed");
                                    if (embed != null)
                                    {
                                    	if (isProcessHF && hfPart != null)
                                        {
                                            picPart = hfPart;
                                        }
                                        else
                                        {
                                            picPart = packagePart;
                                        }
                                    }
                                }
                        	}
                        	
                            PictureShape shape = addPicture(pic, ReaderKit.instance().getShapeAnchor(spPr.element("xfrm"), 1.0f, 1.0f));
                            if(shape != null)
                            {
                            	AutoShapeDataKit.processPictureShape(control, zipPackage, picPart, spPr, themeColor, shape);
                                WPPictureShape wpPictureShape = new WPPictureShape();
                                wpPictureShape.setPictureShape(shape);
                                wpPictureShape.setBounds(shape.getBounds());
                                
                                if(!isInline)
                                {
                                    processWrapAndPosition_Drawing(wpPictureShape, inline, shape.getBounds());
                                }
                                else 
                                {
                                    wpPictureShape.setWrap(WPAbstractShape.WRAP_OLE);
                                }
                                
                                addShape(wpPictureShape, paraElem);
                            }                            
                        }
                    }
                    else if(graphicdata.element("chart") != null)
                    {
                    	//chart
                    	Element chartEle = graphicdata.element("chart");
                        if (chartEle != null && chartEle.attribute("id") != null)
                        {
                            String id = chartEle.attributeValue("id");
                            PackageRelationship ship = packagePart.getRelationship(id);
                            if (ship != null)
                            {
                                PackagePart chartPart = zipPackage.getPart(ship.getTargetURI());
                                try
                                {
                                	AbstractChart abstrChart =  ChartReader.instance().read(control, zipPackage, chartPart, themeColor, MainConstant.APPLICATION_TYPE_WP);
                                    if (abstrChart != null)
                                    {
                                    	Rectangle bounds = new Rectangle();
                                    	Element simplePos = inline.element("simplePos");
                                    	if(simplePos != null)
                                    	{
                                    		String x = simplePos.attributeValue("x");
                                    		if(x != null)
                                    		{
                                    			bounds.x = (int)(Integer.parseInt(x) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                                    		}
                                    		
                                    		String y = simplePos.attributeValue("y");
                                    		if(y != null)
                                    		{
                                    			bounds.y = (int)(Integer.parseInt(y) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                                    		}
                                    	}
                                    	
                                    	Element extent = inline.element("extent");
                                    	if(extent != null)
                                    	{
                                    		String cx = extent.attributeValue("cx");
                                    		if(cx != null)
                                    		{
                                    			bounds.width = (int)(Integer.parseInt(cx) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                                    		}
                                    		
                                    		String cy = extent.attributeValue("cy");
                                    		if(cy != null)
                                    		{
                                    			bounds.height = (int)(Integer.parseInt(cy) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                                    		}
                                    	}
                                    	WPChartShape shape = new WPChartShape();
                                        shape.setAChart(abstrChart);
                                        shape.setBounds(bounds);
                                        if(!isInline)
                                        {
                                            processWrapAndPosition_Drawing(shape, inline, bounds);
                                        }
                                        else 
                                        {
                                        	shape.setWrap(WPAbstractShape.WRAP_OLE);
                                        }
                                        
                                        addShape(shape, paraElem);
                                    }
                                }
                                catch(Exception e)
                                {
                                	return;
                                }
                            }
                        }
                    }
                    else if(graphicdata.element("relIds") != null)
                    {
                        Element dgm = graphicdata.element("relIds");
                        if (dgm != null)
                        { 
                            String val = dgm.attributeValue("dm");
                            if (val != null)
                            {
//                                val = val.substring(3);
//                                int a = Integer.parseInt(val);
//                                val = "rId" + String.valueOf(a + 1);
                                try
                                {
                                    PackageRelationship diagramRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.DIAGRAM_DATA).getRelationshipByID(val);
                                    if (diagramRel != null)
                                    {
                                    	Rectangle rect = new Rectangle();
                                        Element extent = inline.element("extent");
                                        if (extent != null)
                                        {
                                            if (extent.attribute("cx") != null)
                                            {
                                                val = extent.attributeValue("cx");
                                                if (val != null && val.length() > 0)
                                                {
                                                    rect.width = (int)(Integer.parseInt(val) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                                                }
                                            }
                                            if (extent.attribute("cy") != null)
                                            {
                                                val = extent.attributeValue("cy");
                                                if (val != null && val.length() > 0)
                                                {
                                                    rect.height = (int)(Integer.parseInt(val) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                                                }
                                            }
                                        }
                                        
                                    	PackagePart dataPart = zipPackage.getPart(diagramRel.getTargetURI());
                                    	processSmart(control, zipPackage, dataPart, paraElem, inline, rect, isInline);
                                    }
                                }
                                catch (Exception e)
                                {
                                    
                                }
                                
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void processSmart(IControl control, ZipPackage zipPackage, PackagePart dataPart, ParagraphElement paraElem, 
    		Element anchor, Rectangle rect, boolean isInline ) throws Exception
    {    	 
         if (dataPart != null)
         {
             InputStream in = dataPart.getInputStream();
             if (in != null)
             {
                 SAXReader saxreader = new SAXReader();
                 Document dataDoc = saxreader.read(in);
                 in.close();
                 Element root = dataDoc.getRootElement();
                 
                 BackgroundAndFill fill = AutoShapeDataKit.processBackground(control, zipPackage,  dataPart, root.element("bg"), themeColor);;
                 Line line = LineKit.createLine(control, zipPackage, dataPart, root.element("whole").element("ln"), themeColor);
                 PackagePart drawingPart = null;
                 Element e = null;
                 if((e = root.element("extLst")) != null && (e = e.element("ext")) != null && (e = e.element("dataModelExt")) != null)
                 {
                 	String relId = e.attributeValue("relId");
                 	if(relId != null)
                 	{
                 		PackageRelationship smartArDrawingRel = packagePart.getRelationship(relId);
                 		drawingPart = zipPackage.getPart(smartArDrawingRel.getTargetURI());
                 	}
                 	
                 }
                 if(drawingPart == null)
                 {
                 	return;
                 }
                 
                 in = drawingPart.getInputStream();        
                 Document drawingDoc = saxreader.read(in);
                 in.close();
                 
                 if (drawingDoc != null)
                 {
                     root = drawingDoc.getRootElement();
                     if (root != null)
                     {
                         Element spTree = root.element("spTree");
                         if (spTree != null)
                         {
                        	 WPGroupShape groupShape = new WPGroupShape();
                        	 WPAutoShape autoShape = new WPAutoShape();
                             autoShape.addGroupShape(groupShape);
                             
                        	 short wrapType = WPAbstractShape.WRAP_OLE;
                             if(!isInline)
                             {
                            	 processWrapAndPosition_Drawing(autoShape, anchor, rect);
                                 wrapType = getDrawingWrapType(anchor);
                             }                             
                             
                             groupShape.setBounds(rect);                             
                             autoShape.setBackgroundAndFill(fill);
                             autoShape.setLine(line);
                             autoShape.setShapeType(ShapeTypes.Rectangle);
                             if(wrapType != WPAbstractShape.WRAP_OLE)
                             {
                                 groupShape.setWrapType(wrapType);
                                 autoShape.setWrap(wrapType);
                             }
                             else 
                             {
                                 groupShape.setWrapType(WPAbstractShape.WRAP_OLE);
                                 autoShape.setWrap(WPAbstractShape.WRAP_OLE);
                             }                                                            
                             
                             autoShape.setBounds(rect);
                             for (Iterator< ? > it = spTree.elementIterator(); it.hasNext();)
                             {
                                 processAutoShape2010(drawingPart, paraElem, (Element)it.next(), groupShape, 1.0f, 1.0f, 0, 0, false);
                             }
                             addShape(autoShape, paraElem);
                         }
                     }
                 }
             }
         }
    }
    
    /**
     * 
     * @param paraElem
     */
    private void processAutoShapeForPict(Element sp, ParagraphElement paraElem, WPGroupShape parent, float zoomX, float zoomY)
    {
        String name = sp.getName();
        if ("group".equalsIgnoreCase(name))
        {
        	String val = sp.attributeValue("id");
        	if (val != null && (val.startsWith("Genko")
                    || val.startsWith("Header")
                    || val.startsWith("Footer")))
        	{
        		return;
        	}
        	
            Rectangle rect = null;
            AbstractShape shape = null;
            WPGroupShape groupShape = new WPGroupShape();
            if (parent == null)
            {
                shape = new WPAutoShape();
                ((WPAutoShape)shape).addGroupShape(groupShape);
            }
            else
            {
                shape = groupShape;
            }
            
            rect = processAutoShapeStyle(sp, shape, parent, zoomX, zoomY);
            if (rect != null)
            {
                float x = 0;
                float y = 0;
                float w = 0;
                float h = 0;
                String temp;
                String[] values;
                float[] zoom = {1.0f, 1.0f};
                Rectangle childRect = new Rectangle();
                if (sp.attribute("coordorigin") != null)
                {
                    temp = sp.attributeValue("coordorigin");
                    if (temp != null && temp.length() > 0)
                    {
                        values = temp.split(",");
                        if (values != null)
                        {
                        	if(values.length == 2)
                        	{
                        		if(values[0].length() > 0)
                            	{
                            		x = Float.parseFloat(values[0]);
                            	}                            
                                y = Float.parseFloat(values[1]);
                        	}
                        	else if(values.length == 1)
                        	{
                        		x = Float.parseFloat(values[0]);
                        	}
                        }
                    }
                }
                
                if (sp.attribute("coordsize") != null)
                {
                    temp = sp.attributeValue("coordsize");
                    if (temp != null && temp.length() > 0)
                    {
                        values = temp.split(",");
                        if (values != null)
                        {
                        	if(values.length == 2)
                        	{
                        		if(values[0].length() > 0)
                        		{
                        			w = Float.parseFloat(values[0]);
                        		}                        		
                                h = Float.parseFloat(values[1]);
                        	}
                        	else if(values.length == 1)
                        	{
                        		w = Float.parseFloat(values[0]);
                        	}
                            
                        }
                    }
                }
                if (w != 0 && h != 0)
                {
                    zoom[0] = rect.width * MainConstant.EMU_PER_INCH / MainConstant.PIXEL_DPI / zoomX / w;
                    zoom[1] = rect.height * MainConstant.EMU_PER_INCH / MainConstant.PIXEL_DPI / zoomY / h;
                }
                rect = processGrpSpRect(parent, rect);
                
                childRect.x = (int)(x * zoom[0] * zoomX * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                childRect.y = (int)(y * zoom[1] * zoomY * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                childRect.width = (int)(w * zoom[0] * zoomX * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                childRect.height = (int)(h * zoom[1] * zoomY * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                if (parent == null)
                {
                    childRect.x += rect.x;
                    childRect.y += rect.y;
                }
                groupShape.setOffPostion(rect.x - childRect.x, rect.y - childRect.y);
                
                groupShape.setBounds(rect);
                groupShape.setParent(parent);
                groupShape.setRotation(shape.getRotation());
                groupShape.setFlipHorizontal(shape.getFlipHorizontal());
                groupShape.setFlipVertical(shape.getFlipVertical());
                
                if(parent == null)
                {
                	short wrapType = getShapeWrapType(sp);
                	groupShape.setWrapType(wrapType);
                	((WPAutoShape)shape).setWrap(wrapType);
                }
                else
                {
                	groupShape.setWrapType(parent.getWrapType());
                }                
                
                for (Iterator< ? > it = sp.elementIterator(); it.hasNext();)
                {
                    processAutoShapeForPict((Element)it.next(), paraElem, groupShape, zoom[0] * zoomX, zoom[1] * zoomY);
                }
                
                IShape[] shapes = groupShape.getShapes();
                for(IShape sh : shapes)
                {
                	if(sh instanceof WPAbstractShape && shape instanceof WPAbstractShape)
                	{
                		((WPAbstractShape)sh).setWrap((short)((WPAbstractShape)shape).getWrap());
                    	((WPAbstractShape)sh).setHorPositionType(((WPAbstractShape)shape).getHorPositionType());
                    	((WPAbstractShape)sh).setHorizontalRelativeTo(((WPAbstractShape)shape).getHorizontalRelativeTo());
                    	((WPAbstractShape)sh).setHorRelativeValue(((WPAbstractShape)shape).getHorRelativeValue());
                    	((WPAbstractShape)sh).setHorizontalAlignment(((WPAbstractShape)shape).getHorizontalAlignment());
                    	
                    	((WPAbstractShape)sh).setVerPositionType(((WPAbstractShape)shape).getVerPositionType());
                    	((WPAbstractShape)sh).setVerticalRelativeTo(((WPAbstractShape)shape).getVerticalRelativeTo());
                    	((WPAbstractShape)sh).setVerRelativeValue(((WPAbstractShape)shape).getVerRelativeValue());
                    	((WPAbstractShape)sh).setVerticalAlignment(((WPAbstractShape)shape).getVerticalAlignment()); 
                	}                	               	
                }
                if (parent == null)
                {
                    addShape(shape, paraElem);
                }
                else
                {
                    parent.appendShapes(shape);
                }
            }
            else
            {
                shape.dispose();
                shape = null;
            }
        }
        else
        {
        	//ignore genko shapes
        	String val = sp.attributeValue("id");
        	if (val != null && (val.startsWith("Genko")
                    || val.startsWith("Header")
                    || val.startsWith("Footer")))
        	{
        		return;
        	}
        	
        	if ("shape".equalsIgnoreCase(name))
            {
                if (sp.element("imagedata") != null)
                {
                    processPicture(sp, paraElem);
                }
                else
                {
                	WPAutoShape shape = processAutoShape(sp, paraElem, parent, zoomX, zoomY, hasTextbox2007(sp));
                	if(shape != null)
                	{
                		//
                    	processTextbox2007(packagePart, shape, sp);
                	}
                }
            }
            else if ("line".equalsIgnoreCase(name) || "polyline".equalsIgnoreCase(name) || "curve".equalsIgnoreCase(name) 
            		||"rect".equalsIgnoreCase(name)||"roundrect".equalsIgnoreCase(name) || "oval".equalsIgnoreCase(name))
            {
            	WPAutoShape shape = processAutoShape(sp, paraElem, parent, zoomX, zoomY, hasTextbox2007(sp));
            	if(shape != null)
            	{
            		//
                	processTextbox2007(packagePart, shape, sp);
            	}
            }
        }
    }
    
    /**
     * 
     * @param sp
     * @return
     */
    private short getShapeWrapType(Element sp)
    { 
        Element wrap = sp.element("wrap");
        if(wrap != null)
        {
            String type = wrap.attributeValue("type");
            
            if("none".equalsIgnoreCase(type))
            {
                return WPAbstractShape.WRAP_OLE;
            }
            if("square".equalsIgnoreCase(type))
            {
                return WPAbstractShape.WRAP_SQUARE;
            }
            if("tight".equalsIgnoreCase(type))
            {
                return WPAbstractShape.WRAP_TIGHT;
            }
            if("topAndBottom".equalsIgnoreCase(type))
            {
                return WPAbstractShape.WRAP_TOPANDBOTTOM;
            }
            if("through".equalsIgnoreCase(type))
            {
                return WPAbstractShape.WRAP_THROUGH;
            }
        }
//        else
        {
            String style = sp.attributeValue("style");
            if(style != null)
            {
                String[] styles = style.split(";");
                for (int i = styles.length - 1; i >= 0; i--)
                {
                    if(styles[i].contains("z-index:"))
                    {
                        int zIndex = Integer.parseInt(styles[i].replace("z-index:", ""));
                        if(zIndex > 0)
                        {
                            return WPAbstractShape.WRAP_TOP;
                        }
                        else
                        {
                            return WPAbstractShape.WRAP_BOTTOM;
                        }
                    }
                }
            }
        }
        
        return -1;
    }
    
    private short getDrawingWrapType(Element anchor)
    { 
        if(anchor != null)
        {
            if(anchor.element("wrapNone") != null)
            {
                String behindDoc = anchor.attributeValue("behindDoc");
                if("1".equalsIgnoreCase(behindDoc))
                {
                    return WPAbstractShape.WRAP_BOTTOM;
                }
                else
                {
                    return WPAbstractShape.WRAP_TOP;
                }
            }
            else if(anchor.element("wrapSquare") != null)
            {
                return WPAbstractShape.WRAP_SQUARE;
            }
            else if(anchor.element("wrapTight") != null)
            {
                return WPAbstractShape.WRAP_TIGHT;
            }
            else if(anchor.element("wrapThrough") != null)
            {
                return WPAbstractShape.WRAP_THROUGH;
            }
            else if(anchor.element("wrapTopAndBottom") != null)
            {
                return WPAbstractShape.WRAP_TOPANDBOTTOM;
            }
            else
            {
                return WPAbstractShape.WRAP_OLE;
            }
        }
        
        return -1;
    }
    
    /**
     * 
     */
    private void processPicture(Element shape, ParagraphElement paraElem)
    {
        boolean isPictureControl = false;
        if (shape != null)
        {
            Element imagedata = shape.element("imagedata");
            // picture control
            if (imagedata == null)
            {
                imagedata = shape.element("rect");
                if (imagedata != null)
                {
                    isPictureControl = true;
                    shape = imagedata;
                    imagedata = imagedata.element("fill");
                }
            }
            if (imagedata != null)
            {
                String id = imagedata.attributeValue("id");
                if (id != null)
                {
                    PackagePart picPart = null;
                    if (isProcessHF && hfPart != null)
                    {
                        picPart = zipPackage.getPart(hfPart.getRelationship(id).getTargetURI());
                    }
                    else
                    {
                        picPart = zipPackage.getPart(packagePart.getRelationship(id).getTargetURI());
                    }
                    String style = shape.attributeValue("style");
                    if (picPart != null && style != null)
                    {
                    	 String s = shape.attributeValue("id");
                         if (s != null && s.indexOf("PictureWatermark") > 0)
                         {
                             isProcessWatermark = true;
                         }
                        
                        try
                        {
                        	int pictureIndex = control.getSysKit().getPictureManage().addPicture(picPart);
                        	short wrapType = getShapeWrapType(shape);
                        	AbstractShape abstractShape = null;
                        	
                            if (isProcessWatermark)
                            {
                           	 	abstractShape = new WatermarkShape();
                           	 	
                           	 	String blacklevel = imagedata.attributeValue("blacklevel");
                           	 	if(blacklevel != null)
                           	 	{
                           	 		((WatermarkShape)abstractShape).setBlacklevel(Float.parseFloat(blacklevel) / 100000f);
                           	 	}
                           	 	String gain = imagedata.attributeValue("gain");
                           	 	if(gain != null)
                           	 	{
                           	 		((WatermarkShape)abstractShape).setGain(Float.parseFloat(gain) / 100000f);
                           	 	}
                           	 	
                           	 	((WatermarkShape)abstractShape).setWatermarkType(WatermarkShape.Watermark_Picture);
                           	 
                           	 	((WatermarkShape)abstractShape).setPictureIndex(pictureIndex);
                           	 	
                           	 	((WatermarkShape)abstractShape).setWrap(wrapType);
                            }
                            else
                            {
                            	PictureEffectInfo effectInfor = PictureEffectInfoFactory.getPictureEffectInfor_ImageData(imagedata);
                            	PictureShape pictureShape = new PictureShape();
                            	pictureShape.setPictureIndex(pictureIndex);
                            	pictureShape.setZoomX((short)1000);
                            	pictureShape.setZoomY((short)1000);
                            	pictureShape.setPictureEffectInfor(effectInfor);
                            	
                            	abstractShape = new WPPictureShape();
                            	((WPPictureShape)abstractShape).setPictureShape(pictureShape); 
                            	((WPPictureShape)abstractShape).setWrap(wrapType);
                            }
                        	 
                            Rectangle rect = processAutoShapeStyle(shape, abstractShape, null, (short)1000, (short)1000);
                            if(!isProcessWatermark)
                            {
                            	PictureShape picShape = ((WPPictureShape)abstractShape).getPictureShape();
                            	picShape.setBounds(rect);
                                
                                picShape.setBackgroundAndFill(processBackgroundAndFill(shape));
                                picShape.setLine(processLine(shape));
                                                                
                            }
                        	
                            addShape(abstractShape, paraElem);
                            
                            isProcessWatermark = false;
                        }
                        catch (Exception e)
                        {
                            control.getSysKit().getErrorKit().writerLog(e);
                        }                         
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param value
     * @param filled
     * @return
     */
    private int getColor(String value, boolean filled)
    {
    	if(value == null)
    	{
    		if (filled)
            {
                return Color.WHITE;
            }
            else
            {
                return Color.BLACK;
            }
    	}
    	
        int index = value.indexOf(" ");
        if (index > 0)
        {
            value = value.substring(0, index);
        }
        char firstChar = value.charAt(0);
        if (firstChar == '#')
        {
        	if(value.length() > 6)
        	{
        		return Color.parseColor(value);
        	}
        	else if(value.length() == 4)
        	{
        		/**
        		 * #fc9-----#ffcc99
        		 */
        		StringBuilder builder = new StringBuilder();
        		builder.append('#');
        		for(int i = 1; i < 4; i++)
        		{
        			builder.append(value.charAt(i));
        			builder.append(value.charAt(i));
        		}
        		
        		return Color.parseColor(builder.toString());
        	}
            
        }
        if (value.contains("black") || value.contains("darken"))
        {
            return Color.BLACK;
        }
        else if (value.contains("green"))
        {
            return 0xff008000;
        }
        else if (value.contains("silver"))
        {
            return 0xffc0c0c0;
        }
        else if (value.contains("lime"))
        {
            return 0xff00ff00;
        }
        else if (value.contains("gray"))
        {
            return 0xff808080;
        }
        else if (value.contains("olive"))
        {
            return 0xff808000;
        }
        else if (value.contains("white"))
        {
            return Color.WHITE;
        }
        else if (value.contains("yellow"))
        {
            return 0xffffff00;
        }
        else if (value.contains("maroon"))
        {
            return 0xff800000;
        }
        else if (value.contains("navy"))
        {
            return 0xff000080;
        }
        else if (value.contains("red"))
        {
            return Color.RED;
        }
        else if (value.contains("blue"))
        {
            return 0xff0000ff;
        }
        else if (value.contains("purple"))
        {
            return 0xff800080;
        }
        else if (value.contains("teal"))
        {
            return 0xff008080;
        }
        else if (value.contains("fuchsia"))
        {
            return 0xffff00ff;
        }
        else if (value.contains("aqua"))
        {
            return 0xff00ffff;
        }
        else
        {
            if (filled)
            {
                return Color.WHITE;
            }
            else
            {
                return Color.BLACK;
            }
        }
    }
    
    public void processRotation(AutoShape shape)
    {
        float angle = shape.getRotation();
        if (shape.getFlipHorizontal())
        {
            angle = -angle;
        }
        if (shape.getFlipVertical())
        {
            angle = -angle;
        }
        
        int shapeType = shape.getShapeType();
        if (shapeType == ShapeTypes.StraightConnector1
            || shapeType == ShapeTypes.BentConnector3 || shapeType == ShapeTypes.CurvedConnector3)
        {
            if((angle == 45 || angle == 135 || angle == 225)
                && !shape.getFlipHorizontal()
                && !shape.getFlipVertical())
            {
                angle -= 90;
            }
        }
        shape.setRotation(angle);
    }
    
    /**
     * 重新计算group shape中的child shape的位置
     * @param rect
     * @return
     */
    private Rectangle processGrpSpRect(GroupShape parent, Rectangle rect)
    {
        if (parent != null)
        {
            rect.x += parent.getOffX();
            rect.y += parent.getOffY();
        }
        return rect;
    }
    
    private float processPolygonZoom(Element shape, AbstractShape autoShape, GroupShape parent, float zoomX, float zoomY)
    {
    	Rectangle rect = null;
        if (shape != null && autoShape != null && shape.attribute("style") != null)
        {
            // style
            if (shape.attribute("style") != null)
            {
                String styleStr = shape.attributeValue("style");
                if (styleStr != null)
                {
                    float w = 0;
                    float h = 0;
                    float x = 0;
                    float y = 0;
                    String[] styles = styleStr.split(";");
                    for (int i = 0; i < styles.length; i++)
                    {
                        int index = -1;
                        String[] temps = styles[i].split(":");

                        if (temps != null && temps[0] != null && temps[1] != null)
                        {
                            if ("left".equalsIgnoreCase(temps[0]))
                            {
                                index = temps[1].indexOf("pt");
                                if (index > 0)
                                {
                                    x += Float.parseFloat(temps[1].substring(0, index));
                                }
                                else
                                {
                                    index = temps[1].indexOf("in");
                                    if (index > 0)
                                    {
                                        x += Float.parseFloat(temps[1].substring(0, index)) * MainConstant.POINT_DPI;
                                    }
                                    else
                                    {
                                        x += Float.parseFloat(temps[1]) * zoomX * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
                                    }
                                }
                            }
                            else if ("top".equalsIgnoreCase(temps[0]))
                            {
                                index = temps[1].indexOf("pt");
                                if (index > 0)
                                {
                                    y += Float.parseFloat(temps[1].substring(0, index));
                                }
                                else
                                {
                                    index = temps[1].indexOf("in");
                                    if (index > 0)
                                    {
                                        y += Float.parseFloat(temps[1].substring(0, index)) * MainConstant.POINT_DPI;
                                    }
                                    else
                                    {
                                        y += Float.parseFloat(temps[1]) * zoomY * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
                                    }
                                }
                            }
                            else if ("margin-left".equalsIgnoreCase(temps[0]))
                            {
                                index = temps[1].indexOf("pt");
                                if (index > 0)
                                {
                                    x += Float.parseFloat(temps[1].substring(0, index));
                                }
                                else
                                {
                                    index = temps[1].indexOf("in");
                                    if (index > 0)
                                    {
                                        x += Float.parseFloat(temps[1].substring(0, index)) * MainConstant.POINT_DPI;
                                    }
                                    else
                                    {
                                        x += Float.parseFloat(temps[1]) * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
                                    }
                                }
                            }
                            else if ("margin-top".equalsIgnoreCase(temps[0]))
                            {                                
                                index = temps[1].indexOf("pt");
                                if (index > 0)
                                {
                                    y += Float.parseFloat(temps[1].substring(0, index));
                                }
                                else
                                {
                                    index = temps[1].indexOf("in");
                                    if (index > 0)
                                    {
                                        y += Float.parseFloat(temps[1].substring(0, index)) * MainConstant.POINT_DPI;
                                    }
                                    else
                                    {
                                        y += Float.parseFloat(temps[1]) * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
                                    }
                                }
                            }
                            else if ("width".equalsIgnoreCase(temps[0]))
                            {
                                index = temps[1].indexOf("pt");
                                if (index > 0)
                                {
                                    w = Float.parseFloat(temps[1].substring(0, index));
                                }
                                else
                                {
                                    index = temps[1].indexOf("in");
                                    if (index > 0)
                                    {
                                        w = Float.parseFloat(temps[1].substring(0, index)) * MainConstant.POINT_DPI;
                                    }
                                    else
                                    {
                                        w = Float.parseFloat(temps[1]) * zoomX * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
                                    }
                                }
                            }
                            else if ("height".equalsIgnoreCase(temps[0]))
                            {
                                index = temps[1].indexOf("pt");
                                if (index > 0)
                                {
                                    h = Float.parseFloat(temps[1].substring(0, index));
                                }
                                else
                                {
                                    index = temps[1].indexOf("in");
                                    if (index > 0)
                                    {
                                        h = Float.parseFloat(temps[1].substring(0, index)) * MainConstant.POINT_DPI;
                                    }
                                    else
                                    {
                                        h = Float.parseFloat(temps[1]) * zoomY * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
                                    }
                                }
                            }       
                        }
                    }
                    rect = new Rectangle();
                    rect.x = (int)(x * MainConstant.POINT_TO_PIXEL);
                    rect.y = (int)(y * MainConstant.POINT_TO_PIXEL);
                    rect.width = (int)(w * MainConstant.POINT_TO_PIXEL);
                    rect.height = (int)(h * MainConstant.POINT_TO_PIXEL);
                    if (autoShape.getType() != AbstractShape.SHAPE_GROUP && ((WPAutoShape)autoShape).getGroupShape() == null)
                    {
                        processGrpSpRect(parent, rect);
                    }
                    
                    if(autoShape instanceof WPAutoShape && ((WPAutoShape)autoShape).getShapeType() == ShapeTypes.ArbitraryPolygon)
                    {
                    	String coordsize = shape.attributeValue("coordsize");
                    	if(coordsize != null && coordsize.length() > 0)
                    	{
                    		String[] size = coordsize.split(",");
                    		float width = Integer.parseInt(size[0]);
                            float height = Integer.parseInt(size[1]);
                            return Math.min(width /rect.width, height / rect.height);
                    	}
                    }
                }
            }
        }
        return 1.0f;
    }
    
    /**
     * return value in point units
     * @param value
     * @param zoom
     * @return
     */
    private float getValueInPt(String value, float zoom)
    {
    	int index = -1;
        if (value.contains("pt"))
        {
        	index = value.indexOf("pt");
            return Float.parseFloat(value.substring(0, index));
        }
        else if (value.contains("in"))
        {
            index = value.indexOf("in");
            return Float.parseFloat(value.substring(0, index)) * MainConstant.POINT_DPI;
        }
        else if(value.contains("mm"))
        {
        	index = value.indexOf("mm");
            return Float.parseFloat(value.substring(0, index)) * MainConstant.MM_TO_POINT;
        }
        else
        {
        	return Float.parseFloat(value) * zoom * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
        }
    }
    
    /**
     * 
     * @param shape
     * @param autoShape
     */

    private Rectangle processAutoShapeStyle(Element shape, AbstractShape autoShape, GroupShape parent, float zoomX, float zoomY)
    {
        Rectangle rect = null;
        if (shape != null && autoShape != null && shape.attribute("style") != null)
        {
            // style
            if (shape.attribute("style") != null)
            {
                String styleStr = shape.attributeValue("style");
                if (styleStr != null)
                {
                    float w = 0;
                    float h = 0;
                    float x = 0;
                    float y = 0;
                    String[] styles = styleStr.split(";");
                    for (int i = 0; i < styles.length; i++)
                    {
                        int index = -1;
                        String[] temps = styles[i].split(":");

                        if (temps != null && temps[0] != null && temps[1] != null)
                        {
                            if ("position".equalsIgnoreCase(temps[0]))
                            {
                                
                            }
                            else if ("left".equalsIgnoreCase(temps[0]))
                            {
                            	x += getValueInPt(temps[1], zoomX);
                            }
                            else if ("top".equalsIgnoreCase(temps[0]))
                            {
                            	y += getValueInPt(temps[1], zoomY);
                            }
                            else if ("text-align".equalsIgnoreCase(temps[0]))
                            {
                                
                            }
                            else if ("margin-left".equalsIgnoreCase(temps[0]))
                            {
                            	x += getValueInPt(temps[1], 1.0f);
                            }
                            else if ("margin-top".equalsIgnoreCase(temps[0]))
                            {                                
                            	y += getValueInPt(temps[1], 1.0f);
                            }
                            else if ("width".equalsIgnoreCase(temps[0]))
                            {
                            	w = getValueInPt(temps[1], zoomX);
                            }
                            else if ("height".equalsIgnoreCase(temps[0]))
                            {
                            	h = getValueInPt(temps[1], zoomY);                                
                            }
                            else if("mso-width-percent".equalsIgnoreCase(temps[0]))
                            {
                            	if (!relativeType.contains(autoShape))
                                {
                            		int[] relativeVal = new int[4];
                                	relativeVal[0] = (int)Float.parseFloat(temps[1]);
                                    relativeType.add(autoShape);
                                    relativeValue.put(autoShape, relativeVal);
                                }
                            	else
                            	{
                            		relativeValue.get(autoShape)[0] = (int)Float.parseFloat(temps[1]);
                            	}
                            	
                            }
                            else if("mso-height-percent".equalsIgnoreCase(temps[0]))
                            {                            	
                                if (!relativeType.contains(autoShape))
                                {
                                	int[] relativeVal = new int[4];
                                	relativeVal[2] = (int)Float.parseFloat(temps[1]);
                                	relativeType.add(autoShape);
                                    relativeValue.put(autoShape, relativeVal);
                                }
                                else
                                {
                                	relativeValue.get(autoShape)[2] = (int)Float.parseFloat(temps[1]);
                                }
                            }                            	
                            else if ("flip".equalsIgnoreCase(temps[0]))
                            {
                               if ("x".equalsIgnoreCase(temps[1]))
                               {
                                   autoShape.setFlipHorizontal(true);
                               }
                               else if ("y".equalsIgnoreCase(temps[1]))
                               {
                                   autoShape.setFlipVertical(true);
                               }
                            }
                            else if ("rotation".equalsIgnoreCase(temps[0]))
                            {
                                if (temps[1].indexOf("fd") > 0)
                                {
                                    temps[1] = temps[1].substring(0, temps[1].length() - 2);
                                    autoShape.setRotation(Integer.parseInt(temps[1]) / 60000);
                                }
                                else
                                {
                                    autoShape.setRotation(Integer.parseInt(temps[1]));
                                }
                            }
                            else if ("mso-width-relative".equalsIgnoreCase(temps[0]))
                            {
                                /*if ("margin".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setWidhtType(WPAutoShape.WIDTH_RELATIVE_MARGIN);
                                }
                                else if ("left-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setWidhtType(WPAutoShape.WIDTH_RELATIVE_LFET);
                                }
                                else if ("right-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setWidhtType(WPAutoShape.WIDTH_RELATIVE_RIGHT);
                                }
                                else if ("inner-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setWidhtType(WPAutoShape.WIDTH_RELATIVE_INNER);
                                }
                                else if ("outer-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setWidhtType(WPAutoShape.WIDTH_RELATIVE_OUTER);
                                }
                                else
                                {
                                    autoShape.setWidhtType(WPAutoShape.WIDTH_RELATIVE);
                                }*/
                            }
                            else if ("mso-height-relative".equalsIgnoreCase(temps[0]))
                            {
                                /*if ("margin".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setHeightType(WPAutoShape.HEIGHT_RELATIVE_MARGIN);
                                }
                                else if ("top-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setHeightType(WPAutoShape.HEIGHT_RELATIVE_TOP);
                                }
                                else if ("bottom-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setHeightType(WPAutoShape.HEIGHT_RELATIVE_BOTTOM);
                                }
                                else if ("inner-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setHeightType(WPAutoShape.HEIGHT_RELATIVE_INNER);
                                }
                                else if ("outer-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    autoShape.setHeightType(WPAutoShape.HEIGHT_RELATIVE_OUTER);
                                }
                                else
                                {
                                    autoShape.setHeightType(WPAutoShape.HEIGHT_RELATIVE);
                                }*/
                            }
                            // 水平位置
                            else if (parent == null && autoShape.getType() != AbstractShape.SHAPE_GROUP 
                                     && "mso-position-horizontal".equalsIgnoreCase(temps[0]))
                            {
                                ((WPAutoShape)autoShape).setHorizontalAlignment(getAlign(temps[1]));
                                    
                            }
                            else if (parent == null && autoShape.getType() != AbstractShape.SHAPE_GROUP 
                                    && "mso-left-percent".equalsIgnoreCase(temps[0]))
                            {
                            	//horizontal relative position
                            	((WPAutoShape)autoShape).setHorRelativeValue(Integer.parseInt(temps[1]));
                            	
                            	((WPAutoShape)autoShape).setHorPositionType(WPAbstractShape.POSITIONTYPE_RELATIVE);
                            }
                            // 水平相对于
                            else if (parent == null && autoShape.getType() != AbstractShape.SHAPE_GROUP 
                                     && "mso-position-horizontal-relative".equalsIgnoreCase(temps[0]))
                            {
                                // 相对于页边距
                                if ("margin".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_MARGIN);
                                }
                                // 相对于页面
                                else if ("page".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_PAGE);
                                }
                                // 相对于左边距
                                else if ("left-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_LEFT);
                                }
                                // 相对于右边距
                                else if ("right-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_RIGHT);
                                }
                                // 相对于内边距
                                else if ("inner-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_INNER);
                                }
                                // 相对于外边距
                                else if ("outer-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_OUTER);
                                }
                                // 相对于栏
                                else if ("text".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_COLUMN);
                                   
                                }
                                // 相对于字符
                                else if ("char".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setHorizontalRelativeTo(WPAutoShape.RELATIVE_CHARACTER);
                                    
                                }
                            }
                            // 垂直位置
                            else if (parent == null && autoShape.getType() != AbstractShape.SHAPE_GROUP 
                                     && "mso-position-vertical".equalsIgnoreCase(temps[0]))
                            {
                                ((WPAutoShape)autoShape).setVerticalAlignment(getAlign(temps[1]));
                            }
                            else if (parent == null && autoShape.getType() != AbstractShape.SHAPE_GROUP 
                                    && "mso-top-percent".equalsIgnoreCase(temps[0]))
                            {
                            	//vertical relative position
                            	((WPAutoShape)autoShape).setVerRelativeValue(Integer.parseInt(temps[1]));
                            	
                            	((WPAutoShape)autoShape).setVerPositionType(WPAbstractShape.POSITIONTYPE_RELATIVE);
                            }
                            // 垂直相对值
                            else if (parent == null && autoShape.getType() != AbstractShape.SHAPE_GROUP 
                                     && "mso-position-vertical-relative".equalsIgnoreCase(temps[0]))
                            {
                                // relative line
                                if ("line".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_LINE);
                                    
                                }
                                // relative paragraph 
                                else if ("text".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_PARAGRAPH);
                                }
                                // 相对于页边距
                                else if ("margin".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_MARGIN);
                                }
                                // 相对于页面
                                else if ("page".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_PAGE);
                                }
                                // 相对于左边距
                                else if ("top-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_TOP);
                                }
                                // 相对于右边距
                                else if ("bottom-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_BOTTOM);
                                }
                                // 相对于内边距
                                else if ("inner-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_INNER);
                                }
                                // 相对于外边距
                                else if ("outer-margin-area".equalsIgnoreCase(temps[1]))
                                {
                                    ((WPAutoShape)autoShape).setVerticalRelativeTo(WPAutoShape.RELATIVE_OUTER);
                                }
                            }
                        }
                    }
                    rect = new Rectangle();
                    rect.x = (int)(x * MainConstant.POINT_TO_PIXEL);
                    rect.y = (int)(y * MainConstant.POINT_TO_PIXEL);
                    rect.width = (int)(w * MainConstant.POINT_TO_PIXEL);
                    rect.height = (int)(h * MainConstant.POINT_TO_PIXEL);
                    if (autoShape.getType() != AbstractShape.SHAPE_GROUP && ((WPAutoShape)autoShape).getGroupShape() == null)
                    {
                        processGrpSpRect(parent, rect);
                    }
                    
                    if(autoShape instanceof WPAutoShape && ((WPAutoShape)autoShape).getShapeType() == ShapeTypes.ArbitraryPolygon)
                    {
                    	String coordsize = shape.attributeValue("coordsize");
                    	if(coordsize != null && coordsize.length() > 0)
                    	{
                    		String[] size = coordsize.split(",");                        		
                    		Matrix m = new Matrix();
                    		float width = Integer.parseInt(size[0]);
                            float height = Integer.parseInt(size[1]);
                            m.postScale(rect.width / width, rect.height / height);
                            
                            List<ExtendPath> paths = ((WPAutoShape)autoShape).getPaths();
                            for(ExtendPath path : paths)
                            {
                            	path.getPath().transform(m); 
                            }
                    	}
                    }
                    autoShape.setBounds(rect);
                }
            }
            
//            // wrap
//            String val = null;
//            Element wrap = shape.element("wrap");
//            if (parent == null && wrap != null && autoShape.getType() != AbstractShape.SHAPE_GROUP)
//            {
//                if (wrap.attribute("type") != null)
//                {
//                    val = wrap.attributeValue("type");
//                    if ("tight".equalsIgnoreCase(val))
//                    {
//                        ((WPAutoShape)autoShape).setWrap(WPAutoShape.WRAP_TIGHT);
//                    }
//                    else if ("square".equalsIgnoreCase(val))
//                    {
//                        ((WPAutoShape)autoShape).setWrap(WPAutoShape.WRAP_SQUARE);
//                    }
//                }
//            }
//            if ( parent == null && horizontalRelative && verticalRelative && autoShape.getType() != AbstractShape.SHAPE_GROUP)
//            {
//                ((WPAutoShape)autoShape).setWrap(WPAutoShape.WRAP_OLE);
//            }
        }
        return rect;
    }
    
    /**
     * 
     * @param type
     * @return
     */
    private byte getFillType(String type)
    {
    	if("gradient".equalsIgnoreCase(type))
    	{
    		return BackgroundAndFill.FILL_SHADE_LINEAR;
    	}
    	else if("gradientRadial".equalsIgnoreCase(type))
    	{
    		//TTOD gradient Radial and gradient Center
    		return BackgroundAndFill.FILL_SHADE_RADIAL;
    	}
    	else if("pattern".equalsIgnoreCase(type))
    	{
    		//TTOD gradient Radial and gradient Center
    		return BackgroundAndFill.FILL_PATTERN;
    	}
    	else if("tile".equalsIgnoreCase(type))
    	{
    		//TTOD gradient Radial and gradient Center
    		return BackgroundAndFill.FILL_SHADE_TILE;
    	}
    	else if("frame".equalsIgnoreCase(type))
    	{
    		//TTOD gradient Radial and gradient Center
    		return BackgroundAndFill.FILL_PICTURE;
    	}
    	
    	
    	return BackgroundAndFill.FILL_SOLID;
    }
    
    private int getRadialGradientPositionType(Element fill)
    {
    	int positionType = RadialGradientShader.Center_TL;
		String focusposition = fill.attributeValue("focusposition");
		if(focusposition != null && focusposition.length() > 0)
		{
			String[] xy =  focusposition.split(",");
			if(xy != null)
			{
				if(xy.length == 2)
				{
					if(".5".equalsIgnoreCase(xy[0]) && ".5".equalsIgnoreCase(xy[1]))
					{
						//radial from center
						positionType = RadialGradientShader.Center_Center;
					}
					else if("1".equalsIgnoreCase(xy[0]) && "1".equalsIgnoreCase(xy[1]))
					{
						positionType = RadialGradientShader.Center_BR;
					}
					else if("".equalsIgnoreCase(xy[0]) && "1".equalsIgnoreCase(xy[1]))
					{
						positionType = RadialGradientShader.Center_BL;
					}
					else if("1".equalsIgnoreCase(xy[0]) && "".equalsIgnoreCase(xy[1]))
					{
						positionType = RadialGradientShader.Center_TR;
					}
				}
				else if(xy.length == 1 && "1".equalsIgnoreCase(xy[0]))
				{
					positionType = RadialGradientShader.Center_TR;
				}				
			}
		}
		
		return positionType;
    }
    
    private int getAutoShapeType(Element shape)
    {
    	int shapeType = ShapeTypes.NotPrimitive;
    	
    	String name = shape.getName();
        if (name.equals("rect"))   
        {
            shapeType = ShapeTypes.Rectangle;
        }
        else if (name.equals("roundrect"))
        {
            shapeType = ShapeTypes.RoundRectangle;
        }
        else if (name.equals("oval"))
        {
            shapeType = ShapeTypes.Ellipse;
        }
        else if(name.equals("curve"))
        {
        	shapeType = ShapeTypes.Curve;
        }
        else if(name.equals("polyline"))
        {
        	shapeType = ShapeTypes.DirectPolygon;
        }
        else if(name.equals("line"))
        {
        	shapeType = ShapeTypes.WP_Line;
        }
        
        
        // type
        if (shape.attribute("type") != null)
        {
            String val = shape.attributeValue("type");
            if (val != null && val.length() > 9)
            {
                val = val.substring(9);
                shapeType = Integer.parseInt(val);
            }
        }
        else if(shape.attribute("path") != null)
        {
        	shapeType = ShapeTypes.ArbitraryPolygon;
        }
        
        return shapeType;
    }
    
    private float getValue(String value)
    {
    	int index = value.indexOf("pt");
    	float w = 0;
        if (index > 0)
        {
            w = Float.parseFloat(value.substring(0, index));
        }
        else
        {
            index = value.indexOf("in");
            if (index > 0)
            {
                w = Float.parseFloat(value.substring(0, index)) * MainConstant.POINT_DPI;
            }
            else
            {
                w = Float.parseFloat(value) * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH;
            }
        }
        
        return w * MainConstant.POINT_TO_PIXEL;
    }
    
    private PointF[] processPoints(String text)
    {
		List<PointF> ptList = new ArrayList<PointF>();
    	if(text != null)
    	{
    		String[] pts = text.split(",");
    		int cnt = pts.length;
    		int index = 0;
    		while(index < cnt - 1)
    		{
    			ptList.add(new PointF(getValue(pts[index]), getValue(pts[index + 1])));
    			index += 2;
    		}
    	}
    	
    	return ptList.toArray(new PointF[ptList.size()]);
    }
    
    private void processArrow(WPAutoShape autoShape, Element shape)
    {
    	//start and end arrow
        Element stroke = shape.element("stroke");
        if(stroke != null)
        {
            //start arrow
            byte type = getArrowType(stroke.attributeValue("startarrow"));
            if(type > 0)
            {
                autoShape.createStartArrow(type, 
                    getArrowWidth(stroke.attributeValue("startarrowwidth")),
                    getArrowLength(stroke.attributeValue("startarrowlength")));
            }
            
            type = getArrowType(stroke.attributeValue("endarrow"));
            if(type > 0)
            {
                autoShape.createEndArrow(type, 
                    getArrowWidth(stroke.attributeValue("endarrowwidth")),
                    getArrowLength(stroke.attributeValue("endarrowlength")));
            }
        }
    }
    
    private ExtendPath getArrowExtendPath(Path arrowPath, BackgroundAndFill fill, Line line, boolean border, byte arrowType)
    {
    	// extendPath
    	ExtendPath pathExtend = new ExtendPath();
    	pathExtend.setArrowFlag(true);
    	pathExtend.setPath(arrowPath);
        if (fill != null || border)
        {
            if (border)
            {
                if(arrowType != Arrow.Arrow_Arrow)
                {
                	if(line != null)
                	{
                		pathExtend.setBackgroundAndFill(line.getBackgroundAndFill());
                	}
                	else if(fill != null)
                	{
                		pathExtend.setBackgroundAndFill(fill);
                	}                           	
                }
                else
                {
                	pathExtend.setLine(line);
                }
            }
            else if (fill != null)
            {
            	pathExtend.setBackgroundAndFill(fill);
            }
        }
        
        return pathExtend;
    }
    
    /**
     * 
     */
    private WPAutoShape processAutoShape(Element shape, ParagraphElement paraElem, WPGroupShape parent, float zoomX, float zoomY, boolean hasTextbox)
    {
        if (shape != null)
        {
            String val;
            Float[] values = null;
            boolean border = true;          
            int shapeType = getAutoShapeType(shape);
            WPAutoShape autoShape = null;            
            
            // adjust values
            String[] adjustStr = null;
            if (shape.attribute("adj") != null)
            {
                val = shape.attributeValue("adj");
                if (val != null && val.length() > 0)
                {
                    adjustStr = val.split(",");
                }
            }
            if (adjustStr != null && adjustStr.length > 0)
            {
                values = new Float[adjustStr.length];
                for (int i = 0; i < adjustStr.length; i++)
                {
                    String temp = adjustStr[i];
                    if (temp != null && temp.length() > 0)
                    {
                        values[i] = Float.parseFloat(temp) / 21600;
                    }
                    else
                    {
                        values[i] = null;
                    }
                }
            }
            
            BackgroundAndFill fill = processBackgroundAndFill(shape);
            Line line = processLine(shape);
            
            if (shapeType == ShapeTypes.StraightConnector1
                || shapeType == ShapeTypes.BentConnector2 || shapeType == ShapeTypes.BentConnector3 
                || shapeType == ShapeTypes.CurvedConnector3)
            {
            	autoShape = new WPAutoShape();
                autoShape.setShapeType(shapeType);
                autoShape.setLine(line);
                processArrow(autoShape, shape);
                
                if(autoShape.getShapeType() == ShapeTypes.BentConnector2 && values == null)
                {
                    autoShape.setAdjustData(new Float[]{1.0f});
                }
                else
                {
                    autoShape.setAdjustData(values);
                }
            }
            else if(shapeType == ShapeTypes.ArbitraryPolygon)
            {
            	autoShape = new WPAutoShape();
            	autoShape.setShapeType(ShapeTypes.ArbitraryPolygon);
            	processArrow(autoShape, shape);
            	
                String pathContext = shape.attributeValue("path");
                
                float pathzoom = processPolygonZoom(shape, autoShape, parent, zoomX, zoomY);
                int lineWidth = Math.round((line != null ? line.getLineWidth() : 1) * pathzoom);
                PathWithArrow pathWithArrow = VMLPathParser.instance().createPath(autoShape, pathContext, lineWidth);
                
                if(pathWithArrow != null)
                {
                	Path[] paths = pathWithArrow.getPolygonPath();
                    if(paths != null)
                    {
                    	for (int i = 0; i < paths.length; i++)
                        {
                            ExtendPath pathExtend = new ExtendPath();
                            pathExtend.setPath(paths[i]);
                            if (line != null)
                            {
                            	pathExtend.setLine(line);
                            }
                            if (fill != null)
                            {
                                pathExtend.setBackgroundAndFill(fill);
                            }
                            autoShape.appendPath(pathExtend);
                        }    
                    }

                    if(pathWithArrow.getStartArrow() != null)
                    {
                        autoShape.appendPath(getArrowExtendPath(pathWithArrow.getStartArrow(), fill, line, border, autoShape.getStartArrow().getType()));
                    }
                    
                    if(pathWithArrow.getEndArrow() != null)
                    {
                    	autoShape.appendPath(getArrowExtendPath(pathWithArrow.getEndArrow(), fill, line, border, autoShape.getEndArrow().getType()));
                    }                    
                }                                       
            }
            else if(shapeType == ShapeTypes.WP_Line
            		|| shapeType == ShapeTypes.Curve
            		|| shapeType == ShapeTypes.DirectPolygon)
            {
            	autoShape = new WPAutoShape();
            	autoShape.setShapeType(shapeType);
            	processArrow(autoShape, shape);
            	Path path = new Path();
            	Path startArrowPath = null;
            	Path endArrowPath = null;
            	int lineWidth = (line != null ? line.getLineWidth() : 1);
            	if(shapeType == ShapeTypes.Line)
            	{
            		if(line != null)
            		{
            			fill = line.getBackgroundAndFill();
            		}
            		
            		//one line, not StraightConnector1
                	PointF from = processPoints(shape.attributeValue("from"))[0];
                	PointF to = processPoints(shape.attributeValue("to"))[0];                	
                	
                	PointF startArrowTailCenter = null;
                	PointF endArrowTailCenter = null;
                	if(autoShape.getStartArrowhead())
                	{
                		ArrowPathAndTail arrowPathAndTail = 
                				LineArrowPathBuilder.getDirectLineArrowPath(to.x, to.y, from.x, from.y, autoShape.getStartArrow(), lineWidth);
                		startArrowPath = arrowPathAndTail.getArrowPath();
                		startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                	}
                	
                	if(autoShape.getEndArrowhead())
                	{
                		ArrowPathAndTail arrowPathAndTail = 
                				LineArrowPathBuilder.getDirectLineArrowPath(from.x, from.y, to.x, to.y, autoShape.getEndArrow(), lineWidth);
                		endArrowPath = arrowPathAndTail.getArrowPath();
                		endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                	}
                	
                	if(startArrowTailCenter != null)
                	{
                		from = 
                				LineArrowPathBuilder.getReferencedPosition(from.x, from.y, startArrowTailCenter.x, startArrowTailCenter.y, autoShape.getStartArrow().getType());
                	}
                	
                	if(endArrowTailCenter != null)
                	{
                		to = 
                				LineArrowPathBuilder.getReferencedPosition(to.x, to.y, endArrowTailCenter.x, endArrowTailCenter.y, autoShape.getEndArrow().getType());
                	}
                	
                	path.moveTo(from.x, from.y);
                	path.lineTo(to.x, to.y);
            	}
            	else if(shapeType == ShapeTypes.Curve)
                {
                	//one beizer line
                	PointF from = processPoints(shape.attributeValue("from"))[0];
                	PointF ctr1 = processPoints(shape.attributeValue("control1"))[0];
                	PointF ctr2 = processPoints(shape.attributeValue("control2"))[0];
                	PointF to = processPoints(shape.attributeValue("to"))[0];
                	
                	PointF startArrowTailCenter = null;
                	PointF endArrowTailCenter = null;
                	if(autoShape.getStartArrowhead())
                	{
                		ArrowPathAndTail arrowPathAndTail = 
                				LineArrowPathBuilder.getCubicBezArrowPath(to.x, to.y, ctr2.x, ctr2.y, ctr1.x, ctr1.y, from.x, from.y, autoShape.getStartArrow(), lineWidth);
                		startArrowPath = arrowPathAndTail.getArrowPath();
                		startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                	}
                	
                	if(autoShape.getEndArrowhead())
                	{
                		ArrowPathAndTail arrowPathAndTail = 
                				LineArrowPathBuilder.getCubicBezArrowPath(from.x, from.y, ctr1.x, ctr1.y, ctr2.x, ctr2.y,  to.x, to.y, autoShape.getEndArrow(), lineWidth);
                		endArrowPath = arrowPathAndTail.getArrowPath();
                		endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                	}
                	
                	if(startArrowTailCenter != null)
                	{
                		from = 
                				LineArrowPathBuilder.getReferencedPosition(from.x, from.y, startArrowTailCenter.x, startArrowTailCenter.y, autoShape.getStartArrow().getType());
                	}
                	
                	if(endArrowTailCenter != null)
                	{
                		to = 
                				LineArrowPathBuilder.getReferencedPosition(to.x, to.y, endArrowTailCenter.x, endArrowTailCenter.y, autoShape.getEndArrow().getType());
                	}
                	
                	path.moveTo(from.x, from.y);
                	path.cubicTo(ctr1.x, ctr1.y, ctr2.x, ctr2.y, to.x, to.y);                	
                }
            	else if(shapeType == ShapeTypes.DirectPolygon)
                {
                	//direct polygon
                	PointF[] pts = processPoints(shape.attributeValue("points"));
                	int ptCnt = pts.length;
                	
                	PointF startArrowTailCenter = null;
                	PointF endArrowTailCenter = null;
                	if(autoShape.getStartArrowhead())
                	{
                		ArrowPathAndTail arrowPathAndTail = 
                				LineArrowPathBuilder.getDirectLineArrowPath(pts[1].x, pts[1].y, pts[0].x, pts[0].y, autoShape.getStartArrow(), lineWidth);
                		startArrowPath = arrowPathAndTail.getArrowPath();
                		startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                	}
                	
                	if(autoShape.getEndArrowhead())
                	{
                		ArrowPathAndTail arrowPathAndTail = 
                				LineArrowPathBuilder.getDirectLineArrowPath(pts[ptCnt - 2].x, pts[ptCnt - 2].y, pts[ptCnt - 1].x, pts[ptCnt - 1].y, autoShape.getEndArrow(), lineWidth);
                		endArrowPath = arrowPathAndTail.getArrowPath();
                		endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                	}
                	
                	if(startArrowTailCenter != null)
                	{
                		pts[0] = 
                				LineArrowPathBuilder.getReferencedPosition(pts[0].x, pts[0].y, startArrowTailCenter.x, startArrowTailCenter.y, autoShape.getStartArrow().getType());
                	}
                	
                	if(endArrowTailCenter != null)
                	{
                		pts[ptCnt - 1] = 
                				LineArrowPathBuilder.getReferencedPosition(pts[ptCnt - 1].x, pts[ptCnt - 1].y, endArrowTailCenter.x, endArrowTailCenter.y, autoShape.getEndArrow().getType());
                	}
                	
                	path.moveTo(pts[0].x, pts[0].y);
                	int index = 1;
                	while(index < pts.length)
                	{
                		path.lineTo(pts[index].x, pts[index].y);
                		index++;
                	}
                }
            	
            	ExtendPath pathExtend = new ExtendPath();
                pathExtend.setPath(path);
                if (line != null)
                {
                	pathExtend.setLine(line);
                }
                if (fill != null)
                {
                    pathExtend.setBackgroundAndFill(fill);
                }
                autoShape.appendPath(pathExtend);
                
                if(startArrowPath != null)
                {
                    autoShape.appendPath(getArrowExtendPath(startArrowPath, fill, line, border, autoShape.getStartArrow().getType()));
                }
                
                if(endArrowPath != null)
                {
                	autoShape.appendPath(getArrowExtendPath(endArrowPath, fill, line, border, autoShape.getEndArrow().getType()));
                }
            }
            else if (hasTextbox || fill != null || border)
            {
                String s = shape.attributeValue("id");
                if (s != null && s.indexOf("WaterMarkObject") > 0)
                {
                    isProcessWatermark = true;
                }
                if (isProcessWatermark)
                {
                    autoShape = new WatermarkShape();
                }
                else
                {
                    autoShape = new WPAutoShape();
                }
                autoShape.setShapeType(shapeType);
                processArrow(autoShape, shape);
                
                if (fill != null)
                {
                    autoShape.setBackgroundAndFill(fill);
                }
                
                if (line != null)
                {
                	autoShape.setLine(line);
                }
                autoShape.setAdjustData(values);
            }
            
            if (autoShape != null)
            {
                autoShape.setAuotShape07(true);
                if(parent == null)
                {
                    autoShape.setWrap(getShapeWrapType(shape));
                }
                else
                {
                    autoShape.setWrap(parent.getWrapType());
                }
                processAutoShapeStyle(shape, autoShape, parent, zoomX, zoomY);
                processRotation(autoShape);
                
                if (isProcessWatermark)
                {
                	processWatermark( ((WatermarkShape)autoShape), shape);
                    isProcessWatermark = false;
                }

                if (parent == null)
                {
                    addShape(autoShape, paraElem);
                }
                else
                {
                    parent.appendShapes(autoShape);
                }
            }
            
            return autoShape;
        }
        
        return null;
    }

    private void processWatermark(WatermarkShape waterMark, Element shape)
    {	
    	Element textpath = shape.element("textpath");
        if (textpath != null)
        {
        	/**
        	 * text watermark
        	 */
        	
        	waterMark.setWatermarkType(WatermarkShape.Watermark_Text);
        	
        	//font color
        	String fillColor = shape.attributeValue("fillcolor");
            if (fillColor != null && fillColor.length() > 0)
            {
            	waterMark.setFontColor(getColor(fillColor, false));
            }
        	
        	//opacity
        	Element fill = shape.element("fill");
        	if(fill != null)
        	{
        		waterMark.setOpacity(Float.parseFloat(fill.attributeValue("opacity")));
        	}
        	
        	//water mark context
        	waterMark.setWatermartString(textpath.attributeValue("string"));        	
        	
        	//water mark font size
        	String style = textpath.attributeValue("style");
        	String[] styles = style.split(";");
        	for(String item : styles)
        	{
        		String[] items = item.split(":");
        		if("font-size".equalsIgnoreCase(items[0]))
        		{
        			int fontSize = Integer.parseInt(items[1].replace("pt", ""));
        			if(fontSize == 1)
        			{
        				//auto font size
        				waterMark.setAutoFontSize(true);
        			}
        			else
        			{
        				waterMark.setFontSize(fontSize);
        			}
        		}
        	}
        }       
    }
    
    private Line processLine(Element shape)
    {
        String val = shape.attributeValue("stroked");
        // border
        if ("f".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val))
        {
        	return null;
        }        
        
        String type = shape.attributeValue("type");
        List<Element> shapeTypes = shape.getParent().elements("shapetype");
        Element shapetype = null;
        if(type != null && shapeTypes != null)
        {
        	for(Element shapeType : shapeTypes)
            {
            	if(type.equals("#" + shapeType.attributeValue("id")))
            	{
            		shapetype = shapeType;
            		break;
            	}
            }
        }
        
        if(shapetype != null)
        {
        	val = shapetype.attributeValue("stroked");
        	if ("f".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val))
            {
            	return null;
            }
        }
        
        Line line = null;
        int lineColor = 0xFF000000;
        val = shape.attributeValue("strokecolor");
        if (val != null && val.length() > 0)
        {
        	lineColor = getColor(val, false);
        }
        
        BackgroundAndFill lineFill = new BackgroundAndFill();
        lineFill.setForegroundColor(lineColor);
        
        int lineWidth = 1;
        val = shape.attributeValue("strokeweight");
        if(shape.attributeValue("strokeweight") != null)
        {
            if (val.indexOf("pt") >= 0)
            {
                val = val.replace("pt", "");
            }
            else if (val.indexOf("mm") >= 0)
            {
                val = val.replace("mm", "");
            }
            else if (val.indexOf("cm") >= 0)
            {
                val = val.replace("cm", "");
            }
            lineWidth = Math.round(Float.parseFloat(val) * MainConstant.POINT_TO_PIXEL);
        }            

        boolean dash = false;
        Element stroke = null;
        if(shape.element("stroke") != null)
        {
            stroke = shape.element("stroke");
            dash = stroke.attributeValue("dashstyle") != null;
        }
        
        line = new Line();
        line.setBackgroundAndFill(lineFill);
        line.setLineWidth(lineWidth);
        line.setDash(dash);
        
        return line;
    }
    
	private BackgroundAndFill processBackgroundAndFill(Element shape) 
	{
		BackgroundAndFill fill = null;
        boolean filled = true;
		String val;
		// background
		if (shape.attribute("filled") != null)
		{
		    val = shape.attributeValue("filled");
		    if (val != null && val.length() > 0)
		    {
		        if (val.equals("f") || val.equals("false"))
		        {
		            filled = false;
		        }
		    }
		}
		if (filled)
		{
		    Element fillElem = shape.element("fill");
		    if (fillElem != null && fillElem.attribute("id") != null)
		    {
		    	//frame, pattern and tile
		        val = fillElem.attributeValue("id");
		        if (val != null && val.length() > 0)
		        {
		            PackagePart picPart = null;
		            if (isProcessHF && hfPart != null)
		            {
		                picPart = zipPackage.getPart(hfPart.getRelationship(val).getTargetURI());
		            }
		            else
		            {
		                picPart = zipPackage.getPart(packagePart.getRelationship(val).getTargetURI());
		            }
		            if (picPart != null)
		            {  
		            	fill = new BackgroundAndFill();
		            	
		            	byte type = getFillType(fillElem.attributeValue("type"));
		            	try
		            	{
		            		if(type == BackgroundAndFill.FILL_SHADE_TILE)
		                	{
		                		fill.setFillType(BackgroundAndFill.FILL_SHADE_TILE);
		                		int index = control.getSysKit().getPictureManage().addPicture(picPart);
		                		fill.setShader(
		                				new TileShader(control.getSysKit().getPictureManage().getPicture(index), 
		                						TileShader.Flip_None, 1f, 1.0f));
		                		
		                	}
		            		else if(type == BackgroundAndFill.FILL_PATTERN)
		            		{
		            			val = shape.attributeValue("fillcolor");
		            			int foregroundColor = 0xFFFFFFFF;
		                        if (val != null && val.length() > 0)
		                        {
		                        	foregroundColor = getColor(val, false);
		                        }
		                        int backgroundColor = 0xFFFFFFFF;
		                        val = fillElem.attributeValue("color2");
		                        if(val != null)
		                        {
		                        	backgroundColor = getColor(val, true);
		                        }
		                        
		                        fill.setFillType(BackgroundAndFill.FILL_PATTERN);
		                		int index = control.getSysKit().getPictureManage().addPicture(picPart);
		                		fill.setShader(
		                				new PatternShader(control.getSysKit().getPictureManage().getPicture(index), 
		                						backgroundColor, foregroundColor));
		            		}
		                	else
		                	{
		                		fill.setFillType(BackgroundAndFill.FILL_PICTURE);
		                        fill.setPictureIndex(control.getSysKit().getPictureManage().addPicture(picPart));
		                	}
		            	}
		            	catch (Exception e)
		                {
		                    control.getSysKit().getErrorKit().writerLog(e);
		                }                        	
		            } 
		        }
		    }
		    if (fill == null)
		    {
		        fill = new BackgroundAndFill();
		        
		        byte type = BackgroundAndFill.FILL_SOLID;
		        if(fillElem != null)
		        {
		        	type = getFillType(fillElem.attributeValue("type"));
		        }
		        
		        if(fillElem == null || type == BackgroundAndFill.FILL_SOLID)
		        {
		        	int fillColor = Color.WHITE;
		        	fill.setFillType(BackgroundAndFill.FILL_SOLID);

		        	val = shape.attributeValue("fillcolor");
		        	if (val != null && val.length() > 0)
		        	{
		        		fillColor = getColor(val, true);
		        	}
		        	
		        	if(fillElem !=  null)
		        	{
		        		String opacityStr = fillElem.attributeValue("opacity");
			        	 if(opacityStr != null)
			        	 {
			        		 float opacity = Float.parseFloat(opacityStr);
			        		 if(opacity > 1)
			        		 {
			        			 opacity /= 65536f;
			        		 }
			        		 opacity *= 255;
			        		 fillColor = ((byte)opacity << 24) | (fillColor & 0xFFFFFF);
			        	 }
		        	}
		        	
		        	fill.setForegroundColor(fillColor);
		        }
		        else
		        {
		        	Gradient gradient = readGradient(shape, fillElem, type);
		        	
		        	fill.setFillType(type);
		            fill.setShader(gradient);
		        }                   
		    }
		}
		return fill;
	}

	private Gradient readGradient(Element shape, Element fillElem, byte type)
	{
		int focus = 0;
		String sFocus = fillElem.attributeValue("focus");
		if(sFocus != null)
		{
			focus = Integer.parseInt(sFocus.replace("%", ""));
		}
		
		int angle = 0;
		String sAngle = fillElem.attributeValue("angle");
		if(sAngle !=  null)
		{
			angle = Integer.parseInt(sAngle);
		}
		switch(angle)
		{
			case -90:
			case 0:
				angle += 90;
				break;
			case -45:
				angle = 135;
				break;
			case -135:
				angle = 45;
				break;
		}
		
		int[] colors = null;
		float[] positions = null;
		String intermediateColors = fillElem.attributeValue("colors");
		if(intermediateColors != null)
		{
			String[] positionColor = intermediateColors.split(";");
			int length = positionColor.length;
			colors = new int[length];
			positions = new float[length];
			int index = 0;
			String pos = null;
			String color = null;
			for(int i = 0; i < length; i++)
			{
				index = positionColor[i].indexOf(" ");
				pos = positionColor[i].substring(0, index);
				if(pos.contains("f"))
				{
					positions[i] = Float.parseFloat(pos) / 100000f;
				}
				else
				{
					positions[i] = Float.parseFloat(pos);
				}
				colors[i] = getColor(positionColor[i].substring(index + 1, positionColor[i].length()), true);
			}
		}
		else
		{
			
			int color = getColor(shape.attributeValue("fillcolor"), true);
			int color2 = 0;
			String sColor2 = fillElem.attributeValue("color2");
			if(sColor2 != null)
			{
				sColor2 = sColor2.replace("fill ", "");
				int index1 = sColor2.indexOf("(");
				int index2 = sColor2.indexOf(")");
				if(index1 >= 0 && index2 >= 0)
				{
					String sAlpha = sColor2.substring(index1 + 1, index2);
				}
				color2 = getColor(sColor2, true);
			}
			
			colors = new int[]{color, color2};
			
			positions = new float[]{0f, 1f};
		}
		
		Gradient gradient = null;
		if(type == BackgroundAndFill.FILL_SHADE_LINEAR)
		{
			gradient = 
					new LinearGradientShader(angle, colors, positions);
		}
		else if(type == BackgroundAndFill.FILL_SHADE_RADIAL)
		{
			int posType = getRadialGradientPositionType(fillElem);
			gradient = 
					new RadialGradientShader(posType, colors, positions);
			
		}
		
		if(gradient != null)
		{
			gradient.setFocus(focus);
		}
		return gradient;
	}
    
    private AbstractShape processAutoShape2010(ParagraphElement paraElem, Element sp, WPGroupShape parent, 
        float zoomX, float zoomY, int x, int y, boolean bResetRect)
    {
        return processAutoShape2010(packagePart, paraElem, sp, parent,
            zoomX, zoomY, x, y, bResetRect);
    }
    
    private AbstractShape processAutoShape2010(PackagePart packagePart, ParagraphElement paraElem, Element sp, WPGroupShape parent, 
                        float zoomX, float zoomY, int x, int y, boolean bResetRect)
    {
        Rectangle rect = null;
        AbstractShape shape = null;
        if (sp != null)
        {
            Element temp = null;
            String name = sp.getName();
            
            // rect
            if (name.equals("wsp") || name.equals("sp") || name.equals("pic"))
            {
                temp = sp.element("spPr");
            }
            else if (name.equals("wgp") || name.equals("grpSp"))
            {
                temp = sp.element("grpSpPr");
            }
            if (temp != null)
            {
                rect = ReaderKit.instance().getShapeAnchor(temp.element("xfrm"), zoomX, zoomY);
                if (rect != null)
                {
                    if (bResetRect)
                    {
                        rect.x += x;
                        rect.y += y;
                    }
                    
                    rect = processGrpSpRect(parent, rect);
                }
            }
            
            // shape
            if (rect != null && (name.equals("wgp") || name.equals("grpSp")))
            {
                float[] zoomXY = null;
                Rectangle childRect = null;
                Element grpSpPr = sp.element("grpSpPr");
                if (grpSpPr != null)
                {
                    zoomXY = ReaderKit.instance().getAnchorFitZoom(grpSpPr.element("xfrm"));
                    childRect = ReaderKit.instance().getChildShapeAnchor(grpSpPr.element("xfrm"), zoomXY[0]* zoomX, zoomXY[1]* zoomY);
                    if (bResetRect)
                    {
                        childRect.x += x;
                        childRect.y += y;
                    }
                    
                    WPGroupShape grpShape = new WPGroupShape();
                    grpShape.setOffPostion(rect.x - childRect.x, rect.y - childRect.y);
                    grpShape.setBounds(rect);
                    ReaderKit.instance().processRotation(grpSpPr, grpShape);
                    
                    for (Iterator< ? > it = sp.elementIterator(); it.hasNext();)
                    {
                        processAutoShape2010(packagePart, paraElem, (Element)it.next(), grpShape, zoomXY[0]* zoomX, zoomXY[1]* zoomY, 0, 0, false);
                    }
                    
                    if (parent == null)
                    {
                        shape = new WPAutoShape();
                        ((WPAutoShape)shape).addGroupShape(grpShape);
                    }
                    else
                    {
                        shape = grpShape;
                    }
                }
            }
            else if (rect != null)
            {
                if (name.equals("wsp") || name.equals("sp"))
                {
                    try
                    {
                        if (isProcessHF && hfPart != null)
                        {
                        	shape = AutoShapeDataKit.getAutoShape(control, zipPackage, hfPart, sp, 
                                    rect, themeColor, MainConstant.APPLICATION_TYPE_WP, hasTextbox2010(sp));
                    		if(shape != null)
                            {
                            	processTextbox2010(packagePart, (WPAutoShape)shape, sp);
                            }
                        }
                        else
                        {
                        	shape = AutoShapeDataKit.getAutoShape(control, zipPackage, packagePart, sp, 
                                    rect, themeColor, MainConstant.APPLICATION_TYPE_WP, hasTextbox2010(sp));
                    		if(shape != null)
                            {
                            	processTextbox2010(packagePart, (WPAutoShape)shape, sp);
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (name.equals("pic"))
                {
                    shape = addPicture(sp, rect);
                }
            }
            if (shape != null)
            {
                if (parent == null)
                {
                    addShape(shape, paraElem);
                }
                else
                {
                    shape.setParent(parent);
                    if(shape instanceof WPAutoShape)
                    {
                        ((WPAutoShape)shape).setWrap(parent.getWrapType());
                    }
                    parent.appendShapes(shape);
                }
            }
        }
        return shape;
    }
    
    private void processAlternateContent(Element alternateContent, ParagraphElement paraElem)
    {
        if (alternateContent != null)
        {
            Element choice = alternateContent.element("Choice");
            if (choice != null)
            {
                Element drawing = choice.element("drawing");
                if (drawing != null)
                {
                    Element anchor = drawing.element("anchor");
                    short wrapType = - 1;
                    if(anchor == null)
                    {
                    	//inline
                    	anchor = drawing.element("inline"); 
                    	wrapType = WPAbstractShape.WRAP_OLE;
                    }
                    
                    if (anchor != null)
                    {   
                        //如果是稿纸生成autoshape就不需要处理
                        Element docPr = anchor.element("docPr");
                        if (docPr != null)
                        {
                            String val = docPr.attributeValue("name");
                            if (val != null && (val.startsWith("Genko")
                                || val.startsWith("Header")
                                || val.startsWith("Footer")))
                            {
                                return;
                            }
                        }
                        Element graphic = anchor.element("graphic");
                        if (graphic != null)
                        {
                            Element graphicData = graphic.element("graphicData");
                            if (graphicData != null)
                            {
                                for (Iterator< ? > it = graphicData.elementIterator(); it.hasNext();)
                                {
                                    AbstractShape shape = processAutoShape2010(paraElem, (Element)it.next(), null, 1.0f, 1.0f, 0, 0, true);
                                    
                                    if(shape != null)
                                    {
                                    	if(shape instanceof WPAutoShape && ((WPAutoShape)shape).getGroupShape() != null)
                                        {
                                            WPGroupShape grp = ((WPAutoShape)shape).getGroupShape();
                                            if(wrapType == -1)
                                            {
                                            	wrapType = getDrawingWrapType(anchor);
                                            }
                                            
                                            grp.setWrapType(wrapType);
                                            setShapeWrapType(grp, wrapType);
                                        }
                                        
                                        processWrapAndPosition_Drawing((WPAutoShape)shape, anchor, shape.getBounds());
                                    }                                    
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setShapeWrapType(WPGroupShape groupShape, short wrapType)
    {
    	for(IShape item : groupShape.getShapes())
        {
        	if(item instanceof WPAbstractShape)
        	{
        		((WPAbstractShape)item).setWrap(wrapType);
        	}
        	else if(item instanceof WPGroupShape)
        	{
        		setShapeWrapType((WPGroupShape)item, wrapType);
        	}                                                
        }
    }
    
    /**
     * 
     * @param rPr
     * @param attr
     */
    private void processRunAttribute(Element rPr, IAttributeSet attr)
    {
        String val;       
        // 字号
        Element szCs = rPr.element("szCs");
        Element sz = rPr.element("sz");
        if (szCs != null || sz != null)
        {
        	float szSize = 12;
            if(szCs != null)
    		{
            	val = szCs.attributeValue("val");
            	szSize = Math.max(szSize, Float.parseFloat(val) / 2.f);
    		}		
            if(sz != null)
            {
            	val = sz.attributeValue("val");
            	szSize = Math.max(szSize, Float.parseFloat(val) / 2.f);
            }
            
            AttrManage.instance().setFontSize(attr, (int)szSize);
        }
        // 字体
        Element temp = rPr.element("rFonts");
        if (temp != null)
        {
            val = temp.attributeValue("hAnsi");
            if (val == null)
            {
                val = temp.attributeValue("eastAsia");
            }
            if (val != null)
            {
                int index = FontTypefaceManage.instance().addFontName(val);
                if (index >= 0)
                {
                    AttrManage.instance().setFontName(attr, index);
                }
            }
        }
            
        // 字符颜色
        temp = rPr.element("color");
        if (temp != null)
        {
            val = temp.attributeValue("val");
            if ("auto".equals(val) || "FFFFFF".equals(val))
            {
                AttrManage.instance().setFontColor(attr, Color.BLACK);
            }
            else
            {
                AttrManage.instance().setFontColor(attr, 
                    Color.parseColor("#" + val));
            }
        }
        // 粗体
        temp = rPr.element("b");
        if (temp != null)
        {
            AttrManage.instance().setFontBold(attr, true);
        }
        // 斜体
        temp = rPr.element("i");
        if (temp != null)
        {
            AttrManage.instance().setFontItalic(attr, true);
        }
        // 下划线
        Element u = rPr.element("u");
        if (u != null)
        {
        	String underlineType = u.attributeValue("val");
        	if(underlineType != null)
        	{
        		AttrManage.instance().setFontUnderline(attr, 1);
                val = u.attributeValue("color");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setFontUnderlineColr(attr, 
                        Color.parseColor("#" + val));
                }
        	}
        }
        // 删除线
        temp = rPr.element("strike");
        if (temp != null)
        {
        	AttrManage.instance().setFontStrike(attr, !"0".equals(temp.attributeValue("val")));
        }
        // 双删除线
        temp = rPr.element("dstrike");
        if (temp != null)
        {
            AttrManage.instance().setFontDoubleStrike(attr, !"0".equals(temp.attributeValue("val")));
        }
        // 上下标
        Element s = rPr.element("vertAlign");
        if (s != null)
        {
            val = s.attributeValue("val");
            AttrManage.instance().setFontScript(attr, val.equals("superscript") ? 1 : 2);
        }
        //
        // 样式
        temp = rPr.element("rStyle");
        if (temp != null)
        {
            val = temp.attributeValue("val");
            if (val != null && val.length() > 0)
            {
                AttrManage.instance().setParaStyleID(attr, styleStrID.get(val));
            }
        }
        // highlight
        temp = rPr.element("highlight");
        if (temp != null)
        {
            AttrManage.instance().setFontHighLight(attr, FCKit.convertColor(temp.attributeValue("val")));
        }
        
    }
    
    private int processValue(String val, boolean isLeftRight)
    {
    	int a = isLeftRight ? ShapeKit.DefaultMargin_Twip * 2 : ShapeKit.DefaultMargin_Twip;
    	
        if (val != null)
        {
            // 十六进制
            if(ReaderKit.instance().isDecimal(val))
            {
                a = (int)(Integer.parseInt(val) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH * MainConstant.PIXEL_TO_TWIPS);
            }
            else
            {
                a = (int)(Integer.parseInt(val, 16) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH * MainConstant.PIXEL_TO_TWIPS);
            }
        }
        return a;
    }
    
    private void processParagraphs(List<Element> elems)
    {
    	for (Element elem : elems)
        {
        	if ("sdt".equals(elem.getName()))
            {                      
        		elem = elem.element("sdtContent");
                if (elem != null)
                {
                	processParagraphs(elem.elements());
                }
            }
        	
            if ("p".equals(elem.getName()))
            {
                processParagraph(elem, 0);
            }
            else if ("tbl".equals(elem.getName()))
            {
                processTable(elem);
            }
        }
    }
    
    private boolean hasTextbox2007(Element sp)
    {
        Element txbx = sp.element("textbox");        
        if (txbx != null)
        {
            Element txbxContent = txbx.element("txbxContent");
            if (txbxContent != null)
            {
            	return true;
            }
        }
        else if(sp.element("textpath") != null)
        {
        	String context = sp.element("textpath").attributeValue("string");
        	
        	return context != null && context.length() > 0;
        }
        return false;
    }
    
    private boolean processTextbox2007(PackagePart packagePart,  WPAutoShape wpShape, Element sp)
    {
        Element txbx = sp.element("textbox");        
        if (txbx != null)
        {
            Element txbxContent = txbx.element("txbxContent");
            if (txbxContent != null)
            {
                long oldOffset = offset;
                offset = WPModelConstant.TEXTBOX + (textboxIndex << 32);
                wpShape.setElementIndex((int)textboxIndex);
                SectionElement textboxElement = new SectionElement();
                textboxElement.setStartOffset(offset);
                wpdoc.appendElement(textboxElement, offset);
                
                processParagraphs(txbxContent.elements());
                
                // section属性
                
                IAttributeSet attr = textboxElement.getAttribute();
                // width
                // 宽度
                AttrManage.instance().setPageWidth(attr, (int)(wpShape.getBounds().width * MainConstant.PIXEL_TO_TWIPS));
                // 高度
                AttrManage.instance().setPageHeight(attr, (int)(wpShape.getBounds().height * MainConstant.PIXEL_TO_TWIPS));
                
                int leftMargin = ShapeKit.DefaultMargin_Twip * 2;
            	int topMargin = ShapeKit.DefaultMargin_Twip;
            	int rightMargin = ShapeKit.DefaultMargin_Twip * 2;
            	int bottomMargin = ShapeKit.DefaultMargin_Twip;
            	
                String inset = txbx.attributeValue("inset");
                if(inset != null)
                {
                	String[] insets = inset.split(",");
                	if(insets.length > 0 && insets[0].length() > 0)
            		{
            			// 左边距
            			leftMargin = Math.round(getValueInPt(insets[0], 1.0f) * MainConstant.POINT_TO_TWIPS);
            		}
            		
            		if(insets.length > 1 && insets[1].length() > 0)
            		{
            			// 上边距
            			topMargin = Math.round(getValueInPt(insets[1], 1.0f) * MainConstant.POINT_TO_TWIPS);
            		}
            		
            		if(insets.length > 2 && insets[2].length() > 0)
            		{
            			// 右边距
            			rightMargin = Math.round(getValueInPt(insets[2], 1.0f) * MainConstant.POINT_TO_TWIPS);
            		}
            		
            		if(insets.length > 3 && insets[3].length() > 0)
            		{
            			// 下边距
            			bottomMargin = Math.round(getValueInPt(insets[3], 1.0f) * MainConstant.POINT_TO_TWIPS);
            		}
                }

                // 上边距
                AttrManage.instance().setPageMarginTop(attr, topMargin);
                // 下边距
                AttrManage.instance().setPageMarginBottom(attr, bottomMargin);
                // 左边距
                AttrManage.instance().setPageMarginLeft(attr, leftMargin);
                // 右边距
                AttrManage.instance().setPageMarginRight(attr, rightMargin);
                
                String styleStr = sp.attributeValue("style");
                if (styleStr != null)
                {
                	String[] styles = styleStr.split(";");
                    for (int i = 0; i < styles.length; i++)
                    {
                        String[] temps = styles[i].split(":");
                        if (temps != null && temps[0] != null && temps[1] != null)
                        {
                        	if ("text-align".equalsIgnoreCase(temps[0]))
                            {
                        		//alignment in horizontal
                            }
                        	else if ("v-text-anchor".equalsIgnoreCase(temps[0]))
                            {
                        		//alignment in vertical 
                        		if ("middle".equals(temps[1]))
                                {
                                    AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_CENTER);
                                }
                                else if("bottom".equals(temps[1]))
                                {
                                    AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_BOTTOM);
                                }
                                else if ("top".equals(temps[1]))
                                {
                                    AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_TOP);
                                }
                            }
                        	else if("mso-wrap-style".equalsIgnoreCase(temps[0]))
                        	{
                        		wpShape.setTextWrapLine(!"none".equalsIgnoreCase(temps[1]));
                        	}
                        }
                    }
                }
                
                wpShape.setElementIndex((int)textboxIndex);                
                textboxElement.setEndOffset(offset);
                
                textboxIndex++;
                offset = oldOffset;
                return true;
            }
        }
        else if(sp.element("textpath") != null)
        {
        	//word art
        	String context = sp.element("textpath").attributeValue("string");
        	
        	wpShape.setBackgroundAndFill(null);
        	
        	long oldOffset = offset;
            offset = WPModelConstant.TEXTBOX + (textboxIndex << 32);
            wpShape.setElementIndex((int)textboxIndex);
            SectionElement textboxElement = new SectionElement();
            textboxElement.setStartOffset(offset);
            wpdoc.appendElement(textboxElement, offset);
            
            ParagraphElement paraElem = new ParagraphElement();
            long t = offset;
            paraElem.setStartOffset(offset);
            
            LeafElement leaf = null;
            int len = context.length();
            if (len > 0)
            {
//            	context =context.concat("\n");
//            	len++;
            	
                leaf = new LeafElement(context);
                // 属性
                String val = sp.attributeValue("fillcolor");
	        	if (val != null && val.length() > 0)
	        	{
	        		AttrManage.instance().setFontColor(leaf.getAttribute(), getColor(val, true));
	        	}
                
	        	float width = (float)wpShape.getBounds().getWidth() - ShapeKit.DefaultMargin_Twip * 4 * MainConstant.TWIPS_TO_PIXEL;
	        	float height = (float)wpShape.getBounds().getHeight() - ShapeKit.DefaultMargin_Twip * 2 * MainConstant.TWIPS_TO_PIXEL;
	        	
	        	int fontsize = 12;
	        	Paint paint = PaintKit.instance().getPaint();
	        	paint.setTextSize(fontsize);
	        	FontMetrics fm = paint.getFontMetrics();
	        	while((int)paint.measureText(context) < width && (int)(Math.ceil(fm.descent - fm.ascent)) < height)
	        	{	        		
	        		paint.setTextSize(++fontsize);
	        		fm = paint.getFontMetrics();
	        	}
	        	
	        	AttrManage.instance().setFontSize(leaf.getAttribute(), (int)((fontsize - 1) * MainConstant.PIXEL_TO_POINT));
	        	
                leaf.setStartOffset(offset);
                offset += len;
                leaf.setEndOffset(offset);
                paraElem.appendLeaf(leaf);                
            }

            paraElem.setEndOffset(offset);
            if (offset > t)
            {
                //
                wpdoc.appendParagraph(paraElem, offset);
            }
            
            IAttributeSet attr = textboxElement.getAttribute();
            // width
            // 宽度
            AttrManage.instance().setPageWidth(attr, (int)(wpShape.getBounds().width * MainConstant.PIXEL_TO_TWIPS));
            // 高度
            AttrManage.instance().setPageHeight(attr, (int)(wpShape.getBounds().height * MainConstant.PIXEL_TO_TWIPS));
            
        	// 上边距
            AttrManage.instance().setPageMarginTop(attr, ShapeKit.DefaultMargin_Twip);
            // 下边距
            AttrManage.instance().setPageMarginBottom(attr, ShapeKit.DefaultMargin_Twip);
            // 左边距
            AttrManage.instance().setPageMarginLeft(attr, ShapeKit.DefaultMargin_Twip * 2);
            // 右边距
            AttrManage.instance().setPageMarginRight(attr, ShapeKit.DefaultMargin_Twip * 2);
            
            String styleStr = sp.attributeValue("style");
            if (styleStr != null)
            {
            	String[] styles = styleStr.split(";");
                for (int i = 0; i < styles.length; i++)
                {
                    String[] temps = styles[i].split(":");
                    if (temps != null && temps[0] != null && temps[1] != null)
                    {
                    	if ("text-align".equalsIgnoreCase(temps[0]))
                        {
                    		//alignment in horizontal
                        }
                    	else if ("v-text-anchor".equalsIgnoreCase(temps[0]))
                        {
                    		//alignment in vertical 
                    		if ("middle".equals(temps[1]))
                            {
                                AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_CENTER);
                            }
                            else if("bottom".equals(temps[1]))
                            {
                                AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_BOTTOM);
                            }
                            else if ("top".equals(temps[1]))
                            {
                                AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_TOP);
                            }
                        }
                    	else if("mso-wrap-style".equalsIgnoreCase(temps[0]))
                    	{
                    		wpShape.setTextWrapLine(!"none".equalsIgnoreCase(temps[1]));
                    	}
                    }
                }
            }
            wpShape.setElementIndex((int)textboxIndex);                
            textboxElement.setEndOffset(offset);
            
            textboxIndex++;
            offset = oldOffset;
            return true;    
        }
        
        return false;
    }
    
    private boolean hasTextbox2010(Element sp)
    {
    	Element txbx = sp.element("txbx");
        
        if (txbx != null)
        {
            Element txbxContent = txbx.element("txbxContent");
            if (txbxContent != null)
            {
            	return true;
            }
        }
        return false;
    }
    
    /**
     * 
     */
    private boolean processTextbox2010(PackagePart packagePart,  WPAutoShape wpShape, Element sp)
    {
        Element txbx = sp.element("txbx");
        
        if (txbx != null)
        {
            Element txbxContent = txbx.element("txbxContent");
            if (txbxContent != null)
            {
                long oldOffset = offset;
                offset = WPModelConstant.TEXTBOX + (textboxIndex << 32);
                wpShape.setElementIndex((int)textboxIndex);
                SectionElement textboxElement = new SectionElement();
                textboxElement.setStartOffset(offset);
                wpdoc.appendElement(textboxElement, offset);
                List<Element> elems = txbxContent.elements();
                
                processParagraphs(elems);
                // section属性
                
                IAttributeSet attr = textboxElement.getAttribute();
                // width
                // 宽度
                AttrManage.instance().setPageWidth(attr, (int)(wpShape.getBounds().width * MainConstant.PIXEL_TO_TWIPS));
                // 高度
                AttrManage.instance().setPageHeight(attr, (int)(wpShape.getBounds().height * MainConstant.PIXEL_TO_TWIPS));
                Element bodyPr = sp.element("bodyPr");
                if (bodyPr != null)
                {
                    // 上边距
                    AttrManage.instance().setPageMarginTop(attr, processValue(bodyPr.attributeValue("tIns"), false));
                    // 下边距
                    AttrManage.instance().setPageMarginBottom(attr, processValue(bodyPr.attributeValue("bIns"), false));
                    // 左边距
                    AttrManage.instance().setPageMarginLeft(attr, processValue(bodyPr.attributeValue("lIns"), true));
                    // 右边距
                    AttrManage.instance().setPageMarginRight(attr, processValue(bodyPr.attributeValue("rIns"), true));
                    // 垂直对齐
                    String val = bodyPr.attributeValue("anchor");
                    if ("ctr".equals(val))
                    {
                        AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_CENTER);
                    }
                    else if("b".equals(val))
                    {
                        AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_BOTTOM);
                    }
                    else if ("t".equals(val))
                    {
                        AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_TOP);
                    }
                        // 文本框内自动换行
                    val = bodyPr.attributeValue("wrap");
                    wpShape.setTextWrapLine(val == null || "square".equalsIgnoreCase(val));
                    
                    wpShape.setElementIndex((int)textboxIndex);
                }
                textboxElement.setEndOffset(offset);
                textboxIndex++;
                offset = oldOffset;
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * 
     *
     */
    public boolean searchContent(File file, String key) throws Exception
    {
        boolean isContain = false;
        zipPackage = new ZipPackage(filePath);
        PackageRelationship coreRel = zipPackage.getRelationshipsByType(
            PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
        packagePart = zipPackage.getPart(coreRel);
        
        SAXReader saxreader = new SAXReader();
        InputStream in = packagePart.getInputStream();
        Document poiDoc = saxreader.read(in);
        
        // 正文
        Element root = poiDoc.getRootElement();
        Element body = root.element("body");
        StringBuilder sb = new StringBuilder();
        if (body != null)
        {
            List<Element> paras = body.elements("p");
            for (Element para : paras)
            {
                List<Element> runs = para.elements("r");
                for (Element run : runs)
                {   
                    Element text = run.element("t");
                    if (text == null)
                    {
                        continue;
                    }
                    sb.append(text.getText());
                }
                if (sb.indexOf(key) >= 0)
                {
                    isContain = true;
                    break;
                }
                sb.delete(0, sb.length());
            }
        }
        zipPackage= null;
        packagePart = null;
        
        in.close();
        
        return isContain;
    }

    /**
     * fix very large XML documents
     *
     */
    class DOCXSaxHandler implements ElementHandler
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
            String name = elem.getName();elem.elements();
            if ("p".equals(name))
            {
                processParagraph(elem, 0);
            }
            if ("sdt".equals(elem.getName()))
            {                      
        		elem = elem.element("sdtContent");
                if (elem != null)
                {
                	processParagraphs(elem.elements());
                }
            }
            else if ("tbl".equals(name))
            {
                processTable(elem);
            }
            else if("pict".equals(name))
            {
                ParagraphElement paraElem = new ParagraphElement();
                long t = offset;
                paraElem.setStartOffset(offset);
                processPicture(elem, paraElem);
                paraElem.setEndOffset(offset);
                if (offset > t)
                {
                    wpdoc.appendParagraph(paraElem, offset);
                }
            }
            elem.detach();
        }
        
    }
    
    private void processThemeColor() throws Exception
    {
        if (packagePart != null)
        {
            PackageRelationship themeShip = packagePart.getRelationshipsByType(
                PackageRelationshipTypes.THEME_PART).getRelationship(0);
            if (themeShip != null)
            {
                PackagePart themePart = zipPackage.getPart(themeShip.getTargetURI());
                if (themePart != null)
                {
                    themeColor = ThemeReader.instance().getThemeColorMap(themePart);
                    if (themeColor != null)
                    {
                        themeColor.put(SchemeClrConstant.SCHEME_BG1, themeColor.get(SchemeClrConstant.SCHEME_LT1));
                        themeColor.put(SchemeClrConstant.SCHEME_TX1, themeColor.get(SchemeClrConstant.SCHEME_DK1));
                        themeColor.put(SchemeClrConstant.SCHEME_BG2, themeColor.get(SchemeClrConstant.SCHEME_LT2));
                        themeColor.put(SchemeClrConstant.SCHEME_TX2, themeColor.get(SchemeClrConstant.SCHEME_DK2));
                    }
                }
            }
        }
    }
    
    /**
     * 
     */
    private void processRelativeShapeSize()
    {
        int w = AttrManage.instance().getPageWidth(secElem.getAttribute());
        int h = AttrManage.instance().getPageHeight(secElem.getAttribute());
        Iterator<IShape> iter = relativeType.iterator();
        while(iter.hasNext())
        {
        	IShape shape = iter.next();
        	int[] val = relativeValue.get(shape);        	
        	Rectangle r = shape.getBounds();
        	// width
            if (val[0] > 0)
            {
                r.width = (int)(w  * MainConstant.TWIPS_TO_PIXEL * val[0] / 1000f);
            }
            // height
            if (val[2] > 0)
            {
                r.height = (int)(h  * MainConstant.TWIPS_TO_PIXEL * val[2] / 1000f);
            }
        }
    }
    
    private byte getArrowType(String arrowType)
    {
        if("block".equalsIgnoreCase(arrowType))
        {
            return Arrow.Arrow_Triangle;
        }
        else if("classic".equalsIgnoreCase(arrowType))
        {
            return Arrow.Arrow_Stealth;
        }
        else if("oval".equalsIgnoreCase(arrowType))
        {
            return Arrow.Arrow_Oval;
        }
        else if("diamond".equalsIgnoreCase(arrowType))
        {
            return Arrow.Arrow_Diamond;
        }
        else if("open".equalsIgnoreCase(arrowType))
        {
            return Arrow.Arrow_Arrow;
        }
        else
        {
            return Arrow.Arrow_None;
        }
    }
    
    private int getArrowWidth(String width)
    {
        if("narrow".equalsIgnoreCase(width))
        {
            return 0;
        }
        else if("wide".equalsIgnoreCase(width))
        {
            return 2;
        }
        else
        {
            return 1;
        }
    }
    
    private int getArrowLength(String length)
    {
        if("short".equalsIgnoreCase(length))
        {
            return 0;
        }
        else if("long".equalsIgnoreCase(length))
        {
            return 2;
        }
        else
        {
            return 1;
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (isReaderFinish())
        {
            filePath = null;
            zipPackage = null;
            wpdoc = null;
            packagePart = null;
            if (styleStrID != null)
            {
                styleStrID.clear();
                styleStrID = null;
            }
            if (tableGridCol != null)
            {
                tableGridCol.clear();
                tableGridCol = null;
            }
            tableGridCol = null;
            control = null;
            if (relativeType != null)
            {
                relativeType.clear();
                relativeType = null;
            }
            if (relativeValue != null)
            {
                relativeValue.clear();
                relativeValue = null;
            }
            if (bulletNumbersID != null)
            {
                bulletNumbersID.clear();
                bulletNumbersID = null;
            }
        }
    }
    
    //
    private boolean isProcessSectionAttribute;
    //
    private boolean isProcessHF;
    //
    private boolean isProcessWatermark;
    //
    private int styleID;
    // offset计数器，此值非常重要，需要小心行事
    private long offset;
    //
    private long textboxIndex;
    //
    private String filePath;
    private int docSourceType;
    //
    private SectionElement secElem;
    //
    private ZipPackage zipPackage;
    //
    private WPDocument wpdoc;
    //
    private PackagePart packagePart;
    //
    private PackagePart hfPart;
    //
    private Map<String, Integer> styleStrID = new HashMap<String, Integer>();
    //
    private Map<String, Integer> tableStyle = new HashMap<String, Integer>();
    //
    private Map<Integer, Integer> tableGridCol = new HashMap<Integer, Integer>();
    //
    Hashtable<String, String> bulletNumbersID = new Hashtable<String, String>();  
    // theme color
    private Map<String, Integer> themeColor;
    //
    private List<IShape> relativeType = new ArrayList<IShape>();
    //
    private Map<IShape, int[]> relativeValue = new HashMap<IShape, int[]>();
}
