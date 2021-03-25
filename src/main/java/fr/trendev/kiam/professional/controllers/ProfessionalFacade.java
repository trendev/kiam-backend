package fr.trendev.kiam.professional.controllers;

import fr.trendev.kiam.bill.entities.Bill;
import fr.trendev.kiam.bill.entities.Bill_;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.professional.entities.Professional;
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
@Named("professional")
public class ProfessionalFacade extends AbstractFacade<Professional, String> {

    @Inject
    private EntityManager em;

    public ProfessionalFacade() {
        super(Professional.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(String pk) {
        return pk;
    }

    public List<Bill> getBills(Professional professional,
            Date from, Date to) {
     
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Bill> cq = cb.createQuery(Bill.class);
        Root<Bill> root = cq.from(Bill.class);
        cq.select(root)
                .where(
                        cb.between(
                                root.get(Bill_.issueDate),
                                cb.literal(from),
                                cb.literal(to)),
                        cb.equal(root.get(Bill_.professional), professional));
        cq.orderBy(cb.desc(root.get(Bill_.issueDate)));
        return em.createQuery(cq)
                .getResultList();
    }

}
