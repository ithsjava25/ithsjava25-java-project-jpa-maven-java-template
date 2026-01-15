package persistence.repository;

import javax.persistence.EntityManager;

public class TransactionHelper {
    private final EntityManager em;

    public TransactionHelper(EntityManager em) {
        this.em = em;
    }

    public void executeInTransaction(Runnable operation) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            operation.run();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Transaction failed: " + e.getMessage());
            throw e;
        }
    }
}
