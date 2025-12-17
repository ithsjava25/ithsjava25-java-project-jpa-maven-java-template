package org.example;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Long getLoanId() {
        return loanId;
    }

    @ManyToOne @JoinColumn (name = "loan_user")
    private User user;

    @OneToOne(mappedBy = "loan")
    private Book book;
}
