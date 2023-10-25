
package com.cherry.lib.doc.office.macro;

import java.util.Vector;

import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.office.constant.MainConstant;
import com.cherry.lib.doc.office.java.awt.Rectangle;
import com.cherry.lib.doc.office.system.FileKit;
import com.cherry.lib.doc.office.system.MainControl;
import com.cherry.lib.doc.office.system.beans.pagelist.IPageListViewListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class Application
{
    // word application
    public static final byte APPLICATION_TYPE_WP = MainConstant.APPLICATION_TYPE_WP;
    // excel application
    public static final byte APPLICATION_TYPE_SS = MainConstant.APPLICATION_TYPE_SS;
    // PowerPoint application
    public static final byte APPLICATION_TYPE_PPT = MainConstant.APPLICATION_TYPE_PPT;
    // PDF application
    public static final byte APPLICATION_TYPE_PDF = MainConstant.APPLICATION_TYPE_PDF;
    // text application
    public static final byte APPLICATION_TYPE_TXT = MainConstant.APPLICATION_TYPE_WP;
    
    //zoom
    public static final int STANDARD_RATE = MainConstant.STANDARD_RATE;
    public static final int MAXZOOM = MainConstant.MAXZOOM;
    public static final int MAXZOOM_THUMBNAIL = MainConstant.MAXZOOM_THUMBNAIL;
    
    // Drawing mode
    //not callout mode
    public static final int DRAWMODE_NORMAL = MainConstant.DRAWMODE_NORMAL;
    //draw callout
    public static final int DRAWMODE_CALLOUTDRAW = MainConstant.DRAWMODE_CALLOUTDRAW;
    //erase callout
    public static final int DRAWMODE_CALLOUTERASE = MainConstant.DRAWMODE_CALLOUTERASE;
    
    //page list view moving position
    public static final byte MOVING_HORIZONTAL = IPageListViewListener.Moving_Horizontal;
    public static final byte MOVING_VERTICAL = IPageListViewListener.Moving_Vertical;
    
    //the max size of thumbnail
    public static final int THUMBNAILSIZE = 1000;
    
    /**
     * Constructor
     * 
     * @param frame IMainFrame instance
     * @param parent office engine component parent 
     */
    public Application(Activity activity, ViewGroup parent)
    {
        frame = new MacroFrame(this, activity);
        mainControl =  new MainControl(frame);
        this.parent = parent;
    }
    
    /**
     * set view background(color or drawble, not support resource id)
     * @param bg
     */
    public void setViewBackground(Object bg)
    {
    	if(frame != null && bg != null && (bg instanceof Integer || bg instanceof Drawable))
    	{
    		frame.setViewBackground(bg);
    	}
    }
    
    /**
     * added export image listener 
     * 
     * @param listener OfficeToPictureListener instance
     */
    public void addOfficeToPictureListener(OfficeToPictureListener listener)
    {
        if (listener != null)
        {
            mainControl.setOffictToPicture(new MacroOfficeToPicture(listener));    
        }
    }
    /**
     * add dialog listener
     * @param dlgListener dialog listener
     */
    public void addDialogListener(DialogListener dlgListener)
    {          
        if (dlgListener != null)
        {
            mainControl.setCustomDialog(new MacroCustomDialog(dlgListener));
        }
    } 
    
    /**
     * add slideshow listener
     * @param listener slideshow listener
     */
    public void addSlideShowListener(SlideShowListener listener)
    {          
        if (listener != null)
        {
            mainControl.setSlideShow(new MacroSlideShow(listener));
        }
    } 
    
    /**
     * added open file finish listener
     * 
     * @param listener OpenFileFinishListener instance
     */
    public void addOpenFileFinishListener(OpenFileFinishListener listener)
    {
        frame.addOpenFileFinishListener(listener);
    }
    
    /**
     * added touch event listener
     * 
     * @param listener  TouchEventListener instance
     */
    public void addTouchEventListener(TouchEventListener listener)
    {
        frame.addTouchEventListener(listener);
    }
    
    /**
     * added update status listener 
     * 
     * @param listener  UpdateStatusListener instance
     */
    public void addUpdateStatusListener(UpdateStatusListener listener)
    {
        frame.addUpdateStatusListener(listener);
    }  
    
    /**
     * added error listener
     * 
     * @param listener ErrorListener instance
     */
    public void addErrorListener(ErrorListener listener)
    {
        frame.addErrorListener(listener);
    }  
    
    /**
     * @param filePath  the open file of absolute path
     * 
     * @return true   Open the file successfully
     *          false  Open the file failed
     */
    public boolean openFile(String filePath)
    {
        // word
        String file = filePath.toLowerCase();
        if (file.endsWith(MainConstant.FILE_TYPE_DOC)
            || file.endsWith(MainConstant.FILE_TYPE_DOCX)
            || file.endsWith(MainConstant.FILE_TYPE_DOT)
            || file.endsWith(MainConstant.FILE_TYPE_DOTX)
            || file.endsWith(MainConstant.FILE_TYPE_DOTM))
        {
            applicationType = MainConstant.APPLICATION_TYPE_WP;
        }
        // excel
        else if (file.endsWith(MainConstant.FILE_TYPE_XLS)
                 || file.endsWith(MainConstant.FILE_TYPE_XLSX)
                 || file.endsWith(MainConstant.FILE_TYPE_XLT)
                 || file.endsWith(MainConstant.FILE_TYPE_XLTX)
                 || file.endsWith(MainConstant.FILE_TYPE_XLTM)
                 || file.endsWith(MainConstant.FILE_TYPE_XLSM))
        {
            applicationType = MainConstant.APPLICATION_TYPE_SS;
        }
        // PowerPoint
        else if (file.endsWith(MainConstant.FILE_TYPE_PPT)
                 || file.endsWith(MainConstant.FILE_TYPE_PPTX)
                 || file.endsWith(MainConstant.FILE_TYPE_POT)
                 || file.endsWith(MainConstant.FILE_TYPE_PPTM)
                 || file.endsWith(MainConstant.FILE_TYPE_POTX)
                 || file.endsWith(MainConstant.FILE_TYPE_POTM))
        {
            applicationType = MainConstant.APPLICATION_TYPE_PPT;
        }
        // PDF document
        else if (file.endsWith(MainConstant.FILE_TYPE_PDF))
        {
            applicationType = MainConstant.APPLICATION_TYPE_PDF;
        }
        else
        {
            // set word default view is normal view mode
            if(frame != null && frame.isThumbnail())
            {
                setDefaultViewMode(0);
            }
            else
            {
                setDefaultViewMode(1);
            }
            applicationType = MainConstant.APPLICATION_TYPE_WP;
        }
        mainControl.openFile(filePath);
        
        return true;
    }
    
    /**
     * reader file finish call this method
     * {@hide}
     */
    protected void openFileFinish()
    {
        //
        View app = getView();
        if (parent != null)
        {
            parent.addView(app, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
//        else
//        {
//        	//直锟接癸拷锟斤拷activity锟较伙拷影锟斤拷activity锟侥诧拷锟斤拷
//        	frame.getActivity().setContentView(app, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//        }
    }
    
    /**
     * get application type
     * 
     * @return  = 0  word application
     *           = 1  excel application
     *           = 2  PowerPoint application
     *           = 3  PDF application
     *           = 4  TXT application
     *           = -1 error
     * 
     */
    public byte getApplicationType()
    {
        return applicationType;
    }
    
    /**
     * get office engine component
     * 
     * @return View
     * 
     */
    public View getView()
    {
        if (mainControl == null)
        {
            return null;
        }
        return mainControl.getView();
    }
    
    // ============ public engine method =======
    /**
     *  page down action
     */
    public void pageDown()
    {
        if (mainControl == null)
        {
            return ;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_DOWN_ID, null);
    }
    
    /**
     * page up action
     */
    public void pageUp()
    {
        if (mainControl == null)
        {
            return ;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_UP_ID, null);
    }
    
    /**
     * find 
     * 
     * @param str   find of assign content 
     * 
     * @return  true, found
     *           false  no found
     * 
     */
    public boolean find(String str)
    {
        if (mainControl == null 
            || str == null 
            || str.trim().length() == 0 
            || isSlideShowMode())
        {
            return false;
        }
        boolean finded = mainControl.getFind().find(str);
        if(!finded && mainControl.getMainFrame().isShowFindDlg())
        {
            if (applicationType == APPLICATION_TYPE_PDF)
            {
                return finded;
            }
            if(toast == null)
            {  
                toast = Toast.makeText(mainControl.getView().getContext(), "", Toast.LENGTH_SHORT);
            }
            toast.setText(mainControl.getMainFrame().getLocalString("DIALOG_FIND_NOT_FOUND"));
            toast.show(); 
        }
        
        return finded;
    }
    
    /**
     * find of backward
     * 
     * 
     * @return  true, found
     *           false  no found
     */
    public boolean findBackward()
    {
        if (mainControl == null || isSlideShowMode())
        {
            return false;
        }
        boolean finded = mainControl.getFind().findBackward();
        if(!finded && mainControl.getMainFrame().isShowFindDlg())
        {
            if (applicationType == APPLICATION_TYPE_PDF)
            {
                return finded;
            }
            if(toast == null)
            {  
                toast = Toast.makeText(mainControl.getView().getContext(), "", Toast.LENGTH_SHORT);
            }
            toast.setText(mainControl.getMainFrame().getLocalString("DIALOG_FIND_TO_BEGIN"));
            toast.show(); 
        }
        return finded;
    }
    
    /**
     * find of forward
     * 
     * @param str
     * 
     * @return  true, found
     *           false  no found
     */
    public boolean findForward()
    {
        if (mainControl == null || isSlideShowMode())
        {
            return false;
        }
        boolean finded = mainControl.getFind().findForward();
        if(!finded && mainControl.getMainFrame().isShowFindDlg())
        {
            if (applicationType == APPLICATION_TYPE_PDF)
            {
                return finded;
            }
            if(toast == null)
            {  
                toast = Toast.makeText(mainControl.getView().getContext(), "", Toast.LENGTH_SHORT);
            }
            toast.setText(mainControl.getMainFrame().getLocalString("DIALOG_FIND_TO_END"));
            toast.show(); 
        }
        return finded;
    }
    
    
    /**
     * set is support zoom in / zoom out?
     * 
     * @param value    true  support zoom in / zoom out
     *                  false don't support zoom in / zoom out
     */
    public void setTouchZoom(boolean value)
    {
        if (frame == null)
        {
            return;
        }
        frame.setTouchZoom(value);
    }
    
    /**
     * set is support draw page number?
     * 
     * @param value    true  draw page number
     *                  false don锟斤拷t draw page number
     */
    public void setDrawPageNumber(boolean value)
    {
        if (frame == null)
        {
            return;
        }
        frame.setDrawPageNumber(value);
    }    
    
    /**
     * show or hide zooming message
     * @param showZoomingMsg
     */
    public void setShowZoomingMsg(boolean showZoomingMsg)
    {
        if (frame == null)
        {
            return;
        }
        frame.setShowZoomingMsg(showZoomingMsg);
    }
    
    /**
     * pop up error dialog or not
     * @param popupErrorDlg
     */
    public void setPopUpErrorDlg(boolean popupErrorDlg)
    {
        if (frame == null)
        {
            return;
        }
        frame.setPopUpErrorDlg(popupErrorDlg);
    }
    
    /**
     * set flag showing password dialog or not when parsing document with password
     * @param showPasswordDlg show password dialog or not
     */
    public void setShowPasswordDlg(boolean showPasswordDlg)
    {
        if (frame != null)
        {
            frame.setShowPasswordDlg(showPasswordDlg);
        }
        
    }
    
    /**
     * set flag showing find dialog
     * @param b show find dialog flag
     */
    public void setShowFindDlg(boolean b)
    {
        if (frame != null)
        {
            frame.setShowFindDlg(b);
        }
    }
    
    /**
     * 
     * @param showProgressbarDlg
     */
    public void setShowProgressBar(boolean showProgressbarDlg)
    {
        if (frame != null)
        {
            frame.setShowProgressBar(showProgressbarDlg);
        }
        
    }
    
    /**
     * 
     * @param showTXTEncodeDlg
     */
    public void setShowTXTEncodeDlg(boolean showTXTEncodeDlg)
    {
        if (frame != null)
        {
            frame.setShowTXTEncodeDlg(showTXTEncodeDlg);
        }        
    }
    
    /**
     * set TXT default encode
     * @param enode
     */
    public void setTXTDefaultEncode(String enode)
    {
        if (frame != null)
        {
            frame.setTXTDefaultEncode(enode);
        } 
    }
    
    /**
     * set application name
     * 
     * @param name  application name
     */
    public void setAppName(String name)
    {
        if (frame == null)
        {
            return;
        }
        frame.setAppName(name);
    }
    
    /**
     * set top bar height
     * 
     * @param vlaue  top bar height
     */
    public void setTopBarHeight(int value)
    {
        if (frame == null || parent == null)
        {
            return;
        }
        if (value >= parent.getContext().getResources().getDisplayMetrics().heightPixels / 2)
        {
            return;
        }
        frame.setTopBarHeight(value);
    }
    
    /**
     * set bottom bar height
     * 
     * @param value  bottom bar height
     */
    public void setBottomBarHeight(int value)
    {
        if (frame == null || parent == null)
        {
            return;
        }
        if (value >= parent.getContext().getResources().getDisplayMetrics().heightPixels / 2)
        {
            return;
        }
        frame.setBottomBarHeight(value);
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
        if (frame != null)
        {
            frame.setChangePage(b);
        }
    }
    
    /**
     * get zoom value(in fact, the standard ratio of zoom is value/STANDARD_RATE)
     * @see #STANDARD_RATE
     * @return  percentage value
     */
    public int getZoom()
    {
        if (mainControl == null)
        {
            return STANDARD_RATE;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_ZOOM_ID, null);
        return obj == null ? STANDARD_RATE : (int)Math.round(((Float)obj).floatValue() * STANDARD_RATE);
    }
    
    /**
     * get The value of the fit(in fact, the standard ratio of zoom is value/STANDARD_RATE)
     * 
     * @return  value of the fit
     */
    public int getFitZoom()
    {
        if (mainControl == null)
        {
            return STANDARD_RATE;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_FIT_ZOOM_ID, null);
        return obj == null ? STANDARD_RATE : (int)Math.round(((Float)obj).floatValue() * STANDARD_RATE);
    }
    
    /**
     * 
     * @param value
     */
    /*public void setZoom(int value)
    {
        setZoom(value, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }*/
    
    /**
     * @see #MAXZOOM
     * @param value
     * @param pointX
     * @param pointY
     */
    public void setZoom(int value, int pointX, int pointY)
    {
        if (mainControl == null || getView() == null || value >  MAXZOOM
            || value < getFitZoom() || value == getZoom()
            || isSlideShowMode())
        {
            return ;
        }
        if (pointX < 0 || pointY < 0
            || pointX > getView().getWidth()
            || pointY > getView().getHeight())
        {
            pointX = Integer.MIN_VALUE;
            pointY = Integer.MIN_VALUE;
        }
            
        mainControl.actionEvent(EventConstant.APP_ZOOM_ID, new int[]{value, pointX, pointY});
        getView().postInvalidate();
        if (parent != null)
        {
            parent.post(new Runnable()
            {
                
                @ Override
                public void run()
                {
                    if (mainControl != null)
                    {
                        try
                        {
                            if (applicationType == APPLICATION_TYPE_SS)
                            {
                                mainControl.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                            }
                            else if (applicationType == APPLICATION_TYPE_WP && getViewMode() != 2)
                            {
                                mainControl.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                            }
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
            });
            if (applicationType == APPLICATION_TYPE_WP
                && frame.isZoomAfterLayoutForWord())
            {
                mainControl.actionEvent(EventConstant.WP_LAYOUT_NORMAL_VIEW, null);
            }
        }
        
    }
    
    /**
     * set fit size for PPT锟斤拷Word view mode, PDf
     * 
     * @param  value  fit size mode
     *          = 0, fit size of minimum value of pageWidth / viewWidth, pageHeight / viewHeight and 1.0, this is default mode
     *          = 1, fit size of pageWidth
     *          = 2, fit size of PageHeight
     */
    public void setFitSize(int value)
    {   
        if (mainControl == null)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_SET_FIT_SIZE_ID, value);
    }
    
    /**
     * get fit size statue
     * 
     * @return fit size statue
     *          = 0, left/right and top/bottom don't alignment 
     *          = 1, top/bottom alignment
     *          = 2, left/right alignment
     *          = 3, left/right and top/bottom alignment 
     */ 
    public int getFitSizeState()
    {
        if (mainControl == null || applicationType == APPLICATION_TYPE_SS
            ||(applicationType == APPLICATION_TYPE_WP && getViewMode() != 2))
        {
            return 0;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_FIT_SIZE_STATE_ID, null);
        return obj == null ? 3 : (Integer)obj;
        
        /*Rectangle rect = null;
        if (applicationType == APPLICATION_TYPE_PPT)
        {
            rect = getSlideSize();
        }
        else if(applicationType == APPLICATION_TYPE_PDF)
        {
            rect = getPDFPageSize();
        }
        else
        {
            rect = getPageSize();
        }
        if (rect == null)
        {
            return state;
        }
        View view = getView();
        if (view == null)
        {
            return state;
        }
        int zoom = getZoom();
        int wZoom = (int)(Math.round(view.getWidth() / (float)rect.width * 100));
        int hZoom = (int)(Math.round(view.getHeight() / (float)rect.height * 100));
        int vWidth = (int)(rect.width * wZoom);
        int  ight = (int)(rect.height * hZoom); 
        // left/right and top/bottom alignment
        if (zoom == vWidth && view.getHeight() ==  ight)
        {
            state = 3;
        }     
        // left/right alignment;
        else if(view.getWidth() == vWidth && view.getHeight() !=  ight)
        {
            state = 2;
        }
        // top/bottom alignment
        else if (view.getWidth() != vWidth && view.getHeight() ==  ight)
        {
            state = 1;
        }
        return state;*/
    }
    
    /**
     * get the snapshot
     * 
     * @param destBitmap  
     */
    public Bitmap getSnapshot(Bitmap destBitmap)
    {
        if (mainControl == null || destBitmap == null)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_SNAPSHOT_ID, destBitmap);
        return obj == null ? null : (Bitmap)obj; 
    }
    
    // ============ Word engine method ============    
    /**
     * get pages count
     * 
     * @return pages count
     */
    public int getPagesCount()
    {
        if (mainControl == null)
        {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_COUNT_PAGES_ID, null);
        return obj == null ? -1 : (Integer)obj;
        
    }
    
    /**
     * get current page number (base 1)
     * 
     * @return current page number
     */
    public int getCurrentPageNumber()
    {
        if (mainControl == null)
        {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_CURRENT_PAGE_NUMBER_ID, null);
        return obj == null ? -1 : (Integer)obj;
    }    
    
    /**
     * get page image raw data
     * 
     * @param pageNumber    page number (base 1)
     * @return Bitmap image raw data
     */
    public Bitmap getPageToImage(int pageNumber)
    {
        if (mainControl == null || pageNumber < 1)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.WP_PAGE_TO_IMAGE, pageNumber);
        return obj == null ? null : (Bitmap)obj; 
    }
    
    /**
     * get specific area of whole page to image with specified size.
     * if the specific area is not completely contained in the entire page area, return null
     * @param pageNumber page number (base 1)
     * @param srcLeft The x coordinate of source area
     * @param srcTop The y coordinate of source area
     * @param srcWidth width of source area
     * @param srcHeight height of source area
     * @param desWidth width of destination area
     * @param desHeight height of destination area
     * @return specific area image
     */
    private Bitmap getAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight)
    {
        if (mainControl == null || pageNumber < 1)
        {
            return null;
        } 
        
        Object obj = mainControl.getActionValue(EventConstant.APP_PAGEAREA_TO_IMAGE,
            new int[]{pageNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight});
        return obj == null ? null : (Bitmap)obj; 
    }
    
    /**
     * get specific area of whole page to image with specified size.
     * if the specific area is not completely contained in the entire page area, return null
     * @param pageNumber page number (base 1)
     * @param srcLeft The x coordinate of source area
     * @param srcTop The y coordinate of source area
     * @param srcWidth width of source area
     * @param srcHeight height of source area
     * @param desWidth width of destination area
     * @param desHeight height of destination area
     * @return specific area image
     */
    public Bitmap getPageAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight)
    {
        if (applicationType != MainConstant.APPLICATION_TYPE_WP)
        {
            return null;
        }        
        
        return getAreaToImage(pageNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight);
    }
    
    /**
     * get page size 
     * 
     * @param pageNumber page number (base 1)
     * @return page size
     */
    public Rectangle getPageSize(int pageNumber)
    {
        if (mainControl == null)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.WP_GET_PAGE_SIZE, pageNumber);
        return obj == null ? null : (Rectangle)obj;
    }
    
    /**
     * get current view mode of word application (Normal view mode or Page view mode or Print view mode) 
     * 
     * @return value   0, page view mode
     *                  1锟斤拷normal view mode
     *                  2, print view mode
     *                  -1, error
     */
    public int getViewMode()
    {
        if (mainControl == null)
        {
            return  -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.WP_GET_VIEW_MODE, null);
        return obj == null ? 0 : (Integer)obj;
    }
    
    /**
     * set word application default view (Normal view mode or Page view mode or Print view mode)
     * 
     * @param  value   0, page view mode
     *                  1锟斤拷normal view mode
     *                  2, print view mode
     *           
     */
    public void setDefaultViewMode(int viewMode)
    {
        if (frame == null)
        {
            return;
        }
        if (viewMode < 0 || viewMode > 2)
        {
            viewMode = 0;
        }
        frame.setWordDefaultView((byte)viewMode);
    }
    
    /**
     * set normal view, changed after zoom bend, you need to re-layout
     * 
     * @param   true   re-layout
     *           false  don't re-layout
     */
    public void setZoomAfterLayoutForNormalView(boolean value)
    {
        if (frame == null)
        {
            return;
        }
        frame.setZoomAfterLayoutForWord(value);
    }  

    /**
     * switch page for page index (base 0)
     * 
     * @param index     page index
     */
    public void showPage(int index)
    {
        if (mainControl == null || index < 0)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.WP_SHOW_PAGE, index);
    }
    
    /**
     * set word application default view (Normal view or Page view)
     * 
     * @param  value   0, page view mode
     *                  1锟斤拷normal view mode
     *                  2, print view mode
     *           
     */
    public void switchViewMode(int viewMode)
    {
        if (mainControl == null)
        {
            return ;
        }
        if (viewMode <0 || viewMode > 2)
        {
            viewMode = 0;
        }
        mainControl.actionEvent(EventConstant.WP_SWITCH_VIEW, viewMode);
    }
    
    // ============  Excel engine method ============== 
    /**
     * get all sheet name
     * 
     * return vector instance锟斤拷
     *      ex. There are 3 worksheet (Sheet1, Sheet2, Sheet3), then,
     *      vector.get(0) =锟斤拷sheet1锟斤拷
     *      vector.get(1) =锟斤拷sheet2锟斤拷
     *      vector.get(2) =锟斤拷sheet3锟斤拷
     */

    public Vector<String> getAllSheetName()
    {
        if (mainControl == null)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.SS_GET_ALL_SHEET_NAME, null);
        return obj == null ?  null : (Vector<String>)obj;
    }
    
    /**
     * get sheet name
     * @param   sheetNumber  sheet number  (base 1) 
     * 
     * @return sheet name
     */
    public String getSheetName(int sheetNumber)
    {
        if (mainControl == null || sheetNumber < 1)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.SS_GET_SHEET_NAME, sheetNumber);
        return obj == null ? null : (String)obj;
    }
    
    /**
     * get current sheet number (base 1)
     * 
     * @return current page number
     */
    public int getCurrentSheetNumber()
    {
        if (mainControl == null)
        {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_CURRENT_PAGE_NUMBER_ID, null);
        return obj == null ? -1 : (Integer)obj;
    }
    
    /**
     * get sheet count
     * 
     * @return sheet count
     */
    public int getSheetsCount()
    {
        if (mainControl == null)
        {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_COUNT_PAGES_ID, null);
        return obj == null ? -1 : (Integer)obj;
    }

    
    /**
     * show sheet for index (base 0)
     * 
     * @param index     sheet index
     * 
     */
    public void showSheet(int index)
    {
        if (mainControl == null || index < 0)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.SS_SHOW_SHEET, index);
    }
    
    /**
     * remove excel application default sheet bar
     * 
     */
    public void removeDefaultSheetBarForExcel()
    {
        mainControl.actionEvent(EventConstant.SS_REMOVE_SHEET_BAR, null);
    }
    
    
    // ============= PowerPoint engine method ===============    
    /**
     *  show previous slide
     */
    public void previousSlide()
    {
        if (mainControl == null)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_UP_ID, null);
    }
    
    /**
     * show next slide 
     */
    public void nextSlide()
    {
        if (mainControl == null)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.APP_PAGE_DOWN_ID, null);
    }
    
    /**
     * get slide count
     * 
     * @return slide count
     */
    public int getSlidesCount()
    {
        if (mainControl == null)
        {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_COUNT_PAGES_ID, null);
        return obj == null ? -1 : (Integer)obj;
    } 
    
    /**
     * 
     */
    /**
     * The actual loaded into memory the Slide Count
     * 
     * @return slide count
     */
    public int getLoadSlidesCount()
    {
        if (mainControl == null)
        {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_REAL_PAGE_COUNT_ID, null);
        return obj == null ? -1 : (Integer)obj;
    }
    
    /**
     * get current slide number (base 1)
     * 
     * @return current slide number 
     */
    public int getCurrentSlideNumber()
    {
        if (mainControl == null)
        {
            return -1;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_CURRENT_PAGE_NUMBER_ID, null);
        return obj == null ? -1 : (Integer)obj;
    }
    
    /**
     * get slide node for slide number (base 1)
     * 
     * @param slideNumber slide number
     * 
     * @return slide note
     */
    public String getSlideNote(int slideNumber)
    {
        if (mainControl == null || slideNumber < 1)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_GET_SLIDE_NOTE, slideNumber);
        return obj == null ? null : (String)obj;
    }
    
    /**
     * get slide size 
     * 
     * @param slideNumber slide number(base 1)
     * @return page size
     */
    public Rectangle getSlideSize(int slideNumber)
    {
        if (mainControl == null)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_GET_SLIDE_SIZE, slideNumber);
        return obj == null ? null : (Rectangle)obj;
    }
    
    
    /**
     * show slide of index (base 0)
     * 
     * @param index slide index
     */
    public void showSlide(int index)
    {
        if (mainControl == null || index < 0)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.PG_SHOW_SLIDE_ID, index);
    }
    
    /**
     * get slide image raw data
     * 
     * @param slideNumber    slide number (base 1)
     * @return Bitmap image raw data
     */
    public Bitmap getSlideToImage(int slideNumber)
    {
        if (mainControl == null || slideNumber < 1)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDE_TO_IMAGE, slideNumber);
        return obj == null ? null : (Bitmap)obj; 
    }
    
    /**
     * get specific area of whole page to image with specified size.
     * if the specific area is not completely contained in the entire page area, return null
     * @param pageNumber page number (base 1)
     * @param srcLeft The x coordinate of source area
     * @param srcTop The y coordinate of source area
     * @param srcWidth width of source area
     * @param srcHeight height of source area
     * @param desWidth width of destination area
     * @param desHeight height of destination area
     * @return specific area image
     */
    public Bitmap getSlideAreaToImage(int slideNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight)
    {
        if (applicationType != MainConstant.APPLICATION_TYPE_PPT)
        {
            return null;
        }
        return getAreaToImage(slideNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight);
    }
    
    /**
     * get slide thumbnail raw data
     * @see #MAXZOOM_THUMBNAIL
     * @param slideNumber   slide number (base 1)
     * @param zoomValue     0 < thumbnail zoom value <=  MAXZOOM_THUMBNAIL
     * 
     * @return Bitma thumbnail image raw data 
     * 
     */
    public Bitmap getSlideThumbnail(int slideNumber, int zoomValue)
    {
        if (applicationType != APPLICATION_TYPE_PPT 
        		|| mainControl == null 
            || slideNumber < 1
            || zoomValue <= 0 
            || zoomValue >  MAXZOOM_THUMBNAIL)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, new int[]{slideNumber, zoomValue});
        return obj == null ? null : (Bitmap)obj;
    }
    
    
    // ============= PDF engine method ===============    
    /**
     * switch page for page index (base 0)
     * 
     * @param index
     */
    public void showPDFPage(int index)
    {
        if (mainControl == null || index < 0)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.PDF_SHOW_PAGE, index);
    }
    
    /**
     * page to image for page number (base 1)
     * 
     * @return bitmap raw data
     */
    public Bitmap getPDFPageToImage(int pageNumber)
    {
        if (mainControl == null || pageNumber < 1)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PDF_PAGE_TO_IMAGE, pageNumber);
        return obj == null ? null : (Bitmap)obj; 
    }  
    
    /**
     * get specific area of whole page to image with specified size.
     * if the specific area is not completely contained in the entire page area, return null
     * @param pageNumber page number (base 1)
     * @param srcLeft The x coordinate of source area
     * @param srcTop The y coordinate of source area
     * @param srcWidth width of source area
     * @param srcHeight height of source area
     * @param desWidth width of destination area
     * @param desHeight height of destination area
     * @return specific area image
     */
    public Bitmap getPDFPageAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight)
    {
        if (applicationType != MainConstant.APPLICATION_TYPE_PDF)
        {
            return null;
        }        
        
        return getAreaToImage(pageNumber, srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight);
    }
    
    /**
     * get PDF thumbnail raw data
     * @see #MAXZOOM_THUMBNAIL
     * @param slideNumber   slide number (base 1)
     * @param zoomValue     thumbnail zoom value
     * 
     * @return Bitma thumbnail image raw data 
     * 
     */
    public Bitmap getPDFPageThumbnail(int pageNumber, int zoomValue)
    {
        if (applicationType != APPLICATION_TYPE_PDF 
        		|| mainControl == null 
            || pageNumber < 1
            || zoomValue <= 0 
            || zoomValue >  MAXZOOM_THUMBNAIL)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, new int[]{pageNumber, zoomValue});
        return obj == null ? null : (Bitmap)obj;
    }
    
    /**
     * get PDF hyper link URL with page index
     * 
     * @param pageNumber   page number (base 1)
     * @return
     */
    public String[] getPDFHyperlinkURL(int pageNumber)
    {
        if (mainControl == null || pageNumber < 1)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_GET_HYPERLINK_URL_ID, pageNumber);
        return obj == null ? null : (String[])obj;
    }
    
    /**
     * get PDF page size 
     * 
     * @param pageNumber   page number (base 1)
     * @return page size
     */
    public Rectangle getPDFPageSize(int pageNumber)
    {
        if (mainControl == null)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.PDF_GET_PAGE_SIZE, pageNumber);
        return obj == null ? null : (Rectangle)obj;
    }
    
    /**
     * added resource ID for internationalization
     * 
     * @param   resName   resource name, The value must be the same as field name in the resource file
     * @param   resID     The value in the "R.java"
     */
    public void addI18NResID(String resName, int resID)
    {
        frame.addI18NResID(resName, resID);
    }   
    
    /**
     * 
     * @param password password of document
     * @return true: succeed authenticate False: fail authenticate
     */
    public boolean authenticatePassword(String password)
    {
        if (mainControl == null 
            || password == null 
            || password.trim().length() == 0)
        {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_AUTHENTICATE_PASSWORD, password);
        return obj == null ? false : (Boolean)obj;
    }
    
    /**
     * called after authenticated the password
     * @param password password of document
     */
    public void passwordVerified(String password)
    {
        if (mainControl == null)
        {
            return;
        }
        if(!authenticatePassword(password))
        {
            mainControl.getSysKit().getErrorKit().writerLog(new Throwable("Password is incorrect"));
        }
        else
        {
            mainControl.actionEvent(EventConstant.APP_PASSWORD_OK_INIT, password);
        }
    }
    
    /**
     * 
     * @param encode txt encode
     */
    public void txtEncodeDialogFinished(String encode)
    {
        if (mainControl == null || encode == null)
        {
            return;
        }
        mainControl.actionEvent(EventConstant.TXT_DIALOG_FINISH_ID, encode);
    }
    
    /**
     * Re-open the TXT document
     * @param filePath  document path
     * @param encode    TXT encoding
     */
    public void reopenTXT(String filePath, String encode)
    {
        if (mainControl == null || filePath == null || encode == null)
        {
            return;
        }
        if (filePath.toLowerCase().endsWith(MainConstant.FILE_TYPE_TXT))
        {
            setDefaultViewMode(1);
            applicationType = MainConstant.APPLICATION_TYPE_WP;
            mainControl.actionEvent(EventConstant.TXT_REOPNE_ID, new String[]{filePath, encode});
        }
    }
    
    /**
     * Whether the file support?
     * 
     * @param fileName  file name
     * 
     * @return  true     support
     *           false    don't support
     */
    public boolean isSupport(String fileName)
    {
       return FileKit.instance().isSupport(fileName); 
    }
    
    
    /**
     * set animation duration(ms), should be called before begin slideshow
     * @param duration larger than 100ms and less than 1200ms
     */
    public void setAnimationDuration(int duration)
    {
        if (mainControl != null && !isSlideShowMode())
        {
            duration = Math.min(1200, Math.max(duration, 100));            
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_DURATION, duration);
        }        
    }
    
    /**
     * begin slideshow from the slideIndex-th slide
     * @param slideIndex(base 1)
     */
    public void beginSlideShow(int slideIndex)
    {
        if(mainControl != null)
        {
            if(slideIndex < 1 || slideIndex > getSlidesCount())
            {
                slideIndex = 1;
            }
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_GEGIN, slideIndex >= 1 ? slideIndex : 1); 
        }        
    }
    
    /**
     * exit slideshow
     */
    public void exitSlideShow()
    {
        if(mainControl != null)
        {
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_END, null);
        }
    }
    
    /**
     * has next slide or not, be called only when it's playing
     * @return
     */
    public boolean hasNextSlide_Slideshow()
    {
        if (mainControl == null)
        {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_PAGE_DOWN_ID, null);
        return obj == null ? false : (Boolean)obj;
    }
    
    /**
     * has previous slide or not, be called only when it's playing
     * @return
     */
    public boolean hasPreviousSlide_Slideshow()
    {
        if (mainControl == null)
        {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_PAGE_UP_ID, null);
        return obj == null ? false : (Boolean)obj;
    }
    
    /**
     * has next Action or not, be called only when it's playing
     * @return
     */
    public boolean hasNextAction_Slideshow()
    {
        if (mainControl == null)
        {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_HASNEXTACTION, null);
        return obj == null ? false : (Boolean)obj;
    }
    
    /**
     * go to next action of slideshow, be called only when it's playing
     */
    public void nextAction_Slideshow()
    {
        if(mainControl != null)
        {
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_NEXT, null);
        }
    }
    
    /**
     * has previous action(slide) or not, be called only when it's playing
     * @return
     */
    public boolean hasPreviousAction_Slideshow()
    {
        if (mainControl == null)
        {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_HASPREVIOUSACTION, null);
        return obj == null ? false : (Boolean)obj;
    }
    
    /**
     * go to previous action of slideshow, be called only when it's playing
     */
    public void previousAction_Slideshow()
    {
        if(mainControl != null)
        {
            mainControl.actionEvent(EventConstant.PG_SLIDESHOW_PREVIOUS, null);
        }
    }
    
    /**
     * 
     */
    private boolean isSlideShowMode()
    {
        if (mainControl == null 
            || applicationType != APPLICATION_TYPE_PPT)
        {
            return false;
        }
        Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW, null);
        return obj == null ? false : (Boolean)obj;
    }
    
    /**
     * current slide exist or not
     * @param slideIndex(based 1)
     * @return
     */
    public boolean isSlideExist(int slideIndex)
    {
        if (mainControl != null 
            && applicationType == APPLICATION_TYPE_PPT
            && slideIndex > 0)
        {
            Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_SLIDEEXIST, slideIndex);
            return obj == null ? false : (Boolean)obj;
        }
        
        return false;
    }
    
    /**
     * animation steps of current slide
     * @param slideIndex(based 1)
     * @return
     */
    public int getSlideAnimationSteps(int slideIndex)
    {
        if(isSlideExist(slideIndex))
        {
            Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_ANIMATIONSTEPS, slideIndex);
            return obj == null ? -1 : (Integer)obj;
        }
        
        return -1;
    }
    
    /**
     * slideshow to image
     * @param slideIndex slide index(base 1)
     * @param step animation index(base 1)
     * @return
     */
    public Bitmap getSlideshowToImage(int slideIndex, int step)
    {
        if(!isSlideShowMode()
            && step > 0
            && step <= getSlideAnimationSteps(slideIndex))
        {
            Object obj = mainControl.getActionValue(EventConstant.PG_SLIDESHOW_SLIDESHOWTOIMAGE, new int[]{slideIndex, step});
            return obj == null ? null : (Bitmap)obj;
        }        
        return null;
    }
    
    /**
     * get temporary directory path
     * 
     * @return temporary directory path
     */
    public String getTemporaryDirectoryPath()
    {
        if (mainControl == null)
        {
            return null;
        }
        return mainControl.getSysKit().getPictureManage().getPicTempPath();
    }
    
    /**
     * set flag: whether write log or  not
     * @param writeLog
     */
    public void setWriteLog(boolean writeLog)
    {
        if (frame == null)
        {
            return;
        }
        frame.setWriteLog(writeLog);
    }
    
    /**
     * 
     * @param isThumbnail
     */
    public void setThumbnail(boolean isThumbnail)
    {
        if (frame == null)
        {
            return;
        }
        frame.setThumbnail(isThumbnail);
    }
    
    /**
     * get doc thumbnail
     * @see #MAXZOOM_THUMBNAIL
     * @param zoomValue     0 < thumbnail zoom value <=  MAXZOOM_THUMBNAIL
     * @return
     */
    public Bitmap getDocThumbnail(int zoomValue)
    {
        if (applicationType != APPLICATION_TYPE_WP 
        		|| mainControl == null
            || zoomValue <= 0 
            || zoomValue >  MAXZOOM_THUMBNAIL)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, zoomValue);
        return obj == null ? null : (Bitmap)obj;
    }
    
    /**
     * get txt thumbnail, just for print mode
     * @see #MAXZOOM_THUMBNAIL
     * @param zoomValue     0 < thumbnail zoom value <=  MAXZOOM_THUMBNAIL
     * @return
     */
    public Bitmap getTxtThumbnail(int zoomValue)
    {
        if(applicationType != APPLICATION_TYPE_WP 
        		|| mainControl == null
            || zoomValue <= 0 
            || zoomValue >  MAXZOOM_THUMBNAIL)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, zoomValue);
        return obj == null ? null : (Bitmap)obj;
    }
    
    /**
     * get workbook thumbnail
     * @see #STANDARD_RATE
     * @see #THUMBNAILSIZE
     * @see #MAXZOOM_THUMBNAIL
     * @param width         thumbnail width when zoomValue is STANDARD_RATE(0 < width <= THUMBNAILSIZE)
     * @param height        thumbnail height when zoomValue is STANDARD_RATE(0 < height <= THUMBNAILSIZE)
     * @param zoomValue     zoom value(0 < thumbnail zoom value <=  MAXZOOM_THUMBNAIL)
     * @return return null if the specified sheet is null or not Accomplished parse
     */
    public Bitmap getXlsThumbnail(int width, int height, int zoomValue)
    {
        if (applicationType != APPLICATION_TYPE_SS 
        		|| mainControl == null
            || width < 1 || width > THUMBNAILSIZE
            || height < 1 || height > THUMBNAILSIZE
            || zoomValue <= 0 
            || zoomValue >  MAXZOOM_THUMBNAIL)
        {
            return null;
        }
        Object obj = mainControl.getActionValue(EventConstant.APP_THUMBNAIL_ID, new int[]{width, height, zoomValue});
        return obj == null ? null : (Bitmap)obj;
    }
    
    /**
     * dispose memory, must be called, otherwise the memory can not be freed,
     * It will affect the next open file performance
     * 
     */
    public void dispose()
    {   
        if (parent != null)
        {
            if (getView() != null)
            {
                parent.removeView(getView());
            }
        }
        if (mainControl.getReader() != null)
        {
            mainControl.getReader().abortReader();
        }
        if (frame != null)
        {
            frame.dispose();
            frame = null;
        }
        if (mainControl != null)
        {
            mainControl.dispose();
            mainControl = null;
        }
        parent = null;
    }
    
    /**
     * get line width of callout
     * width range: (1 ~ 10)
     */
    public int getCalloutLineWidth()
    {
    	if (mainControl == null)
    	{
    		return 1;
    	}
    	return mainControl.getSysKit().getCalloutManager().getWidth();
    }
    
    /**
     * set line width of callout
     * width range: (1 ~ 10)
     */
    public void setCalloutLineWidth(int width)
    {
    	if (width < 1 || width > 10)
    	{
    		return;
    	}
    	if (mainControl != null)
    	{
    		mainControl.getSysKit().getCalloutManager().setWidth(width);
    	}
    }
    
    /**
     * get paint color of callout
     * 
     */
    public int getCalloutColor()
    {
    	if (mainControl == null)
    	{
    		return Color.RED;
    	}
    	return mainControl.getSysKit().getCalloutManager().getColor();
    }
    
    /**
     * set paint color of callout
     * @param alpha
     * @param red
     * @param green
     * @param blue
     */
    public void setCalloutColor(byte alpha, byte red, byte green, byte blue)
    {
    	if (mainControl != null)
    	{
    		int color = (alpha << 24) | (red << 16 & 0xFF0000) | (green << 8 & 0xFF00) | (blue & 0xFF);
    		mainControl.getSysKit().getCalloutManager().setColor(color);
    	}
    }
    
    /**
     * get drawing mode of callout
     * @see #DRAWMODE_NORMAL
     * @see #DRAWMODE_CALLOUTDRAW
     * @see #DRAWMODE_CALLOUTERASE
     */
    public int getDrawingMode()
    {
    	if (mainControl == null)
    	{
    		return MainConstant.DRAWMODE_NORMAL;
    	}
    	
    	return mainControl.getSysKit().getCalloutManager().getDrawingMode();
    }
    
    /**
     * set drawing mode of callout
     * @see #DRAWMODE_NORMAL
     * @see #DRAWMODE_CALLOUTDRAW
     * @see #DRAWMODE_CALLOUTERASE
     */
    public void setDrawingMode(int mode)
    {
    	if (mainControl == null || mode < DRAWMODE_NORMAL || mode > DRAWMODE_CALLOUTERASE)
    	{
    		return;
    	}
		mainControl.getSysKit().getCalloutManager().setDrawingMode(mode);

    	if (mode == DRAWMODE_CALLOUTDRAW)
    	{
    		parent.post(new Runnable() {
				
				@Override
				public void run() {
					mainControl.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
				}
			});
    	}
    }
    
    /**
     * all vector graphs contained in viewIndex view have been converted or not
     * @param viewIndex
     * @return
     */
    public boolean hasConvertingVectorgraph(int viewIndex)
    {
    	return mainControl.getSysKit().getPictureManage().hasConvertingVectorgraph(viewIndex);
    }
    
    /**
     * set flag whether fitzoom can be larger than 100%, but be smaller than the max zoom
     * @param ignoreOriginalSize
     */
    public void setIgnoreOriginalSize(boolean ignoreOriginalSize)
    {
    	if (frame != null)
        {
    		frame.setIgnoreOriginalSize(ignoreOriginalSize);
        }
    }
    
    /**
     * 
     * @return
     * true fitzoom may be larger than 100%, but be smaller than the max zoom
     * false fitzoom can not larger than 100%
     */
    public boolean isIgnoreOriginalSize()
    {
    	if(frame != null)
    	{
    		return frame.isIgnoreOriginalSize();
    	}
    	return false;
    }
    
    /**
     * must be called before openFile function, and just for ppt, pdf and word print mode
     * @see #MOVING_HORIZONTAL
     * @see #MOVING_VERTICAL
     * @param position MOVING_HORIZONTAL and MOVING_VERTICAL
     */
    public void setPageListViewMovingPosition(byte position)
    {
    	if(frame != null)
    	{
    		frame.setPageListViewMovingPosition(position);
    	}
    }
    
    /**
     * just for ppt, pdf and word print mode
     * @see #MOVING_HORIZONTAL
     * @see #MOVING_VERTICAL
     * @return MOVING_HORIZONTAL and MOVING_VERTICAL
     */
    public byte getPageListViewMovingPosition()
    {
    	if(frame != null)
    	{
    		return frame.getPageListViewMovingPosition();
    	}
    	
    	return MOVING_HORIZONTAL;
    }
    
    
    //
    private byte applicationType = -1;
    //
    private ViewGroup parent;
    //
    private MacroFrame frame;
    //
    private MainControl mainControl;
    //toast
    protected Toast toast;
    
}
