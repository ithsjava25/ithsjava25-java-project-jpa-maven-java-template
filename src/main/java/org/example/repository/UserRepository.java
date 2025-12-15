package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.entity.User;

public class UserRepository {
    private final EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public void save(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    public void delete(User user) {
        try {
            em.getTransaction().begin();
            em.remove(user);
        }
        finally {
            em.close();
        }
    }

}
