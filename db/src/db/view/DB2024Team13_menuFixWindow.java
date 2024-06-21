package db.view;

import db.model.DB2024Team13_menuManager;

import javax.swing.*;

import java.awt.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 메뉴 수정 창을 관리하는 클래스입니다.
 */
public class DB2024Team13_menuFixWindow {
	
	/**
     * 메뉴 수정 창을 표시하는 메소드입니다.
     *
     * @param restaurant 레스토랑 이름
     * @param mainDetailPanel 메인 상세 정보 패널
     */
	public static void showMenuFixWindow(String restaurant, JPanel mainDetailPanel) {
        //프레임 설정
    	JFrame frame = new JFrame("관리자 - 메뉴 수정");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        
        
        //제목 라벨 추가
        JLabel titleLabel = new JLabel("메뉴 수정", SwingConstants.CENTER);
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        //메뉴 패널, 스크롤 추가
        JPanel menuPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        //메뉴 추가 패널 추가
        JPanel addMenuPanel = createAddMenuPanel(restaurant, menuPanel, mainDetailPanel);
        panel.add(addMenuPanel, BorderLayout.SOUTH);
        
        //기존 메뉴 불러오기
        loadMenuItems(restaurant, menuPanel, mainDetailPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
	/**
     * 메뉴 추가 패널을 생성하는 메소드입니다.
     *
     * @param restaurant 레스토랑 이름
     * @param menuPanel 메뉴 패널
     * @param mainDetailPanel 메인 상세 정보 패널
     * @return 생성된 JPanel 객체
     */
    private static JPanel createAddMenuPanel(String restaurant, JPanel menuPanel, JPanel mainDetailPanel) {
        JPanel addMenuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel addMenuLabel = new JLabel("메뉴 추가", SwingConstants.CENTER);
        addMenuLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        addMenuPanel.add(addMenuLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        //메뉴 이름, 가격, 비건 여부 필드 및 체크박스 추가
        JTextField menuNameField = addLabeledField(addMenuPanel, "메뉴 이름", new JTextField(10), gbc, 1);

        JTextField menuPriceField = addLabeledField(addMenuPanel, "가격", new JTextField(10), gbc, 2);

        JCheckBox menuVeganCheckBox = addLabeledField(addMenuPanel, "비건 여부", new JCheckBox(), gbc, 3);
        
        //메뉴 추가 버튼 추가
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addMenuButton = new JButton("메뉴 추가");
        addMenuPanel.add(addMenuButton, gbc);
        
        //메뉴 추가 버튼에 대한 액션
        addMenuButton.addActionListener(e -> {
            String menuName = menuNameField.getText();
            if (menuName.isEmpty()) {
                JOptionPane.showMessageDialog(addMenuPanel, "메뉴 이름을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int menuPrice = Integer.parseInt(menuPriceField.getText());
                boolean isVegan = menuVeganCheckBox.isSelected();
                DB2024Team13_menuManager.addMenuItem(restaurant, menuName, menuPrice, isVegan);
                JOptionPane.showMessageDialog(addMenuPanel, "메뉴가 추가되었습니다.");
                loadMenuItems(restaurant, menuPanel, mainDetailPanel);
                DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurant);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addMenuPanel, "가격은 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        return addMenuPanel;
    }
    
    /**
     * 라벨 및 필드를 추가하는 메소드입니다.
     *
     * @param panel 패널
     * @param labelText 라벨 텍스트
     * @param field 필드
     * @param gbc GridBagConstraints 객체
     * @param y y 좌표
     * @return 추가된 필드
     */
    private static <T extends JComponent> T addLabeledField(JPanel panel, String labelText, T field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
        
        return field;
    }
    
    /**
     * 메뉴 항목을 불러와 패널에 추가하는 메소드입니다.
     *
     * @param restaurantName 레스토랑 이름
     * @param menuPanel 메뉴 패널
     * @param mainDetailPanel 메인 상세 정보 패널
     */
    private static void loadMenuItems(String restaurantName, JPanel menuPanel, JPanel mainDetailPanel) {
        menuPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        try (ResultSet rs = DB2024Team13_menuManager.loadMenuItems(restaurantName)) {
            while (rs.next()) {
                String menuName = rs.getString("menu_name");
                int price = rs.getInt("price");
                boolean isVegan = rs.getBoolean("vegan");
                JPanel menuItemPanel = createMenuItemPanel(restaurantName, menuName, price, isVegan, menuPanel, mainDetailPanel);
                gbc.gridy++;
                menuPanel.add(menuItemPanel, gbc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    /**
     * 개별 메뉴 항목 패널을 생성하는 메소드입니다.
     *
     * @param restaurantName 레스토랑 이름
     * @param menuName 메뉴 이름
     * @param price 가격
     * @param isVegan 비건 여부
     * @param menuPanel 메뉴 패널
     * @param mainDetailPanel 메인 상세 정보 패널
     * @return 생성된 JPanel 객체
     */
    private static JPanel createMenuItemPanel(String restaurantName, String menuName, int price, boolean isVegan, JPanel menuPanel, JPanel mainDetailPanel) {
        JPanel menuItemPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel menuNameLabel = new JLabel(menuName);
        JTextField priceField = new JTextField(String.valueOf(price), 10);
        JCheckBox veganCheckBox = new JCheckBox("비건 여부");
        veganCheckBox.setSelected(isVegan);
        JButton updateButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");

        gbc.gridx = 0;
        menuItemPanel.add(menuNameLabel, gbc);
        gbc.gridx = 1;
        menuItemPanel.add(priceField, gbc);
        gbc.gridx = 2;
        menuItemPanel.add(veganCheckBox, gbc);
        gbc.gridx = 3;
        menuItemPanel.add(updateButton, gbc);
        gbc.gridx = 4;
        menuItemPanel.add(deleteButton, gbc);
        
        //수정 버튼 클릭 시 이벤트
        updateButton.addActionListener(e -> {
            try {
                int newPrice = Integer.parseInt(priceField.getText());
                boolean newIsVegan = veganCheckBox.isSelected();
                DB2024Team13_menuManager.updateMenuItem(restaurantName, menuName, newPrice, newIsVegan);
                JOptionPane.showMessageDialog(menuPanel, "메뉴가 수정되었습니다.");
                loadMenuItems(restaurantName, menuPanel, mainDetailPanel);
                DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurantName);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(menuPanel, "가격은 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        //삭제 버튼 클릭 시 이벤트
        deleteButton.addActionListener(e -> {
            try {
            	DB2024Team13_menuManager.deleteMenuItem(restaurantName, menuName);
                JOptionPane.showMessageDialog(menuPanel, "메뉴가 삭제되었습니다.");
                loadMenuItems(restaurantName, menuPanel, mainDetailPanel);
                DB2024Team13_detailWindow.refreshDetailPanel(mainDetailPanel, restaurantName);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(menuPanel, "메뉴 삭제 중 오류가 발생했습니다.", "삭제 오류", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        return menuItemPanel;
    }
}
