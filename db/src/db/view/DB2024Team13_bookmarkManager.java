package db.view;

import db.controller.DB2024Team13_connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_bookmarkManager {

    /**
     * 주어진 레스토랑이 북마크되었는지 확인하는 메소드
     *
     * @param studentId 학생 ID
     * @param restaurantName 레스토랑 이름
     * @return 북마크 여부
     */
    public static boolean isRestaurantBookmarked(String studentId, String restaurantName) {
        String query = "SELECT COUNT(*) FROM DB2024_bookmark WHERE student_id = ? AND rest_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, restaurantName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 주어진 레스토랑을 북마크에 추가하는 메소드
     *
     * @param studentId 학생 ID
     * @param restaurantName 레스토랑 이름
     */
    public static void addBookmark(String studentId, String restaurantName) {
        String query = "INSERT INTO DB2024_bookmark (student_id, rest_name) VALUES (?, ?)";
        executeUpdate(query, studentId, restaurantName);
    }

    /**
     * 주어진 레스토랑을 북마크에서 삭제하는 메소드
     *
     * @param studentId 학생 ID
     * @param restaurantName 레스토랑 이름
     */
    public static void removeBookmark(String studentId, String restaurantName) {
        String query = "DELETE FROM DB2024_bookmark WHERE student_id = ? AND rest_name = ?";
        executeUpdate(query, studentId, restaurantName);
    }

    /**
     * 주어진 SQL 업데이트 쿼리를 실행하는 메소드
     *
     * @param query 실행할 쿼리
     * @param params 쿼리 매개변수
     */
    private static void executeUpdate(String query, String... params) {
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
