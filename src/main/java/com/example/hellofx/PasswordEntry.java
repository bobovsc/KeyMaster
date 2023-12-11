package com.example.hellofx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PasswordEntry {
    private final StringProperty url;
    private final StringProperty username;
    private final StringProperty password;

    public PasswordEntry(String url, String username, String password) {
        this.url = new SimpleStringProperty(url);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public String getUrl() {
        return url.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String newPassword) {
        password.set(newPassword);
    }

    public StringProperty urlProperty() {
        return url;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }
}
