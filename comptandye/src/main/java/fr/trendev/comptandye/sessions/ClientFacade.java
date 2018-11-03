package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
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

    public ClientFacade() {
        super(Client.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(ClientPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getId());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

}
