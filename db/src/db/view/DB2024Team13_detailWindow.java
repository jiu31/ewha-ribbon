package db.view;

import db.controller.DB2024Team13_userSession;
import db.controller.DB2024Team13_connection;
import db.view.DB2024Team13_addReviewWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_detailWindow {

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
        JLabel avgRatingLabel = createDetailLabel("별점: ");
        JSeparator separator1 = createSeparator();
        JLabel categoryLabel = createDetailLabel("카테고리: ");
        JSeparator separator2 = createSeparator();
        JLabel bestMenuLabel = createDetailLabel("대표메뉴: ");
        JSeparator separator3 = createSeparator();
        JLabel menuLabel = createDetailLabel("전체메뉴 ");
        JLabel menuInfoLabel1 = createDetailLabel("");
        JLabel menuInfoLabel2 = createDetailLabel("");
        JLabel menuInfoLabel3 = createDetailLabel("");
        JSeparator separator4 = createSeparator();
        JLabel sectionLabel = createDetailLabel("구역 이름: ");
        JSeparator separator5 = createSeparator();
        JLabel locationLabel = createDetailLabel("위치: ");
        JSeparator separator6 = createSeparator();
        JLabel veganLabel = createDetailLabel("비건메뉴 여부: ");
        JSeparator separator7 = createSeparator();
        JLabel eatAloneLabel = createDetailLabel("혼밥 가능 여부: ");
        JSeparator separator8 = createSeparator();
        JLabel breaktimeLabel = createDetailLabel("브레이크타임 유무: ");
        
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(avgRatingLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator1);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(categoryLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator2);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(bestMenuLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator3);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(menuLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(menuInfoLabel1);
        detailsContentPanel.add(menuInfoLabel2);
        detailsContentPanel.add(menuInfoLabel3);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator4);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(sectionLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator5);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(locationLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator6);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(veganLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator7);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(eatAloneLabel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(separator8);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(breaktimeLabel);

        // JScrollPane으로 감싸기
        JScrollPane scrollPane = new JScrollPane(detailsContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mainDetailPanel.add(scrollPane, BorderLayout.CENTER);

        // 리뷰 추가 또는 정보 수정 버튼 생성
        JButton actionButton = new JButton(isAdmin ? "정보 수정" : "리뷰 추가");
        actionButton.setFont(new Font("나눔고딕", Font.BOLD, 15));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(actionButton);
        
        mainDetailPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // 리뷰 추가 버튼 클릭 이벤트 추가
        if (!isAdmin) {
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	DB2024Team13_addReviewWindow.showAddReviewDialog(restaurant);
                }
            });
        }

        // 데이터베이스에서 세부 정보를 불러와 레이블에 설정
        loadRestaurantDetails(restaurant, bestMenuLabel, menuInfoLabel1, menuInfoLabel2, menuInfoLabel3, locationLabel, veganLabel, eatAloneLabel, breaktimeLabel, avgRatingLabel, categoryLabel, sectionLabel);

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

    /**
     * 구분선을 생성하는 메소드
     *
     * @return 생성된 구분선
     */
    private static JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

    /**
     * 데이터베이스에서 레스토랑의 세부 정보를 불러와 레이블에 설정하는 메소드
     *
     * @param restaurantName 레스토랑 이름
     * @param bestMenuLabel 대표메뉴 레이블
     * @param menuInfoLabel1 전체메뉴 1 레이블
     * @param menuInfoLabel2 전체메뉴 2 레이블
     * @param menuInfoLabel3 전체메뉴 3 레이블
     * @param locationLabel 위치 레이블
     * @param veganLabel 비건메뉴 여부 레이블
     * @param eatAloneLabel 혼밥 가능 여부 레이블
     * @param breaktimeLabel 브레이크타임 유무 레이블
     * @param avgRatingLabel 별점 레이블
     * @param categoryLabel 카테고리 레이블
     * @param sectionLabel 구역 이름 레이블
     */
    private static void loadRestaurantDetails(String restaurantName, JLabel bestMenuLabel, JLabel menuInfoLabel1, JLabel menuInfoLabel2, JLabel menuInfoLabel3, JLabel locationLabel, JLabel veganLabel, JLabel eatAloneLabel, JLabel breaktimeLabel, JLabel avgRatingLabel, JLabel categoryLabel, JLabel sectionLabel) {
        String detailsQuery = "SELECT r.best_menu, r.location, r.breaktime, r.eat_alone, r.category, r.section_name, AVG(rv.star) AS avg_rating " +
                       "FROM DB2024_restaurant r " +
                       "LEFT JOIN DB2024_review rv ON r.rest_name = rv.rest_name " +
                       "WHERE r.rest_name = ? " +
                       "GROUP BY r.rest_name, r.best_menu, r.location, r.breaktime, r.eat_alone, r.category, r.section_name";
        
        String menuQuery = "SELECT menu_name, price, vegan " +
                           "FROM DB2024_menu " +
                           "WHERE rest_name = ?";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement detailsStmt = conn.prepareStatement(detailsQuery);
             PreparedStatement menuStmt = conn.prepareStatement(menuQuery)) {
            
            detailsStmt.setString(1, restaurantName);
            try (ResultSet rs = detailsStmt.executeQuery()) {
                if (rs.next()) {
                    bestMenuLabel.setText("대표메뉴: " + rs.getString("best_menu"));
                    locationLabel.setText("위치: " + rs.getString("location"));
                    eatAloneLabel.setText("혼밥 가능 여부: " + (rs.getBoolean("eat_alone") ? "O" : "X"));
                    breaktimeLabel.setText("브레이크타임 유무: " + (rs.getBoolean("breaktime") ? "O" : "X"));
                    avgRatingLabel.setText("별점: " + String.format("%.2f", rs.getDouble("avg_rating")));
                    categoryLabel.setText("카테고리: " + rs.getString("category"));
                    sectionLabel.setText("구역 이름: " + rs.getString("section_name"));
                }
            }

            menuStmt.setString(1, restaurantName);
            int menuCount = 0;
            boolean hasVegan = false;
            try (ResultSet rs = menuStmt.executeQuery()) {
                while (rs.next() && menuCount < 3) {
                    String menuInfo = rs.getString("menu_name") + " - " +
                                      rs.getInt("price") + "원" +
                                      " (" + (rs.getBoolean("vegan") ? "비건" : "논비건") + ")";
                    if (rs.getBoolean("vegan")) {
                        hasVegan = true;
                    }
                    if (menuCount == 0) {
                        menuInfoLabel1.setText(menuInfo);
                    } else if (menuCount == 1) {
                        menuInfoLabel2.setText(menuInfo);
                    } else if (menuCount == 2) {
                        menuInfoLabel3.setText(menuInfo);
                    }
                    menuCount++;
                }
            }
            veganLabel.setText("비건메뉴 여부: " + (hasVegan ? "O" : "X"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
