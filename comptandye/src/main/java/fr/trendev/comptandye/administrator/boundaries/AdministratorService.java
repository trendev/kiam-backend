/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.administrator.boundaries;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import fr.trendev.comptandye.security.controllers.PasswordManager;
import fr.trendev.comptandye.administrator.controllers.AdministratorFacade;
import fr.trendev.comptandye.usergroup.controllers.UserGroupFacade;
import fr.trendev.comptandye.common.boundaries.AssociationManagementEnum;
import fr.trendev.comptandye.utils.UUIDGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Administrator")
@RolesAllowed({"Administrator"})
public class AdministratorService extends AbstractCommonService<Administrator, String> {

    @Inject
    PasswordManager passwordManager;

    @Inject
    AdministratorFacade administratorFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    private final Logger LOG = Logger.getLogger(
            AdministratorService.class.
                    getName());

    public AdministratorService() {
        super(Administrator.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Administrator, String> getFacade() {
        return administratorFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
    }

    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @Path("{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("email") String email,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get Administrator : {0}", email);
        return super.find(email, refresh);
    }

    @Path("{email}/userGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getUserGroups(@Suspended final AsyncResponse ar,
            @PathParam("email") String email) {
        super.provideRelation(ar, email, Administrator::getUserGroups,
                UserGroup.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Administrator entity) {
        LOG.log(Level.INFO, "Creating Administrator {0}", entity.getEmail());

        return super.post(entity, e -> {
            //generates an UUID if no one is provided
            if (e.getUuid() == null || e.getUuid().isEmpty()) {
                String uuid = UUIDGenerator.generate("ADMIN-", true);
                LOG.log(Level.WARNING,
                        "No UUID provided for new Administrator {0}. Generated UUID = {1}",
                        new Object[]{e.getEmail(), uuid});
                e.setUuid(uuid);
            }

            //hashes the provided password
            String hpwd = passwordManager.hashPassword(e.
                    getPassword());
            e.setPassword(hpwd);

            //adds the new administrator to the group and the group to the new admin
            UserGroup adminGroup = userGroupFacade.find("Administrator");
            adminGroup.getUserAccounts().add(e);
            e.getUserGroups().add(adminGroup);
            e.setBlocked(false);
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Administrator entity) {
        LOG.log(Level.INFO, "Updating Administrator {0}", entity.getEmail());
        return super.put(entity, entity.getEmail(), e ->
        {
            //hashes the provided password
            if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
                String hpwd = passwordManager.hashPassword(
                        entity.getPassword());
                e.setPassword(hpwd);
            }

            e.setUsername(entity.getUsername());
            e.setRegistrationDate(entity.getRegistrationDate());
        });
    }

    @Path("{email}")
    @DELETE
    public Response delete(@PathParam("email") String email) {
        LOG.log(Level.INFO, "Deleting Administrator {0}", email);
        return super.delete(email, e -> {
            e.getUserGroups().forEach(grp -> {
                grp.getUserAccounts().remove(e);
                LOG.log(Level.INFO,
                        "Administrator {0} removed from UserGroup {1}",
                        new Object[]{email, grp.getName()});
            });
        });
    }

    @Path("{email}/insertToUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToUserGroup(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Inserting Administrator {0} into UserGroup {1}",
                new Object[]{email, name});

        if (name != null && name.equals("Administrator")) {
            throw new WebApplicationException("You cannot add the user ["
                    + email
                    + "] in the Administrator's user group using this service !!!");
        }

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.INSERT,
                email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().add(a) & a.getUserAccounts().add(e));
    }

    @Path("{email}/removeFromUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromUserGroup(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Removing Administrator {0} from UserGroup {1}",
                new Object[]{email, name});

        if (name != null && name.equals("Administrator")) {
            throw new WebApplicationException("You cannot remove the user ["
                    + email
                    + "] from the Administrator's user group using this service !!!");
        }

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().remove(a) & a.getUserAccounts().remove(e));
    }

}
