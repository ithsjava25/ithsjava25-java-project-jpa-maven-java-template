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
import javafx.scene.shape.Rectangle;
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

        // --- TOPPEN ---
        HBox topPanel = new HBox(15);
        topPanel.getStyleClass().add("top-panel");
        topPanel.setPadding(new Insets(10, 15, 10, 15));
        topPanel.setAlignment(Pos.CENTER_LEFT);

        StackPane lcdDisplay = createLCDDisplay();
        HBox.setHgrow(lcdDisplay, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Sök...");
        searchField.getStyleClass().add("itunes-search");
        searchField.textProperty().addListener((obs, old, newVal) -> filterSongs(newVal));

        topPanel.getChildren().addAll(
            createRoundButton("⏮"), createRoundButton("▶"), createRoundButton("⏭"),
            lcdDisplay, searchField
        );

        // --- VÄNSTER ---
        sourceList.setItems(playlistNames);
        sourceList.getStyleClass().add("source-list");
        sourceList.setPrefWidth(200);
        sourceList.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                searchField.clear();
                songTable.setItems(allPlaylists.get(newVal));
            }
        });
        sourceList.getSelectionModel().selectFirst();

        // --- MITTEN ---
        setupTable();

        // --- BOTTEN ---
        HBox bottomPanel = new HBox(10);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.getStyleClass().add("bottom-panel");

        Button btnAddList = new Button("+");
        btnAddList.getStyleClass().add("list-control-button");
        Button btnDeleteList = new Button("-");
        btnDeleteList.getStyleClass().add("list-control-button");
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

        Scene scene = new Scene(root, 950, 600);
        scene.getStylesheets().add(getClass().getResource("/ipod_style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("myTunes");
        stage.show();
    }

    private StackPane createLCDDisplay() {
        StackPane stack = new StackPane();
        Rectangle bg = new Rectangle(350, 45);
        bg.getStyleClass().add("lcd-background");

        VBox textStack = new VBox(2);
        textStack.setAlignment(Pos.CENTER);
        lcdTitle.getStyleClass().add("lcd-title");
        lcdArtist.getStyleClass().add("lcd-artist");

        textStack.getChildren().addAll(lcdTitle, lcdArtist);
        stack.getChildren().addAll(bg, textStack);
        return stack;
    }

    private Button createRoundButton(String icon) {
        Button b = new Button(icon);
        b.getStyleClass().add("itunes-button");
        return b;
    }

    private void setupTable() {
        TableColumn<DisplaySong, String> titleCol = new TableColumn<>("Namn");
        titleCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().name));
        TableColumn<DisplaySong, String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().artist));
        TableColumn<DisplaySong, String> albumCol = new TableColumn<>("Album");
        albumCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().album));
        TableColumn<DisplaySong, String> timeCol = new TableColumn<>("Längd");
        timeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().time));

        songTable.getColumns().setAll(titleCol, artistCol, albumCol, timeCol);
        songTable.getStyleClass().add("song-table");
        songTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        songTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                lcdTitle.setText(newVal.name);
                lcdArtist.setText(newVal.artist);
            }
        });
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
            return song.name.toLowerCase().contains(filter) || song.artist.toLowerCase().contains(filter) || song.album.toLowerCase().contains(filter);
        });
        songTable.setItems(filteredData);
    }

    private void initData(List<org.example.entity.Song> dbSongs) {
        ObservableList<DisplaySong> library = FXCollections.observableArrayList();
        if (dbSongs != null) {
            for (org.example.entity.Song s : dbSongs) {
                String art = (s.getAlbum() != null && s.getAlbum().getArtist() != null) ? s.getAlbum().getArtist().getName() : "Okänd";
                String alb = (s.getAlbum() != null) ? s.getAlbum().getName() : "Okänt";
                library.add(new DisplaySong(s.getTitle(), art, alb, s.getLength()));
            }
        }
        allPlaylists.put("Musik", library);
        playlistNames.add("Musik");
        allPlaylists.put("Favoriter", FXCollections.observableArrayList());
        playlistNames.add("Favoriter");
    }

    private void createNewPlaylist() {
        TextInputDialog d = new TextInputDialog("Ny lista");
        d.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty() && !allPlaylists.containsKey(name)) {
                allPlaylists.put(name, FXCollections.observableArrayList());
                playlistNames.add(name);
            }
        });
    }

    private void deleteSelectedPlaylist() {
        String sel = sourceList.getSelectionModel().getSelectedItem();
        if (sel != null && !sel.equals("Musik")) {
            allPlaylists.remove(sel);
            playlistNames.remove(sel);
        }
    }

    private void removeSelectedSong() {
        DisplaySong sel = songTable.getSelectionModel().getSelectedItem();
        String list = sourceList.getSelectionModel().getSelectedItem();
        if (sel != null && list != null && !list.equals("Musik")) allPlaylists.get(list).remove(sel);
    }

    private void showMoveToMenu(Button anchor) {
        DisplaySong sel = songTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        ContextMenu menu = new ContextMenu();
        for (String n : playlistNames) {
            if (n.equals("Musik")) continue;
            MenuItem itm = new MenuItem(n);
            itm.setOnAction(e -> { if (!allPlaylists.get(n).contains(sel)) allPlaylists.get(n).add(sel); });
            menu.getItems().add(itm);
        }
        menu.show(anchor, anchor.getScene().getWindow().getX() + anchor.getLayoutX(), anchor.getScene().getWindow().getY() + anchor.getLayoutY());
    }

    public static class DisplaySong {
        String name, artist, album, time;
        public DisplaySong(String n, String a, String al, Long t) {
            this.name = n; this.artist = a; this.album = al; this.time = String.valueOf(t);
        }
    }
}
