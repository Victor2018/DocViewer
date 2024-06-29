/*
 * 文件名称:          Presentation.java
 *
 * 编译器:            android2.2
 * 时间:              下午3:49:28
 */
package com.nvqquy98.lib.doc.office.pg.control;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;

import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.ISlideShow;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.pg.animate.ShapeAnimation;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.pg.model.PGNotes;
import com.nvqquy98.lib.doc.office.pg.model.PGSlide;
import com.nvqquy98.lib.doc.office.pg.view.SlideDrawKit;
import com.nvqquy98.lib.doc.office.pg.view.SlideShowView;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IFind;
import com.nvqquy98.lib.doc.office.system.SysKit;
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.CalloutView;
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.IExportListener;

import java.util.List;

/**
 * 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-2
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class Presentation extends FrameLayout implements IFind, IExportListener {
    /**
     *
     */
    public Presentation(Activity activity, PGModel pgModel, IControl control) {
        super(activity);
        this.control = control;
        this.pgModel = pgModel;
        setLongClickable(true);
        pgFind = new PGFind(this);
        //
        editor = new PGEditor(this);
        //
        pgPrintMode = new PGPrintMode(activity, control, pgModel, editor);
        //
        addView(pgPrintMode);
    }

    public void initCalloutView() {
        if (slideshow) {
            if (callouts == null) {
                callouts = new CalloutView(this.getContext(), control, this);
                callouts.setIndex(slideIndex_SlideShow);
                addView(callouts);
            }
        } else {
            pgPrintMode.getListView().getCurrentPageView().initCalloutView();
        }
    }

    @Override
    public void exportImage() {
        if (slideshow) {
            createPicture();
        } else {
            pgPrintMode.exportImage(pgPrintMode.getListView().getCurrentPageView(), null);
        }
    }

    /**
     *
     */
    public void init() {
        //layoutParams = getLayoutParams();
        init = true;
        initSlidebar();
        pgPrintMode.init();
    }

    /**
     *
     */
    public void initSlidebar() {
        /*if (layoutParams == null)
        {
            return;
        }
        // 非指定高度才需要重高度
        if (layoutParams.height == LayoutParams.MATCH_PARENT
            || layoutParams.height == LayoutParams.FILL_PARENT)
        {
            mHeight = ((View)getParent()).getHeight() - getTop();
            int bHeight = control.getMainFrame().getBottomBarHeight();
            if (bHeight > 0 || isSlideShow())
            {
                mHeight -= bHeight;
                setLayoutParams(new LinearLayout.LayoutParams(layoutParams.width, mHeight));
            }
        }*/
    }

    /**
     *
     */
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (pgPrintMode != null) {
            pgPrintMode.setBackgroundColor(color);
        }
    }

    /**
     *
     */
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (pgPrintMode != null) {
            pgPrintMode.setBackgroundResource(resid);
        }
    }

    /**
     *
     */
    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);
        if (pgPrintMode != null) {
            pgPrintMode.setBackgroundDrawable(d);
        }
    }

    public void setViewVisible(boolean visible) {
        pgPrintMode.setVisible(visible);
    }

    public boolean showLoadingSlide() {
        if (currentIndex < getRealSlideCount()) {
            post(new Runnable() {
                /**
                 *
                 */
                public void run() {
                    setViewVisible(true);
                }
            });
            pgPrintMode.showSlideForIndex(currentIndex);
            return true;
        }
        return false;
    }

    /**
     * 显示指定的slide
     */
    public void showSlide(int index, boolean find) {
        if (!find) {
            control.getMainFrame().setFindBackForwardState(false);
        }
        if (index >= pgModel.getSlideCount()) {
            return;
        }
        if (!slideshow) {
            currentIndex = index;
            if (index < getRealSlideCount()) {
                pgPrintMode.showSlideForIndex(index);
            } else {
                setViewVisible(false);
            }
        } else {
            int old = currentIndex;
            currentIndex = index;
            currentSlide = pgModel.getSlide(index);
            if (slideView == null) {
                slideView = new SlideShowView(this, currentSlide);
            }
            if (slideView != null) {
                slideView.changeSlide(currentSlide);
            }
            if (old != currentIndex) {
                control.actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);
                // 显示的slide，需要dispose不显示slide的
                //disposeOldSlideView(pgModel.getSlide(old));
                SlideDrawKit.instance().disposeOldSlideView(pgModel, pgModel.getSlide(old));
            }
            postInvalidate();
            // to picture
            post(new Runnable() {

                @Override
                public void run() {
                    if (control != null) {
                        control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                    }
                }
            });
        }
    }

    /**
     *
     */
    protected void onDraw(Canvas canvas) {
        if (!init || !slideshow) {
            return;
        }
        try {
            slideView.drawSlide(canvas, fitZoom, callouts);
            // auto test code
            if (control.isAutoTest()) {
                if (currentIndex < getRealSlideCount() - 1) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                    showSlide(currentIndex + 1, false);
                } else {
                    control.actionEvent(EventConstant.SYS_AUTO_TEST_FINISH_ID, true);
                }
            }
            if (preShowSlideIndex != currentIndex) {
                control.getMainFrame().changePage();
                preShowSlideIndex = currentIndex;
            }
        } catch (NullPointerException ex) {
            control.getSysKit().getErrorKit().writerLog(ex);
        }
    }

    /**
     *
     */
    public void createPicture() {
        IOfficeToPicture otp = control.getOfficeToPicture();
        if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGE_END) {
            try {
                toPicture(otp);
            } catch (Exception e) {
            }
        }
    }

    /**
     *
     */
    private void toPicture(IOfficeToPicture otp) {
        if (!init || !slideshow) {
            PGPageListItem item = (PGPageListItem) pgPrintMode.getListView().getCurrentPageView();
            item.addRepaintImageView(null);
        } else if (slideView.animationStoped()) {
            boolean b = PictureKit.instance().isDrawPictrue();
            PictureKit.instance().setDrawPictrue(true);
            //
            float paintZoom = (slideshow ? fitZoom : zoom);
            Dimension d = getPageSize();
            int originBitmapW = Math.min((int) (d.width * paintZoom), getWidth());
            int originbitmapH = Math.min((int) (d.height * paintZoom), getHeight());
            Bitmap bitmap = otp.getBitmap(originBitmapW, originbitmapH);
            if (bitmap == null) {
                return;
            }
            Canvas picCanvas = new Canvas(bitmap);
            picCanvas.drawColor(Color.BLACK);
            slideView.drawSlideForToPicture(picCanvas, paintZoom, originBitmapW, originbitmapH);
            control.getSysKit().getCalloutManager().drawPath(picCanvas, getCurrentIndex(), paintZoom);
            otp.callBack(bitmap);
            PictureKit.instance().setDrawPictrue(b);
        }
    }

    /**
     * @param destBitmap
     * @return
     */
    public Bitmap getSnapshot(Bitmap destBitmap) {
        if (destBitmap == null) {
            return null;
        }
        if (!init || !slideshow) {
            return pgPrintMode.getSnapshot(destBitmap);
        } else {
            //
            float paintZoom = (slideshow ? fitZoom : zoom);
            Dimension d = getPageSize();
            int originBitmapW = Math.min((int) (d.width * paintZoom), getWidth());
            int originbitmapH = Math.min((int) (d.height * paintZoom), getHeight());
            paintZoom *= Math.min((destBitmap.getWidth() / (float) originBitmapW), (destBitmap.getHeight() / (float) originbitmapH));
            Canvas picCanvas = new Canvas(destBitmap);
            picCanvas.drawColor(Color.BLACK);
            slideView.drawSlideForToPicture(picCanvas, paintZoom, destBitmap.getWidth(), destBitmap.getHeight());
        }
        return destBitmap;
    }

    /**
     * page to image for page number (base 1)
     *
     * @return bitmap raw data
     */
    public Bitmap slideToImage(int slideNumber) {
        if (slideNumber <= 0 || slideNumber > getRealSlideCount()) {
            return null;
        }
        return SlideDrawKit.instance().slideToImage(pgModel, editor, pgModel.getSlide(slideNumber - 1));
    }

    /**
     * page to image for page number (base 1)
     *
     * @return bitmap raw data
     */
    public Bitmap slideAreaToImage(int slideNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        if (slideNumber <= 0 || slideNumber > getRealSlideCount()
                || !SysKit.isValidateRect((int) (getPageSize().getWidth()), (int) (getPageSize().getHeight()), srcLeft, srcTop, srcWidth, srcHeight)) {
            return null;
        }
        return SlideDrawKit.instance().slideAreaToImage(pgModel, editor, pgModel.getSlide(slideNumber - 1),
                srcLeft, srcTop, srcWidth, srcHeight, desWidth, desHeight);
    }

    /**
     * page to image for page number (base 1)
     *
     * @return bitmap raw data
     */
    public Bitmap getThumbnail(int slideNumber, float zoom) {
        if (slideNumber <= 0 || slideNumber > getRealSlideCount()) {
            return null;
        }
        return SlideDrawKit.instance().getThumbnail(pgModel, editor, pgModel.getSlide(slideNumber - 1), zoom);
    }

    /**
     * get slide node for slide number (base 1)
     *
     * @param slideNumber slide number
     * @return slide note
     */
    public String getSldieNote(int slideNumber) {
        if (slideNumber <= 0 || slideNumber > getSlideCount()) {
            return null;
        }
        PGNotes note = pgModel.getSlide(slideNumber - 1).getNotes();
        return note == null ? "" : note.getNotes();
    }

    /**
     *
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isConfigurationChanged = true;
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        processPageSize(w, h);
    }

    private void processPageSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        /*if (layoutParams == null)
        {
            return;
        }*/
        if (isConfigurationChanged || slideshow) {
            if (isConfigurationChanged) {
                isConfigurationChanged = false;
            }
            
            /*if (layoutParams.height == LayoutParams.MATCH_PARENT
                || layoutParams.height == LayoutParams.FILL_PARENT)
            {
                ViewGroup parent = (ViewGroup)getParent();
                if (parent != null && parent.getChildCount() > 1)
                {
                    mWidth = parent.getWidth();
                    mHeight = parent.getHeight();
                    mHeight -= getTop();
                    mHeight -= control.getMainFrame().getBottomBarHeight();
                    setLayoutParams(new LinearLayout.LayoutParams(layoutParams.width, mHeight));
                    layout(getLeft(), getTop(), getLeft() + mWidth, getTop() + mHeight);
                    
                    final View view = parent.getChildAt(parent.getChildCount() - 1);
                    if (view != null && view != this)
                    {
                        view.setVisibility(View.INVISIBLE);
                        post(new Runnable()
                        {
                            @ Override
                            public void run()
                            {
                                view.setVisibility(View.VISIBLE);
                                requestLayout();
                            }
                        }); 
                    }
                }
            }*/
            fitZoom = getFitZoom();
            if (slideshow) {
                // to picture
                post(new Runnable() {
                    public void run() {
                        control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                    }
                });
            }
        }
    }

    /**
     *
     */
    public float getFitZoom() {
        if (slideshow) {
            Dimension pageSize = getPageSize();
            return Math.min((float) (mWidth) / pageSize.width,
                    (float) (mHeight) / pageSize.height);
        }
        return pgPrintMode.getFitZoom();
    }

    /**
     * 返回当前显示slide的index
     */
    public int getCurrentIndex() {
        return (slideshow ? slideIndex_SlideShow : pgPrintMode.getCurrentPageNumber() - 1);
    }

    /**
     *
     */
    public int getSlideCount() {
        return pgModel.getSlideCount();
    }

    /**
     *
     */
    public int getRealSlideCount() {
        return pgModel.getRealSlideCount();
    }

    /**
     *
     */
    public PGSlide getSlide(int index) {
        return pgModel.getSlide(index);
    }

    /**
     *
     */
    public IControl getControl() {
        return control;
    }

    /**
     * @return Returns the mWidth.
     */
    public int getmWidth() {
        return mWidth;
    }

    /**
     * @param mWidth The mWidth to set.
     */
    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    /**
     * @return Returns the mHeight.
     */
    public int getmHeight() {
        return mHeight;
    }

    /**
     * @param mHeight The mHeight to set.
     */
    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    /**
     * @param w
     * @param h
     */
    public void setSize(int w, int h) {
        this.mWidth = w;
        this.mHeight = h;
    }

    /**
     * @return Returns the zoom.
     */
    public float getZoom() {
        return (slideshow ? fitZoom : pgPrintMode.getZoom());
    }

    /**
     * @param zoom The zoom to set.
     */
    public void setZoom(float zoom, int pointX, int pointY) {
        if (slideshow) {
            return;
        }
        pgPrintMode.setZoom(zoom, pointX, pointY);
    }

    /**
     * set fit size for PPT，Word view mode, PDf
     *
     * @param value fit size mode
     *              = 0, fit size of get minimum value of pageWidth / viewWidth and pageHeight / viewHeight;
     *              = 1, fit size of pageWidth
     *              = 2, fit size of PageHeight
     */
    public void setFitSize(int value) {
        if (slideshow) {
            return;
        }
        pgPrintMode.setFitSize(value);
    }

    /**
     * get fit size statue
     *
     * @return fit size statue
     * = 0, left/right and top/bottom don't alignment
     * = 1, top/bottom alignment
     * = 2, left/right alignment
     * = 3, left/right and top/bottom alignment
     */
    public int getFitSizeState() {
        if (slideshow) {
            return 0;
        }
        return pgPrintMode.getFitSizeState();
    }

    /**
     * @return Returns the pageSize.
     */
    public Dimension getPageSize() {
        return pgModel.getPageSize();
    }

    /**
     * @return Returns the doc.
     */
    public IDocument getRenderersDoc() {
        return pgModel.getRenderersDoc();
    }

    /**
     *
     */
    public PGSlide getCurrentSlide() {
        if (slideshow) {
            return pgModel.getSlide(slideIndex_SlideShow);
        } else {
            return pgPrintMode.getCurrentPGSlide();
        }
    }

    /**
     * @param value
     * @return true: finded  false: not finded
     */
    public boolean find(String value) {
        if (!slideshow) {
            return pgFind.find(value);
        }
        return false;
    }

    /**
     * need call function find first and finded
     *
     * @return
     */
    public boolean findBackward() {
        if (!slideshow) {
            return pgFind.findBackward();
        }
        return false;
    }

    /**
     * need call function find first and finded
     *
     * @return
     */
    public boolean findForward() {
        if (!slideshow) {
            return pgFind.findForward();
        }
        return false;
    }

    /**
     *
     */
    public void resetSearchResult() {
    }

    /**
     *
     */
    public int getPageIndex() {
        return -1;
    }

    /**
     * get selected text
     *
     * @return
     */
    public String getSelectedText() {
        return editor.getHighlight().getSelectText();
    }

    /**
     *
     */
    public PGSlide getSlideMaster(int index) {
        return pgModel.getSlideMaster(index);
    }

    /**
     * @return Returns the pgEditor.
     */
    public PGEditor getEditor() {
        return editor;
    }

    /**
     * set animation duration(ms), should be called before begin slideshow
     *
     * @param duration
     */
    public void setAnimationDuration(int duration) {
        if (slideView == null) {
            slideView = new SlideShowView(this, currentSlide);
        }
        if (slideView != null) {
            slideView.setAnimationDuration(duration);
        }
    }

    /**
     * begin slideshow
     *
     * @param slideIndex(base 1)
     */
    public void beginSlideShow(int slideIndex) {
        synchronized (this) {
            if (slideIndex <= 0 || slideIndex > pgModel.getSlideCount()) {
                return;
            }
            if (eventManage == null) {
                eventManage = new PGEventManage(this, control);
            }
            boolean isChangedSlide = false;
            if (getCurrentIndex() + 1 != slideIndex) {
                isChangedSlide = true;
            }
            setOnTouchListener(eventManage);
            control.getSysKit().getCalloutManager().setDrawingMode(MainConstant.DRAWMODE_NORMAL);
            pgPrintMode.setVisibility(View.GONE);
            slideshow = true;
            processPageSize(getWidth(), getHeight());
            slideIndex_SlideShow = slideIndex - 1;
            currentSlide = pgModel.getSlide(slideIndex_SlideShow);
            if (slideView == null) {
                slideView = new SlideShowView(this, currentSlide);
            }
            slideView.initSlideShow(currentSlide, true);
            setBackgroundColor(Color.BLACK);
            if (callouts == null) {
                if (!control.getSysKit().getCalloutManager().isPathEmpty()) {
                    initCalloutView();
                }
            } else {
                callouts.setIndex(slideIndex_SlideShow);
            }
            postInvalidate();
            if (isChangedSlide && getControl().getMainFrame() != null) {
                getControl().getMainFrame().changePage();
            }
            // to picture
            post(new Runnable() {

                @Override
                public void run() {
                    initSlidebar();
                    control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                }
            });
        }
    }

    /**
     * @return
     */
    public boolean hasNextSlide_Slideshow() {
        synchronized (this) {
            if (slideshow) {
                return slideIndex_SlideShow < pgModel.getSlideCount() - 1;
            }
            return false;
        }
    }

    /**
     * @return
     */
    public boolean hasPreviousSlide_Slideshow() {
        synchronized (this) {
            if (slideshow) {
                return slideIndex_SlideShow >= 1;
            }
            return false;
        }
    }

    /**
     * has next action or not
     *
     * @return
     */
    public boolean hasNextAction_Slideshow() {
        synchronized (this) {
            if (slideshow) {
                if (!slideView.gotoNextSlide()                                   //has next action
                        || slideIndex_SlideShow < pgModel.getSlideCount() - 1)      //has next slide
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * has previous action or not
     *
     * @return
     */
    public boolean hasPreviousAction_Slideshow() {
        synchronized (this) {
            if (slideshow &&
                    (slideIndex_SlideShow >= 1            //has previous slide
                            || !slideView.gotopreviousSlide()))  //has previous action
            {
                return true;
            }
            return false;
        }
    }

    /**
     * show or hide shape one by one
     */
    public void slideShow(byte type) {
        synchronized (this) {
            if (!slideshow || !slideView.animationStoped()
                    || control.getSysKit().getCalloutManager().getDrawingMode() != MainConstant.DRAWMODE_NORMAL) {
                return;
            }
            if (type == ISlideShow.SlideShow_PreviousSlide && hasPreviousSlide_Slideshow()) {
                slideIndex_SlideShow = slideIndex_SlideShow - 1;
                if (slideIndex_SlideShow >= 0) {
                    slideView.initSlideShow(pgModel.getSlide(slideIndex_SlideShow), true);
                    if (getControl().getMainFrame() != null) {
                        getControl().getMainFrame().changePage();
                    }
                }
//                else
//                {
//                    slideIndex_SlideShow = 0;
//                    showTips("It's first slide now.");
//                    return;
//                }
            } else {
                if (slideView.isExitSlideShow()) {
                    control.getMainFrame().fullScreen(false);
                    endSlideShow();
                    return;
                }
                switch (type) {
                    case ISlideShow.SlideShow_PreviousStep:
                        if (hasPreviousAction_Slideshow()) {
                            if (slideView.gotopreviousSlide()) {
                                PGSlide slide = pgModel.getSlide(--slideIndex_SlideShow);
                                if (slide != null) {
                                    slideView.initSlideShow(slide, true);
                                    slideView.gotoLastAction();
                                }
//                                else
//                                {
//                                    slideIndex_SlideShow = 0;
//                                    showTips("It's first action of first slide now.");
//                                    return;
//                                }
                                if (getControl().getMainFrame() != null) {
                                    getControl().getMainFrame().changePage();
                                }
                            } else {
                                slideView.previousActionSlideShow();
                            }
                        }
                        break;
                    case ISlideShow.SlideShow_NextStep:
                        if (hasNextAction_Slideshow()) {
                            if (slideView.gotoNextSlide()) {
                                slideView.initSlideShow(pgModel.getSlide(++slideIndex_SlideShow), true);
                                if (getControl().getMainFrame() != null) {
                                    getControl().getMainFrame().changePage();
                                }
                            } else {
                                slideView.nextActionSlideShow();
                            }
                        }
                        break;
                    case ISlideShow.SlideShow_NextSlide:
                        if (hasNextSlide_Slideshow()) {
                            slideView.initSlideShow(pgModel.getSlide(++slideIndex_SlideShow), true);
                            if (getControl().getMainFrame() != null) {
                                getControl().getMainFrame().changePage();
                            }
                        }
                        break;
                }
            }
            if (callouts != null) {
                callouts.setIndex(slideIndex_SlideShow);
            }
            postInvalidate();
            // to picture
            post(new Runnable() {

                @Override
                public void run() {
                    if (control != null) {
                        control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                    }
                }
            });
        }
    }

    /**
     * slideshow end
     */
    public void endSlideShow() {
        synchronized (this) {
            if (slideshow) {
                control.getSysKit().getCalloutManager().setDrawingMode(MainConstant.DRAWMODE_NORMAL);
                setOnTouchListener(null);
                pgPrintMode.setVisibility(View.VISIBLE);
                Object bg = control.getMainFrame().getViewBackground();
                if (bg != null) {
                    if (bg instanceof Integer) {
                        setBackgroundColor((Integer) bg);
                    } else if (bg instanceof Drawable) {
                        setBackgroundDrawable((Drawable) bg);
                    }
                }
                currentIndex = slideIndex_SlideShow;
                slideshow = false;
                slideView.endSlideShow();
                showSlide(currentIndex, false);
                if (callouts != null) {
                    callouts.setVisibility(View.INVISIBLE);
                }
                // to picture
                post(new Runnable() {

                    @Override
                    public void run() {
                        ISlideShow iSlideshow = control.getSlideShow();
                        if (iSlideshow != null) {
                            iSlideshow.exit();
                        }
                        initSlidebar();
                    }
                });
            }
        }
    }

    /**
     * it's slideshowing or not.
     *
     * @return
     */
    public boolean isSlideShow() {
        return slideshow;
    }

    /**
     *
     */
    public PGFind getFind() {
        return this.pgFind;
    }

    /**
     * @return
     */
    public PGPrintMode getPrintMode() {
        return this.pgPrintMode;
    }

    public Rect getSlideDrawingRect() {
        if (slideshow) {
            if (slideSize == null) {
                slideSize = new Rect(slideView.getDrawingRect());
            } else {
                slideSize.set(slideView.getDrawingRect());
            }
            int w = slideSize.width();
            slideSize.set((mWidth - w) / 2, 0, (mWidth + w) / 2, mHeight);
            return slideSize;
        }
        return null;
    }

    public PGModel getPGModel() {
        return pgModel;
    }

    /**
     * animation steps for current slide
     *
     * @param slideIndex(based 1)
     * @return
     */
    public int getSlideAnimationSteps(int slideIndex) {
        synchronized (this) {
            List<ShapeAnimation> shapeAnimLst = pgModel.getSlide(slideIndex - 1).getSlideShowAnimation();
            if (shapeAnimLst != null) {
                return shapeAnimLst.size() + 1;
            } else {
                return 1;
            }
        }
    }

    /**
     * slideshow to image
     *
     * @param slideIndex slide index(base 1)
     * @param step       animation index(base 1)
     * @return
     */
    public Bitmap getSlideshowToImage(int slideIndex, int step) {
        synchronized (this) {
            if (slideView == null) {
                slideView = new SlideShowView(this, pgModel.getSlide(slideIndex - 1));
            }
            return slideView.getSlideshowToImage(pgModel.getSlide(slideIndex - 1), step);
        }
    }

    /**
     *
     */
    public void dispose() {
        control = null;
        currentSlide = null;
        if (slideView != null) {
            slideView.dispose();
            slideView = null;
        }
        if (eventManage != null) {
            eventManage.dispose();
            eventManage = null;
        }
        pgModel.dispose();
        pgModel = null;
        //layoutParams = null;
        if (pgFind != null) {
            pgFind.dispose();
            pgFind = null;
        }
    }

    // 
    private boolean isConfigurationChanged;
    //
    private boolean init;
    //
    private int preShowSlideIndex = -1;
    // 当前显示的slide index值
    private int currentIndex = -1;
    // 组件的宽度
    private int mWidth;
    // 组件的高度
    private int mHeight;
    //current zoom value
    private float zoom = 1F;
    //
    private PGFind pgFind;
    //
    //private ViewGroup.LayoutParams layoutParams;
    //
    private PGEditor editor;
    //  
    private IControl control;
    //
    private PGSlide currentSlide;
    // PG model 后期需修改
    private PGModel pgModel;
    //
    private SlideShowView slideView;
    //
    private PGEventManage eventManage;

    //
    private boolean slideshow;
    private int slideIndex_SlideShow;
    private float fitZoom = 1f;
    private Rect slideSize = null;

    /**
     *
     */
    private PGPrintMode pgPrintMode;
    private CalloutView callouts;
}
