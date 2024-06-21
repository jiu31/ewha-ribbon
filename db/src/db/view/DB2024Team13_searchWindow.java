package db.view;

import db.model.DB2024Team13_restaurantManager;
import db.model.DB2024Team13_userSessionManager;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Set;

/**
 * 검색 패널을 생성하는 클래스입니다.
 * <p>이 클래스는 사용자가 식당 이름이나 메뉴 이름으로 식당을 검색할 수 있는 패널을 생성합니다.
 * 관리자일 경우 가게 추가 기능도 제공합니다.</p>
 */
public class DB2024Team13_searchWindow {

	/**
     * 검색 패널을 생성하는 메소드입니다.
     *
     * @param mainWindow 메인 윈도우 객체
     * @return 생성된 JPanel 객체
     */
    public static JPanel createSearchPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 검색 바 패널 생성
        JPanel searchBarPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("식당 이름이나 메뉴 이름으로 검색:");
        JTextField searchTextField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBarPanel.add(searchLabel);
        searchBarPanel.add(searchTextField);
        searchBarPanel.add(searchBtn);

        // 식당 목록 모델 및 리스트 생성
        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantJList = new JList<>(restaurantListModel);
        JScrollPane scrollPane = new JScrollPane(restaurantJList);

        // 관리자일 경우 가게 추가 버튼 표시
        if (DB2024Team13_userSessionManager.getInstance().isAdmin()) {
            JButton addRestaurantBtn = new JButton("가게 추가");
            searchBarPanel.add(addRestaurantBtn);
            addRestaurantBtn.addActionListener(e -> {
                boolean success = DB2024Team13_addRestaurantWindow.showAddRestaurantDialog(mainPanel);
                if (success) {
                	DB2024Team13_restaurantManager.searchRestaurant("", restaurantListModel);
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
            restaurantListModel.clear();
            Set<String> restaurantByMenuSet = DB2024Team13_restaurantManager.searchRestaurantByMenu(searchText);
            DB2024Team13_restaurantManager.searchRestaurant(searchText, restaurantListModel);
            for (String restaurantName : restaurantByMenuSet) {
                if (!restaurantListModel.contains(restaurantName)) {
                    restaurantListModel.addElement(restaurantName);
                }
            }
        });
        
        // 초기 모든 식당 목록 불러오기
        DB2024Team13_restaurantManager.searchRestaurant("", restaurantListModel);

        return mainPanel;
    }

}
