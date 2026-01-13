package org.example;

import org.example.model.Artist;
import org.example.model.Album;
import org.example.model.Song;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    static EntityManager em;
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        // Starta databas
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("musicPU");
        em = emf.createEntityManager();

        System.out.println(" VÄLKOMMEN TILL MUSIKAPPEN ");

        // meny
        boolean running = true;
        while(running) {
            System.out.println("\n=== HUVUDMENY ===");
            System.out.println("1. Lägg till artist");
            System.out.println("2. Visa alla artister");
            System.out.println("3. Lägg till album");
            System.out.println("4. Lägg till låt");
            System.out.println("5. Visa all musik");
            System.out.println("0. Avsluta");
            System.out.print("Välj: ");

            int val = scan.nextInt();
            scan.nextLine(); // Rensa

            switch(val) {
                case 1: addArtist(); break;
                case 2: showArtists(); break;
                case 3: addAlbum(); break;
                case 4: addSong(); break;
                case 5: showAllMusic(); break;
                case 0:
                    running = false;
                    System.out.println(" Hej då!");
                    break;
                default:
                    System.out.println(" Ogiltigt val!");
            }
        }

        em.close();
        emf.close();
        scan.close();
    }

    // 1. lägg till artist
    static void addArtist() {
        System.out.print("Artist namn: ");
        String name = scan.nextLine();

        em.getTransaction().begin();
        Artist artist = new Artist(name);
        em.persist(artist);
        em.getTransaction().commit();

        System.out.println(" Artist tillagd!");
    }

    // 2. visa alla artister
    static void showArtists() {
        List<Artist> artists = em.createQuery("SELECT a FROM Artist a", Artist.class).getResultList();

        System.out.println("\n=== ALLA ARTISTER ===");
        for(Artist a : artists) {
            System.out.println("- " + a.getName() + " (ID: " + a.getId() + ")");
        }
        System.out.println("=====================");
    }

    // 3. lägg till album
    static void addAlbum() {
        // visa alla artister först
        List<Artist> artists = em.createQuery("SELECT a FROM Artist a", Artist.class).getResultList();

        if(artists.isEmpty()) {
            System.out.println(" Inga artister finns. Lägg till artist först. ");
            return;
        }

        System.out.println("\n--- Välj artist ---");
        for(int i = 0; i < artists.size(); i++) {
            System.out.println(i + ". " + artists.get(i).getName());
        }
        System.out.print("Välj artist (nummer): ");
        int artistIndex = scan.nextInt();
        scan.nextLine();

        if(artistIndex < 0 || artistIndex >= artists.size()) {
            System.out.println(" Ogiltigt val!");
            return;
        }

        Artist selectedArtist = artists.get(artistIndex);

        System.out.print("Album titel: ");
        String title = scan.nextLine();
        System.out.print("Utgivningsår: ");
        int year = scan.nextInt();
        scan.nextLine();

        em.getTransaction().begin();
        Album album = new Album(title, year);
        album.setArtist(selectedArtist);
        em.persist(album);
        em.getTransaction().commit();

        System.out.println(" Album tillagt!");
    }

    // 4. lägg till låt
    static void addSong() {
        // visa alla album först
        List<Album> albums = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();

        if(albums.isEmpty()) {
            System.out.println(" Inga album finns. Lägg till album först.");
            return;
        }

        System.out.println("\n--- Välj album ---");
        for(int i = 0; i < albums.size(); i++) {
            Album a = albums.get(i);
            System.out.println(i + ". " + a.getTitle() + " - " + a.getArtist().getName());
        }
        System.out.print("Välj album (nummer): ");
        int albumIndex = scan.nextInt();
        scan.nextLine();

        if(albumIndex < 0 || albumIndex >= albums.size()) {
            System.out.println(" Ogiltigt val!");
            return;
        }

        Album selectedAlbum = albums.get(albumIndex);

        System.out.print("Låt titel: ");
        String title = scan.nextLine();
        System.out.print("Längd i sekunder: ");
        int duration = scan.nextInt();
        scan.nextLine();

        em.getTransaction().begin();
        Song song = new Song(title, duration);
        song.setAlbum(selectedAlbum);
        em.persist(song);
        em.getTransaction().commit();

        System.out.println(" Låt tillagd!");
    }

    // 5. visa all musik
    static void showAllMusic() {
        List<Artist> artists = em.createQuery("SELECT a FROM Artist a", Artist.class).getResultList();

        System.out.println("\n=== HELA MUSIKBIBLIOTEKET ===");

        if(artists.isEmpty()) {
            System.out.println("Ingen musik finns ännu.");
            return;
        }

        for(Artist artist : artists) {
            System.out.println("\n ARTIST: " + artist.getName());

            if(artist.getAlbums().isEmpty()) {
                System.out.println("   (Inga album)");
                continue;
            }

            for(Album album : artist.getAlbums()) {
                System.out.println("    ALBUM: " + album.getTitle() + " (" + album.getYear() + ")");

                if(album.getSongs().isEmpty()) {
                    System.out.println("      (Inga låtar)");
                    continue;
                }

                for(Song song : album.getSongs()) {
                    int minutes = song.getDuration() / 60;
                    int seconds = song.getDuration() % 60;
                    System.out.println("      🎶 " + song.getTitle() + " (" + minutes + ":" + String.format("%02d", seconds) + ")");
                }
            }
        }
        System.out.println("==============================");
    }
}
