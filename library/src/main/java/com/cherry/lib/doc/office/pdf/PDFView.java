/*
 * 文件名称:          PDFView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:26:45
 */
package com.cherry.lib.doc.office.pdf;

import com.cherry.lib.doc.office.common.IOfficeToPicture;
import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.office.fc.pdf.PDFLib;
import com.cherry.lib.doc.office.system.IControl;
import com.cherry.lib.doc.office.system.IFind;
import com.cherry.lib.doc.office.system.IMainFrame;
import com.cherry.lib.doc.office.system.SysKit;
import com.cherry.lib.doc.office.system.beans.pagelist.APageListItem;
import com.cherry.lib.doc.office.system.beans.pagelist.APageListView;
import com.cherry.lib.doc.office.system.beans.pagelist.IPageListViewListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * PDF document rending
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
public class PDFView extends FrameLayout implements IPageListViewListener
{
    /**
     * 
     * @param context
     */
    public PDFView(Context context)
    {
        super(context);
    }

    /**
     * 
     *
     */
    public PDFView(Context context, PDFLib pdfLib, IControl control)
    {
        super(context);
        this.control = control;
        this.pdfLib = pdfLib;
        
        listView = new APageListView(context, this);
        addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        find = new PDFFind(this);
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(24);
        
        if (!pdfLib.hasPasswordSync())
        {
            pagesSize = pdfLib.getAllPagesSize();
        }
    } 
    
    /**
     * 
     */
    public void setBackgroundColor(int color) 
    {
        super.setBackgroundColor(color);
        if (listView != null)
        {
            listView.setBackgroundColor(color);
        }
    }
    
    /**
     * 
     *
     */
    public void setBackgroundResource(int resid)
    {
        super.setBackgroundResource(resid);
        if (listView != null)
        {
            listView.setBackgroundResource(resid);
        }
    }
    
    /**
     * 
     *
     */
    public void setBackgroundDrawable(Drawable d) 
    {
       super.setBackgroundDrawable(d);
       if (listView != null)
       {
           listView.setBackgroundDrawable(d);
       }
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
     *
     */
    protected void dispatchDraw(Canvas canvas) 
    {
        super.dispatchDraw(canvas);
        drawPageNubmer(canvas);
    }
    
    /**
     * 
     *
     */
    public void init()
    {
        if (pdfLib.hasPasswordSync())
        {
            new PasswordDialog(control, pdfLib).show();
        }
    }

    /**
     * 
     *
     */
    public void setZoom(float zoom, int pointX, int pointY)
    {
        listView.setZoom(zoom, pointX, pointY);
    }
    
    /**
     * set fit size for PPT，Word view mode, PDf
     * 
     * @param  value  fit size mode
     *          = 0, fit size of get minimum value of pageWidth / viewWidth and pageHeight / viewHeight;
     *          = 1, fit size of pageWidth
     *          = 2, fit size of PageHeight
     */
    public void setFitSize(int value)
    {
        listView.setFitSize(value);
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
        return listView.getFitSizeState();
    }
    
    /**
     * 
     */
    public float getZoom()
    {
        return listView.getZoom();
    }
    
    /**
     * 
     */
    public float getFitZoom()
    {
        return listView.getFitZoom();
    }
    
    /**
     * get current display page number (base 1)
     * 
     * @return page number (base 1)
     */
    public int getCurrentPageNumber()
    {
        return listView.getCurrentPageNumber();
    }
    
    /**
     * 
     */
    public IFind getFind()
    {
        return this.find;
    }
    
    /**
     * 
     */
    public PDFLib getPDFLib()
    {
        return this.pdfLib;
    }
    
    /**
     * 
     */
    public APageListView getListView()
    {
        return this.listView;
    }
    
    /**
     * 
     */
    public void nextPageView()
    {
        listView.nextPageView();
    }

    /**
     * 
     */
    public void previousPageview()
    {
        listView.previousPageview();
    }
    
    /**
     * switch page for page index (base 0)
     * 
     * @param index
     */
    public void showPDFPageForIndex(int index)
    {
        listView.showPDFPageForIndex(index);
    }
    
    /**
     * page to image for page number (base 1)
     * 
     * @return bitmap raw data
     */
    public Bitmap pageToImage(int pageNumber)
    {
        if (pageNumber <= 0 || pageNumber > getPageCount())
        {
            return  null;
        }
        Rect rect = getPageSize(pageNumber - 1);
        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(),  Config.ARGB_8888);
        
        pdfLib.drawPageSync(bitmap, pageNumber - 1, rect.width(), rect.height(),
            0, 0, rect.width(), rect.height(), 1);
        
        return bitmap;
    }
    
