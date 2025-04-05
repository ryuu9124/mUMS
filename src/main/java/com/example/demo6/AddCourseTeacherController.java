package com.example.demo6;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AddCourseTeacherController {
    @FXML private ComboBox<CourseDTO> courseNameBox;
    @FXML private ComboBox<TeacherDTO> teacherComboBox;
    @FXML private Button AddCourseTeacherButton;
    @FXML private Button CancelButton;

    private DatabaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new DatabaseConnection();
        loadTeachers();
        loadCourses();  // Хичээлийг ачааллана.
    }

    private void loadTeachers() {
        List<TeacherDTO> teachers = fetchTeachersFromDatabase();
        teacherComboBox.getItems().addAll(teachers);
    }

    private void loadCourses() {
        List<CourseDTO> courses = fetchCoursesFromDatabase();
        courseNameBox.getItems().addAll(courses);
    }

    private List<TeacherDTO> fetchTeachersFromDatabase() {
        List<TeacherDTO> teachers = new ArrayList<>();
        String query = "SELECT id, нэр, овог FROM Багш";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                teachers.add(new TeacherDTO(
                        rs.getString("id"),
                        rs.getString("нэр"),
                        rs.getString("овог")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teachers;
    }

    private List<CourseDTO> fetchCoursesFromDatabase() {
        List<CourseDTO> courses = new ArrayList<>();
        String query = "SELECT хичээл_id, нэр FROM Хичээл";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                courses.add(new CourseDTO(
                        rs.getString("хичээл_id"),
                        rs.getString("нэр")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    @FXML
    public void addCourse() {
        CourseDTO selectedCourse = courseNameBox.getValue();
        TeacherDTO selectedTeacher = teacherComboBox.getValue();

        if (selectedCourse == null || selectedTeacher == null) {
            System.out.println("Please select a course and a teacher.");
            return;
        }

        try (Connection conn = dbConnection.getConnection()) {
            String insertQuery = "INSERT INTO Багш_Хичээл (багш_id, хичээл_id, админ_id, created_at) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, selectedTeacher.getId());
                pstmt.setString(2, selectedCourse.getId());
                pstmt.setInt(3, 1);  // Default admin ID
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

                pstmt.executeUpdate();
                System.out.println("Course allocated successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Талбарыг цэвэрлэнэ
        courseNameBox.setValue(null);
        teacherComboBox.setValue(null);
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    // DTO- Data Transfer Object Багш нарт зориулсан программын өөр давхаргуудад өгөгдөл дамжуулна.
    public static class TeacherDTO {
        private String id;
        private String firstName;
        private String lastName;

        public TeacherDTO(String id, String firstName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return firstName + " " + lastName;
        }

        public String getId() {
            return id;
        }
    }


    public static class CourseDTO {
        private String id;
        private String name;

        public CourseDTO(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
