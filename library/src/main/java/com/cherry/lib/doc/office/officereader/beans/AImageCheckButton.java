/*
 * 文件名称:          AImageCheckButton.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:12:10
 */
package com.cherry.lib.doc.office.officereader.beans;


import com.cherry.lib.doc.office.common.PaintKit;
import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.office.system.IControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-3-8
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class AImageCheckButton  extends AImageButton
{
    //disable
    public static final short DISABLE = 0;
    //checked
    public static final short CHECK = 1;
    //unchecked
    public static final short UNCHECK = 2;
    
    
    
    /**
     * 
     * @param context
     * @param control
     * @param toolstip
     * @param checkIconResID
     * @param uncheckIconResID
     * @param iconResIdDisable
     * @param actionID
     */
    public AImageCheckButton(Context context, IControl control, 
        String checkTips, String uncheckTips,
        int checkIconResID, int uncheckIconResID,        
        int iconResIdDisable, int actionID)
    {
        super(context, control, checkTips, checkIconResID, iconResIdDisable, actionID);         
        this.uncheckTips = uncheckTips;
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inDensity = context.getResources().getDisplayMetrics().densityDpi;  
        opts.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        uncheckBitmap = BitmapFactory.decodeResource(context.getResources(), uncheckIconResID, opts);
        
    }
    
    /**
     */
    public void onDraw(Canvas canvas)
    {
        Paint paint = PaintKit.instance().getPaint();
        switch(state)
        {
            case DISABLE:
                canvas.drawBitmap(bitmapDisable, (getWidth() - bitmapDisable.getWidth()) / 2, (getHeight() - bitmapDisable.getHeight()) / 2, paint);
                break;
            case CHECK:
                canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, (getHeight() - bitmap.getHeight()) / 2, paint);
                break;
            case UNCHECK:
                canvas.drawBitmap(uncheckBitmap, (getWidth() - uncheckBitmap.getWidth()) / 2, (getHeight() - uncheckBitmap.getHeight()) / 2, paint);
                break;
        }        
    }
    
    /**
     * 
     */
    public void onLongPress(MotionEvent e)
    {
        longPressed = true;        
        switch(state)
        {
            case CHECK:
                control.actionEvent(EventConstant.SYS_SHOW_TOOLTIP, toolstip);
                break;
            case UNCHECK:
                control.actionEvent(EventConstant.SYS_SHOW_TOOLTIP, uncheckTips);
                break;
        } 
    }
    
    
    /**
     * 单击事件
     */
    public void onClick(View v)
    {
        if (longPressed)
        {
            longPressed = false;
            return;
        }
        switch(state)
        {
            case CHECK:
                setState(UNCHECK);
                break;
            case UNCHECK:
                setState(CHECK);
                break;
        }
        control.actionEvent(((AImageButton)v).getActionID(), (state == CHECK));
        postInvalidate();
        longPressed = false;
    }
    
    
    /**
     * 
     * @param state
     */
    public void setState(short state)
    {
        this.state = state;
        setEnabled(state != DISABLE);
    }
    
    /**
     * 
     * @return
     */
    public short getState()
    {
        return state;
    }
    
    public void dispose()
    {
        super.dispose();
        
        if (uncheckBitmap != null)
        {
            uncheckBitmap.recycle();
            uncheckBitmap = null;
        }
    }
    
    
    
    // 显示图片
    protected Bitmap uncheckBitmap;
    //state
    private short state;
    //uncheck tips
    protected String uncheckTips;
}
