package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 * 사용자 정보를 관리하는 클래스입니다.
 * <p>이 클래스는 사용자의 식당 정보 필터링 및 주문 검색 기능을 제공합니다.</p>
 */
public class DB2024Team13_userInfoManager {

    /**
     * 사용자 정보를 기반으로 식당을 필터링하는 메소드입니다.
     *
     * @param query     실행할 SQL 쿼리
     * @param listModel 결과를 추가할 DefaultListModel
     */
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
                        double star = rs.getDouble("star");
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

    /**
     * 사용자의 주문을 검색하는 메소드입니다.
     *
     * @param tableModel 결과를 추가할 DefaultTableModel
     */
    public static void searchOrder(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        String query = "SELECT rest_name, menu_name, order_date_time FROM DB2024_order USE INDEX (idx_order_student_date) WHERE student_id = ? ORDER BY order_date_time";

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
