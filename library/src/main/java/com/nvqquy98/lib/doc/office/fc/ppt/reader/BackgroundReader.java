/*
 * 文件名称:           BackgroundReader.java
 *  
 * 编译器:             android2.2
 * 时间:               下午5:16:06
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.TileShader;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureStretchInfo;
import com.nvqquy98.lib.doc.office.fc.ShaderKit;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * 解析 background
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-3-2
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class BackgroundReader
{
    private static BackgroundReader bgReader = new BackgroundReader();
    
    /**
     * 
     */
    public static BackgroundReader instance()
    {
        return bgReader;
    }
    
    /**
     * get background
     * @throws Exception 
     */
    public BackgroundAndFill getBackground(IControl control, ZipPackage zipPackage, 
        PackagePart packagePart, PGMaster master, Element bg) throws Exception
    {
        if (bg != null)
        {
            Element bgPr = bg.element("bgPr");
            Element bgRef = bg.element("bgRef");
            if (bgRef != null)
            { 
                BackgroundAndFill bgFill = new BackgroundAndFill();
                bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                bgFill.setForegroundColor(ReaderKit.instance().getColor(master, bgRef));
                return bgFill;
            }
            else
            {
                return processBackground(control, zipPackage, packagePart, master, bgPr);
            }
        }
        return null;
    }
    
    /**
     * 
     * @param zipPackage
     * @param packagePart
     * @param master
     * @param bgPr
     * @return
     * @throws Exception
     */
    public BackgroundAndFill processBackground(IControl control, ZipPackage zipPackage, PackagePart packagePart, 
        PGMaster master, Element bgPr) throws Exception
    {
        return processBackground(control, zipPackage, packagePart, master, bgPr, false);
    }
    /**
     * 
     * @param zipPackage
     * @param packagePart
     * @param master
     * @param bgPr
     * @param isTableStyle
     * @return
     * @throws Exception
     */
    public BackgroundAndFill processBackground(IControl control, ZipPackage zipPackage, PackagePart packagePart, 
        PGMaster master, Element bgPr, boolean isTableStyle) throws Exception
    {
        if (bgPr != null)
        {
            BackgroundAndFill bgFill = new BackgroundAndFill();
            Element fill = bgPr.element("solidFill");
            if (fill != null)
            {
                bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                bgFill.setForegroundColor(ReaderKit.instance().getColor(master, fill, isTableStyle));
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
                        PackageRelationship imageShip = packagePart.getRelationship(id);
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
                if (gsLst != null)
                {
                    bgFill.setFillType(ShaderKit.getGradientType(fill));
                    bgFill.setShader(ShaderKit.readGradient(master, fill));
                    return bgFill;
                }
            }
            else if ((fill = bgPr.element("fillRef")) != null)
            {
                bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                bgFill.setForegroundColor(ReaderKit.instance().getColor(master, fill));
                return bgFill;
            }
            else if ((fill = bgPr.element("pattFill")) != null)
            {
                Element bgClr = fill.element("bgClr");
                {
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                    bgFill.setForegroundColor(ReaderKit.instance().getColor(master, bgClr));
                    return bgFill;
                }
            }
        }
        return null;
    }
    
    /**
     * get background
     * @throws Exception 
     */
    public int getBackgroundColor(ZipPackage zipPackage, PackagePart packagePart, 
        PGMaster master, Element bgPr, boolean isTableStyle) throws Exception
    {
        if (bgPr != null)
        {
            Element fill = bgPr.element("solidFill");
            if (fill != null)
            {
                return ReaderKit.instance().getColor(master, fill, isTableStyle);
            }
            else if ((fill = bgPr.element("gradFill")) != null)
            {
                Element gsLst = fill.element("gsLst");
                if (gsLst != null)
                {
                    return ReaderKit.instance().getColor(master, gsLst.element("gs"));
                }
            }
            else if ((fill = bgPr.element("fillRef")) != null)
            {
                return ReaderKit.instance().getColor(master, fill);
            }
            else if ((fill = bgPr.element("pattFill")) != null)
            {
                Element bgClr = fill.element("bgClr");
                {
                    return ReaderKit.instance().getColor(master, bgClr);
                }
            }
        }
        return 0;
    }
    
}
