package db.view;

import db.controller.DB2024Team13_userSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DB2024Team13_detailWindow {

    // 상수 정의
    private static final Color COLOR_BOOKMARKED = new Color(0, 150, 0); // 초록색
    private static final Color COLOR_NOT_BOOKMARKED = new Color(200, 200, 200); // 기본 버튼 색상
    private static final Color COLOR_BOOKMARKED_TEXT = Color.WHITE; // 북마크된 상태의 텍스트 색상
    private static final Color COLOR_NOT_BOOKMARKED_TEXT = Color.BLACK; // 북마크되지 않은 상태의 텍스트 색상

    /**
     * 특정 레스토랑의 상세 정보를 표시하는 패널을 생성하는 메소드
     *
     * @param restaurant 레스토랑 이름
     * @return 상세 정보 패널
     */
    public static JPanel createDetailPanel(String restaurant) {
        // 로그인한 사용자가 관리자 여부 확인
        boolean isAdmin = DB2024Team13_userSession.getInstance().isAdmin();

        // 메인 상세 정보 패널 생성
        JPanel mainDetailPanel = new JPanel(new BorderLayout());
        mainDetailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 레스토랑 이름을 포함하는 패널 생성
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel restaurantTitleLabel = new JLabel(restaurant, SwingConstants.CENTER);
        restaurantTitleLabel.setFont(new Font("나눔고딕", Font.BOLD, 24));

        titlePanel.add(restaurantTitleLabel, BorderLayout.CENTER);

        // 관리자가 아닌 경우에만 북마크 버튼 추가
        if (!isAdmin) {
            JButton bookmarkButton = new JButton("★");
            bookmarkButton.setFont(new Font("나눔고딕", Font.BOLD, 24));
            bookmarkButton.setBackground(COLOR_NOT_BOOKMARKED);
            bookmarkButton.setForeground(COLOR_NOT_BOOKMARKED_TEXT);
            bookmarkButton.setOpaque(true);
            bookmarkButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            // 북마크 버튼 클릭 이벤트 추가
            bookmarkButton.addActionListener(new ActionListener() {
                private boolean isBookmarked = false;

                @Override
                public void actionPerformed(ActionEvent e) {
                    isBookmarked = !isBookmarked;
                    if (isBookmarked) {
                        bookmarkButton.setBackground(COLOR_BOOKMARKED);
                        bookmarkButton.setForeground(COLOR_BOOKMARKED_TEXT);
                    } else {
                        bookmarkButton.setBackground(COLOR_NOT_BOOKMARKED);
                        bookmarkButton.setForeground(COLOR_NOT_BOOKMARKED_TEXT);
                    }
                }
            });

            titlePanel.add(bookmarkButton, BorderLayout.EAST);
        }

        mainDetailPanel.add(titlePanel, BorderLayout.NORTH);

        // 상세 내용을 표시할 패널 생성
        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(Color.WHITE);

        // 상세 정보 레이블 추가
        detailsContentPanel.add(createDetailLabel("별점: "));
        detailsContentPanel.add(createDetailLabel("대표메뉴: "));
        detailsContentPanel.add(createDetailLabel("위치: "));
        detailsContentPanel.add(createDetailLabel("비건메뉴 여부: "));
        detailsContentPanel.add(createDetailLabel("혼밥 가능 여부: "));
        detailsContentPanel.add(createDetailLabel("브레이크타임 유무: "));

        mainDetailPanel.add(detailsContentPanel, BorderLayout.CENTER);

        // 리뷰 추가 또는 정보 수정 버튼 생성
        JButton actionButton = new JButton(isAdmin ? "정보 수정" : "리뷰 추가");
        actionButton.setFont(new Font("나눔고딕", Font.BOLD, 15));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(actionButton);
        
        mainDetailPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainDetailPanel;
    }

    /**
     * 상세 정보를 표시하는 레이블을 생성하는 메소드
     *
     * @param text 레이블에 표시할 텍스트
     * @return 생성된 레이블
     */
    private static JLabel createDetailLabel(String text) {
        JLabel detailLabel = new JLabel(text);
        detailLabel.setFont(new Font("나눔고딕", Font.BOLD, 15));
        return detailLabel;
    }
}
