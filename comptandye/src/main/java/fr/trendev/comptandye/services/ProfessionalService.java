/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Bill;
import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.IndividualFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.AssociationManagementEnum;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UUIDGenerator;
import java.util.Collection;
import java.util.function.Function;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Professional")
@RolesAllowed({"Administrator", "Professional"})
public class ProfessionalService extends AbstractCommonService<Professional, String> {

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    @Inject
    IndividualFacade individualFacade;

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

    @Override
    protected AbstractFacade<Professional, String> getFacade() {
        return professionalFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Professional list");
        return super.findAll();
    }

    @RolesAllowed({"Administrator"})
    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @RolesAllowed({"Administrator"})
    @Path("{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("email") String email,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get Professional : {0}", email);
        return super.find(email, refresh);
    }

    @RolesAllowed({"Professional"})
    @Path("profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response profile(@Context SecurityContext sec,
            @QueryParam("refresh") boolean refresh) {
        String proEmail = this.getProEmail(sec, null);
        LOG.log(Level.INFO,
                "REST request to get the Professional profile of {0}", proEmail);
        return super.find(proEmail, refresh);
    }

    /**
     * Prepares and Persists a Professional. This service is only accessible by
     * Administrator's group member.
     *
     * UUID is auto-generated (UUID provided are ignored), Password is
     * encrypted. All relationships are initialized and empty - except
     * userGroups if inactivate is not specified or false.
     *
     * @param entity the Professional to persist
     * @param inactivate grant the Professional or not. If true, the persisted
     * professional won't be added in the Professional's group. This QueryParam
     * is optional.
     * @return
     */
    @RolesAllowed({"Administrator"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Professional entity,
            @QueryParam("inactivate") boolean inactivate) {
        LOG.log(Level.INFO, "Creating Professional {0}", entity.getEmail());

        return super.post(entity, e -> {

            e.setUuid(UUIDGenerator.generate("PRO-", true));

            //encrypts the provided password
            String encrypted_pwd = PasswordGenerator.encrypt_SHA256(e.
                    getPassword());
            e.setPassword(encrypted_pwd);

            if (!inactivate) {
                this.grantAsProfessional(e);
                e.setBlocked(false);
            }
        });
    }

    private boolean grantAsProfessional(Professional pro) {
        UserGroup proGroup = userGroupFacade.find("Professional");
        return proGroup.getUserAccounts().add(pro) & pro.getUserGroups().add(
                proGroup);
    }

    /**
     * Prepares and Updates a Professional. Email, UUID, blocked and
     * registrationDate fields will be ignored and should not be changed by the
     * way...
     *
     * @param sec the security context (use if a Professional has initiated the
     * request)
     * @param entity the entity to update
     * @return the updated entity
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Professional entity) {
        String proEmail = this.getProEmail(sec, entity.getEmail());

        LOG.log(Level.INFO, "Updating Professional {0}", proEmail);

        return super.put(entity, proEmail, e ->
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

            //Reset the id (if provided) and create new object
            entity.getCustomerDetails().setId(null);
            entity.getAddress().setId(null);
            entity.getSocialNetworkAccounts().setId(null);

            e.setCustomerDetails(entity.getCustomerDetails());
            e.setAddress(entity.getAddress());
            e.setSocialNetworkAccounts(entity.getSocialNetworkAccounts());

            e.setWebsite(entity.getWebsite());
            e.setCompanyID(entity.getCompanyID());
            e.setCompanyName(entity.getCompanyName());
            e.setVATcode(entity.getVATcode());
            e.setCreationDate(entity.getCreationDate());
            e.setBusinesses(entity.getBusinesses());
            e.setCategories(entity.getCategories());
            e.setPaymentModes(entity.getPaymentModes());

        });
    }

    @RolesAllowed({"Administrator"})
    @Path("{email}")
    @DELETE
    public Response delete(@PathParam("email") String email) {
        LOG.log(Level.INFO, "Deleting Professional {0}", email);

        return super.delete(email, e -> {

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

    @RolesAllowed({"Administrator"})
    @Path("{email}/userGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get userGroups of Professional : {0}", email);
        return super.provideRelation(email,
                Professional::getUserGroups, UserGroup.class);
    }

    @RolesAllowed({"Administrator"})
    @Path("{email}/insertToUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToUserGroup(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Inserting Professional {0} into UserGroup {1}",
                new Object[]{email, name});

        if (name != null && name.equals("Professional")) {
            throw new WebApplicationException("You cannot add the user ["
                    + email
                    + "] in the Professional's user group using this service !!!");
        }

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.INSERT,
                email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().add(a) & a.getUserAccounts().add(e));
    }

    @RolesAllowed({"Administrator"})
    @Path("{email}/removeFromUserGroup/{name}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromUserGroup(@PathParam("email") String email,
            @PathParam("name") String name) {
        LOG.log(Level.INFO, "Removing Professional {0} from UserGroup {1}",
                new Object[]{email, name});

        if (name != null && name.equals("Professional")) {
            throw new WebApplicationException("You cannot remove the user ["
                    + email
                    + "] from the Professional's user group using this service !!!");
        }

        return super.<UserGroup, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                email,
                userGroupFacade,
                name, UserGroup.class,
                (e, a) ->
                e.getUserGroups().remove(a) & a.getUserAccounts().remove(e));
    }

    /**
     * Wraps the provideRelation() call catching the professional's email from
     * the security context (Request from Professional) or from the parameter
     * email.
     *
     * @param <R> the type of the element contained in the relation
     * @param sec the security context
     * @param email the professional's email, usually provided by an
     * Administrator
     * @param getter the getter function used to get the elements
     * @param elementClass The class of the element contained in the relation
     * @return
     */
    private <R> Response provideRelation(SecurityContext sec, String email,
            Function<Professional, Collection<R>> getter,
            Class<R> elementClass) {
        String proEmail = this.getProEmail(sec, email);

        if (!proEmail.equals(email)) {
            LOG.log(Level.WARNING,
                    "Professional [{0}] has provided an incorrect email [{1}] which will be ignored...",
                    new Object[]{proEmail, email});
        }

        return super.provideRelation(proEmail, getter, elementClass);
    }

    @Path("{email}/bills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBills(@Context SecurityContext sec,
            @PathParam("email") String email) {
        return this.provideRelation(sec, email, Professional::getBills,
                Bill.class);
    }

    @Path("{email}/clients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients(@Context SecurityContext sec,
            @PathParam("email") String email) {
        return this.provideRelation(sec, email, Professional::getClients,
                Client.class);
    }

    @Path("{email}/offerings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferings(@Context SecurityContext sec,
            @PathParam("email") String email) {
        return this.provideRelation(sec, email, Professional::getOfferings,
                Offering.class);
    }

    @Path("{email}/categories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories(@Context SecurityContext sec,
            @PathParam("email") String email) {
        return this.provideRelation(sec, email, Professional::getCategories,
                Category.class);
    }

    @Path("{email}/collectiveGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCollectiveGroups(@Context SecurityContext sec,
            @PathParam("email") String email) {
        return this.provideRelation(sec, email,
                Professional::getCollectiveGroups, CollectiveGroup.class);
    }

    @Path("{email}/expenses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExpenses(@Context SecurityContext sec,
            @PathParam("email") String email) {
        return this.provideRelation(sec, email,
                Professional::getExpenses, Expense.class);
    }

    @Path("{email}/individuals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIndividuals(@Context SecurityContext sec,
            @PathParam("email") String email) {
        return this.provideRelation(sec, email,
                Professional::getIndividuals, Individual.class);
    }

    @RolesAllowed({"Administrator"})
    @Path("{proEmail}/buildBusinessRelationship/{indEmail}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response buildBusinessRelationship(
            @PathParam("proEmail") String proEmail,
            @PathParam("indEmail") String indEmail) {
        LOG.log(Level.INFO,
                "Build business relationship between Professional {0} and Individual {1}",
                new Object[]{proEmail, indEmail});

        return super.<Individual, String>manageAssociation(
                AssociationManagementEnum.INSERT,
                proEmail,
                individualFacade,
                indEmail, Individual.class,
                (p, i) ->
                p.getIndividuals().add(i) & i.getProfessionals().add(p));
    }

    @RolesAllowed({"Administrator"})
    @Path("{proEmail}/endBusinessRelationship/{indEmail}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response endBusinessRelationship(
            @PathParam("proEmail") String proEmail,
            @PathParam("indEmail") String indEmail) {
        LOG.log(Level.INFO,
                "End business relationship between Professional {0} and Individual {1}",
                new Object[]{proEmail, indEmail});

        return super.<Individual, String>manageAssociation(
                AssociationManagementEnum.REMOVE,
                proEmail,
                individualFacade,
                indEmail, Individual.class,
                (p, i) ->
                p.getIndividuals().remove(i) & i.getProfessionals().remove(p));
    }
}
