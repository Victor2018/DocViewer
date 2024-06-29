package com.nvqquy98.lib.doc.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nvqquy98.lib.doc.util.WordConverter;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class PoiViewer {

    private Context mContext;

    private ViewGroup mRootView;

    private String mFilePath;
    private String mFileExt;

    private String mFileCachePath;

    private WebView mWebView;

    private ProgressDialog mProgressDialog;

    public PoiViewer(Context context) {
        mContext = context;
        initView();
        initCacheDir();
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在加载文件...");
        // 初始化网页
        mWebView = new WebView(mContext);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setTextZoom(128);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    private void initCacheDir() {
        mFileCachePath = mContext.getCacheDir().getAbsolutePath() + "/poiCache/";
        File file = new File(mFileCachePath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void loadFile(ViewGroup fileLayout, String filePath) {
        mRootView = fileLayout;
        mFilePath = filePath;
        mFileExt = filePath.substring(filePath.lastIndexOf("."));
        mProgressDialog.show();
        new ConvertTask().execute(filePath);
    }

    public class ConvertTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... objects) {
            String filePath = objects[0];
            if (mFileExt.equalsIgnoreCase(".doc") || mFileExt.equalsIgnoreCase(".docx")) {
                WordConverter wordConverter = new WordConverter(filePath, mFileCachePath);
                return wordConverter.returnPath;
            }
//            if (mFileExt.equalsIgnoreCase(".xls") || mFileExt.equalsIgnoreCase(".xlsx")) {
//                ExcelConverter excelConverter = new ExcelConverter(filePath, mFileCachePath);
//                excelConverter.readExcelToHtml();
//                return excelConverter.mUrlPath;
//            }
            if (mFileExt.equalsIgnoreCase(".txt")) {
                try {
                    String txtString = FileUtils.readFileToString(new File(filePath), "UTF-8");
                    SpannableString spanString = new SpannableString(txtString);
                    String htmlString = Html.toHtml(spanString);
                    String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
                    String htmlName = fileName.replace(fileExt, "html");
                    File tempFile = new File(mFileCachePath, htmlName);
                    FileUtils.writeStringToFile(tempFile, htmlString, "UTF-8");
                    return "file:///" + tempFile.getAbsolutePath();
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String returnString) {
            mProgressDialog.dismiss();
            if (TextUtils.isEmpty(returnString)) {
                Toast.makeText(mContext, "文件打开失败", Toast.LENGTH_SHORT).show();
                scanForActivity(mContext).finish();
                return;
            }
            mWebView.loadUrl(returnString);
            mRootView.addView(mWebView);
        }
    }

    public void recycle() {
        mWebView.removeAllViews();
        mWebView = null;
        mContext = null;
    }

    public AppCompatActivity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextWrapper) {
            ContextWrapper cw = (ContextWrapper) context;
            return scanForActivity(cw.getBaseContext());
        }
        return null;
    }
}
