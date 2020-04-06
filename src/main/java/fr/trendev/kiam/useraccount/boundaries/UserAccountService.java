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
import fr.trendev.comptandye.useraccount.controllers.EmailValidator;
import fr.trendev.comptandye.useraccount.controllers.UserAccountFacade;
import fr.trendev.comptandye.useraccount.entities.UserAccount;
import fr.trendev.comptandye.usergroup.controllers.UserGroupFacade;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    EmailValidator emailValidator;
    
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
    public Response createProfessional(JsonObject payload) {
        try {

            // collect data from the payload
            String email = payload.getString("email");
            
            if (!emailValidator.valid(email) || email.length() > 100) {
                throw new IllegalArgumentException("[" + email + "] is not a valid email");
            }

            // instantiate a new Professional entity
            Professional pro = new Professional();
            pro.getAddress().setId(UUIDGenerator.generateID());
            pro.getCustomerDetails().setId(UUIDGenerator.generateID());
            pro.getSocialNetworkAccounts().setId(UUIDGenerator.generateID());
            pro.setEmail(email);
            //Generates a new UUID
            pro.setUuid(UUIDGenerator.generate("PRO-", true));

            // auto-generate and hash a password
            String pwd = passwordManager.autoGenerate();
            pro.setPassword(passwordManager.hashPassword(pwd));

            // Puts the Professional in the Professional's UserGroup and links the Professional with the UserGroup
            String groupName = Professional.class.getSimpleName();
            UserGroup proGroup = userGroupFacade.find(groupName);
            if (!proGroup.getUserAccounts().add(pro) || !pro.getUserGroups().add(proGroup)) {
                String errMsg = "User " + email + " cannot be added in the group " + groupName;
                LOG.log(Level.SEVERE, errMsg);
                throw new WebApplicationException(errMsg);
            }
            // unblock the user
            pro.setBlocked(false);

            // persist the professional
            professionalFacade.create(pro);

            /**
             * The response is used and its content is overridden in the
             * UserAccountFilter. JAX-RS will send a valid response if the
             * professional has been persisted. This approach fixes an issue
             * where a email will be send with the credentials even if the
             * professional cannot be persisted (MySql constraints violation for
             * example).
             */
            return Response.ok(Json.createObjectBuilder()
                    .add("email", email)
                    .add("password", pwd)
                    .build()).build();
        } catch (Exception ex) {
            throw new WebApplicationException("Error processing Professional creation", ex);
        }
    }
    
}
