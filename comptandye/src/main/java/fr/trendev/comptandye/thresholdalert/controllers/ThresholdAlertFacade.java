package fr.trendev.comptandye.thresholdalert.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.notification.entities.NotificationPK;
import fr.trendev.comptandye.thresholdalert.entities.ThresholdAlert;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("thresholdalert")
public class ThresholdAlertFacade extends AbstractFacade<ThresholdAlert, NotificationPK> {

    @Inject
    private EntityManager em;

    public ThresholdAlertFacade() {
        super(ThresholdAlert.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(NotificationPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getId());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

}
