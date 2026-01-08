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

        System.out.println("Läser TSV...");

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(
                SeedData.class.getResourceAsStream("/seed-books.tsv"),
                StandardCharsets.UTF_8
            )
        )) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                System.out.println(line);
                count++;
            }
            System.out.println("Rader: " + count + " ✅");
        }

        em.close();
        emf.close();
    }
}

