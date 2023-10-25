/*
 * 文件名称:          Toolbar.java
 *
 * 编译器:            android2.2
 * 时间:              下午1:48:31
 */
package com.cherry.lib.doc.office.officereader.beans;

import java.util.HashMap;
import java.util.Map;

import com.cherry.lib.doc.R;
import com.cherry.lib.doc.office.system.IControl;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
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
public class AToolsbar extends HorizontalScrollView
{
    /**
     *
     * @param control
     */
    public AToolsbar(Context content, IControl control)
    {
        super(content);
        this.control = control;
        this.setAnimation(true);
        this.setVerticalFadingEdgeEnabled(false);
        this.setFadingEdgeLength(0);
        toolsbarFrame = new LinearLayout(content);
        // 在同一水平线
        toolsbarFrame.setOrientation(LinearLayout.HORIZONTAL);
        toolsbarFrame.setMinimumWidth(this.getResources().getDisplayMetrics().widthPixels);
        // button size
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),  R.drawable.sys_toolsbar_button_bg_normal, opts);
        this.buttonWidth = opts.outWidth;
        this.buttonHeight = opts.outHeight;
        toolsbarFrame.setBackgroundColor(getResources().getColor(R.color.purple_500));
//        toolsbarFrame.setBackgroundResource(R.drawable.sys_toolsbar_button_bg_normal);
        addView(toolsbarFrame, new LayoutParams(LayoutParams.MATCH_PARENT, buttonHeight));
    }


    /**
     *
     * @param context
     * @param attrs
     */
    public AToolsbar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     *
     *
     */
    public void onConfigurationChanged(Configuration newConfig)
    {
        toolsbarFrame.setMinimumWidth(this.getResources().getDisplayMetrics().widthPixels);
    }

    /**
     *
     * @param iconResIdDisable
     * @param actionID
     * @return
     */
    protected AImageButton addButton(int iconResID, int iconResIdDisable,
        int tooltipResID, int actionID, boolean isAddSeparated)
    {
        Context context = getContext();
        Resources res = context.getResources();
        AImageButton tb = new AImageButton(context, control,
            res.getString(tooltipResID), iconResID, iconResIdDisable, actionID);
        tb.setNormalBgResID(R.drawable.sys_toolsbar_button_bg_normal);
        tb.setPushBgResID(R.drawable.sys_toolsbar_button_bg_push);
        tb.setLayoutParams(new LayoutParams(buttonWidth, buttonHeight));
        toolsbarFrame.addView(tb);

        toolsbarWidth += buttonWidth;
        if (actionButtonIndex == null)
        {
            actionButtonIndex = new HashMap<Integer, Integer>();
        }
        actionButtonIndex.put(actionID, toolsbarFrame.getChildCount() - 1);

        /*if (isAddSeparated)
        {
            addSeparated();
        }*/
        return tb;
    }

    /**
     *
     * @param checkIconResID
     * @param uncheckIconResID
     * @param iconResIdDisable
     * @param checktipResID
     * @param unchecktipResID
     * @param actionID
     * @param isAddSeparated
     * @return
     */
    protected AImageCheckButton addCheckButton(int checkIconResID, int uncheckIconResID, int iconResIdDisable,
        int checktipResID, int unchecktipResID, int actionID, boolean isAddSeparated)
    {
        Context context = getContext();
        Resources res = context.getResources();
        AImageCheckButton tb = new AImageCheckButton(context, control,
            res.getString(checktipResID), res.getString(unchecktipResID), checkIconResID, uncheckIconResID,
            iconResIdDisable, actionID);

        tb.setNormalBgResID(R.drawable.sys_toolsbar_button_bg_normal);
        tb.setPushBgResID(R.drawable.sys_toolsbar_button_bg_push);
        tb.setLayoutParams(new LayoutParams(buttonWidth, buttonHeight));
        //tb.setOnClickListener(this);
        toolsbarFrame.addView(tb);

        toolsbarWidth += buttonWidth;
        if (actionButtonIndex == null)
        {
            actionButtonIndex = new HashMap<Integer, Integer>();
        }
        actionButtonIndex.put(actionID, toolsbarFrame.getChildCount() - 1);

       /* if (isAddSeparated)
        {
            addSeparated();
        }*/
        return tb;


    }
    /**
     * 添加分隔符
     */
    protected void addSeparated()
    {
        toolsbarFrame.addView(new AImageButton(getContext(), control, "",
            R.drawable.sys_toolsbar_separated_horizontal, -1,  -1),
            new LayoutParams(1, buttonHeight));
        toolsbarWidth += 1;
    }

    /**
     *
     *
     */
    public void onDraw(Canvas canvas)
    {
        if (isAnimation())
        {
            setAnimation(false);
            if (toolsbarFrame.getWidth() > getResources().getDisplayMetrics().widthPixels)
            {
                scrollTo(buttonWidth * 3, 0);
            }
            fling(-4000);
        }
        super.onDraw(canvas);
    }

    /**
     * set button enable/disable
     * @param actionID
     * @param enabled
     */
    public void setEnabled(int actionID, boolean enabled)
    {
        Integer index = actionButtonIndex.get(actionID);
        if (index != null && index >= 0 && index < toolsbarFrame.getChildCount())
        {
            toolsbarFrame.getChildAt(index).setEnabled(enabled);
        }
    }

    /**
     * set check button state
     * @param actionID
     * @param state
     */
    public void setCheckState(int actionID, short state)
    {
        int index = actionButtonIndex.get(actionID);
        if (index >= 0 && index < toolsbarFrame.getChildCount()
            && toolsbarFrame.getChildAt(index) instanceof AImageCheckButton)
        {
            AImageCheckButton check = (AImageCheckButton)toolsbarFrame.getChildAt(index);
            check.setState(state);
        }
    }

    /**
     * 更新button的状态
     */
    public void updateStatus()
    {

    }

    /**
     * @return Returns the buttonWidth.
     */
    public int getButtonWidth()
    {
        return buttonWidth;
    }

    /**
     * @param buttonWidth The buttonWidth to set.
     */
    public void setButtonWidth(int buttonWidth)
    {
        this.buttonWidth = buttonWidth;
    }

    /**
     * @return Returns the buttonHeight.
     */
    public int getButtonHeight()
    {
        return buttonHeight;
    }

    /**
     * @param buttonHeight The buttonHeight to set.
     */
    public void setButtonHeight(int buttonHeight)
    {
        this.buttonHeight = buttonHeight;
    }

    /**
     * @return Returns the toolsbarWidth.
     */
    public int getToolsbarWidth()
    {
        return toolsbarWidth;
    }


    /**
     * @param toolsbarWidth The toolsbarWidth to set.
     */
    public void setToolsbarWidth(int toolsbarWidth)
    {
        this.toolsbarWidth = toolsbarWidth;
    }

    /**
     * @return Returns the animation.
     */
    public boolean isAnimation()
    {
        return animation;
    }


    /**
     * @param animation The animation to set.
     */
    public void setAnimation(boolean animation)
    {
        this.animation = animation;
    }

    /**
     *
     */
    public void dispose()
    {
        control = null;
        if (actionButtonIndex != null)
        {
            actionButtonIndex.clear();
            actionButtonIndex = null;
        }
        int count = toolsbarFrame.getChildCount();
        for (int i = 0; i < count; i++)
        {
            View v = toolsbarFrame.getChildAt(i);
            if (v instanceof AImageButton)
            {
                ((AImageButton)v).dispose();
            }
        }
        toolsbarFrame = null;
    }

    //
    private boolean animation;
    // button宽度
    protected int buttonWidth;
    // button调蓄
    protected int buttonHeight;
    // 工具条实际宽度
    protected int toolsbarWidth;
    // 用于事件派发
    protected IControl control;
    //
    protected LinearLayout toolsbarFrame;
    // 记录actionID对应button在toolsbar子视图的位置
    protected Map<Integer, Integer> actionButtonIndex;
}
