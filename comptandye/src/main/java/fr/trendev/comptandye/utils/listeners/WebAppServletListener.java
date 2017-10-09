/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.listeners;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author jsie
 */
public class WebAppServletListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(
            WebAppServletListener.class.getName());

    //TODO : send email to admin for info
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.log(Level.INFO,
                "====>>>> [{1}] is starting with contextpath [{2}] on {0}",
                new Object[]{
                    new Date(), sce.getServletContext().getServletContextName(),
                    sce.getServletContext().getContextPath()});
    }

    //TODO : send email to admin for info
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.log(Level.INFO, "====>>>> [{1}] is stopping on {0}",
                new Object[]{
                    new Date(), sce.getServletContext().getServletContextName()
                });
    }

}
