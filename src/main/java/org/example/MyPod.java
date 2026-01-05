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

/**
 * Huvudklassen för applikationen "MyPod".
 * Denna klass bygger upp GUI:t (simulerar en iPod) och hanterar navigering.
 */
public class MyPod extends Application {

    // --- DATA-LAGER ---
    // Repositories används för att hämta data från databasen istället för att hårdkoda den.
    private final SongRepository songRepo = new SongRepositoryImpl();
    private final ArtistRepository artistRepo = new ArtistRepositoryImpl();
    private final AlbumRepository albumRepo = new AlbumRepositoryImpl();
    private final ItunesApiClient apiClient = new ItunesApiClient();

    // Listor som håller datan vi hämtat från databasen
    private List<Song> songs;
    private List<Artist> artists;
    private List<Album> albums;

    // --- MENY-DATA ---
    // Huvudmenyns alternativ. "ObservableList" är en speciell lista i JavaFX
    // som GUI:t kan "lyssna" på, även om vi här mest använder den som en vanlig lista.
    private final ObservableList<String> mainMenu = FXCollections.observableArrayList(
        "Songs", "Artists", "Albums", "Playlists");

    // En lista med själva Label-objekten som visas på skärmen (för att kunna markera/avmarkera dem)
    private final List<Label> menuLabels = new ArrayList<>();

    // --- GUI-TILLSTÅND ---
    private int selectedIndex = 0;      // Håller koll på vilket menyval som är markerat just nu
    private VBox screenContent;         // Behållaren för texten/listan inuti "skärmen"
    private StackPane myPodScreen;       // Själva skärm-containern
    private ScrollPane scrollPane;      // Gör att vi kan scrolla om listan är lång
    private boolean isMainMenu = true;  // Flagga för att veta om vi är i huvudmenyn eller en undermeny

