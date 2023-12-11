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
            String encryptedPassword = EncryptionUtil.encrypt(password, PasswordManagerApp.getMasterPassword());
            if (encryptedPassword != null) {
                File file = new File(FILE_NAME);
                FileWriter fr = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(fr);

                br.write(url + ":" + username + ":" + encryptedPassword);
                br.newLine();

                br.close();
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePasswordFile(ObservableList<PasswordEntry> data) {
        try {
            File file = new File(FILE_NAME);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            for (PasswordEntry entry : data) {
                String url = entry.getUrl();
                String username = entry.getUsername();
                String password = entry.getPassword();

                String encryptedPassword = EncryptionUtil.encrypt(password, PasswordManagerApp.getMasterPassword());
                if (encryptedPassword != null) {
                    bw.write(url + ":" + username + ":" + encryptedPassword);
                    bw.newLine();
                }
            }

            bw.close();
            fw.close();
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