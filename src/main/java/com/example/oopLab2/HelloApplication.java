package com.example.oopLab2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends javafx.application.Application {

    public static Stage stStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1065, 430);
        stage.setTitle("CRUD");
        stage.setScene(scene);
        stage.setOnCloseRequest(x -> {
            System.exit(0);
        });
        stStage = stage;
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}