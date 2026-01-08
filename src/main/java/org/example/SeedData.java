package org.example;

//todo:
//1. läsa TSV-fil: seed-books.tsv
//2. skapa Author, Book, Genre-objekt
//3. koppla dem
//4. köra persist() + commit() → då hamnar datan i våran DB.f

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SeedData {

    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("library_system");
        EntityManager em = emf.createEntityManager();

        System.out.println("Seeding startar...");

        // Starta transaktion (behövs för att kunna spara)
        em.getTransaction().begin();

        int saved = 0;

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(
                SeedData.class.getResourceAsStream("/seed-books.tsv"),
                StandardCharsets.UTF_8
            )
        )) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] c = line.split("\t");
                if (c.length < 7) {
                    throw new IllegalArgumentException("För få kolumner i raden: " + line);
                }

                //todo:
                // TSV: title, description, isbn, publishYear, firstName, lastName, genre
                String title = c[0].trim();
                String description = c[1].trim();
                String isbn = c[2].trim();
                int publishYear = Integer.parseInt(c[3].trim());
                String firstName = c[4].trim();
                String lastName = c[5].trim();
                String genreName = c[6].trim();

                //todo:
                // 1) Hitta/skapa Author
                Author author = em.createQuery(
                        "SELECT a FROM Author a WHERE a.firstName = :fn AND a.lastName = :ln",
                        Author.class)
                    .setParameter("fn", firstName)
                    .setParameter("ln", lastName)
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Author a = new Author();
                        a.setFirstName(firstName);
                        a.setLastName(lastName);
                        em.persist(a);
                        return a;
                    });

                //todo:
                // 2) Hitta/skapa Genre (OBS: genreName)
                Genre genre = em.createQuery(
                        "SELECT g FROM Genre g WHERE g.genreName = :name",
                        Genre.class)
                    .setParameter("name", genreName)
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Genre g = new Genre();
                        g.setGenre(genreName);
                        em.persist(g);
                        return g;
                    });

                //todo:
                // 3) Skapa Book
                Book book = em.createQuery(
                        "SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class)
                    .setParameter("isbn", isbn)
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Book b = new Book();
                        b.setTitle(title);
                        b.setDescription(description);
                        b.setIsbn(isbn);
                        b.setPublishYear(publishYear);
                        em.persist(b);
                        return b;
                    });

                //todo:
                // 4) Koppla relationer (fyller book_author + book_genre)
                book.getAuthors().add(author);
                book.getGenres().add(genre);
                genre.getBooks().add(book);

                //todo:
                // 5) Persist book
                em.persist(book);

                saved++;
                System.out.println("Sparade 1 bok: " + title);

                break; // <-- smoke test: bara första raden först
            }
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Seed klart (Saved=" + saved + ")");
    }
}
