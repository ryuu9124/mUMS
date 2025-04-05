package com.example.demo6;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TeacherDashboard {
    public void show(Stage stage, String username) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);


            TeacherDashboardController controller = loader.getController();
            controller.setUsername(username);

            stage.setTitle("Teacher Dashboard");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
