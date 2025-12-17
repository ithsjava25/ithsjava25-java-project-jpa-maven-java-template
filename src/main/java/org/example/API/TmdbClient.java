package org.example.API;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.DTO.CreditsDTO;
import org.example.DTO.MovieDetailsDTO;
import org.example.DTO.TopRatedResponseDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            throw new RuntimeException("TMDB config missing in .env");
        }

        this.httpClient = httpClient;
        this.gson = gson;
    }

    public TopRatedResponseDTO getTopRatedMovies() {
        try {
            String url = baseUrl + "/movie/top_rated?api_key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), TopRatedResponseDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Could not get top rated movies from TMDB", e);
        }
    }

    public MovieDetailsDTO getMovieDetails(int movieId){
        try {
            String url = baseUrl + "/movie/" + movieId + "?api_key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), MovieDetailsDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Could not get movie details from TMDB");
        }

    }

    public CreditsDTO getMovieCredits(int movieId){
        try {
            String url = baseUrl + "/movie/" + movieId + "/credits?api_key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), CreditsDTO.class);

        } catch (Exception e){
            throw new RuntimeException("Could not get movie credits from TMDB");
        }
    }
}
