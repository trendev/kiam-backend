/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.sessions.UserAccountFacade;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
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
@RequestScoped
public class LoggedInUsersBean implements Serializable {

    @Inject
    private UserAccountFacade userAccountFacade;

    @Inject
    private ActiveSessionTracker tracker;

    private List<LoggedInUser> loggedInUsers;

    private PieChartModel userTypes;

    private LineChartModel connections;

    private static final Logger LOG = Logger.getLogger(LoggedInUsersBean.class.
            getName());

    @PostConstruct
    public void init() {
        this.loggedInUsers = new LinkedList<>();
        this.getUserAccounts().forEach(u -> this.getSessions(u).forEach(
                s ->
                this.loggedInUsers.add(
                        new LoggedInUser(u, this.getTypeOfUser(u), s))
        ));
    }

    private List<String> getUserAccounts() {
        List<String> users = tracker.getLoggedUsers();
        users.sort(String::compareTo);
        return users;
    }

    private List<HttpSession> getSessions(String email) {
        List<HttpSession> sessions = tracker.getSession(email);
        sessions.sort((s1, s2) -> s1.getId().compareTo(s2.getId()));
        return sessions;
    }

    public boolean remove(String email, HttpSession session) {
        return tracker.remove(email, session);
    }

    private String getTypeOfUser(String email) {
        return userAccountFacade.getUserAccountType(email);
    }

    public List<LoggedInUser> getLoggedInUsers() {
        return loggedInUsers;
    }

    public PieChartModel getUserTypes() {

        Map<String, Number> map = new TreeMap<>();
        this.getLoggedInUsers().forEach(u -> {
            map.computeIfAbsent(u.getType(), t -> 0);
            map.put(u.getType(), map.get(u.getType()).intValue() + 1);
        });

        userTypes = new PieChartModel(map);
        userTypes.setTitle("Distribution of users");
        userTypes.setLegendPosition("nw");
        userTypes.setShowDataLabels(true);
        return userTypes;
    }

    public LineChartModel getConnections() {
        connections = this.initConnectionModel();
        connections.setTitle("Connections");
        connections.setLegendPosition("e");
        connections.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        connections.getAxes().put(AxisType.X, new CategoryAxis("Time"));
        Axis yAxis = connections.getAxis(AxisType.Y);
        yAxis.setLabel("Number of Connections");
        yAxis.setMin(0);
        yAxis.setMax(25);

        return connections;
    }

    private LineChartModel initConnectionModel() {
        LineChartModel model = new LineChartModel();

        int timeout = 1800;//30 min = 1800s
        int stripe = 6;//cuts in 5 min stripe

        List<String> types = this.loggedInUsers.stream().map(
                LoggedInUser::getType).distinct().
                collect(Collectors.toList());

        types.forEach((type) -> {
            ChartSeries serie = new ChartSeries();
            serie.setLabel(type);

            Calendar cal = Calendar.getInstance();
            Random r = new Random();
            for (int i = 12; i > 0; i--) {
                cal.add(Calendar.MINUTE, -5 * i);
                serie.set("t(" + (-5 * i) + ")", r.nextInt(20));
            }

            model.addSeries(serie);

        });

        return model;
    }

    public static class LoggedInUser {

        private String email;
        private String type;
        private HttpSession httpSession;

        public LoggedInUser() {
        }

        public LoggedInUser(String email, String type, HttpSession httpSession) {
            this.email = email;
            this.type = type;
            this.httpSession = httpSession;
        }

        public String getEmail() {
            return email;
        }

        public String getType() {
            return type;
        }

        public HttpSession getHttpSession() {
            return httpSession;
        }

    }
}