    /**
     * page to image for page number (base 1)
     * 
     * @return bitmap raw data
     */
    public Bitmap getThumbnail(int pageNumber, float zoom)
    {
        if (pageNumber <= 0 || pageNumber > getPageCount())
        {
            return  null;
        }
        Rect rect = getPageSize(pageNumber -1);
        int w = (int)(rect.width() * zoom);
        int h = (int)(rect.height() * zoom);
        Bitmap bitmap = null;
        try
        {
            bitmap = Bitmap.createBitmap(w, h,  Config.ARGB_8888);
            pdfLib.drawPageSync(bitmap, pageNumber - 1, w, h,
                0, 0, w, h, 1);
        }
        catch(OutOfMemoryError e)
        {
            control.getSysKit().getErrorKit().writerLog(e);
        }
        return bitmap;
    }
    
    /**
     * specific area of page to image. if area is not completely contained in the page, return null
     * @param pageNumber page number
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width area width
     * @param height area height
     * @return
     */
    public Bitmap pageAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight)
    {
        if (pageNumber <= 0 || pageNumber > getPageCount())
        {
            return  null;
        }
        //Rectangle rect = pdfLib.getPageSizeSync(pageNumber - 1);
        Rect rect = getPageSize(pageNumber -1);
        if (!SysKit.isValidateRect(rect.width(), rect.height(), srcLeft, srcTop, srcWidth, srcHeight))
        {
            return  null;
        }
        float paintZoom = Math.min(desWidth / (float)srcWidth, desHeight / (float)srcHeight);        
        
        Bitmap bitmap = null;
        try
        {
            bitmap = Bitmap.createBitmap((int)(srcWidth * paintZoom), (int)(srcHeight * paintZoom),  Config.ARGB_8888);
        }
        catch(OutOfMemoryError e)
        {                
            return null;
        }
        if (bitmap == null)
        {
            return null;
        }
        pdfLib.drawPageSync(bitmap, pageNumber - 1, (int)(rect.width() * paintZoom), (int)(rect.height() * paintZoom),
            (int)(srcLeft * paintZoom), (int)(srcTop * paintZoom), (int)(srcWidth * paintZoom), (int)(srcHeight * paintZoom), 1);
        
        return bitmap;
    }
    
    /**
     * 
     */
    public void passwordVerified()
    {
        if(listView != null)
        {
            pagesSize = pdfLib.getAllPagesSize();
            control.getMainFrame().openFileFinish();
            listView.init();

        }
    }
    
    /**
     * 
     */
    public int getPageCount()
    {
        return pdfLib.getPageCountSync();
    }
    
    /**
     * 
     */
    public APageListItem getPageListItem(int position, View convertView, ViewGroup parent)
    {   
        Rect rect = getPageSize(position);
        return new PDFPageListItem(listView, control, rect.width(), rect.height());
    }
    
    /**
     * 
     */
    public Rect getPageSize(int pageIndex)
    {
        if (pageIndex < 0 || pageIndex >= pagesSize.length)
        {
            return null;
        }
        return pagesSize[pageIndex];
    }

    /**
     * 
     *
     */
    public void exportImage(final APageListItem pageItem, final Bitmap srcBitmap)
    {
        if (getControl() == null || srcBitmap == null)
        {
            return;
        }
        if (find.isSetPointToVisible())
        {
            find.setSetPointToVisible(false);
            RectF[] rectF = find.getSearchResult();
            if (rectF != null && rectF.length > 0)
            {
                if (!listView.isPointVisibleOnScreen((int)rectF[0].left, (int)rectF[0].top))
                {
                    listView.setItemPointVisibleOnScreen((int)rectF[0].left, (int)rectF[0].top);
                    return;
                }
            }
        }
        if (exportTask != null)
        {
            exportTask.cancel(true);
            exportTask = null;
        }
        exportTask = new AsyncTask<Void, Object, Bitmap>()
        {
            private boolean isCancal;
            /**
             *
             */
            protected Bitmap doInBackground(Void...v)
            {
                if (control == null || pdfLib == null)
                {
                    return null;
                }
                try
                {
                    IOfficeToPicture otp = control.getOfficeToPicture();
                    if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGE_END)
                    { 
                        int rW = Math.min(getWidth(), srcBitmap.getWidth());
                        int rH = Math.min(getHeight(), srcBitmap.getHeight());
                        Bitmap dstBitmap = otp.getBitmap(rW, rH);
                        if (dstBitmap == null)
                        {
                            return null;
                        }
                        Canvas canvas = new Canvas(dstBitmap);
                        // don't zoom
                        int dx = 0;
                        int dy = 0;
                        int left = pageItem.getLeft();
                        int top = pageItem.getTop();
                        if (dstBitmap.getWidth() == rW && dstBitmap.getHeight() == rH)
                        {
                            if (srcBitmap.getWidth() != rW || srcBitmap.getHeight() != rH)
                            {   
                                dx = Math.min(0, pageItem.getLeft());
                                dy =  Math.min(0, pageItem.getTop());
                            }
                            canvas.drawBitmap(srcBitmap, dx, dy, paint);
                            canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                            control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), getZoom());
                        }
                        // zoom
                        else
                        {
                            Matrix matrix = new Matrix();
                            float xZoom = dstBitmap.getWidth() / (float)rW;
                            float yZoom = dstBitmap.getHeight() / (float)rH;
                            matrix.postScale(xZoom, yZoom);
                            if ((int)(getZoom() * 1000000) == 1000000)
                            {
                                matrix.postTranslate(Math.min(pageItem.getLeft(), 0), Math.min(pageItem.getTop(), 0));  
                                dx = Math.min(0, (int)(pageItem.getLeft() * xZoom));
                                dy = Math.min(0, (int)(pageItem.getTop() * yZoom));
                            }
                            try
                            {
                                Bitmap scaleBitmp = Bitmap.createBitmap(srcBitmap, 0, 0, 
                                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
                                canvas.drawBitmap(scaleBitmp, dx, dy, paint);
                            }
                            catch (OutOfMemoryError e)
                            {
                                canvas.drawBitmap(srcBitmap, matrix, paint);
                            }
                            canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                            control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), getZoom());
                            /*float paintZoom = Math.min(dstBitmap.getWidth() / (float)rW, dstBitmap.getHeight() / (float)rH);
                            int left = (int)(pageItem.getLeft() * paintZoom);
                            int top = (int)(pageItem.getTop() * paintZoom);
                            pdfLib.drawPageSync(dstBitmap, pageItem.getPageIndex(), 
                                (int)(pageItem.getPageWidth() * paintZoom * getZoom()),
                                (int)(pageItem.getPageHeight() * paintZoom * getZoom()),
                                Math.max(left, 0) - left, Math.max(top, 0) - top, 
                                dstBitmap.getWidth(), dstBitmap.getHeight()); */                  
                        }
                        return dstBitmap;
                    }
                }
                catch (Exception e)
                {
                    
                }
                return null;
            }
            
            /**
             * 
             *
             */
            protected void onPreExecute()
            {
                
            }
            
            protected void onCancelled()
            {
                isCancal = true;
            }

            /**
             * 
             *
             */
            protected void onPostExecute(Bitmap bitmap)
            {
                try
                {
                    if (bitmap != null)
                    {
                        if (control == null || isCancal)
                        {
                            return;
                        }
                        IOfficeToPicture otp = control.getOfficeToPicture();
                        if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGE_END)
                        {
                            otp.callBack(bitmap);
                        }
                    }
                }
                catch (Exception e)
                {
                    
                }
            }
            
        };
        //exportTask.execute(null);
    }
    
    /**
     * 
     */
    public Bitmap getSanpshot(Bitmap destBitmap)
    {

        if (destBitmap == null)
        {
            return null;
        }
        APageListItem pageItem = listView.getCurrentPageView();
        if (pageItem == null)
        {
            return null;
        }
        int rW = Math.min(getWidth(), pageItem.getWidth());
        int rH = Math.min(getHeight(), pageItem.getHeight());
        float xZoom = destBitmap.getWidth() / (float)rW;
        float yZoom = destBitmap.getHeight() / (float)rH;
        int left = (int)(pageItem.getLeft() * xZoom);
        int top = (int)(pageItem.getTop() * yZoom);
        int tX = Math.max(left, 0) - left;
        int tY = Math.max(top, 0) - top;
        float tW = (pageItem.getPageWidth() * xZoom * getZoom());
        float tH = (pageItem.getPageHeight() * yZoom * getZoom());
        
        pdfLib.drawPageSync(destBitmap, pageItem.getPageIndex(), 
            tW, tH, tX, tY, 
            destBitmap.getWidth(), destBitmap.getHeight(), 1);
        if (tX == 0 && tW < destBitmap.getWidth()
            && rW == pageItem.getWidth())
        {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            float span = destBitmap.getWidth() - tW;
            Canvas canvas = new Canvas(destBitmap);
            canvas.drawRect(destBitmap.getWidth() - span, 0, destBitmap.getWidth(), destBitmap.getHeight(), paint);
        }
        return destBitmap;
    }

    /**
     * 
     *
     */
    public boolean isInit()
    {
        return !pdfLib.hasPasswordSync();
    }
    
    /**
     * 
     * @return
     * true fitzoom may be larger than 100% but smaller than the max zoom
     * false fitzoom can not larger than 100%
     */
    public boolean isIgnoreOriginalSize()
    {
    	return control.getMainFrame().isIgnoreOriginalSize();
    }
    
    /**
     * page list view moving position
     * @param position horizontal or vertical
     */
    public byte getPageListViewMovingPosition()
    {
    	return control.getMainFrame().getPageListViewMovingPosition();
    }
    
    /**
     * 
     */
    public Object getModel()
    {
        return pdfLib;
    }
    
    /**
     * 
     */
    public IControl getControl()
    {
        return this.control;
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
        return control.getMainFrame().onEventMethod(v, e1, e2, velocityX, velocityY, eventMethodType);
    }

    /**
     * 
     *
     */
    public void updateStutus(Object obj)
    {
       control.actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, obj);
    }

    /**
     * 
     *
     */
    public void resetSearchResult(APageListItem pageItem)
    {
        if (find != null)
        {
            if (pageItem.getPageIndex() != find.getPageIndex())
            {
                find.resetSearchResult();
            }
        }
    }

    /**
     * 
     */
    public boolean isTouchZoom()
    {
        return control.getMainFrame().isTouchZoom();
    }

    /**
     * 
     */
    public boolean isShowZoomingMsg()
    {
        return control.getMainFrame().isShowZoomingMsg();
    }
    
    /**
     * 
     */
    public void changeZoom()
    {
        control.getMainFrame().changeZoom();
    }
    
    /**
     * 
     *
     */
    public void setDrawPictrue(boolean isDrawPictrue)
    {        

    }
    
    /**
     *  set change page flag, Only when effectively the PageSize greater than ViewSize.
     *  (for PPT, word print mode, PDF)
     *  
     *  @param b    = true, change page
     *              = false, don't change page
     */
    public boolean isChangePage()
    {
       return control.getMainFrame().isChangePage();
    }
    
    /**
     * 绘制页信息
     * @param canvas
     * @param zoom
     */
    private void drawPageNubmer(Canvas canvas)
    {
        if (control.getMainFrame().isDrawPageNumber())
        {
            String pn = String.valueOf((listView.getCurrentPageNumber()) + " / " + pdfLib.getPageCountSync());
            int w =  (int)paint.measureText(pn);
            int h =  (int)(paint.descent() - paint.ascent());
            int x = (int)((getWidth() - w) / 2);
            int y = (int)((getHeight() - h) - 20);
            
            Drawable drawable = SysKit.getPageNubmerDrawable(); 
            drawable.setBounds((int)(x - 10), y - 10, x + w + 10, y + h + 10);
            drawable.draw(canvas);
            
            y -= paint.ascent();
            canvas.drawText(pn, x, y, paint);
        }
        
        if (listView.isInit() && preShowPageIndex != listView.getCurrentPageNumber())
        {
            control.getMainFrame().changePage();
            preShowPageIndex = listView.getCurrentPageNumber();
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {    
        if (find != null)
        {
            find.dispose();
        }
        if (find != null)
        {
            find.dispose();
            find = null;
        }
        if (pdfLib != null)
        {
            pdfLib.setStopFlagSync(1);
            pdfLib = null;
        }
        if (listView != null)
        {
            listView.dispose();
        }
        control = null;
    }
    
    private int preShowPageIndex = -1;
    //
    private IControl control;
    //
    private PDFFind find;
    //
    private PDFLib pdfLib;
    //
    private APageListView listView;
    //
    private Rect[] pagesSize;
    // 绘制器
    private Paint paint;
    //
    private AsyncTask<Void, Object, Bitmap> exportTask;
}
