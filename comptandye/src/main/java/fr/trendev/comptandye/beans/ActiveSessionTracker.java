/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class ActiveSessionTracker {

    private final Map<String, List<HttpSession>> map;

    public ActiveSessionTracker() {
        map = Collections.synchronizedMap(new TreeMap<>());
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

}
