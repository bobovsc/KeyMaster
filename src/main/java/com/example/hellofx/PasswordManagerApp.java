package com.example.hellofx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PasswordManagerApp extends Application {

    private static final String MASTER_PASSWORD = "pass";
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginStage();
    }

    private void showLoginStage() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");

        VBox loginLayout = createLoginLayout();
        Scene loginScene = new Scene(loginLayout, 300, 200);

        loginStage.setScene(loginScene);
        loginStage.show();
    }

    private VBox createLoginLayout() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #EFEFEF; -fx-padding: 10px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String enteredPassword = passwordField.getText();
            if (authenticate(enteredPassword)) {
                //successful login
                primaryStage.setTitle("Password Manager");
                primaryStage.setScene(createMainScene());
                primaryStage.show();
                ((Stage) loginButton.getScene().getWindow()).close();
            } else {
                //error message
                showAlert("Incorrect Password", "Please enter the correct password.");
            }
        });

        layout.getChildren().addAll(passwordField, loginButton);
        return layout;
    }

    private boolean authenticate(String enteredPassword) {
        return enteredPassword.equals(MASTER_PASSWORD);
    }

    private Scene createMainScene() {
        PasswordManagerUI passwordManagerUI = new PasswordManagerUI();
        return new Scene(passwordManagerUI.createContent(), 900, 600);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
