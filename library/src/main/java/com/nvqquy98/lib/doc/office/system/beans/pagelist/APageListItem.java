/*
 * 文件名称:          PDFPageView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午9:36:57
 */
package com.nvqquy98.lib.doc.office.system.beans.pagelist;

import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.CalloutView;
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.IExportListener;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

public class APageListItem extends ViewGroup implements IExportListener
{	
    /**
     * 
     * @param content
     * @param parentSize
     */
    public APageListItem(APageListView aListView, int pageWidth, int pageHeight)
    {
        super(aListView.getContext());
        this.listView = aListView;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.setBackgroundColor(Color.WHITE);
        
    }
    
    public IControl getControl()
    {
		return control;
	}
    
    public void initCalloutView()
    {
    	if (callouts == null)
    	{
	        this.callouts = new CalloutView(listView.getContext(), control, this);
	        callouts.setIndex(pageIndex);
	        addView(callouts, 0);
    	}
    }
	
	@Override
	public void exportImage()
	{
		listView.postRepaint(listView.getCurrentPageView());
	}
	
    /**
     * 
     *
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.UNSPECIFIED ?
            pageWidth : View.MeasureSpec.getSize(widthMeasureSpec);
        
        int height = View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.UNSPECIFIED ?
            pageHeight : View.MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    /**
     * 
     * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
     *
     */
    @ Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if (callouts != null)
        {
            int w = right - left;
            int h = bottom - top;
            callouts.setZoom(listView.getZoom());
        	callouts.layout(0, 0, w, h);
        	callouts.bringToFront();
        }
    }
    
    /**
     * 
     * @param pageIndex     page index (base 0)
     * @param pageWidth     page width of after scaled
     * @param pageHeight    page height of after scaled
     */
    public void setPageItemRawData(final int pIndex, int pageWidth, int pageHeight)
    {
        mIsBlank = false;
        pageIndex = pIndex;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        
        if (callouts == null)
        {
	    	if (!control.getSysKit().getCalloutManager().isPathEmpty(pIndex))
	    	{
				initCalloutView();
	    	}
        }
        else
        {
	        callouts.setIndex(pIndex);
        }        
    }
    /**
     * 
     */
    public void releaseResources()
    {
        mIsBlank = true;
        pageIndex = 0;
        if (pageWidth == 0 || pageHeight == 0)
        {
            pageWidth = listView.getWidth();
            pageHeight = listView.getHeight();
        }
    }

    /**
     * black page
     * 
     * @param pageIndex page index (base 0)
     */
    public void blank(int pageIndex)
    {
        mIsBlank = true;
        this.pageIndex = pageIndex;
        if (pageWidth == 0 || pageHeight == 0)
        {
            pageWidth = listView.getWidth();
            pageHeight = listView.getHeight();
        }

    }
    
    /**
     * 
     * @param f
     */
    public void setLinkHighlighting(boolean vlaue)
    {
        
    }


    /**
     * added reapint image view
     */
    protected void addRepaintImageView(Bitmap bmp)
    {
        
    }

    /**
     * remove reapint image view
     */
    protected void removeRepaintImageView()
    {
        
    }
    
    /**
     * 
     * @return
     */
    public int getPageIndex()
    {
        return pageIndex;
    }

    /**
     * 
     *
     */
    public boolean isOpaque()
    {
        return true;
    }
    
    /**
     * 
     */
    public int getPageWidth()
    {
        return pageWidth;
    }
    
    /**
     * 
     */
    public int getPageHeight()
    {
        return pageHeight;
    }
    /**
     * 
     */
    public void dispose()
    {
        listView = null;
    }
    
    // blank page
    protected boolean mIsBlank;
    // page number (base 0)
    protected int pageIndex;
    //
    protected int pageWidth;
    //
    protected int pageHeight;
    //
    protected APageListView listView;
    //
    protected boolean isInit = true;
    //
    protected IControl control;
    //
    protected CalloutView callouts;
    
}
