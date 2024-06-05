package db.view;

import db.model.DB2024Team13_restaurantManager;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;

/**
 * 관리자 - 레스토랑 정보 관리를 위한 창을 표시하는 클래스입니다.
 */
public class DB2024Team13_adminWindow {

	/**
     * 관리자 창을 표시하는 메소드입니다.
     *
     * @param restaurant      레스토랑 이름
     * @param mainDetailPanel 메인 상세 정보 패널
     */
    public static void showAdminWindow(String restaurant, JPanel mainDetailPanel) {
        JFrame frame = new JFrame("관리자 - 레스토랑 정보 관리");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        frame.add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간격 설정
        gbc.anchor = GridBagConstraints.WEST; // 기본적으로 왼쪽 정렬

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬
        JLabel titleLabel = new JLabel("레스토랑 정보 관리", SwingConstants.CENTER);
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; // 왼쪽 정렬로 되돌림
        panel.add(Box.createVerticalStrut(10), gbc); // 간격 추가

        gbc.gridy++;
        JLabel categoryLabel = new JLabel("카테고리:");
        String[] categories = {"한식", "중식", "일식", "양식", "분식", "아시안", "패스트푸드", "카페"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        panel.add(categoryLabel, gbc);

        gbc.gridx++;
        panel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel bestMenuLabel = new JLabel("대표메뉴:");
        JTextField bestMenuField = new JTextField(20);
        panel.add(bestMenuLabel, gbc);

        gbc.gridx++;
        panel.add(bestMenuField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel locationLabel = new JLabel("위치:");
        JTextField locationField = new JTextField(20);
        panel.add(locationLabel, gbc);

        gbc.gridx++;
        panel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel sectionLabel = new JLabel("구역 이름:");
        JComboBox<String> sectionComboBox = new JComboBox<>(new String[]{"정문", "후문", "공대쪽문", "북아현"});
        panel.add(sectionLabel, gbc);

        gbc.gridx++;
        panel.add(sectionComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        //panel.add(Box.createVerticalStrut(10), gbc); // 간격 추가

        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel breaktimeLabel = new JLabel("브레이크타임 유무:");
        JCheckBox breaktimeCheckBox = new JCheckBox();
        checkBoxPanel.add(breaktimeLabel);
        checkBoxPanel.add(breaktimeCheckBox);

        JLabel eatAloneLabel = new JLabel("혼밥 가능 여부:");
        JCheckBox eatAloneCheckBox = new JCheckBox();
        checkBoxPanel.add(eatAloneLabel);
        checkBoxPanel.add(eatAloneCheckBox);

        panel.add(checkBoxPanel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(Box.createVerticalStrut(10), gbc); // 간격 추가

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST; // 버튼은 오른쪽 정렬
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("가게삭제");
        JButton saveButton = new JButton("변경저장");
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, gbc);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "정말로 이 레스토랑을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                	DB2024Team13_restaurantManager.deleteRestaurant(restaurant);
                    JOptionPane.showMessageDialog(null, "레스토랑이 삭제되었습니다.");
                    // 상세 정보 패널을 초기화하거나 다른 화면으로 전환하는 로직 추가 가능
                    frame.dispose();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	DB2024Team13_restaurantManager.updateRestaurantDetails(restaurant, (String) categoryComboBox.getSelectedItem(), bestMenuField.getText(), (String) sectionComboBox.getSelectedItem(), locationField.getText(), breaktimeCheckBox.isSelected(), eatAloneCheckBox.isSelected());
                DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurant);
                frame.dispose();
            }
        });

        frame.setLocationRelativeTo(null); // 창을 화면 중앙에 위치
        frame.setVisible(true);

        // 레스토랑 세부 정보 불러오기
        Map<String, Object> details = DB2024Team13_restaurantManager.loadRestaurantDetails(restaurant);
        categoryComboBox.setSelectedItem(details.getOrDefault("category", ""));
        bestMenuField.setText((String) details.getOrDefault("bestMenu", ""));
        locationField.setText((String) details.getOrDefault("location", ""));
        sectionComboBox.setSelectedItem(details.getOrDefault("section", ""));
        breaktimeCheckBox.setSelected((Boolean) details.getOrDefault("breaktime", false));
        eatAloneCheckBox.setSelected((Boolean) details.getOrDefault("eatAlone", false));
    }
}
