/*
 * 文件名称:           Frame.java
 * 
 * 编译器:             android2.2
 * 时间:               下午1:34:44
 */
package com.cherry.lib.doc.office.officereader;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

/**
 * 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            梁金晶
 * <p>
 * 日期:            2011-10-27
 * <p>
 * 负责人:          梁金晶
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AppFrame extends LinearLayout
{

    public AppFrame(Context context)
    {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);
    }
    
    /**
     * 
     */
    public void dispose()
    {
        //removeAllViews();
    }
}
