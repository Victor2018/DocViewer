/*
 * 文件名称:          TXTEncodingDialog.java
 * 版权所有@ 2011-2014 虹软（杭州）科技有限公司
 * 编译器:           JDK1.5.0_01
 * 时间:             上午9:06:24
 */
package com.nvqquy98.lib.doc.office.wp.dialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import com.nvqquy98.lib.doc.office.fc.util.StreamUtils;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IDialogAction;
import com.nvqquy98.lib.doc.office.system.beans.ADialog;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;

/**
 * select encoding dialog
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2011-11-25
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:        
 * <p>
 * <p>
 */
public class TXTEncodingDialog extends ADialog
{
    public static final String BACK_PRESSED = "BP"; 
    public static final String[] ENCODING_ITEMS = new String[]
        {"GBK", "GB2312", "BIG5", "Unicode" ,"UTF-8" ,"UTF-16",
            "UTF-16LE", "UTF-16BE", "UTF-7", "UTF-32", "UTF-32LE",
            "UTF-32BE", "US-ASCII", "ISO-8859-1", "ISO-8859-2", "ISO-8859-3",
            "ISO-8859-4", "ISO-8859-5", "ISO-8859-6", "ISO-8859-7", "ISO-8859-8",
            "ISO-8859-9", "ISO-8859-10", "ISO-8859-11", "ISO-8859-13", "ISO-8859-14",
            "ISO-8859-15", "ISO-8859-16", "ISO-2022-JP", "ISO-2022-KR", "ISO-2022-CN",
            "ISO-2022-CN-EXT", "UCS-2", "UCS-4", "Windows-1250", "Windows-1251",
            "Windows-1252", "Windows-1253"," Windows-1254", "Windows-1255","Windows-1256",
            "Windows-1257", "Windows-1258", "KOI8-R", "Shift_JIS", "CP864", "EUC-JP",
            "EUC-KR", "BOCU-1", "CESU-8", "SCSU", "HZ-GB-2312", "TIS-620", "macintosh",
            "x-UTF-16LE-BOM", "x-iscii-as", "x-iscii-be", "x-iscii-de", "x-iscii-gu",
            "x-iscii-ka", "x-iscii-ma", "x-iscii-or", "x-iscii-pa", "x-iscii-ta",
            "x-iscii-te", "x-mac-cyrillic"};
    /**
     * 
     */
    public TXTEncodingDialog(IControl control, Context context, IDialogAction action, Vector<Object> model, int dialogID)
    {
        super(control, context, action, model, dialogID, 
            action.getControl().getMainFrame().getLocalString("DIALOG_ENCODING_TITLE"));
        init(context);
    }
       
    /**
     * 
     */
    public void init(Context context)
    {
        // 编码列表
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
            android.R.layout.simple_spinner_item, ENCODING_ITEMS);        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = new Spinner(context);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            /**
             * 
             *
             */
            public void onItemSelected(AdapterView<?>parent, View view, int position, long id)
            {
                String codeNew = spinner.getSelectedItem().toString();
                if (codeNew != null)
                {
                    setPreviewText(codeNew);
                }
            }
            
            /**
             * 
             */
            public void onNothingSelected(AdapterView<?>parent)
            {
                
            }
        });
        dialogFrame.addView(spinner);
        
        // 预览文本
        previewText = new WebView(context);
        previewText.setPadding(5, 2, 5, 2);
        scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        scrollView.addView(previewText);
        dialogFrame.addView(scrollView);
        
        // ok
        ok = new Button(context);
        String okStr = action.getControl().getMainFrame().getLocalString("BUTTON_OK");
        ok.setText(okStr);
        ok.setOnClickListener(this);  
        dialogFrame.addView(ok);
    }
    
    /**
     * 
     *
     */
    public void doLayout()
    {
        int mWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int mHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        // 需要减去标题栏的高度
        mHeight -= getWindow().getDecorView().getHeight() - dialogFrame.getHeight();
        mWidth -= GAP * 10;
        mHeight -= GAP * 10;
        LayoutParams params = new LayoutParams(mWidth - GAP * 2, mHeight - spinner.getBottom() - ok.getHeight());
        params.leftMargin = GAP;
        params.rightMargin = GAP;
        params.bottomMargin = GAP;
        scrollView.setLayoutParams(params);
    }
    
    /**
     * 
     *
     */
    public void onConfigurationChanged(Configuration newConfig)
    {        
        doLayout();
    }
    
    /**
     * 
     *
     */
    public void onClick(View v)
    {
        Vector<Object> vector = new Vector<Object>();
        vector.add(spinner.getSelectedItem().toString());
        action.doAction(dialogID, vector);
        dismiss();
    }
    
    /**
     * 
     */
    public void onBackPressed()
    {
        Vector<Object> vector = new Vector<Object>();
        vector.add(BACK_PRESSED);
        action.doAction(dialogID, vector);
        super.onBackPressed();
    }
    
    /**
     * 
     */
    private void setPreviewText(String code)
    {
        try
        {
            if (code != null)
            {
                InputStream is = StreamUtils.getInputStream(getContext().getContentResolver(),model.get(0).toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(is, code));
                int len = br.read(buffer);
                if (len > 0)
                {
                    String str = "<a>" + new String(buffer, 0, len) + "</a>";
                    //previewText.loadData(new String(str.getBytes("UTF-8")), "text/html", "UTF-8");
                    previewText.loadDataWithBaseURL(null, str.replaceAll("\\r\\n", "<br />"), "text/html", "UTF-8", null);
                }
                br.close();                 
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     *
     */
    public void dispose()
    {
        super.dispose();
        spinner = null;
        previewText = null;
        buffer = null;
        scrollView = null;
    }
    
    //
    private Spinner spinner;
    //
    private WebView previewText;
    //
    private char[] buffer = new char[1024];
    //
    private ScrollView scrollView;
}
