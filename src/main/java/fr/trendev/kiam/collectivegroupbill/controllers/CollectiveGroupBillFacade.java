package fr.trendev.kiam.collectivegroupbill.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.bill.entities.BillPK;
import fr.trendev.kiam.collectivegroupbill.entities.CollectiveGroupBill;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("collectiveGroupBill")
public class CollectiveGroupBillFacade extends AbstractFacade<CollectiveGroupBill, BillPK> {

    @Inject
    private EntityManager em;

    public CollectiveGroupBillFacade() {
        super(CollectiveGroupBill.class);
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

}
