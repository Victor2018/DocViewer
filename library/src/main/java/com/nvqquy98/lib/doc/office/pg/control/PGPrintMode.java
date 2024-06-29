/*
 * 文件名称:          PrintWord.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:53:24
 */
package com.nvqquy98.lib.doc.office.pg.control;

import com.blankj.utilcode.util.ScreenUtils;
import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.common.shape.TextBox;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.pg.model.PGSlide;
import com.nvqquy98.lib.doc.office.pg.view.SlideDrawKit;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.view.STRoot;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IMainFrame;
import com.nvqquy98.lib.doc.office.system.SysKit;
import com.nvqquy98.lib.doc.office.system.beans.pagelist.APageListItem;
import com.nvqquy98.lib.doc.office.system.beans.pagelist.APageListView;
import com.nvqquy98.lib.doc.office.system.beans.pagelist.IPageListViewListener;

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
public class PGPrintMode extends FrameLayout implements IPageListViewListener
{
    /**
     * 
     * @param context
     */
    public PGPrintMode(Context context)
    {
        super(context);
    }

    /**
     * 
     *
     */
    public PGPrintMode(Context context, IControl control, PGModel pgModel, PGEditor editor)
    {
        super(context);
        this.control = control;
        this.pgModel = pgModel;
        this.editor = editor;
        
        listView = new APageListView(context, this);
        addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //修改缩放，避免打开ppt时PPT展示不全或者太小
        listView.setZoom(ScreenUtils.getScreenWidth() * 1f / pgModel.getPageSize().width, 0, 0);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(36);
    } 
    
