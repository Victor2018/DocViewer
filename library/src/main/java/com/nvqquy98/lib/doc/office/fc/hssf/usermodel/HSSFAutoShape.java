/*
 * 文件名称:           HSSFAutoShape.java
 *  
 * 编译器:             android2.2
 * 时间:               下午3:37:10
 */
package com.nvqquy98.lib.doc.office.fc.hssf.usermodel;

import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.AWorkbook;

/**
 * autoshape data
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-3-27
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class HSSFAutoShape extends HSSFTextbox
{   
    public HSSFAutoShape(AWorkbook workbook, EscherContainerRecord escherContainer, HSSFShape parent, 
           HSSFAnchor anchor, int shapeType)
    {
        super(escherContainer, parent, anchor);
        
        setShapeType(shapeType);
        processLineWidth();
        processLine(escherContainer, workbook);
        processSimpleBackground(escherContainer, workbook);
        processRotationAndFlip(escherContainer);
        
        //word art
    	String unicodeText = ShapeKit.getUnicodeGeoText(escherContainer);
    	if(unicodeText != null && unicodeText.length() > 0)
    	{
    		setString(new HSSFRichTextString(unicodeText));
    		setWordArt(true);
    		
    		setNoFill(true);
    		setFontColor(getFillColor());
    	}   
    } 

    
    public Float[] getAdjustmentValue()
    {
        return adjusts;
    }
    
    public void setAdjustmentValue(EscherContainerRecord escherContainer)
    {
        adjusts = ShapeKit.getAdjustmentValue(escherContainer);
    }    
    
    private Float adjusts[];
}
