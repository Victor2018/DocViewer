/*
 * 文件名称:          PrintWord.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:53:24
 */
package com.nvqquy98.lib.doc.office.wp.control;

import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.macro.TouchEventListener;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.SysKit;
import com.nvqquy98.lib.doc.office.system.beans.pagelist.APageListItem;
import com.nvqquy98.lib.doc.office.system.beans.pagelist.APageListView;
import com.nvqquy98.lib.doc.office.system.beans.pagelist.IPageListViewListener;
import com.nvqquy98.lib.doc.office.wp.view.PageRoot;
import com.nvqquy98.lib.doc.office.wp.view.PageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * print mode component
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2013-1-8
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PrintWord extends FrameLayout implements IPageListViewListener
{
    /**
     * 
     * @param context
     */
    public PrintWord(Context context)
    {
        super(context);
    }

    /**
     * 
     *
     */
    public PrintWord(Context context, IControl control, PageRoot pageRoot)
    {
        super(context);
        this.control = control;
        this.pageRoot = pageRoot;

        listView = new APageListView(context, this);
        addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(36);
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
     */
    public void setVisibility(int visibility)
    {
        super.setVisibility(visibility);
        if (visibility == VISIBLE);
        {
            exportImage(listView.getCurrentPageView(), null);
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
     * @param x 为100%的值
     * @param y 为100%的值
     */
    public long viewToModel(int x, int y, boolean isBack)
    {
        int pageIndex = listView.getCurrentPageNumber() - 1;
        if (pageIndex < 0 || pageIndex >= getPageCount())
        {
            return 0;
        }
        return pageRoot.viewToModel(x, y, isBack);
    }

    /**
     * 
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        int pageIndex = listView.getCurrentPageNumber() - 1;
        if (pageIndex < 0 || pageIndex >= getPageCount())
        {
            return rect;
        }
        return pageRoot.modelToView(offset, rect, isBack);
    }
    
    /**
     * 
     */
    public int getPageCount()
    {
        return Math.max(pageRoot.getChildCount(), 1);
    }
    
    /**
     * 
     */
    public APageListItem getPageListItem(int position, View convertView, ViewGroup parent)
    {   
        Rect rect = getPageSize(position);
        return new WPPageListItem(listView, control, rect.width(), rect.height());
    }
    
    /**
     * 
     */
    public Rect getPageSize(int pageIndex)
    {
        PageView view = pageRoot.getPageView(pageIndex);
        if (view != null)
        {
            pageSize.set(0, 0, view.getWidth(), view.getHeight());
        }
        else
        {
            IAttributeSet attr = pageRoot.getDocument().getSection(0).getAttribute();
            int pageWidth = (int)(AttrManage.instance().getPageWidth(attr) * MainConstant.TWIPS_TO_PIXEL);
            int pageHeight = (int)(AttrManage.instance().getPageHeight(attr) * MainConstant.TWIPS_TO_PIXEL);
            pageSize.set(0, 0, pageWidth, pageHeight);
        }
        return pageSize;
    }

    /**
     * 
     *
     */
    public void exportImage(final APageListItem pageItem, final Bitmap srcBitmap)
    {
        if (getControl() == null || !(getParent() instanceof Word))
        {
            return;
        }
        
        WPFind find = (WPFind)control.getFind();
        if (find.isSetPointToVisible())
        {
            find.setSetPointToVisible(false);
            PageView pv = pageRoot.getPageView(pageItem.getPageIndex());
            if (pv == null)
            {
                return;
            }
            Rectangle rect = modelToView(((Word)getParent()).getHighlight().getSelectStart(), new Rectangle(), false);
            rect.x -= pv.getX();
            rect.y -= pv.getY();
            if (!listView.isPointVisibleOnScreen(rect.x, rect.y))
            {
                listView.setItemPointVisibleOnScreen(rect.x, rect.y);
                return;
            }
        
        }
        post(new Runnable()
        {            
            @ Override
            public void run()
            {
                try
                {               
                    IOfficeToPicture otp = getControl().getOfficeToPicture();
                    if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGE_END)
                    { 
                        int rW = Math.min(getWidth(), pageItem.getWidth());
                        int rH = Math.min(getHeight(), pageItem.getHeight());
                        Bitmap dstBitmap = otp.getBitmap(rW, rH);
                        if (dstBitmap == null)
                        {
                            return;
                        }
                        if (getParent() instanceof Word)
                        {
                            ((Word)getParent()).getHighlight().setPaintHighlight(false);
                        }
                        // don't zoom
                        if (dstBitmap.getWidth() == rW && dstBitmap.getHeight() == rH)
                        {
                            Canvas canvas = new Canvas(dstBitmap);
                            canvas.drawColor(Color.WHITE);
                            float zoom = listView.getZoom();
                            PageView pv = pageRoot.getPageView(pageItem.getPageIndex());
                            if (pv !=  null)
                            {
                            	canvas.save();
                                canvas.translate(-pv.getX() * zoom, -pv.getY() * zoom);
                                int left = (int)(pageItem.getLeft());
                                int top = (int)(pageItem.getTop());
                                pv.drawForPrintMode(canvas, -(Math.max(left, 0) - left), -(Math.max(top, 0) - top), zoom);
                                canvas.restore();
                                canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                                control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), zoom);
                            }
                        }
                        // zoom
                        else
                        {
                            PageView pv = pageRoot.getPageView(pageItem.getPageIndex());
                            if (pv !=  null)
                            {
                                float paintZoom = Math.min(dstBitmap.getWidth() / (float)rW, dstBitmap.getHeight() / (float)rH);
                                float zoom = listView.getZoom() * paintZoom;
                                int left = (int)(pageItem.getLeft() * paintZoom);
                                int top = (int)(pageItem.getTop() * paintZoom);
                                Canvas canvas = new Canvas(dstBitmap);
                                canvas.save();
                                canvas.drawColor(Color.WHITE);
                                canvas.translate(-pv.getX() * zoom, -pv.getY() * zoom);
                                pv.drawForPrintMode(canvas, -(Math.max(left, 0) - left), -(Math.max(top, 0) - top), zoom);
                                canvas.restore();
                                canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                                control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), zoom);
                            }
                        }
                        if (getParent() instanceof Word)
                        {
                            ((Word)getParent()).getHighlight().setPaintHighlight(true);
                        }
                        otp.callBack(dstBitmap);
                    }
                }
                catch (Exception e)
                {
                }
            }
        });
    
    }

    /**
     * 
     * @param dstBitmap
     * @return
     */
    public Bitmap getSnapshot(Bitmap dstBitmap)
    {
    	APageListItem pageItem = getListView().getCurrentPageView();
    	if(pageItem == null)
    	{
    		return null;
    	}
    	int rW = Math.min(getWidth(), pageItem.getWidth());
        int rH = Math.min(getHeight(), pageItem.getHeight());
        
        if (getParent() instanceof Word)
        {
            ((Word)getParent()).getHighlight().setPaintHighlight(false);
        }
        // don't zoom
        if (dstBitmap.getWidth() == rW && dstBitmap.getHeight() == rH)
        {
            Canvas canvas = new Canvas(dstBitmap);
            canvas.drawColor(Color.WHITE);
            float zoom = listView.getZoom();
            PageView pv = pageRoot.getPageView(pageItem.getPageIndex());
            if (pv !=  null)
            {
                canvas.translate(-pv.getX() * zoom, -pv.getY() * zoom);
                int left = (int)(pageItem.getLeft());
                int top = (int)(pageItem.getTop());
                pv.drawForPrintMode(canvas, -(Math.max(left, 0) - left), -(Math.max(top, 0) - top), zoom);
            }
        }
        // zoom
        else
        {
            PageView pv = pageRoot.getPageView(pageItem.getPageIndex());
            if (pv !=  null)
            {
                float paintZoom = Math.min(dstBitmap.getWidth() / (float)rW, dstBitmap.getHeight() / (float)rH);
                float zoom = listView.getZoom() * paintZoom;
                int left = (int)(pageItem.getLeft() * paintZoom);
                int top = (int)(pageItem.getTop() * paintZoom);
                Canvas canvas = new Canvas(dstBitmap);
                canvas.drawColor(Color.WHITE);
                canvas.translate(-pv.getX() * zoom, -pv.getY() * zoom);
                pv.drawForPrintMode(canvas, -(Math.max(left, 0) - left), -(Math.max(top, 0) - top), zoom);
            }
        }
        if (getParent() instanceof Word)
        {
            ((Word)getParent()).getHighlight().setPaintHighlight(true);
        }
        
        return dstBitmap;
    }
    
    
    /**
     * 
     *
     */
    public boolean isInit()
    {
        return true;
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
    public int getPageListViewMovingPosition()
    {
    	return control.getMainFrame().getPageListViewMovingPosition();
    }
    
    /**
     * 
     */
    public Object getModel()
    {
        return pageRoot;
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
     *              @see TouchEventListener#EVENT_CLICK
     *              @see TouchEventListener#EVENT_DOUBLE_TAP
     *              @see TouchEventListener#EVENT_DOUBLE_TAP_EVENT
     *              @see TouchEventListener#EVENT_DOWN
     *              @see TouchEventListener#EVENT_FLING
     *              @see TouchEventListener#EVENT_LONG_PRESS
     *              @see TouchEventListener#EVENT_SCROLL
     *              @see TouchEventListener#EVENT_SHOW_PRESS
     *              @see TouchEventListener#EVENT_SINGLE_TAP_CONFIRMED
     *              @see TouchEventListener#EVENT_SINGLE_TAP_UP
     *              @see TouchEventListener#EVENT_TOUCH
     */
    public boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, byte eventMethodType)
    {
        if (eventMethodType == ON_SINGLE_TAP_UP && e1 != null
            && e1.getAction() == MotionEvent.ACTION_UP)
        {
            APageListItem item = listView.getCurrentPageView();
            if (item != null)
            {
                PageView pv = pageRoot.getPageView(item.getPageIndex());
                if (pv != null)
                {
                    float zoom = listView.getZoom();
                    int x = (int)((e1.getX() - item.getLeft()) / zoom) + pv.getX();
                    int y = (int)((e1.getY() - item.getTop()) / zoom) + pv.getY();
                    long offset = pv.viewToModel(x, y, false);
                    if (offset >= 0)
                    {
                        IElement leaf = pv.getDocument().getLeaf(offset);
                        if (leaf != null)
                        {
                            int hyID = AttrManage.instance().getHperlinkID(leaf.getAttribute());
                            if (hyID >= 0)
                            {
                                Hyperlink hylink = control.getSysKit().getHyperlinkManage().getHyperlink(hyID);
                                if (hylink != null)
                                {
                                    control.actionEvent(EventConstant.APP_HYPERLINK, hylink);
                                }
                            }
                        }
                    }
                }
            }
        }
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
        if (getParent() instanceof Word)
        {
            Word word = (Word)getParent();
            if (word.getFind().getPageIndex() != pageItem.getPageIndex())
            {
                word.getHighlight().removeHighlight();
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
     * @param isDrawPictrue The isDrawPictrue to set.
     */
    public void setDrawPictrue(boolean isDrawPictrue)
    {
        PictureKit.instance().setDrawPictrue(isDrawPictrue);
    }
    
    /**
     * 
     * @return
     */
    public PageView getCurrentPageView()
    {
        APageListItem item =  listView.getCurrentPageView();
        if (item != null)
        {
            return pageRoot.getPageView(item.getPageIndex());
        }
        return null;
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
            String pn = String.valueOf((listView.getCurrentPageNumber()) + " / " + pageRoot.getChildCount());
            int w =  (int)paint.measureText(pn);
            int h =  (int)(paint.descent() - paint.ascent());
            int x = (int)((getWidth() - w) / 2);
            int y = (int)((getHeight() - h) - 50);
            
            Drawable drawable = SysKit.getPageNubmerDrawable();
            drawable.setBounds((int)(x - 20), y - 10, x + w + 20, y + h + 10);
            drawable.draw(canvas);
            
            y -= paint.ascent();
            canvas.drawText(pn, x, y, paint);
        }
        
        if (preShowPageIndex != listView.getCurrentPageNumber()
            || prePageCount != getPageCount())
        {
            changePage();
            preShowPageIndex = listView.getCurrentPageNumber();
            prePageCount = getPageCount();
        }
    }
    
    /**
     *
     */
    public void changePage()
    {
        control.getMainFrame().changePage();
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
     * 
     */
    public void dispose()
    {
        control = null;
        if (listView != null)
        {
            listView.dispose();
        }
        pageRoot = null;
        pageSize = null;
    }
    
    private int preShowPageIndex = -1;
    //
    private int prePageCount = -1;
    //
    private IControl control;
    //
    private APageListView listView;
    // 绘制器
    private Paint paint;
    //
    private PageRoot pageRoot;
    //
    private Rect pageSize = new Rect();

}
