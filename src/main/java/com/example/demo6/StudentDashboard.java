package com.example.demo6;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentDashboard {
    public void show(Stage stage, String username) {
        try {
            // FXML файлыг ачааллана
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);

            // Controller
            StudentDashboardController controller = loader.getController();
            controller.setUsername(username);

            stage.setTitle("Student Dashboard");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
