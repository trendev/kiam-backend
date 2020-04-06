/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.utils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jsie
 */
public class HttpRequestHelper {

    public static String getRealRemoteAddressIP(final HttpServletRequest req) {
        String realIP = req.getHeader("X-Real-IP");
        return (realIP == null || realIP.isEmpty()) ? req.getRemoteAddr() : realIP;
    }
}
