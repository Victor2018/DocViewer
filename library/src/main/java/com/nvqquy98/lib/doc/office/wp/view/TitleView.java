/*
 * 文件名称:          TitleView.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:18:40
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.control.IWord;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.AbstractView;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * header、footer view
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-7-4
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class TitleView extends AbstractView
{

    /**
     * 
     * @param elem
     */
    public TitleView(IElement elem)
    {
        super();
        this.elem = elem;
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPViewConstant.TITLE_VIEW;
    }
    
    /**
     * 得到组件
     */
    public IWord getContainer()
    {
        if (pageRoot != null)
        {
            return pageRoot.getContainer();
        }
        return null;
    }
    
    /**
     * 得到组件
     */
    public IControl getControl()
    {
        if (pageRoot != null)
        {
            return pageRoot.getControl();
        }
        return null;
    }

    /**
     * 得到model
     */
    public IDocument getDocument()
    {
        if (pageRoot != null)
        {
            return pageRoot.getDocument();
        }
        return null;
    }
    /**
     * 
     */
    public void setPageRoot(IView root)
    {
        pageRoot = root;
    }
    
    public void dispose()
    {
        super.dispose();
        pageRoot = null;
    }
    
    /**
     * 
     */
    private IView pageRoot;
}
