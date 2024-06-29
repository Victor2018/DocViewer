/*
 * 文件名称:           AutoShapeTypes.java
 *  
 * 编译器:             android2.2
 * 时间:               下午2:31:41
 */
package com.nvqquy98.lib.doc.office.common.autoshape;

import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;

/**
 * autoShape types
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-9-17
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AutoShapeTypes
{
    //
    private static final AutoShapeTypes kit = new AutoShapeTypes();

    /**
     * 
     * @return
     */
    public static AutoShapeTypes instance()
    {
        return kit;
    }
    
    /**
     * 
     */
    public AutoShapeTypes()
    {
        
    }
    
    public int getAutoShapeType(String name)
    {
        if (name != null)
        {
            // line
            if (name.equals("line"))
            {
                return ShapeTypes.Line;
            }
            else if (name.equals("straightConnector1"))
            {
                return ShapeTypes.StraightConnector1;
            }
            else if (name.equals("bentConnector2"))
            {
                return ShapeTypes.BentConnector2;
            }
            else if (name.equals("bentConnector3"))
            {
                return ShapeTypes.BentConnector3;
            }
            else if (name.equals("curvedConnector2"))
            {
                return ShapeTypes.CurvedConnector2;
            }
            else if (name.equals("curvedConnector3"))
            {
                return ShapeTypes.CurvedConnector3;
            }
            else if (name.equals("curvedConnector4"))
            {
                return ShapeTypes.CurvedConnector4;
            }
            else if (name.equals("curvedConnector5"))
            {
                return ShapeTypes.CurvedConnector5;
            }
            // 
            else if (name.equals("rect") || name.equals("Rect"))
            {
                return ShapeTypes.Rectangle;
            }
            else if (name.equals("roundRect"))
            {
                return ShapeTypes.RoundRectangle;
            }
            else if (name.equals("round1Rect"))
            {
                return  ShapeTypes.Round1Rect;
            }
            else if (name.equals("round2SameRect"))
            {
                return ShapeTypes.Round2SameRect;
            }
            else if (name.equals("round2DiagRect"))
            {
                return ShapeTypes.Round2DiagRect;
            }
            else if (name.equals("snip1Rect"))
            {
                return ShapeTypes.Snip1Rect;
            }
            else if (name.equals("snip2SameRect"))
            {
                return ShapeTypes.Snip2SameRect;
            }
            else if (name.equals("snip2DiagRect"))
            {
                return ShapeTypes.Snip2DiagRect;
            }
            else if (name.equals("snipRoundRect"))
            {
                return ShapeTypes.SnipRoundRect;
            }
            else if (name.equals("ellipse"))
            {
                return ShapeTypes.Ellipse;
            }
            else if (name.equals("triangle"))
            {
                return ShapeTypes.Triangle;
            }
            else if (name.equals("rtTriangle"))
            {
                return ShapeTypes.RtTriangle;
            }
            else if (name.equals("parallelogram"))
            {
                return ShapeTypes.Parallelogram;
            }
            else if (name.equals("trapezoid"))
            {
                return ShapeTypes.Trapezoid;
            }
            else if (name.equals("diamond"))
            {
                return ShapeTypes.Diamond;
            }
            else if (name.equals("pentagon"))
            {
                return ShapeTypes.Pentagon;
            }
            else if (name.equals("hexagon"))
            {
                return ShapeTypes.Hexagon;
            }
            else if (name.equals("heptagon"))
            {
                return ShapeTypes.Heptagon;
            }
            else if (name.equals("octagon"))
            {
                return ShapeTypes.Octagon;
            }
            else if (name.equals("decagon"))
            {
                return ShapeTypes.Decagon;
            }
            else if (name.equals("dodecagon"))
            {
                return ShapeTypes.Dodecagon;
            }
            else if (name.equals("pie"))
            {
                return ShapeTypes.Pie;
            }
            else if (name.equals("chord"))
            {
                return ShapeTypes.Chord;
            }
            else if (name.equals("teardrop"))
            {
                return ShapeTypes.Teardrop;
            }
            else if (name.equals("frame"))
            {
                return ShapeTypes.Frame;
            }
            else if (name.equals("halfFrame"))
            {
                return ShapeTypes.HalfFrame;
            }
            else if (name.equals("corner"))
            {
                return ShapeTypes.Corner;
            }
            else if (name.equals("diagStripe"))
            {
                return ShapeTypes.DiagStripe;
            }
            else if (name.equals("plus"))
            {
                return ShapeTypes.Plus;
            }
            else if (name.equals("plaque"))
            {
                return ShapeTypes.Plaque;
            }
            else if (name.equals("can"))
            {
                return ShapeTypes.Can;
            }
            else if (name.equals("cube"))
            {
                return ShapeTypes.Cube;
            }
            else if (name.equals("bevel"))
            {
                return ShapeTypes.Bevel;
            }
            else if (name.equals("donut"))
            {
                return ShapeTypes.Donut;
            }
            else if (name.equals("noSmoking"))
            {
                return ShapeTypes.NoSmoking;
            }
            else if (name.equals("blockArc"))
            {
                return ShapeTypes.BlockArc;
            }
            else if (name.equals("foldedCorner"))
            {
                return ShapeTypes.FoldedCorner;
            }
            else if (name.equals("smileyFace"))
            {
                return ShapeTypes.SmileyFace;
            }
            else if(name.equals("heart"))
            {
                return ShapeTypes.Heart;
            }
            else if (name.equals("lightningBolt"))
            {
                return ShapeTypes.LightningBolt;
            }
            else if(name.equals("sun"))
            {
                return ShapeTypes.Sun;
            }
            else if (name.equals("moon"))
            {
                return ShapeTypes.Moon;
            }
            else if (name.equals("cloud"))
            {
                return ShapeTypes.Cloud;
            }
            else if (name.equals("arc"))
            {
                return ShapeTypes.Arc;
            }
            else if (name.equals("bracketPair"))
            {
                return ShapeTypes.BracketPair;
            }
            else if (name.equals("bracePair"))
            {
                return ShapeTypes.BracePair;
            }
            else if (name.equals("leftBracket"))
            {
                return ShapeTypes.LeftBracket;
            }
            else if (name.equals("rightBracket"))
            {
                return ShapeTypes.RightBracket;
            }
            else if (name.equals("leftBrace"))
            {
                return ShapeTypes.LeftBrace;
            }
            else if (name.equals("rightBrace"))
            {
                return ShapeTypes.RightBrace;
            }
            else if (name.equals("mathPlus"))
            {
                return ShapeTypes.MathPlus;
            }
            else if (name.equals("mathMinus"))
            {
                return ShapeTypes.MathMinus;
            }
            else if (name.equals("mathMultiply"))
            {
                return ShapeTypes.MathMultiply;
            }
            else if (name.equals("mathDivide"))
            {
                return ShapeTypes.MathDivide;
            }
            else if (name.equals("mathEqual"))
            {
                return ShapeTypes.MathEqual;
            }
            else if (name.equals("mathNotEqual"))
            {
                return ShapeTypes.MathNotEqual;
            }
            else if (name.equals("rightArrow"))     ////////////////arrow
            {
                return ShapeTypes.RightArrow;
            }
            else if (name.equals("leftArrow"))
            {
                return ShapeTypes.LeftArrow;
            }
            else if (name.equals("upArrow"))
            {
                return ShapeTypes.UpArrow;
            }
            else if (name.equals("downArrow"))
            {
                return ShapeTypes.DownArrow;
            }
            else if (name.equals("leftRightArrow"))
            {
                return ShapeTypes.LeftRightArrow;
            }
            else if (name.equals("upDownArrow"))
            {
                return ShapeTypes.UpDownArrow;
            }
            else if (name.equals("upDownArrow"))
            {
                return ShapeTypes.UpDownArrow;
            }
            else if (name.equals("quadArrow"))      //十字箭头
            {
                return ShapeTypes.QuadArrow;
            }
            else if (name.equals("leftRightUpArrow"))       //丁字箭头
            {
                return ShapeTypes.LeftRightUpArrow;
            }
            else if (name.equals("bentArrow"))          //圆角右箭头
            {
                return ShapeTypes.BentArrow;
            }
            else if (name.equals("uturnArrow"))          //手杖形箭头
            {
                return ShapeTypes.UturnArrow;
            }
            else if (name.equals("leftUpArrow"))          //直角双向箭头
            {
                return ShapeTypes.LeftUpArrow;
            }
            else if (name.equals("bentUpArrow"))          //直角上箭头
            {
                return ShapeTypes.BentUpArrow;
            }
            else if (name.equals("curvedRightArrow"))          //左弧形箭头
            {
                return ShapeTypes.CurvedRightArrow;
            }
            else if (name.equals("curvedLeftArrow"))          //右弧形箭头
            {
                return ShapeTypes.CurvedLeftArrow;
            }
            else if (name.equals("curvedUpArrow"))          //下弧形箭头
            {
                return ShapeTypes.CurvedUpArrow;
            }
            else if (name.equals("curvedDownArrow"))          //上弧形箭头
            {
                return ShapeTypes.CurvedDownArrow;
            }
            else if (name.equals("stripedRightArrow"))          //虚尾箭头
            {
                return ShapeTypes.StripedRightArrow;
            }
            else if (name.equals("notchedRightArrow"))          //燕尾形箭头
            {
                return ShapeTypes.NotchedRightArrow;
            }
            else if (name.equals("homePlate"))          //五边形
            {
                return ShapeTypes.HomePlate;
            }
            else if (name.equals("chevron"))          //燕尾形
            {
                return ShapeTypes.Chevron;
            }
            else if (name.equals("rightArrowCallout"))          //右箭头标注
            {
                return ShapeTypes.RightArrowCallout;
            }
            else if (name.equals("leftArrowCallout"))          //
            {
                return ShapeTypes.LeftArrowCallout;
            }
            else if (name.equals("downArrowCallout"))          //
            {
                return ShapeTypes.DownArrowCallout;
            }
            else if (name.equals("upArrowCallout"))          //
            {
                return ShapeTypes.UpArrowCallout;
            }
            else if (name.equals("leftRightArrowCallout"))          //左右箭头标注
            {
                return ShapeTypes.LeftRightArrowCallout;
            }
            else if (name.equals("quadArrowCallout"))          //十字箭头标注
            {
                return ShapeTypes.QuadArrowCallout;
            }
            else if (name.equals("circularArrow"))          //环形箭头
            {
                return ShapeTypes.CircularArrow;
            }
            // flowChart
            else if (name.equals("flowChartProcess"))
            {
                return ShapeTypes.FlowChartProcess;
            }
            else if (name.equals("flowChartAlternateProcess"))
            {
                return ShapeTypes.FlowChartAlternateProcess;
            }
            else if (name.equals("flowChartDecision"))
            {
                return ShapeTypes.FlowChartDecision;
            }
            else if (name.equals("flowChartInputOutput"))
            {
                return ShapeTypes.FlowChartInputOutput;
            }
            else if (name.equals("flowChartPredefinedProcess"))
            {
                return ShapeTypes.FlowChartPredefinedProcess;
            }
            else if (name.equals("flowChartInternalStorage"))
            {
                return ShapeTypes.FlowChartInternalStorage;
            }
            else if (name.equals("flowChartDocument"))
            {
                return ShapeTypes.FlowChartDocument;
            }
            else if (name.equals("flowChartMultidocument"))
            {
                return ShapeTypes.FlowChartMultidocument;
            }
            else if (name.equals("flowChartTerminator"))
            {
                return ShapeTypes.FlowChartTerminator;
            }
            else if (name.equals("flowChartPreparation"))
            {
                return ShapeTypes.FlowChartPreparation;
            }
            else if (name.equals("flowChartManualInput"))
            {
                return ShapeTypes.FlowChartManualInput;
            }
            else if (name.equals("flowChartManualOperation"))
            {
                return ShapeTypes.FlowChartManualOperation;
            }
            else if (name.equals("flowChartConnector"))
            {
                return ShapeTypes.FlowChartConnector;
            }
            else if (name.equals("flowChartOffpageConnector"))
            {
                return ShapeTypes.FlowChartOffpageConnector;
            }
            else if (name.equals("flowChartPunchedCard"))
            {
                return ShapeTypes.FlowChartPunchedCard;
            }
            else if (name.equals("flowChartPunchedTape"))
            {
                return ShapeTypes.FlowChartPunchedTape;
            }
            else if (name.equals("flowChartSummingJunction"))
            {
                return ShapeTypes.FlowChartSummingJunction;
            }
            else if (name.equals("flowChartOr"))
            {
                return ShapeTypes.FlowChartOr;
            }
            else if (name.equals("flowChartCollate"))
            {
                return ShapeTypes.FlowChartCollate;
            }
            else if (name.equals("flowChartSort"))
            {
                return ShapeTypes.FlowChartSort;
            }
            else if (name.equals("flowChartExtract"))
            {
                return ShapeTypes.FlowChartExtract;
            }
            else if (name.equals("flowChartMerge"))
            {
                return ShapeTypes.FlowChartMerge;
            }
            else if (name.equals("flowChartOnlineStorage"))
            {
                return ShapeTypes.FlowChartOnlineStorage;
            }
            else if (name.equals("flowChartDelay"))
            {
                return ShapeTypes.FlowChartDelay;
            }
            else if (name.equals("flowChartMagneticTape"))
            {
                return ShapeTypes.FlowChartMagneticTape;
            }
            else if (name.equals("flowChartMagneticDisk"))
            {
                return ShapeTypes.FlowChartMagneticDisk;
            }
            else if (name.equals("flowChartMagneticDrum"))
            {
                return ShapeTypes.FlowChartMagneticDrum;
            }
            else if (name.equals("flowChartDisplay"))
            {
                return ShapeTypes.FlowChartDisplay;
            }
            // wedgecallout
            else if (name.equals("wedgeRectCallout"))
            {
                return ShapeTypes.WedgeRectCallout;
            }
            else if (name.equals("wedgeRoundRectCallout"))
            {
                return ShapeTypes.WedgeRoundRectCallout;
            }
            else if (name.equals("wedgeEllipseCallout"))
            {
                return ShapeTypes.WedgeEllipseCallout;
            }
            else if (name.equals("cloudCallout"))
            {
                return ShapeTypes.CloudCallout;
            }
            else if (name.equals("borderCallout1"))
            {
                return ShapeTypes.BorderCallout1;
            }
            else if (name.equals("borderCallout2"))
            {
                return ShapeTypes.BorderCallout2;
            }
            else if (name.equals("borderCallout3"))
            {
                return ShapeTypes.BorderCallout3;
            }
            else if (name.equals("accentCallout1"))
            {
                return ShapeTypes.AccentCallout1;
            }
            else if (name.equals("accentCallout2"))
            {
                return ShapeTypes.AccentCallout2;
            }
            else if (name.equals("accentCallout3"))
            {
                return ShapeTypes.AccentCallout3;
            }
            else if (name.equals("callout1"))
            {
                return ShapeTypes.Callout1;
            }
            else if (name.equals("callout2"))
            {
                return ShapeTypes.Callout2;
            }
            else if (name.equals("callout3"))
            {
                return ShapeTypes.Callout3;
            }
            else if (name.equals("accentBorderCallout1"))
            {
                return ShapeTypes.AccentBorderCallout1;
            }
            else if (name.equals("accentBorderCallout2"))
            {
                return ShapeTypes.AccentBorderCallout2;
            }
            else if (name.equals("accentBorderCallout3"))
            {
                return ShapeTypes.AccentBorderCallout3;
            }
            else if (name.equals("actionButtonBackPrevious"))       //actionButton
            {
                return ShapeTypes.ActionButtonBackPrevious;
            }
            else if (name.equals("actionButtonForwardNext"))
            {
                return ShapeTypes.ActionButtonForwardNext;
            }
            else if (name.equals("actionButtonBeginning"))
            {
                return ShapeTypes.ActionButtonBeginning;
            }
            else if (name.equals("actionButtonEnd"))
            {
                return ShapeTypes.ActionButtonEnd;
            }
            else if (name.equals("actionButtonHome"))
            {
                return ShapeTypes.ActionButtonHome;
            }
            else if (name.equals("actionButtonInformation"))
            {
                return ShapeTypes.ActionButtonInformation;
            }
            else if (name.equals("actionButtonReturn"))
            {
                return ShapeTypes.ActionButtonReturn;
            }
            else if (name.equals("actionButtonMovie"))
            {
                return ShapeTypes.ActionButtonMovie;
            }
            else if (name.equals("actionButtonDocument"))
            {
                return ShapeTypes.ActionButtonDocument;
            }
            else if (name.equals("actionButtonSound"))
            {
                return ShapeTypes.ActionButtonSound;
            }
            else if (name.equals("actionButtonHelp"))
            {
                return ShapeTypes.ActionButtonHelp;
            }
            else if (name.equals("actionButtonBlank"))
            {
                return ShapeTypes.ActionButtonBlank;
            }
            else if (name.equals("irregularSeal1"))              //star and flag
            {
                return ShapeTypes.IrregularSeal1;
            }
            else if (name.equals("irregularSeal2"))
            {
                return ShapeTypes.IrregularSeal2;
            }
            else if (name.equals("star4"))
            {
                return ShapeTypes.Star4;
            }
            else if (name.equals("star5"))
            {
                return ShapeTypes.Star5;
            }
            else if (name.equals("star6"))
            {
                return ShapeTypes.Star6;
            }
            else if (name.equals("star7"))
            {
                return ShapeTypes.Star7;
            }
            else if (name.equals("star8"))
            {
                return ShapeTypes.Star8;
            }
            else if (name.equals("star10"))
            {
                return ShapeTypes.Star10;
            }
            else if (name.equals("star12"))
            {
                return ShapeTypes.Star12;
            }
            else if (name.equals("star16"))
            {
                return ShapeTypes.Star16;
            }
            else if (name.equals("star24"))
            {
                return ShapeTypes.Star24;
            }
            else if (name.equals("star32"))
            {
                return ShapeTypes.Star32;
            }
            else if (name.equals("ribbon2"))
            {
                return ShapeTypes.Ribbon2;
            }
            else if (name.equals("ribbon"))
            {
                return ShapeTypes.Ribbon;
            }
            else if (name.equals("ellipseRibbon2"))
            {
                return ShapeTypes.EllipseRibbon2;
            }
            else if (name.equals("ellipseRibbon"))
            {
                return ShapeTypes.EllipseRibbon;
            }
            else if (name.equals("verticalScroll"))
            {
                return ShapeTypes.VerticalScroll;
            }
            else if (name.equals("horizontalScroll"))
            {
                return ShapeTypes.HorizontalScroll;
            }
            else if (name.equals("wave"))
            {
                return ShapeTypes.Wave;
            }
            else if (name.equals("doubleWave"))
            {
                return ShapeTypes.DoubleWave;
            }
            else if (name.equals("funnel"))
            {
                return ShapeTypes.Funnel;
            }
            else if (name.equals("gear6"))
            {
                return ShapeTypes.Gear6;
            }
            else if (name.equals("gear9"))
            {
                return ShapeTypes.Gear9;
            }
            else if (name.equals("leftCircularArrow"))
            {
                return ShapeTypes.LeftCircularArrow;
            }
            else if (name.equals("leftRightRibbon"))
            {
                return ShapeTypes.LeftRightRibbon;
            }
            else if (name.equals("pieWedge"))
            {
                return ShapeTypes.PieWedge;
            }
            else if (name.equals("swooshArrow"))
            {
                return ShapeTypes.SwooshArrow;
            }
        }
        return ShapeTypes.NotPrimitive;
    }
}
