package ui;

import javax.swing.*;

import dto.Review;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.BookSearchPanel;
import ui.MyReviewsPanel;


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
        mainPanel.add(new MyReviewsPanel(user_id, this), "myReviews");
        mainPanel.add(new BookSearchPanel(user_id, this), "searchBooks");
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // 버튼 이벤트
        myReviewsBtn.addActionListener(e -> cardLayout.show(mainPanel, "myReviews"));
        searchBooksBtn.addActionListener(e -> cardLayout.show(mainPanel, "searchBooks"));

        setVisible(true);
    }

    public void showReviewDetailPanel(Review review) {
    String key = "reviewDetail_" + review.getReviewId();
    boolean exists = false;
    for (Component comp : mainPanel.getComponents()) {
        if (key.equals(mainPanel.getLayout().toString())) {
            exists = true;
            break;
        }
    }
    if (!exists) {
        ReviewDetailPanel detailPanel = new ReviewDetailPanel(this, review);
        mainPanel.add(detailPanel, key);
    }
    cardLayout.show(mainPanel, key);
}


    public void showReviewBoard(int bookId, String bookTitle) {
        ReviewBoardPanel reviewBoardPanel = new ReviewBoardPanel(this, bookId, bookTitle, user_id);
        mainPanel.add(reviewBoardPanel, "reviewBoard_" + bookId);  // 각 도서별 고유 키로 등록
        cardLayout.show(mainPanel, "reviewBoard_" + bookId);
    }

    public void showSearchBooksPanel() {
        cardLayout.show(mainPanel, "searchBooks");
    }

    public void showMainMenu() {
        cardLayout.show(mainPanel, "default");
    }

    public void showMyReviewsPanel() {
        cardLayout.show(mainPanel, "myReviews");
    }


    // 테스트용 main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("test_user"));
    }

}

