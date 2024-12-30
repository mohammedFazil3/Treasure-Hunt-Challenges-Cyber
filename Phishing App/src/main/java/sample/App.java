package main.java.sample;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.sample.PhishingInterface;

import java.io.FileNotFoundException;

public class App extends Application{
    public void start(Stage primaryStage) {
        PhishingInterface inter = new PhishingInterface(primaryStage);
        inter.initializeComponents();
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
