package db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DB2024Team13_sidebarButtonCreator {

    /**
     * 사이드바에 버튼을 추가하는 메소드
     *
     * @param sidebarPanel 버튼을 추가할 사이드바 패널
     * @param text 버튼에 표시될 텍스트
     * @param actionListener 버튼 클릭 시 수행할 액션 리스너
     */
    public static void addButtonToSidebar(JPanel sidebarPanel, String text, ActionListener actionListener) {
        JButton sidebarButton = new JButton(text);
        
        // 버튼의 정렬 및 스타일 설정
        sidebarButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarButton.setBackground(Color.LIGHT_GRAY);
        sidebarButton.setOpaque(true);
        sidebarButton.setBorderPainted(false);
        sidebarButton.addActionListener(actionListener);
        sidebarButton.setMaximumSize(new Dimension(200, 40));

        // 사이드바 패널에 버튼 추가
        sidebarPanel.add(sidebarButton);
    }
}
