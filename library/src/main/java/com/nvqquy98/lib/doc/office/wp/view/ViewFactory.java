/*
 * 文件名称:          ViewFactory.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:22:30
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.constant.wp.AttrIDConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.system.IControl;

/**
 * 视图工厂
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-9
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ViewFactory
{

    /**
     * 
     * @param elem          model元素
     * @param paraElem      model中段落元素，只有创建leafView才需要，其它view时，传null就可以了
     * @param viewType      视图类型
     * @return
     */
    public static IView createView(IControl control, IElement elem, IElement paraElem, int viewType)
    {
        IView view = null;
        switch (viewType)
        {
            case WPViewConstant.PAGE_VIEW:
                view = new PageView(elem);
                break;
                
            case WPViewConstant.PARAGRAPH_VIEW:
                view = new ParagraphView(elem);
                break;
                
            case WPViewConstant.LINE_VIEW:
                //view = (IView)lineView.allocObject();
                //view.setElement(elem);
                view = new LineView(elem);
                break;
                
            case WPViewConstant.LEAF_VIEW:
                /*if (AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.FONT_SHAPE_ID))
                {
                    view = (IView)objView.allocObject();
                    ((ObjView)view).initProperty(elem, paraElem);
                }
                else
                {
                    view = (IView)leafView.allocObject();
                    ((LeafView)view).initProperty(elem, paraElem);
                }*/
                if (AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.FONT_SHAPE_ID))
                {
                    AbstractShape shape = control.getSysKit().getWPShapeManage().getShape(AttrManage.instance().getShapeID(elem.getAttribute()));
                    if (shape != null)
                    {
                        if (shape.getType() == AbstractShape.SHAPE_AUTOSHAPE 
                        		|| shape.getType() == AbstractShape.SHAPE_CHART)
                        {
                            view = new ShapeView(paraElem, elem, (AutoShape)shape); 
                        }
                        else if (shape.getType() == AbstractShape.SHAPE_PICTURE)
                        {
                            view = new ObjView(paraElem, elem, (com.nvqquy98.lib.doc.office.common.shape.WPAutoShape)shape);
                        }
                    }
                    else
                    {
                        view = new ObjView(paraElem, elem, null);
                    }
                }
                else if (AttrManage.instance().hasAttribute(elem.getAttribute(), AttrIDConstant.FONT_ENCLOSE_CHARACTER_TYPE_ID))
                {
                    view = new EncloseCharacterView(paraElem, elem);
                }
                else
                {
                    view = new LeafView(paraElem, elem);
                }
                break;
                
            case WPViewConstant.TABLE_VIEW:
                if (elem.getType() == WPModelConstant.TABLE_ELEMENT)
                {
                    view = new TableView(elem);
                }
                else
                {
                    view = new ParagraphView(elem);
                }
                break;
                
            case WPViewConstant.TABLE_ROW_VIEW:
                view = new RowView(elem);
                break;
                
            case WPViewConstant.TABLE_CELL_VIEW:
                view = new CellView(elem);
                break;
            
            case WPViewConstant.TITLE_VIEW:
                view = new TitleView(elem);
                break;
                
            case WPViewConstant.BN_VIEW:
                //view = (IView)bnView.allocObject();
                view = new BNView();
                break;
                
            default:
                break;
        }
        return  view;
    }
    
    /**
     * 
     */
    public static void dispose()
    {
        // leaf view share pool
        //leafView.dispose();
        // line view share pool
        //lineView.dispose();
        // object view share pool
        //objView.dispose();
        // bullet and number share pool
        //bnView.dispose();
    }
    
    // LeafView share pool
    //public static AllocPool leafView = new AllocPool(new LeafView(), 1024, 512);
    // LineView share pool
    //public static AllocPool lineView = new AllocPool(new LineView(), 512, 128);
    // ObjView share pool
    //public static AllocPool objView = new AllocPool(new ObjView(), 64, 32);
    // bullet and number pool
    //public static AllocPool bnView = new AllocPool(new BNView(), 64, 32);
    
}
