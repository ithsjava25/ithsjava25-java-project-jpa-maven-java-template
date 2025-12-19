package org.example;

import jakarta.persistence.EntityManagerFactory;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Song;
import org.example.repo.*;

import java.util.ArrayList;
import java.util.List;

public class MyPod extends Application {

    private final SongRepository songRepo = new SongRepositoryImpl();
    private final ArtistRepository artistRepo = new ArtistRepositoryImpl();
    private final AlbumRepository albumRepo = new AlbumRepositoryImpl();
    private final ItunesApiClient apiClient = new ItunesApiClient();

    private List<Song> songs;
    private List<Artist> artists;
    private List<Album> albums;

    private final ObservableList<String> mainMenu = FXCollections.observableArrayList(
        "Songs", "Artists", "Albums", "Playlists");

    private final List<Label> menuLabels = new ArrayList<>();
    private int selectedIndex = 0;
    private VBox screenContent;
    private StackPane ipodScreen;
    private ScrollPane scrollPane; // Sparas som fält för att styra scroll
    private boolean isMainMenu = true;

    @Override
    public void start(Stage primaryStage) {
        // Huvudcontainer
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("ipod-body");

        // Skärmen
        ipodScreen = createIpodScreen();
        root.setTop(ipodScreen);

        // Klickhjulet
        StackPane clickWheel = createClickWheel();
        root.setBottom(clickWheel);
        BorderPane.setMargin(clickWheel, new Insets(30, 0, 0, 0));

        // Ladda data i bakgrunden
        Task<Void> initTask = new Task<>() {
            @Override
            protected Void call() {
                initializeData();
                return null;
            }
        };

        initTask.setOnSucceeded(e -> {
            if (isMainMenu) showMainMenu();
        });

        initTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                Label error = new Label("Failed to load data.");
                error.setStyle("-fx-text-fill: red;");
                screenContent.getChildren().add(error);
            });
        });

        new Thread(initTask).start();

        Scene scene = new Scene(root, 300, 500);
        try {
            scene.getStylesheets().add(getClass().getResource("/ipod_style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS hittades inte, kör utan styling.");
        }

        setupNavigation(scene);
        showMainMenu(); // Initiera första vyn

        primaryStage.setTitle("myPod");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private StackPane createIpodScreen() {
        StackPane screenContainer = new StackPane();
        screenContainer.getStyleClass().add("ipod-screen");

        double width = 260;
        double height = 180;
        screenContainer.setPrefSize(width, height);
        screenContainer.setMaxSize(width, height);

        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        screenContainer.setClip(clip);

        scrollPane = new ScrollPane();
        screenContent = new VBox(2); // Lite mindre mellanrum för listor
        screenContent.setAlignment(Pos.TOP_LEFT);
        screenContent.setPadding(new Insets(10, 5, 10, 5));

        scrollPane.setContent(screenContent);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        screenContainer.getChildren().add(scrollPane);
        return screenContainer;
    }

    private StackPane createClickWheel() {
        StackPane wheel = new StackPane();
        wheel.setPrefSize(200, 200);

        Circle outerWheel = new Circle(100);
        outerWheel.getStyleClass().add("outer-wheel");

        Circle centerButton = new Circle(30);
        centerButton.getStyleClass().add("center-button");

        Label menu = new Label("MENU");
        menu.getStyleClass().add("wheel-text-menu");
        menu.setOnMouseClicked(e -> showMainMenu());

        Label ff = new Label(">>"); ff.getStyleClass().add("wheel-text"); ff.setId("ff-button");
        Label rew = new Label("<<"); rew.getStyleClass().add("wheel-text"); rew.setId("rew-button");
        Label play = new Label(">"); play.getStyleClass().add("wheel-text-play");

        wheel.getChildren().addAll(outerWheel, centerButton, menu, ff, rew, play);
        return wheel;
    }

    private void setupNavigation(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                showMainMenu();
                return;
            }

            int totalItems = menuLabels.size();
            if (totalItems == 0) return;

            int newIndex = selectedIndex;

            if (event.getCode() == KeyCode.DOWN) {
                newIndex = (selectedIndex + 1) % totalItems;
            } else if (event.getCode() == KeyCode.UP) {
                newIndex = (selectedIndex - 1 + totalItems) % totalItems;
            } else if (event.getCode() == KeyCode.ENTER) {
                if (isMainMenu) {
                    showScreen(mainMenu.get(selectedIndex));
                } else {
                    handleSelection(menuLabels.get(selectedIndex).getText());
                }
                return;
            }

            if (newIndex != selectedIndex) {
                selectedIndex = newIndex;
                updateMenu();
            }
        });
    }

    private void updateMenu() {
        for (int i = 0; i < menuLabels.size(); i++) {
            Label label = menuLabels.get(i);
            if (i == selectedIndex) {
                label.getStyleClass().add("selected-item");
                ensureVisible(label); // Scrolla till det markerade objektet
            } else {
                label.getStyleClass().remove("selected-item");
            }
        }
    }

    // Metod för att scrolla ScrollPane automatiskt
    private void ensureVisible(Label node) {
        Platform.runLater(() -> {
            double contentHeight = screenContent.getBoundsInLocal().getHeight();
            double viewportHeight = scrollPane.getViewportBounds().getHeight();
            double nodeY = node.getBoundsInParent().getMinY();

            if (contentHeight > viewportHeight) {
                // Beräkna positionen proportionellt
                double scrollTarget = nodeY / (contentHeight - viewportHeight);
                scrollPane.setVvalue(Math.min(1.0, Math.max(0.0, scrollTarget)));
            }
        });
    }

    private void showScreen(String screenName) {
        screenContent.getChildren().clear();
        menuLabels.clear();
        isMainMenu = false;
        selectedIndex = 0;

        Label screenTitle = new Label(screenName);
        screenTitle.getStyleClass().add("screen-title");
        screenContent.getChildren().add(screenTitle);

        switch (screenName) {
            case "Songs" -> {
                if (songs != null && !songs.isEmpty()) {
                    songs.forEach(s -> addMenuItem(s.getTitle()));
                } else addMenuItem("No songs found");
            }
            case "Artists" -> {
                if (artists != null && !artists.isEmpty()) {
                    artists.forEach(a -> addMenuItem(a.getName()));
                } else addMenuItem("No artists found");
            }
            case "Albums" -> {
                if (albums != null && !albums.isEmpty()) {
                    albums.forEach(al -> addMenuItem(al.getName()));
                } else addMenuItem("No albums found");
            }
            case "Playlists" -> {
                openMusicPlayer();
                return;
            }
        }
        updateMenu();
    }

    private void addMenuItem(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("menu-item");
        label.setMaxWidth(Double.MAX_VALUE); // Gör att hela raden blir markerad
        menuLabels.add(label);
        screenContent.getChildren().add(label);
    }

    private void showMainMenu() {
        screenContent.getChildren().clear();
        menuLabels.clear();
        isMainMenu = true;
        selectedIndex = 0;

        Label title = new Label("myPod");
        title.getStyleClass().add("screen-title");
        screenContent.getChildren().add(title);

        for (String item : mainMenu) {
            addMenuItem(item);
        }
        updateMenu();
    }

    private void handleSelection(String selection) {
        // Här kan du lägga till logik för att spela låten eller öppna albumet
        System.out.println("User selected: " + selection);
    }

    private void openMusicPlayer() {
        ItunesPlayList itunesPlayList = new ItunesPlayList();
        itunesPlayList.showLibrary(this.songs);
    }

    private void initializeData() {
        try {
            EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();
            DatabaseInitializer initializer = new DatabaseInitializer(apiClient, songRepo, albumRepo, artistRepo);
            initializer.init();

            this.songs = songRepo.findAll();
            this.artists = artistRepo.findAll();
            this.albums = albumRepo.findAll();
        } catch (Exception e) {
            System.err.println("Kunde inte ladda data: " + e.getMessage());
        }
    }
}
