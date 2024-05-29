package db.view;

import db.controller.DB2024Team13_connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB2024Team13_utils {

    public static void deleteRestaurant(String restaurantName) {
        String deleteOrderQuery = "DELETE FROM DB2024_order WHERE rest_name = ?";
        String deleteReviewQuery = "DELETE FROM DB2024_review WHERE rest_name = ?";
        String deleteBookmarkQuery = "DELETE FROM DB2024_bookmark WHERE rest_name = ?";
        String deleteMenuQuery = "DELETE FROM DB2024_menu WHERE rest_name = ?";
        String deleteRestaurantQuery = "DELETE FROM DB2024_restaurant WHERE rest_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery);
             PreparedStatement deleteReviewStmt = conn.prepareStatement(deleteReviewQuery);
             PreparedStatement deleteBookmarkStmt = conn.prepareStatement(deleteBookmarkQuery);
             PreparedStatement deleteMenuStmt = conn.prepareStatement(deleteMenuQuery);
             PreparedStatement deleteRestaurantStmt = conn.prepareStatement(deleteRestaurantQuery)) {

            // 레스토랑 관련 데이터 삭제
            deleteOrderStmt.setString(1, restaurantName);
            deleteOrderStmt.executeUpdate();

            deleteReviewStmt.setString(1, restaurantName);
            deleteReviewStmt.executeUpdate();

            deleteBookmarkStmt.setString(1, restaurantName);
            deleteBookmarkStmt.executeUpdate();

            deleteMenuStmt.setString(1, restaurantName);
            deleteMenuStmt.executeUpdate();

            deleteRestaurantStmt.setString(1, restaurantName);
            deleteRestaurantStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
