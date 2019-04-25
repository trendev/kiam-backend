/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.productreference.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.productreference.entities.ProductReference;
import fr.trendev.comptandye.productreference.entities.ProductReference_;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

    public List<ProductReference> findFromBarcode(String barcode) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductReference> cq = cb.createQuery(
                ProductReference.class);
        Root<ProductReference> root = cq.from(ProductReference.class);
        cq.select(root)
                .where(cb.like(root.get(ProductReference_.barcode),
                        barcode + "%"));
//        cq.orderBy(cb.asc(root.get(ProductReference_.brand)), cb.asc(root.get(ProductReference_.description)));
        TypedQuery<ProductReference> q = em.createQuery(cq);
        return q.getResultList();
    }
}
