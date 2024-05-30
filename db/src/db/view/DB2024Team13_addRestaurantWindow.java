package db.view;

import db.controller.DB2024Team13_connection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB2024Team13_addRestaurantWindow {

    public static boolean showAddRestaurantDialog(Component parentComponent) { // 반환값을 boolean으로 변경
        JDialog dialog = new JDialog((Frame) null, "가게 추가", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(15);
        JComboBox<String> locationComboBox = new JComboBox<>(getBuildings());
        JTextField menuField = new JTextField(15);
        JComboBox<String> categoryComboBox = new JComboBox<>(getCategories());
        JCheckBox breakTimeCheckBox = new JCheckBox();
        JLabel sectionLabel = new JLabel();
        JCheckBox eatAloneCheckBox = new JCheckBox();

        // locationComboBox 선택에 따라 sectionLabel 업데이트
        locationComboBox.addActionListener(e -> {
            String selectedBuilding = (String) locationComboBox.getSelectedItem();
            if (selectedBuilding != null) {
                String sectionName = getSectionName(selectedBuilding);
                sectionLabel.setText(sectionName);
            }
        });

        // 초기 선택된 위치에 따라 섹션 이름 설정
        if (locationComboBox.getSelectedItem() != null) {
            String initialBuilding = (String) locationComboBox.getSelectedItem();
            String sectionName = getSectionName(initialBuilding);
            sectionLabel.setText(sectionName);
        }

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("가게 이름:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("위치:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("대표 메뉴:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("카테고리:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("브레이크 타임:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("섹션 이름:"), gbc);
        gbc.gridy++;
        dialog.add(new JLabel("혼밥 가능:"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialog.add(nameField, gbc);
        gbc.gridy++;
        dialog.add(locationComboBox, gbc);
        gbc.gridy++;
        dialog.add(menuField, gbc);
        gbc.gridy++;
        dialog.add(categoryComboBox, gbc);
        gbc.gridy++;
        dialog.add(breakTimeCheckBox, gbc);
        gbc.gridy++;
        dialog.add(sectionLabel, gbc);
        gbc.gridy++;
        dialog.add(eatAloneCheckBox, gbc);

        JButton submitButton = new JButton("추가하기");
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(submitButton, gbc);

        final boolean[] success = {false}; // 가게 추가 성공 여부 저장

        submitButton.addActionListener(e -> {
            // 모든 필드가 입력되었는지 확인
            if (nameField.getText().isEmpty() || 
                locationComboBox.getSelectedItem() == null || 
                menuField.getText().isEmpty() || 
                categoryComboBox.getSelectedItem() == null || 
                sectionLabel.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "모든 필드를 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            success[0] = addRestaurantWithTransaction(
                nameField.getText(),
                (String) locationComboBox.getSelectedItem(),
                menuField.getText(),
                (String) categoryComboBox.getSelectedItem(),
                breakTimeCheckBox.isSelected(),
                sectionLabel.getText(),
                eatAloneCheckBox.isSelected()
            );
            if (success[0]) {
                JOptionPane.showMessageDialog(dialog, "가게가 성공적으로 추가되었습니다.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "가게 추가 실패. 데이터를 확인해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
        return success[0]; // 가게 추가 성공 여부 반환
    }

    private static String[] getCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM DB2024_restaurant";
        try (Connection conn = DB2024Team13_connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }

    private static String[] getBuildings() {
        List<String> buildings = new ArrayList<>();
        String sql = "SELECT DISTINCT building FROM DB2024_section";
        try (Connection conn = DB2024Team13_connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                buildings.add(rs.getString("building"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildings.toArray(new String[0]);
    }

    private static String getSectionName(String building) {
        String sql = "SELECT section FROM DB2024_section WHERE building = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, building);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("section");
                } else {
                    return "섹션을 찾을 수 없습니다.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "오류 발생";
        }
    }

    private static boolean addRestaurantWithTransaction(String name, String location, String bestMenu, String category, boolean breakTime, String sectionName, boolean eatAlone) {
        String restaurantSql = "INSERT INTO DB2024_restaurant (rest_name, location, best_menu, category, breaktime, section_name, eat_alone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement restaurantStmt = null;

        try {
            conn = DB2024Team13_connection.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 가게 정보 추가
            restaurantStmt = conn.prepareStatement(restaurantSql);
            restaurantStmt.setString(1, name);
            restaurantStmt.setString(2, location);
            restaurantStmt.setString(3, bestMenu);
            restaurantStmt.setString(4, category);
            restaurantStmt.setBoolean(5, breakTime);
            restaurantStmt.setString(6, sectionName);
            restaurantStmt.setBoolean(7, eatAlone);
            restaurantStmt.executeUpdate();

            conn.commit(); // 트랜잭션 커밋
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 트랜잭션 롤백
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (restaurantStmt != null) {
                try {
                    restaurantStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
