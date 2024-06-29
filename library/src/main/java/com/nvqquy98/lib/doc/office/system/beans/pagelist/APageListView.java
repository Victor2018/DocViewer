/*
 * 文件名称:          PDFPageAdapterView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:49:11
 */
package com.nvqquy98.lib.doc.office.system.beans.pagelist;

import java.util.LinkedList;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.system.IMainFrame;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.AdapterView;

import timber.log.Timber;

public class APageListView extends AdapterView<Adapter>
{

    private static final int GAP = 20;
    /**
     * 
     * @param context
     */
    public APageListView(Context context)
    {
        super(context);
    }

    /**
     * 
     *
     */
    public APageListView(Context context, IPageListViewListener listener)
    {
        super(context);
        this.pageListViewListener = listener;
        eventManage = new APageListEventManage(this);
        pageAdapter = new APageListAdapter(this);
        setLongClickable(true); 
        this.post(new Runnable()
        {
            @ Override
            public void run()
            {
                // get thumbnail, pageListViewListener = null;
                if (pageListViewListener != null
                    && pageListViewListener.isInit())
                {
                    init();
                }
            }
        });
    }
    
    /**
     *
     */
    public void init()
    {
        isInit = true;
        requestLayout();
    }
    
    /**
     *
     *
     */
    public void requestLayout()
    {
        if (isDoRequestLayout)
        {
            super.requestLayout();
        }
    }
    
