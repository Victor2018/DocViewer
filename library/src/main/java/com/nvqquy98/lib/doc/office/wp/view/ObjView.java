/*
 * 文件名称:          ObjView.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:45:54
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.common.BackgroundDrawer;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.common.shape.PictureShape;
import com.nvqquy98.lib.doc.office.common.shape.WPAutoShape;
import com.nvqquy98.lib.doc.office.common.shape.WPPictureShape;
import com.nvqquy98.lib.doc.office.common.shape.WatermarkShape;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ViewKit;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Embedded picture view
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-4-5
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ObjView extends LeafView
{
    /**
     * 
     */
    public ObjView()
    {
        
    }
    /**
     * 
     * @param paraElem
     * @param elem
     */
    public ObjView(IElement paraElem, IElement elem, WPAutoShape shape)
    {
        super(paraElem, elem);
        this.picShape = shape;
    }
    
	/**
     * 
     */
    public short getType()
    {
        return WPViewConstant.OBJ_VIEW;
    }
    
    
    /**
     * 初始化leaf属性
     */
    public void initProperty(IElement elem, IElement paraElem)
    {
        this.elem = elem;
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20);
    }
    
    /**
     * 视图布局
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public int doLayout(DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr, int x, int y, int w, int h, long maxEnd, int flag)
    {
    	this.pageAttr = pageAttr;
    	
        isInline = docAttr.rootType == WPViewConstant.NORMAL_ROOT 
            || (picShape.getWrap() != WPAutoShape.WRAP_TOP && picShape.getWrap() != WPAutoShape.WRAP_BOTTOM);
        
        if (picShape.isWatermarkShape())
        {
            isInline = false;
        }
        else if(WPViewKit.instance().getArea(start + 1) == WPModelConstant.HEADER 
        		|| WPViewKit.instance().getArea(start + 1) == WPModelConstant.FOOTER)
        {
        	isInline = true;
        }
        int width = 0;
        Rectangle r = picShape.getBounds();
        if (isInline)
        {
            width = r.width;            
            setSize(width, r.height);  
        }
        else if(!picShape.isWatermarkShape())
        {
        	PositionLayoutKit.instance().processShapePosition(this, picShape, pageAttr);
        }
        setEndOffset(start + 1);
        boolean keepOne = ViewKit.instance().getBitValue(flag, WPViewConstant.LAYOUT_FLAG_KEEPONE);
        int breakType = WPViewConstant.BREAK_NO;
        if (keepOne)
        {
            return breakType;
        }
        if (width > w)
        {
            breakType = WPViewConstant.BREAK_LIMIT;
        }
        return breakType;
    }
    
    /**
     * 得到指定结束位置字符宽度
     * 
     * @param maxEnd
     * @return
     */
    public float getTextWidth()
    {
    	if(picShape.isWatermarkShape())
    	{
    		return picShape.getBounds().width;
    	}
    	else
    	{
    		return isInline ? (int)((WPPictureShape)picShape).getPictureShape().getBounds().getWidth() : 0;
    	}        
    }
    
    /**
     * 
     * @param canvas
     * @param x
     * @param y
     * @param zoom
     */
    public synchronized void draw(Canvas canvas, int originX, int originY, float zoom)
    {
        if (isInline)
        {
            IControl control = getControl();
            int left = Math.round((x * zoom) + originX);
        	int top = Math.round((y * zoom) + originY);
        	int right = Math.round((x * zoom) + originX + getWidth() * zoom);
        	int bottom = Math.round((y * zoom) + originY + getHeight() * zoom);
        	
        	rect.set(left, top, right, bottom);

        	if(!picShape.isWatermarkShape())
        	{
        		BackgroundDrawer.drawLineAndFill(canvas, control, getPageNumber(), ((WPPictureShape)picShape).getPictureShape(), rect, zoom);
            	
                PictureKit.instance().drawPicture(canvas, control, getPageNumber(), ((WPPictureShape)picShape).getPictureShape().getPicture(getControl()),
                  left, top, zoom, getWidth() * zoom, getHeight() * zoom, ((WPPictureShape)picShape).getPictureShape().getPictureEffectInfor());
        	}        	
        }
        
    }
    
    /**
     * 
     * @param canvas
     * @param x
     * @param y
     * @param zoom
     */
    public synchronized void drawForWrap(Canvas canvas, int originX, int originY, float zoom)
    {   
        int dX = (int)(x * zoom) + originX;
        int dY = (int)(y * zoom) + originY;
        Rectangle r = picShape.getBounds();      
        IControl control = getControl();
        
        int left = Math.round((x * zoom) + originX);
    	int top = Math.round((y * zoom) + originY);
    	int right = (int)Math.round((x * zoom) + originX + r.getWidth() * zoom);
    	int bottom = (int)Math.round((y * zoom) + originY + r.getHeight() * zoom);
    	
    	rect.set(left, top, right, bottom);
    	
    	if(picShape.isWatermarkShape())
    	{
    		int mainBodyWidth = pageAttr.pageWidth - pageAttr.leftMargin - pageAttr.rightMargin;
            int mainBodyHeight = pageAttr.pageHeight - pageAttr.topMargin - pageAttr.bottomMargin;
            
            float centerX = originX + (pageAttr.leftMargin + mainBodyWidth / 2f) * zoom;
            float centerY = originY + (pageAttr.topMargin + mainBodyHeight / 2f) * zoom;
            
            left = Math.round(centerX - r.width * zoom / 2f);
            top = Math.round(centerY - r.height * zoom / 2f);
            PictureKit.instance().drawPicture(canvas, control, getPageNumber(), 
            		PictureShape.getPicture(control, ((WatermarkShape)picShape).getPictureIndex()),
            		left, 
                    top, 
                    zoom, 
                    Math.round(r.getWidth() * zoom), 
                    Math.round(r.getHeight() * zoom), 
                    ((WatermarkShape)picShape).getEffectInfor());
    	}
    	else
    	{
    		BackgroundDrawer.drawLineAndFill(canvas, control, getPageNumber(), ((WPPictureShape)picShape).getPictureShape(), rect, zoom);
            
            PictureKit.instance().drawPicture(canvas, control, getPageNumber(), 
            		((WPPictureShape)picShape).getPictureShape().getPicture(getControl()),
                left, 
                top, 
                zoom, 
                Math.round(r.getWidth() * zoom), 
                Math.round(r.getHeight() * zoom), 
                ((WPPictureShape)picShape).getPictureShape().getPictureEffectInfor());
    	}    	
    }
    
    /**
     * model到视图
     * @param offset 指定的offset
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public Rectangle modelToView(long offset, Rectangle rect, boolean isBack)
    {
        rect.x += getX();
        rect.y += getY();
        return rect;
    }    
    
    /**
     * @param x
     * @param y
     * @param isBack 是否向后取，是为在视图上，上一行的结束位置与下一行开始位置相同
     */
    public long viewToModel(int x, int y, boolean isBack)
    {
        return start;
    }
    
    public boolean isBehindDoc()
    {
        return picShape.getWrap() == WPAutoShape.WRAP_BOTTOM;
    }
    
    /**
     * 得到基线
     */
    public int getBaseline()
    {
    	if(!picShape.isWatermarkShape())
    	{    		
    		return isInline ? (int)((WPPictureShape)picShape).getPictureShape().getBounds().getHeight() : 0;
    	}
    	
    	return 0;
    }
    
    /**
     * 
     */
    public boolean isInline()
    {
        return isInline;
    }
    /**
     * 放回对象池
     */
    public void free()
    {
        //ViewFactory.objView.free(this);
    }
   
    /**
     * 
     */
    public void dispose()
    {
        super.dispose();
        picShape = null;      
    }
    
    private PageAttr pageAttr;
    // 字符属性
    private WPAutoShape picShape;
    //
    private Rect rect = new Rect();
    //
    private boolean isInline;
}
