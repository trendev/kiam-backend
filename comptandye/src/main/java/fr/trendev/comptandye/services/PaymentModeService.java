/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.sessions.PaymentModeFacade;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 *
 * @author jsie
 */
@Stateless
@Path("PaymentMode")
//@Api(value = "PaymentMode", description = "Payment modes")
public class PaymentModeService {

    @Inject
    PaymentModeFacade facade;

    @Inject
    Logger logger;
}
