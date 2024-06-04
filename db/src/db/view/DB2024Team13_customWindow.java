package db.view;

import javax.swing.*;

import db.model.DB2024Team13_connection;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class DB2024Team13_customWindow {

    /**
     * 선택 패널을 생성하는 메소드
     *
     * @param mainWindow 메인 윈도우 인스턴스
     * @return 선택 패널
     */
    public static JPanel createSelectionPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel mainSelectionPanel = new JPanel(new BorderLayout());
        mainSelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        // 드롭다운 메뉴 패널
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.X_AXIS));

        // 새로운 드롭다운 메뉴 옵션
        String[] sortOptions = {"주문순", "평점순", "이름순"};
        JComboBox<String> sortDropdown = new JComboBox<>(sortOptions);

        // 드롭다운 패널에 드롭다운 메뉴 추가
        JComboBox<String> buildingDropdown = new JComboBox<>();
        Map<String, List<Restaurant>> buildingRestaurantMap = loadBuildingsAndRestaurants(buildingDropdown);

        dropdownPanel.add(buildingDropdown);
        dropdownPanel.add(Box.createRigidArea(new Dimension(10, 0))); // 두 드롭다운 사이에 간격 추가
        dropdownPanel.add(sortDropdown);

        // 카테고리 체크박스 패널 생성
        JPanel categoriesPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        List<JCheckBox> categoryCheckBoxes = new ArrayList<>();
        String[] categories = {"한식", "중식", "일식", "양식", "분식", "아시안", "패스트푸드", "카페"};
        for (String category : categories) {
            JCheckBox checkBox = new JCheckBox(category);
            categoriesPanel.add(checkBox);
            categoryCheckBoxes.add(checkBox);
        }

        filterPanel.add(dropdownPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 드롭다운과 체크박스 사이에 간격 추가
        filterPanel.add(categoriesPanel);

        // 레스토랑 리스트 모델 및 리스트 생성
        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantJList = new JList<>(restaurantListModel);
        JScrollPane restaurantScrollPane = new JScrollPane(restaurantJList);
        mainSelectionPanel.add(filterPanel, BorderLayout.NORTH);
        mainSelectionPanel.add(restaurantScrollPane, BorderLayout.CENTER);

        // 드롭다운 선택에 따라 리스트를 필터링하는 리스너 추가
        buildingDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedBuilding = (String) buildingDropdown.getSelectedItem();
                    List<Restaurant> restaurants;
                    if ("전체".equals(selectedBuilding)) {
                        restaurants = getAllRestaurants(buildingRestaurantMap);
                    } else {
                        restaurants = buildingRestaurantMap.getOrDefault(selectedBuilding, new ArrayList<>());
                    }
                    filterAndDisplayRestaurants(restaurants, restaurantListModel, categoryCheckBoxes, (String) sortDropdown.getSelectedItem());
                }
            }
        });

        // 체크박스 선택에 따라 리스트를 필터링하는 리스너 추가
        for (JCheckBox checkBox : categoryCheckBoxes) {
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String selectedBuilding = (String) buildingDropdown.getSelectedItem();
                    List<Restaurant> restaurants;
                    if ("전체".equals(selectedBuilding)) {
                        restaurants = getAllRestaurants(buildingRestaurantMap);
                    } else {
                        restaurants = buildingRestaurantMap.getOrDefault(selectedBuilding, new ArrayList<>());
                    }
                    filterAndDisplayRestaurants(restaurants, restaurantListModel, categoryCheckBoxes, (String) sortDropdown.getSelectedItem());
                }
            });
        }

        // 정렬 기준 변경에 따라 리스트를 재정렬하는 리스너 추가
        sortDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedBuilding = (String) buildingDropdown.getSelectedItem();
                    List<Restaurant> restaurants;
                    if ("전체".equals(selectedBuilding)) {
                        restaurants = getAllRestaurants(buildingRestaurantMap);
                    } else {
                        restaurants = buildingRestaurantMap.getOrDefault(selectedBuilding, new ArrayList<>());
                    }
                    filterAndDisplayRestaurants(restaurants, restaurantListModel, categoryCheckBoxes, (String) sortDropdown.getSelectedItem());
                }
            }
        });

        // 초기 데이터를 설정 (첫 번째 건물의 레스토랑 리스트를 미리 표시)
        buildingDropdown.insertItemAt("전체", 0);
        buildingDropdown.setSelectedIndex(0);

        // 레스토랑 리스트에 마우스 리스너 추가 (더블 클릭 시 상세 정보 보기)
        restaurantJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedRestaurant = restaurantJList.getSelectedValue();
                    if (selectedRestaurant != null) {
                        mainWindow.displayDetail(selectedRestaurant);
                    }
                }
            }
        });

        return mainSelectionPanel;
    }

    /**
     * 레스토랑 리스트를 필터링하고 화면에 표시하는 메소드
     *
     * @param restaurants 레스토랑 리스트
     * @param listModel 리스트 모델
     * @param checkBoxes 카테고리 체크박스 리스트
     * @param sortOption 정렬 옵션
     */
    private static void filterAndDisplayRestaurants(List<Restaurant> restaurants, DefaultListModel<String> listModel, List<JCheckBox> checkBoxes, String sortOption) {
        listModel.clear();
        List<Restaurant> filteredRestaurants = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            boolean matchesCategory = checkBoxes.stream()
                    .filter(JCheckBox::isSelected)
                    .map(JCheckBox::getText)
                    .anyMatch(category -> restaurant.getCategory().contains(category));

            if (matchesCategory || checkBoxes.stream().noneMatch(JCheckBox::isSelected)) {
                filteredRestaurants.add(restaurant);
            }
        }

        // 정렬
        sortRestaurants(filteredRestaurants, sortOption);

        Set<String> restaurantNames = new HashSet<>();
        for (Restaurant restaurant : filteredRestaurants) {
            if (restaurantNames.add(restaurant.getName())) {
                listModel.addElement(restaurant.getName());
            }
        }
    }

    /**
     * 레스토랑 목록을 정렬하는 메소드
     *
     * @param restaurants 레스토랑 리스트
     * @param sortOption 정렬 옵션
     */
    private static void sortRestaurants(List<Restaurant> restaurants, String sortOption) {
        switch (sortOption) {
            case "주문순":
                restaurants.sort(Comparator.comparingInt(Restaurant::getOrderCount).reversed());
                break;
            case "평점순":
                restaurants.sort(Comparator.comparingDouble(Restaurant::getAvgRating).reversed());
                break;
            case "이름순":
                restaurants.sort(Comparator.comparing(Restaurant::getName));
                break;
        }
    }

    /**
     * 데이터베이스에서 건물과 레스토랑 목록을 가져오는 메소드
     *
     * @param buildingDropdown 건물 드롭다운 컴포넌트
     * @return 건물별 레스토랑 목록을 담은 맵
     */
    private static Map<String, List<Restaurant>> loadBuildingsAndRestaurants(JComboBox<String> buildingDropdown) {
        Map<String, List<Restaurant>> buildingRestaurantMap = new HashMap<>();
        Map<String, String> buildingSectionMap = new HashMap<>();
        String buildingQuery = "SELECT building, section FROM DB2024_section";
        String restaurantQuery = "SELECT rest_name, location, best_menu, category, breaktime, eat_alone, section_name, " +
                "(SELECT order_count FROM DB2024_rest_order_count WHERE DB2024_rest_order_count.rest_name = DB2024_restaurant.rest_name) as order_count, " +
                "(SELECT avg_rating FROM DB2024_rest_avg_rating WHERE DB2024_rest_avg_rating.rest_name = DB2024_restaurant.rest_name) as avg_rating " +
                "FROM DB2024_restaurant WHERE section_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement buildingStmt = conn.prepareStatement(buildingQuery);
             ResultSet buildingRs = buildingStmt.executeQuery()) {

            // Load buildings and sections
            while (buildingRs.next()) {
                String building = buildingRs.getString("building");
                String section = buildingRs.getString("section");
                buildingDropdown.addItem(building);
                buildingSectionMap.put(building, section);
                buildingRestaurantMap.put(building, new ArrayList<>());
            }

            // Load restaurants for each section
            try (PreparedStatement restaurantStmt = conn.prepareStatement(restaurantQuery)) {
                for (String building : buildingSectionMap.keySet()) {
                    String section = buildingSectionMap.get(building);
                    restaurantStmt.setString(1, section);
                    try (ResultSet restaurantRs = restaurantStmt.executeQuery()) {
                        while (restaurantRs.next()) {
                            String name = restaurantRs.getString("rest_name");
                            String location = restaurantRs.getString("location");
                            String bestMenu = restaurantRs.getString("best_menu");
                            String category = restaurantRs.getString("category");
                            boolean breakTime = restaurantRs.getBoolean("breaktime");
                            boolean eatAlone = restaurantRs.getBoolean("eat_alone");
                            int orderCount = restaurantRs.getInt("order_count");
                            double avgRating = restaurantRs.getDouble("avg_rating");

                            Restaurant restaurant = new Restaurant(name, location, bestMenu, category, breakTime, eatAlone, orderCount, avgRating);
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

    /**
     * 모든 레스토랑을 가져오는 메소드
     *
     * @param buildingRestaurantMap 건물별 레스토랑 목록을 담은 맵
     * @return 모든 레스토랑 리스트
     */
    private static List<Restaurant> getAllRestaurants(Map<String, List<Restaurant>> buildingRestaurantMap) {
        Set<String> restaurantNames = new HashSet<>();
        List<Restaurant> allRestaurants = new ArrayList<>();
        for (List<Restaurant> restaurants : buildingRestaurantMap.values()) {
            for (Restaurant restaurant : restaurants) {
                if (restaurantNames.add(restaurant.getName())) {
                    allRestaurants.add(restaurant);
                }
            }
        }
        return allRestaurants;
    }

    /**
     * 레스토랑 클래스 정의
     */
    static class Restaurant {
        private String name;
        private String location;
        private String bestMenu;
        private String category;
        private boolean breakTime;
        private boolean eatAlone;
        private int orderCount;
        private double avgRating;

        public Restaurant(String name, String location, String bestMenu, String category, boolean breakTime, boolean eatAlone, int orderCount, double avgRating) {
            this.name = name;
            this.location = location;
            this.bestMenu = bestMenu;
            this.category = category;
            this.breakTime = breakTime;
            this.eatAlone = eatAlone;
            this.orderCount = orderCount;
            this.avgRating = avgRating;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getBestMenu() {
            return bestMenu;
        }

        public String getCategory() {
            return category;
        }

        public boolean isBreakTime() {
            return breakTime;
        }

        public boolean isEatAlone() {
            return eatAlone;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public double getAvgRating() {
            return avgRating;
        }
    }
}