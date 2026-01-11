package org.example;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private ZonedDateTime loanDate;
    private ZonedDateTime returnDate;

    public Loan(){
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book){
        this.book = book;
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
