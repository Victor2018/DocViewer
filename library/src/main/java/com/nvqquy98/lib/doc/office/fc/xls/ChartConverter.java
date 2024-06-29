/*
 * 文件名称:          ChartConverter.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:21:40
 */
package com.nvqquy98.lib.doc.office.fc.xls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.hssf.formula.EvaluationWorkbook.ExternalSheet;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.AreaEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.NumberEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.eval.ValueEval;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Area3DPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.MemFuncPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.NameXPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Ptg;
import com.nvqquy98.lib.doc.office.fc.hssf.formula.ptg.Ref3DPtg;
import com.nvqquy98.lib.doc.office.fc.hssf.model.InternalWorkbook;
import com.nvqquy98.lib.doc.office.fc.hssf.record.Record;
import com.nvqquy98.lib.doc.office.fc.hssf.record.chart.ObjectLinkRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.chart.SeriesTextRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.record.chart.ValueRangeRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFCell;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFChart;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFDataFormat;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFFormulaEvaluator;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFName;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFChart.HSSFChartType;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFChart.HSSFSeries;
import com.nvqquy98.lib.doc.office.fc.ss.usermodel.ICell;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ACell;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ARow;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.ASheet;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.AWorkbook;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.util.format.NumericFormatter;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.ChartFactory;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.AbstractChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.PointStyle;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.ColumnBarChart.Type;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.CategorySeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYMultipleSeriesDataset;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYSeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.DefaultRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.SimpleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.util.MathHelper;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-3-6
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 * @param <LazyAreaEval>
 */
public class ChartConverter
{
    private static ChartConverter converter = new ChartConverter();
    
    /**
     * 
     */
    public static ChartConverter instance()
    {
        return converter;
    }
    
    public AbstractChart converter(ASheet sheet, HSSFChart chart)
    {
        workbook = sheet.getAWorkbook().getInternalWorkbook();
        
        minY = MathHelper.NULL_VALUE;
        maxY = -MathHelper.NULL_VALUE;
        chartSeriesText = chart.getSeriesText();
        AbstractChart abstractChart = convertToAChart(sheet, chart);
        dispose();
        return abstractChart;
    } 
    
    /**
     * 
     * @param chart
     * @return
     */
    public short getChartType(HSSFChart chart)
    {
        int ordinal = chart.getType().ordinal();
        if( ordinal == HSSFChartType.Area.ordinal())
        {
            return AbstractChart.CHART_AREA;
        }
        else if( ordinal == HSSFChartType.Bar.ordinal())
        {
            return AbstractChart.CHART_BAR;
        }
        else if( ordinal == HSSFChartType.Line.ordinal())
        {
            return AbstractChart.CHART_LINE;
        }
        else if( ordinal == HSSFChartType.Pie.ordinal() /*|| ordinal == 5*/)
        {
            return AbstractChart.CHART_PIE;
        }
        else if( ordinal == HSSFChartType.Scatter.ordinal())
        {
            return AbstractChart.CHART_SCATTER;
        }
        else
        {
            return AbstractChart.CHART_UNKOWN;
        }
    }
    
