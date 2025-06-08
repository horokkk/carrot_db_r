package ui;

import dao.BookDAO;
import dto.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GenreClassificationPanel extends JPanel {
    public GenreClassificationPanel() {
        setLayout(new BorderLayout());

        // 상단 입력창
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("장르명:"));
        JTextField genreField = new JTextField(10);
        JButton searchBtn = new JButton("조회");
        top.add(genreField);
        top.add(searchBtn);
        add(top, BorderLayout.NORTH);

        // 테이블
        String[] cols = {"도서ID", "제목", "장르", "저자"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 버튼 클릭 시 조회
        searchBtn.addActionListener(e -> {
            model.setRowCount(0);
            String genre = genreField.getText().trim();
            List<Book> books = new BookDAO().findByGenre(genre);
            for (Book b : books) {
                model.addRow(new Object[]{
                    b.getBookId(), b.getTitle(),
                    b.getGenreName(), b.getAuthorName()
                });
            }
        });
    }
}
