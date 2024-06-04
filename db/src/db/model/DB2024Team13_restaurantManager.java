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
import java.util.List;
import java.util.Map;

public class DB2024Team13_restaurantManager {

    /**
     * 검색 텍스트를 사용하여 식당을 검색하고 결과를 listModel에 추가합니다.
     * @param searchText 검색할 텍스트
     * @param listModel 검색 결과를 추가할 DefaultListModel
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
     * 식당의 세부 정보를 로드합니다.
     * @param restaurantName 식당 이름
     * @return 식당 세부 정보가 포함된 Map
     */
    public static Map<String, Object> loadRestaurantDetails(String restaurantName) {
        Map<String, Object> details = new HashMap<>();
        String detailsQuery = "SELECT best_menu, location, breaktime, eat_alone, category, section_name, AVG(star) as avgRating " +
                              "FROM DB2024_restaurant r LEFT JOIN DB2024_review rv ON r.rest_name = rv.rest_name " +
                              "WHERE r.rest_name = ? GROUP BY r.rest_name, r.best_menu, r.location, r.breaktime, r.eat_alone, r.category, r.section_name";
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
     * 식당 정보를 업데이트합니다.
     * @param restaurantName 식당 이름
     * @param category 카테고리
     * @param bestMenu 대표 메뉴
     * @param section 구역
     * @param location 위치
     * @param breaktime 브레이크타임 여부
     * @param eatAlone 혼밥 가능 여부
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
     * 식당을 삭제합니다.
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
            for (String query : deleteQueries) {
                executeUpdates(conn, query, restaurantName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 주어진 쿼리와 파라미터로 데이터베이스 업데이트를 실행합니다.
     * @param conn 데이터베이스 연결 객체
     * @param query 실행할 쿼리
     * @param param 쿼리의 파라미터
     * @throws SQLException SQL 예외 발생 시
     */
    private static void executeUpdates(Connection conn, String query, String param) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param);
            stmt.executeUpdate();
        }
    }

    /**
     * 식당의 메뉴 항목을 가져옵니다.
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
     * @return 카테고리 목록
     */
    public static String[] getCategories() {
        return getDataFromDatabase("SELECT DISTINCT category FROM DB2024_restaurant", "category");
    }

    /**
     * 섹션을 가져옵니다.
     * @return 섹션 목록
     */
    public static String[] getSections() {
        return getDataFromDatabase("SELECT DISTINCT section FROM DB2024_section", "section");
    }

    /**
     * 데이터베이스에서 데이터를 가져옵니다.
     * @param query 실행할 쿼리
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
                "FROM DB2024_restaurant WHERE section_name = ?";
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
