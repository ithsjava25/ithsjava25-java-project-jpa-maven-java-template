package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory emf;

    static { emf = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            if (emf.isOpen()) { emf.close(); } }));
    }
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