    @Override
    public void start(Stage primaryStage) {
        // --- LAYOUT SETUP ---
        // BorderPane är bra för att placera saker i Top, Bottom, Center, Left, Right.
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20)); // Lite luft runt kanten
        root.getStyleClass().add("ipod-body"); // CSS-klass för själva iPod-kroppen

        // 1. Skapa och placera skärmen högst upp
        myPodScreen = createMyPodScreen();
        root.setTop(myPodScreen);

        // 2. Skapa och placera klickhjulet längst ner
        StackPane clickWheel = createClickWheel();
        root.setBottom(clickWheel);
        BorderPane.setMargin(clickWheel, new Insets(30, 0, 0, 0)); // Marginal ovanför hjulet

        // --- BAKGRUNDSLADDNING ---
        // Vi använder en Task för att ladda databasen. Detta är kritiskt!
        // Om vi laddar databasen direkt i start() fryser hela fönstret tills det är klart.
        Task<Void> initTask = new Task<>() {
            @Override
            protected Void call() {
                initializeData(); // Detta tunga jobb körs i en separat tråd
                return null;
            }
        };

        // När datan är laddad och klar:
        initTask.setOnSucceeded(e -> {
            if (isMainMenu) showMainMenu(); // Rita upp menyn nu när vi har data
        });

        // Om något går fel (t.ex. ingen databasanslutning):
        initTask.setOnFailed(e -> {
            // Platform.runLater måste användas när vi ändrar GUI:t från en annan tråd
            Platform.runLater(() -> {
                Label error = new Label("Failed to load data.");
                error.setStyle("-fx-text-fill: red;");
                screenContent.getChildren().add(error);
            });
        });

        // Starta laddningstråden
        new Thread(initTask).start();

        // --- SCEN OCH CSS ---
        Scene scene = new Scene(root, 300, 500);
        try {
            // Försök ladda CSS-filen för styling
            scene.getStylesheets().add(getClass().getResource("/ipod_style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS hittades inte, kör utan styling.");
        }

        // Koppla tangentbordslyssnare för att kunna styra menyn
        setupNavigation(scene);
        showMainMenu(); // Initiera första vyn (tom tills datan laddats klart)

        primaryStage.setTitle("myPod");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // En iPod ändrar inte storlek
        primaryStage.show();
    }

    /**
     * Skapar den visuella skärmen (den "lysande" rutan).
     */
    private StackPane createMyPodScreen() {
        StackPane screenContainer = new StackPane();
        screenContainer.getStyleClass().add("ipod-screen");

        double width = 260;
        double height = 180;
        screenContainer.setPrefSize(width, height);
        screenContainer.setMaxSize(width, height);

        // Skapa en "mask" (Rectangle) för att klippa innehållet så hörnen blir rundade
        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        screenContainer.setClip(clip);

        scrollPane = new ScrollPane();
        screenContent = new VBox(2); // VBox staplar element vertikalt. 2px mellanrum.
        screenContent.setAlignment(Pos.TOP_LEFT);
        screenContent.setPadding(new Insets(10, 5, 10, 5));

        // Konfigurera scrollbaren så den inte syns men fungerar
        scrollPane.setContent(screenContent);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Ingen horisontell scroll
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Ingen synlig vertikal scroll
        scrollPane.setFitToWidth(true); // Innehållet ska fylla bredden
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        screenContainer.getChildren().add(scrollPane);
        return screenContainer;
    }

    /**
     * Skapar klickhjulet med knappar.
     */
    private StackPane createClickWheel() {
        StackPane wheel = new StackPane();
        wheel.setPrefSize(200, 200);

        // Det stora yttre hjulet
        Circle outerWheel = new Circle(100);
        outerWheel.getStyleClass().add("outer-wheel");

        // Den lilla knappen i mitten
        Circle centerButton = new Circle(30);
        centerButton.getStyleClass().add("center-button");

        // Etiketter för knapparna (MENU, Play, Fram, Bak)
        Label menu = new Label("MENU");
        menu.getStyleClass().add("wheel-text-menu");
        // Om man klickar på ordet MENU med musen går man tillbaka
        menu.setOnMouseClicked(e -> showMainMenu());

        Label ff = new Label("⏭"); ff.getStyleClass().add("wheel-text"); ff.setId("ff-button");
        Label rew = new Label("⏮"); rew.getStyleClass().add("wheel-text"); rew.setId("rew-button");
        Label play = new Label("▶"); play.getStyleClass().add("wheel-text-play");

        wheel.getChildren().addAll(outerWheel, centerButton, menu, ff, rew, play);
        return wheel;
    }

    /**
     * Hanterar tangentbordsnavigering (Upp, Ner, Enter, Escape).
     */
    private void setupNavigation(Scene scene) {
        scene.setOnKeyPressed(event -> {
            // ESCAPE fungerar som "Back"-knapp
            if (event.getCode() == KeyCode.ESCAPE) {
                showMainMenu();
                return;
            }

            int totalItems = menuLabels.size();
            if (totalItems == 0) return; // Gör inget om listan är tom

            int newIndex = selectedIndex;

            // Navigera NER
            if (event.getCode() == KeyCode.DOWN) {
                // Modulo (%) gör att om vi trycker ner på sista elementet, hamnar vi på 0 igen.
                newIndex = (selectedIndex + 1) % totalItems;
            }
            // Navigera UPP
            else if (event.getCode() == KeyCode.UP) {
                // Matematisk formel för att loopa bakåt (från 0 till sista)
                newIndex = (selectedIndex - 1 + totalItems) % totalItems;
            }
            // Välj (ENTER)
            else if (event.getCode() == KeyCode.ENTER) {
                if (isMainMenu) {
                    // Om vi är i huvudmenyn, gå in i en undermeny (t.ex. "Songs")
                    showScreen(mainMenu.get(selectedIndex));
                } else {
                    // Om vi är i en lista, hantera valet (spela låt etc)
                    handleSelection(menuLabels.get(selectedIndex).getText());
                }
                return;
            }

            // Om indexet ändrades, uppdatera grafiken
            if (newIndex != selectedIndex) {
                selectedIndex = newIndex;
                updateMenu();
            }
        });
    }

    /**
     * Uppdaterar visuellt vilken rad som är vald (lägger till CSS-klass).
     */
    private void updateMenu() {
        for (int i = 0; i < menuLabels.size(); i++) {
            Label label = menuLabels.get(i);
            if (i == selectedIndex) {
                label.getStyleClass().add("selected-item"); // Gör texten markerad
                ensureVisible(label); // Se till att scrollbaren flyttas så vi ser valet
            } else {
                label.getStyleClass().remove("selected-item"); // Ta bort markering
            }
        }
    }

    /**
     * Avancerad metod för att automatiskt scrolla ScrollPane till det markerade elementet.
     */
    private void ensureVisible(Label node) {
        Platform.runLater(() -> {
            double contentHeight = screenContent.getBoundsInLocal().getHeight();
            double viewportHeight = scrollPane.getViewportBounds().getHeight();
            double nodeY = node.getBoundsInParent().getMinY();

            // Om innehållet är högre än skärmen, räkna ut var vi ska scrolla
            if (contentHeight > viewportHeight) {
                // Beräkna positionen (0.0 är toppen, 1.0 är botten)
                double scrollTarget = nodeY / (contentHeight - viewportHeight);
                // Sätt värdet men tvinga det att vara mellan 0 och 1
                scrollPane.setVvalue(Math.min(1.0, Math.max(0.0, scrollTarget)));
            }
        });
    }

    /**
     * Visar en undermeny (Songs, Artists etc).
     */
    private void showScreen(String screenName) {
        screenContent.getChildren().clear(); // Rensa skärmen
        menuLabels.clear();                  // Rensa listan med menyval
        isMainMenu = false;                  // Vi är inte i huvudmenyn längre
        selectedIndex = 0;                   // Återställ markör till toppen

        // Rubrik
        Label screenTitle = new Label(screenName);
        screenTitle.getStyleClass().add("screen-title");
        screenContent.getChildren().add(screenTitle);

        // Fyll på med rätt data beroende på vad användaren valde
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
                openMusicPlayer(); // Öppnar myTunes i nytt fönster
                return;
            }
        }
        updateMenu(); // Uppdatera så första valet är markerat
    }

    /**
     * Hjälpmetod för att lägga till en rad i listan på skärmen.
     */
    private void addMenuItem(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("menu-item");
        label.setMaxWidth(Double.MAX_VALUE); // Gör att raden fyller hela bredden (snyggare markering)
        menuLabels.add(label);
        screenContent.getChildren().add(label);
    }

    /**
     * Visar huvudmenyn igen.
     */
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

    /**
     * Vad som händer när man trycker Enter på en låt/artist.
     */
    private void handleSelection(String selection) {
        // Här kan du lägga till logik för att spela låten eller öppna albumet
        System.out.println("User selected: " + selection);
    }

    /**
     * Öppnar det externa fönstret "ItunesPlayList".
     */
    private void openMusicPlayer() {
        ItunesPlayList itunesPlayList = new ItunesPlayList();
        itunesPlayList.showLibrary(this.songs);
    }

    /**
     * Initierar databasen och hämtar all data.
     * OBS: Denna körs i en bakgrundstråd (via Task i start-metoden).
     */
    private void initializeData() {
        try {
            EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();
            DatabaseInitializer initializer = new DatabaseInitializer(apiClient, songRepo, albumRepo, artistRepo);
            initializer.init(); // Fyll databasen om den är tom

            // Hämta data till minnet
            this.songs = songRepo.findAll();
            this.artists = artistRepo.findAll();
            this.albums = albumRepo.findAll();
        } catch (Exception e) {
            System.err.println("Kunde inte ladda data: " + e.getMessage());
        }
    }
}
