/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.UserGroup;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author jsie
 */
@Stateless
public class UserGroupFacade {

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    public List<UserGroup> findAll() {
        CriteriaQuery<UserGroup> cq = em.getCriteriaBuilder().createQuery(
                UserGroup.class);
        cq.select(cq.from(UserGroup.class));
        return em.createQuery(cq).getResultList();
    }

}
