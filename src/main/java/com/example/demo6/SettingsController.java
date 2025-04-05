package com.example.demo6;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsController {
    @FXML private ListView<HBox> settingsListView;

    @FXML
    public void initialize() {
        loadSettings();
    }

    public void loadSettings() {
        // Settings-ийн UI үүсгэнэ
        ObservableList<HBox> settingsItems = FXCollections.observableArrayList();

        //Settings items
        HBox profileSettingsCard = createSettingsCard("Profile Settings", "Update your personal information");
        HBox securitySettingsCard = createSettingsCard("Security Settings", "Change password and security options");
        HBox notificationSettingsCard = createSettingsCard("Notification Settings", "Manage your notifications");
        HBox systemSettingsCard = createSettingsCard("System Settings", "Configure system parameters");
        HBox backupSettingsCard = createSettingsCard("Backup & Restore", "Manage database backups");

        settingsItems.addAll(profileSettingsCard, securitySettingsCard, notificationSettingsCard,
                systemSettingsCard, backupSettingsCard);

        Platform.runLater(() -> settingsListView.setItems(settingsItems));
    }

    private HBox createSettingsCard(String title, String description) {
        // Settings button
        Button settingsOption = new Button(title);
        settingsOption.setPrefWidth(200);
        settingsOption.setStyle("-fx-background-color: #f0f0f0; -fx-font-weight: bold;");

        // Button-ны action
        settingsOption.setOnAction(e -> showSettingsDialog(title));

        // Settings details
        VBox detailsBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14;");
        descriptionLabel.setWrapText(true);

        detailsBox.getChildren().addAll(titleLabel, descriptionLabel);

        // Create card container
        HBox card = new HBox(15, settingsOption, detailsBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;");

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;"));

        return card;
    }

    private void showSettingsDialog(String settingType) {
        showAlert(Alert.AlertType.INFORMATION, "Settings", settingType + " dialog will be implemented soon.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}