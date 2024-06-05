package db.model;

/**
 * 사용자 세션을 관리하는 클래스입니다.
 */
public class DB2024Team13_userSessionManager {
    
    private static DB2024Team13_userSessionManager instance;
    private String studentId;
    private String nickname;

    /**
     * 기본 생성자. 외부에서 인스턴스화하지 못하도록 private으로 설정합니다.
     */
    private DB2024Team13_userSessionManager() {}

    /**
     * 싱글톤 인스턴스를 반환하는 메소드입니다.
     *
     * @return DB2024Team13_userSessionManager의 인스턴스
     */
    public static DB2024Team13_userSessionManager getInstance() {
        if (instance == null) {
            instance = new DB2024Team13_userSessionManager();
        }
        return instance;
    }

    /**
     * 사용자 정보를 설정하는 메소드입니다.
     *
     * @param studentId 설정할 학생 ID
     * @param nickname 설정할 닉네임
     */
    public void setUser(String studentId, String nickname) {
        this.studentId = studentId;
        this.nickname = nickname;
    }
    
    /**
     * 현재 세션의 학생 ID를 반환하는 메소드입니다.
     *
     * @return 현재 세션의 학생 ID
     */
    public String getStudentId() {
        return studentId;
    }
    
    /**
     * 현재 세션의 닉네임을 반환하는 메소드입니다.
     *
     * @return 현재 세션의 닉네임
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 현재 세션을 초기화하는 메소드입니다.
     */
    public void clearSession() {
        studentId = null;
        nickname = null;
    }

    /**
     * 현재 사용자가 관리자 권한을 가지고 있는지 확인하는 메소드입니다.
     *
     * @return 관리자인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    public boolean isAdmin() {
        return "0000".equals(studentId);
    }
}
