package db.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 메인 윈도우를 관리하는 클래스입니다.
 */
public class DB2024Team13_mainWindow {
    // 상수 정의
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;
  
    private static final Color COLOR_EWHA_GREEN = new Color(0, 72, 42);
    private static final Color COLOR_SELECTED_BUTTON = Color.GRAY;
    private static final Color COLOR_DEFAULT_BUTTON = Color.LIGHT_GRAY;
    private static final Color COLOR_SELECTED_BUTTON_TEXT = Color.WHITE;
    private static final Color COLOR_DEFAULT_BUTTON_TEXT = Color.BLACK;

    // 메인 패널과 사이드바 버튼 리스트 초기화
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final List<JButton> sidebarButtonList = new ArrayList<>();
    private JButton mainButton;

    /**
     * 메인 윈도우를 실행하는 메소드입니다.
     */
    public static void launchMainWindow() {
        SwingUtilities.invokeLater(() -> {
            DB2024Team13_mainWindow app = new DB2024Team13_mainWindow();
            JFrame frame = app.createMainFrame();
            frame.setVisible(true);
        });
    }

    /**
     * 생성자
     */
    public DB2024Team13_mainWindow() {
        // 초기 컨텐츠를 "메인" 패널로 설정하고 사이드바의 "메인" 버튼 하이라이트
        displayContent(DB2024Team13_customWindow.createSelectionPanel(this));
    }

    /**
     * 메인 프레임을 생성하는 메소드입니다.
     *
     * @return 생성된 JFrame 객체
     */
    private JFrame createMainFrame() {
        JFrame frame = new JFrame("EwhaRibbon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLayout(new BorderLayout());

        frame.add(createTitleBar(), BorderLayout.NORTH);
        frame.add(createSidebar(), BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        return frame;
    }

    /**
     * 타이틀 바 패널을 생성하는 메소드입니다.
     *
     * @return 생성된 JPanel 객체
     */
    public JPanel createTitleBar() {
        JPanel titleBarPanel = new JPanel(new BorderLayout());
        titleBarPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 50));
        titleBarPanel.setBackground(COLOR_EWHA_GREEN);

        JLabel titleLabel = new JLabel("EwhaRibbon", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        titleBarPanel.add(titleLabel, BorderLayout.CENTER);
        return titleBarPanel;
    }

    /**
     * 사이드바 패널을 생성하는 메소드입니다.
     *
     * @return 생성된 JPanel 객체
     */
    public JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, WINDOW_HEIGHT));
        sidebarPanel.setBackground(COLOR_DEFAULT_BUTTON);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // "메인" 버튼을 생성하고 mainButton 필드에 할당
        mainButton = addSidebarButton(sidebarPanel, "메인", e -> displayContent(DB2024Team13_customWindow.createSelectionPanel(this)));
        // 나머지 버튼 생성
        addSidebarButton(sidebarPanel, "검색", e -> displayContent(DB2024Team13_searchWindow.createSearchPanel(this)));
        addSidebarButton(sidebarPanel, "내정보", e -> displayContent(DB2024Team13_myInfoWindow.createMyInfoPanel(this)));
        sidebarPanel.add(Box.createVerticalGlue());
        
        // 로그아웃 버튼 추가
        addLogoutButton(sidebarPanel);

        // 초기화 시 "메인" 버튼을 하이라이트
        setSelectedButtonHighlight(mainButton);

        return sidebarPanel;
    }

    /**
     * 사이드바 버튼을 추가하는 메소드입니다.
     *
     * @param sidebarPanel 사이드바 패널
     * @param text 버튼 텍스트
     * @param actionListener 버튼 액션 리스너
     * @return 생성된 JButton 객체
     */
    private JButton addSidebarButton(JPanel sidebarPanel, String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBackground(COLOR_DEFAULT_BUTTON);
        button.setForeground(COLOR_DEFAULT_BUTTON_TEXT);
        button.addActionListener(e -> {
            actionListener.actionPerformed(e);
            setSelectedButtonHighlight(button);
        });
        sidebarButtonList.add(button);
        sidebarPanel.add(button);
        return button;  // 생성된 버튼을 반환
    }

    /**
     * 로그아웃 버튼을 추가하는 메소드입니다.
     *
     * @param sidebarPanel 사이드바 패널
     */
    private void addLogoutButton(JPanel sidebarPanel) {
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        logoutButton.setBackground(COLOR_DEFAULT_BUTTON);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
            frame.dispose();  // 현재 창 닫기
            DB2024Team13_loginWindow.displayLoginWindow();  // 로그인 창 표시
        });
        sidebarPanel.add(logoutButton);
    }

    /**
     * 선택된 사이드바 버튼을 강조하는 메소드입니다.
     *
     * @param selectedButton 선택된 버튼
     */
    private void setSelectedButtonHighlight(JButton selectedButton) {
        for (JButton button : sidebarButtonList) {
            if (button == selectedButton) {
                button.setBackground(COLOR_SELECTED_BUTTON);
                button.setForeground(COLOR_SELECTED_BUTTON_TEXT);
            } else {
                button.setBackground(COLOR_DEFAULT_BUTTON);
                button.setForeground(COLOR_DEFAULT_BUTTON_TEXT);
            }
        }
    }

    /**
     * 메인 컨텐츠 패널을 반환하는 메소드입니다.
     *
     * @return 메인 패널 객체
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * 상세 정보를 표시하는 메소드입니다.
     *
     * @param restaurant 레스토랑 이름
     */
    public void displayDetail(String restaurant) {
        JPanel detailPanel = DB2024Team13_detailWindow.createDetailPanel(restaurant);
        displayContent(detailPanel);
    }

    /**
     * 컨텐츠를 표시하는 메소드입니다.
     *
     * @param component 표시할 컴포넌트
     */
    private void displayContent(Component component) {
        mainPanel.removeAll();
        mainPanel.add(component, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
