/*
 * 文件名称:           PPTXmlKit.java
 *  
 * 编译器:             android2.2
 * 时间:               上午9:18:41
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import java.util.List;

import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.pg.model.PGPlaceholderUtil;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;

import android.graphics.Color;

/**
 * 解析xml
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-2-21
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ReaderKit
{
    private static ReaderKit kit = new ReaderKit();
    
    /**
     * 
     */
    public static ReaderKit instance()
    {
        return kit;
    }
    
    /**
     * get place holder name
     */
    public String getPlaceholderName(Element sp)
    {
        if (sp != null)
        {
            Element temp = null;
            String name = sp.getName();
            if (name.equals("sp"))
            {
                temp = sp.element("nvSpPr");
            }
            else if (name.equals("pic"))
            {
                temp = sp.element("nvPicPr");
            }
            else if (name.equals("graphicFrame"))
            {
                temp = sp.element("nvGraphicFramePr");
            }
            else if (name.equals("grpSp"))
            {
                temp = sp.element("nvGrpSpPr");
            }
            if (temp != null)
            {
                Element cNvPr = temp.element("cNvPr");
                if (cNvPr != null && cNvPr.attribute("name") != null)
                {
                    return cNvPr.attributeValue("name");
                }
            }
        }
        return null;
    }
    
    /**
     * get place holder type
     */
    public String getPlaceholderType(Element sp)
    {
        if (sp != null)
        {
            Element temp = null;
            String name = sp.getName();
            if (name.equals("sp"))
            {
                temp = sp.element("nvSpPr");
            }
            else if (name.equals("pic"))
            {
                temp = sp.element("nvPicPr");
            }
            else if (name.equals("graphicFrame"))
            {
                temp = sp.element("nvGraphicFramePr");
            }
            else if (name.equals("grpSp"))
            {
                temp = sp.element("nvGrpSpPr");
            }
            if (temp != null)
            {
                Element nvPr = temp.element("nvPr");
                if (nvPr != null)
                {
                    Element ph = nvPr.element("ph");
                    if (ph != null && ph.attribute("type") != null)
                    {
                        return ph.attributeValue("type");
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * get place holder index
     */
    public int getPlaceholderIdx(Element sp)
    {
        if (sp != null)
        {
            Element temp = null;
            String name = sp.getName();
            if (name.equals("sp"))
            {
                temp = sp.element("nvSpPr");
            }
            else if (name.equals("pic"))
            {
                temp = sp.element("nvPicPr");
            }
            else if (name.equals("graphicFrame"))
            {
                temp = sp.element("nvGraphicFramePr");
            }
            else if (name.equals("grpSp"))
            {
                temp = sp.element("nvGrpSpPr");
            }
            if (temp != null)
            {
                Element nvPr = temp.element("nvPr");
                if (nvPr != null)
                {
                    Element ph = nvPr.element("ph");
                    if (ph != null && ph.attributeValue("idx") != null)
                    {
                        return (int)Double.parseDouble(ph.attributeValue("idx"));
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * get shape anchor
     */
    public Rectangle getShapeAnchor(Element xfrm, float zoomX, float zoomY)
    {
        if (xfrm != null)
        {
            String val;
            Rectangle rect = new Rectangle();
            Element off = xfrm.element("off");
            if (off != null)
            {
                if (off.attribute("x") != null)
                {
                    val = off.attributeValue("x");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.x = (int)(Integer.parseInt(val) * zoomX 
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.x = (int)(Integer.parseInt(val, 16) * zoomX
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
                if (off.attribute("y") != null)
                {
                    val = off.attributeValue("y");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.y = (int)(Integer.parseInt(val) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.y = (int)(Integer.parseInt(val, 16) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
            }
            Element ext = xfrm.element("ext");
            if (ext != null)
            {
                if (ext.attribute("cx") != null)
                {
                    val = ext.attributeValue("cx");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.width = (int)(Integer.parseInt(val) * zoomX
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.width = (int)(Integer.parseInt(val, 16) * zoomX
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
                if (ext.attributeValue("cy") != null)
                {
                    val = ext.attributeValue("cy");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.height = (int)(Integer.parseInt(val) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.height = (int)(Integer.parseInt(val, 16) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
            }
            return rect;
        }
        return null;
    }
    
    /**
     * get shape anchor
     */
    public Rectangle getChildShapeAnchor(Element xfrm, float zoomX, float zoomY)
    {
        if (xfrm != null)
        {
            String val;
            Rectangle rect = new Rectangle();
            Element off = xfrm.element("chOff");
            if (off != null)
            {
                if (off.attribute("x") != null)
                {
                    val = off.attributeValue("x");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.x = (int)(Integer.parseInt(val) * zoomX 
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.x = (int)(Integer.parseInt(val, 16) * zoomX
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
                if (off.attribute("y") != null)
                {
                    val = off.attributeValue("y");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.y = (int)(Integer.parseInt(val) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.y = (int)(Integer.parseInt(val, 16) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
            }
            Element ext = xfrm.element("chExt");
            if (ext != null)
            {
                if (ext.attribute("cx") != null)
                {
                    val = ext.attributeValue("cx");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.width = (int)(Integer.parseInt(val) * zoomX
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.width = (int)(Integer.parseInt(val, 16) * zoomX
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
                if (ext.attributeValue("cy") != null)
                {
                    val = ext.attributeValue("cy");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            rect.height = (int)(Integer.parseInt(val) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                        else
                        {
                            rect.height = (int)(Integer.parseInt(val, 16) * zoomY
                                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                        }
                    }
                }
            }
            return rect;
        }
        return null;
    }
    
    /**
     * get note
     */
    public String getNotes(Element root)
    {
        Element cSld = root.element("cSld");
        if (cSld != null)
        {
            Element spTree = cSld.element("spTree");
            if (spTree != null)
            {
                List<Element> sps = spTree.elements("sp");
                for (Element sp : sps)
                {
                    String type = getPlaceholderType(sp);
                    if (PGPlaceholderUtil.BODY.equals(type))
                    {
                        String notes = "";
                        Element txBody = sp.element("txBody");
                        if (txBody != null)
                        {
                            List<Element> ps = txBody.elements("p");
                            for (Element p : ps)
                            {
                                List<Element> rs = p.elements("r");
                                for (Element r : rs)
                                {
                                    Element t = r.element("t");
                                    if (t != null)
                                    {
                                        String text = t.getText();
                                        notes += text;
                                    }
                                }
                                notes += '\n';
                            }
                        }
                        String txt = notes.trim();
                        if (txt.length() > 0)
                        {
                            return txt;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public int getColor(PGMaster master, Element solidFill)
    {
        return getColor(master, solidFill, false);
    }
    
    private int  processColorAttribute(Element colorE, int color, boolean isTableStyle)
    {
    	if (colorE.element("tint") != null)
        {
            if(isTableStyle)
            {
                color = ColorUtil.instance().getColorWithTint(color, 
                    1 - Integer.parseInt(colorE.element("tint").attributeValue("val")) / 100000.0);
            }
            else
            {
                color = ColorUtil.instance().getColorWithTint(color, 
                    Integer.parseInt(colorE.element("tint").attributeValue("val")) / 100000.0);
            }
            
        }
        else if (colorE.element("lumOff") != null)
        {
            color = ColorUtil.instance().getColorWithTint(color, 
                Integer.parseInt(colorE.element("lumOff").attributeValue("val")) / 100000.0);
        }
        else if (colorE.element("lumMod") != null)
        {
            color = ColorUtil.instance().getColorWithTint(color, 
                Integer.parseInt(colorE.element("lumMod").attributeValue("val")) / 100000.0 - 1);
        }
        else if (colorE.element("shade") != null)
        {
            color = ColorUtil.instance().getColorWithTint(color, 
                -Integer.parseInt(colorE.element("shade").attributeValue("val")) / 200000.0);
        }
        
        if(colorE.element("alpha") != null)
        {
            String val = colorE.element("alpha").attributeValue("val");
            if(val != null)
            {
                int alpha = (int)(Integer.parseInt(val) / 100000f * 255);
                color = (0xFFFFFF & color) | (alpha << 24);
            }
        }
        return color;
    }
    
    /**
     * get color
     */
    public int getColor(PGMaster master, Element solidFill, boolean isTableStyle)
    {
        if (solidFill != null)
        {
            String val;
            Element temp = solidFill.element("srgbClr");
            if (temp != null && temp.attribute("val") != null)
            {
                val = temp.attributeValue("val");
                if (val != null && val.length() > 0)
                {
                    return  processColorAttribute(temp, Color.parseColor("#" + val), isTableStyle);
                }
            }
            else if((temp = solidFill.element("scrgbClr")) != null)
            {
                int r = Integer.parseInt(temp.attributeValue("r")) * 255 / 100;
                int g = Integer.parseInt(temp.attributeValue("g")) * 255 / 100;
                int b = Integer.parseInt(temp.attributeValue("b")) * 255 / 100;
                return processColorAttribute(temp, ColorUtil.rgb(r, g, b), isTableStyle);
            }
            else if ((temp = solidFill.element("schemeClr")) != null && temp.attribute("val") != null)
            {
                val = temp.attributeValue("val");
                if (val != null && val.length() > 0)
                {
                    int color = -1;
                    if(master != null)
                    {
                        color = master.getColor(val);
                    }
                    
                    return processColorAttribute(temp, color, isTableStyle);
                }
            }
            else if((temp = solidFill.element("sysClr")) != null)
            {
                val = temp.attributeValue("lastClr");
                if (val != null && val.length() > 0)
                {
                    return  Color.parseColor("#" + val);
                }
            }
            else if((temp = solidFill.element("prstClr")) != null)
            {
                val = temp.attributeValue("val");
                if (val.contains("gray"))
                {
                    return Color.GRAY;
                }
                else if (val.contains("white"))
                {
                    return Color.WHITE;
                }
                else if (val.contains("red"))
                {
                    return Color.RED;
                }
                else if (val.contains("green"))
                {
                    return Color.GREEN;
                }
                else if (val.contains("blue"))
                {
                    return Color.BLUE;
                }
                else if (val.contains("yellow"))
                {
                    return Color.YELLOW;
                }
                else if (val.contains("cyan"))
                {
                    return Color.CYAN;
                }
                else
                {
                    return Color.BLACK;
                }
            }
        }
        return Color.WHITE;
    }
    
    /**
     * 
     * @param num
     * @return
     */
    public boolean isDecimal(String num)
    {
        String hexChars = "abcdefABCDEF";
        int len = hexChars.length();
        int index = 0;
        while(index < len)
        {
            if(num.indexOf(hexChars.charAt(index)) > -1)
            {
                return false;
            }
            index++;
        }
        return true;
    }
    
    /**
     * 
     */
    public boolean isUserDrawn(Element sp)
    {
        Element temp = null;
        String name = sp.getName();
        if (name.equals("sp"))
        {
            temp = sp.element("nvSpPr");
        }
        else if (name.equals("pic"))
        {
            temp = sp.element("nvPicPr");
        }
        else if (name.equals("graphicFrame"))
        {
            temp = sp.element("nvGraphicFramePr");
        }
        else if (name.equals("grpSp"))
        {
            temp = sp.element("nvGrpSpPr");
        }
        if (temp != null)
        {
            Element nvPr = temp.element("nvPr");
            if (nvPr != null)
            {
                Element ph = nvPr.element("ph");
                if (ph == null /*|| (ph != null && ph.attribute("type") == null && ph.attribute("idx") == null)*/)
                {
                    return true;
                }
                else if (nvPr.attribute("userDrawn") != null)
                {
                    String val = nvPr.attributeValue("userDrawn");
                    if ((val != null && val.length() > 0 && Integer.parseInt(val) > 0))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * get shape anchor zoom
     */
    public float[] getAnchorFitZoom(Element xfrm)
    {
        float[] zoom = {1.0f, 1.0f};
        if (xfrm != null)
        {
            String val;
            float grpWidth = 0;
            float grpHeight = 0;
            float childWidth = 0;
            float childHeight = 0;
            Element ext = xfrm.element("ext");
            if (ext != null)
            {
                if (ext.attribute("cx") != null)
                {
                    val = ext.attributeValue("cx");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            grpWidth = Integer.parseInt(val);
                        }
                        else
                        {
                            grpWidth = Integer.parseInt(val, 16);
                        }
                    }
                }
                if (ext.attributeValue("cy") != null)
                {
                    val = ext.attributeValue("cy");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            grpHeight = Integer.parseInt(val);
                        }
                        else
                        {
                            grpHeight = Integer.parseInt(val, 16);
                        }
                    }
                }
            }
            ext = xfrm.element("chExt");
            if (ext != null)
            {
                if (ext.attribute("cx") != null)
                {
                    val = ext.attributeValue("cx");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            childWidth = Integer.parseInt(val);
                        }
                        else
                        {
                            childWidth = Integer.parseInt(val, 16);
                        }
                    }
                }
                if (ext.attributeValue("cy") != null)
                {
                    val = ext.attributeValue("cy");
                    if (val != null && val.length() > 0)
                    {
                        if(isDecimal(val))
                        {
                            childHeight = Integer.parseInt(val);
                        }
                        else
                        {
                            childHeight = Integer.parseInt(val, 16);
                        }
                    }
                }
            }
            if (childWidth != 0 && childHeight != 0)
            {
                zoom[0] = grpWidth / childWidth;
                zoom[1] =  grpHeight / childHeight;
            }
        }
        return zoom;
    }
    
    /**
     * for shape property element
     * @param spPr
     * @param shape
     */
    public void processRotation(Element spPr, IShape shape)
    {
        if (spPr != null)
        {
            processRotation(shape, spPr.element("xfrm"));
        }
    }
    
    /**
     * for xfrm element
     * @param shape
     * @param xfrm
     */
    public void processRotation(IShape shape, Element xfrm)
    {
        if (xfrm != null)
        {
            String val;
            if (xfrm.attribute("flipH") != null)
            {
                val = xfrm.attributeValue("flipH");
                if (val != null && val.length() > 0 && Integer.parseInt(val) == 1)
                {
                    shape.setFlipHorizontal(true);
                }
            }
            if (xfrm.attribute("flipV") != null)
            {
                val = xfrm.attributeValue("flipV");
                if (val != null && val.length() > 0 && Integer.parseInt(val) == 1)
                {
                    shape.setFlipVertical(true);
                }
            }
            if (xfrm.attribute("rot") != null)
            {
                val = xfrm.attributeValue("rot");
                if (val != null && val.length() > 0)
                {
                    shape.setRotation(Float.parseFloat(val) / 60000);
                }
            }
        }
    }
    
    /**
     * 
     */
    public boolean isHidden(Element sp)
    {
        Element temp = null;
        String name = sp.getName();
        if (name.equals("sp"))
        {
            temp = sp.element("nvSpPr");
        }
        else if (name.equals("pic"))
        {
            temp = sp.element("nvPicPr");
        }
        else if (name.equals("graphicFrame"))
        {
            temp = sp.element("nvGraphicFramePr");
        }
        else if (name.equals("grpSp"))
        {
            temp = sp.element("nvGrpSpPr");
        }
        if (temp != null)
        {
            Element cNvPr = temp.element("cNvPr");
            if (cNvPr != null && cNvPr.attribute("hidden") != null
                && Integer.parseInt(cNvPr.attributeValue("hidden")) > 0)
            {
                return true;
            }
        }
        return false;
    }
}
