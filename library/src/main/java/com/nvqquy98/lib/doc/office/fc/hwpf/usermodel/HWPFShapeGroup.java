/*
 * 文件名称:           HWPFShapeGroup.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:16:51
 */
package com.nvqquy98.lib.doc.office.fc.hwpf.usermodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherChildAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpgrRecord;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-4-22
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class HWPFShapeGroup extends HWPFShape
{
    public HWPFShapeGroup(EscherContainerRecord escherRecord, HWPFShape parent)
    {
        super(escherRecord, parent);
    }
    
    public Rectangle getCoordinates(float zoomX, float zoomY)
    {
        Rectangle anchor = null;
        EscherContainerRecord spContainer = (EscherContainerRecord)getSpContainer().getChild(0);
        if (spContainer != null)
        {
            EscherSpgrRecord spgr = (EscherSpgrRecord)ShapeKit.getEscherChild(spContainer, EscherSpgrRecord.RECORD_ID);
            if (spgr != null)
            {
                anchor = new Rectangle();
                anchor.x = (int)(spgr.getRectX1() * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.y = (int)(spgr.getRectY1() * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.width = (int)((spgr.getRectX2() - spgr.getRectX1()) * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.height = (int)((spgr.getRectY2() - spgr.getRectY1()) * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
            }
        }
        return anchor;
    }   
    
    public Rectangle getAnchor(float zoomX, float zoomY)
    {
        Rectangle anchor = null;
        EscherContainerRecord spContainer = (EscherContainerRecord)getSpContainer().getChild(0);
        if (spContainer != null)
        {
            EscherClientAnchorRecord clrec = (EscherClientAnchorRecord)ShapeKit.getEscherChild(
                spContainer, EscherClientAnchorRecord.RECORD_ID);
            if (clrec == null)
            {
                EscherChildAnchorRecord rec = (EscherChildAnchorRecord)ShapeKit.getEscherChild(spContainer,
                    EscherChildAnchorRecord.RECORD_ID);
                if (rec != null)
                {
                    anchor = new Rectangle();
                    anchor.x = (int)(rec.getDx1() * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.y = (int)(rec.getDy1() * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.width = (int)((rec.getDx2() - rec.getDx1()) * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                    anchor.height = (int)((rec.getDy2() - rec.getDy1()) * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                }
            }
            else
            {
                anchor = new Rectangle();
                anchor.x = (int)(clrec.getCol1() * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.y = (int)(clrec.getFlag() * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.width = (int)((clrec.getDx1() - clrec.getCol1()) * zoomX * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
                anchor.height = (int)((clrec.getRow1() - clrec.getFlag()) * zoomY * MainConstant.PIXEL_DPI / ShapeKit.EMU_PER_INCH);
            }
        }

        return anchor;
    }
    
    public float[] getShapeAnchorFit(Rectangle rect, float zoomX, float zoomY)
    {
        float[] zoom = {1.0f, 1.0f};
        EscherContainerRecord spContainer = (EscherContainerRecord)getSpContainer().getChild(0);
        if (spContainer != null)
        {
            EscherSpgrRecord spgr = (EscherSpgrRecord)ShapeKit.getEscherChild(spContainer, EscherSpgrRecord.RECORD_ID);
            if (spgr != null)
            {
                float w = spgr.getRectX2() - spgr.getRectX1();
                float h = spgr.getRectY2() - spgr.getRectY1();
                if (w != 0 && h != 0)
                {
                    zoom[0] = rect.width * MainConstant.EMU_PER_INCH / MainConstant.PIXEL_DPI / zoomX / w;
                    zoom[1] = rect.height * MainConstant.EMU_PER_INCH / MainConstant.PIXEL_DPI / zoomY / h;
                }
            }
        }
        return zoom;
    }
    
    /**
     * Whether the shape is horizontally flipped
     *
     * @return whether the shape is horizontally flipped
     */
    public boolean getFlipHorizontal()
    {
        return ShapeKit.getGroupFlipHorizontal(getSpContainer());
    }

    /**
     * Whether the shape is vertically flipped
     *
     * @return whether the shape is vertically flipped
     */
    public boolean getFlipVertical()
    {
        return ShapeKit.getGroupFlipVertical(getSpContainer());
    }
    
    public int getGroupRotation()
    {
        return ShapeKit.getGroupRotation(getSpContainer());
    }
    
    /**
     * @return the shapes contained in this group container
     */
    public HWPFShape[] getShapes()
    {
        Iterator<EscherRecord> iter = getSpContainer().getChildIterator();
        if (iter.hasNext())
        {
            iter.next();
        }
        List<HWPFShape> shapeList = new ArrayList<HWPFShape>();
        while (iter.hasNext())
        {
            EscherRecord r = iter.next();
            if (r instanceof EscherContainerRecord)
            {
                EscherContainerRecord container = (EscherContainerRecord)r;
                HWPFShape shape = HWPFShapeFactory.createShape(container, this);
                shapeList.add(shape);
            }
        }

        HWPFShape[] shapes = shapeList.toArray(new HWPFShape[shapeList.size()]);
        return shapes;
    }
    
    public String getShapeName()
    {
    	return ShapeKit.getShapeName(escherContainer);
    }
}
