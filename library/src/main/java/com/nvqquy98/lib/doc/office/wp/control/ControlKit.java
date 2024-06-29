/*
 * 文件名称:          ControlKit.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:27:06
 */
package com.nvqquy98.lib.doc.office.wp.control;

import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.wp.view.PageRoot;
import com.nvqquy98.lib.doc.office.wp.view.PageView;

/**
 * word application control kit class
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-3-6
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ControlKit
{
    private static ControlKit kit = new ControlKit();
    //
    public static ControlKit instance()
    {
        return kit;
    }
    
    /**
     * Internet search
     * 
     * @param word 
     */
    public void internetSearch(Word word)
    {
        IDocument doc = word.getDocument();
        long start = word.getHighlight().getSelectStart();
        long end = word.getHighlight().getSelectEnd();
        String str = "";
        if (start != end)
        {
            str = doc.getText(start, end);
        }
        word.getControl().getSysKit().internetSearch(str, word.getControl().getMainFrame().getActivity());
    }
    
    /**
     * 
     */
    public void gotoOffset(Word word, long offset)
    {        
        Rectangle rect = new Rectangle();
        if (word.getCurrentRootType() == WPViewConstant.PRINT_ROOT)
        {
            IView root = word.getRoot(WPViewConstant.PAGE_ROOT);
            boolean invalidate = true;
            if (root != null && root.getType() == WPViewConstant.PAGE_ROOT)
            {
                IView pv = ((PageRoot)root).getViewContainer().getParagraph(offset, false);
                while (pv != null && pv.getType() != WPViewConstant.PAGE_VIEW)
                {
                    pv = pv.getParentView();
                }
                if (pv != null)
                {
                    int pageIndex = ((PageView)pv).getPageNumber() - 1;
                    if (pageIndex != word.getCurrentPageNumber() - 1)
                    {
                        word.showPage(pageIndex, -1);
                        invalidate = false;
                    }
                    else
                    {
                        rect.setBounds(0, 0, 0, 0);
                        word.modelToView(offset, rect, false);
                        rect.x -= pv.getX();
                        rect.y -= pv.getY();
                        if (!word.getPrintWord().getListView().isPointVisibleOnScreen(rect.x, rect.y))
                        {
                            word.getPrintWord().getListView().setItemPointVisibleOnScreen(rect.x, rect.y);
                            invalidate = false;
                        }
                        else
                        {
                            word.getPrintWord().exportImage(word.getPrintWord().getListView().getCurrentPageView(), null);
                        }
                    }
                }
            }
            if (invalidate)
            {
                word.postInvalidate();
            }
            return;
        }
        //
        rect.setBounds(0, 0, 0, 0);
        word.modelToView(offset, rect, false);
        Rectangle vRect = word.getVisibleRect();
        float zoom = word.getZoom();
        int x = (int)(rect.x * zoom);
        int y = (int)(rect.y * zoom);
        if (!vRect.contains(x, y))
        {   
            if (x + vRect.width > word.getWordWidth() * zoom)
            {
                x = (int)(word.getWordWidth() * zoom) - vRect.width;
            }
            if (y + vRect.height > word.getWordHeight() * zoom)
            {
                y = (int)(word.getWordHeight() * zoom) - vRect.height;
            }
            word.scrollTo(x, y);
        }
        else
        {
            word.postInvalidate();
        }
        //
        word.getControl().actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);
        //
        if (word.getCurrentRootType() != WPViewConstant.PRINT_ROOT)
        {
            word.getControl().actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
        }
    }

}
