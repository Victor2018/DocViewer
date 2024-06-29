/*
 * 文件名称:          PaintKit.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:42:18
 */
package com.nvqquy98.lib.doc.office.common;

import com.nvqquy98.lib.doc.office.constant.SSConstant;

import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-7
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class PaintKit
{
    private Paint paint = null;
    private static PaintKit pk = new PaintKit();
    /**
     * 
     */
    private PaintKit()
    {
        paint = new Paint();
        paint.setTextSize(SSConstant.HEADER_TEXT_FONTSZIE);
        paint.setTypeface(Typeface.SERIF);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 
     * @return
     */
    public static PaintKit instance()
    {
        return pk;
    }  
    
    public Paint getPaint()
    {
        paint.reset();
        paint.setAntiAlias(true);
        
        return paint;
    }
}
