/*
 * 文件名称:           HSSFShapeFactory.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:23:36
 */
package com.nvqquy98.lib.doc.office.fc.hssf.usermodel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherChildAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientDataRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherPropertyFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpgrRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.CommonObjectDataSubRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.EmbeddedObjectRefSubRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.ObjRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.Record;
import com.nvqquy98.lib.doc.office.fc.hssf.record.SubRecord;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.AWorkbook;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-4-10
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public final class HSSFShapeFactory
{    
    public static HSSFShape createShape(AWorkbook workbook, Map<EscherRecord, Record> shapeToObj, 
                  EscherContainerRecord spContainer, HSSFShape parent)
    {
        if (spContainer.getRecordId() == EscherContainerRecord.SPGR_CONTAINER)
        {
            return createShapeGroup(workbook, shapeToObj, spContainer, parent);
        }
        return createSimpeShape(workbook, shapeToObj, spContainer, parent);
    }
    
    public static HSSFShapeGroup createShapeGroup(AWorkbook workbook, Map<EscherRecord, Record> shapeToObj, 
                  EscherContainerRecord spContainer, HSSFShape parent)
    {
        HSSFShapeGroup group = null;
        List<EscherRecord> childRecords = spContainer.getChildRecords();
        if (childRecords.size() > 0)
        {
            EscherContainerRecord groupContainer = (EscherContainerRecord)childRecords.get(0);
            
            HSSFAnchor anchor = null;            
            if(parent == null)
            {
                EscherClientAnchorRecord anchorRecord = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                    groupContainer, EscherClientAnchorRecord.RECORD_ID);
                if(anchorRecord != null && anchorRecord.getCol2() <= 255 && anchorRecord.getRow2() <= 65535)
                {
                    anchor = HSSFShape.toClientAnchor(anchorRecord);
                }
            }
            else
            {
                EscherChildAnchorRecord childRecord = (EscherChildAnchorRecord)ShapeKit.getEscherChild(
                    groupContainer, EscherChildAnchorRecord.RECORD_ID);
                if(childRecord != null)
                {
                    anchor = HSSFShape.toChildAnchor(childRecord);
                }
            }
            if(anchor == null)
            {
                anchor = new HSSFClientAnchor();
            }
            
            
            EscherRecord opt = ShapeKit.getEscherChild(groupContainer, (short)0xF122);
            if (opt != null)
            {
                EscherPropertyFactory f = new EscherPropertyFactory();
                List<EscherProperty> props = f.createProperties(opt.serialize(), 8, opt.getInstance());
                EscherSimpleProperty p = (EscherSimpleProperty)props.get(0);
                if (p.getPropertyNumber() != 0x39F || p.getPropertyValue() != 1)
                {
                    group = new HSSFShapeGroup(groupContainer, parent, anchor);
                }
            }
            else
            {
                group = new HSSFShapeGroup(groupContainer, parent, anchor);
            }
            
            EscherSpgrRecord spgrRecord = (EscherSpgrRecord)ShapeKit.getEscherChild(
                groupContainer, EscherSpgrRecord.RECORD_ID);
            if(spgrRecord != null)
            {
                group.setCoordinates(spgrRecord.getRectX1(), 
                    spgrRecord.getRectY1(), spgrRecord.getRectX2(), spgrRecord.getRectY2());
            }
            
            for(int i = 1; i < childRecords.size(); i++)
            {
                HSSFShape shape = createShape(workbook, shapeToObj, (EscherContainerRecord)childRecords.get(i), group);
                group.addChildShape(shape);
            }
        } 
        return group;
    }
    
    public static HSSFShape createSimpeShape(AWorkbook workbook, Map<EscherRecord, Record> shapeToObj, 
                  EscherContainerRecord spContainer, HSSFShape parent)
    {
        HSSFShape shape = null;
        HSSFAnchor anchor = null;
        
        if(parent == null)
        {
            EscherClientAnchorRecord anchorRecord = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                spContainer, EscherClientAnchorRecord.RECORD_ID);
            if(anchorRecord != null && anchorRecord.getCol2() <= 255 && anchorRecord.getRow2() <= 65535)
            {
                anchor = HSSFShape.toClientAnchor(anchorRecord);
            }
        }
        else
        {
            EscherChildAnchorRecord childRecord = (EscherChildAnchorRecord)ShapeKit.getEscherChild(
                spContainer, EscherChildAnchorRecord.RECORD_ID);
            if(childRecord != null)
            {
                anchor = HSSFShape.toChildAnchor(childRecord);
            }
        }
        
        
        EscherSpRecord spRecord = spContainer.getChildById(EscherSpRecord.RECORD_ID);
        if(spRecord == null)
        {
        	return null;
        }
        
        int type = spRecord.getOptions() >> 4;
        switch (type)
        {
            case ShapeTypes.TextBox:
                if (shapeToObj != null && shapeToObj.size() > 0)
                {
                    EscherClientDataRecord escherClientDataRecord = 
                    (EscherClientDataRecord)ShapeKit.getEscherChild(spContainer, EscherClientDataRecord.RECORD_ID );
                    Record record = shapeToObj.get(escherClientDataRecord);                 
                   
                    if(record instanceof ObjRecord && ((ObjRecord)record).getSubRecords().get(0) instanceof CommonObjectDataSubRecord)
                    {
                        CommonObjectDataSubRecord commonObjectDataSubRecord = (CommonObjectDataSubRecord)((ObjRecord)record).getSubRecords().get(0);
                        if(commonObjectDataSubRecord.getObjectType() != CommonObjectDataSubRecord.OBJECT_TYPE_COMMENT)
                        {
                            //except comment
                            shape = new HSSFAutoShape(workbook, spContainer, parent, anchor, type);  
                        }
                    }
                    break;
                }
                
            case ShapeTypes.PictureFrame:
                EscherOptRecord opt = (EscherOptRecord)ShapeKit.getEscherChild(
                        spContainer, EscherOptRecord.RECORD_ID );
                EscherSimpleProperty prop = (EscherSimpleProperty)opt.lookup(
                        EscherProperties.BLIP__BLIPTODISPLAY );
                shape = new HSSFPicture(workbook, spContainer, parent, anchor , opt);
                if (prop != null)
                {                    
                    ((HSSFPicture)shape).setPictureIndex(prop.getPropertyValue());
                }
                break;
                
            case ShapeTypes.HostControl:
                //chart ,参考 HSSFChart.getSheetCharts                
                shape = new HSSFChart(workbook, spContainer, parent, anchor);
                break;
                
            case ShapeTypes.Line:
            case ShapeTypes.StraightConnector1:
            case ShapeTypes.BentConnector2:
            case ShapeTypes.BentConnector3:
            case ShapeTypes.CurvedConnector3:
                shape = new HSSFLine(workbook, spContainer, parent, anchor, type);
                break;
                
            case ShapeTypes.NotPrimitive:
            case ShapeTypes.NotchedCircularArrow:
                shape = new HSSFFreeform(workbook, spContainer, parent, anchor, type);
                break;
                
            default:
                shape = new HSSFAutoShape(workbook, spContainer, parent, anchor, type);
                ((HSSFAutoShape)shape).setAdjustmentValue(spContainer);
                break;
        }
        return shape;
    }
    
    

