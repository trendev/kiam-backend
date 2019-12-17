/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.vatrates.boundaries;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.vatrates.entities.VatRates;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.vatrates.controllers.VatRatesFacade;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("VatRates")
@RolesAllowed({"Administrator"})
public class VatRatesService extends AbstractCommonService<VatRates, String> {

    @Inject
    VatRatesFacade vatratesFacade;

    private final Logger LOG = Logger.getLogger(
            VatRatesService.class.
                    getName());

    public VatRatesService() {
        super(VatRates.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<VatRates, String> getFacade() {
        return vatratesFacade;
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

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("id") String id,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get VatRates : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(VatRates payload) {
        LOG.log(Level.INFO, "Creating VatRates {0}", super.stringify(payload));
        return super.post(payload, e -> {
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(VatRates payload) {
        LOG.log(Level.INFO, "Updating VatRates {0}", payload.getCountryId());
        return super.put(payload, payload.getCountryId(),
                e -> {
            e.setCountry(payload.getCountry());
            e.setRates(payload.getRates());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") String id) {
        LOG.log(Level.INFO, "Deleting VatRates {0}", id);
        return super.delete(id, e -> {
        });
    }
}
