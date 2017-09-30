/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.IndividualFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.AssociationManagementEnum;
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
@Path("Individual")
public class IndividualService extends AbstractCommonService<Individual, String> {

    @Inject
    IndividualFacade individualFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    private static final Logger LOG = Logger.getLogger(IndividualService.class.
            getName());

    public IndividualService() {
        super(Individual.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Individual, String> getFacade() {
        return individualFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Individual list");
        return super.findAll();
    }

    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
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
        LOG.log(Level.INFO, "REST request to get Individual : {0}", email);
        return super.find(email, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Individual entity) {
        LOG.log(Level.INFO, "Creating Individual {0}", entity.getEmail());

        return super.post(entity,
                e -> {
            //generates an UUID if no one is provided
            if (e.getUuid() == null || e.getUuid().isEmpty()) {
                String uuid = UUIDGenerator.generate("IND-", true);
                LOG.log(Level.WARNING,
                        "No UUID provided for new Individual {0}. Generated UUID = {1}",
                        new Object[]{e.getEmail(), uuid});
                e.setUuid(uuid);
            }

            //encrypts the provided password
            String encrypted_pwd = PasswordGenerator.encrypt_SHA256(e.
                    getPassword());
            e.setPassword(encrypted_pwd);

            //adds the new individual to the group and the group to the new individual
            UserGroup indGroup = userGroupFacade.find("Individual");
            indGroup.getUserAccounts().add(e);
            e.getUserGroups().add(indGroup);
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Individual entity) {
        LOG.log(Level.INFO, "Updating Individual {0}", entity.getEmail());
        return super.put(entity, entity.getEmail(), e ->
        {
            /**
             * encrypts the provided password
             *
             */
            if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
                String encrypted_pwd = PasswordGenerator.encrypt_SHA256(
                        entity.getPassword());
                e.setPassword(encrypted_pwd);
            }

            e.setUsername(entity.getUsername());
            /**
             * TODO : Should only be performed by an Administrator
             */
            e.setRegistrationDate(entity.getRegistrationDate());

            /**
             * Will automatically ignore the id of the provided object : avoid
             * to hack another object swapping the current saved (or not object)
             * by an existing one.
             */
            entity.getCustomerDetails().setId(null);
            entity.getAddress().setId(null);
            entity.getSocialNetworkAccounts().setId(null);

            e.setCustomerDetails(entity.getCustomerDetails());
            e.setAddress(entity.getAddress());
            e.setSocialNetworkAccounts(entity.getSocialNetworkAccounts());
        });
    }

    @Path("{email}")
    @DELETE
    public Response delete(@PathParam("email") String email) {
        LOG.log(Level.INFO, "Deleting Individual {0}", email);

        return super.delete(email, e -> {

            e.getUserGroups().forEach(grp -> {
                grp.getUserAccounts().remove(e);
                LOG.log(Level.INFO,
                        "Individual {0} removed from UserGroup {1}",
                        new Object[]{email, grp.getName()});
            });

            e.getProfessionals().forEach(p -> {
                p.getIndividuals().remove(e);
                LOG.log(Level.INFO,
                        "Individual {0} and Professional {1} association deleted",
                        new Object[]{email, p.getEmail()});
            });
        });
    }

    @Path("{email}/userGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get userGroups of Individual : {0}", email);
        return super.provideRelation(email, Individual::getUserGroups);
    }

    @Path("{email}/insertToUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToUserGroup(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Inserting Individual {0} into UserGroup {1}",
                new Object[]{email, name});

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
        LOG.log(Level.INFO, "Removing Individual {0} from UserGroup {1}",
                new Object[]{email, name});

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().remove(a) & a.getUserAccounts().remove(e));
    }

    @Path("{email}/professionals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfessionals(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get professionals of Individual : {0}",
                email);
        return super.provideRelation(email,
                Individual::getProfessionals);
    }

    @Path("{email}/individualBills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIndividualBills(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get individualBills of Individual : {0}",
                email);
        return super.provideRelation(email,
                Individual::getIndividualBills);
    }

    @Path("{indEmail}/buildBusinessRelationship/{proEmail}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response buildBusinessRelationship(
            @PathParam("indEmail") String indEmail,
            @PathParam("proEmail") String proEmail) {
        LOG.log(Level.INFO,
                "Build business relationship between Individual {0} and Professional {1}",
                new Object[]{indEmail, proEmail});

        return super.<Professional, String>manageAssociation(
                AssociationManagementEnum.INSERT,
                indEmail,
                professionalFacade,
                proEmail, Professional.class,
                (i, p) ->
                i.getProfessionals().add(p) & p.getIndividuals().add(i));
    }

    @Path("{indEmail}/endBusinessRelationship/{proEmail}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response endBusinessRelationship(
            @PathParam("indEmail") String indEmail,
            @PathParam("proEmail") String proEmail) {
        LOG.log(Level.INFO,
                "End business relationship between Individual {0} and Professional {1}",
                new Object[]{indEmail, proEmail});

        return super.<Professional, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                indEmail,
                professionalFacade,
                proEmail, Professional.class,
                (i, p) ->
                i.getProfessionals().remove(p) & p.getIndividuals().remove(i));
    }
}
