package org.example;

import persistence.repository.*;
import org.example.model.*;
import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    static EntityManager em;
    static Scanner scan = new Scanner(System.in);

    static ArtistRepository artistRepo;
    static AlbumRepository albumRepo;
    static SongRepository songRepo;
    static PlaylistRepository playlistRepo;
    static TransactionHelper transactionHelper;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("musicPU");
        em = emf.createEntityManager();

        artistRepo = new ArtistRepository(em);
        albumRepo = new AlbumRepository(em);
        songRepo = new SongRepository(em);
        playlistRepo = new PlaylistRepository(em);
        transactionHelper = new TransactionHelper(em);

        System.out.println(" VÄLKOMMEN TILL MUSIKAPPEN ");

        boolean running = true;
        while(running) {
            System.out.println("\n=== HUVUDMENY ===");
            System.out.println("1. Lägg till artist");
            System.out.println("2. Visa alla artister");
            System.out.println("3. Lägg till album");
            System.out.println("4. Visa albums för artist");
            System.out.println("5. Lägg till låt");
            System.out.println("6. Visa låtar för album");
            System.out.println("7. Visa all musik");
            System.out.println("8. Skapa spellista");
            System.out.println("9. Visa alla spellistor");
            System.out.println("10. Lägg till låt i spellista");
            System.out.println("11. Ta bort låt från spellista");
            System.out.println("0. Avsluta");
            System.out.print("Välj: ");

            int val = 0;
            try {
                val = scan.nextInt();
            } catch (Exception e) {
                System.out.println("Ogiltigt val!");
                scan.nextLine();
                continue;
            }
            scan.nextLine();

            switch(val) {
                case 1: addArtist(); break;
                case 2: showArtists(); break;
                case 3: addAlbum(); break;
                case 4: showAlbumsByArtist(); break;
                case 5: addSong(); break;
                case 6: showSongsByAlbum(); break;
                case 7: showAllMusic(); break;
                case 8: createPlaylist(); break;
                case 9: showPlaylists(); break;
                case 10: addSongToPlaylist(); break;
                case 11: removeSongFromPlaylist(); break;
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

    static void addArtist() {
        System.out.print("Artist namn: ");
        String name = scan.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Namn får inte vara tomt!");
            return;
        }

        transactionHelper.executeInTransaction(() -> {
            Artist artist = new Artist(name);
            artistRepo.save(artist);
            System.out.println(" Artist tillagd med ID: " + artist.getId());
        });
    }

    static void showArtists() {
        List<Artist> artists = artistRepo.findAll();

        System.out.println("\n=== ALLA ARTISTER ===");
        if (artists.isEmpty()) {
            System.out.println("Inga artister finns.");
            return;
        }

        for(Artist a : artists) {
            System.out.println(a.getId() + ". " + a.getName() +
                " (" + a.getAlbums().size() + " album)");
        }
    }

    static void addAlbum() {
        List<Artist> artists = artistRepo.findAll();

        if(artists.isEmpty()) {
            System.out.println(" Inga artister finns. Lägg till artist först.");
            return;
        }

        System.out.println("\n--- Välj artist ---");
        for(int i = 0; i < artists.size(); i++) {
            System.out.println(i + ". " + artists.get(i).getName());
        }
        System.out.print("Välj artist (nummer): ");

        int artistIndex = -1;
        try {
            artistIndex = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltigt val!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if(artistIndex < 0 || artistIndex >= artists.size()) {
            System.out.println(" Ogiltigt val!");
            return;
        }

        Artist selectedArtist = artists.get(artistIndex);

        System.out.print("Album titel: ");
        String title = scan.nextLine();
        System.out.print("Utgivningsår: ");

        int year = 0;
        try {
            year = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltigt år!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (year < 1900 || year > 2100) {
            System.out.println("Ogiltigt år! Måste vara mellan 1900-2100.");
            return;
        }

        final Artist finalArtist = selectedArtist;
        final String finalTitle = title;
        final int finalYear = year;

        transactionHelper.executeInTransaction(() -> {
            Album album = new Album(finalTitle, finalYear);
            album.setArtist(finalArtist);
            albumRepo.save(album);
            System.out.println(" Album tillagt med ID: " + album.getId());
        });
    }

    static void showAlbumsByArtist() {
        System.out.print("Artist ID (tryck 0 för att se alla artister): ");

        Long artistId = 0L;
        try {
            artistId = scan.nextLong();
        } catch (Exception e) {
            System.out.println("Ogiltigt ID!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (artistId == 0) {
            showArtists();
            return;
        }

        Artist artist = artistRepo.findById(artistId);
        if (artist == null) {
            System.out.println("Artist med ID " + artistId + " finns inte.");
            return;
        }

        List<Album> albums = albumRepo.findByArtistId(artistId);
        System.out.println("\n=== ALBUMS AV " + artist.getName() + " ===");

        if (albums.isEmpty()) {
            System.out.println("Inga album för denna artist.");
            return;
        }

        for(Album a : albums) {
            System.out.println(a.getId() + ". " + a.getTitle() +
                " (" + a.getYear() + ") - " + a.getSongs().size() + " låtar");
        }
    }

    static void addSong() {
        List<Album> albums = albumRepo.findAll();

        if(albums.isEmpty()) {
            System.out.println(" Inga album finns. Lägg till album först.");
            return;
        }

        System.out.println("\n--- Välj album ---");
        for(int i = 0; i < albums.size(); i++) {
            Album a = albums.get(i);
            System.out.println(i + ". " + a.getTitle() + " - " +
                a.getArtist().getName() + " (" + a.getYear() + ")");
        }
        System.out.print("Välj album (nummer): ");

        int albumIndex = -1;
        try {
            albumIndex = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltigt val!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if(albumIndex < 0 || albumIndex >= albums.size()) {
            System.out.println(" Ogiltigt val!");
            return;
        }

        Album selectedAlbum = albums.get(albumIndex);

        System.out.print("Låt titel: ");
        String title = scan.nextLine();
        System.out.print("Längd i sekunder: ");

        int duration = 0;
        try {
            duration = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltig längd!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (duration <= 0) {
            System.out.println("Längden måste vara större än 0.");
            return;
        }

        final Album finalAlbum = selectedAlbum;
        final String finalTitle = title;
        final int finalDuration = duration;

        transactionHelper.executeInTransaction(() -> {
            Song song = new Song(finalTitle, finalDuration);
            song.setAlbum(finalAlbum);
            songRepo.save(song);
            System.out.println(" Låt tillagd med ID: " + song.getId());
        });
    }

    static void showSongsByAlbum() {
        System.out.print("Album ID (tryck 0 för att se alla album): ");

        Long albumId = 0L;
        try {
            albumId = scan.nextLong();
        } catch (Exception e) {
            System.out.println("Ogiltigt ID!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (albumId == 0) {
            showAllAlbums();
            return;
        }

        Album album = albumRepo.findById(albumId);
        if (album == null) {
            System.out.println("Album med ID " + albumId + " finns inte.");
            return;
        }

        List<Song> songs = songRepo.findByAlbumId(albumId);
        System.out.println("\n=== LÅTAR PÅ \"" + album.getTitle() + "\" ===");

        if (songs.isEmpty()) {
            System.out.println("Inga låtar på detta album.");
            return;
        }

        for(Song s : songs) {
            int minutes = s.getDuration() / 60;
            int seconds = s.getDuration() % 60;
            System.out.println(s.getId() + ". " + s.getTitle() +
                " (" + minutes + ":" + String.format("%02d", seconds) + ")");
        }
    }

    static void showAllAlbums() {
        List<Album> albums = albumRepo.findAll();

        System.out.println("\n=== ALLA ALBUM ===");
        if (albums.isEmpty()) {
            System.out.println("Inga album finns.");
            return;
        }

        for(Album a : albums) {
            System.out.println(a.getId() + ". " + a.getTitle() +
                " - " + a.getArtist().getName() +
                " (" + a.getYear() + ") - " + a.getSongs().size() + " låtar");
        }
    }

    static void showAllMusic() {
        List<Artist> artists = artistRepo.findAll();

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
                    System.out.println("      🎶 " + song.getTitle() +
                        " (" + minutes + ":" + String.format("%02d", seconds) + ")");
                }
            }
        }
        System.out.println("==============================");
    }

    static void createPlaylist() {
        System.out.print("Spellista namn: ");
        String name = scan.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Namn får inte vara tomt!");
            return;
        }

        final String finalName = name;

        transactionHelper.executeInTransaction(() -> {
            Playlist playlist = playlistRepo.createPlaylist(finalName);
            System.out.println(" Spellista skapad med ID: " + playlist.getId());
        });
    }

    static void showPlaylists() {
        List<Playlist> playlists = playlistRepo.findAll();

        System.out.println("\n=== ALLA SPELLISTOR ===");
        if (playlists.isEmpty()) {
            System.out.println("Inga spellistor finns.");
            return;
        }

        for(Playlist p : playlists) {
            System.out.println(p.getId() + ". " + p.getName() +
                " (skapad: " + p.getCreatedAt() + ") - " +
                p.getEntries().size() + " låtar");
        }
    }

    static void addSongToPlaylist() {
        List<Playlist> playlists = playlistRepo.findAll();
        List<Song> songs = songRepo.findAll();

        if (playlists.isEmpty()) {
            System.out.println("Inga spellistor finns. Skapa en först.");
            return;
        }

        if (songs.isEmpty()) {
            System.out.println("Inga låtar finns. Lägg till låtar först.");
            return;
        }

        System.out.println("\n--- Välj spellista ---");
        for(int i = 0; i < playlists.size(); i++) {
            Playlist p = playlists.get(i);
            System.out.println(i + ". " + p.getName() +
                " (" + p.getEntries().size() + " låtar)");
        }
        System.out.print("Välj spellista (nummer): ");

        int playlistIndex = -1;
        try {
            playlistIndex = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltigt val!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (playlistIndex < 0 || playlistIndex >= playlists.size()) {
            System.out.println("Ogiltigt val!");
            return;
        }

        Playlist playlist = playlists.get(playlistIndex);

        System.out.println("\n--- Välj låt ---");
        for(int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            System.out.println(i + ". " + s.getTitle() +
                " - " + s.getAlbum().getArtist().getName() +
                " (" + s.getAlbum().getTitle() + ")");
        }
        System.out.print("Välj låt (nummer): ");

        int songIndex = -1;
        try {
            songIndex = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltigt val!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (songIndex < 0 || songIndex >= songs.size()) {
            System.out.println("Ogiltigt val!");
            return;
        }

        Song song = songs.get(songIndex);
        System.out.print("Position i spellistan: ");

        int position = 1;
        try {
            position = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltig position!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (position < 1) {
            System.out.println("Position måste vara minst 1.");
            return;
        }

        final Long finalPlaylistId = playlist.getId();
        final Long finalSongId = song.getId();
        final int finalPosition = position;

        transactionHelper.executeInTransaction(() -> {
            playlistRepo.addSong(finalPlaylistId, finalSongId, finalPosition);
            System.out.println(" Låt tillagd i spellistan!");
        });
    }

    static void removeSongFromPlaylist() {
        List<Playlist> playlists = playlistRepo.findAll();

        if (playlists.isEmpty()) {
            System.out.println("Inga spellistor finns.");
            return;
        }

        System.out.println("\n--- Välj spellista ---");
        for(int i = 0; i < playlists.size(); i++) {
            Playlist p = playlists.get(i);
            System.out.println(i + ". " + p.getName() +
                " (" + p.getEntries().size() + " låtar)");
        }
        System.out.print("Välj spellista (nummer): ");

        int playlistIndex = -1;
        try {
            playlistIndex = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltigt val!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (playlistIndex < 0 || playlistIndex >= playlists.size()) {
            System.out.println("Ogiltigt val!");
            return;
        }

        Playlist playlist = playlists.get(playlistIndex);

        if (playlist.getEntries().isEmpty()) {
            System.out.println("Spellistan är tom.");
            return;
        }

        System.out.println("\n--- Välj låt att ta bort ---");
        List<PlaylistSong> entries = playlist.getEntries();
        for(int i = 0; i < entries.size(); i++) {
            PlaylistSong entry = entries.get(i);
            Song song = entry.getSong();
            System.out.println(i + ". " + song.getTitle() +
                " (position: " + entry.getPosition() + ")");
        }
        System.out.print("Välj låt (nummer): ");

        int songIndex = -1;
        try {
            songIndex = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Ogiltigt val!");
            scan.nextLine();
            return;
        }
        scan.nextLine();

        if (songIndex < 0 || songIndex >= entries.size()) {
            System.out.println("Ogiltigt val!");
            return;
        }

        PlaylistSong entry = entries.get(songIndex);

        final Long finalPlaylistId = playlist.getId();
        final Long finalSongId = entry.getSong().getId();

        transactionHelper.executeInTransaction(() -> {
            playlistRepo.removeSong(finalPlaylistId, finalSongId);
            System.out.println(" Låt borttagen från spellistan!");
        });
    }
}
