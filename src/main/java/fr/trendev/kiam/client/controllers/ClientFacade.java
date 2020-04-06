package fr.trendev.kiam.client.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.client.entities.ClientPK;
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
