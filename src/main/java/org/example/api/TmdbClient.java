package org.example.api;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.dto.CreditsDTO;
import org.example.dto.MovieDetailsDTO;
import org.example.dto.TopRatedResponseDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TmdbClient {

    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final Gson gson;


    public TmdbClient() {
        this(HttpClient.newHttpClient(), new Gson());
    }


    public TmdbClient(HttpClient httpClient, Gson gson) {

        Dotenv dotenv = Dotenv.load();

        this.apiKey = dotenv.get("TMDB_API_KEY");
        this.baseUrl = dotenv.get("TMDB_BASE_URL");

        if (apiKey == null || baseUrl == null) {
            throw new IllegalArgumentException("TMDB config missing in .env");
        }

        this.httpClient = httpClient;
        this.gson = gson;
    }

    public TmdbClient(HttpClient httpClient, Gson gson, String apiKey, String baseUrl) {
        if (apiKey == null || baseUrl == null) {
            throw new IllegalArgumentException("TMDB config missing");
        }

        this.httpClient = httpClient;
        this.gson = gson;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public TopRatedResponseDTO getTopRatedMovies() {
        try {
            String url = baseUrl + "/movie/top_rated?api_key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

            HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                    "TMDB API error: HTTP " + response.statusCode() + " - " + response.body()
                );
            }

            return gson.fromJson(response.body(), TopRatedResponseDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Could not get top rated movies from TMDB", e);
        }
    }

    public MovieDetailsDTO getMovieDetails(int movieId) {
        try {
            String url = baseUrl + "/movie/" + movieId + "?api_key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

            HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                    "TMDB API error: HTTP " + response.statusCode() + " - " + response.body()
                );
            }

            return gson.fromJson(response.body(), MovieDetailsDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Could not get movie details from TMDB", e);
        }
    }

    public CreditsDTO getMovieCredits(int movieId) {
        try {
            String url = baseUrl + "/movie/" + movieId + "/credits?api_key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

            HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                    "TMDB API error: HTTP " + response.statusCode() + " - " + response.body()
                );
            }

            return gson.fromJson(response.body(), CreditsDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Could not get movie credits from TMDB", e);
        }
    }
}
