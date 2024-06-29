/*
 * 文件名称:           SlidePic.java
 *  
 * 编译器:             android2.2
 * 时间:               下午2:58:22
 */
package com.nvqquy98.lib.doc.office.fc.ppt.reader;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipCollection;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.ss.model.drawing.AnchorPoint;
import com.nvqquy98.lib.doc.office.ss.model.drawing.CellAnchor;

/**
 * 解析部件中的 picture
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-3-1
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PictureReader
{
    private static PictureReader picReader = new PictureReader();
    
    /**
     * 
     */
    public static PictureReader instance()
    {
        return picReader;
    }
    
    /**
     * get picture part
     */
    public PackagePart getOLEPart(ZipPackage zipPackage, PackagePart packagePart, String spid, 
           Boolean bExcel) throws Exception
    {
        if (vmlDrawingPart == null)
        {
            PackageRelationshipCollection ships = packagePart.getRelationshipsByType(
                PackageRelationshipTypes.VMLDRAWING_PART);
            for (PackageRelationship oleShip : ships)
            {
                vmlDrawingPart = zipPackage.getPart(oleShip.getTargetURI());
                getShapeIds(bExcel);
            }
        }
        if (spIDs != null)
        {
            String id = spIDs.get(spid);
            if (id != null)
            {
                PackageRelationship imageShip = vmlDrawingPart.getRelationship(id);
                if (imageShip != null)
                {
                    return zipPackage.getPart(imageShip.getTargetURI());
                }
            }
        }
        return null;
    }
    
    /**
     * 获取 spid
     */
    private void getShapeIds(Boolean bExcel) throws Exception
    {
        if (vmlDrawingPart != null)
        {
            SAXReader saxreader = new SAXReader();
            InputStream in = vmlDrawingPart.getInputStream();
            Document poiVml = saxreader.read(in);
            Element root = poiVml.getRootElement();
            if (root != null)
            {
                if (spIDs == null)
                {
                    spIDs = new Hashtable<String, String>();
                }
                if (bExcel && spIDAnchors == null)
                {
                    spIDAnchors = new Hashtable<String, CellAnchor>();
                }
                List<Element> shapes = root.elements("shape");
                for (Element shape : shapes)
                {
                    Element imagedata  = shape.element("imagedata");
                    if (imagedata != null)
                    {
                        String val = shape.attributeValue("spid");
                        
                        if (bExcel)
                        {
                            if (val == null)
                            {
                                val = shape.attributeValue("id");
                            }
                            if (val != null && val.length() > 8)
                            {
                                val = val.substring(8);
                                spIDs.put(val, imagedata.attributeValue("relid"));
                            }
                            else
                            {
                                return;
                            }
                            
                            Element clientData = shape.element("ClientData");
                            if (clientData != null)
                            {
                                Element anchor = clientData.element("Anchor");
                                if (anchor != null)
                                {
                                    String text = anchor.getText();
                                    if (text != null && text.length() > 0)
                                    {
                                        text = text.trim().replaceAll(" ", "");
                                        
                                        String values[] = text.split(",");
                                        if (values != null && values.length == 8)
                                        {
                                            AnchorPoint anchorFrom = new AnchorPoint();
                                            anchorFrom.setColumn((short)Integer.parseInt(values[0]));
                                            anchorFrom.setDX((short)Integer.parseInt(values[1]));
                                            anchorFrom.setRow((short)Integer.parseInt(values[2]));
                                            anchorFrom.setDY((short)Integer.parseInt(values[3]));
                                            
                                            AnchorPoint anchorTo = new AnchorPoint();
                                            anchorTo.setColumn((short)Integer.parseInt(values[4]));
                                            anchorTo.setDX((short)Integer.parseInt(values[5]));
                                            anchorTo.setRow((short)Integer.parseInt(values[6]));
                                            anchorTo.setDY((short)Integer.parseInt(values[7]));
                                            
                                            CellAnchor cellAnchor = new CellAnchor(CellAnchor.TWOCELLANCHOR);
                                            //from
                                            cellAnchor.setStart(anchorFrom);
                                            //to
                                            cellAnchor.setEnd(anchorTo);
                                            
                                            spIDAnchors.put(val, cellAnchor);
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            if (val != null && val.length() > 0)
                            {
                                spIDs.put(val, imagedata.attributeValue("relid"));
                            }
                            else
                            {
                                spIDs.put(shape.attributeValue("id"), imagedata.attributeValue("relid"));
                            }
                        }
                    }
                }
            }
            in.close();
        }
    }
    
    /**
     * for excel
     * @param shapeId
     * @return
     */
    public CellAnchor getExcelShapeAnchor(String shapeId)
    {
        if (shapeId != null && spIDAnchors != null && spIDAnchors.size() > 0)
        {
            return spIDAnchors.get(shapeId);
        }
        return null;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        vmlDrawingPart = null;
        if (spIDs != null)
        {
            spIDs.clear();
            spIDs = null;
        }
        
        if (spIDAnchors != null)
        {
            Iterator<String> iter = spIDAnchors.keySet().iterator();
            while(iter.hasNext())
            {
                spIDAnchors.get(iter.next()).dispose();
            }
            spIDAnchors.clear();
            spIDAnchors = null;
        }
    }
    
    //
    private PackagePart vmlDrawingPart;
    //
    private Map<String, String> spIDs;
    // excel spid and rect
    private Map<String, CellAnchor>spIDAnchors;
}
