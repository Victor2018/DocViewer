/*
 * 文件名称:          AbstractMainFrame.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:08:07
 */
package com.cherry.lib.doc.office.macro;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.lib.doc.office.res.ResKit;
import com.cherry.lib.doc.office.system.IMainFrame;
import com.cherry.lib.doc.office.system.beans.pagelist.IPageListViewListener;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

class MacroFrame implements IMainFrame
{
    public MacroFrame(Application application, Activity activity)
    {
        this.app = application;
        this.activity = activity;
        //
        int resID = activity.getApplication().getApplicationInfo().labelRes;
        if (resID > 0)
        {   
            this.appName = activity.getResources().getString(resID);
        }
    }

    /**
     * get activity instance
     * @return activity instance
     */
    public Activity getActivity()
    {
        return activity;
    }
    
    /**
     * added touch event listener
     */
    protected void addTouchEventListener(TouchEventListener listener)
    {
        this.touchEventListener = listener;
    }
    
    /**
     * added update status listener 
     */
    public void addUpdateStatusListener(UpdateStatusListener listener)
    {
        this.updateStatusListener = listener;
    }
    
    /**
     * added open file finish listener
     * 
     * @param listener OpenFileFinishListener instance
     */
    public void addOpenFileFinishListener(OpenFileFinishListener listener)
    {
        this.openFileFinishListener = listener;
    }
    
    /**
     * added error listener
     * 
     * @param listener ErrorListener instance
     */
    public void addErrorListener(ErrorListener listener)
    {
        this.errorListener = listener;
    }
    
    /**
     * 
     * @return
     */
    public File getTemporaryDirectory()
    {
    	if(activity != null)
    	{
    		// Get path for the file on external storage.  If external
            // storage is not currently mounted this will fail.
            File file = activity.getExternalFilesDir(null);
            if(file != null)
            {
            	return file;
            }
            else
            {
            	return activity.getFilesDir();
            }
    	}
        
        return null;
    }
    
