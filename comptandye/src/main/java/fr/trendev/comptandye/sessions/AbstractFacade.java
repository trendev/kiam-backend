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

    public E find(P id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<E> findAll() {
        EntityManager em = this.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
//        Root<E> root = cq.from(entityClass);
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

//    public List<E> findRange(int startPosition, int size) {
//        return findRange(startPosition, size, null);
//    }
//
//    public List<E> findRange(int startPosition, int size, String entityGraph) {
//        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        cq.select(cq.from(entityClass));
//        Query q = getEntityManager().createQuery(cq);
//        q.setMaxResults(size);
//        q.setFirstResult(startPosition);
//        if (entityGraph != null) {
//            q.setHint("javax.persistence.loadgraph", getEntityManager().
//                    getEntityGraph(entityGraph));
//        }
//        return q.getResultList();
//    }
    public Long count() {

        EntityManager em = this.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(entityClass)));
        TypedQuery<Long> q = em.createQuery(cq);

        return q.getSingleResult();
    }
}
