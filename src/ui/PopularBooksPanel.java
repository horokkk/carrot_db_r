package ui;

import dao.StatsDAO;
import dto.PopularBook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PopularBooksPanel extends JPanel {
    public PopularBooksPanel() {
        setLayout(new BorderLayout());
        String[] cols = {"도서ID", "도서명", "평균평점", "순위"};
        
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
        List<PopularBook> list = new StatsDAO().getPopularBooks();
        for (PopularBook p : list) {
            model.addRow(new Object[]{
                p.getBookId(), p.getBookTitle(), p.getAvgRating(), p.getRank()
            });
        }
    }
}
