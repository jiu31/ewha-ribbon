package db.model;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 식당을 관리하는 클래스입니다.
 * <p>이 클래스는 식당 정보를 검색, 추가, 수정, 삭제하고, 식당의 메뉴와 세부 정보를 관리하는 기능을 제공합니다.</p>
 */
public class DB2024Team13_restaurantManager {

    /**
     * 검색 텍스트를 사용하여 식당을 검색하고 결과를 listModel에 추가합니다.
     *
     * @param searchText 검색할 텍스트
     * @param listModel  검색 결과를 추가할 DefaultListModel
     */
    public static void searchRestaurant(String searchText, DefaultListModel<String> listModel) {
        listModel.clear();
        String query = "SELECT rest_name FROM DB2024_restaurant WHERE rest_name LIKE ? ORDER BY rest_name";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query)) {
            pStmt.setString(1, "%" + searchText + "%");
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    String restaurantName = rs.getString("rest_name");
                    listModel.addElement(restaurantName);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    
    /**
     * 메뉴 이름으로 식당을 검색하는 메소드입니다.
     *
     * @param searchText 검색할 메뉴 이름
     * @return 검색된 식당 이름을 포함하는 Set 객체
     */
    public static Set<String> searchRestaurantByMenu(String searchText) {
        Set<String> restaurantSet = new HashSet<>();
        String menuQuery = "SELECT DISTINCT rest_name FROM DB2024_menu WHERE menu_name LIKE ? ORDER BY rest_name";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(menuQuery)) {
            pStmt.setString(1, "%" + searchText + "%");
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    restaurantSet.add(rs.getString("rest_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantSet;
    }

    /**
     * 식당의 세부 정보를 로드합니다.
     *
     * @param restaurantName 식당 이름
     * @return 식당 세부 정보가 포함된 Map
     */
    public static Map<String, Object> loadRestaurantDetails(String restaurantName) {
        Map<String, Object> details = new HashMap<>();
        String detailsQuery = "SELECT best_menu, location, breaktime, eat_alone, category, section_name, AVG(star) as avgRating " +
                "FROM DB2024_restaurant USE INDEX (idx_restaurant_rest_name) " +
                "LEFT JOIN DB2024_review ON DB2024_restaurant.rest_name = DB2024_review.rest_name " +
                "WHERE DB2024_restaurant.rest_name = ? GROUP BY DB2024_restaurant.rest_name, DB2024_restaurant.best_menu, DB2024_restaurant.location, DB2024_restaurant.breaktime, DB2024_restaurant.eat_alone, DB2024_restaurant.category, DB2024_restaurant.section_name";


        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(detailsQuery)) {
            stmt.setString(1, restaurantName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.put("bestMenu", rs.getString("best_menu"));
                    details.put("location", rs.getString("location"));
                    details.put("breaktime", rs.getBoolean("breaktime"));
                    details.put("eatAlone", rs.getBoolean("eat_alone"));
                    details.put("category", rs.getString("category"));
                    details.put("section", rs.getString("section_name"));
                    details.put("avgRating", rs.getDouble("avgRating"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    /**
     * 새로운 식당을 데이터베이스에 추가합니다.
     *
     * @param name       식당 이름
     * @param location   위치
     * @param bestMenu   대표 메뉴
     * @param category   카테고리
     * @param breakTime  브레이크타임 여부
     * @param sectionName 구역 이름
     * @param eatAlone   혼밥 가능 여부
     * @return 성공 여부를 boolean 값으로 반환
     */
    public static boolean addRestaurant(String name, String location, String bestMenu, String category, boolean breakTime,
                                        String sectionName, boolean eatAlone) {
        String restaurantSql = "INSERT INTO DB2024_restaurant (rest_name, location, best_menu, category, breaktime, section_name, eat_alone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement restaurantStmt = conn.prepareStatement(restaurantSql)) {

            restaurantStmt.setString(1, name);
            restaurantStmt.setString(2, location);
            restaurantStmt.setString(3, bestMenu);
            restaurantStmt.setString(4, category);
            restaurantStmt.setBoolean(5, breakTime);
            restaurantStmt.setString(6, sectionName);
            restaurantStmt.setBoolean(7, eatAlone);
            restaurantStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 식당 정보를 수정합니다.
     *
     * @param restaurantName 식당 이름
     * @param category       카테고리
     * @param bestMenu       대표 메뉴
     * @param section        구역
     * @param location       위치
     * @param breaktime      브레이크타임 여부
     * @param eatAlone       혼밥 가능 여부
     */
    public static void updateRestaurantDetails(String restaurantName, String category, String bestMenu, String section, String location, boolean breaktime, boolean eatAlone) {
        String updateQuery = "UPDATE DB2024_restaurant SET category = ?, best_menu = ?, section_name = ?, location = ?, breaktime = ?, eat_alone = ? WHERE rest_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, category);
            stmt.setString(2, bestMenu);
            stmt.setString(3, section);
            stmt.setString(4, location);
            stmt.setBoolean(5, breaktime);
            stmt.setBoolean(6, eatAlone);
            stmt.setString(7, restaurantName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 트랜잭션을 사용하여 식당을 삭제합니다.
     *
     * @param restaurantName 식당 이름
     */
    public static void deleteRestaurant(String restaurantName) {
        String[] deleteQueries = {
                "DELETE FROM DB2024_order WHERE rest_name = ?",
                "DELETE FROM DB2024_review WHERE rest_name = ?",
                "DELETE FROM DB2024_bookmark WHERE rest_name = ?",
                "DELETE FROM DB2024_menu WHERE rest_name = ?",
                "DELETE FROM DB2024_restaurant WHERE rest_name = ?"
        };
        try (Connection conn = DB2024Team13_connection.getConnection()) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            for (String query : deleteQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, restaurantName);
                    stmt.executeUpdate();
                }
            }
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
     * 식당의 메뉴 항목을 가져옵니다.
     *
     * @param restaurantName 식당 이름
     * @return ResultSet 객체에 메뉴 항목이 포함되어 반환
     * @throws SQLException SQL 예외 발생 시
     */
    public static ResultSet getMenuItems(String restaurantName) throws SQLException {
        String menuQuery = "SELECT menu_name, price, vegan " +
                "FROM DB2024_menu " +
                "WHERE rest_name = ?";
        Connection conn = DB2024Team13_connection.getConnection();
        PreparedStatement menuStmt = conn.prepareStatement(menuQuery);
        menuStmt.setString(1, restaurantName);
        return menuStmt.executeQuery();
    }

    /**
     * 카테고리를 가져옵니다.
     *
     * @return 카테고리 목록
     */
    public static String[] getCategories() {
        return getDataFromDatabase("SELECT DISTINCT category FROM DB2024_restaurant", "category");
    }

    /**
     * 섹션을 가져옵니다.
     *
     * @return 섹션 목록
     */
    public static String[] getSections() {
        return getDataFromDatabase("SELECT DISTINCT section FROM DB2024_section", "section");
    }

    /**
     * 데이터베이스에서 데이터를 가져옵니다.
     *
     * @param query       실행할 쿼리
     * @param columnLabel 결과에서 가져올 컬럼 라벨
     * @return 데이터 목록
     */
    private static String[] getDataFromDatabase(String query, String columnLabel) {
        List<String> dataList = new ArrayList<>();
        try (Connection conn = DB2024Team13_connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                dataList.add(rs.getString(columnLabel));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new String[0]);
    }

    /**
     * 건물과 식당 정보를 가져옵니다.
     *
     * @param buildingDropdown 건물 드롭다운
     * @return 건물과 식당 정보가 포함된 Map
     */
    public static Map<String, List<DB2024Team13_restaurantInfo>> getBuildingsAndRestaurants(JComboBox<String> buildingDropdown) {
        Map<String, List<DB2024Team13_restaurantInfo>> buildingRestaurantMap = new HashMap<>();
        Map<String, String> buildingSectionMap = new HashMap<>();
        String buildingQuery = "SELECT building, section FROM DB2024_section";
        String restaurantQuery = "SELECT rest_name, location, best_menu, category, breaktime, eat_alone, section_name, " +
                "(SELECT order_count FROM DB2024_rest_order_count WHERE DB2024_rest_order_count.rest_name = DB2024_restaurant.rest_name) as order_count, " +
                "(SELECT avg_rating FROM DB2024_rest_avg_rating WHERE DB2024_rest_avg_rating.rest_name = DB2024_restaurant.rest_name) as avg_rating " +
                "FROM DB2024_restaurant USE INDEX (idx_restaurant_section_name) WHERE section_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement buildingStmt = conn.prepareStatement(buildingQuery);
             ResultSet buildingRs = buildingStmt.executeQuery()) {
            while (buildingRs.next()) {
                String building = buildingRs.getString("building");
                String section = buildingRs.getString("section");
                buildingDropdown.addItem(building);
                buildingSectionMap.put(building, section);
                buildingRestaurantMap.put(building, new ArrayList<>());
            }
            try (PreparedStatement restaurantStmt = conn.prepareStatement(restaurantQuery)) {
                for (String building : buildingSectionMap.keySet()) {
                    String section = buildingSectionMap.get(building);
                    restaurantStmt.setString(1, section);
                    try (ResultSet restaurantRs = restaurantStmt.executeQuery()) {
                        while (restaurantRs.next()) {
                            DB2024Team13_restaurantInfo restaurant = new DB2024Team13_restaurantInfo(
                                    restaurantRs.getString("rest_name"),
                                    restaurantRs.getString("location"),
                                    restaurantRs.getString("best_menu"),
                                    restaurantRs.getString("category"),
                                    restaurantRs.getBoolean("breaktime"),
                                    restaurantRs.getBoolean("eat_alone"),
                                    restaurantRs.getInt("order_count"),
                                    restaurantRs.getDouble("avg_rating")
                            );
                            buildingRestaurantMap.get(building).add(restaurant);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildingRestaurantMap;
    }
}