//    /**
//     * build shape tree from escher container
//     * @param container root escher container from which escher records must be taken
//     * @param agg - EscherAggregate
//     * @param out - shape container to which shapes must be added
//     * @param root - node to create HSSFObjectData shapes
//     */
//    public static void createShapeTree(EscherContainerRecord container, Map<EscherRecord, Record> shapeToObj, HSSFShapeContainer out, DirectoryNode root)
//    {
//        if (container.getRecordId() == EscherContainerRecord.SPGR_CONTAINER) 
//        {
//            ObjRecord obj = null;
//            EscherClientDataRecord clientData = ((EscherContainerRecord) container.getChild(0)).getChildById(EscherClientDataRecord.RECORD_ID);
//            if (null != clientData)
//            {
//                obj = (ObjRecord) shapeToObj.get(clientData);
//            }
//            HSSFShapeGroup group = new HSSFShapeGroup(container, obj);
//            List<EscherContainerRecord> children = container.getChildContainers();
//            // skip the first child record, it is group descriptor
//            for (int i = 0; i < children.size(); i++) 
//            {
//                EscherContainerRecord spContainer = children.get(i);
//                if (i != 0)
//                {
//                    createShapeTree(spContainer, shapeToObj, group, root);
//                }
//            }
//            out.addShape(group);
//        } 
//        else if (container.getRecordId() == EscherContainerRecord.SP_CONTAINER)
//        {
//            ObjRecord objRecord = null;
//            TextObjectRecord txtRecord = null;
//
//            for (EscherRecord record : container.getChildRecords()) {
//                switch (record.getRecordId()) 
//                {
//                    case EscherClientDataRecord.RECORD_ID:
//                        objRecord = (ObjRecord) shapeToObj.get(record);
//                        break;
//                    case EscherTextboxRecord.RECORD_ID:
//                        txtRecord = (TextObjectRecord) shapeToObj.get(record);
//                        break;
//                }
//            }
//            if (isEmbeddedObject(objRecord))
//            {
//                HSSFObjectData objectData = new HSSFObjectData(container, objRecord, root);
//                out.addShape(objectData);
//                return;
//            }
//            CommonObjectDataSubRecord cmo = (CommonObjectDataSubRecord) objRecord.getSubRecords().get(0);
//            HSSFShape shape;
//            switch (cmo.getObjectType()) 
//            {
//                case CommonObjectDataSubRecord.OBJECT_TYPE_PICTURE:
//                    shape = new HSSFPicture(container, objRecord);
//                    break;
//                case CommonObjectDataSubRecord.OBJECT_TYPE_RECTANGLE:
//                    shape = new HSSFSimpleShape(container, objRecord, txtRecord);
//                    break;
//                case CommonObjectDataSubRecord.OBJECT_TYPE_LINE:
//                    shape = new HSSFSimpleShape(container, objRecord);
//                    break;
//                case CommonObjectDataSubRecord.OBJECT_TYPE_COMBO_BOX:
//                    shape = new HSSFCombobox(container, objRecord);
//                    break;
//                case CommonObjectDataSubRecord.OBJECT_TYPE_MICROSOFT_OFFICE_DRAWING:
//                    EscherOptRecord optRecord = container.getChildById(EscherOptRecord.RECORD_ID);
//                    EscherProperty property = optRecord.lookup(EscherProperties.GEOMETRY__VERTICES);
//                    if (null != property)
//                    {
//                        shape = new HSSFPolygon(container, objRecord, txtRecord);
//                    } 
//                    else 
//                    {
//                        shape = new HSSFSimpleShape(container, objRecord, txtRecord);
//                    }
//                    break;
//                case CommonObjectDataSubRecord.OBJECT_TYPE_TEXT:
//                    shape = new HSSFTextbox(container, objRecord, txtRecord);
//                    break;
//                case CommonObjectDataSubRecord.OBJECT_TYPE_COMMENT:
//                    shape = new HSSFComment(container, objRecord, txtRecord, agg.getNoteRecordByObj(objRecord));
//                    break;
//                default:
//                    shape = new HSSFSimpleShape(container, objRecord, txtRecord);
//            }
//            out.addShape(shape);
//        }
//    }

    private static boolean isEmbeddedObject(ObjRecord obj) 
    {
        Iterator<SubRecord> subRecordIter = obj.getSubRecords().iterator();
        while (subRecordIter.hasNext()) 
        {
            SubRecord sub = subRecordIter.next();
            if (sub instanceof EmbeddedObjectRefSubRecord)
            {
                return true;
            }
        }
        return false;
    }

}
