/*
 * 文件名称:          ShapeView.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:09:58
 */
package com.nvqquy98.lib.doc.office.ss.view;

import com.nvqquy98.lib.doc.office.common.BackgroundDrawer;
import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.common.autoshape.AutoShapeKit;
import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.common.picture.PictureKit;
import com.nvqquy98.lib.doc.office.common.shape.AChart;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.GroupShape;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.common.shape.PictureShape;
import com.nvqquy98.lib.doc.office.common.shape.SmartArt;
import com.nvqquy98.lib.doc.office.common.shape.TextBox;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.STDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.simpletext.view.STRoot;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
/**
 * draw  shapes of sheet
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-2
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ShapeView
{
    
    /**
     * 
     * @param sheetView
     */
    public ShapeView(SheetView sheetView)
    {
        this.sheetView = sheetView;
        
        //init temp rect
        shapeRect = new Rect();
        temRect = new Rect();
    }
    
    /**
     * 
     * @param shapePostion
     * @return
     */
    public void panzoomViewRect(Rectangle shapePostion, IShape parent)
    {
        float zoom = sheetView.getZoom();
        if(parent != null && parent instanceof SmartArt)
        {
            shapeRect.left = Math.round((shapePostion.x) * zoom) ;
            shapeRect.right = Math.round((shapePostion.x + shapePostion.width ) * zoom);
            shapeRect.top = Math.round((shapePostion.y) * zoom);
            shapeRect.bottom = Math.round((shapePostion.y + shapePostion.height) * zoom);            
        }
        else
        {            
            int x = sheetView.getRowHeaderWidth();
            int y = sheetView.getColumnHeaderHeight(); 
            float scrollX = sheetView.getScrollX();
            float scrollY = sheetView.getScrollY();
            
            shapeRect.left = x + Math.round((shapePostion.x - scrollX) * zoom) ;
            shapeRect.right = x + Math.round((shapePostion.x + shapePostion.width - scrollX) * zoom);
            shapeRect.top = y + Math.round((shapePostion.y - scrollY) * zoom);
            shapeRect.bottom = y + Math.round((shapePostion.y + shapePostion.height - scrollY) * zoom);
        }
        

        //same  to tempRect
        temRect.set(shapeRect.left, shapeRect.top, shapeRect.right, shapeRect.bottom);
        
    } 
    
    /**
     * draw shapes of current sheet
     * @param canvas
     */
    public void draw(Canvas canvas)
    {
        Rect clip = canvas.getClipBounds(); 
        clip.left = sheetView.getRowHeaderWidth();
        clip.top = sheetView.getColumnHeaderHeight();
 
        int cnt = sheetView.getCurrentSheet().getShapeCount();
		IControl control = sheetView.getSpreadsheet().getControl();
        for(int i = 0; i < cnt && !sheetView.getSpreadsheet().isAbortDrawing(); i++)
        {
            IShape shape = sheetView.getCurrentSheet().getShape(i);
            drawShape(canvas, clip, control, null, shape);
        }        
    }    
    

    
    /**
     * 
     * @param canvas
     * @param clip
     * @param control
     * @param parent
     * @param shape
     */
    private void drawShape(Canvas canvas, Rect clip, IControl control, IShape parent, IShape shape)
    {
        canvas.save();
        
        Rectangle bounds = shape.getBounds();
        
        //chart sheet
        if(bounds == null && shape.getType() == AbstractShape.SHAPE_CHART)
        {
            DisplayMetrics display = sheetView.getSpreadsheet().getControl().getMainFrame()
                .getActivity().getResources().getDisplayMetrics();
            int width = Math.max(display.widthPixels, display.heightPixels);
            int height = Math.min(display.widthPixels, display.heightPixels);
            bounds = new Rectangle(0 , 0, (int)Math.round(width), (int)Math.round(height));
            shape.setBounds(bounds);
        }
        
        //shape rect
        panzoomViewRect(bounds, parent);
        
        if(!temRect.intersect(clip) && parent == null)
        {
            return;
        }
        if(shape instanceof GroupShape)
        {
            //flip vertical
            if (shape.getFlipVertical())
            {
                canvas.translate(shapeRect.left, shapeRect.bottom);
                canvas.scale(1, -1);
                canvas.translate(-shapeRect.left, -shapeRect.top);
            }
            //flip horizontal
            if (shape.getFlipHorizontal())
            {
                canvas.translate(shapeRect.right, shapeRect.top);
                canvas.scale(-1, 1);
                canvas.translate(-shapeRect.left, -shapeRect.top);
            }
            
            IShape[] shapes = ((GroupShape)shape).getShapes();
            for (int i = 0; i < shapes.length; i++)
            {
                IShape childShape = shapes[i];
                if(shape.isHidden())
                {
                    continue;
                } 
                drawShape(canvas, clip, control, shape, childShape);
            }
        }
        else
        {
            switch(shape.getType())
            {
                case AbstractShape.SHAPE_PICTURE:
                	PictureShape pictureShape = (PictureShape)shape;
                    processRotation(canvas, pictureShape, shapeRect);
                    
                    BackgroundDrawer.drawLineAndFill(canvas, control, sheetView.getSheetIndex(), pictureShape, shapeRect, sheetView.getZoom());
                    
                    Picture pic = control.getSysKit().getPictureManage().getPicture(((PictureShape)shape).getPictureIndex());
                    PictureKit.instance().drawPicture(canvas, sheetView.getSpreadsheet().getControl(), sheetView.getSheetIndex(), pic, shapeRect.left, shapeRect.top,
                    		sheetView.getZoom(), shapeRect.width(), shapeRect.height(), ((PictureShape)shape).getPictureEffectInfor());
                    break;
                    
                case AbstractShape.SHAPE_TEXTBOX:
                    drawTextbox(canvas, shapeRect, (TextBox)shape);
                    break;
                    
                case AbstractShape.SHAPE_CHART:
                    AChart achart = (AChart)shape;  
                    if (achart.getAChart() != null)
                    {
                        processRotation(canvas, shape, shapeRect);
                        achart.getAChart().setZoomRate(sheetView.getZoom());//PictureKit.WMFZOOM
                        achart.getAChart().draw(canvas, control, shapeRect.left, shapeRect.top, shapeRect.width(), shapeRect.height(), PaintKit.instance().getPaint());
//                        PictureKit.instance().drawPicture(canvas, control, 
//                            control.getSysKit().getPictureManage().getPicture(achart.getDrawingPicture(control)),
//                            shapeRect.left, shapeRect.top , sheetView.getZoom(), shapeRect.width(), shapeRect.height(), null);
                    }
                    break;
                    
                case AbstractShape.SHAPE_LINE:
                case AbstractShape.SHAPE_AUTOSHAPE:
                    AutoShapeKit.instance().drawAutoShape(canvas, control, sheetView.getSheetIndex(), (AutoShape)shape, shapeRect, sheetView.getZoom());
                    break;
                    
                case AbstractShape.SHAPE_SMARTART:
                	SmartArt smartArt = (SmartArt)shape;
                	BackgroundDrawer.drawLineAndFill(canvas, control, sheetView.getSheetIndex(), smartArt, shapeRect, sheetView.getZoom());
                	
                    canvas.translate(shapeRect.left, shapeRect.top);
                    
                    
                  IShape[] shapes = smartArt.getShapes();
                  for(IShape item : shapes)
                  {
                      drawShape(canvas, clip, control, smartArt, item);
                  }

                    break;
            } 
        }
        
        canvas.restore();
    }
   /**
    * 
    * @param canvas
    * @param shapeRect
    * @param textboxData
    */
    private void drawTextbox(Canvas canvas, Rect shapeRect, TextBox textbox)
    {
        SectionElement elem = textbox.getElement();
        if (elem.getEndOffset() - elem.getStartOffset() == 0)
        {
            return;
        }
        if (textbox.isEditor())
        {
            /*int left = (int)(rect.x * zoom);
            int top =  (int)(rect.y * zoom);
            int right = left + (int)(rect.width * zoom);
            int bottom = top + (int)(rect.height * zoom);
            Style s = paint.getStyle();
            paint.setStyle(Style.STROKE);
            canvas.drawRect(left, top, right, bottom, paint); 
            paint.setStyle(s);*/
            return;
        }
        
        processRotation(canvas, textbox, shapeRect);
        STRoot root = textbox.getRootView();
        if(root == null)
        {
            IDocument doc = new STDocument();
            doc.appendSection(elem);            
            
            IAttributeSet attr = elem.getAttribute();
            // 宽度
            AttrManage.instance().setPageWidth(attr, (int)Math.round(textbox.getBounds().getWidth() * MainConstant.PIXEL_TO_TWIPS));
            // 高度
            AttrManage.instance().setPageHeight(attr,  (int)Math.round(textbox.getBounds().getHeight() * MainConstant.PIXEL_TO_TWIPS));
                       
            root = new STRoot(sheetView.getSpreadsheet().getEditor(),  doc);
            root.setWrapLine(textbox.isWrapLine());
            root.doLayout();
            
            textbox.setRootView(root);
        }
        
        if(root != null)
        {
            root.draw(canvas, shapeRect.left, shapeRect.top, sheetView.getZoom());
        }
    } 
    
    /**
     * 
     * @param canvas
     * @param shape
     * @param zoom
     */
    private void processRotation(Canvas canvas, IShape shape, Rect shapeRect)
    {
        float angle = shape.getRotation();
        //flip vertical
        if (shape.getFlipVertical())
        {
            angle += 180;
        }
        
        //rotate transform
        if (angle != 0)
        {
            canvas.rotate(angle, shapeRect.centerX(), shapeRect.centerY());
        }
    }
    
    
    /**
     * 
     */
    public void dispose()
    {
        sheetView = null; 
        shapeRect = null;
        temRect = null;
    }
    
    //
    private SheetView sheetView; 
    
    //temp 
    private  Rect shapeRect;
    private Rect temRect;
}
