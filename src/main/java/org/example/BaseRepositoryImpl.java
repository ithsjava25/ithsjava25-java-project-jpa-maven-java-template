package org.example;

import jakarta.persistence.EntityManager;

import java.util.Optional;

public class BaseRepositoryImpl<T extends org.example.BaseEntity> implements Repository<T> {

    protected EntityManager em;
    protected Class<T> entityClass;


    public BaseRepositoryImpl(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }


    @Override
    public T save(T entity) {
        if (entity == null) {
                throw new IllegalArgumentException("Entity cannot be null");
            }
            if (entity.getId() == null) {
                em.persist(entity);
                return entity;
            } else {
            return em.merge(entity);
        }
    }



    public EntityManager  getEntityManager() {
        return em;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public void deleteById(Long id) {
        T entity = em.find(entityClass, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    public Iterable<T> findAll() {
        return em.createQuery(
            "select e from " + entityClass.getSimpleName() + " e", entityClass
        ).getResultList();
    }

    @Override
    public boolean existsById(Long id) {
        Long count = em.createQuery(
            "select count(e) from " + entityClass.getSimpleName() + " e where e.id = :id",
            Long.class
        ).setParameter("id", id).getSingleResult();
        return count > 0;

    }

    @Override
    public long count() {

        return em.createQuery("select count(e) from " + entityClass.getSimpleName() + " e", Long.class).getSingleResult();
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void clear() {
        em.clear();
    }

}
