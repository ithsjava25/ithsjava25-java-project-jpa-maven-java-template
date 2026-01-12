package org.example;

import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
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

    public boolean loanBook(Long bookId, Long userId) {

        if (isBookLoaned(bookId)) {
            return false;
        }

        User user = em.find(User.class, userId);
        Book book = em.find(Book.class, bookId);

        Loan loan = new Loan();

        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(ZonedDateTime.now());
        loan.setReturnDate(null);
        em.persist(loan);
        return true;
    }
}
