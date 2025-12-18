package org.example;

import org.example.util.JPAUtil;

public class App {

    static void main(String[] args) {

        JPAUtil.inTransaction(em -> {
            System.out.println("Database schema initialized");
        });
    }
}
