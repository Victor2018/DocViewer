/*
 * 文件名称:           PGMaster.java
 *  
 * 编译器:             android2.2
 * 时间:               下午5:17:16
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.ppt.ShapeManage;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.pg.model.PGPlaceholderUtil;
import com.nvqquy98.lib.doc.office.pg.model.PGSlide;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * 解析 master
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-2-16
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class MasterReader
{
    private static MasterReader masterReader = new MasterReader();
    
    /**
     * 
     */
    public static MasterReader instance()
    {
        return masterReader;
    }
    
    /**
     * 
     * @param zipPackage
     * @param masterPart
     * @return
     * @throws Exception
     */
    public PGMaster getMasterData(IControl control, ZipPackage zipPackage, PackagePart masterPart, 
        PGModel pgModel) throws Exception
    {
        SAXReader saxreader = new SAXReader();
        InputStream in = masterPart.getInputStream();
        Document poiMaster = saxreader.read(in);
        Element master = poiMaster.getRootElement();
        PGMaster pgMaster = null;
        if (master != null)
        {
            pgMaster = new PGMaster();
            // color map
            processClrMap(pgMaster, zipPackage, masterPart, master);
            // text style
            processStyle(control, pgMaster, master);
            //
            Element cSld = master.element("cSld");
            if (cSld != null)
            {
                // background 
                processBackgroundAndFill(control, pgMaster, zipPackage, masterPart, cSld);
                
                Element spTree = cSld.element("spTree");
                if (spTree != null)
                {
                    processTextStyle(control, pgMaster, spTree);
                    
                    // slidemaster
                    PGSlide pgSlide = new PGSlide();
                    pgSlide.setSlideType(PGSlide.Slide_Master);
                    for (Iterator< ? > it = spTree.elementIterator(); it.hasNext();)
                    {
                        ShapeManage.instance().processShape(control, zipPackage, masterPart, null, 
                            pgMaster, null, null, pgSlide, PGSlide.Slide_Master, (Element)it.next(), null, 1.0f, 1.0f);
                    }
                    if (pgSlide.getShapeCount() > 0)
                    {
                        pgMaster.setSlideMasterIndex(pgModel.appendSlideMaster(pgSlide));
                    }
                }
            }
        }
        in.close();
        return pgMaster;
    }
    
    /**
     * process color map
     * @throws Exception 
     */
    private void processClrMap(PGMaster pgMaster, ZipPackage zipPackage, PackagePart masterPart, 
        Element master) throws Exception
    {
        // get theme part
        PackageRelationship themeShip = masterPart.getRelationshipsByType(
            PackageRelationshipTypes.THEME_PART).getRelationship(0);
        if (themeShip != null)
        {
            PackagePart themePart = zipPackage.getPart(themeShip.getTargetURI());
            if (themePart != null)
            {
                Map<String, Integer> themeColor = ThemeReader.instance().getThemeColorMap(themePart);
                
                Element clrMap = master.element("clrMap");
                if (clrMap != null)
                {
                    for (int i = 0; i < clrMap.attributeCount(); i++)
                    {
                        String name = clrMap.attribute(i).getName();
                        String value = clrMap.attributeValue(name);
                        if (!name.equals(value))
                        {
                            pgMaster.addColor(value, themeColor.get(value));
                        }
                        pgMaster.addColor(name, themeColor.get(value));
                    }
                }
            }
        }
    }

    /**
     * set background
     * @throws Exception 
     */
    private void processBackgroundAndFill(IControl control, PGMaster pgMaster, ZipPackage zipPackage, 
        PackagePart masterPart, Element cSld) throws Exception
    {
        Element bg = cSld.element("bg");
        if (bg != null)
        {
            pgMaster.setBackgroundAndFill(BackgroundReader.instance().getBackground(control, 
                zipPackage, masterPart, pgMaster, bg));
        }
    }

    /**
     * 处理 Shape部分
     */
    private void processTextStyle(IControl control, PGMaster pgMaster, Element spTree) throws Exception
    {
        for (Iterator< ? > it = spTree.elementIterator(); it.hasNext();)
        {
            Element sp = (Element)it.next();
            String type = ReaderKit.instance().getPlaceholderType(sp);
            type = PGPlaceholderUtil.instance().checkTypeName(type);
            int idx = ReaderKit.instance().getPlaceholderIdx(sp);
            Element txBody = sp.element("txBody");
            if (txBody != null)
            {
                Element lstStyle = txBody.element("lstStyle");
                StyleReader.instance().setStyleIndex(styleIndex);
                if (!PGPlaceholderUtil.instance().isBody(type))
                {
                    pgMaster.addStyleByType(type, StyleReader.instance().getStyles(control, pgMaster, sp, lstStyle));
                }
                else if (idx > 0)
                {
                    pgMaster.addStyleByIdx(idx, StyleReader.instance().getStyles(control, pgMaster, sp, lstStyle));  
                }
                
                styleIndex = StyleReader.instance().getStyleIndex();
            }
        }
    }
 
    /**
     * 处理text style部分
     */
    private void processStyle(IControl control, PGMaster pgMaster, Element master)
    {
        Element txStyles = master.element("txStyles");
        if (txStyles != null)
        {
            StyleReader.instance().setStyleIndex(styleIndex);
            
            Element style = txStyles.element("titleStyle");
            pgMaster.setTitleStyle(StyleReader.instance().getStyles(control, pgMaster, null, style));
            
            style = txStyles.element("bodyStyle");
            pgMaster.setBodyStyle(StyleReader.instance().getStyles(control, pgMaster, null, style)); 
            
            style = txStyles.element("otherStyle");
            pgMaster.setDefaultStyle(StyleReader.instance().getStyles(control, pgMaster, null, style));
            
            styleIndex = StyleReader.instance().getStyleIndex();
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        styleIndex = 10;
    }
    
    //
    private int styleIndex = 10;
}
