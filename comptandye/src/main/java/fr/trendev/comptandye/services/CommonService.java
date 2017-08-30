/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public abstract class CommonService<E, P> {

    private final Class<E> entityClass;

    public CommonService(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract Logger getLogger();

    private String prettyPrintPK(P pk) {
        if (pk instanceof BillPK) {
            BillPK key = (BillPK) pk;
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("reference", key.getReference())
                    .add("deliveryDate", key.getDeliveryDate().toString())
                    .add("professional", key.getProfessional())
                    .build();
            String jsonString = pk.toString();
            try (Writer writer = new StringWriter()) {
                Json.createWriter(writer).write(jsonObject);
                jsonString = writer.toString();
            } catch (IOException ex) {
                getLogger().log(Level.WARNING,
                        "PrimaryKey {0} cannot be pretty printed : {1}",
                        new Object[]{pk, ex});
            } finally {
                return jsonString;
            }

        } else {
            return pk.toString();
        }
    }

    protected Response findAll(AbstractFacade<E, P> facade,
            Function<AbstractFacade<E, P>, List<E>> findAllFunction) {
        try {
            List<E> list = findAllFunction.apply(facade);
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
            getLogger().log(Level.WARNING, errmsg);
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
            getLogger().log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected Response find(AbstractFacade<E, P> facade,
            P primaryKey) {
        try {
            return Optional.ofNullable(facade.find(primaryKey))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            result).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + prettyPrintPK(primaryKey) + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing " + entityClass.getSimpleName()
                    + " "
                    + prettyPrintPK(primaryKey));
            getLogger().log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    protected <R> Response provideRelation(AbstractFacade<E, P> facade,
            P primaryKey, Function<E, R> getFunction) {
        try {

            return Optional.ofNullable(facade.find(primaryKey))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            getFunction.apply(result)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    entityClass.getSimpleName() + " "
                                    + prettyPrintPK(primaryKey) + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing a relationship of "
                    + entityClass.getSimpleName()
                    + " "
                    + prettyPrintPK(primaryKey));
            getLogger().log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }
}
