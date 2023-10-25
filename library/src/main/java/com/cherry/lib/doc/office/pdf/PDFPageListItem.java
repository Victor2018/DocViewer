/*
 * 文件名称:          PDFPageView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午9:36:57
 */
package com.cherry.lib.doc.office.pdf;

import com.cherry.lib.doc.office.constant.EventConstant;
import com.cherry.lib.doc.office.constant.PDFConstant;
import com.cherry.lib.doc.office.fc.pdf.PDFLib;
import com.cherry.lib.doc.office.simpletext.control.SafeAsyncTask;
import com.cherry.lib.doc.office.system.IControl;
import com.cherry.lib.doc.office.system.beans.pagelist.APageListItem;
import com.cherry.lib.doc.office.system.beans.pagelist.APageListView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * PDF document page view
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
public class PDFPageListItem extends APageListItem
{
    /**
     * 
     * @param content
     * @param parentSize
     */
    public PDFPageListItem(APageListView listView, IControl control, int pageWidth, int pageHeight)
    {
        super(listView, pageWidth, pageHeight);
        this.listView = listView;
        this.control = control;
        this.lib = (PDFLib)listView.getModel();
        this.isAutoTest = control.isAutoTest();
        this.setBackgroundColor(PDFConstant.BACKGROUND_COLOR);
    }

