package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItunesPlayList {

    private Map<String, ObservableList<DisplaySong>> allPlaylists = new HashMap<>();
    private ObservableList<String> playlistNames = FXCollections.observableArrayList();

    private TableView<DisplaySong> songTable = new TableView<>();
    private ListView<String> sourceList = new ListView<>();
    private Text lcdTitle = new Text("iTunes");
    private Text lcdArtist = new Text("Välj bibliotek eller spellista");

    public void showLibrary(List<org.example.entity.Song> dbSongs) {
        Stage stage = new Stage();
        initData(dbSongs);

        BorderPane root = new BorderPane();

        // --- TOPPEN (LCD, Kontroller & Sök) ---
        HBox topPanel = new HBox(15);
        topPanel.setPadding(new Insets(10, 15, 10, 15));
        topPanel.setAlignment(Pos.CENTER_LEFT);
        topPanel.setStyle("-fx-background-color: linear-gradient(to bottom, #dcdcdc, #bebebe); -fx-border-color: #999; -fx-border-width: 0 0 1 0;");

        // Skapa LCD
        StackPane lcdDisplay = createLCDDisplay();
        HBox.setHgrow(lcdDisplay, Priority.ALWAYS);

        // Skapa Sökfält
        TextField searchField = new TextField();
        searchField.setPromptText("Sök...");
        searchField.setPrefWidth(200);
        // iTunes-look med runda hörn
        searchField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #aaa; -fx-background-color: white;");

        // Koppla Söklogik
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSongs(newValue);
        });

        topPanel.getChildren().addAll(
            createRoundButton("⏮"),
            createRoundButton("▶"),
            createRoundButton("⏭"),
            lcdDisplay,
            searchField
        );

        // --- VÄNSTER (Källor) ---
        sourceList.setItems(playlistNames);
        sourceList.setPrefWidth(200);
        sourceList.setStyle("-fx-background-color: #ebf1f7; -fx-control-inner-background: #ebf1f7; -fx-font-family:'Lucida Console'");
        sourceList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                searchField.clear(); // Rensa sökning vid byte av lista
                songTable.setItems(allPlaylists.get(newVal));
            }
        });
        sourceList.getSelectionModel().selectFirst();

        // --- MITTEN (Låtlista) ---
        setupTable();

        // --- BOTTEN (Knappar) ---
        HBox bottomPanel = new HBox(10);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setStyle("-fx-background-color: #dcdcdc; -fx-border-color: #999; -fx-border-width: 1 0 0 0;");

        Button btnAddList = new Button("+");
        Button btnDeleteList = new Button("-");
        Button btnMoveToPlaylist = new Button("Lägg till Låt i spellista");
        Button btnRemoveSong = new Button("Ta bort låt från lista");

        btnAddList.setOnAction(e -> createNewPlaylist());
        btnDeleteList.setOnAction(e -> deleteSelectedPlaylist());
        btnRemoveSong.setOnAction(e -> removeSelectedSong());
        btnMoveToPlaylist.setOnAction(e -> showMoveToMenu(btnMoveToPlaylist));

        bottomPanel.getChildren().addAll(btnAddList, btnDeleteList, new Separator(), btnMoveToPlaylist, btnRemoveSong);

        SplitPane splitPane = new SplitPane(sourceList, songTable);
        splitPane.setDividerPositions(0.25);

        root.setTop(topPanel);
        root.setCenter(splitPane);
        root.setBottom(bottomPanel);

        stage.setScene(new Scene(root, 950, 600));
        stage.setTitle("iTunes");
        stage.show();
    }

    private void filterSongs(String searchText) {
        String currentList = sourceList.getSelectionModel().getSelectedItem();
        if (currentList == null) return;

        ObservableList<DisplaySong> masterData = allPlaylists.get(currentList);
        if (searchText == null || searchText.isEmpty()) {
            songTable.setItems(masterData);
            return;
        }

        FilteredList<DisplaySong> filteredData = new FilteredList<>(masterData, song -> {
            String filter = searchText.toLowerCase();
            return song.name.toLowerCase().contains(filter) ||
                song.artist.toLowerCase().contains(filter) ||
                song.album.toLowerCase().contains(filter);
        });

        songTable.setItems(filteredData);
    }

    private void initData(List<org.example.entity.Song> dbSongs) {
        ObservableList<DisplaySong> library = FXCollections.observableArrayList();

        if (dbSongs != null) {
            for (org.example.entity.Song s : dbSongs) {
                String artistName = "Okänd Artist";
                String albumName = "Okänt Album";

                if (s.getAlbum() != null) {
                    albumName = s.getAlbum().getName();
                    // Gå via albumet för att hitta artisten
                    if (s.getAlbum().getArtist() != null) {
                        artistName = s.getAlbum().getArtist().getName();
                    }
                }
                library.add(new DisplaySong(s.getTitle(), artistName, albumName, s.getLength()));
            }
        }

        allPlaylists.put("Musik", library);
        playlistNames.add("Musik");
        allPlaylists.put("Favoriter", FXCollections.observableArrayList());
        playlistNames.add("Favoriter");
    }

    private void setupTable() {
        TableColumn<DisplaySong, String> titleCol = new TableColumn<>("Namn");
        titleCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().name));

        TableColumn<DisplaySong, String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().artist));

        TableColumn<DisplaySong, String> albumCol = new TableColumn<>("Album");
        albumCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().album));

        TableColumn<DisplaySong, String> timeCol = new TableColumn<>("Tid");
        timeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().time));

        songTable.getColumns().clear();
        songTable.getColumns().addAll(titleCol, artistCol, albumCol, timeCol);
        songTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        songTable.setStyle("-fx-font-family: 'Lucida Console'");

        songTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lcdTitle.setText(newVal.name);
                lcdArtist.setText(newVal.artist);
            }
        });
    }

    private StackPane createLCDDisplay() {
        StackPane stack = new StackPane();
        Rectangle bg = new Rectangle(350, 45, Color.WHITE);
        bg.setArcWidth(10); bg.setArcHeight(10);
        bg.setStroke(Color.GRAY);
        VBox textStack = new VBox(2);
        textStack.setAlignment(Pos.CENTER);
        lcdTitle.setFont(Font.font("Lucida Console", FontWeight.BOLD, 12));
        lcdArtist.setFill(Color.DARKSLATEGRAY);
        textStack.getChildren().addAll(lcdTitle, lcdArtist);
        stack.getChildren().addAll(bg, textStack);
        return stack;
    }

    private Button createRoundButton(String icon) {
        Button b = new Button(icon);
        b.setStyle("-fx-background-radius: 50; -fx-min-width: 35; -fx-min-height: 35;");
        return b;
    }

    private void createNewPlaylist() {
        TextInputDialog dialog = new TextInputDialog("Ny lista");
        dialog.setTitle("Skapa spellista");
        dialog.setHeaderText("Ange namn på spellistan");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty() && !allPlaylists.containsKey(name)) {
                allPlaylists.put(name, FXCollections.observableArrayList());
                playlistNames.add(name);
                sourceList.getSelectionModel().select(name);
            }
        });
    }

    private void deleteSelectedPlaylist() {
        String selectedName = sourceList.getSelectionModel().getSelectedItem();
        if (selectedName == null || selectedName.equals("Musik")) return;

        allPlaylists.remove(selectedName);
        playlistNames.remove(selectedName);
        sourceList.getSelectionModel().select("Musik");
    }

    private void removeSelectedSong() {
        DisplaySong selected = songTable.getSelectionModel().getSelectedItem();
        String currentList = sourceList.getSelectionModel().getSelectedItem();
        if (selected != null && currentList != null && !currentList.equals("Musik")) {
            allPlaylists.get(currentList).remove(selected);
        }
    }

    private void showMoveToMenu(Button anchor) {
        DisplaySong selectedSong = songTable.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        ContextMenu moveMenu = new ContextMenu();
        for (String name : playlistNames) {
            if (name.equals("Musik")) continue;
            MenuItem item = new MenuItem(name);
            item.setOnAction(e -> {
                if (!allPlaylists.get(name).contains(selectedSong)) {
                    allPlaylists.get(name).add(selectedSong);
                }
            });
            moveMenu.getItems().add(item);
        }
        moveMenu.show(anchor, anchor.getScene().getWindow().getX() + anchor.getLayoutX(),
            anchor.getScene().getWindow().getY() + anchor.getLayoutY());
    }

    public static class DisplaySong {
        String name, artist, album, time;
        public DisplaySong(String n, String a, String al, Long t) {
            this.name = n;
            this.artist = a;
            this.album = al;
            this.time = String.valueOf(t);
        }
    }
}
