/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.AdministratorFacade;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UUIDGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Administrator")
public class AdministratorService extends AbstractCommonService<Administrator, String> {

    @Inject
    AdministratorFacade administratorFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    private static final Logger LOG = Logger.getLogger(
            AdministratorService.class.
                    getName());

    public AdministratorService() {
        super(Administrator.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Administrator list");
        return super.findAll(administratorFacade, facade -> facade.findAll());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(administratorFacade);
    }

    @Path("{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("email") String email,
            @QueryParam("refresh") Boolean refresh) {
        LOG.log(Level.INFO, "REST request to get Administrator : {0}", email);
        return super.find(administratorFacade, email, refresh);
    }

    @Path("{email}/userGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get userGroups of Administrator : {0}", email);
        return super.provideRelation(administratorFacade,
                email,
                Administrator::getUserGroups);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Administrator entity) {
        LOG.log(Level.INFO, "Creating Administrator {0}", entity.getEmail());

        return super.post(entity, administratorFacade,
                e -> {
            //generates an UUID if no one is provided
            if (e.getUuid() == null || e.getUuid().isEmpty()) {
                String uuid = UUIDGenerator.generate("ADMIN_", true);
                LOG.log(Level.WARNING,
                        "No UUID provided for new Administrator {0}. Generated UUID = {1}",
                        new Object[]{e.getEmail(), uuid});
                e.setUuid(uuid);
            }

            //encrypts the provided password
            String encrypted_pwd = PasswordGenerator.encrypt_SHA256(e.
                    getPassword());
            e.setPassword(encrypted_pwd);

            //adds the new administrator to the group and the group to the new admin
            UserGroup adminGroup = userGroupFacade.find("Administrator");
            adminGroup.getUserAccounts().add(e);
            e.getUserGroups().add(adminGroup);
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Administrator entity) {
        LOG.log(Level.INFO, "Updating Administrator {0}", entity.getEmail());
        return super.put(entity, administratorFacade, entity.getEmail(), e ->
        {
            //encrypts the provided password
            String encrypted_pwd = PasswordGenerator.encrypt_SHA256(
                    entity.getPassword());
            e.setPassword(encrypted_pwd);

            e.setUsername(entity.getUsername());
            e.setUuid(entity.getUuid());
            e.setRegistrationDate(entity.getRegistrationDate());
        });
    }

    @Path("{email}/insertTo/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertTo(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Inserting Administrator {0} into UserGroup {1}",
                new Object[]{email, name});

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.INSERT,
                administratorFacade, email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().add(a) & a.getUserAccounts().add(e));
    }

    @Path("{email}")
    @DELETE
    public Response delete(@PathParam("email") String email) {
        LOG.log(Level.INFO, "Deleting Administrator {0}", email);
        return super.delete(administratorFacade, email, e -> {
            e.getUserGroups().forEach(grp -> {
                grp.getUserAccounts().remove(e);
                LOG.log(Level.INFO,
                        "Administrator {0} removed from UserGroup {1}",
                        new Object[]{email, grp.getName()});
            });
        });
    }

    @Path("{email}/removeFrom/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFrom(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Removing Administrator {0} from UserGroup {1}",
                new Object[]{email, name});

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                administratorFacade, email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().remove(a) & a.getUserAccounts().remove(e));
    }

}
