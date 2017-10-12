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

    /**
     * The datamodel, a flat snapshot of the tracker
     */
    private List<LoggedInUser> loggedInUsers;

    private PieChartModel userTypes;

    private LineChartModel connections;

    private static final Logger LOG = Logger.getLogger(LoggedInUsersBean.class.
            getName());

    /**
     * Refresh delay
     */
    private int refresh = 5;

    /**
     * The session timeout in seconds
     */
    private int session_timeout;

    /**
     * The duration of a track record, usually 1 minute
     */
    private int d;

    /**
     * The duration of a track record in milliseconds
     */
    private long duration;

    /**
     * The number of track records / stripes
     */
    private int stripes;

    /**
     * Returns the refresh delay
     *
     * @return the refresh value
     */
    public int getRefresh() {
        return this.refresh;
    }

    /**
     * Computes and sets the timeout, durations and stripes and initializes
     * (loads) the data model.
     */
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

    /**
     * Loads/Refreshes the data model. Filters and ignores the sessions which
     * will be invalidated during the refreshed JSF session.
     */
    public void initLoggedInUsers() {
        long now = System.currentTimeMillis();

        //sessions older than overdue will be ignored
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
            //uses the previous version of the data model instead 
            //but cleans the potential invalidated session
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

    /**
     * Gets the emails of the logged-in users
     *
     * @return the emails of the users
     */
    public List<String> getUserAccounts() {
        List<String> users = tracker.getLoggedUsers();
        users.sort(String::compareTo);
        return users;
    }

    /**
     * Returns the sessions of the specified user.
     *
     * @param email the user's email
     * @return the http session list
     */
    private List<HttpSession> getSessions(String email) {
        List<HttpSession> sessions = tracker.getSession(email);
        sessions.sort((s1, s2) -> s1.getId().compareTo(s2.getId()));
        return sessions;
    }

    /**
     * Removes an active session linked for a specific user and update the data
     * model used in the form.
     *
     * @param email the user's email
     * @param session the http session to remove from the tracker and invalidate
     * @return the result of the remove from the tracker
     */
    public boolean remove(String email, HttpSession session) {
        boolean result = tracker.remove(email, session);
        this.initLoggedInUsers();
        return result;
    }

    private String getTypeOfUser(String email) {
        return userAccountFacade.getUserAccountType(email);
    }

    /**
     * Returns the datamodel (a flat snapshot of the tracker)
     *
     * @return the datamodel, the list of the logged-in user
     */
    public List<LoggedInUser> getLoggedInUsers() {
        return loggedInUsers;
    }

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

                    for (int i = stripes; i > 0; i--) {

                        //low bound limit of the stripe
                        long tl = now - (duration * i);
                        //high bound limit of the stripe
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

                        //most recent sessions will be at left of the chart
                        serie.set((d * i) - 1, count);
                    }

                    model.addSeries(serie);

                });

        return model;
    }

    /**
     * Entry which will represent a logged-in user. Used to flap the tracker
     * contain
     */
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
