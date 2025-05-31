package ui;

import dao.ReviewDAO;
import dto.Review;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ReviewDetailPanel extends JPanel {
    private Dashboard dashboard;
    private Review review;

    private JTextArea contentArea;
    private JTextField ratingField;
    private JTextField dateField;
    private JPasswordField pwField;

    public ReviewDetailPanel(Dashboard dashboard, Review review) {
        this.dashboard = dashboard;
        this.review = review;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("리뷰 상세 보기", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5, 2));

        centerPanel.add(new JLabel("리뷰 ID: "));
        centerPanel.add(new JLabel(String.valueOf(review.getReviewId())));

        centerPanel.add(new JLabel("내용: "));
        contentArea = new JTextArea(review.getContent());
        centerPanel.add(new JScrollPane(contentArea));

        centerPanel.add(new JLabel("평점: "));
        ratingField = new JTextField(String.valueOf(review.getRating()));
        centerPanel.add(ratingField);

        centerPanel.add(new JLabel("작성일: "));
        dateField = new JTextField(String.valueOf(review.getDate()));
        dateField.setEditable(false);
        centerPanel.add(dateField);

        centerPanel.add(new JLabel("비밀번호 확인: "));
        pwField = new JPasswordField();
        centerPanel.add(pwField);

        add(centerPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton backBtn = new JButton("뒤로가기");
        JButton updateBtn = new JButton("수정");
        JButton deleteBtn = new JButton("삭제");

        backBtn.addActionListener(e -> dashboard.showMyReviewsPanel());
        updateBtn.addActionListener(this::handleUpdate);
        deleteBtn.addActionListener(this::handleDelete);

        btnPanel.add(backBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void handleUpdate(ActionEvent e) {
        String pw = new String(pwField.getPassword());
        if (!new ReviewDAO().verifyReviewOwner(review.getReviewId(), pw)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.");
            return;
        }

        String newContent = contentArea.getText();
        int newRating;
        try {
            newRating = Integer.parseInt(ratingField.getText());
            if (newRating < 1 || newRating > 5) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "1~5 사이의 평점을 입력해주세요.");
            return;
        }

        boolean result = new ReviewDAO().updateReview(review.getReviewId(), newContent, newRating);
        if (result) {
            JOptionPane.showMessageDialog(this, "리뷰가 수정되었습니다.");
            dashboard.showMyReviewsPanel();
        } else {
            JOptionPane.showMessageDialog(this, "수정 실패.");
        }
    }

    private void handleDelete(ActionEvent e) {
        String pw = new String(pwField.getPassword());
        if (!new ReviewDAO().verifyReviewOwner(review.getReviewId(), pw)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = new ReviewDAO().deleteReview(review.getReviewId());
            if (result) {
                JOptionPane.showMessageDialog(this, "삭제되었습니다.");
                dashboard.showMyReviewsPanel();
            } else {
                JOptionPane.showMessageDialog(this, "삭제 실패.");
            }
        }
    }
}

