/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans.admin;

import java.util.Map;
import java.util.TreeMap;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author jsie
 */
/**
 *
 * @author jsie
 */
@Named
@ViewScoped
public class LoggedInUsersBean extends CommonUsersBean {

    private PieChartModel userTypes;

    private LineChartModel connections;

    /**
     * Inits the Pie Chart "Distribution of Sessions by Type"
     *
     * @return the data model of the Pie Chart
     */
    public PieChartModel getUserTypes() {

        Map<String, Number> map = new TreeMap<>();
        this.getLoggedInUsers().forEach(u -> {
            map.computeIfAbsent(u.getType(), t -> 0);
            map.put(u.getType(), map.get(u.getType()).intValue() + 1);
        });

        userTypes = new PieChartModel(map);
        userTypes.setTitle("Distribution of Sessions by Type");
        userTypes.setLegendPosition("nw");
        userTypes.setShowDataLabels(true);
        return userTypes;
    }

    /**
     * Sets the Line Chart "Active Sessions Record"
     *
     * @return the data model of the Line Chart
     */
    public LineChartModel getConnections() {
        connections = this.initConnectionModel();
        connections.setTitle("Active Sessions Record");
        connections.setLegendPosition("e");
        connections.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        connections.getAxes().put(AxisType.X, new CategoryAxis(
                "Last Accessed Time (in minutes)"));
        Axis yAxis = connections.getAxis(AxisType.Y);
        yAxis.setLabel("Connections");
        yAxis.setMin(0);
//        yAxis.setMax(25);

        return connections;
    }

    /**
     * Organizes and groups the sessions by last accessed time. Session closed
     * to be invalidated should be filtered first.
     *
     * @see LoggedInUsersBean#initLoggedInUsers()
     * @see LoggedInUsersBean#getConnections()
     * @return the data model of the Line Chart
     */
    private LineChartModel initConnectionModel() {

        LineChartModel model = new LineChartModel();

        long now = System.currentTimeMillis();

        this.getLoggedInUsers().stream().map(
                LoggedInUser::getType)
                .distinct()
                .forEach((type) -> {
                    ChartSeries serie = new ChartSeries();
                    serie.setLabel(type);

                    for (int i = this.getStripes(); i > 0; i--) {

                        //low bound limit of the stripe
                        long tl = now - (getDuration() * i);
                        //high bound limit of the stripe
                        long tr = now - (getDuration() * (i - 1));

                        long count = this.getLoggedInUsers().stream()
                                .filter(u -> u.getType().equals(type))
                                .filter(u -> {
                                    boolean validity = false;
                                    try {
                                        validity = u.getHttpSession().
                                                getLastAccessedTime() >= tl
                                                & u.getHttpSession().
                                                        getLastAccessedTime()
                                                < tr;
                                    } catch (IllegalStateException ex) {
                                        //ignores invalidated session
                                    }
                                    return validity;
                                })
                                .count();

                        //most recent sessions will be at left of the chart
                        serie.set((this.getD() * i) - 1, count);
                    }

                    model.addSeries(serie);

                });

        return model;
    }

}
