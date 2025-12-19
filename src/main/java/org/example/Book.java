package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private String genre;
    private String description;
    private String isbn;
    private int publishYear;


    public Book() {
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return bookId;
    }
    @ManyToMany @JoinTable(name = "book_author",
    joinColumns = @JoinColumn(name="bookId"))
    private List<Author> authors = new ArrayList<>();

    @ManyToMany(mappedBy = "books")
    private List<Genre> genres = new ArrayList<>();

    @OneToOne @JoinColumn(name = "loaned_book")
    private Loan loan;

}
