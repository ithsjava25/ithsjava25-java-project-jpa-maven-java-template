package org.example.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp {

    public void start(Stage stage) {
        try {
                var fxmlUrl = getClass().getResource("/MainView.fxml");
                if (fxmlUrl == null) {
                throw new IllegalStateException("Cannot find FXML resource: /MainView.fxml");
                }
            FXMLLoader loader = new FXMLLoader(
                fxmlUrl
            );

            Scene scene = new Scene(loader.load());

            stage.setTitle("Movie Database App");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR,
                "Failed to load the application UI: " + e.getMessage()
            );
            alert.showAndWait();
            throw new RuntimeException("UI initialization failed", e);
        }
    }
}
