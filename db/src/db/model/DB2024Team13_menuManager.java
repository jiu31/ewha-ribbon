package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 메뉴를 관리하는 클래스입니다.
 * <p>이 클래스는 메뉴 아이템을 추가, 수정, 삭제 및 조회하는 기능을 제공합니다.</p>
 */
public class DB2024Team13_menuManager {

    /**
     * 새로운 메뉴 아이템을 추가하는 메소드입니다.
     *
     * @param restaurantName 메뉴를 추가할 레스토랑 이름
     * @param menuName 추가할 메뉴의 이름
     * @param price 메뉴의 가격
     * @param isVegan 메뉴가 비건인지 여부
     */
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

    /**
     * 기존의 메뉴 아이템을 수정하는 메소드입니다.
     *
     * @param restaurantName 메뉴가 속한 레스토랑 이름
     * @param menuName 수정할 메뉴의 이름
     * @param price 수정할 메뉴의 가격
     * @param isVegan 수정할 메뉴가 비건인지 여부
     */
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

    /**
     * 트랜잭션을 사용하여 기존의 메뉴 아이템을 삭제하는 메소드입니다.
     *
     * @param restaurantName 삭제할 메뉴가 속한 레스토랑 이름
     * @param menuName 삭제할 메뉴의 이름
     * @throws SQLException SQL 예외가 발생할 경우
     */
    public static void deleteMenuItem(String restaurantName, String menuName) throws SQLException {
        String deleteOrderQuery = "DELETE FROM DB2024_order WHERE rest_name = ? AND menu_name = ?";
        String deleteMenuQuery = "DELETE FROM DB2024_menu WHERE rest_name = ? AND menu_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery);
             PreparedStatement deleteMenuStmt = conn.prepareStatement(deleteMenuQuery)) {

            conn.setAutoCommit(false); // 트랜잭션 시작

            deleteOrderStmt.setString(1, restaurantName);
            deleteOrderStmt.setString(2, menuName);
            deleteOrderStmt.executeUpdate();

            deleteMenuStmt.setString(1, restaurantName);
            deleteMenuStmt.setString(2, menuName);
            deleteMenuStmt.executeUpdate();

            conn.commit(); // 트랜잭션 커밋
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DB2024Team13_connection.getConnection()) {
                conn.rollback(); // 트랜잭션 롤백
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    /**
     * 특정 레스토랑의 모든 메뉴 아이템을 불러오는 메소드입니다.
     *
     * @param restaurantName 메뉴를 불러올 레스토랑 이름
     * @return ResultSet 객체로 메뉴 정보들을 반환합니다.
     * @throws SQLException SQL 예외가 발생할 경우
     */
    public static ResultSet loadMenuItems(String restaurantName) throws SQLException {
        String menuQuery = "SELECT menu_name, price, vegan FROM DB2024_menu WHERE rest_name = ?";
        Connection conn = DB2024Team13_connection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(menuQuery);
        stmt.setString(1, restaurantName);
        return stmt.executeQuery();
    }
}
