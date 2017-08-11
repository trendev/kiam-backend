package fr.trendev.comptandye.sessions;

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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientBillFacade() {
        super(ClientBill.class);
    }

}
