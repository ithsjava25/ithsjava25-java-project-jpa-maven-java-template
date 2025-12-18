package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;

public class JPAUtil {

    private static final EntityManagerFactory emf;

    static {
        emf = Persistence.createEntityManagerFactory("myPU");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (emf.isOpen()) {
                emf.close();
            }
        }));
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void inTransaction(Consumer<EntityManager> work) {
        EntityManager em = getEntityManager();
        try {
            var tx = em.getTransaction();
            tx.begin();
            work.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
