package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class MyReviewsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private String userId;
    private Dashboard parent;

    public MyReviewsPanel(String userId, Dashboard parent) {
        this.userId = userId;
        this.parent = parent;
        setLayout(new BorderLayout());

        String[] columnNames = {"리뷰 ID", "도서 제목", "리뷰 내용", "평점", "작성일"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        
        // MyReviewsPanel.java 내부
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();
                    int reviewId = (int) model.getValueAt(selectedRow, 0);
                    Container parent = MyReviewsPanel.this.getParent();
                    while (!(parent instanceof JFrame) && parent != null) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof JFrame) {
                        JFrame frame = (JFrame) parent;
                        frame.setContentPane(new ReviewDetailPanel(userId, reviewId));
                        frame.revalidate();
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 하단 버튼 영역
        JPanel buttonPanel = new JPanel();
        JButton backBtn = new JButton("돌아가기");
        JButton editBtn = new JButton("수정");
        JButton deleteBtn = new JButton("삭제");

        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 액션
        backBtn.addActionListener(e -> parent.showMainMenu());

        editBtn.addActionListener(e -> handleEdit());
        deleteBtn.addActionListener(e -> handleDelete());

        loadReviews();
    }

    private void loadReviews() {
        model.setRowCount(0); // 기존 행 초기화
        try (Connection conn = db.DBUtil.getConnection()) {
            String sql = "SELECT r.review_id, b.title, r.content, r.rating, r.review_date " +
                         "FROM Review r JOIN Book b ON r.book_id = b.book_id " +
                         "JOIN Member m ON r.member_id = m.member_id " +
                         "WHERE m.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
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

    private void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 리뷰를 선택하세요.");
            return;
        }

        String password = JOptionPane.showInputDialog(this, "비밀번호를 입력하세요:");
        if (!verifyPassword(password)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.");
            return;
        }

        int reviewId = (int) model.getValueAt(selectedRow, 0);
        String currentContent = (String) model.getValueAt(selectedRow, 2);
        String newContent = JOptionPane.showInputDialog(this, "새 리뷰 내용을 입력하세요:", currentContent);

        if (newContent != null && !newContent.trim().isEmpty()) {
            try (Connection conn = db.DBUtil.getConnection()) {
                String sql = "UPDATE Review SET content = ? WHERE review_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newContent);
                stmt.setInt(2, reviewId);
                stmt.executeUpdate();
                loadReviews();
                JOptionPane.showMessageDialog(this, "리뷰 수정 완료!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "리뷰 수정 실패: " + e.getMessage());
            }
        }
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 리뷰를 선택하세요.");
            return;
        }

        String password = JOptionPane.showInputDialog(this, "비밀번호를 입력하세요:");
        if (!verifyPassword(password)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.");
            return;
        }

        int reviewId = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = db.DBUtil.getConnection()) {
                String sql = "DELETE FROM Review WHERE review_id = ?";
                PreparedStatement stmt = co.prepareStatement(sql);
                stmt.setInt(1, reviewId);
                stmt.executeUpdate();
                loadReviews();
                JOptionPane.showMessageDialog(this, "리뷰 삭제 완료!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "리뷰 삭제 실패: " + e.getMessage());
            }
        }
    }

    private boolean verifyPassword(String inputPw) {
        try (Connection conn = db.DBUtil.getConnection()) {
            String sql = "SELECT * FROM Member WHERE user_id = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, inputPw);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "비밀번호 검증 실패: " + e.getMessage());
            return false;
        }
    }

}

