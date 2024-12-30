package main.java.sample;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;


public class logicInterface {
    private final Stage stage;
    Random random = new Random();
    Map<String, List<String>> clueSolution = permutation.generateMap();
    List<Map.Entry<String, List<String>>> entryList = new ArrayList<>(clueSolution.entrySet());

    int randomIndex = random.nextInt(entryList.size());
    Map.Entry<String, List<String>> randomEntry = entryList.get(randomIndex);
    String sol = randomEntry.getKey();
    List<String> clue = randomEntry.getValue();
    char charToRemove = '|';
    int lastIndex = sol.lastIndexOf(charToRemove);
    String solution = sol.substring(0, lastIndex);

    String[] splitString = solution.split("\\|");
    Set<String> set = new HashSet<>();

    public logicInterface(@SuppressWarnings("exports") Stage primaryStage){
        this.stage=primaryStage;
    }
    public void initializeComponents(){
        for (int i = 0; i < splitString.length; i++) {
            set.add(splitString[i].trim());
        }
        stage.setTitle("Logic Grid Puzzle");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        VBox vbox = new VBox(10);

        List<Label> labelList = new ArrayList<>();

        for(String i:clue){
            Label cluesLabel = new Label(i);
            labelList.add(cluesLabel);
            vbox.getChildren().add(cluesLabel);
        }

        Label room101Label = new Label("Room 101");
        Label room102Label = new Label("Room 102");
        Label room103Label = new Label("Room 103");
        Label windowsLabel = new Label("Windows");
        Label linuxLabel = new Label("Linux");
        Label macOSLabel = new Label("MacOS");

        grid.add(new Label("Employees"), 0, 0);
        grid.add(room101Label, 1, 0);
        grid.add(room102Label, 2, 0);
        grid.add(room103Label, 3, 0);
        grid.add(windowsLabel, 4, 0);
        grid.add(linuxLabel, 5, 0);
        grid.add(macOSLabel, 6, 0);

        Label aliceLabel = new Label("Alice");
        Label bobLabel = new Label("Bob");
        Label charlieLabel = new Label("Charlie");

        grid.add(aliceLabel, 0, 1);
        grid.add(bobLabel, 0, 2);
        grid.add(charlieLabel, 0, 3);

        CheckBox aliceRoom101 = new CheckBox();
        CheckBox aliceRoom102 = new CheckBox();
        CheckBox aliceRoom103 = new CheckBox();
        CheckBox aliceWindows = new CheckBox();
        CheckBox aliceLinux = new CheckBox();
        CheckBox aliceMacOS = new CheckBox();

        CheckBox bobRoom101 = new CheckBox();
        CheckBox bobRoom102 = new CheckBox();
        CheckBox bobRoom103 = new CheckBox();
        CheckBox bobWindows = new CheckBox();
        CheckBox bobLinux = new CheckBox();
        CheckBox bobMacOS = new CheckBox();

        CheckBox charlieRoom101 = new CheckBox();
        CheckBox charlieRoom102 = new CheckBox();
        CheckBox charlieRoom103 = new CheckBox();
        CheckBox charlieWindows = new CheckBox();
        CheckBox charlieLinux = new CheckBox();
        CheckBox charlieMacOS = new CheckBox();

        addLogicForRow(aliceRoom101, aliceRoom102, aliceRoom103);
        addLogicForRow(aliceWindows, aliceLinux, aliceMacOS);

        addLogicForRow(bobRoom101, bobRoom102, bobRoom103);
        addLogicForRow(bobWindows, bobLinux, bobMacOS);

        addLogicForRow(charlieRoom101, charlieRoom102, charlieRoom103);
        addLogicForRow(charlieWindows, charlieLinux, charlieMacOS);

        grid.add(aliceRoom101, 1, 1);
        grid.add(aliceRoom102, 2, 1);
        grid.add(aliceRoom103, 3, 1);
        grid.add(aliceWindows, 4, 1);
        grid.add(aliceLinux, 5, 1);
        grid.add(aliceMacOS, 6, 1);

        grid.add(bobRoom101, 1, 2);
        grid.add(bobRoom102, 2, 2);
        grid.add(bobRoom103, 3, 2);
        grid.add(bobWindows, 4, 2);
        grid.add(bobLinux, 5, 2);
        grid.add(bobMacOS, 6, 2);

        grid.add(charlieRoom101, 1, 3);
        grid.add(charlieRoom102, 2, 3);
        grid.add(charlieRoom103, 3, 3);
        grid.add(charlieWindows, 4, 3);
        grid.add(charlieLinux, 5, 3);
        grid.add(charlieMacOS, 6, 3);

        Button submitButton = new Button("Submit");
        Button retryButton = new Button("Retry");
        retryButton.setVisible(false); // Initially hidden

        // Event handler for the submit button - validates the submit button  - if incorrect retry button is shown
        submitButton.setOnAction(e -> {
            if (validateSolution(aliceRoom101, aliceRoom102, aliceRoom103,
                    aliceWindows,aliceLinux, aliceMacOS,
                    bobRoom101,bobRoom102,bobRoom103,
                    bobWindows, bobLinux, bobMacOS,
                    charlieRoom101,charlieRoom102,charlieRoom103,
                    charlieWindows,charlieLinux,charlieMacOS,set)) {
                showAlert("Congrats!", "The hint for your next challenge is flag{ROOM_10.2.127}");
                retryButton.setVisible(false);
            } else {
                showAlert("Incorrect!", "Your solution is incorrect. Please try again.");
                disableCheck(aliceRoom101, aliceRoom102, aliceRoom103,
                        aliceWindows,aliceLinux, aliceMacOS,
                        bobRoom101,bobRoom102,bobRoom103,
                        bobWindows, bobLinux, bobMacOS,
                        charlieRoom101,charlieRoom102,charlieRoom103,
                        charlieWindows,charlieLinux,charlieMacOS);
                retryButton.setVisible(true);
            }
        });

        // Event handler for the retry button - clears the entry and new clues with solution are generated
        retryButton.setOnAction(e -> {
            clearSelections(aliceRoom101, aliceRoom102, aliceRoom103, aliceWindows, aliceLinux, aliceMacOS,
                            bobRoom101, bobRoom102, bobRoom103, bobWindows, bobLinux, bobMacOS,
                            charlieRoom101, charlieRoom102, charlieRoom103, charlieWindows, charlieLinux, charlieMacOS);

            randomIndex = random.nextInt(entryList.size());
            randomEntry = entryList.get(randomIndex);
            solution = randomEntry.getKey();
            clue = randomEntry.getValue();

            lastIndex = solution.lastIndexOf(charToRemove);
            solution = solution.substring(0, lastIndex);

            splitString = solution.split("\\|");
            set = new HashSet<>();

            for (String s : splitString) {
                set.add(s.trim());
            }

            for (int i = 0; i < labelList.size(); i++) {
                Label label = labelList.get(i);
                label.setText(clue.get(i));
            }
            enableCheck(aliceRoom101, aliceRoom102, aliceRoom103,
                    aliceWindows,aliceLinux, aliceMacOS,
                    bobRoom101,bobRoom102,bobRoom103,
                    bobWindows, bobLinux, bobMacOS,
                    charlieRoom101,charlieRoom102,charlieRoom103,
                    charlieWindows,charlieLinux,charlieMacOS);
            retryButton.setVisible(false);
        });

        vbox.getChildren().addAll(grid, submitButton, retryButton);
        Scene scene = new Scene(vbox, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void enableCheck(CheckBox aliceRoom101, CheckBox aliceRoom102, CheckBox aliceRoom103, CheckBox aliceWindows, CheckBox aliceLinux, CheckBox aliceMacOS, CheckBox bobRoom101, CheckBox bobRoom102, CheckBox bobRoom103, CheckBox bobWindows, CheckBox bobLinux, CheckBox bobMacOS, CheckBox charlieRoom101, CheckBox charlieRoom102, CheckBox charlieRoom103, CheckBox charlieWindows, CheckBox charlieLinux, CheckBox charlieMacOS) {
        aliceRoom101.setDisable(false); aliceRoom102.setDisable(false); aliceRoom103.setDisable(false);
        aliceWindows.setDisable(false);aliceLinux.setDisable(false); aliceMacOS.setDisable(false);
        bobRoom101.setDisable(false);bobRoom102.setDisable(false);bobRoom103.setDisable(false);
        bobWindows.setDisable(false); bobLinux.setDisable(false); bobMacOS.setDisable(false);
        charlieRoom101.setDisable(false);charlieRoom102.setDisable(false);charlieRoom103.setDisable(false);
        charlieWindows.setDisable(false);charlieLinux.setDisable(false);charlieMacOS.setDisable(false);
    }

    private void disableCheck(CheckBox aliceRoom101, CheckBox aliceRoom102, CheckBox aliceRoom103, CheckBox aliceWindows, CheckBox aliceLinux, CheckBox aliceMacOS, CheckBox bobRoom101, CheckBox bobRoom102, CheckBox bobRoom103, CheckBox bobWindows, CheckBox bobLinux, CheckBox bobMacOS, CheckBox charlieRoom101, CheckBox charlieRoom102, CheckBox charlieRoom103, CheckBox charlieWindows, CheckBox charlieLinux, CheckBox charlieMacOS) {
        aliceRoom101.setDisable(true); aliceRoom102.setDisable(true); aliceRoom103.setDisable(true);
                aliceWindows.setDisable(true);aliceLinux.setDisable(true); aliceMacOS.setDisable(true);
                bobRoom101.setDisable(true);bobRoom102.setDisable(true);bobRoom103.setDisable(true);
                bobWindows.setDisable(true); bobLinux.setDisable(true); bobMacOS.setDisable(true);
                charlieRoom101.setDisable(true);charlieRoom102.setDisable(true);charlieRoom103.setDisable(true);
                charlieWindows.setDisable(true);charlieLinux.setDisable(true);charlieMacOS.setDisable(true);
    }

    // Method to validate the solution based on the selected checkboxes
    private boolean validateSolution(CheckBox aliceRoom101, CheckBox aliceRoom102, CheckBox aliceRoom103, CheckBox aliceWindows, CheckBox aliceLinux, CheckBox aliceMacOS, CheckBox bobRoom101, CheckBox bobRoom102, CheckBox bobRoom103, CheckBox bobWindows, CheckBox bobLinux, CheckBox bobMacOS, CheckBox charlieRoom101, CheckBox charlieRoom102, CheckBox charlieRoom103, CheckBox charlieWindows, CheckBox charlieLinux, CheckBox charlieMacOS,Set<String> set) {
        String Alice = "";
        String Bob = "";
        String Charlie = "";

        // Iterate over the checkboxes and check if selected
        if (aliceWindows.isSelected()){
            Alice+="Alice uses Windows ";
        } else if (aliceLinux.isSelected()) {
            Alice+="Alice uses Linux ";
        } else if (aliceMacOS.isSelected()) {
            Alice+="Alice uses MacOS ";
        }

        if (aliceRoom101.isSelected()){
            Alice+="and sits in Room 101";
        } else if (aliceRoom102.isSelected()) {
            Alice+="and sits in Room 102";
        } else if (aliceRoom103.isSelected()) {
            Alice+="and sits in Room 103";
        }

        if (bobWindows.isSelected()) {
            Bob+="Bob uses Windows ";
        }else if (bobLinux.isSelected()) {
            Bob+="Bob uses Linux ";
        }else if (bobMacOS.isSelected()) {
            Bob+="Bob uses MacOS ";
        }

        if (bobRoom101.isSelected()){
            Bob+="and sits in Room 101";
        }else if (bobRoom102.isSelected()) {
            Bob+="and sits in Room 102";
        }else if (bobRoom103.isSelected()) {
            Bob+="and sits in Room 103";
        }

        if (charlieWindows.isSelected()) {
            Charlie+="Charlie uses Windows ";
        }
        else if (charlieLinux.isSelected()) {
            Charlie+="Charlie uses Linux ";
        }
        else if (charlieMacOS.isSelected()) {
            Charlie+="Charlie uses MacOS ";
        }

        if (charlieRoom101.isSelected()) {
            Charlie+="and sits in Room 101";
        }
        else if (charlieRoom102.isSelected()) {
            Charlie+="and sits in Room 102";
        }
        else if (charlieRoom103.isSelected()) {
            Charlie+="and sits in Room 103";
        }

        Set<String> attemptSet = new HashSet<>();
        attemptSet.add(Alice);
        attemptSet.add(Bob);
        attemptSet.add(Charlie);

        return attemptSet.equals(set);
    }

    // Method to clear all the selected checkboxes
    private void clearSelections(CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setSelected(false);
        }
    }

    //method for checking and unchecking corresponding checkbox
    private void addLogicForRow(CheckBox checkBox1, CheckBox checkBox2, CheckBox checkBox3) {
        checkBox1.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            checkBox2.setDisable(isNowSelected);
            checkBox3.setDisable(isNowSelected);
        });

        checkBox2.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            checkBox1.setDisable(isNowSelected);
            checkBox3.setDisable(isNowSelected);
        });

        checkBox3.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            checkBox1.setDisable(isNowSelected);
            checkBox2.setDisable(isNowSelected);
        });
    }

    // Method to display an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
