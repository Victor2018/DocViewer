/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.common.BackgroundDrawer;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYMultipleSeriesDataset;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYSeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.BasicStroke;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.SimpleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer.Orientation;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.util.MathHelper;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * The XY chart rendering class.
 */
public abstract class XYChart extends AbstractChart {
  /** The multiple series dataset. */
  protected XYMultipleSeriesDataset mDataset;
  /** The multiple series renderer. */
  protected XYMultipleSeriesRenderer mRenderer;
  /** The current scale value. */
  private float mScale;
  /** The current translate value. */
  private float mTranslate;
  /** The canvas center point. */
  private PointF mCenter;
  /** The visible chart area, in screen coordinates. */
  private Rect mScreenR;
  /** The calculated range. */
  private Map<Integer, double[]> mCalcRange = new HashMap<Integer, double[]>();

  /** The legend shape width. */
  protected static final int SHAPE_WIDTH = 12;
  
  protected XYChart() {
  }

  /**
   * Builds a new XY chart instance.
   * 
   * @param dataset the multiple series dataset
   * @param renderer the multiple series renderer
   */
  public XYChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
    mDataset = dataset;
    mRenderer = renderer;
  }

  // TODO: javadoc
  protected void setDatasetRenderer(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    mDataset = dataset;
    mRenderer = renderer;
  }
  
  /**
   * 
   *(non-Javadoc)
   * @see com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.AbstractChart#setZoomRate(float)
   *
   */
  @Override
  public void setZoomRate(float rate)
  {
      this.mRenderer.setZoomRate(rate);
  }
  
  public float getZoomRate()
  {
	  return mRenderer.getZoomRate();
  }
  
  protected void drawSeriesBackgroundAndFrame(XYMultipleSeriesRenderer renderer, Canvas canvas, Rect rect, Paint paint) 
  {
	  int alpha = paint.getAlpha();
	  Path path = new Path();
	  path.addRect((float)rect.left, (float)rect.top, (float)rect.right, (float)rect.bottom, Direction.CCW);
	  // draw fill
      BackgroundAndFill fill = renderer.getSeriesBackgroundColor();
      if (fill != null)
      {
          paint.setStyle(Style.FILL);
          BackgroundDrawer.drawPathBackground(canvas, null, 1, fill, rect, null, 1.0f, path, paint);
          paint.setAlpha(alpha);
      }
      
      // draw border
      Line frame = renderer.getSeriesFrame();
      if (frame != null)
      {
          paint.setStyle(Style.STROKE);
          paint.setStrokeWidth(2);
          if(frame.isDash())
          {
        	  DashPathEffect dashPathEffect = new DashPathEffect(new float[] {5, 5}, 10);
              paint.setPathEffect(dashPathEffect);
          }            
          
          BackgroundDrawer.drawPathBackground(canvas, null, 1, frame.getBackgroundAndFill(), rect, null, 1.0f, path, paint);
          paint.setStyle(Style.FILL);
          paint.setAlpha(alpha);
      }
      
      paint.reset();
      paint.setAntiAlias(true);
  }
  
  /**
   * The graphical representation of the XY chart.
   * 
   * @param canvas the canvas to paint to
   * @param x the top left x value of the view to draw to
   * @param y the top left y value of the view to draw to
   * @param width the width of the view to draw to
   * @param height the height of the view to draw to
   * @param paint the paint
   */
  @Override
  public void draw(Canvas canvas, IControl control, int x, int y, int width, int height, Paint paint) 
  {	  
      Rect rect = new Rect(x, y, x + width, y + height);
      canvas.save();
      canvas.clipRect(rect);
  	
  	  paint.setAntiAlias(mRenderer.isAntialiasing());
  	  int preColor = paint.getColor();
  	  float preSize = paint.getStrokeWidth();
    
  	  drawBackgroundAndFrame(mRenderer, canvas, control, rect, paint);
  	  int legendHeight = mRenderer.getLegendHeight();

  	  if (mRenderer.isShowLegend() && legendHeight == 0) 
  	  {
  		  legendHeight = height / 5;
  	  }
  	  
  	  int sLength = mDataset.getSeriesCount();
  	  String[] titles = new String[sLength];
  	  for (int i = 0; i < sLength; i++)
  	  {
  		  titles[i] = mDataset.getSeriesAt(i).getTitle();
  	  }
  	  
      Rectangle titleAreaSize = getTitleTextAreaSize(mRenderer, width, height, paint);
  	  Rectangle xTitleAreaSize = getXTitleTextAreaSize(width, height, paint);
  	  Rectangle yTitleAreaSize = getYTitleTextAreaSize(width, height, paint);
  	  
  	  int legendH = height;
  	  if(titleAreaSize != null)
  	  {
  		legendH -= titleAreaSize.height;
  	  }
  	  Rectangle legendSize = getLegendAutoSize(mRenderer, titles, width, legendH, paint);
    
  	  double[] margins = mRenderer.getMargins(); 
    
	  int left = x + (int)(margins[1] * width + mRenderer.getYTitleTextSize() * mRenderer.getZoomRate());
	  if(yTitleAreaSize != null)
	  {
		  left += yTitleAreaSize.width;
	  }
	  
	  int top = y + (int)(margins[0] * height);
	  if(titleAreaSize != null)
	  {
		  top += titleAreaSize.height;
	  }
	  
	  int right = x + width - (int)(margins[3] * width);
	  if(legendSize != null && (legendPos == LegendPosition_Left || legendPos == LegendPosition_Right))
	  {
		  right -= legendSize.width;
	  }
  	  
  	  int bottom = y + height - (int)(margins[2] * height);
  	  if(legendSize != null && (legendPos == LegendPosition_Top || legendPos == LegendPosition_Bottom))
	  {
  		  bottom -= legendSize.height;
	  }
  	  if(xTitleAreaSize != null)
  	  {
  		  bottom -= xTitleAreaSize.height;
  	  }
  	  
  	  //
  	  paint.setTextSize(mRenderer.getLabelsTextSize() * mRenderer.getZoomRate());     
  	  FontMetrics fm = paint.getFontMetrics();
  	  bottom -= (fm.descent - fm.ascent);
     
  	  if (paint.getTypeface() == null
  			  || !paint.getTypeface().toString().equals(mRenderer.getTextTypefaceName())
  			  || paint.getTypeface().getStyle() != mRenderer.getTextTypefaceStyle()) 
  	  {
  		  paint.setTypeface(Typeface.create(mRenderer.getTextTypefaceName(), 
  				  mRenderer.getTextTypefaceStyle()));
  	  }
  	  Orientation or = mRenderer.getOrientation();
  	  if (or == Orientation.VERTICAL)
  	  {
  		  right -= legendHeight;
  		  bottom += legendHeight - 20;
  	  }
  	  int angle = or.getAngle();
  	  boolean rotate = angle == 90;
  	  mScale = (float) (height) / width;
  	  mTranslate = Math.abs(width - height) / 2;
  	  if (mScale < 1)
  	  {
  		  mTranslate *= -1;
  	  }
  	  mCenter = new PointF((x + width) / 2, (y + height) / 2);
  	  if (rotate) 
  	  {
  		  transform(canvas, angle, false);
  	  }

  	  int maxScaleNumber = -Integer.MAX_VALUE;
  	  for (int i = 0; i < sLength; i++) 
  	  {
  		  maxScaleNumber = Math.max(maxScaleNumber, mDataset.getSeriesAt(i).getScaleNumber());
  	  }
  	  maxScaleNumber++;
  	  if (maxScaleNumber < 0)
  	  {
  		  canvas.restore();
  		  return;
  	  }
  	  double[] minX = new double[maxScaleNumber];
  	  double[] maxX = new double[maxScaleNumber];
  	  double[] minY = new double[maxScaleNumber];
  	  double[] maxY = new double[maxScaleNumber];
  	  boolean[] isMinXSet = new boolean[maxScaleNumber];
  	  boolean[] isMaxXSet = new boolean[maxScaleNumber];
  	  boolean[] isMinYSet = new boolean[maxScaleNumber];
  	  boolean[] isMaxYSet = new boolean[maxScaleNumber];

  	  for (int i = 0; i < maxScaleNumber; i++)
  	  {
  		  minX[i] = mRenderer.getXAxisMin(i);
  		  maxX[i] = mRenderer.getXAxisMax(i);
  		  minY[i] = mRenderer.getYAxisMin(i);
  		  maxY[i] = mRenderer.getYAxisMax(i);
  		  isMinXSet[i] = mRenderer.isMinXSet(i);
  		  isMaxXSet[i] = mRenderer.isMaxXSet(i);
  		  isMinYSet[i] = mRenderer.isMinYSet(i);
  		  isMaxYSet[i] = mRenderer.isMaxYSet(i);
  		  if (mCalcRange.get(i) == null) 
  		  {
  			  mCalcRange.put(i, new double[4]);
  		  }
  	  }
    
  	  Map<Integer, List<Double>> allYLabels = new HashMap<Integer, List<Double>>();
  	  for (int i = 0; i < maxScaleNumber; i++) 
  	  {
  		  paint.setTextSize(mRenderer.getLabelsTextSize() * mRenderer.getZoomRate());
  		  fm = paint.getFontMetrics();
  		  float yLabelHeight = fm.descent - fm.ascent;
  		  int lines = (int)((bottom - top) / yLabelHeight) / 2;
  		  int approxNumLabels = Math.min(mRenderer.getYLabels(), lines);
  		  
  		  allYLabels.put(i, getValidLabels(MathHelper.getLabels(minY[i], maxY[i], approxNumLabels)));
  	  }
    
  	  for (int i = 0; i < maxScaleNumber; i++) 
  	  {       
  		  if(Math.abs(minY[i]) > 0.001)
  		  {
  			  //minY[i] != 0
  			  List<Double> yLabels = allYLabels.get(i);
  			  double miny = yLabels.get(0) - (yLabels.get(1) - yLabels.get(0));
  			  if(minY[i] > 0 && miny > 0)
  			  {  				  
  				  minY[i] = miny;
  			  }
  		  }
  	  }
    
  	  float yLabelMaxWidth = 0;
  	  paint.setTextSize(mRenderer.getLabelsTextSize() * mRenderer.getZoomRate());
  	  for (int i = 0; i < maxScaleNumber; i++) 
  	  {
  		  List<Double> yLabels = allYLabels.get(i);
  		  int length = yLabels.size();
  		  for (int j = 0; j < length; j++) 
  		  {
  			  double label = yLabels.get(j);
  			  minY[i] = Math.min(minY[i], label);
  			  maxY[i] = Math.max(maxY[i], label);
  			  yLabelMaxWidth = Math.max(yLabelMaxWidth, paint.measureText(getLabel(label)));
  		  }
  	  }
    
    
    left += yLabelMaxWidth;
    
    if (mScreenR == null) 
    {
      mScreenR = new Rect();
    }
    mScreenR.set(left, top, right, bottom);
    
    //draw series background and frame
    drawSeriesBackgroundAndFrame(mRenderer, canvas, mScreenR, paint);
//    paint.setColor(mRenderer.getSeriesBackgroundColor());
//    canvas.drawRect(mScreenR, paint);
    
    double[] xPixelsPerUnit = new double[maxScaleNumber];
    double[] yPixelsPerUnit = new double[maxScaleNumber];
    for (int i = 0; i < sLength; i++) 
    {
      XYSeries series = mDataset.getSeriesAt(i);
      int scale = series.getScaleNumber();
      if (series.getItemCount() == 0)
      {
        continue;
      }
      if (!isMinXSet[scale]) 
      {
        double minimumX = series.getMinX();
        minX[scale] = Math.min(minX[scale], minimumX);
        mCalcRange.get(scale)[0] = minX[scale];
      }
      if (!isMaxXSet[scale]) 
      {
        double maximumX = series.getMaxX();
        maxX[scale] = Math.max(maxX[scale], maximumX);
        mCalcRange.get(scale)[1] = maxX[scale];
      }
      if (!isMinYSet[scale])
      {
        double minimumY = series.getMinY();
        minY[scale] = Math.min(minY[scale], (float) minimumY);
        mCalcRange.get(scale)[2] = minY[scale];
      }
      if (!isMaxYSet[scale]) 
      {
        double maximumY = series.getMaxY();
        maxY[scale] = Math.max(maxY[scale], (float) maximumY);
        mCalcRange.get(scale)[3] = maxY[scale];
      }
    }
    
    for (int i = 0; i < maxScaleNumber; i++)
    {    	  
  	  if (maxX[i] - minX[i] != 0) 
  	  {
  		  xPixelsPerUnit[i] = (right - left) / (maxX[i] - minX[i]);
  	  }
  	  if (maxY[i] - minY[i] != 0)
  	  {
  		  yPixelsPerUnit[i] = (float) ((bottom - top) / (maxY[i] - minY[i]));
  	  }
    }
    
    //draw title, label, grid
    float off = Math.max(mRenderer.getZoomRate() / 2, 0.5f);
    boolean hasValues = false;    
    for(int i = 0; i < sLength; i++)
    {
        if (mDataset.getSeriesAt(i).getItemCount() > 0) 
        {
            hasValues = true;
            break;
        }
    }
    boolean showLabels = mRenderer.isShowLabels() && hasValues;
    boolean showGrid = mRenderer.isShowGridH();
    boolean showCustomTextGrid = mRenderer.isShowCustomTextGrid();
    if (showLabels || showGrid) 
    {
        List<Double> xLabels;
        if(!getChartType().equals(ScatterChart.TYPE))
        {
            xLabels = new ArrayList<Double>();
            double xLabel = minX[0] + 1;
            while(xLabel <= maxX[0])
            {
                xLabels.add(Math.floor(xLabel));
                xLabel += 1; 
            }  
        }
        else
        {
            xLabels = getValidLabels(MathHelper.getLabels(minX[0], maxX[0], mRenderer.getXLabels()));
            minX[0] = xLabels.get(0);
            maxX[0] = xLabels.get(xLabels.size() - 1);
            xPixelsPerUnit[0] = (right - left) / (maxX[0] - minX[0]);
        }
      
      
      //draw x text label
      int xLabelsLeft = left;
      if (showLabels)
      { 
    	  paint.setColor(mRenderer.getLabelsColor());
    	  paint.setTextSize(mRenderer.getLabelsTextSize() * mRenderer.getZoomRate());
    	  paint.setTextAlign(mRenderer.getXLabelsAlign());
    	  if (mRenderer.getXLabelsAlign() == Align.LEFT) 
    	  {
    		  xLabelsLeft += mRenderer.getLabelsTextSize() / 4;
    	  }
      }
      
      float yAxeX = bottom;
      if(minY[0] < 0)
      {
          yAxeX = (float) (bottom + yPixelsPerUnit[0] * minY[0]);
      }
      
      if(!getChartType().equals(ScatterChart.TYPE))
      {
          drawXLabels(xLabels, mRenderer.getXTextLabelLocations(), canvas, paint, xLabelsLeft, top,
              yAxeX, xPixelsPerUnit[0], minX[0]);  
      }
      else
      {
          drawXLabels(xLabels, null, canvas, paint, xLabelsLeft, top,
              yAxeX, xPixelsPerUnit[0], minX[0]);  
      }
     
      //draw y text label      
      for (int i = 0; i < maxScaleNumber; i++) 
      {          
        paint.setTextAlign(mRenderer.getYLabelsAlign(i));
        List<Double> yLabels = allYLabels.get(i);
        if(Math.abs(yLabels.get(0)- minY[0]) > 0.000001f)
        {
            yLabels.add(minY[0]);
        }        
        
        int length = yLabels.size();
        for (int j = 0; j < length; j++) 
        {
          double label = yLabels.get(j);
          Align axisAlign = mRenderer.getYAxisAlign(i);
          boolean textLabel = mRenderer.getYTextLabel(label, i) != null;
          float yLabel = (float) (bottom - yPixelsPerUnit[i] * (label - minY[i]));
          if (or == Orientation.HORIZONTAL) 
          {
            if (showLabels && !textLabel)
            {
              paint.setColor(mRenderer.getLabelsColor());
              if (axisAlign == Align.LEFT) 
              {             
                drawText(canvas, getLabel(label), left - paint.measureText(getLabel(label)), yLabel, paint,
                		mRenderer.getYLabelsAngle());
              }
              else 
              {
                drawText(canvas, getLabel(label), right, yLabel - 2, paint, mRenderer.getYLabelsAngle());
              }
            }
            if (showGrid)
            {
            	paint.setColor(mRenderer.getGridColor());
            	canvas.drawRect(left, yLabel - off, right, yLabel + off, paint);
            }
          } 
          else if (or == Orientation.VERTICAL)
          {
            if (showLabels && !textLabel) 
            {
              paint.setColor(mRenderer.getLabelsColor());
              //canvas.drawLine(right - getLabelLinePos(axisAlign), yLabel, right, yLabel, paint);
              drawText(canvas, getLabel(label), right + 10, yLabel - 2, paint, mRenderer.getYLabelsAngle());
            }
            if (showGrid) 
            {
              paint.setColor(mRenderer.getGridColor());
              canvas.drawRect(right, Math.round(yLabel) - 1, left, Math.round(yLabel), paint);
            }
          }
        }
      }

      if (showLabels) 
      {
        paint.setColor(mRenderer.getLabelsColor());
        for (int i = 0; i < maxScaleNumber; i++) 
        {
          Align axisAlign = mRenderer.getYAxisAlign(i);
          Double[] yTextLabelLocations = mRenderer.getYTextLabelLocations(i);
          for (Double location : yTextLabelLocations)
          {
            if (minY[i] <= location && location <= maxY[i]) 
            {
              float yLabel = (float) (bottom - yPixelsPerUnit[i]
                  * (location.doubleValue() - minY[i]));
              String label = mRenderer.getYTextLabel(location, i);
              paint.setColor(mRenderer.getLabelsColor());
              if (or == Orientation.HORIZONTAL)
              {
                if (axisAlign == Align.LEFT)
                {
                  canvas.drawLine(left + getLabelLinePos(axisAlign), yLabel, left, yLabel, paint);
                  drawText(canvas, label, left, yLabel - 2, paint, mRenderer.getYLabelsAngle());
                } 
                else 
                {
                  canvas.drawLine(right, yLabel, right + getLabelLinePos(axisAlign), yLabel, paint);
                  drawText(canvas, label, right, yLabel - 2, paint, mRenderer.getYLabelsAngle());
                }
                if (showCustomTextGrid)
                {
                  paint.setColor(mRenderer.getGridColor());
                  canvas.drawLine(left, yLabel, right, yLabel, paint);
                }
              } 
              else 
              {
                canvas.drawLine(right - getLabelLinePos(axisAlign), yLabel, right, yLabel, paint);
                drawText(canvas, label, right + 10, yLabel - 2, paint, mRenderer.getYLabelsAngle());
                if (showCustomTextGrid) 
                {
                  paint.setColor(mRenderer.getGridColor());
                  canvas.drawLine(right, yLabel, left, yLabel, paint);
                }
              }
            }
          }
        }
      }

     
      
      //draw title
      if (showLabels) 
      {
        paint.setColor(mRenderer.getLabelsColor());
        paint.setTextAlign(Align.CENTER);
        paint.setFakeBoldText(true);
        
        if (or == Orientation.HORIZONTAL) 
        {
        	//draw chart title
       	 	if(mRenderer.isShowChartTitle())
       	 	{
       	 		paint.setTextSize(mRenderer.getChartTitleTextSize() * mRenderer.getZoomRate());
       	 		Rectangle maxTitleAreaSize = getMaxTitleAreaSize(width, height);       	 	
            	drawTitle(canvas, mRenderer.getChartTitle(), 1.0f, x + width / 2, y + mRenderer.getChartTitleTextSize() * mRenderer.getZoomRate() * 2, 
            		  maxTitleAreaSize.width, maxTitleAreaSize.height, paint, 0);
       	 	}
       	 	
       	 	//draw y title
       	 	if(yTitleAreaSize != null)
       	 	{        			 
       	 		paint.setTextSize(mRenderer.getYTitleTextSize() * mRenderer.getZoomRate());
       	 		float maxWidth = height * 0.8f;
       	 		float maxHeight = width * 0.2f;
       	 		float yTitleTop = y;
       	 		if(titleAreaSize != null && yTitleAreaSize.height == (int)maxWidth)
       	 		{
       	 			yTitleTop = y + titleAreaSize.height + height / 2;
       	 		}
       	 		else
       	 		{
       	 			yTitleTop = y + height / 2;
       	 		}
             	drawTitle(canvas, mRenderer.getYTitle(), 1.0f, 
             			x + mRenderer.getYTitleTextSize() * mRenderer.getZoomRate() * 1.5f, yTitleTop, maxWidth, maxHeight, paint, -90);
       	 	}
   		 
       	 	//draw x title
       	 	if(xTitleAreaSize != null)
       	 	{
       	 		float maxWidth = width * 0.8f;
       	 		float maxHeight = height * 0.2f;
       	 		paint.setTextSize(mRenderer.getXTitleTextSize() * mRenderer.getZoomRate());
       	 		fm = paint.getFontMetrics();
       	 		float xTitleLeft = x;
       	 		float yTitleTop = y + height - xTitleAreaSize.height;
       	 		if(yTitleAreaSize != null)
       	 		{
       	 			xTitleLeft = x + (width + yTitleAreaSize.width) / 2;
       	 		}
       	 		else
       	 		{
       	 			xTitleLeft = x + width / 2;
       	 		}
       	 		
       	 		if(legendSize != null && (legendPos == LegendPosition_Top || legendPos == LegendPosition_Bottom))
       	 		{
       	 			yTitleTop = y + height - legendSize.height - xTitleAreaSize.height;
       	 		}
       	 		drawTitle(canvas, mRenderer.getXTitle(), 1.0f, xTitleLeft, yTitleTop + fm.descent, maxWidth, maxHeight, paint, 0);
       	 	}
        } 
        else if (or == Orientation.VERTICAL) 
        {
        	drawText(canvas, mRenderer.getXTitle(), x + width / 2, y + height, paint, -90);
        	drawText(canvas, mRenderer.getYTitle(), right + 20 * mRenderer.getZoomRate(), y + height / 2, paint, 0);
        	paint.setTextSize(mRenderer.getChartTitleTextSize()* mRenderer.getZoomRate());
        	drawText(canvas, mRenderer.getChartTitle(), x, top + height / 2, paint, 0);
        }
        
        paint.setFakeBoldText(false);
      }
    }
    
    //draw series    
    for (int i = 0; i < sLength; i++)
    {    	
    	XYSeries series = mDataset.getSeriesAt(i);
    	int scale = series.getScaleNumber();
    	if (series.getItemCount() == 0) 
    	{
    		continue;
    	}
    	SimpleSeriesRenderer seriesRenderer = mRenderer.getSeriesRendererAt(i);
    	int originalValuesLength = series.getItemCount();
    	int valuesLength = originalValuesLength;
    	int length = valuesLength * 2;
    	List<Float> points = new ArrayList<Float>();
    	for (int j = 0; j < length; j += 2) 
    	{
    		int index = j / 2;
    		double yValue = series.getY(index);
    		if (yValue != MathHelper.NULL_VALUE)
    		{
    			points.add((float) (left + xPixelsPerUnit[scale] * (series.getX(index) - minX[scale])));
    			points.add((float) (bottom - yPixelsPerUnit[scale] * (yValue - minY[scale])));
    		} 
    		else 
    		{
    			if (points.size() > 0) 
    			{
    				drawSeries(series, canvas, paint, points, seriesRenderer, Math.min(bottom,
    						(float) (bottom + yPixelsPerUnit[scale] * minY[scale])), i, or);
    				points.clear();
    			}
    		}
    	}
      
    	if (points.size() > 0) 
    	{
    		drawSeries(series, canvas, paint, points, seriesRenderer, Math.min(bottom,
    				(float) (bottom + yPixelsPerUnit[scale] * minY[scale])), i, or);        

    		paint.setStyle(Style.FILL);
    	}
    }

    // draw stuff over the margins such as data doesn't render on these areas
    drawBackground(mRenderer, canvas, x, bottom, width, height - bottom, paint, true, mRenderer
        .getMarginsColor());
    drawBackground(mRenderer, canvas, x, y, width,(int)(margins[0] * height), paint, true, mRenderer
        .getMarginsColor());
    if (or == Orientation.HORIZONTAL)
    {
      drawBackground(mRenderer, canvas, x, y, left - x, height - y, paint, true, mRenderer
          .getMarginsColor());
      drawBackground(mRenderer, canvas, right, y, (int)(margins[3] * width), height - y, paint, true, mRenderer
          .getMarginsColor());
    }
    else if (or == Orientation.VERTICAL) 
    {
      drawBackground(mRenderer, canvas, right, y, width - right, height - y, paint, true, mRenderer
          .getMarginsColor());
      drawBackground(mRenderer, canvas, x, y, left - x, height - y, paint, true, mRenderer
          .getMarginsColor());
    }


    
    //draw legend(series color and name)    
    if (or == Orientation.HORIZONTAL) 
    {
    	if(mRenderer.isShowLegend())
    	{
    		int legendWidth = legendSize.width;
        	int legendHeight2 = Math.min(height, legendSize.height);
        	int legendLeft = x;
        	int legendTop = y;
        	switch(getLegendPosition())
        	{
    	    	case LegendPosition_Right:
    	    	case LegendPosition_Left:
    	    		legendLeft = x + width - legendWidth - (int)(mRenderer.getLegendTextSize() * mRenderer.getZoomRate());
    	    		if(titleAreaSize != null)
    	    		{
    	    			legendTop = y + (height + titleAreaSize.height) / 2;
    	    		}
    	    		else
    	    		{    	    			
    	    			legendTop = y + (height - legendHeight2) / 2;
    	    		}
    	    		break;
    	    	
    	    	case LegendPosition_Top:
    	    	case LegendPosition_Bottom:
    	    		legendLeft = x + (width - legendWidth) / 2;
    	    		legendTop = y + height - legendHeight2;
    	    		break;
        	}
        	
        	drawLegend(canvas, mRenderer, titles, legendLeft, legendTop, legendWidth, legendHeight2, paint, false);
    	}
    	
    }
    else if (or == Orientation.VERTICAL) 
    {
    	transform(canvas, angle, true);
    	drawLegend(canvas, mRenderer, titles, x + SSConstant.SHEET_SPACETOBORDER, y, width, height, paint, false);
    	transform(canvas, angle, false);
    }
    
    //chart axes
    if (mRenderer.isShowAxes()) 
    {    	
    	paint.setColor(mRenderer.getAxesColor());
    	paint.setFakeBoldText(true);
      
    	//x axe
    	float yAxeX = bottom;
    	if(minY[0] < 0)
    	{
    		yAxeX = (float)( bottom + yPixelsPerUnit[0] * minY[0]);
    	}
    	
    	canvas.drawRect(left, Math.round(bottom) - off, right, Math.round(bottom) + off, paint);
    	boolean rightAxis = false;
    	for(int i = 0; i < maxScaleNumber && !rightAxis; i++) 
    	{
    		rightAxis = mRenderer.getYAxisAlign(i) == Align.RIGHT;
    	}
      
    	//y axe
    	if (or == Orientation.HORIZONTAL) 
    	{
    		canvas.drawRect(left - off, top, left + off, bottom, paint);
    		if (rightAxis)
    		{
    			canvas.drawRect(right - off, top, right + off, bottom, paint);
    		}
    	} 
    	else if (or == Orientation.VERTICAL) 
    	{
    		canvas.drawRect(right - off, top, right + off, bottom, paint);
    	}
      
    	paint.setFakeBoldText(false);
    }
    
    if (rotate) 
    {
    	transform(canvas, angle, true);
    }
    
    paint.setColor(preColor);
    paint.setStrokeWidth(preSize);
    
    canvas.restore();
  }
  
  protected Rect getScreenR() 
  {
      return mScreenR;
  }
  
  protected void setScreenR(Rect screenR)
  {
      mScreenR = screenR;
  }
  
  private List<Double> getValidLabels(List<Double> labels)
  {
      List<Double> result = new ArrayList<Double>(labels);
      for (Double label : labels) 
      {
          if (label.isNaN()) 
          {
              result.remove(label);
          }
      }
      return result;
  }

  protected void drawSeries(XYSeries series, Canvas canvas, Paint paint, List<Float> pointsList,
      SimpleSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, Orientation or) 
  {
      BasicStroke stroke = seriesRenderer.getStroke();
      Cap cap = paint.getStrokeCap();
      Join join = paint.getStrokeJoin();
      float miter = paint.getStrokeMiter();
      PathEffect pathEffect = paint.getPathEffect();
      Style style = paint.getStyle();
      if (stroke != null) 
      {
          PathEffect effect = null;
          if (stroke.getIntervals() != null) 
          {
              effect = new DashPathEffect(stroke.getIntervals(), stroke.getPhase());
          }
          setStroke(stroke.getCap(), stroke.getJoin(), stroke.getMiter(), Style.FILL_AND_STROKE, effect, paint);
      }
      
      float[] points = MathHelper.getFloats(pointsList);
      drawSeries(canvas, paint, points, seriesRenderer, yAxisValue, seriesIndex);
      
      if (isRenderPoints(seriesRenderer)) 
      {
          ScatterChart pointsChart = getPointsChart();
          if (pointsChart != null)
          {
              pointsChart.drawSeries(canvas, paint, points, seriesRenderer, yAxisValue, seriesIndex);
          }
      }
      
      paint.setTextSize(seriesRenderer.getChartValuesTextSize());
      if (or == Orientation.HORIZONTAL) 
      {
          paint.setTextAlign(Align.CENTER);
      } 
      else 
      {
          paint.setTextAlign(Align.LEFT);
      }
      
      if (seriesRenderer.isDisplayChartValues())
      {
          drawChartValuesText(canvas, series, paint, points, seriesIndex);
      }
      
      if (stroke != null) 
      {
          setStroke(cap, join, miter, style, pathEffect, paint);
      }
  }
  
  private void setStroke(Cap cap, Join join, float miter, Style style, PathEffect pathEffect, Paint paint) 
  {
      paint.setStrokeCap(cap);
      paint.setStrokeJoin(join);
      paint.setStrokeMiter(miter);
      paint.setPathEffect(pathEffect);
      paint.setStyle(style);
  }
  
  /**
   * The graphical representation of the series values as text.
   * 
   * @param canvas the canvas to paint to
   * @param series the series to be painted
   * @param paint the paint to be used for drawing
   * @param points the array of points to be used for drawing the series
   * @param seriesIndex the index of the series currently being drawn
   */
  protected void drawChartValuesText(Canvas canvas, XYSeries series, Paint paint, float[] points,
      int seriesIndex) {
    for (int k = 0; k < points.length; k += 2) {
      drawText(canvas, getLabel(series.getY(k / 2)), points[k], points[k + 1] - 3.5f, paint, 0);
    }
  }

  /**
   * The graphical representation of a text, to handle both HORIZONTAL and
   * VERTICAL orientations and extra rotation angles.
   * 
   * @param canvas the canvas to paint to
   * @param text the text to be rendered
   * @param x the X axis location of the text
   * @param y the Y axis location of the text
   * @param paint the paint to be used for drawing
   * @param extraAngle the text angle
   */
  protected void drawText(Canvas canvas, String text, float x, float y, Paint paint, float extraAngle) 
  {
    float angle = -mRenderer.getOrientation().getAngle() + extraAngle;
    if (angle != 0) {
      // canvas.scale(1 / mScale, mScale);
      canvas.rotate(angle, x, y);
    }
    canvas.drawText(text, x, y, paint);
    if (angle != 0) {
      canvas.rotate(-angle, x, y);
      // canvas.scale(mScale, 1 / mScale);
    }
  }
  
  private Rectangle getXTitleTextAreaSize(int chartWidth, int chartHeight, Paint paint)
  {
	  if(mRenderer.getXTitle().length() > 0)
	  {
		  float maxWidth = chartWidth * 0.8f;
		  float maxHeight = chartHeight * 0.2f;
		  return getTextSize(mRenderer.getXTitle(), mRenderer.getXTitleTextSize() * mRenderer.getZoomRate(), maxWidth, maxHeight, paint);
	  }
	  else
	  {
		  return null;
	  }
  }
  
  private Rectangle getYTitleTextAreaSize(int chartWidth, int chartHeight, Paint paint)
  {	  
	  if(mRenderer.getYTitle().length() > 0)
	  {
		  //rotate 90
		  float maxWidth = chartHeight * 0.8f;
		  float maxHeight = chartWidth * 0.2f;
		  Rectangle size = getTextSize(mRenderer.getYTitle(), mRenderer.getXTitleTextSize() * mRenderer.getZoomRate(), maxWidth, maxHeight, paint);
		  
		  int w = size.width;
		  size.width = size.height;
		  size.height = w;
		  
		  return size;
	  }
	  else
	  {
		  return null;
	  }
  }
  
  /**
   * Transform the canvas such as it can handle both HORIZONTAL and VERTICAL
   * orientations.
   * 
   * @param canvas the canvas to paint to
   * @param angle the angle of rotation
   * @param inverse if the inverse transform needs to be applied
   */
  private void transform(Canvas canvas, float angle, boolean inverse) {
    if (inverse) {
      canvas.scale(1 / mScale, mScale);
      canvas.translate(mTranslate, -mTranslate);
      canvas.rotate(-angle, mCenter.x, mCenter.y);
    } else {
      canvas.rotate(angle, mCenter.x, mCenter.y);
      canvas.translate(-mTranslate, mTranslate);
      canvas.scale(mScale, 1 / mScale);
    }
  }

  /**
   * Makes sure the fraction digit is not displayed, if not needed.
   * 
   * @param label the input label value
   * @return the label without the useless fraction digit
   */
  protected String getLabel(double label) {
    String text = "";
    if (label == Math.round(label)) {
      text = Math.round(label) + "";
    } else {
      text = label + "";
    }
    return text;
  }

  /**
   * The graphical representation of the labels on the X axis.
   * 
   * @param xLabels the X labels values
   * @param xTextLabelLocations the X text label locations
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param left the left value of the labels area
   * @param top the top value of the labels area
   * @param bottom the bottom value of the labels area
   * @param xPixelsPerUnit the amount of pixels per one unit in the chart labels
   * @param minX the minimum value on the X axis in the chart
   */
  protected void drawXLabels(List<Double> xLabels, Double[] xTextLabelLocations, Canvas canvas,
      Paint paint, int left, int top, float bottom, double xPixelsPerUnit, double minX) 
  {
    int length = xLabels.size();
    boolean showLabels = mRenderer.isShowLabels();
    boolean showGrid = mRenderer.isShowGridV();
    boolean showCustomTextGrid = mRenderer.isShowCustomTextGrid();
    float off = Math.max(mRenderer.getZoomRate() / 2, 0.5f);
    if(xTextLabelLocations == null || xTextLabelLocations.length == 0)
    {
    	//for scatter
        for (int i = 0; i < length; i++) 
        {
          double label = xLabels.get(i);
          float xLabel = (float) (left + xPixelsPerUnit * (label - minX));          
          
          if(showGrid)
          {
          	canvas.drawRect(xLabel - off, top, xLabel + off, bottom + 4 * mRenderer.getZoomRate(), paint);
          }
          else
          {
          	canvas.drawRect(xLabel - off, bottom, xLabel + off, bottom + 4 * mRenderer.getZoomRate(), paint);
          }
          
          drawText(canvas, getLabel(label), xLabel, bottom + mRenderer.getLabelsTextSize() * 4 / 3 * mRenderer.getZoomRate(),
                  paint, mRenderer.getXLabelsAngle());
          
          if (showCustomTextGrid)
          {
            paint.setColor(mRenderer.getGridColor());
            canvas.drawRect(xLabel + (float)xPixelsPerUnit / 2 - off, bottom, xLabel + (float)xPixelsPerUnit / 2 + off, top, paint);
          }
        }
    }    
    else if (showLabels)
    {
      paint.setColor(mRenderer.getLabelsColor());
      for (Double location : xTextLabelLocations)
      {
        float xLabel = (float) (left + xPixelsPerUnit * (location.doubleValue() - minX));
        paint.setColor(mRenderer.getLabelsColor());
        if(showGrid)
        {
        	canvas.drawRect(xLabel + (float)xPixelsPerUnit / 2 - off, top, xLabel + (float)xPixelsPerUnit / 2 + off, bottom + 4 * mRenderer.getZoomRate(), paint);
        }
        else
        {
        	canvas.drawRect(xLabel + (float)xPixelsPerUnit / 2 - off, bottom, xLabel + (float)xPixelsPerUnit / 2 + off, bottom + 4 * mRenderer.getZoomRate(), paint);
        }
        
        drawText(canvas, mRenderer.getXTextLabel(location), xLabel, bottom
            + mRenderer.getLabelsTextSize() * mRenderer.getZoomRate(), paint, mRenderer.getXLabelsAngle());
        if (showCustomTextGrid) 
        {
          paint.setColor(mRenderer.getGridColor());
          //canvas.drawLine(xLabel, bottom, xLabel, top, paint);
          canvas.drawRect(xLabel + (float)xPixelsPerUnit / 2 - off, bottom, xLabel + (float)xPixelsPerUnit / 2 + off, top, paint);
        }
      }
    }
  }
  
  // TODO: docs
  public XYMultipleSeriesRenderer getRenderer() {
    return mRenderer;
  }

  public XYMultipleSeriesDataset getDataset() {
    return mDataset;
  }

  public double[] getCalcRange(int scale) {
    return mCalcRange.get(scale);
  }
  
  public void setCalcRange(double[] range, int scale) {
    mCalcRange.put(scale, range);
  }

  public double[] toRealPoint(float screenX, float screenY) {
    return toRealPoint(screenX, screenY, 0);
  }

  public double[] toScreenPoint(double[] realPoint) {
    return toScreenPoint(realPoint, 0);
  }
  
  private int getLabelLinePos(Align align) {
    int pos = 4;
    if (align == Align.LEFT) {
      pos = -pos;
    }
    return pos;
  }

  /**
   * Transforms a screen point to a real coordinates point.
   * 
   * @param screenX the screen x axis value
   * @param screenY the screen y axis value
   * @return the real coordinates point
   */
  public double[] toRealPoint(float screenX, float screenY, int scale) {
    double realMinX = mRenderer.getXAxisMin(scale);
    double realMaxX = mRenderer.getXAxisMax(scale);
    double realMinY = mRenderer.getYAxisMin(scale);
    double realMaxY = mRenderer.getYAxisMax(scale);
    return new double[] {
        (screenX - mScreenR.left) * (realMaxX - realMinX) / mScreenR.width() + realMinX,
        (mScreenR.top + mScreenR.height() - screenY) * (realMaxY - realMinY) / mScreenR.height()
            + realMinY };
  }

  public double[] toScreenPoint(double[] realPoint, int scale) {
    double realMinX = mRenderer.getXAxisMin(scale);
    double realMaxX = mRenderer.getXAxisMax(scale);
    double realMinY = mRenderer.getYAxisMin(scale);
    double realMaxY = mRenderer.getYAxisMax(scale);
    if (!mRenderer.isMinXSet(scale) || !mRenderer.isMaxXSet(scale) || !mRenderer.isMinXSet(scale) || !mRenderer.isMaxYSet(scale)) {
      double[] calcRange = getCalcRange(scale);
      realMinX = calcRange[0];
      realMaxX = calcRange[1];
      realMinY = calcRange[2];
      realMaxY = calcRange[3];
    }
    return new double[] {
        (realPoint[0] - realMinX) * mScreenR.width() / (realMaxX - realMinX) + mScreenR.left,
        (realMaxY - realPoint[1]) * mScreenR.height() / (realMaxY - realMinY) + mScreenR.top };
  }

  /**
   * The graphical representation of a series.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param points the array of points to be used for drawing the series
   * @param seriesRenderer the series renderer
   * @param yAxisValue the minimum value of the y axis
   * @param seriesIndex the index of the series currently being drawn
   */
  public abstract void drawSeries(Canvas canvas, Paint paint, float[] points,
      SimpleSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex);

  /**
   * Returns if the chart should display the points as a certain shape.
   * 
   * @param renderer the series renderer
   */
  public boolean isRenderPoints(SimpleSeriesRenderer renderer) {
    return false;
  }

  /**
   * Returns the default axis minimum.
   * 
   * @return the default axis minimum
   */
  public double getDefaultMinimum() {
    return MathHelper.NULL_VALUE;
  }

  /**
   * Returns the scatter chart to be used for drawing the data points.
   * 
   * @return the data points scatter chart
   */
  public ScatterChart getPointsChart() {
    return null;
  }

  /**
   * Returns the chart type identifier.
   * 
   * @return the chart type
   */
  public abstract String getChartType();
}
