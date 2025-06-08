package ui;

import dao.StatsDAO;
import dto.BookReviewStat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookReviewStatsPanel extends JPanel {
    public BookReviewStatsPanel() {
        setLayout(new BorderLayout());
        String[] cols = {"도서ID", "도서명", "평균평점", "리뷰수"};
        
        //셀 수정 방지지
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 데이터 로드
        List<BookReviewStat> stats = new StatsDAO().getBookReviewStats();
        for (BookReviewStat s : stats) {
            model.addRow(new Object[]{
                s.getBookId(), s.getBookTitle(), s.getAvgRating(), s.getReviewCount()
            });
        }
    }
}
