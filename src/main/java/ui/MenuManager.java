package org.example.ui;

import persistence.repository.ArtistRepository;
import persistence.repository.AlbumRepository;
import persistence.repository.SongRepository;
import persistence.repository.PlaylistRepository;
import org.example.model.*;
import java.util.List;
import java.util.Scanner;

public class MenuManager {
    private Scanner scanner;
    private ArtistRepository artistRepo;
    private AlbumRepository albumRepo;
    private SongRepository songRepo;
    private PlaylistRepository playlistRepo;

    public MenuManager(Scanner scanner,
                       ArtistRepository artistRepo,
                       AlbumRepository albumRepo,
                       SongRepository songRepo,
                       PlaylistRepository playlistRepo) {
        this.scanner = scanner;
        this.artistRepo = artistRepo;
        this.albumRepo = albumRepo;
        this.songRepo = songRepo;
        this.playlistRepo = playlistRepo;
    }

    //HUVUDMETOD
    public void start() {
        System.out.println("🎵 VÄLKOMMEN TILL MUSIKAPPEN 🎵");

        boolean running = true;
        while (running) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("=== HUVUDMENY ===");
            System.out.println("1. 🎤 Artister");
            System.out.println("2. 💿 Album");
            System.out.println("3. 🎶 Låtar");
            System.out.println("4. 📋 Spellistor");
            System.out.println("5. 🔍 Sök");
            System.out.println("0. ❌ Avsluta");
            System.out.println("=".repeat(40));

            int choice = InputValidator.getIntInput(scanner, "Val: ", 0, 5);

            switch (choice) {
                case 1: artistMenu(); break;
                case 2: albumMenu(); break;
                case 3: songMenu(); break;
                case 4: playlistMenu(); break;
                case 5: searchMenu(); break;
                case 0:
                    running = false;
                    System.out.println("\n👋 Tack för idag!");
                    break;
            }
        }
    }

    // ARTIST-MENY
    private void artistMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== ARTIST-MENY ===");
            System.out.println("1. Visa alla artister");
            System.out.println("2. Lägg till ny artist");
            System.out.println("3. Visa artists album");
            System.out.println("4. Ta bort artist");
            System.out.println("0. ← Tillbaka till huvudmeny");

            int choice = InputValidator.getIntInput(scanner, "Val: ", 0, 4);

            switch (choice) {
                case 1: showAllArtists(); break;
                case 2: addArtist(); break;
                case 3: showArtistAlbums(); break;
                case 4: deleteArtist(); break;
                case 0: inMenu = false; break;
            }
        }
    }

    // LÅT-MENY (med borttagning)
    private void songMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== LÅT-MENY ===");
            System.out.println("1. Visa alla låtar");
            System.out.println("2. Lägg till ny låt");
            System.out.println("3. Ta bort låt");
            System.out.println("0. ← Tillbaka till huvudmeny");

            int choice = InputValidator.getIntInput(scanner, "Val: ", 0, 3);

            switch (choice) {
                case 1: showAllSongs(); break;
                case 2: addSong(); break;
                case 3: deleteSong(); break;
                case 0: inMenu = false; break;
            }
        }
    }

    // METOD FÖR ATT TA BORT LÅT
    private void deleteSong() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("TA BORT LÅT");
        System.out.println("=".repeat(40));

        List<Song> songs = songRepo.findAll();
        if (songs.isEmpty()) {
            System.out.println("⚠️  Inga låtar finns att ta bort.");
            return;
        }

        DisplayHelper.printSongList(songs);

        System.out.print("\nAnge ID på låten du vill ta bort: ");
        Long songId;
        try {
            songId = scanner.nextLong();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("❌ Ogiltigt ID. Ange ett nummer.");
            scanner.nextLine();
            return;
        }

        Song songToDelete = songRepo.findById(songId);
        if (songToDelete == null) {
            System.out.println("❌ Ingen låt hittades med ID: " + songId);
            return;
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.println("BEKRÄFTA BORTTAGNING");
        System.out.println("=".repeat(40));
        System.out.println("Låt: " + songToDelete.getTitle());
        System.out.println("Artist: " + songToDelete.getAlbum().getArtist().getName());
        System.out.println("Album: " + songToDelete.getAlbum().getTitle());
        System.out.println("Längd: " + DisplayHelper.formatDuration(songToDelete.getDuration()));

        System.out.print("\n⚠️  Är du SÄKER på att du vill ta bort denna låt? (JA/nej): ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("JA")) {
            boolean success = songRepo.deleteSong(songId);
            if (success) {
                System.out.println("✅ Låt togs bort!");
            } else {
                System.out.println("❌ Kunde inte ta bort låten.");
            }
        } else {
            System.out.println("❌ Avbruten. Låten togs INTE bort.");
        }
    }

    private void showAllArtists() {
        List<Artist> artists = artistRepo.findAll();
        DisplayHelper.printArtistList(artists);
    }

    private void addArtist() {
        System.out.print("Artistens namn: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Namn får inte vara tomt.");
            return;
        }

        Artist artist = new Artist(name);
        artistRepo.save(artist);
        System.out.println("✅ Artist sparad!");
    }

    private void showArtistAlbums() {
        Long artistId = InputValidator.getLongInput(scanner, "Artist ID: ");
        List<Album> albums = albumRepo.findByArtistId(artistId);

        if (albums.isEmpty()) {
            System.out.println("ℹ️  Artist har inga album.");
        } else {
            DisplayHelper.printAlbumList(albums);
        }
    }

    private void deleteArtist() {
        Long artistId = InputValidator.getLongInput(scanner, "Artist ID att ta bort: ");

        System.out.print("⚠️  Detta tar också bort artistens alla album och låtar. Bekräfta? (JA/nej): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("JA")) {
            boolean success = artistRepo.deleteArtist(artistId);
            System.out.println(success ? "✅ Artist borttagen!" : "❌ Misslyckades");
        } else {
            System.out.println("❌ Avbruten.");
        }
    }

    private void showAllSongs() {
        List<Song> songs = songRepo.findAll();
        DisplayHelper.printSongList(songs);
    }

    private void addSong() {
        System.out.print("Låtens titel: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("❌ Titel får inte vara tom.");
            return;
        }

        int duration = InputValidator.getIntInput(scanner, "Längd i sekunder: ", 1, 3600);
        Long albumId = InputValidator.getLongInput(scanner, "Album ID: ");

        Album album = albumRepo.findById(albumId);
        if (album == null) {
            System.out.println("❌ Album hittades inte.");
            return;
        }

        Song song = new Song(title, duration);
        song.setAlbum(album);
        songRepo.save(song);

        System.out.println("✅ Låt sparad!");
    }

    private void albumMenu() {
        System.out.println("\nAlbum-menyn - implementeras senare");
    }

    private void playlistMenu() {
        System.out.println("\nSpellista-menyn - implementeras senare");
    }

    private void searchMenu() {
        System.out.println("\nSök-menyn - implementeras senare");
    }
}
