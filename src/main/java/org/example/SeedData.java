package org.example;

//todo:
//1. läsa TSV-fil: seed-books.tsv
//2. skapa Author, Book, Genre-objekt
//3. koppla dem
//4. köra persist() + commit() → då hamnar datan i våran DB.f

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class SeedData {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("library_system");
        EntityManager em = emf.createEntityManager();

        System.out.println("JPA ok ✅");

        em.close();
        emf.close();
    }
}
