package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientPK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("client")
public class ClientFacade extends AbstractFacade<Client, ClientPK> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }

}
