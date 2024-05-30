package db.view;

import db.controller.DB2024Team13_connection;
import db.controller.DB2024Team13_userSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_searchWindow {

    public static JPanel createSearchPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchBarPanel = new JPanel(new FlowLayout());
        JTextField searchTextField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBarPanel.add(searchTextField);
        searchBarPanel.add(searchBtn);

        // 추가: 관리자일 경우만 가게 추가 버튼 표시
        JButton addRestaurantBtn = new JButton("가게 추가");
        if (DB2024Team13_userSession.getInstance().isAdmin()) {
            searchBarPanel.add(addRestaurantBtn);
        }

        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantJList = new JList<>(restaurantListModel);
        JScrollPane scrollPane = new JScrollPane(restaurantJList);

        mainPanel.add(searchBarPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

        searchBtn.addActionListener(e -> {
            String searchText = searchTextField.getText().toLowerCase();
            filterRestaurantList(searchText, restaurantListModel);
        });

        addRestaurantBtn.addActionListener(e -> {
            boolean success = DB2024Team13_addRestaurantWindow.showAddRestaurantDialog(mainPanel); // 성공 여부 확인
            if (success) {
                filterRestaurantList("", restaurantListModel); // 성공적으로 추가되면 리스트 업데이트
            }
        });

        loadRestaurantList("", restaurantListModel);

        return mainPanel;
    }

    private static void loadRestaurantList(String searchText, DefaultListModel<String> listModel) {
        listModel.clear();
        String query = "SELECT rest_name FROM DB2024_restaurant WHERE rest_name LIKE ? ORDER BY rest_name";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String restaurantName = rs.getString("rest_name");
                    listModel.addElement(restaurantName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void filterRestaurantList(String searchText, DefaultListModel<String> listModel) {
        loadRestaurantList(searchText, listModel);
    }
}
