package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.IndividualBill;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("invidualBill")
public class IndividualBillFacade extends AbstractFacade<IndividualBill, BillPK> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IndividualBillFacade() {
        super(IndividualBill.class);
    }

}
