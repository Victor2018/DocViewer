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
package com.nvqquy98.lib.doc.office.thirdpart.achartengine;

import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.AbstractChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.BubbleChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.ColumnBarChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.CombinedXYChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.DialChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.DoughnutChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.LineChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.PieChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.RangeBarChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.ScatterChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.TimeChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.ColumnBarChart.Type;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.CategorySeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.MultipleCategorySeries;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.model.XYMultipleSeriesDataset;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.DefaultRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.DialRenderer;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer;

/**
 * Utility methods for creating chart views or intents.
 */
public class ChartFactory {
  /** The key for the chart data. */
  public static final String CHART = "chart";

  /** The key for the chart graphical activity title. */
  public static final String TITLE = "title";

  private ChartFactory() {
    // empty for now
  }

  /**
   * Creates a line chart.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @return a line chart
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset and the renderer don't include the same number of
   *           series
   */
  public static final AbstractChart getLineChart(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    checkParameters(dataset, renderer);
    return new LineChart(dataset, renderer);
  }

  /**
   * Creates a scatter chart.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @return a scatter chart
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset and the renderer don't include the same number of
   *           series
   */
  public static final AbstractChart getScatterChart(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    checkParameters(dataset, renderer);
    return new ScatterChart(dataset, renderer);
  }

  /**
   * Creates a bubble chart.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @return a bubble chart
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset and the renderer don't include the same number of
   *           series
   */
  public static final AbstractChart getBubbleChart(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    checkParameters(dataset, renderer);
    return new BubbleChart(dataset, renderer);
  }

  /**
   * Creates a time chart.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @param format the date format pattern to be used for displaying the X axis
   *          date labels. If null, a default appropriate format will be used.
   * @return a time chart 
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset and the renderer don't include the same number of
   *           series
   */
  public static final AbstractChart getTimeChart(XYMultipleSeriesDataset dataset, 
      XYMultipleSeriesRenderer renderer, String format) {
    checkParameters(dataset, renderer);
    TimeChart chart = new TimeChart(dataset, renderer);
    chart.setDateFormat(format);
    return chart;
  }

  /**
   * Creates a bar chart.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @param type the bar chart type
   * @return a bar chart
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset and the renderer don't include the same number of
   *           series
   */
  public static final AbstractChart getColumnBarChart(XYMultipleSeriesDataset dataset, 
      XYMultipleSeriesRenderer renderer, Type type) {
    checkParameters(dataset, renderer);
     return new ColumnBarChart(dataset, renderer, type);
  }

  /**
   * Creates a range bar chart.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @param type the range bar chart type
   * @return a bar chart
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset and the renderer don't include the same number of
   *           series
   */
  public static final AbstractChart getRangeBarChart(XYMultipleSeriesDataset dataset, 
      XYMultipleSeriesRenderer renderer, Type type) {
    checkParameters(dataset, renderer);
    return new RangeBarChart(dataset, renderer, type);
  }

  /**
   * Creates a combined XY chart.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @param types the chart types (cannot be null)
   * @return a combined XY chart
   * @throws IllegalArgumentException if dataset is null or renderer is null
   *           or if a dataset number of items is different than the number of
   *           series renderers or number of chart types
   */
  public static final AbstractChart getCombinedXYChart(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer, String[] types) {
    if (dataset == null || renderer == null || types == null || dataset.getSeriesCount() != types.length) {
      throw new IllegalArgumentException(
          "Dataset, renderer and types should be not null and the datasets series count should be equal to the types length");
    }
    checkParameters(dataset, renderer);
    return new CombinedXYChart(dataset, renderer, types);
  }

  /**
   * Creates a pie chart 
   * 
   * @param dataset the category series dataset (cannot be null)
   * @param renderer the series renderer (cannot be null)
   * @return a pie chart 
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset number of items is different than the number of
   *           series renderers
   */
  public static final AbstractChart getPieChart(CategorySeries dataset,
      DefaultRenderer renderer) {
    checkParameters(dataset, renderer);
    return new PieChart(dataset, renderer);
  }

  /**
   * Creates a dial chart 
   * 
   * @param dataset the category series dataset (cannot be null)
   * @param renderer the dial renderer (cannot be null)
   * @return a pie chart 
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset number of items is different than the number of
   *           series renderers
   */
  public static final AbstractChart getDialChartView(CategorySeries dataset,
      DialRenderer renderer) {
    checkParameters(dataset, renderer);
    return new DialChart(dataset, renderer);
  }

  /**
   * Creates a doughnut chart 
   * 
   * @param dataset the multiple category series dataset (cannot be null)
   * @param renderer the series renderer (cannot be null)
   * @return a pie chart
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset number of items is different than the number of
   *           series renderers
   */
  public static final AbstractChart getDoughnutChartView(MultipleCategorySeries dataset, 
      DefaultRenderer renderer) {
    checkParameters(dataset, renderer);
    return new DoughnutChart(dataset, renderer);
  }

  /**
   * Checks the validity of the dataset and renderer parameters.
   * 
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset and the renderer don't include the same number of
   *           series
   */
  private static void checkParameters(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    if (dataset == null || renderer == null
        || dataset.getSeriesCount() != renderer.getSeriesRendererCount()) {
      throw new IllegalArgumentException(
          "Dataset and renderer should be not null and should have the same number of series");
    }
  }

  /**
   * Checks the validity of the dataset and renderer parameters.
   * 
   * @param dataset the category series dataset (cannot be null)
   * @param renderer the series renderer (cannot be null)
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset number of items is different than the number of
   *           series renderers
   */
  private static void checkParameters(CategorySeries dataset, DefaultRenderer renderer) {
    if (dataset == null || renderer == null
        || dataset.getItemCount() != renderer.getSeriesRendererCount()) {
      throw new IllegalArgumentException(
          "Dataset and renderer should be not null and the dataset number of items should be equal to the number of series renderers");
    }
  }

  /**
   * Checks the validity of the dataset and renderer parameters.
   * 
   * @param dataset the category series dataset (cannot be null)
   * @param renderer the series renderer (cannot be null)
   * @throws IllegalArgumentException if dataset is null or renderer is null or
   *           if the dataset number of items is different than the number of
   *           series renderers
   */
  private static void checkParameters(MultipleCategorySeries dataset, DefaultRenderer renderer) {
    if (dataset == null || renderer == null
        || !checkMultipleSeriesItems(dataset, renderer.getSeriesRendererCount())) {
      throw new IllegalArgumentException(
          "Titles and values should be not null and the dataset number of items should be equal to the number of series renderers");
    }
  }

  private static boolean checkMultipleSeriesItems(MultipleCategorySeries dataset, int value) {
    int count = dataset.getCategoriesCount();
    boolean equal = true;
    for (int k = 0; k < count && equal; k++) {
      equal = dataset.getValues(k).length == dataset.getTitles(k).length;
    }
    return equal;
  }

}
