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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.PreparedStatement;

public class CoursesController {
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField creditsField;
    @FXML private TextArea descriptionArea;

    @FXML private ListView<HBox> courseListView;

    @FXML
    public void initialize() {
        loadCourses();
    }

    @FXML
    private Button AddCourseButton;

    @FXML
    public void handleAddCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo6/add-course-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Course");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSave() {
        String courseName = courseNameField.getText();
        String courseCode = courseCodeField.getText();
        String description = descriptionArea.getText();
        String creditsText = creditsField.getText();

        if (courseName.isEmpty() || courseCode.isEmpty() || creditsText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsText);
            if (credits <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Credits must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Credits must be a number.");
            return;
        }

        String sql = "INSERT INTO Хичээл (нэр, хичээл_id, кредит, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseName);
            pstmt.setString(2, courseCode);
            pstmt.setInt(3, credits);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully.");
                loadCourses();

                // Цонхыг хаана
                Stage stage = (Stage) courseNameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add course.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
        }
    }

    public void loadCourses() {
        ObservableList<HBox> courseCards = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Хичээл";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String courseCode = rs.getString("хичээл_id");
                String courseName = rs.getString("нэр");
                int credits = rs.getInt("кредит");
                String createdAt = rs.getString("created_at");

                HBox courseCard = createCourseCard(courseName, courseCode, createdAt, credits);
                courseCards.add(courseCard);
            }

            Platform.runLater(() -> {
                if (courseListView != null) {
                    courseListView.setItems(courseCards);
                } else {
                    System.err.println("courseListView is null. Cannot set items.");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load course data: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        System.out.println("Cancel button clicked!");
    }

    private HBox createCourseCard(String courseName, String courseCode, String description, int credits) {
        ImageView courseIcon = new ImageView(new Image(getClass().getResource("/com/example/demo6/default-profile.png").toString()));
        courseIcon.setFitHeight(50);
        courseIcon.setFitWidth(50);

        VBox detailsBox = new VBox(5);
        Label nameLabel = new Label(courseName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label codeLabel = new Label("Code: " + courseCode);
        codeLabel.setStyle("-fx-text-fill: #1e88e5; -fx-font-weight: bold; -fx-font-size: 12;");

        Label creditsLabel = new Label("Credits: " + credits);
        creditsLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12;");
        descriptionLabel.setWrapText(true);

        detailsBox.getChildren().addAll(nameLabel, codeLabel, creditsLabel, descriptionLabel);

        HBox card = new HBox(15, courseIcon, detailsBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;");
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