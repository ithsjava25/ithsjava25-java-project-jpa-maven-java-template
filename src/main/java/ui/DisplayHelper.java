package org.example.ui;  // eller org.example.UI

import org.example.model.Artist;
import org.example.model.Album;
import org.example.model.Song;
import java.util.List;

public class DisplayHelper {

    public static void printArtistList(List<Artist> artists) {
        if (artists.isEmpty()) {
            System.out.println("Inga artister hittades.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.printf("%-4s %-25s %-15s%n", "ID", "NAMN", "ANTAL ALBUM");
        System.out.println("-".repeat(60));

        for (Artist a : artists) {
            System.out.printf("%-4d %-25s %-15d%n",
                a.getId(),
                truncate(a.getName(), 23),
                a.getAlbums().size());
        }
    }

    public static void printAlbumList(List<Album> albums) {
        if (albums.isEmpty()) {
            System.out.println("Inga album hittades.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.printf("%-4s %-25s %-20s %-10s%n",
            "ID", "TITEL", "ARTIST", "ÅR");
        System.out.println("-".repeat(70));

        for (Album a : albums) {
            System.out.printf("%-4d %-25s %-20s %-10d%n",
                a.getId(),
                truncate(a.getTitle(), 23),
                truncate(a.getArtist().getName(), 18),
                a.getYear());  // ÄNDRA HÄR: getYear() istället för getReleaseYear()
        }
    }

    public static void printSongList(List<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("Inga låtar hittades.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.printf("%-4s %-20s %-25s %-20s %-10s%n",
            "ID", "ARTIST", "ALBUM", "LÅT", "LÄNGD");
        System.out.println("-".repeat(80));

        for (Song s : songs) {
            System.out.printf("%-4d %-20s %-25s %-20s %-10s%n",
                s.getId(),
                truncate(s.getAlbum().getArtist().getName(), 18),
                truncate(s.getAlbum().getTitle(), 23),
                truncate(s.getTitle(), 18),
                formatDuration(s.getDuration()));
        }
    }

    public static String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
