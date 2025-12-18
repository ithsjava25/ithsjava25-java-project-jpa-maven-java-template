package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.service.MovieServices;
import org.example.movie.entity.Movie;

import java.util.List;

public class MainController {

    @FXML
    private ListView<String> movieList;

    private final MovieServices movieServices;

    public MainController(MovieServices movieServices) {
        this.movieServices = movieServices;
    }

    @FXML
    private void loadMovies() {
        movieList.getItems().clear();

      /*  List<Movie> movies = movieServices.getAllMovies();*/ //Placeholder - här lägger vi in metoder som byggs i MovieServices, skickas till repos och JPA

       /* for (Movie movie : movies) {
            movieList.getItems().add(movie.getTitle());
        }*/
    }
}
