package db.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import db.controller.DB2024Team13_connection;
import db.controller.DB2024Team13_userSession;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_myinfoWindow {

    // '내 정보' 패널 생성
    public static JPanel createMyInfoPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel myInfoPanel = new JPanel(new BorderLayout());
        myInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 사용자 정보 표시 패널 생성
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userInfoLabel = new JLabel("안녕하세요, " + DB2024Team13_userSession.getInstance().getNickname() + "님!");
        userInfoPanel.add(userInfoLabel);

        myInfoPanel.add(userInfoPanel, BorderLayout.NORTH);

        // 상단에 라디오 버튼 옵션 추가
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton bookmarkOption = new JRadioButton("북마크 목록");
        JRadioButton reviewOption = new JRadioButton("리뷰 조회");
        JRadioButton orderOption = new JRadioButton("주문 조회");

        optionsPanel.add(bookmarkOption);
        optionsPanel.add(reviewOption);
        optionsPanel.add(orderOption);

        ButtonGroup optionGroup = new ButtonGroup();
        optionGroup.add(bookmarkOption);
        optionGroup.add(reviewOption);
        optionGroup.add(orderOption);

        // 세 개의 독립적인 DefaultListModel과 JList 인스턴스 생성
        DefaultListModel<String> bookmarkListModel = new DefaultListModel<>();
        DefaultListModel<String> reviewListModel = new DefaultListModel<>();
        
        // 주문 리스트는 JTable과 DefaultTableModel을 사용
        DefaultTableModel orderTableModel = new DefaultTableModel(new String[]{"주문 일시", "식당", "메뉴"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 셀 수정 불가
            }
        };
        JTable orderTable = new JTable(orderTableModel);

        // JTable 속성 설정
        orderTable.setRowHeight(orderTable.getRowHeight() + 10); // 줄 간격 넓히기
        orderTable.setBackground(Color.WHITE); // 배경을 흰색으로 설정
        orderTable.setShowGrid(false); // 그리드 라인 숨기기

        // 정렬 기능 추가
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(orderTableModel);
        orderTable.setRowSorter(sorter);

        // 테이블의 부모 패널의 배경을 흰색으로 설정
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.getViewport().setBackground(Color.WHITE);

        JList<String> bookmarkList = new JList<>(bookmarkListModel);
        JList<String> reviewList = new JList<>(reviewListModel);

        // CardLayout을 사용하여 리스트와 테이블을 전환
        JPanel listContainerPanel = new JPanel(new CardLayout());
        listContainerPanel.add(new JScrollPane(bookmarkList), "북마크 목록");
        listContainerPanel.add(new JScrollPane(reviewList), "리뷰 조회");
        listContainerPanel.add(orderScrollPane, "주문 조회");

        // 옵션 패널과 리스트 컨테이너 패널을 하나의 패널에 넣고 수직으로 배치
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(optionsPanel);
        centerPanel.add(listContainerPanel);

        myInfoPanel.add(centerPanel, BorderLayout.CENTER);

        loadBookmarkList("", bookmarkListModel);
        loadReviewList("", reviewListModel);
        loadOrderList("", orderTableModel);

        // 옵션 선택에 따라 다른 리스트를 보여주도록 리스너 추가
        bookmarkOption.addActionListener(e -> showCard(listContainerPanel, "북마크 목록"));
        reviewOption.addActionListener(e -> showCard(listContainerPanel, "리뷰 조회"));
        orderOption.addActionListener(e -> showCard(listContainerPanel, "주문 조회"));

        // 기본적으로 첫 번째 옵션을 선택된 상태로 설정
        bookmarkOption.setSelected(true);
        showCard(listContainerPanel, "북마크 목록");

        return myInfoPanel;
    }

    // 데이터베이스에서 사용자가 북마크한 식당 리스트를 불러와 JList에 추가하는 메소드
    private static void loadBookmarkList(String searchText, DefaultListModel<String> listModel) {
        listModel.clear(); // 기존 리스트 초기화

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DB2024Team13_connection.getConnection();
            String studentId = DB2024Team13_userSession.getInstance().getStudentId();
            
            // 북마크된 식당을 검색하는 SQL 쿼리 작성
            String sql = "SELECT rest_name FROM DB2024_bookmark WHERE student_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            rs = pstmt.executeQuery();

            // 결과를 리스트 모델에 추가
            while (rs.next()) {
                String restaurantName = rs.getString("rest_name");
                listModel.addElement(restaurantName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 데이터베이스에서 사용자가 리뷰한 식당 리스트를 불러와 JList에 추가하는 메소드
    private static void loadReviewList(String searchText, DefaultListModel<String> listModel) {
        listModel.clear(); // 기존 리스트 초기화

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DB2024Team13_connection.getConnection();
            String studentId = DB2024Team13_userSession.getInstance().getStudentId();
            
            // 리뷰를 검색하는 SQL 쿼리 작성
            String sql = "SELECT rest_name, star FROM DB2024_review WHERE student_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            rs = pstmt.executeQuery();

            // 결과를 리스트 모델에 추가
            while (rs.next()) {
                String restaurantName = rs.getString("rest_name");
                int star = rs.getInt("star");
                String reviewText = restaurantName + " - 별점: " + star;
                listModel.addElement(reviewText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // 데이터베이스에서 사용자의 주문목록을 불러와 JTable에 추가하는 메소드
    private static void loadOrderList(String searchText, DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // 기존 테이블 초기화

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DB2024Team13_connection.getConnection();
            String studentId = DB2024Team13_userSession.getInstance().getStudentId();
            
            // 주문을 검색하는 SQL 쿼리 작성
            String sql = "SELECT rest_name, menu_name, order_date_time FROM DB2024_order WHERE student_id = ? ORDER BY order_date_time;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            rs = pstmt.executeQuery();

            // 결과를 테이블 모델에 추가
            while (rs.next()) {
                String restaurantName = rs.getString("rest_name");
                String menuName = rs.getString("menu_name");
                String dateTime = rs.getString("order_date_time");
                tableModel.addRow(new Object[]{dateTime, restaurantName, menuName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // CardLayout을 사용하여 카드 보여주기
    private static void showCard(JPanel panel, String card) {
        CardLayout cardLayout = (CardLayout) (panel.getLayout());
        cardLayout.show(panel, card);
    }
}
