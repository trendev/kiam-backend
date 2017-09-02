/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
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
@Path("Professional")
public class ProfessionalService extends AbstractCommonService<Professional, String> {

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    private static final Logger LOG = Logger.getLogger(
            ProfessionalService.class.
                    getName());

    public ProfessionalService() {
        super(Professional.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Professional list");
        return super.findAll(professionalFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(professionalFacade);
    }

    @Path("{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("email") String email,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get Professional : {0}", email);
        return super.find(professionalFacade, email, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Professional entity) {
        LOG.log(Level.INFO, "Creating Professional {0}", entity.getEmail());

        return super.post(entity, professionalFacade,
                e -> {
            //generates an UUID if no one is provided
            if (e.getUuid() == null || e.getUuid().isEmpty()) {
                String uuid = UUIDGenerator.generate("PRO_", true);
                LOG.log(Level.WARNING,
                        "No UUID provided for new Professional {0}. Generated UUID = {1}",
                        new Object[]{e.getEmail(), uuid});
                e.setUuid(uuid);
            }

            //encrypts the provided password
            String encrypted_pwd = PasswordGenerator.encrypt_SHA256(e.
                    getPassword());
            e.setPassword(encrypted_pwd);

            //adds the new professional to the group and the group to the new pro
            UserGroup proGroup = userGroupFacade.find("Professional");
            proGroup.getUserAccounts().add(e);
            e.getUserGroups().add(proGroup);
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Professional entity) {
        LOG.log(Level.INFO, "Updating Professional {0}", entity.getEmail());
        return super.put(entity, professionalFacade, entity.getEmail(), e ->
        {
            //encrypts the provided password
            if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
                String encrypted_pwd = PasswordGenerator.encrypt_SHA256(
                        entity.getPassword());
                e.setPassword(encrypted_pwd);
            }

            e.setUsername(entity.getUsername());
            e.setUuid(entity.getUuid());
            e.setRegistrationDate(entity.getRegistrationDate());

            e.setCustomerDetails(entity.getCustomerDetails());
            e.setAddress(entity.getAddress());
            e.setSocialNetworkAccounts(entity.getSocialNetworkAccounts());

            e.setWebsite(entity.getWebsite());
            e.setCompanyID(entity.getCompanyID());
            e.setVATcode(entity.getVATcode());
            e.setCreationDate(entity.getCreationDate());
            e.setBusinesses(entity.getBusinesses());
            e.setCategories(entity.getCategories());
            e.setPaymentModes(entity.getPaymentModes());

        });
    }

    @Path("{email}")
    @DELETE
    public Response delete(@PathParam("email") String email) {
        LOG.log(Level.INFO, "Deleting Professional {0}", email);
        return super.delete(professionalFacade, email, e -> {

            e.getUserGroups().forEach(grp -> {
                grp.getUserAccounts().remove(e);
                LOG.log(Level.INFO,
                        "Professional {0} removed from UserGroup {1}",
                        new Object[]{email, grp.getName()});
            });

            e.getIndividuals().forEach(i -> {
                i.getProfessionals().remove(e);
                LOG.log(Level.INFO,
                        "Professional {0} and Individual {1} association deleted",
                        new Object[]{email, i.getEmail()});
            });
        });
    }

    @Path("{email}/userGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get userGroups of Professional : {0}", email);
        return super.provideRelation(professionalFacade,
                email,
                Professional::getUserGroups);
    }

    @Path("{email}/insertToUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertTo(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Inserting Professional {0} into UserGroup {1}",
                new Object[]{email, name});

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.INSERT,
                professionalFacade, email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().add(a) & a.getUserAccounts().add(e));
    }

    @Path("{email}/removeFromUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFrom(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Removing Professional {0} from UserGroup {1}",
                new Object[]{email, name});

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                professionalFacade, email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().remove(a) & a.getUserAccounts().remove(e));
    }

    @Path("{email}/bills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBills(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get bills of Professional : {0}", email);
        return super.provideRelation(professionalFacade,
                email,
                Professional::getBills);
    }

    @Path("{email}/clients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get clients of Professional : {0}", email);
        return super.provideRelation(professionalFacade,
                email,
                Professional::getClients);
    }
}
