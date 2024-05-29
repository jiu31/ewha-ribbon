package db.view;

import db.controller.DB2024Team13_connection;
import db.controller.DB2024Team13_userSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_addReviewWindow {

    /**
     * 리뷰 추가 창을 생성하고 표시하는 메소드
     *
     * @param restaurantName 레스토랑 이름
     * @param mainDetailPanel 메인 상세 패널
     */
    public static void showAddReviewDialog(String restaurantName, JPanel mainDetailPanel) {
        JDialog reviewDialog = new JDialog((Frame) null, "리뷰 추가", true);
        reviewDialog.setSize(400, 300);
        reviewDialog.setLocationRelativeTo(null);
        reviewDialog.setLayout(new BorderLayout(20, 20)); // 여백을 추가합니다.

        JPanel reviewPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 사이의 간격을 추가합니다.
        
        JLabel starLabel = new JLabel("별점 (1-5):");
        JTextField starField = new JTextField(10); // 텍스트 필드의 열 수를 줄입니다.

        Font font = new Font("맑은고딕", Font.PLAIN, 16);
        starLabel.setFont(font);
        starField.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        reviewPanel.add(starLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        reviewPanel.add(starField, gbc);

        JButton submitButton = new JButton("리뷰 제출");
        submitButton.setBackground(new Color(0, 72, 42));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(font);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int star;
                try {
                    star = Integer.parseInt(starField.getText());
                    if (star < 1 || star > 5) {
                        JOptionPane.showMessageDialog(reviewDialog, "별점은 1에서 5 사이의 숫자여야 합니다.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(reviewDialog, "별점은 숫자여야 합니다.");
                    return;
                }

                String studentId = DB2024Team13_userSession.getInstance().getStudentId();
                if (addReviewToDatabase(restaurantName, studentId, star)) {
                    JOptionPane.showMessageDialog(reviewDialog, "리뷰가 성공적으로 추가되었습니다.");
                    DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurantName);
                    reviewDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(reviewDialog, "리뷰 추가 중 오류가 발생했습니다.");
                }
            }
        });

        reviewDialog.add(reviewPanel, BorderLayout.CENTER);
        reviewDialog.add(submitButton, BorderLayout.SOUTH);
        reviewDialog.setVisible(true);
    }

    /**
     * 리뷰를 데이터베이스에 추가하는 메소드
     *
     * @param restaurantName 레스토랑 이름
     * @param studentId      학생 ID
     * @param star           별점
     * @return 리뷰 추가 성공 여부
     */
    private static boolean addReviewToDatabase(String restaurantName, String studentId, int star) {
        String getMaxReviewIdSQL = "SELECT MAX(review_id) FROM DB2024_review";
        String insertReviewSQL = "INSERT INTO DB2024_review (review_id, rest_name, student_id, star) VALUES (?, ?, ?, ?)";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement getMaxReviewIdStmt = conn.prepareStatement(getMaxReviewIdSQL);
             PreparedStatement pstmt = conn.prepareStatement(insertReviewSQL)) {
            
            // Get the max review_id
            ResultSet rs = getMaxReviewIdStmt.executeQuery();
            int newReviewId = 1; // Default to 1 if there are no reviews yet
            if (rs.next()) {
                newReviewId = rs.getInt(1) + 1;
            }

            // Insert new review with newReviewId
            pstmt.setInt(1, newReviewId);
            pstmt.setString(2, restaurantName);
            pstmt.setString(3, studentId);
            pstmt.setInt(4, star);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
