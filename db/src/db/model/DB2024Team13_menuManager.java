package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_menuManager {
    // 메뉴 추가
    public static void addMenuItem(String restaurantName, String menuName, int price, boolean isVegan) {
        String insertMenuQuery = "INSERT INTO DB2024_menu (rest_name, menu_name, price, vegan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertMenuQuery)) {
            stmt.setString(1, restaurantName);
            stmt.setString(2, menuName);
            stmt.setInt(3, price);
            stmt.setBoolean(4, isVegan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메뉴 수정
    public static void updateMenuItem(String restaurantName, String menuName, int price, boolean isVegan) {
        String updateMenuQuery = "UPDATE DB2024_menu SET price = ?, vegan = ? WHERE rest_name = ? AND menu_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateMenuQuery)) {
            stmt.setInt(1, price);
            stmt.setBoolean(2, isVegan);
            stmt.setString(3, restaurantName);
            stmt.setString(4, menuName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메뉴 삭제
    public static void deleteMenuItem(String restaurantName, String menuName) throws SQLException {
        String deleteOrderQuery = "DELETE FROM DB2024_order WHERE rest_name = ? AND menu_name = ?";
        String deleteMenuQuery = "DELETE FROM DB2024_menu WHERE rest_name = ? AND menu_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery);
             PreparedStatement deleteMenuStmt = conn.prepareStatement(deleteMenuQuery)) {
            deleteOrderStmt.setString(1, restaurantName);
            deleteOrderStmt.setString(2, menuName);
            deleteOrderStmt.executeUpdate();
            deleteMenuStmt.setString(1, restaurantName);
            deleteMenuStmt.setString(2, menuName);
            deleteMenuStmt.executeUpdate();
        }
    }

    // 메뉴 불러오기
    public static ResultSet loadMenuItems(String restaurantName) throws SQLException {
        String menuQuery = "SELECT menu_name, price, vegan FROM DB2024_menu WHERE rest_name = ?";
        Connection conn = DB2024Team13_connection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(menuQuery);
        stmt.setString(1, restaurantName);
        return stmt.executeQuery();
    }
}