    public void setVisible(boolean visible)
    {
        if(visible)
        {
            listView.setVisibility(View.VISIBLE);
        }
        else
        {
            listView.setVisibility(View.GONE);
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
        //view.setBackgroundColor(color)
        if ((int)(getZoom() * 100) == 100)
        {
            setZoom(getFitZoom(), Integer.MIN_VALUE, Integer.MIN_VALUE);
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
    public void showSlideForIndex(int index)
    {
        listView.showPDFPageForIndex(index);
    }
    
    /**
     * @param x 为100%的值
     * @param y 为100%的值
     * /
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
     * /
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
        return Math.max(pgModel.getSlideCount(), 1);
    }
    
    /**
     * 
     */
    public APageListItem getPageListItem(int position, View convertView, ViewGroup parent)
    {   
        Rect rect = getPageSize(position);
        return new PGPageListItem(listView, control, editor, rect.width(), rect.height());
    }
    
    /**
     * 
     */
    public Rect getPageSize(int pageIndex)
    {
        Dimension d = pgModel.getPageSize();
        if (d == null)
        {
            pageSize.set(0, 0, getWidth(), getHeight());
        }
        else
        {   
            pageSize.set(0, 0, d.width, d.height);
        }
        return pageSize;
    }

    /**
     * 
     *
     */
    public void exportImage(final APageListItem pageItem, final Bitmap srcBitmap)
    {
        if (getControl() == null || !(getParent() instanceof Presentation))
        {
            return;
        }
        PGFind find = (PGFind)control.getFind();
        if (find.isSetPointToVisible())
        {
            find.setSetPointToVisible(false);
            Rectangle rect = editor.modelToView(editor.getHighlight().getSelectStart(), new Rectangle(), false);
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
                    PGSlide slide = pgModel.getSlide(pageItem.getPageIndex());
                    if (slide != null)
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
                            //editor.getHighlight().setPaintHighlight(false);
                            // don't zoom
                            if (dstBitmap.getWidth() == rW && dstBitmap.getHeight() == rH)
                            {
                                Canvas canvas = new Canvas(dstBitmap);
                                canvas.drawColor(Color.WHITE);
                                float zoom = listView.getZoom();
                                int left = pageItem.getLeft();
                                int top = pageItem.getTop();
                                canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                                SlideDrawKit.instance().drawSlide(canvas, pgModel, editor, slide, zoom);
                                control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), zoom);
                            }
                            // zoom
                            else
                            {
                                float paintZoom = Math.min(dstBitmap.getWidth() / (float)rW, dstBitmap.getHeight() / (float)rH);
                                float zoom = listView.getZoom() * paintZoom;
                                int left = (int)(pageItem.getLeft() * paintZoom);
                                int top = (int)(pageItem.getTop() * paintZoom);
                                Canvas canvas = new Canvas(dstBitmap);
                                canvas.drawColor(Color.WHITE);
                                canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                                SlideDrawKit.instance().drawSlide(canvas, pgModel, editor, slide, zoom);
                                control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), zoom);
                            }
                            //editor.getHighlight().setPaintHighlight(true);
                            otp.callBack(dstBitmap);
                        }
                    }
                }
                catch (Exception e)
                {
                }
            }
        });
    }
        
    /**
     * get snap shot
     * @param dstBitmap
     * @return
     */
    public Bitmap getSnapshot(final Bitmap dstBitmap)
    {
    	if (getControl() == null || !(getParent() instanceof Presentation))
        {
            return null;
        }
    	
    	PGPageListItem pageItem = (PGPageListItem)getListView().getCurrentPageView();            
        if (pageItem == null)
        {
            return null;
        }
        
    	PGSlide slide = pgModel.getSlide(pageItem.getPageIndex());
        if (slide != null)
        {
        	 int rW = Math.min(getWidth(), pageItem.getWidth());
             int rH = Math.min(getHeight(), pageItem.getHeight());
             
             // don't zoom
             if (dstBitmap.getWidth() == rW && dstBitmap.getHeight() == rH)
             {
                 Canvas canvas = new Canvas(dstBitmap);
                 canvas.drawColor(Color.WHITE);
                 float zoom = listView.getZoom();
                 int left = pageItem.getLeft();
                 int top = pageItem.getTop();
                 canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                 SlideDrawKit.instance().drawSlide(canvas, pgModel, editor, slide, zoom);
             }
             // zoom
             else
             {
                 float paintZoom = Math.min(dstBitmap.getWidth() / (float)rW, dstBitmap.getHeight() / (float)rH);
                 float zoom = listView.getZoom() * paintZoom;
                 int left = (int)(pageItem.getLeft() * paintZoom);
                 int top = (int)(pageItem.getTop() * paintZoom);
                 Canvas canvas = new Canvas(dstBitmap);
                 canvas.drawColor(Color.WHITE);
                 canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                 SlideDrawKit.instance().drawSlide(canvas, pgModel, editor, slide, zoom);
             }
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
        return pgModel;
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
        if (eventMethodType == ON_SINGLE_TAP_UP && e1 != null
            && e1.getAction() == MotionEvent.ACTION_UP)
        {
            APageListItem item = listView.getCurrentPageView();
            if (item != null)
            {                
                float zoom = listView.getZoom();
                
                int x = (int)((e1.getX() - item.getLeft()) / zoom);
                int y = (int)((e1.getY() - item.getTop()) / zoom);    
                IShape shape = pgModel.getSlide(item.getPageIndex()).getTextboxShape(x, y);
                if (shape != null && shape.getType() == AbstractShape.SHAPE_TEXTBOX)
                {   
                    TextBox textBox = (TextBox)shape;
                    STRoot root = textBox.getRootView();
                    if (root != null)
                    {
                        long offset = root.viewToModel(x - shape.getBounds().x, y - shape.getBounds().y, false);
                        if (offset >= 0)
                        {
                            ParagraphElement paraElem = (ParagraphElement)textBox.getElement().getElement(offset);
                            if (paraElem != null)
                            {
                                IElement leaf = paraElem.getLeaf(offset);
                                if (leaf != null)
                                {
                                    int hyID = AttrManage.instance().getHperlinkID(leaf.getAttribute());
                                    if (hyID >= 0)
                                    {
                                        Hyperlink hylink = control.getSysKit().getHyperlinkManage().getHyperlink(hyID);
                                        if (hylink != null)
                                        {
                                            control.actionEvent(EventConstant.APP_HYPERLINK, hylink);
                                            return true;
                                        }
                                    }
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
        if (getParent() instanceof Presentation)
        {
            Presentation pg = (Presentation)getParent();
            if (pg.getFind().getPageIndex() != pageItem.getPageIndex())
            {
                pg.getEditor().getHighlight().removeHighlight();
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
    public PGSlide getCurrentPGSlide()
    {
        APageListItem item =  listView.getCurrentPageView();
        if (item != null)
        {
            return pgModel.getSlide(item.getPageIndex());
        }
        else
        {
            return pgModel.getSlide(0);
        }
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
            String pn = String.valueOf((listView.getCurrentPageNumber()) + " / " + pgModel.getSlideCount());
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
        
        if (preShowPageIndex != listView.getCurrentPageNumber())
        {
            changePage();
            preShowPageIndex = listView.getCurrentPageNumber();
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
        pgModel = null;
        pageSize = null;
    }
    
    private int preShowPageIndex = -1;
    //
    private IControl control;
    //
    private APageListView listView;
    // 绘制器
    private Paint paint;
    //
    private PGModel pgModel;
    //
    private PGEditor editor;
    //
    private Rect pageSize = new Rect();

}
