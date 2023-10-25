/*
 * 文件名称:          AImageSearchButton.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:48:27
 */
package com.cherry.lib.doc.office.officereader.beans;


import com.cherry.lib.doc.R;
import com.cherry.lib.doc.office.constant.MainConstant;
import com.cherry.lib.doc.office.system.IControl;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-3-12
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
@ SuppressLint("WrongCall")
public class AImageFindButton extends LinearLayout implements OnGestureListener, OnClickListener
{
    /**
     * 
     * @param context
     * @param control
     * @param toolstip
     * @param iconResID
     * @param iconResIdDisable
     * @param actionID
     * @param editWidth
     * @param btnWidth
     * @param height
     */
    public AImageFindButton(Context context, IControl control, String toolstip, 
        int iconResID, int iconResIdDisable, int actionID, int editWidth,
        int btnWidth, int height, TextWatcher textWatcher)
    {
        super(context); 
        this.control = control;
        // 在同一水平线
        setOrientation(LinearLayout.HORIZONTAL);
        setVerticalGravity(Gravity.CENTER);
        
        //edit file name
        editText = new EditText(getContext());
        editText.setFreezesText(false);
        editText.setGravity(Gravity.CENTER);
        editText.setSingleLine();  
        editText.addTextChangedListener(textWatcher);
        
        LayoutParams params = new LayoutParams(
            editWidth - MainConstant.GAP * 2, LayoutParams.WRAP_CONTENT);
        params.leftMargin = MainConstant.GAP;
        params.rightMargin = MainConstant.GAP;
        
        addView(editText, params);
        
        //search button
        btn = new AImageButton(context, control, 
            toolstip, iconResID, iconResIdDisable, actionID);         
        btn.setNormalBgResID(R.drawable.sys_toolsbar_button_bg_normal);
        btn.setPushBgResID(R.drawable.sys_toolsbar_button_bg_push);
        btn.setLayoutParams(new LayoutParams(btnWidth, height));        
        btn.setOnClickListener(this);
        btn.setEnabled(false);
        
        addView(btn);
    }
    
    /**
     * 
     * @param context
     * @param attrs
     */
    public AImageFindButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /**
     * 
     *
     */
    public void onConfigurationChanged(Configuration newConfig)
    {
        //setMinimumWidth(this.getResources().getDisplayMetrics().widthPixels);
    }
    
    /**
     * 
     * @param width
     */
    public void resetEditTextWidth(int width)
    {
        editText.getLayoutParams().width = width;
    }
    
    /**
     * 
     *
     */
    public void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
       super.onLayout(changed, left, top, right, bottom);
       // 如果相等，基本可以确定button总宽度小于屏幕宽度
       //if (getWidth() == getResources().getDisplayMetrics().widthPixels)
       {
           setBackgroundResource(R.drawable.sys_toolsbar_button_bg_normal);
       }
    }
    
    
    /**
     * 单击事件
     */
    public void onDraw(Canvas canvas)
    {
        btn.onDraw(canvas);
    }
    
    /**
     * 单击事件
     */
    public void onClick(View v)
    {
        if(!longPressed && v instanceof AImageButton)
        {
            InputMethodManager imm =
                (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            
           imm.hideSoftInputFromWindow(editText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);            
           imm.hideSoftInputFromInputMethod(editText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);  

            control.actionEvent(((AImageButton)v).getActionID(), editText.getText().toString());             
        }
        
        longPressed = false;
        
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        return btn.onTouchEvent(event);
    }

    /**
     * 
     */
    public boolean onDown(MotionEvent e)
    {
        return btn.onDown(e);
    }
    
    /**
     * 
     */
    public void onShowPress(MotionEvent e)
    {
        btn.onShowPress(e);
    }
    
    /**
     * 
     */
    public boolean onSingleTapUp(MotionEvent e)
    {
        return btn.onSingleTapUp(e);
    }
    
    /**
     * 
     */
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        return btn.onScroll(e1, e2, distanceX, distanceY);
    }
    
    /**
     * 
     */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        return btn.onFling(e1, e2, velocityX, velocityY);
    }
    
    /**
     * 
     */
    public void onLongPress(MotionEvent e)
    {
        longPressed = true;
        btn.onLongPress(e);
    }
    
    /**
     * clear textbox content
     * disable search button
     */
    public void reset()
    {
        editText.setText("");
        btn.setEnabled(false);
    }
    
    public void setFindBtnState(boolean state)
    {
        btn.setEnabled(state);
    }
    
    public void dispose()
    {
        control = null;
        editText = null;
        
        btn.dispose();
        btn = null;
    }
    
    //
    protected IControl control;
    //
    private EditText editText ;
    //search button
    private AImageButton btn;
    //
    //
    private boolean longPressed;
}
