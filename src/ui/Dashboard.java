package ui;

import javax.swing.*;

import dto.Review;
import dao.BookDAO;
import dao.StatsDAO;
import dto.Book;
import dto.BookReviewStat;
import dto.PopularBook;
import dto.AgeGenreStat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.BookSearchPanel;
import ui.MyReviewsPanel;
import ui.BookReviewStatsPanel;
import ui.PopularBooksPanel;
import ui.AgeGenreStatsPanel;



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
        menuPanel.setLayout(new GridLayout(5, 1));
        JButton myReviewsBtn = new JButton("나의 리뷰");
        JButton searchBooksBtn = new JButton("도서 검색");
        JButton popularBtn = new JButton("인기 도서 랭킹");    // 신규
        JButton statsBtn = new JButton("리뷰 통계");           // 신규
        JButton ageGenreBtn = new JButton("연령별 장르 선호"); // 신규
        
        menuPanel.add(myReviewsBtn);
        menuPanel.add(searchBooksBtn);
        menuPanel.add(statsBtn);   // 신규
        menuPanel.add(popularBtn);   // 신규
        menuPanel.add(ageGenreBtn);   // 신규
        getContentPane().add(menuPanel, BorderLayout.WEST);

        // 메인 카드 패널 (화면 전환)
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(new JLabel("왼쪽에서 메뉴를 선택하세요."), "default");
        mainPanel.add(new MyReviewsPanel(user_id, this), "myReviews");
        mainPanel.add(new BookSearchPanel(user_id, this), "searchBooks");
        
        // 신규 기능 패널 등록
        mainPanel.add(new BookReviewStatsPanel(),      "stats");   // 신규
        mainPanel.add(new PopularBooksPanel(),         "popular");   // 신규
        mainPanel.add(new AgeGenreStatsPanel(),        "ageGenre");   // 신규
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // 버튼 이벤트
        myReviewsBtn.addActionListener(e -> cardLayout.show(mainPanel, "myReviews"));
        searchBooksBtn.addActionListener(e -> cardLayout.show(mainPanel, "searchBooks"));        
        statsBtn.addActionListener(e -> cardLayout.show(mainPanel, "stats"));   // 신규
        popularBtn.addActionListener(e -> cardLayout.show(mainPanel, "popular"));   // 신규
        ageGenreBtn.addActionListener(e -> cardLayout.show(mainPanel, "ageGenre"));   // 신규

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
    
    // 리뷰 작성 화면 보기
    public void showReviewWritePanel(int bookId, String bookTitle) {
        String key = "writeReview_" + bookId;
        boolean exists = false;
        for (Component comp : mainPanel.getComponents()) {
            if (key.equals(comp.getName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            ReviewWritePanel panel = new ReviewWritePanel(this, bookId, user_id, bookTitle);
            panel.setName(key);
            mainPanel.add(panel, key);
        }
        cardLayout.show(mainPanel, key);
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

