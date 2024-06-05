package db.view;

import db.model.DB2024Team13_userInfoManager;
import db.model.DB2024Team13_userSessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * '내 정보' 패널을 생성하는 클래스입니다.
 */
public class DB2024Team13_myInfoWindow {

	/**
     * '내 정보' 패널을 생성하는 메소드입니다.
     *
     * @param mainWindow 메인 윈도우 객체
     * @return 생성된 JPanel 객체
     */
    public static JPanel createMyInfoPanel(DB2024Team13_mainWindow mainWindow) {
        JPanel myInfoPanel = new JPanel(new BorderLayout());
        myInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 사용자 정보 표시 패널 생성
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userInfoLabel = new JLabel("안녕하세요, " + DB2024Team13_userSessionManager.getInstance().getNickname() + "님!");
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

        // 북마크 목록과 리뷰 목록을 위한 DefaultListModel 생성
        DefaultListModel<String> bookmarkListModel = new DefaultListModel<>();
        DefaultListModel<String> reviewListModel = new DefaultListModel<>();
        
        // 주문 목록을 위한 DefaultTableModel 생성
        DefaultTableModel orderTableModel = new DefaultTableModel(new String[]{"주문 일시", "식당", "메뉴"}, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // 셀 수정 불가
            }
        };
        JTable orderTable = new JTable(orderTableModel);

        orderTable.setRowHeight(orderTable.getRowHeight() + 10);
        orderTable.setBackground(Color.WHITE); 
        orderTable.setShowGrid(false); 

        // 정렬 기능 추가
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(orderTableModel);
        orderTable.setRowSorter(sorter);

        // 테이블의 부모 패널의 배경을 흰색으로 설정
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.getViewport().setBackground(Color.WHITE);

        JList<String> bookmarkList = new JList<>(bookmarkListModel);
        JList<String> reviewList = new JList<>(reviewListModel);

        // CardLayout을 사용하여 리스트와 테이블을 전환
        JPanel listContainerPanel = new JPanel(new CardLayout());
        listContainerPanel.add(new JScrollPane(bookmarkList), "북마크 목록");
        listContainerPanel.add(new JScrollPane(reviewList), "리뷰 조회");
        listContainerPanel.add(orderScrollPane, "주문 조회");

        // 옵션 패널과 리스트 컨테이너 패널을 하나의 패널에 넣고 수직으로 배치
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(optionsPanel);
        centerPanel.add(listContainerPanel);

        myInfoPanel.add(centerPanel, BorderLayout.CENTER);
        
        // 데이터베이스에서 각 옵션에 해당하는 데이터를 불러와서 모델에 추가
        DB2024Team13_userInfoManager.filterRestaurant("SELECT rest_name FROM DB2024_bookmark WHERE student_id = ?", bookmarkListModel);
        DB2024Team13_userInfoManager.filterRestaurant("SELECT rest_name, star FROM DB2024_review WHERE student_id = ?", reviewListModel);
        DB2024Team13_userInfoManager.searchOrder(orderTableModel);

        // 북마크 목록에서 더블 클릭 이벤트 핸들러 추가
        bookmarkList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedRestaurant = bookmarkList.getSelectedValue();
                    if (selectedRestaurant != null) {
                        mainWindow.displayDetail(selectedRestaurant);
                    }
                }
            }
        });
        
        // 옵션 선택에 따라 다른 리스트를 보여주도록 리스너 추가
        bookmarkOption.addActionListener(e -> showCard(listContainerPanel, "북마크 목록"));
        reviewOption.addActionListener(e -> showCard(listContainerPanel, "리뷰 조회"));
        orderOption.addActionListener(e -> showCard(listContainerPanel, "주문 조회"));

        // 기본적으로 첫 번째 옵션을 선택된 상태로 설정
        bookmarkOption.setSelected(true);
        showCard(listContainerPanel, "북마크 목록");

        return myInfoPanel;
    }

    /**
     * CardLayout을 사용하여 카드 보여주기
     *
     * @param panel 카드 레이아웃을 사용하는 패널
     * @param card 보여줄 카드 이름
     */
    private static void showCard(JPanel panel, String card) {
        CardLayout cardLayout = (CardLayout) (panel.getLayout());
        cardLayout.show(panel, card);
    }
}
