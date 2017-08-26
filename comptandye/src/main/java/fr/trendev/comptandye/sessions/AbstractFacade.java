package fr.trendev.comptandye.sessions;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class AbstractFacade<E, P> {

    private final Class<E> entityClass;

    public AbstractFacade(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(E entity) {
        getEntityManager().persist(entity);
    }

    public E edit(E entity) {
        return getEntityManager().merge(entity);
    }

    public void remove(E entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public E find(P key) {
        return getEntityManager().find(entityClass, key);
    }

    public List<E> findAll() {
        EntityManager em = this.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
//        Root<E> root = cq.from(entityClass);
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    public Long count() {

        EntityManager em = this.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(entityClass)));
        TypedQuery<Long> q = em.createQuery(cq);

        return q.getSingleResult();
    }

    public Object getIdentifier(E entity) throws IllegalArgumentException {
        return getEntityManager().getEntityManagerFactory().
                getPersistenceUnitUtil().getIdentifier(entity);
    }
}
