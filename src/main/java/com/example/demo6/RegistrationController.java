package com.example.demo6;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import java.nio.file.Files;
import java.io.IOException;

public class RegistrationController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField studentIdField;
    @FXML private TextField departureIdField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private DatePicker birthdayPicker;
    @FXML private TextField addressField;
    @FXML private Button registerButton;
    @FXML private Button cancelButton;
    @FXML private ImageView profileImageView;
    @FXML private Circle profileCircle;
    @FXML private Button uploadButton;

    private byte[] profileImageBytes;
    private String status;

    @FXML
    private void initialize() {
        // Зургийг дугуй болгох
        if (profileCircle != null) {
            double radius = profileCircle.getRadius();
            profileImageView.setFitWidth(radius * 2);
            profileImageView.setFitHeight(radius * 2);
            profileImageView.setPreserveRatio(true);
            profileImageView.setClip(new Circle(radius, radius, radius));
        }

        // Танхимын ID-г шинэчлэхийн тулд event listener ашиглав.
        studentIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateDepartureIdAndStatus(newValue);
        });
    }

    @FXML
    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");

        // Файл сонгох филтер
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Файл сонгох цонх үүсгэх
        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(image);

                // Зургийг дугуй болгоно
                if (profileCircle != null) {
                    double radius = profileCircle.getRadius();
                    profileImageView.setClip(new Circle(radius, radius, radius));
                }

                // Зургийг байт болгох
                profileImageBytes = Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load image: " + e.getMessage());
            }
        }
    }

    /**
     * Статус болон танхимын id-г шинэчлэх метод
     */
    private void updateDepartureIdAndStatus(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            departureIdField.setText("");
            status = null;
            return;
        }

        String upperStudentId = studentId.toUpperCase();

        if (upperStudentId.startsWith("SE")) {
            departureIdField.setText("CS1");
            status = "student";
        } else if (upperStudentId.startsWith("IT")) {
            departureIdField.setText("ST");
            status = "student";
        } else if (upperStudentId.startsWith("EE")) {
            departureIdField.setText("TE");
            status = "student";
        } else if (upperStudentId.startsWith("ME")) {
            departureIdField.setText("AE");
            status = "student";
        } else if (upperStudentId.startsWith("T")) {
            departureIdField.setText("TR");
            status = "teacher";
        } else if (upperStudentId.startsWith("A")) {
            departureIdField.setText("AD");
            status = "admin";
        } else {
            departureIdField.setText("");
            status = null;
        }
    }

    @FXML
    private void handleRegister() {
        String studentId = studentIdField.getText();

        if (studentId == null || studentId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Student ID is required!");
            return;
        }

        //Статус болон танхимын id-г дахин шалгаж баталгаажуулав
        updateDepartureIdAndStatus(studentId);

        //Тохирох статус олдсон эсэх
        if (status == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid ID",
                    "The ID format is invalid. Please enter a valid ID starting with SE, IT, EE, ME, T, or A.");
            return;
        }

        // Validation хийгээд өгөгдлийг хадгалана
        if (validateInputs()) {
            if (saveRegistrationData()) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration completed successfully!");
                openDashboard();
            }
        }
    }

    private boolean validateInputs() {
        // Талбарыг хоосон эсэхийг шалгана.
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty() || birthdayPicker.getValue() == null ||
                addressField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return false;
        }

        // И мэйл
        String email = emailField.getText();
        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format!");
            return false;
        }

        // Нууц үг
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return false;
        }

        // Нууц үг урт
        if (passwordField.getText().length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters long!");
            return false;
        }

        return true;
    }

    private boolean saveRegistrationData() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String birthday = (birthdayPicker.getValue() != null) ? birthdayPicker.getValue().toString() : "";
        String address = addressField.getText();
        String id = studentIdField.getText();
        String departureId = departureIdField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect to database.");
                return false;
            }

            if ("admin".equals(status)) {
                String adminQuery = "INSERT INTO Админ (id, нэр, статус, овог, имэйл, нууцҮг, төрсөнОнСар, Зураг, Хаяг, ОнСар) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
                try (PreparedStatement pst = conn.prepareStatement(adminQuery)) {
                    pst.setString(1, id);
                    pst.setString(2, firstName);
                    pst.setString(3, status);
                    pst.setString(4, lastName);
                    pst.setString(5, email);
                    pst.setString(6, password);
                    pst.setString(7, birthday);
                    pst.setBytes(8, (profileImageBytes != null) ? profileImageBytes : null);
                    pst.setString(9, address);
                    return pst.executeUpdate() > 0;
                }
            } else if ("teacher".equals(status)) {
                String teacherQuery = "INSERT INTO Багш (id, нэр, овог, имэйл, нууцҮг, төрсөнОнСар, Зураг, Хаяг, танхим_id, статус, ОнСар) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
                try (PreparedStatement pst = conn.prepareStatement(teacherQuery)) {
                    pst.setString(1, id);
                    pst.setString(2, firstName);
                    pst.setString(3, lastName);
                    pst.setString(4, email);
                    pst.setString(5, password);
                    pst.setString(6, birthday);
                    pst.setBytes(7, (profileImageBytes != null) ? profileImageBytes : null);
                    pst.setString(8, address);
                    pst.setString(9, departureId);
                    pst.setString(10, status);
                    return pst.executeUpdate() > 0;
                }
            } else if ("student".equals(status)) {
                String studentQuery = "INSERT INTO Сурагч (id, нэр, овог, имэйл, нууцҮг, төрсөнОнСар, Зураг, Хаяг, танхим_id, статус, ОнСар) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

                try (PreparedStatement pst = conn.prepareStatement(studentQuery)) {
                    pst.setString(1, id);
                    pst.setString(2, firstName);
                    pst.setString(3, lastName);
                    pst.setString(4, email);
                    pst.setString(5, password);
                    pst.setString(6, birthday);

                    if (profileImageBytes != null) {
                        pst.setBytes(7, profileImageBytes);
                    } else {
                        pst.setNull(7, java.sql.Types.BLOB);
                    }

                    pst.setString(8, address);
                    pst.setString(9, departureId);
                    pst.setString(10, status);

                    return pst.executeUpdate() > 0;
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid user status.");
                return false;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }




    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openDashboard() {
        // Хэрэглэгчийн өгөгдлийг дамжуулаад Dashboard нээнэ.
        Dashboard dashboard = new Dashboard();
        Stage stage = (Stage) registerButton.getScene().getWindow();
        dashboard.show(stage, firstNameField.getText());
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}