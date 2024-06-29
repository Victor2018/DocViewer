/*
 * 文件名称:          Slide.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:59:06
 */
package com.nvqquy98.lib.doc.office.pg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.common.shape.SmartArt;
import com.nvqquy98.lib.doc.office.common.shape.TableCell;
import com.nvqquy98.lib.doc.office.common.shape.TableShape;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.Rectanglef;
import com.nvqquy98.lib.doc.office.pg.animate.ShapeAnimation;

/**
 * Slide数据
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-2-13
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class PGSlide
{
    //master slide
    public static final byte Slide_Master = 0;
    //layout slide
    public static final byte Slide_Layout = 1;
    //Normal slide
    public static final byte Slide_Normal = 2;
    
    /**
     * Constructor
     */
    public PGSlide()
    {   
        shapes = new ArrayList<IShape>();
        showMasterHeadersFooters = true;
        geometryType = -1;
    }
    
    /**
     * Constructor
     */
    public PGSlide(int slideNo, PGNotes notes)
    {
        this.slideNo = slideNo;
        this.notes = notes;
        shapes = new ArrayList<IShape>();
        showMasterHeadersFooters = true;
        geometryType = -1;
    } 
    
    /**
     * set slide number of this slide
     */
    public void setSlideNo(int slideNo)
    {
        this.slideNo = slideNo;
    }
    
    /**
     * get slide number of this slide
     */
    public int getSlideNo()
    {
        return slideNo;
    }
    
    /**
     * 
     * @return
     */
    public int getSlideType()
    {
        return slideType;
    }
    
    /**
     * 
     * @param slideType
     */
    public void setSlideType(int slideType)
    {
        this.slideType = slideType;
    }
    
    /**
     * append shape of this slide
     */
    public void appendShapes(IShape shape)
    {
        if (shape == null)
        {
            return;
        }
        if (!hasTable)
        {
            hasTable = shape.getType() == AbstractShape.SHAPE_TABLE;
        }
        this.shapes.add(shape);
    }
    
    /**
     * get all shapes of this slide
     */
    public IShape[] getShapes()
    {
        return shapes.toArray(new IShape[shapes.size()]);
    }
    
    /**
     * get count of this slide
     */
    public int getShapeCount()
    {
        return shapes.size();
    }
    
    /**
     * for find
     * @return
     */
    public int getShapeCountForFind()
    {
        if (!hasTable)
        {
            return getShapeCount();
        }
        if (shapeCountForFind > 0)
        {
            return shapeCountForFind;
        }
        shapesForFind = new ArrayList<IShape>();
        int count = 0;
        for (IShape shape : shapes)
        {
            if (shape.getType() == AbstractShape.SHAPE_TABLE)
            {
                for (int i = 0; i < ((TableShape)shape).getCellCount(); i++)
                {
                    TableCell cell = ((TableShape)shape).getCell(i);
                    if (cell != null && cell.getText() != null)
                    {
                        shapesForFind.add(cell.getText());
                        count++;
                    }
                }
            }
            else
            {
                shapesForFind.add(shape);
                count++;
            }
        }
        return shapeCountForFind = count;
    }
    
    
    
    /**
     * get shape with index
     */
    public IShape getShape(int index)
    {
        if (index < 0 || index >= shapes.size())
        {
            return null;
        }
        return shapes.get(index);
    }
    
    /**
     * get shape for find
     * @return
     */
    public IShape getShapeForFind(int index)
    {
        if (!hasTable)
        {
            return getShape(index);
        }
        if (index < 0 || index >= shapesForFind.size())
        {
            return null;
        }
        return shapesForFind.get(index);
    }
    
    /**
     * get shape of this slide
     * 
     * @param x
     * @param y
     */
    public IShape getShape(float x, float y)
    {
        for (IShape shape : shapes)
        {
            Rectangle rect = shape.getBounds();
            if(shape.getType() == AbstractShape.SHAPE_TABLE)
            {
                TableShape table = (TableShape)shape;
                int count = table.getCellCount();
                for (int i = 0; i < count; i++)
                {
                    TableCell cell = table.getCell(i);
                    if (cell != null)
                    {   
                        Rectanglef r = cell.getBounds();
                        if (r.contains(x, y))
                        {
                            return cell.getText();
                        }
                    }
                }
            }
            else if (rect.contains(x,  y))
            {
                return shape;
            }
        }
        return null;
    }
    
    /**
     * set note of this slide
     */
    public void setNotes(PGNotes notes)
    {
        this.notes = notes;
    }
    /**
     * get note of this slide
     */
    public PGNotes getNotes()
    {
        return this.notes;
    }  
    
    /**
     * @return Returns the bgFill.
     */
    public BackgroundAndFill getBackgroundAndFill()
    {
        return bgFill;
    }

    /**
     * @param bgFill The bgFill to set.
     */
    public void setBackgroundAndFill(BackgroundAndFill bgFill)
    {
        this.bgFill = bgFill;
    }
    
    /**
     * slide对应的master slide index
     * @param index
     */
    public void setMasterSlideIndex(int index)
    {
        masterIndexs[0] = index;
    }
    
    /**
     * slide对应的layout slide index
     * @param index
     */
    public void setLayoutSlideIndex(int index)
    {
        masterIndexs[1] = index;
    }
    
    /**
     * get master indexs
     * @return
     */
    public int[] getMasterIndexs()
    {
        return masterIndexs;
    }
    
    /**
     * 
     */
    public IShape getShape(int x, int y)
    {
        for (IShape shape : shapes)
        {
            Rectangle rect = shape.getBounds();
            if(shape.getType() == AbstractShape.SHAPE_TABLE)
            {
                TableShape table = (TableShape)shape;
                int count = table.getCellCount();
                for (int i = 0; i < count; i++)
                {
                    TableCell cell = table.getCell(i);
                    if (cell != null)
                    {   
                        Rectanglef r = cell.getBounds();
                        if (r.contains(x, y))
                        {
                            return cell.getText();
                        }
                    }
                }
            }
            else if (rect.contains(x,  y))
            {
                return shape;
            }
        }
        return null;
    }
    
    /**
     * return the textbox shape which is at (x, y).if there are more one shapes, return one on the top
     */
    public IShape getTextboxShape(int x, int y)
    {
        //search from last shape
        int shapeCnt = shapes.size();
        for (int i = shapeCnt - 1; i >= 0; i--)
        {
            IShape  shape = shapes.get(i);
            Rectangle rect = shape.getBounds();
            if(shape.getType() == AbstractShape.SHAPE_TABLE)
            {
                TableShape table = (TableShape)shape;
                int count = table.getCellCount();
                for (int j = 0; j < count; j++)
                {
                    TableCell cell = table.getCell(j);
                    if (cell != null)
                    {   
                        Rectanglef r = cell.getBounds();
                        if (r.contains(x, y))
                        {
                            return cell.getText();
                        }
                    }
                }
            }
            else if (rect.contains(x,  y) && shape.getType() == AbstractShape.SHAPE_TEXTBOX)
            {
                return shape;
            }
        }
        return null;
    }
    
    /**
     * slide has transition or not
     * @param transition
     */
    public void setTransition(boolean transition)
    {
        hasTransition = transition;
    }
    
    /**
     * 
     * @return
     */
    public boolean hasTransition()
    {
        return hasTransition;
    }
    
    public void addShapeAnimation(ShapeAnimation shapeAnim)
    {
        if(shapeAnimLst == null)
        {
            shapeAnimLst = new ArrayList<ShapeAnimation>();
        }
        
        if(shapeAnim != null)
        {
            shapeAnimLst.add(shapeAnim);
        }
    }
    
    /**
     * 
     * @return
     */
    public List<ShapeAnimation> getSlideShowAnimation()
    {
        return shapeAnimLst;
    }
    

    public void addGroupShape(int grpShapeID, List<Integer> childShapes)
    {
        if(grpShapeLst == null)
        {
            grpShapeLst = new HashMap<Integer, List<Integer>>();
        }
        Integer[] arr = new Integer[childShapes.size()];
        childShapes.toArray(arr);
        
        for(Integer id : arr)
        {
            if(grpShapeLst.containsKey(id))
            {
                List<Integer> subShapes = grpShapeLst.remove(id);
                childShapes.remove(id);
                childShapes.addAll(subShapes);
            }
        }
        grpShapeLst.put(grpShapeID, childShapes);
    }
    
    public Map<Integer, List<Integer>> getGroupShape()
    {
        return grpShapeLst;
    }
    
    /**
     * 
     * @param id
     * @param smartArt
     */
    public void addSmartArt(String id, SmartArt smartArt)
    {
        if(smartArtList == null)
        {
            smartArtList = new HashMap<String, SmartArt>();
        }
        
        smartArtList.put(id, smartArt);
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public SmartArt getSmartArt(String id)
    {
        if(id != null && smartArtList != null)
        {
            return smartArtList.remove(id);
        }
        
        return null;
    }
    
    public IShape getTextboxByPlaceHolderID(int placeHolderID)
    {
        int shapeCnt = shapes.size();
        for (int i = 0; i < shapeCnt; i++)
        {
            IShape shape = shapes.get(i);
            if (shape.getType() == AbstractShape.SHAPE_TEXTBOX && shape.getPlaceHolderID() == placeHolderID)
            {
                return shape;
            }
        }
        return null;
    }
    
    /**
     * 
     * @return
     */
    public boolean isShowMasterHeadersFooter()
    {
        return showMasterHeadersFooters;
    }
    
    /**
     * 
     * @param showMasterHeadersFooters
     */
    public void setShowMasterHeadersFooters(boolean showMasterHeadersFooters)
    {
        this.showMasterHeadersFooters = showMasterHeadersFooters;
    }
    
    /**
     * The different kinds of geometry, see SSlideLayoutAtom
     * @return
     */
    public int getGeometryType()
    {
        return geometryType;
    }
    
    /**
     * The different kinds of geometry, see SSlideLayoutAtom
     * @param geometryType
     */
    public void setGeometryType(int geometryType)
    {
        this.geometryType = geometryType;
    }
    
    /**
     * dispose
     */
    public void dispose()
    {
        if (notes != null)
        {
            notes.dispose();
            notes = null;
        }
        if (shapesForFind != null)
        {
            shapesForFind.clear();
            shapesForFind = null;
        }
        if (shapes != null)
        {
            for (IShape shape :shapes)
            {
                shape.dispose();
            }
            shapes.clear();
            shapes = null;
        }
        if (bgFill != null)
        {
            bgFill.dispose();
            bgFill = null;
        }  
        if(shapeAnimLst != null)
        {
            shapeAnimLst.clear();
            shapeAnimLst = null;
        }
    }

    //
    private boolean hasTable;
    // slide number
    private int slideNo;
    //normal, layout, or master
    private int slideType;
    //
    private int shapeCountForFind = -1;
    // shapes of this slide
    private List<IShape> shapes;
    //
    private List<IShape> shapesForFind;
    // note of the slide
    private PGNotes notes; 
    //
    private BackgroundAndFill bgFill;
    // master slide index and layout slide index
    private int[] masterIndexs = {-1, -1};
    
    ////////////////////////////////////////////////////////animaton
    //transition
    private boolean hasTransition;
    //group shape id and shape id
    private Map<Integer, List<Integer>> grpShapeLst;
    //shape animation
    private List<ShapeAnimation> shapeAnimLst;
    
    private Map<String, SmartArt> smartArtList;
    private boolean showMasterHeadersFooters;
    //The different kinds of geometry, see SSlideLayoutAtom
    private int geometryType;
}
