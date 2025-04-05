package com.example.demo6;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentDashboardController {
    @FXML
    private Label welcomeTexts;

    public void setUsername(String username) {
        welcomeTexts.setText("Welcome, " + username);
    }
}
