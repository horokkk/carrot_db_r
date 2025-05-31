package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private String user_id;  // 로그인 시 전달받은 user_id
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public Dashboard(String user_id) {
        this.user_id = user_id;
        setTitle("대시보드 - 사용자: " + user_id);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 전체 레이아웃 구성
        getContentPane().setLayout(new BorderLayout());

        // 왼쪽 메뉴 패널
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 1));
        JButton myReviewsBtn = new JButton("나의 리뷰");
        JButton searchBooksBtn = new JButton("도서 검색");

        menuPanel.add(myReviewsBtn);
        menuPanel.add(searchBooksBtn);
        getContentPane().add(menuPanel, BorderLayout.WEST);

        // 메인 카드 패널 (화면 전환)
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(new JLabel("왼쪽에서 메뉴를 선택하세요."), "default");
        mainPanel.add(new MyReviewsPanel(user_id), "myReviews");
        mainPanel.add(new SearchBooksPanel(user_id), "searchBooks");
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // 버튼 이벤트
        myReviewsBtn.addActionListener(e -> cardLayout.show(mainPanel, "myReviews"));
        searchBooksBtn.addActionListener(e -> cardLayout.show(mainPanel, "searchBooks"));

        setVisible(true);
    }

    public void showReviewDetailPanel(Review review) {
    ReviewDetailPanel detailPanel = new ReviewDetailPanel(this, review);
    setContentPane(detailPanel);
    revalidate();
}

    // 테스트용 main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("test_user"));
    }
}

