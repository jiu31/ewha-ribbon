package db;

import db.view.DB2024Team13_loginWindow;

/**
 * 프로그램의 메인 클래스입니다.
 * <p>
 * 이 클래스는 프로그램의 시작을 담당하며, 사용자가 프로그램을 실행할 때 가장 먼저 호출됩니다.
 * 프로그램의 시작 지점에서 로그인 창을 표시하여 사용자가 로그인할 수 있도록 합니다.
 * </p>
 * <p>기본 생성자는 특별한 초기화 작업을 수행하지 않습니다.</p>
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
