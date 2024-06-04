package db.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 데이터베이스와 연결을 관리하는 클래스입니다.
 */
public class DB2024Team13_connection {
    private static final String DB_URL = "jdbc:mysql://localhost/DB2024Team13";
    private static final String USER = "DB2024Team13";
    private static final String PASS = "DB2024Team13";
    private static Connection connection = null;

    /**
     * 싱글톤 패턴으로 데이터베이스 연결을 반환합니다.
     * 
     * @return 데이터베이스와의 연결 객체
     */
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

    /**
     * 연결이 열려있는지 닫혀있는지 판단합니다.
     * 
     * @param conn 확인할 연결 객체
     * @return 연결이 닫혀있으면 true, 열려있으면 false
     */
    private static boolean isClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return true;
        }
    }
}
