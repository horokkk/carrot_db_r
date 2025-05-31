package ui;

import dao.BookDAO;
import dao.ReviewDAO;
import dto.Book;
import dto.Review;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookSearchPanel extends JPanel {
    private JTextField searchField;
    private JTable bookTable;
    private JTable reviewTable;
    private DefaultTableModel bookModel;
    private DefaultTableModel reviewModel;
    private JButton reviewButton;
    private String userId;

    public BookSearchPanel(String userId) {
        this.userId = userId;
        setLayout(new BorderLayout());

        // Search Bar
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(30);
        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(e -> searchBooks());
        searchPanel.add(new JLabel("도서명 또는 장르명: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Book Table
        bookModel = new DefaultTableModel(new String[]{"ID", "도서명", "장르", "저자"}, 0);
        bookTable = new JTable(bookModel);
        JScrollPane bookScroll = new JScrollPane(bookTable);

        // Review Table
        reviewModel = new DefaultTableModel(new String[]{"작성자", "내용", "평점", "작성일"}, 0);
        reviewTable = new JTable(reviewModel);
        JScrollPane reviewScroll = new JScrollPane(reviewTable);

        // Review Button
        reviewButton = new JButton("리뷰 작성하기");
        reviewButton.addActionListener(e -> openReviewDialog());

        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, bookScroll, reviewScroll);
        splitPane.setDividerLocation(200);

        add(searchPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(reviewButton, BorderLayout.SOUTH);

        // Table Event
        bookTable.getSelectionModel().addListSelectionListener(e -> loadReviews());
    }

    private void searchBooks() {
        String keyword = searchField.getText();
        List<Book> books = new BookDAO().searchBooks(keyword);
        bookModel.setRowCount(0);
        for (Book b : books) {
            bookModel.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getGenreName(), b.getAuthorName()});
        }
    }

    private void loadReviews() {
        int row = bookTable.getSelectedRow();
        if (row == -1) return;

        int bookId = (int) bookModel.getValueAt(row, 0);
        List<Review> reviews = new ReviewDAO().getReviewsByBook(bookId);

        reviewModel.setRowCount(0);
        for (Review r : reviews) {
            reviewModel.addRow(new Object[]{r.getUserId(), r.getContent(), r.getRating(), r.getDate()});
        }
    }

    private void openReviewDialog() {
        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "리뷰를 작성할 도서를 선택하세요.");
            return;
        }

        int bookId = (int) bookModel.getValueAt(row, 0);
        String content = JOptionPane.showInputDialog(this, "리뷰 내용 입력:");
        if (content == null || content.length() < 10 || content.length() > 1000) {
            JOptionPane.showMessageDialog(this, "리뷰는 10자 이상 1000자 이하여야 합니다.");
            return;
        }
        String ratingStr = JOptionPane.showInputDialog(this, "평점 (1~5):");
        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "1~5 사이의 숫자를 입력하세요.");
            return;
        }

        boolean success = new ReviewDAO().insertReview(bookId, userId, content, rating);
        if (success) {
            JOptionPane.showMessageDialog(this, "리뷰 등록 성공!");
            loadReviews();
        } else {
            JOptionPane.showMessageDialog(this, "리뷰 등록 실패.");
        }
    }
}

