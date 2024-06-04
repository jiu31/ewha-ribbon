package db.view;

import db.model.DB2024Team13_query;
import db.model.DB2024Team13_userSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DB2024Team13_searchWindow {

    // 검색 패널 생성
    public static JPanel createSearchPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 검색 바 패널 생성
        JPanel searchBarPanel = new JPanel(new FlowLayout());
        JTextField searchTextField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBarPanel.add(searchTextField);
        searchBarPanel.add(searchBtn);

        // 식당 목록 모델 및 리스트 생성
        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantJList = new JList<>(restaurantListModel);
        JScrollPane scrollPane = new JScrollPane(restaurantJList);

        // 관리자일 경우 가게 추가 버튼 표시
        if (DB2024Team13_userSession.getInstance().isAdmin()) {
            JButton addRestaurantBtn = new JButton("가게 추가");
            searchBarPanel.add(addRestaurantBtn);
            addRestaurantBtn.addActionListener(e -> {
                boolean success = DB2024Team13_addRestaurantWindow.showAddRestaurantDialog(mainPanel);
                if (success) {
                	DB2024Team13_query.searchRestaurant("", restaurantListModel);
                }
            });
        }
        
        mainPanel.add(searchBarPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 더블 클릭 이벤트 핸들러 추가
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

        // 검색 버튼 클릭 이벤트 핸들러 추가
        searchBtn.addActionListener(e -> {
            String searchText = searchTextField.getText().toLowerCase();
            DB2024Team13_query.searchRestaurant(searchText, restaurantListModel);
        });
        
        // 초기 모든 식당 목록 불러오기
        DB2024Team13_query.searchRestaurant("", restaurantListModel);

        return mainPanel;
    }

}