    /**
     * 
     * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
     *
     */
    @ Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        
        int w = right - left;
        int h = bottom - top;
        if (originalImageView != null)
        {
            originalImageView.layout(0, 0, w, h);
        }
        if (searchView != null)
        {
            searchView.layout(0, 0, w, h);
        }
        if (viewWidth != w || viewHeight != h)
        {
            viewWidth =  viewHeight = 0;
            repaintArea = null;
            if (repaintImageView != null)
            {
                repaintImageView.setImageBitmap(null);
            }
        }
        else
        {
            if (repaintImageView != null)
            {
                repaintImageView.layout(repaintArea.left, repaintArea.top, repaintArea.right, repaintArea.bottom);
            }
        }        
//        if (control.getMainFrame().isShowProgressBar() || true)
        {
            if (mBusyIndicator != null)
            {
                int x, y;
                if (w > listView.getWidth())
                {
                    x = (listView.getWidth() - PDFConstant.BUSY_SIZE) / 2 - left;
                }
                else
                {
                    x = (w - PDFConstant.BUSY_SIZE) / 2;
                }
                if (h > listView.getHeight())
                {
                    y = (listView.getHeight() - PDFConstant.BUSY_SIZE) / 2 - top;
                }
                else
                {
                    y = (h - PDFConstant.BUSY_SIZE) / 2;
                }
                mBusyIndicator.layout(x, y, x + PDFConstant.BUSY_SIZE, y + PDFConstant.BUSY_SIZE);
            }
        }
        
    }
    
    /**
     * 
     * @param pageIndex     page index (base 0)
     * @param pageWidth     page width of after scaled
     * @param pageHeight    page height of after scaled
     */
    public void setPageItemRawData(final int pIndex, int pWidth, int pHeight)
    {
        super.setPageItemRawData(pIndex, pWidth, pHeight);
        isOriginalBitmapValid = false;
        // Cancel pending render task
        if (darwOriginalPageTask != null)
        {
            darwOriginalPageTask.cancel(true);
            darwOriginalPageTask = null;
        }
        if (originalImageView == null)
        {
            originalImageView = new ImageView(listView.getContext())
            {
                public boolean isOpaque()
                {
                    return true;
                }
                
                public void onDraw(Canvas canvas)
                {
                    try
                    {
                        super.onDraw(canvas);
                    }
                    catch (Exception e)
                    {   
                    }
                }
            };
            originalImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(originalImageView);
        }

        // Calculate scaled size that fits within the screen limits
        // This is the size at minimum zoom
        if (pageWidth <= 0 || pageHeight <= 0)
        {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE)
        {
            // When hardware accelerated, updates to the bitmap seem to be
            // ignored, so we recreate it. There may be another way around this
            // that we are yet to find.
            originalImageView.setImageBitmap(null);
        }

        float zoom = listView.getFitZoom();
        if (originalBitmap == null 
            || originalBitmap.getWidth() != (int)(pageWidth * zoom) 
            || originalBitmap.getHeight() != (int)(pageHeight * zoom))
        {
            int bW = (int)(pageWidth * zoom);
            int bH = (int)(pageHeight * zoom);
            try
            {
//                if (bW > 2000 || bH > 2000)
//                {
//                    bW /= 5;
//                    bH /= 5;
//                }
                if (!listView.isInitZoom())
                {
                    listView.setZoom(zoom, false);
                }
                if (originalBitmap != null)
                {
                	while(!lib.isDrawPageSyncFinished())
                	{
                		try
                		{
                			Thread.sleep(100);
                		}
                		catch(Exception e)
                		{
                			
                		}
                	}
                    originalBitmap.recycle();
                }
                originalBitmap = Bitmap.createBitmap(bW, bH, Bitmap.Config.ARGB_8888);
            }
            catch (OutOfMemoryError e)
            {
            	System.gc();
            	try
            	{
            		Thread.sleep(50);
            		originalBitmap = Bitmap.createBitmap(bW, bH, Bitmap.Config.ARGB_8888);
            	}
            	catch(Exception ee)
            	{
            		return;
            	}
            }
        }

        final APageListItem own = this;
        // Render the page in the background
        darwOriginalPageTask = new SafeAsyncTask<Void, Void, Bitmap>()
        {
            private boolean isCancel = false;
            /**
             * 
             *
             */
            protected Bitmap doInBackground(Void...v)
            {
                try
                {
                    if (originalBitmap == null)
                    {
                        return null;
                    }                    
                    Thread.sleep(pageIndex == listView.getCurrentPageNumber() - 1 ? 500 : 1000);
    
                    if (isCancel)
                    {
                        return null;
                    }
                    lib.drawPageSync(originalBitmap, pageIndex, 
                        originalBitmap.getWidth(), originalBitmap.getHeight(), 
                    0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), 1);
                    return originalBitmap;
                }
                catch (Exception e)
                {
                    return null;
                }
            }

            /**
             * 
             *
             */
            protected void onPreExecute()
            {
                originalImageView.setImageBitmap(null);
                if (mBusyIndicator == null)
                {
                    mBusyIndicator = new ProgressBar(getContext());
                    mBusyIndicator.setIndeterminate(true);
                    mBusyIndicator.setBackgroundResource(android.R.drawable.progress_horizontal);
                    addView(mBusyIndicator);
                    mBusyIndicator.setVisibility(VISIBLE);
                }
                else
                {
                    mBusyIndicator.setVisibility(VISIBLE);
                }
            }
            
            /**
             * 
             *
             */
            protected void onCancelled() 
            {
                isCancel = true;
            }

            /**
             * 
             *
             */
            protected void onPostExecute(Bitmap v)
            {
                try
                {
                    mIsBlank = false;
                    isOriginalBitmapValid = true;
                    if (listView != null)
                    {
                        if (mBusyIndicator != null)
                        {
                            mBusyIndicator.setVisibility(INVISIBLE);
                        }                        
                    }
                    listView.setDoRequstLayout(false);
                    originalImageView.setImageBitmap(originalBitmap);
                    listView.setDoRequstLayout(true);
                    invalidate();
                    if (listView != null)
                    {
                        if ((int)(listView.getZoom() * 100) == 100
                            || (isInit && pIndex == 0))
                        {
                            if (v != null)
                            {
                                if (isInit && pIndex == 0)
                                {
                                    listView.postRepaint(listView.getCurrentPageView());
                                }
                                else
                                {
                                    listView.exportImage(own, originalBitmap);
                                }
                            }
                        }
                        isInit = false;
                        // auto test
                        if (isAutoTest)
                        {
                            control.actionEvent(EventConstant.SYS_AUTO_TEST_FINISH_ID, true);
                        }
                    }
                }
                catch (NullPointerException e)
                {
                    
                }
            }
        };

        darwOriginalPageTask.safeExecute();

        
        if (searchView == null)
        {
            searchView = new View(getContext())
            {
               /**
                 * 
                 *
                 */
                protected void onDraw(Canvas canvas)
                {
                    super.onDraw(canvas);
                    PDFFind find = (PDFFind)control.getFind();
                    if (find != null && !mIsBlank)
                    {
                        find.drawHighlight(canvas, 0, 0, own);
                    }
                }
            };
            addView(searchView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
    }
    /**
     * 
     */
    public void releaseResources()
    {
        super.releaseResources();
        isOriginalBitmapValid = false;
        // Cancel pending render task
        if (darwOriginalPageTask != null)
        {
            darwOriginalPageTask.cancel(true);
            darwOriginalPageTask = null;
        }

        if (repaintSyncTask != null)
        {
            repaintSyncTask.cancel(true);
            repaintSyncTask = null;
        }
        if (originalImageView != null)
        {
            originalImageView.setImageBitmap(null);
        }

        if (repaintImageView != null)
        {
            repaintImageView.setImageBitmap(null);
        }
        
        if (control.getMainFrame().isShowProgressBar() || true)
        {
            if (mBusyIndicator != null)
            {
                mBusyIndicator.setVisibility(VISIBLE);
            }
        }
//        else 
//        {
//            if(listView != null && pageIndex == listView.getCurrentPageNumber() - 1)
//            {
//                ICustomDialog customDialog = control.getCustomDialog();
//                if (customDialog != null)
//                {
//                    customDialog.showDialog(ICustomDialog.DIALOGTYPE_LOADING);
//                }
//            }
//        }
    }

    /**
     * black page
     * 
     * @param pageIndex page index (base 0)
     */
    public void blank(int pIndex)
    {
        super.blank(pIndex);
        isOriginalBitmapValid = false;
        // Cancel pending render task
        if (darwOriginalPageTask != null)
        {
            darwOriginalPageTask.cancel(true);
            darwOriginalPageTask = null;
        }

        if (repaintSyncTask != null)
        {
            repaintSyncTask.cancel(true);
            repaintSyncTask = null;
        }
        if (originalImageView != null)
        {   
            originalImageView.setImageBitmap(null);
        }

        if (repaintImageView != null)
        {   
            repaintImageView.setImageBitmap(null);
        }        
        if (mBusyIndicator != null)
        {
            mBusyIndicator.setVisibility(VISIBLE);
        }
    }
    
    /**
     * 
     * @param f
     */
    public void setLinkHighlighting(boolean vlaue)
    {
        /*if (mSearchView != null)
        {
            mSearchView.invalidate();
        }*/
    }
    
    
    /**
     * get hyperlink count assign location
     * 
     * @param x     x axis value
     * @param y     y axis value
     * @return hyperlink count 
     */
    public int getHyperlinkCount(float x, float y)
    {
        float scale = getWidth() / (float)pageWidth;
        float docRelX = (x - getLeft()) / scale;
        float docRelY = (y - getTop()) / scale;
        
        return lib.getHyperlinkCountSync(pageIndex, docRelX, docRelY);
    }

    /**
     * added reapint image view
     */
    protected void addRepaintImageView(Bitmap bmp)
    {
        Rect viewArea = new Rect(getLeft(), getTop(), getRight(), getBottom());
        // If the viewArea's size matches the unzoomed size, there is no need for an hq patch
        if (viewArea.width() != pageWidth
            || viewArea.height() != pageHeight
            || (originalBitmap != null && ((int)listView.getZoom()) * 100 == 100
                && (originalBitmap.getWidth() != pageWidth
                                           || originalBitmap.getHeight() != pageHeight)))
        {
            //Point patchViewSize = new Point(viewArea.width(), viewArea.height());
            Rect paintArea = new Rect(0, 0, listView.getWidth(), listView.getHeight());

            // Intersect and test that there is an intersection
            if (!paintArea.intersect(viewArea))
            {
                return;
            }

            // Offset patch area to be relative to the view top left
            paintArea.offset(-viewArea.left, -viewArea.top);

            // If being asked for the same area as last time, nothing to do
            if (paintArea.equals(repaintArea)
                && viewHeight == viewArea.width()
                && viewHeight == viewArea.height())
            {
                return;
            }

            // Stop the drawing of previous patch if still going
            if (repaintSyncTask != null)
            {
                repaintSyncTask.cancel(true);
                repaintSyncTask = null;
            }

            // Create and add the image view if not already done
            if (repaintImageView == null)
            {
                repaintImageView = new ImageView(listView.getContext())
                {
                    public boolean isOpaque()
                    {
                        return true;
                    }
                    /**
                     *
                     */
                    public void onDraw(Canvas canvas)
                    {
                        try
                        {
                            super.onDraw(canvas);
                        }
                        catch (Exception e)
                        {                        
                        }
                    }
                };
                
                repaintImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                repaintImageView.setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
                //addView(repaintImageView);
            }
            final PDFPageListItem own = this;
            repaintSyncTask = new SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo>()
            {
                /**
                 * 
                 * @see android.os.AsyncTask#doInBackground(Params[])
                 *
                 */
                protected RepaintAreaInfo doInBackground(RepaintAreaInfo...v)
                {
                    try
                    {
                        lib.drawPageSync(v[0].bm, pageIndex, 
                            v[0].viewWidth, v[0].viewHeight,
                            v[0].repaintArea.left, v[0].repaintArea.top, 
                            v[0].repaintArea.width(), v[0].repaintArea.height(), 1);
                        return v[0];
                    }
                    catch (Exception e)
                    {
                        return null;
                    }
                }

                /**
                 * 
                 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
                 *
                 */
                protected void onPostExecute(RepaintAreaInfo v)
                {
                    try
                    {
                        viewWidth = v.viewWidth;
                        viewHeight = v.viewHeight;
                        repaintArea = v.repaintArea;
                        // dipose 
                        Drawable d = repaintImageView.getDrawable();
                        if (d instanceof BitmapDrawable)
                        {
                            if (((BitmapDrawable)d).getBitmap() != null)
                            {
                            	while(!lib.isDrawPageSyncFinished())
                            	{
                            		try
                            		{
                            			Thread.sleep(100);
                            		}
                            		catch(Exception e)
                            		{
                            			
                            		}
                            	}
                                ((BitmapDrawable)d).getBitmap().recycle();
                            }
                            listView.setDoRequstLayout(false);
                            repaintImageView.setImageBitmap(null);
                            repaintImageView.setImageBitmap(v.bm);
                            listView.setDoRequstLayout(true);
                        }
                        //requestLayout();
                        // Calling requestLayout here doesn't lead to a later call to layout. No idea
                        // why, but apparently others have run into the problem.
                        repaintImageView.layout(repaintArea.left, repaintArea.top,
                            repaintArea.right, repaintArea.bottom);
                        if (repaintImageView.getParent() == null)
                        {
                            addView(repaintImageView);
                            if (searchView != null)
                            {
                                searchView.bringToFront();
                            }
                        }
                        invalidate();
                        if (listView != null)
                        {
                            listView.exportImage(own, v.bm);                        
                        }
                    }
                    catch (Exception e)
                    {
                        
                    }
                }
            };

            try
            {
                Bitmap bitmap = Bitmap.createBitmap(paintArea.width(), paintArea.height(), Bitmap.Config.ARGB_8888);
                repaintSyncTask.safeExecute(new RepaintAreaInfo(bitmap, viewArea.width(), viewArea.height(), paintArea));
            }
            catch (OutOfMemoryError e)
            {          
                if (repaintImageView != null)
                {
                    Drawable d = repaintImageView.getDrawable();
                    if (d instanceof BitmapDrawable)
                    {
                        if (((BitmapDrawable)d).getBitmap() != null)
                        {
                        	while(!lib.isDrawPageSyncFinished())
                        	{
                        		try
                        		{
                        			Thread.sleep(100);
                        		}
                        		catch(Exception ee)
                        		{
                        			
                        		}
                        	}
                            ((BitmapDrawable)d).getBitmap().recycle();
                        }
                    }
                }
                System.gc();
                try
                {
                    Thread.sleep(50);
                    Bitmap bitmap = Bitmap.createBitmap(paintArea.width(), paintArea.height(), Bitmap.Config.ARGB_8888);
                    repaintSyncTask.safeExecute(new RepaintAreaInfo(bitmap, viewArea.width(), viewArea.height(), paintArea));
                }
                catch (OutOfMemoryError e2)
                {
                    
                }
                catch (Exception  aa)
                {
                    
                }
            }
        }
        else if (!mIsBlank)
        {
            if (isOriginalBitmapValid)
            {
                listView.exportImage(this, originalBitmap);
            }
        }
    }

    /**
     * remove reapint image view
     */
    protected void removeRepaintImageView()
    {
        // Stop the drawing of the patch if still going
        if (repaintSyncTask != null)
        {
            repaintSyncTask.cancel(true);
            repaintSyncTask = null;
        }

        viewWidth = viewHeight = 0;
        repaintArea = null;
        if (repaintImageView != null)
        {
            repaintImageView.setImageBitmap(null);
        }
    }
    
    /**
     * 
     */
    protected void drawSerachView(Canvas canvas)
    {
        if (searchView != null)
        {
            searchView.draw(canvas);
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        listView = null;
        if (darwOriginalPageTask != null)
        {
            darwOriginalPageTask.cancel(true);
            darwOriginalPageTask = null;
        }
        if (repaintSyncTask != null)
        {
            repaintSyncTask.cancel(true);
            repaintSyncTask = null;
        }
        if (originalImageView != null)
        {   
            Drawable d = originalImageView.getDrawable();
            if (d instanceof BitmapDrawable)
            {
                if (((BitmapDrawable)d).getBitmap() != null)
                {
                	while(!lib.isDrawPageSyncFinished())
                	{
                		try
                		{
                			Thread.sleep(100);
                		}
                		catch(Exception e)
                		{
                			
                		}
                	}
                    ((BitmapDrawable)d).getBitmap().recycle();
                }
            }
            originalImageView.setImageBitmap(null);
        }

        if (repaintImageView != null)
        {   
            Drawable d = repaintImageView.getDrawable();
            if (d instanceof BitmapDrawable)
            {
                if (((BitmapDrawable)d).getBitmap() != null)
                {
                	while(!lib.isDrawPageSyncFinished())
                	{
                		try
                		{
                			Thread.sleep(100);
                		}
                		catch(Exception e)
                		{
                			
                		}
                	}
                    ((BitmapDrawable)d).getBitmap().recycle();
                }
            }
            repaintImageView.setImageBitmap(null);
        }
    }
    
    //
    private boolean isOriginalBitmapValid;
    //
    private boolean isAutoTest;
    // parent component width
    //private int pWidth;
    // parent component height
    //private int pHeight;
    // the width of repaint component 
    private int viewWidth;
    // the height of repaint component
    private int viewHeight;

    // Image rendered at minimum zoom
    private ImageView originalImageView;
    // Image rendered at minimum zoom
    private Bitmap originalBitmap;
    // draw original page image 
    private SafeAsyncTask<Void, Void, Bitmap> darwOriginalPageTask;
    
    // repaint image view
    private ImageView repaintImageView;
    // repaint synchronized task
    private SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo> repaintSyncTask;    
    // View size on the basis of which the patch was created
    // parent component size
    private Rect repaintArea;
    //
    private View searchView;
    //
    //private RectF[] searchRect;
    //
    private final PDFLib lib;
    //
    private ProgressBar mBusyIndicator;
    
}
