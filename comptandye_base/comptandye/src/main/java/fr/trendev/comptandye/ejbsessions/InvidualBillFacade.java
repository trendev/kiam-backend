package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.InvidualBill;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("invidualBill")
public class InvidualBillFacade extends AbstractFacade<InvidualBill, BillPK> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvidualBillFacade() {
        super(InvidualBill.class);
    }

}
