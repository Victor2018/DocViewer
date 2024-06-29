/*
 * 文件名称:          ChartReader.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:59:09
 */
package com.nvqquy98.lib.doc.office.fc.xls.Reader.drawing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.autoshape.AutoShapeDataKit;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.constant.SchemeClrConstant;
import com.nvqquy98.lib.doc.office.fc.LineKit;
import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.io.SAXReader;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.ss.model.drawing.TextParagraph;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.ChartFactory;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.AbstractChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.PointStyle;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.RoundChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.XYChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.ColumnBarChart.Type;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.CategorySeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYMultipleSeriesDataset;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYSeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.DefaultRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.SimpleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYSeriesRenderer;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-3-2
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ChartReader
{
	/**
	 * Axis Position Value
	 */
	private static final short AxisPosition_Bottom = 0;
	private static final short AxisPosition_Left = 1;
	private static final short AxisPosition_Right = 2;
	private static final short AxisPosition_Top = 3;
	
    private static final String themeIndex[] = {
        SchemeClrConstant.SCHEME_ACCENT1,
        SchemeClrConstant.SCHEME_ACCENT2,
        SchemeClrConstant.SCHEME_ACCENT3,
        SchemeClrConstant.SCHEME_ACCENT4,
        SchemeClrConstant.SCHEME_ACCENT5, 
        SchemeClrConstant.SCHEME_ACCENT6};
    
    private static final double tints[] = {-0.25, 0, 0.4, 0.6, 0.8, -0.5};
    
    private static ChartReader reader = new ChartReader();
    
    /**
     * 
     */
    public static ChartReader instance()
    {
        return reader;
    }
    
    /**
     * get AbstractChart
     * @param chartPart
     * @param sheet
     * @return
     */
    public AbstractChart read(IControl control, ZipPackage zipPackage, PackagePart  chartPart, Map<String, Integer> schemeColor, byte appType) throws Exception
    {
        this.schemeColor = schemeColor;
        this.type = AbstractChart.CHART_UNKOWN;
        this.chart = null;
        maxX = Double.MIN_VALUE;
        minX = Double.MAX_VALUE;
        maxY = Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        
        hasMaxX = false;
        hasMinX = false;
        hasMaxY = false;
        hasMinY = false;
        
        SAXReader saxreader = new SAXReader();  
        InputStream in = chartPart.getInputStream();
        Document chartDoc = saxreader.read(in);
        in.close();
        
        Element root = chartDoc.getRootElement();
        
        BackgroundAndFill fill = null;
        Line line = null;
        Element spPr = root.element("spPr");
        if (spPr != null)
        {
        	if(spPr.element("noFill") == null)
        	{
        		fill = AutoShapeDataKit.processBackground(control, zipPackage, chartPart, spPr, schemeColor);
        		if(fill == null)
        		{
        			//auto fill
        			fill = new BackgroundAndFill();
                	fill.setFillType(BackgroundAndFill.FILL_SOLID);
                	fill.setForegroundColor(0xFFFFFFFF);
        		}
        	}
            
        	if(spPr.element("ln") != null)
        	{
        		line = LineKit.createChartLine(control, zipPackage, chartPart, spPr.element("ln"), schemeColor);
        	}
        	else
        	{
        		line = new Line();
            	BackgroundAndFill lineFill = new BackgroundAndFill();
            	lineFill.setFillType(BackgroundAndFill.FILL_SOLID);
            	lineFill.setForegroundColor(0xFF747474);
    	       	line.setBackgroundAndFill(lineFill);
    	       	line.setLineWidth(1);
        	}
        }
        else
        {
        	//auto fill and line
        	fill = new BackgroundAndFill();
        	fill.setFillType(BackgroundAndFill.FILL_SOLID);
        	fill.setForegroundColor(0xFFFFFFFF);
        	
        	line = new Line();
        	BackgroundAndFill lineFill = new BackgroundAndFill();
        	lineFill.setFillType(BackgroundAndFill.FILL_SOLID);
        	lineFill.setForegroundColor(0xFF747474);
	       	line.setBackgroundAndFill(lineFill);
	       	line.setLineWidth(1);
        }
        
        //default text size
        float defaultFontSize = getTextSize(root.element("txPr"));
        
        //
        AbstractChart abstractChart = buildAChart(root.element("chart"), defaultFontSize, appType);
        DefaultRenderer renderer = null;
        if(abstractChart instanceof XYChart)
        {
        	Element plotArea = root.element("chart").element("plotArea");
        	 BackgroundAndFill plotAreaFill = null;
             Line plotAreaFrame = null;
             spPr = plotArea.element("spPr");
             if (spPr != null)
             {
             	if(spPr.element("noFill") == null)
             	{
             		plotAreaFill = AutoShapeDataKit.processBackground(control, zipPackage, chartPart, spPr, schemeColor);
             	}
             	
             	plotAreaFrame = LineKit.createChartLine(control, zipPackage, chartPart, spPr.element("ln"), schemeColor);
             }
             
        	renderer = ((XYChart)abstractChart).getRenderer();
        	
        	((XYMultipleSeriesRenderer)renderer).setSeriesBackgroundColor(plotAreaFill);
        	((XYMultipleSeriesRenderer)renderer).setSeriesFrame(plotAreaFrame);
        }
        else if(abstractChart instanceof RoundChart)
        {
        	renderer = ((RoundChart)abstractChart).getRenderer();
        }
        
        if(renderer != null)
        {
        	renderer.setDefaultFontSize(defaultFontSize);
            renderer.setBackgroundAndFill(fill);
            renderer.setChartFrame(line);
        }
        
        dispose();
        
        return abstractChart;
    }
    
    private float getTextSize(Element txPr)
    {
    	float fontSize = 12;
    	Element ele = txPr;
    	if(ele != null && (ele = ele.element("p")) != null 
        		&& (ele = ele.element("pPr")) != null
        		&& (ele = ele.element("defRPr")) != null)
        {
        	String sz = ele.attributeValue("sz");
        	if(sz != null && sz.length() > 0)
        	{
        		fontSize = Integer.parseInt(sz) / 100f;
        	}
        }
    	
    	return fontSize;
    }
    
    private void processLegend(Element legend, DefaultRenderer renderer, AbstractChart abstractChart)
    {
    	if(legend != null && renderer != null)
    	{
    		renderer.setShowLegend(true);
    		
    		//legend position
    		byte legendPos = AbstractChart.LegendPosition_Right;
    		if(legend.element("legendPos") != null)
    		{
    			String val = legend.element("legendPos").attributeValue("val");
    			if("l".equalsIgnoreCase(val))
    			{
    				legendPos = AbstractChart.LegendPosition_Left;
    			}
    			else if("t".equalsIgnoreCase(val))
    			{
    				legendPos = AbstractChart.LegendPosition_Top;
    			}
    			else if("b".equalsIgnoreCase(val))
    			{
    				legendPos = AbstractChart.LegendPosition_Bottom;
    			}	
    		}
    		abstractChart.setLegendPosition(legendPos);
    		
    		//legend text size
    		float fontSize = renderer.getDefaultFontSize();
    		if(legend.element("txPr") != null)
    		{
    			fontSize = getTextSize(legend.element("txPr"));
    		}

			renderer.setLegendTextSize(fontSize);
    	}
    }
    
    /**
     * 
     * @param sheet
     * @param chartElement
     * @param bgColor
     * @return
     */
    private AbstractChart buildAChart(Element chartElement, float defaultFontSize, byte appType)
    {        
        Element plotArea = chartElement.element("plotArea");
        
        DefaultRenderer renderer = null;
        XYMultipleSeriesDataset dataset = null;
        //for pie chart
        CategorySeries pieDataset = null;
        
        PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND, PointStyle.SQUARE, PointStyle.TRIANGLE,
            PointStyle.X, PointStyle.CIRCLE };   
        
        getChartInfo(plotArea);
        AbstractChart abstractChart = null;
        switch(type)
        {
            case AbstractChart.CHART_AREA:
            case AbstractChart.CHART_BAR:
            case AbstractChart.CHART_STOCK:
            case AbstractChart.CHART_SURFACE:
            case AbstractChart.CHART_DOUGHNUT:
            case AbstractChart.CHART_BUBBLE:
            case AbstractChart.CHART_RADAR:
                renderer = buildXYMultipleSeriesRenderer(plotArea, defaultFontSize, appType);                
                dataset = getXYMultipleSeriesDataset(chart, type, (XYMultipleSeriesRenderer)renderer);
                abstractChart = ChartFactory.getColumnBarChart(dataset, (XYMultipleSeriesRenderer)renderer, Type.DEFAULT);
                break;
                
            case AbstractChart.CHART_LINE:
                renderer = buildXYMultipleSeriesRenderer(plotArea, defaultFontSize, appType); 
                dataset = getXYMultipleSeriesDataset(chart, type, (XYMultipleSeriesRenderer)renderer, styles);               
                ((XYMultipleSeriesRenderer)renderer).setYLabels(10);
                abstractChart = ChartFactory.getLineChart(dataset, (XYMultipleSeriesRenderer)renderer);  
                break;
                
            case AbstractChart.CHART_PIE:
                renderer = buildDefaultRenderer();
                renderer.setZoomEnabled(true);
                pieDataset = buildCategoryDataset(chart, renderer);
                abstractChart = ChartFactory.getPieChart(pieDataset, renderer);
                break;
                
            case AbstractChart.CHART_SCATTER:
                renderer = buildXYMultipleSeriesRenderer(plotArea, defaultFontSize, appType); 
                dataset = getXYMultipleSeriesDataset(chart, type, (XYMultipleSeriesRenderer)renderer, styles);
                ((XYMultipleSeriesRenderer)renderer).setXLabels(10);
                ((XYMultipleSeriesRenderer)renderer).setYLabels(10);
                for (int i = 0; i < renderer.getSeriesRendererCount(); i++) 
                {
                    ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
                }               
                abstractChart = ChartFactory.getScatterChart(dataset, (XYMultipleSeriesRenderer)renderer);
                break;
                
            default:                
                break;
        }
        
        //process chart title
        Element titleElement = chartElement.element("title");
        if(titleElement != null)
        {
        	List<TextParagraph> title = getTitle(titleElement);
            String chartTitle = "";
            float fontSize = 0;
            if(title != null && title.size() > 0)
            {
                for(int i = 0; i< title.size(); i++)
                {
                    chartTitle += title.get(i).getTextRun();
                    if(title.get(i).getFont() != null)
                    {
                    	fontSize = Math.max(fontSize, (int)title.get(i).getFont().getFontSize());
                    }                
                }            
            }
            
            renderer.setShowChartTitle(true);
            if(fontSize == 0)
        	{
        		fontSize = defaultFontSize;
        	}
        	renderer.setChartTitleTextSize(fontSize);
        	
            if(chartTitle.length() == 0)
            {
            	if(dataset != null)
            	{
            		if(dataset !=  null && dataset.getSeriesCount() == 1)
                	{
                		chartTitle = dataset.getSeriesAt(0).getTitle();
                	}
                	else
                	{
                		chartTitle = "Chart Title";
                	}
            	}
            	else if(pieDataset != null)
            	{
            		chartTitle = pieDataset.getTitle();
            	}            	
            }
            renderer.setChartTitle(chartTitle);            
        }        
        else
        {
        	renderer.setShowChartTitle(false);
        }
        
        //process chart legend
        Element legend = chartElement.element("legend");
        if(legend != null)
        {
        	processLegend(chartElement.element("legend"), renderer, abstractChart);
        }
        else
        {
        	renderer.setShowLegend(false);
        }
        
        if(abstractChart != null)
        {
        	abstractChart.setCategoryAxisTextColor(schemeColor.get("tx1"));
        }
        return abstractChart;
    }
    
    /**
     * 
     * @param titleElement
     * @return
     */
    private List<TextParagraph> getTitle(Element titleElement)
    {
        if(titleElement != null
            &&titleElement.element("tx") != null
            && titleElement.element("tx").element("rich") != null)
        {
            //Rich Text
            Element rich = titleElement.element("tx").element("rich");
            
            Element bodyPr = rich.element("bodyPr");
            short vertical = -1;
            if(bodyPr != null)
            {
                vertical = DrawingReader.getVerticalByString(bodyPr.attributeValue("anchor"));
            }
            
            //Text Paragraphs            
            @ SuppressWarnings("unchecked")
            Iterator<Element> iter = rich.elements("p").iterator();
            TextParagraph textParagraph;
            List<TextParagraph> paragraphs = new ArrayList<TextParagraph>();
            
            while(iter.hasNext())
            {
                textParagraph = DrawingReader.getTextParagraph(iter.next());
                if(textParagraph != null)
                {
                    if(vertical > -1)
                    {
                        textParagraph.setVerticalAlign(vertical);
                    }
                    paragraphs.add(textParagraph);
                }
            }
            
            return paragraphs;
        }
        
        return null;
    }
    
    /**
     * 
     * @param themeColor
     * @param solidFillElement
     * @return
     */
    private int getColor(Element solidFillElement)
    {
        Element clr;
        int color = -1;
        if(solidFillElement.element("srgbClr") != null)
        {
            clr = solidFillElement.element("srgbClr");
            String val = clr.attributeValue("val");
            if(val.length() > 6)
            {
                val = val.substring(val.length() - 6);
            }
            color = Integer.parseInt(val, 16);
            color = (0xFF << 24) | color;
        }
        else if(solidFillElement.element("schemeClr") != null)
        {
            clr = solidFillElement.element("schemeClr");
            color = schemeColor.get(clr.attributeValue("val"));
            
            if(clr.element("lumMod") != null)
            {
                double tint = Integer.parseInt(clr.element("lumMod").attributeValue("val")) / 100000.0;
                if(clr.element("lumOff") != null)
                {
                    tint = Integer.parseInt(clr.element("lumOff").attributeValue("val")) / 100000.0;                   
                }
                else
                {
                    tint = tint - 1; 
                }
                color = ColorUtil.instance().getColorWithTint(color, tint);
            }
        }
        else if(solidFillElement.element("sysClr") != null)
        {
            clr = solidFillElement.element("sysClr");
            //get system color
            color = Integer.parseInt( clr.attributeValue("lastClr"), 16);
            color = (0xFF << 24) | color;
        }
        
        return color;
    }
   
    /**
     * 
     * @param plotArea
     */
    private void getChartInfo(Element plotArea)
    {
        if(plotArea.element("barChart") != null)
        {
            chart = plotArea.element("barChart");
            type = AbstractChart.CHART_BAR;
        }
        else if(plotArea.element("bar3DChart") != null)
        {
            chart = plotArea.element("bar3DChart");
            type = AbstractChart.CHART_BAR;
        }
        else if(plotArea.element("pieChart") != null)
        {
            chart = plotArea.element("pieChart");
            type = AbstractChart.CHART_PIE;
        }
        else if(plotArea.element("pie3DChart") != null)
        {
            chart = plotArea.element("pie3DChart");
            type = AbstractChart.CHART_PIE;
        }
        else if(plotArea.element("ofPieChart") != null)
        {
        	 chart = plotArea.element("ofPieChart");
             type = AbstractChart.CHART_PIE;
        }
        else if(plotArea.element("lineChart") != null)
        {
            chart = plotArea.element("lineChart");
            type = AbstractChart.CHART_LINE;
        }
        else if(plotArea.element("line3DChart") != null)
        {
            chart = plotArea.element("line3DChart");
            type = AbstractChart.CHART_LINE;
        }
        else if(plotArea.element("scatterChart") != null)
        {
            chart = plotArea.element("scatterChart");
            type = AbstractChart.CHART_SCATTER;   
        }
        else if(plotArea.element("areaChart") != null)
        {
        	chart = plotArea.element("areaChart");
            type = AbstractChart.CHART_AREA;
        }
        else if(plotArea.element("area3DChart") != null)
        {
        	chart = plotArea.element("area3DChart");
            type = AbstractChart.CHART_AREA;
        }
        else if(plotArea.element("stockChart") != null)
        {
        	chart = plotArea.element("stockChart");
            type = AbstractChart.CHART_STOCK;
        }
        else if(plotArea.element("surfaceChart") != null)
        {
        	chart = plotArea.element("surfaceChart");
            type = AbstractChart.CHART_SURFACE;
        }
        else if(plotArea.element("surface3DChart") != null)
        {
        	chart = plotArea.element("surface3DChart");
            type = AbstractChart.CHART_SURFACE;
        }
        else if(plotArea.element("doughnutChart") != null)
        {
        	chart = plotArea.element("doughnutChart");
            type = AbstractChart.CHART_DOUGHNUT;
        }
        else if(plotArea.element("bubbleChart") != null)
        {
        	chart = plotArea.element("bubbleChart");
//            type = AbstractChart.CHART_BUBBLE;
        	type = AbstractChart.CHART_SCATTER;
        }
        else if(plotArea.element("radarChart") != null)
        {
        	chart = plotArea.element("radarChart");
            type = AbstractChart.CHART_RADAR;
        }
    }
    
    /**
     * 
     * @param sheet
     * @param plotArea
     * @param chartTitle
     * @param bgColor
     * @param styles
     * @return
     */
    private XYMultipleSeriesRenderer buildXYMultipleSeriesRenderer(Element plotArea, float defaultFontSize, byte appType) 
    {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        //renderer.setMargins(new int[] {40, 50, 40, 20});
        renderer.setXTitleTextSize(defaultFontSize);
        renderer.setYTitleTextSize(defaultFontSize);

        renderer.setLabelsTextSize(defaultFontSize);
        
        List<TextParagraph> xLabel = null;
        List<TextParagraph> yLabel = null;
        {
            //scatter chart
        	List<Element> valAxs = plotArea.elements("valAx");
        	List<Element> catAxs = plotArea.elements("catAx");
        	
        	List<Element> eles = new ArrayList<Element>();
        	for(int i = 0; i < valAxs.size(); i++)
        	{
        		eles.add(valAxs.get(i));
        	}
        	
        	for(int i = 0; i < catAxs.size(); i++)
        	{
        		eles.add(catAxs.get(i));
        	}
        	
        	for(int i = 0; i < eles.size(); i++)
        	{
        		Element axPos = eles.get(i).element("axPos");
        		if(axPos != null)
        		{
        			short sAxpos = getAxisPosition(axPos);
        			switch(sAxpos)
        			{
	        			case AxisPosition_Left:
	        			case AxisPosition_Right:
	        				yLabel = getTitle(eles.get(i).element("title"));
	        				
	        				if(eles.get(i).element("majorGridlines") != null || eles.get(i).element("minorGridlines") != null)
	        				{
	        					renderer.setShowGridH(true);
	        				}
	        				break;
	        				
	        			case AxisPosition_Top:
	        			case AxisPosition_Bottom:
	        				xLabel = getTitle(eles.get(i).element("title"));
	        				
	        				if(eles.get(i).element("majorGridlines") != null || eles.get(i).element("minorGridlines") != null)
	        				{
	        					renderer.setShowGridV(true);
	        				}
	        				break;
        			}
        			
        			getMaxMinValue(eles.get(i).element("scaling"), sAxpos);
        		}
        	}
        }
        
        //x label
        if(xLabel != null && xLabel.size() > 0)
        {
            renderer.setXTitle(xLabel.get(0).getTextRun());
        }
        
        //y label
        if(yLabel != null && yLabel.size() > 0)
        {
            renderer.setYTitle(yLabel.get(0).getTextRun());
        }
        
        renderer.setLabelsColor(schemeColor.get("tx1"));
        renderer.setGridColor(schemeColor.get("tx1"));
        renderer.setAxesColor(schemeColor.get("tx1"));
        return renderer;
    }
    
    private short getAxisPosition(Element axPos)
    {
    	String val = null;
    	if(axPos != null && (val = axPos.attributeValue("val")) != null)
		{
			if("b".equalsIgnoreCase(val))
			{
				return AxisPosition_Bottom;
			}
			else if("l".equalsIgnoreCase(val))
			{
				return AxisPosition_Left;
			}
			else if("r".equalsIgnoreCase(val))
			{
				return AxisPosition_Right;
			}
			else if("t".equalsIgnoreCase(val))
			{
				return AxisPosition_Top;
			}
		}
    	
    	return AxisPosition_Left;
    }
    
    private void getMaxMinValue(Element scaling, short axPos)
    {
    	switch(axPos)
		{
			case AxisPosition_Left:
			case AxisPosition_Right:
				if(scaling.element("max") != null)
		        {
		            hasMaxY = true;
		            maxY = Double.parseDouble(scaling.element("max").attributeValue("val"));
		        }
		        
		        if(scaling.element("min") != null)
		        {
		            hasMinY = true;
		            minY = Double.parseDouble(scaling.element("min").attributeValue("val"));
		        }   
				break;
				
			case AxisPosition_Top:
			case AxisPosition_Bottom:
				if(scaling.element("max") != null)
		        {
		            hasMaxX = true;
		            maxX = Double.parseDouble(scaling.element("max").attributeValue("val"));
		        }
		        
		        if(scaling.element("min") != null)
		        {
		            hasMinX = true;
		            minX = Double.parseDouble(scaling.element("min").attributeValue("val"));
		        }   
				break;
		}
    }
    
    /**
     * get series title
     * @param seriesTextElement
     * @return
     */
    private String getSeriesTitle(Element seriesTextElement)
    {
        if(seriesTextElement == null)
        {
            return null;
        }
        
        if(seriesTextElement.element("strRef") != null)
        {
            //String Cache
            Element stringCache = seriesTextElement.element("strRef").element("strCache");
            
            //Text Value
            if(stringCache.element("pt") != null)
            {
                Element text = stringCache.element("pt").element("v");
                
                return text.getText();
            }
        }
        else if(seriesTextElement.element("v") != null)
        {
            return seriesTextElement.element("v").getText();
        }
        
        return null;
    }
    
    /**
     * 
     * @param sheet
     * @param renderer
     * @param series
     * @param styles
     */
    private void setSeriesRendererProp(XYMultipleSeriesRenderer renderer, Element series, PointStyle[] styles)
    {
        //forecolor and point style of series
        int seriesOrder = Integer.parseInt(series.element("order").attributeValue("val"));
       
        XYSeriesRenderer r = new XYSeriesRenderer(); 
        
        int index = seriesOrder % themeIndex.length;
        index = schemeColor.get(themeIndex[index]);   
        index = ColorUtil.instance().getColorWithTint(index, tints[seriesOrder / themeIndex.length]);
        r.setColor(index);
        
        if(styles != null && styles.length > 0)
        {
            index = seriesOrder % (styles.length - 1);
            r.setPointStyle(styles[index]);
        }

        renderer.addSeriesRenderer(r);      
        
        //x label
        Element cache = null;
        boolean isNumCache = false;
        if(series.element("cat") != null)
        {
            //Number Cache
        	if(series.element("cat").element("numRef") != null)
        	{
        		cache = series.element("cat").element("numRef").element("numCache");
        		isNumCache = true;
        	}
        	else if(series.element("cat").element("strRef") != null)
        	{
        		cache = series.element("cat").element("strRef").element("strCache");
        	}
        		
            
        }
        else if(series.element("xVal") != null)
        {           
        	if(series.element("xVal").element("strRef") != null)
        	{
        		cache = series.element("xVal").element("strRef").element("strCache");
        	}
        }
        
        if(cache != null)
        {
        	@ SuppressWarnings("unchecked")
            Iterator<Element> iter = cache.elements("pt").iterator();
            Element pt;
            index = 1;
            while(iter.hasNext())
            {
                pt = iter.next();
                renderer.addXTextLabel(index++, pt.element("v").getText());
            }            
        }
    }
    
    /**
     * 
     * @param series
     * @return
     */
    private Object getSeries(Element series, short chartType)
    {
        //get series name
        String seriesName = null;
        if( getSeriesTitle(series.element("tx")) != null)
        {
        	seriesName = getSeriesTitle(series.element("tx"));            
        }
        else
        {
        	seriesName = "Series " + (Integer.parseInt(series.element("order").attributeValue("val")) + 1);
        }         
        
        //get series cell value            
        //Number Cache        
        if(chartType != AbstractChart.CHART_SCATTER)
        {
        	if(series.element("val") != null)
            {
        		CategorySeries aSeries= new CategorySeries(seriesName);
        		Element number = null;
                if(series.element("val").element("numRef") != null)
                {
                    number = series.element("val").element("numRef").element("numCache"); 
                }
                else if(series.element("val").element("numLit") != null)
                {
                    number = series.element("val").element("numLit");
                }
                
                Element numPoint;
                double value;
                @ SuppressWarnings("unchecked")
                Iterator<Element> iter = number.elements("pt").iterator();
                while(iter.hasNext())
                {
                    numPoint = iter.next();
                    value = Double.parseDouble(numPoint.element("v").getText());
                    aSeries.add(value);
                }
                
                return aSeries;
            }
        }
        else
        {        	
        	//scatter
        	Element xNumber = null;
        	Element yNumber = null;
        	
        	if(series.element("xVal") != null)
            {
        		if(series.element("xVal").element("numRef") != null)
            	{
        			xNumber = series.element("xVal").element("numRef").element("numCache");
            	}
            }
        	
        	if(series.element("yVal") != null)
        	{
        		if(series.element("yVal").element("numRef") != null)
                {
        			yNumber = series.element("yVal").element("numRef").element("numCache"); 
                }
                else if(series.element("yVal").element("numLit") != null)
                {
                	yNumber = series.element("yVal").element("numLit");
                }
        	}
        	
        	if(xNumber != null && yNumber != null)
        	{
        		XYSeries aSeries= new XYSeries(seriesName);
        		Element xNumPoint;
        		Element yNumPoint;
                double valueX;
                double valueY;
                @ SuppressWarnings("unchecked")
                Iterator<Element> iterX = xNumber.elements("pt").iterator();
                @SuppressWarnings("unchecked")
				Iterator<Element> iterY = yNumber.elements("pt").iterator();
                while(iterX.hasNext() && iterY.hasNext())
                {
                	xNumPoint = iterX.next();
                	yNumPoint = iterY.next();
                	valueX = Double.parseDouble(xNumPoint.element("v").getText());
                	valueY = Double.parseDouble(yNumPoint.element("v").getText());
                    aSeries.add(valueX,valueY);
                } 
                
                return aSeries;
        	}
        	else if(yNumber != null)
            {
        		CategorySeries aSeries= new CategorySeries(seriesName);
        		Element yNumPoint;
                double valueY;
                @SuppressWarnings("unchecked")
				Iterator<Element> iterY = yNumber.elements("pt").iterator();
                while(iterY.hasNext())
                {
                	yNumPoint = iterY.next();
                	valueY = Double.parseDouble(yNumPoint.element("v").getText());
                    aSeries.add(valueY);
                } 
                
                return aSeries;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @param sheet
     * @param chart
     * @param renderer
     * @return
     */
    private XYMultipleSeriesDataset getXYMultipleSeriesDataset(Element chart, short chartType, XYMultipleSeriesRenderer renderer)
    {
        return getXYMultipleSeriesDataset(chart, chartType, renderer, null);
    }
    
    /**
     * 
     * @param sheet
     * @param chart
     * @param renderer
     * @param styles
     * @return
     */
    private XYMultipleSeriesDataset getXYMultipleSeriesDataset(Element chart, short chartType, XYMultipleSeriesRenderer renderer, PointStyle[] styles) 
    {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        @ SuppressWarnings("unchecked")
        List<Element> seriesList= chart.elements("ser");  
        
        
        final int seriesCount = seriesList.size();        
        for (int i = 0; i < seriesCount; i++) 
        {
            //
            setSeriesRendererProp(renderer, seriesList.get(i), styles);
            
            //
            Object series = getSeries(seriesList.get(i), chartType);
            if(series == null)
            {
            	return null;
            }
            
            if(series instanceof CategorySeries)
            {
            	dataset.addSeries(((CategorySeries)series).toXYSeries());
            }
            else if(series instanceof XYSeries)
            {
            	dataset.addSeries((XYSeries)series);
            }            
            
            if(!hasMaxY)
            {
                maxY =  Math.max(maxY, dataset.getSeriesAt(i).getMaxY());
            }   
            
            if(!hasMinY)
            {
                minY =  Math.min(minY, dataset.getSeriesAt(i).getMinY());
            }   
        }
        
        double xAxisMin = Integer.MAX_VALUE;
        double xAxisMax = Integer.MIN_VALUE;
        for(int i = 0; i < dataset.getSeriesCount(); i++)
        {
        	xAxisMin = Math.min(xAxisMin, dataset.getSeriesAt(i).getMinX());
        	xAxisMax = Math.max(xAxisMax, dataset.getSeriesAt(i).getMaxX());
        }
        //set renderer property
        if(hasMinX)
        {
        	renderer.setXAxisMin(minX);
        }
        else
        {
        	if(chartType != AbstractChart.CHART_SCATTER)
            {
            	renderer.setXAxisMin(0.5);
            }
            else
            {
            	renderer.setXAxisMin(xAxisMin);
            }
        }        
        
        if(hasMaxX)
        {
        	renderer.setXAxisMax(maxX); 
        }
        else
        {
        	if(chartType != AbstractChart.CHART_SCATTER)
            {
                renderer.setXAxisMax(xAxisMax + 0.5); 
            }
            else
            {
                renderer.setXAxisMax(xAxisMax); 
            }
        }
        
        if(Math.abs(minY - Double.MAX_VALUE) < 0.1f)
        {
            minY = 0;
        }
        if(Math.abs(maxY - Double.MIN_VALUE) < 0.1f)
        {
            maxY = 0;
        }
        renderer.setYAxisMin(minY);
        renderer.setYAxisMax(maxY);        
        
        return dataset;
    }
    
    /**
     * 
     * @param bgColor
     * @return
     */
    protected DefaultRenderer buildDefaultRenderer()
    {
      DefaultRenderer renderer = new DefaultRenderer(); 
//      renderer.setLabelsTextSize(15);
//      renderer.setLegendTextSize(15);
//      renderer.setMargins(new int[] {70, 60, 15, 55});
      renderer.setShowGridH(true);
      
      renderer.setLabelsColor(schemeColor.get("tx1"));
      renderer.setAxesColor(schemeColor.get("tx1"));
      
      return renderer;
    }
    
    /**
     * 
     * @param sheet
     * @param chart
     * @param renderer
     * @return
     */
    protected CategorySeries buildCategoryDataset(Element chart, DefaultRenderer renderer) 
    {
        if(chart.element("ser") != null)
        {  
            CategorySeries aSeries = new CategorySeries("");
            
            Element series = chart.element("ser");
            
            //get series title            
            if(series.element("tx") != null)
            {
                aSeries = new CategorySeries(getSeriesTitle(series.element("tx")));
            }
            else
            {
                aSeries = new CategorySeries("");
            }
            
            //get catogry
            List<String> catList = new ArrayList<String>(10);
            if(series.element("cat") != null)
            {
                @ SuppressWarnings("unchecked")
                Iterator<Element> iter = series.element("cat").element("strRef").element("strCache").elements("pt").iterator();
                Element pt;
                while(iter.hasNext())
                {
                    pt = iter.next();
                    catList.add(pt.element("v").getText());
                }  
            }            
            
            //series cell value
            List<Double> valList = new ArrayList<Double>(10);
            if(series.element("val") != null)
            {
                @ SuppressWarnings("unchecked")
                Iterator<Element> iter = series.element("val").element("numRef").element("numCache").elements("pt").iterator();
                Element pt;
                while(iter.hasNext())
                {
                    pt = iter.next();
                    valList.add(Double.parseDouble(pt.element("v").getText()));
                }  
            }  
            
            SimpleSeriesRenderer r;
            int color;
            if(catList.size() > 0 && catList.size() == valList.size())
            {
                for(int i =0; i < catList.size(); i++)
                {
                    r = new SimpleSeriesRenderer();
                    
                    color = i % themeIndex.length;
                    color = schemeColor.get(themeIndex[color]); 
                    color = ColorUtil.instance().getColorWithTint(color, tints[i / themeIndex.length]);
                    r.setColor(color);
                    renderer.addSeriesRenderer(r);
                    
                    aSeries.add(catList.get(i), valList.get(i));
                }
            }
            else
            {
                for(int i =0; i < valList.size(); i++)
                {
                    r = new SimpleSeriesRenderer();
                    
                    color = i % themeIndex.length;
                    color = schemeColor.get(themeIndex[color]);  
                    color = ColorUtil.instance().getColorWithTint(color, tints[i / themeIndex.length]);
                    r.setColor(color);
                    renderer.addSeriesRenderer(r);
                    
                    aSeries.add(valList.get(i));
                }
            }
            


            return aSeries;
        }      
        
        return null;
    }
    
    private void dispose()
    {
        schemeColor = null;
        chart = null;
    }
    
    private Map<String, Integer> schemeColor;
    
    private short type;
    private Element chart;
    
    private boolean hasMaxX, hasMinX, hasMaxY, hasMinY;
    private double maxX, minX, maxY, minY;
}
