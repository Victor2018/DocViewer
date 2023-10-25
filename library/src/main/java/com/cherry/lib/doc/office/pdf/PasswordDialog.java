/*
 * 文件名称:          PasswordDialog.java
 *  
 * 编译器:            android2.2
 * 时间:              下午8:17:29
 */
package com.cherry.lib.doc.office.pdf;

import com.cherry.lib.doc.office.common.ICustomDialog;
import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.office.fc.pdf.PDFLib;
import com.cherry.lib.doc.office.res.ResKit;
import com.cherry.lib.doc.office.system.IControl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2013-3-4
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PasswordDialog
{
    
    /**
     * 
     */
    public PasswordDialog(IControl control, PDFLib lib)
    {
        this.control = control;
        this.lib = lib;
    }
    
    /**
     * 
     */
    public void show()
    {
        if(control.getMainFrame().isShowPasswordDlg())
        {
            alertBuilder = new AlertDialog.Builder(control.getActivity());
            requestPassword(lib); 
        }
        else
        {
            ICustomDialog dlgListener = control.getCustomDialog();
            if(dlgListener != null)
            {
                dlgListener.showDialog(ICustomDialog.DIALOGTYPE_PASSWORD);
            }
        }
    }

    /**
     * 
     * @param savedInstanceState
     */
    private void requestPassword(final PDFLib lib)
    {
        pwView = new EditText(control.getActivity());
        pwView.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        pwView.setTransformationMethod(new PasswordTransformationMethod());

        AlertDialog alert = alertBuilder.create();
        alert.setTitle(ResKit.instance().getLocalString("DIALOG_ENTER_PASSWORD"));
        alert.setView(pwView);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, 
            ResKit.instance().getLocalString("BUTTON_OK"), 
            new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (lib.authenticatePasswordSync(pwView.getText().toString()))
                {
                    control.actionEvent(EventConstant.APP_PASSWORD_OK_INIT, null);
                    return;
                }
                else
                {
                    requestPassword(lib);
                }
            }
        });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, 
            ResKit.instance().getLocalString("BUTTON_CANCEL"),
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    //lib.dispose();
                    control.getActivity().onBackPressed();
                    //control.getMainFrame().destroyEngine();
                }
            });
        
        alert.setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    dialog.dismiss();
                    control.getActivity().onBackPressed();
                    //control.getMainFrame().destroyEngine();
                    return true;
                }
                return false;
            }
        });
        alert.show();
    }
    
    //
    private IControl control;
    //
    private PDFLib lib;
    //
    private AlertDialog.Builder alertBuilder;
    //
    private EditText pwView;
}
