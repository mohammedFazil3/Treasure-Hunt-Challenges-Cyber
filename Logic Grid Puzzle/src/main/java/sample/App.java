package main.java.sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{
    public void start(Stage primaryStage) {
        logicInterface inter = new logicInterface(primaryStage);
        inter.initializeComponents();
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
