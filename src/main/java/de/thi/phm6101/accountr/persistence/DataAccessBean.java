package de.thi.phm6101.accountr.persistence;


import de.thi.phm6101.accountr.domain.AbstractEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

@Stateless
public class DataAccessBean {

    private static final Logger LOGGER = LogManager.getLogger(DataAccessBean.class);

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    public <T extends AbstractEntity> T get(Class<T> clazz, long id) {
        return em.find(clazz, id);
    }

    public <T extends AbstractEntity> List<T> getAll(Class<T> clazz) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        return em.createQuery(query.select(query.from(clazz))).getResultList();
    }

    public <T extends AbstractEntity> List<T> namedQuery(Class<T> clazz, String queryName, Map<String, Object> parameters) {
        TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
        for (Map.Entry<String, Object> parameter : parameters.entrySet())
        {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        return query.getResultList();
    }

    public <T extends AbstractEntity> void insert(T t) {
        em.persist(t);
    }

    public <T extends AbstractEntity> T update(T t) {
        return em.merge(t);
    }

    public <T extends AbstractEntity> void delete(T t) {
        em.remove(em.contains(t) ? t : em.merge(t));
    }

}