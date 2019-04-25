package fr.trendev.comptandye.individualbill.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.bill.entities.BillPK;
import fr.trendev.comptandye.individualbill.entities.IndividualBill;
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
        sb.append(pk.getReference());
        //converts the delivery date in UTC long
        sb.append("/").append(pk.getDeliveryDate().getTime());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }
}
