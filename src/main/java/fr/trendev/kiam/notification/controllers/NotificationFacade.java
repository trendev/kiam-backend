package fr.trendev.comptandye.notification.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.notification.entities.Notification;
import fr.trendev.comptandye.notification.entities.NotificationPK;
import fr.trendev.comptandye.notification.entities.Notification_;
import fr.trendev.comptandye.professional.entities.Professional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

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

    public int checkAll(Professional professional) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Notification> cu = cb.createCriteriaUpdate(
                Notification.class);
        Root<Notification> root = cu.from(Notification.class);
        cu.set(root.get(Notification_.checked), true);
        cu.where(cb.equal(root.get(Notification_.professional), professional));
        Query q = em.createQuery(cu);
        return q.executeUpdate();
    }

}
