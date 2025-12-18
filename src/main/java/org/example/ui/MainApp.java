package org.example.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp {

    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MainView.fxml")
            );

            Scene scene = new Scene(loader.load());

            stage.setTitle("Movie Database App");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
