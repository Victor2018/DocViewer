/*
 * 文件名称:           RunArrt.java
 *  
 * 编译器:             android2.2
 * 时间:               下午2:21:14
 */
package com.nvqquy98.lib.doc.office.fc.ppt.attribute;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.HyperlinkReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.ReaderKit;
import com.nvqquy98.lib.doc.office.fc.xls.Reader.SchemeColorUtil;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.simpletext.font.FontTypefaceManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.LeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.model.Style;
import com.nvqquy98.lib.doc.office.simpletext.model.StyleManage;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;
import com.nvqquy98.lib.doc.office.ss.util.format.NumericFormatter;

import android.graphics.Color;

/**
 * 管理text run 属性
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-3-12
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class RunAttr
{
    private static RunAttr kit = new RunAttr();
    
    /**
     * 
     */
    public static RunAttr instance()
    {
        return kit;
    }
    
    /**
     * 
     * @param pgMaster
     * @param paraElem
     * @param p
     * @param attrLayout
     * @param offset
     * @param fontScale
     * @return
     */
    public int processRun(PGMaster pgMaster, ParagraphElement paraElem, 
        Element p, IAttributeSet attrLayout, int offset, int fontScale, int styleID)
    {
        maxFontSize = 0;
        LeafElement leaf = null;
        Element pPr = p.element("pPr");
        // 如果没有 r 元素，说明只有一个回车符的段落
        if (p.elements("r").size() == 0 && p.elements("fld").size() == 0 && p.elements("br").size() == 0)
        {
            leaf = new LeafElement("\n");
            // 属性
            if (pPr != null)
            {
                pPr = pPr.element("rPr");
            }
            if (pPr == null)
            {
                pPr = p.element("endParaRPr");
            }
            setRunAttribute(pgMaster, pPr, leaf.getAttribute(), attrLayout, fontScale, styleID, true);
            // set max font size
            setMaxFontSize(AttrManage.instance().getFontSize(paraElem.getAttribute(), leaf.getAttribute()));
            leaf.setStartOffset(offset);
            offset++;
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            return offset;
        }
        for (Iterator< ? > it = p.elementIterator(); it.hasNext();)
        {
            Element r = (Element)it.next();
            String name = r.getName();
            if (name.equals("r") || name.equals("fld") || name.equals("br"))
            {
                String text = null;
                if(name.equals("fld") 
                		&& r.attributeValue("type") != null 
                		&& r.attributeValue("type").contains("datetime"))
                {
                	//field code : current time
                	text = NumericFormatter.instance().getFormatContents("yyyy/m/d", new Date(System.currentTimeMillis()));
                }
                else
                {
                	Element t = r.element("t");
                    if (name.equals("br"))
                    {
                        text = String.valueOf('\u000b');
                    }
                    else if (t != null)
                    {
                        text = t.getText();
                    }
                }                
                
                if (text != null)
                {
                    text = text.replace((char)160, ' ');
                    int len = text.length();
                    if (len > 0)
                    {
                        leaf = new LeafElement(text);
                        // 属性
                        setRunAttribute(pgMaster, r.element("rPr"), leaf.getAttribute(), attrLayout, 
                            fontScale, styleID, "\n".equals(text));
                        // set max font size
                        setMaxFontSize(AttrManage.instance().getFontSize(paraElem.getAttribute(), leaf.getAttribute()));
                        // 开始 offset
                        leaf.setStartOffset(offset);
                        offset += len;
                        // 结束 offset
                        leaf.setEndOffset(offset);
                        paraElem.appendLeaf(leaf);
                    }
                }
            }
        }
        if (leaf != null)
        {
            leaf.setText(leaf.getText(null) + "\n");
            offset++;
        }
        return offset;
    }
    
    /**
     * 字号
     */
    private void setFontSize(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_SIZE_ID))
            {
                AttrManage.instance().setFontSize(attrTo, AttrManage.instance().getFontSize(null, attrFrom));
            }
        }
    }
    
    /**
     * 字体
     */
    private void setFontTypeface(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_NAME_ID))
            {
                AttrManage.instance().setFontName(attrTo, AttrManage.instance().getFontName(null, attrFrom));
            }
        }
    }
    
    /**
     * 字符颜色
     */
    private void setFontColor(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_COLOR_ID))
            {
                AttrManage.instance().setFontColor(attrTo, AttrManage.instance().getFontColor(null, attrFrom));
            }
        }
    }
    
    /**
     * 粗体
     */
    private void setFontBold(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_BOLD_ID))
            {
                AttrManage.instance().setFontBold(attrTo, AttrManage.instance().getFontBold(null, attrFrom));
            }
        }
    }
    
    /**
     * 斜体
     */
    private void setFontItalic(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_ITALIC_ID))
            {
                AttrManage.instance().setFontItalic(attrTo, AttrManage.instance().getFontItalic(null, attrFrom));
            }
        }
    }
    
    /**
     * 删除线
     */
    private void setFontStrike(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_STRIKE_ID))
            {
                AttrManage.instance().setFontStrike(attrTo, AttrManage.instance().getFontStrike(null, attrFrom));
            }
        }
    }
    
    /**
     * 双删除线
     */
    private void setFontDoubleStrike(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_DOUBLESTRIKE_ID))
            {
                AttrManage.instance().setFontDoubleStrike(attrTo, AttrManage.instance().getFontDoubleStrike(null, attrFrom));
            }
        }
    }
  
    /**
     * 下划线
     */
    private void setFontUnderline(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_UNDERLINE_ID))
            {
                AttrManage.instance().setFontUnderline(attrTo, AttrManage.instance().getFontUnderline(null, attrFrom));
                if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_UNDERLINE_COLOR_ID))
                {
                    AttrManage.instance().setFontUnderlineColr(attrTo, AttrManage.instance().getFontUnderlineColor(null, attrFrom));
                }
                else
                {
                    if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_COLOR_ID))
                    {
                        AttrManage.instance().setFontUnderlineColr(attrTo, AttrManage.instance().getFontColor(null, attrFrom));
                    }
                }
            }
        }
    }
    
    /**
     * 上下标
     */
    private void setFontScript(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_SCRIPT_ID))
            {
                AttrManage.instance().setFontScript(attrTo, AttrManage.instance().getFontScript(null, attrFrom));
            }
        }
    }
   
    /**
     * 超链接
     */
    private void setHyperlinkID(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.FONT_HYPERLINK_ID))
            {
                AttrManage.instance().setHyperlinkID(attrTo, AttrManage.instance().getHperlinkID(attrFrom));
            }
        }
    }
    
    /**
     * set text run attribute
     */
    public void setRunAttribute(PGMaster master, Element rPr, IAttributeSet attr, IAttributeSet attrLayout, 
        int fontScale, int styleID, boolean newLine)
    {
        if (rPr != null)
        {
            String val;
            // 字号
            if (rPr.attribute("sz") != null)
            {
                val = rPr.attributeValue("sz");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setFontSize(attr, (int)(Float.parseFloat(val) / 100));
                }
            }
            else
            {
                setFontSize(attrLayout, attr);
            }
            
            if (!newLine)
            {
                // 字体
                Element temp = rPr.element("latin");
                if (temp != null || rPr.element("ea") != null)
                {
                    if (temp == null)
                    {
                        temp = rPr.element("ea");
                    }
                    val = temp.attributeValue("typeface");
                    if (val != null)
                    {
                        int index = FontTypefaceManage.instance().addFontName(val);
                        if (index >= 0)
                        {
                            AttrManage.instance().setFontName(attr, index);
                        }
                    }
                }
                else
                {
                    setFontTypeface(attrLayout, attr);
                }
                
                // 字符颜色
                temp = rPr.element("solidFill");
                Integer fontColor = null;
                if (temp != null)
                {
                    fontColor = ReaderKit.instance().getColor(master, temp);
                    AttrManage.instance().setFontColor(attr, fontColor);
                }
                else if ((temp = rPr.element("gradFill")) != null)
                {
                    Element gsLst = temp.element("gsLst");
                    if (gsLst != null)
                    {
                        fontColor = ReaderKit.instance().getColor(master, gsLst.element("gs"));
                        AttrManage.instance().setFontColor(attr, fontColor);
                    }
                }
                else
                {
                    setFontColor(attrLayout, attr);
                }
                
                // 粗体
                if (rPr.attribute("b") != null)
                {
                    val = rPr.attributeValue("b");
                    if (val != null && val.length() > 0 && Integer.parseInt(val) > 0)
                    {
                        AttrManage.instance().setFontBold(attr, true);
                    }
                }
                else
                {
                    setFontBold(attrLayout, attr);
                }
                
                // 斜体
                if (rPr.attribute("i") != null)
                {
                    val = rPr.attributeValue("i");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setFontItalic(attr, Integer.parseInt(val) > 0);
                    }
                }
                else
                {
                    setFontItalic(attrLayout, attr);
                }
                
                // 下划线
                if (rPr.attribute("u") != null)
                {
                    val = rPr.attributeValue("u");
                    if (val != null && val.length() > 0)
                    {
                        if (!val.equalsIgnoreCase("none"))
                        {
                            AttrManage.instance().setFontUnderline(attr, 1);
                            
                            Element uFill = rPr.element("uFill");
                            if (uFill != null && (temp = uFill.element("solidFill")) != null)
                            {
                                AttrManage.instance().setFontUnderlineColr(attr, ReaderKit.instance().getColor(master, temp));
                            }
                            else
                            {
                                if (fontColor != null)
                                {
                                    AttrManage.instance().setFontUnderlineColr(attr, fontColor);
                                }
                            }
                        }
                    }
                }
                else
                {
                    setFontUnderline(attrLayout, attr);
                }
                
                // 删除线
                if (rPr.attribute("strike") != null)
                {
                    val = rPr.attributeValue("strike");
                    if (val.equals("dblStrike"))
                    {
                        // 双删除线
                        AttrManage.instance().setFontDoubleStrike(attr, true);
                    }
                    else if (val.equals("sngStrike"))
                    {
                        AttrManage.instance().setFontStrike(attr, true);
                    }
                }
                else
                {
                    setFontStrike(attrLayout, attr);
                    setFontDoubleStrike(attrLayout, attr);
                }
                
                // 上下标
                if (rPr.attribute("baseline") != null)
                {
                    val = rPr.attributeValue("baseline");
                    if (val != null && val.length() > 0)
                    {
                        int value = Integer.parseInt(val);
                        if (value != 0)
                        {
                            AttrManage.instance().setFontScript(attr, value > 0 ? 1 : 2);
                        }
                    }
                }
                else
                {
                    setFontScript(attrLayout, attr);
                }
                
                // hyperlink
                temp = rPr.element("hlinkClick");
                if (temp != null)
                {
                	int color = Color.BLUE;
                	if(master != null)
                	{
                		color = master.getSchemeColor().get("hlink");
                	}
                    AttrManage.instance().setFontColor(attr, color);
                    AttrManage.instance().setFontUnderline(attr, 1);
                    AttrManage.instance().setFontUnderlineColr(attr, color);
                    
                    val = temp.attributeValue("id");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setHyperlinkID(attr, HyperlinkReader.instance().getLinkIndex(val));
                    }
                }
                else
                {
                    setHyperlinkID(attrLayout, attr);
                }
            }
        }
        else if (attrLayout != null)
        {
            setFontSize(attrLayout, attr);
            if (!newLine)
            {
                setFontTypeface(attrLayout, attr);
                setFontColor(attrLayout, attr);
                setFontBold(attrLayout, attr);
                setFontItalic(attrLayout, attr);
                setFontUnderline(attrLayout, attr);
                setFontStrike(attrLayout, attr);
                setFontDoubleStrike(attrLayout, attr);
                setFontScript(attrLayout, attr);
                setHyperlinkID(attrLayout, attr);
            }
        }
        AttrManage.instance().setFontScale(attr, fontScale);
        
        // set default font size 18
        if (!AttrManage.instance().hasAttribute(attr, AttrIDConstant.FONT_SIZE_ID))
        {
            Style style = StyleManage.instance().getStyle(styleID);
            if (style != null && style.getAttrbuteSet() != null 
                && AttrManage.instance().hasAttribute(style.getAttrbuteSet(), AttrIDConstant.FONT_SIZE_ID))
            {
                return;
            }
            else
            {
                if (!table && slide)
                {
                    AttrManage.instance().setFontSize(attr, 18);
                }
            }
        }
    }
    
    /**
     * xlsx shared item color
     * @param book
     * @param clr
     * @return
     */
    private int getRunPropColor(Workbook book, Element clr)
    {
        int color = -1;
        String val;
        if(clr.attributeValue("indexed") != null)
        {
            val = clr.attributeValue("indexed");
            color = book.getColor(Integer.parseInt(val));
        }
        else if(clr.attributeValue("theme") != null)
        {
            val = clr.attributeValue("theme");
            //get scheme color
            color = SchemeColorUtil.getThemeColor(book, Integer.parseInt(val));            
        }
        else if(clr.attributeValue("rgb") != null)
        {
            val = clr.attributeValue("rgb");
            //get system color
            color = (int)Long.parseLong(val, 16);
        }
        
        if(clr.attributeValue("tint") != null)
        {
            double tint = Double.parseDouble(clr.attributeValue("tint"));            
            color = ColorUtil.instance().getColorWithTint(color, tint);
        }
        
        return color;
    }
    
    /**
     * 
     * @param themeColor
     * @param solidFillElement
     * @return
     */
    public int getColor(Workbook book, Element solidFillElement)
    {
        String val;
        Element clr;
        int color = -1;
        if(solidFillElement.element("srgbClr") != null)
        {
            clr = solidFillElement.element("srgbClr");
            color = (int)Long.parseLong( clr.attributeValue("val"), 16);
            color = (0xFF << 24) | color;
        }
        else if(solidFillElement.element("schemeClr") != null)
        {
            clr = solidFillElement.element("schemeClr");
            //get scheme color
            Map<String, Integer> schemeColor = SchemeColorUtil.getSchemeColor(book);
            color = schemeColor.get(clr.attributeValue("val"));
            
            if (clr.element("tint") != null)
            {
                color = ColorUtil.instance().getColorWithTint(color, 
                    Integer.parseInt(clr.element("tint").attributeValue("val")) / 100000.0);
            }
            else if (clr.element("lumOff") != null)
            {
                color = ColorUtil.instance().getColorWithTint(color, 
                    Integer.parseInt(clr.element("lumOff").attributeValue("val")) / 100000.0);
            }
            else if (clr.element("lumMod") != null)
            {
                color = ColorUtil.instance().getColorWithTint(color, 
                    Integer.parseInt(clr.element("lumMod").attributeValue("val")) / 100000.0 - 1);
            }
            else if (clr.element("shade") != null)
            {
                color = ColorUtil.instance().getColorWithTint(color, 
                    -Integer.parseInt(clr.element("shade").attributeValue("val")) / 200000.0);
            }
            
            if(clr.element("alpha") != null)
            {
                val = clr.element("alpha").attributeValue("val");
                if(val != null)
                {
                    int alpha = (int)(Integer.parseInt(val) / 100000f * 255);
                    color = (0xFFFFFF & color) | (alpha << 24);
                }
            }
        }
        else if(solidFillElement.element("sysClr") != null)
        {
            clr = solidFillElement.element("sysClr");
            //get system color
            color = Integer.parseInt( clr.attributeValue("lastClr"), 16);
            color = (0xFF << 24) | color;
        }
        return color;
    }
    
    /**
     * just for sheet xml parsed
     * @param sheet
     * @param rPr
     * @param attr
     * @param attrLayout
     */
    public void setRunAttribute(Sheet sheet, Element rPr, IAttributeSet attr, IAttributeSet attrLayout)
    {
        if (rPr != null)
        {
            String val;
            // 字号
            if (rPr.attribute("sz") != null)
            {
                val = rPr.attributeValue("sz");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setFontSize(attr, (int)(Float.parseFloat(val) / 100));
                }
            }
            else
            {
                setFontSize(attrLayout, attr);
            }
            
            // 字符颜色
            Element temp = rPr.element("solidFill");
            if (temp != null)
            {
                AttrManage.instance().setFontColor(attr,getColor(sheet.getWorkbook(), temp));
            }
            else
            {
                setFontColor(attrLayout, attr);
            }
            
            // 粗体
            if (rPr.attribute("b") != null)
            {
                AttrManage.instance().setFontBold(attr, Integer.parseInt(rPr.attributeValue("b")) == 1 ? true : false);
            }
            else
            {
                setFontBold(attrLayout, attr);
            }
            
            // 斜体
            if (rPr.attribute("i") != null)
            {
                AttrManage.instance().setFontItalic(attr, Integer.parseInt(rPr.attributeValue("i")) == 1 ? true : false);
            }
            else
            {
                setFontItalic(attrLayout, attr);
            }
            
            // 下划线
            if (rPr.attributeValue("u") != null && !rPr.attributeValue("u").equalsIgnoreCase("none"))
            {
                AttrManage.instance().setFontUnderline(attr, 1);
                Element uFill = rPr.element("uFill");
                if (uFill != null)
                {
                    temp = uFill.element("solidFill");
                    if (temp != null)
                    {
                        AttrManage.instance().setFontUnderlineColr(attr, getColor(sheet.getWorkbook(), temp));
                    }
                }
            }
            else
            {
                setFontUnderline(attrLayout, attr);
            }
            
            // 删除线
            if (rPr.attribute("strike") != null)
            {
                val = rPr.attributeValue("strike");
                if (val.equals("dblStrike"))
                {
                    // 双删除线
                    AttrManage.instance().setFontDoubleStrike(attr, true);
                }
                else if (val.equals("sngStrike"))
                {
                    AttrManage.instance().setFontStrike(attr, true);
                }
            }
            else
            {
                setFontStrike(attrLayout, attr);
                setFontDoubleStrike(attrLayout, attr);
            }
            
            // 上下标
            if (rPr.attribute("baseline") != null)
            {
                val = rPr.attributeValue("baseline");
                if (val != null && !val.equalsIgnoreCase("0"))
                {
                    AttrManage.instance().setFontScript(attr, Integer.parseInt(val) > 0 ? 1 : 2);
                }
            }
            else
            {
                setFontScript(attrLayout, attr);
            }
            
            // hyperlink
            temp = rPr.element("hlinkClick");
            if (temp != null && temp.attribute("id") != null)
            {
                val = temp.attributeValue("id");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setFontColor(attr, Color.BLUE);
                    AttrManage.instance().setFontUnderline(attr, 1);
                    AttrManage.instance().setFontUnderlineColr(attr, Color.BLUE);
                    AttrManage.instance().setHyperlinkID(attr, HyperlinkReader.instance().getLinkIndex(val));
                }
            }
            else
            {
                setHyperlinkID(attrLayout, attr);
            }
        }
        else if (attrLayout != null)
        {
            setFontSize(attrLayout, attr);
            setFontColor(attrLayout, attr);
            setFontBold(attrLayout, attr);
            setFontItalic(attrLayout, attr);
            setFontUnderline(attrLayout, attr);
            setFontStrike(attrLayout, attr);
            setFontDoubleStrike(attrLayout, attr);
            setFontScript(attrLayout, attr);
            setHyperlinkID(attrLayout, attr);
        }
    }
    
    /**
    * just for xlsx shared item parsed
    * @param sheet
    * @param rPr
    * @param attr
    * @param attrLayout
    */
   public void setRunAttribute(Workbook book, int fontID, Element rPr, IAttributeSet attr, IAttributeSet attrLayout)
   {
       if (rPr != null)
       {
           String val;
//           Font font = book.getFont(fontID);
           Element temp = rPr.element("sz");
           // 字号
           if (temp != null)
           {
               val = temp.attributeValue("val");
               if (val != null && val.length() > 0)
               {
                   AttrManage.instance().setFontSize(attr, (int)Float.parseFloat(val));
               }
           }
           else
           {
               setFontSize(attrLayout, attr);
           }
           
           // 字符颜色
           temp = rPr.element("color");
           if (temp != null)
           {
               AttrManage.instance().setFontColor(attr, getRunPropColor(book, temp));
           }
           else
           {
               setFontColor(attrLayout, attr);
           }
           
           // 粗体
           temp = rPr.element("b");
           if (temp != null)
           {
               AttrManage.instance().setFontBold(attr, true);
           }
           else
           {
               setFontBold(attrLayout, attr);
           }
           
           // 斜体
           temp = rPr.element("i");
           if (temp != null)
           {
               AttrManage.instance().setFontItalic(attr, true);
           }
           else
           {
               setFontItalic(attrLayout, attr);
           }
           
           // 下划线
           temp = rPr.element("u");
           if (temp != null)
           {
               AttrManage.instance().setFontUnderline(attr, 1);               
           }
           else
           {
               setFontUnderline(attrLayout, attr);
           }
           
           // 删除线
           temp = rPr.element("strike");
           if (temp != null)
           {
               AttrManage.instance().setFontStrike(attr, true);
               setFontDoubleStrike(attrLayout, attr);
           }
           else
           {
               setFontStrike(attrLayout, attr);
               setFontDoubleStrike(attrLayout, attr);
           }
           
           // 上下标
           temp = rPr.element("vertAlign");
           if (temp != null)
           {
               val = temp.attributeValue("val");
               if (val.equalsIgnoreCase("superscript"))
               {
                   AttrManage.instance().setFontScript(attr, Font.SS_SUPER);
               }
               else if (val.equalsIgnoreCase("subscript"))
               {
                   AttrManage.instance().setFontScript(attr, Font.SS_SUB);
               }
               else
               {
                   AttrManage.instance().setFontScript(attr, Font.SS_NONE);
               }
           }
           else
           {
               setFontScript(attrLayout, attr);
           }
           
           // hyperlink
           setHyperlinkID(attrLayout, attr);
       }
       else if (attrLayout != null)
       {
           Font font = book.getFont(fontID);
           if(font != null)
           {
               AttrManage.instance().setFontSize(attr, (int)font.getFontSize());
               AttrManage.instance().setFontColor(attr, book.getColor(font.getColorIndex()));
               AttrManage.instance().setFontBold(attr, font.isBold());
               AttrManage.instance().setFontItalic(attr, font.isItalic());
               AttrManage.instance().setFontUnderline(attr, font.getUnderline());
               AttrManage.instance().setFontStrike(attr, font.isStrikeline());
               setFontDoubleStrike(attrLayout, attr);
               AttrManage.instance().setFontScript(attr, font.getSuperSubScript());
               setHyperlinkID(attrLayout, attr);
           }
           else
           {
               setFontSize(attrLayout, attr);
               setFontColor(attrLayout, attr);
               setFontBold(attrLayout, attr);
               setFontItalic(attrLayout, attr);
               setFontUnderline(attrLayout, attr);
               setFontStrike(attrLayout, attr);
               setFontDoubleStrike(attrLayout, attr);
               setFontScript(attrLayout, attr);
               setHyperlinkID(attrLayout, attr);
           }
       }
   }
    
    /**
     * 
     * @param sheet
     * @param cell
     * @param attr
     * @param attrLayout
     */
    public void setRunAttribute(Sheet sheet, Cell cell, IAttributeSet attr, IAttributeSet attrLayout)
    {
        if (cell != null)
        {
            CellStyle style = cell.getCellStyle();
            Workbook book = sheet.getWorkbook();
            Font font = book.getFont(style.getFontIndex());
            
            // 字号
            AttrManage.instance().setFontSize(attr, (int)(font.getFontSize() + 0.5f));
            
            // 字符颜色
            AttrManage.instance().setFontColor(attr,book.getColor(font.getColorIndex()));
            
            // 粗体
            AttrManage.instance().setFontBold(attr, font.isBold());
            
            // 斜体
            AttrManage.instance().setFontItalic(attr, font.isItalic());
            
            // 下划线
            AttrManage.instance().setFontUnderline(attr, font.getUnderline());           
            
            
            // 删除线
            AttrManage.instance().setFontStrike(attr, font.isStrikeline());
        }
        else if (attrLayout != null)
        {
            setFontSize(attrLayout, attr);
            setFontColor(attrLayout, attr);
            setFontBold(attrLayout, attr);
            setFontItalic(attrLayout, attr);
            setFontUnderline(attrLayout, attr);
            setFontStrike(attrLayout, attr);
            setFontDoubleStrike(attrLayout, attr);
            setFontScript(attrLayout, attr);
            setHyperlinkID(attrLayout, attr);
        }
    }
    
    /**
     * 
     * @param sheet
     * @param font
     * @param attr
     * @param attrLayout
     */
    public void setRunAttribute(Sheet sheet, Font font, IAttributeSet attr, IAttributeSet attrLayout)
    {
        if (font != null)
        {
            Workbook book = sheet.getWorkbook();
            
            // 字号
            AttrManage.instance().setFontSize(attr, (int)(font.getFontSize() + 0.5f));
            
            // 字符颜色
            AttrManage.instance().setFontColor(attr, book.getColor(font.getColorIndex()));
            
            // 粗体
            AttrManage.instance().setFontBold(attr, font.isBold());
            
            // 斜体
            AttrManage.instance().setFontItalic(attr, font.isItalic());
            
            // 下划线
            AttrManage.instance().setFontUnderline(attr, font.getUnderline());           
            
            
            // 删除线
            AttrManage.instance().setFontStrike(attr, font.isStrikeline());
        }
        else if (attrLayout != null)
        {
            setFontSize(attrLayout, attr);
            setFontColor(attrLayout, attr);
            setFontBold(attrLayout, attr);
            setFontItalic(attrLayout, attr);
            setFontUnderline(attrLayout, attr);
            setFontStrike(attrLayout, attr);
            setFontDoubleStrike(attrLayout, attr);
            setFontScript(attrLayout, attr);
            setHyperlinkID(attrLayout, attr);
        }
    }    
    
    /**
     * 
     * @return
     */
    public int getMaxFontSize()
    {
        return maxFontSize;
    }
    
    /**
     * 
     * @param size
     */
    public void setMaxFontSize(int size)
    {
        if (size > maxFontSize)
        {
            maxFontSize = size;
        }
    }
    
    /**
     * 
     */
    public void resetMaxFontSize()
    {
        maxFontSize = 0;
    }
    
    /**
     * 
     * @param bTable
     */
    public void setTable(boolean table)
    {
        this.table = table;
    }
    
    /**
     * 
     * @param slide
     */
    public void setSlide(boolean slide)
    {
        this.slide = slide;
    }
    
    /**
     * 
     * @return
     */
    public boolean isTable()
    {
        return table;
    }
    
    /**
     * 
     * @return
     */
    public boolean isSlide()
    {
        return slide;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        maxFontSize = 0;
    }
    
    // 一个段落下字号的最大值
    private int maxFontSize = 0;
    // text of table or not
    private boolean table;
    // text of slide or not
    private boolean slide;
}
