package db.view;

import db.controller.DB2024Team13_connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB2024Team13_addRestaurantWindow {

    public static void showAddRestaurantDialog(Component parentComponent) {
        JDialog dialog = new JDialog((Frame) null, "가게 추가", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField menuField = new JTextField();
        JTextField categoryField = new JTextField();
        JCheckBox breakTimeCheckBox = new JCheckBox();
        JTextField sectionNameField = new JTextField();
        JCheckBox eatAloneCheckBox = new JCheckBox();

        dialog.add(new JLabel("가게 이름:"));
        dialog.add(nameField);
        dialog.add(new JLabel("위치:"));
        dialog.add(locationField);
        dialog.add(new JLabel("대표 메뉴:"));
        dialog.add(menuField);
        dialog.add(new JLabel("카테고리:"));
        dialog.add(categoryField);
        dialog.add(new JLabel("브레이크 타임:"));
        dialog.add(breakTimeCheckBox);
        dialog.add(new JLabel("섹션 이름:"));
        dialog.add(sectionNameField);
        dialog.add(new JLabel("혼밥 가능:"));
        dialog.add(eatAloneCheckBox);

        JButton submitButton = new JButton("추가하기");
        submitButton.addActionListener(e -> {
            boolean success = addRestaurant(
                nameField.getText(),
                locationField.getText(),
                menuField.getText(),
                categoryField.getText(),
                breakTimeCheckBox.isSelected(),
                sectionNameField.getText(),
                eatAloneCheckBox.isSelected()
            );
            if (success) {
                JOptionPane.showMessageDialog(dialog, "가게가 성공적으로 추가되었습니다.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "가게 추가 실패. 데이터를 확인해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JLabel());
        dialog.add(submitButton);
        dialog.setVisible(true);
    }

    private static boolean addRestaurant(String name, String location, String bestMenu, String category, boolean breakTime, String sectionName, boolean eatAlone) {
        String sql = "INSERT INTO DB2024_restaurant (rest_name, location, best_menu, category, breaktime, section_name, eat_alone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, bestMenu);
            pstmt.setString(4, category);
            pstmt.setBoolean(5, breakTime);
            pstmt.setString(6, sectionName);
            pstmt.setBoolean(7, eatAlone);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
