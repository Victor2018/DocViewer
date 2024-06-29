/*
 * 文件名称:          TextBox.java
 *  
 * 编译器:            android2.2
 * 时间:              下午7:55:23
 */
package com.nvqquy98.lib.doc.office.common.shape;

import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.simpletext.view.STRoot;

/**
 * text box shape
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class TextBox extends AbstractShape
{
	//Meta Characters type
	public static final byte MC_None = 0;
	public static final byte MC_SlideNumber = 1;
	public static final byte MC_DateTime = 2;
	public static final byte MC_GenericDate = 3;
	public static final byte MC_Footer = 4;
	public static final byte MC_RTFDateTime = 5;
   
    public TextBox()
    {
        setPlaceHolderID(-1);
    }
    
    public short getType()
    {
        return SHAPE_TEXTBOX;
    }
    
    /**
     * get element of this text box
     */
    public SectionElement getElement()
    {
        return elem;
    }
    
    /**
     * set element of this text box
     */
    public void setElement(SectionElement section)
    {
        this.elem = section;
    }
    
    
    /**
     * set data of this shape
     */
    public void setData(Object data)
    {
        if (data instanceof SectionElement)
        {
            this.elem =  (SectionElement)data;    
        }
    }    
    
    /**
     * 
     */
    public Object getData()
    {
        return elem;
    }
    
    /**
     * @return Returns the isEditor.
     */
    public boolean isEditor()
    {
        return isEditor;
    }

    /**
     * @param isEditor The isEditor to set.
     */
    public void setEditor(boolean isEditor)
    {
        this.isEditor = isEditor;
    }

    /**
     * @return Returns the rootView.
     */
    public STRoot getRootView()
    {
        return rootView;
    }

    /**
     * @param rootView The rootView to set.
     */
    public void setRootView(STRoot rootView)
    {
        this.rootView = rootView;
    }

    /**
     * @return Returns the isWrapLine.
     */
    public boolean isWrapLine()
    {
        return isWrapLine;
    }

    /**
     * @param isWrapLine The isWrapLine to set.
     */
    public void setWrapLine(boolean isWrapLine)
    {
        this.isWrapLine = isWrapLine;
    }
    
    /**
     * 
     * @return
     */
    public byte getMCType()
    {
        return mcType;
    }

    /**
     * 
     * @param mcType
     */
    public void setMCType(byte mcType)
    {
        this.mcType = mcType;
    }

    public  void setWordArt(boolean isWordArt)
    {
    	this.isWordArt = isWordArt;
    }
    
    public boolean isWordArt()
    {
    	return isWordArt;
    }
    
    /**
     * dispose
     */
    public void dispose()
    {
        super.dispose();
        if (elem != null)
        {
            elem.dispose();
            elem = null;
        }
    }
    
    private boolean isWrapLine;
    //
    private boolean isEditor;
    //
    private SectionElement elem;
    //
    private STRoot rootView;
    private byte mcType = MC_None;
    private boolean isWordArt;
}
