package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.IndividualBill;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("individualBill")
public class IndividualBillFacade extends AbstractFacade<IndividualBill, BillPK> {

    @Inject
    private EntityManager em;

    public IndividualBillFacade() {
        super(IndividualBill.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(BillPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append("key?");
        sb.append("reference=").append(pk.getReference());
        //converts the delivery date in UTC long
        sb.append("&deliverydate=").append(pk.getDeliveryDate().getTime());
        sb.append("&professional=").append(pk.getProfessional());
        return sb.toString();
    }
}
