package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 로그인 기능을 관리하는 클래스입니다.
 */
public class DB2024Team13_loginManager {

    /**
     * 주어진 학번과 비밀번호를 사용하여 사용자를 검증하는 메소드입니다.
     *
     * @param studentId 검증할 사용자의 학번
     * @param password 검증할 사용자의 비밀번호
     * @return 로그인 성공 시 true, 실패 시 false를 반환합니다.
     */
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
