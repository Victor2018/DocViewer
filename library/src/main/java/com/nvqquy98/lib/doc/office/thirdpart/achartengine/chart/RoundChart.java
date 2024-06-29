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

import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.CategorySeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.DefaultRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.SimpleSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * The pie chart rendering class.
 */
public abstract class RoundChart extends AbstractChart {
  /** The legend shape width. */
  protected static final int SHAPE_WIDTH = 10;
  /** The series dataset. */
  protected CategorySeries mDataset;
  /** The series renderer. */
  protected DefaultRenderer mRenderer;

  /**
   * Builds a new pie chart instance.
   * 
   * @param dataset the series dataset
   * @param renderer the series renderer
   */
  public RoundChart(CategorySeries dataset, DefaultRenderer renderer) {
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
      Rect rect = new Rect(x, y, x + width, y + height);
      canvas.save();
      canvas.clipRect(rect);
      
    paint.setAntiAlias(mRenderer.isAntialiasing());
    paint.setStyle(Style.FILL);
    paint.setTextSize(mRenderer.getLabelsTextSize());
    int legendSize = mRenderer.getLegendHeight();
    if (mRenderer.isShowLegend() && legendSize == 0) {
      legendSize = height / 5;
    }
    int left = x;
    int top = y;
    int right = x + width;
    int sLength = mDataset.getItemCount();
    double total = 0;
    String[] titles = new String[sLength];
    for (int i = 0; i < sLength; i++) {
      total += mDataset.getValue(i);
      titles[i] = mDataset.getCategory(i);
    }
    if (mRenderer.isFitLegend()) {
      legendSize = drawLegend(canvas, mRenderer, titles, left, y, width, height,
          paint, true);
    }
    int bottom = y + height - legendSize;
    drawBackground(mRenderer, canvas, x, y, width, height, paint, false, DefaultRenderer.NO_COLOR);

    float currentAngle = 0;
    int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
    int radius = (int) (mRadius * 0.35 * mRenderer.getScale());
    int centerX = (left + right) / 2;
    int centerY = (bottom + top) / 2;
    float shortRadius = radius * 0.9f;
    float longRadius = radius * 1.1f;

    RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    List<RectF> prevLabelsBounds = new ArrayList<RectF>();
    for (int i = 0; i < sLength; i++) {
      paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
      float value = (float) mDataset.getValue(i);
      float angle = (float) (value / total * 360);
      canvas.drawArc(oval, currentAngle, angle, true, paint);
      drawLabel(canvas, mDataset.getCategory(i), mRenderer, prevLabelsBounds, centerX, centerY,
          shortRadius, longRadius, currentAngle, angle, left, right, paint);
      currentAngle += angle;
    }
    prevLabelsBounds.clear();
    drawLegend(canvas, mRenderer, titles, left, y, width, height, paint, false);
  
    canvas.restore();
  }

  /**
   * Returns the legend shape width.
   * 
   * @param seriesIndex the series index
   * @return the legend shape width
   */
  public int getLegendShapeWidth(int seriesIndex) {
    return (int)getRenderer().getLegendTextSize();
  }

  /**
   * The graphical representation of the legend shape.
   * 
   * @param canvas the canvas to paint to
   * @param renderer the series renderer
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   * @param seriesIndex the series index
   * @param paint the paint to be used for drawing
   */
  public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y,
      int seriesIndex, Paint paint)
  {
      float shapeWidth = getLegendShapeWidth(0) * mRenderer.getZoomRate();
      float halfShapeWidth =  shapeWidth/ 2;
      x += halfShapeWidth;
      canvas.drawRect(x, y - halfShapeWidth, x + shapeWidth, y + halfShapeWidth, paint);
      //draw legend shape frame
      paint.setStyle(Style.STROKE);
      paint.setColor(Color.BLACK);
      canvas.drawRect(Math.round(x), y - halfShapeWidth, x + shapeWidth, y + halfShapeWidth, paint);
      paint.setStyle(Style.FILL);
  }

  /**
   * Returns the renderer.
   * 
   * @return the renderer
   */
  public DefaultRenderer getRenderer() {
    return mRenderer;
  }

}
