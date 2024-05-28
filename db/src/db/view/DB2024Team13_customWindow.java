package db.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        JPanel filterPanel = new JPanel(new BorderLayout());

        // 드롭다운 메뉴 옵션
        String[] dropdownOptions = {"건물1", "건물2", "건물3", "건물4"};
        JComboBox<String> buildingDropdown = new JComboBox<>(dropdownOptions);

        // 카테고리 체크박스 패널 생성
        JPanel categoriesPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        List<JCheckBox> categoryCheckBoxes = new ArrayList<>();
        String[] categories = {"한식", "중식", "일식", "양식", "분식", "아시안", "패스트푸드", "카페"};
        for (String category : categories) {
            JCheckBox checkBox = new JCheckBox(category);
            categoriesPanel.add(checkBox);
            categoryCheckBoxes.add(checkBox);
        }

        filterPanel.add(buildingDropdown, BorderLayout.NORTH);
        filterPanel.add(categoriesPanel, BorderLayout.CENTER);

        // 레스토랑 리스트 모델 및 리스트 생성
        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantJList = new JList<>(restaurantListModel);
        JScrollPane restaurantScrollPane = new JScrollPane(restaurantJList);
        mainSelectionPanel.add(filterPanel, BorderLayout.NORTH);
        mainSelectionPanel.add(restaurantScrollPane, BorderLayout.CENTER);

        // 예시 데이터 추가
        Map<String, List<Restaurant>> buildingRestaurantMap = new HashMap<>();
        buildingRestaurantMap.put("건물1", List.of(
                new Restaurant("Restaurant A", "건물1", "Menu 1", "한식", false, true),
                new Restaurant("Restaurant B", "건물1", "Menu 2", "중식", true, false),
                new Restaurant("Restaurant C", "건물1", "Menu 3", "일식", false, true),
                // 추가 데이터
                new Restaurant("Restaurant D", "건물1", "Menu 4", "양식", false, true),
                new Restaurant("Restaurant E", "건물1", "Menu 5", "분식", true, false),
                new Restaurant("Restaurant F", "건물1", "Menu 6", "한식", false, true),
                new Restaurant("Restaurant G", "건물1", "Menu 7", "중식", true, false),
                new Restaurant("Restaurant H", "건물1", "Menu 8", "일식", false, true),
                new Restaurant("Restaurant I", "건물1", "Menu 9", "양식", false, true),
                new Restaurant("Restaurant J", "건물1", "Menu 10", "분식", true, false),
                new Restaurant("Restaurant K", "건물1", "Menu 11", "한식", false, true),
                new Restaurant("Restaurant L", "건물1", "Menu 12", "중식", true, false),
                new Restaurant("Restaurant M", "건물1", "Menu 13", "일식", false, true),
                new Restaurant("Restaurant N", "건물1", "Menu 14", "양식", false, true),
                new Restaurant("Restaurant O", "건물1", "Menu 15", "분식", true, false)
        ));
        buildingRestaurantMap.put("건물2", List.of(
                new Restaurant("Restaurant P", "건물2", "Menu 1", "아시안", false, true),
                new Restaurant("Restaurant Q", "건물2", "Menu 2", "패스트푸드", true, false),
                new Restaurant("Restaurant R", "건물2", "Menu 3", "카페", false, true),
                // 추가 데이터
                new Restaurant("Restaurant S", "건물2", "Menu 4", "한식", false, true),
                new Restaurant("Restaurant T", "건물2", "Menu 5", "중식", true, false),
                new Restaurant("Restaurant U", "건물2", "Menu 6", "일식", false, true),
                new Restaurant("Restaurant V", "건물2", "Menu 7", "양식", false, true),
                new Restaurant("Restaurant W", "건물2", "Menu 8", "분식", true, false),
                new Restaurant("Restaurant X", "건물2", "Menu 9", "한식", false, true),
                new Restaurant("Restaurant Y", "건물2", "Menu 10", "중식", true, false),
                new Restaurant("Restaurant Z", "건물2", "Menu 11", "일식", false, true),
                new Restaurant("Restaurant AA", "건물2", "Menu 12", "양식", false, true),
                new Restaurant("Restaurant AB", "건물2", "Menu 13", "분식", true, false),
                new Restaurant("Restaurant AC", "건물2", "Menu 14", "한식", false, true),
                new Restaurant("Restaurant AD", "건물2", "Menu 15", "중식", true, false)
        ));
        buildingRestaurantMap.put("건물3", List.of(
                new Restaurant("Restaurant AE", "건물3", "Menu 1", "일식", false, true),
                new Restaurant("Restaurant AF", "건물3", "Menu 2", "양식", true, false),
                new Restaurant("Restaurant AG", "건물3", "Menu 3", "분식", false, true),
                // 추가 데이터
                new Restaurant("Restaurant AH", "건물3", "Menu 4", "아시안", false, true),
                new Restaurant("Restaurant AI", "건물3", "Menu 5", "패스트푸드", true, false),
                new Restaurant("Restaurant AJ", "건물3", "Menu 6", "카페", false, true),
                new Restaurant("Restaurant AK", "건물3", "Menu 7", "한식", false, true),
                new Restaurant("Restaurant AL", "건물3", "Menu 8", "중식", true, false),
                new Restaurant("Restaurant AM", "건물3", "Menu 9", "일식", false, true),
                new Restaurant("Restaurant AN", "건물3", "Menu 10", "양식", false, true),
                new Restaurant("Restaurant AO", "건물3", "Menu 11", "분식", true, false),
                new Restaurant("Restaurant AP", "건물3", "Menu 12", "아시안", false, true),
                new Restaurant("Restaurant AQ", "건물3", "Menu 13", "패스트푸드", true, false),
                new Restaurant("Restaurant AR", "건물3", "Menu 14", "카페", false, true),
                new Restaurant("Restaurant AS", "건물3", "Menu 15", "한식", false, true)
        ));
        buildingRestaurantMap.put("건물4", List.of(
                new Restaurant("Restaurant AT", "건물4", "Menu 1", "중식", false, true),
                new Restaurant("Restaurant AU", "건물4", "Menu 2", "일식", true, false),
                new Restaurant("Restaurant AV", "건물4", "Menu 3", "양식", false, true),
                // 추가 데이터
                new Restaurant("Restaurant AW", "건물4", "Menu 4", "분식", false, true),
                new Restaurant("Restaurant AX", "건물4", "Menu 5", "아시안", true, false),
                new Restaurant("Restaurant AY", "건물4", "Menu 6", "패스트푸드", false, true),
                new Restaurant("Restaurant AZ", "건물4", "Menu 7", "카페", false, true),
                new Restaurant("Restaurant BA", "건물4", "Menu 8", "한식", true, false),
                new Restaurant("Restaurant BB", "건물4", "Menu 9", "중식", false, true),
                new Restaurant("Restaurant BC", "건물4", "Menu 10", "일식", false, true),
                new Restaurant("Restaurant BD", "건물4", "Menu 11", "양식", true, false),
                new Restaurant("Restaurant BE", "건물4", "Menu 12", "분식", false, true),
                new Restaurant("Restaurant BF", "건물4", "Menu 13", "아시안", false, true),
                new Restaurant("Restaurant BG", "건물4", "Menu 14", "패스트푸드", true, false),
                new Restaurant("Restaurant BH", "건물4", "Menu 15", "카페", false, true)
        ));

        // 드롭다운 선택에 따라 리스트를 필터링하는 리스너 추가
        buildingDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedBuilding = (String) buildingDropdown.getSelectedItem();
                    List<Restaurant> restaurants = buildingRestaurantMap.getOrDefault(selectedBuilding, new ArrayList<>());
                    filterAndDisplayRestaurants(restaurants, restaurantListModel, categoryCheckBoxes);
                }
            }
        });

        // 체크박스 선택에 따라 리스트를 필터링하는 리스너 추가
        for (JCheckBox checkBox : categoryCheckBoxes) {
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String selectedBuilding = (String) buildingDropdown.getSelectedItem();
                    List<Restaurant> restaurants = buildingRestaurantMap.getOrDefault(selectedBuilding, new ArrayList<>());
                    filterAndDisplayRestaurants(restaurants, restaurantListModel, categoryCheckBoxes);
                }
            });
        }

        // 초기 데이터를 설정 (첫 번째 건물의 레스토랑 리스트를 미리 표시)
        String initialBuilding = dropdownOptions[0];
        List<Restaurant> initialRestaurantList = buildingRestaurantMap.getOrDefault(initialBuilding, new ArrayList<>());
        filterAndDisplayRestaurants(initialRestaurantList, restaurantListModel, categoryCheckBoxes);

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
     */
    private static void filterAndDisplayRestaurants(List<Restaurant> restaurants, DefaultListModel<String> listModel, List<JCheckBox> checkBoxes) {
        listModel.clear();
        for (Restaurant restaurant : restaurants) {
            boolean matchesCategory = checkBoxes.stream()
                    .filter(JCheckBox::isSelected)
                    .map(JCheckBox::getText)
                    .anyMatch(category -> restaurant.getCategory().contains(category));

            if (matchesCategory || checkBoxes.stream().noneMatch(JCheckBox::isSelected)) {
                listModel.addElement(restaurant.getName());
            }
        }
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

        public Restaurant(String name, String location, String bestMenu, String category, boolean breakTime, boolean eatAlone) {
            this.name = name;
            this.location = location;
            this.bestMenu = bestMenu;
            this.category = category;
            this.breakTime = breakTime;
            this.eatAlone = eatAlone;
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
    }
}
