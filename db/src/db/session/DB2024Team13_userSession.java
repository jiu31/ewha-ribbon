package db.session;

public class DB2024Team13_userSession {
    private static DB2024Team13_userSession instance;
    private String studentId;
    private String nickname;

    private DB2024Team13_userSession() {}

    public static DB2024Team13_userSession getInstance() {
        if (instance == null) {
            instance = new DB2024Team13_userSession();
        }
        return instance;
    }

    public void setUser(String studentId, String nickname) {
        this.studentId = studentId;
        this.nickname = nickname;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getNickname() {
        return nickname;
    }

    public void clearSession() {
        studentId = null;
        nickname = null;
    }
}
