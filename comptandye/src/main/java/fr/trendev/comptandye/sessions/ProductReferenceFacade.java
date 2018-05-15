/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.ProductReference;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("productreference")
public class ProductReferenceFacade extends AbstractFacade<ProductReference, String> {

    @Inject
    private EntityManager em;

    public ProductReferenceFacade() {
        super(ProductReference.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(String pk) {
        return pk;
    }
}
