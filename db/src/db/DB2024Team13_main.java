package db;

import db.view.DB2024Team13_loginWindow;

/**
 * 프로그램의 메인 클래스입니다.
 * <p>기본 생성자는 특별한 초기화 작업을 수행하지 않습니다.
 */
public class DB2024Team13_main {
	/**
     * 프로그램의 시작 지점입니다. 로그인 창을 표시합니다.
     *
     * @param args 커맨드 라인 인수
     */
    public static void main(String[] args) {
        // 로그인 창으로 시작
        DB2024Team13_loginWindow.displayLoginWindow();
    }
}
