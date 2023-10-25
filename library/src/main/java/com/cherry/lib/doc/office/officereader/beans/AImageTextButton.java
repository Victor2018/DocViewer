/*
 * 文件名称:           ImageTextButton.java
 * 
 * 编译器:             android2.2
 * 时间:               下午1:34:44
 */

package com.cherry.lib.doc.office.officereader.beans;


import com.cherry.lib.doc.office.constant.MainConstant;
import com.cherry.lib.doc.office.system.IControl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

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
public class AImageTextButton extends AImageButton
{
    // 文本在图片上面
    public static final int TEXT_TOP = 0;// 0
    // 文本在图片下面
    public static final int TEXT_BOTTOM = TEXT_TOP + 1; // 1
    // 文本在图片左面
    public static final int TEXT_LEFT = TEXT_BOTTOM + 1;// 2
    // 文本在图片左面
    public static final int TEXT_RIGHT = TEXT_LEFT + 1;// 3
    //
    public static final int GAP = MainConstant.GAP;
    
    /**
     * 
     * @param context           Activity实例
     * @param control           Control实例
     * @param text              显示的文本
     * @param tooltip           toolstip文本
     * @param iconResID         显示的图标ID
     * @param iconResIdDisable  button不可用时显示图片，如查不需要处理请传入-1
     * @param actionID          button的ActionID
     * @param textGravity       文本相对图标的位置
     * @param fontSize          文本字体大小
     */
    public AImageTextButton(Context context, IControl control, String text, String toolstip, 
        int iconResID, int iconResIdDisable, int actionID, int textGravity, int fontSize)
    {
        super(context, control, toolstip, iconResID, iconResIdDisable, actionID);
        setEnabled(true);
        this.text = text;
        paint = new Paint();
        // 文字相对图片位置必须有效
        if (textGravity >= TEXT_TOP &&  textGravity <= TEXT_RIGHT)
        {
            this.textGravity = textGravity;
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
            paint.setTextSize(fontSize);
            if (text != null && text.length() > 0)
            {
                textWidth = (int)paint.measureText(text);
                textHeight = (int)Math.ceil((paint.descent() - paint.ascent()));
            }
        }

    }
    
    /**
     * 
     *
     */
    public void onDraw(Canvas canvas)
    {
        Rect clip = canvas.getClipBounds();
        // 绘制区域宽度
        int w = clip.right - clip.left;
        // 绘制区域高度
        int h = clip.bottom - clip.top;
        // icon 宽度
        int bW = bitmap.getWidth();
        // icon 高度
        int bH = bitmap.getHeight();
        int x;
        int y;
        // 文本在上面
        if (textGravity == TEXT_TOP)
        {
            x = w - textWidth / 2;
            y = (h - bH - GAP * 2 - textHeight) / 2;
            
            canvas.drawText(text, x, y - paint.ascent(), paint);
            y = h - bH - GAP;
            x = (w - bW) / 2;
            canvas.drawBitmap(bitmap, x, y, paint);
        }
        // 文本在下面
        else if (textGravity == TEXT_BOTTOM)
        {
        	topIndent = (h - bH - GAP * 6 - textHeight) / 2;
            x = (w - bW) / 2;
            y = topIndent;
            canvas.drawBitmap(bitmap, x, y, paint);
            
            y = bH  + topIndent + GAP * 6;
            x = (w - textWidth) / 2;
            canvas.drawText(text, x, y - paint.ascent(), paint);
        }
        // 文本在左边
        else if (textGravity == TEXT_LEFT)
        {
            x = (w - textWidth - bW - GAP * 2) / 2;
            y = (h - textHeight) / 2;
            canvas.drawText(text, x, y - paint.ascent(), paint);
            
            y = (h - bH) / 2;
            x = w - bW - GAP;
            canvas.drawBitmap(bitmap, x, y, paint);
        }
        // 文本在左边
        else if (textGravity == TEXT_RIGHT)
        {
        	leftIndent = w / 10;
            y = (h - bH) / 2;
            x = leftIndent;
            canvas.drawBitmap(bitmap, x, y, paint);
            
            y = (h - textHeight) / 2;
            x = bW + leftIndent + GAP * 6;
            canvas.drawText(text, x, y - paint.ascent(), paint);
        }
    }

    /**
     * @return Returns the topIndent.
     */
    public int getTopIndent()
    {
        return topIndent;
    }

    /**
     * @param topIndent The topIndent to set.
     */
    public void setTopIndent(int topIndent)
    {
        this.topIndent = topIndent;
    }

    /**
     * @return Returns the bottomIndent.
     */
    public int getBottomIndent()
    {
        return bottomIndent;
    }

    /**
     * @param bottomIndent The bottomIndent to set.
     */
    public void setBottomIndent(int bottomIndent)
    {
        this.bottomIndent = bottomIndent;
    }

    /**
     * @return Returns the leftIndent.
     */
    public int getLeftIndent()
    {
        return leftIndent;
    }

    /**
     * @param leftIndent The leftIndent to set.
     */
    public void setLeftIndent(int leftIndent)
    {
        this.leftIndent = leftIndent;
    }

    /**
     * @return Returns the rightIndent.
     */
    public int getRightIndent()
    {
        return rightIndent;
    }

    /**
     * @param rightIndent The rightIndent to set.
     */
    public void setRightIndent(int rightIndent)
    {
        this.rightIndent = rightIndent;
    }
    
    /**
     * 
     *
     */
    public void dispose()
    {
        super.dispose();
        text = null;
    }

    // 见常量定义
    private int textGravity = -1;
    //
    private int textWidth;
    //
    private int textHeight;
    // 上边距
    private int topIndent;
    // 下边距
    private int bottomIndent;
    // 左边距
    private int leftIndent;
    // 右边距
    private int rightIndent;
    // 显示文本
    private String text;
    
    private Paint paint;
}
