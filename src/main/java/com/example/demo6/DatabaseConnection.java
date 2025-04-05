package com.example.demo6;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/registration.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Нэвтрэх үед хэрэглэгчийн өгөгдлийг өгнө
    public static UserData getUserData(String email, String password) {
        UserData userData = null;
        Connection conn = null;
        PreparedStatement pstStudent = null;
        PreparedStatement pstTeacher = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            // Сурагч хүснэгтийг шалгана
            pstStudent = conn.prepareStatement(
                    "SELECT * FROM Сурагч WHERE имэйл = ? AND нууцҮг = ?");
            pstStudent.setString(1, email);
            pstStudent.setString(2, password);
            rs = pstStudent.executeQuery();

            if (rs.next()) {
                userData = extractUserData(rs);
            } else {
                // Дараа нь багш
                rs.close();
                pstTeacher = conn.prepareStatement(
                        "SELECT * FROM Багш WHERE имэйл = ? AND нууцҮг = ?");
                pstTeacher.setString(1, email);
                pstTeacher.setString(2, password);
                rs = pstTeacher.executeQuery();

                if (rs.next()) {
                    userData = extractUserData(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstStudent, pstTeacher, conn);
        }

        return userData;
    }
    public static String getUsernameByEmail(String email) {
        String query = "SELECT нэр FROM Сурагч WHERE имэйл = ? " +
                "UNION " +
                "SELECT нэр FROM Багш WHERE имэйл = ? " +
                "UNION " +
                "SELECT нэр FROM Админ WHERE имэйл = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, email);
            stmt.setString(3, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("нэр"); // Эхний таарсан нэрийг буцаана
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    // Хэрэглэгчийн өгөгдлийг буцаахал тусална.
    private static UserData extractUserData(ResultSet rs) throws SQLException {
        return new UserData(
                rs.getString("id"),
                rs.getString("нэр"),
                rs.getString("овог"),
                rs.getString("имэйл"),
                rs.getString("танхим_id"),
                rs.getString("статус"),
                rs.getString("Хаяг"),
                rs.getString("төрсөнОнСар"),
                rs.getBytes("Зураг")
        );
    }
    // Өгөдлийн санг хаах туслагч функц
    private static void closeResources(ResultSet rs, PreparedStatement pst1,
                                       PreparedStatement pst2, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (pst1 != null) pst1.close();
            if (pst2 != null) pst2.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String checkUserCredentials(String email, String password) {

        String query = "SELECT статус FROM Сурагч WHERE имэйл = ? AND нууцҮг = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("статус");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        query = "SELECT статус FROM Багш WHERE имэйл = ? AND нууцҮг = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("статус");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        query = "SELECT статус FROM Админ WHERE имэйл = ? AND нууцҮг = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("статус");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



}