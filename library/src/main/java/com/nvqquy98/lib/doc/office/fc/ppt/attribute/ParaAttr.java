/*
 * 文件名称:           ParaAttr.java
 *  
 * 编译器:             android2.2
 * 时间:               下午1:52:05
 */
package com.nvqquy98.lib.doc.office.fc.ppt.attribute;

import java.util.List;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.ppt.bulletnumber.BulletNumberManage;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.ReaderKit;
import com.nvqquy98.lib.doc.office.pg.model.PGLayout;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.pg.model.PGPlaceholderUtil;
import com.nvqquy98.lib.doc.office.pg.model.PGStyle;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttributeSetImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.simpletext.model.Style;
import com.nvqquy98.lib.doc.office.simpletext.model.StyleManage;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * 管理段落属性
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
public class ParaAttr
{
    // 一行一磅字符对应的段前段后磅值
    public static final float POINT_PER_LINE_PER_FONTSIZE = 1.2f;
    
    private static ParaAttr kit = new ParaAttr();
    
    /**
     * 
     */
    public static ParaAttr instance()
    {
        return kit;
    }
    
    /**
     * process paragraph
     * @param master
     * @param pgLayout
     * @param defautltStyle
     * @param secElem
     * @param txBody
     * @param type
     * @param idx
     * @return
     */
    public int processParagraph(IControl control, PGMaster master, PGLayout pgLayout, PGStyle defaultStyle, 
        SectionElement secElem, Element styleElement, Element txBody, String type, int idx)
    {
        String val;
        int fontScale = 100;
        int lnSpcReduction = 0;
        Boolean defaultFontColor = false;
        Element bodyPr = txBody.element("bodyPr");
        if (bodyPr != null)
        {
            Element normAutofit = bodyPr.element("normAutofit");
            if (normAutofit != null)
            {
                if (normAutofit.attribute("fontScale") != null)
                {
                    val = normAutofit.attributeValue("fontScale");
                    if (val != null && val.length() > 0)
                    {
                        fontScale = Integer.parseInt(val) / 1000;
                    }
                }
                if (normAutofit.attribute("lnSpcReduction") != null)
                {
                    val = normAutofit.attributeValue("lnSpcReduction");
                    if (val != null && val.length() > 0)
                    {
                        lnSpcReduction = Integer.parseInt(val);
                    }
                }
            }
        }
        int offset = 0;
        boolean subTitle = PGPlaceholderUtil.SUBTITLE.equals(type);
        Element lstStyle = txBody.element("lstStyle");
        List<Element> ps = txBody.elements("p");
        for (Element p : ps)
        {   
            int lvl = 1;
            Element pPr = p.element("pPr");
            if (pPr != null && pPr.attribute("lvl") != null)
            {
                val = pPr.attributeValue("lvl");
                if (val != null && val.length() > 0)
                {
                    lvl += Integer.parseInt(val);
                }
            }
            int layoutStyle = -1;
            if (pgLayout != null)
            {
                layoutStyle = pgLayout.getStyleID(type, idx, lvl);
            }
            int masterStyle = -1;
            if (master != null)
            {
                masterStyle = master.getTextStyle(type, idx, lvl);
            }
            if (masterStyle < 0 && defaultStyle != null)
            {
                defaultFontColor = true;
                masterStyle = defaultStyle.getStyle(lvl);
            }
            
            ParagraphElement paraElem = new ParagraphElement();
            paraElem.setStartOffset(offset);
            IAttributeSet attrLayout = null;
            
            if (lstStyle != null)
            {
            	int ind = lvl;
                if (lvl > 0 || lstStyle.element("defPPr") == null)
                {
                	ind += 1;
                }
                Element txStyle = null;
                switch(ind)
                {
                    case 1:
                        txStyle = lstStyle.element("defPPr");
                        break;
                        
                    case 2:
                        txStyle = lstStyle.element("lvl1pPr");
                        break;
                        
                    case 3:
                        txStyle = lstStyle.element("lvl2pPr");
                        break;
                        
                    case 4:
                        txStyle = lstStyle.element("lvl3pPr");
                        break;
                        
                    case 5:
                        txStyle = lstStyle.element("lvl4pPr");
                        break;
                        
                    case 6:
                        txStyle = lstStyle.element("lvl5pPr");
                        break;
                        
                    case 7:
                        txStyle = lstStyle.element("lvl6pPr");
                        break;
                        
                    case 8:
                        txStyle = lstStyle.element("lvl7pPr");
                        break;
                        
                    case 9:
                        txStyle = lstStyle.element("lvl8pPr");
                        break;
                        
                    case 10:
                        txStyle = lstStyle.element("lvl9pPr");
                        break;
                        
                    default:
                        break;
                }
                if (txStyle != null)
                {
                    attrLayout = new AttributeSetImpl();
                    // paragraph attribute set
                    setParaAttribute(control, txStyle, attrLayout, null, -1, -1, 0, true, subTitle);
                    Element defRPr = txStyle.element("defRPr");
                    // character attribute set
                    RunAttr.instance().setRunAttribute(master, defRPr, attrLayout, null, 100, -1, false);
                    processParaWithPct(txStyle, attrLayout);
                }
            }
            if (attrLayout == null && layoutStyle > 0)
            {
                Style style = StyleManage.instance().getStyle(layoutStyle);
                if (style != null)
                {
                    attrLayout = style.getAttrbuteSet();
                }
            }
            else if(attrLayout == null && styleElement != null)
            {
                Element fontRef = styleElement.element("fontRef");
                if(fontRef.elements().size() > 0)
                {
                    int fontColor = ReaderKit.instance().getColor(master, fontRef);
                    attrLayout = new AttributeSetImpl();
                    AttrManage.instance().setFontColor(attrLayout, fontColor);
                    
                }                
            }
            else if(defaultFontColor && attrLayout == null && defaultStyle != null)
            {
                String fontColor = defaultStyle.getDefaultFontColor(lvl);
                if (fontColor != null)
                {
                    attrLayout = new AttributeSetImpl();            	
                    AttrManage.instance().setFontColor(attrLayout,  master.getColor(fontColor));
                }
            }
            
            offset = RunAttr.instance().processRun(master, paraElem, p, attrLayout, offset, fontScale, masterStyle);
            
            // when leafElem only contains \n, don't show bullet number 
        	if (p.elements("r").size() == 0 && p.elements("fld").size() == 0)
            {
                setParaAttribute(control, pPr, paraElem.getAttribute(), attrLayout, layoutStyle, masterStyle, 
                    lnSpcReduction, false, subTitle);
            }
            else
            {
                setParaAttribute(control, pPr, paraElem.getAttribute(), attrLayout, layoutStyle, masterStyle, 
                    lnSpcReduction, true, subTitle);
            }  

            processParaWithPct(pPr, paraElem.getAttribute());
            
            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
        }
        BulletNumberManage.instance().clearData();
        RunAttr.instance().setMaxFontSize(0);
        return offset;
    }
    
