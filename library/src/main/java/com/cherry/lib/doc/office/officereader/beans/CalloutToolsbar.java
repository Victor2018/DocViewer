
package com.cherry.lib.doc.office.officereader.beans;

import com.cherry.lib.doc.R;
import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.office.system.IControl;

import android.content.Context;
import android.util.AttributeSet;

public class CalloutToolsbar extends AToolsbar
{

    /**
     * 
     * @param context
     * @param attrs
     */
    public CalloutToolsbar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 
     * @param context
     * @param control
     */
    public CalloutToolsbar(Context context, IControl control)
    {
        super(context, control);
        init();
        toolsbarFrame.setBackgroundResource(R.drawable.sys_toolsbar_button_bg_normal);
    }

    private void init()
    {
        // 标签
        addButton(R.drawable.app_back, R.drawable.app_back_disable, R.string.app_toolsbar_back,
            EventConstant.APP_BACK_ID, true);

        // 画笔
        addCheckButton(R.drawable.app_pen_check, R.drawable.app_pen, R.drawable.app_pen,
            R.string.app_toolsbar_pen_check, R.string.app_toolsbar_pen, EventConstant.APP_PEN_ID,
            true);

        // 擦除
        addCheckButton(R.drawable.app_eraser_check, R.drawable.app_eraser,
            R.drawable.app_eraser_disable, R.string.app_toolsbar_eraser_check,
            R.string.app_toolsbar_eraser, EventConstant.APP_ERASER_ID, true);

        // 标签
        addButton(R.drawable.app_color, R.drawable.app_color_disable, R.string.app_toolsbar_color,
            EventConstant.APP_COLOR_ID, true);

    }
}
