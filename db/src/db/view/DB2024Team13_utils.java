package db.view;

import db.controller.DB2024Team13_connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB2024Team13_utils {

    public static void deleteRestaurant(String restaurantName) {
        String[] deleteQueries = {
            "DELETE FROM DB2024_order WHERE rest_name = ?",
            "DELETE FROM DB2024_review WHERE rest_name = ?",
            "DELETE FROM DB2024_bookmark WHERE rest_name = ?",
            "DELETE FROM DB2024_menu WHERE rest_name = ?",
            "DELETE FROM DB2024_restaurant WHERE rest_name = ?"
        };

        try (Connection conn = DB2024Team13_connection.getConnection()) {
            for (String query : deleteQueries) {
                executeDeleteQuery(conn, query, restaurantName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeDeleteQuery(Connection conn, String query, String restaurantName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, restaurantName);
            stmt.executeUpdate();
        }
    }
}
