package com.example.hellofx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.*;
import java.util.List;

public class PasswordFileHandler {

    //pass file
    private static final String FILE_NAME = "passwords.txt";

    public static void writePasswordToFile(String url, String username, String password) {
        try {
            File file = new File(FILE_NAME);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);

            br.write(url + ":" + username + ":" + password);
            br.newLine();

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TableView<PasswordEntry> getPasswords() {
        TableView<PasswordEntry> table = new TableView<>();

        TableColumn<PasswordEntry, String> urlCol = new TableColumn<>("URL");
        urlCol.setCellValueFactory(data -> data.getValue().urlProperty());

        TableColumn<PasswordEntry, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> data.getValue().usernameProperty());

        TableColumn<PasswordEntry, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(data -> data.getValue().passwordProperty());

        table.getColumns().addAll(urlCol, usernameCol, passwordCol);

        ObservableList<PasswordEntry> data = FXCollections.observableArrayList();

        try {
            File file = new File(FILE_NAME);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String url = parts[0];
                String username = parts[1];
                String password = parts[2];
                data.add(new PasswordEntry(url, username, password));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.setItems(data);
        return table;
    }
}