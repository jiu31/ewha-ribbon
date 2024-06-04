package db.view;

import javax.swing.*;

import db.model.DB2024Team13_connection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_menuFixWindow {

    public static void showMenuFixWindow(String restaurant, JPanel mainDetailPanel) {
        JFrame frame = new JFrame("관리자 - 메뉴 수정");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("메뉴 수정", SwingConstants.CENTER);
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 메뉴 추가 패널
        JPanel addMenuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 메뉴 추가 라벨
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel addMenuLabel = new JLabel("메뉴 추가", SwingConstants.CENTER);
        addMenuLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        addMenuPanel.add(addMenuLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        addMenuPanel.add(new JLabel("메뉴 이름"), gbc);

        gbc.gridx = 1;
        JTextField menuNameField = new JTextField(10);
        addMenuPanel.add(menuNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addMenuPanel.add(new JLabel("가격"), gbc);

        gbc.gridx = 1;
        JTextField menuPriceField = new JTextField(10);
        addMenuPanel.add(menuPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addMenuPanel.add(new JLabel("비건 여부"), gbc);

        gbc.gridx = 1;
        JCheckBox menuVeganCheckBox = new JCheckBox();
        addMenuPanel.add(menuVeganCheckBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addMenuButton = new JButton("메뉴 추가");
        addMenuPanel.add(addMenuButton, gbc);

        panel.add(addMenuPanel, BorderLayout.SOUTH);

        // 기존 메뉴 불러오기
        loadMenuItems(restaurant, menuPanel, mainDetailPanel);

        // 메뉴 추가 버튼 클릭 이벤트
        addMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String menuName = menuNameField.getText();
                if (menuName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "메뉴 이름을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int menuPrice = Integer.parseInt(menuPriceField.getText());
                    boolean isVegan = menuVeganCheckBox.isSelected();
                    addMenuItem(restaurant, menuName, menuPrice, isVegan);
                    JOptionPane.showMessageDialog(frame, "메뉴가 추가되었습니다.");
                    loadMenuItems(restaurant, menuPanel, mainDetailPanel); // 메뉴 목록 갱신
                    DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurant); // 상세 정보 패널 갱신
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "가격은 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setLocationRelativeTo(null); // 창을 화면 중앙에 위치
        frame.setVisible(true);
    }

    private static void loadMenuItems(String restaurantName, JPanel menuPanel, JPanel mainDetailPanel) {
        menuPanel.removeAll(); // 패널 초기화
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        String menuQuery = "SELECT menu_name, price, vegan FROM DB2024_menu WHERE rest_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(menuQuery)) {
            
            stmt.setString(1, restaurantName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String menuName = rs.getString("menu_name");
                    int price = rs.getInt("price");
                    boolean isVegan = rs.getBoolean("vegan");
                    JPanel menuItemPanel = createMenuItemPanel(restaurantName, menuName, price, isVegan, menuPanel, mainDetailPanel);
                    gbc.gridy++;
                    menuPanel.add(menuItemPanel, gbc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        menuPanel.revalidate(); // 패널 갱신
        menuPanel.repaint(); // 패널 다시 그리기
    }

    private static JPanel createMenuItemPanel(String restaurantName, String menuName, int price, boolean isVegan, JPanel menuPanel, JPanel mainDetailPanel) {
        JPanel menuItemPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel menuNameLabel = new JLabel(menuName);
        JTextField priceField = new JTextField(String.valueOf(price), 10);
        JCheckBox veganCheckBox = new JCheckBox("비건 여부");
        veganCheckBox.setSelected(isVegan);
        JButton updateButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");

        gbc.gridx = 0;
        menuItemPanel.add(menuNameLabel, gbc);
        gbc.gridx = 1;
        menuItemPanel.add(priceField, gbc);
        gbc.gridx = 2;
        menuItemPanel.add(veganCheckBox, gbc);
        gbc.gridx = 3;
        menuItemPanel.add(updateButton, gbc);
        gbc.gridx = 4;
        menuItemPanel.add(deleteButton, gbc);

        // 수정 버튼 클릭 이벤트
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int newPrice = Integer.parseInt(priceField.getText());
                    boolean newIsVegan = veganCheckBox.isSelected();
                    updateMenuItem(restaurantName, menuName, newPrice, newIsVegan);
                    JOptionPane.showMessageDialog(menuPanel, "메뉴가 수정되었습니다.");
                    loadMenuItems(restaurantName, menuPanel, mainDetailPanel); // 메뉴 목록 갱신
                    DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurantName); // 상세 정보 패널 갱신
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(menuPanel, "가격은 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 삭제 버튼 클릭 이벤트
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteMenuItem(restaurantName, menuName);
                    JOptionPane.showMessageDialog(menuPanel, "메뉴가 삭제되었습니다.");
                    loadMenuItems(restaurantName, menuPanel, mainDetailPanel); // 메뉴 목록 갱신
                    DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurantName); // 상세 정보 패널 갱신
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(menuPanel, "메뉴 삭제 중 오류가 발생했습니다.", "삭제 오류", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        return menuItemPanel;
    }

    private static void addMenuItem(String restaurantName, String menuName, int price, boolean isVegan) {
        String insertMenuQuery = "INSERT INTO DB2024_menu (rest_name, menu_name, price, vegan) VALUES (?, ?, ?, ?)";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertMenuQuery)) {
            
            stmt.setString(1, restaurantName);
            stmt.setString(2, menuName);
            stmt.setInt(3, price);
            stmt.setBoolean(4, isVegan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateMenuItem(String restaurantName, String menuName, int price, boolean isVegan) {
        String updateMenuQuery = "UPDATE DB2024_menu SET price = ?, vegan = ? WHERE rest_name = ? AND menu_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateMenuQuery)) {
            
            stmt.setInt(1, price);
            stmt.setBoolean(2, isVegan);
            stmt.setString(3, restaurantName);
            stmt.setString(4, menuName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteMenuItem(String restaurantName, String menuName) throws SQLException {
        String deleteOrderQuery = "DELETE FROM DB2024_order WHERE rest_name = ? AND menu_name = ?";
        String deleteMenuQuery = "DELETE FROM DB2024_menu WHERE rest_name = ? AND menu_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery);
             PreparedStatement deleteMenuStmt = conn.prepareStatement(deleteMenuQuery)) {

            // 주문 삭제
            deleteOrderStmt.setString(1, restaurantName);
            deleteOrderStmt.setString(2, menuName);
            deleteOrderStmt.executeUpdate();

            // 메뉴 삭제
            deleteMenuStmt.setString(1, restaurantName);
            deleteMenuStmt.setString(2, menuName);
            deleteMenuStmt.executeUpdate();
        }
    }
}
