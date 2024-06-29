/*
 * 文件名称:          WPRead.java
 *
 * 编译器:            android2.2
 * 时间:              下午2:11:03
 */

package com.nvqquy98.lib.doc.office.wp.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.animate.FadeAnimation;
import com.nvqquy98.lib.doc.office.simpletext.control.Highlight;
import com.nvqquy98.lib.doc.office.simpletext.control.IHighlight;
import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IDialogAction;
import com.nvqquy98.lib.doc.office.system.SysKit;
import com.nvqquy98.lib.doc.office.system.beans.pagelist.APageListView;
import com.nvqquy98.lib.doc.office.wp.view.LayoutKit;
import com.nvqquy98.lib.doc.office.wp.view.NormalRoot;
import com.nvqquy98.lib.doc.office.wp.view.PageRoot;
import com.nvqquy98.lib.doc.office.wp.view.PageView;
import com.nvqquy98.lib.doc.office.wp.view.WPViewKit;

public class Word extends LinearLayout implements IWord {

    /**
     * @param context
     * @param attrs
     */
    public Word(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     */
    public Word(Context context, IDocument doc, String filePath, IControl control) {
        super(context);
        this.control = control;
        this.doc = doc;
        int defaultMode = control.getMainFrame().getWordDefaultView();
        setCurrentRootType(defaultMode);
        if (defaultMode == WPViewConstant.NORMAL_ROOT) {
            normalRoot = new NormalRoot(this);
        } else if (defaultMode == WPViewConstant.PAGE_ROOT) {
            pageRoot = new PageRoot(this);
        } else if (defaultMode == WPViewConstant.PRINT_ROOT) {
            pageRoot = new PageRoot(this);
            printWord = new PrintWord(context, control, pageRoot);
            addView(printWord);
        }
        dialogAction = new WPDialogAction(control);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(24);
        visibleRect = new Rectangle();
        initManage();
        if (defaultMode == WPViewConstant.PRINT_ROOT) {
            setOnClickListener(null);
        }
    }

    /**
     *
     */
    private void initManage() {
        //
        eventManage = new WPEventManage(this, control);
        setOnTouchListener(eventManage);
        setLongClickable(true);
        //
        wpFind = new WPFind(this);
        //
        status = new StatusManage();
        //
        highlight = new Highlight(this);
    }

    /**
     * 初始化
     */
    public void init() {
        if (normalRoot != null) {
            normalRoot.doLayout(0, 0, mWidth, mHeight, Integer.MAX_VALUE, 0);
        } else {
            pageRoot.doLayout(0, 0, mWidth, mHeight, Integer.MAX_VALUE, 0);
        }
        initFinish = true;
        if (printWord != null) {
            printWord.init();
        }
        if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            return;
        }
        // to picture
        post(new Runnable() {

            @Override
            public void run() {
                control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
            }
        });
    }

