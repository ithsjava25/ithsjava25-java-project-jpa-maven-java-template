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

    // HUVUDMETOD
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
            System.out.println("0. ❌ Avsluta");
            System.out.println("=".repeat(40));

            int choice = InputValidator.getIntInput(scanner, "Val: ", 0, 4);

            switch (choice) {
                case 1: artistMenu(); break;
                case 2: albumMenu(); break;
                case 3: songMenu(); break;
                case 4: playlistMenu(); break;
                case 0:
                    running = false;
                    System.out.println("\n👋 Tack för idag!");
                    break;
            }
        }
    }

    // ========== ARTIST-MENY ==========
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

    private void showAllArtists() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("ALLA ARTISTER");
        System.out.println("=".repeat(40));

        List<Artist> artists = artistRepo.findAll();
        if (artists.isEmpty()) {
            System.out.println("Inga artister finns.");
            return;
        }

        System.out.printf("%-4s %-30s %-15s%n", "ID", "NAMN", "ANTAL ALBUM");
        System.out.println("-".repeat(50));

        for (Artist artist : artists) {
            System.out.printf("%-4d %-30s %-15d%n",
                artist.getId(),
                truncate(artist.getName(), 28),
                artist.getAlbums().size());
        }
    }

    private void addArtist() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("LÄGG TILL NY ARTIST");
        System.out.println("=".repeat(40));

        String name = InputValidator.getNonEmptyString(scanner, "Artistens namn: ");

        try {
            Artist artist = new Artist(name);
            artistRepo.save(artist);
            System.out.println("✅ Artist sparad med ID: " + artist.getId());
        } catch (Exception e) {
            System.out.println("❌ Kunde inte spara artisten: " + e.getMessage());
        }
    }

    private void showArtistAlbums() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("VISA ARTISTS ALBUM");
        System.out.println("=".repeat(40));

        // Visa alla artister först
        List<Artist> artists = artistRepo.findAll();
        if (artists.isEmpty()) {
            System.out.println("Inga artister finns.");
            return;
        }

        System.out.println("\n--- Tillgängliga artister ---");
        for (Artist artist : artists) {
            System.out.println(artist.getId() + ". " + artist.getName() +
                " (" + artist.getAlbums().size() + " album)");
        }

        Long artistId = InputValidator.getLongInput(scanner, "\nAnge Artist ID: ");
        Artist artist = artistRepo.findById(artistId);

        if (artist == null) {
            System.out.println("❌ Artist hittades inte.");
            return;
        }

        List<Album> albums = albumRepo.findByArtistId(artistId);

        System.out.println("\n" + "=".repeat(50));
        System.out.println("ALBUM AV: " + artist.getName().toUpperCase());
        System.out.println("=".repeat(50));

        if (albums.isEmpty()) {
            System.out.println("Denna artist har inga album än.");
        } else {
            System.out.printf("%-4s %-30s %-10s%n", "ID", "ALBUM", "ÅR");
            System.out.println("-".repeat(50));

            for (Album album : albums) {
                System.out.printf("%-4d %-30s %-10d%n",
                    album.getId(),
                    truncate(album.getTitle(), 28),
                    album.getReleaseYear());
            }
        }
    }

    private void deleteArtist() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("TA BORT ARTIST");
        System.out.println("=".repeat(40));

        Long artistId = InputValidator.getLongInput(scanner, "Artist ID att ta bort: ");
        Artist artist = artistRepo.findById(artistId);

        if (artist == null) {
            System.out.println("❌ Artist hittades inte.");
            return;
        }

        System.out.println("\n⚠️  VARNING: Detta tar också bort artistens alla album och låtar!");
        System.out.println("Artist att ta bort: " + artist.getName());
        System.out.println("Antal album: " + artist.getAlbums().size());

        System.out.print("\nÄr du SÄKER på att du vill ta bort denna artist? (skriv 'JA' för att bekräfta): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("JA")) {
            boolean success = artistRepo.deleteArtist(artistId);
            System.out.println(success ? "✅ Artist borttagen!" : "❌ Misslyckades att ta bort artisten.");
        } else {
            System.out.println("❌ Avbruten. Ingen artist togs bort.");
        }
    }

    // ========== ALBUM-MENY ==========
    private void albumMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== ALBUM-MENY ===");
            System.out.println("1. Visa alla album");
            System.out.println("2. Lägg till nytt album");
            System.out.println("3. Visa albums låtar");
            System.out.println("4. Ta bort album");
            System.out.println("0. ← Tillbaka till huvudmeny");

            int choice = InputValidator.getIntInput(scanner, "Val: ", 0, 4);

            switch (choice) {
                case 1: showAllAlbums(); break;
                case 2: addAlbum(); break;
                case 3: showAlbumSongs(); break;
                case 4: deleteAlbum(); break;
                case 0: inMenu = false; break;
            }
        }
    }

    private void showAllAlbums() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ALLA ALBUM");
        System.out.println("=".repeat(50));

        List<Album> albums = albumRepo.findAll();
        if (albums.isEmpty()) {
            System.out.println("Inga album finns.");
            return;
        }

        System.out.printf("%-4s %-25s %-20s %-10s%n", "ID", "ALBUM", "ARTIST", "ÅR");
        System.out.println("-".repeat(60));

        for (Album album : albums) {
            String artistName = "Okänd artist";
            if (album.getArtist() != null) {
                artistName = album.getArtist().getName();
            }

            System.out.printf("%-4d %-25s %-20s %-10d%n",
                album.getId(),
                truncate(album.getTitle(), 23),
                truncate(artistName, 18),
                album.getReleaseYear());
        }
    }

    private void addAlbum() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("LÄGG TILL NYTT ALBUM");
        System.out.println("=".repeat(40));

        // Visa alla artister först
        List<Artist> artists = artistRepo.findAll();
        if (artists.isEmpty()) {
            System.out.println("❌ Inga artister finns. Lägg till en artist först.");
            return;
        }

        System.out.println("\n--- Välj artist ---");
        System.out.printf("%-4s %-30s%n", "ID", "ARTIST");
        System.out.println("-".repeat(35));

        for (Artist artist : artists) {
            System.out.printf("%-4d %-30s%n",
                artist.getId(),
                truncate(artist.getName(), 28));
        }

        Long artistId = InputValidator.getLongInput(scanner, "\nAnge Artist ID: ");
        Artist artist = artistRepo.findById(artistId);

        if (artist == null) {
            System.out.println("❌ Artist hittades inte.");
            return;
        }

        String title = InputValidator.getNonEmptyString(scanner, "Albumets titel: ");
        int year = InputValidator.getIntInput(scanner, "Utgivningsår: ", 1900, 2100);

        // Bekräfta
        System.out.println("\n" + "=".repeat(40));
        System.out.println("BEKRÄFTA ALBUM");
        System.out.println("=".repeat(40));
        System.out.println("Artist: " + artist.getName());
        System.out.println("Album: " + title);
        System.out.println("År: " + year);

        System.out.print("\nVill du spara detta album? (skriv 'JA' för att bekräfta): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("JA")) {
            System.out.println("❌ Avbruten. Albumet sparades inte.");
            return;
        }

        // Skapa och spara album
        try {
            Album album = new Album(title, year);
            album.setArtist(artist);
            albumRepo.save(album);
            System.out.println("✅ Album sparad med ID: " + album.getId());
        } catch (Exception e) {
            System.out.println("❌ Kunde inte spara albumet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlbumSongs() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("VISA ALBUMS LÅTAR");
        System.out.println("=".repeat(40));

        // Visa alla album först
        List<Album> albums = albumRepo.findAll();
        if (albums.isEmpty()) {
            System.out.println("Inga album finns.");
            return;
        }

        System.out.println("\n--- Tillgängliga album ---");
        System.out.printf("%-4s %-25s %-20s%n", "ID", "ALBUM", "ARTIST");
        System.out.println("-".repeat(50));

        for (Album album : albums) {
            String artistName = "Okänd";
            if (album.getArtist() != null) {
                artistName = album.getArtist().getName();
            }

            System.out.printf("%-4d %-25s %-20s%n",
                album.getId(),
                truncate(album.getTitle(), 23),
                truncate(artistName, 18));
        }

        Long albumId = InputValidator.getLongInput(scanner, "\nAnge Album ID: ");
        Album album = albumRepo.findById(albumId);

        if (album == null) {
            System.out.println("❌ Album hittades inte.");
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("LÅTAR PÅ: " + album.getTitle().toUpperCase());
        if (album.getArtist() != null) {
            System.out.println("ARTIST: " + album.getArtist().getName());
        }
        System.out.println("ÅR: " + album.getReleaseYear());
        System.out.println("=".repeat(50));

        List<Song> songs = songRepo.findByAlbumId(albumId);
        if (songs.isEmpty()) {
            System.out.println("Det här albumet har inga låtar än.");
        } else {
            System.out.printf("%-4s %-30s %-15s%n", "ID", "LÅT", "LÄNGD");
            System.out.println("-".repeat(50));

            for (Song song : songs) {
                System.out.printf("%-4d %-30s %-15s%n",
                    song.getId(),
                    truncate(song.getTitle(), 28),
                    formatDuration(song.getDuration()));
            }
        }
    }

    private void deleteAlbum() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("TA BORT ALBUM");
        System.out.println("=".repeat(40));

        Long albumId = InputValidator.getLongInput(scanner, "Album ID att ta bort: ");
        Album album = albumRepo.findById(albumId);

        if (album == null) {
            System.out.println("❌ Album hittades inte.");
            return;
        }

        System.out.println("\n⚠️  VARNING: Detta tar också bort albumets alla låtar!");
        System.out.println("Album att ta bort: " + album.getTitle());
        if (album.getArtist() != null) {
            System.out.println("Artist: " + album.getArtist().getName());
        }
        System.out.println("Antal låtar: " + album.getSongs().size());

        System.out.print("\nÄr du SÄKER på att du vill ta bort detta album? (skriv 'JA' för att bekräfta): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("JA")) {
            boolean success = albumRepo.deleteAlbum(albumId);
            System.out.println(success ? "✅ Album borttaget!" : "❌ Misslyckades att ta bort albumet.");
        } else {
            System.out.println("❌ Avbruten. Inget album togs bort.");
        }
    }

    // ========== LÅT-MENY ==========
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

    private void showAllSongs() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALLA LÅTAR");
        System.out.println("=".repeat(60));

        List<Song> songs = songRepo.findAll();
        if (songs.isEmpty()) {
            System.out.println("Inga låtar finns.");
            return;
        }

        System.out.printf("%-4s %-20s %-25s %-15s%n", "ID", "LÅT", "ALBUM", "LÄNGD");
        System.out.println("-".repeat(65));

        for (Song song : songs) {
            String albumTitle = "Okänt album";
            if (song.getAlbum() != null) {
                albumTitle = song.getAlbum().getTitle();
            }

            System.out.printf("%-4d %-20s %-25s %-15s%n",
                song.getId(),
                truncate(song.getTitle(), 18),
                truncate(albumTitle, 23),
                formatDuration(song.getDuration()));
        }
    }

    private void addSong() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("LÄGG TILL NY LÅT");
        System.out.println("=".repeat(40));

        // Visa alla album först
        List<Album> albums = albumRepo.findAll();
        if (albums.isEmpty()) {
            System.out.println("❌ Inga album finns. Lägg till ett album först.");
            return;
        }

        System.out.println("\n--- Välj album ---");
        System.out.printf("%-4s %-25s %-20s %-10s%n", "ID", "ALBUM", "ARTIST", "ÅR");
        System.out.println("-".repeat(60));

        for (Album album : albums) {
            String artistName = "Okänd";
            if (album.getArtist() != null) {
                artistName = album.getArtist().getName();
            }

            System.out.printf("%-4d %-25s %-20s %-10d%n",
                album.getId(),
                truncate(album.getTitle(), 23),
                truncate(artistName, 18),
                album.getReleaseYear());
        }

        Long albumId = InputValidator.getLongInput(scanner, "\nAnge Album ID: ");
        Album album = albumRepo.findById(albumId);

        if (album == null) {
            System.out.println("❌ Album hittades inte.");
            return;
        }

        String title = InputValidator.getNonEmptyString(scanner, "Låtens titel: ");
        int duration = InputValidator.getIntInput(scanner, "Längd i sekunder: ", 1, 3600);

        // Bekräfta
        System.out.println("\n" + "=".repeat(40));
        System.out.println("BEKRÄFTA LÅT");
        System.out.println("=".repeat(40));
        System.out.println("Låt: " + title);
        System.out.println("Längd: " + formatDuration(duration));
        System.out.println("Album: " + album.getTitle());
        if (album.getArtist() != null) {
            System.out.println("Artist: " + album.getArtist().getName());
        }

        System.out.print("\nVill du spara denna låt? (skriv 'JA' för att bekräfta): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("JA")) {
            System.out.println("❌ Avbruten. Låten sparades inte.");
            return;
        }

        // Skapa och spara låten
        try {
            Song song = new Song(title, duration);
            song.setAlbum(album);
            songRepo.save(song);
            System.out.println("✅ Låt sparad med ID: " + song.getId());
        } catch (Exception e) {
            System.out.println("❌ Kunde inte spara låten: " + e.getMessage());
        }
    }

    private void deleteSong() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("TA BORT LÅT");
        System.out.println("=".repeat(40));

        List<Song> songs = songRepo.findAll();
        if (songs.isEmpty()) {
            System.out.println("⚠️  Inga låtar finns att ta bort.");
            return;
        }

        System.out.println("\n--- Tillgängliga låtar ---");
        System.out.printf("%-4s %-25s %-20s%n", "ID", "LÅT", "ALBUM");
        System.out.println("-".repeat(50));

        for (Song song : songs) {
            String albumTitle = "Okänt album";
            if (song.getAlbum() != null) {
                albumTitle = song.getAlbum().getTitle();
            }

            System.out.printf("%-4d %-25s %-20s%n",
                song.getId(),
                truncate(song.getTitle(), 23),
                truncate(albumTitle, 18));
        }

        Long songId = InputValidator.getLongInput(scanner, "\nAnge ID på låten du vill ta bort: ");
        Song song = songRepo.findById(songId);

        if (song == null) {
            System.out.println("❌ Ingen låt hittades med ID: " + songId);
            return;
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.println("BEKRÄFTA BORTTAGNING");
        System.out.println("=".repeat(40));
        System.out.println("Låt att ta bort: " + song.getTitle());
        System.out.println("Längd: " + formatDuration(song.getDuration()));

        if (song.getAlbum() != null) {
            System.out.println("Album: " + song.getAlbum().getTitle());
            if (song.getAlbum().getArtist() != null) {
                System.out.println("Artist: " + song.getAlbum().getArtist().getName());
            }
        }

        System.out.print("\n⚠️  Är du SÄKER på att du vill ta bort denna låt? (skriv 'JA' för att bekräfta): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("JA")) {
            boolean success = songRepo.deleteSong(songId);
            System.out.println(success ? "✅ Låt borttagen!" : "❌ Kunde inte ta bort låten.");
        } else {
            System.out.println("❌ Avbruten. Låten togs INTE bort.");
        }
    }

    // ========== SPELLISTA-MENY ==========
    private void playlistMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== SPELLISTA-MENY ===");
            System.out.println("1. Visa alla spellistor");
            System.out.println("2. Skapa ny spellista");
            System.out.println("3. Lägg till låt i spellista");
            System.out.println("4. Ta bort låt från spellista");
            System.out.println("5. Visa spellista");
            System.out.println("6. Ta bort spellista");
            System.out.println("0. ← Tillbaka till huvudmeny");

            int choice = InputValidator.getIntInput(scanner, "Val: ", 0, 6);

            switch (choice) {
                case 1: showAllPlaylists(); break;
                case 2: createPlaylist(); break;
                case 3: addSongToPlaylist(); break;
                case 4: removeSongFromPlaylist(); break;
                case 5: showPlaylistDetails(); break;
                case 6: deletePlaylist(); break;
                case 0: inMenu = false; break;
            }
        }
    }

    private void showAllPlaylists() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("ALLA SPELLISTOR");
        System.out.println("=".repeat(40));

        List<Playlist> playlists = playlistRepo.findAll();
        if (playlists.isEmpty()) {
            System.out.println("Inga spellistor finns.");
            return;
        }

        System.out.printf("%-4s %-25s %-15s%n", "ID", "NAMN", "ANTAL LÅTAR");
        System.out.println("-".repeat(45));

        for (Playlist playlist : playlists) {
            System.out.printf("%-4d %-25s %-15d%n",
                playlist.getId(),
                truncate(playlist.getName(), 23),
                playlist.getEntries().size());
        }
    }

    private void createPlaylist() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("SKAPA NY SPELLISTA");
        System.out.println("=".repeat(40));

        String name = InputValidator.getNonEmptyString(scanner, "Spellistans namn: ");

        try {
            Playlist playlist = playlistRepo.createPlaylist(name);
            System.out.println("✅ Spellista skapad med ID: " + playlist.getId());
        } catch (Exception e) {
            System.out.println("❌ Kunde inte skapa spellista: " + e.getMessage());
        }
    }

    private void addSongToPlaylist() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("LÄGG TILL LÅT I SPELLISTA");
        System.out.println("=".repeat(40));

        // Visa spellistor
        List<Playlist> playlists = playlistRepo.findAll();
        if (playlists.isEmpty()) {
            System.out.println("❌ Inga spellistor finns. Skapa en först.");
            return;
        }

        System.out.println("\n--- Välj spellista ---");
        for (Playlist playlist : playlists) {
            System.out.println(playlist.getId() + ". " + playlist.getName() +
                " (" + playlist.getEntries().size() + " låtar)");
        }

        Long playlistId = InputValidator.getLongInput(scanner, "\nAnge Spellista ID: ");

        // Visa låtar
        List<Song> songs = songRepo.findAll();
        if (songs.isEmpty()) {
            System.out.println("❌ Inga låtar finns. Lägg till låtar först.");
            return;
        }

        System.out.println("\n--- Välj låt ---");
        for (Song song : songs) {
            String albumInfo = "";
            if (song.getAlbum() != null) {
                albumInfo = " (Album: " + song.getAlbum().getTitle() + ")";
            }
            System.out.println(song.getId() + ". " + song.getTitle() + albumInfo);
        }

        Long songId = InputValidator.getLongInput(scanner, "\nAnge Låt ID: ");
        int position = InputValidator.getIntInput(scanner, "Position i spellistan: ", 1, 1000);

        try {
            playlistRepo.addSong(playlistId, songId, position);
            System.out.println("✅ Låt tillagd i spellistan!");
        } catch (Exception e) {
            System.out.println("❌ Kunde inte lägga till låt: " + e.getMessage());
        }
    }

    private void removeSongFromPlaylist() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("TA BORT LÅT FRÅN SPELLISTA");
        System.out.println("=".repeat(40));

        System.out.println("Denna funktion kräver mer avancerad implementation.");
        System.out.println("Kommer snart...");
    }

    private void showPlaylistDetails() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("VISA SPELLISTA");
        System.out.println("=".repeat(40));

        System.out.println("Denna funktion kräver mer avancerad implementation.");
        System.out.println("Kommer snart...");
    }

    private void deletePlaylist() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("TA BORT SPELLISTA");
        System.out.println("=".repeat(40));

        System.out.println("Denna funktion kräver mer avancerad implementation.");
        System.out.println("Kommer snart...");
    }

    // ========== HJÄLPMETODER ==========
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
}
