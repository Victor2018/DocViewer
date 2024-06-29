/*
 * 文件名称:          IActivity.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:24:10
 */
package com.nvqquy98.lib.doc.office.system;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

/**
 * activity interface
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-15
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IMainFrame
{
    // onTouch
    public final static byte ON_TOUCH = 0;
    // onDown
    public final static byte ON_DOWN = 1;
    // onShowPresso
    public final static byte ON_SHOW_PRESS = 2;
    // onSingleTapUp
    public final static byte ON_SINGLE_TAP_UP = 3;
    // onScroll
    public final static byte ON_SCROLL = 4;
    // onLongPress
    public final static byte ON_LONG_PRESS = 5;
    // onFling
    public final static byte ON_FLING = 6;
    // onSingleTapConfirmed
    public final static byte ON_SINGLE_TAP_CONFIRMED = 7;
    // onDoubleTap
    public final static byte ON_DOUBLE_TAP = 8;
    // onDoubleTapEvent
    public final static byte ON_DOUBLE_TAP_EVENT = 9;
    // onClick
    public final static byte ON_CLICK = 10;

    /**
     * get activity instance
     * @return activity instance
     */
    public Activity getActivity();
    
    /**
     * do action 
     *
     * @param actionID action ID 
     * 
     * @param obj acValue
     * 
     * @return  True if the listener has consumed the event, false otherwise. 
     */
    public boolean doActionEvent(int actionID, Object obj);
    
    /**
     * reader file finish call this method
     */
    public void openFileFinish();

    public void openFileFailed();
    
    /**
     * update tool bar status
     */    
    public void updateToolsbarStatus();
    
    /**
     * set the find back button and find forward button state
     * 
     * @param state
     */
    public void setFindBackForwardState(boolean state);
    
    /**
     * get bottom  bar height
     * @return bottom bar height
     */
    public int getBottomBarHeight();
    
    /**
     * get top bar height
     * @return top bar height
     */
    public int getTopBarHeight();
    
    /**
     * get application name;
     * @return application name
     */
    public String getAppName();
    
    /**
     * 
     * @return
     */
    public File getTemporaryDirectory();
    
    /**
     * event method, office engine dispatch 
     * 
     * @param       v             event source
     * @param       e1            MotionEvent instance
     * @param       e2            MotionEvent instance
     * @param       xValue        eventNethodType is ON_SCROLL, this is value distanceX
     *                             eventNethodType is ON_FLING, this is value velocityY
     *                             eventNethodType is other type, this is value -1   
     * 
     * @param       yValue        eventNethodType is ON_SCROLL, this is value distanceY
     *                             eventNethodType is ON_FLING, this is value velocityY
     *                             eventNethodType is other type, this is value -1  
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
    public boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float xValue, float yValue, byte eventMethodType);
    
    /**
     * is support draw page number
     * @return  true  draw page number
     *           false don’t draw page number
     */

    public boolean isDrawPageNumber();
    
    /**
     * true: show message when zooming
     * false: not show message when zooming
     * @return
     */
    public boolean isShowZoomingMsg();
    
    /**
     * true: pop up dialog when throw err
     * false: not pop up dialog when throw err
     * @return
     */
    public boolean isPopUpErrorDlg();
    
    /**
     * show password dialog when parse file with password
     * @return
     */
    public boolean isShowPasswordDlg(); 
    
    /**
     * show progress bar or not when parsing document
     * @return
     */
    public boolean isShowProgressBar();
    
    /**
     * 
     */
    public boolean isShowFindDlg();
    
    /**
     * show txt encode dialog when parse txt file
     * @return
     */
    public boolean isShowTXTEncodeDlg();
    
    /**
     * get txt default encode when not showing txt encode dialog
     * @return null if showing txt encode dialog
     */
    public String getTXTDefaultEncode();
    
    /**
     * is support zoom in / zoom out
     * 
     * @return  true  touch zoom
     *           false don’t touch zoom
     */

    public boolean isTouchZoom();
    
    /**
     * normal view, changed after zoom bend, you need to re-layout
     * 
     *  @return  true   re-layout
     *            false  don't re-layout   
     */
    public boolean isZoomAfterLayoutForWord();
    
    /**
     * Word application default view (Normal or Page)
     * 
     * @return 0, page view
     *          1，normal view;
     *           
     */
    public byte getWordDefaultView();
    
    /**
     * get Internationalization resource
     * 
     * @param resName Internationalization resource name
     */
    public String getLocalString(String resName);
    
    /**
     * callback this method after zoom change
     */
    public void changeZoom();
    
    /**
     * 
     */
    public void changePage();
    
    /**
     * 
     */
    public void completeLayout();
    
    /**
     * when engine error occurred callback this method
     */
    public void error(int errorCode);
    
    /**
     * full screen, not show top tool bar
     */
    public void fullScreen(boolean fullscreen);
    
    /**
     * 
     * @param visible
     */
    public void showProgressBar(boolean visible);
    
    /**
     * 
     * @param viewList
     */
    public void updateViewImages(List<Integer> viewList);   

    
    /**
     *  set change page flag, Only when effectively the PageSize greater than ViewSize.
     *  (for PPT, word print mode, PDF)
     *  
     *  @param b    = true, change page
     *              = false, don't change page
     */
    public boolean isChangePage();
    
    /**
     * when need destroy office engine instance callback this method
     */
    //public void destroyEngine();
    
    /**
     * 
     * @param saveLog
     */
    public void setWriteLog(boolean saveLog);
    
    /**
     * 
     * @return
     */
    public boolean isWriteLog();
    
    /**
     * 
     * @param isThumbnail
     */
    public void setThumbnail(boolean isThumbnail);
    
    /**
     * 
     * @return
     */
    public boolean isThumbnail();
    
    /**
     * get view backgrouond
     * @return
     */
    public Object getViewBackground();
    
    /**
     * set flag whether fitzoom can be larger than 100% but smaller than the max zoom
     * @param ignoreOriginalSize
     */
    public void setIgnoreOriginalSize(boolean ignoreOriginalSize);
    
    /**
     * 
     * @return
     * true fitzoom may be larger than 100% but smaller than the max zoom
     * false fitzoom can not larger than 100%
     */
    public boolean isIgnoreOriginalSize();
    
    /**
     * page list view moving position
     * @param position horizontal or vertical
     */
    public int getPageListViewMovingPosition();
    
    /**
     * 
     */
    public void dispose();
}
