package org.example;

import persistence.repository.ArtistRepository;
import persistence.repository.AlbumRepository;
import persistence.repository.SongRepository;
import persistence.repository.PlaylistRepository;
import org.example.ui.MenuManager;
import javax.persistence.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎵 Startar Musikappen...");

        try {
            // Skapa EntityManagerFactory
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("musicPU");
            System.out.println("✅ Ansluten till databasen");

            // Skapa EntityManager
            EntityManager em = emf.createEntityManager();

            // Skapa Scanner för användarinput
            Scanner scanner = new Scanner(System.in);

            // Skapa alla repositories
            System.out.println("🔄 Skapar repositories...");
            ArtistRepository artistRepo = new ArtistRepository(em);
            AlbumRepository albumRepo = new AlbumRepository(em);
            SongRepository songRepo = new SongRepository(em);
            PlaylistRepository playlistRepo = new PlaylistRepository(em);

            // Skapa MenuManager och starta huvudmenyn
            MenuManager menuManager = new MenuManager(scanner, artistRepo, albumRepo, songRepo, playlistRepo);

            // Starta applikationen
            menuManager.start();

            // Stäng alla resurser
            System.out.println("👋 Stänger applikationen...");
            scanner.close();
            em.close();
            emf.close();

        } catch (Exception e) {
            System.err.println("❌ Ett fel uppstod vid start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
