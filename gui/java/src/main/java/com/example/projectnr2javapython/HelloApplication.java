package com.example.projectnr2javapython;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Grafik Zajęć");
        stage.setScene(scene);

        ((HelloController)fxmlLoader.getController()).setStage(stage);

        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.sizeToScene();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}