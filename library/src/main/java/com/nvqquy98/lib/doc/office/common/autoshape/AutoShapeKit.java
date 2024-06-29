/*
 * 文件名称:           AutoShapeKit.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:26:09
 */
package com.nvqquy98.lib.doc.office.common.autoshape;

import java.util.List;

import com.nvqquy98.lib.doc.office.common.BackgroundDrawer;
import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.actionButton.ActionButtonPathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.arrow.ArrowPathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.baseshape.BaseShapePathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.flowChart.FlowChartDrawing;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.line.LinePathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.math.MathPathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.rect.RectPathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.smartArt.SmartArtPathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.starAndBanner.BannerPathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.starAndBanner.star.StarPathBuilder;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.wedgecallout.WedgeCalloutDrawing;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.shape.ArbitraryPolygonShape;
import com.nvqquy98.lib.doc.office.common.shape.AutoShape;
import com.nvqquy98.lib.doc.office.common.shape.LineShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.common.shape.WPAutoShape;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.pg.animate.IAnimation;
import com.nvqquy98.lib.doc.office.pg.animate.ShapeAnimation;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * draw autoShape
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-8-27
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AutoShapeKit
{
    //
    public static int ARROW_WIDTH = 10;
    
    private static Rect rect = new Rect();
    
    private static Matrix m = new Matrix();
    //
    private static final AutoShapeKit kit = new AutoShapeKit();

    /**
     * 
     * @return
     */
    public static AutoShapeKit instance()
    {
        return kit;
    }
    
    /**
     * 
     */
    public AutoShapeKit()
    {
        
    }

    
    /**
     * 
     * @param shape all shapes except textbox
     * @return
     */
    private IAnimation getShapeAnimation(AutoShape shape)
    {
        IAnimation animation = (IAnimation)shape.getAnimation();
        if(animation != null)
        {
            ShapeAnimation shapeAnim = animation.getShapeAnimation();
            int paraBegin = shapeAnim.getParagraphBegin();
            int paraEnd = shapeAnim.getParagraphEnd();
            
            if((paraBegin == ShapeAnimation.Para_All && paraEnd == ShapeAnimation.Para_All)
                || (paraBegin == ShapeAnimation.Para_BG && paraEnd == ShapeAnimation.Para_BG))
            {
                return animation;
            } 
        }
        
        return null;
    }
    
    private void processShapeRect(Rect rect, IAnimation animation)
    {
        if(animation != null)
        {         
            int width = rect.width();
            int height = rect.height();
            
            int alpha = animation.getCurrentAnimationInfor().getAlpha();
            float rate = alpha / 255f * 0.5f;
            
            int centerX = rect.centerX();
            int centerY = rect.centerY();
            rect.set((int)(centerX - width * rate),
                (int)(centerY - height * rate),
                (int)(centerX + width * rate),
                (int)(centerY + height * rate));
        }
    }
    
    /**
     * 
     * @param canvas
     * @param shape
     * @param zoom
     */
    public void drawAutoShape(Canvas canvas, IControl control, int viewIndex, AutoShape shape, float zoom)
    {
        Rectangle shapeRect = shape.getBounds();
        int left = Math.round(shapeRect.x * zoom);
        int top = Math.round(shapeRect.y * zoom);
        int width = Math.round(shapeRect.width * zoom);
        int height = Math.round(shapeRect.height * zoom);
        rect.set(left, top, left + width, top + height);
        drawAutoShape(canvas, control, viewIndex, shape, rect, zoom);
    }
    
    /**
     * 
     * @param canvas
     * @param shape
     * @param rect
     * @param zoom
     */
    public void drawAutoShape(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Rect rect, float zoom)
    {
        IAnimation animation = getShapeAnimation(shape);
        
        //zoom by animation
        processShapeRect(rect, animation);        
        int type = shape.getShapeType();
        switch(type)
        {
        	case ShapeTypes.Line:
            case ShapeTypes.StraightConnector1:                             // 箭头，双箭头
            case ShapeTypes.BentConnector2:                                 // 肘形连接符，肘形箭头连接符，肘形双箭头连接符    
            case ShapeTypes.BentConnector3:
            case ShapeTypes.CurvedConnector2:
            case ShapeTypes.CurvedConnector3: 
            case ShapeTypes.CurvedConnector4: 
            case ShapeTypes.CurvedConnector5:                               // 曲线连接符，曲线箭头连接符，曲线双箭头连接符
            {
            	if(shape instanceof LineShape)
            	{
            		List<ExtendPath> pathList = LinePathBuilder.getLinePath((LineShape)shape, rect, zoom);               
                    for(int i = 0; i < pathList.size(); i++)
                    {
                        ExtendPath extendPath = new ExtendPath(pathList.get(i));
                        drawShape(canvas, control, viewIndex, shape, extendPath, rect, animation, zoom);
                    }
            	}                
            }
                break;
                
            case ShapeTypes.ArbitraryPolygon:
            {
                m.reset();
                m.postScale(zoom, zoom);
                List<ExtendPath> pathList = ((ArbitraryPolygonShape)shape).getPaths();                
                for(int i = 0; i < pathList.size(); i++)
                {
                    ExtendPath extendPath = new ExtendPath(pathList.get(i));                    
                    extendPath.getPath().transform(m); 
                    extendPath.getPath().offset(rect.left, rect.top);
                    
                    drawShape(canvas, control, viewIndex, shape, extendPath, rect, animation, zoom);
                }
            }  
                break; 
            case ShapeTypes.WP_Line:
            case ShapeTypes.Curve:
            case ShapeTypes.DirectPolygon:
            {
            	m.reset();
                m.postScale(zoom, zoom);
                List<ExtendPath> pathList = ((WPAutoShape)shape).getPaths();
                
                Rect r = new Rect(rect);
                if(rect.width() == 0 || rect.height() == 0)
                {
                	//wp line, curve, polygon rect adjust
                	RectF bounds = new RectF();
                	pathList.get(0).getPath().computeBounds(bounds, true);
                	r.set((int)bounds.left, (int)bounds.top, (int)bounds.right, (int)bounds.bottom);
                }  
                
                for(int i = 0; i < pathList.size(); i++)
                {
                    ExtendPath extendPath = new ExtendPath(pathList.get(i));
                    extendPath.getPath().transform(m); 
                    extendPath.getPath().offset(rect.left, rect.top);
                    
                    drawShape(canvas, control, viewIndex, shape, extendPath, r, animation, zoom);
                }
            }
            	break;
            case ShapeTypes.Rectangle:                                      // 矩形 ，文本框，垂直文本框
            case ShapeTypes.TextBox:                
            case ShapeTypes.RoundRectangle:                                 // 圆角矩形                
            case ShapeTypes.Round1Rect:                                     // 单圆角矩形
            case ShapeTypes.Round2SameRect:                                 // 同侧圆角矩形
            case ShapeTypes.Round2DiagRect:                                 // 对角圆角矩形
            case ShapeTypes.Snip1Rect:                                      // 剪去单角的矩形
            case ShapeTypes.Snip2SameRect:                                  // 剪去同侧角的矩形
            case ShapeTypes.Snip2DiagRect:                                  // 剪去对角的矩形                
            case ShapeTypes.SnipRoundRect:                                  // 剪去单角的单圆角矩形
            case ShapeTypes.TextPlainText:
                drawShape(canvas, control, viewIndex, shape, RectPathBuilder.getRectPath(shape, rect), rect, animation, zoom);
                break;
                
            case ShapeTypes.Ellipse:                                        // 椭圆
            case ShapeTypes.Triangle:                                       // 等腰三角形
            case ShapeTypes.RtTriangle:                                     // 直角三角形
            case ShapeTypes.Parallelogram:                                  // 平行四边形
            case ShapeTypes.Trapezoid:                                      // 梯形
            case ShapeTypes.Diamond:                                        // 菱形
            case ShapeTypes.Pentagon:                                       // 正五边形
            case ShapeTypes.Hexagon:                                        // 六边形
            case ShapeTypes.Heptagon:                                       // 七边形
            case ShapeTypes.Octagon:                                        // 八边形
            case ShapeTypes.Decagon:                                        // 十边形
            case ShapeTypes.Dodecagon:                                      // 十二边形
            case ShapeTypes.Pie:                                            // 饼形
            case ShapeTypes.Chord:                                          // 弦形
            case ShapeTypes.Teardrop:                                       // 泪滴形
            case ShapeTypes.Frame:                                          // 图文框
            case ShapeTypes.HalfFrame:                                      // 半闭框
            case ShapeTypes.Corner:                                         // L形
            case ShapeTypes.DiagStripe:                                     // 斜纹
            case ShapeTypes.Plus:                                           // 十字形
            case ShapeTypes.Plaque:                                         // 缺角矩形
            case ShapeTypes.Can:                                            // 圆柱形
            case ShapeTypes.Cube:                                           // 立方体
            case ShapeTypes.Bevel:                                          // 棱台
            case ShapeTypes.Donut:                                          // 同心圆
            case ShapeTypes.NoSmoking:                                      // 禁止符
            case ShapeTypes.BlockArc:                                       // 空心弧
            case ShapeTypes.FoldedCorner:                                   // 折角形
            case ShapeTypes.SmileyFace:                                     // 笑脸
            case ShapeTypes.Sun:                                            // 太阳形
            case ShapeTypes.Heart:                                          // 心形
            case ShapeTypes.LightningBolt:                                  // 闪电形
            case ShapeTypes.Moon:                                           // 新月形
            case ShapeTypes.Cloud:                                         // 云形
            case ShapeTypes.Arc:                                            // 弧形
            case ShapeTypes.BracketPair:                                    // 双括号
            case ShapeTypes.BracePair:                                      // 双大括号
            case ShapeTypes.LeftBracket:                                    // 左中括号
            case ShapeTypes.RightBracket:                                   // 右中括号
            case ShapeTypes.LeftBrace:                                      // 左大括号
            case ShapeTypes.RightBrace:                                     // 右大括号
            {
                Object obj = BaseShapePathBuilder.getBaseShapePath(shape, rect);
                if(obj instanceof Path)
                {
                    drawShape(canvas, control, viewIndex, shape, (Path)obj, rect, animation, zoom);
                }
                else
                {
                    @ SuppressWarnings("unchecked")
                    List<ExtendPath> pathList = (List<ExtendPath>)obj;
                    for(int i = 0; i < pathList.size(); i++)
                    {
                        ExtendPath extendPath = new ExtendPath(pathList.get(i));
                        drawShape(canvas, control, viewIndex, shape, extendPath, rect, animation, zoom);
                    }
                }
            }
                break;
            case ShapeTypes.MathPlus:                                       // 加号
            case ShapeTypes.MathMinus:                                      // 减号
            case ShapeTypes.MathMultiply:                                   // 乘号                
            case ShapeTypes.MathDivide:                                     // 除号
            case ShapeTypes.MathEqual:                                      // 等号
            case ShapeTypes.MathNotEqual:                                   // 不等号
                drawShape(canvas, control, viewIndex, shape, MathPathBuilder.getMathPath(shape, rect), rect, animation, zoom);
                break;            
            
            case ShapeTypes.RightArrow:                 //arrow drawing
            case ShapeTypes.LeftArrow:                
            case ShapeTypes.UpArrow:
            case ShapeTypes.DownArrow:
            case ShapeTypes.LeftRightArrow:
            case ShapeTypes.UpDownArrow:
            case ShapeTypes.QuadArrow:
            case ShapeTypes.LeftRightUpArrow:
            case ShapeTypes.BentArrow:
            case ShapeTypes.UturnArrow:
            case ShapeTypes.LeftUpArrow:
            case ShapeTypes.BentUpArrow:
            case ShapeTypes.StripedRightArrow:
            case ShapeTypes.NotchedRightArrow:
            case ShapeTypes.HomePlate:
            case ShapeTypes.Chevron:
            case ShapeTypes.RightArrowCallout:
            case ShapeTypes.LeftArrowCallout:
            case ShapeTypes.DownArrowCallout:
            case ShapeTypes.UpArrowCallout:
            case ShapeTypes.LeftRightArrowCallout:
            case ShapeTypes.UpDownArrowCallout:
            case ShapeTypes.QuadArrowCallout:
            case ShapeTypes.CircularArrow:
            case ShapeTypes.CurvedRightArrow:
            case ShapeTypes.CurvedLeftArrow:
            case ShapeTypes.CurvedUpArrow:
            case ShapeTypes.CurvedDownArrow:
            {
                Object obj = ArrowPathBuilder.getArrowPath(shape, rect);
                if(obj instanceof Path)
                {
                    drawShape(canvas, control, viewIndex, shape, (Path)obj, rect, animation, zoom);
                }
                else
                {
                    @ SuppressWarnings("unchecked")
                    List<Path> list = (List<Path>)obj;
                    int cnt = list.size();
                    for(int i = 0; i < cnt; i++)
                    {
                        drawShape(canvas, control, viewIndex, shape, list.get(i), rect, animation, zoom);
                    }
                }
            }  
                break;
                
            // flowChart 
            case ShapeTypes.FlowChartProcess:
            case ShapeTypes.FlowChartAlternateProcess:
            case ShapeTypes.FlowChartDecision:
            case ShapeTypes.FlowChartInputOutput:
            case ShapeTypes.FlowChartPredefinedProcess:
            case ShapeTypes.FlowChartInternalStorage:
            case ShapeTypes.FlowChartDocument:
            case ShapeTypes.FlowChartMultidocument:
            case ShapeTypes.FlowChartTerminator:
            case ShapeTypes.FlowChartPreparation:
            case ShapeTypes.FlowChartManualInput:
            case ShapeTypes.FlowChartManualOperation:
            case ShapeTypes.FlowChartConnector:
            case ShapeTypes.FlowChartOffpageConnector:
            case ShapeTypes.FlowChartPunchedCard:
            case ShapeTypes.FlowChartPunchedTape:
            case ShapeTypes.FlowChartSummingJunction:
            case ShapeTypes.FlowChartOr:
            case ShapeTypes.FlowChartCollate:
            case ShapeTypes.FlowChartSort:
            case ShapeTypes.FlowChartExtract:
            case ShapeTypes.FlowChartMerge:
            case ShapeTypes.FlowChartOnlineStorage:
            case ShapeTypes.FlowChartDelay:
            case ShapeTypes.FlowChartMagneticTape:
            case ShapeTypes.FlowChartMagneticDisk:
            case ShapeTypes.FlowChartMagneticDrum:
            case ShapeTypes.FlowChartDisplay:
                FlowChartDrawing.instance().drawFlowChart(canvas, control, viewIndex, shape, rect, zoom);
                break;
              
            // wedgecallout
            case ShapeTypes.WedgeRectCallout:
            case ShapeTypes.WedgeRoundRectCallout:
            case ShapeTypes.WedgeEllipseCallout:
            case ShapeTypes.CloudCallout:
            case ShapeTypes.BorderCallout1:
            case ShapeTypes.BorderCallout2:
            case ShapeTypes.BorderCallout3:
            case ShapeTypes.BorderCallout4:
            case ShapeTypes.AccentCallout1:
            case ShapeTypes.AccentCallout2:
            case ShapeTypes.AccentCallout3:
            case ShapeTypes.AccentCallout4:
            case ShapeTypes.Callout1:
            case ShapeTypes.Callout2:
            case ShapeTypes.Callout3:
            case ShapeTypes.Callout4:
            case ShapeTypes.AccentBorderCallout1:
            case ShapeTypes.AccentBorderCallout2:
            case ShapeTypes.AccentBorderCallout3:
            case ShapeTypes.AccentBorderCallout4:
                Object obj =  WedgeCalloutDrawing.instance().getWedgeCalloutPath(shape, rect);
                if(obj instanceof Path)
                {
                    drawShape(canvas, control, viewIndex, shape, (Path)obj, rect, animation, zoom);
                }
                else
                {
                    @ SuppressWarnings("unchecked")
                    List<ExtendPath> list = (List<ExtendPath>)obj;
                    int cnt = list.size();
                    for(int i = 0; i < cnt; i++)
                    {
                        drawShape(canvas, control, viewIndex, shape, list.get(i), rect, animation, zoom);
                    }
                }     
                break;
                
            case ShapeTypes.ActionButtonBackPrevious:                   //action button
            case ShapeTypes.ActionButtonForwardNext:
            case ShapeTypes.ActionButtonBeginning:
            case ShapeTypes.ActionButtonEnd:
            case ShapeTypes.ActionButtonHome:
            case ShapeTypes.ActionButtonInformation:
            case ShapeTypes.ActionButtonReturn:
            case ShapeTypes.ActionButtonMovie:
            case ShapeTypes.ActionButtonDocument:
            case ShapeTypes.ActionButtonSound:
            case ShapeTypes.ActionButtonHelp:
            case ShapeTypes.ActionButtonBlank:
                List<ExtendPath> pathList = ActionButtonPathBuilder.getActionButtonExtendPath(shape, rect);
                if(pathList == null)
                {
                    return;
                }
                int cnt = pathList.size();                
                for(int i = 0; i < cnt; i++)
                {
                    drawShape(canvas, control, viewIndex, shape, pathList.get(i), rect, animation, zoom);
                }
                
                break;
            
            case ShapeTypes.IrregularSeal1:
            case ShapeTypes.IrregularSeal2:
            case ShapeTypes.Star:
            case ShapeTypes.Star4:
            case ShapeTypes.Star5:
            case ShapeTypes.Star6:
            case ShapeTypes.Star7:
            case ShapeTypes.Star8:
            case ShapeTypes.Star10:
            case ShapeTypes.Star12:
            case ShapeTypes.Star16:
            case ShapeTypes.Star24:
            case ShapeTypes.Star32:
                Path path = StarPathBuilder.getStarPath(shape, rect); 
                if(path != null)
                {                    
                    drawShape(canvas, control, viewIndex, shape, path, rect, animation, zoom);
                }
                break;
                
            case ShapeTypes.Ribbon:
            case ShapeTypes.Ribbon2:
            case ShapeTypes.EllipseRibbon:
            case ShapeTypes.EllipseRibbon2:    
            case ShapeTypes.VerticalScroll:
            case ShapeTypes.HorizontalScroll:
            case ShapeTypes.Wave:
            case ShapeTypes.DoubleWave:
            case ShapeTypes.LeftRightRibbon:
                pathList = BannerPathBuilder.getFlagExtendPath(shape, rect);
                if(pathList == null)
                {
                    return;
                }
                cnt = pathList.size();                
                for(int i = 0; i < cnt; i++)
                {
                    drawShape(canvas, control, viewIndex, shape, pathList.get(i), rect, animation, zoom);
                }
                break;
                
            case ShapeTypes.Funnel:
            case ShapeTypes.Gear6:
            case ShapeTypes.Gear9:
            case ShapeTypes.LeftCircularArrow:
            case ShapeTypes.PieWedge:
            case ShapeTypes.SwooshArrow:
                path = SmartArtPathBuilder.getStarPath(shape, rect);
                if(path != null)
                {
                    drawShape(canvas, control, viewIndex, shape, path, rect, animation, zoom);
                }
                break;
            default:
                break;
        }
    }
    
    private void processCanvas(Canvas canvas, Rect rect, float angle, boolean flipH, boolean flipV, IAnimation animation)
    {
         if(animation != null)
         {
             angle += animation.getCurrentAnimationInfor().getAngle();
         }
         
         //flip vertical
         if (flipV)
         {
             canvas.translate(rect.left, rect.bottom);
             canvas.scale(1, -1);
             canvas.translate(-rect.left, -rect.top);
             
             angle = -angle;
         }
         //flip horizontal
         if (flipH)
         {
             canvas.translate(rect.right, rect.top);
             canvas.scale(-1, 1);
             canvas.translate(-rect.left, -rect.top);
             
             angle = -angle;
         }
         
         if (angle != 0)
         {
             canvas.rotate(angle, rect.centerX(), rect.centerY());
         }
         
    }
    
    public void drawShape(Canvas canvas,IControl control, int viewIndex, AutoShape shape, ExtendPath pathExtend, Rect rect, IAnimation animation, float zoom)
    {
        // save
        canvas.save();
        Paint paint = PaintKit.instance().getPaint();
        
        processCanvas(canvas, rect, shape.getRotation(), shape.getFlipHorizontal(), shape.getFlipVertical(), animation);
        
        int alpha = paint.getAlpha();
        // draw fill
        BackgroundAndFill fill = pathExtend.getBackgroundAndFill();
        if (fill != null)
        {
            paint.setStyle(Style.FILL);
            BackgroundDrawer.drawPathBackground(canvas, control, viewIndex, fill, rect, animation, zoom, pathExtend.getPath(), paint);
        	paint.setAlpha(alpha);
        }
        
        // draw border
        if (pathExtend.hasLine())
        {
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(pathExtend.getLine().getLineWidth() * zoom);
            if(pathExtend.getLine().isDash() && !pathExtend.isArrowPath())
            {
            	DashPathEffect dashPathEffect = new DashPathEffect(new float[] {5 * zoom, 5 * zoom}, 10);
                paint.setPathEffect(dashPathEffect);
            }            
            
            BackgroundDrawer.drawPathBackground(canvas, control, viewIndex, pathExtend.getLine().getBackgroundAndFill(), rect, animation, zoom, pathExtend.getPath(), paint);
            paint.setAlpha(alpha);
        }        
        
        //restore
        canvas.restore();
    }
    
    /**
     * 
     * @param canvas
     * @param shape
     * @param path
     * @param rect
     */
    public void drawShape(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Path path, Rect rect, float zoom)
    {
        this.drawShape(canvas, control, viewIndex, shape, path, rect, getShapeAnimation(shape), zoom);
    }
    
    /**
     * 
     * @param canvas
     * @param shape
     * @param path
     * @param rect
     */
    public void drawShape(Canvas canvas, IControl control, int viewIndex, AutoShape shape, Path path, Rect rect, IAnimation animation, float zoom)
    {
    	if(path == null)
    	{
    		return;
    	}
    	
        // save
        canvas.save();
        Paint paint = PaintKit.instance().getPaint();
        int color = paint.getColor();
        Style style = paint.getStyle();        
        int alpha = paint.getAlpha();
        
        processCanvas(canvas, rect, shape.getRotation(), shape.getFlipHorizontal(), shape.getFlipVertical(), animation);
        
        // draw fill
        BackgroundAndFill fill = shape.getBackgroundAndFill();
        if (fill != null)
        {
            paint.setStyle(Style.FILL);
            BackgroundDrawer.drawPathBackground(canvas, control, viewIndex, fill, rect, animation, zoom, path, paint);
        	paint.setAlpha(alpha);
        }
        // draw border
        if (shape.hasLine())
        {
            paint.setStyle(Style.STROKE);

            paint.setStrokeWidth(shape.getLine().getLineWidth() * zoom);
            if(shape.getLine().isDash())
            {
            	DashPathEffect dashPathEffect = new DashPathEffect(new float[] {5 * zoom, 5 * zoom}, 10);
                paint.setPathEffect(dashPathEffect);
            }  
            BackgroundDrawer.drawPathBackground(canvas, control, viewIndex, shape.getLine().getBackgroundAndFill(), rect, animation, zoom, path, paint);
        	paint.setAlpha(alpha);
        }        
        
        //restore
        paint.setAlpha(alpha);
        paint.setColor(color);
        paint.setStyle(style);
        canvas.restore();
    }
}
