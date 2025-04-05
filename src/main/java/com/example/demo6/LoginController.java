package com.example.demo6;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo6/registration-form.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 800);
            Stage registrationStage = new Stage();
            registrationStage.setTitle("Registration");
            registrationStage.setScene(scene);
            registrationStage.show();
            // Нэвтрэх цонхыг хаана
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open registration form.");
        }
    }

    @FXML
    protected void onLoginButtonClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Талбар хоосон эсэхийг шалгана
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both email and password.");
            return;
        }

        // Хэрэглэгчийн статусыг авна
        String status = DatabaseConnection.checkUserCredentials(email, password);

        // Хэрвээ статус байхгүй бол эррор заана
        if (status == null) {
            showAlert("Login Failed", "Invalid email or password.");
            return; // Don't close the login window if credentials are invalid
        }

        // Статусаас нь хамаарч өөр цонх харуулна
        switch (status) {
            case "admin":
                Dashboard adminDashboard = new Dashboard();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                adminDashboard.show(stage, emailField.getText());
                break;

            case "student":

                String username = DatabaseConnection.getUsernameByEmail(email);


                if (username != null) {
                    // If username is found, show the student dashboard
                    StudentDashboard studentDashboard = new StudentDashboard();
                    studentDashboard.show(new Stage(), username);
                    // Close the login window after the dashboard is shown
                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();
                } else {
                    // If no username is found for the email
                    showAlert("Error", "No student found with the provided email.");
                }
                break;

            case "teacher":
                String usernameOfTeacher = DatabaseConnection.getUsernameByEmail(email);

                if (usernameOfTeacher != null) {

                    TeacherDashboard teacherDashboard = new TeacherDashboard();
                    teacherDashboard.show(new Stage(), usernameOfTeacher);
                    // Close the login window after the dashboard is shown
                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();
                } else {
                    // If no username is found for the email
                    showAlert("Error", "No student found with the provided email.");
                }
                break;

            default:
                // Handle any unexpected cases (if any)
                showAlert("Error", "Unknown user status.");
                break;
        }
    }


    // Хэрэглэгчийн мэдээллийг өгөгдлийн сангаас шалгана
      /*  if (DatabaseConnection.checkUserCredentials(email, password)) {
            // Instead of loading a new scene, directly open the dashboard
            openDashboard();
            // Close the login window
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }*/


    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




    private void openDashboard() {

        Dashboard dashboard = new Dashboard();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        dashboard.show(stage, emailField.getText());
    }
}
