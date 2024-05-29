package db.view;

import db.controller.DB2024Team13_connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_adminWindow {

    public static void showAdminWindow(String restaurant, JPanel mainDetailPanel) {
        JFrame frame = new JFrame("관리자 - 레스토랑 정보 수정");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("레스토랑 정보 수정", SwingConstants.CENTER);
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(20)); // 간격 추가

        JLabel categoryLabel = new JLabel("카테고리:");
        JTextField categoryField = new JTextField();
        panel.add(categoryLabel);
        panel.add(categoryField);

        JLabel bestMenuLabel = new JLabel("대표메뉴:");
        JTextField bestMenuField = new JTextField();
        panel.add(bestMenuLabel);
        panel.add(bestMenuField);

        JLabel sectionLabel = new JLabel("구역 이름:");
        JTextField sectionField = new JTextField();
        panel.add(sectionLabel);
        panel.add(sectionField);

        JLabel locationLabel = new JLabel("위치:");
        JTextField locationField = new JTextField();
        panel.add(locationLabel);
        panel.add(locationField);

        JLabel breaktimeLabel = new JLabel("브레이크타임 유무:");
        JCheckBox breaktimeCheckBox = new JCheckBox();
        panel.add(breaktimeLabel);
        panel.add(breaktimeCheckBox);

        JLabel eatAloneLabel = new JLabel("혼밥 가능 여부:");
        JCheckBox eatAloneCheckBox = new JCheckBox();
        panel.add(eatAloneLabel);
        panel.add(eatAloneCheckBox);

        loadRestaurantDetails(restaurant, categoryField, bestMenuField, sectionField, locationField, breaktimeCheckBox, eatAloneCheckBox);

        JButton saveButton = new JButton("저장");
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRestaurantDetails(restaurant, categoryField.getText(), bestMenuField.getText(), sectionField.getText(), locationField.getText(), breaktimeCheckBox.isSelected(), eatAloneCheckBox.isSelected());
                DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurant);
                frame.dispose();
            }
        });

        JButton deleteButton = new JButton("삭제");
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "정말로 이 레스토랑을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteRestaurant(restaurant);
                    JOptionPane.showMessageDialog(null, "레스토랑이 삭제되었습니다.");
                    frame.dispose();
                }
            }
        });

        frame.setVisible(true);
    }

    private static void loadRestaurantDetails(String restaurantName, JTextField categoryField, JTextField bestMenuField, JTextField sectionField, JTextField locationField, JCheckBox breaktimeCheckBox, JCheckBox eatAloneCheckBox) {
        String detailsQuery = "SELECT best_menu, location, breaktime, eat_alone, category, section_name " +
                       "FROM DB2024_restaurant " +
                       "WHERE rest_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement detailsStmt = conn.prepareStatement(detailsQuery)) {
            
            detailsStmt.setString(1, restaurantName);
            try (ResultSet rs = detailsStmt.executeQuery()) {
                if (rs.next()) {
                    bestMenuField.setText(rs.getString("best_menu"));
                    locationField.setText(rs.getString("location"));
                    breaktimeCheckBox.setSelected(rs.getBoolean("breaktime"));
                    eatAloneCheckBox.setSelected(rs.getBoolean("eat_alone"));
                    categoryField.setText(rs.getString("category"));
                    sectionField.setText(rs.getString("section_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRestaurantDetails(String restaurantName, String category, String bestMenu, String section, String location, boolean breaktime, boolean eatAlone) {
        String updateQuery = "UPDATE DB2024_restaurant SET category = ?, best_menu = ?, section_name = ?, location = ?, breaktime = ?, eat_alone = ? WHERE rest_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            
            stmt.setString(1, category);
            stmt.setString(2, bestMenu);
            stmt.setString(3, section);
            stmt.setString(4, location);
            stmt.setBoolean(5, breaktime);
            stmt.setBoolean(6, eatAlone);
            stmt.setString(7, restaurantName);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRestaurant(String restaurantName) {
        String deleteOrderQuery = "DELETE FROM DB2024_order WHERE rest_name = ?";
        String deleteReviewQuery = "DELETE FROM DB2024_review WHERE rest_name = ?";
        String deleteBookmarkQuery = "DELETE FROM DB2024_bookmark WHERE rest_name = ?";
        String deleteMenuQuery = "DELETE FROM DB2024_menu WHERE rest_name = ?";
        String deleteRestaurantQuery = "DELETE FROM DB2024_restaurant WHERE rest_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery);
             PreparedStatement deleteReviewStmt = conn.prepareStatement(deleteReviewQuery);
             PreparedStatement deleteBookmarkStmt = conn.prepareStatement(deleteBookmarkQuery);
             PreparedStatement deleteMenuStmt = conn.prepareStatement(deleteMenuQuery);
             PreparedStatement deleteRestaurantStmt = conn.prepareStatement(deleteRestaurantQuery)) {

            // 주문 삭제
            deleteOrderStmt.setString(1, restaurantName);
            deleteOrderStmt.executeUpdate();

            // 리뷰 삭제
            deleteReviewStmt.setString(1, restaurantName);
            deleteReviewStmt.executeUpdate();

            // 북마크 삭제
            deleteBookmarkStmt.setString(1, restaurantName);
            deleteBookmarkStmt.executeUpdate();

            // 메뉴 삭제
            deleteMenuStmt.setString(1, restaurantName);
            deleteMenuStmt.executeUpdate();

            // 레스토랑 삭제
            deleteRestaurantStmt.setString(1, restaurantName);
            deleteRestaurantStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
