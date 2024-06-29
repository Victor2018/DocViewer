/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.nvqquy98.lib.doc.office.fc.ss.usermodel.charts;

import java.util.List;

import com.nvqquy98.lib.doc.office.fc.ss.util.DataMarker;



/**
 * @author Roman Kashitsyn
 */
public interface ScatterChartData extends ChartData {
	/**
	 * @param xMarker data marker to be used for X value range
	 * @param yMarker data marker to be used for Y value range
	 * @return a new scatter chart serie
	 */
	ScatterChartSerie addSerie(DataMarker xMarker, DataMarker yMarker);

	/**
	 * @return list of all series
	 */
	List<? extends ScatterChartSerie> getSeries();
}
