package org.example;

import jakarta.persistence.EntityManager;
import java.util.List;

public class LoanServices {

    final private EntityManager em;

    public LoanServices(EntityManager em) {
        this.em = em;
    }

    public boolean isBookLoaned(Long bookId) {

        List<Loan> loans = em.createQuery(
            "SELECT l FROM Loan l WHERE l.bookId = :bookId AND l.returnDate IS NULL",
            Loan.class
        )
        .setParameter("bookId", bookId)
            .getResultList();

        if (loans.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
