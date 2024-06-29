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
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYSeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.SimpleSeriesRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

/**
 * The bar chart rendering class.
 */
public class ColumnBarChart extends XYChart
{
    /** The constant to identify this chart type. */
    public static final String TYPE = "Column Bar";
    
    /** The chart type. */
    protected Type mType = Type.DEFAULT;

    /**
     * The bar chart type enum.
     */
    public enum Type
    {
        DEFAULT, STACKED;
    }

    ColumnBarChart()
    {
    }

    /**
     * Builds a new bar chart instance.
     * 
     * @param dataset the multiple series dataset
     * @param renderer the multiple series renderer
     * @param type the bar chart type
     */
    public ColumnBarChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, Type type)
    {
        super(dataset, renderer);
        mType = type;
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
        int seriesNr = mDataset.getSeriesCount();
        int length = points.length;
        paint.setColor(seriesRenderer.getColor());
        paint.setStyle(Style.FILL);
        float halfDiffX = getHalfDiffX(points, length, seriesNr);
        for (int i = 0; i < length; i += 2)
        {
            float x = points[i];
            float y = points[i + 1];
            drawBar(canvas, x, yAxisValue, x, y, halfDiffX, seriesNr, seriesIndex, paint);
        }
        paint.setColor(seriesRenderer.getColor());
    }

    protected void drawBar(Canvas canvas, float xMin, float yMin, float xMax, float yMax,
        float halfDiffX, int seriesNr, int seriesIndex, Paint paint)
    {
        int scale = mDataset.getSeriesAt(seriesIndex).getScaleNumber();
        if (mType == Type.STACKED)
        {
            drawBar(canvas, xMin - halfDiffX, yMax, xMax + halfDiffX, yMin, scale, seriesIndex,
                paint);
        }
        else
        {
            float startX = xMin - seriesNr * halfDiffX + seriesIndex * 2 * halfDiffX;
            drawBar(canvas, startX, yMax, startX + 2 * halfDiffX, yMin, scale, seriesIndex, paint);
        }
    }

    private void drawBar(Canvas canvas, float xMin, float yMin, float xMax, float yMax, int scale,
        int seriesIndex, Paint paint)
    {
        SimpleSeriesRenderer renderer = mRenderer.getSeriesRendererAt(seriesIndex);
        if (renderer.isGradientEnabled())
        {
            float minY = (float)toScreenPoint(new double[]{0, renderer.getGradientStopValue()},
                scale)[1];
            float maxY = (float)toScreenPoint(new double[]{0, renderer.getGradientStartValue()},
                scale)[1];
            float gradientMinY = Math.max(minY, yMin);
            float gradientMaxY = Math.min(maxY, yMax);
            int gradientMinColor = renderer.getGradientStopColor();
            int gradientMaxColor = renderer.getGradientStartColor();
            int gradientStartColor = gradientMinColor;
            int gradientStopColor = gradientMaxColor;

            if (yMin < minY)
            {
                paint.setColor(gradientMaxColor);
                canvas.drawRect(Math.round(xMin), Math.round(yMin), Math.round(xMax),
                    Math.round(gradientMinY), paint);
            }
            else
            {
                gradientStopColor = getGradientPartialColor(gradientMaxColor, gradientMinColor,
                    (maxY - gradientMinY) / (maxY - minY));
            }
            if (yMax > maxY)
            {
                paint.setColor(gradientMinColor);
                canvas.drawRect(Math.round(xMin), Math.round(gradientMaxY), Math.round(xMax),
                    Math.round(yMax), paint);
            }
            else
            {
                gradientStartColor = getGradientPartialColor(gradientMinColor, gradientMaxColor,
                    (gradientMaxY - minY) / (maxY - minY));
            }
            GradientDrawable gradient = new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{
                gradientStartColor, gradientStopColor});
            gradient.setBounds(Math.round(xMin), Math.round(gradientMinY), Math.round(xMax),
                Math.round(gradientMaxY));
            gradient.draw(canvas);
        }
        else
        {
            if(Math.abs(yMax - yMin) < 0.0000001f)
            {
                return;
            }
            
            canvas.drawRect(Math.round(xMin), Math.round(yMin), Math.round(xMax), Math.round(yMax),
                paint);
            
            
            int color = paint.getColor();
            paint.setColor(Color.BLACK);
            paint.setStyle(Style.STROKE);
            
            canvas.drawRect(Math.round(xMin), Math.round(yMin), Math.round(xMax), Math.round(yMax),
                    paint);
            paint.setStyle(Style.FILL);
            paint.setColor(color);
        }
    }

    private int getGradientPartialColor(int minColor, int maxColor, float fraction)
    {
        int alpha = Math.round(fraction * Color.alpha(minColor) + (1 - fraction)
            * Color.alpha(maxColor));
        int r = Math.round(fraction * Color.red(minColor) + (1 - fraction) * Color.red(maxColor));
        int g = Math.round(fraction * Color.green(minColor) + (1 - fraction)
            * Color.green(maxColor));
        int b = Math.round(fraction * Color.blue(minColor) + (1 - fraction)
            * Color.blue((maxColor)));
        return Color.argb(alpha, r, g, b);
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
        int seriesIndex)
    {
        int seriesNr = mDataset.getSeriesCount();
        float halfDiffX = getHalfDiffX(points, points.length, seriesNr);
        for (int k = 0; k < points.length; k += 2)
        {
            float x = points[k];
            if (mType == Type.DEFAULT)
            {
                x += seriesIndex * 2 * halfDiffX - (seriesNr - 1.5f) * halfDiffX;
            }
            drawText(canvas, getLabel(series.getY(k / 2)), x, points[k + 1] - 3.5f, paint, 0);
        }
    }

    /**
     * Returns the legend shape width.
     * 
     * @param seriesIndex the series index
     * @return the legend shape width
     */
    public int getLegendShapeWidth(int seriesIndex)
    {
        return (int)getRenderer().getLegendTextSize()/*SHAPE_WIDTH*/;
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
        float shapeWidth = getRenderer().getLegendTextSize() * mRenderer.getZoomRate();
        float halfShapeWidth = shapeWidth / 2;
        x += halfShapeWidth;
        
        canvas.drawRect(x, y - halfShapeWidth, x + shapeWidth, y + halfShapeWidth, paint);
        //draw legend shape frame
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(x, y - halfShapeWidth, x + shapeWidth, y + halfShapeWidth, paint);
        paint.setStyle(Style.FILL);
    }

    /**
     * Calculates and returns the half-distance in the graphical representation of
     * 2 consecutive points.
     * 
     * @param points the points
     * @param length the points length
     * @param seriesNr the series number
     * @return the calculated half-distance value
     */
    protected float getHalfDiffX(float[] points, int length, int seriesNr)
    {
        int div = length;
        if (length > 2)
        {
            div = length - 2;
        }
        float halfDiffX = (points[length - 2] - points[0]) / div;
        if (halfDiffX == 0)
        {
            halfDiffX = getScreenR().width() / 2;;
        }

        if (mType != Type.STACKED)
        {
            halfDiffX /= (seriesNr + 1);
        }
        return (float)(halfDiffX / (getCoeficient() * (1 + mRenderer.getBarSpacing())));
    }

    /**
     * Returns the value of a constant used to calculate the half-distance.
     * 
     * @return the constant value
     */
    protected float getCoeficient()
    {
        return 1f;
    }

    /**
     * Returns the default axis minimum.
     * 
     * @return the default axis minimum
     */
    public double getDefaultMinimum()
    {
        return 0;
    }

    /**
     * Returns the chart type identifier.
     * 
     * @return the chart type
     */
    public String getChartType()
    {
        return TYPE;
    }
}
