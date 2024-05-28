package db.view;

import javax.swing.*;
import java.awt.*;

public class DB2024Team13_detailWindow {

    /**
     * 특정 레스토랑의 상세 정보를 표시하는 패널을 생성하는 메소드
     *
     * @param restaurant 레스토랑 이름
     * @return 상세 정보 패널
     */
    public static JPanel createDetailPanel(String restaurant) {
        // 메인 상세 정보 패널 생성
        JPanel mainDetailPanel = new JPanel(new BorderLayout());
        mainDetailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 레스토랑 이름을 표시하는 제목 레이블 생성
        JLabel restaurantTitleLabel = new JLabel(restaurant, SwingConstants.CENTER);
        restaurantTitleLabel.setFont(new Font("나눔고딕", Font.BOLD, 24));
        mainDetailPanel.add(restaurantTitleLabel, BorderLayout.NORTH);

        // 상세 내용을 표시할 패널 생성
        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(Color.WHITE);

        // 상세 정보 레이블 추가
        detailsContentPanel.add(createDetailLabel("별점: "));
        detailsContentPanel.add(createDetailLabel("대표메뉴: "));
        detailsContentPanel.add(createDetailLabel("위치: "));
        detailsContentPanel.add(createDetailLabel("비건메뉴 여부: "));
        detailsContentPanel.add(createDetailLabel("혼밥 가능 여부: "));
        detailsContentPanel.add(createDetailLabel("브레이크타임 유무: "));

        mainDetailPanel.add(detailsContentPanel, BorderLayout.CENTER);

        return mainDetailPanel;
    }

    /**
     * 상세 정보를 표시하는 레이블을 생성하는 메소드
     *
     * @param text 레이블에 표시할 텍스트
     * @return 생성된 레이블
     */
    private static JLabel createDetailLabel(String text) {
        JLabel detailLabel = new JLabel(text);
        detailLabel.setFont(new Font("나눔고딕", Font.BOLD, 15));
        return detailLabel;
    }
}
