/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.nvqquy98.lib.doc.office.fc.hssf.usermodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nvqquy98.lib.doc.office.fc.ddf.EscherBSERecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherComplexProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperty;
import com.nvqquy98.lib.doc.office.fc.hssf.record.EscherAggregate;
import com.nvqquy98.lib.doc.office.fc.ss.usermodel.Chart;
import com.nvqquy98.lib.doc.office.fc.ss.usermodel.Drawing;
import com.nvqquy98.lib.doc.office.fc.util.Internal;
import com.nvqquy98.lib.doc.office.fc.util.StringUtil;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ASheet;



/**
 * The patriarch is the toplevel container for shapes in a sheet.  It does
 * little other than act as a container for other shapes and groups.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public final class HSSFPatriarch implements HSSFShapeContainer, Drawing {
    private List<HSSFShape> _shapes = new ArrayList<HSSFShape>();
    private int _x1 = 0;
    private int _y1  = 0 ;
    private int _x2 = 1023;
    private int _y2 = 255;

    /**
     * The EscherAggregate we have been bound to.
     * (This will handle writing us out into records,
     *  and building up our shapes from the records)
     */
    private EscherAggregate _boundAggregate;
	protected ASheet _sheet; // TODO make private

    /**
     * Creates the patriarch.
     *
     * @param sheet the sheet this patriarch is stored in.
     */
    public HSSFPatriarch(ASheet sheet, EscherAggregate boundAggregate){
        _sheet = sheet;
		_boundAggregate = boundAggregate;
    }

    /**
     * Creates a new group record stored under this patriarch.
     *
     * @param anchor    the client anchor describes how this group is attached
     *                  to the sheet.
     * @return  the newly created group.
     */
    public HSSFShapeGroup createGroup(HSSFClientAnchor anchor)
    {
        HSSFShapeGroup group = new HSSFShapeGroup(null, null, anchor);
        group.setAnchor(anchor);
        addShape(group);
        return group;
    }

    /**
     * Creates a simple shape.  This includes such shapes as lines, rectangles,
     * and ovals.
     *
     * @param anchor    the client anchor describes how this group is attached
     *                  to the sheet.
     * @return  the newly created shape.
     */
    public HSSFSimpleShape createSimpleShape(HSSFClientAnchor anchor)
    {
        HSSFSimpleShape shape = new HSSFSimpleShape(null, null, anchor);
        shape.setAnchor(anchor);
        addShape(shape);
        return shape;
    }

    /**
     * Creates a picture.
     *
     * @param anchor    the client anchor describes how this group is attached
     *                  to the sheet.
     * @return  the newly created shape.
     */
    public HSSFPicture createPicture(HSSFClientAnchor anchor, int pictureIndex)
    {
        HSSFPicture shape = new HSSFPicture(null, null, anchor);
        shape.setPictureIndex( pictureIndex );
        shape.setAnchor(anchor);
        addShape(shape);

        EscherBSERecord bse = _sheet.getAWorkbook().getInternalWorkbook().getBSERecord(pictureIndex);
        bse.setRef(bse.getRef() + 1);
        return shape;
    }

    public HSSFPicture createPicture(IClientAnchor anchor, int pictureIndex)
    {
        return createPicture((HSSFClientAnchor)anchor, pictureIndex);
    }

    /**
     * Creates a polygon
     *
     * @param anchor    the client anchor describes how this group is attached
     *                  to the sheet.
     * @return  the newly created shape.
     */
    public HSSFPolygon createPolygon(HSSFClientAnchor anchor)
    {
        HSSFPolygon shape = new HSSFPolygon(null, null, anchor);
        shape.setAnchor(anchor);
        addShape(shape);
        return shape;
    }

    /**
     * Constructs a textbox under the patriarch.
     *
     * @param anchor    the client anchor describes how this group is attached
     *                  to the sheet.
     * @return      the newly created textbox.
     */
    public HSSFTextbox createTextbox(HSSFClientAnchor anchor)
    {
        HSSFTextbox shape = new HSSFTextbox(null, null, anchor);
        shape.setAnchor(anchor);
        addShape(shape);
        return shape;
    }

    /**
     * Constructs a cell comment.
     *
     * @param anchor    the client anchor describes how this comment is attached
     *                  to the sheet.
     * @return      the newly created comment.
     */
   public HSSFComment createComment(HSSFAnchor anchor)
    {
        HSSFComment shape = new HSSFComment(null, null, anchor);
        shape.setAnchor(anchor);
        addShape(shape);
        return shape;
    }

    /**
     * YK: used to create autofilters
     *
     * @see com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFSheet#setAutoFilter(org.apache.poi.ss.util.CellRangeAddress)
     */
     HSSFSimpleShape createComboBox(HSSFAnchor anchor)
     {
         HSSFSimpleShape shape = new HSSFSimpleShape(null, null, anchor);
         shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_COMBO_BOX);
         shape.setAnchor(anchor);
         addShape(shape);
         return shape;
     }

    public HSSFComment createCellComment(IClientAnchor anchor) {
        return createComment((HSSFAnchor)anchor);
    }

    /**
     * Returns a list of all shapes contained by the patriarch.
     */
    public List<HSSFShape> getChildren()
    {
        return _shapes;
    }

    /**
     * add a shape to this drawing
     */
    @Internal
    public void addShape(HSSFShape shape){
        shape._patriarch = this;
        _shapes.add(shape);
    }

    /**
     * Total count of all children and their children's children.
     */
    public int countOfAllChildren() {
        int count = _shapes.size();
        for (Iterator<HSSFShape> iterator = _shapes.iterator(); iterator.hasNext();) {
            HSSFShape shape = iterator.next();
            count += shape.countOfAllChildren();
        }
        return count;
    }
    /**
     * Sets the coordinate space of this group.  All children are constrained
     * to these coordinates.
     */
    public void setCoordinates(int x1, int y1, int x2, int y2){
        _x1 = x1;
        _y1 = y1;
        _x2 = x2;
        _y2 = y2;
    }

    /**
     * Does this HSSFPatriarch contain a chart?
     * (Technically a reference to a chart, since they
     *  get stored in a different block of records)
     * FIXME - detect chart in all cases (only seems
     *  to work on some charts so far)
     */
    public boolean containsChart() {
        // TODO - support charts properly in usermodel

        // We're looking for a EscherOptRecord
        EscherOptRecord optRecord = (EscherOptRecord)
            _boundAggregate.findFirstWithId(EscherOptRecord.RECORD_ID);
        if(optRecord == null) {
            // No opt record, can't have chart
            return false;
        }

        for(Iterator<EscherProperty> it = optRecord.getEscherProperties().iterator(); it.hasNext();) {
            EscherProperty prop = it.next();
            if(prop.getPropertyNumber() == 896 && prop.isComplex()) {
                EscherComplexProperty cp = (EscherComplexProperty)prop;
                String str = StringUtil.getFromUnicodeLE(cp.getComplexData());

                if(str.equals("Chart 1\0")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * The top left x coordinate of this group.
     */
    public int getX1()
    {
        return _x1;
    }

    /**
     * The top left y coordinate of this group.
     */
    public int getY1()
    {
        return _y1;
    }

    /**
     * The bottom right x coordinate of this group.
     */
    public int getX2()
    {
        return _x2;
    }

    /**
     * The bottom right y coordinate of this group.
     */
    public int getY2()
    {
        return _y2;
    }

    /**
     * Returns the aggregate escher record we're bound to
     */
    protected EscherAggregate _getBoundAggregate() {
        return _boundAggregate;
    }

    /**
     * Creates a new client anchor and sets the top-left and bottom-right
     * coordinates of the anchor.
     *
     * @param dx1  the x coordinate in EMU within the first cell.
     * @param dy1  the y coordinate in EMU within the first cell.
     * @param dx2  the x coordinate in EMU within the second cell.
     * @param dy2  the y coordinate in EMU within the second cell.
     * @param col1 the column (0 based) of the first cell.
     * @param row1 the row (0 based) of the first cell.
     * @param col2 the column (0 based) of the second cell.
     * @param row2 the row (0 based) of the second cell.
     * @return the newly created client anchor
     */
    public HSSFClientAnchor createAnchor(int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2){
        return new HSSFClientAnchor(dx1, dy1, dx2, dy2, (short)col1, row1, (short)col2, row2);
    }

	public Chart createChart(IClientAnchor anchor) {
		throw new RuntimeException("NotImplemented");
	}

	public void dispose()
	{
	    _shapes.clear();
	    _shapes = null;
	    
	    _boundAggregate = null;	    
	    _sheet = null;
	}
}
