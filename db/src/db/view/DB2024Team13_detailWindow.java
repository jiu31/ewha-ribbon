package db.view;

import db.model.DB2024Team13_restaurantManager;
import db.model.DB2024Team13_bookmarkManager;
import db.model.DB2024Team13_userSessionManager;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Map;

/**
 * 레스토랑 상세 정보를 표시하는 창을 관리하는 클래스입니다.
 */
public class DB2024Team13_detailWindow {

    private static final Color COLOR_BOOKMARKED = new Color(0, 150, 0); // 초록색
    private static final Color COLOR_NOT_BOOKMARKED = new Color(200, 200, 200); // 기본 버튼 색상
    private static final Color COLOR_BOOKMARKED_TEXT = Color.WHITE; // 북마크된 상태의 텍스트 색상
    private static final Color COLOR_NOT_BOOKMARKED_TEXT = Color.BLACK; // 북마크되지 않은 상태의 텍스트 색상
    
    /**
     * 레스토랑 상세 정보를 표시하는 패널을 생성하는 메소드입니다.
     *
     * @param restaurant 레스토랑 이름
     * @return 설정된 JPanel 객체
     */
    public static JPanel createDetailPanel(String restaurant) {
        boolean isAdmin = DB2024Team13_userSessionManager.getInstance().isAdmin();
        String studentId = DB2024Team13_userSessionManager.getInstance().getStudentId();

        JPanel mainDetailPanel = new JPanel(new BorderLayout());
        mainDetailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel restaurantTitleLabel = new JLabel(restaurant, SwingConstants.CENTER);
        restaurantTitleLabel.setFont(new Font("나눔고딕", Font.BOLD, 24));

        titlePanel.add(restaurantTitleLabel, BorderLayout.CENTER);

        if (!isAdmin) {
            JButton bookmarkButton = createBookmarkButton(studentId, restaurant);
            titlePanel.add(bookmarkButton, BorderLayout.EAST);
        }

        mainDetailPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(Color.WHITE);

        addDetailLabel(detailsContentPanel, "별점: ", "avgRatingLabel");
        addDetailLabel(detailsContentPanel, "카테고리: ", "categoryLabel");
        addDetailLabel(detailsContentPanel, "대표메뉴: ", "bestMenuLabel");
        addDetailLabel(detailsContentPanel, "전체메뉴 ", "menuLabel");

        JPanel menuInfoPanel = new JPanel();
        menuInfoPanel.setLayout(new BoxLayout(menuInfoPanel, BoxLayout.Y_AXIS));
        menuInfoPanel.setName("menuInfoPanel");
        detailsContentPanel.add(menuInfoPanel);
        detailsContentPanel.add(Box.createVerticalStrut(10)); // 공백 추가
        detailsContentPanel.add(createSeparator());

        addDetailLabel(detailsContentPanel, "구역 이름: ", "sectionLabel");
        addDetailLabel(detailsContentPanel, "위치: ", "locationLabel");
        addDetailLabel(detailsContentPanel, "비건메뉴 여부: ", "veganLabel");
        addDetailLabel(detailsContentPanel, "혼밥 가능 여부: ", "eatAloneLabel");
        addDetailLabel(detailsContentPanel, "브레이크타임 유무: ", "breaktimeLabel");

        JScrollPane scrollPane = new JScrollPane(detailsContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mainDetailPanel.add(scrollPane, BorderLayout.CENTER);

        JButton actionButton = new JButton(isAdmin ? "정보 관리" : "리뷰 추가");
        actionButton.setFont(new Font("나눔고딕", Font.BOLD, 15));

        JButton menuEditButton = new JButton("메뉴 관리");
        menuEditButton.setFont(new Font("나눔고딕", Font.BOLD, 15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        if (isAdmin) {
            buttonPanel.add(menuEditButton);
        }
        buttonPanel.add(actionButton);

        mainDetailPanel.add(buttonPanel, BorderLayout.SOUTH);

        addAdminActionListeners(isAdmin, restaurant, mainDetailPanel, menuEditButton, actionButton);

        Map<String, Object> details = DB2024Team13_restaurantManager.loadRestaurantDetails(restaurant);
        updateDetailLables(details, mainDetailPanel);

        try {
            ResultSet menuRs = DB2024Team13_restaurantManager.getMenuItems(restaurant);
            updateMenuLabels(menuRs, mainDetailPanel);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mainDetailPanel;
    }

    /**
     * 북마크 버튼을 생성하는 메소드입니다.
     *
     * @param studentId 학생 ID
     * @param restaurant 레스토랑 이름
     * @return 생성된 JButton 객체
     */
    private static JButton createBookmarkButton(String studentId, String restaurant) {
        JButton bookmarkButton = new JButton("★");
        bookmarkButton.setFont(new Font("나눔고딕", Font.BOLD, 24));
        bookmarkButton.setBackground(COLOR_NOT_BOOKMARKED);
        bookmarkButton.setForeground(COLOR_NOT_BOOKMARKED_TEXT);
        bookmarkButton.setOpaque(true);
        bookmarkButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        boolean isBookmarked = DB2024Team13_bookmarkManager.isRestaurantBookmarked(studentId, restaurant);

        if (isBookmarked) {
            bookmarkButton.setBackground(COLOR_BOOKMARKED);
            bookmarkButton.setForeground(COLOR_BOOKMARKED_TEXT);
        }

        bookmarkButton.addActionListener(new ActionListener() {
            private boolean bookmarked = isBookmarked;

            @Override
            public void actionPerformed(ActionEvent e) {
                bookmarked = !bookmarked;
                if (bookmarked) {
                    bookmarkButton.setBackground(COLOR_BOOKMARKED);
                    bookmarkButton.setForeground(COLOR_BOOKMARKED_TEXT);
                    DB2024Team13_bookmarkManager.addBookmark(studentId, restaurant);
                } else {
                    bookmarkButton.setBackground(COLOR_NOT_BOOKMARKED);
                    bookmarkButton.setForeground(COLOR_NOT_BOOKMARKED_TEXT);
                    DB2024Team13_bookmarkManager.removeBookmark(studentId, restaurant);
                }
            }
        });

        return bookmarkButton;
    }

    /**
     * 상세 정보 패널에 레이블을 추가하는 메소드입니다.
     *
     * @param panel 추가할 JPanel 객체
     * @param text 레이블 텍스트
     * @param name 레이블 이름
     */
    private static void addDetailLabel(JPanel panel, String text, String name) {
        JLabel label = createDetailLabel(text);
        label.setName(name);
        panel.add(Box.createVerticalStrut(10));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createSeparator());
    }

    /**
     * 상세 정보 레이블을 생성하는 메소드입니다.
     *
     * @param text 레이블 텍스트
     * @return 생성된 JLabel 객체
     */
    private static JLabel createDetailLabel(String text) {
        JLabel detailLabel = new JLabel(text);
        detailLabel.setFont(new Font("나눔고딕", Font.BOLD, 15));
        return detailLabel;
    }

    /**
     * 구분선을 생성하는 메소드입니다.
     *
     * @return 생성된 JSeparator 객체
     */
    private static JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

    /**
     * 관리자용 액션 리스너를 추가하는 메소드입니다.
     *
     * @param isAdmin 관리자 여부
     * @param restaurant 레스토랑 이름
     * @param mainDetailPanel 메인 상세 정보 패널
     * @param menuEditButton 메뉴 수정 버튼
     * @param actionButton 액션 버튼
     */
    private static void addAdminActionListeners(boolean isAdmin, String restaurant, JPanel mainDetailPanel, JButton menuEditButton, JButton actionButton) {
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
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DB2024Team13_addReviewWindow.showAddReviewDialog(restaurant, mainDetailPanel);
                }
            });
        }
    }

    /**
     * 상세 정보 레이블을 업데이트하는 메소드입니다.
     *
     * @param details 레스토랑 세부 정보
     * @param mainDetailPanel 메인 상세 정보 패널
     */
    private static void updateDetailLables(Map<String, Object> details, JPanel mainDetailPanel) {
        setLabelText(mainDetailPanel, "avgRatingLabel", "별점: " + String.format("%.2f", (Double) details.getOrDefault("avgRating", 0.0)));
        setLabelText(mainDetailPanel, "categoryLabel", "카테고리: " + details.getOrDefault("category", "N/A"));
        setLabelText(mainDetailPanel, "bestMenuLabel", "대표메뉴: " + details.getOrDefault("bestMenu", "N/A"));
        setLabelText(mainDetailPanel, "sectionLabel", "구역 이름: " + details.getOrDefault("section", "N/A"));
        setLabelText(mainDetailPanel, "locationLabel", "위치: " + details.getOrDefault("location", "N/A"));
        setLabelText(mainDetailPanel, "veganLabel", "비건메뉴 여부: " + ((boolean) details.getOrDefault("vegan", false) ? "O" : "X"));
        setLabelText(mainDetailPanel, "eatAloneLabel", "혼밥 가능 여부: " + ((boolean) details.getOrDefault("eatAlone", false) ? "O" : "X"));
        setLabelText(mainDetailPanel, "breaktimeLabel", "브레이크타임 유무: " + ((boolean) details.getOrDefault("breaktime", false) ? "O" : "X"));
    }
    
    /**
     * 메뉴 레이블을 업데이트하는 메소드입니다.
     *
     * @param rs 메뉴 정보 ResultSet 객체
     * @param mainDetailPanel 메인 상세 정보 패널
     * @throws SQLException SQL 예외 발생 시
     */
    private static void updateMenuLabels(ResultSet rs, JPanel mainDetailPanel) throws SQLException {
        JPanel menuInfoPanel = (JPanel) findComponentByName(mainDetailPanel, "menuInfoPanel");
        if (menuInfoPanel == null) {
            throw new IllegalStateException("menuInfoPanel not found in mainDetailPanel");
        }
        boolean hasVegan = false;
        menuInfoPanel.removeAll();
        menuInfoPanel.setBackground(Color.WHITE);
        while (rs.next()) {
            String menuInfo = rs.getString("menu_name") + " - " +
                              rs.getInt("price") + "원" +
                              " (" + (rs.getBoolean("vegan") ? "비건" : "논비건") + ")";
            if (rs.getBoolean("vegan")) {
                hasVegan = true;
            }
            JLabel menuLabel = createDetailLabel(menuInfo);
            menuLabel.setOpaque(false);
            menuInfoPanel.add(menuLabel);
        }
        setLabelText(mainDetailPanel, "veganLabel", "비건메뉴 여부: " + (hasVegan ? "O" : "X"));
        menuInfoPanel.revalidate();
        menuInfoPanel.repaint();
    }

    /**
     * 컨테이너에서 레이블 텍스트를 설정하는 메소드입니다.
     *
     * @param container 대상 Container 객체
     * @param labelName 레이블 이름
     * @param text 설정할 텍스트
     */
    private static void setLabelText(Container container, String labelName, String text) {
        JLabel label = (JLabel) findComponentByName(container, labelName);
        if (label != null) {
            label.setText(text);
        }
    }
    
    /**
     * 컨테이너에서 이름으로 컴포넌트를 찾는 메소드입니다.
     *
     * @param container 대상 Container 객체
     * @param name 컴포넌트 이름
     * @return 찾은 컴포넌트, 없을 경우 null
     */
    private static Component findComponentByName(Container container, String name) {
        if (container == null) {
            return null;
        }
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

    /**
     * 상세 정보 패널을 새로고침하는 메소드입니다.
     *
     * @param mainDetailPanel 메인 상세 정보 패널
     * @param restaurant 레스토랑 이름
     */
    public static void refreshDetailPanel(JPanel mainDetailPanel, String restaurant) {
        Map<String, Object> details = DB2024Team13_restaurantManager.loadRestaurantDetails(restaurant);
        updateDetailLables(details, mainDetailPanel);

        try {
            ResultSet menuRs = DB2024Team13_restaurantManager.getMenuItems(restaurant);
            updateMenuLabels(menuRs, mainDetailPanel);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainDetailPanel.revalidate();
        mainDetailPanel.repaint();
    }
}
