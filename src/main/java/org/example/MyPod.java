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
import javafx.stage.Stage;
import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Song;
import org.example.repo.*;

import java.util.ArrayList;
import java.util.List;

public class MyPod extends Application{


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
    private boolean isMainMenu = true;

    @Override
    public void start(Stage primaryStage) {
//        // Initiera databasen och hämta data
//        initializeData();

    //Huvudcontainer
    BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("ipod-body");

        //Skärmen
        ipodScreen = createIpodScreen();
        root.setTop(ipodScreen);

        //Klickhjulet
        StackPane clickWheel = createClickWheel();
        root.setBottom(clickWheel);
        BorderPane.setMargin(clickWheel, new Insets(30, 0, 0, 0));

    // CodeRabbit Suggestion //
    // Move Initialization to background thread //

    // Initialize data in background
    Task<Void> initTask = new Task<>() {

    @Override
    protected Void call() {
        initializeData();
        return null;
            }};

    initTask.setOnSucceeded(e -> {
        // Refresh UI after data loads
         if (isMainMenu) {
             showMainMenu();}
                    });

    initTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                  Label error = new Label("Failed to load data: " + initTask.getException().getMessage());
                  error.setStyle("-fx-text-fill: red;");
                  screenContent.getChildren().add(error);
                  });
                  });

            new Thread(initTask).start();

        // --------------------------------//


        Scene scene = new Scene(root, 300, 500);
        try {
            scene.getStylesheets().add(getClass().getResource("/ipod_style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS hittades inte, kör utan styling.");
        }

        setupNavigation(scene);
        updateMenu();

        primaryStage.setTitle("myPod");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    private StackPane createIpodScreen() {
        StackPane screenContainer = new StackPane();
        screenContainer.getStyleClass().add("ipod-screen");

        // Skapa ScrollPane
        ScrollPane scrollPane = new ScrollPane();

        // Innehållet (VBox)
        screenContent = new VBox(5);
        screenContent.setAlignment(Pos.TOP_LEFT);
        screenContent.setPadding(new Insets(10, 5, 10, 5));

        // Koppla ihop dem
        scrollPane.setContent(screenContent);

        // Inställningar för att dölja scrollbars och fixa storleken
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Ingen horisontell scroll
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Dölj vertikal scrollbar
        scrollPane.setFitToWidth(true); // Se till att VBoxen fyller bredden
        scrollPane.setPannable(true);   // Gör det möjligt att "dra" med musen om man vill

        // Gör ScrollPane genomskinlig så att CSS-bakgrunden på ipod-screen syns
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        showMainMenu();

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

        Label ff = new Label(">>"); ff.getStyleClass().add("wheel-text");ff.setId("ff-button");
        Label rew = new Label("<<"); rew.getStyleClass().add("wheel-text");rew.setId("rew-button");
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

            if (!isMainMenu) return;

            int newIndex = selectedIndex;
            if (event.getCode() == KeyCode.DOWN) {
                newIndex = (selectedIndex + 1) % mainMenu.size();
            } else if (event.getCode() == KeyCode.UP) {
                newIndex = (selectedIndex - 1 + mainMenu.size()) % mainMenu.size();
            } else if (event.getCode() == KeyCode.ENTER) {
                showScreen(mainMenu.get(selectedIndex));
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
            } else {
                label.getStyleClass().remove("selected-item");
            }
        }
    }

    private void showScreen(String screenName) {
        screenContent.getChildren().clear();
        isMainMenu = false;
        menuLabels.clear();

        Label screenTitle = new Label(screenName);
        screenTitle.getStyleClass().add("screen-title");
        screenContent.getChildren().add(screenTitle);

        switch (screenName) {
            case "Songs" -> {
                if (songs != null) songs.forEach(s -> addMenuItem(s.getTitle()));
                else addMenuItem("No songs available");
            }
            case "Artists" -> {
                if (artists != null) artists.forEach(a -> addMenuItem(a.getName()));
                else addMenuItem("No artists available");
            }
            case "Albums" -> {
               if (albums != null) albums.forEach(al -> addMenuItem(al.getName()));
               else addMenuItem("No albums available");
            }
            case "Playlists" -> addMenuItem("Inga spellistor skapade");
        }
    }

    private void addMenuItem(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("menu-item");
        screenContent.getChildren().add(label);
    }

    private void showMainMenu() {
        screenContent.getChildren().clear();
        isMainMenu = true;
        menuLabels.clear();

        Label title = new Label("myPod");
        title.getStyleClass().add("screen-title");
        screenContent.getChildren().add(title);

        for (String item : mainMenu) {
            Label label = new Label(item);
            label.getStyleClass().add("menu-item");
            menuLabels.add(label);
            screenContent.getChildren().add(label);
        }
        selectedIndex = 0;
        updateMenu();
    }

    // Hämtning från db
    private void initializeData() {
        try {

            EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();
            if (!emf.isOpen()) {
                throw new IllegalStateException("EntityManagerFactory is not open");
            }

            DatabaseInitializer initializer = new DatabaseInitializer(apiClient, songRepo, albumRepo, artistRepo);
            initializer.init();

            // Repository - Hitta alla
            this.songs = songRepo.findAll();
            this.artists = artistRepo.findAll();
            this.albums = albumRepo.findAll();

        } catch (Exception e) {
            System.err.println("Kunde inte ladda data: " + e.getMessage());
            throw new RuntimeException("Kunde inte ladda data: " + e);
        }
    }


}

