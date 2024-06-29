/*
 * 文件名称:          AppControl.java
 *
 * 编译器:            android2.2
 * 时间:              下午7:17:16
 */
package com.nvqquy98.lib.doc.office.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.nvqquy98.lib.doc.bean.FileType;
import com.nvqquy98.lib.doc.office.common.ICustomDialog;
import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.ISlideShow;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.doc.TXTKit;
import com.nvqquy98.lib.doc.office.fc.pdf.PDFLib;
import com.nvqquy98.lib.doc.office.pg.control.PGControl;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.ss.control.SSControl;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.wp.control.WPControl;

import java.lang.reflect.Method;
import java.util.Objects;

import timber.log.Timber;

public class MainControl extends AbstractControl {
    public MainControl(IMainFrame frame) {
        this.frame = frame;
        uncaught = new AUncaughtExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(uncaught);
        sysKit = new SysKit(this);
        init();
    }

    /**
     *
     */
    private void init() {
        initListener();
        toast = Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "", Toast.LENGTH_SHORT);
        Intent intent = getActivity().getIntent();
        String autoTest = intent.getStringExtra("autoTest");
        isAutoTest = autoTest != null && autoTest.equals("true");
    }

    private void initListener() {
        onKeyListener = (dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                isCancel = true;
                if (reader != null) {
                    reader.abortReader();
                    reader.dispose();
                }
                //getMainFrame().destroyEngine();
                Objects.requireNonNull(getActivity()).onBackPressed();
                return true;
            }
            return false;
        };
        handler = new Handler(Looper.myLooper()) {
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                if (isCancel) {
                    return;
                }
                final Message message = msg;
                switch (msg.what) {
                    case MainConstant.HANDLER_MESSAGE_SUCCESS:
                        post(() -> {
                            try {
                                if (getMainFrame().isShowProgressBar()) {
                                    dismissProgressDialog();
                                } else {
                                    if (customDialog != null) {
                                        customDialog.dismissDialog(ICustomDialog.DIALOGTYPE_LOADING);
                                    }
                                }
                                createApplication(message.obj);
                            } catch (Exception e) {
                                sysKit.getErrorKit().writerLog(e, true);
                            }
                        });
                        break;
                    case MainConstant.HANDLER_MESSAGE_ERROR:
                        post(() -> {
                            dismissProgressDialog();

                           /* Toast.makeText(getActivity(), "File cannot be open", Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(getActivity()).finish();*/
                            frame.openFileFailed();
                        });
                        break;
                    case MainConstant.HANDLER_MESSAGE_SHOW_PROGRESS:
                        if (getMainFrame().isShowProgressBar()) {
                            post(() -> {
                                progressDialog = ProgressDialog.show(getActivity(),
                                        frame.getAppName(), frame.getLocalString("DIALOG_LOADING"),
                                        false, false, null);
                                progressDialog.setOnKeyListener(onKeyListener);
                            });
                        } else {
                            if (customDialog != null) {
                                customDialog.showDialog(ICustomDialog.DIALOGTYPE_LOADING);
                            }
                        }
                        break;
                    case MainConstant.HANDLER_MESSAGE_DISMISS_PROGRESS:
                        post(() -> dismissProgressDialog());
                        break;
                    case MainConstant.HANDLER_MESSAGE_SEND_READER_INSTANCE:
                        reader = (IReader) msg.obj;
                        break;
                }
            }
        };
    }

    /**
     * dismiss progress dialog
     */
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     *
     */
    private void createApplication(Object obj) throws Exception {
        if (obj == null) {
            throw new Exception("Document with password");
        }
        // word
        if (applicationType == MainConstant.APPLICATION_TYPE_WP) {
            appControl = new WPControl(this, (IDocument) obj, filePath);
        }
        // excel
        else if (applicationType == MainConstant.APPLICATION_TYPE_SS) {
            appControl = new SSControl(this, (Workbook) obj, filePath);
        }
        // powerpoint
        else if (applicationType == MainConstant.APPLICATION_TYPE_PPT) {
            appControl = new PGControl(this, (PGModel) obj, filePath);
        }
        View view = appControl.getView();
        if (view != null) {
            Object bg = frame.getViewBackground();
            if (bg != null) {
                if (bg instanceof Integer) {
                    view.setBackgroundColor((Integer) bg);
                } else if (bg instanceof Drawable) {
                    view.setBackground((Drawable) bg);
                }
            }
        }
        final boolean hassPassword = applicationType == MainConstant.APPLICATION_TYPE_PDF && ((PDFLib) obj).hasPasswordSync();
        if (applicationType == MainConstant.APPLICATION_TYPE_PDF) {
            if (!hassPassword) {
                /*handler.post(new Runnable()
                {
                    
                    @ Override
                    public void run()
                    {
                        frame.openFileFinish();
                    }
                });*/
                frame.openFileFinish();
            }
        } else {
            frame.openFileFinish();
        }
        PictureKit.instance().setDrawPictrue(true);
        //use a handler as easiest method to post a Runnable Delayed.
        //we cannot check hardware-acceleration directly as it will return reasonable results after attached to Window.
        handler.post(new Runnable() {

            @Override
            public void run() {
                //now lets check for HardwareAcceleration since it is only avaliable since ICS.
                // 14 = ICS_VERSION_CODE
                try {
                    View contentView = getView();
                    //use reflection to get that Method
                    assert contentView != null;
                    Method isHardwareAccelerated = contentView.getClass().getMethod("isHardwareAccelerated");
                    Object o = isHardwareAccelerated.invoke(contentView);
                    if (o instanceof Boolean && (Boolean) o) {
                        //ok we're shure that HardwareAcceleration is on.
                        //Now Try to switch it off:
                        Method setLayerType = contentView.getClass().getMethod("setLayerType", int.class, android.graphics.Paint.class);
                        int LAYER_TYPE_SOFTWARE = contentView.getClass().getField("LAYER_TYPE_SOFTWARE").getInt(null);
                        setLayerType.invoke(contentView, LAYER_TYPE_SOFTWARE, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
                actionEvent(EventConstant.SYS_INIT_ID, null);
                if (applicationType == MainConstant.APPLICATION_TYPE_PDF) {
                    if (!hassPassword) {
                        frame.updateToolsbarStatus();
                    }
                } else {
                    frame.updateToolsbarStatus();
                }
                //
                getView().postInvalidate();
            }
        });       
        /*// 初始化数
        actionEvent(EventConstant.SYS_INIT_ID, null);
        // 更新状态
        frame.updateToolsbarStatus();*/
    }

    /**
     *
     */
    public boolean openFile(String filePath, int docSourceType, String fileType) {
        Timber.e("openFile  openFile-fileType =%s", fileType);
        this.filePath = filePath;
        if (!TextUtils.isEmpty(fileType) && !TextUtils.equals("-1", fileType)) {
            applicationType = getDocFileType(Integer.parseInt(fileType));
            new FileReaderThread(this, handler, filePath, docSourceType, Integer.parseInt(fileType), null).start();
        } else {
            String fileName = filePath.toLowerCase();
            // word
            if (fileName.endsWith(MainConstant.FILE_TYPE_DOC)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOCX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_TXT)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOT)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOTM)) {
                applicationType = MainConstant.APPLICATION_TYPE_WP;
            }
            // excel
            else if (fileName.endsWith(MainConstant.FILE_TYPE_XLS)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLSX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLT)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLTM)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLSM)) {
                applicationType = MainConstant.APPLICATION_TYPE_SS;
            }
            // PowerPoint
            else if (fileName.endsWith(MainConstant.FILE_TYPE_PPT)
                    || fileName.endsWith(MainConstant.FILE_TYPE_PPTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POT)
                    || fileName.endsWith(MainConstant.FILE_TYPE_PPTM)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POTM)) {
                applicationType = MainConstant.APPLICATION_TYPE_PPT;
            }
            // PDF document
            else if (fileName.endsWith(MainConstant.FILE_TYPE_PDF)) {
                applicationType = MainConstant.APPLICATION_TYPE_PDF;
            } else {
                applicationType = MainConstant.APPLICATION_TYPE_WP;
            }
            boolean isSupport = FileKit.instance().isSupport(fileName);
            // txt or no support
            if (fileName.endsWith(MainConstant.FILE_TYPE_TXT)
                    || !isSupport) {
                TXTKit.instance().readText(this, handler, filePath, docSourceType);
            } else {
                new FileReaderThread(this, handler, filePath, docSourceType, -1, null).start();
            }
        }
        return true;
    }

    /**
     * 布局视图
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void layoutView(int x, int y, int w, int h) {
        //appControl.layoutView(x, y, w, h);
    }

    /**
     * action派发
     *
     * @param actionID 动作ID
     * @param obj      动作ID的Value
     */
    public void actionEvent(int actionID, final Object obj) {
        if (actionID == EventConstant.SYS_READER_FINSH_ID) {
            if (reader != null) {
                if (appControl != null) {
                    appControl.actionEvent(actionID, obj);
                }
                reader.dispose();
                reader = null;
            }
        }
        if (frame == null || frame.doActionEvent(actionID, obj)) {
            return;
        }
        switch (actionID) {
            case MainConstant.HANDLER_MESSAGE_SUCCESS: {
                try {
                    Message msg = new Message();
                    msg.obj = obj;
                    reader.dispose();
                    msg.what = MainConstant.HANDLER_MESSAGE_SUCCESS;
                    handler.handleMessage(msg);
                } catch (Throwable e) {
                    sysKit.getErrorKit().writerLog(e);
                }
            }
            break;
            case EventConstant.SYS_SET_PROGRESS_BAR_ID:
                if (handler != null) {
                    handler.post(new Runnable() {
                        /**
                         *
                         */
                        public void run() {
                            if (!isDispose) {
                                frame.showProgressBar((Boolean) obj);
                            }
                        }
                    });
                }
                break;
            case EventConstant.TEST_REPAINT_ID:
                getView().postInvalidate();
                break;
            case EventConstant.SYS_SHOW_TOOLTIP:
                if (obj != null && obj instanceof String) {
                    toast.setText((String) obj);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case EventConstant.SYS_CLOSE_TOOLTIP:
                toast.cancel();
                break;
            case EventConstant.APP_CONTENT_SELECTED:  //selected interesting content
                appControl.actionEvent(actionID, obj);
                frame.updateToolsbarStatus();
                break;
            case EventConstant.APP_ABORTREADING:
                if (reader != null) {
                    reader.abortReader();
                }
                break;
            case EventConstant.SYS_START_BACK_READER_ID:
                if (handler != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (!isDispose) {
                                frame.showProgressBar(true);
                            }
                        }
                    });
                }
                break;
            case EventConstant.SYS_READER_FINSH_ID:
                if (handler != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isDispose) {
                                frame.showProgressBar(false);
                            }
                        }
                    });
                }
                break;
            case EventConstant.TXT_DIALOG_FINISH_ID:
                TXTKit.instance().reopenFile(this, handler, filePath, docSourceType, (String) obj);
                break;
            case EventConstant.TXT_REOPNE_ID:
                String[] strings = (String[]) obj;
                if (strings.length == 2) {
                    this.filePath = strings[0];
                    applicationType = MainConstant.APPLICATION_TYPE_WP;
                    TXTKit.instance().reopenFile(this, handler, filePath, docSourceType, strings[1]);
                }
                break;
            default:
                if (appControl != null) {
                    appControl.actionEvent(actionID, obj);
                }
        }
    }

    /**
     *
     */
    public IFind getFind() {
        return appControl.getFind();
    }

    /**
     * 得到action的状态
     *
     * @return obj
     */
    public Object getActionValue(int actionID, Object obj) {
        switch (actionID) {
            case EventConstant.SYS_FILEPAHT_ID: // 文件路径
                return filePath;
            default:
                break;
        }
        if (appControl == null) {
            return null;
        }
        Object ret = null;
        if (actionID == EventConstant.APP_THUMBNAIL_ID
                || actionID == EventConstant.WP_PAGE_TO_IMAGE
                || actionID == EventConstant.APP_PAGEAREA_TO_IMAGE
                || actionID == EventConstant.PG_SLIDE_TO_IMAGE
                || actionID == EventConstant.PG_SLIDESHOW_SLIDESHOWTOIMAGE) {
            boolean b = PictureKit.instance().isDrawPictrue();
            boolean isThumbnail = frame.isThumbnail();
            PictureKit.instance().setDrawPictrue(true);
            if (actionID == EventConstant.APP_THUMBNAIL_ID) {
                frame.setThumbnail(true);
            }
            ret = appControl.getActionValue(actionID, obj);
            if (actionID == EventConstant.APP_THUMBNAIL_ID) {
                frame.setThumbnail(isThumbnail);
            }
            PictureKit.instance().setDrawPictrue(b);
        } else {
            ret = appControl.getActionValue(actionID, obj);
        }
        return ret;
    }

    /**
     * 获取应用组件
     */
    public View getView() {
        if (appControl == null) {
            return null;
        }
        return appControl.getView();
    }

    /**
     *
     */
    public Dialog getDialog(Activity activity, int id) {
        return null;
    }

    /**
     *
     */
    public boolean isAutoTest() {
        return isAutoTest;
    }

    /**
     *
     */
    public IMainFrame getMainFrame() {
        return frame;
    }

    /**
     *
     */
    public Activity getActivity() {
        return frame.getActivity();
    }

    /**
     *
     */
    public IOfficeToPicture getOfficeToPicture() {
        return officeToPicture;
    }

    /**
     *
     */
    public ICustomDialog getCustomDialog() {
        return customDialog;
    }

    /**
     * @return
     */
    public ISlideShow getSlideShow() {
        return slideShow;
    }

    /**
     *
     */
    public IReader getReader() {
        return reader;
    }

    /**
     *
     */
    public int getApplicationType() {
        return applicationType;
    }

    /**
     * set OfficeToPicture instance
     */
    public void setOffictToPicture(IOfficeToPicture opt) {
        officeToPicture = opt;
    }

    /**
     *
     */
    public void setCustomDialog(ICustomDialog dlg) {
        this.customDialog = dlg;
    }

    /**
     * @param slideshow
     */
    public void setSlideShow(ISlideShow slideshow) {
        this.slideShow = slideshow;
    }

    /**
     *
     */
    public SysKit getSysKit() {
        return this.sysKit;
    }

    /**
     * current view index
     *
     * @return
     */
    public int getCurrentViewIndex() {
        return appControl.getCurrentViewIndex();
    }

    /**
     *
     */
    public void dispose() {
        isDispose = true;
        if (appControl != null) {
            appControl.dispose();
            appControl = null;
        }
        if (reader != null) {
            reader.dispose();
            reader = null;
        }
        if (officeToPicture != null) {
            officeToPicture.dispose();
            officeToPicture = null;
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (customDialog != null) {
            customDialog = null;
        }
        if (slideShow != null) {
            slideShow = null;
        }
        frame = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (uncaught != null) {
            uncaught.dispose();
            uncaught = null;
        }
        onKeyListener = null;
        toast = null;
        filePath = null;
        //disposeStatic();
        System.gc();
        if (sysKit != null) {
            sysKit.dispose();
        }
    }

    private int getDocFileType(int fileType) {
        switch (fileType) {
            case FileType.DOC:
            case FileType.DOCX:
            case FileType.TXT:
                return MainConstant.APPLICATION_TYPE_WP;
            case FileType.XLS:
            case FileType.XLSX:
                return MainConstant.APPLICATION_TYPE_SS;
            case FileType.PPT:
            case FileType.PPTX:
                return MainConstant.APPLICATION_TYPE_PPT;
            case FileType.PDF:
                return MainConstant.APPLICATION_TYPE_PDF;
        }
        return -1;
    }

    /**
     *
     */
    /*private void disposeStatic()
    {
        PictureManage.instance().dispose();
        HyperlinkManage.instance().dispose();
        ListManage.instance().dispose();
        //SysKit.instance().setControl(null);
        PGBulletText.instance().dispose();
        ViewFactory.dispose();
    }*/

    private boolean isDispose;
    //
    private boolean isCancel;
    // 自动化测试
    private boolean isAutoTest;
    //
    private int applicationType = -1;
    // 文件路径
    private String filePath;
    //
    private IMainFrame frame;
    //
    private IOfficeToPicture officeToPicture;
    //
    private ICustomDialog customDialog;
    //
    private ISlideShow slideShow;
    //
    private IReader reader;
    //toast
    private Toast toast;
    // 
    private ProgressDialog progressDialog;
    //
    private DialogInterface.OnKeyListener onKeyListener;
    //
    private Handler handler;
    private int docSourceType;
    //
    private IControl appControl;
    //
    public SysKit sysKit;
    //
    private AUncaughtExceptionHandler uncaught;
}
