package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB2024Team13_transactionManager {

    /**
     * 트랜잭션을 사용하여 새로운 식당을 데이터베이스에 추가합니다.
     * @param name 식당 이름
     * @param location 위치
     * @param bestMenu 대표 메뉴
     * @param category 카테고리
     * @param breakTime 브레이크타임 여부
     * @param sectionName 구역 이름
     * @param eatAlone 혼밥 가능 여부
     * @return 성공 여부를 boolean 값으로 반환
     */
    public static boolean addRestaurantWithTransaction(String name, String location, String bestMenu, String category, boolean breakTime,
                                                       String sectionName, boolean eatAlone) {
        String restaurantSql = "INSERT INTO DB2024_restaurant (rest_name, location, best_menu, category, breaktime, section_name, eat_alone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement restaurantStmt = conn.prepareStatement(restaurantSql)) {

            conn.setAutoCommit(false);

            restaurantStmt.setString(1, name);
            restaurantStmt.setString(2, location);
            restaurantStmt.setString(3, bestMenu);
            restaurantStmt.setString(4, category);
            restaurantStmt.setBoolean(5, breakTime);
            restaurantStmt.setString(6, sectionName);
            restaurantStmt.setBoolean(7, eatAlone);
            restaurantStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollbackTransaction();
            return false;
        }
    }

    /**
     * 트랜잭션을 롤백합니다.
     */
    private static void rollbackTransaction() {
        try (Connection conn = DB2024Team13_connection.getConnection()) {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
