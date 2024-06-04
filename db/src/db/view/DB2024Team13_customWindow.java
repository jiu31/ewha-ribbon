package db.view;

import db.model.DB2024Team13_restData;
import db.view.DB2024Team13_restInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class DB2024Team13_customWindow {
	
	//restData와 restInfo에서 정보 및 속성을 가져와 사용자가 보는 화면을 설정함
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
        Map<String, List<DB2024Team13_restInfo>> buildingRestaurantMap = DB2024Team13_restData.loadBuildingsAndRestaurants(buildingDropdown);

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

    private static void filterAndDisplayRestaurants(List<DB2024Team13_restInfo> restaurants, DefaultListModel<String> listModel, List<JCheckBox> checkBoxes, String sortOption) {
        listModel.clear();
        List<DB2024Team13_restInfo> filteredRestaurants = new ArrayList<>();
        for (DB2024Team13_restInfo restaurant : restaurants) {
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
                .map(DB2024Team13_restInfo::getName)
                .distinct()
                .forEach(listModel::addElement);
    }

    private static void sortRestaurants(List<DB2024Team13_restInfo> restaurants, String sortOption) {
        switch (sortOption) {
            case "주문순":
                restaurants.sort(Comparator.comparingInt(DB2024Team13_restInfo::getOrderCount).reversed());
                break;
            case "평점순":
                restaurants.sort(Comparator.comparingDouble(DB2024Team13_restInfo::getAvgRating).reversed());
                break;
            case "이름순":
                restaurants.sort(Comparator.comparing(DB2024Team13_restInfo::getName));
                break;
        }
    }

    private static List<DB2024Team13_restInfo> getFilteredRestaurants(JComboBox<String> buildingDropdown, Map<String, List<DB2024Team13_restInfo>> buildingRestaurantMap) {
        String selectedBuilding = (String) buildingDropdown.getSelectedItem();
        if ("전체".equals(selectedBuilding)) {
            return getAllRestaurants(buildingRestaurantMap);
        } else {
            return buildingRestaurantMap.getOrDefault(selectedBuilding, new ArrayList<>());
        }
    }

    private static List<DB2024Team13_restInfo> getAllRestaurants(Map<String, List<DB2024Team13_restInfo>> buildingRestaurantMap) {
        List<DB2024Team13_restInfo> allRestaurants = new ArrayList<>();
        buildingRestaurantMap.values().forEach(allRestaurants::addAll);
        return allRestaurants;
    }
}
