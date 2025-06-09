package ui;

import dto.Review;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class MyReviewsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private String user_id;
    private Dashboard dashboard;

    public MyReviewsPanel(String user_id, Dashboard dashboard) {
        this.user_id = user_id;
        this.dashboard = dashboard;
        setLayout(new BorderLayout());

        String[] columnNames = {"리뷰 ID", "도서 제목", "리뷰 내용", "평점", "작성일"};
        model = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // 모든 셀 수정 불가능하게 설정
        }
};
        table = new JTable(model);

        // 더블 클릭 시 ReviewDetailPanel로 이동
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();

                    int reviewId = (int) model.getValueAt(selectedRow, 0);
                    String bookTitle = (String) model.getValueAt(selectedRow, 1);
                    String content = (String) model.getValueAt(selectedRow, 2);
                    int rating = (int) model.getValueAt(selectedRow, 3);
                    String date = model.getValueAt(selectedRow, 4).toString();

                    Review review = new Review(reviewId, -1, user_id, content, rating, date);
                    dashboard.showReviewDetailPanel(review, bookTitle);
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 하단 '돌아가기' 버튼만 남김
        JPanel buttonPanel = new JPanel();
        JButton backBtn = new JButton("돌아가기");
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> dashboard.showMainMenu());

        loadReviews();
    }

    private void loadReviews() {
        model.setRowCount(0); // 기존 행 초기화
        try (Connection conn = db.DBUtil.getConnection()) {
            String sql = "SELECT r.review_id, b.title, r.content, r.rating, r.review_date " +
                         "FROM ReviewDetailView r " +
                         "JOIN Book b ON r.book_id = b.book_id " +
                         "WHERE r.user_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("review_id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("content"));
                row.add(rs.getInt("rating"));
                row.add(rs.getDate("review_date"));
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "리뷰 불러오기 실패: " + e.getMessage());
        }
    }
}


