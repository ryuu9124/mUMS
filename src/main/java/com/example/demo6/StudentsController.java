package com.example.demo6;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentsController {
    @FXML private ListView<HBox> studentListView;

    @FXML
    public void initialize() {
        // Initial load
        loadStudents();
    }

    @FXML
    public void handleAddStudent() {
        // Implement student creation dialog
        showAlert(Alert.AlertType.INFORMATION, "Feature Coming Soon", "Add Student functionality will be available soon.");
    }

    public void loadStudents() {
        ObservableList<HBox> studentCards = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT нэр, овог, имэйл, төрсөнОнСар, Хаяг, Зураг FROM Сурагч WHERE статус='student'")) {

            while (rs.next()) {
                HBox studentCard = createPersonCard(
                        rs.getString("нэр"),
                        rs.getString("овог"),
                        rs.getString("имэйл"),
                        rs.getString("төрсөнОнСар"),
                        rs.getString("Хаяг"),
                        rs.getBytes("Зураг"),
                        "Student"
                );
                studentCards.add(studentCard);
            }

            Platform.runLater(() -> studentListView.setItems(studentCards));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load student data.");
        }
    }

    private HBox createPersonCard(String firstName, String lastName, String email,
                                  String birthday, String address, byte[] imageData, String role) {
        // Profile image
        Image profileImage;
        if (imageData != null && imageData.length > 0) {
            profileImage = new Image(new ByteArrayInputStream(imageData));
        } else {
            profileImage = new Image(getClass().getResource("/com/example/demo6/default-profile.png").toString());
        }

        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(50);
        profileImageView.setFitWidth(50);
        profileImageView.setPreserveRatio(true);

        // Make image circular
        Circle clip = new Circle(25, 25, 25);
        profileImageView.setClip(clip);

        // Person details
        VBox detailsBox = new VBox(5);
        Label nameLabel = new Label(firstName + " " + lastName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label roleLabel = new Label(role);
        roleLabel.setStyle("-fx-text-fill: #1e88e5; -fx-font-weight: bold; -fx-font-size: 12;");

        Label emailLabel = new Label(email);
        emailLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14;");

        Label detailsLabel = new Label(String.format("Birthday: %s • Address: %s", birthday, address));
        detailsLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12;");

        detailsBox.getChildren().addAll(nameLabel, roleLabel, emailLabel, detailsLabel);

        // Create card container
        HBox card = new HBox(15, profileImageView, detailsBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;");

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;"));

        return card;
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