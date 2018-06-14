package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Notification;
import fr.trendev.comptandye.entities.NotificationPK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("notification")
public class NotificationFacade extends AbstractFacade<Notification, NotificationPK> {

    @Inject
    private EntityManager em;

    public NotificationFacade() {
        super(Notification.class);
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
