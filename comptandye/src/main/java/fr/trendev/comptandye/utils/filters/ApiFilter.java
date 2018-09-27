/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.servlet.Filter;

/**
 *
 * @author jsie
 */
public abstract class ApiFilter implements Filter {

    private final Set<String> allowedOrigins;

    /**
     * Sets the Allowed Origins. Localhost / jsie-macbook URLs should be removed
     * for Production (security purpose)
     */
    public ApiFilter() {
        this.allowedOrigins = new HashSet<String>();
        this.allowedOrigins.addAll(
                Arrays.asList("http://localhost:4200",
                        "http://localhost:8080",
                        "http://jsie-macbook:4200",
                        "https://localhost:4200",
                        "http://localhost:9876"));
    }

    protected boolean isOriginAllowed(String origin) {
        return Objects.nonNull(origin)
                && !origin.isEmpty()
                && this.allowedOrigins.contains(origin);
    }

}
