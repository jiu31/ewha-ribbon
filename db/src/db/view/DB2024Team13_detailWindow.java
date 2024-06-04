package db.view;

import db.model.DB2024Team13_connection;
import db.model.DB2024Team13_userSession;
import db.view.DB2024Team13_utils;

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
        String studentId = DB2024Team13_userSession.getInstance().getStudentId();

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
                private boolean isBookmarked = isRestaurantBookmarked(studentId, restaurant);

                @Override
                public void actionPerformed(ActionEvent e) {
                    isBookmarked = !isBookmarked;
                    if (isBookmarked) {
                        bookmarkButton.setBackground(COLOR_BOOKMARKED);
                        bookmarkButton.setForeground(COLOR_BOOKMARKED_TEXT);
                        addBookmark(studentId, restaurant);
                    } else {
                        bookmarkButton.setBackground(COLOR_NOT_BOOKMARKED);
                        bookmarkButton.setForeground(COLOR_NOT_BOOKMARKED_TEXT);
                        removeBookmark(studentId, restaurant);
                    }
                }
            });

            if (isRestaurantBookmarked(studentId, restaurant)) {
                bookmarkButton.setBackground(COLOR_BOOKMARKED);
                bookmarkButton.setForeground(COLOR_BOOKMARKED_TEXT);
            }

            titlePanel.add(bookmarkButton, BorderLayout.EAST);
        }

        mainDetailPanel.add(titlePanel, BorderLayout.NORTH);

        // 상세 내용을 표시할 패널 생성
        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(Color.WHITE);

        // 상세 정보 레이블 추가
        JLabel avgRatingLabel = createDetailLabel("별점: ");
        avgRatingLabel.setName("avgRatingLabel");
        JSeparator separator1 = createSeparator();
        JLabel categoryLabel = createDetailLabel("카테고리: ");
        categoryLabel.setName("categoryLabel");
        JSeparator separator2 = createSeparator();
        JLabel bestMenuLabel = createDetailLabel("대표메뉴: ");
        bestMenuLabel.setName("bestMenuLabel");
        JSeparator separator3 = createSeparator();
        JLabel menuLabel = createDetailLabel("전체메뉴 ");
        JPanel menuInfoPanel = new JPanel();
        menuInfoPanel.setLayout(new BoxLayout(menuInfoPanel, BoxLayout.Y_AXIS));
        menuInfoPanel.setName("menuInfoPanel");
        JSeparator separator4 = createSeparator();
        JLabel sectionLabel = createDetailLabel("구역 이름: ");
        sectionLabel.setName("sectionLabel");
        JSeparator separator5 = createSeparator();
        JLabel locationLabel = createDetailLabel("위치: ");
        locationLabel.setName("locationLabel");
        JSeparator separator6 = createSeparator();
        JLabel veganLabel = createDetailLabel("비건메뉴 여부: ");
        veganLabel.setName("veganLabel");
        JSeparator separator7 = createSeparator();
        JLabel eatAloneLabel = createDetailLabel("혼밥 가능 여부: ");
        eatAloneLabel.setName("eatAloneLabel");
        JSeparator separator8 = createSeparator();
        JLabel breaktimeLabel = createDetailLabel("브레이크타임 유무: ");
        breaktimeLabel.setName("breaktimeLabel");

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
        detailsContentPanel.add(menuInfoPanel);
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

        // 리뷰 추가 또는 정보 수정 및 메뉴 수정 버튼 생성
        JButton actionButton = new JButton(isAdmin ? "정보 관리" : "리뷰 추가");
        actionButton.setFont(new Font("나눔고딕", Font.BOLD, 15));

        JButton menuEditButton = new JButton("메뉴 관리");
        menuEditButton.setFont(new Font("나눔고딕", Font.BOLD, 15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        if (isAdmin) {
            buttonPanel.add(menuEditButton); // 메뉴 수정 버튼을 정보 수정 버튼 왼쪽에 추가
        }
        buttonPanel.add(actionButton);

        mainDetailPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 메뉴 수정 버튼 클릭 이벤트 추가
        if (isAdmin) {
            menuEditButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DB2024Team13_menuFixWindow.showMenuFixWindow(restaurant, mainDetailPanel);
                }
            });

            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DB2024Team13_adminWindow.showAdminWindow(restaurant, mainDetailPanel);
                }
            });
        } else {
            // 리뷰 추가 버튼 클릭 이벤트 추가
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DB2024Team13_addReviewWindow.showAddReviewDialog(restaurant, mainDetailPanel);
                }
            });
        }

        // 데이터베이스에서 세부 정보를 불러와 레이블에 설정
        loadRestaurantDetails(restaurant, bestMenuLabel, menuInfoPanel, locationLabel, veganLabel, eatAloneLabel, breaktimeLabel, avgRatingLabel, categoryLabel, sectionLabel);

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
     * @param menuInfoPanel 전체메뉴 패널
     * @param locationLabel 위치 레이블
     * @param veganLabel 비건메뉴 여부 레이블
     * @param eatAloneLabel 혼밥 가능 여부 레이블
     * @param breaktimeLabel 브레이크타임 유무 레이블
     * @param avgRatingLabel 별점 레이블
     * @param categoryLabel 카테고리 레이블
     * @param sectionLabel 구역 이름 레이블
     * @return 성공 여부
     */
    private static boolean loadRestaurantDetails(String restaurantName, JLabel bestMenuLabel, JPanel menuInfoPanel, JLabel locationLabel, JLabel veganLabel, JLabel eatAloneLabel, JLabel breaktimeLabel, JLabel avgRatingLabel, JLabel categoryLabel, JLabel sectionLabel) {
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
                } else {
                    return false;
                }
            }

            menuStmt.setString(1, restaurantName);
            boolean hasVegan = false;
            menuInfoPanel.removeAll(); // 패널 초기화
            menuInfoPanel.setBackground(Color.WHITE); // 패널 배경색 설정
            try (ResultSet rs = menuStmt.executeQuery()) {
                while (rs.next()) {
                    String menuInfo = rs.getString("menu_name") + " - " +
                                      rs.getInt("price") + "원" +
                                      " (" + (rs.getBoolean("vegan") ? "비건" : "논비건") + ")";
                    if (rs.getBoolean("vegan")) {
                        hasVegan = true;
                    }
                    JLabel menuLabel = createDetailLabel(menuInfo);
                    menuLabel.setOpaque(false); // 레이블 배경 투명 설정
                    menuInfoPanel.add(menuLabel);
                }
            }
            veganLabel.setText("비건메뉴 여부: " + (hasVegan ? "O" : "X"));
            menuInfoPanel.revalidate(); // 패널 갱신
            menuInfoPanel.repaint(); // 패널 다시 그리기

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 주어진 레스토랑이 북마크되었는지 확인하는 메소드
     *
     * @param studentId 학생 ID
     * @param restaurantName 레스토랑 이름
     * @return 북마크 여부
     */
    private static boolean isRestaurantBookmarked(String studentId, String restaurantName) {
        String query = "SELECT COUNT(*) FROM DB2024_bookmark WHERE student_id = ? AND rest_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, restaurantName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 주어진 레스토랑을 북마크에 추가하는 메소드
     *
     * @param studentId 학생 ID
     * @param restaurantName 레스토랑 이름
     */
    private static void addBookmark(String studentId, String restaurantName) {
        String query = "INSERT INTO DB2024_bookmark (student_id, rest_name) VALUES (?, ?)";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, restaurantName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 주어진 레스토랑을 북마크에서 삭제하는 메소드
     *
     * @param studentId 학생 ID
     * @param restaurantName 레스토랑 이름
     */
    private static void removeBookmark(String studentId, String restaurantName) {
        String query = "DELETE FROM DB2024_bookmark WHERE student_id = ? AND rest_name = ?";
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, restaurantName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 패널을 갱신하는 메소드
     *
     * @param mainDetailPanel 메인 상세 패널
     * @param restaurant 레스토랑 이름
     */
    public static void refreshDetailPanel(JPanel mainDetailPanel, String restaurant) {
        JLabel bestMenuLabel = (JLabel) findComponentByName(mainDetailPanel, "bestMenuLabel");
        JPanel menuInfoPanel = (JPanel) findComponentByName(mainDetailPanel, "menuInfoPanel");
        JLabel locationLabel = (JLabel) findComponentByName(mainDetailPanel, "locationLabel");
        JLabel veganLabel = (JLabel) findComponentByName(mainDetailPanel, "veganLabel");
        JLabel eatAloneLabel = (JLabel) findComponentByName(mainDetailPanel, "eatAloneLabel");
        JLabel breaktimeLabel = (JLabel) findComponentByName(mainDetailPanel, "breaktimeLabel");
        JLabel avgRatingLabel = (JLabel) findComponentByName(mainDetailPanel, "avgRatingLabel");
        JLabel categoryLabel = (JLabel) findComponentByName(mainDetailPanel, "categoryLabel");
        JLabel sectionLabel = (JLabel) findComponentByName(mainDetailPanel, "sectionLabel");

        loadRestaurantDetails(restaurant, bestMenuLabel, menuInfoPanel, locationLabel, veganLabel, eatAloneLabel, breaktimeLabel, avgRatingLabel, categoryLabel, sectionLabel);

        mainDetailPanel.revalidate();
        mainDetailPanel.repaint();
    }

    // 패널 내에서 이름으로 컴포넌트를 찾는 유틸리티 메서드 추가
    private static Component findComponentByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
            if (component instanceof Container) {
                Component found = findComponentByName((Container) component, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
