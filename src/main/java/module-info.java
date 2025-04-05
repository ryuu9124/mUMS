module com.example.demo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;  // 👈 Add this line
    requires java.prefs; // Add this line


    opens com.example.demo6 to javafx.fxml;
    exports com.example.demo6;
}