    /**
     *
     */
    public void onDraw(Canvas canvas) {
        if (!initFinish || currentRootType == WPViewConstant.PRINT_ROOT) {
            return;
        }
        try {
            if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
                pageRoot.draw(canvas, 0, 0, zoom);
                drawPageNubmer(canvas, zoom);
            } else if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
                normalRoot.draw(canvas, 0, 0, normalZoom);
            }
            // to picture
            IOfficeToPicture otp = control.getOfficeToPicture();
            if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGING) {
                toPicture(otp);
            }
        } catch (Exception e) {
            control.getSysKit().getErrorKit().writerLog(e);
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
        if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            WPPageListItem item = (WPPageListItem) printWord.getListView().getCurrentPageView();
            item.addRepaintImageView(null);
            return;
        }
        boolean b = PictureKit.instance().isDrawPictrue();
        PictureKit.instance().setDrawPictrue(true);
        Bitmap bitmap = otp.getBitmap(getWidth(), getHeight());
        if (bitmap == null) {
            return;
        }
        float paintZoom = getZoom();
        float tX = -getScrollX();
        float tY = -getScrollY();
        if (bitmap.getWidth() != getWidth() || bitmap.getHeight() != getHeight()) {
            float newZoom = Math.min((float) bitmap.getWidth() / getWidth(),
                    (float) bitmap.getHeight() / getHeight())
                    * getZoom();
            float pageWidth = pageRoot != null ? pageRoot.getChildView().getWidth() * newZoom : 0;
            float x = 0;
            if (pageWidth > bitmap.getWidth() || getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
                //x = (int)(pageWidth - getWidth()) / 2;
                x = getScrollX() / paintZoom * newZoom;
                x = Math.min(x, getWordWidth() * newZoom - bitmap.getWidth());
            }
            float y = getScrollY() / paintZoom * newZoom;
            y = Math.min(y, getWordHeight() * newZoom - getHeight());
            tX = -Math.max(0, x);
            tY = -Math.max(0, y);
            paintZoom = newZoom;
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(tX, tY);
        canvas.drawColor(Color.GRAY);
        if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
            pageRoot.draw(canvas, 0, 0, paintZoom);
        } else if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            normalRoot.draw(canvas, 0, 0, paintZoom);
        }
        otp.callBack(bitmap);
        PictureKit.instance().setDrawPictrue(b);
    }

    /**
     * @return
     */
    public Bitmap getSnapshot(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        if (getCurrentRootType() == WPViewConstant.PRINT_ROOT && printWord != null) {
            return printWord.getSnapshot(bitmap);
        }
        boolean b = PictureKit.instance().isDrawPictrue();
        PictureKit.instance().setDrawPictrue(true);
        float paintZoom = getZoom();
        float tX = -getScrollX();
        float tY = -getScrollY();
        if (bitmap.getWidth() != getWidth() || bitmap.getHeight() != getHeight()) {
            float newZoom = Math.min((float) bitmap.getWidth() / getWidth(),
                    (float) bitmap.getHeight() / getHeight())
                    * getZoom();
            float pageWidth = pageRoot != null ? pageRoot.getChildView().getWidth() * newZoom : 0;
            float x = 0;
            if (pageWidth > bitmap.getWidth() || getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
                //x = (int)(pageWidth - getWidth()) / 2;
                x = getScrollX() / paintZoom * newZoom;
                x = Math.min(x, getWordWidth() * newZoom - bitmap.getWidth());
            }
            float y = getScrollY() / paintZoom * newZoom;
            y = Math.min(y, getWordHeight() * newZoom - getHeight());
            tX = -Math.max(0, x);
            tY = -Math.max(0, y);
            paintZoom = newZoom;
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(tX, tY);
        canvas.drawColor(Color.GRAY);
        if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
            pageRoot.draw(canvas, 0, 0, paintZoom);
        } else if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            normalRoot.draw(canvas, 0, 0, paintZoom);
        }
        PictureKit.instance().setDrawPictrue(b);
        return bitmap;
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
        if (!initFinish) {
            return;
        }
        eventManage.stopFling();
        LayoutKit.instance().layoutAllPage(pageRoot, zoom);
        if (currentRootType == WPViewConstant.PAGE_ROOT) {
            Rectangle r = getVisibleRect();
            int sX = r.x;
            int sY = r.y;
            int wW = (int) (getWordWidth() * zoom);
            int wH = (int) (getWordHeight() * zoom);
            if (r.x + r.width > wW) {
                sX = wW - r.width;
            }
            if (r.y + r.height > wH) {
                sY = wH - r.height;
            }
            if (sX != r.x || sY != r.y) {
                scrollTo(Math.max(0, sX), Math.max(0, sY));
            }
        }
        if (w != oldw && control.getMainFrame().isZoomAfterLayoutForWord()) {
            layoutNormal();
            setExportImageAfterZoom(true);
        }
        post(new Runnable() {

            @Override
            public void run() {
                control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
            }
        });
    }

    /**
     *
     */
    public void layoutNormal() {
        if (normalRoot != null) {
            normalRoot.stopBackLayout();
            post(new Runnable() {
                public void run() {
                    if (currentRootType == WPViewConstant.NORMAL_ROOT) {
                        scrollTo(0, getScrollY());
                    }
                    normalRoot.layoutAll();
                    postInvalidate();
                }
            });
        }
    }

    /**
     *
     */
    public void layoutPrintMode() {
        post(new Runnable() {

            @Override
            public void run() {
                if (currentRootType == WPViewConstant.PRINT_ROOT
                        && printWord != null) {
                    APageListView listView = printWord.getListView();
                    if (listView != null && listView.getChildCount() == 1) {
                        listView.requestLayout();
                    }
                }
            }
        });
    }

    /**
     *
     */
    public void computeScroll() {
        if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            return;
        }
        eventManage.computeScroll();
    }

    /**
     *
     */
    public void switchView(int rootType) {
        if (rootType == getCurrentRootType()) {
            return;
        }
        eventManage.stopFling();
        setCurrentRootType(rootType);
        PictureKit.instance().setDrawPictrue(true);
        if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            if (normalRoot == null) {
                normalRoot = new NormalRoot(this);
                normalRoot.doLayout(0, 0, mWidth, mHeight, Integer.MAX_VALUE, 0);
            }
            setOnTouchListener(eventManage);
            if (printWord != null) {
                printWord.setVisibility(INVISIBLE);
            }
        } else if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
            if (pageRoot == null) {
                pageRoot = new PageRoot(this);
                pageRoot.doLayout(0, 0, mWidth, mHeight, Integer.MAX_VALUE, 0);
            } else {
                LayoutKit.instance().layoutAllPage(pageRoot, zoom);
            }
            setOnTouchListener(eventManage);
            if (printWord != null) {
                printWord.setVisibility(INVISIBLE);
            }
        } else if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            if (pageRoot == null) {
                pageRoot = new PageRoot(this);
                pageRoot.doLayout(0, 0, mWidth, mHeight, Integer.MAX_VALUE, 0);
            }
            if (printWord == null) {
                printWord = new PrintWord(getContext(), control, pageRoot);
                //print view background
                Object bg = control.getMainFrame().getViewBackground();
                ;
                if (bg != null) {
                    if (bg instanceof Integer) {
                        printWord.setBackgroundColor((Integer) bg);
                    } else if (bg instanceof Drawable) {
                        printWord.setBackgroundDrawable((Drawable) bg);
                    }
                }
                addView(printWord);
                post(new Runnable() {
                    public void run() {
                        printWord.init();
                        printWord.postInvalidate();
                    }
                });
            } else {
                printWord.setVisibility(VISIBLE);
            }
            scrollTo(0, 0);
            setOnClickListener(null);
            return;
        }
        post(new Runnable() {

            @Override
            public void run() {
                scrollTo(0, getScrollY());
                postInvalidate();
            }
        });
    }

    /**
     *
     */
    public Rectangle getVisibleRect() {
        visibleRect.x = getScrollX();
        visibleRect.y = getScrollY();
        visibleRect.width = this.getWidth();
        visibleRect.height = this.getHeight();
        return visibleRect;
    }

    /**
     * @param zoom The zoom to set.
     */
    public void setZoom(float zoom, int pointX, int pointY) {
        float oldZoom = 1.0f;
        if (currentRootType == WPViewConstant.PAGE_ROOT) {
            oldZoom = this.zoom;
            this.zoom = zoom;
            LayoutKit.instance().layoutAllPage(pageRoot, zoom);
        } else if (currentRootType == WPViewConstant.PRINT_ROOT) {
            printWord.setZoom(zoom, pointX, pointY);
            return;
        } else if (currentRootType == WPViewConstant.NORMAL_ROOT) {
            oldZoom = this.normalZoom;
            this.normalZoom = zoom;
        }
        scrollToFocusXY(zoom, oldZoom, pointX, pointY);
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
        if (currentRootType == WPViewConstant.PRINT_ROOT) {
            printWord.setFitSize(value);
        }
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
        if (currentRootType == WPViewConstant.PRINT_ROOT) {
            return printWord.getFitSizeState();
        }
        return 0;
    }

    /**
     * @param newScale
     * @param oldScale
     * @param focusScreenX
     * @param focusScreenY
     */
    private void scrollToFocusXY(float newScale, float oldScale, int focusScreenX, int focusScreenY) {
        if (focusScreenX == Integer.MIN_VALUE && focusScreenY == Integer.MIN_VALUE) {
            focusScreenX = getWidth() / 2;
            focusScreenY = getHeight() / 2;
        }
        float viewpageWidth = 0;
        float viewpageHeight = 0;
        if (getCurrentRootType() == WPViewConstant.PAGE_ROOT
                && pageRoot != null && pageRoot.getChildView() != null) {
            viewpageWidth = pageRoot.getChildView().getWidth();
            viewpageHeight = pageRoot.getChildView().getHeight();
        } else {
            viewpageWidth = getWidth();
            viewpageHeight = getHeight();
        }
        int lastpageHeight = (int) (viewpageHeight * (oldScale));
        float ratioY = 1.0f * (getScrollY() + focusScreenY) / lastpageHeight;
        int lastpageWidth = (int) (viewpageWidth * (oldScale));
        float ratioX = 1.0f * (getScrollX() + focusScreenX) / lastpageWidth;
        //do scroll by, so that the view scale from the center of pointers
        int pageHeight = (int) (viewpageHeight * (newScale));
        int pageWidth = (int) (viewpageWidth * (newScale));
        scrollBy((int) ((pageWidth - lastpageWidth) * ratioX),
                (int) ((pageHeight - lastpageHeight) * ratioY));
    }

    /**
     *
     */
    public void scrollTo(int x, int y) {
        x = Math.min(Math.max(x, 0), (int) (getWordWidth() * getZoom() - getWidth()));
        y = Math.min(Math.max(y, 0), (int) (getWordHeight() * getZoom() - getHeight()));
        super.scrollTo(Math.max(x, 0), Math.max(y, 0));
    }

    /**
     *
     */
    public int getCurrentPageNumber() {
        if (currentRootType == WPViewConstant.NORMAL_ROOT || pageRoot == null) {
            return 1;
        }
        if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            return printWord.getCurrentPageNumber();
        }
        PageView pv = WPViewKit.instance().getPageView(pageRoot, (int) (getScrollX() / zoom),
                (int) (getScrollY() / zoom) + getHeight() / 3);
        if (pv == null) {
            return 1;
        }
        return pv.getPageNumber();
    }

    /**
     * get page size
     *
     * @param pageIndex based on 1
     * @return
     */
    public Rectangle getPageSize(int pageIndex) {
        if (pageRoot == null || currentRootType == WPViewConstant.NORMAL_ROOT) {
            return new Rectangle(0, 0, getWidth(), getHeight());
        }
        if (pageIndex < 0 || pageIndex > pageRoot.getChildCount()) {
            return null;
        }
        PageView pv = WPViewKit.instance().getPageView(pageRoot, (int) (getScrollX() / zoom),
                (int) (getScrollY() / zoom) + getHeight() / 5);
        if (pv == null) {
            IAttributeSet attr = doc.getSection(0).getAttribute();
            int pageWidth = (int) (AttrManage.instance().getPageWidth(attr) * MainConstant.TWIPS_TO_PIXEL);
            int pageHeight = (int) (AttrManage.instance().getPageHeight(attr) * MainConstant.TWIPS_TO_PIXEL);
            return new Rectangle(0, 0, pageWidth, pageHeight);
        }
        return new Rectangle(0, 0, pv.getWidth(), pv.getHeight());
    }

    /**
     * 绘制页信息
     *
     * @param canvas
     * @param zoom
     */
    private void drawPageNubmer(Canvas canvas, float zoom) {
        int currentNumber = getCurrentPageNumber();
        if (control.getMainFrame().isDrawPageNumber() && pageRoot != null) {
            Rect rect = canvas.getClipBounds();
            if (rect.width() != getWidth()
                    || rect.height() != getHeight()) {
                return;
            }
            String pn = String.valueOf(currentNumber) + " / "
                    + String.valueOf(pageRoot.getPageCount());
            int w = (int) paint.measureText(pn);
            int h = (int) (paint.descent() - paint.ascent());
            int x = (int) ((rect.right + getScrollX() - w) / 2);
            int y = (int) ((rect.bottom - h) - 50);
            Drawable drawable = SysKit.getPageNubmerDrawable();
            drawable.setBounds((int) (x - 20), y - 10, x + w + 20, y + h + 10);
            drawable.draw(canvas);
            y -= paint.ascent();
            canvas.drawText(pn, x, y, paint);
        }
        if (preShowPageIndex != currentNumber
                || prePageCount != getPageCount()) {
            control.getMainFrame().changePage();
            preShowPageIndex = currentNumber;
            prePageCount = getPageCount();
        }
    }

    /**
     * @param x 为100%的值
     * @param y 为100%的值
     */
    public long viewToModel(int x, int y, boolean isBack) {
        if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
            return pageRoot.viewToModel(x, y, isBack);
        } else if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            return normalRoot.viewToModel(x, y, isBack);
        } else if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            return printWord.viewToModel(x, y, isBack);
        }
        return 0;
    }

    /**
     *
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack) {
        if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
            return pageRoot.modelToView(offset, rect, isBack);
        } else if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            return normalRoot.modelToView(offset, rect, isBack);
        } else if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            return printWord.modelToView(offset, rect, isBack);
        }
        return rect;
    }

    /**
     *
     */
    public IView getRoot(int rootType) {
        if (rootType == WPViewConstant.PAGE_ROOT) {
            return pageRoot;
        } else if (rootType == WPViewConstant.NORMAL_ROOT) {
            return normalRoot;
        }
        return null;
    }

    /**
     *
     */
    public String getText(long start, long end) {
        return doc.getText(start, end);
    }

    /**
     * @return Returns the dialogAction.
     */
    public IDialogAction getDialogAction() {
        return dialogAction;
    }

    /**
     *
     */
    public WPFind getFind() {
        return wpFind;
    }

    /**
     * @return Returns the filePath.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     *
     */
    public IHighlight getHighlight() {
        return highlight;
    }

    /**
     *
     */
    public IDocument getDocument() {
        return doc;
    }

    /**
     *
     */
    public IControl getControl() {
        return control;
    }

    /**
     * @return Returns the status.
     */
    public StatusManage getStatus() {
        return status;
    }

    /**
     *
     */
    public WPEventManage getEventManage() {
        return eventManage;
    }

    /**
     *
     */
    public void setWordWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    /**
     *
     */
    public void setWordHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    /**
     *
     */
    public void setSize(int w, int h) {
        mWidth = w;
        mHeight = h;
    }

    /**
     *
     */
    public int getWordHeight() {
        if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
            return mHeight;
        } else if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            return normalRoot.getHeight();
        }
        return getHeight();
    }

    /**
     *
     */
    public int getWordWidth() {
        if (getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
            return mWidth;
        } else if (getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            return normalRoot.getWidth();
        }
        return getWidth();
    }

    /**
     * switch page for page index (base 0)
     *
     * @param index page index
     */
    protected void showPage(int index, int direction) {
        if (index < 0 || index >= getPageCount()
                || getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            return;
        }
        if (getCurrentRootType() == WPViewConstant.PRINT_ROOT) {
            if (direction == EventConstant.APP_PAGE_UP_ID) {
                printWord.previousPageview();
            } else if (direction == EventConstant.APP_PAGE_DOWN_ID) {
                printWord.nextPageView();
            } else {
                printWord.showPDFPageForIndex(index);
            }
            return;
        }
        IView view = pageRoot.getPageView(index);
        if (view != null) {
            this.scrollTo(getScrollX(), (int) (view.getY() * zoom));
        }
    }

    /**
     * page to image for page number (base 1)
     *
     * @return bitmap raw data
     */
    public Bitmap pageToImage(int pageNumber) {
        if (pageNumber <= 0 || pageNumber > getPageCount()
                || pageRoot == null || pageRoot.getChildView() == null
                || getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            return null;
        }
        IView view = pageRoot.getPageView(pageNumber - 1);
        if (view == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(-view.getX(), -view.getY());
        canvas.drawColor(Color.WHITE);
        ((PageView) view).draw(canvas, 0, 0, 1);
        return bitmap;
    }

    /**
     * specific area of page to image. if area is not completely contained in the page, return null
     *
     * @param pageNumber page number
     * @return
     */
    public Bitmap pageAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        if (pageNumber <= 0 || pageNumber > getPageCount()
                || pageRoot == null || pageRoot.getChildView() == null
                || getCurrentRootType() == WPViewConstant.NORMAL_ROOT) {
            return null;
        }
        IView view = pageRoot.getPageView(pageNumber - 1);
        if (view != null && SysKit.isValidateRect(view.getWidth(), view.getHeight(), srcLeft, srcTop, srcWidth, srcHeight)) {
            boolean b = PictureKit.instance().isDrawPictrue();
            PictureKit.instance().setDrawPictrue(true);
            float paintZoom = Math.min(desWidth / (float) srcWidth, desHeight / (float) srcHeight);
            Bitmap bitmap = null;
            try {
                bitmap = Bitmap.createBitmap((int) (srcWidth * paintZoom), (int) (srcHeight * paintZoom), Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                return null;
            }
            if (bitmap == null) {
                return null;
            }
            float tX = -(srcLeft + view.getX()) * paintZoom;
            float tY = -(srcTop + view.getY()) * paintZoom;
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(tX, tY);
            canvas.drawColor(Color.WHITE);
            ((PageView) view).draw(canvas, 0, 0, paintZoom);
            PictureKit.instance().setDrawPictrue(b);
            return bitmap;
        }
        return null;
    }

    /**
     * @param zoom
     * @return
     */
    public Bitmap getThumbnail(float zoom) {
        Rectangle size = getPageSize(1);
        if (size != null) {
            int thumbnailWidth = Math.round(size.width * zoom);
            int thumbnailHeight = Math.round(size.height * zoom);
            return pageAreaToImage(1, 0, 0, size.width, size.height, thumbnailWidth, thumbnailHeight);
        }
        return null;
    }

    /**
     *
     */
    public int getPageCount() {
        if (currentRootType == WPViewConstant.NORMAL_ROOT || pageRoot == null) {
            return 1;
        }
        return pageRoot.getPageCount();
    }

    /**
     * @return Returns the currentRootType.
     */
    public int getCurrentRootType() {
        return currentRootType;
    }

    /**
     * @param currentRootType The currentRootType to set.
     */
    public void setCurrentRootType(int currentRootType) {
        this.currentRootType = currentRootType;
    }

    /**
     * @return Returns the zoom.
     */
    public float getZoom() {
        if (currentRootType == WPViewConstant.NORMAL_ROOT) {
            return normalZoom;
        } else if (currentRootType == WPViewConstant.PAGE_ROOT) {
            return zoom;
        } else if (currentRootType == WPViewConstant.PRINT_ROOT) {
            if (printWord != null) {
                return printWord.getZoom();
            } else {
                return zoom;
            }
        }
        return zoom;
    }

    /**
     *
     */
    public float getFitZoom() {
        if (currentRootType == WPViewConstant.NORMAL_ROOT) {
            return 0.5f;
        }
        if (pageRoot == null) {
            return 1.f;
        }
        float z = 1.f;
        // print mode
        if (currentRootType == WPViewConstant.PRINT_ROOT) {
            return printWord.getFitZoom();
        }
        // page mode
        else if (currentRootType == WPViewConstant.PAGE_ROOT) {
            IView view = pageRoot.getChildView();
            int pageWidth = view == null ? 0 : view.getWidth();
            if (pageWidth == 0) {
                pageWidth = (int) (AttrManage.instance().getPageWidth(doc.getSection(0).getAttribute()) * MainConstant.TWIPS_TO_PIXEL);
            }
            int viewWidth = getWidth();
            if (viewWidth == 0) {
                viewWidth = ((View) getParent()).getWidth();
            }
            z = (float) (viewWidth - WPViewConstant.PAGE_SPACE) / pageWidth;
        }
        return Math.max(z, 1.0f);
    }

    /**
     *
     */
    public byte getEditType() {
        return MainConstant.APPLICATION_TYPE_PPT;
    }

    /**
     * @return Returns the isExportImageAfterZoom.
     */
    public boolean isExportImageAfterZoom() {
        return isExportImageAfterZoom;
    }

    /**
     * @param isExportImageAfterZoom The isExportImageAfterZoom to set.
     */
    public void setExportImageAfterZoom(boolean isExportImageAfterZoom) {
        this.isExportImageAfterZoom = isExportImageAfterZoom;
    }

    /**
     * @return
     */
    public FadeAnimation getParagraphAnimation(int pargraphID) {
        return null;
    }

    /**
     *
     */
    public IShape getTextBox() {
        return null;
    }

    /**
     *
     */
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (printWord != null) {
            printWord.setBackgroundColor(color);
        }
    }

    /**
     *
     */
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (printWord != null) {
            printWord.setBackgroundResource(resid);
        }
    }

    /**
     *
     */
    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);
        if (printWord != null) {
            printWord.setBackgroundDrawable(d);
        }
    }

    /**
     *
     */
    public PrintWord getPrintWord() {
        return printWord;
    }

    /**
     * update total pages after layout completed
     */
    public void updateFieldText() {
        if (pageRoot != null && pageRoot.checkUpdateHeaderFooterFieldText()) {
            control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
        }
    }

    /**
     *
     */
    public void dispose() {
        control = null;
        if (status != null) {
            status.dispose();
            status = null;
        }
        if (highlight != null) {
            highlight.dispose();
            highlight = null;
        }
        if (eventManage != null) {
            eventManage.dispose();
            eventManage = null;
        }
        if (pageRoot != null) {
            pageRoot.dispose();
            pageRoot = null;
        }
        if (normalRoot != null) {
            normalRoot.dispose();
            normalRoot = null;
        }
        if (dialogAction != null) {
            dialogAction.dispose();
            dialogAction = null;
        }
        if (wpFind != null) {
            wpFind.dispose();
            wpFind = null;
        }
        if (doc != null) {
            doc.dispose();
            doc = null;
        }
        if (printWord != null) {
            printWord.dispose();
        }
        setOnClickListener(null);
        doc = null;
        paint = null;
        visibleRect = null;
    }

    private int preShowPageIndex = -1;
    //
    private int prePageCount = -1;
    //
    private boolean isExportImageAfterZoom;
    //
    private boolean initFinish;
    // 当前显示的root类型
    private int currentRootType;
    //
    protected int mWidth;
    //
    protected int mHeight;
    //
    protected float zoom = 1.f;
    //
    private float normalZoom = 1.f;
    //
    protected IControl control;
    //
    protected IDocument doc;
    // status
    protected StatusManage status;
    //
    protected IHighlight highlight;
    // 事件管理
    protected WPEventManage eventManage;
    // 文件路径
    private String filePath;
    //
    private IDialogAction dialogAction;
    //
    private PageRoot pageRoot;
    //
    private NormalRoot normalRoot;
    //
    private PrintWord printWord;
    // 绘制器
    private Paint paint;
    //
    private WPFind wpFind;
    //
    private Rectangle visibleRect;
}
