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
import java.util.List;

import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.CategorySeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.DefaultRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * The pie chart rendering class.
 */
public class PieChart extends RoundChart {
  /**
   * Builds a new pie chart instance.
   * 
   * @param dataset the series dataset
   * @param renderer the series renderer
   */
  public PieChart(CategorySeries dataset, DefaultRenderer renderer) {
    super(dataset, renderer);
  }

  /**
   * The graphical representation of the pie chart.
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
	canvas.save();
	canvas.clipRect(x, y, x + width, y + height);
	  
    paint.setAntiAlias(mRenderer.isAntialiasing());
    paint.setStyle(Style.FILL);
    paint.setTextSize(mRenderer.getLabelsTextSize());
    
    drawBackgroundAndFrame(mRenderer, canvas, control, new Rect(x, y, x + width, y + height), paint);
    
    int legendSize = mRenderer.getLegendHeight();
    if (mRenderer.isShowLegend() && legendSize == 0)
    {
      legendSize = height / 5;
    }
    
    int sLength = mDataset.getItemCount();
    double total = 0;
    String[] titles = new String[sLength];
    for (int i = 0; i < sLength; i++) 
    {
      total += mDataset.getValue(i);
      titles[i] = mDataset.getCategory(i);
    }
    
    Rectangle titleAreaSize = getTitleTextAreaSize(mRenderer, width, height, paint);
    int legendH = height;
	if(titleAreaSize != null)
	{
		legendH -= titleAreaSize.height;
	}
	Rectangle legendAreaSize = getLegendAutoSize(mRenderer, titles, width, legendH, paint);
	 
    double[] margins = mRenderer.getMargins(); 
    int left = x + (int)(margins[1] * width);	  
	int top = y + (int)(margins[0] * height);
	if(titleAreaSize != null)
	{
		top += titleAreaSize.height;
	}
	int right = x + width - (int)(margins[3] * width);
	if(legendAreaSize != null && (legendPos == LegendPosition_Left || legendPos == LegendPosition_Right))
	{
		right -= legendAreaSize.width;
	}
	  
	int bottom = y + height - (int)(margins[2] * height);
	if(legendAreaSize != null && (legendPos == LegendPosition_Top || legendPos == LegendPosition_Bottom))
	{
		bottom -= legendAreaSize.height;
	}
	  
    float size = mRenderer.getLegendTextSize() * mRenderer.getZoomRate();
    paint.setTextSize(size );
    paint.setTextAlign(Align.CENTER);
    paint.setFakeBoldText(true);
    
    //draw chart title    
	 if(mRenderer.isShowChartTitle())
	 {
	 	paint.setTextSize(mRenderer.getChartTitleTextSize() * mRenderer.getZoomRate());
	 	Rectangle maxTitleAreaSize = getMaxTitleAreaSize(width, height);
    	drawTitle(canvas, mRenderer.getChartTitle(), 1.0f, x + width / 2, y + mRenderer.getChartTitleTextSize() * mRenderer.getZoomRate() * 2, 
    		  maxTitleAreaSize.width, maxTitleAreaSize.height, paint, 0);
	 }
	 	
    paint.setFakeBoldText(false);    
    
    bottom = y + height - legendSize;

    float currentAngle = 0;
    int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
    int radius = (int) (mRadius * 0.35 * mRenderer.getScale());
    
    int centerX = (int)(left + margins[1] * width + right - margins[3] * width) / 2;
    int centerY = (int)(bottom - margins[2] * height + top + margins[0] * height) / 2;
    float shortRadius = radius * 0.9f;
    float longRadius = radius * 1.1f;

    RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    List<RectF> prevLabelsBounds = new ArrayList<RectF>();
    for (int i = 0; i < sLength; i++) 
    {
      paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
      float value = (float) mDataset.getValue(i);
      float angle = (float) (value / total * 360);
      canvas.drawArc(oval, currentAngle - 90, angle, true, paint);
//      drawLabel(canvas, mDataset.getCategory(i), mRenderer, prevLabelsBounds, centerX, centerY,
//          shortRadius, longRadius, currentAngle - 90, angle, left, right, paint);
      currentAngle += angle;
    }
   
    
    prevLabelsBounds.clear();
    if(mRenderer.isShowLegend())
	{
		int legendWidth = legendAreaSize.width;
    	int legendHeight = Math.min(height, legendAreaSize.height);
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
	    			legendTop = y + (height - legendHeight) / 2;
	    		}
	    		break;
	    	
	    	case LegendPosition_Top:
	    	case LegendPosition_Bottom:
	    		legendLeft = x + (width - legendWidth) / 2;
	    		legendTop = y + height - legendHeight;
	    		break;
    	}
    	
    	drawLegend(canvas, mRenderer, titles, legendLeft, legendTop, legendWidth, legendHeight, paint, false);
	}
    
    canvas.restore();
  }
}
