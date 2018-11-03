package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.ClientBill;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("clientBill")
public class ClientBillFacade extends AbstractFacade<ClientBill, BillPK> {

    @Inject
    private EntityManager em;

    public ClientBillFacade() {
        super(ClientBill.class);
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
