package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.ui.MainApp;
import org.example.util.JPAUtil;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainApp mainApp = new MainApp();
        mainApp.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);

        JPAUtil.inTransaction(em -> {
            System.out.println("Database schema initialized");
        });
    }
}
