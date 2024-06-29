/*
 * 文件名称:          PPTXReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:35:29
 */
package com.nvqquy98.lib.doc.office.fc.ppt;

import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.bean.DocSourceType;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementHandler;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementPath;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ContentTypes;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipCollection;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.RunAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.bulletnumber.BulletNumberManage;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.BackgroundReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.HyperlinkReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.LayoutReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.MasterReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.PictureReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.ReaderKit;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.SmartArtReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.StyleReader;
import com.nvqquy98.lib.doc.office.fc.ppt.reader.TableStyleReader;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.pg.animate.ShapeAnimation;
import com.nvqquy98.lib.doc.office.pg.model.PGLayout;
import com.nvqquy98.lib.doc.office.pg.model.PGMaster;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.pg.model.PGNotes;
import com.nvqquy98.lib.doc.office.pg.model.PGPlaceholderUtil;
import com.nvqquy98.lib.doc.office.pg.model.PGSlide;
import com.nvqquy98.lib.doc.office.pg.model.PGStyle;
import com.nvqquy98.lib.doc.office.simpletext.model.Style;
import com.nvqquy98.lib.doc.office.simpletext.model.StyleManage;
import com.nvqquy98.lib.doc.office.system.AbortReaderError;
import com.nvqquy98.lib.doc.office.system.AbstractReader;
import com.nvqquy98.lib.doc.office.system.BackReaderThread;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.StopReaderError;

