package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;
import java.util.Date;

@Entity
public class Loan {

    @Id
    private Long loanId;

    private ZonedDateTime loanDate;
    private ZonedDateTime returnDate;
    private Long userId;
    private Long bookId;

    public Loan(){
    }


    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ZonedDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public ZonedDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(ZonedDateTime loanDate) {
        this.loanDate = loanDate;
    }
}
