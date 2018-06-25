package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Bill;
import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.Bill_;
import fr.trendev.comptandye.entities.Professional;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
@Named("bill")
public class BillFacade extends AbstractFacade<Bill, BillPK> {

    @Inject
    private EntityManager em;

    public BillFacade() {
        super(Bill.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(BillPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getReference());
        //converts the delivery date in UTC long
        sb.append("/").append(pk.getDeliveryDate().getTime());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

    public List<Date> findLastValidBillsRefDate(Professional professional) {
        EntityManager em = this.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        Root<Bill> root = cq.from(Bill.class);
        cq.select(cb.greatest(root.get(Bill_.deliveryDate))).where(
                cb.isFalse(root.get(Bill_.cancelled)),
                cb.equal(root.get(Bill_.professional), professional));
        return em.createQuery(cq).getResultList();
    }

}
