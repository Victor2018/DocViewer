/*
 * 文件名称:          PDFFind.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:38:43
 */
package com.cherry.lib.doc.office.pdf;

import com.cherry.lib.doc.office.common.ICustomDialog;
import com.cherry.lib.doc.office.constant.PDFConstant;
import com.cherry.lib.doc.office.macro.DialogListener;
import com.cherry.lib.doc.office.simpletext.control.SafeAsyncTask;
import com.cherry.lib.doc.office.system.IFind;
import com.cherry.lib.doc.office.system.beans.pagelist.APageListItem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * find content
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
public class PDFFind implements IFind
{

    /**
     * 
     * @param word
     */
    public PDFFind(PDFView pdfView)
    {
        this.pdfView = pdfView;
        toast = Toast.makeText(pdfView.getContext(), "", Toast.LENGTH_SHORT);
        paint = new Paint();
        paint.setColor(PDFConstant.HIGHLIGHT_COLOR);
    }
    
    /**
     * 
     *
     */
    public boolean find(String value)
    {
        if (value == null)
        {
            return false;
        }
        isStartSearch = true;
        this.query = value;
        pageIndex = pdfView.getCurrentPageNumber() - 1;
        search(1);
        return true;
    }
    
    /**
     * 
     *
     */
    public boolean findBackward()
    {
        if (query == null)
        {
            return false;
        }
        isStartSearch = false;
        if (pageIndex == 0)
        {
            toast.setText(pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_BEGIN"));
            toast.show();
            return false;
        }
        pageIndex--;
        search(-1);
        return true;
    }

    /**
     * 
     *
     */
    public boolean findForward()
    {
        if (query == null)
        {
            return false;
        }
        isStartSearch = false;
        if (pageIndex + 1 >= pdfView.getPageCount())
        {
            toast.setText(pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_END"));
            toast.show();
            return false;
        }
        pageIndex++;
        search(1);
        return true;
    }
    
    /**
     * search of assign content
     *  
     * @param direction    = 1  forward
     *                     = -1  backward   
     * @return
     */
    private void search(final int direction)
    {        
        if (safeSearchTask != null)
        {
            safeSearchTask.cancel(true);
            safeSearchTask = null;
        }
        isSetPointToVisible = false;
        searchResult = null;
        isCancel = false;
        final int searchCount = direction > 0 ? pdfView.getPageCount() - pageIndex : pageIndex;
        final boolean showFindDlg = pdfView.getControl().getMainFrame().isShowFindDlg();
        
        final ProgressDialog progressDialog = new ProgressDialog(pdfView.getControl().getActivity());        
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(pdfView.getControl().getMainFrame().getLocalString("DIALOG_PDF_SEARCHING"));
        progressDialog.setMax(searchCount);
        progressDialog.setOnKeyListener(new OnKeyListener()
        {   
            @ Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    isCancel = true;
                    if (safeSearchTask != null)
                    {
                        safeSearchTask.cancel(true);
                        safeSearchTask = null;
                    }
                }
                return true;
            }
        });
        
        safeSearchTask = new SafeAsyncTask<Void, Integer, RectF[]>()
        {
            /**
             *
             */
            protected RectF[] doInBackground(Void...params)
            {
                try
                {
                    int index = 1;
                    while (0 <= pageIndex && pageIndex < pdfView.getPageCount() && !isCancelled())
                    {
                        publishProgress(index++);
                        RectF searchHits[] = pdfView.getPDFLib().searchContentSync(pageIndex, query);
                        if (searchHits != null && searchHits.length > 0)
                        {
                            return searchHits;
                        }
                        pageIndex += direction;
                    }
                }
                catch (Exception e)
                {
                    return null;
                }
                return null;
            }
           
            
            @ Override
            protected void onCancelled()
            {
                super.onCancelled();
                if(showFindDlg)
                {
                    progressDialog.cancel();
                }
                else
                {
                    ICustomDialog dlgListener = pdfView.getControl().getCustomDialog();
                    if(dlgListener != null)
                    {
                        dlgListener.dismissDialog(DialogListener.DIALOGTYPE_FIND);
                    }
                }
            }

            @ Override
            protected void onProgressUpdate(Integer...values)
            {
                super.onProgressUpdate(values);
                if(showFindDlg)
                {
                    progressDialog.setProgress(values[0]);
                }
            }

            @ Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                if(showFindDlg)
                {
                    pdfView.postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            if (!isCancel)
                            {
                                progressDialog.show();
                                progressDialog.setProgress(1);
                            }
                        }
                    }, 0);
                }
                else
                {
                    ICustomDialog dlgListener = pdfView.getControl().getCustomDialog();
                    if(dlgListener != null)
                    {
                        dlgListener.showDialog(DialogListener.DIALOGTYPE_FIND);
                    }
                }
                
            }

            /**
             *
             *
             */
            protected void onPostExecute(RectF[] result)
            {
                if(showFindDlg)
                {
                    progressDialog.cancel();
                }
                else
                {
                    ICustomDialog dlgListener = pdfView.getControl().getCustomDialog();
                    if(dlgListener != null)
                    {
                        dlgListener.dismissDialog(DialogListener.DIALOGTYPE_LOADING);
                    }
                }                
                if (result != null)
                {
                    searchResult = result;
                    // Ask the ReaderView to move to the resulting page
                    if (pdfView.getCurrentPageNumber() - 1 != pageIndex)
                    {
                        pdfView.getListView().showPDFPageForIndex(pageIndex);
                        isSetPointToVisible = true;
                    }
                    else 
                    {
                        if (pdfView.getListView().isPointVisibleOnScreen((int)searchResult[0].left, (int)searchResult[0].top))
                        {
                            pdfView.invalidate();    
                        }
                        else
                        {
                            pdfView.getListView().setItemPointVisibleOnScreen((int)searchResult[0].left, (int)searchResult[0].top);
                        }
                        
                    }
                }
                else if (showFindDlg)
                {
                    String str = "";
                    if (isStartSearch)
                    {
                        pdfView.getControl().getMainFrame().setFindBackForwardState(false);
                        str = pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_NOT_FOUND");
                    }
                    else if (direction > 0)
                    {
                        str = pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_END");
                    }
                    else if (direction < 0)
                    {
                        str = pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_BEGIN");
                    }
                    if (str != null && str.length() > 0)
                    {
                        toast.setText(str);
                        toast.show();
                    }
                }
            }
        };  
        safeSearchTask.safeExecute(null);
    }
    
    
    /**
     * 
     */
    public void drawHighlight(Canvas canvas, int dx, int dy, APageListItem item)
    {
        if (this.pageIndex == item.getPageIndex())
        {
            float scale = item.getWidth() / (float)item.getPageWidth();
            if (searchResult != null && searchResult.length > 0)
            {
                for (RectF rect : searchResult)
                {
                    canvas.drawRect(rect.left * scale + dx * scale, rect.top * scale + dy * scale,
                        rect.right * scale  + dx * scale, rect.bottom * scale + dy * scale, paint);
                }
            }
        }
    }
    
    /**
     * 
     */
    protected RectF[] getSearchResult()
    {
        return this.searchResult;
    }
    
    /**
     * 
     */
    public void resetSearchResult()
    {
        this.searchResult = null;
    }
    
    /**
     * 
     */
    public int getPageIndex()
    {
        return this.pageIndex;
    }
    
    
    /**
     * @return Returns the isSetPointToVisible.
     */
    public boolean isSetPointToVisible()
    {
        return isSetPointToVisible;
    }

    /**
     * @param isSetPointToVisible The isSetPointToVisible to set.
     */
    public void setSetPointToVisible(boolean isSetPointToVisible)
    {
        this.isSetPointToVisible = isSetPointToVisible;
    }

    /**
     * 
     */
    public void dispose()
    {
        pdfView = null;
        toast = null;
    }
    
    protected Paint paint;
    //toast
    protected Toast toast;
    //
    private boolean isSetPointToVisible;
    //
    private boolean isStartSearch;
    //
    private boolean isCancel;
    //
    private int pageIndex;
    //
    private String query; 
    //
    private PDFView pdfView;
    //
    private RectF[] searchResult;
    //
    private SafeAsyncTask safeSearchTask;
    //
}