    /**
     * event method, office engine dispatch 
     * 
     * @param       v             event source
     * @param       e1            MotionEvent instance
     * @param       e2            MotionEvent instance
     * @param       velocityX     x axis velocity
     * @param       velocityY     y axis velocity  
     * @param       eventNethodType  event method      
     *              @see IMainFrame#ON_CLICK
     *              @see IMainFrame#ON_DOUBLE_TAP
     *              @see IMainFrame#ON_DOUBLE_TAP_EVENT
     *              @see IMainFrame#ON_DOWN
     *              @see IMainFrame#ON_FLING
     *              @see IMainFrame#ON_LONG_PRESS
     *              @see IMainFrame#ON_SCROLL
     *              @see IMainFrame#ON_SHOW_PRESS
     *              @see IMainFrame#ON_SINGLE_TAP_CONFIRMED
     *              @see IMainFrame#ON_SINGLE_TAP_UP
     *              @see IMainFrame#ON_TOUCH
     */
    public boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, byte eventMethodType)
    {
        if (touchEventListener != null)
        {
            touchEventListener.onEventMethod(v, e1, e2, velocityX, velocityY, eventMethodType);
        }
        return false;
    }
    
    /**
     * update tool bar status
     */    
    public void updateToolsbarStatus()
    {
        if (updateStatusListener != null)
        {
            updateStatusListener.updateStatus();
        }
    }
    
    /**
     * callback this method after zoom change
     */
    public void changeZoom()
    {
        if (updateStatusListener != null)
        {
            updateStatusListener.changeZoom();
        }
    }
    
    /**
     * callback this method after zoom change
     */
    public void changePage()
    {
        if (updateStatusListener != null)
        {
            updateStatusListener.changePage();
        }
    }
    
    /**
     * callback this method after layout completed
     */
    public void completeLayout()
    {
        if (updateStatusListener != null)
        {
            updateStatusListener.completeLayout();
        }
    }
    
    /**
     * full screen, not show top tool bar
     */
    public void fullScreen(boolean fullscreen)
    {
        
    }    
    
    /**
     * 
     *(non-Javadoc)
     * @see com.cherry.lib.doc.office.system.IMainFrame#showProgressBar(boolean)
     *
     */
    public void showProgressBar(boolean visible)
    {
        if(showProgressbarDlg)
        {
            activity.setProgressBarIndeterminateVisibility(visible);
        }
    }
    
    /**
     * 
     * @param viewList
     */
    public void updateViewImages(List<Integer> viewList)
    {
    	if (updateStatusListener != null)
        {
    		updateStatusListener.updateViewImage(viewList.toArray(new Integer[viewList.size()]));
        }
    }
    
    /**
     * do action 
     *
     * @param actionID action ID 
     * 
     * @param obj acValue
     * 
     * @return  True if the listener has consumed the event, false otherwise. 
     */
    public boolean doActionEvent(int actionID, Object obj)
    {
        return false;
    }
    
    /**
     * reader file finish call this method
     */
    public void openFileFinish()
    {   
        app.openFileFinish();
        if (openFileFinishListener != null)
        {
            openFileFinishListener.openFileFinish();
        }
        
    }
    
    /**
     * when engine error occurred callback this method
     * 
     * @param codeCode  error code 
     */
    public void error(int errorCode)
    {
        if (errorListener != null)
        {
            errorListener.error(errorCode);
        }
    }
    
    /**
     * when need destroy office engine instance callback this method
     */
    public void destroyEngine()
    {
        if (errorListener != null)
        {
            //errorListener.destroyEngine();
        }
    }
    
    
    /**
     * set the find back button and find forward button state
     * 
     * @param state
     */
    public void setFindBackForwardState(boolean state)
    {
        
    }
    
    /**
     * get bottom  bar height
     * 
     * @return bottom bar height
     */
    public int getBottomBarHeight()
    {
        return this.bottomBarHeight;
    }
    
    /**
     * set bottom bar height
     * 
     * @param value  bottom bar height
     */
    public void setBottomBarHeight(int value)
    {
        this.bottomBarHeight = value;
    }
    
    /**
     * get top bar height
     * 
     * @return top bar height
     */
    public int getTopBarHeight()
    {
        return this.topBarHeight;
    }
    
    /**
     * set top bar height
     * 
     * @param vlaue  top bar height
     */
    public void setTopBarHeight(int value)
    {
        this.topBarHeight = value;
    }
    
    /**
     * get application name
     * 
     * @return application name
     */
    public String getAppName()
    {
        return appName == null ? "wxiwei" : appName;
    }
    
    /**
     * set application name
     * 
     * @param name  application name
     */
    public void setAppName(String name)
    {
        this.appName = name;
    }
    
    /**
     * is support draw page number
     * 
     * @return  true  draw page number
     *           false don’t draw page number
     */
    public boolean isDrawPageNumber()
    {
        return this.isDrawPageNumber;
    }
    
    /**
     * set is support draw page number?
     * 
     * @param value    true  draw page number
     *                  false don’t draw page number
     */
    public void setDrawPageNumber(boolean value)
    {
        this.isDrawPageNumber = value;
    }
    
    /**
     * true: show message when zooming
     * false: not show message when zooming
     * @return
     */
    public boolean isShowZoomingMsg()
    {
        return showZoomingMsg;
    }
    
    /**
     * show or hide zooming message
     * @param showZoomingMsg
     */
    public void setShowZoomingMsg(boolean showZoomingMsg)
    {
        this.showZoomingMsg = showZoomingMsg;
    }
    
    /**
     * true: pop up dialog when throw err
     * false: not pop up dialog when throw err
     * @return
     */
    public boolean isPopUpErrorDlg()
    {
        return popupErrorDlg;
    }
    
    /**
     * 
     * @param showPasswordDlg
     */
    public void setShowPasswordDlg(boolean showPasswordDlg)
    {
        this.showPasswordDlg = showPasswordDlg;
    }
    
    /**
     * show password dialog when parse file with password
     * @return
     */
    public boolean isShowPasswordDlg()
    {
        return showPasswordDlg;
    }
    
    /**
     * 
     * @param showProgressbarDlg
     */
    public void setShowProgressBar(boolean showProgressbarDlg)
    {
        this.showProgressbarDlg = showProgressbarDlg;
    }
    
    /**
     * show progress bar or not when parsing document
     * @return
     */
    public boolean isShowProgressBar()
    {
        return showProgressbarDlg;
    }
    
    /**
     * 
     */
    public void setShowFindDlg(boolean b)
    {
       this.showFindDlg = b;
    }
    
    /**
     * 
     */
    public boolean isShowFindDlg()
    {
        return showFindDlg;
    }
    
    /**
     * 
     * @param showTXTEncodeDlg
     */
    public void setShowTXTEncodeDlg(boolean showTXTEncodeDlg)
    {
        this.showTXTEncodeDlg = showTXTEncodeDlg;
    }
    
    /**
     * show txt encode dialog when parse txt file
     * @return
     */
    public boolean isShowTXTEncodeDlg()
    {
        return showTXTEncodeDlg;
    }
    
    /**
     * set txt default encode
     * @param encode
     */
    public void setTXTDefaultEncode(String encode)
    {
        this.txtDefalutEncode = encode;
    }
    
    /**
     * get txt default encode when not showing txt encode dialog
     * @return null if showing txt encode dialog
     */
    public String getTXTDefaultEncode()
    {
        if(!showTXTEncodeDlg)
        {
            return txtDefalutEncode;
        }
        
        return null;
    }
    /**
     * pop up error dialog or hot
     * @param popupErrorDlg
     */
    public void setPopUpErrorDlg(boolean popupErrorDlg)
    {
        this.popupErrorDlg = popupErrorDlg;
    }
    
    /**
     * is support zoom in / zoom out
     * 
     * @return  true  touch zoom
     *           false don’t touch zoom
     */
    public boolean isTouchZoom()
    {
        return this.isTouchZoom;
    }
    
    /**
     * set is support zoom in / zoom out?
     * 
     * @param value    true  support zoom in / zoom out
     *                  false don't support zoom in / zoom out
     */
    public void setTouchZoom(boolean value)
    {
        this.isTouchZoom = value;
    }
    
    /**
     * normal view, changed after zoom bend, you need to re-layout
     * 
     * @return  true   re-layout
     *           false  don't re-layout   
     */
    public boolean isZoomAfterLayoutForWord()
    {
        return this.isZoomAfterLayoutForWord;
    }
    
    /**
     * set normal view, changed after zoom bend, you need to re-layout
     * 
     * @param   true   re-layout
     *           false  don't re-layout
     */
    public void setZoomAfterLayoutForWord(boolean value)
    {
        this.isZoomAfterLayoutForWord = value;
    }    
    
    /**
     * get word application default view (Normal or Page)
     * 
     * @return 0, page view
     *          1，normal view;
     *           
     */
    public byte getWordDefaultView()
    {
        return wordDefaultView;
    }
    
    /**
     * get word application default view (Normal or Page)
     * 
     * @param  value   0, page view
     *                  1，normal view;
     *           
     */
    public void setWordDefaultView(byte value)
    {
        this.wordDefaultView = value;
    }
    
    /**
     *  set change page flag, Only when effectively the PageSize greater than ViewSize.
     *  (for PPT, word print mode, PDF)
     *  
     *  @param b    = true, change page
     *              = false, don't change page
     */
    public void setChangePage(boolean b)
    {
        this.isChangePage = b;
    }
    
    /**
     *  get change page flag, Only when effectively the PageSize greater than ViewSize.
     *  when page size greater then view size do change page
     */
    public boolean isChangePage()
    {
        return this.isChangePage;
    }
    
    /**
     * get Internationalization resource
     * 
     * @param resName Internationalization resource name
     * 
     * @return  resource value
     */
    public String getLocalString(String resName)
    {
        if (resI18N == null)
        {
            resI18N = new HashMap<String, Integer>();
            try
            {
                String className = activity.getPackageName();
                // load "ResConstant"
                Class cls = Class.forName(className + ".R$string");
                // get all fields
                Field[] fields = cls.getDeclaredFields();
                String fieldName;
                for (Field field : fields)
                {
                    fieldName =  field.getName().toUpperCase();
                    if (ResKit.instance().hasResName(fieldName))
                    {
                        resI18N.put(fieldName, field.getInt(null));
                    }
                }
            }
            catch (Exception e)
            {
                
            }
        }
        String str = null;
        Integer id = resI18N.get(resName);
        if (id != null)
        {
            str = activity.getResources().getString(id);
        }
        if (str == null || str.length() == 0)
        {
            str = ResKit.instance().getLocalString(resName);
        }
        return str;
    }
    
    
    /**
     * added resource ID for internationalization
     * 
     * @param   resName   resource name, The value must be the same as field name in the resource file
     * @param   resID     The value in the "R.java"
     */
    protected void addI18NResID(String resName, int resID)
    {
        if (resI18N == null)
        {
            resI18N = new HashMap<String, Integer>();
        }
        resI18N.put(resName, resID);
    }
    
    /**
     * 
     * @param saveLog
     */
    public void setWriteLog(boolean saveLog)
    {
        this.writeLog = saveLog;
    }
    
    /**
     * 
     * @return
     */
    public boolean isWriteLog()
    {
        return writeLog;            
    }
    
    /**
     * 
     * @param isDrawVectorPicImmediately
     */
    public void setThumbnail(boolean isThumbnail)
    {
        this.isThumbnail = isThumbnail;
    }
    
    /**
     * 
     * @return
     */
    public boolean isThumbnail()
    {
        return isThumbnail;
    }
    
    /**
     * set view background
     * @param bgColor
     */
    public void setViewBackground(Object bg)
    {
    	this.bg = bg;
    }
    
    /**
     * 
     */
    public Object getViewBackground()
    {
    	return bg;
    }
    
    /**
     * set flag whether fitzoom can be larger than 100%, but be smaller than the max zoom
     * @param ignoreOriginalSize
     */
    public void setIgnoreOriginalSize(boolean ignoreOriginalSize)
    {
    	this.ignoreOriginalSize = ignoreOriginalSize;
    }
    
    /**
     * 
     * @return
     * true fitzoom may be larger than 100%, but be smaller than the max zoom
     * false fitzoom can not larger than 100%
     */
    public boolean isIgnoreOriginalSize()
    {
    	return ignoreOriginalSize;
    }
    
    /**
     * set page list view moving in horizontal or vertical
     * @param position
     */
    public void setPageListViewMovingPosition(byte position)
    {
    	this.pageListViewMovingPosition = position;
    }
    
    /**
     * page list view moving position
     * @param position horizontal or vertical
     */
    public byte getPageListViewMovingPosition()
    {
    	return pageListViewMovingPosition;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        app = null;
        activity = null;
        updateStatusListener = null;
        touchEventListener = null;
        errorListener = null;
        openFileFinishListener = null;
        txtDefalutEncode = null;
    }
    
    // support touch zoom in / zoom out
    private boolean isTouchZoom = true;
    // is draw page number
    private boolean isDrawPageNumber = true;
    //show message when zooming or nor
    private boolean showZoomingMsg = true;
    //popup error dialog when throw err or not
    private boolean popupErrorDlg = true;
    
    //show password dialog
    private boolean showPasswordDlg = true;
    //show progress bar when parsing document
    private boolean showProgressbarDlg = true;
    // show find dialog
    private boolean showFindDlg = true;
    //show txt encode chosing dialog
    private boolean showTXTEncodeDlg = true;
    // page size greater then view size do change page
    private boolean isChangePage = true;
    //txt default encode
    private String txtDefalutEncode;
    // normal view, changed after zoom bend, you need to re-layout
    private boolean isZoomAfterLayoutForWord = true;
    // Word application default view (Normal or Page)，0 = page view, 1 = normal view;
    private byte wordDefaultView;
    //
    private int bottomBarHeight;
    //
    private int topBarHeight;
    // application name    
    private String appName;
    //
    private Application app;
    //
    private Activity activity;
    //
    private TouchEventListener touchEventListener;
    //
    private UpdateStatusListener updateStatusListener;
    //
    private OpenFileFinishListener openFileFinishListener;
    //
    private ErrorListener errorListener;
    //
    private Map<String, Integer> resI18N;
    
    //whether write log to temporary file
    private boolean writeLog = true;
    //open file to get thumbnail, or not
    private boolean isThumbnail;
    //view background
    private Object bg = Color.GRAY;
    
    //
    private boolean ignoreOriginalSize = false;
    private byte pageListViewMovingPosition = IPageListViewListener.Moving_Horizontal;
}
