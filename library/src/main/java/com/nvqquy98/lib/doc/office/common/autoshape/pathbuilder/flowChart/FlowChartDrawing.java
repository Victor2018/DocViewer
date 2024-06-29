/*
 * 文件名称:           FlowChartDrawing.java
 *  
 * 编译器:             android2.2
 * 时间:               下午3:24:00
 */
package com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.flowChart;

import com.nvqquy98.lib.doc.office.common.autoshape.AutoShapeKit;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * draw flowChart
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-9-21
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class FlowChartDrawing
{
    private static Rect flowRect = new Rect();
    
    private static RectF rectF = new RectF();
    
    private static Path path = new Path();
    
    //
    private static final FlowChartDrawing kit = new FlowChartDrawing();

    /**
     * 
     * @return
     */
    public static FlowChartDrawing instance()
    {
        return kit;
    }
    
    /**
     * 
     * @param canvas
     * @param shape
     * @param rect
     */
    public void drawFlowChart(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        int type = shape.getShapeType();
        switch(type)
        {
            case ShapeTypes.FlowChartProcess:                               // 过程
                drawFlowChartProcess(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartAlternateProcess:                      // 可选过程
                drawFlowChartAlternateProcess(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartDecision:                              // 决策
                drawFlowChartDecision(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartInputOutput:                           // 数据
                drawFlowChartInputOutput(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartPredefinedProcess:                     // 预定义过程
                drawFlowChartPredefinedProcess(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartInternalStorage:                       // 内被存储
                drawFlowChartInternalStorage(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartDocument:                              // 文档
                drawFlowChartDocument(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartMultidocument:                         // 多文档
                drawFlowChartMultidocument(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartTerminator:                            // 终止
                drawFlowChartTerminator(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartPreparation:                           // 准备
                drawFlowChartPreparation(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartManualInput:                           // 手动输入
                drawFlowChartManualInput(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartManualOperation:                       // 手动操作
                drawFlowChartManualOperation(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartConnector:                             // 联系
                drawFlowChartConnector(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartOffpageConnector:                      // 离页连接符
                drawFlowChartOffpageConnector(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartPunchedCard:                           // 卡片
                drawFlowChartPunchedCard(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartPunchedTape:                           // 资料带
                drawFlowChartPunchedTape(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartSummingJunction:                       // 汇总连接
                drawFlowChartSummingJunction(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartOr:                                    // 或者
                drawFlowChartOr(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartCollate:                               // 对照
                drawFlowChartCollate(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartSort:                                  // 排序
                drawFlowChartSort(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartExtract:                               // 摘录
                drawFlowChartExtract(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartMerge:                                 // 合并
                drawFlowChartMerge(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartOnlineStorage:                         // 库存数据
                drawFlowChartOnlineStorage(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartDelay:                                 // 延期
                drawFlowChartDelay(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartMagneticTape:                          // 顺序访问存储器
                drawFlowChartMagneticTape(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartMagneticDisk:                          // 磁盘
                drawFlowChartMagneticDisk(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartMagneticDrum:                          // 直接访问存储器
                drawFlowChartMagneticDrum(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            case ShapeTypes.FlowChartDisplay:                               // 显示
                drawFlowChartDisplay(canvas, control, viewIndex, shape, rect, zoom);
                break;
                
            default:
                break;
        }
    }
    
    /**
     * 过程
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartProcess(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 可选过程
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartAlternateProcess(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = Math.min(rect.width(), rect.height()) * 0.18f;
        
        path.reset();
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addRoundRect(rectF, new float[]{x, x, x, x, x, x, x, x}, Path.Direction.CW);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 决策
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartDecision(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {   
        path.reset();
        path.moveTo(rect.exactCenterX(), rect.top);
        path.lineTo(rect.right, rect.exactCenterY());
        path.lineTo(rect.exactCenterX(), rect.bottom);
        path.lineTo(rect.left, rect.exactCenterY());
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }

    
    /**
     * 数据
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartInputOutput(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.2f;
        
        path.reset();
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 预定义过程
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartPredefinedProcess(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.125f;
        
        path.reset();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.left + x, rect.bottom);
        path.moveTo(rect.right - x, rect.top);
        path.lineTo(rect.right - x, rect.bottom);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 内部储存
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartInternalStorage(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.125f;
        float y = rect.height() * 0.125f;
        
        path.reset();
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW);
        
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.left + x, rect.bottom);
        path.moveTo(rect.left, rect.top + y);
        path.lineTo(rect.right, rect.top + y);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 文档
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartDocument(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.height() * 0.2f;
        float y = rect.height() * 0.07f;
        
        path.reset();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        rectF.set(rect.exactCenterX(), rect.bottom - x, 
            rect.right + (float)rect.width() / 2, rect.bottom + x - y * 2);
        path.arcTo(rectF, 270, -90);
        rectF.set(rect.left, rect.bottom - y * 2, rect.exactCenterX(), rect.bottom);
        path.arcTo(rectF, 0, 180);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 多文档
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartMultidocument(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        int x = (int)(rect.width() * 0.137);
        int y = (int)(rect.height() * 0.167);
        
        flowRect.set(rect.left + x, rect.top, rect.right, rect.bottom - y);
        drawFlowChartDocument(canvas, control, viewIndex, shape, flowRect, zoom);
        
        flowRect.set(rect.left + x / 2, rect.top + y / 2, rect.right - x / 2, rect.bottom - y / 2);
        drawFlowChartDocument(canvas, control, viewIndex, shape, flowRect, zoom);
        
        flowRect.set(rect.left, rect.top + y, rect.right - x, rect.bottom);
        drawFlowChartDocument(canvas, control, viewIndex, shape, flowRect, zoom);
    }
    
    /**
     * 终止
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartTerminator(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.16f;
        float y = rect.height() * 0.5f;
        
        path.reset();
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addRoundRect(rectF, new float[]{x, y, x, y, x, y, x, y}, Path.Direction.CW);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 准备
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartPreparation(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.2f;
        
        path.reset();
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right - x, rect.top);
        path.lineTo(rect.right, rect.exactCenterY());
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left + x, rect.bottom);
        path.lineTo(rect.left, rect.exactCenterY());
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 手动输入
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartManualInput(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.height() * 0.2f;
        
        path.reset();
        path.moveTo(rect.left, rect.top + x);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 手动操作
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartManualOperation(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.2f;
        
        path.reset();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right - x, rect.bottom);
        path.lineTo(rect.left + x, rect.bottom);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 联系
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartConnector(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 离页连接符
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartOffpageConnector(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.height() * 0.2f;
        
        path.reset();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom - x);
        path.lineTo(rect.exactCenterX(), rect.bottom);
        path.lineTo(rect.left, rect.bottom - x);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 卡片
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartPunchedCard(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.2f;
        float y = rect.height() * 0.2f;
        
        path.reset();
        path.moveTo(rect.left + x, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top + y);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 资料带
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartPunchedTape(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.height() * 0.1f;
        
        path.reset();
        rectF.set(rect.left, rect.top, rect.exactCenterX(), rect.top + x * 2);
        path.arcTo(rectF, 180, -180);
        rectF.set(rect.exactCenterX(), rect.top, rect.right, rect.top + x * 2);
        path.arcTo(rectF, 180, 180);
        rectF.set(rect.exactCenterX(), rect.bottom - x * 2, rect.right, rect.bottom);
        path.arcTo(rectF, 0, -180);
        rectF.set(rect.left, rect.bottom - x * 2, rect.exactCenterX(), rect.bottom);
        path.arcTo(rectF, 0, 180);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 汇总连接
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartSummingJunction(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = (float)Math.sqrt(2) * rect.width() / 4;
        float y = (float)Math.sqrt(2) * rect.height() / 4;
        float x0 = rect.exactCenterX();
        float y0 = rect.exactCenterY();
        
        path.reset();
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        
        path.moveTo(x0 - x, y0 - y);
        path.lineTo(x0 + x, y0 + y);
        
        path.moveTo(x0 + x, y0 - y);
        path.lineTo(x0 - x, y0 + y);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 或者
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartOr(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        
        path.moveTo(rect.exactCenterX(), rect.top);
        path.lineTo(rect.exactCenterX(), rect.bottom);
        
        path.moveTo(rect.left, rect.exactCenterY());
        path.lineTo(rect.right, rect.exactCenterY());
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 对照 
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartCollate(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();   
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.exactCenterX(), rect.exactCenterY());
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.exactCenterX(), rect.exactCenterY());
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 排序
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartSort(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();  
        path.moveTo(rect.exactCenterX(), rect.top);
        path.lineTo(rect.right, rect.exactCenterY());
        path.lineTo(rect.exactCenterX(), rect.bottom);       
        path.lineTo(rect.left, rect.exactCenterY());
        path.close();
        
        path.moveTo(rect.left, rect.exactCenterY());
        path.lineTo(rect.right, rect.exactCenterY());
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 摘录
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartExtract(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();   
        path.moveTo(rect.exactCenterX(), rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 合并
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartMerge(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();       
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.exactCenterX(), rect.bottom);       
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 库存数据
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartOnlineStorage(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.16f;
        
        path.reset();
        rectF.set(rect.right - x, rect.top, rect.right + x, rect.bottom);
        path.arcTo(rectF, 90, 180);
        rectF.set(rect.left, rect.top, rect.left + x * 2, rect.bottom);
        path.arcTo(rectF, 270, -180);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 延期
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartDelay(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        path.reset();
        path.moveTo(rect.left, rect.top);
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.arcTo(rectF, 270, 180);
        path.lineTo(rect.left, rect.bottom);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 顺序访问存储器
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartMagneticTape(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.15f;
        float y = rect.height() * 0.15f;
        
        path.reset();
        rectF.set(rect.left, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
        
        boolean border = shape.hasLine();
        shape.setLine(false);
        
        path.reset();
        path.moveTo(rect.exactCenterX(), rect.bottom - y);
        path.lineTo(rect.right, rect.bottom - y);
        path.lineTo(rect.right, rect.bottom);
        path.moveTo(rect.exactCenterX(), rect.bottom);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
        shape.setLine(border);
        
        path.reset();
        path.moveTo(rect.right - x, rect.bottom - y);
        path.lineTo(rect.right, rect.bottom - y);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.exactCenterX(), rect.bottom);
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 磁盘
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartMagneticDisk(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.height() * 0.32f;
        
        path.reset();
        rectF.set(rect.left, rect.top, rect.right, rect.top + x);
        path.addOval(rectF, Path.Direction.CW);
        rectF.set(rect.left, rect.bottom - x, rect.right, rect.bottom);
        path.arcTo(rectF, 0, 180);
        rectF.set(rect.left, rect.top, rect.right, rect.top + x);
        path.arcTo(rectF, 180, -180);       
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 直接访问存储器 
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartMagneticDrum(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.34f;
        
        path.reset();
        rectF.set(rect.right - x, rect.top, rect.right, rect.bottom);
        path.addOval(rectF, Path.Direction.CW);
        path.moveTo(rect.right - x / 2, rect.bottom);
        rectF.set(rect.right - x, rect.top, rect.right, rect.bottom);
        path.arcTo(rectF, 90, 180);
        rectF.set(rect.left, rect.top, rect.left + x, rect.bottom);
        path.arcTo(rectF, 270, -180);       
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
    
    /**
     * 显示
     * @param canvas
     * @param shape
     * @param rect
     */
    private void drawFlowChartDisplay(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        float x = rect.width() * 0.16f;
        
        path.reset();
        path.moveTo(rect.left, rect.exactCenterY());
        path.lineTo(rect.left + x, rect.top);
        rectF.set(rect.right - x * 2, rect.top, rect.right, rect.bottom);
        path.arcTo(rectF, 270, 180);
        path.lineTo(rect.left + x, rect.bottom);
        path.close();
        
        AutoShapeKit.instance().drawShape(canvas, control, viewIndex, shape, path, rect, zoom);
    }
}
