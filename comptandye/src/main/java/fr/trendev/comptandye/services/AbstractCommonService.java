/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public abstract class AbstractCommonService<E, P> {

    @Inject
    ObjectMapper om;

    private final Class<E> entityClass;

    public AbstractCommonService(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract Logger getLogger();

    private String prettyPrintPK(P pk) {
        if (pk instanceof BillPK) {
            BillPK key = (BillPK) pk;
            StringBuilder sb = new StringBuilder();
            sb.append("pk?");
            sb.append("reference=").append(key.getReference()).append("&");
            sb.append("deliveryDate=").append(key.getDeliveryDate().getTime()).
                    append("&");
            sb.append("professional=").append(key.getProfessional());
            return sb.toString();

        } else {
            return pk.toString();
        }
    }

    protected Response findAll(AbstractFacade<E, P> facade) {
        try {
            List<E> list = facade.findAll();
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
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected Response count(AbstractFacade<E, P> facade) {
        try {
            Long count = facade.count();
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
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected Response find(AbstractFacade<E, P> facade,
            P pk, boolean refresh) {
        try {
            return Optional.ofNullable(facade.find(pk))
                    .map(result -> {
                        if (refresh) {
                            facade.refresh(result);
                        }
                        return Response.status(Response.Status.OK).
                                entity(result).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + prettyPrintPK(pk) + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing " + entityClass.getSimpleName()
                    + " "
                    + prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected <R> Response provideRelation(AbstractFacade<E, P> facade,
            P pk, Function<E, R> getFunction) {
        try {

            return Optional.ofNullable(facade.find(pk))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            getFunction.apply(result)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + prettyPrintPK(pk) + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing a relationship of "
                    + entityClass.getSimpleName()
                    + " "
                    + prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected Response post(E entity, AbstractFacade<E, P> facade,
            Consumer<E> initAction) {

        String jsonString = this.stringify(entity);

        try {
            initAction.accept(entity);
            facade.create(entity);
            facade.flush();
            facade.refresh(entity);
            P pk = facade.getIdentifier(entity);
            getLogger().log(Level.INFO, entityClass.getSimpleName()
                    + " {0} created", prettyPrintPK(pk));
            return Response.created(new URI("/restapi/" + entityClass.
                    getSimpleName() + "/" + prettyPrintPK(pk))).entity(entity).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs creating "
                    + entityClass.getSimpleName()
                    + " "
                    + jsonString);
            getLogger().log(Level.WARNING, errmsg, ex);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected Response put(E entity, AbstractFacade<E, P> facade, P pk,
            Consumer<E> updateAction) {
        try {
            return Optional.ofNullable(facade.find(pk))
                    .map(result -> {
                        updateAction.accept(result);
                        facade.edit(result);
                        getLogger().log(Level.INFO, entityClass.getSimpleName()
                                + " {0} updated", prettyPrintPK(pk));
                        return Response.status(Response.Status.OK).entity(
                                result).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + prettyPrintPK(pk) + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs updating "
                    + entityClass.getSimpleName()
                    + " "
                    + prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected Response delete(AbstractFacade<E, P> facade, P pk,
            Consumer<E> prepareDelete) {
        try {
            return Optional.ofNullable(facade.find(pk))
                    .map(result -> {
                        prepareDelete.accept(result);
                        facade.flush();
//TODO : investigation in progress
//                        facade.refresh(result);
                        facade.remove(result);
                        getLogger().log(Level.INFO, entityClass.getSimpleName()
                                + " {0} deleted", prettyPrintPK(pk));
                        return Response.ok().build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + prettyPrintPK(pk) + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs deleting "
                    + entityClass.getSimpleName()
                    + " "
                    + prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected <A, U> Response manageAssociation(
            AssociationManagementEnum option,
            AbstractFacade<E, P> entityFacade,
            P entityPk,
            AbstractFacade<A, U> associationFacade,
            U associationPk,
            Class<A> associationEntityClass,
            BiFunction<E, A, Boolean> associationFunction) {
        try {
            return Optional.ofNullable(entityFacade.find(entityPk))
                    .map(e -> {
                        return Optional.ofNullable(associationFacade.find(
                                associationPk))
                                .map(a -> {
                                    boolean result = associationFunction.
                                            apply(e, a);
                                    entityFacade.edit(e);
                                    associationFacade.edit(a);
                                    getLogger().log(Level.INFO,
                                            "{0} {1} {2} {3} {4} : {5}",
                                            new Object[]{entityClass.
                                                        getSimpleName(),
                                                entityPk,
                                                option.equals(
                                                        AssociationManagementEnum.INSERT) ? "inserted in " : "removed from",
                                                associationEntityClass.
                                                        getSimpleName(),
                                                associationPk,
                                                result});
                                    return result ? Response.ok(a).build() : Response.
                                            status(Response.Status.BAD_REQUEST).
                                            entity(Json.createObjectBuilder().
                                                    add("error",
                                                            "No existing association").
                                                    build()).build();
                                })
                                .orElse(Response.status(
                                        Response.Status.NOT_FOUND).entity(
                                                Json.createObjectBuilder().add(
                                                        "error",
                                                        entityClass.
                                                                getSimpleName()
                                                        + " "
                                                        + entityPk
                                                        + " cannot be "
                                                        + (option.equals(
                                                                AssociationManagementEnum.INSERT) ? "inserted into" : "removed from")
                                                        + " undiscovered "
                                                        + associationEntityClass.
                                                                getSimpleName()
                                                        + " "
                                                        + associationPk).build()).
                                        build());
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + entityPk
                                    + " not found and cannot be " + (option.
                                            equals(
                                                    AssociationManagementEnum.INSERT) ? "inserted into" : "removed from")
                                    + " "
                                    + associationPk).build()).build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs " + (option.equals(
                            AssociationManagementEnum.INSERT) ? "inserting" : "removing")
                    + " " + entityClass.getSimpleName()
                    + " " + entityPk
                    + " in " + associationEntityClass.getSimpleName() + " "
                    + associationPk);
            getLogger().log(Level.WARNING, errmsg, ex);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected String stringify(E entity) {
        String jsonString = entity.toString();
        try {
            jsonString = om.writeValueAsString(entity);
        } catch (JsonProcessingException ex) {
            getLogger().log(Level.WARNING,
                    "Entity " + entity + " can not be produced as a String", ex);
        }
        return jsonString;
    }
}
