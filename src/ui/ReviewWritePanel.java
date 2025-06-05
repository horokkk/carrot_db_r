package ui;

import dao.ReviewDAO;
import dto.Review;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ReviewWritePanel extends JPanel {
    private Dashboard dashboard;
    private int bookId;
    private String userId;
    private String bookTitle;

    private JTextArea contentArea;
    private JTextField ratingField;

    public ReviewWritePanel(Dashboard dashboard, int bookId, String userId, String bookTitle) {
        this.dashboard = dashboard;
        this.bookId = bookId;
        this.userId = userId;
        this.bookTitle = bookTitle;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("✏ " + bookTitle + " 리뷰 작성", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        formPanel.add(new JLabel("내용 (10자 이상):"));
        contentArea = new JTextArea(5, 20);
        formPanel.add(new JScrollPane(contentArea));

        formPanel.add(new JLabel("평점 (1~5):"));
        ratingField = new JTextField();
        formPanel.add(ratingField);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton submitBtn = new JButton("작성");
        JButton cancelBtn = new JButton("취소");

        submitBtn.addActionListener(e -> handleSubmit());
        cancelBtn.addActionListener(e -> dashboard.showReviewBoard(bookId, bookTitle));

        btnPanel.add(submitBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void handleSubmit() {
        System.out.println("handleSubmit() 호출됨");
        String content = contentArea.getText().trim();
        int rating;

        if (content.length() < 10) {
            JOptionPane.showMessageDialog(this, "리뷰는 10자 이상이어야 합니다.");
            return;
        }

        try {
            rating = Integer.parseInt(ratingField.getText());
            if (rating < 1 || rating > 5) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "평점은 1~5 사이의 정수여야 합니다.");
            return;
        }

        boolean result = new ReviewDAO().writeReview(bookId, userId, content, rating, LocalDate.now().toString());
        if (result) {
            JOptionPane.showMessageDialog(this, "리뷰가 등록되었습니다!");
            dashboard.showReviewBoard(bookId, bookTitle);
        } else {
            JOptionPane.showMessageDialog(this, "리뷰 등록 실패.");
        }
    }
}

