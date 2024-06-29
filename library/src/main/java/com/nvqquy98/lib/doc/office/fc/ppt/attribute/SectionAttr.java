/*
 * 文件名称:           ParaAttr.java
 *  
 * 编译器:             android2.2
 * 时间:               下午1:04:08
 */
package com.nvqquy98.lib.doc.office.fc.ppt.attribute;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttributeSetImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;

/**
 * 管理章节属性
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
public class SectionAttr
{
    // default left and right margin value
    public static final int DEFAULT_MARGIN_LEFT_RIGHT = 144;
    // default top and bottom margin value
    public static final int DEFAULT_MARGIN_TOP_BOTTOM = 72;
    // default table margin value
    public static final int DEFAULT_TABLE_MARGIN = 30;
    
    private static SectionAttr kit = new SectionAttr();
    
    /**
     * 
     */
    public static SectionAttr instance()
    {
        return kit;
    }
    
    /**
     * set margin left
     */
    public void setPageMarginLeft(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PAGE_LEFT_ID))
            {
                AttrManage.instance().setPageMarginLeft(attrTo, 
                    AttrManage.instance().getPageMarginLeft(attrFrom));
            }
        }
    }
    
    /**
     * set margin right
     */
    public void setPageMarginRight(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PAGE_RIGHT_ID))
            {
                AttrManage.instance().setPageMarginRight(attrTo, 
                    AttrManage.instance().getPageMarginRight(attrFrom));
            }
        }
    }
    
    /**
     * set margin top
     */
    public void setPageMarginTop(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PAGE_TOP_ID))
            {
                AttrManage.instance().setPageMarginTop(attrTo, 
                    AttrManage.instance().getPageMarginTop(attrFrom));
            }
        }
    }
    
    /**
     * set margin bottom
     */
    public void setPageMarginBottom(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PAGE_BOTTOM_ID))
            {
                AttrManage.instance().setPageMarginBottom(attrTo, 
                    AttrManage.instance().getPageMarginBottom(attrFrom));
            }
        }
    }
    
    /**
     * set vertical alignment
     */
    public void setPageVerticalAlign(IAttributeSet attrFrom, IAttributeSet attrTo)
    {
        if (attrFrom != null)
        {
            if (AttrManage.instance().hasAttribute(attrFrom, AttrIDConstant.PAGE_VERTICAL_ID))
            {
                AttrManage.instance().setPageVerticalAlign(attrTo, 
                    AttrManage.instance().getPageVerticalAlign(attrFrom));
            }
        }
    }
    
    /**
     * 首先取 layout里的值，没有再取 master里的值
     */
    public IAttributeSet getDefautSectionAttr(IAttributeSet attrLayout, IAttributeSet attrMaster)
    {
        if (attrLayout != null || attrMaster != null)
        {
            IAttributeSet attr = new AttributeSetImpl();
            if (attrLayout == null)
            {
                setPageMarginLeft(attrMaster, attr);
                setPageMarginRight(attrMaster, attr);
                setPageMarginTop(attrMaster, attr);
                setPageMarginBottom(attrMaster, attr);
                setPageVerticalAlign(attrMaster, attr);
            }
            else
            {
                if (attrMaster == null)
                {
                    setPageMarginLeft(attrLayout, attr);
                    setPageMarginRight(attrLayout, attr);
                    setPageMarginTop(attrLayout, attr);
                    setPageMarginBottom(attrLayout, attr);
                    setPageVerticalAlign(attrLayout, attr);
                }
                else
                {
                    if (AttrManage.instance().hasAttribute(attrLayout, AttrIDConstant.PAGE_LEFT_ID))
                    {
                        setPageMarginLeft(attrLayout, attr);
                    }
                    else
                    {
                        setPageMarginLeft(attrMaster, attr);
                    }
                    
                    if (AttrManage.instance().hasAttribute(attrLayout, AttrIDConstant.PAGE_RIGHT_ID))
                    {
                        setPageMarginRight(attrLayout, attr);
                    }
                    else
                    {
                        setPageMarginRight(attrMaster, attr);
                    }
                    
                    if (AttrManage.instance().hasAttribute(attrLayout, AttrIDConstant.PAGE_TOP_ID))
                    {
                        setPageMarginTop(attrLayout, attr);
                    }
                    else
                    {
                        setPageMarginTop(attrMaster, attr);
                    }
                    
                    if (AttrManage.instance().hasAttribute(attrLayout, AttrIDConstant.PAGE_BOTTOM_ID))
                    {
                        setPageMarginBottom(attrLayout, attr);
                    }
                    else
                    {
                        setPageMarginBottom(attrMaster, attr);
                    }
                    if (AttrManage.instance().hasAttribute(attrLayout, AttrIDConstant.PAGE_VERTICAL_ID))
                    {
                        setPageVerticalAlign(attrLayout, attr);
                    }
                    else
                    {
                        setPageVerticalAlign(attrMaster, attr);
                    }
                }
            }
            return attr;
        }
        return null;
    }
    
    /**
     * 首先取slide 里的值，没有取 layout里的值，最后才取 master里的值
     * set section attribute
     */
    public void setSectionAttribute(Element bodyPr, IAttributeSet attr, IAttributeSet attrLayout, 
        IAttributeSet attrMaster, boolean table)
    {
        byte verAlign = WPAttrConstant.PAGE_V_TOP;
        //byte horAlign = WPAttrConstant.PAGE_H_LEFT;
        /*if (table)
        {
            verAlign = WPAttrConstant.PAGE_V_CENTER;
        }*/
        
        IAttributeSet attrStyle = getDefautSectionAttr(attrLayout, attrMaster);
        if (bodyPr != null)
        {
            String val;
            // 左边距
            if (bodyPr.attribute("lIns") != null)
            {
                val = bodyPr.attributeValue("lIns");
                if (val != null && val.length() > 0)
                {
                    int value = (int)(Integer.parseInt(val) 
                        * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH * MainConstant.POINT_TO_TWIPS);
                    AttrManage.instance().setPageMarginLeft(attr, value);
                }
            }
            else
            {
                setPageMarginLeft(attrStyle, attr);
            }
            
            // 右边距
            if (bodyPr.attribute("rIns") != null)
            {
                val = bodyPr.attributeValue("rIns");
                if (val != null && val.length() > 0)
                {
                    int value = (int)(Integer.parseInt(val) 
                        * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH * MainConstant.POINT_TO_TWIPS);
                    AttrManage.instance().setPageMarginRight(attr, value);
                }
            }
            else
            {
                setPageMarginRight(attrStyle, attr);
            }
            
            // 上边距
            if (bodyPr.attribute("tIns") != null)
            {
                val = bodyPr.attributeValue("tIns");
                if (val != null && val.length() > 0)
                {
                    int value = (int)(Integer.parseInt(val) 
                        * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH * MainConstant.POINT_TO_TWIPS);
                    AttrManage.instance().setPageMarginTop(attr, value);
                }
            }
            else
            {
                setPageMarginTop(attrStyle, attr);
            }
            
            // 下边距
            if (bodyPr.attribute("bIns") != null)
            {
                val = bodyPr.attributeValue("bIns");
                if (val != null && val.length() > 0)
                {
                    int value = (int)(Integer.parseInt(val) 
                        * MainConstant.POINT_DPI / MainConstant.EMU_PER_INCH * MainConstant.POINT_TO_TWIPS);
                    AttrManage.instance().setPageMarginBottom(attr, value);
                }
            }
            else
            {
                setPageMarginBottom(attrStyle, attr);
            }   
            
            //alignment in vertical
            if ((val = bodyPr.attributeValue("anchor")) != null)
            {
                if(val.equals("t"))
                {
                    verAlign = WPAttrConstant.PAGE_V_TOP;
                }
                else if(val.equals("ctr"))
                {
                    verAlign = WPAttrConstant.PAGE_V_CENTER;
                }
                else if(val.equals("b"))
                {
                    verAlign = WPAttrConstant.PAGE_V_BOTTOM;
                }
                else if(val.equals("just"))
                {
                    verAlign = WPAttrConstant.PAGE_V_CENTER;
                }
                else if(val.equals("dist"))
                {
                    verAlign = WPAttrConstant.PAGE_V_CENTER;
                }
                AttrManage.instance().setPageVerticalAlign(attr, verAlign);
            }
            else
            {
                setPageVerticalAlign(attrStyle, attr);
            }
            
            //alignment in horizontal
            if ((val = bodyPr.attributeValue("anchorCtr")) != null)
            {
                if(val.equals("1"))
                {
                    AttrManage.instance().setPageHorizontalAlign(attr, WPAttrConstant.PAGE_H_CENTER);    
                }
            }
            else
            {
                if (attrStyle != null)
                {
                    if (AttrManage.instance().hasAttribute(attrStyle, AttrIDConstant.PAGE_HORIZONTAL_ID))
                    {
                        AttrManage.instance().setPageHorizontalAlign(attr, 
                            AttrManage.instance().getPageHorizontalAlign(attrStyle));
                    }
                }
            }
            
        }
        else if (attrStyle != null)
        {
            setPageMarginLeft(attrStyle, attr);
            setPageMarginRight(attrStyle, attr);
            setPageMarginTop(attrStyle, attr);
            setPageMarginBottom(attrStyle, attr);
            setPageVerticalAlign(attrStyle, attr);
        }
        
        // set default value
        if (RunAttr.instance().isSlide())
        {
            /*if (!AttrManage.instance().hasAttribute(attr, AttrIDConstant.PAGE_VERTICAL_ID))
            {
                AttrManage.instance().setPageVerticalAlign(attr, verAlign);
            }*/
            if (!AttrManage.instance().hasAttribute(attr, AttrIDConstant.PAGE_LEFT_ID))
            {
                if (table)
                {
                    AttrManage.instance().setPageMarginLeft(attr, DEFAULT_TABLE_MARGIN);
                }
                else
                {
                    AttrManage.instance().setPageMarginLeft(attr, DEFAULT_MARGIN_LEFT_RIGHT);
                }
            }
            if (!AttrManage.instance().hasAttribute(attr, AttrIDConstant.PAGE_RIGHT_ID))
            {
                if (table)
                {
                    AttrManage.instance().setPageMarginRight(attr, DEFAULT_TABLE_MARGIN);
                }
                else
                {
                    AttrManage.instance().setPageMarginRight(attr, DEFAULT_MARGIN_LEFT_RIGHT);
                }
            }
            if (!AttrManage.instance().hasAttribute(attr, AttrIDConstant.PAGE_TOP_ID))
            {
                if (table)
                {
                    AttrManage.instance().setPageMarginTop(attr, DEFAULT_TABLE_MARGIN);
                }
                else
                {
                    AttrManage.instance().setPageMarginTop(attr, DEFAULT_MARGIN_TOP_BOTTOM);
                }
            }
            if (!AttrManage.instance().hasAttribute(attr, AttrIDConstant.PAGE_BOTTOM_ID))
            {
                if (table)
                {
                    AttrManage.instance().setPageMarginBottom(attr, 0);
                }
                else
                {
                    AttrManage.instance().setPageMarginBottom(attr, DEFAULT_MARGIN_TOP_BOTTOM);
                }
            }
        }
    }
}
