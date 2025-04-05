package com.example.demo6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HomeController {
    @FXML private ListView<String> recentActivitiesListView;

    @FXML
    public void initialize() {
        // Статик мэдээлэл
        ObservableList<String> activities = FXCollections.observableArrayList(
                "New student registered (John Doe)",
                "Course CS101 updated",
                "Teacher assignment changed for Math 202",
                "System maintenance scheduled for next weekend",
                "New academic calendar uploaded"
        );

        recentActivitiesListView.setItems(activities);
    }
}