    /**
     * 
     *
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int n = getChildCount();
        for (int i = 0; i < n; i++)
        {
            View view = getChildAt(i);
            if (view instanceof APageListItem)
            {   
                APageListItem pv = (APageListItem)view;
                pv.measure(View.MeasureSpec.EXACTLY | (int)(pv.getPageWidth() * zoom),
                    View.MeasureSpec.EXACTLY | (int)(pv.getPageHeight() * zoom));
            }
        }
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w Current width of this view.
     * @param h Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) 
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        if (isConfigurationChanged)
        {                    
        	float fitZoom = getFitZoom();
            if(zoom < fitZoom)
            {
            	setZoom(fitZoom, false);
            	isInit = false;
            	postDelayed(new Runnable()
                {
                    /**
                     * 
                     */ 
                    public void run()
                    {
                    	isInit = true;
                    	isResetLayout = true;
                        requestLayout();
                    }
                }, 1);
            	
            	pageListViewListener.changeZoom();
            }            
        }
    }
    
    /**
     * 
     */
    public boolean onTouchEvent(MotionEvent event)
    {
    	APageListItem pageView = getCurrentPageView();
    	if (pageView != null)
    	{
    		if (pageView.getControl().getSysKit().getCalloutManager().getDrawingMode() != MainConstant.DRAWMODE_NORMAL)
    		{
    			return false;
    		}
    		
    	}
        eventManage.processOnTouch(event);
        pageListViewListener.onEventMethod(this, event, null, -1, -1, IMainFrame.ON_TOUCH);
        return true;
    }
    
    /**
     * 
     * @see android.widget.AdapterView#onLayout(boolean, int, int, int, int)
     *
     */
    @ Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if (!isInit)
        {
            return;
        }          
        
        if(pageListViewListener.getPageListViewMovingPosition() == IPageListViewListener.Moving_Horizontal)
        {
        	layout_Horizontal();
        }
        else
        {
        	layout_Vertical();
        }
        
        invalidate();
        
        if (isConfigurationChanged)
        {
            isConfigurationChanged = false;
            APageListItem pageView = getCurrentPageView();
            if (pageView != null)
            {
                postRepaint(pageView);
            }
        }
    }
    
    private void layout_Horizontal()
    {
    	APageListItem currentView = childViewsCache.get(currentIndex);        
        Point cvOffset;
        if (!isResetLayout)
        {
            // Move to next or previous if current is sufficiently off center
            if (currentView != null && Math.abs(currentView.getLeft()) < currentView.getWidth())
            {
                cvOffset = getScreenSizeOffset(currentView);
                // next page
                if (currentView.getLeft() + currentView.getMeasuredWidth() 
                    + cvOffset.x + GAP / 2 + eventManage.getScrollX() < getWidth() / 2
                    && currentIndex + 1 < pageAdapter.getCount()
                    && !eventManage.isOnFling())
                {
                    postUnRepaint(currentView);
                    post(eventManage);
                    currentIndex++;
                    //resetupChildren();
                }
                // previous page
                else if (currentView.getLeft() - cvOffset.x - GAP / 2 + eventManage.getScrollX() >= getWidth() / 2
                    && currentIndex > 0
                    && !eventManage.isOnFling())
                {
                    postUnRepaint(currentView);
                    post(eventManage);
                    currentIndex--;
                    //resetupChildren();
                }
            }

            // Remove not needed children and hold them for reuse
            int numChildren = childViewsCache.size();
            int pIndexs[] = new int[numChildren];
            for (int i = 0; i < numChildren; i++)
            {
                pIndexs[i] = childViewsCache.keyAt(i);
            }
            for (int i = 0; i < numChildren; i++)
            {
                if ( pIndexs[i] < currentIndex - 1 ||  pIndexs[i] > currentIndex + 1)
                {
                    APageListItem pv = childViewsCache.get( pIndexs[i]);                    
                    pv.releaseResources();
                    pageViewCache.add(pv);
                    removeViewInLayout(pv);
                    childViewsCache.remove(pIndexs[i]);
                }
            }
        }
        else
        {
            isResetLayout = false;
            boolean isRepaint = false;
            eventManage.setScrollAxisValue(0, 0);
            // Remove not needed children and hold them for reuse
            int numChildren = childViewsCache.size();
            int pIndexs[] = new int[numChildren];
            for (int i = 0; i < numChildren; i++)
            {
                pIndexs[i] = childViewsCache.keyAt(i);
            }
            for (int i = 0; i < numChildren; i++)
            {
                if ( pIndexs[i] < currentIndex - 1 ||  pIndexs[i] > currentIndex + 1)
                {
                    APageListItem pv = childViewsCache.get( pIndexs[i]);                    
                    pv.releaseResources();
                    pageViewCache.add(pv);
                    removeViewInLayout(pv);
                    childViewsCache.remove(pIndexs[i]);
                    isRepaint = pIndexs[i] == currentIndex;
                }
            }
            if ((int)(zoom * 100) != 100 || !isRepaint)
            {
                post(eventManage);
            }
        }

        // Ensure current view is present
        int cvLeft, cvRight, cvTop, cvBottom;
        boolean notPresent = currentView == null;
        currentView = createPageView(currentIndex);
        // When the view is sub-screen-size in either dimension we
        // offset it to center within the screen area, and to keep
        // the views spaced out
        cvOffset = getScreenSizeOffset(currentView);
        if (notPresent)
        {
            //Main item not already present. Just place it top left
            cvLeft = cvOffset.x;
            cvTop = cvOffset.y;
        }
        else
        {
            // Main item already present. Adjust by scroll offsets
            cvLeft = currentView.getLeft() + eventManage.getScrollX();
            cvTop = currentView.getTop() + eventManage.getScrollY();
        }
        // Scroll values have been accounted for
        eventManage.setScrollAxisValue(0, 0);
        cvRight = cvLeft + currentView.getMeasuredWidth();
        cvBottom = cvTop + currentView.getMeasuredHeight();

        if (!eventManage.isTouchEventIn() && eventManage.isScrollerFinished())
        {
            Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
            cvRight += corr.x;
            cvLeft += corr.x;
            cvTop += corr.y;
            cvBottom += corr.y;
        }
        else if (currentView.getMeasuredHeight() <= getHeight())
        {
            // When the current view is as small as the screen in height, clamp
            // it vertically
            Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
            cvTop += corr.y;
            cvBottom += corr.y;
        }

        currentView.layout(cvLeft, cvTop, cvRight, cvBottom);

        // previous page
        if (currentIndex > 0)
        {
            View preView = createPageView(currentIndex - 1);
            Point leftOffset = getScreenSizeOffset(preView);
            int gap = leftOffset.x + GAP + cvOffset.x;
            
            preView.layout(cvLeft - preView.getMeasuredWidth() - gap,
                (cvBottom + cvTop - preView.getMeasuredHeight()) / 2, cvLeft - gap,
                (cvBottom + cvTop + preView.getMeasuredHeight()) / 2);
        }
        // next page
        if (currentIndex + 1 < pageAdapter.getCount())
        {
            View nextView = createPageView(currentIndex + 1);
            Point rightOffset = getScreenSizeOffset(nextView);
            int gap = cvOffset.x + GAP + rightOffset.x;
            
            nextView.layout(cvRight + gap,
                (cvBottom + cvTop - nextView.getMeasuredHeight()) / 2,
                cvRight + nextView.getMeasuredWidth() + gap,
                (cvBottom + cvTop + nextView.getMeasuredHeight()) / 2);
        }
    }
    
    private void layout_Vertical()
    {
    	APageListItem currentView = childViewsCache.get(currentIndex);        
        Point cvOffset;
        if (!isResetLayout)
        {
            // Move to next or previous if current is sufficiently off center
            if (currentView != null /*&& Math.abs(currentView.getTop()) < currentView.getHeight()*/)
            {
                cvOffset = getScreenSizeOffset(currentView);
                // next page
                if (currentView.getTop() + currentView.getMeasuredHeight() 
                    + cvOffset.y + GAP / 2 + eventManage.getScrollY() < getHeight() / 2
                    && currentIndex + 1 < pageAdapter.getCount()
                    && !eventManage.isOnFling())
                {
                    postUnRepaint(currentView);
                    post(eventManage);
                    currentIndex++;
                    Timber.e("current ++ %s", String.valueOf(currentIndex));
                }
                // previous page
                else if (currentView.getTop() - cvOffset.y - GAP / 2 + eventManage.getScrollY() >= getHeight() / 2
                    && currentIndex > 0
                    && !eventManage.isOnFling())
                {
                    postUnRepaint(currentView);
                    post(eventManage);
                    currentIndex--;
                    Timber.e("current -- %s", String.valueOf(currentIndex));
                }
            }
            
            // Remove not needed children and hold them for reuse
            int numChildren = childViewsCache.size();
            int pIndexs[] = new int[numChildren];
            for (int i = 0; i < numChildren; i++)
            {
                pIndexs[i] = childViewsCache.keyAt(i);
            }
            for (int i = 0; i < numChildren; i++)
            {
                if ( pIndexs[i] < currentIndex - 1 ||  pIndexs[i] > currentIndex + 1)
                {
                    APageListItem pv = childViewsCache.get( pIndexs[i]);                    
                    pv.releaseResources();
                    pageViewCache.add(pv);
                    removeViewInLayout(pv);
                    childViewsCache.remove(pIndexs[i]);
                }
            }
        }
        else
        {
            isResetLayout = false;
            boolean isRepaint = false;
            eventManage.setScrollAxisValue(0, 0);
            // Remove not needed children and hold them for reuse
            int numChildren = childViewsCache.size();
            int pIndexs[] = new int[numChildren];
            for (int i = 0; i < numChildren; i++)
            {
                pIndexs[i] = childViewsCache.keyAt(i);
            }
            for (int i = 0; i < numChildren; i++)
            {
                if ( pIndexs[i] < currentIndex - 1 ||  pIndexs[i] > currentIndex + 1)
                {
                    APageListItem pv = childViewsCache.get( pIndexs[i]);                    
                    pv.releaseResources();
                    pageViewCache.add(pv);
                    removeViewInLayout(pv);
                    childViewsCache.remove(pIndexs[i]);
                    isRepaint = pIndexs[i] == currentIndex;
                }
            }
            
            if ((int)(zoom * 100) != 100 || !isRepaint)
            {
                post(eventManage);
            }
        }

        // Ensure current view is present
        int cvLeft, cvRight, cvTop, cvBottom;
        boolean notPresent = currentView == null;
        currentView = createPageView(currentIndex);
        // When the view is sub-screen-size in either dimension we
        // offset it to center within the screen area, and to keep
        // the views spaced out
        cvOffset = getScreenSizeOffset(currentView);
        if (notPresent)
        {
            //Main item not already present. Just place it top left
            cvLeft = cvOffset.x;
            cvTop = cvOffset.y;
        }
        else
        {
            // Main item already present. Adjust by scroll offsets
            cvLeft = currentView.getLeft() + eventManage.getScrollX();
            cvTop = currentView.getTop() + eventManage.getScrollY();
        }
        // Scroll values have been accounted for
        eventManage.setScrollAxisValue(0, 0);
        cvRight = cvLeft + currentView.getMeasuredWidth();
        cvBottom = cvTop + currentView.getMeasuredHeight();

        if (!eventManage.isTouchEventIn() && eventManage.isScrollerFinished())
        {
            Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
            cvRight += corr.x;
            cvLeft += corr.x;
            cvTop += corr.y;
            cvBottom += corr.y;
        }
        else if (currentView.getMeasuredWidth() <= getWidth())
        {
            // When the current view is as small as the screen in height, clamp
            // it vertically
            Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
            cvRight += corr.x;
            cvLeft += corr.x;
        }

        currentView.layout(cvLeft, cvTop, cvRight, cvBottom);

        // previous page
        if (currentIndex > 0)
        {
            View preView = createPageView(currentIndex - 1);
            Point leftOffset = getScreenSizeOffset(preView);
            int gap = leftOffset.y + GAP + cvOffset.y;
            preView.layout(cvLeft,            		
            		cvTop - gap - preView.getMeasuredHeight(), 
            		cvRight,
            		cvBottom - gap - preView.getMeasuredHeight());
        }
        // next page
        if (currentIndex + 1 < pageAdapter.getCount())
        {        	
            View nextView = createPageView(currentIndex + 1);
            Point rightOffset = getScreenSizeOffset(nextView);
            int gap = cvOffset.y + GAP + rightOffset.y;
            nextView.layout(cvLeft,
        		  cvTop + gap + nextView.getMeasuredHeight(),
        		  cvRight,
        		  cvBottom + gap + nextView.getMeasuredHeight());
        }
    }
    
    /**
     * 
     *
     */
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        isConfigurationChanged = true;
    }
    
    /**
     * switch page for page index (base 0)
     * 
     * @param index
     */
    public void showPDFPageForIndex(final int index)
    {
        if (index < 0 ||  index >= pageAdapter.getCount())
        {
            return;
        }
        currentIndex = index;
        postDelayed(new Runnable()
        {
            
            int gotoIndex = index;
            /**
             * 
             */ 
            public void run()
            {
                if (gotoIndex == currentIndex)
                {
                    isResetLayout = true;
                    requestLayout();
                }
            }
        }, 1);
        pageListViewListener.updateStutus(null);
    }
    
    /**
     * 
     */
    public void nextPageView()
    {
        if (currentIndex + 1 >= pageAdapter.getCount())
        {
            return;
        }        
        APageListItem pageView = childViewsCache.get(currentIndex + 1);
        if (pageView != null)
        {
            currentIndex++;
            eventManage.slideViewOntoScreen(pageView);
        }
        else
        {
        	postDelayed(new Runnable()
            {
                /**
                 * 
                 */ 
                public void run()
                {
                	isResetLayout = true;
                    requestLayout();
                }
            }, 1);
            pageListViewListener.updateStutus(null);
        }
    }

    /**
     * 
     */
    public void previousPageview()
    {
        if (currentIndex == 0)
        {
            return;
        }        
        APageListItem pageView = childViewsCache.get(currentIndex - 1);
        if (pageView != null)
        {
            currentIndex--;
            eventManage.slideViewOntoScreen(pageView);
        }
    }
    
    /**
     *
     */
    public void exportImage(final APageListItem view, final Bitmap srcBitmap)
    {
        if (view.getPageIndex() != currentIndex || eventManage.isTouchEventIn()
            || !eventManage.isScrollerFinished())
        {
            return;
        }
        pageListViewListener.exportImage(view, srcBitmap);
    }    
    
    /**
     * 
     * @param x     is don't zoom
     * @param y     is don't zoom
     * @return
     */
    public boolean isPointVisibleOnScreen(int x, int y)
    {
        x = (int)(x * zoom);
        y = (int)(y * zoom);
        APageListItem item = getCurrentPageView();
        if (item == null)
        {
            return false;
        }
        int left = Math.max(item.getLeft(), 0) - item.getLeft();
        int top = Math.max(item.getTop(), 0) - item.getTop();
        return left < left + getWidth() && top < top + getHeight() 
               && x >= left && x < left + getWidth() && y >= top && y < top + getHeight();
        
    }
    
    /**
     * 
     * @param x     is don't zoom
     * @param y     is don't zoom
     * @return
     */
    public void setItemPointVisibleOnScreen(int x, int y)
    {
        if (x < 0 && y < 0)
        {
            return ;
        }
        APageListItem item = getCurrentPageView();
        if (item != null && !isPointVisibleOnScreen(x, y))
        {
            x = (int)(x * zoom);
            y = (int)(y * zoom);
            int cvLeft = 0, cvRight = 0, cvTop = 0, cvBottom = 0;
            if (x > 0)
            {
                if (x + getWidth() > item.getMeasuredWidth())
                {
                    cvLeft = -(item.getMeasuredWidth() - getWidth());
                }
                else
                {
                    cvLeft = -x;    
                }
            }
            if (y > 0)
            {
                if (y + getHeight() > item.getMeasuredHeight())
                {
                    cvTop = -(item.getMeasuredHeight() - getHeight());
                }
                else
                {
                    cvTop = -y;    
                }    
            } 
            
            // current page 
            Point cvOffset = getScreenSizeOffset(item);
            cvRight = cvLeft + item.getMeasuredWidth();
            cvBottom = cvTop + item.getMeasuredHeight();
            if (item.getMeasuredHeight() <= getHeight())
            {
                Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
                cvTop += corr.y;
                cvBottom += corr.y;
            }
            item.layout(cvLeft, cvTop, cvRight, cvBottom);

            // previous page
            if (currentIndex > 0)
            {
                View preView = childViewsCache.get(currentIndex - 1);
                if (preView != null)
                {
                    Point leftOffset = getScreenSizeOffset(preView);
                    int gap = leftOffset.x + GAP + cvOffset.x;
                    
                    preView.layout(cvLeft - preView.getMeasuredWidth() - gap,
                        (cvBottom + cvTop - preView.getMeasuredHeight()) / 2, cvLeft - gap,
                        (cvBottom + cvTop + preView.getMeasuredHeight()) / 2);
                }
            }
            // next page
            if (currentIndex + 1 < pageAdapter.getCount())
            {
                View nextView = childViewsCache.get(currentIndex + 1);
                if (nextView != null)
                {
                    Point rightOffset = getScreenSizeOffset(nextView);
                    int gap = cvOffset.x + GAP + rightOffset.x;
                    
                    nextView.layout(cvRight + gap,
                        (cvBottom + cvTop - nextView.getMeasuredHeight()) / 2,
                        cvRight + nextView.getMeasuredWidth() + gap,
                        (cvBottom + cvTop + nextView.getMeasuredHeight()) / 2);
                }
            }
            postRepaint(item);
        }
    }
    
    /**
     * 
     */
    public Object getModel()
    {
        return pageListViewListener.getModel();
    }
    
    /**
     * 
     * @return
     */
    public int getDisplayedPageIndex()
    {
        return currentIndex;
    }
    
    /**
     * 
     *
     */
    public void setZoom(float zoom, int pointX, int pointY)
    {
        setZoom(zoom, pointX, pointY, true);
    }
    
    public void setZoom(float zoomValue, final boolean isRepaint)
    {
        setZoom(zoomValue, Integer.MIN_VALUE, Integer.MIN_VALUE, isRepaint);
    }
    
    /**
     * 
     *
     */
    public void setZoom(float zoomValue, int pointX, int pointY, final boolean isRepaint)
    {
        if ((int)(zoomValue * MainConstant.ZOOM_ROUND) == (int)(this.zoom * MainConstant.ZOOM_ROUND))
        {
            return;
        }        
        isInitZoom = true;
        if(pointX == Integer.MIN_VALUE && pointY == Integer.MIN_VALUE)
        {
            pointX = getWidth() / 2;
            pointY = getHeight() / 2;
        }
        float oldZoom = zoom;
        zoom = zoomValue;
        pageListViewListener.changeZoom();
        post(new Runnable()
        {   
            @ Override
            public void run()
            {  
                if (isRepaint)
                {
                    APageListItem pageView = getCurrentPageView();
                    if (pageView != null)
                    {
                        postRepaint(pageView);
                    }
                }
            }
        });
        if (isRepaint)
        {
            APageListItem v = getCurrentPageView();
            int left = 0;
            int top = 0;
            if (v != null)
            {
                left = v.getLeft();
                top = v.getTop();
            }
            float factor = zoom / oldZoom;
            // Work out the focus point relative to the view top left
            int viewFocusX = pointX - (left + eventManage.getScrollX());
            int viewFocusY = pointY - (top + eventManage.getScrollY());
            // Scroll to maintain the focus point
            eventManage.setScrollAxisValue((int)(viewFocusX - viewFocusX * factor), 
                (int)(viewFocusY - viewFocusY * factor));
            requestLayout();
            
        }
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
        setZoom(getFitZoom(value), true);
        postInvalidate();
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
        int state = 0;
        APageListItem item = getCurrentPageView();
        if (item != null)
        {
            int w = Math.abs(item.getWidth() - getWidth());
            int h = Math.abs(item.getHeight() - getHeight());
            // left/right and top/bottom alignment
            if (w < 2 && h < 2)
            {
                state = 3;
            }
            // left/right alignment
            else if (w < 2 &&  h >= 2)
            {
                state = 2;
            }
            // top/bottom alignment
            else if (w >= 2 &&  h < 2)
            {
                state = 1;
            }
        }
        return state;
    }
    
    /**
     * 
     */
    public float getZoom()
    {
        return zoom;
    }
    
    /**
     * 
     */
    public float getFitZoom()
    {
        return getFitZoom(0);
    }
    /**
     * 
     */
    public float getFitZoom(int value)
    {
        if (currentIndex < 0 || currentIndex >= pageListViewListener.getPageCount())
        {
            return 1.0f;
        }
        Rect rect = pageListViewListener.getPageSize(currentIndex);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        ViewParent v = getParent();
        while (viewWidth == 0 && v != null)
        {
            if (v == null || !(v instanceof View))
            {
                break;
            }
            viewWidth = ((View)v).getWidth();
            viewHeight = ((View)v).getHeight();
            v = v.getParent();
        }
        if (viewWidth == 0 || viewHeight == 0)
        {
            return 1.0f;
        }
        
        float maxZoom = MainConstant.MAXZOOM / MainConstant.STANDARD_RATE;
    	if (value == 0)
        {
    		if(!pageListViewListener.isIgnoreOriginalSize())
            {
    			return Math.min(Math.min(viewWidth / (float)rect.width(), viewHeight / (float)rect.height()), 1.0f);
            }
    		else
    		{
    			return Math.min(Math.min(viewWidth / (float)rect.width(), viewHeight / (float)rect.height()), maxZoom);
    		}
            
        }
        else if (value == 1)
        {
            return Math.min(viewWidth / (float)rect.width(), maxZoom);
        }
        else if (value == 2)
        {
            return Math.min(viewHeight / (float)rect.height(), maxZoom);
        }
        
        return 1.0f;
    }
    
    
    
    /**
     * get current display page number (base 1)
     * 
     * @return page number (base 1)
     */
    public int getCurrentPageNumber()
    {
        return currentIndex + 1;
    }
    
    /**
     * 
     * @see android.widget.AdapterView#getAdapter()
     *
     */
    @ Override
    public Adapter getAdapter()
    {
        return pageAdapter;
    }

    /**
     * 
     * @see android.widget.AdapterView#setAdapter(android.widget.Adapter)
     *
     */
    @ Override
    public void setAdapter(Adapter adapter)
    {
        this.pageAdapter = adapter;
    }

    /**
     * 
     *(non-Javadoc)
     * @see android.widget.AdapterView#getSelectedView()
     *
     */
    @ Override
    public View getSelectedView()
    {
        return null;
    }
    /**
     * 
     * @see android.widget.AdapterView#setSelection(int)
     *
     */
    @ Override
    public void setSelection(int position)
    {        
    }

    
    /**
     * 
     * @param v
     */
    protected void postUnRepaint(final APageListItem view)
    {
        if (view == null)
        {
            return;
        }
        post(new Runnable()
        {
            public void run()
            {
                view.removeRepaintImageView();
            }
        });
    }
    

    /**
     * 
     * @param v
     */
    public void postRepaint(final APageListItem view)
    {
        if (view == null)
        {
            return;
        }
        post(new Runnable()
        {
            public void run()
            {
                view.addRepaintImageView(null);
            }
        });
    }
    
    /**
     * 
     * @param i
     * @return
     */
    public APageListItem getCurrentPageView()
    {
        if (childViewsCache != null)
        {
            return childViewsCache.get(currentIndex);
        }
        return  null;
    }

    
    /**
     * 
     */
    protected IPageListViewListener getPageListViewListener()
    {
        return this.pageListViewListener;
    }
    
    /**
     * 
     * @param i
     * @return
     */
    private APageListItem createPageView(int pageIndex)
    {
        APageListItem pageView = childViewsCache.get(pageIndex);
        if (pageView == null)
        {
            pageView = (APageListItem)pageAdapter.getView(pageIndex, 
                (pageViewCache.size() == 0 ?  null : pageViewCache.removeFirst()), this);
            LayoutParams params = pageView.getLayoutParams();
            if (params == null)
            {
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            addViewInLayout(pageView, 0, params, true);
            // Record the view against it's adapter index
            childViewsCache.append(pageIndex, pageView);
            // 计算page的size
            pageView.measure(View.MeasureSpec.EXACTLY | (int)(pageView.getPageWidth() * zoom),
                View.MeasureSpec.EXACTLY | (int)(pageView.getPageHeight() * zoom));
        }
        return pageView;
    }
    
    /**
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    protected Rect getScrollBounds(int left, int top, int right, int bottom)
    {
        int xmin = getWidth() - right;
        int xmax = -left;
        int ymin = getHeight() - bottom;
        int ymax = -top;

        // In either dimension, if view smaller than screen then
        // constrain it to be central
        if (xmin > xmax)
        {
            xmin = xmax = (xmin + xmax) / 2;
        }
        if (ymin > ymax)
        {
            ymin = ymax = (ymin + ymax) / 2;
        }

        return new Rect(xmin, ymin, xmax, ymax);
    }

    /**
     * 
     * @param v
     * @return
     */
    protected Rect getScrollBounds(View v)
    {
        // There can be scroll amounts not yet accounted for in
        // onLayout, so add mXScroll and mYScroll to the current
        // positions when calculating the bounds.
        return getScrollBounds(v.getLeft() + eventManage.getScrollX(), 
            v.getTop() + eventManage.getScrollY(),
            v.getLeft() + v.getMeasuredWidth() + eventManage.getScrollX(), 
            v.getTop() + v.getMeasuredHeight() + eventManage.getScrollY());
    }

    /**
     * 
     * @param bounds
     * @return
     */
    protected Point getCorrection(Rect bounds)
    {
        return new Point(Math.min(Math.max(0, bounds.left), bounds.right), 
            Math.min(Math.max(0, bounds.top), bounds.bottom));
    }


    /**
     * 
     * @param v
     * @return
     */
    protected Point getScreenSizeOffset(View v)
    {
        return new Point(Math.max((getWidth() - v.getMeasuredWidth()) / 2, 0), 
            Math.max((getHeight() - v.getMeasuredHeight()) / 2, 0));
    }
    
    /**
     * 
     */
    public int getPageCount()
    {
        return pageListViewListener.getPageCount();
    }
    
    /**
     * 
     */
    protected APageListItem getPageListItem(int position, View convertView, ViewGroup parent)
    {
        return pageListViewListener.getPageListItem(position, convertView, parent);
    }
    
    /**
     * 
     */
    public boolean isInit()
    {
        return this.isInit;
    }
    
    /**
     * 
     */
    public void setDoRequstLayout(boolean b)
    {
        this.isDoRequestLayout = b;
    }
    
    public boolean isInitZoom()
    {
        return isInitZoom;
    }

    public void setInitZoom(boolean isInitZoom)
    {
        this.isInitZoom = isInitZoom;
    }

    /**
     * 
     */
    public void dispose()
    {
        pageListViewListener = null;
        if (eventManage != null)
        {
            eventManage.dispose();
            eventManage = null;
        }
        if (pageAdapter instanceof APageListAdapter)
        {
            ((APageListAdapter)pageAdapter).dispose();
            pageAdapter = null;
        }
        if (childViewsCache != null)
        {
            int size = childViewsCache.size();
            for (int i = 0; i < size; i++)
            {
                childViewsCache.valueAt(i).dispose();
            }
            childViewsCache.clear();
            childViewsCache = null;
        }
        if (pageViewCache != null)
        {
            for (APageListItem page : pageViewCache)
            {
                page.dispose();
            }
            pageViewCache.clear();
            pageViewCache = null;
        }
    }
    
    //
    private boolean isDoRequestLayout = true;
    // 
    private boolean isConfigurationChanged;
    //
    private boolean isInit;
    //
    private boolean isInitZoom;
    //
    private boolean isResetLayout;
    //
    private float zoom = 1.0f;
    // Adapter's index for the current view (base 0)
    private int currentIndex; 
    //
    private IPageListViewListener pageListViewListener;
    //
    private Adapter pageAdapter;
    //
    private APageListEventManage eventManage;
    //
    private SparseArray<APageListItem> childViewsCache = new SparseArray<APageListItem>(3);
    // Shadows the children of the adapter view
    // but with more sensible indexing
    private LinkedList<APageListItem> pageViewCache = new LinkedList<APageListItem>();

    public LinkedList<APageListItem> getPageViewCache() {
        return pageViewCache;
    }

}
