/*
 * 文件名称:          SSEventManage.java
 *
 * 编译器:            android2.2
 * 时间:              下午1:43:24
 */

package com.nvqquy98.lib.doc.office.wp.control;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.beans.AEventManage;

/**
 * SS的事件管理，包括触摸、手型等事件
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-9
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class WPEventManage extends AEventManage {
    /**
     * @param spreadsheet
     */
    public WPEventManage(Word word, IControl control) {
        super(word.getContext(), control);
        this.word = word;
    }

    /**
     * 触摸事件
     */
    public boolean onTouch(View v, MotionEvent event) {
        try {
            super.onTouch(v, event);
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    PictureKit.instance().setDrawPictrue(true);
                    processDown(v, event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (zoomChange) {
                        zoomChange = false;
                        if (word.getCurrentRootType() == WPViewConstant.PAGE_ROOT) {
                            control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                        }
                        if (control.getMainFrame().isZoomAfterLayoutForWord()) {
                            control.actionEvent(EventConstant.WP_LAYOUT_NORMAL_VIEW, null);
                        }
                    }
                    word.getControl().actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            //ErrorUtil.instance().writerLog(e);
            Log.d(TAG, "onTouch: " + e.getMessage());
            control.getSysKit().getErrorKit().writerLog(e);
        }
        return false;
    }

    private static final String TAG = "WPEventManage";

    /**
     * process touch event of down
     *
     * @param v
     * @param event
     */
    protected void processDown(View v, MotionEvent event) {
        int x = convertCoorForX(event.getX());
        int y = convertCoorForY(event.getY());
        long offset = word.viewToModel(x, y, false);
        if (word.getHighlight().isSelectText()) {
            word.getHighlight().removeHighlight();
            word.getStatus().setPressOffset(offset);
            word.postInvalidate();
        }
    }

    /**
     *
     */
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (word.getStatus().isSelectTextStatus()) {
            return true;
        }
        super.onScroll(e1, e2, distanceX, distanceY);
        // 向右
        boolean change = false;
        boolean isScrollX = Math.abs(distanceX) > Math.abs(distanceY);
        Rectangle r = word.getVisibleRect();
        int sX = r.x;
        int sY = r.y;
        float zoom = word.getZoom();
        int wW = 0;
        if (word.getCurrentRootType() == WPViewConstant.NORMAL_ROOT
                && control.getMainFrame().isZoomAfterLayoutForWord()) {
            if (word.getWidth() == word.getWordWidth()) {
                wW = word.getWidth();
            } else {
                wW = (int) (word.getWordWidth() * zoom);
            }
        } else {
            wW = (int) (word.getWordWidth() * zoom);
        }
        int wH = (int) (word.getWordHeight() * zoom);
        // X方向
        if (isScrollX) {
            // 向右
            if (distanceX > 0 && sX + r.width < wW) {
                sX += distanceX;
                if (sX + r.width > wW) {
                    sX = wW - r.width;
                }
                change = true;
            }
            // 向左
            else if (distanceX < 0 && sX > 0) {
                sX += distanceX;
                if (sX < 0) {
                    sX = 0;
                }
                change = true;
            }
        }
        // Y方向
        else {
            // 向下
            if (distanceY > 0 && sY + r.height < wH) {
                sY += distanceY;
                if (sY + r.height > wH) {
                    sY = wH - r.height;
                }
                change = true;
            }
            // 向上
            else if (distanceY < 0 && sY > 0) {
                sY += distanceY;
                if (sY < 0) {
                    sY = 0;
                }
                change = true;
            }
        }
        if (change) {
            isScroll = true;
            word.scrollTo(sX, sY);
        }
        return true;
    }

    /**
     * Fling the scroll view
     *
     * @param velocityX X方向速率
     * @param velocityY Y方向速率
     */
    public void fling(int velocityX, int velocityY) {
        super.fling(velocityX, velocityY);
        Rectangle r = ((Word) word).getVisibleRect();
        float zoom = word.getZoom();
        // Y方向滚动
        oldY = 0;
        oldX = 0;
        int wW = 0;
        if (word.getCurrentRootType() == WPViewConstant.NORMAL_ROOT
                && control.getMainFrame().isZoomAfterLayoutForWord()) {
            if (word.getWidth() == word.getWordWidth()) {
                wW = word.getWidth();
            } else {
                wW = (int) (word.getWordWidth() * zoom) + 5;
            }
        } else {
            wW = (int) (word.getWordWidth() * zoom);
        }
        if (Math.abs(velocityY) > Math.abs(velocityX)) {
            oldY = r.y;
            mScroller.fling(r.x, r.y, 0, velocityY, 0, r.x, 0, (int) (word.getWordHeight() * zoom) - r.height);
        }
        // X方向流动
        else {
            oldX = r.x;
            mScroller.fling(r.x, r.y, velocityX, 0, 0, wW - r.width, r.y, 0);
        }
        word.postInvalidate();
        //
    }

    /**
     *
     */
    public boolean onDoubleTapEvent(MotionEvent e) {
        /*if (e.getAction() == MotionEvent.ACTION_UP)
        {
            int x = convertCoorForX(e.getX());
            int y = convertCoorForY(e.getY());
            IView view = WPViewKit.instance().getView((Word)word, x, y, WPViewConstant.PARAGRAPH_VIEW, false);
            if (view != null)
            {
                word.getHighlight().addHighlight(view.getStartOffset(null), view.getEndOffset(null));
                word.postInvalidate();
                word.getControl().actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);
            }
        }*/
        super.onDoubleTapEvent(e);
        return true;
    }

    /**
     *
     */
    public boolean onSingleTapUp(MotionEvent e) {
        super.onSingleTapUp(e);
        if (e.getAction() == MotionEvent.ACTION_UP) {
            int x = convertCoorForX(e.getX());
            int y = convertCoorForY(e.getY());
            long offset = word.viewToModel(x, y, false);
            if (offset >= 0) {
                IElement leaf = word.getDocument().getLeaf(offset);
                if (leaf != null) {
                    int hyID = AttrManage.instance().getHperlinkID(leaf.getAttribute());
                    if (hyID >= 0) {
                        Hyperlink hylink = control.getSysKit().getHyperlinkManage().getHyperlink(hyID);
                        if (hylink != null) {
                            control.actionEvent(EventConstant.APP_HYPERLINK, hylink);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     */
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            isFling = true;
            PictureKit.instance().setDrawPictrue(false);
            int sX = mScroller.getCurrX();
            int sY = mScroller.getCurrY();
            if ((oldX == sX && oldY == sY)
                    || (sX == word.getScrollX() && sY == word.getScrollY())) {
                PictureKit.instance().setDrawPictrue(true);
                mScroller.abortAnimation();
                word.postInvalidate();
                return;
            }
            oldX = sX;
            oldY = sY;
            word.scrollTo(sX, sY);
        } else {
            if (!PictureKit.instance().isDrawPictrue()) {
                PictureKit.instance().setDrawPictrue(true);
                word.postInvalidate();
            }
        }
    }

    /**
     * @return
     */
    protected int convertCoorForX(float x) {
        return (int) ((x + word.getScrollX()) / word.getZoom());
    }

    /**
     * @return
     */
    protected int convertCoorForY(float y) {
        return (int) ((y + word.getScrollY()) / word.getZoom());
    }

    /**
     *
     */
    public void dispose() {
        super.dispose();
        word = null;
    }

    //
    private int oldX;
    //
    private int oldY;
    // word
    protected Word word;
}
