package db.view;

import db.controller.DB2024Team13_userSession;
import db.controller.DB2024Team13_connection;
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

    public static JPanel createDetailPanel(String restaurant) {
        boolean isAdmin = DB2024Team13_userSession.getInstance().isAdmin();
        String studentId = DB2024Team13_userSession.getInstance().getStudentId();

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

        loadRestaurantDetails(restaurant, mainDetailPanel);

        return mainDetailPanel;
    }

    private static JButton createBookmarkButton(String studentId, String restaurant) {
        JButton bookmarkButton = new JButton("★");
        bookmarkButton.setFont(new Font("나눔고딕", Font.BOLD, 24));
        bookmarkButton.setBackground(COLOR_NOT_BOOKMARKED);
        bookmarkButton.setForeground(COLOR_NOT_BOOKMARKED_TEXT);
        bookmarkButton.setOpaque(true);
        bookmarkButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        boolean isBookmarked = isRestaurantBookmarked(studentId, restaurant);

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
                    addBookmark(studentId, restaurant);
                } else {
                    bookmarkButton.setBackground(COLOR_NOT_BOOKMARKED);
                    bookmarkButton.setForeground(COLOR_NOT_BOOKMARKED_TEXT);
                    removeBookmark(studentId, restaurant);
                }
            }
        });

        return bookmarkButton;
    }

    private static void addDetailLabel(JPanel panel, String text, String name) {
        JLabel label = createDetailLabel(text);
        label.setName(name);
        panel.add(Box.createVerticalStrut(10));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createSeparator());
    }

    private static JLabel createDetailLabel(String text) {
        JLabel detailLabel = new JLabel(text);
        detailLabel.setFont(new Font("나눔고딕", Font.BOLD, 15));
        return detailLabel;
    }

    private static JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

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

    private static void loadRestaurantDetails(String restaurantName, JPanel mainDetailPanel) {
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
            ResultSet rs = detailsStmt.executeQuery();
            updateDetailLabels(rs, mainDetailPanel);

            menuStmt.setString(1, restaurantName);
            ResultSet menuRs = menuStmt.executeQuery();
            updateMenuLabels(menuRs, mainDetailPanel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateDetailLabels(ResultSet rs, JPanel mainDetailPanel) throws SQLException {
        if (rs.next()) {
            setLabelText(mainDetailPanel, "bestMenuLabel", "대표메뉴: " + rs.getString("best_menu"));
            setLabelText(mainDetailPanel, "locationLabel", "위치: " + rs.getString("location"));
            setLabelText(mainDetailPanel, "eatAloneLabel", "혼밥 가능 여부: " + (rs.getBoolean("eat_alone") ? "O" : "X"));
            setLabelText(mainDetailPanel, "breaktimeLabel", "브레이크타임 유무: " + (rs.getBoolean("breaktime") ? "O" : "X"));
            setLabelText(mainDetailPanel, "avgRatingLabel", "별점: " + String.format("%.2f", rs.getDouble("avg_rating")));
            setLabelText(mainDetailPanel, "categoryLabel", "카테고리: " + rs.getString("category"));
            setLabelText(mainDetailPanel, "sectionLabel", "구역 이름: " + rs.getString("section_name"));
        }
    }

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

    private static void setLabelText(Container container, String labelName, String text) {
        JLabel label = (JLabel) findComponentByName(container, labelName);
        if (label != null) {
            label.setText(text);
        }
    }

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

    private static boolean isRestaurantBookmarked(String studentId, String restaurantName) {
        String query = "SELECT COUNT(*) FROM DB2024_bookmark WHERE student_id = ? AND rest_name = ?";
        return executeCountQuery(query, studentId, restaurantName);
    }

    private static boolean executeCountQuery(String query, String param1, String param2) {
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param1);
            stmt.setString(2, param2);
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

    private static void addBookmark(String studentId, String restaurantName) {
        String query = "INSERT INTO DB2024_bookmark (student_id, rest_name) VALUES (?, ?)";
        executeUpdate(query, studentId, restaurantName);
    }

    private static void removeBookmark(String studentId, String restaurantName) {
        String query = "DELETE FROM DB2024_bookmark WHERE student_id = ? AND rest_name = ?";
        executeUpdate(query, studentId, restaurantName);
    }

    private static void executeUpdate(String query, String param1, String param2) {
        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param1);
            stmt.setString(2, param2);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void refreshDetailPanel(JPanel mainDetailPanel, String restaurant) {
        loadRestaurantDetails(restaurant, mainDetailPanel);
        mainDetailPanel.revalidate();
        mainDetailPanel.repaint();
    }
}
