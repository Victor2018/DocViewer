/*
 * 文件名称:           MasterData.java
 *  
 * 编译器:             android2.2
 * 时间:               上午9:07:18
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.ParaAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.RunAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.SectionAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.bulletnumber.BulletNumberManage;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.pg.model.PGStyle;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttributeSetImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.Style;
import com.nvqquy98.lib.doc.office.simpletext.model.StyleManage;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * shape layout, margin, text style
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-2-29
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class StyleReader
{   
    private static StyleReader style = new StyleReader();
    
    /**
     * 
     */
    public static StyleReader instance()
    {
        return style;
    }
    
    /**
     * get PGStyles
     */
    public PGStyle getStyles(IControl control, PGMaster pgMaster, Element sp, Element style)
    {
        PGStyle pgStyle = new PGStyle();
        processSp(pgStyle, sp);
        processStyle(control, pgStyle, pgMaster, style);
        return pgStyle;
    }
 
    /**
     *  shape layout, margin
     */
    private void processSp(PGStyle pgStyle, Element sp)
    {
        if (sp != null)
        {
            // anchor
            Element spPr = sp.element("spPr");
            if (spPr != null)
            {
                pgStyle.setAnchor(ReaderKit.instance().getShapeAnchor(spPr.element("xfrm"), 1.0f, 1.0f));
            }
            // section attribute
            Element txBody = sp.element("txBody");
            if (txBody != null)
            {
                Element bodyPr = txBody.element("bodyPr");
                if (bodyPr != null)
                {
                    IAttributeSet attr = new AttributeSetImpl();
                    SectionAttr.instance().setSectionAttribute(bodyPr, attr, null, null, false);
                    pgStyle.setSectionAttr(attr);
                }
            }
        }
    }
    
    /**
     * reader style
     */
    private void processStyle(IControl control, PGStyle pgStyle, PGMaster pgMaster, Element style)
    {
        if (style != null)
        {
            Element lvl1pPr = style.element("lvl1pPr");
            if (lvl1pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl1pPr, 1);
            }
            Element lvl2pPr = style.element("lvl2pPr");
            if (lvl2pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl2pPr, 2);
            }
            Element lvl3pPr = style.element("lvl3pPr");
            if (lvl3pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl3pPr, 3);
            }
            Element lvl4pPr = style.element("lvl4pPr");
            if (lvl4pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl4pPr, 4);
            }
            Element lvl5pPr = style.element("lvl5pPr");
            if (lvl5pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl5pPr, 5);
            }
            Element lvl6pPr = style.element("lvl6pPr");
            if (lvl6pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl6pPr, 6);
            }
            Element lvl7pPr = style.element("lvl7pPr");
            if (lvl7pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl7pPr, 7);
            }
            Element lvl8pPr = style.element("lvl8pPr");
            if (lvl8pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl8pPr, 8);
            }
            Element lvl9pPr = style.element("lvl9pPr");
            if (lvl9pPr != null)
            {
                processStyleAttribute(control, pgStyle, pgMaster, lvl9pPr, 9);
            }
        }
    }

    /**
     * set sytle attribute
     */
    private void processStyleAttribute(IControl control, PGStyle pgStyle, PGMaster pgMaster, Element paraStyle, int lvl)
    {
        Style style = new Style();
        style.setId(index);
        style.setType((byte)0);
        // paragraph attribute set
        ParaAttr.instance().setParaAttribute(control, paraStyle, style.getAttrbuteSet(), null, -1, -1, 0, false, false);
        Element defRPr = paraStyle.element("defRPr");        
        // character attribute set
        RunAttr.instance().setRunAttribute(pgMaster, defRPr, style.getAttrbuteSet(), null, 100, -1, false);
        RunAttr.instance().setMaxFontSize(AttrManage.instance().getFontSize(style.getAttrbuteSet(), 
            style.getAttrbuteSet()));
        // 处理以行为单位的段前段后
        ParaAttr.instance().processParaWithPct(paraStyle, style.getAttrbuteSet());
        RunAttr.instance().resetMaxFontSize();
        StyleManage.instance().addStyle(style);
        
        pgStyle.addStyle(lvl, index);
        BulletNumberManage.instance().addBulletNumber(control, index, paraStyle);
        index++;
    }
    
    /**
     * 获取下次设置 style index 的开始值
     */
    public int getStyleIndex()
    {
        return index;
    }
    
    /**
     * 设置 style index 的开始值
     */
    public void setStyleIndex(int index)
    {
        this.index = index;
    }
    
    //
    private int index;
}
