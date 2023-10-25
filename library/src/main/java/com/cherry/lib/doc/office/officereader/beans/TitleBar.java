/*
 * 文件名称:          TitleBar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:31:38
 */
package com.cherry.lib.doc.office.officereader.beans;

import com.cherry.lib.doc.office.constant.MainConstant;
import com.cherry.lib.doc.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * TODO: app title bar view
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-11-14
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class TitleBar extends LinearLayout
{   
    public TitleBar(Context context)
    {
        super(context);
        
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),  R.drawable.sys_button_focus_bg_vertical, opts);
        height = opts.outHeight;        

        setBackgroundResource(R.drawable.sys_button_focus_bg_vertical);
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(24);
        FontMetrics fm = paint.getFontMetrics();
        
        yPostion = (height - fm.descent + fm.ascent) / 2  -  fm.ascent;
        
        mBusyIndicator = new ProgressBar(getContext());
        mBusyIndicator.setIndeterminate(true);
        //mBusyIndicator.setBackgroundResource(R.drawable.busy);
        addView(mBusyIndicator);
        mBusyIndicator.setVisibility(GONE);
    }
    
    public int getTitleHeight()
    {
        return height;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
        this.postInvalidate();
    }
    
    /**
     * 
     *
     */
    protected void onDraw(Canvas canvas)
    {
        if(title != null)
        {
            canvas.drawText(title, MainConstant.GAP, yPostion, paint);
        }
    }
    
    
    public void showProgressBar(boolean visible)
    {
        mBusyIndicator.setVisibility(visible ? VISIBLE : GONE);
    }
    
    /**
     * 
     *
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBusyIndicator != null)
        {
            int limit = Math.min(this.getWidth(), getHeight()) / 2;
            mBusyIndicator.measure(View.MeasureSpec.AT_MOST
                | limit, View.MeasureSpec.AT_MOST  | limit);

        }
    }
    
    /**
     * 
     * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
     *
     */
    @ Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        
        int w = right - left;
        int h = bottom - top;
        if (mBusyIndicator != null)
        {
            int bw = mBusyIndicator.getMeasuredWidth();
            int bh = mBusyIndicator.getMeasuredHeight();

            mBusyIndicator.layout(w - bw - MainConstant.GAP, (h - bh) / 2, w - MainConstant.GAP, (h + bh) / 2);
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        paint = null;
        title = null;
        
        if (mBusyIndicator != null)
        {
            removeView(mBusyIndicator);
            mBusyIndicator = null;
        }
    }
    
    private String title;
    private int height;
    private Paint paint;
    private float yPostion;    

    //
    private ProgressBar mBusyIndicator;
}
