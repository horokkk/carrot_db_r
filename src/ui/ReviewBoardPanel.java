package ui;

import dao.ReviewDAO;
import dto.Review;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ReviewBoardPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private String bookTitle;
    private int bookId;
    private String userId;
    private Dashboard dashboard;

    public ReviewBoardPanel(Dashboard dashboard, int bookId, String bookTitle, String userId) {
        this.dashboard = dashboard;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.userId = userId;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("📚 " + bookTitle + " 리뷰 목록", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        String[] cols = {"리뷰 ID", "작성자", "내용", "평점", "작성일"};
        model = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER); //수정

        // 🔽 하단 버튼 패널
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton writeReviewButton = new JButton("✏ 리뷰 작성하기");
        JButton backButton = new JButton("← 도서 검색으로 돌아가기");

        writeReviewButton.addActionListener(e -> {
            dashboard.showReviewWritePanel(bookId, bookTitle);
        });


        backButton.addActionListener(e -> dashboard.showSearchBooksPanel());

        btnPanel.add(writeReviewButton);
        btnPanel.add(backButton);
        add(btnPanel, BorderLayout.SOUTH);

        // 리뷰 더블 클릭 이벤트
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int reviewId = (int) model.getValueAt(row, 0);
                    String reviewerId = (String) model.getValueAt(row, 1);
                    String content = (String) model.getValueAt(row, 2);
                    int rating = (int) model.getValueAt(row, 3);
                    String date = model.getValueAt(row, 4).toString();

                    Review review = new Review(reviewId, bookId, reviewerId, content, rating, date);
                    dashboard.showReviewDetailPanel(review, bookTitle);
                }
            }
        });

        loadReviews(); // 초기 리뷰 목록 불러오기
    }

    private void loadReviews() {
        model.setRowCount(0);
        List<Review> reviews = new ReviewDAO().getReviewsByBook(bookId);
        for (Review r : reviews) {
            model.addRow(new Object[]{
                r.getReviewId(),
                r.getUserId(),
                r.getContent(),
                r.getRating(),
                r.getDate()
            });
        }
    }
}
