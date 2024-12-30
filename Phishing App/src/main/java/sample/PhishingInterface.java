package main.java.sample;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.util.Arrays;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.*;

public class PhishingInterface {
    private Scene interfaceScene;
    private Stage stage;
    private int currentImageIndex = 0;
    private Image[] images = new Image[5];
    private final boolean[] answers = {false, true, false, false, false};
    private boolean[] attempts = new boolean[5];

    public PhishingInterface(@SuppressWarnings("exports") Stage primaryStage){
        this.stage=primaryStage;
    }

    public void initializeComponents(){
        //Defining the layout:
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);

        //controls
        Label appName = new Label("Phishing Identification App");
        appName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        GridPane.setHalignment(appName, HPos.CENTER);
        Label introText = new Label("Welcome to Phishing Identification App!\nLearn to Identify Phishing Emails and Protect Yourself!");
        introText.setStyle("-fx-font-size: 16px;");
        introText.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(introText, HPos.CENTER);
        Label purposeText = new Label("This app will help you practice identifying phishing emails, a crucial skill in today’s digital age. You'll review various email samples and determine if they are legitimate or phishing attempts. At the end, you'll receive a score based on your performance.");
        purposeText.setWrapText(true);
        purposeText.setStyle("-fx-font-size: 14px;");
        Label guideText = new Label("Quick Guide:");
        guideText.setStyle("-fx-font-size: 14px;");
        Label howToUseText = new Label("1. Click Start to begin.\n2. Review each email presented.\n3. Choose whether the email is Legit or Phishing.\n4. Receive your score and see how you did!");
        howToUseText.setStyle("-fx-font-size: 14px;");
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

        //eventHandler for startButton
        startButton.setOnAction(e -> {
            // Load the quiz scene or start the quiz
            Scene quizScene=null;
            quizScene = createQuizScene(stage);
            stage.setScene(quizScene);
        });

        //adding the control to the layout
        layout.add(appName, 0, 0, 2, 1);
        layout.add(introText, 0, 1, 2, 1);
        layout.add(purposeText, 0, 2, 2, 1);
        layout.add(guideText,0,3,2,1);
        layout.add(howToUseText, 0, 4, 2, 1);
        layout.add(startButton, 0, 5, 2, 1);
        GridPane.setHalignment(startButton, HPos.CENTER);

        //adding the layout to the login Scene
        interfaceScene = new Scene(layout,900,600);

        stage.setTitle("Welcome Page");

        //adding the scene to the stage
        stage.setScene(interfaceScene);



        stage.show();
    }

    private Scene createQuizScene(Stage primaryStage){
        for (int i = 0; i < 5; i++) {
            String imagePath = "/main/resources/image" + (i + 1) + ".png";
            InputStream stream = getClass().getResourceAsStream(imagePath);
            System.out.println(imagePath);
            if (stream == null) {
                throw new RuntimeException("Image not found: /com/example/assets/image" + (i + 1) + ".png");
            }
            images[i] = new Image(stream);
        }


        // Create components for the quiz scene
        ImageView imageView = new ImageView(images[currentImageIndex]);
        imageView.setFitHeight(500);
        imageView.setFitWidth(600);

        ToggleGroup group = new ToggleGroup();
        RadioButton phishingOption = new RadioButton("Phishing");
        phishingOption.setToggleGroup(group);
        RadioButton legitOption = new RadioButton("Legit");
        legitOption.setToggleGroup(group);

        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        nextButton.setOnAction(e -> {
            if (group.getSelectedToggle() == null) {
                showAlert("Please select an option before proceeding.");
            } else {
                boolean selectedAnswer = legitOption.isSelected();
                if (selectedAnswer == answers[currentImageIndex]) {
                    attempts[currentImageIndex]=true;
                } else {
                    attempts[currentImageIndex]=false;
                }
                currentImageIndex++;
                if (currentImageIndex < images.length) {
                    group.selectToggle(null); // Reset selection
                    // Move to the next image
                    imageView.setImage(images[currentImageIndex]);
                } else {
                    int result = 0;
                    for(int i=0;i<attempts.length;i++){
                        if(attempts[i]==false){
                            result+=1;
                        }
                    }
                    if(result==0){
                        showAlert("Congratulations! 🎉 You've successfully identified all phishing emails. Your keen eye for detail helps protect against online threats. Keep up the great work! \n Here is your clue/direction for your next challenge: ROOM 127");
                        currentImageIndex = 0;
                        images = new Image[5];
                        attempts = new boolean[5];
                        stage.setScene(interfaceScene);
                    }
                    else {
                        // Create an alert with a retry option
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Quiz Incomplete");
                        alert.setHeaderText("Oops! You missed some phishing emails.");
                        alert.setContentText("It looks like you didn't identify all the phishing emails correctly. Would you like to try again?");

                        ButtonType retryButton = new ButtonType("Retry");
                        alert.getButtonTypes().setAll(retryButton);

                        alert.showAndWait().ifPresent(response -> {
                            if (response == retryButton) {
                                // Reset the quiz state
                                currentImageIndex = 0;
                                Arrays.fill(attempts, false); // Reset attempts array

                                // Go back to the welcome screen
                                currentImageIndex = 0;
                                images = new Image[5];
                                attempts = new boolean[5];
                                stage.setScene(interfaceScene);
                            }
                        });
                    }

                }
            }
        });
        // Layout for the quiz
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(imageView, 0, 0, 2, 1);
        gridPane.add(phishingOption, 0, 1);
        gridPane.add(legitOption, 1, 1);
        gridPane.add(nextButton, 0, 2, 2, 1); // Spanning across 2 columns

        return new Scene(gridPane, 1000, 700);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);

        TextArea textArea = new TextArea(message);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefHeight(150);
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setMaxWidth(Double.MAX_VALUE);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }
}