    private String getFormatContents(Sheet sheet, Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        String value = "";
        String key = "";
        short numericType;
        
        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                key =  HSSFDataFormat.getFormatCode(workbook, style.getNumberFormatID());
                numericType = NumericFormatter.instance().getNumericCellType(key);
               
                if(numericType == Cell.CELL_TYPE_NUMERIC_SIMPLEDATE)
                {
                    value = NumericFormatter.instance().getFormatContents(key, cell.getDateCellValue(sheet.getWorkbook().isUsing1904DateWindowing()));
                }
                else
                {
                    value = NumericFormatter.instance().getFormatContents(key, cell.getNumberValue(), numericType);
                } 
                break;
            case Cell.CELL_TYPE_STRING:
                Object si = sheet.getWorkbook().getSharedItem(cell.getStringCellValueIndex());
                if(si instanceof SectionElement)
                {
                    value = ((SectionElement)si).getText(null);
                }
                else
                {
                    value = (String)si;
                }
                break;
            case Cell.CELL_TYPE_FORMULA:
                break;
            default:
        }  
        return value;
    }
    
    private double getCellNumericValue(Sheet sheet, Cell cell)
    {
        if(cell == null)
        {
            return 0;
        }
        
        if(cell.getCellType() == ICell.CELL_TYPE_NUMERIC)
        {
            return cell.getNumberValue();
        }
        else if(cell.getCellType() == ICell.CELL_TYPE_BLANK)
        {
            return 0;
        }        
        
        return 0;
    }
    
    private List<Double> getData(ASheet sheet, Ptg[] valuePtg )
    {
    	if(valuePtg == null || valuePtg.length <= 0)
    	{
    		return null;
    	}
    	
    	List<Double> dataList = new ArrayList<Double>();
    	if(valuePtg[0] instanceof Area3DPtg)
        {
            Area3DPtg area3DPtg = (Area3DPtg)valuePtg[0];
            
            //chart source data was not the current sheet
            Sheet cursheet;
            ExternalSheet externalSheet = workbook.getExternalSheet(area3DPtg.getExternSheetIndex());
            if(externalSheet == null)
            {
                int otherIndex = workbook.getSheetIndexFromExternSheetIndex(area3DPtg.getExternSheetIndex());
                cursheet = sheet.getWorkbook().getSheet(otherIndex);
            }
            else
            {
                cursheet = sheet.getWorkbook().getSheet(externalSheet.getSheetName());
            }
            
            Row row;
            double value = 0;
            if(area3DPtg.getFirstRow() == area3DPtg.getLastRow())
            {
                row = cursheet.getRow(area3DPtg.getFirstRow());
                for (int k = area3DPtg.getFirstColumn(); k <= area3DPtg.getLastColumn(); k++) 
                {  
                    value = 0;
                    if(row != null)
                    {
                        value = getCellNumericValue(cursheet, row.getCell(k));
                    }
                    
                    dataList.add(value);
                }
            }
            else if(area3DPtg.getFirstColumn() == area3DPtg.getLastColumn())
            {
                for(int j = area3DPtg.getFirstRow(); j <= area3DPtg.getLastRow(); j++)
                {
                    row = cursheet.getRow(j);
                    value = 0;
                    if(row != null)
                    {
                        value = getCellNumericValue(cursheet, row.getCell(area3DPtg.getFirstColumn()));
                    }
                    
                    dataList.add(value);
                }
            }
        }
        else if(valuePtg[0] instanceof MemFuncPtg)
        {
            int index = 0;
            while(index < valuePtg.length)
            {
                if(valuePtg[index] instanceof Ref3DPtg)
                {
                    Ref3DPtg ref = (Ref3DPtg)valuePtg[index];
                    //chart source data was not the current sheet
                    Sheet cursheet;
                    ExternalSheet externalSheet = workbook.getExternalSheet(ref.getExternSheetIndex());
                    if(externalSheet == null)
                    {
                        int otherIndex = workbook.getSheetIndexFromExternSheetIndex(ref.getExternSheetIndex());
                        cursheet = sheet.getWorkbook().getSheet(otherIndex);
                    }
                    else
                    {
                        cursheet = sheet.getWorkbook().getSheet(externalSheet.getSheetName());
                    }
                    
                    Row row = cursheet.getRow(ref.getRow());
                    double value = 0;
                    value = 0;
                    if(row != null)
                    {
                        value = getCellNumericValue(cursheet, row.getCell(ref.getColumn()));
                    }                        
                    dataList.add(value);
                }                    
                index++;
            }
        }
        else if(valuePtg[0] instanceof NameXPtg)
        {
            try
            {
                NameXPtg namePtg = (NameXPtg)valuePtg[0];
                HSSFName hssfName = ((AWorkbook)sheet.getWorkbook()).getNameAt(namePtg.getNameIndex());                
                
                ValueEval valueEval = evaluate(sheet, hssfName);
                if(valueEval instanceof AreaEval)
                {
                    AreaEval area = (AreaEval)valueEval;

                    double value = 0;
                    if(area.getFirstRow() == area.getLastRow())
                    {
                        for (int k = area.getFirstColumn(); k <= area.getLastColumn(); k++) 
                        {  
                            value = ((NumberEval)area.getAbsoluteValue(area.getFirstRow(), k)).getNumberValue();
                            
                            dataList.add(value);
                        }
                    }
                    else if(area.getFirstColumn() == area.getLastColumn())
                    {
                        for(int j = area.getFirstRow(); j <= area.getLastRow(); j++)
                        {
                            value = ((NumberEval)area.getAbsoluteValue(j, area.getFirstColumn())).getNumberValue();
                            
                            dataList.add(value);
                        }
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
            
        }
        else if(valuePtg.length > 0 && valuePtg[0] instanceof Ref3DPtg)
        {
            Ref3DPtg ref = (Ref3DPtg)valuePtg[0];
            Sheet cursheet;
            ExternalSheet externalSheet = workbook.getExternalSheet(ref.getExternSheetIndex());
            if(externalSheet == null)
            {
                int otherIndex = workbook.getSheetIndexFromExternSheetIndex(ref.getExternSheetIndex());
                cursheet = sheet.getWorkbook().getSheet(otherIndex);
            }
            else
            {
                cursheet = sheet.getWorkbook().getSheet(externalSheet.getSheetName());
            }
            
            Row row = cursheet.getRow(ref.getRow());
            double value = 0;
            value = 0;
            if(row != null)
            {
                value = getCellNumericValue(cursheet, row.getCell(ref.getColumn()));
            }                        
            dataList.add(value);
        }
    	
    	return dataList;
    }
    
    private XYMultipleSeriesDataset getXYMultipleSeriesDataset(ASheet sheet, 
                                        HSSFChart chart, XYMultipleSeriesRenderer renderer, short chartType) 
    { 
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        HSSFSeries[] series = chart.getSeries();  
        SimpleSeriesRenderer[]  seriesRenderers = renderer.getSeriesRenderers();
        final int seriesCount = series.length;
        
        for (int i = 0; i < seriesCount; i++) 
        {
            //get series name
            String seriesName = null;            
            if( series[i].getSeriesTitle() != null)
            {
            	seriesName = series[i].getSeriesTitle();
            }
            else
            {
            	seriesName = "Series " + (i + 1);
            }
            
            List<Double> xList = null;
            boolean isValidateXList = true;
            if(chartType == AbstractChart.CHART_SCATTER)
            {
            	xList = new ArrayList<Double>();
            	Ptg[] valuePtg = series[i].getDataCategoryLabels().getFormulaOfLink();
            	if(valuePtg.length > 0)
            	{
            		xList = getData(sheet, valuePtg);
                	
                	for(int j = 0; j < xList.size() - 1; j++)
                	{
                		if(Math.abs(xList.get(j) - xList.get(j + 1)) < 0.000000001f)
                		{
                			isValidateXList = false;
                			break;
                		}
                	}
            	}
            }
            
            //get series cell value
            Ptg[] valuePtg =  series[i].getDataValues().getFormulaOfLink(); 
            if(valuePtg.length <= 0)
            {
                renderer.removeSeriesRenderer(seriesRenderers[i]);
                chart.removeSeries(series[i]);
                continue;
            }
            
            List<Double> yList = getData(sheet, valuePtg);;
            
            if(chartType == AbstractChart.CHART_SCATTER && isValidateXList)
            {
            	if(xList != null && yList != null && xList.size() == yList.size())
            	{
            		XYSeries aSeries = new XYSeries(seriesName);
                	for(int j = 0; j < xList.size(); j++)
                	{
                		aSeries.add(xList.get(j), yList.get(j));
                		minY = Math.min(yList.get(j), minY);
                		maxY = Math.max(yList.get(j), maxY);
                	}
                	
                	dataset.addSeries(aSeries);
            	}            	
            }
            else
            {
            	CategorySeries aSeries = new CategorySeries(seriesName);
            	
            	for(Double value : yList)
            	{
            		aSeries.add(value);
            		minY = Math.min(value, minY);
            		maxY = Math.max(value, maxY);
            	}
            	dataset.addSeries(aSeries.toXYSeries());
            }
        }
        
        return dataset;
   }
    
    /**
     * 最大新建一个cell,因为area可能会包含给出的那个cell
     * @param sheet
     * @return
     */
    private ValueEval evaluate(ASheet sheet, HSSFName name)
    {
        ACell cell = null;
        
        int s = sheet.getFirstRowNum();
        int e = sheet.getLastRowNum();
        for(; s <= e; s++)
        {
            ARow row = (ARow)sheet.getRow(sheet.getLastRowNum());
            short col = -1;
            if(row.getFirstCol() > 0)
            {
                col = (short)(row.getFirstCol() - 1);
                
            }
            else if( row.getLastCol() < HSSFCell.LAST_COLUMN_NUMBER)
            {
                col = (short)(row.getLastCol() + 1);
            }
            
            if(col >= 0)
            {
                cell = new ACell((AWorkbook)sheet.getWorkbook(), sheet, s, (short)col);
                break;
            }
        }       

        
        if(cell != null)
        {
            cell.setCellFormula(name.getRefersToFormulaDefinition());
            HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator((AWorkbook)sheet.getWorkbook());
            
            ValueEval value = evaluator.evaluateFormulaValueEval(cell);
            
            cell.dispose();
            cell = null;
            evaluator = null;
            
            return value;
        }
        
        return null;
    }
    
    private XYMultipleSeriesRenderer buildXYMultipleSeriesRenderer(ASheet sheet, HSSFChart chart)
    {
        return buildXYMultipleSeriesRenderer(sheet, chart, null);
    }
    private XYMultipleSeriesRenderer buildXYMultipleSeriesRenderer(ASheet sheet, HSSFChart chart, PointStyle[] styles) 
    {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXTitleTextSize(16);
        renderer.setYTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        //renderer.setMargins(new int[] {70, 60, 15, 55});
        renderer.setShowGridH(true);
        
        int colorOffSet = 0;
        switch(getChartType(chart))
        {
            case AbstractChart.CHART_AREA:
            case AbstractChart.CHART_BAR:
            case AbstractChart.CHART_PIE:
                //automatic, 24 is the default start index of area format record in HSSFPalette
                colorOffSet = 24;
                break;
            case AbstractChart.CHART_LINE:
            case AbstractChart.CHART_SCATTER:
                colorOffSet = 32;
                break;
        }
        
//      //set margin color of chart
//      if(chart.getMarginColorFormat() != null)
//      {
//          renderer.setBackgroundColor(sheet.getWorkbook().getColor(chart.getMarginColorFormat().getForecolorIndex()));
//      }
//      //set background color of series
//      if(chart.getSeriesBackgroundColorFormat() != null)
//      {
//      	//下面取出来的颜色是标题背景色， 不对
//          renderer.setSeriesBackgroundColor(sheet.getWorkbook().getColor(chart.getSeriesBackgroundColorFormat().getForecolorIndex()));
//      }}
        
        //forecolor and point style of series
        HSSFSeries[] series = chart.getSeries(); 
        if(styles != null && styles.length > 0)
        {
            int pointStyleIndex = 0;
            for(int i = 0; i < series.length; i++)
            {
                XYSeriesRenderer r = new XYSeriesRenderer(); 
                r.setPointStyle(styles[pointStyleIndex]);
                pointStyleIndex = (pointStyleIndex + 1) %  styles.length;
                int color = 0x808080;
//                if( series[i].getAreaFormat() != null && series[i].getAreaFormat().getForegroundColor() != 0)
//                {
//                    //custom
//                    color = 0xFF<<24 | series[i].getAreaFormat().getForegroundColor()/*sheet.getWorkbook().getColor(series[i].getAreaFormat().getForecolorIndex())*/;
//                }
//                else
                {
                    color = sheet.getWorkbook().getColor(i + colorOffSet);
                }
                
                r.setColor(color);
                renderer.addSeriesRenderer(r);
            }             
        }
        else
        {
            for(int i = 0; i < series.length; i++)
            {
                XYSeriesRenderer r = new XYSeriesRenderer(); 
                int color = 0x808080;
//                if( series[i].getAreaFormat() != null)
//                {
//                    //custom
//                    color = sheet.getWorkbook().getColor(series[i].getAreaFormat().getForecolorIndex());
//                }
//                else
                {
                    //automatic, 24 is the default start index of area format record in HSSFPalette
                    color = sheet.getWorkbook().getColor(i + 24);        
                }
                r.setColor(color);
                renderer.addSeriesRenderer(r);
            }    
        }
        
        
        //x label
        if(series.length > 0)
        {
            //x轴上显示的标注，如1,2,3....或者一，二，三...等等，需要处理
            Ptg[] categoryLabelsPtg = series[0].getDataCategoryLabels().getFormulaOfLink();
            if(categoryLabelsPtg.length > 0 && categoryLabelsPtg[0] instanceof Area3DPtg)
            {
                Area3DPtg area3DPtg = (Area3DPtg)categoryLabelsPtg[0]; 
                Sheet cursheet;
               
                ExternalSheet externalSheet = workbook.getExternalSheet(area3DPtg.getExternSheetIndex());
                if(externalSheet == null)
                {
                    int otherIndex = workbook.getSheetIndexFromExternSheetIndex(area3DPtg.getExternSheetIndex());
                    cursheet = sheet.getWorkbook().getSheet(otherIndex);
                }
                else
                {
                    cursheet = sheet.getWorkbook().getSheet(externalSheet.getSheetName());
                }
                
                Row row;
                String textLabel;
                int xTextLabel = 1;
                if(area3DPtg.getFirstRow() == area3DPtg.getLastRow())
                {
                    row = cursheet.getRow(area3DPtg.getFirstRow());
                    for (int k = area3DPtg.getFirstColumn(); k <= area3DPtg.getLastColumn(); k++) 
                    {  
                        if(row != null && row.getCell(k) != null)
                        {
                            textLabel = getFormatContents(cursheet, row.getCell(k));
                        }
                        else
                        {
                            textLabel =  String.valueOf(k - area3DPtg.getFirstColumn() + 1);
                        }
                        renderer.addXTextLabel(xTextLabel++, textLabel);
                    }
                }
                else if(area3DPtg.getFirstColumn() == area3DPtg.getLastColumn())
                {
                    for(int j = area3DPtg.getFirstRow(); j <= area3DPtg.getLastRow(); j++)
                    {
                        row = cursheet.getRow(j);
                        if(row != null && row.getCell(area3DPtg.getFirstColumn()) != null)
                        {
                            textLabel = getFormatContents(cursheet, row.getCell(area3DPtg.getFirstColumn()));
                        }
                        else
                        {
                            textLabel =  String.valueOf(j - area3DPtg.getFirstRow() + 1);
                        }
                        renderer.addXTextLabel(xTextLabel++, textLabel);
                    }
                }
                
            }            
        }
        
        //title(chart, x label, y label)
        Iterator<SeriesTextRecord> iter = chartSeriesText.keySet().iterator();
        while(iter.hasNext())
        {
            SeriesTextRecord seriesTextRecord = iter.next();
            Record r = chartSeriesText.get(seriesTextRecord);
            if(r instanceof ObjectLinkRecord)
            {
            	switch(((ObjectLinkRecord)r).getAnchorId())
                {
                    case ObjectLinkRecord.ANCHOR_ID_CHART_TITLE:
                        renderer.setChartTitle(seriesTextRecord.getText());
                        break;
                    case ObjectLinkRecord.ANCHOR_ID_Y_AXIS:
                        renderer.setYTitle(seriesTextRecord.getText());
                        break;
                    case ObjectLinkRecord.ANCHOR_ID_X_AXIS:
                        renderer.setXTitle(seriesTextRecord.getText());
                        break;
                    case ObjectLinkRecord.ANCHOR_ID_SERIES_OR_POINT:
                        break;
                    case ObjectLinkRecord.ANCHOR_ID_Z_AXIS:
                        break;
                    default:
                        break;
                }
            }
        }
      
        
        return renderer;
    }
    
    private void setChartSettings(XYMultipleSeriesRenderer renderer, XYMultipleSeriesDataset dataset, HSSFChart chart) 
    {
    	int seriesItemCount = -1;
    	for(int i = 0; i < dataset.getSeriesCount(); i++)
    	{
    		seriesItemCount = Math.max(seriesItemCount, dataset.getSeriesAt(i).getItemCount());
    	}
    	
    	List<ValueRangeRecord>  valueRangeList = chart.getValueRangeRecord();
    	if(valueRangeList.size() > 0)
    	{
    		ValueRangeRecord valueRangeRecord;
        	if(getChartType(chart) != AbstractChart.CHART_SCATTER)
        	{
              
        		renderer.setXAxisMin(0.5);
        		renderer.setXAxisMax(seriesItemCount + 0.5); 
              
        		valueRangeRecord = valueRangeList.get(0);          
        	}
        	else
        	{
              //axis x 
              double minX = dataset.getSeriesAt(0).getMinX();
              double maxX = dataset.getSeriesAt(0).getMaxX();
              
              valueRangeRecord = valueRangeList.get(0);
              if(!valueRangeRecord.isAutomaticMinimum())
              {
                  minX = valueRangeRecord.getMinimumAxisValue();
              }          
              
              if(!valueRangeRecord.isAutomaticMaximum())
              {
                  maxX = valueRangeRecord.getMaximumAxisValue();
              }
              renderer.setXAxisMin(minX);      
              renderer.setXAxisMax(maxX);
              
              valueRangeRecord = valueRangeList.get(1);
        	}
          
        	//axis y
        	if(!valueRangeRecord.isAutomaticMinimum())
        	{
        		minY = valueRangeRecord.getMinimumAxisValue();
        	}
          
        	if(!valueRangeRecord.isAutomaticMaximum())
        	{
        		maxY = valueRangeRecord.getMaximumAxisValue();
        	}
    	}
    	else
    	{
    		if(getChartType(chart) != AbstractChart.CHART_SCATTER)
        	{              
        		renderer.setXAxisMin(0.5);
        		renderer.setXAxisMax(seriesItemCount + 0.5);      
        	}
        	else
        	{
        		//axis x 
        		double minX = dataset.getSeriesAt(0).getMinX();
        		double maxX = dataset.getSeriesAt(0).getMaxX();
        		renderer.setXAxisMin(minX);      
        		renderer.setXAxisMax(maxX);
        	}
    	}
    	
    	renderer.setYAxisMin(minY);      
    	renderer.setYAxisMax(maxY);      
    }
    
    /**
     * Builds a category renderer to use the provided colors.
     * 
     * @param colors the colors
     * @return the category renderer
     */
    protected DefaultRenderer buildDefaultRenderer(Sheet sheet, HSSFChart chart)
    {
      DefaultRenderer renderer = new DefaultRenderer(); 
      renderer.setLabelsTextSize(15);
      renderer.setLegendTextSize(15);
      //renderer.setMargins(new int[] {70, 60, 15, 55});
      renderer.setShowGridH(true);
      
      //set margin color of chart
      if(chart.getMarginColorFormat() != null)
      {
          int color = sheet.getWorkbook().getColor(chart.getMarginColorFormat().getForecolorIndex());
          renderer.setBackgroundColor(color);
      }      
      
      HSSFSeries[] series = chart.getSeries();        
      final int seriesCount = series.length;
      if(seriesCount > 0)
      {                 
          
          //get series cell value
          Ptg[] valuePtg =  series[0].getDataValues().getFormulaOfLink();            
          if(valuePtg.length > 0 && valuePtg[0] instanceof Area3DPtg)
          {
              Area3DPtg area3DPtg = (Area3DPtg)valuePtg[0];              
              
              if(area3DPtg.getFirstColumn() == area3DPtg.getLastColumn())
              {
                  for(int j = area3DPtg.getFirstRow(); j <= area3DPtg.getLastRow(); j++)
                  {               
                      SimpleSeriesRenderer r = new SimpleSeriesRenderer(); 
                      
                      int color = sheet.getWorkbook().getColor(j - area3DPtg.getFirstRow() + 24);                     
                      r.setColor(color);
                      renderer.addSeriesRenderer(r);
                  }
              }
              else if(area3DPtg.getFirstRow() == area3DPtg.getLastRow())
              {
                  for(int j = area3DPtg.getFirstColumn(); j <= area3DPtg.getLastColumn(); j++)
                  {
                      SimpleSeriesRenderer r = new SimpleSeriesRenderer(); 
                      int color = sheet.getWorkbook().getColor(j - area3DPtg.getFirstColumn() + 24);                     
                      
                      r.setColor(color);
                      renderer.addSeriesRenderer(r);
                  }
              }
              
              
          }   
      }      
     
      //chart title
      Iterator<SeriesTextRecord> iter = chartSeriesText.keySet().iterator();
      while(iter.hasNext())
      {
          SeriesTextRecord seriesTextRecord = iter.next();
          Record r = chartSeriesText.get(seriesTextRecord);
          if(r instanceof ObjectLinkRecord)
          {
          	switch(((ObjectLinkRecord)r).getAnchorId())
              {
                  case ObjectLinkRecord.ANCHOR_ID_CHART_TITLE:
                      renderer.setChartTitle(seriesTextRecord.getText());
                      break;
              }
          }
      }
      
      return renderer;
    }
    
    private String getCategory(Sheet sheet, Area3DPtg catPtg, int offIncrement)
    {
        Row row;
        Cell cell;
        String name = "";
        if(catPtg.getFirstColumn() == catPtg.getLastColumn())
        {
            row = sheet.getRow(catPtg.getFirstRow() + offIncrement);
            if(row != null && (cell = row.getCell(catPtg.getFirstColumn())) != null)
            {
                name = getFormatContents(sheet, cell); 
            }
            else
            {
                name = String.valueOf(offIncrement + 1);
            }
        }
        else if(catPtg.getFirstRow() == catPtg.getLastRow())
        {
            row = sheet.getRow(catPtg.getFirstRow());
            if(row != null && (cell = row.getCell(catPtg.getFirstColumn() + offIncrement)) != null)
            {
                name = getFormatContents(sheet, cell);
            }
            else
            {
                name = String.valueOf(offIncrement + 1);
            }
        }
        return name;
    }
    
    /**
     * Builds a category series using the provided values.
     * 
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    protected CategorySeries buildCategoryDataset(Sheet sheet, HSSFChart chart) 
    {
        CategorySeries aSeries = new CategorySeries("");
                
        if(chart.getSeries().length > 0)
        {  
            //get series title
            HSSFSeries series = chart.getSeries()[0];
            if(series.getSeriesTitle() != null)
            {
                aSeries = new CategorySeries(series.getSeriesTitle());
            }
            else
            {
                aSeries = new CategorySeries("");
            }
            
            Ptg[] categoryLabelsPtg =  series.getDataCategoryLabels().getFormulaOfLink();  
            Ptg[] dataValuesPtg =  series.getDataValues().getFormulaOfLink();
            
            if(dataValuesPtg.length <= 0 || !(dataValuesPtg[0] instanceof Area3DPtg))
            {
                return null;
            }
            
            Area3DPtg valuePtg = (Area3DPtg)dataValuesPtg[0];
            Sheet cursheet;
            ExternalSheet externalSheet = workbook.getExternalSheet(valuePtg.getExternSheetIndex());
            if(externalSheet == null)
            {
                int otherIndex = workbook.getSheetIndexFromExternSheetIndex(valuePtg.getExternSheetIndex());
                cursheet = sheet.getWorkbook().getSheet(otherIndex);
            }
            else
            {
                cursheet = sheet.getWorkbook().getSheet(externalSheet.getSheetName());
            }
            
            
            if(categoryLabelsPtg.length > 0 && categoryLabelsPtg[0] instanceof Area3DPtg)
            {
                Area3DPtg catPtg = (Area3DPtg)categoryLabelsPtg[0]; 
                
                Row dataRow;
                double value = 0;
                String name;
                if(valuePtg.getFirstColumn() == valuePtg.getLastColumn())
                {
                    for(int j = valuePtg.getFirstRow(); j <= valuePtg.getLastRow(); j++)
                    {
                        
                        name = getCategory(cursheet, catPtg, j - valuePtg.getFirstRow());
                        value = 0;
                        dataRow = cursheet.getRow(j);
                        if(dataRow != null)
                        {
                            value = getCellNumericValue(cursheet, dataRow.getCell(valuePtg.getFirstColumn()));
                        }
                        
                        aSeries.add(name, value);                                             
                    }
                }
                else if(valuePtg.getFirstRow() == valuePtg.getLastRow())
                {
                    dataRow = cursheet.getRow(valuePtg.getFirstRow());                   
                    
                    for(int j = valuePtg.getFirstColumn(); j <= valuePtg.getLastColumn(); j++)
                    {
                        name = getCategory(cursheet, catPtg, j - valuePtg.getFirstColumn());
                        
                        value = 0;
                        if(dataRow != null )
                        {
                            value = getCellNumericValue(cursheet, dataRow.getCell(j));
                        }
                        aSeries.add(name, value);
                    }
                }
            }
            else
            {
                //get series cell value          
                Row row;
                double value = 0;
                if(valuePtg.getFirstColumn() == valuePtg.getLastColumn())
                {
                    for(int j = valuePtg.getFirstRow(); j <= valuePtg.getLastRow(); j++)
                    {
                        value = 0;
                        row = cursheet.getRow(j);
                        if(row != null)
                        {
                            value = getCellNumericValue(cursheet, row.getCell(valuePtg.getFirstColumn()));
                        }
                        aSeries.add(value);
                    }
                }
                else if(valuePtg.getFirstRow() == valuePtg.getLastRow())
                {
                    row = cursheet.getRow(valuePtg.getFirstRow());
                    for(int j = valuePtg.getFirstColumn(); j <= valuePtg.getLastColumn(); j++)
                    {
                        value = 0;
                        if(row != null)
                        {
                            value = getCellNumericValue(cursheet, row.getCell(j));
                        }
                        aSeries.add(value);
                    }
                } 
            }


            return aSeries;
        }      
        
        return null;
    }
    
    private  AbstractChart convertToAChart(ASheet sheet, HSSFChart chart)
    {
        DefaultRenderer renderer;
        XYMultipleSeriesDataset dataset;
        PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND, PointStyle.SQUARE, PointStyle.TRIANGLE,
            PointStyle.X, PointStyle.CIRCLE };
        switch(getChartType(chart))
        {
//            case AbstractChart.CHART_AREA:
//                break;
//            case AbstractChart.CHART_BAR:
//                renderer = buildXYMultipleSeriesRenderer(sheet, chart); 
//                if(renderer == null)
//                {
//                    return null;
//                }
//                
//                dataset = getXYMultipleSeriesDataset(sheet, chart, (XYMultipleSeriesRenderer)renderer);
//                if(dataset == null)
//                {
//                    return null;
//                }
//                
//                setChartSettings((XYMultipleSeriesRenderer)renderer, dataset.getSeriesAt(0).getItemCount(), chart);
//                return ChartFactory.getBarChart(dataset, (XYMultipleSeriesRenderer)renderer, Type.DEFAULT);                
//            
            case AbstractChart.CHART_LINE:
                renderer = buildXYMultipleSeriesRenderer(sheet, chart, styles);
                if(renderer == null)
                {
                    return null;
                }
                
                dataset = getXYMultipleSeriesDataset(sheet, chart, (XYMultipleSeriesRenderer)renderer, AbstractChart.CHART_LINE);
                if(dataset == null)
                {
                    return null;
                }
                
                setChartSettings((XYMultipleSeriesRenderer)renderer, dataset, chart);
                ((XYMultipleSeriesRenderer)renderer).setYLabels(10);
                return ChartFactory.getLineChart(dataset, (XYMultipleSeriesRenderer)renderer); 
                
            case AbstractChart.CHART_PIE:
                renderer = buildDefaultRenderer(sheet, chart);
                if(renderer == null)
                {
                    return null;
                }
                
                renderer.setZoomEnabled(true);
                CategorySeries categoryDataset = buildCategoryDataset(sheet, chart);
                if(categoryDataset == null)
                {
                    return null;
                }
                return ChartFactory.getPieChart(categoryDataset, renderer);
                
            case AbstractChart.CHART_SCATTER:
                renderer = buildXYMultipleSeriesRenderer(sheet, chart, styles);
                if(renderer == null)
                {
                    return null;
                }
                 
                dataset = getXYMultipleSeriesDataset(sheet, chart, (XYMultipleSeriesRenderer)renderer, AbstractChart.CHART_SCATTER);
                if(dataset == null)
                {
                    return null;
                }
                
                setChartSettings((XYMultipleSeriesRenderer)renderer, dataset, chart);
                
                for (int i = 0; i < renderer.getSeriesRendererCount(); i++) 
                {
                    ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
                }               
                return ChartFactory.getScatterChart(dataset, (XYMultipleSeriesRenderer)renderer);
            default: 
            	try
            	{
            		renderer = buildXYMultipleSeriesRenderer(sheet, chart); 
                    if(renderer == null)
                    {
                        return null;
                    }
                    
                    dataset = getXYMultipleSeriesDataset(sheet, chart, (XYMultipleSeriesRenderer)renderer, AbstractChart.CHART_BAR);
                    if(dataset == null)
                    {
                        return null;
                    }
                    
                    setChartSettings((XYMultipleSeriesRenderer)renderer, dataset, chart);
                    return ChartFactory.getColumnBarChart(dataset, (XYMultipleSeriesRenderer)renderer, Type.DEFAULT); 
            	}
            	catch(Exception e)
            	{
            		return null;
            	}
        }
    } 
    
    public AbstractChart getAChart()
    {
        return chart;
    }
    
    
    private void dispose()
    {
        workbook = null;       
        chart =  null;
        chartSeriesText = null;
        
    }
    private InternalWorkbook workbook;
    private AbstractChart chart;
    
    private Map<SeriesTextRecord, Record> chartSeriesText;
    double minY, maxY;
}
