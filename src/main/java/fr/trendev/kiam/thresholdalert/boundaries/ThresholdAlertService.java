/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.thresholdalert.boundaries;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.notification.entities.NotificationPK;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.thresholdalert.entities.ThresholdAlert;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.thresholdalert.controllers.ThresholdAlertFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("ThresholdAlert")
@RolesAllowed({"Administrator"})
public class ThresholdAlertService extends AbstractCommonService<ThresholdAlert, NotificationPK> {
    
    @Inject
    private ThresholdAlertFacade thresholdAlertFacade;
    
    @Inject
    private ProfessionalFacade professionalFacade;
    
    private final Logger LOG = Logger.getLogger(ThresholdAlertService.class.
            getName());
    
    public ThresholdAlertService() {
        super(ThresholdAlert.class);
    }
    
    @Override
    protected Logger getLogger() {
        return LOG;
    }
    
    @Override
    protected AbstractFacade<ThresholdAlert, NotificationPK> getFacade() {
        return thresholdAlertFacade;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, ThresholdAlert payload,
            @QueryParam("professional") String professional) {
        
        String email = this.getProEmail(sec, professional);
        
        return super.<Professional, String>post(payload, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade,
                ThresholdAlert::setProfessional,
                Professional::getNotifications, e -> {
            e.setId(UUIDGenerator.generateID());
        });
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, ThresholdAlert payload,
            @QueryParam("professional") String professional) {
        
        NotificationPK pk = new NotificationPK(payload.getId(), this.
                getProEmail(sec,
                        professional));
        
        LOG.log(Level.INFO, "Updating ThresholdAlert {0}",
                thresholdAlertFacade.
                        prettyPrintPK(pk));
        return super.put(payload, pk, e -> {
            e.setChecked(payload.isChecked());
            e.setLevelRank(payload.getLevelRank());
            e.setBarcode(payload.getBarcode());
            e.setDescription(payload.getDescription());
            e.setBrand(payload.getBrand());
            e.setThreshold(payload.getThreshold());
            e.setQty(payload.getQty());
            e.setQualifier(payload.getQualifier());
        });
    }
}
