package db.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB2024Team13_connection {
    private static final String DB_URL = "jdbc:mysql://localhost/DB2024Team13";
    private static final String USER = "DB2024Team13";
    private static final String PASS = "DB2024Team13";
    private static Connection connection = null;

    // 싱글톤 패턴으로 구현
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("데이터베이스 연결 성공!");
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
            }
        }
        return connection;
    }

    // 메인 메소드로 연결 테스트
    public static void main(String[] args) {
        getConnection();
    }
}
