/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.useraccount.boundaries;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.security.controllers.PasswordManager;
import fr.trendev.comptandye.useraccount.controllers.UserAccountFacade;
import fr.trendev.comptandye.useraccount.entities.UserAccount;
import fr.trendev.comptandye.usergroup.controllers.UserGroupFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.minidev.json.JSONArray;

/**
 *
 * @author jsie
 */
@Stateless
@Path("user-account")
@PermitAll
public class UserAccountService extends AbstractCommonService<UserAccount, String> {

    @Inject
    PasswordManager passwordManager;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    UserAccountFacade userAccountFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    private static final Logger LOG = Logger.getLogger(UserAccountService.class.getName());

    public UserAccountService() {
        super(UserAccount.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<UserAccount, String> getFacade() {
        return userAccountFacade;
    }

    @Path("create-professional")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProfessional(Professional payload) {
        try {
            Professional pro = new Professional();

            pro.setEmail(payload.getEmail());
            pro.setBusinesses(payload.getBusinesses());
            pro.setPaymentModes(payload.getPaymentModes());

            String pwd = passwordManager.autoGenerate();
            pro.setPassword(passwordManager.hashPassword(pwd));
            
            professionalFacade.create(pro);

            return Response.ok(Json.createObjectBuilder()
                    .add("email", payload.getEmail())
                    .add("password", pwd)
                    .build()).build();
        } catch (Exception ex) {
            throw new WebApplicationException("Error processing Professional creation", ex);
        }
    }

}
