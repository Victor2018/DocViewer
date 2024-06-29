/*
 * 文件名称:           AutoShapeDataKit.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:36:49
 */
package com.nvqquy98.lib.doc.office.common.autoshape;

import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.TileShader;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureStretchInfo;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.ArbitraryPolygonShape;
import com.nvqquy98.lib.doc.office.common.shape.Arrow;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.LineShape;
import com.nvqquy98.lib.doc.office.common.shape.PictureShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.common.shape.WPAutoShape;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.LineKit;
import com.nvqquy98.lib.doc.office.fc.ShaderKit;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.ReaderKit;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Color;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-4-8
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AutoShapeDataKit
{
    /**
     * 
     * @param schemeColor
     * @param solidFillElement
     * @return
     */
    public static int getColor(Map<String, Integer> schemeColor, Element solidFillElement)
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
        else if((clr = solidFillElement.element("scrgbClr")) != null)
        {
            int r = Integer.parseInt(clr.attributeValue("r")) * 255 / 100;
            int g = Integer.parseInt(clr.attributeValue("g")) * 255 / 100;
            int b = Integer.parseInt(clr.attributeValue("b")) * 255 / 100;
            return ColorUtil.rgb(r, g, b);
        }
        else if(solidFillElement.element("schemeClr") != null
            || solidFillElement.element("prstClr") != null)
        {
            if (schemeColor != null && schemeColor.size() > 0)
            {
                clr = solidFillElement.element("schemeClr");
                if (clr == null)
                {
                    clr = solidFillElement.element("prstClr");
                }
                val = clr.attributeValue("val");
                if("black".equals(val))
                {
                    color = Color.BLACK;
                }
                else if ("red".equals(val))
                {
                    color = Color.RED;
                }
                else if ("gray".equals(val))
                {
                    color = Color.GRAY;
                }
                else if ("blue".equals(val))
                {
                    color = Color.BLUE;
                }
                else if ("green".equals(val))
                {
                    color = Color.GREEN;
                }
                //get scheme color
                if (color == -1)
                {
                    color = schemeColor.get(val);   
                }
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
     * 
     * @param control
     * @param zipPackage
     * @param drawingPart
     * @param bgPr
     * @param schemeColor
     * @return
     * @throws Exception
     */
    public static BackgroundAndFill processBackground(IControl control, ZipPackage zipPackage, PackagePart  drawingPart, 
        Element bgPr, Map<String, Integer> schemeColor)
    {
    	try
    	{
    		if (bgPr != null)
            {
                BackgroundAndFill bgFill = new BackgroundAndFill();
                Element fill = bgPr.element("solidFill");
                if (fill != null)
                {
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                    bgFill.setForegroundColor(getColor(schemeColor, fill));
                    return bgFill;
                }
                // picture
                else if ((fill = bgPr.element("blipFill")) != null)
                {
                    Element blip = fill.element("blip");
                    if (blip != null && blip.attribute("embed") != null)
                    {
                        String id = blip.attributeValue("embed");
                        if (id != null)
                        {
                            PackageRelationship imageShip = drawingPart.getRelationship(id);
                            if (imageShip != null)
                            {
                                PackagePart picPart = zipPackage.getPart(imageShip.getTargetURI());
                                if (picPart != null)
                                {  
                                	Element tile = fill.element("tile");
                                	if(tile == null)
                                	{
                                		bgFill.setFillType(BackgroundAndFill.FILL_PICTURE);
                                		Element stretch = fill.element("stretch");
                                		if(stretch != null)
                                		{
                                			Element fillRect = stretch.element("fillRect");
                                			if(fillRect != null)
                                			{
                                				PictureStretchInfo stretchInfo = new PictureStretchInfo();
                                				boolean validate = false;
                                    			String str = fillRect.attributeValue("l");
                                    			if(str != null)
                                    			{
                                    				validate = true;
                                    				stretchInfo.setLeftOffset(Float.parseFloat(str) / 100000);
                                    			}
                                    			
                                    			str = fillRect.attributeValue("r");
                                    			if(str != null)
                                    			{
                                    				validate = true;
                                    				stretchInfo.setRightOffset(Float.parseFloat(str) / 100000);
                                    			}
                                    			
                                    			str = fillRect.attributeValue("t");
                                    			if(str != null)
                                    			{
                                    				validate = true;
                                    				stretchInfo.setTopOffset(Float.parseFloat(str) / 100000);
                                    			}
                                    			
                                    			str = fillRect.attributeValue("b");
                                    			if(str != null)
                                    			{
                                    				validate = true;
                                    				stretchInfo.setBottomOffset(Float.parseFloat(str) / 100000);
                                    			}
                                    			
                                    			if(validate)
                                    			{
                                    				bgFill.setStretch(stretchInfo);
                                    			}                                			
                                			}
                                		}
                                        bgFill.setPictureIndex(control.getSysKit().getPictureManage().addPicture(picPart));
                                	}
                                	else
                                	{
                                		int index = control.getSysKit().getPictureManage().addPicture(picPart);
                                		bgFill.setFillType(BackgroundAndFill.FILL_SHADE_TILE);
                                		TileShader tileShader = ShaderKit.readTile(control.getSysKit().getPictureManage().getPicture(index), tile);
                                		Element alphaModFix = blip.element("alphaModFix");
                                		if(alphaModFix != null)
                                		{
                                			String amt = alphaModFix.attributeValue("amt");
                                			if(amt != null)
                                			{
                                				tileShader.setAlpha(Math.round(Integer.parseInt(amt) / 100000.f * 255));
                                			}
                                		}
                                		
                                        bgFill.setShader(tileShader);
                                	}
                                    return bgFill;
                                }
                            }
                        }
                    }
                }
                else if ((fill = bgPr.element("gradFill")) != null)
                {
                    Element gsLst = fill.element("gsLst");
                    {
                    	bgFill.setFillType(ShaderKit.getGradientType(fill));
                        bgFill.setShader(ShaderKit.readGradient(schemeColor, fill));
                        return bgFill;
                    }
                }
                else if ((fill = bgPr.element("fillRef")) != null)
                {
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                    bgFill.setForegroundColor(getColor(schemeColor, fill));
                    return bgFill;
                }
                else if ((fill = bgPr.element("pattFill")) != null)
                {
                    Element bgClr = fill.element("bgClr");
                    {
                        bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                        bgFill.setForegroundColor(getColor(schemeColor, bgClr));
                        return bgFill;
                    }
                }
            }
            return null;
    	}
        catch(Exception e)
        {
        	return null;
        }
    }
    
    public static AbstractShape getAutoShape(IControl control, ZipPackage zipPackage, PackagePart  drawingPart, 
            Element sp, Rectangle rect, Map<String, Integer> schemeColor, int type) throws Exception
    {
    	return getAutoShape(control, zipPackage,  drawingPart,sp, rect, schemeColor, type, false);
    }
    
    /**
     * 
     * @param control
     * @param zipPackage
     * @param drawingPart
     * @param sp
     * @param rect
     * @param schemeColor
     * @param type
     * @param hasTextbox
     * @return
     * @throws Exception
     */
    public static AbstractShape getAutoShape(IControl control, ZipPackage zipPackage, PackagePart  drawingPart, 
        Element sp, Rectangle rect, Map<String, Integer> schemeColor, int type, boolean hasTextbox) throws Exception
    {
        if (rect == null || sp == null)
        {
            return null;
        }
        
        int shapeType = ShapeTypes.ArbitraryPolygon;
        Element spPr = sp.element("spPr");
        if (spPr != null)
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
            else if (name != null)
            {
                if (name.contains("Text Box") || name.contains("TextBox"))
                {
                    shapeType = ShapeTypes.Rectangle;
                }
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
                    if (gds.size() > 0)
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
            
            BackgroundAndFill fill = null;
            if (fill == null && spPr.element("noFill") == null && !spName.equals("cxnSp"))
            {
                fill = processBackground(control, zipPackage, drawingPart, spPr, schemeColor);
                if (fill == null && shapeType != ShapeTypes.Arc && shapeType != ShapeTypes.BracketPair 
                    && shapeType != ShapeTypes.LeftBracket && shapeType != ShapeTypes.RightBracket
                    && shapeType != ShapeTypes.BracePair && shapeType != ShapeTypes.LeftBrace
                    && shapeType != ShapeTypes.RightBrace && shapeType != ShapeTypes.ArbitraryPolygon)
                {
                    fill = processBackground(control, zipPackage, drawingPart, sp.element("style"), schemeColor);
                }
            }
            
            Line line = LineKit.createShapeLine(control, zipPackage, drawingPart, sp, schemeColor);
            
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
            
            if(shapeType != ShapeTypes.Line && shapeType != ShapeTypes.StraightConnector1
        			&& rect != null && (rect.width == 0 || rect.height == 0))
        	{
        		return null;
        	}
            
            // lineShape or autoShape
            if (shapeType == ShapeTypes.Line || shapeType == ShapeTypes.StraightConnector1
                || shapeType == ShapeTypes.BentConnector3 || shapeType == ShapeTypes.CurvedConnector3)
            {
                LineShape lineShape = null;
                if (type == MainConstant.APPLICATION_TYPE_WP)
                {
                    lineShape = new WPAutoShape();
                }
                else
                {
                    lineShape = new LineShape();
                }
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
                            lineShape.createEndArrow(arrowType, 
                                Arrow.getArrowSize(temp.attributeValue("w")), 
                                Arrow.getArrowSize(temp.attributeValue("len")));
                        }
                    }
                }
                ReaderKit.instance().processRotation(spPr, lineShape);
                return lineShape;
            }
            else if(shapeType == ShapeTypes.ArbitraryPolygon)
            {
                ArbitraryPolygonShape arbitraryPolygonShape = null;
                if (type == MainConstant.APPLICATION_TYPE_WP)
                {
                    arbitraryPolygonShape = new WPAutoShape();
                }
                else
                {
                    arbitraryPolygonShape = new ArbitraryPolygonShape();
                }
                
                BackgroundAndFill lineFill = null;
                if(line != null)
                {
                	lineFill = line.getBackgroundAndFill();
                }
                ArbitraryPolygonShapePath.processArbitraryPolygonShape(arbitraryPolygonShape, sp, fill, border, lineFill, ln, rect);

                arbitraryPolygonShape.setShapeType(shapeType);
                arbitraryPolygonShape.setLine(line);
                ReaderKit.instance().processRotation(spPr, arbitraryPolygonShape);
                return arbitraryPolygonShape;
            }
            else if (hasTextbox || fill != null || border)
            {
                AutoShape autoShape = null;
                if (type == MainConstant.APPLICATION_TYPE_WP)
                {
                    autoShape = new WPAutoShape();
                    autoShape.setShapeType(shapeType);
                }
                else
                {
                    autoShape = new AutoShape(shapeType);
                }
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
                ReaderKit.instance().processRotation(spPr, autoShape);
                
                return autoShape;
            }
        }
        return null;
    }
    
    public static void processPictureShape(IControl control, ZipPackage zipPackage, PackagePart  drawingPart, 
            Element bgPr, Map<String, Integer> schemeColor, PictureShape shape)
    {
    	if (shape == null)
        {
            return;
        }
        
        if (bgPr != null)
        {
        	BackgroundAndFill fill = 
        			AutoShapeDataKit.processBackground(control, zipPackage, drawingPart, 
        					bgPr, schemeColor);
        	
        	shape.setBackgroundAndFill(fill);
        	Line line = LineKit.createLine(control, zipPackage, drawingPart, bgPr.element("ln"), schemeColor);
            shape.setLine(line);
         }
    }
}
