package db.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

// 데이터베이스에 SQL 쿼리를 실행하는 클래스
public class DB2024Team13_query {
	// 로그인 기능을 구현하는 메소드
	public static boolean validateLogin(String studentId, String password) {
		// 입력받은 학번과 비밀번호를 갖는 사용자 검색하는 질의문
        String query = "SELECT * FROM DB2024_customer WHERE student_id = ? AND password = ?";
        Connection connection = DB2024Team13_connection.getConnection();

        try (PreparedStatement pStmt = connection.prepareStatement(query)) {
            pStmt.setString(1, studentId);
            pStmt.setString(2, password);
            
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    String nickname = rs.getString("nickname");
                    // 사용자의 학번과 닉네임을 가져와서 유저 세션 설정
                    DB2024Team13_userSession.getInstance().setUser(studentId, nickname);
                    return true;
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        // 해당하는 사용자가 데이터베이스에 없으면 로그인 실패, false 반환
        return false;
    }
    
	// searchWindow에서 호출
	// 입력받은 식당 이름으로 데이터베이스에서 식당을 검색하는 메소드
    public static void searchRestaurant(String searchText, DefaultListModel<String> listModel) {
        listModel.clear();
        // 데이터베이스에 식당 이름으로 식당 검색하는 질의문
        String query = "SELECT rest_name FROM DB2024_restaurant WHERE rest_name LIKE ? ORDER BY rest_name";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query)) {
        	// 상황에 맞는 searchText
            pStmt.setString(1, "%" + searchText + "%");
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    String restaurantName = rs.getString("rest_name");
                    listModel.addElement(restaurantName);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    
    // addReviewWindow에서 호출
    // 데이터베이스에 리뷰 추가
    public static boolean addReview(String restaurantName, String studentId, int star) {
    	
        String query1 = "SELECT MAX(review_id) FROM DB2024_review";
        String query2 = "INSERT INTO DB2024_review (review_id, rest_name, student_id, star) VALUES (?, ?, ?, ?)";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt1 = conn.prepareStatement(query1);
             PreparedStatement pStmt2 = conn.prepareStatement(query2)) {
            
            // review_id 최댓값 검색
            ResultSet rs = pStmt1.executeQuery();
            int newReviewId = 1; // 아직 리뷰가 없을 경우 1
            if (rs.next()) {
                newReviewId = rs.getInt(1) + 1;
            }

            // 새로운 리뷰 삽입
            pStmt2.setInt(1, newReviewId);
            pStmt2.setString(2, restaurantName);
            pStmt2.setString(3, studentId);
            pStmt2.setInt(4, star);
            pStmt2.executeUpdate();

            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    
    // myInfoWindow에서 호출
    // 데이터베이스에서 query 실행 결과를 불러와 JList에 추가하는 메소드
    public static void filterRestaurant(String query, DefaultListModel<String> listModel) {
        listModel.clear(); // 기존 리스트 초기화

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            String studentId = DB2024Team13_userSession.getInstance().getStudentId();
            pStmt.setString(1, studentId);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    if (listModel instanceof DefaultListModel) {
                        String restaurantName = rs.getString("rest_name");
                        // 리뷰 검색 질의문일 경우, 별점 표기
                        if (rs.getMetaData().getColumnCount() > 1) {
                            int star = rs.getInt("star");
                            String reviewText = restaurantName + " - 별점: " + star;
                            listModel.addElement(reviewText);
                        } else {
                            listModel.addElement(restaurantName);
                        }
                    }
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    
    // myInfoWindow에서 호출
    // 데이터베이스에서 사용자의 주문목록을 불러와 JTable에 추가하는 메소드
    public static void searchOrder(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // 기존 테이블 초기화

        String query = "SELECT rest_name, menu_name, order_date_time FROM DB2024_order WHERE student_id = ? ORDER BY order_date_time";

        try (Connection conn = DB2024Team13_connection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            String studentId = DB2024Team13_userSession.getInstance().getStudentId();
            pStmt.setString(1, studentId);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    String restaurantName = rs.getString("rest_name");
                    String menuName = rs.getString("menu_name");
                    String dateTime = rs.getString("order_date_time");
                    tableModel.addRow(new Object[]{dateTime, restaurantName, menuName});
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
