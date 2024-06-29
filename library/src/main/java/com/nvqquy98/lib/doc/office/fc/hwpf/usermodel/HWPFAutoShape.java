/*
 * 文件名称:           HWPFAutoShape.java
 *  
 * 编译器:             android2.2
 * 时间:               上午9:49:40
 */
package com.nvqquy98.lib.doc.office.fc.hwpf.usermodel;

import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-4-19
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class HWPFAutoShape extends HWPFShape
{
    public HWPFAutoShape(EscherContainerRecord escherRecord, HWPFShape parent)
    {
        super(escherRecord, parent);
    }
    
    public String getShapeName()
    {
    	return ShapeKit.getShapeName(escherContainer);
    }
}
