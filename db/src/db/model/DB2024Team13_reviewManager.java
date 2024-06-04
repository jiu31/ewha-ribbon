package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_reviewManager {
    // 리뷰 추가
    public static boolean addReview(String restaurantName, String studentId, int star) {
        String query1 = "SELECT MAX(review_id) FROM DB2024_review";
        String query2 = "INSERT INTO DB2024_review (review_id, rest_name, student_id, star) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt1 = conn.prepareStatement(query1);
             PreparedStatement pStmt2 = conn.prepareStatement(query2)) {
            ResultSet rs = pStmt1.executeQuery();
            int newReviewId = 1;
            if (rs.next()) {
                newReviewId = rs.getInt(1) + 1;
            }
            pStmt2.setInt(1, newReviewId);
            pStmt2.setString(2, restaurantName);
            pStmt2.setString(3, studentId);
            pStmt2.setInt(4, star);
            pStmt2.executeUpdate();
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
}
