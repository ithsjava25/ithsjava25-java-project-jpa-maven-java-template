package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {

    static void main(String[] args) {
        EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("myPU");

        emf.close();
    }
}
