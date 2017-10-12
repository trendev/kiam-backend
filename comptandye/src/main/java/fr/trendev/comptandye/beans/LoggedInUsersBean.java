/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.sessions.UserAccountFacade;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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
@SessionScoped
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

    private int refresh = 5;

    private int session_timeout;
    private int d;
    private long duration;
    private int stripes;

    public int getRefresh() {
        return this.refresh;
    }

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().
                getSession(false);

        this.session_timeout = session.getMaxInactiveInterval();
        this.d = 1;
        this.duration = d * 60 * 1000l;
        this.stripes = (this.session_timeout / 60) / d;
        LOG.log(Level.INFO, "Session timeout = {0} seconds",
                this.session_timeout);

        this.initLoggedInUsers();
    }

    public void initLoggedInUsers() {

        long now = System.currentTimeMillis();
        //will limit the last accessed time to sessions which won't be invalidated after the refresh
        //sessions with a last accessed time in the last XX(refresh value) seconds will be ignored
        long overdue = now - (session_timeout * 1000l) + (this.refresh
                * 1000l);

        try {
            List<LoggedInUser> list = new LinkedList<>();
            this.getUserAccounts().forEach(u -> this.getSessions(u).forEach(
                    s -> {
                try {
                    //ignore session which will be invalidated after this refresh
                    if (s.getLastAccessedTime() > overdue) {
                        list.add(
                                new LoggedInUser(u, this.getTypeOfUser(u), s));
                    }
                } catch (IllegalStateException ex) {
                    //ignores invalidated sessions
                }
            }));
            //updates the logged in user list
            this.loggedInUsers = list;
        } catch (ConcurrentModificationException ex) {
            //cannot update the model because the tracker is locked
            //use the remaining datamodel instead but clean the potential invalidated session
            this.loggedInUsers = this.loggedInUsers.stream()
                    .filter(u -> {
                        boolean validity = false;
                        try {
                            validity = u.getHttpSession().getLastAccessedTime()
                                    > overdue;
                        } catch (IllegalStateException ise) {
                            //ignores invalidated session
                        }
                        return validity;
                    })
                    .collect(Collectors.toList());

        }
    }

    public List<String> getUserAccounts() {
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
        userTypes.setTitle("Distribution of Sessions by Type");
        userTypes.setLegendPosition("nw");
        userTypes.setShowDataLabels(true);
        return userTypes;
    }

    public LineChartModel getConnections() {
        connections = this.initConnectionModel();
        connections.setTitle("Active Sessions");
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

    private LineChartModel initConnectionModel() {

        LineChartModel model = new LineChartModel();

        long now = System.currentTimeMillis();

        this.getLoggedInUsers().stream().map(
                LoggedInUser::getType)
                .distinct()
                .forEach((type) -> {
                    ChartSeries serie = new ChartSeries();
                    serie.setLabel(type);

                    for (int i = stripes; i > 0; i--) {

                        long tl = now - (duration * i);
                        long tr = now - (duration * (i - 1));

                        long count = this.loggedInUsers.stream()
                                .filter(u -> u.getType().equals(type))
                                .filter(u -> {
                                    boolean validity = false;
                                    try {
                                        validity = u.httpSession.
                                                getLastAccessedTime() >= tl
                                                & u.httpSession.
                                                        getLastAccessedTime()
                                                < tr;
                                    } catch (IllegalStateException ex) {
                                        //ignores invalidated session
                                    }
                                    return validity;
                                })
                                .count();

                        serie.set((d * i) - 1, count);
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
