package org.example;

import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;

public class LoanServices {

    final private EntityManager em;

    public LoanServices(EntityManager em) {
        this.em = em;
    }

    // Kolla om en bok är utlånad
    public boolean isBookLoaned(Long bookId) {

        List<Loan> loans = em.createQuery(
            "SELECT l FROM Loan l WHERE l.book.bookId = :bookId AND l.returnDate IS NULL",
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

    // Låna en bok
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

    // Lämna tillbak en bok
    public boolean returnBook(User user, Book book) {

        Loan loan = em.createQuery(
            "SELECT l FROM Loan l WHERE l.user = :user AND l.book = :book AND l.returnDate IS NULL",
            Loan.class
        )
            .setParameter("user", user)
            .setParameter("book", book)
            .getResultStream()
            .findFirst()
            .orElse(null);

        if (loan == null) {
            return false;
        }

        em.getTransaction().begin();

        loan.setReturnDate(ZonedDateTime.now());
        book.setLoan(null);
        em.getTransaction().commit();
        return true;
    }
}
