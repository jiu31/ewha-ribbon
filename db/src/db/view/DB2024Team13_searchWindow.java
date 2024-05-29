package db.view;

import db.controller.DB2024Team13_connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_searchWindow {
    
    // 검색 패널 생성 메소드
    public static JPanel createSearchPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 검색 바 패널 생성
        JPanel searchBarPanel = new JPanel(new FlowLayout());
        JTextField searchTextField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBarPanel.add(searchTextField);
        searchBarPanel.add(searchBtn);

        // 리스트 모델 및 리스트 생성
        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantJList = new JList<>(restaurantListModel);
        JScrollPane scrollPane = new JScrollPane(restaurantJList);

        mainPanel.add(searchBarPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 리스트에 마우스 리스너 추가 (더블 클릭 시 상세 정보 보기)
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

        // 검색 버튼 클릭 시 리스트 필터링
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchTextField.getText().toLowerCase();
                filterRestaurantList(searchText, restaurantListModel);
            }
        });

        // 초기 데이터 로드
        loadRestaurantList("", restaurantListModel);

        return mainPanel;
    }
    // 데이터베이스에서 레스토랑 리스트를 불러와 JList에 추가하는 메소드
    private static void loadRestaurantList(String searchText, DefaultListModel<String> listModel) {
        listModel.clear();
        String query = "SELECT rest_name FROM DB2024_restaurant WHERE rest_name LIKE ?";
        
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

    // 리스트 필터링 메소드
    private static void filterRestaurantList(String searchText, DefaultListModel<String> listModel) {
        loadRestaurantList(searchText, listModel);
    }
}
    // 데이터베이스 연결 관련 주석
    // 실제 데이터베이스와의 연결은 다음과 같이 구현할 수 있습니다:
    // 1. 데이터베이스 연결 설정
    // 2. 검색어에 따른 쿼리 실행
    // 3. 결과를 리스트 모델에 추가
    // 이 부분은 데이터베이스 라이브러리를 사용하여 구현해야 합니다.
