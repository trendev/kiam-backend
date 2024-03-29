/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.common.boundaries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.exceptions.ExceptionHandler;
import fr.trendev.kiam.exceptions.ExceptionHelper;
import fr.trendev.kiam.exceptions.InvalidDeliveryDateException;
import fr.trendev.kiam.security.controllers.AuthenticationHelper;
import fr.trendev.kiam.utils.UUIDGenerator;
import fr.trendev.kiam.useraccount.entities.UserAccountType;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBTransactionRolledbackException;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.json.Json;
import javax.persistence.RollbackException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
public abstract class AbstractCommonService<E, P> {

    /**
     * Used only in stringify. Easiest than creating/parsing JSON Objects.
     */
    @Inject
    private ObjectMapper om;

    @Inject
    private AuthenticationHelper authenticationHelper;

    @Inject
    protected ExceptionHandler exceptionHandler;
    
    @Inject
    protected UUIDGenerator UUIDGenerator;

    @Resource
    private ManagedExecutorService managedExecutorService;

    private final Class<E> entityClass;

    public AbstractCommonService(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     *
     * @return the logger of the Service
     */
    protected abstract Logger getLogger();

    /**
     *
     * @return the facade (EJB session) associated to the Service
     */
    protected abstract AbstractFacade<E, P> getFacade();

    protected ManagedExecutorService getManagedExecutorService() {
        return managedExecutorService;
    }

    /**
     * Finds and provides all elements
     *
     * @param ar the Asynchronous Response
     */
    protected void findAll(final AsyncResponse ar) {
        getLogger().log(Level.INFO, "Providing the {0} list", entityClass.
                getSimpleName());
        CompletableFuture
                .supplyAsync(() -> this.findAll(), managedExecutorService)
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));
    }

    private Response findAll() {
        try {
            List<E> list = getFacade().findAll();
            getLogger().log(Level.INFO, "{0} list size = {1}", new Object[]{
                entityClass.getSimpleName(), list.
                size()});

            return Response.status(Response.Status.OK)
                    .entity(list).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing " + entityClass.getSimpleName()
                    + " list");
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    protected Response count() {
        try {
            Long count = getFacade().count();
            getLogger().log(Level.INFO, "Total Count of {0} = {1}",
                    new Object[]{entityClass.getSimpleName(), count});

            return Response.status(Response.Status.OK)
                    .entity(count).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing " + entityClass.getSimpleName()
                    + " count");
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Finds an entity or refreshes it from the DB.
     *
     * @param pk the primary key of the entity
     * @param refresh a boolean which will indicate if refresh must be
     * performed. Default is false.
     * @return the entity find in the persistent context or refreshed from the
     * DB.
     */
    protected Response find(P pk, boolean refresh) {
        try {
            return Optional.ofNullable(getFacade().find(pk))
                    .map(result -> {
                        if (refresh) {
                            getFacade().refresh(result);
                        }
                        return Response.status(Response.Status.OK).
                                entity(result).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + getFacade().prettyPrintPK(pk)
                                    + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing " + entityClass.getSimpleName()
                    + " "
                    + getFacade().prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Provides a specific relation from an Entity.
     *
     * @param <R> the type of the entity in the relation
     * @param ar the Asynchronous Response
     * @param pk the primary key of the entity
     * @param getFunction the getter in the entity which will provide the
     * relation
     * @param elementClass the Class of the element contained in the relation
     */
    protected <R> void provideRelation(final AsyncResponse ar,
            P pk,
            Function<E, Collection<R>> getFunction,
            Class<R> elementClass) {
        CompletableFuture
                .supplyAsync(() -> this.provideRelation(pk, getFunction,
                        elementClass), managedExecutorService)
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));
    }

    private <R> Response provideRelation(
            P pk,
            Function<E, Collection<R>> getFunction,
            Class<R> elementClass) {
        try {
            getLogger().log(Level.INFO,
                    "REST request to get list of {0} from {1} {2}",
                    new Object[]{
                        elementClass.getSimpleName(),
                        entityClass.getSimpleName(),
                        getFacade().prettyPrintPK(pk)});
            return Optional.ofNullable(getFacade().find(pk))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            getFunction.apply(result)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + getFacade().prettyPrintPK(pk)
                                    + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing a relationship of "
                    + entityClass.getSimpleName()
                    + " "
                    + getFacade().prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Prepares and Persists an Entity which is not owned by a Professional (an
     * Entity without professional's email in primary key). If the primary key
     * of the entity is an auto-generated id, the id field is reset to null in
     * initAction and will be generated when the entity will be persist on the
     * DB.
     *
     * @param payload the entity to persist
     * @param initAction the operations used to prepare the persist (ex: add the
     * entity to a relation, set fields like passwords, uuid...)
     * @return the persisted entity
     */
    protected Response post(E payload, Consumer<E> initAction) {

        String jsonString = this.stringify(payload);

        try {
            initAction.accept(payload);
            getFacade().create(payload);
//            getFacade().flush();
//            getFacade().refresh(entity);
            P pk = getFacade().getIdentifier(payload);
            getLogger().log(Level.INFO, entityClass.getSimpleName()
                    + " {0} created", getFacade().prettyPrintPK(pk));
            return Response.created(new URI(entityClass.getSimpleName() + "/"
                    + getFacade().prettyPrintPK(pk))).
                    entity(
                            payload).
                    build();
        } catch (InvalidDeliveryDateException | RollbackException |
                EJBTransactionRolledbackException ex) {
            throw ex;
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs creating "
                    + entityClass.getSimpleName()
                    + " "
                    + jsonString);
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Prepares and Persists an Entity which is owned by a Professional (an
     * Entity with professional's email in primary key). These entities require
     * to first find the owner and to add the entity to the Professional's
     * entity list. Ex: persisting a Client requires to find the Professional
     * and add the Client to the Professional's Client list. If an error occurs,
     * a Rollback is performed by the JPA provider.
     *
     * @param <O> the owner's type, usually Professional
     * @param <K> the owner's primary key type, usually String
     * @param payload the entity to persist
     * @param ownerPK the primary key of the owner
     * @param ownerPrettyPrintFunction the method used to pretty print the
     * primary key of the owner, should be located in an AbstractFacade.
     * @param ownerClass the Class of the owner
     * @param ownerFacade the facade (EJB session) used with the owner
     * @param setFunction the setter function from the Entity used to set is
     * link with the owner, ex: Category::setProfessional
     * @param getFunction the getter function from the owner used to create the
     * other side of the previous link
     * @param initAction operations to perform before persisting the entity,
     * provided to {@link AbstractCommonService#post}
     * @return the persisted entity
     */
    protected <O, K> Response post(E payload, K ownerPK,
            BiFunction<AbstractFacade<O, K>, K, String> ownerPrettyPrintFunction,
            Class<O> ownerClass, AbstractFacade<O, K> ownerFacade,
            BiConsumer<E, O> setFunction,
            Function<O, List<? super E>> getFunction,
            Consumer<E> initAction) {
        String jsonString = this.stringify(payload);
        getLogger().log(Level.INFO, "Creating {0} {1}", new Object[]{
            entityClass.getSimpleName(), jsonString});
        try {
            return Optional.ofNullable(ownerFacade.find(ownerPK))
                    .map(o -> {
                        setFunction.accept(payload, o);
                        getFunction.apply(o).add(payload);
                        return this.post(payload, initAction);
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Cannot create " + entityClass.
                                            getSimpleName() + " " + jsonString
                                    + " because " + ownerClass.getSimpleName()
                                    + " " + ownerPrettyPrintFunction.apply(
                                            ownerFacade,
                                            ownerPK)
                                    + " is not found !").
                                    build()).
                            build());

        } catch (InvalidDeliveryDateException | RollbackException |
                EJBTransactionRolledbackException ex) {
            throw ex;
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs in subroutine post() creating "
                    + entityClass.getSimpleName() + " "
                    + jsonString);
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }

    }

    /**
     * Prepares and Updates an Entity.
     *
     * @param payload the entity to update
     * @param pk the primary key of the entity
     * @param updateAction operations to perform before the update (additional
     * checks and so on)
     * @return the updated entity
     */
    protected Response put(E payload, P pk, Consumer<E> updateAction) {
        try {
            return Optional.ofNullable(getFacade().find(pk))
                    .map(result -> {
                        updateAction.accept(result);
                        //facade.edit(result);
                        getLogger().log(Level.INFO, entityClass.getSimpleName()
                                + " {0} updated", getFacade().prettyPrintPK(pk));
                        return Response.status(Response.Status.OK).entity(
                                result).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + getFacade().prettyPrintPK(pk)
                                    + " not found").
                                    build()).
                            build());
        } catch (EJBTransactionRolledbackException | RollbackException ex) {
            throw ex;
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs updating "
                    + entityClass.getSimpleName()
                    + " "
                    + getFacade().prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Prepares and Deletes an Entity. Additional operations required to remove
     * the entity from a relationship should be provided in the prepareDelete
     * consumer.
     *
     * @param pk the primary key of the Entity
     * @param prepareDelete operations to perform before deleting the entity
     * @return HTTP OK if no error occurs
     */
    protected Response delete(P pk, Consumer<E> prepareDelete) {
        try {
            return Optional.ofNullable(getFacade().find(pk))
                    .map(result -> {
                        prepareDelete.accept(result);
                        //commit all previous modifications
                        getFacade().flush();
                        getFacade().remove(result);
                        getLogger().log(Level.INFO, entityClass.getSimpleName()
                                + " {0} deleted", getFacade().prettyPrintPK(pk));
                        return Response.ok().build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + getFacade().prettyPrintPK(pk)
                                    + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs deleting "
                    + entityClass.getSimpleName()
                    + " "
                    + getFacade().prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Adds or Removes an Entity from a Relationship.
     *
     * @param <A> type of the entity on the other side of the relationship,
     * called the target
     * @param <U> the type of the primary key of the target
     * @param option INSERT or REMOVE
     * @param entityPk the primary key of the entity
     * @param associationFacade the facade (EJB session) used to manipulate the
     * target
     * @param associationPk the primary key of the target
     * @param associationEntityClass the Class of the target
     * @param associationFunction the operations to perform manipulate the
     * relationship (add on both sides or remove from both sides)
     * @return the target of the relationship
     */
    protected <A, U> Response manageAssociation(
            AssociationManagementEnum option,
            P entityPk,
            AbstractFacade<A, U> associationFacade,
            U associationPk,
            Class<A> associationEntityClass,
            BiFunction<E, A, Boolean> associationFunction) {
        try {
            return Optional.ofNullable(getFacade().find(entityPk))
                    .map(e -> {
                        return Optional.ofNullable(associationFacade.find(
                                associationPk))
                                .map(a -> {
                                    boolean result = associationFunction.
                                            apply(e, a);
                                    getFacade().edit(e);
                                    associationFacade.edit(a);
                                    getLogger().log(Level.INFO,
                                            "{0} {1} {2} {3} {4} : {5}",
                                            new Object[]{entityClass.
                                                        getSimpleName(),
                                                getFacade().prettyPrintPK(
                                                        entityPk),
                                                option.equals(
                                                        AssociationManagementEnum.INSERT) ? "associated to " : "unassociated with",
                                                associationEntityClass.
                                                        getSimpleName(),
                                                associationFacade.prettyPrintPK(
                                                        associationPk),
                                                result});

                                    if (!result) {
                                        String errmsg = "No existing association between "
                                                + entityClass.
                                                        getSimpleName()
                                                + " "
                                                + getFacade().
                                                        prettyPrintPK(
                                                                entityPk)
                                                + " and "
                                                + associationEntityClass.
                                                        getSimpleName()
                                                + " "
                                                + associationFacade.
                                                        prettyPrintPK(
                                                                associationPk);
                                        throw new BadRequestException(errmsg);
                                    } else {
                                        return Response.ok(a).build();
                                    }
                                })
                                .orElse(Response.status(
                                        Response.Status.NOT_FOUND).entity(
                                                Json.createObjectBuilder().add(
                                                        "error",
                                                        entityClass.
                                                                getSimpleName()
                                                        + " "
                                                        + getFacade().
                                                                prettyPrintPK(
                                                                        entityPk)
                                                        + " cannot be "
                                                        + (option.equals(
                                                                AssociationManagementEnum.INSERT) ? "associated to" : "unassociated with")
                                                        + " undiscovered "
                                                        + associationEntityClass.
                                                                getSimpleName()
                                                        + " "
                                                        + associationFacade.
                                                                prettyPrintPK(
                                                                        associationPk)).
                                                        build()).
                                        build());
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + getFacade().prettyPrintPK(
                                            entityPk)
                                    + " not found and cannot be " + (option.
                                            equals(
                                                    AssociationManagementEnum.INSERT) ? "associated to" : "unassociated with")
                                    + " "
                                    + associationFacade.prettyPrintPK(
                                            associationPk)).build()).build());
        } catch (EJBTransactionRolledbackException | RollbackException ex) {
            throw ex;
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs " + (option.equals(
                            AssociationManagementEnum.INSERT) ? "associating" : "unassociated")
                    + " " + entityClass.getSimpleName()
                    + " " + getFacade().prettyPrintPK(
                            entityPk)
                    + " with " + associationEntityClass.getSimpleName() + " "
                    + associationFacade.prettyPrintPK(
                            associationPk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Represents an Entity as a Json String.
     *
     * @param entity the entity to stringify (serialize)
     * @return the serialized entity
     * @throws BadRequestException if an error occurs during the serialization
     */
    protected String stringify(E entity) {
        try {
            String jsonString = om.writeValueAsString(entity);
            return jsonString;
        } catch (JsonProcessingException ex) {
            String errmsg = "Entity " + entity
                    + " can not be produced as a String";
            getLogger().log(Level.WARNING,
                    errmsg, ex);
            throw new BadRequestException(errmsg);
        }
    }

    /**
     * Extracts the email of professional from the SecurityContext or provides
     * the email found in the query.
     *
     * @param sec the security context
     * @param professional the owner's email provided in the query parameters
     * @return the professional's email
     */
    protected String getProEmail(SecurityContext sec, String professional) {
        return authenticationHelper
                .getProEmail(sec, professional);
    }

    /**
     * Extracts the individual's email from the SecurityContext or provides the
     * email found in the query.
     *
     * @param sec the security context
     * @param individual the owner's email provided in the query parameters
     * @return the individual's email
     */
    protected String getIndEmail(SecurityContext sec, String individual) {
        return Optional.ofNullable(
                sec.isUserInRole(UserAccountType.INDIVIDUAL)
                ? sec.getUserPrincipal().getName() : individual)
                .map(Function.identity())
                .orElseThrow(() -> new BadRequestException(
                        "Impossible to find the individual's email from the SecurityContext or from the Query Parameters"));
    }
}
