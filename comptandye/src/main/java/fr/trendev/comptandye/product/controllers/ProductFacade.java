package fr.trendev.comptandye.product.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.product.entities.ProductPK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("product")
public class ProductFacade extends AbstractFacade<Product, ProductPK> {

    @Inject
    private EntityManager em;

    public ProductFacade() {
        super(Product.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(ProductPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getProductReference());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

}
