package db.view;

import db.model.DB2024Team13_reviewManager;
import db.model.DB2024Team13_userSessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DB2024Team13_addReviewWindow {

	// 리뷰 추가 창 생성, 표시
    public static void showAddReviewDialog(String restaurantName, JPanel mainDetailPanel) {
        JDialog reviewDialog = new JDialog((Frame) null, "리뷰 추가", true);
        reviewDialog.setSize(400, 300);
        reviewDialog.setLocationRelativeTo(null);
        reviewDialog.setLayout(new BorderLayout(20, 20));
        
        JPanel reviewPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel starLabel = new JLabel("별점 (1-5):");
        JTextField starField = new JTextField(10);

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
        
        // 리뷰 제출 버튼 클릭 이벤트 처리
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int star;
                try {
                    star = Integer.parseInt(starField.getText());
                    if (star < 1 || star > 5) {
                        JOptionPane.showMessageDialog(reviewDialog, "별점: 1~5 사이 숫자");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(reviewDialog, "별점으로 숫자를 입력하시오");
                    return;
                }

                String studentId = DB2024Team13_userSessionManager.getInstance().getStudentId();
                if (DB2024Team13_reviewManager.addReview(restaurantName, studentId, star)) {
                    JOptionPane.showMessageDialog(reviewDialog, "리뷰 추가 성공");
                    DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurantName);
                    reviewDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(reviewDialog, "리뷰 추가 실패");
                }
            }
        });

        reviewDialog.add(reviewPanel, BorderLayout.CENTER);
        reviewDialog.add(submitButton, BorderLayout.SOUTH);
        reviewDialog.setVisible(true);
    }

}
