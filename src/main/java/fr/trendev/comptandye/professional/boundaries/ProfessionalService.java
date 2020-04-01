/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.professional.boundaries;

import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.category.entities.Category;
import fr.trendev.comptandye.client.entities.Client;
import fr.trendev.comptandye.collectivegroup.entities.CollectiveGroup;
import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.common.boundaries.AssociationManagementEnum;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.expense.entities.Expense;
import fr.trendev.comptandye.individual.controllers.IndividualFacade;
import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.notification.entities.Notification;
import fr.trendev.comptandye.offering.entities.Offering;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.security.controllers.PasswordManager;
import fr.trendev.comptandye.usergroup.controllers.UserGroupFacade;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import fr.trendev.comptandye.vatrates.controllers.VatRatesFacade;
import fr.trendev.comptandye.vatrates.entities.VatRates;
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
@Path("Professional")
@RolesAllowed({"Administrator", "Professional"})
public class ProfessionalService extends AbstractCommonService<Professional, String> {

    @Inject
    PasswordManager passwordManager;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    @Inject
    IndividualFacade individualFacade;

    @Inject
    VatRatesFacade vatRatesFacade;

    private final Logger LOG = Logger.getLogger(
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
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
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
     * @return
     */
    @RolesAllowed({"Administrator"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Professional entity) {
        LOG.log(Level.INFO, "Creating Professional {0}", entity.getEmail());

        return super.post(entity, e -> {

            //Reset a new id (if provided)
            entity.getCustomerDetails().setId(UUIDGenerator.generateID());
            entity.getAddress().setId(UUIDGenerator.generateID());
            entity.getSocialNetworkAccounts().setId(UUIDGenerator.generateID());

            //Generates a new UUID
            e.setUuid(UUIDGenerator.generate("PRO-", true));

            //Generates + Hashes the provided password
            String hpwd = passwordManager.hashPassword(e.
                    getPassword());
            e.setPassword(hpwd);

            // Puts the Professional in the Professional's UserGroup and links the Professional with the UserGroup
            String groupName = Professional.class.getSimpleName();

            UserGroup proGroup = userGroupFacade.find(groupName);
            if (!proGroup.getUserAccounts().add(e) || !e.getUserGroups().add(proGroup)) {
                String errMsg = "User " + e.getEmail() + " cannot be added in the group " + groupName;
                LOG.log(Level.SEVERE, errMsg);
                throw new WebApplicationException(errMsg);
            }
            // Unblocks the user
            e.setBlocked(false);

            //Reset bills count and ref date (if provided)
            //Values ignored in PUT
            e.setBillsCount(0);
            e.setBillsRefDate(null);

            this.controlVATCode(e);

            /**
             * Resets subscription fields
             */
            e.setStripeCustomerId(null);
            e.setStripeSubscriptionId(null);
            e.setTos(false);
            e.setRescissionDate(null);
        });
    }

    /**
     * Control if a VAT Code is provided. If a VAT Code is provided, the
     * specific VAT Rates will be linked with the Professional
     *
     * @param pro the professional
     */
    private void controlVATCode(Professional pro) {

        String vatcode = pro.getVatcode();
        if (vatcode != null) {
            try {
                String countryId = vatcode.substring(0, 2);
                VatRates vr = this.vatRatesFacade.find(countryId);
                if (vr != null) {
                    pro.setVatRates(vr);
                } else {
                    throw new Exception("Cannot set VAT code for country id ["
                            + countryId + "]");
                }
            } catch (Exception ex) {
                throw new WebApplicationException(
                        "Cannot find VAT Rates from VAT code [" + vatcode + "]");
            }
        } else {
            pro.setVatRates(null);
        }
    }

    /**
     * Prepares and Updates a Professional. Email, UUID, blocked and
     * registrationDate and Stripe, TOS fields will be ignored and should not be
     * changed by the way...
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

        return super.put(
                entity,
                proEmail,
                e -> {
                    // hashes the provided password
                    if (entity.getPassword() != null
                    && !entity.getPassword().isEmpty()) {
                        String hpwd = passwordManager.hashPassword(
                                entity.getPassword());
                        e.setPassword(hpwd);
                    }

                    e.setUsername(entity.getUsername());

                    // set id preventing id spoufing (security reason)
                    entity.getCustomerDetails().setId(UUIDGenerator.generateID());
                    entity.getAddress().setId(UUIDGenerator.generateID());
                    entity.getSocialNetworkAccounts().setId(UUIDGenerator.generateID());

                    e.setCustomerDetails(entity.getCustomerDetails());
                    e.setAddress(entity.getAddress());
                    e.setSocialNetworkAccounts(entity.getSocialNetworkAccounts());

                    e.setWebsite(entity.getWebsite());
                    e.setCompanyID(entity.getCompanyID());
                    e.setCompanyName(entity.getCompanyName());
                    e.setJobrole(entity.getJobrole());

                    /**
                     * Sets the VAT code if specified and links with the
                     * corresponding VAT rates. Sets to null otherwise. Provided
                     * VAT Rates are ignored.
                     */
                    e.setVatcode(entity.getVatcode());
                    this.controlVATCode(e);

                    e.setCreationDate(entity.getCreationDate());
                    e.setBusinesses(entity.getBusinesses());
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
    public void getUserGroups(@Suspended final AsyncResponse ar, @PathParam(
            "email") String email) {
        LOG.log(Level.INFO,
                "REST request to get userGroups of Professional : {0}", email);
        super.provideRelation(ar, email,
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
                (e, a)
                -> e.getUserGroups().add(a) & a.getUserAccounts().add(e));
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
                (e, a)
                -> e.getUserGroups().remove(a) & a.getUserAccounts().remove(e));
    }

    @Path("bills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getBills(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getBills,
                Bill.class);
    }

    @Path("clients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getClients(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getClients,
                Client.class);
    }

    @Path("offerings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getOfferings(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getOfferings,
                Offering.class);
    }

    @Path("categories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getCategories(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getCategories,
                Category.class);
    }

    @Path("collectiveGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getCollectiveGroups(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getCollectiveGroups, CollectiveGroup.class);
    }

    @Path("expenses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getExpenses(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getExpenses, Expense.class);
    }

    @Path("stock")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getStock(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getStock, Product.class);
    }

    @Path("notifications")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getNotifications(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
                Professional::getNotifications, Notification.class);
    }

    @Path("individuals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getIndividuals(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("email") String email) {
        this.provideRelation(ar, this.getProEmail(sec, email),
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
                (p, i)
                -> p.getIndividuals().add(i) & i.getProfessionals().add(p));
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
                (p, i)
                -> p.getIndividuals().remove(i) & i.getProfessionals().remove(p));
    }

    /**
     * Resets the Stripe data of the Professional but does not delete the Stripe
     * objects on their platform.
     *
     * @param email the owner
     * @return HTTP Response
     */
    @RolesAllowed({"Administrator"})
    @Path("clear-subscription-info/{email}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearSubscriptionInfo(@PathParam("email") String email) {
        LOG.log(Level.WARNING, "Clearing Stripe information of user {0}", email);
        try {
            Professional pro = professionalFacade.find(email);
            if (pro == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                pro.setStripeCustomerId(null);
                pro.setStripeSubscriptionId(null);
                pro.setTos(false);
                LOG.
                        log(Level.INFO,
                                "Stripe information of user [{0}] : CLEARED",
                                email);
                return Response.accepted(pro).build();
            }
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error clearing Subscription information of user ["
                    + email + "]",
                    ex);
        }

    }

}
