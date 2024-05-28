package db.view;

import javax.swing.*;

import db.session.DB2024Team13_userSession;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DB2024Team13_myinfoWindow {

    // '내 정보' 패널 생성
    public static JPanel createMyInfoPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel myInfoPanel = new JPanel(new BorderLayout());
        myInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 사용자 정보 표시 패널 생성
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userInfoLabel = new JLabel("안녕하세요, " + DB2024Team13_userSession.getInstance().getNickname() + "님!");
        userInfoPanel.add(userInfoLabel);

        myInfoPanel.add(userInfoPanel, BorderLayout.NORTH);

        // 상단에 라디오 버튼 옵션 추가
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton bookmarkOption = new JRadioButton("북마크 목록");
        JRadioButton reviewOption = new JRadioButton("리뷰 조회");
        JRadioButton orderOption = new JRadioButton("주문 조회");

        optionsPanel.add(bookmarkOption);
        optionsPanel.add(reviewOption);
        optionsPanel.add(orderOption);

        ButtonGroup optionGroup = new ButtonGroup();
        optionGroup.add(bookmarkOption);
        optionGroup.add(reviewOption);
        optionGroup.add(orderOption);

        // 세 개의 독립적인 DefaultListModel과 JList 인스턴스 생성
        DefaultListModel<String> bookmarkListModel = new DefaultListModel<>();
        DefaultListModel<String> reviewListModel = new DefaultListModel<>();
        DefaultListModel<String> orderListModel = new DefaultListModel<>();

        JList<String> bookmarkList = new JList<>(bookmarkListModel);
        JList<String> reviewList = new JList<>(reviewListModel);
        JList<String> orderList = new JList<>(orderListModel);

        // CardLayout을 사용하여 리스트를 전환
        JPanel listContainerPanel = new JPanel(new CardLayout());
        listContainerPanel.add(new JScrollPane(bookmarkList), "북마크 목록");
        listContainerPanel.add(new JScrollPane(reviewList), "리뷰 조회");
        listContainerPanel.add(new JScrollPane(orderList), "주문 조회");

        // 옵션 패널과 리스트 컨테이너 패널을 하나의 패널에 넣고 수직으로 배치
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(optionsPanel);
        centerPanel.add(listContainerPanel);

        myInfoPanel.add(centerPanel, BorderLayout.CENTER);

        // 예시 데이터 추가 (추후 데이터베이스 연결 부분)
        addSampleData(bookmarkListModel, reviewListModel, orderListModel);

        // 각 리스트에 마우스 리스너 추가
        addMouseListenerToList(bookmarkList, mainWindow);
        addMouseListenerToList(reviewList, mainWindow);
        addMouseListenerToList(orderList, mainWindow);

        // 옵션 선택에 따라 다른 리스트를 보여주도록 리스너 추가
        bookmarkOption.addActionListener(e -> showCard(listContainerPanel, "북마크 목록"));
        reviewOption.addActionListener(e -> showCard(listContainerPanel, "리뷰 조회"));
        orderOption.addActionListener(e -> showCard(listContainerPanel, "주문 조회"));

        // 기본적으로 첫 번째 옵션을 선택된 상태로 설정
        bookmarkOption.setSelected(true);
        showCard(listContainerPanel, "북마크 목록");

        return myInfoPanel;
    }

    // 예시 데이터 추가 메소드
    private static void addSampleData(DefaultListModel<String> bookmarkListModel, DefaultListModel<String> reviewListModel, DefaultListModel<String> orderListModel) {
        bookmarkListModel.addElement("Restaurant A - 북마크");
        bookmarkListModel.addElement("Restaurant D - 북마크");
        bookmarkListModel.addElement("Restaurant G - 북마크");

        reviewListModel.addElement("Restaurant B - 리뷰");
        reviewListModel.addElement("Restaurant E - 리뷰");
        reviewListModel.addElement("Restaurant H - 리뷰");

        orderListModel.addElement("Restaurant C - 주문");
        orderListModel.addElement("Restaurant F - 주문");
        orderListModel.addElement("Restaurant I - 주문");
    }

    // JList에 마우스 리스너 추가
    private static void addMouseListenerToList(JList<String> list, DB2024Team13_mainWindow mainWindow) {
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 더블 클릭 감지
                    String selectedRestaurant = list.getSelectedValue();
                    if (selectedRestaurant != null) {
                        mainWindow.displayDetail(selectedRestaurant);
                    }
                }
            }
        });
    }

    // CardLayout을 사용하여 카드 보여주기
    private static void showCard(JPanel panel, String card) {
        CardLayout cardLayout = (CardLayout) (panel.getLayout());
        cardLayout.show(panel, card);
    }
}
