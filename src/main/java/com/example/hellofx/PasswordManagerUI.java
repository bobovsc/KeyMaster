package com.example.hellofx;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class PasswordManagerUI {

    private TableView<PasswordEntry> passwordTable;
    private TextField urlField;

    public TabPane createContent() {
        TabPane tabPane = new TabPane();

        //main password management
        Tab passwordTab = createPasswordTab();
        tabPane.getTabs().add(passwordTab);

        //password generator
        Tab generatePasswordTab = createGeneratePasswordTab();
        tabPane.getTabs().add(generatePasswordTab);

        return tabPane;
    }

    private Tab createPasswordTab() {
        Tab tab = new Tab("Password Management");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label titleLabel = new Label("Password Manager");
        titleLabel.setStyle("-fx-font-size: 20px;");

        Label urlLabel = new Label("URL:");
        urlField = new TextField();

        Label nameLabel = new Label("Username:");
        TextField nameField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String url = urlField.getText();
            String username = nameField.getText();
            String password = passField.getText();

            PasswordFileHandler.writePasswordToFile(url, username, password);

            urlField.clear();
            nameField.clear();
            passField.clear();

            refreshTable();
        });

        passwordTable = PasswordFileHandler.getPasswords();

        layout.getChildren().addAll(titleLabel, urlLabel, urlField, nameLabel, nameField, passLabel, passField, saveButton, passwordTable);

        tab.setContent(layout);
        return tab;
    }

    private Tab createGeneratePasswordTab() {
        Tab tab = new Tab("Generate Password");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label titleLabel = new Label("Password Generator");
        titleLabel.setStyle("-fx-font-size: 20px;");

        Slider lengthSlider = new Slider(8, 15, 10);
        lengthSlider.setShowTickMarks(true);
        lengthSlider.setShowTickLabels(true);
        lengthSlider.setMajorTickUnit(5);

        TextField generatedPasswordField = new TextField();
        generatedPasswordField.setEditable(false);

        Button generatePasswordButton = new Button("Generate Password");
        generatePasswordButton.setOnAction(event -> {
            int passwordLength = (int) lengthSlider.getValue();
            String generatedPassword = PasswordGenerator.generatePassword(passwordLength);
            generatedPasswordField.setText(generatedPassword);
        });

        layout.getChildren().addAll(titleLabel, lengthSlider, generatePasswordButton, generatedPasswordField);

        tab.setContent(layout);
        return tab;
    }

    private void refreshTable() {
        TableView<PasswordEntry> newTable = PasswordFileHandler.getPasswords();
        passwordTable.setItems(newTable.getItems());
    }
}