    /**
     * 
     * @param attr
     * @param align
     */
    public void setParaAlign(IAttributeSet attr, String align)
    {
        // 左对齐
        if (align.equals("l"))
        {
            AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_LEFT);
        }
        // 居中
        else if (align.equals("ctr"))
        {
            AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_CENTER);
        }
        // 右对齐
        else if (align.equals("r"))
        {
            AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_RIGHT);
        }
    }
    
    /**
     * 水平对齐方式
     */
    public void setParaHorizontalAlign(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_HORIZONTAL_ID))
            {
                AttrManage.instance().setParaHorizontalAlign(attrTo, 
                    AttrManage.instance().getParaHorizontalAlign(attrFrom));
            }
        }
    }
    
    /**
     * 段前间距
     */
    public void setParaBefore(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_BEFORE_ID))
            {
                AttrManage.instance().setParaBefore(attrTo, 
                    AttrManage.instance().getParaBefore(attrFrom));
            }
        }
    }
    
    /**
     * 段后间距
     */
    public void setParaAfter(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_AFTER_ID))
            {
                AttrManage.instance().setParaAfter(attrTo, 
                    AttrManage.instance().getParaAfter(attrFrom));
            }
        }
    }
    
    /**
     * 行距
     */
    public void setParaLineSpace(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_LINESPACE_TYPE_ID))
            {
                AttrManage.instance().setParaLineSpaceType(attrTo, 
                    AttrManage.instance().getParaLineSpaceType(attrFrom));
            }
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_LINESPACE_ID))
            {
                AttrManage.instance().setParaLineSpace(attrTo, 
                    AttrManage.instance().getParaLineSpace(attrFrom));
            }
        }
    }
    
    /**
     * 左缩进 
     */
    public void setParaIndentLeft(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_INDENT_LEFT_ID))
            {
                AttrManage.instance().setParaIndentLeft(attrTo, 
                    AttrManage.instance().getParaIndentLeft(attrFrom));
            }
        }
    }
    
    /**
     * 右缩进 
     */
    public void setParaIndentRight(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_INDENT_RIGHT_ID))
            {
                AttrManage.instance().setParaIndentRight(attrTo, 
                    AttrManage.instance().getParaIndentRight(attrFrom));
            }
        }
    }
    
    /**
     * 特殊缩进 
     */
    public void setParaSpecialIndent(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PARA_SPECIALINDENT_ID))
            {
                AttrManage.instance().setParaSpecialIndent(attrTo, 
                    AttrManage.instance().getParaSpecialIndent(attrFrom));
            }
        }
    }
    
    /**
     * set paragraph attribute
     */
    public void setParaAttribute(IControl control, Element pPr, IAttributeSet attr, IAttributeSet attrLayout, 
        int layoutStyle, int masterStyle, int lnSpcReduction, boolean addBullet, boolean subTitle)
    {
        String val;
        if (pPr != null)
        {
            // 水平对齐
            Element temp;
            if (pPr.attribute("algn") != null)
            {
                val = pPr.attributeValue("algn");
                setParaAlign(attr, val);
            }
            else
            {
                setParaHorizontalAlign(attrLayout, attr);
            }
            
            // 段前
            Element spcBef = pPr.element("spcBef");
            if (spcBef != null)
            {
                // 固定值
                temp = spcBef.element("spcPts");
                if (temp != null && temp.attribute("val") != null)
                {
                    val = temp.attributeValue("val");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaBefore(attr, 
                            (int)(Integer.parseInt(val) / 100 * MainConstant.POINT_TO_TWIPS));
                    }
                }
            }
            else
            {
                setParaBefore(attrLayout, attr);
            }
            
            // 段后
            Element spcAft = pPr.element("spcAft");
            if (spcAft != null)
            {
                // 固定值
                temp = spcAft.element("spcPts");
                if (temp != null && temp.attribute("val") != null)
                {
                    val = temp.attributeValue("val");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaAfter(attr, 
                            (int)(Integer.parseInt(val) / 100 * MainConstant.POINT_TO_TWIPS));
                    }
                }
            }
            else
            {
                setParaAfter(attrLayout, attr);
            }
            
            // 行距
            Element lnSpc = pPr.element("lnSpc");
            if (lnSpc != null)
            {
                // 固定值
                temp = lnSpc.element("spcPts");
                if (temp != null && temp.attribute("val") != null)
                { 
                    val = temp.attributeValue("val");
                    if (val != null && val.length() > 0)
                    {
                        // 行距类型
                        AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SPACE_EXACTLY);
                        // 行距
                        AttrManage.instance().setParaLineSpace(attr, 
                            (int)(Integer.parseInt(val) / 100 * MainConstant.POINT_TO_TWIPS));
                    }
                }
                
                // 多倍行距
                temp = lnSpc.element("spcPct");
                if (temp != null && temp.attribute("val") != null)
                {
                    val = temp.attributeValue("val");
                    if (val != null && val.length() > 0)
                    {
                        // 行距类型
                        AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE); 
                        // 行距
                        AttrManage.instance().setParaLineSpace(attr, (float)(Integer.parseInt(val) - lnSpcReduction) / 100000);
                    }
                }
            }
            else
            {
                if (lnSpcReduction > 0)
                {
                    // 行距类型
                    AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE);
                    // 行距
                    AttrManage.instance().setParaLineSpace(attr, (float)(100000 - lnSpcReduction) / 100000);
                }
                else
                {
                    setParaLineSpace(attrLayout, attr);
                }
            }
            
            // 右缩进
            if (pPr.attribute("marR") != null)
            {
                val = pPr.attributeValue("marR");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaIndentRight(attr, 
                        (int)(Integer.parseInt(val) * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH 
                            * MainConstant.POINT_TO_TWIPS));
                }
            }
            else
            {
                setParaIndentRight(attrLayout, attr);
            }
        }
        else
        {
            setParaHorizontalAlign(attrLayout, attr);
            setParaBefore(attrLayout, attr);
            setParaAfter(attrLayout, attr);
            // para linespace
            if (lnSpcReduction > 0)
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE);
                // 行距
                AttrManage.instance().setParaLineSpace(attr, (float)(100000 - lnSpcReduction) / 100000);
            }
            else
            {
                setParaLineSpace(attrLayout, attr);
            }
            setParaIndentLeft(attrLayout, attr);
            setParaIndentRight(attrLayout, attr);
        }
        
        Style style = StyleManage.instance().getStyle(masterStyle);
        
        // 缩进
        // 左缩进
        int leftFrom = 0;
        int left = 0;
        if (pPr != null && pPr.attribute("marL") != null)
        {
            val = pPr.attributeValue("marL");
            if (val != null && val.length() > 0)
            {
                left = (int)(Integer.parseInt(val) * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH 
                    * MainConstant.POINT_TO_TWIPS);
                AttrManage.instance().setParaIndentInitLeft(attr, left);
                AttrManage.instance().setParaIndentLeft(attr, left);
            }
        }
        else if (attrLayout != null)
        {
            if (AttrManage.instance().hasAttribute(attrLayout, AttrIDConstant.PARA_INDENT_LEFT_ID))
            {
                leftFrom = 1;
                left = AttrManage.instance().getParaIndentInitLeft(attrLayout);
                AttrManage.instance().setParaIndentLeft(attr, left);
            }
        }
        else
        {
            if (style != null && style.getAttrbuteSet() != null 
                && AttrManage.instance().hasAttribute(style.getAttrbuteSet(), AttrIDConstant.PARA_INDENT_LEFT_ID))
            {
                leftFrom = 2;
                left = AttrManage.instance().getParaIndentInitLeft(style.getAttrbuteSet());
                AttrManage.instance().setParaIndentLeft(attr, left);
            }
        }
        
        // 首行、悬挂缩进
        int indent = 0;
        if (pPr != null && pPr.attribute("indent") != null)
        {
            val = pPr.attributeValue("indent");
            if (val != null && val.length() > 0)
            {
                indent = (int)(Integer.parseInt(val)* MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH 
                    * MainConstant.POINT_TO_TWIPS);
                setSpecialIndent(attr, left, indent, true);
            }
        }
        else if (attrLayout != null)
        {
            if (AttrManage.instance().hasAttribute(attrLayout, AttrIDConstant.PARA_SPECIALINDENT_ID))
            {
                indent = AttrManage.instance().getParaSpecialIndent(attrLayout);
//                if (leftFrom == 1)
//                {
//                    setSpecialIndent(attr, left, indent, false);
//                }
//                else
                {
                    setSpecialIndent(attr, left, indent, true);
                }
            }
        }
        else
        {
            if (style != null && style.getAttrbuteSet() != null 
                && AttrManage.instance().hasAttribute(style.getAttrbuteSet(), AttrIDConstant.PARA_SPECIALINDENT_ID))
            {
                indent = AttrManage.instance().getParaSpecialIndent(style.getAttrbuteSet());
//                if (leftFrom == 2)
//                {
//                    setSpecialIndent(attr, left, indent, false);
//                }
//                else
                {
                    setSpecialIndent(attr, left, indent, true);
                }
            }
        }
        
        // bullet and number
        if (addBullet && (pPr == null || (pPr != null && pPr.element("buNone") == null)))
        {
            int id = BulletNumberManage.instance().addBulletNumber(control, -1, pPr);
            if (id == -1 && attrLayout != null)
            {
                id = AttrManage.instance().getPGParaBulletID(attrLayout);
            }
            if (id == -1 && layoutStyle >= 0)
            {
                id = BulletNumberManage.instance().getBulletID(layoutStyle);
            }
            if (id == -1 && masterStyle > 0 && !subTitle)
            {
                id = BulletNumberManage.instance().getBulletID(masterStyle);
            }
            if (id >= 0)
            {
                AttrManage.instance().setPGParaBulletID(attr, id);
            }
        }
        
        // set style
        if (masterStyle > 0)
        {
            AttrManage.instance().setParaStyleID(attr, masterStyle);
        }
    }
    
    /**
     * 
     * @param attr
     * @param left
     * @param indent
     * @param bSet
     */
    public void setSpecialIndent(IAttributeSet attr, int left, int indent, boolean bSet)
    {
    	// indent >= 0 为首行缩进，indent < 0 为悬挂缩进
    	if(indent < 0 && Math.abs(indent) > left)
    	{
    		indent = -left;
    	}
        AttrManage.instance().setParaSpecialIndent(attr, indent);
        // 悬挂缩进值也设置到左缩进，左缩进需要减去悬挂缩进
        if (bSet && indent < 0)
        {
            AttrManage.instance().setParaIndentLeft(attr, left + indent);
        }       
    }
    
    /**
     * set paragraph attribute
     */
    public void setParaAttribute(CellStyle style, IAttributeSet attr, IAttributeSet attrLayout)
    {
        if (style != null && attrLayout != null)
        {            
            int indent = (int)(style.getIndent() * SSConstant.INDENT_TO_PIXEL);
            // 水平对齐
            switch(style.getHorizontalAlign())
            {
                case CellStyle.ALIGN_LEFT:
                case CellStyle.ALIGN_GENERAL:
                    attrLayout.setAttribute(AttrIDConstant.PARA_INDENT_LEFT_ID, Math.round(indent * MainConstant.PIXEL_TO_TWIPS));
                    attrLayout.setAttribute(AttrIDConstant.PARA_INDENT_RIGHT_ID, 0);
                    AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_LEFT);
                    break;
                    
                case CellStyle.ALIGN_RIGHT:

                    attrLayout.setAttribute(AttrIDConstant.PARA_INDENT_LEFT_ID, 0);
                    attrLayout.setAttribute(AttrIDConstant.PARA_INDENT_RIGHT_ID, Math.round(indent * MainConstant.PIXEL_TO_TWIPS));
                    AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_RIGHT);
                    break;
                    
                case CellStyle.ALIGN_CENTER:
                case CellStyle.ALIGN_FILL:
                case CellStyle.ALIGN_JUSTIFY:
                case CellStyle.ALIGN_CENTER_SELECTION:
                    AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_CENTER);
                    break;                
            }            
            
            
            setParaBefore(attrLayout, attr);
            
            setParaAfter(attrLayout, attr);
            
            setParaLineSpace(attrLayout, attr);
            
            // 缩进
            // 左缩进
            setParaIndentLeft(attrLayout, attr);
            
            // 右缩进
            setParaIndentRight(attrLayout, attr);
            
            // 首行、悬挂缩进
            setParaSpecialIndent(attrLayout, attr);
        }
        else if (attrLayout != null)
        {
            setParaHorizontalAlign(attrLayout, attr);
            setParaBefore(attrLayout, attr);
            setParaAfter(attrLayout, attr);
            setParaLineSpace(attrLayout, attr);
        }       
    }
    
    /**
     * 处理以行为单位的段前段后
     */
    public void processParaWithPct(Element pPr, IAttributeSet attr)
    {
        int fontSize = RunAttr.instance().getMaxFontSize();
        if (pPr != null)
        { 
            Element temp;
            String val;
            // 段前
            Element spcBef = pPr.element("spcBef");
            if (spcBef != null)
            {
                // 段前 ？行
                temp = spcBef.element("spcPct");
                if (temp != null && temp.attribute("val") != null)
                {
                    val = temp.attributeValue("val");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaBefore(attr, 
                            (int)(Integer.parseInt(val) / 100000.f * fontSize * POINT_PER_LINE_PER_FONTSIZE * MainConstant.POINT_TO_TWIPS));
                    }
                }
            }
            
            // 段后
            Element spcAft = pPr.element("spcAft");
            if (spcAft != null)
            {
                // 段后 ？行
                temp = spcAft.element("spcPct");
                if (temp != null && temp.attribute("val") != null)
                {
                    val = temp.attributeValue("val");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaAfter(attr, 
                            (int)(Integer.parseInt(val) / 100000.f * fontSize * POINT_PER_LINE_PER_FONTSIZE * MainConstant.POINT_TO_TWIPS));
                    }
                }
            }
        }
    }
}
