package org.example;

import jakarta.persistence.EntityManagerFactory;
import org.example.repo.*;

public class App {
    public static void main(String[] args) {
        ItunesApiClient apiClient = new ItunesApiClient();
        SongRepository songRepo = new SongRepositoryImpl();
        AlbumRepository albumRepo = new AlbumRepositoryImpl();
        ArtistRepository artistRepo = new ArtistRepositoryImpl();

        try {
            EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();
            if (!emf.isOpen()) {
                throw new IllegalStateException("EntityManagerFactory is not open");
            }
            DatabaseInitializer initializer = new DatabaseInitializer(apiClient, songRepo, albumRepo, artistRepo);
            initializer.init();
            System.out.println("Database initialization completed successfully");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
