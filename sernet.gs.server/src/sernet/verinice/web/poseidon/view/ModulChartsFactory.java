/*******************************************************************************
 * Copyright (c) 2017 Benjamin Weißenfels.
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     @author Benjamin Weißenfels <bw[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package sernet.verinice.web.poseidon.view;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 * @author Benjamin Weißenfels <bw[at]sernet[dot]de>
 *
 */
public class ModulChartsFactory {

    private Map<String, Map<String, Number>> data;

    public ModulChartsFactory(Map<String, Map<String, Number>> data) {
        this.data = data;
    }

    public HorizontalBarChartModel getHorizontalBarChartModel() {

        HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();


        setData(horizontalBarModel);

        horizontalBarModel.setTitle("Horizontal and Stacked");
        horizontalBarModel.setLegendPosition("e");

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);

        xAxis.setLabel("Status");
        xAxis.setMin(0);
        xAxis.setMax(getMax());
        xAxis.setTickInterval("5");

        yAxis.setLabel("Bausteinkapitel");

        horizontalBarModel.setExtender("bausteinUmsetzungHorizontalBar");
        horizontalBarModel.setSeriesColors(ChartUtils.getColors(data.keySet()));
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setShadow(false);

        return horizontalBarModel;
    }


    public BarChartModel getVerticalBarChartModel() {

       BarChartModel horizontalBarModel = new BarChartModel();

        setData(horizontalBarModel);

        horizontalBarModel.setLegendPosition("e");

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);

        xAxis.setLabel("Bausteinkapitel");
        xAxis.setMin(0);
        xAxis.setMax(getMax());
        xAxis.setTickInterval("100");
        xAxis.setTickAngle(90);

        yAxis.setLabel("Massnahmen");

        horizontalBarModel.setSeriesColors(ChartUtils.getColors(data.keySet()));
        horizontalBarModel.setShadow(false);
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setExtender("bausteinUmsetzungVerticalBar");

        return horizontalBarModel;
    }



    private Object getMax() {
        int MARGIN = 10;
        int max = 0;
        for (Map<String, Number> n : data.values()) {
            for (Number e2 : n.values()) {
                max = Math.max(max, e2.intValue());
            }
        }

        return max + MARGIN;
    }

    private void setData(BarChartModel horizontalBarModel) {
        for(Entry<String, Map<String, Number>> dataPoint : ChartUtils.transalteMapKeyLabel(this.data).entrySet()) {
            ChartSeries chartSeries = new ChartSeries(dataPoint.getKey());
            for(Entry<String, Number> e : dataPoint.getValue().entrySet()) {
                chartSeries.set(e.getKey(), e.getValue());
            }
            horizontalBarModel.addSeries(chartSeries);
        }
    }
}
