package db.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DB2024Team13_mainWindow {
    // 상수 정의
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color COLOR_EWHA_GREEN = new Color(0, 72, 42);
    private static final Color COLOR_SELECTED_BUTTON = Color.GRAY;
    private static final Color COLOR_DEFAULT_BUTTON = Color.LIGHT_GRAY;
    private static final Color COLOR_SELECTED_BUTTON_TEXT = Color.WHITE;
    private static final Color COLOR_DEFAULT_BUTTON_TEXT = Color.BLACK;

    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final List<JButton> sidebarButtonList = new ArrayList<>();

    // 메인 윈도우 실행 메소드
    public static void launchMainWindow() {
        SwingUtilities.invokeLater(() -> {
            DB2024Team13_mainWindow app = new DB2024Team13_mainWindow();
            JFrame frame = app.createMainFrame();
            frame.setVisible(true);
        });
    }

    // 메인 프레임 생성
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

    // 제목 바 패널 생성
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

    // 사이드바 패널 생성
    public JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, WINDOW_HEIGHT));
        sidebarPanel.setBackground(COLOR_DEFAULT_BUTTON);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        addSidebarButton(sidebarPanel, "메인", e -> displayContent(DB2024Team13_customWindow.createSelectionPanel(this)));
        addSidebarButton(sidebarPanel, "검색", e -> displayContent(DB2024Team13_searchWindow.createSearchPanel(this)));
        addSidebarButton(sidebarPanel, "내정보", e -> displayContent(DB2024Team13_myinfoWindow.createMyInfoPanel(this)));

        sidebarPanel.add(Box.createVerticalGlue());

        return sidebarPanel;
    }

    // 사이드바에 버튼 추가
    private void addSidebarButton(JPanel sidebarPanel, String text, ActionListener actionListener) {
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
    }

    // 선택된 버튼 강조
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

    // 메인 콘텐츠 패널 반환
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // 상세 정보 보여주기
    public void displayDetail(String restaurant) {
        JPanel detailPanel = DB2024Team13_detailWindow.createDetailPanel(restaurant);
        displayContent(detailPanel);
    }

    // 콘텐츠 보여주기
    private void displayContent(Component component) {
        mainPanel.removeAll();
        mainPanel.add(component, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
