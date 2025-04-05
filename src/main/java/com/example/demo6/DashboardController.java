package com.example.demo6;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardController {
    @FXML private Label welcomeText;
    @FXML private Label contentTitle;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalTeachersLabel;
    @FXML private Label totalCoursesLabel;
    @FXML private StackPane contentArea;

    @FXML private Button homeButton;
    @FXML private Button studentsButton;
    @FXML private Button teachersButton;
    @FXML private Button coursesButton;
    @FXML private Button courseAllocationButton;
    @FXML private Button settingsButton;

    @FXML
    public void initialize() {
        // Event handling аргууд
        homeButton.setOnAction(event -> switchToHome());
        studentsButton.setOnAction(event -> switchToStudents());
        teachersButton.setOnAction(event -> switchToTeachers());
        coursesButton.setOnAction(event -> switchToCourses());
        courseAllocationButton.setOnAction(event -> switchToCourseAllocation());
        settingsButton.setOnAction(event -> switchToSettings());
        updateStatistics();
    }

    private void hideAllPages() {
        contentArea.getChildren().forEach(page -> page.setVisible(false));
    }

    private void switchToHome() {
        hideAllPages();
        contentArea.lookup("#homePage").setVisible(true);
        contentTitle.setText("");
        updateStatistics();
    }

    private void switchToStudents() {
        hideAllPages();
        contentArea.lookup("#studentsPage").setVisible(true);
        contentTitle.setText("Students");
        updateStatistics();
    }

    private void switchToTeachers() {
        hideAllPages();
        contentArea.lookup("#teachersPage").setVisible(true);
        contentTitle.setText("Teachers");
        updateStatistics();
    }

    private void switchToCourses() {
        hideAllPages();
        contentArea.lookup("#coursesPage").setVisible(true);
        contentTitle.setText("Courses");
        updateStatistics();
    }

    private void switchToCourseAllocation() {
        hideAllPages();
        contentArea.lookup("#courseAllocationPage").setVisible(true);
        contentTitle.setText("Course Allocation");
        updateStatistics();
    }

    private void switchToSettings() {
        hideAllPages();
        contentArea.lookup("#settingsPage").setVisible(true);
        contentTitle.setText("Settings");
        updateStatistics();
    }

    public void setUsername(String username) {
        welcomeText.setText("Welcome, " + username);
    }

    @FXML
    private void handleLogout() {
        try {
            // Цонхыг хаана
            Stage currentStage = (Stage) welcomeText.getScene().getWindow();
            currentStage.close();

            // Нэвтрэх цонхыг нээнэ
            Stage loginStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            loginStage.setTitle("Login");
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login screen.");
        }
    }
    private void updateStatistics() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Сурагчдийн тоог шинэчлэнэ
            ResultSet rsStudents = stmt.executeQuery("SELECT COUNT(*) as count FROM Сурагч");
            if (rsStudents.next()) {
                int studentCount = rsStudents.getInt("count");
                Platform.runLater(() -> totalStudentsLabel.setText(String.valueOf(studentCount)));
            }


            ResultSet rsTeachers = stmt.executeQuery("SELECT COUNT(*) as count FROM Багш");
            if (rsTeachers.next()) {
                int teacherCount = rsTeachers.getInt("count");
                Platform.runLater(() -> totalTeachersLabel.setText(String.valueOf(teacherCount)));
            }


            ResultSet rsCourses = stmt.executeQuery("SELECT COUNT(*) as count FROM Хичээл");
            if (rsCourses.next()) {
                int courseCount = rsCourses.getInt("count");
                Platform.runLater(() -> totalCoursesLabel.setText(String.valueOf(courseCount)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load statistics.");
        }
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
