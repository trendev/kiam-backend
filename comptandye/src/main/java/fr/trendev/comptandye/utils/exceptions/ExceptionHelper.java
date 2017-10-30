/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import java.text.MessageFormat;

/**
 *
 * @author jsie
 */
public class ExceptionHelper {

    @SuppressWarnings("empty-statement")
    private static Throwable findRootCauseException(Throwable e) {
        Throwable t = e;
        for (; t.getCause() != null; t = t.getCause());
        return t;
    }

    public final static String handleException(Throwable ex, String message) {

        Throwable t = findRootCauseException(ex);

        String errmsg = MessageFormat.format(
                "{0}: {1} ; {2}",
                new Object[]{message, t.getClass().toString(), t.getMessage()});

        return errmsg;

    }
}
