package db.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DB2024Team13_mainWindow {
    // Constants
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;
  
    private static final Color COLOR_EWHA_GREEN = new Color(0, 72, 42);
    private static final Color COLOR_SELECTED_BUTTON = Color.GRAY;
    private static final Color COLOR_DEFAULT_BUTTON = Color.LIGHT_GRAY;
    private static final Color COLOR_SELECTED_BUTTON_TEXT = Color.WHITE;
    private static final Color COLOR_DEFAULT_BUTTON_TEXT = Color.BLACK;

    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final List<JButton> sidebarButtonList = new ArrayList<>();
    private JButton mainButton;  // "메인" 버튼을 참조할 필드를 추가합니다.

    // Main window execution method
    public static void launchMainWindow() {
        SwingUtilities.invokeLater(() -> {
            DB2024Team13_mainWindow app = new DB2024Team13_mainWindow();
            JFrame frame = app.createMainFrame();
            frame.setVisible(true);
        });
    }

    // Constructor
    public DB2024Team13_mainWindow() {
        // Set initial content to "메인" panel and highlight "메인" button
        displayContent(DB2024Team13_customWindow.createSelectionPanel(this));
    }

    // Create main frame
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

    // Create title bar panel
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

    // Create sidebar panel
    public JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, WINDOW_HEIGHT));
        sidebarPanel.setBackground(COLOR_DEFAULT_BUTTON);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // "메인" 버튼을 생성하고 mainButton 필드에 할당합니다.
        mainButton = addSidebarButton(sidebarPanel, "메인", e -> displayContent(DB2024Team13_customWindow.createSelectionPanel(this)));
        addSidebarButton(sidebarPanel, "검색", e -> displayContent(DB2024Team13_searchWindow.createSearchPanel(this)));
        addSidebarButton(sidebarPanel, "내정보", e -> displayContent(DB2024Team13_myinfoWindow.createMyInfoPanel(this)));
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Add logout button
        addLogoutButton(sidebarPanel);

        // 초기화 시 "메인" 버튼을 하이라이트
        setSelectedButtonHighlight(mainButton);

        return sidebarPanel;
    }

    // Add sidebar button
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
        return button;  // 생성된 버튼을 반환합니다.
    }

    // Add logout button
    private void addLogoutButton(JPanel sidebarPanel) {
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        logoutButton.setBackground(COLOR_DEFAULT_BUTTON);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
            frame.dispose();  // Close the current window
            DB2024Team13_loginWindow.displayLoginWindow();  // Display the login window
        });
        sidebarPanel.add(logoutButton);
    }

    // Highlight selected button
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

    // Return main content panel
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Display detailed information
    public void displayDetail(String restaurant) {
        JPanel detailPanel = DB2024Team13_detailWindow.createDetailPanel(restaurant);
        displayContent(detailPanel);
    }

    // Display content
    private void displayContent(Component component) {
        mainPanel.removeAll();
        mainPanel.add(component, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
