/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.individual.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.common.boundaries.AssociationManagementEnum;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.individual.controllers.IndividualFacade;
import fr.trendev.kiam.individual.entities.Individual;
import fr.trendev.kiam.individualbill.entities.IndividualBill;
import fr.trendev.kiam.professional.controllers.ProfessionalFacade;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.security.controllers.PasswordManager;
import fr.trendev.kiam.usergroup.controllers.UserGroupFacade;
import fr.trendev.kiam.usergroup.entities.UserGroup;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Individual")
@RolesAllowed({"Administrator"})
public class IndividualService extends AbstractCommonService<Individual, String> {

    @Inject
    PasswordManager passwordManager;

    @Inject
    IndividualFacade individualFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    private final Logger LOG = Logger.getLogger(IndividualService.class.
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
        LOG.log(Level.INFO, "REST request to get Individual : {0}", email);
        return super.find(email, refresh);
    }

    @RolesAllowed({"Individual"})
    @Path("profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response profile(@Context SecurityContext sec,
            @QueryParam("refresh") boolean refresh) {
        String indEmail = this.getIndEmail(sec, null);
        LOG.log(Level.INFO, "REST request to get the Individual profile of {0}",
                indEmail);
        return super.find(indEmail, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Individual entity) {
        LOG.log(Level.INFO, "Creating Individual {0}", entity.getEmail());

        return super.post(entity,
                e -> {

                    //Reset the id (if provided)
                    entity.getCustomerDetails().setId(UUIDGenerator.generateID());
                    entity.getAddress().setId(UUIDGenerator.generateID());
                    entity.getSocialNetworkAccounts().setId(UUIDGenerator.generateID());

                    e.setUuid(UUIDGenerator.generate("IND-", true));

                    //hashes the provided password
                    String hwpd = passwordManager.hashPassword(e.
                            getPassword());
                    e.setPassword(hwpd);

                    //adds the new individual to the group and the group to the new individual
                    UserGroup indGroup = userGroupFacade.find("Individual");
                    indGroup.getUserAccounts().add(e);
                    e.getUserGroups().add(indGroup);
                    e.setBlocked(false);
                });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //Should only be performed by an Administrator
    public Response put(Individual entity) {
        LOG.log(Level.INFO, "Updating Individual {0}", entity.getEmail());
        return super.put(entity, entity.getEmail(), e
                -> {
            /**
             * hashes the provided password
             *
             */
            if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
                String hpwd = passwordManager.hashPassword(
                        entity.getPassword());
                e.setPassword(hpwd);
            }

            e.setUsername(entity.getUsername());
            e.setRegistrationDate(entity.getRegistrationDate());

            // override CustomerDetails, Address and SocialNetworkAccounts id (security reason)
            entity.getCustomerDetails().setId(UUIDGenerator.generateID());
            entity.getAddress().setId(UUIDGenerator.generateID());
            entity.getSocialNetworkAccounts().setId(UUIDGenerator.generateID());

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
    public void getUserGroups(@Suspended final AsyncResponse ar,
            @PathParam("email") String email) {
        super.provideRelation(ar, email, Individual::getUserGroups,
                UserGroup.class);
    }

    @Path("{email}/insertToUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToUserGroup(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Inserting Individual {0} into UserGroup {1}",
                new Object[]{email, name});

        if (name != null && name.equals("Individual")) {
            throw new WebApplicationException("You cannot add the user ["
                    + email
                    + "] in the Individual's user group using this service !!!");
        }

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.INSERT,
                email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a)
                -> e.getUserGroups().add(a) & a.getUserAccounts().add(e));
    }

    @Path("{email}/removeFromUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromUserGroup(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Removing Individual {0} from UserGroup {1}",
                new Object[]{email, name});

        if (name != null && name.equals("Individual")) {
            throw new WebApplicationException("You cannot remove the user ["
                    + email
                    + "] from the Individual's user group using this service !!!");
        }

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a)
                -> e.getUserGroups().remove(a) & a.getUserAccounts().remove(e));
    }

    @Path("{email}/professionals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getProfessionals(@Suspended final AsyncResponse ar, @PathParam(
            "email") String email) {
        super.provideRelation(ar, email,
                Individual::getProfessionals, Professional.class);
    }

    @Path("{email}/individualBills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getIndividualBills(@Suspended final AsyncResponse ar,
            @PathParam("email") String email) {
        super.provideRelation(ar, email,
                Individual::getIndividualBills, IndividualBill.class);
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
                (i, p)
                -> i.getProfessionals().add(p) & p.getIndividuals().add(i));
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
                (i, p)
                -> i.getProfessionals().remove(p) & p.getIndividuals().remove(i));
    }
}
