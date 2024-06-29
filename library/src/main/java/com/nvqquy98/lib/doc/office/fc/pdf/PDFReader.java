/*
 * 文件名称:          PDFReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:30:30
 */
package com.nvqquy98.lib.doc.office.fc.pdf;

import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.system.AbstractReader;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * reader PDF document 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PDFReader extends AbstractReader
{
    /**
     * 
     * @param filePath
     */
    public PDFReader(IControl control, String filePath) throws Exception
    {
        this.control = control;
        this.filePath = filePath;
    }
     
    /**
     *
     */
    public Object getModel() throws Exception
    {        
        control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
        //lib = new PDFLib(filePath);
        lib = PDFLib.getPDFLib();
        lib.openFileSync(filePath);
        /*if (lib.hasPasswordSync())
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
        }*/
        return lib;
    }
    
    /**
     * 
     * @param savedInstanceState
     */
    /*private void requestPassword(final PDFLib lib)
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
    }*/

    /**
     * 
     *
     */
    public void dispose()
    {
        super.dispose();
        lib = null;
        control = null;
    }
    
    //
    private String filePath;
    //
    private PDFLib lib;
    //
    //private AlertDialog.Builder alertBuilder;
    //
    //private EditText pwView;

}
