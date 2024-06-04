package db.model;

public class DB2024Team13_userSessionManager {
	
    private static DB2024Team13_userSessionManager instance;
    private String studentId;
    private String nickname;

    private DB2024Team13_userSessionManager() {}

    public static DB2024Team13_userSessionManager getInstance() {
        if (instance == null) {
            instance = new DB2024Team13_userSessionManager();
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

    public boolean isAdmin() {
        return "0000".equals(studentId);
    }
}
