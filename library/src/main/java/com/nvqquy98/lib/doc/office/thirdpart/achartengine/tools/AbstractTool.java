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
package com.nvqquy98.lib.doc.office.thirdpart.achartengine.tools;

import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.AbstractChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.XYChart;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.renderers.XYMultipleSeriesRenderer;


/**
 * Abstract class for being extended by graphical view tools.
 */
public abstract class AbstractTool {
  /** The chart. */
  protected AbstractChart mChart;
  /** The renderer. */
  protected XYMultipleSeriesRenderer mRenderer;

  /**
   * Abstract tool constructor.
   * 
   * @param chart the chart
   */
  public AbstractTool(AbstractChart chart) {
    mChart = chart;
    if (chart instanceof XYChart) {
      mRenderer = ((XYChart) chart).getRenderer();
    }
  }

  public double[] getRange(int scale) {
    double minX = mRenderer.getXAxisMin(scale);
    double maxX = mRenderer.getXAxisMax(scale);
    double minY = mRenderer.getYAxisMin(scale);
    double maxY = mRenderer.getYAxisMax(scale);
    return new double[] { minX, maxX, minY, maxY };
  }

  public void checkRange(double[] range, int scale) {
    if (mChart instanceof XYChart) {
      double[] calcRange = ((XYChart) mChart).getCalcRange(scale);
      if (calcRange != null) {
        if (!mRenderer.isMinXSet(scale)) {
          range[0] = calcRange[0];
          mRenderer.setXAxisMin(range[0], scale);
        }
        if (!mRenderer.isMaxXSet(scale)) {
          range[1] = calcRange[1];
          mRenderer.setXAxisMax(range[1], scale);
        }
        if (!mRenderer.isMinYSet(scale)) {
          range[2] = calcRange[2];
          mRenderer.setYAxisMin(range[2], scale);
        }
        if (!mRenderer.isMaxYSet(scale)) {
          range[3] = calcRange[3];
          mRenderer.setYAxisMax(range[3], scale);
        }
      }
    }
  }

  protected void setXRange(double min, double max, int scale) {
    mRenderer.setXAxisMin(min, scale);
    mRenderer.setXAxisMax(max, scale);
  }

  protected void setYRange(double min, double max, int scale) {
    mRenderer.setYAxisMin(min, scale);
    mRenderer.setYAxisMax(max, scale);
  }

}
