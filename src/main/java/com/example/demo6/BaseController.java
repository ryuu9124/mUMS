package com.example.demo6;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public abstract class BaseController {
    protected void showAlert(Alert.AlertType alertType, String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    // Нийтлэг аргуудыг нэмж болно
}