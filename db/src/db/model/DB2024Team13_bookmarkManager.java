package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 식당 즐겨찾기를 관리하는 클래스입니다.
 * <p>이 클래스는 즐겨찾기 확인, 추가 및 삭제 기능을 포함합니다.</p>
 */
public class DB2024Team13_bookmarkManager {
    
    /**
     * 학생이 특정 레스토랑을 즐겨찾기 했는지 확인합니다.
     * 
     * @param studentId 학생의 ID
     * @param restaurantName 레스토랑의 이름
     * @return 해당 레스토랑이 학생에 의해 즐겨찾기 되어있으면 true, 아니면 false
     */
    public static boolean isRestaurantBookmarked(String studentId, String restaurantName) {
        String query = "SELECT COUNT(*) FROM DB2024_bookmark USE INDEX (idx_bookmark_student_rest) WHERE student_id = ? AND rest_name = ?";
        return executeCountQuery(query, studentId, restaurantName);
    }

    /**
     * 주어진 쿼리를 실행하여 결과의 개수를 반환합니다.
     * 
     * @param query 실행할 쿼리
     * @param param1 첫 번째 매개변수
     * @param param2 두 번째 매개변수
     * @return 쿼리 결과의 개수가 0보다 크면 true, 아니면 false
     */
    private static boolean executeCountQuery(String query, String param1, String param2) {
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param1);
            stmt.setString(2, param2);
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
     * 학생의 즐겨찾기에 레스토랑을 추가합니다.
     * 
     * @param studentId 학생의 ID
     * @param restaurantName 레스토랑의 이름
     */
    public static void addBookmark(String studentId, String restaurantName) {
        String query = "INSERT INTO DB2024_bookmark (student_id, rest_name) VALUES (?, ?)";
        executeUpdate(query, studentId, restaurantName);
    }

    /**
     * 학생의 즐겨찾기에서 레스토랑을 제거합니다.
     * 
     * @param studentId 학생의 ID
     * @param restaurantName 레스토랑의 이름
     */
    public static void removeBookmark(String studentId, String restaurantName) {
        String query = "DELETE FROM DB2024_bookmark WHERE student_id = ? AND rest_name = ?";
        executeUpdate(query, studentId, restaurantName);
    }

    /**
     * 주어진 쿼리를 실행합니다.
     * 
     * @param query 실행할 쿼리
     * @param param1 첫 번째 매개변수
     * @param param2 두 번째 매개변수
     */
    private static void executeUpdate(String query, String param1, String param2) {
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param1);
            stmt.setString(2, param2);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
