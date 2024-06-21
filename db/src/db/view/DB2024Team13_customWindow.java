package db.view;

import db.model.DB2024Team13_restaurantManager;
import db.model.DB2024Team13_restaurantInfo;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.*;
import java.util.List;

/**
 * '메인' 패널을 생성합니다. 레스토랑 데이터를 필터링하여 표시하는 클래스입니다.
 * <p>이 클래스는 사용자가 레스토랑 데이터를 다양한 기준으로 필터링하고 정렬하여 볼 수 있는 메인 패널을 생성합니다.</p>
 */
public class DB2024Team13_customWindow {
	
	/**
     * restData와 restInfo에서 정보 및 속성을 가져와 사용자가 보는 화면을 설정하는 메소드입니다.
     *
     * @param mainWindow 메인 윈도우 객체
     * @return 설정된 JPanel 객체
     */
    public static JPanel createSelectionPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel mainSelectionPanel = new JPanel(new BorderLayout());
        mainSelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.X_AXIS));

        String[] sortOptions = {"주문순", "평점순", "이름순"};
        JComboBox<String> sortDropdown = new JComboBox<>(sortOptions);

        JComboBox<String> buildingDropdown = new JComboBox<>();
        Map<String, List<DB2024Team13_restaurantInfo>> buildingRestaurantMap = DB2024Team13_restaurantManager.getBuildingsAndRestaurants(buildingDropdown);

        dropdownPanel.add(buildingDropdown);
        dropdownPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        dropdownPanel.add(sortDropdown);

        JPanel categoriesPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        List<JCheckBox> categoryCheckBoxes = new ArrayList<>();
        String[] categories = {"한식", "중식", "일식", "양식", "분식", "아시안", "패스트푸드", "카페"};
        for (String category : categories) {
            JCheckBox checkBox = new JCheckBox(category);
            categoriesPanel.add(checkBox);
            categoryCheckBoxes.add(checkBox);
        }

        filterPanel.add(dropdownPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(categoriesPanel);

        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantJList = new JList<>(restaurantListModel);
        JScrollPane restaurantScrollPane = new JScrollPane(restaurantJList);
        mainSelectionPanel.add(filterPanel, BorderLayout.NORTH);
        mainSelectionPanel.add(restaurantScrollPane, BorderLayout.CENTER);

        buildingDropdown.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                filterAndDisplayRestaurants(
                        getFilteredRestaurants(buildingDropdown, buildingRestaurantMap),
                        restaurantListModel,
                        categoryCheckBoxes,
                        (String) sortDropdown.getSelectedItem()
                );
            }
        });

        categoryCheckBoxes.forEach(checkBox -> checkBox.addItemListener(e -> filterAndDisplayRestaurants(
                getFilteredRestaurants(buildingDropdown, buildingRestaurantMap),
                restaurantListModel,
                categoryCheckBoxes,
                (String) sortDropdown.getSelectedItem()
        )));

        sortDropdown.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                filterAndDisplayRestaurants(
                        getFilteredRestaurants(buildingDropdown, buildingRestaurantMap),
                        restaurantListModel,
                        categoryCheckBoxes,
                        (String) sortDropdown.getSelectedItem()
                );
            }
        });

        buildingDropdown.insertItemAt("전체", 0);
        buildingDropdown.setSelectedIndex(0);

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
     * 레스토랑 정보를 필터링하고 결과를 표시하는 메소드입니다.
     *
     * @param restaurants 필터링할 레스토랑 목록
     * @param listModel 결과를 추가할 DefaultListModel 객체
     * @param checkBoxes 선택된 카테고리 체크박스 목록
     * @param sortOption 정렬 옵션
     */
    private static void filterAndDisplayRestaurants(List<DB2024Team13_restaurantInfo> restaurants, DefaultListModel<String> listModel, List<JCheckBox> checkBoxes, String sortOption) {
        listModel.clear();
        List<DB2024Team13_restaurantInfo> filteredRestaurants = new ArrayList<>();
        for (DB2024Team13_restaurantInfo restaurant : restaurants) {
            boolean matchesCategory = checkBoxes.stream()
                    .filter(JCheckBox::isSelected)
                    .map(JCheckBox::getText)
                    .anyMatch(category -> restaurant.getCategory().contains(category));

            if (matchesCategory || checkBoxes.stream().noneMatch(JCheckBox::isSelected)) {
                filteredRestaurants.add(restaurant);
            }
        }

        sortRestaurants(filteredRestaurants, sortOption);

        filteredRestaurants.stream()
                .map(DB2024Team13_restaurantInfo::getName)
                .distinct()
                .forEach(listModel::addElement);
    }
    
    /**
     * 레스토랑 목록을 정렬하는 메소드입니다.
     *
     * @param restaurants 정렬할 레스토랑 목록
     * @param sortOption 정렬 옵션
     */
    private static void sortRestaurants(List<DB2024Team13_restaurantInfo> restaurants, String sortOption) {
        switch (sortOption) {
            case "주문순":
                restaurants.sort(Comparator.comparingInt(DB2024Team13_restaurantInfo::getOrderCount).reversed());
                break;
            case "평점순":
                restaurants.sort(Comparator.comparingDouble(DB2024Team13_restaurantInfo::getAvgRating).reversed());
                break;
            case "이름순":
                restaurants.sort(Comparator.comparing(DB2024Team13_restaurantInfo::getName));
                break;
        }
    }

    /**
     * 선택된 건물에 따라 필터링된 레스토랑 목록을 반환하는 메소드입니다.
     *
     * @param buildingDropdown 선택된 건물 드롭다운
     * @param buildingRestaurantMap 건물별 레스토랑 정보 맵
     * @return 필터링된 레스토랑 목록
     */
    private static List<DB2024Team13_restaurantInfo> getFilteredRestaurants(JComboBox<String> buildingDropdown, Map<String, List<DB2024Team13_restaurantInfo>> buildingRestaurantMap) {
        String selectedBuilding = (String) buildingDropdown.getSelectedItem();
        if ("전체".equals(selectedBuilding)) {
            return getAllRestaurants(buildingRestaurantMap);
        } else {
            return buildingRestaurantMap.getOrDefault(selectedBuilding, new ArrayList<>());
        }
    }

    /**
     * 모든 레스토랑 목록을 반환하는 메소드입니다.
     *
     * @param buildingRestaurantMap 건물별 레스토랑 정보 맵
     * @return 모든 레스토랑 목록
     */
    private static List<DB2024Team13_restaurantInfo> getAllRestaurants(Map<String, List<DB2024Team13_restaurantInfo>> buildingRestaurantMap) {
        List<DB2024Team13_restaurantInfo> allRestaurants = new ArrayList<>();
        buildingRestaurantMap.values().forEach(allRestaurants::addAll);
        return allRestaurants;
    }
}
