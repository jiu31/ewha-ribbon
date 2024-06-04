package db.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 데이터베이스와 연결하는 클래스
public class DB2024Team13_connection {
    private static final String DB_URL = "jdbc:mysql://localhost/DB2024Team13";
    private static final String USER = "DB2024Team13";
    private static final String PASS = "DB2024Team13";
    private static Connection connection = null;

    // 싱글톤 패턴으로 구현
    public static Connection getConnection() {
    	// 연결이 없거나, 닫힌 경우에만 새로운 연결 생성
        if (connection == null || isClosed(connection)) {
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return connection;
    }
    
    // 연결이 열렸다가 닫힌 상황인지 판단
    private static boolean isClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return true;
        }
    }
}
