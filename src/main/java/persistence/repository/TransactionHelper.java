package persistence.repository;

import javax.persistence.EntityManager;

public class TransactionHelper {
    private final EntityManager em;

    public TransactionHelper(EntityManager em) {
        this.em = em;
    }

    public void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    public void commitTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    public void executeInTransaction(Runnable operation) {
        try {
            beginTransaction();
            operation.run();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }
}
