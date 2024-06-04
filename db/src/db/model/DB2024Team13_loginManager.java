package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_loginManager {
    // 로그인 기능을 구현하는 메소드
    public static boolean validateLogin(String studentId, String password) {
        String query = "SELECT * FROM DB2024_customer WHERE student_id = ? AND password = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query)) {
            pStmt.setString(1, studentId);
            pStmt.setString(2, password);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    String nickname = rs.getString("nickname");
                    DB2024Team13_userSessionManager.getInstance().setUser(studentId, nickname);
                    return true;
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }
}
