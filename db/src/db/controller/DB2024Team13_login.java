package db.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2024Team13_login {

    public static boolean validateLogin(String studentId, String password) {
        String query = "SELECT * FROM DB2024_customer WHERE student_id = ? AND password = ?";
        Connection connection = DB2024Team13_connection.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nickname = resultSet.getString("nickname");
                    DB2024Team13_userSession.getInstance().setUser(studentId, nickname);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
