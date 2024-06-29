/*
 * 文件名称:          Spreadsheet.java
 *
 * 编译器:            android2.2
 * 时间:              下午3:48:56
 */

package com.nvqquy98.lib.doc.office.ss.control;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Message;
import android.widget.LinearLayout;

import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.interfacePart.IReaderListener;
import com.nvqquy98.lib.doc.office.ss.util.ModelUtil;
import com.nvqquy98.lib.doc.office.ss.view.SheetView;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IFind;
import com.nvqquy98.lib.doc.office.system.ReaderHandler;
import com.nvqquy98.lib.doc.office.system.beans.AEventManage;
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.CalloutView;
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.IExportListener;

import java.io.File;

/**
 * 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-3
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class Spreadsheet extends LinearLayout implements IFind, IReaderListener, IExportListener {

    /**
     * @param context
     */
    public Spreadsheet(Context context, String filepath, Workbook book, IControl control, ExcelView parent) {
        super(context);
        this.parent = parent;
        fileName = filepath;
        setBackgroundColor(Color.WHITE);
        this.workbook = book;
        this.control = control;
        eventManage = new SSEventManage(this, control);
        this.editor = new SSEditor(this);
        setOnTouchListener(eventManage);
        setLongClickable(true);
    }

    public CalloutView getCalloutView() {
        return callouts;
    }

    public void initCalloutView() {
        if (callouts == null) {
            callouts = new CalloutView(this.getContext(), control, this);
            callouts.setIndex(currentSheetIndex);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.leftMargin = SSConstant.DEFAULT_ROW_HEADER_WIDTH;
            params.topMargin = SSConstant.DEFAULT_COLUMN_HEADER_HEIGHT;
            addView(callouts, params);
        }
    }

    @Override
    public void exportImage() {
        control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
    }

    /**
     * 初始化显示的sheet，默认第一第sheet
     */
    public void init() {
        //layoutParams = getLayoutParams();
        //file name
        int index = fileName.lastIndexOf(File.separator);
        if (index > 0) {
            fileName = fileName.substring(index + 1);
        }
        //set title name
        control.actionEvent(EventConstant.SS_SHEET_CHANGE,
                fileName + " : " + workbook.getSheet(0).getSheetName());
        if (sheetview == null) {
            sheetview = new SheetView(this, workbook.getSheet(0));
        }
        //initSheetbar();
        initFinish = true;
        short state = workbook.getSheet(0).getState();
        if (state != Sheet.State_Accomplished) {
            workbook.getSheet(0).setReaderListener(this);
            control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);
        }
        //if (state == Sheet.State_Accomplished)
        {
            // to picture
            post(new Runnable() {
                @Override
                public void run() {
                    control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                }
            });
        }
    }

    /**
     *
     */
    private void initSheetbar() {
        /*if (!isDefaultSheetBar)
        {
            this.sheetbarHeight = control.getMainFrame().getBottomBarHeight();
            int mHeight = ((View)getParent()).getHeight() - getTop();
            // 非指定高度才需要重高度
            if (layoutParams.height == LayoutParams.MATCH_PARENT
                || layoutParams.height == LayoutParams.FILL_PARENT)
            {
                mHeight -= sheetbarHeight;
                setLayoutParams(new LinearLayout.LayoutParams(layoutParams.width, mHeight));
            }
            return;
        }
        int maxWidth = layoutParams.width == LayoutParams.MATCH_PARENT
            || layoutParams.width == LayoutParams.FILL_PARENT ? getResources().getDisplayMetrics().widthPixels
                : layoutParams.width;
        bar = new SheetBar(getContext(), control, maxWidth);
        sheetbarHeight = bar.getSheetbarHeight();
        int mHeight = ((View)getParent()).getHeight() - getTop();
        // 非指定高度才需要重高度
        if (layoutParams.height == LayoutParams.MATCH_PARENT
            || layoutParams.height == LayoutParams.FILL_PARENT)
        {
            mHeight -= sheetbarHeight;
            setLayoutParams(new LinearLayout.LayoutParams(layoutParams.width, mHeight));
        }
        
        ((ViewGroup)getParent()).addView(bar, new LayoutParams(layoutParams.width == LayoutParams.MATCH_PARENT
            || layoutParams.width == LayoutParams.FILL_PARENT ? LayoutParams.WRAP_CONTENT : layoutParams.width, LayoutParams.WRAP_CONTENT));*/
    }

    /**
     *
     */
    public void removeSheetBar() {
        isDefaultSheetBar = false;
        //((ViewGroup)getParent()).removeView(bar);
    }

    /**
     * 得到sheet的个数
     */
    public int getSheetCount() {
        return workbook.getSheetCount();
    }

    /**
     * 显示指定的sheet
     *
     * @param sheetName 要显示的sheet名称
     */
    public void showSheet(String sheetName) {
        if (currentSheetName != null && currentSheetName.equals(sheetName)) {
            return;
        }
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            return;
        }
        currentSheetName = sheetName;
        currentSheetIndex = workbook.getSheetIndex(sheet);
        //change focused button
        /*if(isDefaultSheetBar)
        {
            //bar.setFocusSheetButton(currentSheetIndex);
        }
        else
        {
            control.getMainFrame().doActionEvent(EventConstant.SS_CHANGE_SHEET, currentSheetIndex);
        }*/
        showSheet(sheet);
    }

    /**
     * 显示指定的sheet
     *
     * @param sheetIndex 要显示的sheet名称
     */
    public void showSheet(int sheetIndex) {
        if (currentSheetIndex == sheetIndex
                || sheetIndex >= getSheetCount()) {
            return;
        }
        Sheet sheet = workbook.getSheet(sheetIndex);
        currentSheetIndex = sheetIndex;
        currentSheetName = sheet.getSheetName();
        //change focused button
        /*if(isDefaultSheetBar)
        {
            //bar.setFocusSheetButton(currentSheetIndex);
        }
        else
        {
            control.getMainFrame().doActionEvent(EventConstant.SS_CHANGE_SHEET, currentSheetIndex);
        }*/
        control.actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);
        if (callouts != null) {
            callouts.setIndex(currentSheetIndex);
        }
        showSheet(sheet);
    }

    /**
     * @param sheet
     */
    private void showSheet(Sheet sheet) {
        try {
            eventManage.stopFling();
            control.getMainFrame().setFindBackForwardState(false);
            control.actionEvent(EventConstant.SS_SHEET_CHANGE,
                    fileName + " : " + sheet.getSheetName());
            sheetview.changeSheet(sheet);
            postInvalidate();
            if (sheet.getState() != Sheet.State_Accomplished) {
                //current sheet has not finished parsing; 
                sheet.setReaderListener(this);
                control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);
                control.actionEvent(EventConstant.APP_ABORTREADING, null);
            } else {
                control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
            }
            //read not accomplished sheet in slide window
            ReaderHandler readerHandler = workbook.getReaderHandler();
            if (readerHandler != null) {
                Message msg = new Message();
                msg.what = MainConstant.HANDLER_MESSAGE_SUCCESS;
                msg.obj = currentSheetIndex;
                readerHandler.handleMessage(msg);
            }
        } catch (Exception e) {
            control.getSysKit().getErrorKit().writerLog(e);
        }
    }

    /**
     *
     */
    protected void onDraw(Canvas canvas) {
        if (!initFinish) {
            return;
        }
        try {
            sheetview.drawSheet(canvas);
            // auto test code
            if (control.isAutoTest()/* && sheetbar != null*/) {
                if (currentSheetIndex < workbook.getSheetCount() - 1) {
                    try {
                        while (sheetview.getCurrentSheet().getState() != Sheet.State_Accomplished) {
                            Thread.sleep(50);
                        }
                    } catch (Exception e) {
                    }
                    showSheet(currentSheetIndex + 1);
                } else {
                    control.actionEvent(EventConstant.SYS_AUTO_TEST_FINISH_ID, true);
                }
            } else {
                IOfficeToPicture otp = control.getOfficeToPicture();
                if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGING) {
                    toPicture(otp);
                }
            }
            if (sheetview.getCurrentSheet().getState() != Sheet.State_Accomplished) {
                invalidate();
            }
            if (preShowSheetIndex != currentSheetIndex) {
                control.getMainFrame().changePage();
                preShowSheetIndex = currentSheetIndex;
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
        boolean b = PictureKit.instance().isDrawPictrue();
        PictureKit.instance().setDrawPictrue(true);
        //
        Bitmap bitmap = otp.getBitmap(getWidth(), getHeight());
        if (bitmap == null) {
            return;
        }
        Canvas picCanvas = new Canvas(bitmap);
        float oldPaintZoom = sheetview.getZoom();
        if (bitmap.getWidth() != getWidth() || bitmap.getHeight() != getHeight()) {
            float zoom = Math.min((float) bitmap.getWidth() / getWidth(), (float) bitmap.getHeight() / getHeight()) * oldPaintZoom;
            sheetview.setZoom(zoom, true);
        }
        picCanvas.drawColor(Color.WHITE);
        sheetview.drawSheet(picCanvas);
        control.getSysKit().getCalloutManager().drawPath(picCanvas, currentSheetIndex, oldPaintZoom);
        otp.callBack(bitmap);
        sheetview.setZoom(oldPaintZoom, true);
        //
        PictureKit.instance().setDrawPictrue(b);
    }

    /**
     * @param destBitmap
     * @return
     */
    public Bitmap getSnapshot(Bitmap destBitmap) {
        if (destBitmap == null) {
            return null;
        }
        synchronized (sheetview) {
            Canvas picCanvas = new Canvas(destBitmap);
            float oldPaintZoom = sheetview.getZoom();
            if (destBitmap.getWidth() != getWidth() || destBitmap.getHeight() != getHeight()) {
                float zoom = Math.min((float) destBitmap.getWidth() / getWidth(), (float) destBitmap.getHeight() / getHeight()) * oldPaintZoom;
                sheetview.setZoom(zoom, true);
            }
            picCanvas.drawColor(Color.WHITE);
            sheetview.drawSheet(picCanvas);
            sheetview.setZoom(oldPaintZoom, true);
            return destBitmap;
        }
    }
    /**
     *
     */
    /**
     * get xls thumbnail
     *
     * @param width     thumbnail width when zoomValue is 100
     * @param height    thumbnail height when zoomValue is 100
     * @param zoomValue zoom value
     * @return
     */
    public Bitmap getThumbnail(int width, int height, float zoomValue) {
        Sheet sheet = workbook.getSheet(0);
        if (sheet == null || sheet.getState() != Sheet.State_Accomplished) {
            return null;
        }
        if (sheetview == null) {
            sheetview = new SheetView(this, workbook.getSheet(0));
        }
        return sheetview.getThumbnail(sheet, width, height, zoomValue);
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
        if (isConfigurationChanged) {
            isConfigurationChanged = false;
            // to picture
            post(new Runnable() {
                public void run() {
                    control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                }
            });
        }
    }

    /**
     * 计算流动的位置
     */
    public void computeScroll() {
        eventManage.computeScroll();
    }

    /**
     *
     */
    public IControl getControl() {
        return control;
    }

    /**
     * 得到sheetView视图
     */
    public SheetView getSheetView() {
        return sheetview;
    }

    /**
     * @return Returns the workbook.
     */
    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * @return
     */
    public String getActiveCellContent() {
        if (sheetview.getCurrentSheet().getActiveCell() != null) {
            return ModelUtil.instance().getFormatContents(
                    workbook, sheetview.getCurrentSheet().getActiveCell());
        }
        return "";
    }

    /**
     * active cell hyperlink address
     *
     * @return
     */
    public Hyperlink getActiveCellHyperlink() {
        Cell cell = sheetview.getCurrentSheet().getActiveCell();
        if (cell != null && cell.getHyperLink() != null) {
            return cell.getHyperLink();
        }
        return null;
    }

    /**
     * @param findValue
     * @return true: finded   false: not finded
     */
    public boolean find(String value) {
        return sheetview.find(value);
    }

    public boolean findBackward() {
        return sheetview.findBackward();
    }

    public boolean findForward() {
        return sheetview.findForward();
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
     *
     */
    public float getZoom() {
        if (sheetview == null) {
            sheetview = new SheetView(this, workbook.getSheet(0));
        }
        return sheetview.getZoom();
    }

    /**
     *
     */
    public void setZoom(float zoom) {
        if (sheetview == null) {
            sheetview = new SheetView(this, workbook.getSheet(0));
        }
        sheetview.setZoom(zoom); //zoom
    }
//    /**
//     * 
//     * @param zoom
//     * @param pointX
//     * @param pointY
//     */
//    public void setZoom(float zoom, float pointX, float pointY)
//    {
//    	if (sheetview == null)
//        {
//            sheetview = new SheetView(this, workbook.getSheet(0));
//        }
//        sheetview.setZoom(zoom, pointX, pointY); //zoom
//    }

    /**
     *
     */
    public float getFitZoom() {
        return 0.5f;
    }

    /**
     *
     */
    public AEventManage getEventManage() {
        return this.eventManage;
    }

    /**
     * this function be callde by sheet reader thread,
     * so we need to post to main thread to update UI
     * (non-Javadoc)
     *
     * @see com.nvqquy98.lib.doc.office.ss.model.interfacePart.IReaderListener#OnReadingFinished()
     */
    public void OnReadingFinished() {
        if (control != null && control.getMainFrame().getActivity() != null) {
            post(new Runnable() {
                public void run() {
                    Sheet sheet = workbook.getSheet(currentSheetIndex);
                    control.actionEvent(EventConstant.SS_SHEET_CHANGE,
                            fileName + " : " + sheet.getSheetName());
                    control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
                    control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                    postInvalidate();
                }
            });
        }
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * abort current sheet drawing
     */
    public void abortDrawing() {
        abortDrawing = true;
    }

    /**
     *
     */
    public void startDrawing() {
        abortDrawing = false;
    }

    /**
     * @return
     */
    public boolean isAbortDrawing() {
        return abortDrawing;
    }

    /**
     *
     */
    public int getCurrentSheetNumber() {
        return this.currentSheetIndex + 1;
    }

    /**
     * get sheet bar height
     *
     * @return
     */
    public int getBottomBarHeight() {
        return parent.getBottomBarHeight();
    }

    /**
     *
     */
    public IWord getEditor() {
        return this.editor;
    }

    /**
     *
     */
    public void dispose() {
        parent = null;
        fileName = null;
        control = null;
        workbook = null;
        if (sheetview != null) {
            sheetview.dispose();
            sheetview = null;
        }
        if (eventManage != null) {
            eventManage.dispose();
            eventManage = null;
        }
        if (editor != null) {
            editor.dispose();
            editor = null;
        }
        /*if (bar != null)
        {
            bar.dispose();
            bar = null;
        }*/
    }

    private ExcelView parent;
    //
    private boolean isConfigurationChanged;
    //
    private boolean isDefaultSheetBar = true;
    //abort current sheet drawing or not
    private boolean abortDrawing;
    //
    private boolean initFinish;
    //
    private int preShowSheetIndex = -1;
    //
    private int currentSheetIndex;
    private String currentSheetName;
    //
    private int sheetbarHeight;
    //file name
    private String fileName;
    //
    private IControl control;
    // excel model 后期需修改
    private Workbook workbook;
    // 当前Sheet
    private SheetView sheetview;
    // 事件管理器
    private SSEventManage eventManage;
    //
    private SSEditor editor;
    //
    private CalloutView callouts;
    //private ViewGroup.LayoutParams layoutParams;
    //
    //private SheetBar bar;
}