/**
 * 解析pptx的文档
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-15
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
@ SuppressWarnings("unchecked")
public class PPTXReader extends AbstractReader
{
    // 
    public static final int FIRST_READ_SLIDE_NUM = 2;
    
    /**
     * fix very large XML documents
     *
     */
    class PresentationSaxHandler implements ElementHandler
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
            String name = elem.getName();
            try
            {
                if(name.equals("sldMasterIdLst"))
                {
                    // master part
                    processMasterPart(elem);
                }
                else if(name.equals("defaultTextStyle"))
                {
                    processDefaultTextStyle(elem);
                }
                else if (name.equals("sldSz"))
                {
                    setPageSize(elem);
                } 
                else if (name.equals("sldId"))
                {
                    addSlideID(elem);
                }                
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }      
            
            elem.detach();            
        } 
    }
    
    /**
     * fix very large XML documents
     *
     */
    class SlideSaxHandler implements ElementHandler
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
            try
            {
                if(("bg").equals(elem.getName()))
                {
                    // 背景
                    processBackground(slidePart, pgMaster, pgLayout, pgSlide, elem);
                }
                else if ("sld".equals(elem.getName()))
                {
                    if (elem.attribute("showMasterSp") != null)
                    {
                        String val = elem.attributeValue("showMasterSp");
                        if (val != null && val.length() > 0 && Integer.valueOf(val) == 0)
                        {
                            showMasterSp = false;
                        }    
                    }
                }
                else if("par".equals(elem.getName()))
                {
                    processSlideShow(pgSlide, elem);
                }
                else if("transition".equals(elem.getName()))
                {
                    //slide transition
                    pgSlide.setTransition(elem.elements().size() > 0);
                }
                else
                {
                    ShapeManage.instance().processShape(control, zipPackage, slidePart, pgModel, 
                        pgMaster, pgLayout, defaultStyle, pgSlide, PGSlide.Slide_Normal, elem, null, 1.0f, 1.0f);
                }
               
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            elem.detach();            
        }   
    }
    
    /**
     * fix very large XML documents
     *
     */
    class PresentationSaxHandler_Search implements ElementHandler
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
            if (("sldId").equals(elem.getName()))
            {
                note = false;
                PackagePart slidePart =  zipPackage.getPart(
                    packagePart.getRelationship(elem.attribute(1).getValue()).getTargetURI());
                if (slidePart != null)
                {
                    SAXReader saxreader = new SAXReader();
                    try
                    {   
                        // search slide
                        InputStream in = slidePart.getInputStream();
                        SlideNoteSaxHandler_Search slideSaxHandler = new SlideNoteSaxHandler_Search(); 
                        saxreader.addHandler("/sld/cSld/spTree/sp", slideSaxHandler);
                        saxreader.addHandler("/sld/cSld/spTree/grpSp", slideSaxHandler);
                        saxreader.read(in);                    
                        in.close();
                        
                        // search notes                    
                        PackageRelationship notesShip = slidePart.getRelationshipsByType(
                            PackageRelationshipTypes.NOTES_SLIDE).getRelationship(0);
                        if (notesShip != null)
                        {
                            PackagePart notesPart = zipPackage.getPart(notesShip.getTargetURI());
                            
                            note = true;
                            in = notesPart.getInputStream();  
                            saxreader.resetHandlers();
                            saxreader.addHandler("/notes/cSld/spTree/sp", slideSaxHandler);
                            saxreader.read(in);
                            in.close();
                        }                 
                    }
                    catch(StopReaderError e)
                    {
                        elem.detach();
                        throw new StopReaderError("stop");
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        saxreader.resetHandlers();
                    }
                }
            }
            
            elem.detach();            
        }    
    }
    
    
    /**
     * fix very large XML documents
     *
     */
    class SlideNoteSaxHandler_Search implements ElementHandler
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
            searchContentForText(elem, key); 
            
            elem.detach();   
            
            if(searched)
            {
                throw new StopReaderError("stop");
            }
        }    
    }  
    
    /**
     * 
     * @param filePath
     */
    public PPTXReader(IControl control, String filePath,int docSourceType)
    {
        this.control = control;
        this.filePath = filePath;
        this.docSourceType = docSourceType;
    }
    
    /**
     * 
     * @return
     */
    public Object getModel() throws Exception
    {  
        if (pgModel != null)
        {
            return pgModel;
        }
        initPackagePart();
        pgModel = new PGModel();
        
        processPresentation();
        return pgModel;
    }
    
    /**
     * 
     */
    public void initPackagePart()  throws Exception
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

        PackageRelationship coreRel = zipPackage.getRelationshipsByType(
            PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
        if (coreRel == null || !coreRel.getTargetURI().toString().equals("/ppt/presentation.xml"))
        {
            throw new Exception("Format error");
        }
        packagePart = zipPackage.getPart(coreRel);
    }

    /**
     * process presentation xml
     * @throws Exception
     */
    private void processPresentation() throws Exception
    {
        // get presentation xml
        SAXReader saxreader = new SAXReader();
        try
        {
            InputStream in = packagePart.getInputStream();
            
            PresentationSaxHandler preSaxHandler = new PresentationSaxHandler();
            
            saxreader.addHandler("/presentation/sldMasterIdLst", preSaxHandler);
            saxreader.addHandler("/presentation/defaultTextStyle", preSaxHandler);
            saxreader.addHandler("/presentation/sldSz", preSaxHandler);
            saxreader.addHandler("/presentation/sldIdLst/sldId", preSaxHandler);
            
            Document poi = saxreader.read(in);
            Element root = poi.getRootElement();
            if (root != null)
            {
                if (root.attribute("firstSlideNum") != null)
                {
                    String val = root.attributeValue("firstSlideNum");
                    if (val != null && val.length() > 0)
                    {
                        pgModel.setSlideNumberOffset(Integer.valueOf(val) - 1);
                    }    
                }
            }
            in.close();
            if (sldIds == null)
            {
                throw new Exception("Format error");
            }
            pgModel.setSlideCount(sldIds.size());
            
            //table style part
            ArrayList<PackagePart> tableStyleParts = zipPackage.getPartsByContentType(ContentTypes.TABLE_STYLE_PART);
            if(tableStyleParts.size() > 0)
            {
            	PackagePart tableStylePackagePart = tableStyleParts.get(0);
                if(tableStylePackagePart != null)
                {            	
                	Style style = StyleManage.instance().getStyle(defaultStyle.getStyle(1));
                	int fontsize = 12;
                    if (style != null)
                    {
                        fontsize = style.getAttrbuteSet().getAttribute(AttrIDConstant.FONT_SIZE_ID);
                        if(fontsize < 0)
                        {
                        	fontsize = 12;
                        }
                    }
                    
                    TableStyleReader.instance().read(pgModel, tableStylePackagePart, fontsize);
                }
            }
            
            processSlidePart();
        }
        catch (Exception  e)
        {
            throw e;
        }
        finally
        {
            saxreader.resetHandlers();
        }
    }
    
    /**
     * reader style
     */
    private void processDefaultFontColor(Element style, int lvl)
    {
        if (style != null)
        {
            String val = null;
            Element temp = style.element("defRPr");
            if (temp != null)
            {
                temp = temp.element("solidFill");
                if (temp != null)
                {
                    if ((temp = temp.element("schemeClr")) != null && temp.attribute("val") != null)
                    {
                        val = temp.attributeValue("val");
                        if (val != null && val.length() > 0)
                        {
                            defaultStyle.addDefaultFontColor(lvl, val);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param defaultTextStyle
     */
    private void processDefaultTextStyle(Element defaultTextStyle)
    {
        if (nameMaster != null)
        {
            Iterator<String> iter = nameMaster.keySet().iterator();
            if(iter.hasNext())
            {
                StyleReader.instance().setStyleIndex(1);
                defaultStyle = StyleReader.instance().getStyles(control, nameMaster.get(iter.next()), null, defaultTextStyle);
            }
        }
        
        // default font colol
        if (defaultTextStyle != null && defaultStyle != null)
        {
            Element temp = defaultTextStyle.element("lvl1pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 1);
            }
            temp = defaultTextStyle.element("lvl2pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 2);
            }
            temp = defaultTextStyle.element("lvl3pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 3);
            }
            temp = defaultTextStyle.element("lvl4pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 4);
            }
            temp = defaultTextStyle.element("lvl5pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 5);
            }
            temp = defaultTextStyle.element("lvl6pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 6);
            }
            temp = defaultTextStyle.element("lvl7pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 7);
            }
            temp = defaultTextStyle.element("lvl8pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 8);
            }
            temp = defaultTextStyle.element("lvl9pPr");
            if (temp != null)
            {
                processDefaultFontColor(temp, 9);
            }
        }
    }

    /**
     * 
     * @param masterIdLst
     * @throws Exception
     */
    public void processMasterPart(Element masterIdLst) throws Exception
    {
        // process the first master part
        List<Element> masterIds = masterIdLst.elements("sldMasterId");
        if (masterIds.size() > 0)
        {
            Element masterId = masterIds.get(0);
            if (abortReader)
            {
                return;
            }
            int index = 0;
            if (masterId.attributeCount() > 1)
            {
                index = 1;
            }
            PackagePart masterPart =  zipPackage.getPart(
                packagePart.getRelationship(masterId.attribute(index).getValue()).getTargetURI());
            nameMaster.put(masterPart.getPartName().getName(), 
                MasterReader.instance().getMasterData(control, zipPackage, masterPart, pgModel));
        }
    }
 
    /**
     * 
     * @param slideId
     */
    private void addSlideID(Element slideId)
    {
        if(sldIds == null)
        {
            sldIds = new ArrayList<String>();
        }
        sldIds.add(slideId.attribute(1).getValue());
    }
    
    /**
     * process slidepart
     * @throws Exception
     */
    public void processSlidePart() throws Exception
    {
        if (sldIds.size() > 0)
        {
            int len = Math.min(sldIds.size(), FIRST_READ_SLIDE_NUM);
            for (int i = 0; i < len && !abortReader; i++)
            {
                processSlide(sldIds.get(currentReaderIndex++));
            }
            
            if (!isReaderFinish())
            {
                new BackReaderThread(this, control).start();
            }
        }
        else
        {
            /*PGSlide pgSlide = new PGSlide();
            pgModel.appendSlide(pgSlide);*/
            throw new Exception("Format error");
        }
    }
    
    /**
     * 
     *
     */
    public boolean isReaderFinish()
    {
        if (pgModel != null && sldIds != null)
        {
            return abortReader || pgModel.getSlideCount() == 0 || currentReaderIndex >= sldIds.size();
        }
        return true;
    }
    
    /**
     * 
     */
    public void backReader() throws Exception
    {
        try
        {
            processSlide(sldIds.get(currentReaderIndex++));
            //control.actionEvent(EventConstant.PG_REPAINT_ID, null);
            control.actionEvent(EventConstant.APP_COUNT_PAGES_CHANGE_ID, null);
        }
        catch (Error e)
        {
            control.getSysKit().getErrorKit().writerLog(e, true);
        }
        
    }

    /**
     * 处理slide
     */
    private void processSlide(String sldId) throws Exception
    {
        showMasterSp = true;
        slidePart = zipPackage.getPart(
            packagePart.getRelationship(sldId).getTargetURI());
        
        // get layout part
        PackageRelationship layoutShip = slidePart.getRelationshipsByType(
            PackageRelationshipTypes.LAYOUT_PART).getRelationship(0);
        PackagePart layoutPart = zipPackage.getPart(layoutShip.getTargetURI());
        
        // get master 
        PackageRelationship ship = layoutPart.getRelationshipsByType(
            PackageRelationshipTypes.SLIDE_MASTER).getRelationship(0);
        pgMaster = nameMaster.get(ship.getTargetURI().toString());
        if (pgMaster == null)
        {
            PackagePart masterPart =  zipPackage.getPart(ship.getTargetURI());
            pgMaster = MasterReader.instance().getMasterData(control, zipPackage, masterPart, pgModel);
            nameMaster.put(masterPart.getPartName().getName(), pgMaster);
        }

        // layout
        pgLayout = nameLayout.get(layoutPart.getPartName().getName());
        if (pgLayout == null)
        {
            pgLayout = LayoutReader.instance().getLayouts(control, zipPackage, layoutPart, pgModel, pgMaster, null);
            nameLayout.put(layoutPart.getPartName().getName(), pgLayout);
        }
        
        pgSlide = new PGSlide();
        pgSlide.setSlideType(PGSlide.Slide_Normal);
        
        //smart art
        PackageRelationshipCollection smartArtDataCollection = slidePart.getRelationshipsByType(PackageRelationshipTypes.DIAGRAM_DATA);
        if(smartArtDataCollection != null && smartArtDataCollection.size() > 0)
        {
        	int cnt = smartArtDataCollection.size();
        	PackageRelationship rel = null;
        	for(int i = 0; i < cnt; i++)
        	{
        		rel = smartArtDataCollection.getRelationship(i);
        		pgSlide.addSmartArt(rel.getId(), 
        				SmartArtReader.instance().read(control, 
        						zipPackage,
        						pgModel, 
        						pgMaster, 
        						pgLayout, 
        						pgSlide, 
        						slidePart,
        						zipPackage.getPart(rel.getTargetURI())));
        	}
        }
        
        // hyperlink
        HyperlinkReader.instance().getHyperlinkList(control, slidePart);
        
        // slide xml
        SAXReader saxreader = new SAXReader();
        try
        {
            InputStream in = slidePart.getInputStream();
            
            SlideSaxHandler slideSaxHandler = new SlideSaxHandler();
            saxreader.addHandler("/sld/cSld/bg", slideSaxHandler);
            saxreader.addHandler("/sld/cSld/spTree/sp", slideSaxHandler);
            saxreader.addHandler("/sld/cSld/spTree/cxnSp", slideSaxHandler);
            saxreader.addHandler("/sld/cSld/spTree/pic", slideSaxHandler);
            saxreader.addHandler("/sld/cSld/spTree/graphicFrame", slideSaxHandler);
            saxreader.addHandler("/sld/cSld/spTree/grpSp", slideSaxHandler);
            saxreader.addHandler("/sld/cSld/spTree/AlternateContent", slideSaxHandler);
            saxreader.addHandler("/sld/timing/tnLst/par/cTn/childTnLst/seq/cTn/childTnLst/par", slideSaxHandler);
            saxreader.addHandler("/sld/timing/bldLst/bldP", slideSaxHandler);
            //07 pg document
            saxreader.addHandler("/sld/transition", slideSaxHandler);
            //2010 pg document
            saxreader.addHandler("/sld/AlternateContent/Choice/transition", slideSaxHandler);
            saxreader.addHandler("/sld", slideSaxHandler);
            saxreader.read(in);
            
            //
            in.close();
            
            processBackground(slidePart, pgMaster, pgLayout, pgSlide, null);           
            
            //group shape          
            processGroupShape(pgSlide);
            
            // slide number
            pgSlide.setSlideNo(slideNum++);
            // 处理 notes
            processNotes(slidePart, pgSlide);
            // slidemaster
            if (showMasterSp && pgLayout.isAddShapes())
            {
                pgSlide.setMasterSlideIndex(pgMaster.getSlideMasterIndex());
            }
            pgSlide.setLayoutSlideIndex(pgLayout.getSlideMasterIndex());
            //
            pgModel.appendSlide(pgSlide);
            
            pgSlide = null;
            pgLayout = null;
            pgMaster = null;
            slidePart = null;
            PictureReader.instance().dispose();
            HyperlinkReader.instance().disposs();
        }
        finally
        {
            saxreader.resetHandlers();
        }
    }
    
    private void processGroupShape(PGSlide pgSlide)
    {
        Map<Integer, List<Integer>> grpShape = pgSlide.getGroupShape();
        if(grpShape == null)
        {
            return;
        }
        
//        processGroupShapeID(grpShape);
        int count = pgSlide.getShapeCount();
        int grpSpID;
        for (int i = 0; i < count; i++)
        {
            IShape shape = pgSlide.getShape(i);
            grpSpID = getGroupShapeID(shape.getShapeID(), grpShape);
            shape.setGroupShapeID(grpSpID);
        }
    }
    
    /**
     * get group id of shape
     * @param shapeID
     * @param grpShape
     * @return
     */
    private int getGroupShapeID(int shapeID, Map<Integer, List<Integer>> grpShape)
    {
        Iterator<Integer> grpIDIter = grpShape.keySet().iterator();
        while(grpIDIter.hasNext())
        {
            int grpID = grpIDIter.next();
            List<Integer> childShape = grpShape.get(grpID);
            if(childShape != null && childShape.contains(shapeID))
            {
                return grpID;
            }
        }
        
        return -1;
    }
    
    /**
     * groupshape中全部shape处理成直接记录最外层groupshape的GroupID
     * @param grpShape
     */
    private void processGroupShapeID(Map<Integer, List<Integer>> grpShape)
    {
        boolean repeat = false;        
        Iterator<Integer> grpIDIter = grpShape.keySet().iterator();
        List<Integer> removeGrpID = null;
        while(grpIDIter.hasNext())
        {
            int grpID = grpIDIter.next();
            List<Integer> childShape = grpShape.get(grpID);
            int grpCnt = childShape.size();
            List<Integer> ids = null;
            for(int i = 0; i < grpCnt; i++)
            {
                List<Integer> childchildShape = grpShape.get(childShape.get(i));
                if(childchildShape != null && childchildShape.size() > 0)
                {
                    if(ids == null)
                    {
                        ids = new ArrayList<Integer>();
                    }
                    ids.addAll(childchildShape);
                }
            }
            
            if(ids !=  null && ids.size() > 0)
            {
                //current node is group shape
                repeat = true;
                childShape.addAll(ids);
            }
            else
            {
                //all shapes are not groupshape
                if(removeGrpID == null)
                {
                    removeGrpID = new ArrayList<Integer>();
                }
                removeGrpID.add(grpID);
            }
        }
        
        if(repeat)
        {
            for(int i = 0; i < removeGrpID.size(); i++)
            {
                grpShape.remove(removeGrpID.get(i));
            }
            processGroupShapeID(grpShape);
        }
    }    
    
    /**
     * 处理slide的背景
     * @throws Exception 
     */
    private void processBackground(PackagePart slidePart, PGMaster pgMaster, 
        PGLayout pgLayout, PGSlide pgSlide, Element bg) throws Exception
    {
        BackgroundAndFill bgFill = null;
        if (bg == null && pgSlide.getBackgroundAndFill() == null)
        {
            bgFill = pgLayout.getBackgroundAndFill();
            if (bgFill == null)
            {
                bgFill = pgMaster.getBackgroundAndFill();
            }
            pgSlide.setBackgroundAndFill(bgFill);
        }
        else if (bg != null)
        {
            bgFill = BackgroundReader.instance().getBackground(control, zipPackage, slidePart, pgMaster, bg);
            pgSlide.setBackgroundAndFill(bgFill);
        }
    }
    
    /**
     * set slide page size
     * @param model
     */
    public void setPageSize(Element slideSize)
    {
        float pgx = 0;
        float pgy = 0;
        if (slideSize != null)
        {
            pgx = Float.parseFloat(slideSize.attributeValue("cx")) 
                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH;
            pgy = Float.parseFloat(slideSize.attributeValue("cy")) 
                * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH;
        }
        pgModel.setPageSize(new Dimension((int)pgx, (int)pgy));
    }
    
    /**
     * 
     */
    private void processNotes(PackagePart slidePart, PGSlide pgSlide) throws Exception
    {
        // get notes part
        PackageRelationship notesShip = slidePart.getRelationshipsByType(
            PackageRelationshipTypes.NOTES_SLIDE).getRelationship(0);
        if (notesShip != null)
        {
            PackagePart notesPart = zipPackage.getPart(notesShip.getTargetURI());
            //
            SAXReader saxreader = new SAXReader();
            InputStream in = notesPart.getInputStream();
            Document poiNote = saxreader.read(in);
            
            Element root = poiNote.getRootElement();
            if (root != null)
            {
                String notes = ReaderKit.instance().getNotes(root);
                if (notes != null)
                {
                    PGNotes pgNotes = new PGNotes(notes);
                    pgSlide.setNotes(pgNotes);
                }
            }
            
            in.close();
        }
    }
    
    //animation id and type
    private void processSlideShow(PGSlide pgSlide, Element elem)
    {
        try
        {
            //"/cTn/childTnLst/par/cTn/childTnLst//par"   
            List<Element> elements= elem.element("cTn").element("childTnLst").elements("par");
            if(elements.size() >= 1)
            {
                //after previous
                for(Element item : elements)
                {
                    List<Element> elementList = item.element("cTn").element("childTnLst").elements("par");
                    for(Element e : elementList)
                    {
                        //cTn, with previous( when elementList.size() > 1)
                        elem = e.element("cTn");
                        processAnimation(pgSlide, elem);
                    }
                }
                
            }
        }
        catch(Exception e)
        {
            
        }
        
    }
    
    private void processAnimation(PGSlide pgSlide, Element elem)
    {
        String sType = elem.attributeValue("presetClass");
        elem = elem.element("childTnLst");
        if(elem.element("set") != null)
        {
            elem = elem.element("set").element("cBhvr").element("tgtEl").element("spTgt");
        }
        else
        {
            //emph
            elem = (Element)elem.elements().get(0);
            elem = elem.element("cBhvr").element("tgtEl").element("spTgt");
        }
        
        String shapeID = elem.attributeValue("spid");
        byte nType = ShapeAnimation.SA_EMPH;
        if(sType.equals("entr"))
        {
            nType = ShapeAnimation.SA_ENTR;
        }
        else if(sType.equals("emph"))
        {
            nType = ShapeAnimation.SA_EMPH;
        }
        else if(sType.equals("exit"))
        {
            nType = ShapeAnimation.SA_EXIT;
        }
        else
        {
            //path, verb, mediacall
            return;
        }        
        
        if(elem.element("txEl") != null && elem.element("txEl").element("pRg") != null)
        {
            elem = elem.element("txEl").element("pRg");
            //paragraph range
            String s = elem.attributeValue("st");
            String e = elem.attributeValue("end");
            
            pgSlide.addShapeAnimation(
                new ShapeAnimation(Integer.parseInt(shapeID), nType, Integer.parseInt(s), Integer.parseInt(e)));
        }
        else if(elem.element("bg") != null)
        {
            //background
            pgSlide.addShapeAnimation(
                new ShapeAnimation(Integer.parseInt(shapeID), nType, ShapeAnimation.Para_BG, ShapeAnimation.Para_BG));
        }
        else
        {
            pgSlide.addShapeAnimation(
                new ShapeAnimation(Integer.parseInt(shapeID), nType));
        }
    }
    
    /**
     * 
     * @param file
     * @param key
     * @return
     */
    public boolean searchContent(File file, String key) throws Exception
    {
        searched = false;
        this.key = key;
        
        zipPackage = new ZipPackage(file.getAbsolutePath());
        PackageRelationship coreRel = zipPackage.getRelationshipsByType(
            PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
        packagePart = zipPackage.getPart(coreRel);
        
        SAXReader saxreader = new SAXReader();
        try
        {   
            // presentation xml
            InputStream in = packagePart.getInputStream();
            PresentationSaxHandler_Search preSaxHandler = new PresentationSaxHandler_Search();        
            saxreader.addHandler("/presentation/sldIdLst/sldId", preSaxHandler);
            
            saxreader.read(in);
            in.close();
        }
        catch(StopReaderError e)
        {
            
        }
        finally
        {
            saxreader.resetHandlers();
        }
        
        this.key = null;
        zipPackage= null;
        packagePart = null;
        
        return searched;
    }
    
    /**
     * search xml
     * @param elem
     * @param key
     * @return
     */
    public boolean searchContentForText(Element elem, String key)
    {
        String name = elem.getName();
        if (name.equals("sp"))
        {
            StringBuilder sb = new StringBuilder();
            if (note && !PGPlaceholderUtil.BODY.equals(ReaderKit.instance().getPlaceholderType(elem)))
            {
                return false;
            }
            Element txBody = elem.element("txBody");
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
                            sb.append(t.getText());
                        }
                    }
                    if (sb.indexOf(key) >= 0)
                    {
                        this.key = null;
                        zipPackage= null;
                        packagePart = null;
                        searched = true;
                        return true;
                    }
                    sb.delete(0, sb.length());
                }
            }
        }
        else if (name.equals("grpSp"))
        {
            for (Iterator< ? > it = elem.elementIterator(); it.hasNext();)
            {
                if (searchContentForText((Element)it.next(), key))
                {
                    this.key = null;
                    zipPackage= null;
                    packagePart = null;
                    searched = true;
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (isReaderFinish())
        {
            super.dispose();

            if (abortReader && pgModel != null 
                && pgModel.getSlideCount() < FIRST_READ_SLIDE_NUM 
                && sldIds != null && sldIds.size() > 0)
            {
                pgModel.dispose();
            }
            pgModel = null;
            filePath = null;
            zipPackage = null; 
            packagePart = null;
            //packageRel = null;
            if (nameLayout != null)
            {
                Iterator<String> iter = nameLayout.keySet().iterator();
                while(iter.hasNext())
                {
                    nameLayout.get(iter.next()).disposs();
                }
                nameLayout.clear();
                nameLayout = null;
            }
            if (nameMaster != null)
            {
//                Iterator<String> iter = nameMaster.keySet().iterator();
//                while(iter.hasNext())
//                {
//                    nameMaster.get(iter.next()).dispose();
//                }
                nameMaster.clear();
                nameMaster = null;
            }
            if (sldIds != null)
            {
                sldIds.clear();
                sldIds = null;
            }
            if (defaultStyle != null)
            {
                defaultStyle.dispose();
                defaultStyle = null;
            }
                      
            key = null;
            
            pgSlide = null;
            pgLayout = null;
            pgMaster = null;
            slidePart = null;
            HyperlinkReader.instance().disposs();
            PictureReader.instance().dispose();
            LayoutReader.instance().dispose();
            MasterReader.instance().dispose();
            RunAttr.instance().dispose();
            BulletNumberManage.instance().dispose();
        }
    }
   
    //
    private int slideNum = 1;
    //
    private int currentReaderIndex;
    //
    private PGModel pgModel;
    //
    private String filePath;
    private int docSourceType;
    //
    private ZipPackage zipPackage;
    //
    private PackagePart packagePart;
    //
    private Map<String, PGLayout> nameLayout = new Hashtable<String, PGLayout>();
    //
    private Map<String, PGMaster> nameMaster = new Hashtable<String, PGMaster>();
    //
    private List<String> sldIds;
    // default text style
    private PGStyle defaultStyle;
    
    //temp parameter    
    private PackagePart slidePart;
    private PGMaster pgMaster;
    private PGLayout pgLayout;
    private PGSlide pgSlide;
    
    //for search
    private String key;
    private boolean searched;
    private boolean note;
    
    private boolean showMasterSp;
}
