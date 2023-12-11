package com.example.hellofx;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.util.List;

public class PasswordManagerUI {

    private TableView<PasswordEntry> passwordTable;
    private TextField urlField;

    public TabPane createContent() {
        TabPane tabPane = new TabPane();

        //main password management
        Tab passwordTab = createPasswordTab();
        passwordTab.setClosable(false);
        tabPane.getTabs().add(passwordTab);

        //password generator
        Tab generatePasswordTab = createGeneratePasswordTab();
        tabPane.getTabs().add(generatePasswordTab);
        generatePasswordTab.setClosable(false);

        //password checker
        Tab passwordCheckerTab = createPasswordCheckerTab();
        passwordCheckerTab.setClosable(false);
        tabPane.getTabs().add(passwordCheckerTab);

        passwordTab.setClosable(false);
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
        TextField passField = new TextField();

        // Button to generate a password
        Button generatePasswordButton = new Button("Generate Password");
        generatePasswordButton.setOnAction(event -> {
            // Generate a password with 10 characters
            String generatedPassword = PasswordGenerator.generatePassword(10);
            passField.setText(generatedPassword);
        });

        //Buttons
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String url = urlField.getText();
            String username = nameField.getText();
            String password = passField.getText();

            if (url.isEmpty() || username.isEmpty() || password.isEmpty()) {
                // If any field is empty, just refresh the table
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in the fields.");
                alert.showAndWait();
                refreshTable();
            } else {
                // Check if the entry already exists in the table
                boolean entryExists = passwordTable.getItems().stream()
                        .anyMatch(entry -> entry.getUrl().equals(url) && entry.getUsername().equals(username));

                if (!entryExists) {
                    // Save the password only if the entry doesn't already exist
                    PasswordFileHandler.writePasswordToFile(url, username, password);

                    urlField.clear();
                    nameField.clear();
                    passField.clear();

                    refreshTable();
                } else {
                    // Display an alert for duplicate entry
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Duplicate Entry");
                    alert.setHeaderText(null);
                    alert.setContentText("Entry with the same URL and Username already exists.");
                    alert.showAndWait();
                }
            }
        });


        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            PasswordEntry selectedEntry = passwordTable.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                // Set the values of the selected row to the fields for editing
                urlField.setText(selectedEntry.getUrl());
                nameField.setText(selectedEntry.getUsername());
                passField.setText(selectedEntry.getPassword());

                // Remove the selected entry from the table
                passwordTable.getItems().remove(selectedEntry);

                // Update the password file based on the table data
                PasswordFileHandler.updatePasswordFile(passwordTable.getItems());
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            PasswordEntry selectedEntry = passwordTable.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                // Remove the selected entry from the table
                passwordTable.getItems().remove(selectedEntry);

                // Update the password file based on the table data
                PasswordFileHandler.updatePasswordFile(passwordTable.getItems());
            } else {
                // Display an alert if no row is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Row Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a row to delete.");
                alert.showAndWait();
            }
        });


        Button copyButton = new Button("Copy Password");
        copyButton.setOnAction(event -> {
            PasswordEntry selectedEntry = passwordTable.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                //String usernameToCopy = selectedEntry.getUsername();
                String passwordToCopy = selectedEntry.getPassword();

                //Clipboard API
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(passwordToCopy);
                clipboard.setContent(content);
            } else {
                // Display an alert if no row is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Row Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a row to copy the password.");
                alert.showAndWait();
            }
        });

        Button checkPwnedButton = new Button("Check Pwned Passwords");
        checkPwnedButton.setOnAction(event -> checkPwnedPasswords());

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(saveButton, editButton, deleteButton, copyButton, checkPwnedButton);

        HBox passwordButtonBox = new HBox(10);
        passwordButtonBox.setPadding(new Insets(10));
        Button changePasswordButton = new Button("Change Master Password");
        changePasswordButton.setOnAction(event -> showChangePasswordDialog());
        passwordButtonBox.getChildren().addAll(changePasswordButton, generatePasswordButton);


        passwordTable = PasswordFileHandler.getPasswords();
        refreshTable();

        layout.getChildren().addAll(titleLabel, urlLabel, urlField, nameLabel, nameField, passLabel, passField, buttonBox, passwordTable, passwordButtonBox);

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
        lengthSlider.setShowTickMarks(false);
        lengthSlider.setShowTickLabels(true);
        lengthSlider.setMajorTickUnit(1);

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

    private Tab createPasswordCheckerTab() {
        Tab tab = new Tab("Password Checker");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label titleLabel = new Label("Password Checker");
        titleLabel.setStyle("-fx-font-size: 20px;");

        Label checkPasswordLabel = new Label("Check if your password has been pwned:");

        TextField checkPasswordField = new TextField();

        Button checkButton = new Button("Check Password");
        checkButton.setOnAction(event -> {
            String passwordToCheck = checkPasswordField.getText();
            if (!passwordToCheck.isEmpty()) {
                if (PasswordChecker.isPasswordPwned(passwordToCheck)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Password Pwned");
                    alert.setHeaderText(null);
                    alert.setContentText("This password has been pwned. Please choose a different one.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Password Not Pwned");
                    alert.setHeaderText(null);
                    alert.setContentText("This password has not been pwned. It is safe to use.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a password to check.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(titleLabel, checkPasswordLabel, checkPasswordField, checkButton);
        tab.setContent(layout);
        return tab;
    }

    private void refreshTable() {
        TableView<PasswordEntry> newTable = PasswordFileHandler.getPasswords();
        decryptPasswords(newTable.getItems());
        passwordTable.setItems(newTable.getItems());
    }

    private void decryptPasswords(List<PasswordEntry> entries) {
        for (PasswordEntry entry : entries) {
            String encryptedPassword = entry.getPassword();
            String decryptedPassword = DecryptionUtil.decrypt(encryptedPassword, PasswordManagerApp.getMasterPassword());
            entry.setPassword(decryptedPassword);
        }
    }

    private void showChangePasswordDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Master Password");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new master password:");

        dialog.showAndWait().ifPresent(newPassword -> {
            PasswordManagerApp.updateMasterPassword(newPassword);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Changed");
            alert.setHeaderText(null);
            alert.setContentText("Master password has been changed successfully.");
            alert.showAndWait();
        });
    }

    private void checkPwnedPasswords() {
        // Get the list of passwords from the table
        List<PasswordEntry> entries = passwordTable.getItems();

        // Call the PasswordChecker class to check for pwned passwords
        PasswordChecker passwordChecker = new PasswordChecker();
        List<String> pwnedPasswords = passwordChecker.checkPasswords(entries);

        // Display the result (you can customize this based on your UI)
        if (pwnedPasswords.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Pwned Passwords");
            alert.setHeaderText(null);
            alert.setContentText("No passwords in the table have been found in any leaked databases.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Pwned Passwords Found");
            alert.setHeaderText(null);
            alert.setContentText("The following passwords have been found in leaked databases:\n" + String.join("\n", pwnedPasswords));
            alert.showAndWait();
        }
    }
}