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

import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYMultipleSeriesDataset;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.SimpleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * The scatter chart rendering class.
 */
public class ScatterChart extends XYChart {
  /** The constant to identify this chart type. */
  public static final String TYPE = "Scatter";
  /** The default point shape size. */
  private static final float SIZE = 3;
  
  /** The point shape size. */
  private float size = SIZE;

  private boolean drawFrame = true;
  
  ScatterChart() {
  }

  /**
   * Builds a new scatter chart instance.
   * 
   * @param dataset the multiple series dataset
   * @param renderer the multiple series renderer
   */
  public ScatterChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
    super(dataset, renderer);
    size = renderer.getPointSize();
  }

  // TODO: javadoc
  protected void setDatasetRenderer(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
    super.setDatasetRenderer(dataset, renderer);
    size = renderer.getPointSize();
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
  public void drawSeries(Canvas canvas, Paint paint, float[] points,
      SimpleSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex) 
  {
    XYSeriesRenderer renderer = (XYSeriesRenderer) seriesRenderer;
    paint.setColor(renderer.getColor());
    if (renderer.isFillPoints()) 
    {
      paint.setStyle(Style.FILL);
    }
    else 
    {
      paint.setStyle(Style.STROKE);
    }
    int length = points.length;
    switch (renderer.getPointStyle()) 
    {
    case X:
      for (int i = 0; i < length; i += 2) 
      {
        drawX(canvas, paint, points[i], points[i + 1]);
      }
      break;
    case CIRCLE:
      for (int i = 0; i < length; i += 2)
      {
        drawCircle(canvas, paint, points[i], points[i + 1]);
      }
      break;
    case TRIANGLE:
      float[] path = new float[6];
      for (int i = 0; i < length; i += 2) 
      {
        drawTriangle(canvas, paint, path, points[i], points[i + 1]);
      }
      break;
    case SQUARE:
      for (int i = 0; i < length; i += 2)
      {
        drawSquare(canvas, paint, points[i], points[i + 1]);
      }
      break;
    case DIAMOND:
      path = new float[8];
      for (int i = 0; i < length; i += 2)
      {
        drawDiamond(canvas, paint, path, points[i], points[i + 1]);
      }
      break;
    case POINT:
      canvas.drawPoints(points, paint);
      break;
    }
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
    if (((XYSeriesRenderer) renderer).isFillPoints())
    {
    	paint.setStyle(Style.FILL);
    } 
    else 
    {
    	paint.setStyle(Style.STROKE);
    }

    float shapeWidth = (int)getRenderer().getLegendTextSize() * mRenderer.getZoomRate();
    x += shapeWidth / 2;
    
    switch (((XYSeriesRenderer) renderer).getPointStyle()) 
    {
    case X:
      drawX(canvas, paint, x, y);
      break;
    case CIRCLE:
      drawCircle(canvas, paint, x, y);
      break;
    case TRIANGLE:
      drawTriangle(canvas, paint, new float[6], x, y);
      break;
    case SQUARE:
      drawSquare(canvas, paint, x, y);
      break;
    case DIAMOND:
      drawDiamond(canvas, paint, new float[8], x , y);
      break;
    case POINT:
      canvas.drawPoint(x , y, paint);
      break;
    }
//    if(drawFrame)
//    {
//        float halfShapeWidth =  shapeWidth/ 2;
//        x -= halfShapeWidth;
//        //draw legend shape frame
//        paint.setStyle(Style.STROKE);
//        paint.setColor(Color.BLACK);
//        paint.setAlpha(255);
//        canvas.drawRect(Math.round(x), y - halfShapeWidth, x + shapeWidth, y + halfShapeWidth, paint);
//        paint.setStyle(Style.FILL);
//    }    
  }

  /**
   * The graphical representation of an X point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawX(Canvas canvas, Paint paint, float x, float y)
  {
      float temSize = size * mRenderer.getZoomRate();
      canvas.drawLine(x - temSize, y - temSize, x + temSize, y + temSize, paint);
      canvas.drawLine(x + temSize, y - temSize, x - temSize, y + temSize, paint);
  }

  /**
   * The graphical representation of a circle point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawCircle(Canvas canvas, Paint paint, float x, float y) 
  {
      float temSize = size * mRenderer.getZoomRate();
      canvas.drawCircle(x, y, temSize, paint);
  }

  /**
   * The graphical representation of a triangle point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param path the triangle path
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawTriangle(Canvas canvas, Paint paint, float[] path, float x, float y) 
  {
      float temSize = size * mRenderer.getZoomRate();
      path[0] = x;
      path[1] = y - temSize - temSize / 2;
      path[2] = x - temSize;
      path[3] = y + temSize;
      path[4] = x + temSize;
      path[5] = path[3];
      drawPath(canvas, path, paint, true);
  }

  /**
   * The graphical representation of a square point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawSquare(Canvas canvas, Paint paint, float x, float y) 
  {
      float temSize = size * mRenderer.getZoomRate();
      canvas.drawRect(x - temSize, y - temSize, x + temSize, y + temSize, paint);
  }

  /**
   * The graphical representation of a diamond point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param path the diamond path
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawDiamond(Canvas canvas, Paint paint, float[] path, float x, float y) 
  {
      float temSize = size * mRenderer.getZoomRate();
      path[0] = x;
      path[1] = y - temSize;
      path[2] = x - temSize;
      path[3] = y;
      path[4] = x;
      path[5] = y + temSize;
      path[6] = x + temSize;
      path[7] = y;
      drawPath(canvas, path, paint, true);
  }

  /**
   * Returns the chart type identifier.
   * 
   * @return the chart type
   */
  public String getChartType() {
    return TYPE;
  }

  public void setDrawFrameFlag(boolean drawFrame)
  {
      this.drawFrame = drawFrame;
  }
  
  public boolean isDrawFrame()
  {
      return drawFrame;
  }
}
