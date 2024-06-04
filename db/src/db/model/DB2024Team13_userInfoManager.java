package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class DB2024Team13_userInfoManager {
    // 사용자 정보 필터링
    public static void filterRestaurant(String query, DefaultListModel<String> listModel) {
        listModel.clear();
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query)) {
            String studentId = DB2024Team13_userSessionManager.getInstance().getStudentId();
            pStmt.setString(1, studentId);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    String restaurantName = rs.getString("rest_name");
                    if (rs.getMetaData().getColumnCount() > 1) {
                        int star = rs.getInt("star");
                        listModel.addElement(restaurantName + " - 별점: " + star);
                    } else {
                        listModel.addElement(restaurantName);
                    }
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // 주문 검색
    public static void searchOrder(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        String query = "SELECT rest_name, menu_name, order_date_time FROM DB2024_order WHERE student_id = ? ORDER BY order_date_time";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query)) {
            pStmt.setString(1, DB2024Team13_userSessionManager.getInstance().getStudentId());
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{rs.getString("order_date_time"), rs.getString("rest_name"), rs.getString("menu_name")});
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
